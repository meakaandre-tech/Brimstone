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


def steady_shot(path, tries=12, gap=4.0, pct=1.0):
    """Waits until two captures `gap` seconds apart differ on under `pct`% of pixels (chunks, lighting and map
    tiles have caught up), then keeps the second. Animated scenes (water) may never get there: after `tries` it
    keeps the last capture. Returns how many pairs it took, or -1 if it gave up. Same procedure in every run."""
    import numpy as np
    from PIL import Image
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
    ids = sys.argv[7:]
    suite = json.load(open(suite_file))
    scenes = {s["id"]: s for s in suite["scenes"]}
    os.makedirs(os.path.join(out, "shots"), exist_ok=True)
    console = open(fifo, "w", buffering=1)
    summary = []

    def send(cmd):
        if cmd.startswith("#wait "):
            time.sleep(float(cmd[6:]))
        else:
            console.write(cmd + "\n")
            time.sleep(0.3)

    def xdo(*args):
        subprocess.run(["xdotool", *args], check=False)

    if suite.get("prepare") and any(not scenes[i].get("skip") for i in ids):
        print("preparing the suite", flush=True)
        for cmd in suite["preamble"] + suite["prepare"]:
            send(cmd)

    for sid in ids:
        sc = scenes[sid]
        entry = {"scene": sid}
        if sc.get("skip"):
            entry["skipped"] = sc["skip"]
            summary.append(entry)
            print(f"scene {sid}: skipped ({sc['skip']})", flush=True)
            continue
        print(f"scene {sid}: setting up", flush=True)
        for cmd in suite["preamble"] + sc["setup"]:
            send(cmd)
        time.sleep(sc.get("settleSeconds", 20))
        screen = sc.get("screen")
        if screen and screen.get("ciKey"):
            xdo("key", screen["ciKey"])
        if screen:
            # park the mouse where no screen draws a tooltip, the same in every run
            xdo("mousemove", "1", "1")
            time.sleep(5)
        shot = os.path.join(out, "shots", sid + ".png")
        entry["steadyAfter"] = steady_shot(shot)
        entry["shot"] = os.path.exists(shot)
        if screen:
            # every run closes the scene's screen the same way before going on
            xdo("key", "Escape")
            time.sleep(1.5)
            xdo("mousemove", "640", "360")
        if mode == "brimstone":
            start = os.path.getsize(latest) if os.path.exists(latest) else 0
            xdo("key", "slash")
            time.sleep(1.5)
            xdo("type", "--delay", "40", f"brimstone bench {sid} {bench_seconds}")
            time.sleep(0.5)
            xdo("key", "Return")
            waits = sum(float(c[6:]) for c in sc["setup"] if c.startswith("#wait "))
            budget = time.time() + waits + sc.get("settleSeconds", 20) + int(bench_seconds) + 240
            pressed = False
            result = "timeout"
            while time.time() < budget:
                time.sleep(2)
                with open(latest, errors="replace") as f:
                    f.seek(start)
                    tail = f.read()
                if screen and screen.get("ciKey") and not pressed and f"BRIMSTONE BENCH WAITING FOR SCREEN scene={sid}" in tail:
                    time.sleep(1)
                    xdo("key", screen["ciKey"])
                    pressed = True
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
