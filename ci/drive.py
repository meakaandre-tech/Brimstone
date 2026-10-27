#!/usr/bin/env python3
"""Drives one CI client session through a list of scenes.

For each scene: send the suite's preamble and the scene's setup to the server console (the same commands the bench
sends as the player), wait for it to settle, press the scene's key if it measures a screen, take a screenshot. In a
Brimstone run it then types `/brimstone bench <scene> <seconds>` and waits for the bench's END line in latest.log.

usage: drive.py SUITE_FILE FIFO OUT_DIR LATEST_LOG MODE(vanilla|brimstone) BENCH_SECONDS SCENE_ID...
"""
import json
import os
import subprocess
import sys
import time


def capture(path):
    subprocess.run(["import", "-window", "root", path], check=False)


LATEST = None
WAITS = []


def wait_uploads(timeout=120):
    """Package 4b: Brimstone's terrain uploads go under a per-frame budget, so after F3+A a section can wait a few
    frames for its mesh. In Brimstone runs (brimstone.debug.logUploadQueue) the client logs "Brimstone uploads:
    waiting" and "... caught up"; wait until the last such line is "caught up". Runs without the line (vanilla, measure
    runs, the bench-only jar) don't wait. Returns the seconds waited, -1 on a timeout. It only sees waits already
    logged: meshes that F3+A has not staged yet are left to steady_shot's own check, as in every run."""
    if LATEST is None or not os.path.exists(LATEST):
        return 0
    t0 = time.time()
    while time.time() - t0 < timeout:
        with open(LATEST, errors="replace") as f:
            text = f.read()
        w, c = text.rfind("Brimstone uploads: waiting"), text.rfind("Brimstone uploads: caught up")
        if w < 0 or c > w:
            return round(time.time() - t0, 1)
        time.sleep(1)
    return -1


def steady_shot(path, tries=12, gap=4.0, pct=1.0):
    """Waits until two captures `gap` seconds apart differ on under `pct`% of pixels (chunks, lighting and map
    tiles have caught up), then keeps the second. Animated scenes (water) may never get there: after `tries` it
    keeps the last capture. Returns how many pairs it took, or -1 if it gave up. Same procedure in every run."""
    import numpy as np
    from PIL import Image
    WAITS.append(wait_uploads())
    tmp = path + ".prev.png"
    capture(tmp)
    for i in range(tries):
        time.sleep(gap)
        capture(path)
        try:
            a = np.asarray(Image.open(tmp).convert("RGB"), dtype=np.int16)
            b = np.asarray(Image.open(path).convert("RGB"), dtype=np.int16)
            d = float((np.abs(a - b).max(axis=2) > 8).mean() * 100) if a.shape == b.shape else 100.0
        except Exception:
            d = 100.0
        if d < pct:
            os.remove(tmp)
            return i + 1
        os.replace(path, tmp)
    os.replace(tmp, path)
    return -1


def main():
    suite_file, fifo, out, latest, mode, bench_seconds = sys.argv[1:7]
    global LATEST
    LATEST = latest
    ids = sys.argv[7:]
    suite = json.load(open(suite_file))
    loaded = set(open(os.environ["MOD_IDS"]).read().split()) if os.environ.get("MOD_IDS") else None
    scenes = {s["id"]: s for s in suite["scenes"]}
    os.makedirs(os.path.join(out, "shots"), exist_ok=True)
    console = open(fifo, "w", buffering=1)
    summary = []

    def send(cmd):
        if cmd.startswith("#wait "):
            time.sleep(float(cmd[6:]))
        elif cmd == "#chunks":
            # package 7: F3+A (reload all chunks, so a section changed by the setup's fills is not left stale or
            # missing: the hud scene's wall once had a hole in a measure run) and F3+D (clear the chat, which F3+A
            # writes to), the same in every run; the bench skips this line
            xdo("keydown", "F3")
            xdo("key", "a")
            xdo("keyup", "F3")
            time.sleep(3)
            xdo("keydown", "F3")
            xdo("key", "d")
            xdo("keyup", "F3")
            time.sleep(1)
        else:
            console.write(cmd + "\n")
            time.sleep(0.3)

    def xdo(*args):
        subprocess.run(["xdotool", *args], check=False)

    def chat(text):
        # a client command typed in chat (package 7: a screen only a client command opens, e.g. Create's /ponder)
        xdo("key", "slash")
        time.sleep(1.5)
        xdo("type", "--delay", "40", text)
        time.sleep(0.5)
        xdo("key", "Return")

    runnable = [i for i in ids if not scenes[i].get("skip") and not (scenes[i].get("requiresMod") and loaded is not None and scenes[i]["requiresMod"] not in loaded)]
    if suite.get("prepare") and runnable and (not suite.get("prepareFor") or suite["prepareFor"] in runnable):
        print("preparing the suite", flush=True)
        for cmd in suite["preamble"] + suite["prepare"]:
            send(cmd)

    for sid in ids:
        sc = scenes[sid]
        entry = {"scene": sid}
        if not sc.get("skip") and sc.get("requiresMod") and loaded is not None and sc["requiresMod"] not in loaded:
            sc["skip"] = "needs the mod " + sc["requiresMod"] + " (" + sc.get("requiresNote", "not loaded") + ")"
        # a run a scene cannot run in (package 3: "ciSkipRuns", for a driver crash that hits vanilla too); compare.py
        # leaves that profile out for the scene
        if not sc.get("skip") and os.environ.get("RUN") in sc.get("ciSkipRuns", []):
            sc["skip"] = "not run in " + os.environ["RUN"] + ": " + sc.get("ciSkipReason", "ciSkipRuns")
        if sc.get("skip"):
            entry["skipped"] = sc["skip"]
            summary.append(entry)
            print(f"scene {sid}: skipped ({sc['skip']})", flush=True)
            continue
        print(f"scene {sid}: setting up", flush=True)
        if sc.get("ciClearChat"):
            # F3+D before the setup (package 7: the hud scene's open chat): the chat then holds only the setup's own
            # lines in every run (Brimstone and measure runs' benches add messages, which moved its scrollbar)
            xdo("keydown", "F3")
            xdo("key", "d")
            xdo("keyup", "F3")
            time.sleep(1)
        for cmd in suite["preamble"] + sc["setup"]:
            send(cmd)
        time.sleep(sc.get("settleSeconds", 20))
        screen = sc.get("screen")
        if screen and screen.get("ciKey"):
            xdo("key", screen["ciKey"])
        elif screen and screen.get("ciChat"):
            chat(screen["ciChat"])
        if screen:
            # park the mouse where no screen draws a tooltip, the same in every run
            xdo("mousemove", "1", "1")
            time.sleep(5)
        # keys a scene needs pressed after its setup (package 3: the Build Gun's next-tab key, which picks a building
        # so its hologram shows); the same in every run
        for k in sc.get("ciKeys", []):
            xdo("key", k)
            time.sleep(1.5)
        if not screen:
            # F3+A: vanilla's own "reload all chunks", so a section the client dropped or never meshed under load
            # is rebuilt before the picture is taken; the same in every run
            xdo("keydown", "F3")
            xdo("key", "a")
            xdo("keyup", "F3")
            time.sleep(3)
            # F3+D: vanilla's "clear chat", so F3+A's "Reloading all chunks" line (whose fade-out depends on how long the
            # steady-shot wait takes) is in no screenshot; the same in every run (package 2: a vanilla run once caught
            # it faded and the others not)
            xdo("keydown", "F3")
            xdo("key", "d")
            xdo("keyup", "F3")
            time.sleep(1)
        WAITS.clear()
        shot = os.path.join(out, "shots", sid + ".png")
        entry["steadyAfter"] = steady_shot(shot)
        entry["shot"] = os.path.exists(shot)
        # extra fixed points (package 2): commands to the server, then a screenshot named <scene>.<name>, the same in
        # every run; the Brimstone bench afterwards sets the scene up again from scratch
        for extra in sc.get("ciShots", []):
            for cmd in extra["commands"]:
                send(cmd)
            time.sleep(3)
            p = os.path.join(out, "shots", sid + "." + extra["name"] + ".png")
            entry.setdefault("extraShots", {})[extra["name"]] = steady_shot(p)
        # seconds each shot waited for Brimstone's upload queue; -1: timed out (compare.py fails on it)
        entry["uploadWaits"] = list(WAITS)
        if screen:
            # every run closes the scene's screen the same way before going on
            xdo("key", "Escape")
            time.sleep(1.5)
            xdo("mousemove", "640", "360")
        if mode == "brimstone":
            start = os.path.getsize(latest) if os.path.exists(latest) else 0
            xdo("key", "slash")
            time.sleep(1.5)
            xdo("type", "--delay", "40", f"brimstone bench {sid} {bench_seconds} confirm")
            time.sleep(0.5)
            xdo("key", "Return")
            waits = sum(float(c[6:]) for c in sc["setup"] if c.startswith("#wait "))
            budget = time.time() + waits + sc.get("settleSeconds", 20) + int(bench_seconds) + 240
            pressed = 0.0
            result = "timeout"
            while time.time() < budget:
                time.sleep(2)
                with open(latest, errors="replace") as f:
                    f.seek(start)
                    tail = f.read()
                # the scene's screen key (or chat command) once the bench waits for it; again every 15 s while the bench
                # still waits (package 7: one measure run's chat key press was lost and the bench gave up)
                waiting = f"BRIMSTONE BENCH WAITING FOR SCREEN scene={sid}" in tail and f"BRIMSTONE BENCH MEASURING scene={sid}" not in tail
                if screen and (screen.get("ciKey") or screen.get("ciChat")) and waiting and time.time() >= pressed:
                    time.sleep(1)
                    if screen.get("ciKey"):
                        xdo("key", screen["ciKey"])
                    else:
                        chat(screen["ciChat"])
                    pressed = time.time() + 15
                if f"BRIMSTONE BENCH END scene={sid}" in tail:
                    result = "done"
                    break
                if f"BRIMSTONE BENCH ABORTED scene={sid}" in tail:
                    result = "aborted"
                    break
            entry["bench"] = result
            print(f"scene {sid}: bench {result}", flush=True)
            time.sleep(3)
        summary.append(entry)
    with open(os.path.join(out, "drive-" + "-".join(ids) + ".json"), "w") as f:
        json.dump(summary, f, indent=2)


if __name__ == "__main__":
    main()
