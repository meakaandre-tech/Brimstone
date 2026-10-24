# CI limits and baselines

Set in package 0, when Brimstone draws nothing of its own: every difference between two runs is noise. Frozen when
package 0 merges; from then on a limit is never raised and a baseline only goes down after a real improvement.

## Profiles

Every scene runs in each suite (core: plain world, Fabric API and Brimstone only; SIFTEC: SIFTEC's pack and server)
as: vanilla twice (`vanilla-a`, `vanilla-b`), vanilla old-GPU, vanilla Vulkan, and Brimstone in the same three GPU
profiles. **full**: Mesa llvmpipe OpenGL 4.5, every extension. **old-GPU**: multi-draw indirect and buffer storage
hidden, GL forced to 3.3 (vanilla's minimum); the Brimstone run also sets `brimstone.debug.forceIntelMdiIssue`, which
applies vanilla's Intel multi-draw-indirect workaround flag. **Vulkan**: Mesa lavapipe through
`--graphicsBackend vulkan`. Brimstone full is compared with vanilla-a, old-GPU with vanilla old-GPU, Vulkan with
vanilla Vulkan.

## Pixel limits (`ci/pixel-limits.json`)

A screenshot's difference is the share of pixels whose largest RGB channel differs by more than 8 (of 255): small
enough to catch a wrong block, colour or shade, large enough to ignore rounding.

**Noise samples.** Each CI run gives four per scene: vanilla a against vanilla b, and each Brimstone profile against
its vanilla run. With Brimstone doing nothing, all four are run-to-run noise. Only runs after a scene's last setup
change count (listed below; every sample is in `ci/pixel-noise-samples.json`). Runs where the SIFTEC suite broke for
a reason outside the picture (run 8: SIFTEC's test commands left its release) are left out for the scenes it broke.

**Limit = max(2 x largest sample, largest sample + 0.25, 0.1)**, rounded up to 0.05. Doubling leaves room for
noise larger than the samples showed; the +0.25 floor keeps near-zero scenes from failing on a single stray pixel
cluster. Water and glass and the nether were recomputed on the last package-0 run when new vanilla-vs-vanilla
samples came in larger than the earlier ones (water and glass 3.3% to 6.45%, nether 0.7% to 0.8%).

| scene | runs | samples | largest % | limit % |
|---|---|---|---|---|
| core/flythrough | 4, 5, 6, 7, 8, 10 | 19 | 7.4623 | 14.95 |
| core/mobs | 4, 5, 6, 7, 8, 10 | 19 | 0.1465 | 0.4 |
| core/nether | 4, 5, 6, 7, 8, 10 | 19 | 0.385 | 0.8 |
| core/spawn | 12 | 4 | 5.0239 | 10.05 |
| core/water_glass | 4, 5, 6, 7, 8, 10 | 19 | 3.2233 | 6.45 |
| siftec/factory | 5, 6, 7 | 9 | 0.0344 | 0.3 |
| siftec/hub | 10 | 4 | 4.36 | 8.75 |
| siftec/mam | 4, 5, 6, 7, 10 | 16 | 4.6265 | 9.3 |
| siftec/map | 6, 7 | 6 | 5.6602 | 11.35 |
| siftec/sift | 7, 8, 10 | 10 | 0.5553 | 1.15 |
| core/occlusion (package 2) | 40079570978 | 4 | 0.0 | 0.25 |
| core/occlusion_move (package 2) | 40091787069 | 4 | 0.0 | 0.25 |
| core/occlusion_move.x2297 (package 2) | 40091787069 | 4 | 0.0 | 0.25 |
| core/occlusion_move.x2305 (package 2) | 40091787069 | 4 | 0.0 | 0.25 |
| core/occlusion_move.x2309 (package 2) | 40091787069 | 4 | 0.0068 | 0.3 |
| core/occlusion_move.open (package 2) | 40091787069 | 4 | 0.0 | 0.25 |
| core/occlusion_move.closed (package 2) | 40091787069 | 4 | 0.0 | 0.25 |
| core/occlusion_move.bare_x2300 (package 2) | 40091787069 | 4 | 0.0148 | 0.3 |
| core/occlusion_move.bare_x2297 (package 2) | 40091787069 | 4 | 0.0073 | 0.3 |
| core/occlusion_move.bare_x2305 (package 2) | 40091787069 | 4 | 0.0 | 0.25 |
| core/occlusion_move.bare_x2309 (package 2) | 40091787069 | 4 | 0.0 | 0.25 |
| core/occlusion_move.bare_open (package 2) | 40091787069 | 4 | 0.0133 | 0.3 |

The occlusion_move shots (package 2) were sampled in the collection run 40091787069, also in dry run. The happy
ghast, whose tentacles animate, appears only in the last shot (`ghast`), so its animation adds noise to no other
shot.

The core occlusion scene (package 2) was sampled in run 40079570978 with Brimstone's visibility module in dry run
(deciding and counting but hiding nothing, so Brimstone drew exactly vanilla's set): vanilla a/b and all three
Brimstone profiles differed on 0.0% of pixels, so the rule's floor applies (0 + 0.25).

The SIFTEC factory and map limits come from earlier runs; both scenes are skipped while SIFTEC's test commands are
missing from its release (they need the `siftec_selftest` mod), so they aren't checked now.

Where the noise comes from: animated water and kelp (fly-through, spawn, water and glass; their animation phase
differs per run), the HUB and MAM screens (fade-in timing). Clouds are turned off in CI's client options because
their position follows elapsed game time, which differs between runs. To keep the picture steady CI also: pins time
and weather in each dimension (The Sift has its own clock and weather), uses mobs without random variants,
pregenerates the SIFTEC areas the scenes look at, loads every core run from one world generated once per CI run
(`ci/core-world.sh`, job `core-world`; package 1: before that each run generated its own, and feature placement at
chunk borders depends on generation order, so trees differed between vanilla a and b and spawn noise reached 18.6%;
with the shared world it is 0.36%; limits unchanged), presses F3+A before each world screenshot and waits for two
captures 4 s apart to agree, and parks the mouse for screen scenes. All of it is the same in every run.

## Strict pixel limits (`ci/strict-pixel-limits.json`, package 1)

The 8/255 threshold above ignores small shifts. The strict check doesn't: per scene and Brimstone profile it counts
the pixels where **all four vanilla runs (GL 4.5 a and b, GL 3.3, Vulkan) agree exactly** but the Brimstone run
differs from its own profile's vanilla run **at all** (threshold 0). Over the limit fails. Like the other limits these
may only go down, never up.

**Noise samples.** The same count for vanilla against itself: vanilla b against a (and a against b) on the pixels
where the other three vanilla runs agree, plus the counts of Brimstone builds that hold every vertex exactly
(a7aff90, ae203ca, 14479f5). Core scenes count from the shared core world on (a7aff90; before it, spawn noise was
129,368 pixels); SIFTEC scenes from every run with screenshots (82ba60c onwards).

**Limit = the largest sample x 1.5, rounded up to 100 pixels.** Package 2's scenes, where a wrongly hidden object may
cover only a few hundred pixels, use a finer rule: the largest sample x 1.5, rounded up to 10 pixels, and at least 10
pixels. The floor guards against single-pixel flakes. Every object that Brimstone could hide wrongly in a shot must
cover more on screen than that shot's limit. That was measured against shots of the same views with every object
removed (the `bare_*` shots); see docs/progress.md, package 2. core/occlusion (all samples 0 in three runs) got 20
pixels: the smallest hideable object seen there, the iron golem in the doorway, covers about 200.

| scene | vanilla b/a samples (pixels) | Brimstone samples, max | limit |
|---|---|---|---|
| core/spawn | 2842/3674, 12000/1090, 811/5490 | 3338 | 18000 |
| core/flythrough | 1487/492, 672/3877, 342/1100 | 906 | 5900 |
| core/water_glass | 2566/16964, 12740/1330, 4798/1040 | 1336 | 25500 |
| core/nether | after the lightmap pin: 230/80; before it, on the side where the flicker matched: 216, 27, 96, 339, 26, 549, 36, 250 | 4 | 900 |
| core/mobs | 304/268, 382/126, 30/104 | 105 | 600 |
| core/occlusion (package 2) | 0/0 in runs 40079570978, 40081970862, 40083026856 | 0 (dry run, all three profiles) | 20 (was 100; lowered in package 2's review fixes) |
| core/occlusion_move (package 2) | 0/0 (run 40091787069) | 0 (dry run) | 10 |
| core/occlusion_move.x2297 (package 2) | 0/0 (run 40091787069) | 0 (dry run) | 10 |
| core/occlusion_move.x2305 (package 2) | 0/0 (run 40091787069) | 0 (dry run) | 10 |
| core/occlusion_move.x2309 (package 2) | 0/16 (run 40091787069) | 19 (dry run) | 30 |
| core/occlusion_move.open (package 2) | 0/0 (run 40091787069) | 0 (dry run) | 10 |
| core/occlusion_move.closed (package 2) | 0/0 (run 40091787069) | 0 (dry run) | 10 |
| core/occlusion_move.bare_x2300 (package 2) | 0/0 (run 40091787069) | 60 (dry run) | 90 |
| core/occlusion_move.bare_x2297 (package 2) | 0/0 (run 40091787069) | 43 (dry run) | 70 |
| core/occlusion_move.bare_x2305 (package 2) | 0/0 (run 40091787069) | 0 (dry run) | 10 |
| core/occlusion_move.bare_x2309 (package 2) | 0/0 (run 40091787069) | 0 (dry run) | 10 |
| core/occlusion_move.bare_open (package 2) | 0/0 (run 40091787069) | 69 (dry run) | 110 |
| SIFTEC/sift | 530/323, 547/723, 805/965, 793/991, 2218/3223, 2963/29, 800/40, 379/3072 | 2099 | 4900 |
| SIFTEC/hub | 40/8030, 17/1679, 8244/40, 52/1205, 56/8, 2450/1692, 8280/36, 8276/832 | 1536 | 12500 |
| SIFTEC/mam | 40/8012, 17/1177, 6566/86, 52/1204, 56/8, 1386/49, 103392/36, 8258/832 | 95075 | 155100 |

Where the strict noise comes from: water and lava animation phase (spawn, fly-through, water and glass), the screen
fade of SIFTEC's HUB and MAM screens (MAM shows two fade states about 95,000 pixels apart), and, in the nether until
package 1, the lightmap's block-light flicker. The flicker is random per tick and shifts every block-lit pixel by one
colour level, and lava lights most of the nether scene: vanilla runs disagreed with each other on up to 349,419 of
those pixels (vanilla b against a). Since package 1, CI's lightmap pin (`ci/pin-mod`, a CI-only mod loaded in every
client run, vanilla and Brimstone alike) holds the flicker at zero, its resting value. With it the four vanilla runs
agree on 98.7% of the nether frame, against 22-71% before. The one-level nether shift the package 1 re-review saw
(248,492 pixels) was this flicker; vanilla b against a shows the same one-sided one-level shift in earlier runs.

Since the package 2 merge, the same pin mod also holds every animated texture (water, lava, fire, portals) on its
first frame, in every client run. Main's CI on the package 2 merge failed on the fly-through's strict gate in the
Vulkan profile (7,384 pixels against 5,900, with full at 5,653 and old-GPU at 5,152 in the same run): a dithered band
along the water surface at the horizon, where the vanilla runs happened to share an animation frame and the
Brimstone run caught another. Water is still drawn by vanilla, so this was noise; the limit wasn't raised, the cause
was pinned. The strict limits set before this pin stay as they are (they now have more room). Package 8 (animated
textures only when seen) needs its own check with animation running.

## Count baselines (`ci/baselines/<suite>-<profile>.json`)

From the bench in each Brimstone run (20 s per scene in CI). **Draw calls**: per-frame average (frames that line up
with a game tick draw about 26 more, so the median flips between two values; the average held within 0.1%).
**Uploads and bytes**: per-frame median (chunk uploads arrive in bursts, so averages moved 20-35% between identical
runs). **Memory**: heap used after a GC at the end of the bench, and java.nio direct buffers.

**Baseline = the highest value across the clean runs listed.** CI fails when a count goes over **baseline x 1.10
plus a step allowance**: 2 draw calls, 1 upload, 16 KiB, 16 MB heap, 4 MB direct (direct buffers grow in 2 MB steps
and land on either side between runs). The fly-through is gated on memory only: its uploads and draw calls depend on
how fast CI's server generates the new terrain. The Vulkan baselines come from two runs; the core spawn scene's limit and baselines from one run (its camera moved to a fixed height in the last package-0 run).

| suite-profile | scene | draw calls/frame (avg) | uploads/frame (median) | bytes/frame (median) | heap after GC MB | direct MB | runs |
|---|---|---|---|---|---|---|---|
| core-full | flythrough | 218.16 | 61 | 2109248 | 324.25 | 4.24 | 4,5,6,7,8,10,11 |
| core-full | mobs | 240.07 | 17 | 1010948 | 268.37 | 6.99 | 4,5,6,7,8,10,11 |
| core-full | nether | 215.94 | 12 | 19648 | 273.53 | 5.99 | 4,5,6,7,8,10,11 |
| core-full | spawn | 218.25 | 14 | 22476 | 259.07 | 3.99 | 12 |
| core-full | occlusion_move (package 2, memory gated only) | 159.53 | 15 | 104812 | 265.74 | 4.24 | 40091787069 (dry run) |
| core-full | occlusion (package 2) | 159.53 | 15 | 126320 | 265.1 | 4.24 | 40079570978 (dry run) |
| core-full | water_glass | 219.1 | 15 | 15576 | 250.02 | 5.99 | 4,5,6,7,8,10,11 |
| core-old-gpu | flythrough | 1146.26 | 57 | 1988104 | 324.79 | 5.99 | 4,5,6,7,8,10,11 |
| core-old-gpu | mobs | 434.39 | 16 | 1015892 | 268.23 | 5.99 | 4,5,6,7,8,10,11 |
| core-old-gpu | nether | 687.04 | 11 | 10656 | 272.57 | 5.99 | 4,5,6,7,8,10,11 |
| core-old-gpu | spawn | 942.31 | 13 | 6372 | 256.29 | 3.73 | 12 |
| core-old-gpu | occlusion_move (package 2, memory gated only) | 169.58 | 14 | 102572 | 265.66 | 5.99 | 40091787069 (dry run) |
| core-old-gpu | occlusion (package 2) | 169.7 | 14 | 124172 | 264.64 | 4.24 | 40079570978 (dry run) |
| core-old-gpu | water_glass | 489.1 | 14 | 10264 | 249.83 | 5.99 | 4,5,6,7,8,10,11 |
| core-vulkan | flythrough | 218.25 | 62 | 2258432 | 318.06 | 4.23 | 10,11 |
| core-vulkan | mobs | 240.09 | 17 | 1008176 | 264.31 | 6.23 | 10,11 |
| core-vulkan | nether | 216.09 | 12 | 19912 | 268.95 | 4.23 | 10,11 |
| core-vulkan | spawn | 218.94 | 15 | 27764 | 257.2 | 3.98 | 12 |
| core-vulkan | occlusion_move (package 2, memory gated only) | 160.14 | 15 | 104756 | 265.16 | 4.23 | 40091787069 (dry run) |
| core-vulkan | occlusion (package 2) | 161.13 | 15 | 126356 | 264.78 | 3.98 | 40079570978 (dry run) |
| core-vulkan | water_glass | 219.09 | 15 | 15108 | 244.39 | 4.23 | 10,11 |
| siftec-full | hub | 295.96 | 18 | 67484 | 451.33 | 4.24 | 10,11 |
| siftec-full | mam | 340.12 | 21 | 24656 | 471.8 | 8.12 | 4,5,6,7,10,11 |
| siftec-full | sift | 424.41 | 19 | 55764 | 440.07 | 5.87 | 7,8,10,11 |
| siftec-old-gpu | hub | 342.2 | 17 | 66484 | 451.46 | 5.99 | 10,11 |
| siftec-old-gpu | mam | 400.79 | 20 | 22916 | 472.87 | 5.99 | 4,5,6,7,10,11 |
| siftec-old-gpu | sift | 849.48 | 18 | 73228 | 441.24 | 4.24 | 7,8,10,11 |
| siftec-vulkan | hub | 316.68 | 18 | 67484 | 450.49 | 4.23 | 10,11 |
| siftec-vulkan | mam | 352.85 | 21 | 24656 | 462.56 | 5.46 | 10,11 |
| siftec-vulkan | sift | 424.25 | 19 | 55980 | 439.9 | 4.23 | 10,11 |

The occlusion scene's baselines (package 2) come from one run with Brimstone's visibility module in dry run, i.e. with
Brimstone drawing exactly vanilla's entities and block entities.

Run-to-run spread of the old-GPU draw calls is wider (up to about 15% in mobs and water and glass) because vanilla
draws each visible section separately there and the count follows how far meshing has got.

Meshing time per section is measured since package 1 (the bench's `meshing` block: vanilla's mesher plus Brimstone's
transcode, sections meshed while measuring) and shown in CI's report; it is not gated (it follows CI's CPU load).

## Counter audit

The counting mixins have their own config (not required, `defaultRequire: 0`), so a counter that no longer fits the
game is skipped instead of crashing it. The config's plugin records which counting handlers each loaded class
received; every bench report lists missing ones (and classes not loaded because their backend isn't in use), and CI
fails if any is missing.

The `occlusion_move.ghast` shot (package 2) was removed before package 2 merged: the happy ghast's tentacle animation
made vanilla's own noise reach 239 strict pixels against its 240 limit, and the shot tested nothing Brimstone hides.
Removed rather than raised; no limit was raised.

### Draw-call baselines after the texture-animation pin

Holding animated textures still also stops the game redrawing them into the block atlas every frame, so every run
(vanilla and Brimstone) makes fewer draw calls. Left alone, the draw-call baselines would have had that much extra
room and the 10% gate would have missed a regression of the same size. Each draw-call baseline was lowered by
exactly the drop measured for the same scene and profile between two runs of the same Brimstone code without and
with the pin (package 2 run 40094673590 against run 40104120800), so each keeps the margin it had. No baseline went
up; other counts are unchanged.

| suite-profile | scene | before | drop | after |
|---|---|---|---|---|
| core-full | flythrough | 218.16 | 95.08 | 123.08 |
| core-full | mobs | 240.07 | 23.85 | 216.22 |
| core-full | nether | 215.94 | 39.38 | 176.56 |
| core-full | occlusion | 159.53 | 63.1 | 96.43 |
| core-full | occlusion_move | 159.53 | 63.02 | 96.51 |
| core-full | spawn | 218.25 | 95.16 | 123.09 |
| core-full | water_glass | 219.1 | 36.63 | 182.47 |
| core-old-gpu | flythrough | 1146.26 | 104.63 | 1041.63 |
| core-old-gpu | mobs | 434.39 | 24.2 | 410.19 |
| core-old-gpu | nether | 687.04 | 46.27 | 640.77 |
| core-old-gpu | occlusion | 169.7 | 63.08 | 106.62 |
| core-old-gpu | occlusion_move | 169.58 | 63.02 | 106.56 |
| core-old-gpu | spawn | 942.31 | 98.11 | 844.2 |
| core-old-gpu | water_glass | 489.1 | 31.3 | 457.8 |
| core-vulkan | flythrough | 218.25 | 95.28 | 122.97 |
| core-vulkan | mobs | 240.09 | 31.49 | 208.6 |
| core-vulkan | nether | 216.09 | 74.07 | 142.02 |
| core-vulkan | occlusion | 161.13 | 63.18 | 97.95 |
| core-vulkan | occlusion_move | 160.14 | 62.89 | 97.25 |
| core-vulkan | spawn | 218.94 | 94.1 | 124.84 |
| core-vulkan | water_glass | 219.09 | 65.81 | 153.28 |
| siftec-full | hub | 295.96 | 179.37 | 116.59 |
| siftec-full | mam | 340.12 | 182.09 | 158.03 |
| siftec-full | sift | 424.41 | 268.59 | 155.82 |
| siftec-old-gpu | hub | 342.2 | 179.26 | 162.94 |
| siftec-old-gpu | mam | 400.79 | 178.41 | 222.38 |
| siftec-old-gpu | sift | 849.48 | 179.21 | 670.27 |
| siftec-vulkan | hub | 316.68 | 179.7 | 136.98 |
| siftec-vulkan | mam | 352.85 | 181.32 | 171.53 |
| siftec-vulkan | sift | 424.25 | 273.94 | 150.31 |
