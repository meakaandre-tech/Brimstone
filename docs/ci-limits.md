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
| core/spawn | 4, 5, 6, 7, 8, 10 | 19 | 2.3583 | 4.75 |
| core/water_glass | 4, 5, 6, 7, 8, 10 | 19 | 3.2233 | 6.45 |
| siftec/factory | 5, 6, 7 | 9 | 0.0344 | 0.3 |
| siftec/hub | 10 | 4 | 4.36 | 8.75 |
| siftec/mam | 4, 5, 6, 7, 10 | 16 | 4.6265 | 9.3 |
| siftec/map | 6, 7 | 6 | 5.6602 | 11.35 |
| siftec/sift | 7, 8, 10 | 10 | 0.5553 | 1.15 |

The SIFTEC factory and map limits come from earlier runs; both scenes are skipped while SIFTEC's test commands are
missing from its release (they need the `siftec_selftest` mod), so they aren't checked now.

Where the noise comes from: animated water and kelp (fly-through, spawn, water and glass; their animation phase
differs per run), the HUB and MAM screens (fade-in timing). Clouds are turned off in CI's client options because
their position follows elapsed game time, which differs between runs. To keep the picture steady CI also: pins time
and weather in each dimension (The Sift has its own clock and weather), uses mobs without random variants,
pregenerates the SIFTEC areas the scenes look at, presses F3+A before each world screenshot and waits for two
captures 4 s apart to agree, and parks the mouse for screen scenes. All of it is the same in every run.

## Count baselines (`ci/baselines/<suite>-<profile>.json`)

From the bench in each Brimstone run (20 s per scene in CI). **Draw calls**: per-frame average (frames that line up
with a game tick draw about 26 more, so the median flips between two values; the average held within 0.1%).
**Uploads and bytes**: per-frame median (chunk uploads arrive in bursts, so averages moved 20-35% between identical
runs). **Memory**: heap used after a GC at the end of the bench, and java.nio direct buffers.

**Baseline = the highest value across the clean runs listed.** CI fails when a count goes over **baseline x 1.10
plus a step allowance**: 2 draw calls, 1 upload, 16 KiB, 16 MB heap, 4 MB direct (direct buffers grow in 2 MB steps
and land on either side between runs). The fly-through is gated on memory only: its uploads and draw calls depend on
how fast CI's server generates the new terrain. The Vulkan baselines come from one run so far.

| suite-profile | scene | draw calls/frame (avg) | uploads/frame (median) | bytes/frame (median) | heap after GC MB | direct MB | runs |
|---|---|---|---|---|---|---|---|
| core-full | flythrough | 218.09 | 61 | 2109248 | 324.25 | 4.24 | 4,5,6,7,8,10 |
| core-full | mobs | 240.07 | 17 | 1010948 | 268.37 | 6.99 | 4,5,6,7,8,10 |
| core-full | nether | 215.94 | 12 | 19648 | 273.53 | 5.99 | 4,5,6,7,8,10 |
| core-full | spawn | 220.84 | 15 | 36532 | 263.05 | 5.99 | 4,5,6,7,8,10 |
| core-full | water_glass | 219.1 | 15 | 15576 | 250.02 | 5.99 | 4,5,6,7,8,10 |
| core-old-gpu | flythrough | 1146.26 | 57 | 1988104 | 324.79 | 5.99 | 4,5,6,7,8,10 |
| core-old-gpu | mobs | 433.84 | 16 | 1015892 | 268.23 | 5.99 | 4,5,6,7,8,10 |
| core-old-gpu | nether | 687.04 | 11 | 10656 | 272.57 | 5.99 | 4,5,6,7,8,10 |
| core-old-gpu | spawn | 992.09 | 14 | 13604 | 263.27 | 5.99 | 4,5,6,7,8,10 |
| core-old-gpu | water_glass | 489.1 | 14 | 10264 | 249.83 | 5.99 | 4,5,6,7,8,10 |
| core-vulkan | flythrough | 218.09 | 36 | 651160 | 317.92 | 4.23 | 10 |
| core-vulkan | mobs | 182.57 | 17 | 993288 | 263.99 | 6.23 | 10 |
| core-vulkan | nether | 187.27 | 12 | 19088 | 268.95 | 4.23 | 10 |
| core-vulkan | spawn | 219.96 | 15 | 28992 | 260.21 | 4.23 | 10 |
| core-vulkan | water_glass | 192.52 | 15 | 14828 | 244.39 | 4.23 | 10 |
| siftec-full | hub | 295.96 | 18 | 67484 | 451.0 | 4.24 | 10 |
| siftec-full | mam | 340.12 | 21 | 24656 | 471.8 | 8.12 | 4,5,6,7,10 |
| siftec-full | sift | 424.41 | 19 | 55764 | 439.89 | 5.87 | 7,8,10 |
| siftec-old-gpu | hub | 342.2 | 17 | 66484 | 451.46 | 5.99 | 10 |
| siftec-old-gpu | mam | 400.79 | 20 | 22916 | 472.87 | 5.99 | 4,5,6,7,10 |
| siftec-old-gpu | sift | 849.48 | 18 | 47292 | 441.24 | 4.12 | 7,8,10 |
| siftec-vulkan | hub | 295.62 | 18 | 67484 | 450.49 | 3.46 | 10 |
| siftec-vulkan | mam | 299.27 | 21 | 24656 | 462.56 | 5.46 | 10 |
| siftec-vulkan | sift | 424.05 | 19 | 55980 | 439.9 | 3.46 | 10 |

Run-to-run spread of the old-GPU draw calls is wider (up to about 15% in mobs and water and glass) because vanilla
draws each visible section separately there and the count follows how far meshing has got.

Meshing time per section is not measured yet: there is no Brimstone mesher. The bench's JSON has a `meshing` field
for it.

## Counter audit

The counting mixins have their own config (not required, `defaultRequire: 0`), so a counter that no longer fits the
game is skipped instead of crashing it. The config's plugin records which counting handlers each loaded class
received; every bench report lists missing ones (and classes not loaded because their backend isn't in use), and CI
fails if any is missing.
