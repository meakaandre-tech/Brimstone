#!/bin/bash
# usage: pack-check.sh DIR SET [complete] [REPORT]
# Checks the jars CI downloaded into DIR against ci/pack-lock.txt (lines "<sha256>  <SET>/<jar name>"; sets: core,
# siftec, sodium). Any jar that differs from its locked hash, or isn't in the lock, fails with "pack changed: <jar>";
# with "complete", so does a locked jar of SET that is missing from DIR. Quicksilver's own jars, CI's pin mod and the server harness (built
# in CI) are not checked. Every jar's hash is also written to REPORT (default: stdout) for the record.
# A jar locked as "*" is accepted at any hash (MapMod, while it is being finished: Andre, 2026-10-30).
# A SET with no entries in the lock yet is recorded, not checked (the first run after adding a set). Updating the lock
# is a deliberate commit that re-collects the numbers the changed jars affect (docs/ci-limits.md, "Pack lock").
dir=$1
set_=$2
mode=${3:-partial}
report=${4:-/dev/stdout}
lock="$(cd "$(dirname "$0")" && pwd)/pack-lock.txt"
locked=$(grep -v '^#' "$lock" 2>/dev/null | awk -v s="$set_/" 'index($2, s) == 1 {print $1, substr($2, length(s) + 1)}')
bad=0
seen=""
for j in "$dir"/*.jar; do
  [ -f "$j" ] || continue
  name=$(basename "$j")
  case "$name" in quicksilver*.jar|ci-pin.jar|ci-server-harness*.jar) continue;; esac
  sha=$(sha256sum "$j" | cut -c1-64)
  echo "$sha  $set_/$name" >> "$report"
  seen="$seen $name"
  [ -n "$locked" ] || continue
  want=$(echo "$locked" | awk -v n="$name" '$2 == n {print $1}')
  if [ -z "$want" ]; then
    echo "pack changed: $name (not in ci/pack-lock.txt, set $set_)"; bad=1
  elif [ "$want" != "*" ] && [ "$want" != "$sha" ]; then
    echo "pack changed: $name (sha256 $sha, locked $want)"; bad=1
  fi
done
if [ -z "$locked" ]; then
  echo "pack lock: no entries for set $set_ yet; recorded only"
elif [ "$mode" = complete ]; then
  for name in $(echo "$locked" | awk '{print $2}'); do
    case " $seen " in *" $name "*) ;; *) echo "pack changed: $name (locked, missing)"; bad=1;; esac
  done
fi
[ $bad = 0 ] && [ -n "$locked" ] && echo "pack lock: set $set_ matches ci/pack-lock.txt"
exit $bad
