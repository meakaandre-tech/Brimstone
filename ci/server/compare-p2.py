#!/usr/bin/env python3
"""Phase 2 checks that sit next to compare-server.py (P2-2): the perf job, the Logic module, and SIFTEC's report-only
state. Kept in its own file so compare-server.py (shared with other branches) stays as it is.

usage:
  compare-p2.py server RESULTS_DIR REPORT_DIR   after compare-server.py, in server-compare
  compare-p2.py client RESULTS_DIR REPORT_DIR   after ci/compare.py, in compare

server:
  - perf (ci/server/perf-run.sh, RESULTS_DIR/<suite>-perf): per scene, the median MSPT of vanilla's and Quicksilver's
    plain rounds (1-3, one runner, alternating order) and where the time went (round 6's JFR, ci/server/perf-jfr.py).
    Gate: every run finished with no failed command; in GATED_SCENES Quicksilver's median MSPT over rounds 1-5 is below
    vanilla's.
    Scenes marked `world` in perf-<suite>.txt: every Quicksilver round's saved world equals vanilla round 1's on every
    key where vanilla's rounds (1-6) agree (ci/server/worlddiff.py, the server suite's rule).
  - Logic module (P2-2): every Quicksilver server start (server suite and perf) logs `Quicksilver logic:` with every
    optimisation on; no `Quicksilver logic fallback:` line in any run; no identity hash in the module's sources (P2-1's
    rule: an identity hash moves every later one, and with them the order of vanilla's hash sets).
  - SIFTEC report-only (SIFTEC_REPORT_ONLY=1, Andre 2026-10-31: until SIFTEC starts with MapMod's latest again):
    compare-server.py's failures for suite siftec are listed as owed, not failed; every other failure still fails.
client:
  - Logic module in every Quicksilver client run (its integrated server): on, no fallback; measure runs: off.
  - SIFTEC report-only for ci/compare.py's failures, as above.
Writes REPORT_DIR/p2.md (server) or REPORT_DIR/p2-client.md (client) and exits 1 on any gated failure.
"""
import glob
import json
import os
import re
import statistics
import subprocess
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.normpath(os.path.join(HERE, "..", ".."))
# scenes whose MSPT is gated (Quicksilver's median below vanilla's); the rest are reported
GATED_SCENES = {("core", "mobs"), ("core", "random")}
LOGIC_ON = "Quicksilver logic:"
LOGIC_FALLBACK = "Quicksilver logic fallback:"
REPORT_ONLY_SUITES = ("siftec",) if os.environ.get("SIFTEC_REPORT_ONLY") == "1" else ()
PLAIN_ROUNDS = (1, 2, 3, 4, 5)
ALL_ROUNDS = (1, 2, 3, 4, 5, 6)
JFR_ROUND = 6


def load(path, default=None):
    try:
        with open(path) as f:
            return json.load(f)
    except (OSError, ValueError):
        return default


def text(path):
    try:
        with open(path, errors="replace") as f:
            return f.read()
    except OSError:
        return ""


def scenes(suite):
    out = []
    path = os.path.join(HERE, "perf-%s.txt" % suite)
    if not os.path.exists(path):
        return out
    for line in open(path):
        p = line.split()
        if p and not p[0].startswith("#"):
            out.append((p[0], p[1], int(p[2]), p[3] == "world"))
    return out


def worlddiff(a, b, out):
    if not (os.path.isdir(a) and os.path.isdir(b)):
        return None
    p = subprocess.run([sys.executable, "-I", os.path.join(HERE, "worlddiff.py"), a, b, out, "--mode=world"],
                       check=False, stdout=subprocess.DEVNULL, stderr=subprocess.PIPE, text=True)
    if p.returncode:
        return {"error": (p.stderr.strip().splitlines() or ["?"])[-1]}
    return load(out)


def logic_status(log, quicksilver, where, fails):
    """The Logic module's status line(s) in one server log: fully on in a Quicksilver run, absent in a vanilla one."""
    st = re.findall(re.escape(LOGIC_ON) + r" (.*)", log)
    if quicksilver:
        if not st:
            fails.append("%s: no '%s' status line" % (where, LOGIC_ON))
        elif any(re.search(r"=(off|fallback)|^off", x) for x in st):
            fails.append("%s: Logic module not fully on: %s" % (where, [x for x in st if re.search(r"=(off|fallback)|^off", x)][0][:300]))
    elif st:
        fails.append("%s: a Logic status line in a run without Quicksilver" % where)
    fb = [l for l in log.splitlines() if LOGIC_FALLBACK in l]
    if fb:
        fails.append("%s: %d Logic fallbacks (first: %s)" % (where, len(fb), fb[0][:300]))
    return st[-1] if st else None


def fmt(v):
    return "-" if v is None else ("%.3f" % v)


def perf(res, rep, lines, fails, owed, summary):
    for d in sorted(glob.glob(os.path.join(res, "*-perf"))):
        suite = os.path.basename(d)[:-len("-perf")]
        sc = scenes(suite)
        if not sc:
            continue
        sink = owed if suite in REPORT_ONLY_SUITES else fails
        lines.append("## Perf: %s (one runner; MSPT while sprinting)\n" % suite)
        lines.append("| scene | gated | vanilla MSPT median (rounds) | Quicksilver MSPT median (rounds) | change | p99 vanilla / Quicksilver |\n|---|---|---|---|---|---|")
        tables = []
        for scene, script, ticks, keep_world in sc:
            got = {}
            for side in ("vanilla", "quicksilver"):
                for r in ALL_ROUNDS:
                    rd = os.path.join(d, scene, "%s%d" % (side, r))
                    j = load(os.path.join(rd, "ci-harness-result.json"), {})
                    log = text(os.path.join(rd, "server.log"))
                    where = "%s perf %s %s round %d" % (suite, scene, side, r)
                    if "msptAvg" not in j or "error" in j or "CI HARNESS RUN DONE" not in log:
                        sink.append("%s: did not finish (%s)" % (where, j.get("error", "no result")))
                        continue
                    if j.get("commandErrors"):
                        sink.append("%s: %d commands failed" % (where, j["commandErrors"]))
                    if j.get("ticks") != ticks:
                        sink.append("%s: ran %s ticks, the scene asks for %d" % (where, j.get("ticks"), ticks))
                    loaded = bool(re.search(r"^\s*- quicksilver ", log, re.M))
                    if loaded != (side == "quicksilver"):
                        sink.append("%s: Quicksilver loaded: %s" % (where, loaded))
                    logic_status(log, side == "quicksilver", where, sink)
                    got[(side, r)] = j
            med = {}
            for side in ("vanilla", "quicksilver"):
                vals = [got[(side, r)]["msptAvg"] for r in PLAIN_ROUNDS if (side, r) in got]
                p99 = [got[(side, r)]["msptP99"] for r in PLAIN_ROUNDS if (side, r) in got]
                med[side] = (statistics.median(vals) if vals else None, vals, statistics.median(p99) if p99 else None)
            v, q = med["vanilla"][0], med["quicksilver"][0]
            change = "-" if not (v and q) else "%+.1f%%" % ((q - v) / v * 100)
            gated = (suite, scene) in GATED_SCENES
            lines.append("| %s | %s | %s (%s) | %s (%s) | %s | %s / %s |" % (
                scene, "yes" if gated else "reported", fmt(v), ", ".join("%.3f" % x for x in med["vanilla"][1]),
                fmt(q), ", ".join("%.3f" % x for x in med["quicksilver"][1]), change, fmt(med["vanilla"][2]), fmt(med["quicksilver"][2])))
            summary.setdefault(suite, {})[scene] = {"vanilla": med["vanilla"][1], "quicksilver": med["quicksilver"][1],
                                                    "vanillaMedian": v, "quicksilverMedian": q}
            if gated and v is not None and q is not None and not q < v:
                sink.append("%s perf %s: Quicksilver's median MSPT %.3f is not below vanilla's %.3f" % (suite, scene, q, v))
            if gated and (v is None or q is None or len(med["vanilla"][1]) < len(PLAIN_ROUNDS) or len(med["quicksilver"][1]) < len(PLAIN_ROUNDS)):
                sink.append("%s perf %s: not every round finished on both sides" % (suite, scene))

            # where the time went (round 6, JFR), each phase in ms per tick: its share of samples x that run's MSPT
            jv = load(os.path.join(d, scene, "vanilla%d" % JFR_ROUND, "jfr.json"), {})
            jq = load(os.path.join(d, scene, "quicksilver%d" % JFR_ROUND, "jfr.json"), {})
            mv = (got.get(("vanilla", JFR_ROUND)) or {}).get("msptAvg")
            mq = (got.get(("quicksilver", JFR_ROUND)) or {}).get("msptAvg")
            t = ["### %s %s: MSPT by phase (round 6, JFR; ms per tick = share of server-thread samples x that run's MSPT)\n" % (suite, scene),
                 "Samples: vanilla %s, Quicksilver %s; MSPT in round 6: vanilla %s, Quicksilver %s.\n" % (jv.get("samples"), jq.get("samples"), fmt(mv), fmt(mq)),
                 "| phase | vanilla ms | Quicksilver ms | saved ms |\n|---|---|---|---|"]
            pv = {x["phase"]: x["share"] for x in jv.get("phases", [])}
            pq = {x["phase"]: x["share"] for x in jq.get("phases", [])}
            for ph in [x["phase"] for x in jv.get("phases", [])] or [x["phase"] for x in jq.get("phases", [])]:
                a = pv.get(ph, 0) * mv if mv else None
                b = pq.get(ph, 0) * mq if mq else None
                if (a or 0) < 0.005 and (b or 0) < 0.005:
                    continue
                t.append("| %s | %s | %s | %s |" % (ph.replace("  ", "&nbsp;&nbsp;"), fmt(a), fmt(b), fmt(a - b) if a is not None and b is not None else "-"))
            for side, jj in (("vanilla", jv), ("Quicksilver", jq)):
                t.append("\nTop methods, %s (self share of samples): %s" % (side, ", ".join(
                    "%s %.1f%%" % (x["method"], x["share"] * 100) for x in jj.get("self", [])[:25])))
            if not jv or not jq:
                t.append("\n(no JFR summary for a side)")
            tables.append("\n".join(t) + "\n")

            # the scene's saved worlds (scenes marked world): every Quicksilver round against vanilla round 1, gated
            # where vanilla's rounds agree
            if keep_world:
                wa = os.path.join(d, scene, "vanilla1", "world")
                noise, missing = set(), False
                for r in ALL_ROUNDS[1:]:
                    n = worlddiff(wa, os.path.join(d, scene, "vanilla%d" % r, "world"), os.path.join(rep, "%s-perf-%s-noise-%d.json" % (suite, scene, r)))
                    if not n or "error" in n:
                        missing = True
                        continue
                    for dim in n.get("dimensions", {}).values():
                        noise |= set(dim.get("keys", []))
                rows = []
                for r in ALL_ROUNDS:
                    qd = worlddiff(wa, os.path.join(d, scene, "quicksilver%d" % r, "world"), os.path.join(rep, "%s-perf-%s-quicksilver-%d.json" % (suite, scene, r)))
                    if not qd or "error" in qd or not qd.get("dimensions"):
                        sink.append("%s perf %s: world of quicksilver round %d missing or not compared (%s)" % (suite, scene, r, (qd or {}).get("error", "missing")))
                        continue
                    qk = set()
                    chunks = 0
                    for dim in qd["dimensions"].values():
                        qk |= set(dim.get("keys", []))
                        chunks += dim.get("chunks", 0)
                    bad = sorted(qk - noise)
                    rows.append("| %d | %d | %d | %d |" % (r, chunks, len(qk), len(bad)))
                    if bad:
                        sink.append("%s perf %s: world of quicksilver round %d differs from vanilla where vanilla agrees: %d keys (first: %s)"
                                    % (suite, scene, r, len(bad), ", ".join(bad[:5])))
                    if chunks == 0:
                        sink.append("%s perf %s: no chunks compared" % (suite, scene))
                if missing:
                    sink.append("%s perf %s: a vanilla round's world is missing or not compared" % (suite, scene))
                tables.append("### %s %s: saved worlds\n\nVanilla's own differences across its six rounds (noise, not gated): %d keys%s.\n\n"
                              "| Quicksilver round | chunks | keys differing from vanilla round 1 | where vanilla agrees |\n|---|---|---|---|\n%s\n"
                              % (suite, scene, len(noise), (" (first: %s)" % ", ".join(sorted(noise)[:5])) if noise else "", "\n".join(rows)))
        lines.append("")
        lines += tables


def server(res, rep):
    fails, owed, lines, summary = [], [], ["# Phase 2 checks (compare-p2.py)\n"], {}
    perf(res, rep, lines, fails, owed, summary)

    # the Logic module in the server suite's runs (every server start: worldgen, GameTests, scripted, random-tick, heap)
    lines.append("## Logic module in the server suite\n\n| run | status |\n|---|---|")
    for d in sorted(glob.glob(os.path.join(res, "*-*"))):
        name = os.path.basename(d)
        m = re.match(r"(\w+)-(vanilla-[abc]|quicksilver|net-vanilla-client|net-qs-client)$", name)
        if not m:
            continue
        suite, run = m.groups()
        sink = owed if suite in REPORT_ONLY_SUITES else fails
        qs_server = run in ("quicksilver", "net-vanilla-client")
        for p in sorted(glob.glob(os.path.join(d, "*-server.log"))):
            st = logic_status(text(p), qs_server, "%s %s" % (name, os.path.basename(p)), sink)
            if qs_server:
                lines.append("| %s %s | %s |" % (name, os.path.basename(p), (st or "missing")[:200]))
    lines.append("")

    # no identity hash in the Logic module (P2-1's rule)
    src = os.path.join(ROOT, "src", "main", "java", "com", "meakaandre", "quicksilver")
    bad = re.compile(r"identityHashCode|IdentityHashMap|getClass\(\)\.hashCode\(\)|Reference2|ReferenceOpen|Identity")
    files = glob.glob(os.path.join(src, "logic", "**", "*.java"), recursive=True) + glob.glob(os.path.join(src, "mixin", "logic", "**", "*.java"), recursive=True)
    if not files:
        fails.append("no Logic module sources found")
    for f in sorted(files):
        for i, l in enumerate(text(f).splitlines(), 1):
            if bad.search(l) and not l.strip().startswith(("*", "//", "/*")):
                fails.append("identity hash in the Logic module: %s:%d: %s" % (os.path.basename(f), i, l.strip()[:120]))

    # compare-server.py's verdict, with SIFTEC's failures owed while SIFTEC is report-only
    srv = load(os.path.join(rep, "summary.json"))
    if srv is None:
        fails.append("no compare-server.py summary")
    else:
        for f in srv.get("fails", []):
            if f.split(" ")[0].split("/")[0] in REPORT_ONLY_SUITES:
                owed.append("compare-server: " + f)
            else:
                fails.append("compare-server: " + f)
    finish(rep, "p2.md", lines, fails, owed, summary)
    return 1 if fails else 0


def client(res, rep):
    fails, owed, lines = [], [], ["# Phase 2 checks, client runs (compare-p2.py)\n", "| run | Logic status |\n|---|---|"]
    for d in sorted(glob.glob(os.path.join(res, "*-*"))):
        name = os.path.basename(d)
        m = re.match(r"(\w+)-(quicksilver-\w+|measure-\w+)$", name)
        if not m:
            continue
        suite, run = m.groups()
        sink = owed if suite in REPORT_ONLY_SUITES else fails
        log = "\n".join(text(p) for p in sorted(glob.glob(os.path.join(d, "**", "*.log"), recursive=True)))
        if run.startswith("measure"):
            if "Quicksilver logic: off, quicksilver.ci.measureOnly is set" not in log:
                sink.append("%s: the Logic module did not stay off for measuring" % name)
            st = None
        else:
            st = logic_status(log, True, name, sink)
        fb = [l for l in log.splitlines() if LOGIC_FALLBACK in l]
        if fb and run.startswith("measure"):
            sink.append("%s: %d Logic fallbacks" % (name, len(fb)))
        lines.append("| %s | %s |" % (name, (st or "-")[:200]))
    lines.append("")
    rj = load(os.path.join(rep, "report.json"))
    if rj is None:
        fails.append("no ci/compare.py report.json")
    else:
        for f in rj.get("failures", []):
            if re.split(r"[/ :]", f)[0] in REPORT_ONLY_SUITES:
                owed.append("compare: " + f)
            else:
                fails.append("compare: " + f)
    finish(rep, "p2-client.md", lines, fails, owed, {})
    return 1 if fails else 0


def finish(rep, name, lines, fails, owed, summary):
    if REPORT_ONLY_SUITES:
        lines.append("## Owed (report-only: %s, SIFTEC_REPORT_ONLY=1)\n" % ", ".join(REPORT_ONLY_SUITES))
        lines += ["- " + f for f in owed] or ["(nothing)"]
        lines.append("")
    lines.append("## Result\n\n**%s**" % ("PASS" if not fails else "FAIL"))
    lines += ["- " + f for f in fails]
    os.makedirs(rep, exist_ok=True)
    with open(os.path.join(rep, name), "w") as f:
        f.write("\n".join(lines) + "\n")
    with open(os.path.join(rep, name.replace(".md", ".json")), "w") as f:
        json.dump({"fails": fails, "owed": owed, "perf": summary}, f, indent=1)
    print("\n".join(lines))


if __name__ == "__main__":
    if len(sys.argv) != 4 or sys.argv[1] not in ("server", "client"):
        print(__doc__)
        sys.exit(2)
    try:
        sys.exit((server if sys.argv[1] == "server" else client)(sys.argv[2], sys.argv[3]))
    except Exception:  # noqa: BLE001 - a crash must still leave a report, and fail
        import traceback
        os.makedirs(sys.argv[3], exist_ok=True)
        with open(os.path.join(sys.argv[3], "p2.md" if sys.argv[1] == "server" else "p2-client.md"), "w") as f:
            f.write("# Phase 2 checks\n\n**FAIL**: compare-p2.py crashed\n\n```\n%s```\n" % traceback.format_exc())
        traceback.print_exc()
        sys.exit(1)
