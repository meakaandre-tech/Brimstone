# Benching on the school laptop

Windows, Intel integrated graphics, Prism Launcher. Brimstone 0.0.1 only measures, so every number from this build is
a **vanilla** number: the baseline later packages are judged against.

Run two instances: a plain one (vanilla Fabric, Fabric API, Brimstone) and the SIFTEC one with Brimstone added.

## Before each session

- Plug the charger in and set Windows to its best-performance power mode.
- Close the browser and anything else heavy.
- Keep the game window focused and don't touch mouse or keyboard while a bench is measuring (the chat says when it
  starts and ends). Right-click only when a HUB or MAM scene asks you to.

## Get Brimstone

Download `brimstone-26.3.jar` from the `latest` release of meakaandre-tech/Brimstone on GitHub.

## Instance 1: plain

1. In Prism: **Add Instance** → Minecraft **26.3** → Mod loader **Fabric**, loader **0.19.5** → OK.
2. Instance → **Edit** → **Mods** → **Download mods** → add **Fabric API** for 26.3 (0.161.0+26.3 or newer).
   Then **Add file** → `brimstone-26.3.jar`.
3. Edit → **Settings** → Java: use a **Java 25** runtime (Prism can download it). Memory: set max memory to what you
   normally give Minecraft and write it down.
4. Edit → Settings → **Game windows**: tick "Start Minecraft with a custom window size", **1280 × 720**.
5. Launch. In Options → Video Settings: **Max Framerate: Unlimited**, **VSync: Off**, **GUI Scale: 2**, Graphics API
   left on **Default**. Leave the rest as you play.
6. **Singleplayer → Create New World**: Game Mode **Creative**, **Allow Commands: On**, More → **Seed: 12345**,
   World Type Default. Create it and wait 30 seconds in the world.
7. Type these one at a time, waiting for "[Brimstone] Bench ... done" before the next (each takes about 1.5 minutes).
   A scene changes the world (gamerules, time, weather, game mode, your position, blocks and mobs it places), so the
   bench refuses until you add `confirm`; without it, it tells you what it will change:

   ```
   /brimstone bench flythrough confirm
   /brimstone bench spawn confirm
   /brimstone bench water_glass confirm
   /brimstone bench mobs confirm
   /brimstone bench nether confirm
   ```

   Run `flythrough` first: it flies over terrain that has never been generated, which is only true in a new world.
8. Memory after 10 minutes (one run is enough): `/brimstone bench spawn 600 confirm`.
9. Quit to the title screen.

## Instance 2: SIFTEC

1. Copy your SIFTEC instance in Prism (right-click → **Copy Instance**) so your own worlds and settings stay apart.
2. In the copy: Edit → Mods → **Add file** → `brimstone-26.3.jar`. Same Java, memory and 1280 × 720 window as above;
   same video settings (Unlimited, VSync off, GUI scale 2).
3. **Create a new world** just for this: Creative, **Allow Commands: On**, **Seed: 12345**. The scenes build
   platforms and place a HUB and a MAM, so never run them in a world you play in.
4. Wait 30 seconds, then run, one at a time:

   ```
   /brimstone bench flythrough confirm
   /brimstone bench spawn confirm
   /brimstone bench sift confirm
   /brimstone bench hub confirm
   /brimstone bench mam confirm
   /brimstone bench water_glass confirm
   /brimstone bench mobs confirm
   /brimstone bench nether confirm
   ```

   `hub` and `mam` stand you in front of the block and ask you to right-click it. Measuring starts when the screen
   opens. Leave it open: the bench closes it when it is done.

   `factory` and `map` need SIFTEC's selftest mod (its test commands build the factory row and open the map), which
   SIFTEC's release doesn't include, so the bench won't offer them in a normal SIFTEC instance. If you drop
   `siftec-selftest-26.3.jar` into the mods folder, also run `/brimstone bench factory confirm` and
   `/brimstone bench map confirm`.
5. Memory after 10 minutes: `/brimstone bench sift 600 confirm`.

## What to paste back

Open the instance folder (Prism: right-click → **Folder**) → `.minecraft/logs/latest.log` (or `minecraft/logs`).
Each bench wrote one block:

```
===== BRIMSTONE BENCH BEGIN =====
  scene: ...
  ...
===== BRIMSTONE BENCH END =====
```

Paste every block, and say which instance it came from and the max memory you set. The same numbers are also in
`.minecraft/brimstone/bench/*.json` if a block is easier to send as a file.

If a scene says it stopped (for example "the HubScreen screen was not opened within 2 minutes"), paste that line too.
