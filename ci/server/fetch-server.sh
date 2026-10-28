#!/bin/bash
# usage: fetch-server.sh DIR
# A plain Fabric server in DIR (server.jar, mods/fabric-api.jar, eula), the versions in gradle.properties, from the
# same sources as ci/core-world.sh. Run from the repo root.
set -e
dir=$1
mc=$(grep '^minecraft_version' gradle.properties | sed 's/.*= *//')
loader=$(grep '^loader_version' gradle.properties | sed 's/.*= *//')
fapi=$(grep '^fabric_version' gradle.properties | sed 's/.*= *//')
mkdir -p "$dir/mods"
inst=$(curl -fsSL --retry 5 --retry-all-errors --retry-delay 5 https://meta.fabricmc.net/v2/versions/installer | python3 -c "import json,sys; print(json.load(sys.stdin)[0]['version'])")
curl -fsSL --retry 5 --retry-all-errors --retry-delay 5 -o "$dir/server.jar" "https://meta.fabricmc.net/v2/versions/loader/$mc/$loader/$inst/server/jar"
fapiurl=$(curl -fsSL --retry 5 --retry-all-errors --retry-delay 5 "https://api.modrinth.com/v2/project/fabric-api/version" | python3 -c "import json,sys; v=[x for x in json.load(sys.stdin) if x['version_number']=='$fapi']; print(v[0]['files'][0]['url'])")
curl -fsSL --retry 5 --retry-all-errors --retry-delay 5 -o "$dir/mods/fabric-api.jar" "$fapiurl"
echo "eula=true" > "$dir/eula.txt"
echo "fabric server $mc, loader $loader, fabric api $fapi in $dir"
