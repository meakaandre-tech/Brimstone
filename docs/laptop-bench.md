# Benching on the school laptop

Windows, Intel integrated graphics, Prism Launcher. Quicksilver 0.0.1 only measures, so every number from this build is
a **vanilla** number: the baseline later packages are judged against.

Run two instances: a plain one (vanilla Fabric, Fabric API, Quicksilver) and the SIFTEC one with Quicksilver added.

## Before each session

- Plug the charger in and set Windows to its best-performance power mode.
- Close the browser and anything else heavy.
- Keep the game window focused and don't touch mouse or keyboard while a bench is measuring (the chat says when it
  starts and ends). Right-click only when a HUB or MAM scene asks you to.

## Get Quicksilver

Download `quicksilver-26.3.jar` from the `latest` release of meakaandre-tech/Quicksilver on GitHub.

## Instance 1: plain

1. In Prism: **Add Instance** → Minecraft **26.3** → Mod loader **Fabric**, loader **0.19.5** → OK.
2. Instance → **Edit** → **Mods** → **Download mods** → add **Fabric API** for 26.3 (0.161.0+26.3 or newer).
   Then **Add file** → `quicksilver-26.3.jar`.
3. Edit → **Settings** → Java: use a **Java 25** runtime (Prism can download it). Memory: set max memory to what you
   normally give Minecraft and write it down.
4. Edit → Settings → **Game windows**: tick "Start Minecraft with a custom window size", **1280 × 720**.
5. Launch. In Options → Video Settings: **Max Framerate: Unlimited**, **VSync: Off**, **GUI Scale: 2**, Graphics API
   left on **Default**. Leave the rest as you play.
6. **Singleplayer → Create New World**: Game Mode **Creative**, **Allow Commands: On**, More → **Seed: 12345**,
   World Type Default. Create it and wait 30 seconds in the world.
7. Type these one at a time, waiting for "[Quicksilver] Bench ... done" before the next (each takes about 1.5 minutes).
   A scene changes the world (gamerules, time, weather, game mode, your position, blocks and mobs it places), so the
   bench refuses until you add `confirm`; without it, it tells you what it will change:

   ```
   /quicksilver bench flythrough confirm
   /quicksilver bench spawn confirm
   /quicksilver bench water_glass confirm
   /quicksilver bench mobs confirm
   /quicksilver bench nether confirm
   ```

   Run `flythrough` first: it flies over terrain that has never been generated, which is only true in a new world.
8. Memory after 10 minutes (one run is enough): `/quicksilver bench spawn 600 confirm`.
9. Quit to the title screen.

## Instance 2: SIFTEC

1. Copy your SIFTEC instance in Prism (right-click → **Copy Instance**) so your own worlds and settings stay apart.
2. In the copy: Edit → Mods → **Add file** → `quicksilver-26.3.jar`. Same Java, memory and 1280 × 720 window as above;
   same video settings (Unlimited, VSync off, GUI scale 2).
3. **Create a new world** just for this: Creative, **Allow Commands: On**, **Seed: 12345**. The scenes build
   platforms and place a HUB and a MAM, so never run them in a world you play in.
4. Wait 30 seconds, then run, one at a time:

   ```
   /quicksilver bench flythrough confirm
   /quicksilver bench spawn confirm
   /quicksilver bench sift confirm
   /quicksilver bench hub confirm
   /quicksilver bench mam confirm
   /quicksilver bench water_glass confirm
   /quicksilver bench mobs confirm
   /quicksilver bench nether confirm
   ```

   `hub` and `mam` stand you in front of the block and ask you to right-click it. Measuring starts when the screen
   opens. Leave it open: the bench closes it when it is done.

   `factory` and `map` need SIFTEC's selftest mod (its test commands build the factory row and open the map), which
   SIFTEC's release doesn't include, so the bench won't offer them in a normal SIFTEC instance. If you drop
   `siftec-selftest-26.3.jar` into the mods folder, also run `/quicksilver bench factory confirm` and
   `/quicksilver bench map confirm`.
5. Memory after 10 minutes: `/quicksilver bench sift 600 confirm`.

## What to paste back

Open the instance folder (Prism: right-click → **Folder**) → `.minecraft/logs/latest.log` (or `minecraft/logs`).
Each bench wrote one block:

```
===== QUICKSILVER BENCH BEGIN =====
  scene: ...
  ...
===== QUICKSILVER BENCH END =====
```

Paste every block, and say which instance it came from and the max memory you set. The same numbers are also in
`.minecraft/quicksilver/bench/*.json` if a block is easier to send as a file.

If a scene says it stopped (for example "the HubScreen screen was not opened within 2 minutes"), paste that line too.

## Vanilla, Sodium and Quicksilver side by side (package 4b)

One plain instance, three mod sets, the same seed and scenes, so the three logs compare directly. The **bench-only
jar** (`quicksilver-bench-26.3.jar`, in the same `latest` release as `quicksilver-26.3.jar`) is `/quicksilver bench`, its
counters and its scenes with every Quicksilver module left out: it changes nothing, so with it the game draws as vanilla
does (or as Sodium does, next to Sodium). Quicksilver itself has the same bench built in.

| run | mods folder (besides Fabric API) |
|---|---|
| vanilla | `quicksilver-bench-26.3.jar` |
| Sodium | `quicksilver-bench-26.3.jar` and Sodium **0.9.2** for 26.3 (Prism: Download mods, Modrinth, "Sodium", pick 0.9.2) |
| Quicksilver | `quicksilver-26.3.jar` only |

Never put the bench-only jar next to Quicksilver, or Quicksilver next to Sodium: the game refuses to start (each says so).

1. Make the plain instance as in "Instance 1: plain" above (steps 1-5: Fabric 26.3, loader 0.19.5, Fabric API, Java 25,
   1280 x 720, Max Framerate Unlimited, VSync Off, GUI Scale 2), but add only the jars in the table above, not step 2's
   `quicksilver-26.3.jar` in every copy. Then copy it twice in Prism (right-click, **Copy
   Instance**) and name the three copies `bench-vanilla`, `bench-sodium`, `bench-quicksilver`. Put the mods from the table
   in each. Sodium has its own video settings screen: set the same Max Framerate, VSync and GUI Scale there, leave the
   rest on Sodium's defaults.
2. In each instance, one after the other (not at the same time), with the charger in and nothing else open:
   **Create New World**, Creative, Allow Commands On, **Seed 12345**, World Type Default. Wait 30 seconds, then run one at
   a time, waiting for "[Quicksilver] Bench ... done" each time:

   ```
   /quicksilver bench flythrough confirm
   /quicksilver bench spawn confirm
   /quicksilver bench water_glass confirm
   /quicksilver bench nether confirm
   /quicksilver bench mobs confirm
   ```

   A new world each time, in every instance, so all three start from the same freshly generated terrain; `flythrough`
   first: it flies over terrain that was never generated, which is where upload spikes and stutter show. Quit to the title screen after the last one.
3. Paste back the five bench blocks of each instance from `logs/latest.log` (or send the JSON files from
   `quicksilver/bench/`), saying which instance each came from.

What to compare: `fps` (average and 1% low), `frame time ms` (p99 and frames over 50 ms; the floor is 1% lows of at least
30 FPS), `upload bytes/frame` (its max is the largest upload in one frame), and `heap MB`. In the Quicksilver instance the
`backFaces` and `uploadBudget` lines say how many triangles were left out and how many sections waited for a later frame.
Draw calls are counted where the game draws through its own graphics layer (renderpearl); a mod that draws past it would
show fewer draw calls than it makes, so for Sodium compare frame times and FPS first.
