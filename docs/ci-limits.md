# CI limits and baselines

Set in package 0, when Quicksilver draws nothing of its own: every difference between two runs is noise. Frozen when
package 0 merges; from then on a limit is never raised and a baseline only goes down after a real improvement.

## Profiles

Every scene runs in each suite (core: plain world, Fabric API and Quicksilver only; SIFTEC: SIFTEC's pack and server)
as: vanilla twice (`vanilla-a`, `vanilla-b`), vanilla old-GPU, vanilla Vulkan, and Quicksilver in the same three GPU
profiles. **full**: Mesa llvmpipe OpenGL 4.5, every extension. **old-GPU**: multi-draw indirect and buffer storage
hidden, GL forced to 3.3 (vanilla's minimum); the Quicksilver run also sets `quicksilver.debug.forceIntelMdiIssue`, which
applies vanilla's Intel multi-draw-indirect workaround flag. **Vulkan**: Mesa lavapipe through
`--graphicsBackend vulkan`. Quicksilver full is compared with vanilla-a, old-GPU with vanilla old-GPU, Vulkan with
vanilla Vulkan.

## Quick CI and per-branch logs (design doc, Quick CI, 2026-10-26)

Branch pushes run **quick mode**: the core suite only, with vanilla-a, vanilla-b and quicksilver-old (about 10
minutes). Quicksilver's old-GPU run is compared with vanilla-a (vanilla's GL 3.3 and GL 4.5 pictures differ no more
than vanilla a and b: on run 40233004611, 0 to 82 pixels over 8/255 per shot against 0 to 1,295 for a/b), and the
strict check counts the pixels where vanilla a and b agree. Every limit and baseline is the full mode's. Quick mode
leaves out the measure runs (so the same-run draw-call gate, the terrain GPU memory gate and the measure runs'
pictures), the full and Vulkan profiles and the SIFTEC suite; the report says so at the top. **Full mode** (every
suite and run, every gate) runs on main, on a commit with `[full]` in its message (every pre-merge check) and on a
manual run (workflow_dispatch). A package merges only on a green full run. Each branch publishes its logs to its
own `ci-logs-<branch>` branch (`/` becomes `-`; main's are on `ci-logs-main`; the old `ci-logs` branch is no
longer written). `ci/compare.py` reads the mode from `CI_MODE` (`quick`, else full).

Equivalence of full mode: the new compare.py and the one before it gave identical failures, data and reports on
run 40233004611's results, and on a copy with injected failures (wrong pictures in three profiles and a measure run,
a missing measure bench, a translucency miss); a full CI run of the change is in docs/progress.md.

## Smallest thing each scene must catch (standing rules, 2026-10-26)

Noise that vanilla shows against itself may be re-collected by the documented rule (largest sample x 1.5 over at
least 3 runs) even upward, but each scene's strict limit must stay below the smallest thing it has to catch. Sizes
are on-screen pixels at 1280x720, measured on run 40233004611's vanilla-a screenshots (a block face counted as the
square of its edge in pixels at the subject's distance). Other gates that catch the same failure are named.

| scene | what it must catch | smallest such thing on screen | strict limit | below? |
|---|---|---|---|---|
| core/spawn | a missing, misplaced or wrongly lit terrain section | one section on the near cliff (blocks ~25 px): ~100,000; at mid distance (blocks ~8 px): ~16,000 | 18,057 | near: yes; mid-distance section: **no** |
| core/flythrough | a missing or stale section while moving | a section at mid distance (blocks ~3 px): ~2,300 | 6,020 | **no** (the visibility pop-in check gates missing section layers exactly) |
| core/water_glass | a wrong water, glass or translucent block | one block face (~44 px edge): ~1,900 | 25,508 | **no** (the per-frame translucency check gates order and sort exactly) |
| core/nether | a wrong block or light on a netherrack face | one block face (~50 px): ~2,500 | 905 | yes |
| core/mobs | a missing or wrongly drawn mob | a pig ~1,200; a chicken ~480 | 10 (was 600; 2026-10-28, chicken wing pin) | yes, a chicken too |
| core/occlusion | a wrongly hidden entity or block entity | the iron golem in the doorway ~200 (package 2) | 22 | yes |
| core/occlusion_move (+ shots) | a wrongly hidden object while moving | each object larger than its shot's limit, measured with the bare shots (package 2) | 10 to 110 | yes |
| core/translucent_move (+ shots) | a stained-glass block out of order | one pane block face (~34 px): ~1,150 | 13 to 41 | yes |
| core/water_glass_oit | a wrong translucent block with Improved Transparency | one block face: ~1,900 | 57 | yes |
| core/translucent_move_oit (+ shots) | as translucent_move, with Improved Transparency | one pane block face: ~1,150 | 10 to 40 | yes |
| core/particles (+ bare) | a missing, misplaced or wrongly drawn particle (package 8) | of the 49 particles visible in the shot, measured against the `bare` shot without them: **smallest 9 pixels**, next 16, median about 200 | 4 (Andre, 2026-10-27; was 10) | yes, every particle |
| core/animation (+ behind, front) | an animated block or particle showing another frame than vanilla's (package 8) | the lantern ~600; a block face ~2,500 | 10 | yes |
| core/animated_items | an animated sprite outside terrain (hotbar, held block, item frame, block display, fire on a mob) showing another frame than vanilla's | the item frame's magma block (~20 px square, ~400) | 10 | yes |
| siftec/factory | a wrongly drawn Create machine part | a machine ~4,800; one ladder strip ~320 | 500 | machine yes; one ladder strip alone **no** |
| siftec/sift | a wrong block or light in the Sift terrain | a block face at mid distance (~24 px): ~580 | 100 | yes |
| siftec/hub | a wrong GUI element or text line (package 7) | a line of text ~2,000; one glyph ~20-40 | 10 (was 12,506) | yes |
| siftec/mam | a wrong item icon or GUI element (package 7) | one item icon (32 px): ~1,000 | 10 (was 155,106) | yes |
| siftec/map | a wrong map pixel, icon or text (package 7) | one map pixel (12 x 12): 144; an icon (22 px): ~300 | 10 (was not gated) | yes |
| siftec/ponder | a wrong item icon or text on Create's Ponder index (package 7) | one item icon (32 px): ~1,000 | 10 | yes |
| core/outline (+ bare) | a missing or wrong crosshair block outline (package 7) | the outline: 2,318 pixels; one edge ~200-400 | 10 | yes |
| siftec/hologram | a missing or wrong outline line | one outline edge ~1,200 | 413 | yes |
| core/flat_plants (package 4) | a plant kind lit wrongly with smooth lighting off | one row of one plant kind ~1,500 or more; one flower ~150 | 350 | row yes; one flower **no** (a wrong light rule hits every plant of a kind) |
| core/renderer_off (package 4) | a block drawn wrongly with the block renderer off | one block face (~35 px): ~1,200 | 10 | yes |
| siftec/factory_flat (package 4) | a wrongly flat-lit Create or SIFTEC part | as factory: a machine ~4,800; one ladder strip ~320 | 40 | yes |
| siftec/create_ct (package 4) | a wrong connected-texture border | one block's border strip (~38 px by 3): ~110 | 10 | yes |

The **no** rows are limits already set (by the rule and Andre's earlier decisions) that are larger than the smallest
thing the scene shows; they are listed for Andre, not changed. Package 7 made the hub and mam scenes quiet and gave
the map a strict gate (see "GUI scenes, the block outline and Ponder" below).

## Pack lock (`ci/pack-lock.txt`, `ci/pack-check.sh`; Andre, 2026-10-30)

Every jar CI downloads is listed with its sha256 (`<sha256>  <set>/<jar>`; sets `core`: Fabric API, `siftec`: SIFTEC's
server mods, i.e. SIFTEC, its self-test, MapMod, Create Fly, Create: New Age, Create Diesel Generators, Traveler's
Backpack, Fabric API and the pack mods from SIFTEC's `.github/smoke-pack-mods`, `sodium`: the bench-sodium run).
`ci/pack-check.sh` runs in the core and SIFTEC world jobs (before any client run), on each client run's server folder,
on the bench-sodium run's Sodium and in the server scripts; a jar that differs or isn't listed fails the run with
"pack changed: <jar>" (and a listed jar missing from a complete set). Each client run writes the hashes it saw to
`pack-sha256.txt` in its logs. Both world caches are keyed on the lock.

Why: SIFTEC's jars come from `latest`-style release tags that are rebuilt on every build. MapMod's changed between
main's run 40827558273 and package 4c's full runs (a round minimap, about 97 more GUI draws per frame in vanilla's runs
as much as Quicksilver's, and a noisier map scene), and the gates failed with no change in Quicksilver.

**MapMod, while it is being finished (Andre, 2026-10-30):** locked as `*` (any build). The map and hud scenes are
skipped (`skip` in `scenes/siftec.json`); factory_flat and hologram, where its minimap shows, leave the minimap's screen
area out of the strict check (`ciStrictMask`, 1056-1280 x 0-240) and gate draw calls against vanilla in the same run
only (`ciGatedCounts` without draw calls; vanilla's own draws rose by the same ~97 GUI draws with the round-minimap
build). Its compass strip (also MapMod's) shows in most SIFTEC scenes and stays fully checked. When MapMod is finished:
its hash goes back in the lock, the map and hud scenes come back with their numbers re-collected, and the strict check
covers the minimap again.

Updating the lock is a deliberate commit: it names the changed jars and re-collects the numbers they affect (baselines
and limits, by the rules above, from at least 3 clean runs on the new jars, with vanilla's numbers as the reason).

## Pixel limits (`ci/pixel-limits.json`)

A screenshot's difference is the share of pixels whose largest RGB channel differs by more than 8 (of 255): small
enough to catch a wrong block, colour or shade, large enough to ignore rounding.

**Noise samples.** Each CI run gives four per scene: vanilla a against vanilla b, and each Quicksilver profile against
its vanilla run. With Quicksilver doing nothing, all four are run-to-run noise. Only runs after a scene's last setup
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
| siftec/hub | 10 (package 7: 40254531258, 40260765722, 40260847921) | 4 (package 7: 12, all 0) | 4.36 (package 7: 0) | 8.75 (package 7: **0.25**) |
| siftec/mam | 4, 5, 6, 7, 10 (package 7: as hub) | 16 (package 7: 12, all 0) | 4.6265 (package 7: 0) | 9.3 (package 7: **0.25**) |
| siftec/map | 6, 7 (package 7: as hub) | 6 (package 7: 12, all 0) | 5.6602 (package 7: 0) | 11.35 (package 7: **0.25**) |
| siftec/ponder (package 7; its final setup) | 40273668937, 40276630375, 40288414898 | 12 | 0 | 0.25 |
| core/outline, outline.bare (package 7) | 40254531258, 40260765722, 40260847921 (bare: 40260847921) | 12 (bare 4) | 0 | 0.25 |
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

Package 3's scenes (`translucent_move` and `translucent_move_oit`, each with six fixed-point shots, and
`water_glass_oit`) were sampled in the marked collection run 40115084815, with Quicksilver's translucency in dry run
(translucent terrain left with vanilla; solid and cutout still Quicksilver's), so all four samples per shot are noise.
Every sample was at most 0.001%, so the rule's floor applies (0.3). See the table below for the strict gate.

| scene / shot (package 3) | samples (vanilla a/b, full, old-GPU, Vulkan; %) | limit % |
|---|---|---|
| core/translucent_move | 0.0001, 0.0001, 0.0001, 0.0002 | 0.3 |
| core/translucent_move.x2404 / .x2414 / .x2424 | at most 0.0003 | 0.3 |
| core/translucent_move.low | 0.0004, 0.0004, 0.0004, 0.0009 | 0.3 |
| core/translucent_move.back / .bare_back | at most 0.0002 | 0.3 |
| core/translucent_move_oit (all seven shots) | at most 0.001 | 0.3 |
| core/water_glass_oit | 0.0037, 0.003, 0.0033, 0.0035 | 0.3 |
| siftec/hologram (run 40123696828; not run on Vulkan) | 0.0027 (vanilla a/b), 0.2629 (full), 0.238 (old-GPU) | 0.55 |

Package 4's scenes (smooth lighting off, the block renderer switched off, Create connected textures). Quicksilver's
block lighting is Indigo's own code, and a diagnostic run (40274749083) found Quicksilver's block renderer and Indigo's
identical on every block of the measure runs; so Quicksilver's samples below are the 16-byte vertex's rounding (package
3, Andre's decision), not lighting. Samples per run: vanilla a/b; Quicksilver full, old-GPU, Vulkan; the measure runs
(Indigo) were 0.0 in every run but factory_flat in 40279156080 (0.0041 full). Limit by the rule above.

| scene (package 4) | runs | samples (%) | limit % |
|---|---|---|---|
| core/flat_plants | 40270542386 (quick), 40274749083, 40279156080, 40287221751 | a/b 0.0 in all four; Quicksilver 0.023, 0.023 / 0.023 / 0.0226 (each full run) | 0.3 |
| core/renderer_off | the same four | 0.0 everywhere | 0.25 |
| siftec/factory_flat | 40274749083, 40279156080, 40287221751 | a/b 0.0, 0.0041, 0.0; Quicksilver 0.0 but 0.0041 (full, 40279156080) | 0.3 |
| siftec/create_ct (the GUI hidden from 40279156080 on) | 40279156080, 40287221751 (third run: the final run of the branch) | 0.0 everywhere (before the GUI was hidden, run 40274749083: a/b 0.0532, all in SIFTEC's minimap) | 0.25 |

The SIFTEC `hologram` scene (package 3) was sampled in run 40123696828, with only the SIFTEC suite in translucency
dry run. It is not run in the two Vulkan runs (`ciSkipRuns`): Mesa's lavapipe segfaults as soon as Create's outliner
draws the hologram, in vanilla's Vulkan run as much as Quicksilver's, so its samples and limits cover the other three
profiles, and its strict gate uses the three other vanilla runs.

The occlusion_move shots (package 2) were sampled in the collection run 40091787069, also in dry run. The happy
ghast, whose tentacles animate, appears only in the last shot (`ghast`), so its animation adds noise to no other
shot.

The core occlusion scene (package 2) was sampled in run 40079570978 with Quicksilver's visibility module in dry run
(deciding and counting but hiding nothing, so Quicksilver drew exactly vanilla's set): vanilla a/b and all three
Quicksilver profiles differed on 0.0% of pixels, so the rule's floor applies (0 + 0.25).

The SIFTEC factory and map limits come from earlier runs; both scenes were skipped while SIFTEC's test commands were
missing from its release (they need the `siftec_selftest` mod). They run again since 2026-10-25 (see below); the map's
limits were re-collected in package 7.

Where the noise comes from: animated water and kelp (fly-through, spawn, water and glass; their animation phase
differs per run), the HUB and MAM screens (fade-in timing, it was thought; package 7 found the player's random name
and skin and the MAM tab's glint). Clouds are turned off in CI's client options because
their position follows elapsed game time, which differs between runs. To keep the picture steady CI also: pins time
and weather in each dimension (The Sift has its own clock and weather), uses mobs without random variants,
pregenerates the SIFTEC areas the scenes look at, loads every core run from one world generated once per CI run
(`ci/core-world.sh`, job `core-world`; package 1: before that each run generated its own, and feature placement at
chunk borders depends on generation order, so trees differed between vanilla a and b and spawn noise reached 18.6%;
with the shared world it is 0.36%; limits unchanged), presses F3+A before each world screenshot and waits for two
captures 4 s apart to agree, and parks the mouse for screen scenes. All of it is the same in every run.

## Strict pixel limits (`ci/strict-pixel-limits.json`, package 1)

The 8/255 threshold above ignores small shifts. The strict check doesn't: per scene and Quicksilver profile it counts
the pixels where **all four vanilla runs (GL 4.5 a and b, GL 3.3, Vulkan) agree exactly** but the Quicksilver run
differs from its own profile's vanilla run **by more than 2/255 in any channel** (threshold 0 until package 3; see
"The 16-byte vertex" below). Over the limit fails. Like the other limits these may only go down, never up, except by
Andre's decisions recorded here.

**Noise samples.** The same count for vanilla against itself: vanilla b against a (and a against b) on the pixels
where the other three vanilla runs agree, plus the counts of Quicksilver builds that hold every vertex exactly
(a7aff90, ae203ca, 14479f5). Core scenes count from the shared core world on (a7aff90; before it, spawn noise was
129,368 pixels); SIFTEC scenes from every run with screenshots (82ba60c onwards).

**Limit = the largest sample x 1.5, rounded up to 100 pixels.** Package 2's scenes, where a wrongly hidden object may
cover only a few hundred pixels, use a finer rule: the largest sample x 1.5, rounded up to 10 pixels, and at least 10
pixels. The floor guards against single-pixel flakes. Every object that Quicksilver could hide wrongly in a shot must
cover more on screen than that shot's limit. That was measured against shots of the same views with every object
removed (the `bare_*` shots); see docs/progress.md, package 2. core/occlusion (all samples 0 in three runs) got 20
pixels: the smallest hideable object seen there, the iron golem in the doorway, covers about 200.

| scene | vanilla b/a samples (pixels) | Quicksilver samples, max | limit |
|---|---|---|---|
| core/spawn | 2842/3674, 12000/1090, 811/5490 | 3338 | 18000 |
| core/flythrough | 1487/492, 672/3877, 342/1100 | 906 | 5900 |
| core/water_glass | 2566/16964, 12740/1330, 4798/1040 | 1336 | 25500 |
| core/nether | after the lightmap pin: 230/80; before it, on the side where the flicker matched: 216, 27, 96, 339, 26, 549, 36, 250 | 4 | 900 |
| core/mobs | 304/268, 382/126, 30/104; with the chicken wing pin 0/0 in runs 40471457759, 40481430766, 40516650792, 40526712029 | 105; with the pin 0 (every Quicksilver and measure run, all four runs) | **10** (was 600; finer rule, 2026-10-28) |
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
| core/translucent_move (package 3) | 0/1 (run 40115084815) | 2 (dry run) | 10 |
| core/translucent_move.x2404 / .x2414 / .x2424 (package 3) | at most 3 | at most 3 (dry run) | 10 |
| core/translucent_move.low (package 3) | 2/4 | 11 (dry run, Vulkan) | 20 |
| core/translucent_move.back / .bare_back (package 3) | at most 3 | at most 4 (dry run) | 10 |
| core/translucent_move_oit, .x2404, .x2414, .x2424, .back, .bare_back (package 3) | at most 6 | at most 5 (dry run) | 10 |
| core/translucent_move_oit.low (package 3) | 7/4 | 7 (dry run) | 20 |
| core/water_glass_oit (package 3) | 23/14 | 15 (dry run) | 40 |
| SIFTEC/hologram (package 3) | over eleven shots in run 40155387068: 221-234 / 9-22 (the largest, 234, is mostly SIFTEC's minimap); earlier 7/4 (run 40139105024), 15/10 (run 40123696828, before the pin) | 63 (dry run, eleven shots, run 40155387068) | 360 (21840 before the batch order pin; 30 from the first, under-sampled re-collection) |
| SIFTEC/factory (strict limit from 2026-10-25) | 0/0 in most runs; **315/0** (run 40156711337 attempt 2: a vanilla-only flip, see below) | 2 (dry run); real runs 0 or 313-315 | 500 (100 from the first, single-run collection) |
| SIFTEC/map | before package 7's pins: 6960/8712, 17712/5856, 13276/6048, 8352/4028, 22532/6288 (runs 40155387068 to 40162905190); with them 0/0 in runs 40254531258, 40260765722, 40260847921 | 3168 (dry run); with the pins 0 (measure runs 0; 144 in run 40249146134's measure-old run, before mob griefing was turned off) | **10** (package 7; not gated before, `ciNoStrict`) |
| SIFTEC/sift | with the Sift particle pin, threshold >2/255: 0/0, 0/0, 0/0 (runs 40185974374, 40187878826, 40189806229); before: 530/323, 547/723, 805/965, 793/991, 2218/3223, 2963/29, 800/40, 379/3072 (threshold 0) | 55-61 (16-byte vertex: the rounding extra, not noise) | 100 (4900 before the pin) |
| SIFTEC/hub | before package 7's pins: 40/8030, 17/1679, 8244/40, 52/1205, 56/8, 2450/1692, 8280/36, 8276/832; with them 0/0 in runs 40249146134, 40254531258, 40260765722, 40260847921 | 1536; with the pins 0 (measure runs 0) | **10** (package 7; was 12500, then 12506) |
| SIFTEC/mam | before package 7's pins: 40/8012, 17/1177, 6566/86, 52/1204, 56/8, 1386/49, 103392/36, 8258/832; with them 0/0 in the same four runs | 95075; with the pins 0 (measure runs 0) | **10** (package 7; was 155100, then 155106) |
| core/flat_plants (package 4) | 0/0 in runs 40274749083, 40279156080, 40287221751 | 225 / 225 / 217 (full / old-GPU / Vulkan) in each of the three; quick run 40270542386 old-GPU 248; measure runs 0 | 350 (finer rule: 10 for the noise, plus 1.5 x the rounding extra 225, rounded up to 10) |
| core/renderer_off (package 4) | 0/0 in the same three | 0 | 10 |
| SIFTEC/factory_flat (package 4) | 0/0, 0/26, 0/0 (runs 40274749083, 40279156080, 40287221751) | 0 | 40 (26 x 1.5, rounded up to 10) |
| SIFTEC/create_ct (package 4; the GUI hidden from 40279156080 on) | 0/0, 0/0 (runs 40279156080, 40287221751; the third run is the branch's final run); before: 32/167 (minimap) | 0 | 10 (floor) |
| SIFTEC/ponder (package 7) | final setup: 0/0 in runs 40273668937, 40276630375, 40288414898 (earlier placements: 0/0) | final setup: 0 in every Quicksilver and measure run of those three runs (earlier placements: 3-5 pixels of the minimap in every Quicksilver and measure run, 425 and 578 once; see below) | **10** |
| core/outline, outline.bare (package 7) | 0/0 in runs 40249146134 to 40260847921 (bare: 40260847921) | 0 (measure runs 0) | **10** |

Package 3's shots use package 2's finer rule (largest sample x 1.5, rounded up to 10 pixels, at least 10). The
smallest translucent element tested there, the single ice block in front of the stained-glass stripes, covers
8,148-8,157 pixels on screen in every vanilla run (3,429-3,433 differ by more than 8/255), measured between the
`back` and `bare_back` shots (the same view without that block): far above the strict limit of 10. Every other
element (the stripes, the tank, the panes, the ice inside the water) is larger.

The hologram scene was an exception until 2026-10-25: Create's outliner drew the Build Gun hologram in one of two
brightness states (the far edges 3.6% darker or not), so its first strict limit (21,840) was larger than the whole
outline (about 14,500 pixels) and the strict gate could not see a wrong outline. Probe runs (40135570271,
40138168127) found the cause: the game's outline feature phase groups submits into batches by render type in a map
whose order follows identity hash codes, so from one game start to the next the outliner's translucent checkered
faces were drawn before or after its solid edges; when the edges came first, the far edges (seen through the faces)
were blended under them. The outliner's own state (colour, light, alpha, pose, normals) and the lightmap's inputs were
identical in both states. Since then the CI pin mod (`FeatureBatchOrderMixin`) keeps those batches in the order they
were first submitted, in every client run, vanilla and Quicksilver alike: all vanilla runs now draw the outline the
same way (they agree on 100% of the hologram frame), and the strict limit was re-collected by the rule above in a
SIFTEC noise collection run (40139105024): vanilla b/a 7/4, Quicksilver dry run 15 (full) and 18 (old-GPU), so
18 x 1.5 = 27, rounded up to 30. That came from one shot per run and was too few samples: vanilla's own water
changes by 15-27 pixels from one shot to the next in the same run, and SIFTEC's minimap differs between runs. So the
limit was re-collected (with Andre's OK, 2026-10-25) over eleven shots of the scene per run, 0.5 s plus a steady-shot
wait apart, in run 40155387068 (dry run): vanilla b/a 221-234 and 9-22 (the 234 is mostly the minimap's tiles plus
scattered water pixels), Quicksilver dry run up to 63 (full) and 24 (old-GPU): 234 x 1.5 = 351, rounded up to 10:
**360**. The outline covers about 14,500 pixels, about 3,900 of them over Quicksilver's water.

The crosshair outline: the outline the game draws around the block under the crosshair moved by one pixel row from
one frame to the next in the old-GPU profile (GL 3.3), in vanilla's own runs too (run 40152673881: 2 of 11 hologram
shots in vanilla's old-GPU run, 5 of 11 in Quicksilver's, with the same draws and the same target block in every frame),
so the shot decided about 100-230 pixels along the hologram's bottom edge. Since 2026-10-25 the CI pin mod
(`BlockOutlineMixin`) leaves that outline out of every client run, vanilla and Quicksilver alike (with it, all eleven
shots in every profile agreed there, run 40154022133). **The block outline is therefore untested in CI**; package 6
or 7 must add a scene that checks it with the outline drawn.

The SIFTEC factory and map scenes run again since SIFTEC's latest release carries its selftest jar (2026-10-25); they
had no strict limits (skipped since package 1). Factory's first limit (100, run 40155387068 alone) was under-sampled: in about one run in
four, a yellow-and-black striped part of one Create machine in the factory is in another phase (its angle or
texture follows time), in vanilla's runs too (vanilla b against a: 315 pixels in run 40156711337 attempt 2;
Quicksilver 313-315 in runs 40162905190 and 40166776073). By the rule, 315 x 1.5 = 472.5, rounded up to 100: **500**
(Andre's OK, 2026-10-25). Package 5 (Flywheel and Create) makes that animation repeatable in CI, vanilla and Quicksilver
alike, then re-collects and lowers this limit. The map's first limit (13,100, one run) was under-sampled: vanilla runs differ from each
other there by up to 22,532 pixels, because SIFTEC's map shows whichever tiles it has scanned by the time it opens.
Two attempts to make it steady from the client side (Andre's time box) did not: waiting until two captures 5 s apart
were identical (run 40161774743: every run steady at once, vanilla runs still 37-44k pixels apart) and standing 60 s
in the area before opening (run 40162905190: 35-58k apart). So, as Andre decided (fallback (b)), the map has **no
strict gate** (`"ciNoStrict"` on the scene; compare.py reports its counts); its 8/255 limit (11.35%) stays.
**Package 7 (GUI) must give the map real strict coverage.** Done: see "GUI scenes, the block outline and Ponder" below.

Where the strict noise comes from: water and lava animation phase (spawn, fly-through, water and glass), what was
thought to be the screen fade of SIFTEC's HUB and MAM screens (MAM showed two states about 95,000 pixels apart;
package 7 found the player's random name and skin and the MAM tab's glint, now pinned), and, in the nether until
package 1, the lightmap's block-light flicker. The flicker is random per tick and shifts every block-lit pixel by one
colour level, and lava lights most of the nether scene: vanilla runs disagreed with each other on up to 349,419 of
those pixels (vanilla b against a). Since package 1, CI's lightmap pin (`ci/pin-mod`, a CI-only mod loaded in every
client run, vanilla and Quicksilver alike) holds the flicker at zero, its resting value. With it the four vanilla runs
agree on 98.7% of the nether frame, against 22-71% before. The one-level nether shift the package 1 re-review saw
(248,492 pixels) was this flicker; vanilla b against a shows the same one-sided one-level shift in earlier runs.

Since the package 2 merge, the same pin mod also holds every animated texture (water, lava, fire, portals) on its
first frame, in every client run. Main's CI on the package 2 merge failed on the fly-through's strict gate in the
Vulkan profile (7,384 pixels against 5,900, with full at 5,653 and old-GPU at 5,152 in the same run): a dithered band
along the water surface at the horizon, where the vanilla runs happened to share an animation frame and the
Quicksilver run caught another. Water is still drawn by vanilla, so this was noise; the limit wasn't raised, the cause
was pinned. The strict limits set before this pin stay as they are (they now have more room). Package 8 (animated
textures only when seen) needs its own check with animation running.

### The 16-byte vertex: strict threshold 2/255 and new limits (decided by Andre, 2026-10-25)

Package 3 moved every terrain layer to one 16-byte vertex: positions off the 1/2048-block grid and UVs off the
1/65536 grid are rounded (largest error 0.000245 block and 1.45e-5 UV, at most 0.06 px on a 1280x720 screen; on-grid
vertices are bit-identical, shader harness). The rounding moves interpolated colours by 1-2/255 on rounded geometry
(water surfaces above all): at threshold 0 that added up to about 2,700 strict pixels in water_glass and 4,500 in
the hologram scene, almost all off by exactly 1/255, nothing visible (vanilla next to Quicksilver at 4x, plant-heavy
and water-edge views, sent to Andre). **Andre's decision:** the strict gate counts a pixel only when it differs by
more than 2/255 in any channel, still only where every vanilla run agrees exactly; each scene's extra under that
threshold is measured, and **new strict limit = old limit + 1.5 x the extra**. The 8/255 gate is unchanged. The count
at any difference is still reported next to the gated one.

How the extra was measured: per scene and profile, the strict count (>2/255) in the five runs of the exact 28/16-byte
build before the switch (`a9444ac` 40164365265, `2e3f85f` 40166776073, `99fe3bd` 40167492584, `5298f1d`
40168745352, `998a7a0` 40169188320) and in the five runs with the 16-byte vertex (`1dd6880` 40170948997, `49fec37`
40172887031, `629f023` 40177279502, `9a3d411` 40180107484 attempts 1 and 2); extra = median after - median before,
the largest over the profiles. The median, not the largest single run, because single runs on both sides carry
run-wide noise events that vanilla shows too (spawn in run 40180107484 attempt 1: 2,228-2,309 in all three Quicksilver
profiles at once, where vanilla b against a reached 1,350-1,582 in other runs; hub: vanilla b against a up to 9,812):
the largest-run method would have raised spawn by 2,834 and hub by 1,638 on noise alone. Every one of the ten runs
passes the new limits. (The agent chose the median while Andre was away; recorded at the top of docs/progress.md.)

| scene | old limit | extra (median after - before) | largest single run after | new limit |
|---|---|---|---|---|
| core/flythrough | 5900 | 80 (vulkan: 18 before, 98 after) | 115 | **6020** |
| core/mobs | 600 | -31 (old-gpu: 94 before, 63 after) | 317 | **600** |
| core/nether | 900 | 3 (vulkan: 0 before, 3 after) | 164 | **905** |
| core/occlusion | 20 | 1 (vulkan: 0 before, 1 after) | 1 | **22** |
| core/occlusion_move | 10 | 0 (vulkan: 0 before, 0 after) | 0 | **10** |
| core/occlusion_move.bare_open | 110 | 0 (vulkan: 0 before, 0 after) | 0 | **110** |
| core/occlusion_move.bare_x2297 | 70 | 0 (vulkan: 0 before, 0 after) | 0 | **70** |
| core/occlusion_move.bare_x2300 | 90 | 0 (vulkan: 0 before, 0 after) | 0 | **90** |
| core/occlusion_move.bare_x2305 | 10 | 0 (vulkan: 0 before, 0 after) | 0 | **10** |
| core/occlusion_move.bare_x2309 | 10 | 0 (vulkan: 0 before, 0 after) | 0 | **10** |
| core/occlusion_move.closed | 10 | 0 (vulkan: 0 before, 0 after) | 0 | **10** |
| core/occlusion_move.open | 10 | 0 (vulkan: 0 before, 0 after) | 0 | **10** |
| core/occlusion_move.x2297 | 10 | 0 (vulkan: 0 before, 0 after) | 0 | **10** |
| core/occlusion_move.x2305 | 10 | 0 (vulkan: 0 before, 0 after) | 0 | **10** |
| core/occlusion_move.x2309 | 30 | 1 (old-gpu: 0 before, 1 after) | 21 | **32** |
| core/spawn | 18000 | 38 (vulkan: 278 before, 316 after) | 2309 | **18057** |
| core/translucent_move | 10 | 2 (vulkan: 0 before, 2 after) | 3 | **13** |
| core/translucent_move.back | 10 | 2 (old-gpu: 1 before, 3 after) | 4 | **13** |
| core/translucent_move.bare_back | 10 | 2 (vulkan: 1 before, 3 after) | 3 | **13** |
| core/translucent_move.low | 20 | 14 (old-gpu: 3 before, 17 after) | 20 | **41** |
| core/translucent_move.x2404 | 10 | 3 (vulkan: 1 before, 4 after) | 7 | **15** |
| core/translucent_move.x2414 | 10 | 2 (vulkan: 1 before, 3 after) | 7 | **13** |
| core/translucent_move.x2424 | 10 | 2 (old-gpu: 0 before, 2 after) | 4 | **13** |
| core/translucent_move_oit | 10 | 1 (vulkan: 1 before, 2 after) | 3 | **12** |
| core/translucent_move_oit.back | 10 | 1 (old-gpu: 0 before, 1 after) | 5 | **12** |
| core/translucent_move_oit.bare_back | 10 | 3 (old-gpu: 0 before, 3 after) | 5 | **15** |
| core/translucent_move_oit.low | 20 | 13 (vulkan: 3 before, 16 after) | 17 | **40** |
| core/translucent_move_oit.x2404 | 10 | 4 (vulkan: 1 before, 5 after) | 6 | **16** |
| core/translucent_move_oit.x2414 | 10 | 0 (old-gpu: 2 before, 2 after) | 3 | **10** |
| core/translucent_move_oit.x2424 | 10 | 3 (old-gpu: 0 before, 3 after) | 4 | **15** |
| core/water_glass | 25500 | 5 (vulkan: 14 before, 19 after) | 33 | **25508** |
| core/water_glass_oit | 40 | 11 (old-gpu: 11 before, 22 after) | 27 | **57** |
| siftec/factory | 500 | 0 (vulkan: 0 before, 0 after) | 247 | **500** |
| siftec/hologram | 360 | 35 (old-gpu: 15 before, 50 after) | 244 | **413** |
| siftec/hub | 12500 | 4 (vulkan: 24 before, 28 after) | 1518 | **12506** |
| siftec/mam | 155100 | 4 (vulkan: 24 before, 28 after) | 575 | **155106** |

Scenes not listed kept their limits (extra 0 or below: mobs, factory, every `occlusion_move` shot but `x2309`,
`translucent_move_oit.x2414`). The map stayed ungated (`ciNoStrict`) until package 7 gave it a strict limit (see "GUI scenes, the block outline and
Ponder").

**sift** (re-collected, Andre's decision): its strict noise was the Sift dimension's floating note particles
(`the_sift:sift_note`, random places in every run, vanilla's too); mob spawning off plus sending the mobs away did not
remove them, the CI pin mod's `SiftParticlesMixin` (no particles in the Sift dimension, every client run) did. Samples
under >2/255 with the pin, runs 40185974374, 40187878826 and 40189806229: vanilla b/a 0/0 in each and the measure
runs 0, so vanilla's noise is 0; Quicksilver 55 / 55 / 61 (full / old-GPU / Vulkan) in each, the 16-byte vertex's
rounding (decision 3's extra). Limit = noise + 1.5 x that extra = 91.5, rounded up to 100 pixels as for the other
SIFTEC scenes: **100** (was 4,900). Samples in `ci/pixel-noise-samples.json`.

### GUI scenes, the block outline and Ponder (package 7, 2026-10-26)

**Where the hub and mam noise came from** (vanilla a against b, run 40260847921 of package 4's branch and package 7's
first run): the dev launcher names the player `Player<random digits>`. The offline UUID made from that name picks the
default skin, so the first-person arm in the lower right was a different skin in every run (about 40,000 pixels),
and SIFTEC prints the name on both screens (`<player>'s Company`; a different number of digits moves the rest of the
line). The MAM's selected tab carries an enchantment glint, whose texture offset follows the wall clock (about 450
pixels). The "two fade states" seen earlier were these. **Pins** (`ci/client-run.sh`, every client run, vanilla and
Quicksilver alike): `--username QuicksilverCI`, `glintSpeed:0.0` (the glint is still drawn, but holds still).

**The map.** Its tiles differed pixel by pixel (one map pixel is 4 x 4 blocks, 12 x 12 screen pixels): the area round
300 -200 was generated during each run, and SIFTEC's map records a chunk's top blocks when it loads, so trees at chunk
borders (which depend on generation order) and whatever loaded in time differed; the markers SIFTEC's test command
places sit relative to where `spreadplayers` landed the player, a block either way. **Pins:** the CI world
(`ci/siftec-world.sh`) pre-generates blocks 96..511 x -400..-1 (what the map shows at its scale, with room); the scene
puts the player on the ground at 300.5 -199.5; the CI world has random ticks off (`random_tick_speed 0`: water freezing
over, grass and leaves changing) and mob griefing off (`mob_griefing false`: one map pixel changed colour in run
40249146134's measure-old run, a mob changing a block there).

**Samples** (strict, >2/255): vanilla b/a and a/b 0/0 for hub, mam, map and outline in all four runs with the pins
(40249146134, 40254531258, 40260765722, 40260847921); vanilla a, b and GL 3.3 identical pixel for pixel on these four
scenes; every Quicksilver and measure run 0. The strict check needs all four vanilla runs to agree: on hub and map the
Vulkan run agrees on 99.8% and 99.99% of the frame; on mam on 88-99.8% (Vulkan and some GL runs differ by 1/255 on
the sky and floor behind the screen, never on the screen itself). **Limits** by package 2's finer rule (the smallest
thing here, a glyph, covers a few dozen pixels): largest sample x 1.5, rounded up to 10, at least 10: **10** for hub
(was 12,506), mam (was 155,106), map (was not gated; `ciNoStrict` removed and compare.py's allow-list emptied),
outline and outline.bare. 8/255 limits by package 0's rule (max(2 x largest, largest + 0.25, 0.1)): **0.25** (hub
was 8.75, mam 9.3, map 11.35). All lowered or new.

**The block outline** (core scene `outline`): CI's pin mod (`BlockOutlineMixin`) leaves the crosshair outline out of
every scene except round the scene's stone block at 2680 200 -1988, so CI checks it again. Creative mode (no outline
in spectator); the `bare` shot is the same view in adventure mode, where the empty hand cannot break stone and the
game draws no outline: the outline covers 2,318 pixels (every one over 8/255; one edge about 200-400), Vulkan draws
its back edge a row off (163 pixels the strict check leaves out). The old-GPU row flicker that led to the pin did not
show in any of the four runs.

**Ponder** (SIFTEC scene `ponder`): Create's Ponder index, opened with Create's client command (`/ponder index`, typed
in chat by drive.py in every run: `ciChat`). The Ponder scenes themselves keep moving (belts, kinetics, the nixie
scene's dancing parrot), so the index is what CI checks. Pins: `menuBackgroundBlurriness:0` in every run (the menu
blur came out differently on Vulkan: 433,312 pixels over 2/255 against GL, which agreed exactly; Vulkan also darkens
the world behind the index differently, so with the world behind it the strict check saw 42% of the frame); the camera
looks straight up at the sky (above the HUB platform), with one stone block above it so Quicksilver has terrain to draw
(CI requires that in every scene), so only sky and that block are behind the screen (99.3% of the frame covered,
run 40266027605). The scene runs before the map scene, so SIFTEC's minimap and map markers are not on yet: after it,
each Quicksilver and measure run showed the map scene's markers twice (each bench sets the map up again: 578 pixels in
run 40266027605), and over the HUB platform the minimap's east edge showed tiles of chunks generated during the run
(425 pixels in run 40260847921's old-GPU run). Samples of the final setup (from `0dd5438`: runs 40273668937, 40276630375, 40288414898): vanilla b/a and a/b 0/0,
every Quicksilver and measure run 0 strict pixels and 0% over 8/255, 99.69% of the frame covered. Earlier placements
(not used for the limits): 3-5 pixels of the minimap in every Quicksilver and measure run, the same in measure runs.
Limits by the finer rule: **10** strict; 8/255 by package 0's rule: **0.25**.

**The HUD scene** (SIFTEC `hud`, after the rebase on main): normal play in creative with nine hotbar items, the chat
open (its last 6 lines, `chatHeightFocused:0.25`), SIFTEC's compass strip and minimap, on a gray concrete platform high
above 400 -110 facing a gray concrete wall. Pins for it: the pin mod holds a focused text box's cursor on; drive.py
clears the chat before the setup (`ciClearChat`; bench messages had moved the chat's scrollbar, 48 pixels) and after
the fills reloads all chunks and clears the chat again (the `#chunks` step; a vanilla stand-in run once showed a hole
in the wall, 4,080 pixels, run 40450860976); SIFTEC's map setup runs once per world, so the map markers it places are
not doubled by the benches (they showed twice on the compass of later scenes and sessions). Samples: vanilla b/a and
a/b 0/0 and every Quicksilver and measure run 0 in runs 40436256016, 40444035980, 40454752063 (run 40450860976: the
hole, before the `#chunks` step). Limits by the finer rule: strict **10**, 8/255 **0.25**. Coverage: the strict check
sees 44% of the frame (minimap 86%, hotbar 92%, chat 51%, compass 41%): Vulkan shades the concrete wall in fine rings
that differ from OpenGL's by 1-2/255 (fog or interpolation; with a textured wall it differed on most pixels, run
40414693058, and with the sky behind on the sun's glow, run 40404251173); the 8/255 check covers the whole frame.

**Count baselines** (highest clean value, runs 40254531258, 40260765722, 40260847921; gated as every scene):

| suite-profile | scene | draw calls/frame (avg) | uploads/frame (median) | bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|---|
| core-full | outline | 10.72 (was 127.0) | 18 | 6356 | 264.69 | 5.48 |
| core-old-gpu | outline | 19.67 (was 138.0) | 16 | 5012 | 264.41 | 3.73 |
| core-vulkan | outline | 10.67 (was 127.0) | 18 | 6356 | 264.58 | 3.71 |
| siftec-full | hud | 31.0 | 16 | 71636 | 494.92 | 8.24 |
| siftec-old-gpu | hud | 58.0 | 14 | 59972 | 493.56 | 8.18 |
| siftec-vulkan | hud | 31.0 | 16 | 72908 | 490.43 | 8.16 |

Outline and hud since the rebase on main after packages 4 and 8 (runs 40444035980, 40450860976, 40454752063): the
outline scene moved to x 2680 (clear of package 4's flat_plants) and package 8's animated textures took most of its
draws, so its baselines went down to the values above (highest clean value; first set from runs 40254531258,
40260765722, 40260847921 at x 2500 before package 8).

The `ponder` scene is gated like hub, on the per-frame average and median (its average is flat at about 30 fps:
180.57-181.12 in every run, against a 227 median), baselines on the average from the three runs of its final setup
(40273668937, 40276630375, 40288414898):

| suite-profile | scene | draw calls/frame (avg) | uploads/frame (median) | bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|---|
| siftec-full | ponder | 181.05 | 14 | 52248 | 463.81 | 8.24 |
| siftec-old-gpu | ponder | 180.81 | 12 | 52212 | 463.76 | 8.24 |
| siftec-vulkan | ponder | 180.89 | 14 | 52248 | 462.87 | 8.23 |

## Count baselines (`ci/baselines/<suite>-<profile>.json`)

From the bench in each Quicksilver run (20 s per scene in CI). **Draw calls**: per-frame average (frames that line up
with a game tick draw about 26 more, so the median flips between two values; the average held within 0.1%).
**Uploads and bytes**: per-frame median (chunk uploads arrive in bursts, so averages moved 20-35% between identical
runs). **Memory**: heap used after a GC at the end of the bench, and java.nio direct buffers.

**Baseline = the highest value across the clean runs listed.** CI fails when a count goes over **baseline x 1.10
plus a step allowance**: 2 draw calls, 1 upload, 16 KiB, 16 MB heap, 4 MB direct (direct buffers grow in 2 MB steps
and land on either side between runs). Against these committed baselines the fly-through is gated on memory only (its draw calls are gated against vanilla in the same run since package 3, see "Against vanilla in the same run"): its uploads and draw calls depend on
how fast CI's server generates the new terrain. The Vulkan count baselines come from two runs; the core spawn scene's limit and baselines from one run (its camera moved to a fixed height in the last package-0 run).

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

The occlusion scene's baselines (package 2) come from one run with Quicksilver's visibility module in dry run, i.e. with
Quicksilver drawing exactly vanilla's entities and block entities.

Package 3's scenes (`translucent_move`, `water_glass_oit`, `translucent_move_oit`) got their baselines from the
collection run 40115084815, with Quicksilver's translucency in dry run (vanilla drawing all translucent terrain, as
before package 3). Against these baselines `translucent_move` and `translucent_move_oit` are gated on memory only (`ciGatedCounts`; their draw calls are gated against vanilla in the same run): the
moving camera makes vanilla sort again and upload new index lists at times that depend on the runner's speed.

| suite-profile | scene | draw calls/frame (avg) | uploads/frame (median) | bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|---|
| core-full | translucent_move (memory gated only) | 122.86 | 15 | 10108 | 267.48 | 5.48 |
| core-full | water_glass_oit | 131.0 | 15 | 14688 | 245.02 | 3.73 |
| core-full | translucent_move_oit (memory gated only) | 131.0 | 15 | 10108 | 247.19 | 3.73 |
| core-old-gpu | translucent_move (memory gated only) | 141.14 | 14 | 3060 | 268.19 | 3.73 |
| core-old-gpu | water_glass_oit | 154.0 | 14 | 7604 | 245.78 | 3.73 |
| core-old-gpu | translucent_move_oit (memory gated only) | 161.0 | 14 | 2948 | 248.15 | 3.73 |
| core-vulkan | translucent_move (memory gated only) | 126.0 | 15 | 10220 | 268.35 | 5.46 |
| core-vulkan | water_glass_oit | 131.0 | 15 | 15024 | 245.99 | 3.46 |
| core-vulkan | translucent_move_oit (memory gated only) | 131.0 | 15 | 10332 | 248.38 | 3.46 |
| siftec-full | hologram (draw calls: **median**, see below; direct MB: see "SIFTEC scenes on SIFTEC's 2026-10-25 release"; the rest run 40123696828, SIFTEC dry run) | 165 | 24 | 70368 | 453.57 | 8.24 |
| siftec-old-gpu | hologram (draw calls: **median**, see below; direct MB: see "SIFTEC scenes on SIFTEC's 2026-10-25 release"; the rest run 40123696828, SIFTEC dry run) | 166 | 23 | 70624 | 452.39 | 6.31 |

### Hologram draw calls: gated on the median (decided by Andre, 2026-10-25)

The hologram scene runs below CI's 30 fps cap on slower runners, and its per-frame draw-call *average* follows the
frame rate (frames that line up with a game tick draw more): about 119 at 30 fps, 142-148 at 22-24 fps, while
Quicksilver's own draws are the same in every run. Per the design doc (Testing, "Counts that follow the runner's
speed"), this scene alone is gated on the per-frame **median** (`"ciDrawCallGate": "median"` in `siftec.json`), in
every profile it runs in (full and old-GPU; not run on Vulkan). Baseline = the highest clean median from package 3's
runs on `4cb5419`, `6d48fc7` and later. Every other scene and every other count is gated as before.

| run | commit | full: median (avg) | old-GPU: median (avg) | clean? |
|---|---|---|---|---|
| 40123696828 | `4cb5419` | 166 (119.16) | 170 (155.07) | no: translucency and visibility dry run (Quicksilver drew vanilla's set, vanilla drew the translucent terrain) |
| 40126212181 | `6d48fc7` | 166 (118.92) | 167 (124.18) | yes (green) |
| 40128444322 | `0db931b` | 166 (144.24) | 167 (148.72) | yes (failed only on this average) |
| 40130610610 | `13f56fb` | 166 (142.45) | 167 (146.9) | yes (failed only on this average) |

Baselines: **full 166, old-GPU 167** (gate: x1.10 + 2), then **165 / 166** since the crosshair outline pin: it removed exactly one draw per frame from this scene (median 174 to 173 on full, 175 to 174 on old-GPU between runs 40152673881 and 40154022133), so the baselines went down by that draw, as after the texture-animation pin. The dry run's 170 is left out: in a dry run Quicksilver isn't
drawing the translucent terrain itself, so its count is not Quicksilver's.

### SIFTEC scenes on SIFTEC's 2026-10-25 release (factory and map back, sift uploads, hologram direct buffers)

Baselines from the three runs of commit `41c9e44` (run 40156711337, attempts 1-3; a run counts as clean for a scene
when that scene passed every check in it, Andre 2026-10-25):

- **`sift` upload bytes re-baselined** (Andre's rule: vanilla jumps the same way, so it is the pack). The per-frame
  median moves in about 12.9 KB steps (30k, 43k, 56k, 69k, 82k) and since SIFTEC's latest releases reaches 81,788 in
  some runs (over the old gate, about 77.7k). Vanilla's own counts in the same runs (branch-only jobs that ran the
  factory and sift scenes with every Quicksilver module off, so the bench measured vanilla): vanilla a / b / old-GPU /
  Vulkan = 43,120 / 68,936 / 47,504 / 55,980, then 43,120 / 55,972 / 34,316 / 55,764, then 55,980 / 56,304 / 60,252 /
  43,128. Quicksilver in the same runs: full 68,940 / 43,128 / 68,940, old-GPU 43,264 / 43,376 / 43,376, Vulkan
  55,980 / 56,084 / 43,232. New baselines: full **68,940** (was 55,764), old-GPU **43,376** (was 73,228: down),
  Vulkan **56,084** (was 55,980). Then, Andre's one-off exception to "highest clean value" (2026-10-25, other scenes
  keep the rule): the upload breakdown by kind shows every byte of the jump is in MAP_WRITE (writes into mapped GPU
  buffers) in vanilla and Quicksilver alike; WRITE_TEXTURE is 0 everywhere, WRITE_BUFFER is constant (4,016 B/frame on
  full and Vulkan, 112 on old-GPU), always 3 + 16 uploads per frame (2 + ~15 on old-GPU). So the `sift` baseline is the
  highest value seen in any run, vanilla or Quicksilver, **81,788** on every profile (gate as usual: x1.10 + 16 KiB).

  | run | vanilla a | vanilla b | vanilla old | vanilla vk | Quicksilver full | Quicksilver old | Quicksilver vk |
  |---|---|---|---|---|---|---|---|
  | 40156711337 #1 | 43,120 (39,125) | 68,936 (64,928) | 47,504 (47,359) | 55,980 (54,012) | 68,940 (66,584) | 43,264 (44,452) | 55,980 (54,217) |
  | #2 | 43,120 (41,420) | 55,972 (51,423) | 34,316 (34,236) | 55,764 (51,497) | 43,128 (44,802) | 43,376 (43,256) | 56,084 (52,072) |
  | #3 | 55,980 (49,537) | 56,304 (56,785) | 60,252 (59,115) | 43,128 (38,981) | 68,940 (64,885) | 43,376 (43,246) | 43,232 (47,485) |
  | 40154022133 | - | - | - | - | 55,872 (51,858) | 30,860 (33,856) | 81,788 (77,935) |

  Upload bytes per frame, median; in brackets the MAP_WRITE part (a per-frame average, so it doesn't add up exactly
  to the median). Vanilla's factory uploads matched Quicksilver's too (6,284-6,488 on full, 49,444-49,604
  old-GPU, 51,036-51,056 Vulkan).
- **`hologram` direct buffers re-baselined** (Andre's OK): full **8.24** (was 4.24), old-GPU **6.31** (was 3.73, one
  dry-run sample from when factory and map were skipped). The map scene now runs just before the hologram in the same
  client session, and the JVM's direct pool grows to 8.24 MB there and keeps that size (attempt 1, full: mam 7.73, map
  8.24, hologram 8.24; attempt 2, old-GPU the same). Old-GPU's value comes from attempts 1 and 3 (6.06, 6.31): attempt 2
  failed this very check (8.24 against the old 3.73), so it was not clean for the hologram (review finding, 2026-10-25);
  its 8.24 is within the new gate (6.31 x 1.10 + 4 = 10.94). Quicksilver allocates no java.nio direct buffers at all, and a
  15-minute hologram bench showed no growth (docs/progress.md, package 3).
- **`factory` and `map` count baselines** (they had none, so their counts were not gated): from the same runs (map
  from attempts 1 and 3; attempt 2 failed the map's then strict limit).

| suite-profile | scene | draw calls/frame (avg) | uploads/frame (median) | bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|---|
| siftec-full | factory | 111.52 | 14 | 6488 | 421.74 | 5.48 |
| siftec-full | map | 177.0 | 14 | 47032 | 504.13 | 8.24 |
| siftec-old-gpu | factory | 119.43 | 13 | 48604 | 421.54 | 5.48 |
| siftec-old-gpu | map | 225.0 | 12 | 31404 | 506.16 | 6.31 |
| siftec-vulkan | factory | 111.25 | 15 | 51148 | 420.23 | 3.71 |
| siftec-vulkan | map | 177.0 | 14 | 47072 | 504.09 | 8.23 |

Run-to-run spread of the old-GPU draw calls is wider (up to about 15% in mobs and water and glass) because vanilla
draws each visible section separately there and the count follows how far meshing has got.

Meshing time per section is measured since package 1 (the bench's `meshing` block: vanilla's mesher plus Quicksilver's
transcode, sections meshed while measuring) and shown in CI's report; it is not gated (it follows CI's CPU load).

### Against vanilla in the same run: draw calls and terrain GPU memory (package 3, decided by Andre, 2026-10-25)

**Measure runs.** Every CI run has three more client runs per suite, `measure-full`, `measure-old` and `measure-vk`:
Quicksilver loaded with every module off (`-Dquicksilver.ci.measureOnly=true`), in the profile of the Quicksilver run of the
same name, so Quicksilver's bench measures vanilla's own counts under the same pins and runner. They must look like
vanilla: their screenshots go through the same 8/255 and strict limits against vanilla's runs as Quicksilver's runs
(compare.py's "Measure runs" table); run 40180107484 attempt 2: every measure run inside every limit. CI also
checks that a measure run really is vanilla: both modules' "measureOnly is set" log lines and no "on" line, every
bench with terrain off ("measure only (CI)") and visibility off, no Quicksilver arena and no layer taken, and the same
profile checks of its probe as the Quicksilver run (old-GPU's forced Intel flag). Since package 4 they also draw blocks
and items with Fabric's Indigo, as the vanilla runs do: CI takes `fabric-renderer-api-v1:contains_renderer` out of
the measure run's copy of the jar and Quicksilver does not register its renderer when measureOnly is set; CI requires
Indigo's registration line and Quicksilver's "Quicksilver renderer: off" line, and no Quicksilver registration.

**Draw calls.** In every scene, Quicksilver's draw calls per frame against the measure run of its profile, on the
per-frame average **and** the median: the translucent scenes (`ci/vanilla-draw-calls.json`: water_glass,
translucent_move, water_glass_oit, translucent_move_oit, flythrough; SIFTEC hologram) at most vanilla's, the others at
most vanilla's x 1.10 + 2 (the count gate's allowance). These sit next to the committed count baselines, which stay.
Exceptions, each by Andre's rule:
- `siftec/hologram`: median only (decided earlier, see above).
- `siftec/sift`: median only, because its average follows the runner's frame rate (below 20 fps every frame lines up
  with a game tick and draws the tick's extra draws). Run 40180107484 attempt 1, full: Quicksilver 154.0 average / 154
  median at 15.1 fps, vanilla's measure run 135.58 / 154 at 22.64 fps; Vulkan 153.97 / 154 at 15.8 fps against
  153.94 / 154 at 12.37 fps. (`"ciVanillaDrawGate": "median"` on the scene, with this reason.)
- `core/water_glass`, `water_glass_oit`, `nether`, `translucent_move`, `mobs`: median only, by the same rule. Each
  failed only on the average, in the Vulkan profile (lavapipe runs near 20 fps), where the Quicksilver run and the
  measure run ran at different speeds, with equal medians: water_glass 124.0 / 124 at 18.57 fps against 103.78 / 124
  at 24.15 fps; water_glass_oit 129.01 / 129 at 9.86 fps against 129.0 / 129 at 13.46 fps; nether 120.73 / 121 at
  18.93 fps against 102.5 / 121 at 23.63 fps (all three run 40180107484 attempt 2); translucent_move 124.0 / 124 at
  15.57 fps against 123.4 / 124 at 19.28 fps; mobs 134.0 / 134 at 18.81 fps against 115.65 / 134 at 23.72 fps (both
  run 40193520268, and attempt 2 above). The reasons are on the scenes. Gated on both average and median: spawn,
  flythrough, occlusion, occlusion_move, translucent_move_oit, factory, hub, map, ponder (package 7: its average is
  flat at about 30 fps, 180.57-181.12 in every run).
- `core/outline` (package 7): median only, by the same rule. Run 40266027605, Vulkan: Quicksilver 127.0 average / 127
  median at 16.42 fps against vanilla's measure run 101.35 / 127 at 25.57 fps (limit 113.48); and the other way round
  in run 40276630375, Vulkan: Quicksilver 98.66 / 127 at 26.42 fps against 127.0 / 127 at 16.04 fps.
- `siftec/mam`: median only, by the same rule (2026-10-26). Run 40238316211 (the ci-quick branch, main's code),
  Vulkan: Quicksilver 132.6 average / 162 median at 25.17 fps against vanilla's measure run 117.94 / 162 at 29.31 fps
  (limit x 1.10 + 2: 131.73); full in the same run 116.03 / 162 against 116.48 / 162, both at 29.83 fps.
- `core/flat_plants` and `core/renderer_off` (package 4): median only, by the same rule (the slower run lines up more
  frames with game ticks, so the average moves with fps and the median does not). flat_plants, run 40274749083,
  Vulkan: Quicksilver 121.34 average / 124 median at 20.36 fps against vanilla's measure run 103.03 / 124 at 24.41 fps
  (limit x 1.10 + 2: 115.33); renderer_off, run 40279156080, Vulkan: 96.3 / 123 at 25.89 fps against 84.85 / 123 at
  29.76 fps (limit 95.34). The reasons are on the scenes. factory_flat and create_ct are gated on both.
- `core/flythrough`: vanilla + 2 on full and Vulkan (old-GPU stays at vanilla), Andre's rule for a fly-through excess of 2 draws per frame or fewer. Run
  40180107484 attempt 1, Vulkan: average 123.11 against vanilla's 123.05 (medians 123 and 123; full 123.07 against
  123.07). Quicksilver's terrain draws there are exactly vanilla's 3 indirect draws (solid, cutout, translucent: one
  arena each); the 0.06 is in the other draws (`single` 120.11 against 120.05), which depend on what the server has
  generated as the camera flies. (`"allowance"` in `ci/vanilla-draw-calls.json`.)

Before arenas per layer, the fly-through drew 136-139 against vanilla's 123 on full and Vulkan: translucent runs broke
at every change between two shared arenas (run 40172887031: 12-15 breaks per frame, none from index types after
`49fec37` widened 16-bit index lists to 32 bits). With arenas per layer (`629f023`) there are no arena breaks.

**Terrain GPU memory.** The bench reports terrain GPU memory reserved (buffers made) and used: vanilla's own heaps (its
section dispatcher's vertex and index buffers per layer: heaps x heap size, 128 MiB vertex heaps and 32 MiB index
heaps, made on first use) and Quicksilver's arenas (each starts at 4 MiB and doubles up to 64 MiB, per layer). CI fails
when a Quicksilver run reserves more than vanilla's measure run of the same profile, in any scene. Run 40180107484
attempt 1, full profile (MB reserved / used):

| scene | Quicksilver | vanilla |
|---|---|---|
| spawn | 36 / 21.8 | 416 / 38.0 |
| flythrough | 112 / 87.1 | 416 / 144.1 |
| water_glass | 36 / 12.7 | 416 / 21.7 |
| nether | 20 / 15.2 | 256 / 26.5 |
| mobs | 16 / 9.7 | 256 / 16.9 |
| translucent_move | 28 / 13.2 | 416 / 23.0 |
| SIFTEC factory | 28 / 1.4 | 256 / 2.6 |
| SIFTEC sift | 16 / 7.4 | 416 / 12.9 |
| SIFTEC map | 56 / 31.6 | 416 / 60.2 |
| SIFTEC hologram | 56 / 0.24 | 288 / 0.39 |

A grown arena keeps its size until it empties (the hologram's 56-72 MB were grown in the map scene before it): a known
limit, for package 9.

## Package 8: the particle and animation scenes

Both scenes are made the same in every run, vanilla and Quicksilver alike, by the CI pin mod (`ci/pin-mod`, `Pins`;
docs/progress.md, package 8): in the particle scene's area particles take their random numbers from where and what
they are, are not ticked and are drawn at partial tick 0 while the player is there, and are removed when the player
leaves; in the animation scene's area every animated texture starts again from frame 0 when the player enters, runs
exactly 70 texture ticks and is held (counted per texture tick since the animation flake fix, see below). Vanilla's runs agree exactly on both (vanilla b/a 0 strict pixels in every run), so the
limits are the rules' floors.

| scene / shot | runs | samples (8/255 %: vanilla a/b, Quicksilver; strict: vanilla b/a, Quicksilver, measure runs) | limit % | strict limit |
|---|---|---|---|---|
| core/particles | 40257302632, 40260148458, 40263318499, 40267202902 (full), 40271672678, 40275109388 (full), 40293774852 (full) | all 0.0; strict all 0 | 0.25 | **4** |
| core/particles.bare | 40271672678, 40275109388 (full), 40293774852 (full) | all 0.0; strict all 0 | 0.25 | **4** |
| core/animation, .front | 40263318499, 40267202902 (full), 40271672678, 40275109388 (full) | vanilla a/b 0.0, Quicksilver 0.0001 (every profile); strict vanilla 0/0, Quicksilver 1, measure runs 0 | 0.25 | 10 |
| core/animation.behind | the same | all 0.0; strict all 0 | 0.25 | 10 |
| core/animated_items (Andre's follow-up) | 40404276347 (quick), 40412755923 (full) | all 0.0; strict all 0 | 0.25 | 10 |

Limit = max(2 x largest, largest + 0.25, 0.1) rounded up to 0.05 (0.25); strict = largest x 1.5 rounded up to 10, at
least 10 (10), except the particle scene's: **4** (Andre, 2026-10-27), below its smallest particle (9 pixels), as its
samples are 0 in every run and the finer rule's floor (10) would let that particle go missing unseen. Quicksilver's one strict pixel in the animation shots (x 965, y 384) is on the lantern's handle edge: the lantern
model's off-grid coordinates rounded by the 16-byte vertex (package 3, Andre's decision), the same pixel in every
profile and run, and not in the measure runs. Earlier runs of the animation scene (before its blocks behind the camera
moved two sections back, `5db4432`) are not counted.

**Counts.** core/animated_items (Andre's follow-up): baselines from run 40412755923, its draw calls gated on the
median like the animation scene's (60 in every profile, average 46.7). Baselines for the other two scenes from runs 40267202902 and 40275109388 (`ci/baselines/core-*.json`, the rule: the highest clean
value). The animation scene's bench lets animation run freely (the pin's marker block), so its draws per frame follow
the runner's frame rate (vanilla redraws changed sprites once per client tick): it is gated on the median from the
start (`ciDrawCallGate`, Andre's rule for such scenes): 44 in every profile (average 33.3), raised to 49 when the
review fixes added two steps to its bench (a block particle and a block placed while measuring; quick run
40288502378, old-GPU median 49): the scene is new in this package, so its baseline is set, not re-collected.

**Existing draw-call baselines were not lowered.** Quicksilver's animation module removes most of vanilla's per-tick
atlas redraws even in the pinned scenes (vanilla still redraws its 23 interpolated block sprites every tick: 77-115
draws per frame in the core scenes and 94-140 in SIFTEC's, against Quicksilver's 0-10; `compare/report.md`, "Effects"),
so every draw-call baseline now has that much room. They are left as they are so the lead can merge packages 4, 7
and 8 by hand; lowering them is listed for Andre (docs/progress.md, package 8).

### Draw-call baselines after packages 4, 7a and 8 (2026-10-27)

Every scene's `drawCallsPerFrame` baseline lowered to the highest value in three clean runs of the merged build
(main's run 40463682395 on `7597832`, package 7a's run 40459438787 on the same code, and run 40471457759 with the
chicken pin), as the rule allows after a real improvement: package 8 (animated textures only when seen) took
70-130 draws per frame of texture animation out of every scene, e.g. core spawn 123.09 to 13.0 (full), SIFTEC sift
670.27 to 41.0 (old-GPU), ponder 181.05 to 87.25. 66 baselines lowered; none raised; the other counts unchanged.

`siftec/create_ct` direct buffers, full profile: 4.24 to 10.24 MB, re-collected by the noise rule (standing rules):
the JVM's direct pool grows in 2 MB steps at a moment that varies by run, and vanilla's own measure run showed 10.12
in the same run (40463682395) where Quicksilver's showed 10.24; Quicksilver's samples 10.24, 6.06, 5.99, 8.24; the other
profiles' baselines were already 8.24 and 8.29.

`core/mobs` strict noise: the chickens' wing flap (their last ticks of falling) made two vanilla runs differ by up
to 718 strict pixels (vanilla's measure run on Vulkan, run 40463682395, limit 600). CI's pin mod now holds the wings
at rest in every run, vanilla and Quicksilver alike; with it every mobs sample is 0 (run 40471457759). **Lowered
2026-10-28: 600 -> 10.** Four full runs with the pin (40471457759, 40481430766, 40516650792 and main's 40526712029;
the last three recomputed from their PNGs): vanilla b/a 0/0 and every Quicksilver and measure run 0 in each, so by
package 2's finer rule (largest sample x 1.5, rounded up to 10, at least 10) the limit is 10, below a chicken (about
480 px). Samples in `ci/pixel-noise-samples.json`.

### The animation pin's flake (main's run 40487515684) and its fix

Main's full run 40487515684 failed core/animation, animation.front (2,573 strict pixels) and animation.behind (450)
in two measure runs (measure-full, measure-old), every difference at most 8/255: an interpolated animation one blend
step off. Cause: the pin counted its 70-tick run in client ticks, but the game ticks its textures once per frame that
runs at least one client tick (`Minecraft.runTick`: one `TextureManager.tick` before the frame's client ticks), so a
run whose frames fell behind (catching up two client ticks in one frame) advanced its textures fewer times. The pin
now decides run and hold at the start of each texture tick and counts 70 texture ticks; its log line gives the client
ticks they took. Not noise: no limit changed. compare.py now also fails a run whose client log lacks the pin's "texture
ticks of animation done" line (the fix's first run, 40496751536, passed with the new mixin missing from the pin's
config: animation never ran, the scenes showed frame 0 in every run alike).

## Counter audit

The counting mixins have their own config (not required, `defaultRequire: 0`), so a counter that no longer fits the
game is skipped instead of crashing it. The config's plugin records which counting handlers each loaded class
received; every bench report lists missing ones (and classes not loaded because their backend isn't in use), and CI
fails if any is missing.

The `occlusion_move.ghast` shot (package 2) was removed before package 2 merged: the happy ghast's tentacle animation
made vanilla's own noise reach 239 strict pixels against its 240 limit, and the shot tested nothing Quicksilver hides.
Removed rather than raised; no limit was raised.

### Draw-call baselines after the texture-animation pin

Holding animated textures still also stops the game redrawing them into the block atlas every frame, so every run
(vanilla and Quicksilver) makes fewer draw calls. Left alone, the draw-call baselines would have had that much extra
room and the 10% gate would have missed a regression of the same size. Each draw-call baseline was lowered by
exactly the drop measured for the same scene and profile between two runs of the same Quicksilver code without and
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

Scene clean-up (since the fix on main after package 2): scenes that clear an area move the entities there out of the
world (`tp` to y=-200) instead of killing them, so no death animation or dropped loot is left to bob in view at a
time that depends on how fast the runner is. The object-free `occlusion_move.bare_x2297` shot reached 72 strict
pixels against 70 in the Vulkan run on a dropped item from the clean-up; the limit was not raised.

## Package 4b: back faces, the upload budget and the bench-only jar

New gates in every Quicksilver run (`ci/compare.py`), none of them a limit or baseline (no file in `ci/*.json` changed):
- **Back faces:** each bench's `backFaces` block must be `on`, and its triangles per frame below the same frames'
  count with every face drawn (`trianglesAllFacesPerFrame`, what Quicksilver drew before 4b).
- **Upload budget:** `uploadBudget` must be `on` with the camera known, and no visible section's mesh may have waited
  more than `maxVisibleWaitFramesAllowed` (15) frames since it was first seen visible (past that it goes in whatever the
  budget).
- **Screenshots:** Quicksilver runs set `quicksilver.debug.logUploadQueue` and `ci/drive.py` waits, before each shot, until
  the client's last "Quicksilver uploads:" line is "caught up" (runs without the line don't wait: vanilla, measure and
  bench-only runs). Each scene's drive JSON records the seconds waited per shot (`uploadWaits`); a timeout (-1, after
  120 s) fails the run.
- Reported, not gated: a "Package 4b" table per suite and profile (triangles per frame and with every face, index ranges
  per section layer, the largest upload bytes in one frame against vanilla's measure run, Quicksilver's own largest
  terrain upload and the average budget, frames over 50 ms, sections waiting).

**Bench-only jar runs** (full mode, core suite only): `bench-plain` (Fabric API and `quicksilver-bench`) and `bench-sodium`
(the same plus Sodium 0.9.2 for 26.3, downloaded from Modrinth in the run), benching spawn, flythrough, water_glass and
nether. compare.py requires the bench-only jar's start-up line, a bench per scene written by `quicksilver-bench`, no
Quicksilver module block or log line, no counter missing, Sodium loaded in `bench-sodium` only, and Quicksilver itself not
loaded. Their pictures are not compared (Sodium draws its own way); the "Bench-only jar" table reports fps, frame-time
p99, frames over 50 ms, draw calls and the largest upload per frame.

## Server suite (phase 2, P2-0)

The server suite (`ci/server/`, jobs `server-base`, `server`, `server-net`, `server-compare`; logs on
`ci-logs-server-<branch>`) runs on every branch: quick mode runs its core suite (and SIFTEC's with `[siftec]` in the
commit message), full mode runs core, SIFTEC and Terralith (Andre, 2026-10-29). It has **no tolerances**: there is no limit to
raise. Every comparison is keyed, and Quicksilver must equal vanilla-a on every key where all three vanilla runs
(vanilla-a, vanilla-b, vanilla-c) agree. A key where vanilla differs from itself is vanilla's noise: it is reported
(per dimension, with examples in `compare/<suite>-<check>-noise-{b,c}.json`) and can't be gated, as phase 1's strict
pixel check gates only where every vanilla run agrees. Three vanilla samples are the standing noise rule's minimum.

| check | key | gate |
|---|---|---|
| worldgen | each chunk the harness generated (`ci/server/gen-<suite>.txt`), its whole saved NBT byte for byte; and the same chunks block by block (below), which gates any chunk whose bytes vanilla doesn't repeat; every listed dimension with exactly its area's chunk count | equal where vanilla agrees |
| world diff (core, SIFTEC; and core's random-tick run, P2-1) | each block's state; each block entity's top-level fields; each pending scheduled block and fluid tick; each entity (type, exact position, health, the stack an item entity holds; one without an exact match on the other side is keyed by type and block; UUIDs aren't compared, as worldgen's entities don't repeat theirs) | equal where vanilla agrees |
| GameTests (core) | each test's result and the tick it finished | equal to vanilla-a; vanilla-b and c must equal vanilla-a too |
| SIFTEC self-tests | each suite's passed/failed counts | equal to vanilla-a; vanilla-b and c too |
| Create train (SIFTEC) | every arrival at a station with its tick, final speed, schedule state | the same line in every run; at least 2 arrivals |
| network check | the client joins, stays until the run ends, no protocol error or desync line | always |
| scripted run | every setup command succeeds, the run finishes its ticks | always |
| random-tick run (core, P2-1) | the same counts in every run (random positions drawn, random ticks, sections let through and held, precipitation, mobs spawned), each above zero; vanilla's runs equal on every key at or above the scenes' y 192 and on every entity | always |
| heap run (every suite, P2-1) | live heap after a full GC (the JVM's class histogram total) after load and after 12000 ticks (ten minutes of game time, sprinting); every run must finish it | Quicksilver below the lowest of the three vanilla runs, both times |
| Memory module (P2-1) | every server start of the quicksilver run logs `Quicksilver memory:` with every optimisation on; no `Quicksilver memory fallback:` line in any run (client runs too, ci/compare.py) | always |

**What makes runs repeat** (all in vanilla's runs and Quicksilver's alike; CI only):

- `-Dmax.bg.threads=1`, and the harness generates one step at a time over each area (every chunk reaches a step
  before any starts the next), so worldgen order can't depend on thread timing. Before this, vanilla a/b differed in
  3 of 1,685 overworld chunks (the area's edge).
- The light engine works on its own thread: the server saves after worldgen, and the scripted run starts, only once
  no light engine has had work for 20 ticks (before this, SkyLight differed in 1 to 4 chunks).
- Unseeded random sources (`RandomSupport.generateUniqueSeed`) come from per-thread fixed sequences, and at the
  scripted run's tick 0 every level's and entity's random is re-seeded from fixed values.
- The scripted run waits frozen until its area (and every entity in it) is loaded, then at least 200 more frozen
  ticks after the setup commands: Create and SIFTEC tick through their own hooks even while the game is frozen
  (before this, a furnace's cooking time and a bearing's angle differed between vanilla runs).
- No natural mob spawning in the scripted runs: where a mob may spawn reads light from the light engine's thread.
- No random ticks in the scripted runs: they draw on the shared level random over every loaded chunk, and vanilla's
  own runs differed in 600 to 900 blocks with them on (cave vines growing and berrying, vines spreading). They are tested
  in core's random-tick run instead (below).
- Just before tick 0 every pending scheduled tick is taken out and scheduled again in one fixed order (trigger tick,
  priority, x, z, y, type) (P2-1). Chunks are post-processed as they finish loading, in a different order in every run,
  and each update gave its ticks the level's next sub-tick number: the same ticks, in a different order, and where water
  and falling sand meet (the cave at chunk -1,-2) the cave went different ways.
- When the run ends every loaded chunk (the areas and three rings) is marked changed, so the save as the server stops
  writes each as it is at the last tick (P2-1). The game also saves chunks during a run on a wall-clock timer, and one
  it didn't count as changed since wasn't written again: SIFTEC's furnace (cooking time) and Create's bearing (angle),
  and the cave's pending ticks, were equal in every run at the last tick and differed on disk.
- SIFTEC's bearing motor and furnace are placed at tick 1 (Create and SIFTEC tick them through their own hooks while the
  game is frozen before tick 0). Their saved fields are logged during the run (`report` lines, `CI REPORT`).
- GameTest server only: the ticking-chunk shuffle and the chunk tick's weather draws use their own randoms (they drew
  on the shared level random once per loaded chunk). The crop farm is still judged on its state at tick 200, not on
  how many bone meals it took: growth draws on the shared random, which the GameTest world uses in ways that depend
  on chunk loading.

**Everything gated (P2-1):** `REPORT_ONLY` and `KNOWN_NOISE` in `ci/server/compare-server.py` are empty. The pending
scheduled ticks (reported only in P2-0) and SIFTEC's furnace and bearing fields (known noise in P2-0) repeat since the
two harness fixes above; vanilla's three runs agree on every key (run 40963578144).

**The random-tick run** (core, `ci/server/scripted-run-core-random.txt`, P2-1): the base world with crops on farmland,
melon and pumpkin stems, sugar cane, cactus, bamboo, a berry bush, three saplings, grass spreading over dirt, copper,
a block of leaves with no log, and a snowy patch with a pool and two cauldrons in rain, on a platform at y 198 to 205;
1200 ticks at random tick speed 20. Random ticks and precipitation are pinned (`Pins.java`): each chunk draws on its own
random seeded by its position and the game time, and only sections at or above y 192 tick, so the terrain below (whose
grass and cave vines read light the light engine's thread may not have finished) can't move anything. Natural spawning:
CI has no player and the game spawns only near one, so a Fabric API fake player is put in the overworld's player list
only (creative, at 16.5 64 16.5: not an entity, it loads nothing and is never saved), and the harness runs the game's
spawner for two chunks (-3,2 and 2,-3) every tick from a random pinned per chunk and tick (monsters every tick,
creatures every 400 game ticks, up to 24 per chunk and category). 4 monsters spawn in every run, no creature. Not
covered: which chunks the game picks to spawn in near a real player; and since the shared level random is re-seeded
before each spawner call, a change in how often other code draws on it isn't seen there.

**History, P2-0's vanilla noise** (three vanilla runs, run 40645807938; all of it fixed in P2-1):

| suite / check | keys where vanilla differs from itself | what |
|---|---|---|
| core / worldgen | 0 of 432 chunks | |
| core / world diff | 0 blocks, block entities or entities; 3 to 101 pending scheduled ticks (reported) | (before random ticks were off: 600 to 900 blocks of cave vines and vines, a gravel cave-in in some runs) |
| SIFTEC / worldgen | 0 of 432 chunks | (before the light fix: 1 to 29 chunks in the nether and the Sift) |
| SIFTEC / world diff | 2 block entities | the furnace's cooking time and the mechanical bearing's angle (SIFTEC and Create tick these through their own hooks; the 200-tick frozen head start made the rest repeat) |
| Terralith / worldgen | 0 of 144 chunks | |

Smallest thing each check must catch: one block state, one block entity field, one scheduled tick, one entity's
existence, type, position (exactly: a 1e-7 drift is caught) or health, one item stack, one chunk's byte; one GameTest
tick or result; one self-test. The heap gate has no tolerance either: Quicksilver below vanilla's lowest run (vanilla's
own runs spread up to 1.7 MiB, in core; Quicksilver is 10.6 MiB below in core, 61 in SIFTEC, 82 in Terralith).

### Perf job and Logic module (P2-2)

**MSPT, same runner** (`server-perf`, `ci/server/perf-run.sh`, `ci/server/perf-<suite>.txt`): CI's runners differ from each
other by more than Quicksilver's savings (one runner played the mob scene at 2.9 ms per tick, another at 1.6), so the
server suite's per-run MSPT can't show a saving. The perf job plays each perf scene on one runner, vanilla and
Quicksilver alternately (five rounds, the side that goes first alternating; a sixth with JFR for MSPT by phase), sprinting
for the scene's ticks, with the scripts' `qs-at` lines left out. Scenes: core `mobs` (172 mobs over mixed shapes, a boat
and a minecart; 4800 ticks), `random` (the random-tick run, 6000 ticks), `run` (the scripted run, 4800 ticks); SIFTEC
`train` (its scripted run with the Create train, 1200 ticks). Rounds were 2400 ticks and three per side at first (about 2 s of
server work for the random-tick scene, JIT warm-up and GC moving single rounds by 10-20%); single rounds still spread
by 10-20%, which the median of five per side absorbs (first run of five: every gated median 5-11% below vanilla's).

| check | key | gate |
|---|---|---|
| perf MSPT (core `mobs`, `random`) | the median MSPT average over rounds 1-5 per side | Quicksilver's median below vanilla's; every round finished on both sides, no failed command |
| perf MSPT (core `run`; SIFTEC `train`) | the same | reported |
| perf world (core `mobs`) | each Quicksilver round's saved world against vanilla round 1 (the world diff's keys) | equal where vanilla's six rounds agree (they agree on every key) |
| Logic module (P2-2) | every server start of a Quicksilver run (server suite, network check, perf) and every Quicksilver client run logs `Quicksilver logic:` with every optimisation on; measure runs log it off; no `Quicksilver logic fallback:` line anywhere; no identity hash in its sources | always |

MSPT by phase: the share of the server thread's JFR samples (1 ms) under each phase's frame, times that run's MSPT;
`compare/p2.md` lists them with the top methods. Method names only are published.

**SIFTEC report-only** (`SIFTEC_REPORT_ONLY: "1"` in `build.yml`, Andre 2026-10-31): until SIFTEC starts with MapMod's latest
release again, `ci/server/compare-p2.py` lists compare-server.py's and compare.py's SIFTEC failures under "Owed" and
fails on every other failure. Set it to `"0"` to gate SIFTEC again.

