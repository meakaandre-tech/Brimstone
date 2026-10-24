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
  - visibility (package 2): every Brimstone bench must show the visibility module on with no fallback, no section
    layer of vanilla's visible list left undrawn in any measured frame (terrain pop-in), and no hidden entity or block
    entity that a ray from the camera still reaches on screen (entity pop-in; every hidden object is checked);
    scenes with "ciRequiresCulling" must hide some entities and block entities;
  - strict picture: on the pixels where all four vanilla runs (GL 4.5 a and b, GL 3.3, Vulkan) agree exactly, the
    number of pixels where a Brimstone run differs from its own profile's vanilla run at all (threshold 0); over
    ci/strict-pixel-limits.json fails (see docs/ci-limits.md).
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


def load(path):
    return np.asarray(Image.open(path).convert("RGB"), dtype=np.int16) if os.path.exists(path) else None


def strict(shot, out_prefix):
    """Per Brimstone profile: pixels where all vanilla runs agree exactly but the Brimstone run differs at all."""
    v = {r: load(shot(r)) for r in ("vanilla-a", "vanilla-b", "vanilla-old", "vanilla-vk")}
    if any(x is None for x in v.values()) or len({x.shape for x in v.values()}) != 1:
        return None, {}
    agree = np.ones(v["vanilla-a"].shape[:2], bool)
    for x in v.values():
        agree &= (x == v["vanilla-a"]).all(axis=2)
    out = {}
    for prof, (b, vr) in PAIRS.items():
        bs = load(shot(b))
        if bs is None or bs.shape != v[vr].shape:
            out[prof] = None
            continue
        bad = agree & (bs != v[vr]).any(axis=2)
        out[prof] = int(bad.sum())
        if out[prof]:
            Image.fromarray((bad * 255).astype(np.uint8)).save(f"{out_prefix}-{prof}-strict.png")
    return round(float(agree.mean() * 100), 2), out


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
            "visibility": j.get("visibility"),
            "frames": j["fps"]["frames"],
        }
    return out


GPU_ERROR = re.compile(r"OpenGL debug message: [^\n]*type=ERROR|Validation Error|VUID-")


def gpu_errors(text):
    return len(GPU_ERROR.findall(text))


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
    strict_limits = json.load(open("ci/strict-pixel-limits.json")) if os.path.exists("ci/strict-pixel-limits.json") else None
    failures, lines, data = [], [], {}
    for sfile in sorted(glob.glob("src/main/resources/brimstone/scenes/*.json")):
        suite = json.load(open(sfile))
        name = suite["suite"]
        scenes = [s for s in suite["scenes"]]
        sd = data.setdefault(name, {"noise": {}, "picture": {}, "strict": {}, "counts": {}, "skipped": {}})
        strict_lines = []
        lines.append(f"## Suite {name}\n")
        runs = {r: os.path.join(res, f"{name}-{r}") for r in RUNS}
        for r, d in runs.items():
            if not os.path.isdir(d):
                failures.append(f"{name}: run {r} produced no results")
        # vanilla's own profile line, and Brimstone's probe line
        for r, d in runs.items():
            t = logs(d)
            if os.path.isdir(d) and "brimstone_ci_pin" not in t:
                failures.append(f"{name}/{r}: CI's lightmap pin (ci/pin-mod) is not among the loaded mods")
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
                    if re.search(r"Brimstone visibility: turned off", t):
                        failures.append(f"{name}/{r}: the visibility module fell back to vanilla (see client log)")
                    if "Brimstone visibility: on " not in t:
                        failures.append(f"{name}/{r}: the visibility module never turned on")
                    if "DRY RUN (noise collection)" in t:
                        failures.append(f"{name}/{r}: visibility dry run (a noise collection run: hides nothing, never for merge)")
                    nwarn = len(re.findall(r"Brimstone visibility check:", t))
                    if nwarn:
                        failures.append(f"{name}/{r}: pop-in: {nwarn} ray-check warnings in the client log (a hidden object was reachable from the camera)")
                    if re.search(r"Brimstone visibility: deferring", t):
                        failures.append(f"{name}/{r}: the visibility module deferred to vanilla (see client log)")
                    for f in glob.glob(os.path.join(d, "bench", "*.json")):
                        bj = json.load(open(f))
                        vis = bj.get("visibility")
                        scene_def = next((x for x in scenes if x["id"] == bj["scene"]), {})
                        if vis is None:
                            failures.append(f"{name}/{r}: bench {bj['scene']} has no visibility report")
                        else:
                            if vis.get("state") != "on" or vis.get("fallbacks", 0) != 0:
                                failures.append(f"{name}/{r}: bench {bj['scene']}: visibility module {vis.get('state')}, fallbacks {vis.get('fallbacks')} ({vis.get('reason')})")
                            if not vis.get("terrainFrames"):
                                failures.append(f"{name}/{r}: bench {bj['scene']}: the terrain pop-in check did not run")
                            if vis.get("sectionLayersMissing", 0) != 0:
                                failures.append(f"{name}/{r}: bench {bj['scene']}: pop-in: {vis.get('sectionLayersMissing')} section layers of vanilla's visible list were not drawn")
                            if not vis.get("verify"):
                                failures.append(f"{name}/{r}: bench {bj['scene']}: the ray check of hidden objects was not on")
                            if vis.get("violations", 0) != 0:
                                failures.append(f"{name}/{r}: bench {bj['scene']}: pop-in: {vis.get('violations')} hidden entities or block entities were on screen and unoccluded (see client log)")
                            if vis.get("dryRun"):
                                failures.append(f"{name}/{r}: bench {bj['scene']}: visibility dry run (noise collection run, never for merge)")
                            if vis.get("violationsSinceStart", 0) != 0 or vis.get("unverifiedSinceStart", 0) != 0:
                                failures.append(f"{name}/{r}: bench {bj['scene']}: ray check since start-up: {vis.get('violationsSinceStart')} violations, {vis.get('unverifiedSinceStart')} unchecked hidden objects")
                            if vis.get("unverifiedObjects", 0) != 0:
                                failures.append(f"{name}/{r}: bench {bj['scene']}: {vis.get('unverifiedObjects')} hidden objects were not ray-checked (check budget)")
                            if scene_def.get("ciRequiresCulling") and not (vis.get("entitiesCulled", 0) > 0 and vis.get("blockEntitiesCulled", 0) > 0):
                                failures.append(f"{name}/{r}: bench {bj['scene']}: Brimstone hid no entities ({vis.get('entitiesCulled')}) or no block entities ({vis.get('blockEntitiesCulled')}) where walls cover some")
                        ter = bj.get("terrain")
                        if ter is None:
                            failures.append(f"{name}/{r}: bench {bj['scene']} has no terrain report")
                        elif ter.get("state") != "active" or ter.get("fallbacks", 0) != 0:
                            failures.append(f"{name}/{r}: bench {bj['scene']}: terrain module {ter.get('state')}, fallbacks {ter.get('fallbacks')} ({ter.get('reason')})")
                        elif not (ter.get("layersTaken", 0) > 0 and ter.get("sectionLayersPerFrame", 0) > 0 and ter.get("drawCallsPerFrame", 0) > 0):
                            failures.append(f"{name}/{r}: bench {bj['scene']}: Brimstone drew no terrain (layers taken {ter.get('layersTaken')}, section layers/frame {ter.get('sectionLayersPerFrame')}, draws/frame {ter.get('drawCallsPerFrame')})")
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
        # GPU errors from the game's debug output (OpenGL debug messages of type ERROR, Vulkan validation errors):
        # a Brimstone run must not log more than the vanilla run on the same GPU profile
        lines.append("")
        lines.append("GPU error messages in the client logs (Brimstone run / vanilla run of the same profile):\n")
        for prof, (b, v) in PAIRS.items():
            eb, ev = gpu_errors(logs(runs[b])), gpu_errors(logs(runs[v]))
            lines.append(f"- {prof}: {eb} / {ev}")
            if eb > ev:
                failures.append(f"{name}/{b}: {eb} GPU error messages in the client log, {ev} in {v}")
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
            shot_ids = [sid] + [sid + "." + x["name"] for x in sc.get("ciShots", [])]
            for sid in shot_ids:
                shot = lambda r, sid=sid: os.path.join(runs[r], "shots", sid + ".png")
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
                agree, counts = strict(shot, os.path.join(rep, "diff", f"{name}-{sid}"))
                sd["strict"][sid] = {"vanillaAgreePct": agree, "pixels": counts}
                slimit = None if strict_limits is None else strict_limits.get(name, {}).get(sid)
                if agree is None:
                    failures.append(f"{name}/{sid}: strict picture check is missing a vanilla screenshot")
                for prof in PAIRS:
                    c = counts.get(prof)
                    if agree is not None and c is None:
                        failures.append(f"{name}/{sid}: strict picture check is missing the {prof} screenshot")
                    elif c is not None and strict_limits is not None:
                        if slimit is None:
                            failures.append(f"{name}/{sid}: no strict pixel limit in ci/strict-pixel-limits.json")
                        elif c > slimit:
                            failures.append(f"{name}/{sid}: {prof} differs from vanilla on {c} pixels where every vanilla run"
                                            f" agrees exactly (strict limit {slimit})")
                strict_lines.append(f"| {sid} | {agree} | {slimit if strict_limits is not None else 'not set yet'} | "
                                    + " | ".join(str(counts.get(p)) for p in PAIRS) + " |")
        lines.append("")
        lines.append("Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs at all:\n")
        lines.append("| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |")
        lines.append("|---|---|---|---|---|---|")
        lines.extend(strict_lines)
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
            lines.append("| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex (compact/wide; avg) | live vertices | wide % | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |")
            lines.append("|---|---|---|---|---|---|---|---|---|---|---|")
            for sc in scenes:
                c = cur.get(sc["id"])
                if sc.get("skip") or c is None or not c.get("terrain"):
                    continue
                t = c["terrain"]
                files = glob.glob(os.path.join(runs[b], "bench", f"*-{sc['id']}.json"))
                mesh = (json.load(open(files[0])).get("meshing") or {}) if files else {}
                lines.append(f"| {sc['id']} | {t.get('lastDrawPath')} | {t.get('drawCallsPerFrame')} | {t.get('sectionLayersPerFrame')} | {t.get('vertexBytes')}/{t.get('wideVertexBytes')}; {t.get('bytesPerVertex')} (vanilla {t.get('vanillaVertexBytes')}) | {t.get('liveVertices')} | {t.get('widePct')} | {t.get('usedMB')} / {t.get('arenaMB')} | {t.get('vanillaEquivalentMB')} | {t.get('layersTaken')} / {t.get('layersKeptByVanilla')} | {mesh.get('msPerSection')} ({mesh.get('compileMsP95')}) |")
            lines.append("")
            lines.append(f"Visibility, {name} {prof} (reported; pop-in and fallbacks gated above):\n")
            lines.append("| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |")
            lines.append("|---|---|---|---|---|---|---|")
            for sc in scenes:
                c = cur.get(sc["id"])
                if sc.get("skip") or c is None or not c.get("visibility"):
                    continue
                v = c["visibility"]
                lines.append(f"| {sc['id']}{' (dry run)' if v.get('dryRun') else ''} | {v.get('sectionsVisiblePerFrame')} | {v.get('entitiesPerFrame')} ({v.get('entitiesTestedPerFrame')}, {v.get('entitiesCulledPerFrame')}) | {v.get('blockEntitiesPerFrame')} ({v.get('blockEntitiesTestedPerFrame')}, {v.get('blockEntitiesCulledPerFrame')}) | {v.get('cullMsPerFrame')} ({v.get('cullMsMax')}; {v.get('framesOverBudget')}) | {v.get('sectionLayersMissing')} | {v.get('verifiedObjects')} / {v.get('verifiedRays')} / {v.get('violations')} / {v.get('unverifiedObjects')} |")
            lines.append("")
    lines.insert(0, "# Brimstone CI comparison\n\n" + ("**FAILED**\n\n" + "\n".join(f"- {f}" for f in failures) if failures else "**passed**") + "\n")
    open(os.path.join(rep, "report.md"), "w").write("\n".join(lines) + "\n")
    json.dump({"failures": failures, "suites": data}, open(os.path.join(rep, "report.json"), "w"), indent=2)
    print("\n".join(lines))
    sys.exit(1 if failures else 0)


if __name__ == "__main__":
    main()
