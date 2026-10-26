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

## Quick CI and per-branch logs (design doc, Quick CI, 2026-10-26)

Branch pushes run **quick mode**: the core suite only, with vanilla-a, vanilla-b and brimstone-old (about 10
minutes). Brimstone's old-GPU run is compared with vanilla-a (vanilla's GL 3.3 and GL 4.5 pictures differ no more
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
| core/mobs | a missing or wrongly drawn mob | a pig ~1,200; a chicken ~480 | 600 | pig yes; chicken **no** |
| core/occlusion | a wrongly hidden entity or block entity | the iron golem in the doorway ~200 (package 2) | 22 | yes |
| core/occlusion_move (+ shots) | a wrongly hidden object while moving | each object larger than its shot's limit, measured with the bare shots (package 2) | 10 to 110 | yes |
| core/translucent_move (+ shots) | a stained-glass block out of order | one pane block face (~34 px): ~1,150 | 13 to 41 | yes |
| core/water_glass_oit | a wrong translucent block with Improved Transparency | one block face: ~1,900 | 57 | yes |
| core/translucent_move_oit (+ shots) | as translucent_move, with Improved Transparency | one pane block face: ~1,150 | 10 to 40 | yes |
| siftec/factory | a wrongly drawn Create machine part | a machine ~4,800; one ladder strip ~320 | 500 | machine yes; one ladder strip alone **no** |
| siftec/sift | a wrong block or light in the Sift terrain | a block face at mid distance (~24 px): ~580 | 100 | yes |
| siftec/hub | a wrong GUI element or text line (package 7) | a line of text ~2,000 | 12,506 | **no** |
| siftec/mam | a wrong item icon or GUI element (package 7) | one item icon (32 px): ~1,000 | 155,106 | **no** |
| siftec/map | (not strict-gated, Andre's decision: its noise is larger than anything it could show) | | not gated | |
| siftec/hologram | a missing or wrong outline line | one outline edge ~1,200 | 413 | yes |

The **no** rows are limits already set (by the rule and Andre's earlier decisions) that are larger than the smallest
thing the scene shows; they are listed for Andre, not changed. They matter most for package 7 (hub and mam are its
GUI scenes) and should be re-collected or the scenes made quieter there.

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

Package 3's scenes (`translucent_move` and `translucent_move_oit`, each with six fixed-point shots, and
`water_glass_oit`) were sampled in the marked collection run 40115084815, with Brimstone's translucency in dry run
(translucent terrain left with vanilla; solid and cutout still Brimstone's), so all four samples per shot are noise.
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

The SIFTEC `hologram` scene (package 3) was sampled in run 40123696828, with only the SIFTEC suite in translucency
dry run. It is not run in the two Vulkan runs (`ciSkipRuns`): Mesa's lavapipe segfaults as soon as Create's outliner
draws the hologram, in vanilla's Vulkan run as much as Brimstone's, so its samples and limits cover the other three
profiles, and its strict gate uses the three other vanilla runs.

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
differs from its own profile's vanilla run **by more than 2/255 in any channel** (threshold 0 until package 3; see
"The 16-byte vertex" below). Over the limit fails. Like the other limits these may only go down, never up, except by
Andre's decisions recorded here.

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
| core/translucent_move (package 3) | 0/1 (run 40115084815) | 2 (dry run) | 10 |
| core/translucent_move.x2404 / .x2414 / .x2424 (package 3) | at most 3 | at most 3 (dry run) | 10 |
| core/translucent_move.low (package 3) | 2/4 | 11 (dry run, Vulkan) | 20 |
| core/translucent_move.back / .bare_back (package 3) | at most 3 | at most 4 (dry run) | 10 |
| core/translucent_move_oit, .x2404, .x2414, .x2424, .back, .bare_back (package 3) | at most 6 | at most 5 (dry run) | 10 |
| core/translucent_move_oit.low (package 3) | 7/4 | 7 (dry run) | 20 |
| core/water_glass_oit (package 3) | 23/14 | 15 (dry run) | 40 |
| SIFTEC/hologram (package 3) | over eleven shots in run 40155387068: 221-234 / 9-22 (the largest, 234, is mostly SIFTEC's minimap); earlier 7/4 (run 40139105024), 15/10 (run 40123696828, before the pin) | 63 (dry run, eleven shots, run 40155387068) | 360 (21840 before the batch order pin; 30 from the first, under-sampled re-collection) |
| SIFTEC/factory (strict limit from 2026-10-25) | 0/0 in most runs; **315/0** (run 40156711337 attempt 2: a vanilla-only flip, see below) | 2 (dry run); real runs 0 or 313-315 | 500 (100 from the first, single-run collection) |
| SIFTEC/map | 6960/8712, 17712/5856, 13276/6048, 8352/4028, 22532/6288 (runs 40155387068 to 40162905190) | 3168 (dry run) | **not gated** (`ciNoStrict`, see below) |
| SIFTEC/sift | with the Sift particle pin, threshold >2/255: 0/0, 0/0, 0/0 (runs 40185974374, 40187878826, 40189806229); before: 530/323, 547/723, 805/965, 793/991, 2218/3223, 2963/29, 800/40, 379/3072 (threshold 0) | 55-61 (16-byte vertex: the rounding extra, not noise) | 100 (4900 before the pin) |
| SIFTEC/hub | 40/8030, 17/1679, 8244/40, 52/1205, 56/8, 2450/1692, 8280/36, 8276/832 | 1536 | 12500 |
| SIFTEC/mam | 40/8012, 17/1177, 6566/86, 52/1204, 56/8, 1386/49, 103392/36, 8258/832 | 95075 | 155100 |

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
were first submitted, in every client run, vanilla and Brimstone alike: all vanilla runs now draw the outline the
same way (they agree on 100% of the hologram frame), and the strict limit was re-collected by the rule above in a
SIFTEC noise collection run (40139105024): vanilla b/a 7/4, Brimstone dry run 15 (full) and 18 (old-GPU), so
18 x 1.5 = 27, rounded up to 30. That came from one shot per run and was too few samples: vanilla's own water
changes by 15-27 pixels from one shot to the next in the same run, and SIFTEC's minimap differs between runs. So the
limit was re-collected (with Andre's OK, 2026-10-25) over eleven shots of the scene per run, 0.5 s plus a steady-shot
wait apart, in run 40155387068 (dry run): vanilla b/a 221-234 and 9-22 (the 234 is mostly the minimap's tiles plus
scattered water pixels), Brimstone dry run up to 63 (full) and 24 (old-GPU): 234 x 1.5 = 351, rounded up to 10:
**360**. The outline covers about 14,500 pixels, about 3,900 of them over Brimstone's water.

The crosshair outline: the outline the game draws around the block under the crosshair moved by one pixel row from
one frame to the next in the old-GPU profile (GL 3.3), in vanilla's own runs too (run 40152673881: 2 of 11 hologram
shots in vanilla's old-GPU run, 5 of 11 in Brimstone's, with the same draws and the same target block in every frame),
so the shot decided about 100-230 pixels along the hologram's bottom edge. Since 2026-10-25 the CI pin mod
(`BlockOutlineMixin`) leaves that outline out of every client run, vanilla and Brimstone alike (with it, all eleven
shots in every profile agreed there, run 40154022133). **The block outline is therefore untested in CI**; package 6
or 7 must add a scene that checks it with the outline drawn.

The SIFTEC factory and map scenes run again since SIFTEC's latest release carries its selftest jar (2026-10-25); they
had no strict limits (skipped since package 1). Factory's first limit (100, run 40155387068 alone) was under-sampled: in about one run in
four, a yellow-and-black striped part of one Create machine in the factory is in another phase (its angle or
texture follows time), in vanilla's runs too (vanilla b against a: 315 pixels in run 40156711337 attempt 2;
Brimstone 313-315 in runs 40162905190 and 40166776073). By the rule, 315 x 1.5 = 472.5, rounded up to 100: **500**
(Andre's OK, 2026-10-25). Package 5 (Flywheel and Create) makes that animation repeatable in CI, vanilla and Brimstone
alike, then re-collects and lowers this limit. The map's first limit (13,100, one run) was under-sampled: vanilla runs differ from each
other there by up to 22,532 pixels, because SIFTEC's map shows whichever tiles it has scanned by the time it opens.
Two attempts to make it steady from the client side (Andre's time box) did not: waiting until two captures 5 s apart
were identical (run 40161774743: every run steady at once, vanilla runs still 37-44k pixels apart) and standing 60 s
in the area before opening (run 40162905190: 35-58k apart). So, as Andre decided (fallback (b)), the map has **no
strict gate** (`"ciNoStrict"` on the scene; compare.py reports its counts); its 8/255 limit (11.35%) stays.
**Package 7 (GUI) must give the map real strict coverage.**

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

### The 16-byte vertex: strict threshold 2/255 and new limits (decided by Andre, 2026-10-25)

Package 3 moved every terrain layer to one 16-byte vertex: positions off the 1/2048-block grid and UVs off the
1/65536 grid are rounded (largest error 0.000245 block and 1.45e-5 UV, at most 0.06 px on a 1280x720 screen; on-grid
vertices are bit-identical, shader harness). The rounding moves interpolated colours by 1-2/255 on rounded geometry
(water surfaces above all): at threshold 0 that added up to about 2,700 strict pixels in water_glass and 4,500 in
the hologram scene, almost all off by exactly 1/255, nothing visible (vanilla next to Brimstone at 4x, plant-heavy
and water-edge views, sent to Andre). **Andre's decision:** the strict gate counts a pixel only when it differs by
more than 2/255 in any channel, still only where every vanilla run agrees exactly; each scene's extra under that
threshold is measured, and **new strict limit = old limit + 1.5 x the extra**. The 8/255 gate is unchanged. The count
at any difference is still reported next to the gated one.

How the extra was measured: per scene and profile, the strict count (>2/255) in the five runs of the exact 28/16-byte
build before the switch (`a9444ac` 40164365265, `2e3f85f` 40166776073, `99fe3bd` 40167492584, `5298f1d`
40168745352, `998a7a0` 40169188320) and in the five runs with the 16-byte vertex (`1dd6880` 40170948997, `49fec37`
40172887031, `629f023` 40177279502, `9a3d411` 40180107484 attempts 1 and 2); extra = median after - median before,
the largest over the profiles. The median, not the largest single run, because single runs on both sides carry
run-wide noise events that vanilla shows too (spawn in run 40180107484 attempt 1: 2,228-2,309 in all three Brimstone
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
`translucent_move_oit.x2414`). The map stays ungated (`ciNoStrict`).

**sift** (re-collected, Andre's decision): its strict noise was the Sift dimension's floating note particles
(`the_sift:sift_note`, random places in every run, vanilla's too); mob spawning off plus sending the mobs away did not
remove them, the CI pin mod's `SiftParticlesMixin` (no particles in the Sift dimension, every client run) did. Samples
under >2/255 with the pin, runs 40185974374, 40187878826 and 40189806229: vanilla b/a 0/0 in each and the measure
runs 0, so vanilla's noise is 0; Brimstone 55 / 55 / 61 (full / old-GPU / Vulkan) in each, the 16-byte vertex's
rounding (decision 3's extra). Limit = noise + 1.5 x that extra = 91.5, rounded up to 100 pixels as for the other
SIFTEC scenes: **100** (was 4,900). Samples in `ci/pixel-noise-samples.json`.

## Count baselines (`ci/baselines/<suite>-<profile>.json`)

From the bench in each Brimstone run (20 s per scene in CI). **Draw calls**: per-frame average (frames that line up
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

The occlusion scene's baselines (package 2) come from one run with Brimstone's visibility module in dry run, i.e. with
Brimstone drawing exactly vanilla's entities and block entities.

Package 3's scenes (`translucent_move`, `water_glass_oit`, `translucent_move_oit`) got their baselines from the
collection run 40115084815, with Brimstone's translucency in dry run (vanilla drawing all translucent terrain, as
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
Brimstone's own draws are the same in every run. Per the design doc (Testing, "Counts that follow the runner's
speed"), this scene alone is gated on the per-frame **median** (`"ciDrawCallGate": "median"` in `siftec.json`), in
every profile it runs in (full and old-GPU; not run on Vulkan). Baseline = the highest clean median from package 3's
runs on `4cb5419`, `6d48fc7` and later. Every other scene and every other count is gated as before.

| run | commit | full: median (avg) | old-GPU: median (avg) | clean? |
|---|---|---|---|---|
| 40123696828 | `4cb5419` | 166 (119.16) | 170 (155.07) | no: translucency and visibility dry run (Brimstone drew vanilla's set, vanilla drew the translucent terrain) |
| 40126212181 | `6d48fc7` | 166 (118.92) | 167 (124.18) | yes (green) |
| 40128444322 | `0db931b` | 166 (144.24) | 167 (148.72) | yes (failed only on this average) |
| 40130610610 | `13f56fb` | 166 (142.45) | 167 (146.9) | yes (failed only on this average) |

Baselines: **full 166, old-GPU 167** (gate: x1.10 + 2), then **165 / 166** since the crosshair outline pin: it removed exactly one draw per frame from this scene (median 174 to 173 on full, 175 to 174 on old-GPU between runs 40152673881 and 40154022133), so the baselines went down by that draw, as after the texture-animation pin. The dry run's 170 is left out: in a dry run Brimstone isn't
drawing the translucent terrain itself, so its count is not Brimstone's.

### SIFTEC scenes on SIFTEC's 2026-10-25 release (factory and map back, sift uploads, hologram direct buffers)

Baselines from the three runs of commit `41c9e44` (run 40156711337, attempts 1-3; a run counts as clean for a scene
when that scene passed every check in it, Andre 2026-10-25):

- **`sift` upload bytes re-baselined** (Andre's rule: vanilla jumps the same way, so it is the pack). The per-frame
  median moves in about 12.9 KB steps (30k, 43k, 56k, 69k, 82k) and since SIFTEC's latest releases reaches 81,788 in
  some runs (over the old gate, about 77.7k). Vanilla's own counts in the same runs (branch-only jobs that ran the
  factory and sift scenes with every Brimstone module off, so the bench measured vanilla): vanilla a / b / old-GPU /
  Vulkan = 43,120 / 68,936 / 47,504 / 55,980, then 43,120 / 55,972 / 34,316 / 55,764, then 55,980 / 56,304 / 60,252 /
  43,128. Brimstone in the same runs: full 68,940 / 43,128 / 68,940, old-GPU 43,264 / 43,376 / 43,376, Vulkan
  55,980 / 56,084 / 43,232. New baselines: full **68,940** (was 55,764), old-GPU **43,376** (was 73,228: down),
  Vulkan **56,084** (was 55,980). Then, Andre's one-off exception to "highest clean value" (2026-10-25, other scenes
  keep the rule): the upload breakdown by kind shows every byte of the jump is in MAP_WRITE (writes into mapped GPU
  buffers) in vanilla and Brimstone alike; WRITE_TEXTURE is 0 everywhere, WRITE_BUFFER is constant (4,016 B/frame on
  full and Vulkan, 112 on old-GPU), always 3 + 16 uploads per frame (2 + ~15 on old-GPU). So the `sift` baseline is the
  highest value seen in any run, vanilla or Brimstone, **81,788** on every profile (gate as usual: x1.10 + 16 KiB).

  | run | vanilla a | vanilla b | vanilla old | vanilla vk | Brimstone full | Brimstone old | Brimstone vk |
  |---|---|---|---|---|---|---|---|
  | 40156711337 #1 | 43,120 (39,125) | 68,936 (64,928) | 47,504 (47,359) | 55,980 (54,012) | 68,940 (66,584) | 43,264 (44,452) | 55,980 (54,217) |
  | #2 | 43,120 (41,420) | 55,972 (51,423) | 34,316 (34,236) | 55,764 (51,497) | 43,128 (44,802) | 43,376 (43,256) | 56,084 (52,072) |
  | #3 | 55,980 (49,537) | 56,304 (56,785) | 60,252 (59,115) | 43,128 (38,981) | 68,940 (64,885) | 43,376 (43,246) | 43,232 (47,485) |
  | 40154022133 | - | - | - | - | 55,872 (51,858) | 30,860 (33,856) | 81,788 (77,935) |

  Upload bytes per frame, median; in brackets the MAP_WRITE part (a per-frame average, so it doesn't add up exactly
  to the median). Vanilla's factory uploads matched Brimstone's too (6,284-6,488 on full, 49,444-49,604
  old-GPU, 51,036-51,056 Vulkan).
- **`hologram` direct buffers re-baselined** (Andre's OK): full **8.24** (was 4.24), old-GPU **6.31** (was 3.73, one
  dry-run sample from when factory and map were skipped). The map scene now runs just before the hologram in the same
  client session, and the JVM's direct pool grows to 8.24 MB there and keeps that size (attempt 1, full: mam 7.73, map
  8.24, hologram 8.24; attempt 2, old-GPU the same). Old-GPU's value comes from attempts 1 and 3 (6.06, 6.31): attempt 2
  failed this very check (8.24 against the old 3.73), so it was not clean for the hologram (review finding, 2026-10-25);
  its 8.24 is within the new gate (6.31 x 1.10 + 4 = 10.94). Brimstone allocates no java.nio direct buffers at all, and a
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

Meshing time per section is measured since package 1 (the bench's `meshing` block: vanilla's mesher plus Brimstone's
transcode, sections meshed while measuring) and shown in CI's report; it is not gated (it follows CI's CPU load).

### Against vanilla in the same run: draw calls and terrain GPU memory (package 3, decided by Andre, 2026-10-25)

**Measure runs.** Every CI run has three more client runs per suite, `measure-full`, `measure-old` and `measure-vk`:
Brimstone loaded with every module off (`-Dbrimstone.ci.measureOnly=true`), in the profile of the Brimstone run of the
same name, so Brimstone's bench measures vanilla's own counts under the same pins and runner. They must look like
vanilla: their screenshots go through the same 8/255 and strict limits against vanilla's runs as Brimstone's runs
(compare.py's "Measure runs" table); run 40180107484 attempt 2: every measure run inside every limit. CI also
checks that a measure run really is vanilla: both modules' "measureOnly is set" log lines and no "on" line, every
bench with terrain off ("measure only (CI)") and visibility off, no Brimstone arena and no layer taken, and the same
profile checks of its probe as the Brimstone run (old-GPU's forced Intel flag). Since package 4 they also draw blocks
and items with Fabric's Indigo, as the vanilla runs do: CI takes `fabric-renderer-api-v1:contains_renderer` out of
the measure run's copy of the jar and Brimstone does not register its renderer when measureOnly is set; CI requires
Indigo's registration line and Brimstone's "Brimstone renderer: off" line, and no Brimstone registration.

**Draw calls.** In every scene, Brimstone's draw calls per frame against the measure run of its profile, on the
per-frame average **and** the median: the translucent scenes (`ci/vanilla-draw-calls.json`: water_glass,
translucent_move, water_glass_oit, translucent_move_oit, flythrough; SIFTEC hologram) at most vanilla's, the others at
most vanilla's x 1.10 + 2 (the count gate's allowance). These sit next to the committed count baselines, which stay.
Exceptions, each by Andre's rule:
- `siftec/hologram`: median only (decided earlier, see above).
- `siftec/sift`: median only, because its average follows the runner's frame rate (below 20 fps every frame lines up
  with a game tick and draws the tick's extra draws). Run 40180107484 attempt 1, full: Brimstone 154.0 average / 154
  median at 15.1 fps, vanilla's measure run 135.58 / 154 at 22.64 fps; Vulkan 153.97 / 154 at 15.8 fps against
  153.94 / 154 at 12.37 fps. (`"ciVanillaDrawGate": "median"` on the scene, with this reason.)
- `core/water_glass`, `water_glass_oit`, `nether`, `translucent_move`, `mobs`: median only, by the same rule. Each
  failed only on the average, in the Vulkan profile (lavapipe runs near 20 fps), where the Brimstone run and the
  measure run ran at different speeds, with equal medians: water_glass 124.0 / 124 at 18.57 fps against 103.78 / 124
  at 24.15 fps; water_glass_oit 129.01 / 129 at 9.86 fps against 129.0 / 129 at 13.46 fps; nether 120.73 / 121 at
  18.93 fps against 102.5 / 121 at 23.63 fps (all three run 40180107484 attempt 2); translucent_move 124.0 / 124 at
  15.57 fps against 123.4 / 124 at 19.28 fps; mobs 134.0 / 134 at 18.81 fps against 115.65 / 134 at 23.72 fps (both
  run 40193520268, and attempt 2 above). The reasons are on the scenes. Gated on both average and median: spawn,
  flythrough, occlusion, occlusion_move, translucent_move_oit, factory, hub, map.
- `siftec/mam`: median only, by the same rule (2026-10-26). Run 40238316211 (the ci-quick branch, main's code),
  Vulkan: Brimstone 132.6 average / 162 median at 25.17 fps against vanilla's measure run 117.94 / 162 at 29.31 fps
  (limit x 1.10 + 2: 131.73); full in the same run 116.03 / 162 against 116.48 / 162, both at 29.83 fps.
- `core/flythrough`: vanilla + 2 on full and Vulkan (old-GPU stays at vanilla), Andre's rule for a fly-through excess of 2 draws per frame or fewer. Run
  40180107484 attempt 1, Vulkan: average 123.11 against vanilla's 123.05 (medians 123 and 123; full 123.07 against
  123.07). Brimstone's terrain draws there are exactly vanilla's 3 indirect draws (solid, cutout, translucent: one
  arena each); the 0.06 is in the other draws (`single` 120.11 against 120.05), which depend on what the server has
  generated as the camera flies. (`"allowance"` in `ci/vanilla-draw-calls.json`.)

Before arenas per layer, the fly-through drew 136-139 against vanilla's 123 on full and Vulkan: translucent runs broke
at every change between two shared arenas (run 40172887031: 12-15 breaks per frame, none from index types after
`49fec37` widened 16-bit index lists to 32 bits). With arenas per layer (`629f023`) there are no arena breaks.

**Terrain GPU memory.** The bench reports terrain GPU memory reserved (buffers made) and used: vanilla's own heaps (its
section dispatcher's vertex and index buffers per layer: heaps x heap size, 128 MiB vertex heaps and 32 MiB index
heaps, made on first use) and Brimstone's arenas (each starts at 4 MiB and doubles up to 64 MiB, per layer). CI fails
when a Brimstone run reserves more than vanilla's measure run of the same profile, in any scene. Run 40180107484
attempt 1, full profile (MB reserved / used):

| scene | Brimstone | vanilla |
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

Scene clean-up (since the fix on main after package 2): scenes that clear an area move the entities there out of the
world (`tp` to y=-200) instead of killing them, so no death animation or dropped loot is left to bob in view at a
time that depends on how fast the runner is. The object-free `occlusion_move.bare_x2297` shot reached 72 strict
pixels against 70 in the Vulkan run on a dropped item from the clean-up; the limit was not raised.
