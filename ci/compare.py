#!/usr/bin/env python3
"""Compares CI client runs and fails the build on a wrong picture, a missing profile or a rise in counts.

usage: compare.py RESULTS_DIR REPORT_DIR

RESULTS_DIR holds one directory per run, named <suite>-<run> (run: vanilla-a, vanilla-b, vanilla-old, quicksilver-full,
quicksilver-old), as written by ci/client-run.sh. Per suite:
  - noise: vanilla-a against vanilla-b, scene by scene;
  - picture: quicksilver-full against vanilla-a, quicksilver-old against vanilla-old; over ci/pixel-limits.json fails;
  - probe: Quicksilver's capability line must show each profile (full: both extensions; old: neither, GL 3.3);
  - counts (draw calls: per-frame average, as frames that line up with a game tick draw more and the median flips;
    a scene with "ciDrawCallGate": "median" is gated on the per-frame median instead, as its average follows the
    runner's frame rate (design doc, Testing: "Counts that follow the runner's speed"); uploads and bytes: per-frame
    median, as chunk uploads come in bursts; heap after GC; direct buffers): each Quicksilver profile's bench results
    against ci/baselines/<suite>-<profile>.json; a rise over 10% fails. The current numbers are written to
    REPORT_DIR/baselines/ for committing; against vanilla measured in the same run (the measure-* runs: Quicksilver
    loaded with every module off), Quicksilver's draw calls per frame (average and median; the median alone for scenes
    marked so) must be at or under vanilla's in the translucent scenes listed in ci/vanilla-draw-calls.json and under
    vanilla's x 1.10 + 2 in the others, and the terrain GPU memory reserved in a Quicksilver run at or under vanilla's in
    every scene (package 3);
  - server: Quicksilver runs also load Quicksilver on the server; its log must list it and show no Quicksilver error.
A diff is the share of pixels whose largest channel difference is over 8 (of 255): small enough to catch a wrong
block or shade, large enough to ignore rounding.
  - visibility (package 2): every Quicksilver bench must show the visibility module on with no fallback, no section
    layer of vanilla's visible list left undrawn in any measured frame (terrain pop-in), and no hidden entity or block
    entity that a ray from the camera still reaches on screen (entity pop-in; every hidden object is checked);
    scenes with "ciRequiresCulling" must hide some entities and block entities;
  - translucency (package 3): every Quicksilver bench must show its per-frame translucency check on with no miss (section
    order, latest sort, back to front), no measured frame where vanilla still drew translucent sections next to
    Quicksilver's, no dry run; scenes with "ciRequiresTranslucent" must have Quicksilver draw translucent sections, scenes
    with "ciRequiresOit" must be drawn with Improved Transparency in every measured frame and the others never;
  - strict picture: on the pixels where all four vanilla runs (GL 4.5 a and b, GL 3.3, Vulkan) agree exactly, the
    number of pixels where a Quicksilver run differs from its own profile's vanilla run by more than STRICT_THRESHOLD
    (2/255 in any channel: Andre's decision, 2026-10-25, as the 16-byte vertex's rounding moves interpolated colours by
    1-2/255; the count at any difference is reported next to it); over
    ci/strict-pixel-limits.json fails (see docs/ci-limits.md); a scene with "ciNoStrict" (a reason) is reported, not
    gated, on this check.
  - animation (package 8): every Quicksilver run must show the animation module on with no fallback and no late redraw
    (a stale animated sprite redrawn only after something may have drawn from it); measure runs must show it off;
    scenes with "ciRequiresAnimationSkip" must have Quicksilver leave out changed sprites and redraw stale ones.
"""
import glob
import json
import os
import re
import sys

import numpy as np
from PIL import Image

THRESHOLD = 8
STRICT_THRESHOLD = 2
RISE = 1.10
# absolute allowance on top of the 10%, for values that move in steps (see docs/ci-limits.md)
SLACK = {"drawCallsPerFrame": 2.0, "uploadsPerFrame": 1.0, "uploadBytesPerFrame": 16384.0,
         "heapUsedAfterGcMB": 16.0, "directBuffersMB": 4.0}
GATED = list(SLACK)
PAIRS = {"full": ("quicksilver-full", "vanilla-a"), "old-gpu": ("quicksilver-old", "vanilla-old"), "vulkan": ("quicksilver-vk", "vanilla-vk")}
VANILLA_RUNS = ("vanilla-a", "vanilla-b", "vanilla-old", "vanilla-vk")
SUITES = None  # every suite
# scenes allowed to opt out of the strict gate ("ciNoStrict"), each by a decision recorded in docs/ci-limits.md
NO_STRICT_ALLOWED = set()  # siftec/map was the only one; strict-gated since package 7
RUNS = ["vanilla-a", "vanilla-b", "vanilla-old", "vanilla-vk", "quicksilver-full", "quicksilver-old", "quicksilver-vk",
        "measure-full", "measure-old", "measure-vk"]
# per profile, the run that measures vanilla's own counts (Quicksilver loaded, every module off; package 3)
MEASURE = {"full": "measure-full", "old-gpu": "measure-old", "vulkan": "measure-vk"}
# Quick mode (CI_MODE=quick, branch pushes; design doc, Quick CI): the core suite only, with vanilla a, vanilla b and
# Quicksilver on the old-GPU profile. Quicksilver's old-GPU run is compared with vanilla a (vanilla's GL 3.3 and GL 4.5
# pictures differ no more than vanilla a and b do), the strict check uses the pixels where vanilla a and b agree, and
# the gates that need a run quick mode leaves out (the measure runs: same-run draw calls, terrain GPU memory, the
# measure runs' own pictures; the other profiles) are skipped and said so in the report. Every limit is the full mode's.
QUICK = os.environ.get("CI_MODE") == "quick"
if QUICK:
    PAIRS = {"old-gpu": ("quicksilver-old", "vanilla-a")}
    VANILLA_RUNS = ("vanilla-a", "vanilla-b")
    RUNS = ["vanilla-a", "vanilla-b", "quicksilver-old"]
    SUITES = {"core"}


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


def strict(shot, out_prefix, skip_runs=(), pairs=None, mask=None):
    """Per Quicksilver profile: pixels where all vanilla runs agree exactly but the Quicksilver run differs by more than
    STRICT_THRESHOLD, and (reported only) by anything at all. Runs a scene skips ("ciSkipRuns", package 3) are left out
    of both the agreement and the counts, and so are a scene's "ciStrictMask" rectangles ([x0, y0, x1, y1], end
    exclusive; MapMod's minimap while MapMod is being finished, Andre 2026-10-30)."""
    v = {r: load(shot(r)) for r in VANILLA_RUNS if r not in skip_runs}
    if any(x is None for x in v.values()) or len({x.shape for x in v.values()}) != 1:
        return None, {}, {}
    agree = np.ones(v["vanilla-a"].shape[:2], bool)
    for x in v.values():
        agree &= (x == v["vanilla-a"]).all(axis=2)
    for x0, y0, x1, y1 in mask or ():
        agree[y0:y1, x0:x1] = False
    out, anyd = {}, {}
    for prof, (b, vr) in (pairs or PAIRS).items():
        if b in skip_runs or vr in skip_runs:
            continue
        bs = load(shot(b))
        if bs is None or bs.shape != v[vr].shape:
            out[prof] = None
            continue
        d = np.abs(bs - v[vr]).max(axis=2)
        bad = agree & (d > STRICT_THRESHOLD)
        out[prof] = int(bad.sum())
        anyd[prof] = int((agree & (d > 0)).sum())
        if out[prof]:
            Image.fromarray((bad * 255).astype(np.uint8)).save(f"{out_prefix}-{prof}-strict.png")
    return round(float(agree.mean() * 100), 2), out, anyd


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
            "drawCallsPerFrameMedian": g["drawCallsPerFrame"].get("p50"),
            "uploadBytesPerFrameAvg": g["uploadBytesPerFrame"]["avg"],
            "heapUsedAfterGcMB": m["heapUsedAfterGcMB"],
            "directBuffersMB": m.get("directBuffersMB", 0),
            "directBuffersStartMB": m.get("directBuffersStartMB"),
            "directBuffersMaxMB": m.get("directBuffersMaxMB"),
            "directBuffersMaxAtTick": m.get("directBuffersMaxAtTick"),
            "benchFile": os.path.basename(f),
            "meshingMsPerSection": (j.get("meshing") or {}).get("msPerSection"),
            # package 4c
            "meshing": j.get("meshing"),
            "mesher": j.get("mesher"),
            "terrain": j.get("terrain"),
            "visibility": j.get("visibility"),
            "translucency": j.get("translucency"),
            "terrainGpu": j.get("terrainGpu"),
            "effects": j.get("effects"),
            "animation": j.get("animation"),
            "gui": j.get("gui"),
            "frames": j["fps"]["frames"],
            "fps": j["fps"].get("average"),
            # package 4b
            "backFaces": j.get("backFaces"),
            "uploadBudget": j.get("uploadBudget"),
            "maxUploadBytes": g["uploadBytesPerFrame"].get("max"),
            "over50ms": j["frameTime"].get("over50ms"),
            "p99ms": j["frameTime"].get("p99ms"),
            "jar": j.get("jar", "quicksilver"),
            "counters": j.get("counters"),
            "mods": (j.get("mods") or {}).get("list", []),
        }
    return out


PROFILE_NAMES = {"full": "full", "old-gpu": "old-GPU", "vulkan": "Vulkan"}
GPU_ERROR = re.compile(r"OpenGL debug message: [^\n]*type=ERROR|Validation Error|VUID-")


def gpu_errors(text):
    return len(GPU_ERROR.findall(text))


def drive_upload_timeouts(run_dir):
    """Package 4b: shots whose wait for Quicksilver's upload queue timed out (ci/drive.py records -1)."""
    out = []
    for f in glob.glob(os.path.join(run_dir, "drive-*.json")):
        for e in json.load(open(f)):
            if -1 in (e.get("uploadWaits") or []):
                out.append(e["scene"])
    return out


def drive_skips(run_dir):
    out = {}
    for f in glob.glob(os.path.join(run_dir, "drive-*.json")):
        for e in json.load(open(f)):
            if e.get("skipped"):
                out[e["scene"]] = e["skipped"]
    return out


def session_options(suite):
    """Each client session's options in client-run.sh's order (client-<n>.log is session n): per render distance and
    set of client options and Quicksilver config entries (quicksilver.<key>=<value>)."""
    rd = {}
    for x in suite["scenes"]:
        o = dict(x.get("clientOptions") or {})
        o.update({"quicksilver." + k: v for k, v in (x.get("quicksilverConfig") or {}).items()})
        opts = ",".join(k + "=" + str(v) for k, v in sorted(o.items()))
        rd.setdefault((x.get("renderDistance", 8), opts), []).append(x["id"])
    return [k[1] for k in sorted(rd)]


def logs(run_dir):
    text = ""
    for f in sorted(glob.glob(os.path.join(run_dir, "client-*.log"))):
        text += open(f, errors="replace").read()
    return text


BENCH_ONLY = ("bench-plain", "bench-sodium")
BENCH_ONLY_SCENES = ("spawn", "flythrough", "water_glass", "nether")


DIRECT_JUMP_MB = 4.0  # a rise this large in one step is reported (phase 1's gate allows 10% + 4 MB on the end value)


def direct_buffers(res, lines):
    """Reported, not gated (Andre, 2026-10-29: open bug, a Quicksilver-only +8 MB step in SIFTEC full from the map
    scene on, run 40803126672 attempt 1): every run's direct-buffer MB per scene, in run order, at the start of
    measuring, its peak (with the tick) and at the end, and each rise of DIRECT_JUMP_MB or more marked with where it
    happened: inside a scene (start to end) or between scenes (the previous scene's end to this one's start)."""
    lines.append("## Direct buffers per scene (reported; open bug, see docs/progress.md)\n")
    lines.append("| run | scene | start MB | peak MB (tick) | end MB | rise |\n|---|---|---|---|---|---|")
    jumps = []
    for d in sorted(glob.glob(os.path.join(res, "*-*"))):
        run = os.path.basename(d)
        b = bench_results(d)
        prev_end = None
        for scene, v in sorted(b.items(), key=lambda kv: kv[1]["benchFile"]):
            st, mx, end = v.get("directBuffersStartMB"), v.get("directBuffersMaxMB"), v.get("directBuffersMB")
            rise = []
            if st is not None and prev_end is not None and st - prev_end >= DIRECT_JUMP_MB:
                rise.append("between scenes +%.2f" % (st - prev_end))
            if st is not None and end is not None and end - st >= DIRECT_JUMP_MB:
                rise.append("inside the scene +%.2f (peak at tick %s)" % (end - st, v.get("directBuffersMaxAtTick")))
            if rise:
                jumps.append("%s/%s: %s" % (run, scene, "; ".join(rise)))
            lines.append("| %s | %s | %s | %s (%s) | %s | %s |" % (run, scene, st, mx, v.get("directBuffersMaxAtTick"), end, "; ".join(rise)))
            prev_end = end
    lines.append("")
    lines.append("Rises of %.0f MB or more: %s\n" % (DIRECT_JUMP_MB, "; ".join(jumps) if jumps else "none"))


def bench_only(res, failures, lines):
    """Package 4b: the bench-only jar (quicksilver-bench) on plain Fabric and next to Sodium (full mode, core suite): it
    loads, benches its scenes with every counter applied, carries no Quicksilver module, and Sodium is really there."""
    if QUICK:
        return
    lines.append("## Bench-only jar (quicksilver-bench)\n")
    lines.append("| run | scene | fps avg | frame time p99 ms | frames over 50 ms | draw calls/frame (median) | largest upload bytes in one frame | counters missing |")
    lines.append("|---|---|---|---|---|---|---|---|")
    for r in BENCH_ONLY:
        d = os.path.join(res, f"core-{r}")
        if not os.path.isdir(d):
            failures.append(f"core/{r}: run produced no results")
            continue
        t = logs(d)
        if "Quicksilver bench-only jar" not in t:
            failures.append(f"core/{r}: the bench-only jar did not start (no 'Quicksilver bench-only jar' line)")
        if re.search(r"Quicksilver (terrain|visibility|animation|renderer|block renderer)[: ]", t):
            failures.append(f"core/{r}: a Quicksilver module logged in a bench-only run")
        got = bench_results(d)
        for sid in BENCH_ONLY_SCENES:
            c = got.get(sid)
            if c is None:
                failures.append(f"core/{r}: no bench result for {sid}")
                continue
            miss = (c.get("counters") or {}).get("missing")
            if c.get("jar") != "quicksilver-bench":
                failures.append(f"core/{r}/{sid}: bench written by {c.get('jar')}, not the bench-only jar")
            if c.get("terrain") is not None or c.get("visibility") is not None or c.get("animation") is not None:
                failures.append(f"core/{r}/{sid}: a Quicksilver module reported in the bench-only jar")
            if miss is None or miss:
                failures.append(f"core/{r}/{sid}: counters that did not apply: {miss}")
            if c.get("frames", 0) == 0:
                failures.append(f"core/{r}/{sid}: measured no frames")
            mods = c.get("mods") or []
            has_sodium = any(m.startswith("sodium@") for m in mods)
            if (r == "bench-sodium") != has_sodium:
                failures.append(f"core/{r}/{sid}: Sodium {'missing' if r == 'bench-sodium' else 'loaded'} (mods: {len(mods)})")
            if any(m.startswith("quicksilver@") for m in mods):
                failures.append(f"core/{r}/{sid}: Quicksilver itself is loaded next to the bench-only jar")
            lines.append(f"| {r} | {sid} | {c.get('fps')} | {c.get('p99ms')} | {c.get('over50ms')} | {c.get('drawCallsPerFrameMedian')} | {c.get('maxUploadBytes')} | {miss} |")
    lines.append("")


SP_IDLE_SHARE = 0.25  # at most this share of the time behind with work waiting and no run meshing


def single_player(res, failures, lines):
    """Package 4c (Andre, 2026-10-30): the single-player run (full mode, core suite): the client on its own integrated
    server, CI's pin mod working the server for a fixed time per tick, the fly-through benched. Server-aware meshing must
    back off while the server is behind (at most one section at a time) and keep a thread meshing (sections meshed while
    behind, little time with work waiting and nothing meshing)."""
    if QUICK:
        return
    lines.append("## Single-player run (server-aware meshing)\n")
    d = os.path.join(res, "core-sp-quicksilver")
    if not os.path.isdir(d):
        failures.append("core/sp-quicksilver: run produced no results")
        return
    t = logs(d)
    if "Quicksilver CI pin: server load" not in t:
        failures.append("core/sp-quicksilver: the server load pin never ran (no 'Quicksilver CI pin: server load' line)")
    b = None
    for f in sorted(glob.glob(os.path.join(d, "bench", "*.json"))):
        j = json.load(open(f))
        if j.get("scene") == "flythrough":
            b = j
    if b is None:
        failures.append("core/sp-quicksilver: no bench result for flythrough")
        return
    sa = (b.get("mesher") or {}).get("serverAware") or {}
    behind_ms = sa.get("benchMsBehind") or 0
    idle_ms = sa.get("benchMsBehindWithNoRunMeshingButWorkWaiting") or 0
    share = round(idle_ms / behind_ms, 3) if behind_ms else None
    lines.append("| state | single player | server tick ms avg (budget) | ticks backed off / ticks | sections meshed while behind | most runs at once while behind | ms behind | of it no run meshing with work waiting | restarts |")
    lines.append("|---|---|---|---|---|---|---|---|---|")
    lines.append(f"| {sa.get('state')} | {sa.get('singlePlayer')} | {sa.get('benchServerTickMsAvg')} ({sa.get('serverTickBudgetMs')}) | "
                 f"{sa.get('benchTicksWithMeshingBackedOff')} / {sa.get('benchServerTicks')} | {sa.get('benchSectionsMeshedWhileBehind')} | "
                 f"{sa.get('benchMostRunsAtOnceWhileBehind')} | {behind_ms} | {idle_ms} ({share}) | {sa.get('loopRestarts')} |")
    lines.append("")
    if sa.get("state") != "on" or not sa.get("singlePlayer"):
        failures.append(f"core/sp-quicksilver: server-aware meshing not on in single player ({sa.get('state')}, single player {sa.get('singlePlayer')})")
    ticks, backed = sa.get("benchServerTicks") or 0, sa.get("benchTicksWithMeshingBackedOff") or 0
    if not backed or backed < ticks / 2:
        failures.append(f"core/sp-quicksilver: the load did not keep the server behind ({backed} of {ticks} ticks with meshing backed off)")
    if not sa.get("benchSectionsMeshedWhileBehind"):
        failures.append("core/sp-quicksilver: no section meshed while the server was behind (meshing stopped instead of backing off)")
    if (sa.get("benchMostRunsAtOnceWhileBehind") or 0) > 1:
        failures.append(f"core/sp-quicksilver: {sa.get('benchMostRunsAtOnceWhileBehind')} sections meshed at once while the server was behind (limit 1)")
    if not behind_ms:
        failures.append("core/sp-quicksilver: no time behind measured in the bench (benchMsBehind 0)")
    if share is not None and share > SP_IDLE_SHARE:
        failures.append(f"core/sp-quicksilver: no thread meshing with work waiting for {share * 100:.0f}% of the time behind (over {SP_IDLE_SHARE * 100:.0f}%)")


def main():
    res, rep = sys.argv[1], sys.argv[2]
    os.makedirs(os.path.join(rep, "diff"), exist_ok=True)
    os.makedirs(os.path.join(rep, "baselines"), exist_ok=True)
    limits = json.load(open("ci/pixel-limits.json")) if os.path.exists("ci/pixel-limits.json") else {}
    strict_limits = json.load(open("ci/strict-pixel-limits.json")) if os.path.exists("ci/strict-pixel-limits.json") else None
    vanilla_draws = json.load(open("ci/vanilla-draw-calls.json")) if os.path.exists("ci/vanilla-draw-calls.json") else {}
    failures, lines, data = [], [], {}
    for sfile in sorted(glob.glob("src/main/resources/quicksilver/scenes/*.json")):
        suite = json.load(open(sfile))
        name = suite["suite"]
        if SUITES is not None and name not in SUITES:
            continue
        scenes = [s for s in suite["scenes"]]
        scene_by_id = {s["id"]: s for s in scenes}
        sd = data.setdefault(name, {"noise": {}, "picture": {}, "strict": {}, "counts": {}, "skipped": {}})
        strict_lines, measure_lines = [], []
        lines.append(f"## Suite {name}\n")
        runs = {r: os.path.join(res, f"{name}-{r}") for r in RUNS}
        for r, d in runs.items():
            if not os.path.isdir(d):
                failures.append(f"{name}: run {r} produced no results")
        # vanilla's own profile line, and Quicksilver's probe line
        for r, d in runs.items():
            for sid in drive_upload_timeouts(d):
                failures.append(f"{name}/{r}/{sid}: a screenshot was taken while Quicksilver's terrain uploads were still waiting (the wait timed out)")
            t = logs(d)
            # phase 2, P2-1: the Memory module fully on in every Quicksilver run (its integrated server logs the status
            # line), off in the measure runs (vanilla's stand-in)
            if os.path.isdir(d) and r.startswith("quicksilver"):
                mem = re.findall(r"Quicksilver memory: (.*)", t)
                if not mem:
                    failures.append(f"{name}/{r}: no Quicksilver memory status line in the client log")
                elif any(re.search(r"=(off|fallback)|^off", x) for x in mem):
                    failures.append(f"{name}/{r}: the Memory module was not fully on: {[x for x in mem if re.search(r'=(off|fallback)|^off', x)][0][:200]}")
            if os.path.isdir(d) and r.startswith("measure") and "Quicksilver memory: off, quicksilver.ci.measureOnly is set" not in t:
                failures.append(f"{name}/{r}: the Memory module did not stay off for measuring (no measure-only line in the client log)")
            if os.path.isdir(d) and "quicksilver_ci_pin" not in t:
                failures.append(f"{name}/{r}: CI's pin mod (ci/pin-mod: lightmap flicker, texture animation, feature batch order, no crosshair outline, no particles in the Sift, the particle and animation areas, banner sway) is not among the loaded mods")
            # the animation scenes test animation only if the pin really ran its fixed number of texture ticks there
            # (fix-animation-flake: a pin mixin that did not apply left them on frame 0 and still passed)
            if os.path.isdir(d) and any(s["id"] == "animation" and not s.get("skip") for s in scenes) \
                    and "texture ticks of animation done" not in t:
                failures.append(f"{name}/{r}: the animation pin never ran its texture ticks (no 'texture ticks of animation done' line in the client log)")
            m = re.search(r"Using graphics backend (\S+), using drivers: ([^\n]*)", t)
            ext = re.search(r"Using graphics device extensions: ([^\n]*)", t)
            probe = re.search(r"Quicksilver capabilities: ([^\n]*)", t)
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
            if r.startswith("vanilla") and os.path.isdir(d) and "[Indigo] Registering Indigo renderer" not in t:
                failures.append(f"{name}/{r}: the vanilla run did not draw with Fabric's Indigo renderer")
            if r.startswith(("quicksilver", "measure")):
                if not probe:
                    failures.append(f"{name}/{r}: no Quicksilver capability line")
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
                    # package 4: Quicksilver's Fabric Renderer API implementation draws in every Quicksilver run, never falling
                    # back; measure runs stand in for vanilla, so Indigo draws there as in the vanilla runs
                    # checked per client session (each is its own game start); a session whose scenes switch Quicksilver's
                    # block renderer off must start with it off and draw (its pictures are gated like every scene's)
                    sess_opts = session_options(suite)
                    for f in sorted(glob.glob(os.path.join(d, "client-*.log"))):
                        st, sess = open(f, errors="replace").read(), os.path.basename(f)
                        m_n = re.fullmatch(r"client-(\d+)\.log", sess)
                        if not m_n:
                            continue  # client-gradle-<n>.log: the same session's Gradle output
                        off = bool(m_n) and int(m_n.group(1)) <= len(sess_opts) and "quicksilver.block-renderer=off" in sess_opts[int(m_n.group(1)) - 1]
                        if not r.startswith("measure"):
                            if off and "Quicksilver block renderer: off" not in st:
                                failures.append(f"{name}/{r}/{sess}: the session with block-renderer=off did not start with Quicksilver's block renderer off")
                            if not off and "Quicksilver block renderer: off" in st:
                                failures.append(f"{name}/{r}/{sess}: Quicksilver's block renderer was off in a session that does not switch it off")
                        if r.startswith("measure"):
                            if "[Indigo] Registering Indigo renderer" not in st:
                                failures.append(f"{name}/{r}/{sess}: the measure run did not draw with Fabric's Indigo renderer")
                            if "Quicksilver renderer: off, quicksilver.ci.measureOnly is set" not in st or "Fabric Renderer API implementation registered" in st:
                                failures.append(f"{name}/{r}/{sess}: Quicksilver's Fabric Renderer API implementation did not stay off for measuring")
                        else:
                            if "Fabric Renderer API implementation registered" not in st:
                                failures.append(f"{name}/{r}/{sess}: Quicksilver's Fabric Renderer API implementation was not registered")
                            if "[Indigo] Registering Indigo renderer" in st:
                                failures.append(f"{name}/{r}/{sess}: Indigo registered as the renderer next to Quicksilver")
                            # phase 2, P2-1: the Memory module (both sides) never falls back
                            nmem = len(re.findall(r"Quicksilver memory fallback:", st))
                            if nmem:
                                failures.append(f"{name}/{r}/{sess}: the Memory module fell back to vanilla's code {nmem} times (see client log)")
                            nwarn = len(re.findall(r"Quicksilver renderer fallback:", st))
                            if nwarn:
                                failures.append(f"{name}/{r}/{sess}: the Fabric Renderer API implementation fell back to vanilla's code {nwarn} times (see client log)")
                            want_test = "Quicksilver renderer self-test: not run (block renderer off)" if off else "Quicksilver renderer self-test: passed"
                            if want_test not in st or "Quicksilver renderer self-test FAILED" in st:
                                failures.append(f"{name}/{r}/{sess}: the renderer self-test did not pass (see client log)")
                    if r.startswith("measure"):
                        # a measure run stands in for vanilla only while every Quicksilver module is off in it (package 3)
                        for mod in ("terrain", "visibility", "animation"):
                            if f"Quicksilver {mod}: off, quicksilver.ci.measureOnly is set" not in t:
                                failures.append(f"{name}/{r}: the {mod} module did not stay off for measuring (no measure-only line in the client log)")
                        if "Quicksilver terrain: on " in t or "Quicksilver visibility: on " in t or "Quicksilver animation: on " in t:
                            failures.append(f"{name}/{r}: a Quicksilver module turned on in a measure run")
                        for f in glob.glob(os.path.join(d, "bench", "*.json")):
                            bj = json.load(open(f))
                            ter, vis, tg = bj.get("terrain") or {}, bj.get("visibility") or {}, bj.get("terrainGpu") or {}
                            if ter.get("state") != "off" or ter.get("reason") != "measure only (CI)" or vis.get("state") != "off":
                                failures.append(f"{name}/{r}: bench {bj['scene']}: modules not off for measuring (terrain {ter.get('state')} ({ter.get('reason')}), visibility {vis.get('state')})")
                            if tg.get("quicksilverArenasReservedMB") != 0 or ter.get("layersTaken", 0) != 0:
                                failures.append(f"{name}/{r}: bench {bj['scene']}: Quicksilver held terrain in a measure run ({tg.get('quicksilverArenasReservedMB')} MB, {ter.get('layersTaken')} layers)")
                            # package 4c: vanilla's section compiler meshes every section in a measure run
                            me = bj.get("mesher") or {}
                            if me.get("state") != "off" or me.get("reason") != "measure only (CI)" or me.get("sectionsMeshed", 0) != 0:
                                failures.append(f"{name}/{r}: bench {bj['scene']}: Quicksilver's mesher not off for measuring ({me.get('state')} ({me.get('reason')}), {me.get('sectionsMeshed')} sections meshed)")
                            an = bj.get("animation") or {}
                            if an.get("state") != "off" or an.get("reason") != "measure only (CI)" or an.get("skippedSinceStart", 0) != 0:
                                failures.append(f"{name}/{r}: bench {bj['scene']}: animation module not off for measuring ({an.get('state')} ({an.get('reason')}), left out {an.get('skippedSinceStart')})")
                    else:
                        # the terrain module must be on in every Quicksilver run and must never have fallen back to vanilla
                        if re.search(r"Quicksilver terrain: (turned off|not used)", t):
                            failures.append(f"{name}/{r}: the terrain module fell back to vanilla (see client log)")
                        if "Quicksilver terrain: on " not in t:
                            failures.append(f"{name}/{r}: the terrain module never turned on")
                        if re.search(r"Quicksilver visibility: turned off", t):
                            failures.append(f"{name}/{r}: the visibility module fell back to vanilla (see client log)")
                        if "Quicksilver visibility: on " not in t:
                            failures.append(f"{name}/{r}: the visibility module never turned on")
                        if "DRY RUN (noise collection)" in t:
                            failures.append(f"{name}/{r}: visibility dry run (a noise collection run: hides nothing, never for merge)")
                        nwarn = len(re.findall(r"Quicksilver visibility check:", t))
                        if nwarn:
                            failures.append(f"{name}/{r}: pop-in: {nwarn} ray-check warnings in the client log (a hidden object was reachable from the camera)")
                        nwarn = len(re.findall(r"Quicksilver translucency check:", t))
                        if nwarn:
                            failures.append(f"{name}/{r}: translucency: {nwarn} per-frame check misses in the client log (Quicksilver's translucent draws differed from vanilla's order or latest sort)")
                        if re.search(r"Quicksilver visibility: deferring", t):
                            failures.append(f"{name}/{r}: the visibility module deferred to vanilla (see client log)")
                        if re.search(r"Quicksilver animation: turned off", t):
                            failures.append(f"{name}/{r}: the animation module fell back to vanilla (see client log)")
                        if "Quicksilver animation: on " not in t:
                            failures.append(f"{name}/{r}: the animation module never turned on")
                        # package 4c: Quicksilver's own mesher on, never fallen back, and its check of every section meshed
                        # outside a bench against vanilla's compiler without a single difference
                        if re.search(r"Quicksilver mesher: (turned off|off,|off for the session)", t):
                            failures.append(f"{name}/{r}: Quicksilver's mesher fell back to vanilla's section compiler or stayed off (see client log)")
                        if "Quicksilver mesher: on " not in t:
                            failures.append(f"{name}/{r}: Quicksilver's mesher never turned on")
                        if "Quicksilver mesher: fluid self-test:" not in t:
                            failures.append(f"{name}/{r}: the mesher's fluid self-test did not run (see client log)")
                        nwarn = len(re.findall(r"Quicksilver mesher check:", t))
                        if nwarn:
                            failures.append(f"{name}/{r}: mesher check: {nwarn} sections where Quicksilver's mesh differed from vanilla's compiler's (see client log)")
                        checked = kept = 0
                        for f in glob.glob(os.path.join(d, "bench", "*.json")):
                            me = json.load(open(f)).get("mesher") or {}
                            checked = max(checked, me.get("checked", 0))
                            kept = max(kept, me.get("biomeLookupsKeptSinceStart", 0))
                            # Andre, 2026-10-30: like-for-like (same sections, same thread, both orders), Quicksilver's median
                            # ms per section at or under vanilla's compiler's, in every scene; 2026-10-31: in flat-lit sessions
                            # (smooth lighting off) at most vanilla's + 10% (docs/ci-limits.md, "Package 4c: like-for-like
                            # timing", for the reason and the numbers)
                            mq, mv = me.get("checkedMedianQuicksilverMs"), me.get("checkedMedianVanillaCompilerMs")
                            sid = json.load(open(f))["scene"]
                            flat = str(((scene_by_id.get(sid) or {}).get("clientOptions") or {}).get("ao", "")).lower() == "false"
                            allowed = round(mv * (1.10 if flat else 1.0), 3) if mv is not None else None
                            if me.get("checkedPairs") and mq is not None and mv is not None and mq > allowed:
                                failures.append(f"{name}/{r}: bench {sid}: Quicksilver's median meshing time {mq} ms per section is over"
                                                f" {'vanilla + 10% (flat-lit) ' if flat else ''}{allowed} ms (vanilla's compiler {mv} ms; same sections,"
                                                f" same thread; {me.get('checkedPairs')} pairs)")
                            # a scene whose session meshes few sections reloads its chunks until the median has enough pairs
                            # (Andre, 2026-10-31: factory_flat, 500+), and is held to it
                            want = (scene_by_id.get(sid) or {}).get("ciMinMeshPairs")
                            if want and (me.get("checkedPairs") or 0) < want:
                                failures.append(f"{name}/{r}: bench {sid}: {me.get('checkedPairs')} like-for-like pairs, the scene asks for {want}")
                            if me.get("state") != "on" or me.get("fallbacks", 0) != 0 or me.get("mismatched", 0) != 0 or me.get("check") != "on":
                                failures.append(f"{name}/{r}: bench {json.load(open(f))['scene']}: mesher {me.get('state')} ({me.get('reason')}), fallbacks {me.get('fallbacks')}, mismatched {me.get('mismatched')}, check {me.get('check')}")
                        if os.path.isdir(os.path.join(d, "bench")) and checked == 0:
                            failures.append(f"{name}/{r}: the mesher check compared no section with vanilla's compiler")
                        if os.path.isdir(os.path.join(d, "bench")) and kept == 0:
                            failures.append(f"{name}/{r}: the mesher's biome lookups for tints were never kept (the hook into vanilla's blend did not apply)")
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
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: Quicksilver hid no entities ({vis.get('entitiesCulled')}) or no block entities ({vis.get('blockEntitiesCulled')}) where walls cover some")
                            # package 4: which path the blocks and API mesh quads took since the session started; with
                            # block-renderer=off none may go Quicksilver's way, else none to vanilla's block renderer
                            rs = bj.get("renderer")
                            off_scene = (scene_def.get("quicksilverConfig") or {}).get("block-renderer") == "off"
                            if rs is None:
                                failures.append(f"{name}/{r}: bench {bj['scene']} has no renderer report")
                            elif off_scene and not (rs.get("blockRendererOff") and rs.get("blocksQuicksilver") == 0 and rs.get("meshQuadsQuicksilver") == 0 and rs.get("blocksVanilla", 0) > 0):
                                failures.append(f"{name}/{r}: bench {bj['scene']}: with block-renderer=off, blocks Quicksilver/vanilla {rs.get('blocksQuicksilver')}/{rs.get('blocksVanilla')}, mesh quads by Quicksilver {rs.get('meshQuadsQuicksilver')} (want 0/more than 0, 0)")
                            elif not off_scene and (rs.get("blockRendererOff") or rs.get("blocksVanilla") != 0 or rs.get("blocksQuicksilver", 0) == 0):
                                failures.append(f"{name}/{r}: bench {bj['scene']}: blocks Quicksilver/vanilla {rs.get('blocksQuicksilver')}/{rs.get('blocksVanilla')} (want more than 0/0), block renderer off: {rs.get('blockRendererOff')}")
                            tr = bj.get("translucency")
                            if tr is None:
                                failures.append(f"{name}/{r}: bench {bj['scene']} has no translucency report")
                            else:
                                if tr.get("dryRun"):
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: translucency dry run (noise collection run, never for merge)")
                                if not tr.get("verify"):
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: the per-frame translucency check was not on")
                                if tr.get("violations", 0) != 0 or tr.get("violationsSinceStart", 0) != 0:
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: translucency check: {tr.get('violations')} misses while measuring, {tr.get('violationsSinceStart')} since start-up")
                                if tr.get("mixedFrames", 0) != 0:
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: {tr.get('mixedFrames')} measured frames drew translucent sections of vanilla's own next to Quicksilver's")
                                if scene_def.get("ciRequiresTranslucent") and not tr.get("dryRun") and not (tr.get("sectionLayersPerFrame", 0) > 0 and tr.get("entriesChecked", 0) > 0):
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: Quicksilver drew no translucent sections (section layers/frame {tr.get('sectionLayersPerFrame')}, checked {tr.get('entriesChecked')})")
                                frames_measured = tr.get("framesMeasured", 0)
                                if scene_def.get("ciRequiresOit") and not tr.get("dryRun") and tr.get("oitFrames", 0) < frames_measured:
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: Improved Transparency was not on in every measured frame ({tr.get('oitFrames')} stage draws for {frames_measured} frames)")
                                if not scene_def.get("ciRequiresOit") and tr.get("oitFrames", 0) != 0:
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: drawn with Improved Transparency, which this scene does not set")
                            ter = bj.get("terrain")
                            if ter is None:
                                failures.append(f"{name}/{r}: bench {bj['scene']} has no terrain report")
                            elif ter.get("state") != "active" or ter.get("fallbacks", 0) != 0:
                                failures.append(f"{name}/{r}: bench {bj['scene']}: terrain module {ter.get('state')}, fallbacks {ter.get('fallbacks')} ({ter.get('reason')})")
                            elif ter.get("alphaLayersKeptByVanilla", 0) or ter.get("translucentHandedBackForAlpha"):
                                failures.append(f"{name}/{r}: bench {bj['scene']}: {ter.get('alphaLayersKeptByVanilla')} terrain layers with a vertex alpha below 255 (Quicksilver's vertex has none; translucent handed back: {ter.get('translucentHandedBackForAlpha')})")
                            elif not (ter.get("layersTaken", 0) > 0 and ter.get("sectionLayersPerFrame", 0) > 0 and ter.get("drawCallsPerFrame", 0) > 0):
                                failures.append(f"{name}/{r}: bench {bj['scene']}: Quicksilver drew no terrain (layers taken {ter.get('layersTaken')}, section layers/frame {ter.get('sectionLayersPerFrame')}, draws/frame {ter.get('drawCallsPerFrame')})")
                            # package 4b: facing groups that face away from the camera are left out in every scene (fewer triangles
                            # than drawing every layer whole), and terrain uploads go through the budget
                            bf, ub = bj.get("backFaces"), bj.get("uploadBudget")
                            if bf is None or ub is None:
                                failures.append(f"{name}/{r}: bench {bj['scene']} has no backFaces or uploadBudget report")
                            else:
                                if bf.get("state") != "on":
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: back faces drawn ({bf.get('reason')})")
                                elif not (bf.get("trianglesPerFrame", 0) < bf.get("trianglesAllFacesPerFrame", 0)):
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: triangles per frame not down ({bf.get('trianglesPerFrame')} of {bf.get('trianglesAllFacesPerFrame')} with every face)")
                                if ub.get("state") != "on" or not ub.get("cameraKnown"):
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: upload budget {ub.get('state')}, camera known {ub.get('cameraKnown')}")
                                # a visible section's mesh waits a bounded number of frames under the budget (review S4)
                                if ub.get("visibleWaitFramesMax", 0) > ub.get("maxVisibleWaitFramesAllowed", 0):
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: a visible section's mesh waited {ub.get('visibleWaitFramesMax')} frames (at most {ub.get('maxVisibleWaitFramesAllowed')})")
                            an = bj.get("animation")
                            if an is None:
                                failures.append(f"{name}/{r}: bench {bj['scene']} has no animation report")
                            else:
                                if an.get("state") != "on" or an.get("fallbacks", 0) != 0:
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: animation module {an.get('state')}, fallbacks {an.get('fallbacks')} ({an.get('reason')})")
                                if an.get("late", 0) != 0 or an.get("lateSinceStart", 0) != 0:
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: {an.get('late')} stale animated sprites redrawn late while measuring, {an.get('lateSinceStart')} since start-up (something may have drawn from them first)")
                                # counted inside this scene's measured window only (the scene's steps make each path run there)
                                if scene_def.get("ciRequiresAnimationSkip") and not (an.get("skipped", 0) > 0 and an.get("catchUpsBeforeTerrain", 0) > 0 and an.get("catchUpsOnFirstUse", 0) > 0):
                                    failures.append(f"{name}/{r}: bench {bj['scene']}: Quicksilver did not leave out and redraw animated sprites while measuring here (left out {an.get('skipped')}, redrawn before terrain {an.get('catchUpsBeforeTerrain')}, on first use {an.get('catchUpsOnFirstUse')})")
                            miss = bj.get("counters", {}).get("missing")
                            if miss is None:
                                failures.append(f"{name}/{r}: bench {bj['scene']} has no counter audit")
                            elif miss:
                                failures.append(f"{name}/{r}: counters that did not apply: {', '.join(miss)}")
                srv = os.path.join(d, "server.log")
                st = open(srv, errors="replace").read() if os.path.exists(srv) else ""
                if not re.search(r"\bquicksilver\b", st):
                    failures.append(f"{name}/{r}: the server did not list Quicksilver among its mods")
                if re.search(r"(ERROR|Exception)[^\n]*[Bb]rimstone|[Bb]rimstone[^\n]*(ERROR|Exception)", st):
                    failures.append(f"{name}/{r}: the server logged a Quicksilver error")
        # GPU errors from the game's debug output (OpenGL debug messages of type ERROR, Vulkan validation errors):
        # a Quicksilver run must not log more than the vanilla run on the same GPU profile
        lines.append("")
        lines.append("GPU error messages in the client logs (Quicksilver run / vanilla run of the same profile):\n")
        for prof, (b, v) in PAIRS.items():
            eb, ev = gpu_errors(logs(runs[b])), gpu_errors(logs(runs[v]))
            lines.append(f"- {prof}: {eb} / {ev}")
            if eb > ev:
                failures.append(f"{name}/{b}: {eb} GPU error messages in the client log, {ev} in {v}")
        lines.append("")
        if QUICK:
            lines.append("**Quick mode** (core suite; vanilla a, vanilla b, Quicksilver old-GPU against vanilla a): the measure-run"
                         " gates (same-run draw calls, terrain GPU memory, measure pictures) and the full and Vulkan profiles are"
                         " not run. A merge needs a full run.\n")
        lines.append("| scene | noise (vanilla a/b) % | limit % | " + " | ".join(PROFILE_NAMES[p] + " %" for p in PAIRS) + " |")
        lines.append("|---|---|---|" + "---|" * len(PAIRS))
        skips = [drive_skips(d) for d in runs.values() if os.path.isdir(d)]
        for sc in scenes:
            sid = sc["id"]
            # runs a scene cannot run in (package 3, "ciSkipRuns": a driver crash that hits vanilla too) are expected to skip it
            skip_runs = set(sc.get("ciSkipRuns", []))
            sd.setdefault("skippedRuns", {})[sid] = sorted(skip_runs) if skip_runs else None
            reasons = {s.get(sid) for r, s in zip([r for r, d in runs.items() if os.path.isdir(d)], skips) if r not in skip_runs}
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
                skipped_profiles = {prof for prof, (b, v) in PAIRS.items() if b in skip_runs or v in skip_runs}
                for prof, (b, v) in PAIRS.items():
                    row[prof] = None if prof in skipped_profiles else diff(shot(b), shot(v), os.path.join(rep, "diff", f"{name}-{sid}-{prof}.png"))
                sd["picture"][sid] = row
                limit = limits.get(name, {}).get(sid)
                for prof, dv in row.items():
                    if prof in skipped_profiles:
                        continue
                    if dv is None:
                        failures.append(f"{name}/{sid}: no {prof} screenshot pair")
                    elif limit is None:
                        failures.append(f"{name}/{sid}: no pixel limit in ci/pixel-limits.json")
                    elif dv["pct"] > limit:
                        failures.append(f"{name}/{sid}: {prof} differs from vanilla on {dv['pct']}% of pixels (limit {limit}%)")
                fmt = lambda d, p=None: f"skipped ({sc.get('ciSkipReason', 'ciSkipRuns')})" if p in skipped_profiles else ("missing" if d is None else f"{d['pct']}")
                lines.append(f"| {sid} | {fmt(noise)} | {limit} | " + " | ".join(fmt(row[p], p) for p in PAIRS) + " |")
                agree, counts, counts_any = strict(shot, os.path.join(rep, "diff", f"{name}-{sid}"), skip_runs, mask=sc.get("ciStrictMask"))
                sd["strict"][sid] = {"vanillaAgreePct": agree, "pixels": counts, "pixelsAnyDifference": counts_any}
                slimit = None if strict_limits is None else strict_limits.get(name, {}).get(sid)
                if agree is None:
                    failures.append(f"{name}/{sid}: strict picture check is missing a vanilla screenshot")
                for prof in PAIRS:
                    if prof in skipped_profiles:
                        continue
                    c = counts.get(prof)
                    if agree is not None and c is None:
                        failures.append(f"{name}/{sid}: strict picture check is missing the {prof} screenshot")
                    elif c is not None and strict_limits is not None:
                        if sc.get("ciNoStrict") and f"{name}/{sid}" in NO_STRICT_ALLOWED:
                            # no strict gate for this scene (its own noise is larger than anything it could show; the
                            # 8/255 limit above still applies); the counts are reported
                            continue
                        if slimit is None:
                            failures.append(f"{name}/{sid}: no strict pixel limit in ci/strict-pixel-limits.json")
                        elif c > slimit:
                            failures.append(f"{name}/{sid}: {prof} differs from vanilla on {c} pixels where every vanilla run"
                                            f" agrees exactly, by more than {STRICT_THRESHOLD}/255 (strict limit {slimit})")
                # the measure runs (Quicksilver loaded, every module off) must look like vanilla within vanilla's own
                # noise, the same 8/255 and strict limits as Quicksilver's runs (package 3): they stand in for vanilla
                # in the draw-call and terrain memory gates
                mpairs = {} if QUICK else {prof: (MEASURE[prof], v) for prof, (b, v) in PAIRS.items() if prof not in skipped_profiles}
                mrow = {prof: diff(shot(mb), shot(v), os.path.join(rep, "diff", f"{name}-{sid}-{prof}-measure.png")) for prof, (mb, v) in mpairs.items()}
                _, mcounts, _ = strict(shot, os.path.join(rep, "diff", f"{name}-{sid}-measure"), skip_runs, mpairs, mask=sc.get("ciStrictMask")) if mpairs else (None, {}, {})
                no_strict = sc.get("ciNoStrict") and f"{name}/{sid}" in NO_STRICT_ALLOWED
                for prof, dv in mrow.items():
                    if dv is None:
                        failures.append(f"{name}/{sid}: no {MEASURE[prof]} screenshot")
                        continue
                    if limit is not None and dv["pct"] > limit:
                        failures.append(f"{name}/{sid}: {MEASURE[prof]} (vanilla stand-in) differs from vanilla on {dv['pct']}% of pixels (limit {limit}%)")
                    mc = mcounts.get(prof)
                    if not no_strict and mc is None:
                        failures.append(f"{name}/{sid}: strict picture check is missing the {MEASURE[prof]} screenshot (or its size differs)")
                    elif not no_strict and slimit is not None and mc > slimit:
                        failures.append(f"{name}/{sid}: {MEASURE[prof]} (vanilla stand-in) differs from vanilla on {mc} strict pixels (limit {slimit})")
                measure_lines.append(f"| {sid} | " + " | ".join("skipped" if p not in mrow else ("missing" if mrow[p] is None else f"{mrow[p]['pct']} / {mcounts.get(p)}") for p in PAIRS) + " |")
                shown = f"not gated ({sc['ciNoStrict']})" if sc.get("ciNoStrict") and f"{name}/{sid}" in NO_STRICT_ALLOWED else (slimit if strict_limits is not None else 'not set yet')
                strict_lines.append(f"| {sid} | {agree} | {shown} | "
                                    + " | ".join(f"{counts.get(p)} ({counts_any.get(p)})" for p in PAIRS) + " |")
        lines.append("")
        lines.append(f"Strict picture: pixels where {"all four" if len(VANILLA_RUNS) == 4 else "both"} vanilla runs agree exactly but the Quicksilver run differs by more than"
                     f" {STRICT_THRESHOLD}/255 (in brackets: differs at all, reported only):\n")
        lines.append("| scene | vanilla runs agree on % of pixels | strict limit (pixels) | " + " | ".join(PROFILE_NAMES[p] for p in PAIRS) + " |")
        lines.append("|---|---|---|" + "---|" * len(PAIRS))
        lines.extend(strict_lines)
        lines.append("")
        if not QUICK:
            lines.append("Measure runs (Quicksilver loaded, every module off) against vanilla's run of the same profile: % of pixels over"
                         f" 8/255 / strict pixels over {STRICT_THRESHOLD}/255; the same limits as Quicksilver's runs:\n")
            lines.append("| scene | full | old-GPU | Vulkan |")
            lines.append("|---|---|---|---|")
            lines.extend(measure_lines)
            lines.append("")
        for prof, (b, _) in PAIRS.items():
            cur = bench_results(runs[b])
            # scenes whose draw-call average follows the runner's frame rate are gated (and baselined) on the median
            median_gated = {sc["id"] for sc in scenes if sc.get("ciDrawCallGate") == "median"}
            for sid in median_gated:
                if sid in cur:
                    cur[sid]["drawCallsPerFrame"] = cur[sid]["drawCallsPerFrameMedian"]
            sd["counts"][prof] = cur
            json.dump(cur, open(os.path.join(rep, "baselines", f"{name}-{prof}.json"), "w"), indent=2, sort_keys=True)
            base_file = f"ci/baselines/{name}-{prof}.json"
            base = json.load(open(base_file)) if os.path.exists(base_file) else None
            lines.append(f"### Counts, {name} {prof}\n")
            lines.append("| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |")
            lines.append("|---|---|---|---|---|---|")
            for sc in scenes:
                sid = sc["id"]
                if sc.get("skip"):
                    continue
                if b in set(sc.get("ciSkipRuns", [])):
                    lines.append(f"| {sid} | skipped ({sc.get('ciSkipReason', 'ciSkipRuns')}) | | | | |")
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
                    if k == "drawCallsPerFrame" and sid in median_gated:
                        cell += f" median (avg {c['drawCallsPerFrameAvg']})"
                    if base is None:
                        cell += " (no baseline)"
                    elif k not in gated:
                        cell += " (reported, not gated)"
                    elif sid in base:
                        bv = base[sid][k]
                        cell += f" (base {bv})"
                        if v > bv * RISE + SLACK[k]:
                            failures.append(f"{name}/{prof}/{sid}: {k}{' (median)' if k == 'drawCallsPerFrame' and sid in median_gated else ''} rose from {bv} to {v} (over 10%)")
                    cells.append(cell)
                if base is not None and sid not in base:
                    # package 4: a scene with no committed baseline would go ungated without a word
                    failures.append(f"{name}/{prof}/{sid}: no committed baseline entry in {base_file}")
                lines.append(f"| {sid} | " + " | ".join(cells) + " |")
            # package 3 (Andre, 2026-10-25), against vanilla measured in the same run (MEASURE): in the translucent
            # scenes (ci/vanilla-draw-calls.json) Quicksilver's median draw calls per frame at most vanilla's median; in
            # every scene the terrain GPU memory reserved (Quicksilver's arenas and whatever vanilla's own heaps still
            # hold) at most what vanilla's heaps reserve
            if not QUICK:
                van = bench_results(runs[MEASURE[prof]])
                lines.append("")
                lines.append(f"Draw calls against vanilla measured in the same run ({MEASURE[prof]}), {name} {prof}: average and median per frame;"
                             " translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:\n")
                lines.append("| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |")
                lines.append("|---|---|---|---|")
                translucent = set(vanilla_draws.get(name, []))
                # a per-scene allowance on top of vanilla's (Andre's decisions only, each explained in the file)
                allowance = vanilla_draws.get("allowance", {})
                for sc in scenes:
                    sid = sc["id"]
                    c, vc = cur.get(sid), van.get(sid)
                    if sc.get("skip") or c is None or b in set(sc.get("ciSkipRuns", [])):
                        continue
                    if vc is None or vc.get("drawCallsPerFrameMedian") is None:
                        failures.append(f"{name}/{prof}/{sid}: no vanilla measurement in {MEASURE[prof]}")
                        continue
                    # the hologram scene (Andre's decision) and scenes whose average only failed because the runner's speed
                    # differed between the two runs ("ciVanillaDrawGate": "median", with the evidence in docs/ci-limits.md)
                    # are gated on the median alone
                    median_only = sc.get("ciDrawCallGate") == "median" or sc.get("ciVanillaDrawGate") == "median"
                    stats = [("median", "drawCallsPerFrameMedian")] + ([] if median_only else [("average", "drawCallsPerFrameAvg")])
                    for label, k in stats:
                        lim = vc[k] + allowance.get(f"{name}/{prof}/{sid}", 0) if sid in translucent else round(vc[k] * RISE + SLACK["drawCallsPerFrame"], 2)
                        if c[k] > lim:
                            failures.append(f"{name}/{prof}/{sid}: {label} {c[k]} draw calls per frame, more than {lim} (vanilla's {vc[k]} in the same run"
                                            f"{(' + ' + str(allowance[name + '/' + prof + '/' + sid])) if sid in translucent and name + '/' + prof + '/' + sid in allowance else '' if sid in translucent else ' x 1.10 + 2'}; fps {c.get('fps')} against vanilla's {vc.get('fps')})")
                    lines.append(f"| {sid} | {c['drawCallsPerFrameAvg']} / {c['drawCallsPerFrameMedian']} ({c.get('fps')}) | {vc['drawCallsPerFrameAvg']} / {vc['drawCallsPerFrameMedian']} ({vc.get('fps')}) | "
                                 f"{' and '.join(l for l, _ in stats)}{(', at most vanilla + ' + str(allowance[name + '/' + prof + '/' + sid])) if sid in translucent and name + '/' + prof + '/' + sid in allowance else ', at most vanilla' if sid in translucent else ', x 1.10 + 2'} |")
                lines.append("")
                lines.append(f"Terrain GPU memory, {name} {prof} (MB; reserved in the Quicksilver run must not be more than vanilla's):\n")
                lines.append("| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |")
                lines.append("|---|---|---|---|---|")
                for sc in scenes:
                    sid = sc["id"]
                    c, vc = cur.get(sid), van.get(sid)
                    if sc.get("skip") or c is None or b in set(sc.get("ciSkipRuns", [])):
                        continue
                    tg, vg = c.get("terrainGpu") or {}, (vc or {}).get("terrainGpu") or {}
                    if tg.get("reservedMB") is None or vg.get("reservedMB") is None:
                        failures.append(f"{name}/{prof}/{sid}: terrain GPU memory missing ({tg.get('problem') or vg.get('problem') or 'no terrainGpu in a bench'})")
                    elif tg["reservedMB"] > vg["reservedMB"]:
                        failures.append(f"{name}/{prof}/{sid}: {tg['reservedMB']} MB of terrain GPU memory reserved, more than vanilla's {vg['reservedMB']} MB")
                    lines.append(f"| {sid} | {tg.get('reservedMB')} / {tg.get('usedMB')} | {tg.get('quicksilverArenasReservedMB')} / {tg.get('quicksilverArenasUsedMB')} | {tg.get('vanillaHeapsReservedMB')} / {tg.get('vanillaHeapsUsedMB')} | {vg.get('reservedMB')} / {vg.get('usedMB')} |")
                lines.append("")
            if base is None:
                failures.append(f"{name}/{prof}: no committed baseline {base_file}")
            # package 4b: triangles drawn against every face, and the upload spike against vanilla in the same run
            van4b = bench_results(runs[MEASURE[prof]]) if not QUICK else {}
            lines.append("")
            lines.append(f"Package 4b, {name} {prof}: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the"
                         " largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting"
                         + ("" if QUICK else f"; vanilla from {MEASURE[prof]}") + ":\n")
            lines.append("| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver"
                         + ("" if QUICK else " / vanilla") + " | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver"
                         + ("" if QUICK else " / vanilla") + " | sections waiting avg / max |")
            lines.append("|---|---|---|---|---|---|---|---|")
            for sc in scenes:
                c = cur.get(sc["id"])
                if sc.get("skip") or c is None:
                    continue
                bf, ub, vc = c.get("backFaces") or {}, c.get("uploadBudget") or {}, van4b.get(sc["id"]) or {}
                lines.append(f"| {sc['id']} | {bf.get('trianglesPerFrame')} ({bf.get('trianglesAllFacesPerFrame')}) | {bf.get('trianglesLeftOutPct')} | {bf.get('rangesPerSectionLayer')} | "
                             f"{c.get('maxUploadBytes')}{'' if QUICK else ' / ' + str(vc.get('maxUploadBytes'))} | {ub.get('maxBytesInOneFrame')} ({ub.get('budgetBytesPerFrame')}) | "
                             f"{c.get('over50ms')}{'' if QUICK else ' / ' + str(vc.get('over50ms'))} | {ub.get('sectionsWaitingAvg')} / {ub.get('sectionsWaitingMax')} |")
            lines.append("")
            # package 4c: meshing time per section over each whole bench (setup, settling, measuring), Quicksilver's mesher
            # against vanilla's compiler in the measure run of the same profile; the check and the sections left to vanilla
            lines.append(f"Package 4c, {name} {prof}: meshing ms per section over each bench (sections in brackets)"
                         + ("" if QUICK else f", Quicksilver's mesher against vanilla's compiler in {MEASURE[prof]}") + ":\n")
            lines.append("| scene | Quicksilver ms/section (sections) | " + ("" if QUICK else "vanilla ms/section (sections) | Quicksilver / vanilla | ")
                         + "while measuring: Quicksilver ms/section (sections) | to vanilla's compiler this bench | checked / mismatched since start-up | same sections, same thread: Quicksilver / vanilla's compiler ms (mean) | median (gated) |")
            lines.append("|---|---|" + ("" if QUICK else "---|---|") + "---|---|---|---|---|")
            for sc in scenes:
                c = cur.get(sc["id"])
                if sc.get("skip") or c is None:
                    continue
                mq, me = c.get("meshing") or {}, c.get("mesher") or {}
                vm = (van4b.get(sc["id"]) or {}).get("meshing") or {}
                ratio = round(mq["benchMsPerSection"] / vm["benchMsPerSection"], 3) if mq.get("benchMsPerSection") and vm.get("benchMsPerSection") else None
                lines.append(f"| {sc['id']} | {mq.get('benchMsPerSection')} ({mq.get('benchSections')}) | "
                             + ("" if QUICK else f"{vm.get('benchMsPerSection')} ({vm.get('benchSections')}) | {ratio} | ")
                             + f"{mq.get('msPerSection')} ({mq.get('sections')}) | {me.get('benchSectionsToVanilla')} | {me.get('checked')} / {me.get('mismatched')} | {me.get('checkedQuicksilverMsPerSection')} / {me.get('checkedVanillaCompilerMsPerSection')} | {me.get('checkedMedianQuicksilverMs')} / {me.get('checkedMedianVanillaCompilerMs')} |")
            lines.append("")
            lines.append(f"Terrain module, {name} {prof} (reported, not gated):\n")
            lines.append("| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |")
            lines.append("|---|---|---|---|---|---|---|---|---|---|---|")
            for sc in scenes:
                c = cur.get(sc["id"])
                if sc.get("skip") or c is None or not c.get("terrain"):
                    continue
                t = c["terrain"]
                files = glob.glob(os.path.join(runs[b], "bench", f"*-{sc['id']}.json"))
                mesh = (json.load(open(files[0])).get("meshing") or {}) if files else {}
                lines.append(f"| {sc['id']} | {t.get('lastDrawPath')} | {t.get('drawCallsPerFrame')} | {t.get('sectionLayersPerFrame')} | {t.get('vertexBytes')} (vanilla {t.get('vanillaVertexBytes')}) | {t.get('liveVertices')} | {t.get('positionRoundedPct')} / {round(100.0 * (t.get('verticesWithUvRounded') or 0) / max(1, t.get('verticesConverted') or 0), 3)} | {t.get('usedMB')} / {t.get('arenaMB')} | {t.get('vanillaEquivalentMB')} | {t.get('layersTaken')} / {t.get('layersKeptByVanilla')} | {mesh.get('msPerSection')} ({mesh.get('compileMsP95')}) |")
            lines.append("")
            lines.append(f"Translucency, {name} {prof} (package 3; the check, mixed frames and dry run gated above):\n")
            lines.append("| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |")
            lines.append("|---|---|---|---|---|---|---|---|---|")
            for sc in scenes:
                c = cur.get(sc["id"])
                if sc.get("skip") or c is None or not c.get("translucency"):
                    continue
                v = c["translucency"]
                lines.append(f"| {sc['id']}{' (dry run)' if v.get('dryRun') else ''} | {v.get('sectionLayersPerFrame')} | {v.get('drawCallsPerFrame')} | {v.get('vanillaSectionLayersPerFrame')} | {v.get('mixedFrames')} | {v.get('oitFrames')} | {v.get('sorts')} ({v.get('sortMsAvg')} / {v.get('sortMsP95')} / {v.get('sortMsMax')}; {v.get('sortsOnRenderThread')}) | {v.get('resortsUploaded')} | {v.get('entriesChecked')} / {v.get('entriesUnchecked')} / {v.get('violations')} |")
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
            # package 8: what vanilla spends on effects (its measure run, every Quicksilver module off) next to Quicksilver
            van = {} if QUICK else bench_results(runs[MEASURE[prof]])
            lines.append(f"Effects, {name} {prof} (reported; draw calls per frame by what they draw for; vanilla = {MEASURE[prof] if not QUICK else 'no measure run in quick mode'}):\n")
            lines.append("| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |")
            lines.append("|---|---|---|---|---|---|---|---|---|---|---|---|")
            for sc in scenes:
                c = cur.get(sc["id"])
                if sc.get("skip") or c is None or not c.get("effects"):
                    continue
                e, a = c["effects"], c.get("animation") or {}
                ve = ((van.get(sc["id"]) or {}).get("effects") or {}).get("drawCallsPerFrame", {})
                d, p = e.get("drawCallsPerFrame", {}), e.get("particles", {})
                both = lambda k: f"{ve.get(k, '-')} / {d.get(k)}"
                lines.append(f"| {sc['id']} | {both('animation')} | {both('particles')} | {both('sky')} | {both('weather')} | {both('clouds')} | {p.get('aliveAvg')} / {p.get('drawnPerFrame')} | {p.get('tickMsPerTick')} / {p.get('extractMsPerFrame')} | {a.get('sprites')} ({a.get('usedOutsideTerrain')}) | {a.get('changedPerTick')} / {a.get('redrawnPerTick')} / {a.get('skippedPerTick')} | {a.get('catchUps')} / {a.get('late')} | {a.get('seenPerFrame')} |")
            lines.append("")
            # package 7: the game's GUI pass, Quicksilver's run and vanilla's (the measure run) side by side (reported)
            van_gui = {} if QUICK else bench_results(runs[MEASURE[prof]])
            lines.append(f"GUI pass, {name} {prof} (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas,"
                         " picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing"
                         " the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound);"
                         " Quicksilver / vanilla (measure run):\n")
            lines.append("| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |")
            lines.append("|---|---|---|---|---|---|")
            for sc in scenes:
                c = cur.get(sc["id"])
                if sc.get("skip") or c is None or not c.get("gui"):
                    continue
                g, vg = c["gui"], ((van_gui.get(sc["id"]) or {}).get("gui") or {})
                cell = lambda k: f"{g.get(k)} / {vg.get(k, '-')}"
                lines.append(f"| {sc['id']} | {cell('drawCallsPerFrame')} | {cell('prepareDrawCallsPerFrame')} | {cell('elementsPerFrame')} | {cell('statesPerFrame')} (pipelines {g.get('pipelinesPerFrame')}, textures {g.get('textureSetupsPerFrame')}, scissors {g.get('scissorAreasPerFrame')}) | {cell('reorderedDrawsPerFrame')} ({cell('reorderedIgnoringTextPerFrame')}) |")
            lines.append("")
    bench_only(res, failures, lines)
    single_player(res, failures, lines)
    direct_buffers(res, lines)
    sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
    import siftec_status
    failures, siftec_soft, siftec_lines = siftec_status.split(failures, res, os.path.join(os.path.dirname(os.path.abspath(rep)), "siftec-world", "siftec-world.txt"))
    lines[0:0] = siftec_lines
    lines.insert(0, "# Quicksilver CI comparison\n\n" + ("**FAILED**\n\n" + "\n".join(f"- {f}" for f in failures) if failures else "**passed**") + "\n")
    open(os.path.join(rep, "report.md"), "w").write("\n".join(lines) + "\n")
    json.dump({"failures": failures, "siftecReportOnly": siftec_soft, "suites": data}, open(os.path.join(rep, "report.json"), "w"), indent=2)
    print("\n".join(lines))
    sys.exit(1 if failures else 0)


if __name__ == "__main__":
    main()
