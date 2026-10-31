#!/bin/bash
# Package 4c (Andre, 2026-10-30): one single-player client run (RUN=sp-quicksilver, core suite, full mode only). The
# client plays the core world on its own integrated server, with CI's pin mod working the server for SERVER_LOAD_MS
# per tick (standing in for a heavy factory: SIFTEC's factory isn't in the core pack), and benches the fly-through
# scene with Quicksilver. The bench's mesher.serverAware block shows whether meshing backed off while the server was
# behind and kept a thread meshing; ci/compare.py checks it.
set +e
root=$PWD
OUT=${OUT:-/tmp/out}
BENCH_SECONDS=${BENCH_SECONDS:-20}
SERVER_LOAD_MS=${SERVER_LOAD_MS:-45}
mkdir -p "$OUT"
exec > >(tee "$OUT/run.txt") 2>&1
echo "single-player run: suite=$SUITE run=$RUN server load ${SERVER_LOAD_MS} ms per tick"
suitefile=src/main/resources/quicksilver/scenes/core.json
[ -d core-server/world ] || { echo "no core world (core-server/world)"; ls -la core-server; exit 1; }
bash ci/pack-check.sh core-server/mods core complete "$OUT/pack-sha256.txt" || { echo "the pack changed (see above)"; exit 1; }

# the core world as a single-player save, with commands allowed (the bench sets its scene up with commands)
mkdir -p run/saves run/mods
rm -rf run/saves/ci-sp && cp -r core-server/world run/saves/ci-sp
python3 -I ci/sp-leveldat.py run/saves/ci-sp/level.dat || { echo "could not allow commands in the save"; exit 1; }

for j in core-server/mods/*.jar; do case "$j" in *fabric-api*) ;; *) cp "$j" run/mods/;; esac; done
cp dist/quicksilver.jar run/mods/ || exit 1
if [ -f ci-pin/ci-pin.jar ]; then cp ci-pin/ci-pin.jar run/mods/; else echo "ci-pin.jar missing"; exit 1; fi
ls -l run/mods
rm -rf build/classes/java/main build/resources/main
mkdir -p build/classes/java/main build/resources/main

sudo apt-get update -q >/dev/null 2>&1
sudo apt-get install -y -q xvfb xdotool x11-utils imagemagick libgl1-mesa-dri libglx-mesa0 libegl1 libegl-mesa0 mesa-utils >/dev/null 2>&1
export LIBGL_ALWAYS_SOFTWARE=1 SDL_VIDEO_FORCE_EGL=1
Xvfb :99 -screen 0 1280x720x24 >/dev/null 2>&1 &
export DISPLAY=:99
sleep 2

rd=$(python3 -c "import json; print([s for s in json.load(open('$suitefile'))['scenes'] if s['id']=='flythrough'][0].get('renderDistance', 8))")
printf 'pauseOnLostFocus:false\nglDebugVerbosity:1\nonboardAccessibility:false\ntutorialStep:none\nnarrator:0\nrenderDistance:%s\nsimulationDistance:%s\nguiScale:2\nmaxFps:30\nsoundCategory_master:0.0\nrenderClouds:"false"\nglintSpeed:0.0\nmenuBackgroundBlurriness:0\nchatHeightFocused:0.25\nkey_key.use:22\n' "$rd" "$rd" > run/options.txt
mkdir -p run/config; rm -f run/config/quicksilver.properties
rm -f run/logs/latest.log run/logs/*.log.gz
jvm="-Dquicksilver.ci.serverLoadMs=$SERVER_LOAD_MS"
(env -u CI JAVA_TOOL_OPTIONS="$jvm" timeout 1800 ./gradlew runClient --no-daemon -Dorg.gradle.console=plain -x compileJava -x processResources -x classes \
   --args="--quickPlaySingleplayer ci-sp --width 1280 --height 720 --username QuicksilverCI" > "$OUT/client-gradle-1.log" 2>&1; echo "EXIT $?" >> "$OUT/client-gradle-1.log") &
joined=0
for i in $(seq 1 180); do
  if grep -q 'joined the game' run/logs/latest.log 2>/dev/null; then joined=1; break; fi
  grep -q '^EXIT' "$OUT/client-gradle-1.log" && break
  sleep 5
done
chat() { xdotool key slash; sleep 1.5; xdotool type --delay 40 "$1"; sleep 0.5; xdotool key Return; sleep 1; }
if [ $joined = 1 ]; then
  sleep 40
  xdotool mousemove 640 360
  for c in $(python3 -c "import json; print(' '.join(c.replace(' ', '~') for c in json.load(open('$suitefile')).get('preamble', [])))"); do chat "${c//\~/ }"; done
  start=$(stat -c %s run/logs/latest.log)
  chat "quicksilver bench flythrough $BENCH_SECONDS confirm"
  result=timeout
  for i in $(seq 1 200); do
    sleep 3
    if tail -c +"$start" run/logs/latest.log | grep -q 'QUICKSILVER BENCH END scene=flythrough'; then result=done; break; fi
    if tail -c +"$start" run/logs/latest.log | grep -q 'QUICKSILVER BENCH ABORTED scene=flythrough'; then result=aborted; break; fi
  done
  echo "bench flythrough: $result"
  import -window root "$OUT/end.png"
else
  echo "the single-player world did not start"
  import -window root "$OUT/no-join.png"
fi
pkill -f KnotClient; sleep 8
cp run/logs/latest.log "$OUT/client-1.log" 2>/dev/null
cp -r run/crash-reports "$OUT/client-crash-reports" 2>/dev/null
mkdir -p "$OUT/bench" && cp run/quicksilver/bench/*.json "$OUT/bench/" 2>/dev/null
ls -R "$OUT" | head -40
exit 0
