#!/bin/bash
# Builds the core suite's server and world once (./core-server): a plain Fabric server with Fabric API, seed 12345,
# with the areas the core scenes look at generated and saved. Every core client run (vanilla and Brimstone, every GPU
# profile) then loads the same saved chunks. Generating them in each run made the picture differ between identical
# runs: Minecraft's feature placement at chunk borders (trees, for one) depends on the order chunks generate in.
set +e
root=$PWD
mkdir -p /tmp/out
exec > >(tee /tmp/out/core-world.txt) 2>&1
mc=$(grep '^minecraft_version' gradle.properties | sed 's/.*= *//')
loader=$(grep '^loader_version' gradle.properties | sed 's/.*= *//')
fapi=$(grep '^fabric_version' gradle.properties | sed 's/.*= *//')
mkdir -p core-server/mods && cd core-server
inst=$(curl -fsSL https://meta.fabricmc.net/v2/versions/installer | python3 -c "import json,sys; print(json.load(sys.stdin)[0]['version'])")
curl -fsSL -o server.jar "https://meta.fabricmc.net/v2/versions/loader/$mc/$loader/$inst/server/jar" || exit 1
fapiurl=$(curl -fsSL "https://api.modrinth.com/v2/project/fabric-api/version" | python3 -c "import json,sys; v=[x for x in json.load(sys.stdin) if x['version_number']=='$fapi']; print(v[0]['files'][0]['url'])")
curl -fsSL -o mods/fabric-api.jar "$fapiurl" || exit 1
echo "eula=true" > eula.txt
printf 'level-seed=12345\nwhite-list=false\nenforce-whitelist=false\nonline-mode=false\nmax-tick-time=-1\nview-distance=12\ngamemode=creative\ndifficulty=peaceful\nspawn-protection=0\npause-when-empty-seconds=-1\n' > server.properties
mkfifo in.fifo
(java -Xmx3G -jar server.jar --nogui < in.fifo > server.log 2>&1; echo "EXIT $?" >> server.log) &
exec 3> in.fifo
for i in $(seq 1 120); do
  if grep -q 'Done (' server.log || grep -q '^EXIT' server.log; then break; fi
  sleep 5
done
# forceload takes at most 256 chunks per command: each area is cut into strips of whole chunks
area() { # dimension x1 z1 x2 z2 (block coordinates, inclusive)
  python3 - "$@" <<'PY'
import sys
dim, x1, z1, x2, z2 = sys.argv[1], *map(int, sys.argv[2:])
cx1, cz1, cx2, cz2 = x1 >> 4, z1 >> 4, x2 >> 4, z2 >> 4
rows = cz2 - cz1 + 1
step = max(1, 256 // rows)
cx = cx1
while cx <= cx2:
    e = min(cx2, cx + step - 1)
    print(f"execute in {dim} run forceload add {cx*16} {cz1*16} {e*16+15} {cz2*16+15}")
    cx = e + 1
PY
}
if grep -q 'Done (' server.log; then
  {
    area minecraft:overworld -64 -192 271 143            # spawn (camera 96 -27, 8 chunks)
    area minecraft:overworld 1824 -2160 2287 -1808       # water and glass, mobs (2000/2100 -1985, 8 chunks)
    area minecraft:the_nether -176 -176 175 175          # nether (0 0, 8 chunks)
    area minecraft:overworld -6224 -6224 -5777 -5777     # fly-through start (-6000 -6000, 12 chunks)
  } > /tmp/out/core-world-commands.txt
  while read -r line; do echo "$line" >&3; sleep 0.3; done < /tmp/out/core-world-commands.txt
  sleep 300
  echo "execute in minecraft:overworld run forceload remove all" >&3
  echo "execute in minecraft:the_nether run forceload remove all" >&3
  echo "save-all flush" >&3
  sleep 30
  echo "stop" >&3
  for i in $(seq 1 90); do grep -q '^EXIT' server.log && break; sleep 2; done
fi
exec 3>&-
rm -f in.fifo
cp server.log /tmp/out/core-world-server.log
grep -q 'Done (' server.log || exit 1
du -sh world
