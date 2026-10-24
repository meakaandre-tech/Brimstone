# Brimstone progress

## Where things stand

- **Now:** package 0 (repo, CI, bench harness) done and merged; package 1 (terrain meshing and region buffers) done
  on branch `package-1`, green, awaiting review. See "Hand-off" at the bottom for the exact state.
- **Pending on Andre:**
  - SIFTEC's `latest` release no longer carries SIFTEC's test commands (they moved to its CI-only `siftec_selftest`
    mod), so the SIFTEC factory and map scenes are skipped. Attaching `siftec-selftest-26.3.jar` to SIFTEC's
    `latest` release brings them back; Brimstone's CI already fetches it from there.
  - Laptop benches: package 0's vanilla baselines (steps in `docs/laptop-bench.md`), then after packages 1 (commands
    in the package 1 section), 2, 5 and 10.
- **To decide:** the "Choices to confirm" lists under each package.

---

## Package 0: repo, CI, benchmark harness

**Branch** `package-0`, squash-merged to main as `MERGE_SHA` (filled in after the merge).
**CI:** final branch run https://github.com/meakaandre-tech/Brimstone/actions/runs/39955089989 (green); main run:
MAIN_RUN. **ci-logs** (branch `ci-logs`, overwritten by every run): `compare/report.md` (all tables),
`compare/diff/<suite>-<scene>-<profile>.png`, `runs/<suite>-<run>/` with `shots/*.png`, `bench/*.json`,
`client-<n>.log`, `server.log`, `drive-*.json`, `gfx.txt`; `build/` holds the game class dumps.

### Done-when

1. **Repo: done.** meakaandre-tech/Brimstone, public. main's first commit is README, LICENSE (LGPL-3.0, canonical
   GNU text) and COPYING (GPL-3.0); everything else came in through `package-0`.
2. **Builds against 26.3: done.** Java 25, loader 0.19.5, Loom 1.17-SNAPSHOT, Fabric API 0.161.0+26.3, mod id
   `brimstone`, group `com.meakaandre`. Client entrypoint and client-only mixins; CI loads it on the server in every
   Brimstone run (`server.log`: `- brimstone 0.0.1+26.3`, no Brimstone errors, checked by `ci/compare.py`).
3. **Capability probe: done.** One line at start-up and a read-only `Capabilities` record
   (`BrimstoneClient.capabilities()`); nothing uses it yet. From the final run:
   - full: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=4.5 backend=OpenGL os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=yes GL_ARB_buffer_storage=yes deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`
   - old-GPU: `... gl=3.3 backend=OpenGL ... GL_ARB_multi_draw_indirect=no GL_ARB_buffer_storage=no ... vanilla.multiDrawIndirect=no vanilla.persistentMapping=no vanilla.mdiKnownIssues=yes`
   - Vulkan: `gpuVendor="0x10005" ... driver="1.4.318 llvmpipe Mesa 25.2.8..." gl=n/a backend=Vulkan ... GL_ARB_multi_draw_indirect=n/a GL_ARB_buffer_storage=n/a ... vanilla.mdiKnownIssues=no`
4. **`/brimstone bench [scene] [seconds] [confirm]`: done.** One block between `===== BRIMSTONE BENCH BEGIN =====`
   and `===== BRIMSTONE BENCH END =====` in latest.log, and a JSON file in `<game dir>/brimstone/bench/`: average
   FPS, 1% and 0.1% lows, frame-time p50/p95/p99/max, frames over 50 ms; draw calls, uploads and bytes per frame
   (average, median, p95, max, and by kind); heap used/after GC/committed, direct and mapped buffers, non-heap; the
   probe line, resolution, render distance, max FPS, vsync; the mod list hash; the counter audit. Draw calls and
   uploads come from count-only mixins on renderpearl's frontend (both backends) and on each backend's transient
   memory and buffer mapping (`docs/backend.md`). A scene changes the world, so it runs only with `confirm` and
   otherwise says what it will change.
5. **Scenes: done, two skipped.** One data file per suite in `src/main/resources/brimstone/scenes/`: core (plain
   world: spawn, fly-through at 12 chunks on a scripted path, water and glass, nether, mob crowd) and SIFTEC (The
   Sift, HUB screen, MAM screen; factory and fullscreen map **skipped**: both need SIFTEC's test commands, which
   are no longer in SIFTEC's release). Each scene pins time, weather, game mode, position and camera.
6. **CI: done.** Core and SIFTEC suites, each run as vanilla x2, vanilla old-GPU, vanilla Vulkan, Brimstone full,
   Brimstone old-GPU (with vanilla's Intel multi-draw-indirect flag forced), Brimstone Vulkan (lavapipe). Every
   scene is screenshotted and benched; pixel diffs against vanilla per profile, count baselines per profile with a
   10% gate, the counter audit, probe and backend checks; ci-logs; the jar as the `latest` release on main.
   Final run's pixel differences (% of pixels; limits in `docs/ci-limits.md`):

   | suite / scene | noise (vanilla a/b) | limit | full | old-GPU | Vulkan |
   |---|---|---|---|---|---|
   | core / spawn | 0.51 | 10.05 | 0.27 | 2.60 | 0.32 |
   | core / fly-through | 6.44 | 14.95 | 7.07 | 5.29 | 4.73 |
   | core / water and glass | 2.57 | 6.45 | 1.59 | 3.58 | 0.42 |
   | core / nether | 0.00 | 0.8 | 0.30 | 0.33 | 0.01 |
   | core / mobs | 0.15 | 0.4 | 0.14 | 0.05 | 0.12 |
   | SIFTEC / The Sift | 0.64 | 1.15 | 0.41 | 0.22 | 0.22 |
   | SIFTEC / HUB | 4.30 | 8.75 | 4.46 | 1.56 | 0.24 |
   | SIFTEC / MAM | 4.54 | 9.3 | 4.59 | 1.60 | 0.15 |
7. **Backend check: done.** `docs/backend.md`. 26.3 ships OpenGL and Vulkan (`com.mojang.renderpearl`, frontend
   over both). Chosen by `preferredGraphicsBackend` in options.txt (`default`/`opengl` try OpenGL then Vulkan;
   `vulkan` tries Vulkan then OpenGL) or the `--graphicsBackend` launch argument; a crashed start-up resets it.
   Minimum: OpenGL 3.3 core (no extension required). Vanilla's `GlHeuristics` flags multi-draw indirect as broken
   on Intel integrated graphics.
8. **Laptop steps: done.** `docs/laptop-bench.md`: a plain instance (Fabric, Fabric API, Brimstone) and the SIFTEC
   instance, exact Prism steps, which scenes, what to paste back. **Laptop bench pending** (Andre away).

### Pixel limits and count baselines

`docs/ci-limits.md` has every number and the reasoning. Limit per scene = max(2 x largest noise sample, largest +
0.25, 0.1) over all package-0 samples (vanilla a/b and every Brimstone profile, since Brimstone draws nothing yet).
Baselines = the highest clean value per count; draw calls gate on the per-frame average, uploads and bytes on the
median, plus memory; fly-through gated on memory only. Frozen from here on.

### Deviations from the doc and the brief

- Extra vanilla runs: a second vanilla run (noise), and vanilla in the old-GPU and Vulkan profiles, so each
  Brimstone profile is compared with vanilla on the same driver profile.
- SIFTEC factory and map scenes skipped (SIFTEC's test commands left its release); HUB and MAM are set up with
  plain commands (`setblock`) instead of SIFTEC's test commands.
- The factory target ("50+ Create machines") couldn't be checked: the buildings_a row has 8 buildings.
- CI client runs from Brimstone's own Gradle dev environment with the jars in `run/mods` (vanilla runs have no
  Brimstone anywhere), not from a Prism-like launcher.
- CI turns clouds off in the client options (their position follows elapsed game time, which differs between
  runs). Package 8 (effects, clouds) will need its own deterministic cloud check.
- Bench scenes leave the gamerules `advance_time`, `advance_weather`, `send_command_feedback` set to false.

### Things the doc gets wrong or doesn't know

- Vanilla 26.3 already keeps terrain in big shared buffers (TLSF-allocated heaps per layer) and draws it with one
  multi-draw indirect per layer. Only the plain path (no MDI, or Intel integrated where vanilla distrusts MDI,
  i.e. the school laptop) draws one call per section per layer. Package 1's gain is there and in the vertex
  format, not in replacing an unpooled vanilla. Research: `docs/terrain-research.md` (added with package 1).
- "Draw calls must go down against vanilla": on the full and Vulkan profiles vanilla is already at about 215-240
  calls per frame in CI, most of them not terrain.

### Choices to confirm

- The per-scene limit rule and the median/average choice for counts (`docs/ci-limits.md`).
- HUB and MAM opened with plain commands; factory and map skipped until SIFTEC publishes its selftest jar.

---

## Package 1: terrain meshing and region buffers (done on the branch, awaiting review)

**Branch** `package-1` (not merged). **CI:** green run https://github.com/meakaandre-tech/Brimstone/actions/runs/40063424378
on `1312f85` (every profile, both suites, compare passed). **ci-logs** (overwritten by later runs): `compare/report.md`
has every table below plus a "Terrain module" table per suite and profile; `runs/<suite>-brimstone-*/bench/*.json` have
the `meshing` and `terrain` blocks.

**Done when (doc):** "Chunk sections mesh on worker threads into a compact vertex format, packed into large region
buffers and drawn with few calls. Screenshots match vanilla pixel for pixel on solid blocks."

### What was built (`src/main/java/com/meakaandre/brimstone/terrain/`, `mixin/terrain/`, `assets/brimstone/shaders/core/terrain.vsh`)

1. **Meshing on worker threads into a 16-byte vertex.** Vanilla's mesher keeps running on its worker threads (AO,
   light, tint and FRAPI models through Indigo all baked as before). Where a finished section's layer would go into
   vanilla's buffers (`RenderSection.addSectionBuffersToUberBuffer`, on that worker thread), Brimstone takes the
   solid and cutout layers and transcodes them into its own vertex (`CompactVertex`): 16 bytes instead of vanilla's 28
   (`DefaultVertexFormat.BLOCK`: position 3xf32, colour RGBA8, UV0 2xf32, UV2 2xi16, checked at start-up). Layout:
   position u16x3 at 1/2048 block from -8 (section origin + -8..24), region slot u8, block light u8, colour RGB8, sky
   light u8, UV u16x2 at 1/65536 (65535 = 1.0). Light is clamped to 0..240, which the terrain shader clamps to anyway.
   Positions on the 1/2048 grid and UVs on the 1/65536 grid come back bit-exact (every block face of a model on
   vanilla's 1/16-pixel grid; every 1/16 texel of atlases up to 4096, CI's are 2048 and 4096). Off-grid values (plant
   offsets, fluid heights, rotated parts) are rounded to at most 1/4096 block; CI reports the share (positions
   9-22% of vertices, UVs under 1%). Translucent stays with vanilla (package 3), as does any layer that doesn't fit
   at all (alpha not 255, out of range; none in CI).
2. **Large region buffers, uploads as small copies.** Meshes live in a few 64 MiB GPU buffers ("arenas", made when
   needed, never resized or re-made) sub-allocated with the game's own TLSF allocator (`TerrainStore`). Sections are
   grouped in regions of 8x4x8; a region keeps its sections in one arena where it can, so it draws with one call.
   Each layer goes in as one `writeToBuffer` of its bytes at the end of the frame, where vanilla uploads its own;
   then vanilla's own upload callback swaps the mesh in, so fade-in, visibility graph and block entities work as in
   vanilla.
3. **Few draw calls, through renderpearl only (both backends).** `TerrainRenderer` builds one draw list per layer
   (solid, cutout) from the sections vanilla found visible, and draws right after vanilla's own part of each layer,
   inside vanilla's terrain pass with vanilla's uniforms, atlas and lightmap bound (Fabric's terrain events still
   wrap it). How it draws follows vanilla:
   - **plain path** (no multi-draw indirect, or Intel where vanilla's `multiDrawIndirectHasKnownIssues` is set; the
     target laptop): one direct multi-draw per region and layer (`RenderPass.multiDrawIndexed` with base vertices;
     OpenGL's `glMultiDrawElementsBaseVertex`, core since 3.2; on Vulkan the interleaved form when the device has
     `multiDrawDirectInterleaved`, else one draw per section). Section origins come from the vertex's region slot and
     a small uniform block per region (origin + fade-in per section), rewritten only when a fade-in value changes.
   - **indirect path** (frames where vanilla itself uses multi-draw indirect): one indirect multi-draw per arena and
     layer; Brimstone's commands and section data go into the same uploads as vanilla's.
4. **Shaders.** Brimstone's own vertex shader (reads the compact vertex, does the same arithmetic in the same order
   as the game's terrain vertex stage) and the game's own terrain fragment shader by reference
   (`minecraft:core/terrain`, not copied), so the pixel stage is vanilla's. Same depth state, culling, cut-off (0.5).
5. **Crash safety.** The module turns on only after its first frame proves every hook landed, the vanilla vertex
   format is the one it reads, and its pipelines compiled; the log says `Brimstone terrain: on (...)` with the draw
   path. Any exception in its code, a pipeline that stops compiling (resource reload) or a missing hook turns it off
   for the session with one ERROR line, drops its buffers and has vanilla rebuild every section (what F3+A does), so
   vanilla draws all terrain. Exceptions never escape into vanilla's code. CI fails if the module never turned on, if
   any fallback line appears, or if any bench reports it not active or a fallback count above zero
   (`ci/compare.py`). Attempt 1 showed the fallback working: a late-applied hook was missing, the module stayed off,
   vanilla drew everything, CI failed on it.
6. **Bench.** `meshing` in the JSON (was null): sections meshed while measuring, vanilla mesher ms avg/p50/p95 and
   Brimstone's transcode ms per layer, `msPerSection` (both). New `terrain` block: state, fallbacks, draw path,
   Brimstone's draws and section layers per frame, vertex bytes, arenas and MB used, what vanilla's format would need,
   layers taken/kept, rounded share. Both are in the latest.log block and in CI's report (reported, not gated).

### Evidence (run 40063424378)

Pixel differences (% of pixels whose largest channel differs by more than 8; limits from `docs/ci-limits.md`,
unchanged):

| suite / scene | noise (vanilla a/b) | limit | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| core / spawn | 1.02 | 10.05 | 0.74 | 1.18 | 0.23 |
| core / fly-through | 4.73 | 14.95 | 5.28 | 6.15 | 5.52 |
| core / water and glass | 0.92 | 6.45 | 1.81 | 1.86 | 1.27 |
| core / nether | 0.27 | 0.8 | 0.20 | 0.01 | 0.26 |
| core / mobs | 0.04 | 0.4 | 0.08 | 0.08 | 0.10 |
| SIFTEC / The Sift | 0.17 | 1.15 | 0.03 | 0.12 | 0.25 |
| SIFTEC / HUB | 4.19 | 8.75 | 2.42 | 3.30 | 4.33 |
| SIFTEC / MAM | 4.27 | 9.3 | 2.35 | 3.54 | 4.57 |

Draw calls per frame on the **old-GPU profile** (vanilla's Intel flag forced; package 0 baseline is vanilla's
per-section path):

| scene | baseline (vanilla path) | Brimstone | of which Brimstone's terrain draws, for N section layers |
|---|---|---|---|
| core / spawn | 942.31 | 386.90 | 22.1 for 594 |
| core / fly-through (not gated) | 1146.26 | 431.57 | 34.4 for 714 |
| core / water and glass | 489.10 | 233.13 | 13.0 for 271 |
| core / nether | 687.04 | 233.89 | 20.1 for 474 |
| core / mobs | 434.39 | 248.07 | 11.0 for 201 |
| SIFTEC / The Sift | 849.48 | 445.77 | 19.1 for 424 |
| SIFTEC / HUB | 342.20 | 299.21 | 6.0 for 49 |
| SIFTEC / MAM | 400.79 | 301.86 | 4.0 for 86 |

On full and Vulkan, vanilla already draws terrain with about one indirect multi-draw per layer; Brimstone draws its
part with about 2 per frame (one per layer per arena) and the totals stay at the baseline (218-240 core, 295-424
SIFTEC). Every other count (uploads, upload bytes, heap after GC, direct buffers) is within its baseline in every
profile.

**Vertex size and memory:** 16 bytes a vertex against vanilla's 28 (-43%). Live terrain in CI: spawn 19.2 MB where
vanilla's format would need 32.2 MB; fly-through 72.6 MB against 121.5 MB; MAM 15.3 MB against 25.6 MB. Arenas are
reserved in 64 MiB steps (one for 8-chunk scenes, two for the 12-chunk fly-through).

**Meshing time per section** (vanilla mesher + transcode, on the worker threads, while measuring; SIFTEC scenes and
mobs mesh nothing while measuring, so null there): core full spawn 4.2 ms, fly-through 6.7 ms, nether 3.0 ms; old-GPU
4.9 / 6.3 / 3.2 ms; Vulkan 3.3 / 5.9 / 2.7 ms (CI's 4-core runners, llvmpipe sharing the CPU).

### Deviations from the doc and the plan

- **Meshing is vanilla's, transcoded** (the plan's choice), not a new mesher: vanilla already meshes on worker
  threads and bakes AO/light/tint/FRAPI; Brimstone transcodes on the same thread. FRAPI models go through Indigo as
  in vanilla.
- **Region buffers:** a few shared 64 MiB arenas with regions as a grouping inside them, not separate buffers per
  region. Per-region buffers would mean hundreds of buffers and a buffer bind per region; with arenas a region is
  still one draw call and the vertex buffer changes only per arena. Arenas are written with copies, not persistently
  mapped.
- **Off-grid vertices are rounded** (at most 1/4096 block, 1/131072 of the atlas) instead of keeping those layers
  with vanilla. Keeping them left about half the layers with vanilla (attempt 2). Block faces on the model grid stay
  bit-exact; pixel diffs stay within the noise limits.
- **Fragment shader is the game's own,** used by reference; only the vertex shader is Brimstone's.
- **Draw calls on full/Vulkan don't go down** (vanilla's indirect path already merges them); the gain there is the
  vertex size and memory. The draw-call gain is on the plain path, as the research predicted.
- **Plain path on Vulkan:** interleaved multi-draw (VK_EXT_multi_draw through renderpearl's
  `multiDrawDirectInterleaved`) when the device has it; the log names the path.
- **CI script change:** `.github/dump-bytecode`/`dump-data` widened and the dump-data step added to `build.yml`
  (facts only; nothing judged differently). `ci/compare.py` gained the fallback checks and the terrain table.

### Choices Andre should confirm

- Rounding off-grid positions/UVs to the 1/2048-block, 1/65536-UV grid (vs keeping those layers with vanilla).
- Shared 64 MiB arenas instead of per-region buffers; arena size.
- Using the game's terrain fragment shader by reference (Brimstone's terrain follows vanilla shader changes and
  resource packs that replace that shader).
- Baselines are left as they were. The old-GPU draw-call baselines could come down (e.g. spawn 942 to about 390)
  once a second green run confirms the new values; not done on one run, as the old-GPU counts spread up to 15%.

### Open issues

- The fallback path (module off mid-session, vanilla rebuilds) ran only in its "before the first frame" form in CI
  (attempt 1). A mid-session fallback is not exercised by CI.
- SIFTEC Vulkan MAM draw calls came out at 390 against a baseline of 353 in one run (attempt 2) and 377 in attempt 1
  with the module off, on slower runners (22-23 fps against 29). It is a count-noise effect of frame rate (frames on a
  game tick draw more), not terrain; it may come back on a slow runner.
- Meshing time is null for scenes that mesh nothing while measuring; the fly-through, spawn and nether carry it.
- SIFTEC factory and map scenes are still skipped (SIFTEC's selftest jar is not in its release).

### Laptop bench (Andre, when back)

Same instances and steps as `docs/laptop-bench.md`, with the Brimstone jar from this branch (after the merge, the
`latest` release). Plain instance:

```
/brimstone bench flythrough confirm
/brimstone bench spawn confirm
/brimstone bench water_glass confirm
/brimstone bench mobs confirm
/brimstone bench nether confirm
```

SIFTEC instance:

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

Then paste the latest.log blocks (or the JSON files from `brimstone/bench/`). The `terrain` line should say `active`
with `plain, one multi-draw per region and layer` (Intel integrated: vanilla's flag sends it down the plain path) and
`fallbacks 0`; compare FPS and frame times with package 0's vanilla numbers.

---

## Hand-off

Working notes for a new chat picking this up: read this file, then `docs/backend.md`, `docs/ci-limits.md`,
`docs/laptop-bench.md` and the design doc. CI results are on the `ci-logs` branch. Local builds aren't possible in
the agent workspace (no access to Fabric's maven or Mojang); everything is built and tested in CI. A full CI run takes
about 25 minutes (45 when the SIFTEC world cache is rebuilt).

**Now:** package 1 on branch `package-1` is implemented and green (report above); a separate reviewer agent checks
it against the done-when and the rules before the merge. The branch carries a branch-only CI commit (CI on pushes to
`package-1`) that should not go to main as is.
