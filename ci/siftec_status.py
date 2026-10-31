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


def is_siftec(failure):
    return failure.startswith(("siftec", "suite siftec", "SIFTEC"))


def start_status(res):
    """{run directory: first start-up error line} for every SIFTEC run whose logs show SIFTEC failing to start."""
    bad = {}
    for d in sorted(glob.glob(os.path.join(res, "siftec-*"))):
        for f in sorted(glob.glob(os.path.join(d, "**", "*.log"), recursive=True)):
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


def split(failures, res):
    """(gating failures, SIFTEC's report-only failures, report lines). Without report-only mode, everything gates."""
    bad = start_status(res)
    lines = []
    if bad:
        lines.append("**SIFTEC did not start** in %d run(s):\n" % len(bad))
        lines += ["- %s: `%s`" % (r, e) for r, e in sorted(bad.items())]
        lines.append("")
    if not REPORT_ONLY:
        return failures, [], lines
    gate = [f for f in failures if not is_siftec(f)]
    soft = [f for f in failures if is_siftec(f)]
    head = ["## SIFTEC: report only\n",
            "Until SIFTEC starts with MapMod's latest build again, SIFTEC's suites fail nothing (docs/progress.md, open"
            " items). Core and Terralith gate as usual.\n"]
    if not bad:
        head.append("SIFTEC started in every run.\n")
    body = ["SIFTEC failures (not gating):\n"] + ["- " + f for f in soft] if soft else ["No SIFTEC failures.\n"]
    return gate, soft, head + lines + body + [""]
