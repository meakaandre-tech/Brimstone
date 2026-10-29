#!/bin/bash
# One server-suite run (phase 2, P2-0): SUITE (core | siftec | terralith) x RUN (base | vanilla-a | vanilla-b | quicksilver).
# siftec: SIFTEC's pack (ci/server/siftec-mods.sh): worldgen (its overworld, the nether, the Sift), its self-tests and a scripted
# run with Create's contraptions.
# terralith: Terralith from Modrinth: worldgen only.
# Every run loads the CI-only server harness (ci/server/harness, its jar in ci-server-harness/) and runs Java with
# -Dmax.bg.threads=1, so chunk generation happens on one worker in the order the server asks for it (Andre's decision
# 2026-10-28: fixed single-threaded order, byte for byte). quicksilver runs add the built mod jar (dist/quicksilver.jar)
# to the server's mods; vanilla runs have no Quicksilver anywhere.
#
#   base:  generates ci/server/base-area-<suite>.txt on a fresh world (seed 12345) and packs it as server-base.tar: the world
#          every scripted run starts from, so the world diff sees the run, not generation.
#   other: 1. worldgen: ci/server/gen-<suite>.txt on a fresh world, saved to $OUT/gen (compared byte for byte);
#          2. GameTests: Fabric's headless GameTest server with the harness's tests, $OUT/gametest.xml and the log;
#          3. scripted run: the base world, ci/server/scripted-run-<suite>.txt (N ticks), saved to $OUT/run (world diff),
#             with ci-harness-result.json (milliseconds per tick, heap, start-up) and, in quicksilver runs, the
#             server bench's JSON;
#          3b. random-tick run (core, P2-1): the base world, ci/server/scripted-run-core-random.txt (random ticks pinned
#             per chunk, precipitation, natural spawning), saved to $OUT/random (world diff);
#          5. heap run (P2-1, every suite): the base world, the suite's scene (ci/server/heap-<suite>.txt or its
#             scripted run's) for ten minutes of game time sprinting; live heap and class histograms after load and at
#             the end in $OUT/heap.
set +e
root=$PWD
SUITE=${SUITE:-core}
OUT=${OUT:-/tmp/sout/$SUITE-$RUN}
mkdir -p "$OUT"
exec > >(tee "$OUT/run.txt") 2>&1
echo "server suite=$SUITE run=$RUN"
srv=$root/srv
rm -rf "$srv"
bash ci/server/fetch-server.sh "$srv" || { echo "could not fetch the server"; exit 1; }
cp ci-server-harness/ci-server-harness.jar "$srv/mods/" || { echo "ci-server-harness.jar missing"; exit 1; }
mc=$(grep '^minecraft_version' gradle.properties | sed 's/.*= *//')
case "$SUITE" in
  siftec) bash ci/server/siftec-mods.sh "$srv/mods" || { echo "could not assemble SIFTEC's pack"; exit 1; };;
  terralith)
    # Terralith's newest Fabric build for this game version and the mods it requires (Lithostitched), from Modrinth
    python3 -I ci/server/modrinth-fetch.py "$mc" "$srv/mods" terralith || { echo "could not fetch Terralith"; exit 1; };;
esac
case "$RUN" in quicksilver) cp dist/quicksilver.jar "$srv/mods/" || exit 1;; esac
ls -l "$srv/mods"
java -version 2>&1 | head -1

props() { # level-name difficulty
  printf 'level-seed=12345\nlevel-name=%s\nwhite-list=false\nenforce-whitelist=false\nonline-mode=false\nmax-tick-time=-1\npause-when-empty-seconds=-1\nspawn-protection=0\ndifficulty=%s\ngamemode=survival\nview-distance=6\nsimulation-distance=6\nsync-chunk-writes=true\n' "$1" "$2" > "$srv/server.properties"
}

# serve NAME TIMEOUT_S [java -D options...]: one server start; the harness (or the GameTest server) stops it
serve() {
  local name=$1 limit=$2; shift 2
  mkdir -p "$OUT/$name"
  (cd "$srv" && timeout "$limit" java -Xmx3G -Dmax.bg.threads=1 -Dci.harness.out="$OUT/$name" "$@" -jar server.jar --nogui \
     < /dev/null > "$OUT/$name-server.log" 2>&1; echo "EXIT $?" >> "$OUT/$name-server.log")
  tail -n 3 "$OUT/$name-server.log"
  cp -r "$srv/crash-reports" "$OUT/$name-crash-reports" 2>/dev/null
  rm -rf "$srv/crash-reports"
}

# keep only what the comparisons read: region, entities and poi directories and level.dat, per dimension
keep_world() { # world_dir dest
  mkdir -p "$2"
  (cd "$1" && find . -type d \( -name region -o -name entities -o -name poi \) -prune -print | while read -r d; do
     mkdir -p "$2/$d" && cp -r "$d/." "$2/$d/"; done
   cp level.dat "$2/" 2>/dev/null)
  du -sh "$2"
}

if [ "$RUN" = base ]; then
  props base peaceful
  serve base 1800 -Dci.harness.gen="$root/ci/server/base-area-$SUITE.txt"
  grep -q 'CI HARNESS GEN DONE' "$OUT/base-server.log" || { echo "base world: generation did not finish"; exit 1; }
  (cd "$srv" && rm -f base/session.lock && tar cf "$root/server-base.tar" base) && ls -l "$root/server-base.tar"
  exit 0
fi

# 1. worldgen
props gen peaceful
serve gen 2400 -Dci.harness.gen="$root/ci/server/gen-$SUITE.txt"
grep -q 'CI HARNESS GEN DONE' "$OUT/gen-server.log" || echo "worldgen: generation did not finish"
keep_world "$srv/gen" "$OUT/gen/world"
rm -rf "$srv/gen"

# 2. GameTests (Fabric's headless GameTest server: runs every test, then stops). Core only: the GameTest server
# can't load SIFTEC's pack (its registries fail on Create: New Age's recipes there), so Create is covered by the
# SIFTEC suite's scripted run instead.
if [ "$SUITE" = core ]; then
  props gametest peaceful
  # Fabric's GameTest module (not in Fabric API's release jar; see build.yml) for this phase only
  cp ci-server-harness/fabric-gametest-api-v1.jar "$srv/mods/" || echo "fabric-gametest-api-v1.jar missing"
  serve gametest 1800 -Dfabric-api.gametest -Dfabric-api.gametest.report-file="$OUT/gametest/gametest.xml"
  rm -f "$srv/mods/fabric-gametest-api-v1.jar"
  grep -c 'CI GAMETEST' "$OUT/gametest-server.log"
  rm -rf "$srv/gametest"
fi

# 3. scripted run on the suite's base world (core and siftec; ci/server/scripted-run-<suite>.txt)
if [ -f "ci/server/scripted-run-$SUITE.txt" ] && [ -f server-base.tar ]; then
  (cd "$srv" && tar xf "$root/server-base.tar" && mv base world)
  props world normal
  # P2-1 diagnosis: every scheduled tick in three boxes where vanilla's pending ticks varied (the cave at chunk -1,-2;
  # lava in the ring at chunk -5,-1; water in the outer ring at chunk 3,5), and every chunk's post-processing
  trace="-24,30,-32,0,70,-16;-80,-60,-16,-64,-30,0;32,-10,64,64,20,96"
  serve run 1800 -Dci.harness.run="$root/ci/server/scripted-run-$SUITE.txt" -Dci.harness.trace="$trace"
  grep -q 'CI HARNESS RUN DONE' "$OUT/run-server.log" || echo "scripted run: did not finish"
  keep_world "$srv/world" "$OUT/run/world"
  mkdir -p "$OUT/bench" && cp "$srv"/quicksilver/bench/*.json "$OUT/bench/" 2>/dev/null
  rm -rf "$srv/world" "$srv/quicksilver"
elif [ -f "ci/server/scripted-run-$SUITE.txt" ]; then
  echo "no base world (server-base.tar): scripted run skipped"
fi

# 3b. the random-tick run (core): random ticks, precipitation and natural spawning, pinned (P2-1)
if [ -f "ci/server/scripted-run-$SUITE-random.txt" ] && [ -f server-base.tar ]; then
  (cd "$srv" && tar xf "$root/server-base.tar" && mv base world)
  props world normal
  serve random 1800 -Dci.harness.run="$root/ci/server/scripted-run-$SUITE-random.txt"
  grep -q 'CI HARNESS RUN DONE' "$OUT/random-server.log" || echo "random-tick run: did not finish"
  keep_world "$srv/world" "$OUT/random/world"
  rm -rf "$srv/world"
fi

# 5. the heap run (every suite with a base world, P2-1): the scene for ten minutes of game time, sprinting
heap_script=ci/server/heap-$SUITE.txt
[ -f "$heap_script" ] || heap_script=ci/server/scripted-run-$SUITE.txt
if [ -f "$heap_script" ] && [ -f server-base.tar ]; then
  (cd "$srv" && tar xf "$root/server-base.tar" && mv base world)
  props world normal
  serve heap 1800 -Dci.harness.run="$root/$heap_script" -Dci.harness.heap=true -Dci.harness.ticks=12000
  grep -q 'CI HARNESS RUN DONE' "$OUT/heap-server.log" || echo "heap run: did not finish"
  rm -rf "$srv/world" "$srv/quicksilver"
fi

# 4. SIFTEC's self-tests, on a fresh world, as SIFTEC's own CI runs them (its .github/smoke-commands.txt)
if [ "$SUITE" = siftec ]; then
  props selftest peaceful
  printf 'gamemode=creative\n' >> "$srv/server.properties"
  waitfor() { # seconds pattern file
    for i in $(seq 1 "$1"); do grep -q "$2" "$3" && return 0; sleep 1; done; echo "waited $1 s for: $2"; }
  rm -f "$srv/in.fifo"; mkfifo "$srv/in.fifo"
  (cd "$srv" && timeout 3600 java -Xmx3G -jar server.jar --nogui < in.fifo > "$OUT/selftest-server.log" 2>&1
   echo "EXIT $?" >> "$OUT/selftest-server.log") &
  exec 3> "$srv/in.fifo"
  for i in $(seq 1 120); do grep -q -e 'Done (' -e '^EXIT' "$OUT/selftest-server.log" && break; sleep 5; done
  if grep -q 'Done (' "$OUT/selftest-server.log"; then
    while read -r line; do
      case "$line" in
        "#sleep "*) sleep "${line#\#sleep }";;
        "#wait "*) w=${line#\#wait }; waitfor "${w%% *}" "${w#* }" "$OUT/selftest-server.log";;
        "#"*|"") ;;
        *) echo "$line" >&3; sleep 0.3;;
      esac
    done < siftec/.github/smoke-commands.txt
    sleep 10
    echo "stop" >&3
    for i in $(seq 1 90); do grep -q '^EXIT' "$OUT/selftest-server.log" && break; sleep 2; done
  else
    echo "SIFTEC server did not start"
  fi
  exec 3>&-
  grep -E 'SELFTEST .*(done|result|OK): [0-9]+ passed' "$OUT/selftest-server.log" | grep -v 'System chat' | sed 's/.*SELFTEST/SELFTEST/' | head -60
fi
ls -R "$OUT" | head -60
