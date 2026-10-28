# Quicksilver

A standalone optimisation mod for Minecraft 26.3 on Fabric: drop it into any instance and the game should run faster
and look exactly the same. It starts with a rewrite of the renderer and later grows to cover what the other
optimisation mods do. It is built to run on old integrated graphics as well as new machines.

Work in progress, built one work package at a time from its design doc. It was called Brimstone until 2026-10-28 (that
name was taken on Modrinth); the repo's history and `docs/progress.md` keep the old name. Releases: the `latest` release
has `quicksilver-26.3.jar` (the mod) and `quicksilver-bench-26.3.jar` (only the bench, to measure vanilla or Sodium the
same way; never next to Quicksilver).

## Using it

Needs Fabric Loader 0.19.5 or newer and Fabric API. Client-side; it also loads harmlessly on a server. Quicksilver
replaces Sodium's job and is Fabric's block renderer itself (in place of Indigo), so it does not load next to Sodium or
another Fabric Renderer API renderer. `config/quicksilver.properties`, `block-renderer=off` (needs a restart) leaves
every block and item to vanilla's own code (with vanilla's own smooth lighting, which differs slightly from Indigo's).

- `/quicksilver bench [scene] [seconds]` (or `/qs bench ...`) measures frame rate, frame times, draw calls, uploads and memory, and writes
  the results to `latest.log` and `<game dir>/quicksilver/bench/`. With no scene (or `here`) it measures where you
  stand; scenes set up a fixed view and need commands allowed (single player with cheats on, or op). The scene list
  is in `src/main/resources/quicksilver/scenes/`.

## Tested with

Every build is checked in CI against vanilla, pixel by pixel, in a plain world (only Fabric API and Quicksilver) and in
the SIFTEC modpack, with a full and an old-GPU driver profile. See `docs/`.

## Licence

Quicksilver is free software under the GNU Lesser General Public License, version 3 (see `LICENSE`), which builds on the
GNU General Public License, version 3 (see `COPYING`).
