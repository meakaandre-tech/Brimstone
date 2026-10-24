# Brimstone progress

## Where things stand

- **Now:** package 0 (repo, CI, bench harness) done and merged; package 1 (terrain meshing and region buffers) done
  on branch `package-1`, review fixes in, green, awaiting re-review. See "Hand-off" at the bottom for the exact state.
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

## Package 1: terrain meshing and region buffers (done on the branch, review fixes in, awaiting re-review)

**Branch** `package-1` (not merged). **CI:** green run https://github.com/meakaandre-tech/Brimstone/actions/runs/40067617184
on `a7aff90` (every profile, both suites, compare passed, first run after the review fixes). **ci-logs** (overwritten by
later runs): `compare/report.md` has every table below plus a "Terrain module" table per suite and profile;
`runs/<suite>-brimstone-*/bench/*.json` have the `meshing` and `terrain` blocks.

**Done when (doc):** "Chunk sections mesh on worker threads into a compact vertex format, packed into large region
buffers and drawn with few calls. Screenshots match vanilla pixel for pixel on solid blocks."

### What was built (`src/main/java/com/meakaandre/brimstone/terrain/`, `mixin/terrain/`, `assets/brimstone/shaders/core/terrain.vsh`)

1. **Meshing on worker threads, every vertex exact.** Vanilla's mesher keeps running on its worker threads (AO,
   light, tint and FRAPI models through Indigo all baked as before). Where a finished section's layer would go into
   vanilla's buffers (`RenderSection.addSectionBuffersToUberBuffer`, on that worker thread), Brimstone takes the
   solid and cutout layers and sorts their quads into two vertex formats (`CompactVertex.split`), keeping vanilla's
   quad order within each:
   - **compact, 16 bytes** (vanilla's `DefaultVertexFormat.BLOCK` has 28: position 3xf32, colour RGBA8, UV0 2xf32,
     UV2 2xi16, checked at start-up): position u16x3 at 1/2048 block from -8, region slot u8, block light u8, colour
     RGB8, sky light u8, UV u16x2 at 1/65536 (65535 = 1.0). A quad goes here only if every one of its 4 vertices
     round-trips bit for bit: the stored value is decoded with the shader's own float operations and compared with
     vanilla's float bits (position, UV), and alpha must be 255. Light is stored clamped to 0..240, which lands on the
     same lightmap texel as vanilla's clamp in the shader.
   - **wide, 32 bytes** for every other quad (plant offsets, fluid heights, rotated parts, UVs off the 1/65536 grid,
     e.g. on atlases wider than 4096): vanilla's 28 bytes unchanged plus the u16 region slot and padding.

   Both live in the same arenas; each section layer is up to two allocations. Nothing is rounded any more, so the
   vertex stage gets vanilla's position, UV, colour and light for every vertex. Translucent stays with vanilla
   (package 3).
2. **Large region buffers, uploads as small copies.** Meshes live in a few 64 MiB GPU buffers ("arenas") sub-allocated
   with the game's own TLSF allocator (`TerrainStore`). Sections are grouped in regions of 8x4x8; a region keeps its
   sections in one arena where it can. Each part goes in as one `writeToBuffer` at the end of the frame, where vanilla
   uploads its own; then vanilla's own upload callback swaps the mesh in, so fade-in, visibility graph and block
   entities work as in vanilla. An arena that empties is closed (except the first), and a region's uniform buffer is
   closed when its last section goes.
3. **Few draw calls, through renderpearl only (both backends).** `TerrainRenderer` builds one draw list per layer and
   format from the sections vanilla found visible, and draws right after vanilla's own part of each layer, inside
   vanilla's terrain pass with vanilla's uniforms, atlas and lightmap bound. How it draws follows vanilla:
   - **plain path** (no multi-draw indirect, or Intel where vanilla's `multiDrawIndirectHasKnownIssues` is set; the
     target laptop): one direct multi-draw per region, layer and format (`RenderPass.multiDrawIndexed`, OpenGL's
     `glMultiDrawElementsBaseVertex`; on Vulkan the interleaved form when the device has `multiDrawDirectInterleaved`,
     else one draw per section). Section origins come from the region slot and a small uniform block per region,
     rewritten only when a fade-in value changes.
   - **indirect path** (frames where vanilla itself uses multi-draw indirect): one indirect multi-draw per arena,
     layer and format; Brimstone's commands and section data go into the same uploads as vanilla's.
4. **Shaders.** Brimstone's own vertex shader, written in its own structure (`terrain.vsh`: one body for both formats
   and both paths, own names for everything the interface doesn't force; only the shared uniform blocks, sampler and
   the instance attributes keep the game's names). It does the same float operations in the same order where order
   changes rounding (marked "order" in the file). The game's terrain fragment shader is used by reference
   (`minecraft:core/terrain`, not copied), so the pixel stage is vanilla's. Same depth state, culling, cut-off (0.5).
5. **Crash safety and hand-back.** States STARTING, ACTIVE, DEFERRED, OFF (`Terrain`). It turns on after its first
   frames prove every hook landed, the vanilla vertex format, that no resource pack replaces the shader code it stands
   in for, and its pipelines; the log says `Brimstone terrain: on (...)` with the formats, draw path and GPU error
   check. First-frame setup runs inside its own try/catch. Anything unexpected later (exception, pipeline that stops
   compiling, GPU error) turns it off for the session with one ERROR line and hands terrain back: staged meshes are
   dropped, every section it holds is marked dirty (`LevelExtractor.setSectionDirty`, per section, no full reset) so
   vanilla meshes it again. If the cause was outside drawing (meshing, bookkeeping) Brimstone keeps drawing what it
   holds until vanilla's new mesh of each section replaces it, so nothing disappears; after an error in drawing or a
   pack override it stops drawing at once and those sections are missing for the moment vanilla takes to re-mesh
   them. The log says how many sections were handed back and which of the two.
6. **Bench.** `meshing` in the JSON: sections meshed while measuring, vanilla mesher ms and Brimstone's transcode ms,
   `msPerSection`. `terrain` block: state, fallbacks, draw path, Brimstone's draws and section layers per frame,
   compact/wide bytes, bytes per vertex (live average), live compact/wide vertices and wide %, arenas and MB used,
   what vanilla's format would need, layers taken/kept, layers with wide quads by reason (position, UV, alpha).

### Review fixes (review of `82ba60c`)

- **M1, exactness.** Rounding is gone; per-quad split into the compact or the wide format as above, verified per
  vertex at transcode. Chosen by measurement: the alternative, leaving any layer with a misfit quad to vanilla, kept
  about half the layers with vanilla (attempt 2), while the split keeps every solid/cutout layer (layers kept by
  vanilla: 0 in every scene). Cost: live wide share 4.4-14.4% of vertices, so 16.7-18.3 bytes per vertex on average
  instead of 16 (vanilla 28), and one extra draw per layer and arena (indirect) or per region and layer (plain) where a
  region has wide quads.
- **M2, large atlases.** UVs are checked per vertex, so a quad off the compact UV grid takes the wide format, never
  rounded. Log lines: one when the block atlas is wider than 4096 at start-up, one on the first quad with an off-grid
  UV (seen in CI: some UVs are off the 1/65536 grid even on CI's atlases, 20-108 layers per scene).
- **M3, resource packs.** `TerrainResources` checks that `minecraft:shaders/core/terrain.vsh` and its includes (fog,
  globals, projection, sample_lightmap, terrainglobals, chunksection) come from the pack `vanilla`
  (`Resource.sourcePackId`); otherwise DEFERRED with a WARN line naming file and pack, vanilla draws all terrain.
  Re-checked after every resource reload (hook on `ShaderManager.apply`); back to STARTING when the override is gone.
- **M4, GPU errors.** `TerrainGpuErrors` reads renderpearl's `GpuDevice.getLastDebugMessages` before and after each
  terrain draw and upload; a new message of type ERROR (or a validation error) turns the module off. No raw GL.
  CI sets `glDebugVerbosity:1` (the game's default) in every run alike; `ci/compare.py` counts OpenGL debug ERROR
  messages and Vulkan validation lines per client log and fails if a Brimstone run logs more than the vanilla run of
  its profile. Result: core 0/0 in every profile; SIFTEC full 2/2, old-GPU 18/18 (SIFTEC's own shaders probing GLSL
  4.50/4.60 on Mesa, in vanilla as well), Vulkan 0/0.
- **M5, shader.** `terrain.vsh` rewritten as described in 4.
- **Minors.** First-frame setup outside the draw try/catch now has its own; staged meshes carry a generation and the
  store refuses meshes from a disposed section dispatcher's late workers (hooks on its constructor and on both ends of
  `dispose`), so nothing staged in one world reaches the next; a failed upload frees every buffer not yet copied and
  the allocation it was writing; compare.py fails a scene where Brimstone took no layers or drew no section layers or
  draw calls; empty arenas and region uniform buffers are released; mid-session hand-back as in 5; debug property
  `-Dbrimstone.debug.forcePlainTerrain=true` makes Brimstone use its plain path on every frame (WARN line when set).
- **Property not set in CI:** brimstone-vk is the only Vulkan Brimstone run, and setting it there would remove the
  only Vulkan coverage of the indirect path. The Vulkan plain path (interleaved multi-draw) stays uncovered in CI.
- **Core spawn noise (18.56% vanilla a/b in run 40064438473).** Cause: each core run generated its own world, and
  feature placement at chunk borders depends on chunk generation order, so trees differed between vanilla a and b.
  Now a `core-world` job generates the world once per CI run (`ci/core-world.sh`: same seed, the scene areas
  forceloaded, saved; cached on the script and game version) and every core run loads it. Spawn noise is now 0.36%.
  Limits and baselines untouched.

### Evidence (run 40067617184)

Pixel differences (% of pixels whose largest channel differs by more than 8; limits from `docs/ci-limits.md`,
unchanged):

| suite / scene | noise (vanilla a/b) | limit | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| core / spawn | 0.36 | 10.05 | 0.21 | 0.57 | 0.45 |
| core / fly-through | 0.03 | 14.95 | 0.05 | 0.02 | 0.01 |
| core / water and glass | 2.25 | 6.45 | 1.73 | 0.44 | 1.31 |
| core / nether | 0.07 | 0.8 | 0.01 | 0.01 | 0.34 |
| core / mobs | 0.13 | 0.4 | 0.14 | 0.12 | 0.14 |
| SIFTEC / The Sift | 0.33 | 1.15 | 0.18 | 0.03 | 0.11 |
| SIFTEC / HUB | 4.54 | 8.75 | 4.21 | 0.18 | 2.36 |
| SIFTEC / MAM | 4.68 | 9.3 | 4.44 | 0.09 | 2.12 |

Draw calls per frame on the **old-GPU profile** (vanilla's Intel flag forced; package 0 baseline is vanilla's
per-section path):

| scene | baseline (vanilla path) | Brimstone | of which Brimstone's terrain draws, for N section layers |
|---|---|---|---|
| core / spawn | 942.31 | 402.43 | 37.2 for 787 |
| core / fly-through (not gated) | 1146.26 | 458.55 | 57.6 for 937 |
| core / water and glass | 489.10 | 243.16 | 23.1 for 454 |
| core / nether | 687.04 | 244.90 | 31.1 for 626 |
| core / mobs | 434.39 | 256.02 | 21.1 for 340 |
| SIFTEC / The Sift | 849.48 | 448.63 | 21.1 for 426 |
| SIFTEC / HUB | 342.20 | 303.99 | 10.0 for 78 |
| SIFTEC / MAM | 400.79 | 303.92 | 7.0 for 145 |

On full and Vulkan, vanilla already draws terrain with about one indirect multi-draw per layer; Brimstone draws its
part with 3-5 per frame (per layer, arena and format) and the totals stay at the baseline (217-242 core, 297-426
SIFTEC; SIFTEC Vulkan HUB/MAM 333/377 against 317/353, within the allowance). Every other count (uploads, upload bytes,
heap after GC, direct buffers) is within its baseline in every profile.

**Vertex size and memory** (live, core full / SIFTEC full): bytes per vertex 17.2-18.2 / 16.7-18.3 (compact 16, wide
32, vanilla 28); wide share 7.5-13.9% / 4.4-14.4% of live vertices. Spawn 22.0 MB where vanilla's format would need
32.4 MB; fly-through 78.2 MB against 121.6 MB; MAM 16.6 MB against 25.6 MB. Arenas are reserved in 64 MiB steps (one
for 8-chunk scenes, two for the 12-chunk fly-through).

**Meshing time per section** (vanilla mesher + split, worker threads, while measuring; null where nothing meshed
while measuring): core full spawn 4.0 ms, fly-through 6.7 ms, nether 2.6 ms; old-GPU 5.5 / 8.1 / 2.4 ms; Vulkan 2.1 /
5.1 / 1.0 ms (CI's 4-core runners, llvmpipe sharing the CPU).

### Deviations from the doc and the plan

- **Meshing is vanilla's, transcoded** (the plan's choice), not a new mesher.
- **Two vertex formats** instead of one compact format: the compact format can't hold every vertex exactly, and
  exactness comes first (review M1).
- **Region buffers:** a few shared 64 MiB arenas with regions as a grouping inside them, not separate buffers per
  region (one draw per region and format, the vertex buffer changes only per arena). Written with copies, not
  persistently mapped.
- **Fragment shader is the game's own,** used by reference; only the vertex shader is Brimstone's.
- **Draw calls on full/Vulkan don't go down** (vanilla's indirect path already merges them); the gain there is vertex
  size and memory. The draw-call gain is on the plain path.
- **CI changes:** `core-world` job and `ci/core-world.sh`; `glDebugVerbosity:1` in the client options; compare.py
  gained the GPU-error, drew-terrain and fallback checks and the terrain table. No game files are published.

### Choices Andre should confirm

- Exact per-quad split (16-byte compact plus 32-byte wide) over rounding.
- Shared 64 MiB arenas instead of per-region buffers; arena size.
- Using the game's terrain fragment shader by reference; deferring to vanilla when a pack replaces the vertex shader
  or its includes (a pack replacing only the fragment shader keeps Brimstone on, and Brimstone draws with that shader).
- Baselines are left as they were. The old-GPU draw-call baselines could come down (e.g. spawn 942 to about 400) once
  more green runs confirm the new values.

### Remaining trade-offs and open issues

- **GPU error check on Vulkan:** renderpearl's Vulkan backend returns no debug messages to the game
  (`getLastDebugMessages` is empty), so the runtime check works on OpenGL only; the log line says which. Vulkan
  validation layers are not enabled in CI (they would slow lavapipe a lot and shift the gated per-frame counts);
  compare.py still counts any validation lines.
- A GPU error from other code that lands between Brimstone's mark and check (same render thread, inside the terrain
  draw or upload) would also turn Brimstone off: safe direction, not seen in CI.
- Draw order within a layer is compact quads then wide quads (depth test makes this invisible for opaque and cut-out
  geometry; coplanar quads of one section split across the formats could z-fight differently from vanilla, not seen
  in any CI scene).
- The mid-session hand-back is not exercised by CI (only the before-first-frame fallback was, in attempt 1).
- `brimstone.debug.forcePlainTerrain` is not set in CI (see above); the Vulkan plain path is uncovered.
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

**Now:** package 1 on branch `package-1` is implemented, the first review's findings are fixed and CI is green (report
above); the reviewer checks it again before the merge. The branch carries a branch-only CI commit (CI on pushes to
`package-1`) that should not go to main as is.
