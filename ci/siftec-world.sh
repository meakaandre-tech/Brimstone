#!/bin/bash
# Builds SIFTEC's server and CI world in ./siftec-server, the way SIFTEC's own CI does: the latest SIFTEC main build
# (its `latest` release), Fabric API, Create Fly, Create: New Age, Traveler's Backpack (SIFTEC's .github/fetch-tb.py),
# the pack mods in SIFTEC's .github/smoke-pack-mods, then SIFTEC's .github/smoke-commands.txt on a fresh world, seed
# 12345. SIFTEC's .github files come from a read-only checkout of SIFTEC main in ./siftec.
set +e
root=$PWD
S=$root/siftec/.github
mkdir -p /tmp/out
exec > >(tee /tmp/out/siftec-world.txt) 2>&1
mc=$(grep '^minecraft_version' gradle.properties | sed 's/.*= *//')
loader=$(grep '^loader_version' gradle.properties | sed 's/.*= *//')
fapi=$(grep '^fabric_version' gradle.properties | sed 's/.*= *//')
mkdir -p siftec-server/mods && cd siftec-server
inst=$(curl -fsSL https://meta.fabricmc.net/v2/versions/installer | python3 -c "import json,sys; print(json.load(sys.stdin)[0]['version'])")
curl -fsSL -o server.jar "https://meta.fabricmc.net/v2/versions/loader/$mc/$loader/$inst/server/jar" || exit 1
fapiurl=$(curl -fsSL "https://api.modrinth.com/v2/project/fabric-api/version" | python3 -c "import json,sys; v=[x for x in json.load(sys.stdin) if x['version_number']=='$fapi']; print(v[0]['files'][0]['url'])")
curl -fsSL -o mods/fabric-api.jar "$fapiurl" || exit 1
curl -fsSL -o mods/siftec-26.3.jar https://github.com/meakaandre-tech/SIFTEC/releases/download/latest/siftec-26.3.jar || exit 1
# SIFTEC's test commands (siftec selftest / worldtest) are their own mod, built only in SIFTEC's CI and not in its
# release; fetched when the release carries it, otherwise the scenes that need it are skipped
if curl -fsSL -o mods/siftec-selftest-26.3.jar https://github.com/meakaandre-tech/SIFTEC/releases/download/latest/siftec-selftest-26.3.jar; then
  echo "siftec selftest mod: from the latest release"
else
  rm -f mods/siftec-selftest-26.3.jar; echo "siftec selftest mod: not in SIFTEC's latest release"
fi
curl -fsSL -o mods/create-fly-26.3.jar https://github.com/meakaandre-tech/Create-Fly/releases/download/pack-26.3-latest/create-fly-26.3.jar || exit 1
curl -fsSL -o mods/create-new-age-26.3.jar https://github.com/meakaandre-tech/Create-New-Age/releases/download/fabric-26.3-latest/create-new-age-fabric-26.3.jar || exit 1
python3 -I "$S/fetch-tb.py" "$mc" /tmp/tb /tmp/out
cp /tmp/tb/runtime/*.jar mods/ 2>/dev/null || echo "no Traveler's Backpack build for $mc"
while read -r url; do
  case "$url" in
    modrinth:*)
      found=$(curl -fsSL "https://api.modrinth.com/v2/project/${url#modrinth:}/version" | python3 -c "import json,sys; v=[x for x in json.load(sys.stdin) if '$mc' in x['game_versions'] and 'fabric' in x['loaders']]; print(v[0]['files'][0]['url'] if v else '')" || true)
      echo "$url -> $found"
      url=$found;;
  esac
  case "$url" in http*) curl -fsSL -o "mods/$(basename "$url")" "$url" || echo "could not fetch $url";; esac
done < "$S/smoke-pack-mods"
ls -l mods
echo "eula=true" > eula.txt
printf 'level-seed=12345\nwhite-list=false\nenforce-whitelist=false\nonline-mode=false\nmax-tick-time=-1\nview-distance=6\ngamemode=creative\ndifficulty=peaceful\nspawn-protection=0\npause-when-empty-seconds=-1\n' > server.properties
mkfifo in.fifo
(java -Xmx3G -jar server.jar --nogui < in.fifo > server.log 2>&1; echo "EXIT $?" >> server.log) &
exec 3> in.fifo
for i in $(seq 1 120); do
  if grep -q 'Done (' server.log || grep -q '^EXIT' server.log; then break; fi
  sleep 5
done
if grep -q 'Done (' server.log; then
  while read -r line; do case "$line" in "#sleep "*) sleep "${line#\#sleep }";; *) echo "$line" >&3; sleep 0.3;; esac; done < "$S/smoke-commands.txt"
  sleep 10
  # Quicksilver's own addition: generate the areas the SIFTEC scenes look at that SIFTEC's smoke run doesn't (The Sift
  # around 0,0 and the MAM platform), so every client run loads the same saved chunks instead of generating them
  echo "execute in the_sift:the_sift run forceload add -128 -128 127 127" >&3
  echo "execute in minecraft:overworld run forceload add 2128 -2064 2271 -1937" >&3
  # package 7: the map scene's area round 300 -200 (what the fullscreen map shows at its scale, with room), so its
  # terrain is the same saved chunks in every run instead of generated (and recorded by SIFTEC's map) in each
  echo "execute in minecraft:overworld run forceload add 96 -400 255 -1" >&3
  echo "execute in minecraft:overworld run forceload add 256 -400 415 -1" >&3
  echo "execute in minecraft:overworld run forceload add 416 -400 511 -1" >&3
  sleep 210
  echo "execute in the_sift:the_sift run forceload remove all" >&3
  echo "execute in minecraft:overworld run forceload remove 2128 -2064 2271 -1937" >&3
  echo "execute in minecraft:overworld run forceload remove 96 -400 255 -1" >&3
  echo "execute in minecraft:overworld run forceload remove 256 -400 415 -1" >&3
  echo "execute in minecraft:overworld run forceload remove 416 -400 511 -1" >&3
  # package 7: no random ticks and no mob griefing in the CI world (water freezing over, grass and leaves changing at
  # random, sheep eating grass: one of the map's pixels changed colour in one run), in vanilla's runs as much as
  # Quicksilver's, so what each scene shows doesn't depend on how long a run has been going
  echo "gamerule random_tick_speed 0" >&3
  echo "gamerule mob_griefing false" >&3
  echo "save-all flush" >&3
  sleep 20
  echo "stop" >&3
  for i in $(seq 1 90); do grep -q '^EXIT' server.log && break; sleep 2; done
fi
exec 3>&-
rm -f in.fifo
cp server.log /tmp/out/siftec-world-server.log
grep -q 'Done (' server.log || exit 1
du -sh world mods
