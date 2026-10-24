# Brimstone progress

## Where things stand

- **Now:** packages 0 (repo, CI, bench harness) and 1 (terrain meshing and region buffers) done and merged; package 2
  (visibility) done on branch `package-2`, green, awaiting review. See "Hand-off" at the bottom for the exact state.
- **Pending on Andre:**
  - SIFTEC's `latest` release no longer carries SIFTEC's test commands (they moved to its CI-only `siftec_selftest`
    mod), so the SIFTEC factory and map scenes are skipped. Attaching `siftec-selftest-26.3.jar` to SIFTEC's
    `latest` release brings them back; Brimstone's CI already fetches it from there.
  - Laptop benches: package 0's vanilla baselines (steps in `docs/laptop-bench.md`), then after packages 1 (commands
    in the package 1 section), 2, 5 and 10.
- **To decide:**
  - CI publishes javap dumps of the game's classes (signatures, and bytecode of the graphics and renderer classes) on
    the public `ci-logs` branch, as SIFTEC's CI does (Brimstone's dump was modelled on it, as asked). Package 1's CI
    briefly also published the game's shader sources there; that was removed and `ci-logs` republished without
    them on 2026-10-24, but GitHub may keep old force-pushed commits fetchable by ID for a while. Decide whether
    the class dumps may stay public, or whether CI should keep them private (agents can't download Actions
    artifacts from the workspace, so later packages would then work only from what was dumped before).
  - Package 1's compact vertex format now covers only about half the terrain (see package 1, "Review"): accept it,
    or pick one of the options listed there.
  - The "Choices to confirm" lists under each package.

---

## Package 0: repo, CI, benchmark harness

**Branch** `package-0`, squash-merged to main as `3394905`.
**CI:** final branch run https://github.com/meakaandre-tech/Brimstone/actions/runs/39955089989 (green); main run
https://github.com/meakaandre-tech/Brimstone/actions/runs/39962885289 (green, `latest` release published). **ci-logs** (branch `ci-logs`, overwritten by every run): `compare/report.md` (all tables),
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

## Package 1: terrain meshing and region buffers

**Branch** `package-1` (not merged). **CI:** green run https://github.com/meakaandre-tech/Brimstone/actions/runs/40072856079
on `159a0ac` (every profile, both suites, compare passed, including the new strict picture gate). **ci-logs**
(overwritten by later runs): `compare/report.md` has every table below plus a "Terrain module" table per suite and
profile; `runs/<suite>-brimstone-*/bench/*.json` have the `meshing` and `terrain` blocks.

**Done when (doc):** "Chunk sections mesh on worker threads into a compact vertex format, packed into large region
buffers and drawn with few calls. Screenshots match vanilla pixel for pixel on solid blocks."

### What was built (`src/main/java/com/meakaandre/brimstone/terrain/`, `mixin/terrain/`, `assets/brimstone/shaders/core/terrain.vsh`)

1. **Meshing on worker threads, every vertex exact, one format per layer.** Vanilla's mesher keeps running on its
   worker threads (AO, light, tint and FRAPI models through Indigo all baked as before). Where a finished section's
   layer would go into vanilla's buffers (`RenderSection.addSectionBuffersToUberBuffer`, on that worker thread),
   Brimstone takes the solid and cutout layers and converts each into one of two vertex formats
   (`CompactVertex.split`), keeping vanilla's quad order:
   - **compact, 16 bytes** (vanilla's `DefaultVertexFormat.BLOCK` has 28: position 3xf32, colour RGBA8, UV0 2xf32,
     UV2 2xi16, checked at start-up): position u16x3 at 1/2048 block from -8, region slot u8, block light u8, colour
     RGB8, sky light u8, UV u16x2 at 1/65536 (65535 = 1.0). A layer goes here only if every vertex in it round-trips
     bit for bit: the stored value is decoded with the shader's own float operations and compared with vanilla's
     float bits (position, UV), and alpha must be 255.
   - **wide, 28 bytes** (vanilla's size) for every other layer (one with plant offsets, fluid heights, rotated parts,
     UVs off the 1/65536 grid): vanilla's position, colour and UV unchanged, then block and sky light as u8 each and
     the u16 region slot in vanilla's 4 light bytes.
   - Light is stored clamped to 0..240 in both; that lands on the same lightmap texel as vanilla's own clamp in the
     shader (checked bit for bit, see 4).

   Nothing is rounded, and a layer never mixes formats, so coplanar quads keep vanilla's draw order. Translucent stays
   with vanilla (package 3).
2. **Large region buffers, uploads as small copies.** Meshes live in a few 64 MiB GPU buffers ("arenas") sub-allocated
   with the game's own TLSF allocator (`TerrainStore`). Sections are grouped in regions of 8x4x8; a region keeps its
   sections in one arena where it can. Each layer goes in as one `writeToBuffer` at the end of the frame, where vanilla
   uploads its own; then vanilla's own upload callback swaps the mesh in, so fade-in, visibility graph and block
   entities work as in vanilla. An arena that empties is closed (except the first), and a region's uniform buffer is
   closed when its last section goes.
3. **Few draw calls, through renderpearl only (both backends).** `TerrainRenderer` builds one draw list per layer and
   format from the sections vanilla found visible, and draws right after vanilla's own part of each layer, inside
   vanilla's terrain pass with vanilla's uniforms, atlas and lightmap bound:
   - **plain path** (no multi-draw indirect, or Intel where vanilla's `multiDrawIndirectHasKnownIssues` is set; the
     target laptop): one direct multi-draw per region, layer and format (`RenderPass.multiDrawIndexed`, OpenGL's
     `glMultiDrawElementsBaseVertex`; on Vulkan the interleaved form when the device has `multiDrawDirectInterleaved`,
     else one draw per section). Section origins come from the region slot and a small uniform block per region,
     rewritten only when a fade-in value changes.
   - **indirect path** (frames where vanilla itself uses multi-draw indirect): one indirect multi-draw per arena,
     layer and format, all from one Brimstone command list per frame that goes into vanilla's own command upload;
     section data goes into vanilla's instance upload.
4. **Shaders.** Brimstone's own vertex shader: its own code only decodes the 16-byte vertex and finds the section's
   position; fog distances and the lightmap lookup are the game's own include functions (`fog.glsl`,
   `sample_lightmap.glsl`, by reference), and the projection and fade-in keep the game's operand order. The game's
   terrain fragment shader is used by reference (`minecraft:core/terrain`, not copied). Same depth state, culling and
   cut-off (0.5). Checked locally on the same Mesa as CI (llvmpipe, GL 4.5), with the vanilla sources read locally
   and never committed: vanilla's vertex stage and Brimstone's, fed the same 200,000 vertices (light -40..400 too)
   through transform feedback, give bit-identical outputs (position, both fog distances, colour, UV, fade) in both
   formats. Rendered through the game's fragment shader they give identical pixels.
5. **Crash safety and hand-back.** States STARTING, ACTIVE, DEFERRED, OFF (`Terrain`). It turns on after its first
   frames prove every hook landed, the vanilla vertex format, that no resource pack replaces the shader code it stands
   in for, and its pipelines; the log says `Brimstone terrain: on (...)` with the formats, draw path and GPU error
   check. Anything unexpected later (exception, pipeline that stops compiling, GPU error) turns it off for the session
   with one ERROR line and hands terrain back (see "Re-review fixes", N-M1).
6. **Bench.** `meshing` (vanilla mesher and transcode ms, `msPerSection`) and `terrain` (state, fallbacks, draw path,
   draws and section layers per frame, compact/wide bytes, bytes per vertex, live compact/wide vertices and wide %,
   arena MB used, what vanilla's format would need, layers taken/kept, layers that went wide by reason).

### Fixes from the first review (of `82ba60c`)

- **M1, exactness:** rounding removed; exact formats as in 1. **M2, large atlases:** UVs are checked per vertex, so
  off-grid UVs make the layer wide, never rounded; log lines for an atlas wider than 4096 and for the first layer with
  an off-grid UV. **M3, resource packs:** `TerrainResources` checks that `minecraft:shaders/core/terrain.vsh` and its
  includes (fog, globals, projection, sample_lightmap, terrainglobals, chunksection) come from the pack `vanilla`;
  otherwise DEFERRED with a WARN line, re-checked after every resource reload (`ShaderManager.apply`).
  **M4, GPU errors:** `TerrainGpuErrors` compares renderpearl's `GpuDevice.getLastDebugMessages` before and after
  each terrain draw and upload (no raw GL); CI sets `glDebugVerbosity:1` in every run and compare.py fails if a
  Brimstone run logs more GL ERROR or Vulkan validation lines than its vanilla run (core 0/0, SIFTEC 2/2 and 18/18 on
  OpenGL from SIFTEC's own shader probes, Vulkan 0/0). **M5, shader:** own vertex shader, see 4.
- **Minors:** first-frame setup has its own try/catch; staged meshes carry a generation and meshes from a disposed
  dispatcher's late workers are refused; a failed upload frees what it hadn't copied; compare.py fails a scene where
  Brimstone drew no terrain; empty arenas and region buffers are released; debug property
  `-Dbrimstone.debug.forcePlainTerrain=true` (not set in CI: brimstone-vk is the only Vulkan Brimstone run, and
  setting it would remove Vulkan's indirect coverage).
- **Core spawn noise (18.56%):** each core run generated its own world, and feature placement at chunk borders
  depends on generation order. A `core-world` job (`ci/core-world.sh`) now generates the world once per cache key and
  every core run loads it; spawn noise is 0.32-0.36%.

### Re-review fixes (of `ae203ca`)

- **N-B1, nether one level darker:** not a Brimstone error. The shader's outputs are bit-identical to vanilla's
  (checked as in 4). The shift came from the lightmap's random block-light flicker: it changes every tick and moves
  every block-lit pixel by one colour level, and lava lights most of the nether scene. Vanilla against vanilla shows
  the same one-sided one-level shift: vanilla b against a differed on up to 349,419 such pixels in earlier runs. The
  re-review's mask (all four vanilla runs agree) happened to catch the four vanilla runs in one flicker state and the
  Brimstone runs in another. Fix on the CI side: `ci/pin-mod`, a CI-only mod built in the build job and loaded in every
  client run (vanilla and Brimstone alike), holds the flicker at zero, its resting value. compare.py fails a run where
  it isn't loaded. With it the four vanilla runs agree on 98.7% of the nether frame (22-71% before), and Brimstone
  differs there on 0 / 36 / 14 pixels (full / old-GPU / Vulkan). The vertex shader now also calls the game's fog and
  lightmap functions by reference, as asked.
- **New strict picture gate:** per scene and profile, the pixels where all four vanilla runs agree exactly but the
  Brimstone run differs at all (threshold 0). Limits are in `ci/strict-pixel-limits.json`, with samples and reasoning
  in `docs/ci-limits.md`: the largest noise sample x 1.5, may only go down. Noise sample means vanilla b against a
  where the other three agree, plus exact Brimstone builds. Vanilla's own noise under this metric goes far beyond the
  29-329 pixels the re-review measured: water animation and SIFTEC's screen fade reach 3,000-17,000 pixels, and MAM
  has two fade states about 95,000 apart. So the limits are per scene. The nether limit is 900; the re-review's
  248,492 would fail it.

  | scene | limit | full | old-GPU | Vulkan |
  |---|---|---|---|---|
  | core/spawn | 18000 | 4424 | 1140 | 4694 |
  | core/fly-through | 5900 | 966 | 917 | 335 |
  | core/water and glass | 25500 | 1242 | 2465 | 241 |
  | core/nether | 900 | 0 | 36 | 14 |
  | core/mobs | 600 | 182 | 127 | 207 |
  | SIFTEC/The Sift | 4900 | 794 | 449 | 210 |
  | SIFTEC/HUB | 12500 | 76 | 52 | 1292 |
  | SIFTEC/MAM | 155100 | 76 | 52 | 1292 |

- **N-M1, staged meshes on hand-back:** `TerrainStore.dropStaged` keeps the dropped meshes, and at the end of the frame
  (`finishDropped`) each one gets vanilla's own "layer uploaded" callback. So vanilla completes the mesh (with that
  layer empty) and later releases it the normal way, translucent parts included. If a newer mesh of the section is
  already in, the dropped one is released (`releaseSectionMesh`). Their sections are marked dirty with the held ones.
  This covers every fallback and the resource-pack defer after a reload. The log line gives how many sections were
  handed back and how many of them had a mesh on its way in.
- **m5, quad order:** one format per section layer (1). Cost, measured in run 40072856079: 43-56% of layers (by
  count) go wide, nearly all for positions off the 1/2048 grid (plant offsets, fluid heights) and 1-2% for UVs. Live
  wide share is 47-89% of vertices in core scenes and 15-75% in SIFTEC, so 21.7-26.6 bytes per vertex in core and
  17.8-25.0 in SIFTEC, against 28 for vanilla. With per-quad splitting it was 17.2-18.3, and with a 32-byte wide
  format it would have been worse than vanilla, so the wide format went to 28 bytes.
- **m3, upload bytes:** Brimstone's indirect commands are one list per frame (one slice), and with one format per
  layer the parts per frame are back to one per section layer (spawn 580-634, against 851). In the steady scenes
  upload bytes per frame are below the package 0 baseline in every profile (water and glass 15,044 against 15,576;
  nether 18,140 against 19,648). Spawn's median follows mesh uploads that are still arriving and moved between 23,300
  and 31,104 over the last runs (baseline 22,476, within the gate).
- **m6:** after a draw error during a keep-drawing hand-back, Brimstone's buffers go at the end of that frame.
- **m2:** the dormant `dump-data` step is removed from `build.yml`.
- **m1 (documented):** the GPU error check sees only messages whose text is new in renderpearl's last-10 list. A
  repeat of a message already there (GlDebug counts repeats instead of adding them) is missed. On Vulkan renderpearl
  gives the game no debug messages, so there is no runtime check there (the log line says so), and CI's validation
  match can't fire without validation layers. **m4 (documented):** the Vulkan plain path is not run in CI.

### Evidence (run 40072856079)

Pixel differences (% of pixels whose largest channel differs by more than 8; limits from `docs/ci-limits.md`,
unchanged); the strict counts are in the table above:

| suite / scene | noise (vanilla a/b) | limit | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| core / spawn | 0.32 | 10.05 | 0.53 | 0.55 | 0.64 |
| core / fly-through | 0.03 | 14.95 | 0.03 | 0.04 | 0.03 |
| core / water and glass | 2.59 | 6.45 | 0.71 | 0.49 | 1.32 |
| core / nether | 0.02 | 0.8 | 0.15 | 0.29 | 0.01 |
| core / mobs | 0.11 | 0.4 | 0.12 | 0.10 | 0.14 |
| SIFTEC / The Sift | 0.14 | 1.15 | 0.15 | 0.12 | 0.29 |
| SIFTEC / HUB | 3.99 | 8.75 | 0.76 | 3.10 | 4.09 |
| SIFTEC / MAM | 4.33 | 9.3 | 0.90 | 3.29 | 4.34 |

Draw calls per frame on the **old-GPU profile** (vanilla's Intel flag forced; package 0 baseline is vanilla's
per-section path):

| scene | baseline (vanilla path) | Brimstone | of which Brimstone's terrain draws, for N section layers |
|---|---|---|---|
| core / spawn | 942.31 | 405.94 | 38.2 for 632 |
| core / fly-through (not gated) | 1146.26 | 467.62 | 60.3 for 719 |
| core / water and glass | 489.10 | 223.19 | 18.0 for 273 |
| core / nether | 687.04 | 244.91 | 31.1 for 471 |
| core / mobs | 434.39 | 228.60 | 17.0 for 197 |
| SIFTEC / The Sift | 849.48 | 447.25 | 20.1 for 424 |
| SIFTEC / HUB | 342.20 | 303.21 | 10.0 for 49 |
| SIFTEC / MAM | 400.79 | 303.46 | 7.0 for 86 |

On full and Vulkan, vanilla already draws terrain with about one indirect multi-draw per layer. Brimstone draws its
part with 3-6 per frame (per layer, arena and format), and the totals stay at the baseline (183-242 core, 297-425
SIFTEC). Every other count (uploads, upload bytes, heap after GC, direct buffers) is within its baseline in every
profile.

**Vertex size and memory** (live, full profile): spawn 26.3 MB where vanilla's format would need 32.4 MB; fly-through
104.5 MB against 120.7 MB; nether 21.3 MB against 25.5 MB; water and glass 20.0 MB against 20.6 MB; MAM 20.7 MB
against 25.4 MB; The Sift 8.1 MB against 12.3 MB. Bytes per vertex: see m5 above.

**Meshing time per section** (vanilla mesher + conversion, worker threads, while measuring; null where nothing meshed
while measuring) stays in the range of the earlier runs: core 2-7 ms on CI's 4-core runners with llvmpipe sharing the
CPU.

### Deviations from the doc and the plan

- **Meshing is vanilla's, converted** (the plan's choice), not a new mesher.
- **Two vertex formats, one per layer:** the compact format can't hold every layer exactly, and exactness and
  vanilla's quad order come first. The memory gain is 0 to -34% depending on the scene (The Sift -34%, spawn -19%, fly-through -13%, water and glass -3%,
  mobs 0%), not the -43% of an all-compact format.
- **Region buffers:** a few shared 64 MiB arenas with regions as a grouping inside them, not one buffer per region.
- **Fragment shader and fog/lightmap functions are the game's own,** used by reference.
- **Draw calls on full/Vulkan don't go down** (vanilla's indirect path already merges them); the gain there is memory.
  The draw-call gain is on the plain path.
- **CI:** `core-world` job; CI-only lightmap pin mod in every client run; `glDebugVerbosity:1`; strict picture gate
  and limits; compare.py's GPU-error, drew-terrain and fallback checks. No game files are published.

### Choices Andre should confirm

- One format per layer (vanilla's quad order) over per-quad splitting (smaller: 17-18 bytes per vertex).
- The CI-only lightmap pin mod (it changes nothing but the flicker, in vanilla and Brimstone runs alike).
- The strict limits, which are large for scenes with animation or screen fades. Tighter limits need those pinned too.
- Shared 64 MiB arenas; deferring to vanilla when a pack replaces the terrain vertex shader or its includes.
- Baselines are left as they were. The old-GPU draw-call baselines could come down (e.g. spawn 942 to about 410).

### Remaining trade-offs and open issues

- GPU error check: OpenGL only, and it misses an exact repeat of a message already in the list (m1).
- The mid-session hand-back is not exercised by CI.
- The Vulkan plain path is not run in CI (m4).
- After a hand-back caused by a draw error or a pack override, sections are missing until vanilla has meshed them
  again; they are marked dirty, so this looks like F3+A.
- SIFTEC factory and map scenes are still skipped (SIFTEC's selftest jar is not in its release).

### Review

Three rounds by a separate reviewer agent that hadn't seen the work (reading the code, the CI logs and the
screenshots against the doc's done-when and the rules). Reviews kept in the session scratchpad; summary:

- **Round 1 (of `82ba60c`): blocking.** B1: the CI's new data-dump step published the game's shader sources on the
  public ci-logs branch (removed by the coordinator; ci-logs republished). M1 off-grid geometry rounded (now exact);
  M2 no atlas-size check (now per-vertex); M3 resource-pack terrain shaders ignored (now defers to vanilla); M4 GL
  errors not detected (now on OpenGL through renderpearl's debug messages, and compared in CI); M5 shader too close to
  vanilla's text (rewritten). Minors: first-frame crash path, staged meshes leaking into the next world, upload leak,
  no "did Brimstone draw" check, buffers never released, untested Vulkan plain path: all fixed or documented.
- **Round 2 (of `ae203ca`): blocking.** N-B1 nether one level darker on stable pixels (later shown to be the game's
  block-light flicker, not Brimstone; CI now pins it and a strict exact-pixel gate was added); N-M1 dropped staged
  meshes not marked dirty on hand-back (fixed); m5 quad order changed by splitting a layer across formats (now one
  format per layer); m3 upload bytes up (one command list per frame); m6, m2 fixed.
- **Round 3 (of `2f64f93`): no findings that break the doc or the rules.** Open for Andre:
  - **N-M2 (major, design):** the compact 16-byte format now holds only about half the terrain (21.7-26.6 bytes per
    vertex in core, 17.8-25.0 in SIFTEC, against vanilla's 28 and the doc's target of 16): one plant with a random
    offset sends its whole cutout layer to the 28-byte exact format. Options: (1) accept and update the doc;
    (2) store compact and wide runs in vanilla's quad order (more draw ranges); (3) a compact variant that holds plant
    offsets; (4) at least accept -0.0 positions and report the wide share per layer.
  - m1: the strict-gate limits are sound but loose where vanilla is noisy (worst case a local error of up to 2-3% of
    the frame on spawn and water and glass, 17% on MAM, could hide; MAM's limit comes from one fade outlier).
  - m3: no runtime GPU-error detection on Vulkan; Vulkan plain path not in CI; the transient hand-back gap.

**Merged** to main as `7534f16` (main run: see package 2's section). The package 0 merge had accidentally kept
`package-0` in main's CI trigger list (harmless; removed in this merge).

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

## Package 2: visibility

**Branch** `package-2` (not merged). **CI:** green run https://github.com/meakaandre-tech/Brimstone/actions/runs/40083026856
on `5d7dc04` (every profile, both suites, compare passed). **ci-logs** (overwritten by later runs): `compare/report.md`
has every table below plus a "Visibility" table per suite and profile; `runs/<suite>-brimstone-*/bench/*.json` have
the `visibility` block; the client logs have the `Brimstone visibility: on (...)` line.

**Done when (doc):** "Frustum plus cave culling over sections; block entities and entities hidden when nothing can see
them. No pop-in in the benchmark fly-through."

### What vanilla 26.3 already does (research, from the bytecode dump)

- **Sections: frustum and cave graph, already.** `SectionOcclusionGraph` walks a breadth-first graph from the camera's
  section through each section's `VisibilitySet` (which faces connect through non-opaque cells, built by `VisGraph`
  from `BlockState.isSolidRender`), with a direction rule ("smart cull") so the walk never turns back toward the camera.
  Full rebuilds run on the background executor, partial updates on the render thread when a mesh swaps in. The result
  is an octree; `LevelExtractor.applyFrustum` collects the sections in the frustum into `LevelRenderer.visibleSections`
  (only when the camera turned by a whole degree or the graph changed). That list is the only input of the terrain draw
  lists, vanilla's and Brimstone's (package 1). This is the doc's "cave graph walked from the camera's section".
- **Entities: frustum only.** `LevelExtractor.isEntityVisible` = `EntityRenderDispatcher.shouldRender` (frustum test on
  the renderer's culling box, plus the leash and rider cases) and `LevelRenderer.isSectionCompiledAndVisible`, which only
  checks that the entity's section has a mesh and is faded in to 0.3. It does **not** ask the cave graph: an entity in
  a cave section the graph has culled is still drawn, and so is one behind a wall in a visible section.
- **Block entities: per section only.** `extractVisibleBlockEntities` takes every renderable block entity of every
  section in `visibleSections` (faded in to 0.3). There is no test per block entity, neither frustum nor occlusion.
  Block entities whose renderer draws off-screen (beacons, structure blocks) are a separate global list.
- No GPU occlusion queries, no hierarchical depth. Graph quality: the graph is conservative for straight lines of
  sight (a line crosses each section through two connected faces and never reverses on an axis), but it can be stale
  while it rebuilds asynchronously, and it culls whole sections only.

### What was built (`src/main/java/com/meakaandre/brimstone/visibility/`, `mixin/visibility/`)

1. **Sections: vanilla's set, exactly.** Brimstone adds no section culling: the brief forbids dropping any section
   vanilla draws, and vanilla's graph already is the doc's frustum plus cave graph. Brimstone's terrain keeps drawing
   from vanilla's `visibleSections`. New: a per-frame audit while a bench measures (`TerrainRenderer.audit`): every
   solid and cutout layer with geometry of every section in vanilla's visible list must be in Brimstone's draw list or
   in vanilla's own buffers (checked under vanilla's lock); any that is in neither is counted as pop-in.
2. **Entities and block entities: an occlusion test that proves invisibility** (`Occlusion`). After vanilla's own test
   says "draw", Brimstone may still say "nothing can see it". The proof is a slab: a plane `axis = k` strictly between
   the camera and the object's box such that every line from the camera to the box crosses the plane inside a
   rectangle whose every cell, beyond the plane, is
   - a full opaque cube Brimstone trusts: a `minecraft:` block with `isSolidRender()` (the predicate vanilla's own cave
     graph and face culling use), a plain model, no random offset;
   - with its face toward the camera actually drawn: vanilla's `Block.shouldRenderFace` against the cell in front;
   - in a section vanilla draws this frame (in `visibleSections`), fully faded in, whose mesh was built after the last
     change to anything it depends on (`SectionChanges`: every `SectionUpdateTracker.setDirty`, every block change
     including those vanilla meshes nothing for, chunk data arriving or dropped (with neighbours), level resets; the
     same "compile start after change" comparison vanilla uses for its transient blocks).
   Such a line meets that drawn opaque face before the box, so the depth test hides it. The camera is treated as a box
   of +-0.25 blocks (view bobbing moves the eye about 0.1), and the rectangle bounds all segments from that box to the
   object's box (the crossing point is monotone in every corner coordinate, so its extremes are at corners). Candidate
   planes come from walking the grid along the centre segment: a plane that can prove anything is crossed there in one
   of its cells. The test is view-direction independent, so screen edges need nothing special; glass, leaves, slabs,
   fluids and any modded block never occlude; nearby objects (no plane fits between) are never hidden. A rectangle over
   4096 cells is not tried.
   The geometry was checked before coding with a Python model against brute-force random rays (about 1100 hidden
   cases, 0 violations; deliberately broken variants were caught: 40 and 4 violations).
3. **Which objects** (`Visibility.recognised`): only vanilla's own kinds drawn by vanilla's own renderer class.
   Entities: mobs (not the ender dragon, guardians (beams) or frogs (tongue)), items, experience orbs, armour stands,
   minecarts, item frames and paintings; never a player or mannequin (`Avatar`), anything with a custom name (name tags
   draw through walls), glowing or outlined, leashed, riding or ridden, the camera entity, larger than 6 blocks, or
   that vanilla doesn't cull (`affectedByCulling`). Box: vanilla's own culling box, grown by 1 + 0.25 x size (model
   parts, held items and shadows). Block entities: `minecraft:` types whose renderer is vanilla's and doesn't draw off
   screen; box = the block grown by 1 (beds, double chests, banners, bells, held items). Block entities are also hidden
   when that box is outside the frustum vanilla tests entities with (vanilla tests them per section only). Create's,
   Flywheel's, GeckoLib's and every other mod's entities and block entities are never touched.
4. **Cost:** render thread, inside vanilla's extract phase, with a budget of 2 ms per frame (past it the rest of the
   frame's objects are drawn untested). Measured in CI (llvmpipe, 4 cores shared): 0.01-0.14 ms per frame on average,
   at most 0.42 ms in one frame, never over budget. No worker threads: the cost doesn't call for them, and reading the
   level from other threads during extraction would need more care than it would save.
5. **Crash safety:** states STARTING, ON, OFF. It turns on at the first frame only if every change-tracking hook landed
   (own mixin config, not required, with a plugin that checks them) and the two added members (an invoker on
   `EntityRenderer`, a frame stamp on `RenderSection`) applied; it never hides anything while Brimstone's terrain is
   handing sections back. Any exception turns it off for the session with one ERROR line
   (`Brimstone visibility: turned off ...`); CI fails on that line and on any `fallbacks` in the bench.
6. **Bench** (`visibility` block and log line): sections visible, entities and block entities vanilla would draw /
   tested / hidden per frame (block entities also: hidden for being outside the frustum), ms per frame (average, max,
   frames over budget), section layers missing (the audit), and the ray check's objects / rays / violations /
   unchecked.

### Pop-in check (CI)

- **Terrain:** the audit in 1 runs in every Brimstone bench frame, fly-through included; compare.py fails on any
  missing section layer (and if the audit didn't run).
- **Entities and block entities:** every Brimstone client run sets `-Dbrimstone.debug.verifyVisibility=true`. Each
  object Brimstone hides is then checked independently (`VisibilityCheck`) with rays from the exact camera position to a
  grid on the box Brimstone claimed hidden (16 points per face, the corners, the centre). A point inside vanilla's
  frustum that a ray reaches without first entering an opaque cube through a drawn face is a violation (logged with
  positions). It shares only the opaque-cube rule with the test, none of its geometry. compare.py fails on any
  violation and on any hidden object left unchecked (ray budget 50,000 per frame).
- **Pictures:** both pixel gates in every scene and profile, including the new occlusion scene, where the wall and the
  floor hide some objects and others are seen through glass and a doorway.
- Limitation, stated plainly: the fly-through has nothing to hide (only the player and 1-2 block entities are ever in
  view, none hidden), so there the proof of "no pop-in" is the terrain audit (0 of about 430 visible sections' layers
  missing, every frame, every profile) and the pixel gates. The hiding itself is proved in the occlusion scene.

### Evidence (run 40083026856)

Pixel differences (% of pixels whose largest channel differs by more than 8; limits unchanged except the new scene):

| suite / scene | noise (vanilla a/b) | limit | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| core / spawn | 0.63 | 10.05 | 0.39 | 0.87 | 0.10 |
| core / fly-through | 0.04 | 14.95 | 0.04 | 0.03 | 0.05 |
| core / water and glass | 0.87 | 6.45 | 0.66 | 1.22 | 1.26 |
| core / nether | 0.25 | 0.8 | 0.25 | 0.19 | 0.33 |
| core / mobs | 0.14 | 0.4 | 0.13 | 0.08 | 0.10 |
| core / occlusion (new) | 0.00 | 0.25 | 0.00 | 0.00 | 0.00 |
| SIFTEC / The Sift | 0.49 | 1.15 | 0.45 | 0.17 | 0.05 |
| SIFTEC / HUB | 2.97 | 8.75 | 3.41 | 3.39 | 2.87 |
| SIFTEC / MAM | 3.22 | 9.3 | 3.65 | 3.45 | 2.65 |

Strict gate (pixels where all four vanilla runs agree exactly and Brimstone differs at all):

| scene | limit | full | old-GPU | Vulkan |
|---|---|---|---|---|
| core/spawn | 18000 | 470 | 379 | 123 |
| core/fly-through | 5900 | 3 | 627 | 314 |
| core/water and glass | 25500 | 2128 | 7127 | 4472 |
| core/nether | 900 | 0 | 86 | 0 |
| core/mobs | 600 | 44 | 90 | 60 |
| core/occlusion (new) | 100 | 0 | 0 | 0 |
| SIFTEC/The Sift | 4900 | 44 | 334 | 104 |
| SIFTEC/HUB | 12500 | 4 | 52 | 4 |
| SIFTEC/MAM | 155100 | 4 | 52 | 4 |

Visibility (per measured frame, same in all three profiles within rounding; full profile shown):

| scene | sections visible | entities vanilla draws (tested, hidden) | block entities (tested, hidden) | cull ms (max) | layers missing | ray check: objects / rays / violations |
|---|---|---|---|---|---|---|
| spawn | 326 | 1 (0, 0) | 11 (11, 0) | 0.13 (0.20) | 0 | 0 / 0 / 0 |
| fly-through | 430 | 1 (0, 0) | 1.9 (0, 0) | 0.00 | 0 | 0 / 0 / 0 |
| water and glass | 139 | 1 (0, 0) | 0 | 0.00 | 0 | 0 / 0 / 0 |
| nether | 300 | 1 (0, 0) | 1 (1, 1 outside the frustum) | 0.00 | 0 | 301 / 0 / 0 |
| mobs | 107 | 96 (95, 0) | 0 | 0.13 (0.22) | 0 | 0 / 0 / 0 |
| occlusion | 49 | 11 (10, 5) | 9 (9, 4) | 0.06 (0.09) | 0 | 5400 / 533,400 / 0 |
| SIFTEC The Sift | 244 | 2 (1, 0) | 0 | 0.01 (0.03) | 0 | 0 / 0 / 0 |
| SIFTEC HUB / MAM | 38 / 55 | 1 (0, 0) | 1 (0, 0) | 0.00 | 0 | 0 / 0 / 0 |

(The player counts as one entity vanilla draws in every scene; in spectator mode Brimstone never tests it.) The mob
crowd stands in the open, so nothing there is hidden, and it is identical under both gates. In SIFTEC nothing is
hidden either: its machines are Create's (left to Create and Flywheel) and its scenes hold no vanilla entities behind
walls.

Counts against baseline: every gated count within its baseline in every profile and both suites. Occlusion scene
(baseline = the dry-run run, Brimstone drawing vanilla's set): draw calls 156.6 / 166.7 / 156.9 against 159.5 / 169.7 /
161.1 (full / old-GPU / Vulkan); upload bytes per frame (median) 66.7 KB / 64.5 KB / 66.7 KB against 126.3 / 124.2 /
126.4 KB (the hidden entities' per-frame data is no longer uploaded). Mob crowd heap after GC is 277 MB against a
baseline of 268 (within the gate; it was the same in the dry run, before anything was hidden).

### Runs

1. `ddae18c` run 40079570978: dry run (decide and count, hide nothing) to sample the occlusion scene's noise. Failed,
   as planned, only on the new scene's missing limits; 0 violations, 0 missing layers, everything else green.
2. `c6adfc8` run 40081970862: hiding on, new limits. Failed on SIFTEC/The Sift old-GPU 1.50% (limit 1.15%). The cause
   was vanilla's own old-GPU run: its screenshot was taken after F3+A's chat line ("Reloading all chunks") had faded,
   while every other run still showed it (vanilla-old against vanilla-a: 1.60%; Brimstone old-GPU against vanilla a
   and b: 0.25% and 0.21%, exact in the chat band). Brimstone hid nothing in that scene.
3. `5d7dc04` run 40083026856: CI clears chat (vanilla's F3+D) after F3+A in every run, vanilla and Brimstone alike.
   Green.

### Deviations from the doc and the brief

- **No section culling of Brimstone's own.** Vanilla already has the doc's frustum plus cave graph, and anything
  stricter could only drop sections vanilla draws, which the brief forbids. Brimstone keeps vanilla's set and audits
  it. "Only visible sections enter the draw list" was already true in package 1.
- **The occlusion test is a slab proof, not a general occlusion query.** It is exact where it says "hidden", but it
  hides less than a depth-buffer test would (no combination of several partial occluders, generous boxes around
  entities). Correctness first.
- **No worker threads** (cost measured at 0.01-0.14 ms per frame on CI). The 2 ms budget caps the render-thread cost.
- **CI:** new core scene `occlusion` with its own limits (8/255: 0.25%, strict: 100 pixels) and count baselines from
  the dry run; `ci/client-run.sh` turns the ray check on in every Brimstone run (and supports `VISIBILITY_DRY_RUN=1`,
  used once); `ci/drive.py` clears chat after F3+A; compare.py gates the visibility block; more classes in
  `.github/dump-bytecode` (the visibility and entity classes: `SectionOcclusionGraph.*`, `Octree.*`, `ViewArea`,
  `SectionUpdateTracker.*`, `VisGraph`, entity and block-entity render dispatch, `Frustum`, `Camera`,
  `ClientChunkCache.*`). No game files are extracted or published.

### Choices Andre should confirm

- Hide only vanilla's own entity and block-entity kinds and trust only vanilla's own full opaque blocks as occluders.
  Caveat: a resource pack that gives a full opaque block (stone, dirt...) a see-through model or texture (an "x-ray"
  pack) would let things show through in vanilla that Brimstone hides. Vanilla's own cave graph and face culling make
  the same assumption, but at section scale. Options: accept; or switch the module off when a non-built-in resource
  pack is selected.
- The margins around entities (1 + 0.25 x size blocks) and block entities (1 block), and the exclusions in "Which
  objects".
- The occlusion scene's strict limit: all four samples were 0, and the rule (largest x 1.5, rounded up to 100) would
  give 0; I set one rounding step, 100 pixels (written down in `docs/ci-limits.md`). Its limits and baselines come from
  one run.
- Clearing chat (F3+D) in CI before world screenshots (it changes every world screenshot, in vanilla and Brimstone
  runs alike).
- Baselines left as they were. The occlusion scene's upload bytes could come down (126 KB to about 67 KB); the
  old-GPU draw-call baselines from package 1 still could too.

### Remaining trade-offs and open issues

- The fly-through has nothing to hide, so it exercises the terrain audit and the pixel gates, not the hiding (see
  "Pop-in check").
- A block change that vanilla decides needs no new mesh (`ModelManager.requiresRender` false) is tracked by Brimstone
  anyway; the only state change Brimstone could miss is one that doesn't pass through any of the hooked paths (none
  known).
- The `SIFTEC` factory and map scenes are still skipped (selftest jar not in SIFTEC's release), so the Create-heavy
  factory isn't in CI.

### Laptop bench (Andre, when back)

Same instances and steps as `docs/laptop-bench.md`, with this branch's jar (after the merge, the `latest` release).
Plain instance:

```
/brimstone bench occlusion confirm
/brimstone bench mobs confirm
/brimstone bench flythrough confirm
/brimstone bench spawn confirm
/brimstone bench water_glass confirm
/brimstone bench nether confirm
```

SIFTEC instance:

```
/brimstone bench sift confirm
/brimstone bench hub confirm
/brimstone bench mam confirm
/brimstone bench occlusion confirm
/brimstone bench mobs confirm
/brimstone bench flythrough confirm
```

Paste the latest.log blocks (or the JSON files). The `visibility` line should say `on`, `fallbacks 0`, `section layers
missing 0`, and in `occlusion` about 5 entities and 4 block entities hidden per frame; note its `ms/frame` and `max`
(the culling cost on the laptop's CPU) next to FPS and frame times.

---

## Hand-off

Working notes for a new chat picking this up: read this file, then `docs/backend.md`, `docs/ci-limits.md`,
`docs/laptop-bench.md` and the design doc. CI results are on the `ci-logs` branch. Local builds aren't possible in
the agent workspace (no access to Fabric's maven or Mojang); everything is built and tested in CI. A full CI run takes
about 25 minutes (45 when the SIFTEC world cache is rebuilt).

**Now:** package 2 (visibility) implemented on branch `package-2` (green; see its section): next, review rounds, then
merge. CI's dry-run switch (`VISIBILITY_DRY_RUN=1` in the client job's env) samples a new scene's noise with
Brimstone drawing exactly vanilla's set.
