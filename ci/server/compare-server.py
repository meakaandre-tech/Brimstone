#!/usr/bin/env python3
"""Compares the server suite's runs (phase 2, P2-0) and fails the build on any Quicksilver-only difference.

usage: compare-server.py RESULTS_DIR REPORT_DIR

RESULTS_DIR holds one directory per run, <suite>-<run> (run: vanilla-a, vanilla-b, vanilla-c, quicksilver), as written by
ci/server/server-run.sh. Per suite:
  - worldgen: every chunk the harness generated (ci/server/gen-<suite>.txt) byte for byte (ci/server/worlddiff.py --mode gen; Andre's decision 2026-10-28:
    fixed single-threaded generation order, byte for byte);
  - world diff (core): the scripted run's saved world (--mode world: block states and block entities exactly,
    entities by type, count, position and health);
    both gated the same way: Quicksilver must equal vanilla-a on every key (chunk; block, block entity, entity type)
    where all three vanilla runs agree. Where vanilla differs from itself (its noise) nothing can be gated; the
    report shows how many keys that is, per dimension (as phase 1's strict pixel check gates only where every
    vanilla run agrees).
  - GameTests: every test in ci/server/harness must log its line in every run, pass, and give the same result in the
    same tick in vanilla-b, vanilla-c and quicksilver as in vanilla-a; the JUnit report must show no failure.
  - scripted run: every run must finish its ticks; milliseconds per tick, heap and start-up are reported against
    vanilla (CI's runners aren't real hardware: reported, not gated).
  - Quicksilver: loaded on the server in its run and in no vanilla run; no error line naming it; its server bench
    must have written its report.
Writes REPORT_DIR/report.md and REPORT_DIR/summary.json. Exit 1 on any failure.
"""
import glob
import json
import os
import re
import subprocess
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
import worlddiff as worlddiff_mod  # noqa: E402  (the comparator's dimension layout, for the area file)
ERRORS = []  # the comparator's own crashes, reported (and failed) at the end
RUNS = ("vanilla-a", "vanilla-b", "vanilla-c", "quicksilver")
OTHER_VANILLA = ("vanilla-b", "vanilla-c")  # each compared with vanilla-a: their differences are vanilla's noise
# keys reported but not gated, per suite and check, and why (docs/ci-limits.md, "Server suite")
WHY_TICKS = ("which sand, gravel and fluid ticks are still pending when the world is saved varies between vanilla's "
             "own runs, at one cave (chunk -1,-2 of seed 12345: sand and gravel over water), in core and SIFTEC alike; "
             "cause not found yet. Blocks, block entities and entities stay gated")
REPORT_ONLY = {("core", "run"): (("sb", "sf"), WHY_TICKS), ("siftec", "run"): (("sb", "sf"), WHY_TICKS)}
TESTS = ("hopper_chain", "redstone_clock", "piston_machine", "piston_door", "item_sorter", "crop_farm", "mob_farm",
         "villager_trade")
QS_IDS = ("quicksilver",)
NET_RUNS = ("net-vanilla-client", "net-qs-client")
# what counts as a protocol problem or a desync in either side's log during the network check
PROTOCOL = re.compile(r"DecoderException|EncoderException|Failed to decode|Failed to encode|Invalid packet|Unknown packet|"
                      r"Error receiving packet|Error sending packet|Internal Exception|Kicked|Disconnected from server|"
                      r"Network protocol error|out of sync|desync", re.I)


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


def worlddiff(a, b, out, mode, extra=()):
    if not (os.path.isdir(a) and os.path.isdir(b)):
        return None
    p = subprocess.run([sys.executable, "-I", os.path.join(HERE, "worlddiff.py"), a, b, out, "--mode=" + mode] + list(extra),
                       check=False, stdout=subprocess.DEVNULL, stderr=subprocess.PIPE, text=True)
    if p.returncode:
        with open(out + ".err", "w") as f:
            f.write(p.stderr)
        ERRORS.append("worlddiff %s %s: %s" % (mode, os.path.basename(out), (p.stderr.strip().splitlines() or ["?"])[-1]))
    return load(out)


def q_dims(report):
    return list(report.get("dimensions", {})) if report else []


def gametests(log):
    found = {}
    for m in re.finditer(r"CI GAMETEST (\S+) tick=(\d+) result=(.*)", log):
        found[m.group(1)] = (int(m.group(2)), m.group(3).strip())
    return found


def selftests(log):
    """SIFTEC's suite summaries: {suite: (passed, failed)}, the last summary of each suite name (the server's own log
    lines, not their chat copies)."""
    out = {}
    for m in re.finditer(r"^(?!.*System chat).*SELFTEST (.+?) (?:done|result|OK): (\d+) passed, (\d+) failed", log, re.M):
        out[m.group(1)] = (int(m.group(2)), int(m.group(3)))
    return out


def junit_failures(path):
    t = text(path)
    if not t:
        return None
    return len(re.findall(r"<failure\b", t)) + len(re.findall(r"<error\b", t))


def main(res, rep):
    os.makedirs(rep, exist_ok=True)
    fails, lines, summary = [], [], {}
    suites = sorted({os.path.basename(p)[:-len(r) - 1] for p in glob.glob(os.path.join(res, "*-*"))
                     for r in RUNS if os.path.basename(p).endswith("-" + r)})
    lines.append("# Server suite (phase 2)\n")
    expected = ["core"] if os.environ.get("CI_MODE") == "quick" else ["core", "siftec", "terralith"]
    expected += [x for x in os.environ.get("CI_EXTRA_SUITES", "").split(",") if x and x not in expected]
    lines.append("Mode: %s (suites expected: %s; quick mode runs core only)\n" % (os.environ.get("CI_MODE", "full"), ", ".join(expected)))
    for e in expected:
        if e not in suites:
            fails.append("suite %s: no results" % e)
    if not suites:
        fails.append("no server results at all")
    for suite in suites:
        d = {r: os.path.join(res, "%s-%s" % (suite, r)) for r in RUNS}
        s = summary.setdefault(suite, {})
        lines.append("## %s\n" % suite)
        for r in RUNS:
            if not os.path.isdir(d[r]):
                fails.append("%s: run %s missing" % (suite, r))

        # ---- worldgen (byte for byte) and the world diff after the scripted run (core: its base world is plain
        # vanilla). Gate: Quicksilver must equal vanilla-a on every key (chunk; block, block entity, entity type)
        # where vanilla-a, vanilla-b and vanilla-c agree (the noise rule: 3 vanilla samples). Keys where vanilla disagrees with itself are its noise: reported, and
        # how much of the world they leave ungated.
        has_run = os.path.exists(os.path.join(HERE, "scripted-run-%s.txt" % suite))
        # worldgen: byte for byte per chunk, and the same chunks block by block (blocks, block entities, scheduled
        # ticks, entities): where vanilla's bytes ever differ from its own, the block-level check still gates them
        # (Andre's decision: byte for byte, per block where vanilla a/b differ)
        checks = [("Worldgen (byte for byte)", "gen", "gen", "chunks"),
                  ("Worldgen (block by block)", "gen", "world", "blocks, block entities, scheduled ticks, entities")]
        area_file = os.path.join(HERE, "gen-%s.txt" % suite)
        expected = {}
        for line in open(area_file):
            p = line.split()
            if p and not p[0].startswith("#"):
                x1, z1, x2, z2 = map(int, p[1:5])
                expected[p[0]] = expected.get(p[0], 0) + (x2 - x1 + 1) * (z2 - z1 + 1)
        for r in RUNS:
            j = load(os.path.join(d[r], "gen", "ci-harness-result.json"), {})
            if "error" in j or j.get("mode") != "gen":
                fails.append("%s worldgen %s: the harness did not finish (%s)" % (suite, r, j.get("error", "no result")))
            if "CI HARNESS GEN DONE" not in text(os.path.join(d[r], "gen-server.log")):
                fails.append("%s worldgen %s: no CI HARNESS GEN DONE in its log" % (suite, r))
        if has_run:
            checks.append(("World diff after the scripted run", "run", "world", "blocks, block entities, entity types"))
        for title, sub, mode, unit in checks:
            lines.append("### %s\n\n| dimension | chunks | vanilla noise (a vs b, a vs c) | quicksilver vs vanilla-a | quicksilver where vanilla agrees |\n|---|---|---|---|---|" % title)
            wa, wq = os.path.join(d["vanilla-a"], sub, "world"), os.path.join(d["quicksilver"], sub, "world")
            # worldgen: the chunks the harness was asked for (ci/server/gen-<suite>.txt), not the ring generated around
            # them nor the spawn chunks the server makes at start-up
            extra = ["--areas=" + area_file] if sub == "gen" else []
            tag = sub if mode == "world" or sub != "gen" else "genbytes"
            noises = [worlddiff(wa, os.path.join(d[v], sub, "world"), os.path.join(rep, "%s-%s-noise-%s.json" % (suite, tag, v[-1])), mode, extra)
                      for v in OTHER_VANILLA]
            qs = worlddiff(wa, wq, os.path.join(rep, "%s-%s-quicksilver.json" % (suite, tag)), mode, extra)
            noise = None if None in noises else {"dimensions": {k: {"keys": sorted(set().union(*[set(n["dimensions"].get(k, {}).get("keys", [])) for n in noises])),
                                                                     "missing": any("missing" in n["dimensions"].get(k, {"missing": 1}) for n in noises) or None}
                                                                 for k in set().union(*[set(n["dimensions"]) for n in noises])}}
            if noise is None or qs is None:
                fails.append("%s %s: a world is missing or the comparison failed" % (suite, title.lower()))
                lines.append("| missing | | | | |\n")
                continue
            total_bad = 0
            for dim in sorted(set(noise["dimensions"]) | set(qs["dimensions"])):
                n, q = noise["dimensions"].get(dim, {}), qs["dimensions"].get(dim, {})
                if n.get("missing") or "missing" in q or not q:
                    fails.append("%s %s: dimension %s missing in a run" % (suite, title.lower(), dim))
                    lines.append("| %s | missing | | | |" % dim)
                    continue
                nk, qk = set(n.get("keys", [])), set(q.get("keys", []))
                skip, why = REPORT_ONLY.get((suite, tag), ((), ""))
                if skip:
                    reported = sorted(k for k in qk - nk if k.startswith(skip))
                    if reported:
                        lines.append("| %s | (reported, not gated: %d %s keys: %s) | | | |" % (dim, len(reported), "/".join(skip), why))
                    qk = {k for k in qk if not k.startswith(skip)}
                bad = sorted(qk - nk)
                total_bad += len(bad)
                lines.append("| %s | %d | %d | %d | %d |" % (dim, q.get("chunks", 0), len(nk), len(qk), len(bad)))
                if q.get("chunks", 0) == 0:
                    fails.append("%s %s: no chunks in %s" % (suite, title.lower(), dim))
                if bad:
                    fails.append("%s %s: %d differences from vanilla where vanilla agrees with itself in %s (first: %s; "
                                 "details in %s-%s-quicksilver.json)" % (suite, title.lower(), len(bad), dim, ", ".join(bad[:5]), suite, tag))
                s.setdefault(tag, {})[dim] = {"chunks": q.get("chunks", 0), "noise": len(nk), "quicksilver": len(qk), "gated": len(bad),
                                              "noiseKeys": sorted(nk)[:50]}
            if sub == "gen":
                for dim_id, n in expected.items():
                    dirs = [os.path.normpath(os.path.join(x, "region")) for x in worlddiff_mod.dimension_dirs(dim_id)]
                    got = [qs["dimensions"][k].get("chunks", 0) for k in q_dims(qs) if os.path.normpath(k) in dirs]
                    if got != [n]:
                        fails.append("%s %s: %s compared %s chunks, the area file asks for %d" % (suite, title.lower(), dim_id, got or "no", n))
            lines.append("\nKeys compared: %s. Noise examples in %s-%s-noise-b.json and -noise-c.json.\n" % (unit, suite, tag))

        # ---- GameTests (core: the GameTest server can't load SIFTEC's pack; see server-run.sh)
        if suite == "core":
            gt = {r: gametests(text(os.path.join(res, "%s-%s" % (suite, r), "gametest-server.log"))) for r in RUNS}
            lines.append("### GameTests (tick finished, result)\n\n| test | vanilla-a | vanilla-b | vanilla-c | quicksilver |\n|---|---|---|---|---|")
            for t in TESTS:
                row = []
                for r in RUNS:
                    v = gt[r].get(t)
                    row.append("missing" if v is None else "%d: %s" % v)
                    if v is None:
                        fails.append("%s GameTest %s: no result in %s" % (suite, t, r))
                lines.append("| %s | %s |" % (t, " | ".join(row)))
                a = gt["vanilla-a"].get(t)
                for v in OTHER_VANILLA:
                    if a is not None and gt[v].get(t) not in (None, a):
                        fails.append("%s GameTest %s: vanilla-a and %s differ (harness not deterministic)" % (suite, t, v))
                if a is not None and gt["quicksilver"].get(t) not in (None, a):
                    fails.append("%s GameTest %s: quicksilver differs from vanilla" % (suite, t))
            for r in RUNS:
                j = load(os.path.join(d[r], "gametest", "ci-harness-result.json"), {})
                if j.get("commandErrors"):
                    fails.append("%s GameTests %s: %d set-up commands failed" % (suite, r, j["commandErrors"]))
                n = junit_failures(os.path.join(d[r], "gametest", "gametest.xml"))
                if n is None:
                    fails.append("%s GameTests %s: no JUnit report" % (suite, r))
                elif n:
                    fails.append("%s GameTests %s: %d failed in the JUnit report" % (suite, r, n))
            s["gametests"] = {r: {k: list(v) for k, v in gt[r].items()} for r in RUNS}
            lines.append("")

        # ---- scripted run numbers (suites with a scripted run), Quicksilver loaded where it should be (every suite)
        if has_run:
            lines.append("### Scripted run (relative only)\n\n| run | finished | load wait (ticks) | mspt avg | mspt p99 | ticks over 50 ms | heap after load MB | heap end MB | start-up ms | world load ms |\n|---|---|---|---|---|---|---|---|---|---|")
        for r in RUNS:
            if has_run:
                j = load(os.path.join(d[r], "run", "ci-harness-result.json"), {})
                ok = "msptAvg" in j and "error" not in j
                if not ok:
                    fails.append("%s scripted run %s: did not finish (%s)" % (suite, r, j.get("error", "no result")))
                if j.get("commandErrors", 0):
                    fails.append("%s scripted run %s: %d commands failed or did nothing (CI HARNESS CMD in run-server.log)"
                                 % (suite, r, j["commandErrors"]))
                lines.append("| %s | %s | %s | %s | %s | %s | %s | %s | %s | %s |" % (r, "yes" if ok else "no", j.get("loadWaitTicks"),
                             j.get("msptAvg"), j.get("msptP99"), j.get("ticksOver50ms"), j.get("heapAfterLoadMB"), j.get("heapEndMB"),
                             j.get("startupMs"), j.get("worldLoadMs")))
                s["run-numbers-" + r] = j
            log = "\n".join(text(p) for p in glob.glob(os.path.join(d[r], "*-server.log")))
            loaded = any(re.search(r"^\s*- %s " % q, log, re.M) for q in QS_IDS)
            if r == "quicksilver" and not loaded:
                fails.append("%s: Quicksilver not in the quicksilver run's mod list" % suite)
            if r != "quicksilver" and loaded:
                fails.append("%s: Quicksilver loaded in vanilla run %s" % (suite, r))
            errs = [l for l in log.splitlines() if re.search(r"/(ERROR|FATAL)\]", l) and re.search(r"quicksilver", l, re.I)]
            if errs:
                fails.append("%s %s: %d error lines naming Quicksilver (first: %s)" % (suite, r, len(errs), errs[0][:200]))
        lines.append("")
        if has_run:
            bench = sorted(glob.glob(os.path.join(d["quicksilver"], "bench", "*.json")))
            if not bench:
                fails.append("%s: no server bench report in the quicksilver run" % suite)
            else:
                b = load(bench[-1], {})
                s["bench"] = b
                lines.append("Quicksilver server bench: `%s`\n" % json.dumps(b.get("server", b))[:600])

        # ---- the Create train (SIFTEC's scripted run): every arrival with its tick, final speed and schedule state,
        # equal in every run; at least two arrivals (it ran its schedule)
        if suite == "siftec":
            tr = {}
            for r in RUNS:
                m = re.findall(r"CI TRAIN result=(.*)", text(os.path.join(d[r], "run-server.log")))
                tr[r] = m[-1].strip() if m else None
            lines.append("### Create train\n\n| run | result |\n|---|---|")
            for r in RUNS:
                lines.append("| %s | %s |" % (r, tr[r] or "missing"))
            lines.append("")
            for r in RUNS:
                if tr[r] is None:
                    fails.append("siftec train: no result in %s" % r)
                elif "error=" in tr[r]:
                    fails.append("siftec train %s: %s" % (r, tr[r][tr[r].index("error="):][:200]))
                elif int(re.match(r"arrivals=(\d+)", tr[r]).group(1)) < 2:
                    fails.append("siftec train %s: fewer than 2 arrivals (%s)" % (r, tr[r][:120]))
            for v in OTHER_VANILLA:
                if tr["vanilla-a"] and tr[v] and tr[v] != tr["vanilla-a"]:
                    fails.append("siftec train: vanilla-a and %s differ (%s / %s)" % (v, tr["vanilla-a"][:80], tr[v][:80]))
            if tr["vanilla-a"] and tr["quicksilver"] and tr["quicksilver"] != tr["vanilla-a"]:
                fails.append("siftec train: quicksilver differs from vanilla (%s / %s)" % (tr["quicksilver"][:80], tr["vanilla-a"][:80]))
            s["train"] = tr

        # ---- SIFTEC's self-tests: every suite's passed and failed counts, against vanilla
        if suite == "siftec":
            st = {r: selftests(text(os.path.join(d[r], "selftest-server.log"))) for r in RUNS}
            names = sorted(set().union(*[set(v) for v in st.values()]))
            lines.append("### SIFTEC self-tests (passed/failed)\n\n| suite | vanilla-a | vanilla-b | vanilla-c | quicksilver |\n|---|---|---|---|---|")
            for n in names:
                lines.append("| %s | %s |" % (n, " | ".join("%d/%d" % st[r][n] if n in st[r] else "missing" for r in RUNS)))
                a, q = st["vanilla-a"].get(n), st["quicksilver"].get(n)
                for v in OTHER_VANILLA:
                    if a is not None and st[v].get(n) is not None and st[v][n] != a:
                        fails.append("siftec self-test %s: vanilla-a %s and %s %s differ" % (n, a, v, st[v][n]))
                if a is not None and q != a:
                    fails.append("siftec self-test %s: quicksilver %s, vanilla %s" % (n, q, a))
            for n in names:
                if st["quicksilver"].get(n) is None:
                    fails.append("siftec self-test %s: no result in quicksilver (vanilla has one)" % n)
            for r in RUNS:
                if not st[r]:
                    fails.append("siftec self-tests: no results in %s" % r)
            s["selftests"] = {r: {k: list(v) for k, v in st[r].items()} for r in RUNS}
            lines.append("")

    # ---- network check (ci/server/net-run.sh)
    lines.append("## Network check\n\n| run | client joined | run finished | client left early | protocol problems | server bytes in/out |\n|---|---|---|---|---|---|")
    for r in NET_RUNS:
        d = os.path.join(res, "core-" + r)
        if not os.path.isdir(d):
            fails.append("network %s: run missing" % r)
            lines.append("| %s | missing | | | | |" % r)
            continue
        slog, clog = text(os.path.join(d, "run-server.log")), text(os.path.join(d, "client.log"))
        client_qs = any(re.search(r"^\s*- %s " % q, clog, re.M) for q in QS_IDS)
        server_qs = any(re.search(r"^\s*- %s " % q, slog, re.M) for q in QS_IDS)
        if client_qs != (r == "net-qs-client") or server_qs != (r == "net-vanilla-client"):
            fails.append("network %s: Quicksilver on the client %s, on the server %s (expected only on the %s)"
                         % (r, client_qs, server_qs, "client" if r == "net-qs-client" else "server"))
        # the server stopping at the end disconnects the client: its log counts up to that point (an early
        # disconnect shows in the server's log, checked by order below)
        cut = re.search(r"Server closed|Disconnected|Connection lost|Lost connection", clog, re.I)
        if cut:
            clog = clog[:cut.start()]
        joined = "joined the game" in slog
        done_at = slog.find("CI HARNESS RUN DONE")
        left = [m.start() for m in re.finditer(r"left the game|lost connection", slog)]
        early = bool(left) and (done_at < 0 or min(left) < done_at)
        problems = [l for l in (slog + "\n" + clog).splitlines() if PROTOCOL.search(l)]
        b = load((sorted(glob.glob(os.path.join(d, "bench", "*.json"))) or [""])[-1], {})
        net = (b.get("server") or {}).get("network") or {}
        lines.append("| %s | %s | %s | %s | %d | %s/%s |" % (r, joined, done_at >= 0, early, len(problems),
                     net.get("serverBytesIn", "-"), net.get("serverBytesOut", "-")))
        if not joined:
            fails.append("network %s: the client did not join" % r)
        if done_at < 0:
            fails.append("network %s: the scripted run did not finish" % r)
        if early:
            fails.append("network %s: the client left before the run finished" % r)
        if problems:
            fails.append("network %s: %d protocol problems (first: %s)" % (r, len(problems), problems[0][:200]))
        if r == "net-vanilla-client" and not net.get("serverBytesOut"):
            fails.append("network %s: Quicksilver's server bench counted no bytes sent" % r)
        summary.setdefault("network", {})[r] = {"joined": joined, "finished": done_at >= 0, "leftEarly": early,
                                                 "problems": problems[:5], "network": net}
    lines.append("")

    for e in ERRORS:
        fails.append("comparator crashed: " + e)
    lines.append("## Result\n")
    lines.append("**%s**" % ("PASS" if not fails else "FAIL"))
    lines += ["- " + f for f in fails]
    with open(os.path.join(rep, "report.md"), "w") as f:
        f.write("\n".join(lines) + "\n")
    with open(os.path.join(rep, "summary.json"), "w") as f:
        json.dump({"fails": fails, "suites": summary}, f, indent=1)
    print("\n".join(lines))
    return 1 if fails else 0


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print(__doc__)
        sys.exit(2)
    try:
        sys.exit(main(sys.argv[1], sys.argv[2]))
    except Exception:  # noqa: BLE001 - a crash must still leave a report, and fail
        import traceback
        os.makedirs(sys.argv[2], exist_ok=True)
        with open(os.path.join(sys.argv[2], "report.md"), "w") as f:
            f.write("# Server suite (phase 2)\n\n**FAIL**: compare-server.py crashed\n\n```\n%s```\n" % traceback.format_exc())
        traceback.print_exc()
        sys.exit(1)
