#!/bin/bash
# The server suite's perf job (phase 2, P2-2): MSPT of vanilla and Quicksilver measured on ONE runner, so the two are
# compared in the same run on the same machine (CI's runners differ from each other by more than Quicksilver's savings).
# SUITE (core | siftec | ...): the scenes in ci/server/perf-<SUITE>.txt (none: nothing to do), each a scripted run on
# the suite's base world (server-base.tar), played sprinting (`tick sprint`) for its number of ticks, with the
# script's qs-at lines left out (Quicksilver's bench would cost its side time vanilla's doesn't pay).
#
# Rounds, alternating which side goes first so a runner that speeds up or slows down during the job doesn't favour
# either side: rounds 1-5 (vanilla first in odd rounds, Quicksilver first in even ones; MSPT: the median of the five),
# then round 6, Quicksilver then vanilla with Java Flight Recorder sampling the server thread every millisecond (where
# the time goes, by phase and method: ci/server/perf-jfr.py; not used for the MSPT medians).
# Every run loads the CI harness (fixed seeds, fixed generation order) and Java runs with -Dmax.bg.threads=1, as in the
# server suite. Scenes marked `world` keep their saved world: ci/server/compare-p2.py compares every Quicksilver round
# with vanilla's rounds (gated where vanilla's rounds agree, the server suite's rule).
# Output: $OUT/<scene>/<side><round>/ (ci-harness-result.json, the server log, jfr.json in round 6, world/).
set +e
root=$PWD
SUITE=${SUITE:-core}
OUT=${OUT:-/tmp/sout/$SUITE-perf}
mkdir -p "$OUT"
exec > >(tee "$OUT/perf.txt") 2>&1
spec=ci/server/perf-$SUITE.txt
if [ ! -f "$spec" ]; then echo "no $spec: no perf scenes for $SUITE"; exit 0; fi
[ -f server-base.tar ] || { echo "no base world (server-base.tar)"; exit 1; }
echo "perf suite=$SUITE"
srv=$root/srv
rm -rf "$srv"
bash ci/server/fetch-server.sh "$srv" || { echo "could not fetch the server"; exit 1; }
cp ci-server-harness/ci-server-harness.jar "$srv/mods/" || { echo "ci-server-harness.jar missing"; exit 1; }
mc=$(grep '^minecraft_version' gradle.properties | sed 's/.*= *//')
case "$SUITE" in
  siftec) bash ci/server/siftec-mods.sh "$srv/mods" || { echo "could not assemble SIFTEC's pack"; exit 1; };;
  terralith) python3 -I ci/server/modrinth-fetch.py "$mc" "$srv/mods" terralith || { echo "could not fetch Terralith"; exit 1; };;
esac
[ -f dist/quicksilver.jar ] || { echo "dist/quicksilver.jar missing"; exit 1; }
nproc; grep -m1 'model name' /proc/cpuinfo; java -version 2>&1 | head -1

printf 'level-seed=12345\nlevel-name=world\nwhite-list=false\nenforce-whitelist=false\nonline-mode=false\nmax-tick-time=-1\npause-when-empty-seconds=-1\nspawn-protection=0\ndifficulty=normal\ngamemode=survival\nview-distance=6\nsimulation-distance=6\nsync-chunk-writes=true\n' > "$srv/server.properties"

keep_world() { # world_dir dest (as server-run.sh)
  mkdir -p "$2"
  (cd "$1" && find . -type d \( -name region -o -name entities -o -name poi \) -prune -print | while read -r d; do
     mkdir -p "$2/$d" && cp -r "$d/." "$2/$d/"; done
   cp level.dat "$2/" 2>/dev/null)
}

# one run: side (vanilla|quicksilver) round scene script ticks keep(world|-) jfr(0|1)
one() {
  local side=$1 round=$2 scene=$3 script=$4 ticks=$5 keep=$6 jfr=$7
  local dir="$OUT/$scene/$side$round"
  mkdir -p "$dir"
  rm -f "$srv/mods/quicksilver.jar"
  [ "$side" = quicksilver ] && cp dist/quicksilver.jar "$srv/mods/"
  rm -rf "$srv/world" "$srv/quicksilver" "$srv/crash-reports"
  (cd "$srv" && tar xf "$root/server-base.tar" && mv base world)
  # the scene's script, sprinting, without Quicksilver-only lines
  grep -v '^qs-at ' "ci/server/$script" > "$dir/script.txt"
  printf '\nat 0 tick sprint %s\n' "$ticks" >> "$dir/script.txt"
  local extra=()
  # P2-2 (temporary): the Logic module logs the names of BlockCollisions' methods and calls (where a hook belongs)
  [ "$side" = quicksilver ] && extra+=("-Dquicksilver.debug.logicShape=true")
  if [ "$jfr" = 1 ]; then
    extra+=("-XX:StartFlightRecording=filename=$dir/rec.jfr,dumponexit=true,settings=profile,jdk.ExecutionSample#period=1ms")
  fi
  local t0=$(date +%s)
  (cd "$srv" && timeout 1800 java -Xmx3G -Dmax.bg.threads=1 -Dci.harness.out="$dir" -Dci.harness.run="$dir/script.txt" \
     -Dci.harness.ticks="$ticks" "${extra[@]}" -jar server.jar --nogui < /dev/null > "$dir/server.log" 2>&1
   echo "EXIT $?" >> "$dir/server.log")
  local j=$(python3 -c "import json,sys; j=json.load(open(sys.argv[1])); print(j.get('msptAvg'), j.get('msptP50'), j.get('msptP99'), j.get('error',''))" "$dir/ci-harness-result.json" 2>/dev/null)
  echo "$scene $side r$round: mspt avg/p50/p99 $j ($(( $(date +%s) - t0 )) s)"
  grep -q 'CI HARNESS RUN DONE' "$dir/server.log" || { echo "  did not finish:"; tail -n 5 "$dir/server.log"; }
  [ "$keep" = world ] && keep_world "$srv/world" "$dir/world"
  if [ "$jfr" = 1 ]; then
    if [ -f "$dir/rec.jfr" ]; then
      python3 -I ci/server/perf-jfr.py "$dir/rec.jfr" "$dir/jfr.json" || echo "  jfr summary failed"
      rm -f "$dir/rec.jfr"
    else
      echo "  no JFR recording"
    fi
  fi
  cp -r "$srv/crash-reports" "$dir/crash-reports" 2>/dev/null
  rm -rf "$srv/world" "$srv/quicksilver" "$srv/crash-reports"
}

while read -r scene script ticks keep; do
  case "$scene" in ''|'#'*) continue;; esac
  echo "== scene $scene ($script, $ticks ticks sprinting, keep: $keep)"
  for r in 1 2 3 4 5; do
    if [ $((r % 2)) = 1 ]; then first=vanilla; second=quicksilver; else first=quicksilver; second=vanilla; fi
    one $first $r "$scene" "$script" "$ticks" "$keep" 0
    one $second $r "$scene" "$script" "$ticks" "$keep" 0
  done
  one quicksilver 6 "$scene" "$script" "$ticks" "$keep" 1
  one vanilla 6 "$scene" "$script" "$ticks" "$keep" 1
done < "$spec"
echo "perf done"
