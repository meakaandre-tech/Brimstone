# CI limits and baselines

Set in package 0, when Brimstone draws nothing of its own: every difference between two runs is noise.

## Pixel limits (`ci/pixel-limits.json`)

A screenshot's difference is the share of pixels whose largest RGB channel differs by more than 8 (of 255): small
enough to catch a wrong block, colour or shade, large enough to ignore rounding.

**Noise samples.** Each CI run gives three per scene: vanilla run a against vanilla run b, Brimstone full against
vanilla a, and Brimstone old-GPU against vanilla old-GPU. With Brimstone doing nothing, all three are run-to-run
noise. Only runs after a scene's last setup change count (listed below; every sample is in
`ci/pixel-noise-samples.json`).

**Limit = max(2 x largest sample, largest sample + 0.25, 0.1)**, rounded up to 0.05. Doubling leaves room for
noise larger than the few samples showed; the +0.25 floor keeps near-zero scenes (factory, mobs) from failing on a
single stray pixel cluster.

| scene | runs | samples | largest % | limit % |
|---|---|---|---|---|
| core/flythrough | 4, 5, 6, 7 | 12 | 7.4623 | 14.95 |
| core/mobs | 4, 5, 6, 7 | 12 | 0.1287 | 0.4 |
| core/nether | 4, 5, 6, 7 | 12 | 0.3415 | 0.7 |
| core/spawn | 4, 5, 6, 7 | 12 | 2.3583 | 4.75 |
| core/water_glass | 4, 5, 6, 7 | 12 | 1.6292 | 3.3 |
| siftec/factory | 5, 6, 7 | 9 | 0.0344 | 0.3 |
| siftec/hub | 4, 5, 6, 7 | 12 | 4.415 | 8.85 |
| siftec/mam | 4, 5, 6, 7 | 12 | 4.4551 | 8.95 |
| siftec/map | 6, 7 | 6 | 5.6602 | 11.35 |
| siftec/sift | 7 | 3 | 0.5553 | 1.15 |

Where the noise comes from: animated water and kelp (fly-through, spawn, water and glass; their animation phase
differs per run), HUB and MAM screens (screen fade-in timing), the map (tile shading). Clouds are turned off in CI's
client options because their position follows elapsed game time, which differs between runs. To keep the picture
steady CI also: pins time and weather in each dimension (The Sift has its own clock and weather), uses mobs without
random variants, builds the factory row once and lets its lighting settle, pregenerates the SIFTEC areas the scenes
look at, presses F3+A before each world screenshot and waits for two captures 4 s apart to agree, and parks the mouse
for screen scenes. All of it is the same in every run.

Sample counts are small for the map (6) and The Sift (3); if either flakes, widen it from the new samples rather
than loosening the rule.

## Count baselines (`ci/baselines/<suite>-<profile>.json`)

From the bench in each Brimstone run (20 s per scene in CI). Per frame, the **median** is gated, not the average:
chunk uploads arrive in bursts, so averages moved 20-35% between identical runs while medians held. Memory is heap
used after a GC at the end of the bench, and java.nio direct buffers.

**Baseline = the highest value across clean runs 4-7.** CI fails when a count goes over **baseline x 1.10 plus a
step allowance**: 2 draw calls, 1 upload, 16 KiB, 16 MB heap, 4 MB direct (direct buffers grow in 2 MB steps and land
on either side between runs). The fly-through is gated on memory only: its uploads and draw calls depend on how fast
CI's server generates the new terrain.

| suite-profile | scene | draw calls/frame | uploads/frame | upload bytes/frame | heap after GC MB | direct MB |
|---|---|---|---|---|---|---|
| core-full | flythrough | 205 | 61 | 2109248 | 324.25 | 4.24 |
| core-full | mobs | 252 | 17 | 1010948 | 268.37 | 5.99 |
| core-full | nether | 202 | 12 | 19648 | 273.53 | 5.99 |
| core-full | spawn | 207 | 15 | 29084 | 263.05 | 5.99 |
| core-full | water_glass | 205 | 15 | 15424 | 250.02 | 5.99 |
| core-old-gpu | flythrough | 1149 | 57 | 1988104 | 324.79 | 5.99 |
| core-old-gpu | mobs | 423 | 16 | 1015892 | 268.23 | 5.99 |
| core-old-gpu | nether | 673 | 11 | 10656 | 272.57 | 5.99 |
| core-old-gpu | spawn | 1002 | 14 | 13492 | 263.27 | 5.99 |
| core-old-gpu | water_glass | 501 | 14 | 10004 | 249.83 | 5.99 |
| siftec-full | factory | 315 | 14 | 6508 | 424.03 | 5.81 |
| siftec-full | hub | 411 | 19 | 282500 | 489.95 | 8.06 |
| siftec-full | mam | 398 | 21 | 24548 | 471.8 | 8.12 |
| siftec-full | map | 435 | 18 | 85432 | 515.99 | 8.12 |
| siftec-full | sift | 396 | 19 | 68852 | 442.84 | 5.87 |
| siftec-old-gpu | factory | 388 | 14 | 49688 | 424.54 | 5.48 |
| siftec-old-gpu | hub | 1490 | 18 | 287332 | 490.7 | 5.99 |
| siftec-old-gpu | mam | 410 | 20 | 22868 | 472.87 | 5.99 |
| siftec-old-gpu | map | 959 | 18 | 79044 | 515.01 | 5.99 |
| siftec-old-gpu | sift | 825 | 18 | 47676 | 442.86 | 5.35 |

Meshing time per section is not measured yet: there is no Brimstone mesher. The bench's JSON has a `meshing` field
for it.

The old-GPU draw calls are 2-5x the full profile's because vanilla itself draws each chunk section separately when
multi-draw indirect is missing.
