#!/usr/bin/env python3
"""usage: modrinth-fetch.py MC_VERSION MODS_DIR PROJECT...

Downloads each project's newest Fabric build for MC_VERSION from Modrinth into MODS_DIR, with every dependency that
build marks as required (followed recursively). Exit 1 if any project or required dependency has no such build.
"""
import json
import os
import sys
import time
import urllib.request

API = "https://api.modrinth.com/v2"
# already in every server's mods (ci/server/fetch-server.sh): never fetched twice
SKIP = {"fabric-api", "P7dR8mSH"}


def retry(fn, *args):
    """Up to 5 tries, 5 s apart: a CI download now and then gets its connection reset."""
    for i in range(5):
        try:
            return fn(*args)
        except OSError:
            if i == 4:
                raise
            time.sleep(5)


def get(url):
    def once():
        req = urllib.request.Request(url, headers={"User-Agent": "meakaandre-tech/Quicksilver CI"})
        with urllib.request.urlopen(req, timeout=60) as r:
            return json.load(r)
    return retry(once)


def newest(project, mc):
    versions = [v for v in get("%s/project/%s/version" % (API, project)) if mc in v["game_versions"] and "fabric" in v["loaders"]]
    return versions[0] if versions else None


def main(mc, out, projects):
    os.makedirs(out, exist_ok=True)
    seen, todo, ok = set(), list(projects), True
    while todo:
        p = todo.pop(0)
        if p in seen or p in SKIP:
            continue
        seen.add(p)
        v = newest(p, mc)
        if v is None:
            print("no Fabric build of %s for %s" % (p, mc))
            ok = False
            continue
        f = next((x for x in v["files"] if x.get("primary")), v["files"][0])
        path = os.path.join(out, f["filename"])
        retry(urllib.request.urlretrieve, f["url"], path)
        print("%s %s -> %s" % (p, v["version_number"], f["filename"]))
        for dep in v.get("dependencies", []):
            if dep.get("dependency_type") == "required" and dep.get("project_id"):
                todo.append(dep["project_id"])
    return 0 if ok else 1


if __name__ == "__main__":
    if len(sys.argv) < 4:
        print(__doc__)
        sys.exit(2)
    sys.exit(main(sys.argv[1], sys.argv[2], sys.argv[3:]))
