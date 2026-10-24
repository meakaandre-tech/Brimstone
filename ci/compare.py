#!/usr/bin/env python3
"""Compares CI client runs and fails the build on a wrong picture, a missing profile or a rise in counts.

usage: compare.py RESULTS_DIR REPORT_DIR

RESULTS_DIR holds one directory per run, named <suite>-<run> (run: vanilla-a, vanilla-b, vanilla-old, brimstone-full,
brimstone-old), as written by ci/client-run.sh. Per suite:
  - noise: vanilla-a against vanilla-b, scene by scene;
  - picture: brimstone-full against vanilla-a, brimstone-old against vanilla-old; over ci/pixel-limits.json fails;
  - probe: Brimstone's capability line must show each profile (full: both extensions; old: neither, GL 3.3);
  - counts (draw calls: per-frame average, as frames that line up with a game tick draw more and the median flips;
    uploads and bytes: per-frame median, as chunk uploads come in bursts; heap after GC; direct buffers): each Brimstone profile's bench results against ci/baselines/<suite>-<profile>.json; a rise over 10%
    fails. The current numbers are written to REPORT_DIR/baselines/ for committing;
  - server: Brimstone runs also load Brimstone on the server; its log must list it and show no Brimstone error.
A diff is the share of pixels whose largest channel difference is over 8 (of 255): small enough to catch a wrong
block or shade, large enough to ignore rounding.
"""
import glob
import json
import os
import re
import sys

import numpy as np
from PIL import Image

THRESHOLD = 8
RISE = 1.10
# absolute allowance on top of the 10%, for values that move in steps (see docs/ci-limits.md)
SLACK = {"drawCallsPerFrame": 2.0, "uploadsPerFrame": 1.0, "uploadBytesPerFrame": 16384.0,
         "heapUsedAfterGcMB": 16.0, "directBuffersMB": 4.0}
GATED = list(SLACK)
PAIRS = {"full": ("brimstone-full", "vanilla-a"), "old-gpu": ("brimstone-old", "vanilla-old"), "vulkan": ("brimstone-vk", "vanilla-vk")}
RUNS = ["vanilla-a", "vanilla-b", "vanilla-old", "vanilla-vk", "brimstone-full", "brimstone-old", "brimstone-vk"]


def diff(a, b, out=None):
    if not (os.path.exists(a) and os.path.exists(b)):
        return None
    x = np.asarray(Image.open(a).convert("RGB"), dtype=np.int16)
    y = np.asarray(Image.open(b).convert("RGB"), dtype=np.int16)
    if x.shape != y.shape:
        return {"pct": 100.0, "mean": 255.0, "note": f"sizes differ {x.shape} {y.shape}"}
    d = np.abs(x - y).max(axis=2)
    if out is not None:
        Image.fromarray(np.clip(d * 8, 0, 255).astype(np.uint8)).save(out)
    return {"pct": round(float((d > THRESHOLD).mean() * 100), 4), "mean": round(float(np.abs(x - y).mean()), 4)}


def bench_results(run_dir):
    out = {}
    for f in sorted(glob.glob(os.path.join(run_dir, "bench", "*.json"))):
        j = json.load(open(f))
        g, m = j["gpu"], j["memory"]
        out[j["scene"]] = {
            "drawCallsPerFrame": g["drawCallsPerFrame"]["avg"],
            "uploadsPerFrame": g["uploadsPerFrame"].get("p50", g["uploadsPerFrame"]["avg"]),
            "uploadBytesPerFrame": g["uploadBytesPerFrame"].get("p50", g["uploadBytesPerFrame"]["avg"]),
            "drawCallsPerFrameAvg": g["drawCallsPerFrame"]["avg"],
            "uploadBytesPerFrameAvg": g["uploadBytesPerFrame"]["avg"],
            "heapUsedAfterGcMB": m["heapUsedAfterGcMB"],
            "directBuffersMB": m.get("directBuffersMB", 0),
            "meshingMsPerSection": (j.get("meshing") or {}).get("msPerSection"),
            "terrain": j.get("terrain"),
            "frames": j["fps"]["frames"],
        }
    return out


def drive_skips(run_dir):
    out = {}
    for f in glob.glob(os.path.join(run_dir, "drive-*.json")):
        for e in json.load(open(f)):
            if e.get("skipped"):
                out[e["scene"]] = e["skipped"]
    return out


def logs(run_dir):
    text = ""
    for f in sorted(glob.glob(os.path.join(run_dir, "client-*.log"))):
        text += open(f, errors="replace").read()
    return text


def main():
    res, rep = sys.argv[1], sys.argv[2]
    os.makedirs(os.path.join(rep, "diff"), exist_ok=True)
    os.makedirs(os.path.join(rep, "baselines"), exist_ok=True)
    limits = json.load(open("ci/pixel-limits.json")) if os.path.exists("ci/pixel-limits.json") else {}
    failures, lines, data = [], [], {}
    for sfile in sorted(glob.glob("src/main/resources/brimstone/scenes/*.json")):
        suite = json.load(open(sfile))
        name = suite["suite"]
        scenes = [s for s in suite["scenes"]]
        sd = data.setdefault(name, {"noise": {}, "picture": {}, "counts": {}, "skipped": {}})
        lines.append(f"## Suite {name}\n")
        runs = {r: os.path.join(res, f"{name}-{r}") for r in RUNS}
        for r, d in runs.items():
            if not os.path.isdir(d):
                failures.append(f"{name}: run {r} produced no results")
        # vanilla's own profile line, and Brimstone's probe line
        for r, d in runs.items():
            t = logs(d)
            m = re.search(r"Using graphics backend (\S+), using drivers: ([^\n]*)", t)
            ext = re.search(r"Using graphics device extensions: ([^\n]*)", t)
            probe = re.search(r"Brimstone capabilities: ([^\n]*)", t)
            lines.append(f"- {r}: backend `{m.group(1) if m else '?'}` drivers `{m.group(2).strip() if m else '?'}`")
            if probe:
                lines.append(f"  - probe: `{probe.group(1).strip()}`")
            old = r.endswith("-old")
            vk = r.endswith("-vk")
            exts = ext.group(1) if ext else ""
            if vk and (not m or m.group(1) != "Vulkan"):
                failures.append(f"{name}/{r}: vanilla is not on the Vulkan backend ({m.group(1) if m else 'no backend line'})")
            if not vk and m and m.group(1) != "OpenGL":
                failures.append(f"{name}/{r}: expected OpenGL, got {m.group(1)}")
            if m and old and not m.group(2).strip().startswith("3.3"):
                failures.append(f"{name}/{r}: GL version is not forced to 3.3 ({m.group(2).strip()})")
            if ext and old and ("GL_ARB_multi_draw_indirect" in exts or "GL_ARB_buffer_storage" in exts):
                failures.append(f"{name}/{r}: vanilla still lists a hidden extension")
            if r.startswith("brimstone"):
                if not probe:
                    failures.append(f"{name}/{r}: no Brimstone capability line")
                else:
                    p = probe.group(1)
                    if vk:
                        if " backend=Vulkan " not in p:
                            failures.append(f"{name}/{r}: probe does not show backend=Vulkan")
                    else:
                        want = "no" if old else "yes"
                        for e in ("GL_ARB_multi_draw_indirect", "GL_ARB_buffer_storage"):
                            if f"{e}={want}" not in p:
                                failures.append(f"{name}/{r}: probe shows {e} not {want}")
                        if old and " gl=3.3 " not in p:
                            failures.append(f"{name}/{r}: probe does not show gl=3.3")
                        if old and "vanilla.mdiKnownIssues=yes" not in p:
                            failures.append(f"{name}/{r}: vanilla's Intel multi-draw-indirect flag is not forced on")
                    # the terrain module must be on in every Brimstone run and must never have fallen back to vanilla
                    if re.search(r"Brimstone terrain: (turned off|not used)", t):
                        failures.append(f"{name}/{r}: the terrain module fell back to vanilla (see client log)")
                    if "Brimstone terrain: on " not in t:
                        failures.append(f"{name}/{r}: the terrain module never turned on")
                    for f in glob.glob(os.path.join(d, "bench", "*.json")):
                        bj = json.load(open(f))
                        ter = bj.get("terrain")
                        if ter is None:
                            failures.append(f"{name}/{r}: bench {bj['scene']} has no terrain report")
                        elif ter.get("state") != "active" or ter.get("fallbacks", 0) != 0:
                            failures.append(f"{name}/{r}: bench {bj['scene']}: terrain module {ter.get('state')}, fallbacks {ter.get('fallbacks')} ({ter.get('reason')})")
                        miss = bj.get("counters", {}).get("missing")
                        if miss is None:
                            failures.append(f"{name}/{r}: bench {bj['scene']} has no counter audit")
                        elif miss:
                            failures.append(f"{name}/{r}: counters that did not apply: {', '.join(miss)}")
                srv = os.path.join(d, "server.log")
                st = open(srv, errors="replace").read() if os.path.exists(srv) else ""
                if not re.search(r"\bbrimstone\b", st):
                    failures.append(f"{name}/{r}: the server did not list Brimstone among its mods")
                if re.search(r"(ERROR|Exception)[^\n]*[Bb]rimstone|[Bb]rimstone[^\n]*(ERROR|Exception)", st):
                    failures.append(f"{name}/{r}: the server logged a Brimstone error")
        lines.append("")
        lines.append("| scene | noise (vanilla a/b) % | limit % | full % | old-GPU % | Vulkan % |")
        lines.append("|---|---|---|---|---|---|")
        skips = [drive_skips(d) for d in runs.values() if os.path.isdir(d)]
        for sc in scenes:
            sid = sc["id"]
            reasons = {s.get(sid) for s in skips}
            if not sc.get("skip") and reasons and None not in reasons:
                sc["skip"] = sorted(reasons)[0]
            elif not sc.get("skip") and len(reasons) > 1:
                failures.append(f"{name}/{sid}: skipped in some runs but not others")
            if sc.get("skip"):
                sd["skipped"][sid] = sc["skip"]
                lines.append(f"| {sid} | skipped: {sc['skip']} | | | | |")
                continue
            shot = lambda r: os.path.join(runs[r], "shots", sid + ".png")
            noise = diff(shot("vanilla-a"), shot("vanilla-b"), os.path.join(rep, "diff", f"{name}-{sid}-noise.png"))
            sd["noise"][sid] = noise
            row = {}
            for prof, (b, v) in PAIRS.items():
                row[prof] = diff(shot(b), shot(v), os.path.join(rep, "diff", f"{name}-{sid}-{prof}.png"))
            sd["picture"][sid] = row
            limit = limits.get(name, {}).get(sid)
            for prof, dv in row.items():
                if dv is None:
                    failures.append(f"{name}/{sid}: no {prof} screenshot pair")
                elif limit is None:
                    failures.append(f"{name}/{sid}: no pixel limit in ci/pixel-limits.json")
                elif dv["pct"] > limit:
                    failures.append(f"{name}/{sid}: {prof} differs from vanilla on {dv['pct']}% of pixels (limit {limit}%)")
            fmt = lambda d: "missing" if d is None else f"{d['pct']}"
            lines.append(f"| {sid} | {fmt(noise)} | {limit} | {fmt(row['full'])} | {fmt(row['old-gpu'])} | {fmt(row['vulkan'])} |")
        lines.append("")
        for prof, (b, _) in PAIRS.items():
            cur = bench_results(runs[b])
            sd["counts"][prof] = cur
            json.dump(cur, open(os.path.join(rep, "baselines", f"{name}-{prof}.json"), "w"), indent=2, sort_keys=True)
            base_file = f"ci/baselines/{name}-{prof}.json"
            base = json.load(open(base_file)) if os.path.exists(base_file) else None
            lines.append(f"### Counts, {name} {prof}\n")
            lines.append("| scene | draw calls/frame (avg) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |")
            lines.append("|---|---|---|---|---|---|")
            for sc in scenes:
                sid = sc["id"]
                if sc.get("skip"):
                    continue
                c = cur.get(sid)
                if c is None:
                    failures.append(f"{name}/{prof}: no bench result for {sid}")
                    continue
                if c["frames"] == 0:
                    failures.append(f"{name}/{prof}: bench for {sid} measured no frames")
                cells = []
                gated = sc.get("ciGatedCounts", GATED)
                for k in ["drawCallsPerFrame", "uploadsPerFrame", "uploadBytesPerFrame", "heapUsedAfterGcMB", "directBuffersMB"]:
                    v = c[k]
                    cell = f"{v}"
                    if base is None:
                        cell += " (no baseline)"
                    elif k not in gated:
                        cell += " (reported, not gated)"
                    elif sid in base:
                        bv = base[sid][k]
                        cell += f" (base {bv})"
                        if v > bv * RISE + SLACK[k]:
                            failures.append(f"{name}/{prof}/{sid}: {k} rose from {bv} to {v} (over 10%)")
                    cells.append(cell)
                lines.append(f"| {sid} | " + " | ".join(cells) + " |")
            if base is None:
                failures.append(f"{name}/{prof}: no committed baseline {base_file}")
            lines.append("")
            lines.append(f"Terrain module, {name} {prof} (reported, not gated):\n")
            lines.append("| scene | path | Brimstone draws/frame | section layers/frame | vertex bytes | live vertices | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |")
            lines.append("|---|---|---|---|---|---|---|---|---|---|")
            for sc in scenes:
                c = cur.get(sc["id"])
                if sc.get("skip") or c is None or not c.get("terrain"):
                    continue
                t = c["terrain"]
                files = glob.glob(os.path.join(runs[b], "bench", f"*-{sc['id']}.json"))
                mesh = (json.load(open(files[0])).get("meshing") or {}) if files else {}
                lines.append(f"| {sc['id']} | {t.get('lastDrawPath')} | {t.get('drawCallsPerFrame')} | {t.get('sectionLayersPerFrame')} | {t.get('vertexBytes')} (vanilla {t.get('vanillaVertexBytes')}) | {t.get('liveVertices')} | {t.get('usedMB')} / {t.get('arenaMB')} | {t.get('vanillaEquivalentMB')} | {t.get('layersTaken')} / {t.get('layersKeptByVanilla')} | {mesh.get('msPerSection')} ({mesh.get('compileMsP95')}) |")
            lines.append("")
    lines.insert(0, "# Brimstone CI comparison\n\n" + ("**FAILED**\n\n" + "\n".join(f"- {f}" for f in failures) if failures else "**passed**") + "\n")
    open(os.path.join(rep, "report.md"), "w").write("\n".join(lines) + "\n")
    json.dump({"failures": failures, "suites": data}, open(os.path.join(rep, "report.json"), "w"), indent=2)
    print("\n".join(lines))
    sys.exit(1 if failures else 0)


if __name__ == "__main__":
    main()
