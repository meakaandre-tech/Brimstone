#!/bin/bash
# One CI client run: SUITE (core | siftec) x RUN (vanilla-a | vanilla-b | vanilla-old | vanilla-vk | brimstone-full |
# brimstone-old | brimstone-vk | measure-full | measure-old | measure-vk). measure runs load Brimstone with every module
# off (brimstone.ci.measureOnly): vanilla draws everything and Brimstone's bench measures it, in the same profile as the
# Brimstone run of the same name (package 3).
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

case "$RUN" in brimstone-*|measure-*) brim=1;; *) brim=0;; esac
# package 4b: bench-plain / bench-sodium load only the bench-only jar (brimstone-bench, every Brimstone module out),
# on plain Fabric or next to Sodium, and bench the scenes in BENCH_SCENES; the server has no Brimstone
case "$RUN" in bench-*) benchonly=1;; *) benchonly=0;; esac
case "$RUN" in measure-*) measure=1;; *) measure=0;; esac
case "$RUN" in *-old)
  export MESA_EXTENSION_OVERRIDE="-GL_ARB_multi_draw_indirect -GL_ARB_buffer_storage"
  export MESA_GL_VERSION_OVERRIDE=3.3
  export MESA_GLSL_VERSION_OVERRIDE=330
  echo "old-GPU profile: MESA_EXTENSION_OVERRIDE='$MESA_EXTENSION_OVERRIDE' MESA_GL_VERSION_OVERRIDE=3.3 MESA_GLSL_VERSION_OVERRIDE=330";;
esac
extra_args=""
client_jvm=""
case "$RUN" in *-vk) extra_args="--graphicsBackend vulkan"; echo "Vulkan profile: --graphicsBackend vulkan on lavapipe";; esac
case "$RUN" in brimstone-old|measure-old) client_jvm="-Dbrimstone.debug.forceIntelMdiIssue=true"; echo "old-GPU Brimstone run: $client_jvm";; esac
# every Brimstone run: the visibility module checks each entity and block entity it hides with rays from the camera
# (package 2's pop-in check; ci/compare.py fails on any violation). VISIBILITY_DRY_RUN=1 makes it decide and count but hide
# nothing (used once to sample the new occlusion scene's noise with Brimstone drawing exactly vanilla's set).
# every Brimstone run also checks its translucent draws each frame against vanilla's order and latest sort (package 3;
# ci/compare.py fails on any miss). TRANSLUCENCY_DRY_RUN=1 leaves translucent terrain with vanilla (noise collection only).
# and every Brimstone run runs the renderer's self-test once in the world (package 4; ci/compare.py requires "passed").
if [ $measure = 1 ]; then
  client_jvm="$client_jvm -Dbrimstone.ci.measureOnly=true"
  echo "measure run JVM properties:$client_jvm"
elif [ $brim = 1 ]; then
  # package 8: the animation module logs each animated sprite's first use outside terrain, with where it came from
  # package 4b: the terrain upload budget logs when meshes wait for a later frame and when it has caught up, so
  # drive.py takes no screenshot while a section is still waiting (vanilla uploads at once and logs nothing)
  client_jvm="$client_jvm -Dbrimstone.debug.logUploadQueue=true -Dbrimstone.debug.verifyVisibility=true -Dbrimstone.debug.verifyTranslucency=true -Dbrimstone.debug.verifyRenderer=true -Dbrimstone.debug.animationUses=true"
  if [ "${VISIBILITY_DRY_RUN:-0}" = 1 ]; then client_jvm="$client_jvm -Dbrimstone.debug.visibilityDryRun=true"; fi
  if [ "${TRANSLUCENCY_DRY_RUN:-0}" = 1 ]; then client_jvm="$client_jvm -Dbrimstone.debug.translucencyDryRun=true"; fi
  echo "Brimstone run JVM properties:$client_jvm"
fi

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
# measure runs stand in for vanilla, so Fabric's Indigo draws there as in the vanilla runs: their copy of the jar does
# not declare fabric-renderer-api-v1:contains_renderer (and Brimstone does not register its renderer, measureOnly)
if [ $measure = 1 ]; then
  python3 -I - server/mods/brimstone.jar <<'PY' || { echo "could not take contains_renderer out of the measure run's jar"; exit 1; }
import json, os, sys, zipfile
jar = sys.argv[1]
src = zipfile.ZipFile(jar)
out = zipfile.ZipFile(jar + ".tmp", "w", zipfile.ZIP_DEFLATED)
for item in src.infolist():
    data = src.read(item.filename)
    if item.filename == "fabric.mod.json":
        meta = json.loads(data)
        del meta["custom"]["fabric-renderer-api-v1:contains_renderer"]
        data = json.dumps(meta, indent=2).encode()
    out.writestr(item, data)
out.close()
os.replace(jar + ".tmp", jar)
print("measure run: contains_renderer taken out of the jar's fabric.mod.json (Indigo draws)")
PY
fi
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
if [ $benchonly = 1 ]; then
  [ -f dist/brimstone-bench.jar ] || { echo "dist/brimstone-bench.jar missing"; exit 1; }
  cp dist/brimstone-bench.jar run/mods/
  if [ "$RUN" = bench-sodium ]; then
    # Sodium 0.9.2 for this Minecraft version, from Modrinth (a runtime test only: nothing of Sodium's is in the repo)
    surl=$(curl -fsSL "https://api.modrinth.com/v2/project/sodium/version?loaders=%5B%22fabric%22%5D&game_versions=%5B%22$mc%22%5D" | python3 -c "
import json,sys
vs=[v for v in json.load(sys.stdin) if '0.9.2' in v['version_number']]
print(vs[0]['files'][0]['url'] if vs else '')
for v in vs[:1]: print(v['version_number'], file=sys.stderr)")
    if [ -z "$surl" ]; then echo "no Sodium 0.9.2 build for Minecraft $mc on Modrinth"; exit 1; fi
    curl -fsSL -o run/mods/sodium.jar "$surl" || { echo "could not download Sodium from $surl"; exit 1; }
    echo "Sodium: $surl"
  fi
fi
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

# one client session per render distance and set of client options (package 3: scenes with "clientOptions", e.g.
# Improved Transparency, get their own session with those lines in options.txt; the same in every run; package 4:
# "brimstoneConfig", e.g. the block renderer off, likewise, written to config/brimstone.properties)
groups=$(python3 -c "
import json; s=json.load(open('$suitefile'))
rd={}
for x in s['scenes']:
    o=dict(x.get('clientOptions') or {}); o.update({'brimstone.'+k: v for k,v in (x.get('brimstoneConfig') or {}).items()})
    opts=','.join(k+'='+str(v) for k,v in sorted(o.items()))
    rd.setdefault((x.get('renderDistance',8), opts),[]).append(x['id'])
print(' '.join(str(k[0])+'|'+(k[1] or '-')+'|'+','.join(v) for k,v in sorted(rd.items())))")
echo "sessions: $groups"
n=0
for g in $groups; do
  n=$((n+1))
  rd=${g%%|*}; rest=${g#*|}; opts=${rest%%|*}; ids=$(echo "${rest#*|}" | tr ',' ' ')
  if [ $benchonly = 1 ]; then
    keep=""
    for id in $ids; do case " $BENCH_SCENES " in *" $id "*) keep="$keep $id";; esac; done
    ids=$(echo $keep)
    if [ -z "$ids" ]; then echo "session $n: no scene of BENCH_SCENES, not started"; continue; fi
  fi
  # clouds off: their position follows elapsed game time, which differs between runs, so they would only add noise
  # no version line: the file is datafixed from the oldest layout, where key codes are LWJGL2 numbers (22 = U)
  # glintSpeed 0 (package 7): the enchantment glint's texture offset follows the wall clock, so a glinting item (the MAM's
  # selected tab) was caught in a different phase in every run; at speed 0 it is still drawn, but holds still
  # menuBackgroundBlurriness 0 (package 7): the menu blur behind screens such as Create's Ponder index came out
  # differently on Vulkan than on OpenGL (433,000 pixels apart), which left the strict check blind on 63% of that screen
  # chatHeightFocused 0.25 (package 7): the open chat shows its last 6 lines only, so the SIFTEC hud scene's chat shows
  # its own lines and not the bench messages earlier scenes leave in Brimstone runs
  # glDebugVerbosity 1 (the game's default, set explicitly): the GPU's high-severity debug messages, errors among them,
  # go to the log in every run alike; ci/compare.py compares their counts between Brimstone and vanilla
  printf 'pauseOnLostFocus:false\nglDebugVerbosity:1\nonboardAccessibility:false\ntutorialStep:none\nnarrator:0\nrenderDistance:%s\nguiScale:2\nmaxFps:30\nsoundCategory_master:0.0\nrenderClouds:"false"\nglintSpeed:0.0\nmenuBackgroundBlurriness:0\nchatHeightFocused:0.25\nkey_key.use:22\n' "$rd" > run/options.txt
  # package 4: "brimstoneConfig" entries (brimstone.<key>=<value>) go to Brimstone's config file, the rest to options.txt
  mkdir -p run/config; rm -f run/config/brimstone.properties
  if [ "$opts" != "-" ]; then
    for kv in $(echo "$opts" | tr ',' ' '); do
      case "$kv" in
        brimstone.*) k=${kv%%=*}; echo "${k#brimstone.}=${kv#*=}" >> run/config/brimstone.properties;;
        *) echo "${kv%%=*}:${kv#*=}" >> run/options.txt;;
      esac
    done
    echo "session $n client options: $opts"
  fi
  rm -f run/logs/latest.log run/logs/*.log.gz
  : > "$OUT/joined-$n.txt"
  before=$(grep -c 'joined the game' server/server.log)
  # --username (package 7): the dev launcher otherwise names the player Player<random digits>, and the offline UUID
  # made from the name picks the default skin, so the first-person arm and SIFTEC's company name ("<player>'s
  # Company" on the HUB and MAM screens) differed between runs; one fixed name in every run
  (env -u CI JAVA_TOOL_OPTIONS="${client_jvm# }" timeout 2400 ./gradlew runClient --no-daemon -Dorg.gradle.console=plain -x compileJava -x processResources -x classes \
     --args="--quickPlayMultiplayer localhost --width 1280 --height 720 --username BrimstoneCI $extra_args" > "$OUT/client-gradle-$n.log" 2>&1; echo "EXIT $?" >> "$OUT/client-gradle-$n.log") &
  for i in $(seq 1 150); do
    now=$(grep -c 'joined the game' server/server.log)
    if [ "$now" -gt "$before" ] || grep -q '^EXIT' "$OUT/client-gradle-$n.log"; then break; fi
    sleep 5
  done
  if [ "$(grep -c 'joined the game' server/server.log)" -gt "$before" ]; then
    echo "op @a" >&3
    sleep 40
    xdotool mousemove 640 360
    python3 -I ci/drive.py "$suitefile" server/in.fifo "$OUT" run/logs/latest.log "$([ $brim = 1 ] || [ $benchonly = 1 ] && echo brimstone || echo vanilla)" "$BENCH_SECONDS" $ids
  else
    echo "client did not join (session $n)"
    import -window root "$OUT/no-join-$n.png"
  fi
  pkill -f KnotClient; sleep 8
  # the game rolls latest.log over at midnight (UTC on the runners) into a dated .log.gz: keep the session's rolled
  # parts in front of it, so client-$n.log is the whole session (run 40605316942 crossed midnight and lost them)
  # (one session is one game start: only the text from its last "Loading Minecraft" line on, since the game may also
  # archive a previous session's log into a .log.gz when it starts)
  { for z in $(ls run/logs/*.log.gz 2>/dev/null | sort); do zcat "$z"; done; cat run/logs/latest.log 2>/dev/null; } \
    | awk '/Loading Minecraft .* with Fabric Loader/ {n++} {l[NR]=$0; s[NR]=n} END {for (i=1;i<=NR;i++) if (s[i]==n) print l[i]}' > "$OUT/client-$n.log"
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
