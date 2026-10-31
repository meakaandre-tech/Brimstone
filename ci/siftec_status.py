"""SIFTEC's suites while they report only (Andre, 2026-10-31): until SIFTEC starts with MapMod's latest build again,
a SIFTEC failure (client or server) is listed in the report but fails nothing; core and Terralith gate as usual.
The workflow's mode job sets CI_SIFTEC_REPORT_ONLY (docs/progress.md, open items)."""
import glob
import os
import re

REPORT_ONLY = os.environ.get("CI_SIFTEC_REPORT_ONLY") == "true"
# SIFTEC not starting: an entrypoint error naming it, or a missing method of a mod it calls (MapMod's API)
START_FAIL = re.compile(r"provided by 'siftec'|Could not execute entrypoint stage '\w+' due to errors.*siftec"
                        r"|NoSuchMethodError: '[^']*mapmod|NoClassDefFoundError: com/meakaandre/mapmod", re.I)
# SIFTEC's pack not even put together: a jar that isn't the locked one (a new SIFTEC or self-test build), or no download
PACK_FAIL = re.compile(r"^pack changed: .*$|^could not assemble SIFTEC's pack.*$", re.M)


def is_siftec(failure):
    return failure.startswith(("siftec", "suite siftec", "SIFTEC"))


def start_status(res):
    """{run directory: why SIFTEC didn't start} for every SIFTEC run: its pack not assembled (run.txt), SIFTEC failing at
    start-up (a log), or no game log at all."""
    bad = {}
    for d in sorted(glob.glob(os.path.join(res, "siftec-*"))):
        try:
            run = open(os.path.join(d, "run.txt"), errors="replace").read()
        except OSError:
            run = ""
        packs = PACK_FAIL.findall(run)
        if packs:
            bad[os.path.basename(d)] = "the pack: " + "; ".join(p.strip()[:160] for p in packs[:3])
            continue
        logs = [f for f in glob.glob(os.path.join(d, "**", "*.log"), recursive=True)]
        if not logs:
            bad[os.path.basename(d)] = "no game log"
            continue
        for f in sorted(logs):
            try:
                text = open(f, errors="replace").read()
            except OSError:
                continue
            m = START_FAIL.search(text)
            if m:
                line = text[text.rfind("\n", 0, m.start()) + 1:text.find("\n", m.end()) if "\n" in text[m.end():] else len(text)]
                bad[os.path.basename(d)] = "%s: %s" % (os.path.relpath(f, d), line.strip()[:240])
                break
    return bad


def split(failures, res, world_txt=None):
    """(gating failures, SIFTEC's report-only failures, report lines). Without report-only mode, everything gates.
    world_txt: the SIFTEC world job's log (the client suite's runs all start from that world)."""
    bad = start_status(res)
    lines = []
    runs = glob.glob(os.path.join(res, "siftec-*"))
    world = ""
    if world_txt:
        try:
            world = open(world_txt, errors="replace").read()
        except OSError:
            world = ""
    packs = PACK_FAIL.findall(world)
    if packs:
        lines.append("**SIFTEC's world job stopped at the pack check** (SIFTEC's runs need its world):\n")
        lines += ["- `%s`" % p.strip()[:200] for p in packs[:6]]
        lines.append("")
    if not runs and any(is_siftec(f) for f in failures):
        lines.append("**No SIFTEC run left any results** (its world or pack job failed first).\n")
    if bad:
        lines.append("**SIFTEC did not start** in %d of %d run(s):\n" % (len(bad), len(glob.glob(os.path.join(res, "siftec-*")))))
        lines += ["- %s: `%s`" % (r, e) for r, e in sorted(bad.items())]
        lines.append("")
    if not REPORT_ONLY:
        return failures, [], lines
    gate = [f for f in failures if not is_siftec(f)]
    soft = [f for f in failures if is_siftec(f)]
    head = ["## SIFTEC: report only\n",
            "Until SIFTEC starts with MapMod's latest build again, SIFTEC's suites fail nothing (docs/progress.md, open"
            " items). Core and Terralith gate as usual.\n"]
    if runs and not bad:
        head.append("SIFTEC started in every run.\n")
    body = ["SIFTEC failures (not gating):\n"] + ["- " + f for f in soft] if soft else ["No SIFTEC failures.\n"]
    return gate, soft, head + lines + body + [""]
