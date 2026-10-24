#!/bin/bash
# One CI client run: SUITE (core | siftec) x RUN (vanilla-a | vanilla-b | vanilla-old | brimstone-full | brimstone-old).
#
# Starts the suite's server (core: a plain Fabric server with Fabric API, seed 12345, fresh world; siftec: SIFTEC's
# server and CI world from the siftec-world job), then one dev client per render distance the suite's scenes use,
# drives the scenes (ci/drive.py), and leaves everything in $OUT. Brimstone runs put the built jar in the client's and
# the server's mods; vanilla runs have no Brimstone anywhere (the dev classpath's own copy is emptied out first).
# old runs hide multi-draw indirect and buffer storage and force GL down to vanilla's minimum (3.3); the Brimstone old
# run also sets brimstone.debug.forceIntelMdiIssue (vanilla's Intel multi-draw-indirect flag, the school laptop's path).
# vk runs use Mesa's Vulkan driver (lavapipe) through the --graphicsBackend launch argument.
set +e
root=$PWD
OUT=${OUT:-/tmp/out}
BENCH_SECONDS=${BENCH_SECONDS:-20}
mkdir -p "$OUT"
exec > >(tee "$OUT/run.txt") 2>&1
echo "suite=$SUITE run=$RUN"
mc=$(grep '^minecraft_version' gradle.properties | sed 's/.*= *//')
loader=$(grep '^loader_version' gradle.properties | sed 's/.*= *//')
fapi=$(grep '^fabric_version' gradle.properties | sed 's/.*= *//')
suitefile=src/main/resources/brimstone/scenes/$SUITE.json

case "$RUN" in brimstone-*) brim=1;; *) brim=0;; esac
case "$RUN" in *-old)
  export MESA_EXTENSION_OVERRIDE="-GL_ARB_multi_draw_indirect -GL_ARB_buffer_storage"
  export MESA_GL_VERSION_OVERRIDE=3.3
  export MESA_GLSL_VERSION_OVERRIDE=330
  echo "old-GPU profile: MESA_EXTENSION_OVERRIDE='$MESA_EXTENSION_OVERRIDE' MESA_GL_VERSION_OVERRIDE=3.3 MESA_GLSL_VERSION_OVERRIDE=330";;
esac
extra_args=""
client_jvm=""
case "$RUN" in *-vk) extra_args="--graphicsBackend vulkan"; echo "Vulkan profile: --graphicsBackend vulkan on lavapipe";; esac
case "$RUN" in brimstone-old) client_jvm="-Dbrimstone.debug.forceIntelMdiIssue=true"; echo "old-GPU Brimstone run: $client_jvm";; esac

# ---- server
if [ "$SUITE" = siftec ]; then
  cp -r siftec-server server
elif [ -d core-server ]; then
  # the core world, generated once per CI run by ci/core-world.sh, so every run loads the same chunks
  cp -r core-server server
  echo "core server and world: from the core-world job"
else
  mkdir -p server/mods
  inst=$(curl -fsSL https://meta.fabricmc.net/v2/versions/installer | python3 -c "import json,sys; print(json.load(sys.stdin)[0]['version'])")
  curl -fsSL -o server/server.jar "https://meta.fabricmc.net/v2/versions/loader/$mc/$loader/$inst/server/jar"
  fapiurl=$(curl -fsSL "https://api.modrinth.com/v2/project/fabric-api/version" | python3 -c "import json,sys; v=[x for x in json.load(sys.stdin) if x['version_number']=='$fapi']; print(v[0]['files'][0]['url'])")
  curl -fsSL -o server/mods/fabric-api.jar "$fapiurl"
  echo "eula=true" > server/eula.txt
fi
if [ $brim = 1 ]; then cp dist/brimstone.jar server/mods/; fi
cd server
# SIFTEC's own settings, plus a view distance that covers the 12-chunk fly-through and no console echo in chat
printf 'level-seed=12345\nwhite-list=false\nenforce-whitelist=false\nonline-mode=false\nmax-tick-time=-1\nview-distance=12\ngamemode=creative\ndifficulty=peaceful\nspawn-protection=0\npause-when-empty-seconds=-1\nbroadcast-console-to-ops=false\n' > server.properties
ls -l mods
rm -f in.fifo; mkfifo in.fifo
(java -Xmx3G -jar server.jar --nogui < in.fifo > server.log 2>&1; echo "EXIT $?" >> server.log) &
exec 3> in.fifo
for i in $(seq 1 120); do
  if grep -q 'Done (' server.log || grep -q '^EXIT' server.log; then break; fi
  sleep 5
done
cd "$root"
if ! grep -q 'Done (' server/server.log; then echo "server did not start"; cp server/server.log "$OUT/"; exit 1; fi

# ---- client
sudo apt-get update -q >/dev/null 2>&1
sudo apt-get install -y -q xvfb xdotool x11-utils imagemagick libgl1-mesa-dri libglx-mesa0 libegl1 libegl-mesa0 libgles2 mesa-utils mesa-vulkan-drivers libvulkan1 vulkan-tools python3-numpy python3-pil >/dev/null 2>&1
lvp=$(ls /usr/share/vulkan/icd.d/lvp_icd*.json 2>/dev/null | head -1)
case "$RUN" in *-vk) [ -n "$lvp" ] && export VK_DRIVER_FILES="$lvp" VK_ICD_FILENAMES="$lvp";; esac
mkdir -p run/mods
for j in server/mods/*.jar; do case "$j" in *fabric-api*) ;; *) cp "$j" run/mods/;; esac; done
# every run, vanilla and Brimstone alike: CI's lightmap pin (ci/pin-mod), which holds the block-light flicker still
if [ -f ci-pin/ci-pin.jar ]; then cp ci-pin/ci-pin.jar run/mods/; else echo "ci-pin.jar missing"; exit 1; fi
ls -l run/mods
# mod ids the client loads (pack jars plus the dev classpath's Minecraft, loader and Fabric API), for scenes that need one
(for j in run/mods/*.jar; do unzip -p "$j" fabric.mod.json 2>/dev/null | python3 -c "import json,sys; print(json.load(sys.stdin)['id'])" 2>/dev/null; done; echo minecraft fabricloader fabric-api) > "$OUT/mod-ids.txt"
export MOD_IDS="$OUT/mod-ids.txt"
# the dev classpath must not carry Brimstone: every run loads it from run/mods or not at all
rm -rf build/classes/java/main build/resources/main
mkdir -p build/classes/java/main build/resources/main
export LIBGL_ALWAYS_SOFTWARE=1
# MC 26.3 (SDL3) asks for an sRGB-capable framebuffer; Xvfb's GLX visuals have none, EGL does
export SDL_VIDEO_FORCE_EGL=1
Xvfb :99 -screen 0 1280x720x24 >/dev/null 2>&1 &
export DISPLAY=:99
sleep 2
(glxinfo -B; eglinfo -B 2>/dev/null | head -60; vulkaninfo --summary 2>/dev/null | head -60) > "$OUT/gfx.txt" 2>&1
echo "op @a" >&3

groups=$(python3 -c "
import json; s=json.load(open('$suitefile'))
rd={}
[rd.setdefault(x.get('renderDistance',8),[]).append(x['id']) for x in s['scenes']]
print(' '.join(str(k)+':'+','.join(v) for k,v in sorted(rd.items())))")
echo "sessions: $groups"
n=0
for g in $groups; do
  n=$((n+1))
  rd=${g%%:*}; ids=$(echo "${g#*:}" | tr ',' ' ')
  # clouds off: their position follows elapsed game time, which differs between runs, so they would only add noise
  # no version line: the file is datafixed from the oldest layout, where key codes are LWJGL2 numbers (22 = U)
  # glDebugVerbosity 1 (the game's default, set explicitly): the GPU's high-severity debug messages, errors among them,
  # go to the log in every run alike; ci/compare.py compares their counts between Brimstone and vanilla
  printf 'pauseOnLostFocus:false\nglDebugVerbosity:1\nonboardAccessibility:false\ntutorialStep:none\nnarrator:0\nrenderDistance:%s\nguiScale:2\nmaxFps:30\nsoundCategory_master:0.0\nrenderClouds:"false"\nkey_key.use:22\n' "$rd" > run/options.txt
  rm -f run/logs/latest.log
  : > "$OUT/joined-$n.txt"
  before=$(grep -c 'joined the game' server/server.log)
  (env -u CI JAVA_TOOL_OPTIONS="$client_jvm" timeout 2400 ./gradlew runClient --no-daemon -Dorg.gradle.console=plain -x compileJava -x processResources -x classes \
     --args="--quickPlayMultiplayer localhost --width 1280 --height 720 $extra_args" > "$OUT/client-gradle-$n.log" 2>&1; echo "EXIT $?" >> "$OUT/client-gradle-$n.log") &
  for i in $(seq 1 150); do
    now=$(grep -c 'joined the game' server/server.log)
    if [ "$now" -gt "$before" ] || grep -q '^EXIT' "$OUT/client-gradle-$n.log"; then break; fi
    sleep 5
  done
  if [ "$(grep -c 'joined the game' server/server.log)" -gt "$before" ]; then
    echo "op @a" >&3
    sleep 40
    xdotool mousemove 640 360
    python3 -I ci/drive.py "$suitefile" server/in.fifo "$OUT" run/logs/latest.log "$([ $brim = 1 ] && echo brimstone || echo vanilla)" "$BENCH_SECONDS" $ids
  else
    echo "client did not join (session $n)"
    import -window root "$OUT/no-join-$n.png"
  fi
  pkill -f KnotClient; sleep 8
  cp run/logs/latest.log "$OUT/client-$n.log" 2>/dev/null
  cp -r run/crash-reports "$OUT/client-crash-reports" 2>/dev/null
done
mkdir -p "$OUT/bench" && cp run/brimstone/bench/*.json "$OUT/bench/" 2>/dev/null
echo "stop" >&3
for i in $(seq 1 60); do grep -q '^EXIT' server/server.log && break; sleep 2; done
exec 3>&-
cp server/server.log "$OUT/server.log"
cp -r server/crash-reports "$OUT/server-crash-reports" 2>/dev/null
ls -R "$OUT" | head -80
exit 0
