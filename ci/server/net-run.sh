#!/bin/bash
# The server suite's network check (phase 2, P2-0): RUN = net-vanilla-client (a vanilla client on a Quicksilver
# server) or net-qs-client (a Quicksilver client on a vanilla server). The server loads the base world with the CI
# harness in run mode, waiting for the client (-Dci.harness.waitPlayer): the client joins, then the scripted run plays
# its ticks with the client connected, and the server stops. ci/server/compare-server.py checks that the client
# joined, stayed connected until the server stopped, and that neither side logged an error or a protocol problem.
# The client is the dev client (./gradlew runClient), set up as in ci/client-run.sh.
set +e
root=$PWD
OUT=${OUT:-/tmp/sout/core-$RUN}
mkdir -p "$OUT"
exec > >(tee "$OUT/run.txt") 2>&1
echo "network check run=$RUN"
case "$RUN" in
  net-vanilla-client) qs_server=1; qs_client=0;;
  net-qs-client) qs_server=0; qs_client=1;;
  *) echo "unknown run $RUN"; exit 1;;
esac
srv=$root/srv
rm -rf "$srv"
bash ci/server/fetch-server.sh "$srv" || { echo "could not fetch the server"; exit 1; }
cp ci-server-harness/ci-server-harness.jar "$srv/mods/" || exit 1
[ $qs_server = 1 ] && cp dist/quicksilver.jar "$srv/mods/"
[ -f server-base.tar ] || { echo "no base world"; exit 1; }
(cd "$srv" && tar xf "$root/server-base.tar" && mv base world)
printf 'level-seed=12345\nlevel-name=world\nwhite-list=false\nenforce-whitelist=false\nonline-mode=false\nmax-tick-time=-1\npause-when-empty-seconds=-1\nspawn-protection=0\ndifficulty=normal\ngamemode=survival\nview-distance=6\nsimulation-distance=6\n' > "$srv/server.properties"
ls -l "$srv/mods"
(cd "$srv" && timeout 3600 java -Xmx3G -Dmax.bg.threads=1 -Dci.harness.out="$OUT/run" -Dci.harness.waitPlayer=true \
   -Dci.harness.run="$root/ci/server/scripted-run-core.txt" -jar server.jar --nogui < /dev/null > "$OUT/run-server.log" 2>&1
 echo "EXIT $?" >> "$OUT/run-server.log") &
for i in $(seq 1 120); do grep -q 'Done (' "$OUT/run-server.log" && break; grep -q '^EXIT' "$OUT/run-server.log" && break; sleep 5; done
grep -q 'Done (' "$OUT/run-server.log" || { echo "server did not start"; exit 1; }

# ---- client (as in ci/client-run.sh)
sudo apt-get update -q >/dev/null 2>&1
sudo apt-get install -y -q xvfb libgl1-mesa-dri libglx-mesa0 libegl1 libegl-mesa0 libgles2 >/dev/null 2>&1
mkdir -p run/mods
[ $qs_client = 1 ] && cp dist/quicksilver.jar run/mods/
ls -l run/mods
# the dev classpath must not carry Quicksilver: the client loads it from run/mods or not at all
rm -rf build/classes/java/main build/resources/main
mkdir -p build/classes/java/main build/resources/main
printf 'pauseOnLostFocus:false\nonboardAccessibility:false\ntutorialStep:none\nnarrator:0\nrenderDistance:6\nguiScale:2\nmaxFps:30\nsoundCategory_master:0.0\nrenderClouds:"false"\n' > run/options.txt
export LIBGL_ALWAYS_SOFTWARE=1 SDL_VIDEO_FORCE_EGL=1
Xvfb :99 -screen 0 1280x720x24 >/dev/null 2>&1 &
export DISPLAY=:99
sleep 2
(env -u CI timeout 2400 ./gradlew runClient --no-daemon -Dorg.gradle.console=plain -x compileJava -x processResources -x classes \
   --args="--quickPlayMultiplayer localhost --width 1280 --height 720 --username QuicksilverCI" > "$OUT/client-gradle.log" 2>&1
 echo "EXIT $?" >> "$OUT/client-gradle.log") &
# the run waits for the client; then it plays its ticks and the server stops
for i in $(seq 1 660); do grep -q '^EXIT' "$OUT/run-server.log" && break; sleep 5; done
sleep 20
pkill -f KnotClient; sleep 8
cp run/logs/latest.log "$OUT/client.log" 2>/dev/null
cp -r run/crash-reports "$OUT/client-crash-reports" 2>/dev/null
cp -r "$srv/crash-reports" "$OUT/server-crash-reports" 2>/dev/null
mkdir -p "$OUT/bench" && cp "$srv"/quicksilver/bench/*.json "$OUT/bench/" 2>/dev/null
grep -E 'joined the game|left the game|lost connection|CI HARNESS RUN (START|DONE)' "$OUT/run-server.log" | head
ls -R "$OUT" | head -30
