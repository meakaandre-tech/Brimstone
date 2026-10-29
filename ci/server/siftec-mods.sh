#!/bin/bash
# usage: siftec-mods.sh MODS_DIR
# SIFTEC's pack in MODS_DIR, as ci/siftec-world.sh assembles it: SIFTEC's latest release and its self-test mod
# (siftec-selftest-26.3.jar, CI only), Create Fly, Create: New Age, Traveler's Backpack (SIFTEC's .github/fetch-tb.py)
# and the mods in SIFTEC's .github/smoke-pack-mods. SIFTEC's .github comes from a read-only checkout in ./siftec.
set -e
mods=$1
S=$PWD/siftec/.github
mc=$(grep '^minecraft_version' gradle.properties | sed 's/.*= *//')
mkdir -p "$mods"
curl -fsSL --retry 5 --retry-all-errors --retry-delay 5 -o "$mods/siftec-26.3.jar" https://github.com/meakaandre-tech/SIFTEC/releases/download/latest/siftec-26.3.jar
curl -fsSL --retry 5 --retry-all-errors --retry-delay 5 -o "$mods/siftec-selftest-26.3.jar" https://github.com/meakaandre-tech/SIFTEC/releases/download/latest/siftec-selftest-26.3.jar
curl -fsSL --retry 5 --retry-all-errors --retry-delay 5 -o "$mods/create-fly-26.3.jar" https://github.com/meakaandre-tech/Create-Fly/releases/download/pack-26.3-latest/create-fly-26.3.jar
curl -fsSL --retry 5 --retry-all-errors --retry-delay 5 -o "$mods/create-new-age-26.3.jar" https://github.com/meakaandre-tech/Create-New-Age/releases/download/fabric-26.3-latest/create-new-age-fabric-26.3.jar
set +e
python3 -I "$S/fetch-tb.py" "$mc" /tmp/tb /tmp/tb-out
cp /tmp/tb/runtime/*.jar "$mods/" 2>/dev/null || echo "no Traveler's Backpack build for $mc"
while read -r url; do
  case "$url" in
    modrinth:*)
      found=$(curl -fsSL --retry 5 --retry-all-errors --retry-delay 5 "https://api.modrinth.com/v2/project/${url#modrinth:}/version" | python3 -c "import json,sys; v=[x for x in json.load(sys.stdin) if '$mc' in x['game_versions'] and 'fabric' in x['loaders']]; print(v[0]['files'][0]['url'] if v else '')" || true)
      echo "$url -> $found"
      url=$found;;
  esac
  case "$url" in http*) curl -fsSL --retry 5 --retry-all-errors --retry-delay 5 -o "$mods/$(basename "$url")" "$url" || echo "could not fetch $url";; esac
done < "$S/smoke-pack-mods"
ls -l "$mods"
