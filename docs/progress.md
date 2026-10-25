# Brimstone progress

## Where things stand

**For Andre (2026-10-26):** package 3 merged as MERGE_SHA (main CI MAIN_RUN, `latest` rebuilt). PACKAGE4_STATUS

Decisions made on your behalf while you were away (each also in its section):
1. **Strict extras measured on medians:** extra = median of five 16-byte runs minus median of five runs before the
   switch, largest over the profiles (not the largest single run, which carried run-wide noise that vanilla shows
   too; that would have raised spawn by 2,834 and hub by 1,638). Every run passes the new limits (`docs/ci-limits.md`).
2. **Permanent `measure-*` CI runs** (Brimstone loaded, every module off) to measure vanilla in the same run: your
   memory gate and same-run draw-call gate need vanilla's numbers and vanilla runs have no bench. They are checked
   against vanilla's pictures, and that their modules really stayed off (review round 3).
3. **Fly-through vanilla + 2** (your rule, full and Vulkan only): Vulkan averaged 123.11 against 123.05 in one run;
   Brimstone's terrain draws are exactly vanilla's 3, the 0.06 is in other draws.
4. **sift draw calls on the median** (your rule): its average follows the runner's fps (154.0 at 15.1 fps against
   135.58 at 22.64 fps; medians 154 and 154).
5. **sift particles:** the CI pin holds off every particle in the Sift dimension (25 `the_sift:sift_note`, one
   `minecraft:poof`), not only the notes; sift strict limit re-collected: 4,900 -> 100. Mob spawning is off for the
   sift scene and back on in the next one; the setup sends every non-player entity away.
6. **Translucent scene list for the at-or-under-vanilla draw gate** stays water_glass, translucent_move,
   water_glass_oit, translucent_move_oit, flythrough and the hologram; spawn, hub, mam and the rest are gated at
   vanilla x 1.10 + 2 (hub and mam averages are 1-7 draws over vanilla's on some profiles). Confirm or tighten.
7. **The memory gate reads one snapshot at the end of each bench**; the moment of a grow, when an arena's old and new
   buffers both exist (up to 96 MiB for one arena), isn't measured.
8. **The hologram scene skips the measure-vk run** (lavapipe segfaults on Create's outliner, as in vanilla's run).

Known limits recorded for later packages: vertex alpha below 255 (package 4), a grown arena doesn't shrink until
empty (package 9), a deterministic particle scene (package 8), the block outline untested in CI (package 6 or 7), the
factory's Create animation (package 5), the map's strict coverage (package 7).

- **Done and merged:** packages 0 (repo, CI, bench harness), 1 (terrain meshing and region buffers), 2 (visibility)
  and 3 (translucency, the 16-byte vertex, arenas per layer).
- **Pending on Andre:** laptop benches (`docs/laptop-bench.md`, and each package's "Laptop bench" section); the
  "Choices to confirm" lists under each package.

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

- **The SIFTEC hologram scene's draw-call baseline on full (119.16) came from one sample at 30 fps, and the scene's
  average follows the runner's frame rate** (run 6 above: 144.24 at 23.6 fps, over the 10% gate, with Brimstone's own
  draws unchanged). On a slow runner that gate can fail without any change in Brimstone. Raising the baseline or
  changing what is gated is Andre's call (rules); options: take the baseline from the slow mode (about 155, as on
  old-GPU), gate this scene's draw calls on the median (166-170 in every run), or report them only, like the
  fly-through's.
- GPU error check: OpenGL only, and it misses an exact repeat of a message already in the list (m1).
- The mid-session hand-back is not exercised by CI.
- The Vulkan plain path is not run in CI (m4).
- After a hand-back caused by a draw error or a pack override, sections are missing until vanilla has meshed them
  again; they are marked dirty, so this looks like F3+A.
- SIFTEC factory and map run again since 2026-10-25 (map without a strict gate; see "Finishing package 3").

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

**Merged** to main as `7534f16` (main run https://github.com/meakaandre-tech/Brimstone/actions/runs/40075302839, green, `latest` published). The package 0 merge had accidentally kept
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

**Branch** `package-2` (not merged). **CI:** green run https://github.com/meakaandre-tech/Brimstone/actions/runs/40093135028
on `a09678d`, after the review fixes (every profile, both suites, compare passed). The first green version, before
review, was `5d7dc04` (run 40083026856). **ci-logs** (overwritten by later runs): `compare/report.md` has every table
below plus a "Visibility" table per suite and profile; `runs/<suite>-brimstone-*/bench/*.json` have the `visibility`
block; `runs/core-*/shots/occlusion_move.*.png` are the fixed-point shots.

**Done when (doc):** "Frustum plus cave culling over sections; block entities and entities hidden when nothing can see
them. No pop-in in the benchmark fly-through."

### What vanilla 26.3 already does (research, from the bytecode dump)

- **Sections: frustum and cave graph, already.**
  - `SectionOcclusionGraph` walks a breadth-first graph from the camera's section through each section's
    `VisibilitySet`: which faces connect through non-opaque cells, built by `VisGraph` from
    `BlockState.isSolidRender`. A direction rule ("smart cull") keeps the walk from turning back toward the camera.
  - Full rebuilds run on the background executor; partial updates run on the render thread when a mesh swaps in.
  - The result is an octree. `LevelExtractor.applyFrustum` collects the sections in the frustum into
    `LevelRenderer.visibleSections`, but only when the camera has turned by a whole degree or the graph has changed.
  - That list is the only input of the terrain draw lists, vanilla's and Brimstone's (package 1). This is the doc's
    "cave graph walked from the camera's section".
- **Entities: frustum only.**
  - `LevelExtractor.isEntityVisible` is `EntityRenderDispatcher.shouldRender` (a frustum test on the renderer's
    culling box, plus the leash and rider cases) and `LevelRenderer.isSectionCompiledAndVisible`. The latter only
    checks that the entity's section has a mesh and is faded in to 0.3.
  - It does **not** ask the cave graph: an entity in a cave section the graph has culled is still drawn, and so is
    one behind a wall in a visible section.
- **Block entities: per section only.**
  - `extractVisibleBlockEntities` takes every renderable block entity of every section in `visibleSections` (faded in
    to 0.3). There is no test per block entity, neither frustum nor occlusion.
  - Block entities whose renderer draws off-screen (beacons, structure blocks) are a separate global list.
- **No GPU occlusion queries, no hierarchical depth.**
- **Graph quality:** the graph is conservative for straight lines of sight (a line crosses each section through two
  connected faces and never reverses on an axis). But it can be stale while it rebuilds asynchronously, and it culls
  whole sections only.

### What was built (`src/main/java/com/meakaandre/brimstone/visibility/`, `mixin/visibility/`)

1. **Sections: vanilla's set, exactly.**
   - Brimstone adds no section culling. The brief forbids dropping any section vanilla draws, and vanilla's graph
     already is the doc's frustum plus cave graph. Brimstone's terrain keeps drawing from vanilla's `visibleSections`.
   - New: a per-frame audit while a bench measures (`TerrainRenderer.audit`). Every solid and cutout layer with
     geometry, of every section in vanilla's visible list, must be in Brimstone's draw list or in vanilla's own buffers
     (checked under vanilla's lock). Any layer that is in neither is counted as pop-in.
2. **Entities and block entities: an occlusion test that proves invisibility** (`Occlusion`). After vanilla's own test
   says "draw", Brimstone may still say "nothing can see it". The proof is a slab: a plane `axis = k` strictly between
   the camera and the object's box, such that every line from the camera to the box crosses the plane inside a
   rectangle whose every cell, beyond the plane, is:
   - a full opaque cube Brimstone trusts: a `minecraft:` block with `isSolidRender()` (the predicate vanilla's own
     cave graph and face culling use), a plain model, and no random offset;
   - showing a face toward the camera that is actually drawn: vanilla's `Block.shouldRenderFace` against the cell in
     front;
   - in a section vanilla draws this frame (in `visibleSections`), fully faded in, whose mesh was built after the last
     change to anything it depends on. `SectionChanges` tracks every `SectionUpdateTracker.setDirty`, every block
     change (including those vanilla builds no new mesh for), chunk data arriving or dropped (with neighbours), and
     level resets. It uses the same "compile start after change" comparison vanilla uses for its transient blocks.

   Such a line meets that drawn opaque face before it reaches the box, so the depth test hides it. Details:
   - The camera is treated as a box of ±0.25 blocks, because view bobbing moves the eye by about 0.1.
   - The rectangle bounds all segments from that box to the object's box. The crossing point is monotone in every
     corner coordinate, so its extremes are at corners.
   - Candidate planes come from walking the grid along the centre segment: a plane that can prove anything is crossed
     there in one of its cells.
   - The test doesn't depend on the view direction, so screen edges, roll and bobbing need nothing special.
   - Glass, leaves, slabs, fluids and any modded block never occlude. Nearby objects (no plane fits between) are never
     hidden.
   - Not tried: a rectangle over 4096 cells, or more than 32 full rectangles per object.
   - The geometry was checked before coding with a Python model against brute-force random rays: about 1100 hidden
     cases, 0 violations; deliberately broken variants were caught. The reviewer's independent model agrees
     (`scratchpad/model/slab.py` in the session; not in the repo).
3. **Which objects: allow-lists** (`Visibility.ENTITY_MARGIN`, `Visibility.BLOCK_ENTITY_TYPES`). The type and the
   extent reasoning for each are in **`docs/visibility-allowlist.md`**. The object's class and its renderer's class
   must also be vanilla's.
   - **Entities:** cow, mooshroom, pig, sheep, chicken, villager, iron golem, snow golem, zombie, skeleton, creeper,
     spider, item. Box: vanilla's culling box grown by 2 blocks on every side, times the scale attribute.
   - **Never hidden, whatever the type:** a custom name, glowing, leashed, riding or ridden, sleeping, dying, the
     camera entity.
   - **Block entities:** chest, trapped chest, ender chest, sign, hanging sign, banner, bell, shulker box, decorated
     pot, campfire, lectern, enchanting table. Box: the block grown by 1.
   - Everything else is never hidden: end gateways, beacons, ghasts and every modded type (Create, Flywheel,
     GeckoLib...).
4. **No frustum test of Brimstone's own.** The frame is drawn with view bobbing and the damage tilt on top of the
   frustum vanilla culls with, so a per-block-entity frustum test was unsound (review M1). It is gone; block entities
   are only ever hidden by the occlusion proof.
5. **Cost:**
   - Render thread, inside vanilla's extract phase, with a budget of 2 ms per frame. The timer covers all the work per
     object; past the budget, the rest of the frame's objects are drawn untested.
   - Measured in CI (llvmpipe, 4 cores shared): 0.00-0.16 ms per frame on average, at most 0.6 ms in one frame, never
     over budget.
   - No worker threads: the cost doesn't call for them.
6. **Crash safety and deferral:** states STARTING, ON, DEFERRED, OFF.
   - **Turning on** happens at the first frame, only if:
     - every change-tracking hook landed (its own mixin config, not required, with a plugin that checks them);
     - the two added members applied (an invoker on `EntityRenderer`, a frame stamp on `RenderSection`).
   - **Nothing is hidden** while Brimstone's terrain is handing sections back.
   - **DEFERRED** (nothing hidden, one WARN line) while any pack other than the game's own supplies a block texture,
     model or blockstate in the `minecraft` namespace (`VisibilityResources`). In 26.3 a quad's draw layer follows its
     texture's transparency, so such a pack could make an "opaque" block see-through. This is checked again on every
     resource reload.
   - **OFF:** any exception turns the module off for the session with one ERROR line. CI fails on that line, on
     deferral and on any `fallbacks`.
7. **Bench** (`visibility` block and log line), per frame:
   - sections visible;
   - entities and block entities vanilla would draw, how many Brimstone tested, and how many it hid;
   - ms per frame (average, max, frames over budget);
   - section layers missing (the audit);
   - the ray check's objects, rays, violations and unchecked objects, both in the window and since start-up;
   - tracked section changes.

### Pop-in check (CI)

- **Terrain:** the audit in 1 runs in every Brimstone bench frame, fly-through included. compare.py fails on any
  missing section layer, and if the audit didn't run.
- **Entities and block entities, every frame:** every Brimstone client run sets
  `-Dbrimstone.debug.verifyVisibility=true`. Each object Brimstone hides is then checked independently
  (`VisibilityCheck`):
  - Rays go from the exact camera position to a grid on the box Brimstone claimed hidden: points at most half a block
    apart, edges and corners included.
  - A ray that reaches its point without first entering an opaque cube through a drawn face is a violation.
  - The check is view-independent, like the test: it checks every point, on screen or not. So no camera transform
    (frustum, bobbing, damage tilt) can make it skip a point, and frustum-type bugs can't hide from it.
  - It shares only the opaque-cube rule with the test, none of its geometry.
  - compare.py fails on: any violation, in the measuring window or at any other time since start-up; any
    `Brimstone visibility check:` warning in the client log; any hidden object left unchecked (ray budget 100,000 per
    frame).
- **Moving and changing (review M3): scene `occlusion_move`.** The occlusion layout, with the camera sliding along the
  wall at 0.8 blocks per second for the 20 s bench. Objects are hidden and come into view through the window and the
  two-block doorway as the camera passes. Scripted commands during the bench (`measureSteps`, sent by the bench):
  - at 2, 4, 9, 11, 15 and 17 s, a pig is teleported between a hidden spot, the doorway, and the opening (the moving
    entity);
  - at 6 s, part of the wall in front of a hidden cow and chest is removed with `fill ... air`;
  - at 13 s, that part is put back.

  The per-frame ray check covers every frame of that pass (about 2,770 hidden objects and 1.5 million rays per
  profile, 0 violations). Fixed-point shots of the same layout (`ciShots`), taken the same way in every vanilla and
  Brimstone run, are compared with vanilla under both gates:

  | shot | what it shows |
  |---|---|
  | `occlusion_move` | the start of the path, x 2293.5 |
  | `x2297`, `x2305` | other points of the path |
  | `x2309` | the moved pig in the doorway |
  | `open` | after the wall opening |
  | `closed` | after it was closed again |
    | `bare_*` | the same views with every object removed, to measure each object's on-screen area |
- **What can't be measured:**
  - Vanilla runs don't bench, so there is no per-frame comparison with vanilla's own picture while moving. The
    per-frame proof is the ray check, and the screenshots compare fixed points.
  - The ray check reads the world, not the drawn mesh. So it cannot see a mesh older than the world, i.e. Brimstone
    trusting a wall that the world has but the drawn mesh doesn't have yet. The section-freshness rule guards against
    that. The wall-closing step exercises it, and the `closed` shot compares the end state, but the frames in between
    are not measured against vanilla.
  - Sections fading in are excluded by the test (fully faded in only), so they can't be measured.
- **Pictures:** both pixel gates in every scene and shot.
- **The fly-through itself has nothing to hide:** only the player and 1-2 block entities are in view, none hidden.
  There, the pop-in proof is the terrain audit (0 missing layers of about 430 visible sections, every frame, every
  profile). Hiding while moving is proved in `occlusion_move`.

### Review fixes (review of `9f21a13`)

- **B1 (end gateway beams hidden):** block entities are an allow-list now. End gateways, beacons and everything not
  listed are never tested.
- **M2 (ghast tentacles outside the margin):** entities are an allow-list with a margin of 2 blocks times scale, and
  extents were checked per type (`docs/visibility-allowlist.md`). Ghast, happy ghast, ghastling and squid are not on
  it. The `occlusion_move.ghast` shot was removed before the merge (second review, M-A): its vanilla noise from the happy
  ghast's tentacle animation came within 1 px of its limit, and it proved nothing about hiding (a happy ghast is never
  hidden, and with the 2-block margin its box's view of the wall reaches the window). Earlier text: it showed a happy ghast whose body the wall hides while its tentacles hang in the
  window (about 365 visible pixels). It is identical to vanilla under both gates.
- **M1 (block-entity frustum test against the cull frustum):** removed. The ray check is now view-independent, which
  is stronger than testing against the drawn view: it checks every point regardless of the camera transform.
- **M3 (no moving or changing evidence):** `occlusion_move`, as above.
- **Strict limit of the occlusion scene (m4 / item 4):**
  - The static `occlusion` scene is unchanged from `9f21a13` (all its samples were 0 in three runs). Its strict limit
    came down from 100 to 20 pixels. The smallest hideable object seen there, the iron golem in the doorway, covers
    about 200.
  - I first rebuilt that scene (bigger doorway, the happy ghast in it). In the collection run that showed vanilla
    noise of up to 139 strict pixels, from the ghast's animated tentacles, above the old limit of 100, which may not be
    raised. So the rebuilt layout lives only in `occlusion_move`, without the small sign, and the ghast only in its own
    last shot.
  - Package 2's shots use the finer rule written down in `docs/ci-limits.md` (largest sample × 1.5, rounded up to 10
    pixels, at least 10).
  - Every hideable object visible in a shot covers more than that shot's strict limit. Measured against the `bare_*`
    shots by projecting each object's box:
    - `x2297`: smallest 350 pixels, limit 10;
    - `x2305`: 94 pixels, limit 10 (the window seen at an angle);
    - `x2309`: 35 pixels, limit 30 (a chest seen at a grazing angle through the window; this is the tight case);
    - `open`: 486 pixels, limit 10;
    - `closed`: 627 pixels, limit 10.
- **m1 (dry run can go green):** compare.py fails any run whose client log or bench shows a dry run. Dry run is only
  set in marked noise-collection runs (`VISIBILITY_DRY_RUN=1` in the client job's env), which therefore always fail.
- **m3 (SectionChanges silently resets):** at the 65,536-entry cap, entries are dropped whose section has since got a
  newer mesh, or has left the view area. Vanilla resets a section's mesh when its slot moves (`setSectionNode` →
  `reset`), so any later mesh is newer. Only if less than a quarter is freed does everything count as changed until
  rebuilt, with a WARN line.
- **m8 (budget timer):** it now covers all the work per object (type and class checks, renderer lookup, test), plus a
  cap of 32 full rectangles per object.
- **m2 (ray-check limits):** warnings outside the measuring window are counted (`violationsSinceStart`) and gated, and
  sampling went from 16 points per face to at most half a block apart, edges included. The check is described as a
  sampled check, not a proof.
- **m5 / item 6 (x-ray resource packs):** the module defers, as in 6 above.
- **m6 (dump):** see "Deviations" for exactly what is dumped and published.
- **m7:** the `package-2` trigger in `build.yml` is the branch-only commit and is stripped at merge (left as is, as the
  coordinator asked).
- **CI infrastructure** (fixing these didn't count as attempts):
  - the compare job now also runs after a failed build, so `build/build.log` reaches ci-logs;
  - publishing tolerates missing client results.

### Evidence (run 40093135028)

Pixel differences (% of pixels whose largest channel differs by more than 8). The limits are unchanged except for the
new shots.

| suite / scene | noise (vanilla a/b) | limit | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| core / spawn | 0.68 | 10.05 | 0.25 | 0.48 | 0.36 |
| core / fly-through | 0.04 | 14.95 | 0.02 | 0.03 | 0.06 |
| core / water and glass | 0.95 | 6.45 | 0.70 | 1.63 | 1.16 |
| core / nether | 0.22 | 0.8 | 0.03 | 0.31 | 0.21 |
| core / mobs | 0.11 | 0.4 | 0.12 | 0.10 | 0.09 |
| core / occlusion | 0.00 | 0.25 | 0.00 | 0.00 | 0.00 |
| core / occlusion_move (10 shots) | 0.00 | 0.25 | 0.00 | 0.00 | 0.00 |
| core / occlusion_move.x2309 | 0.00 | 0.3 | 0.0065 | 0.0067 | 0.0065 |
| SIFTEC / The Sift | 0.05 | 1.15 | 0.16 | 0.16 | 0.17 |
| SIFTEC / HUB | 0.01 | 8.75 | 3.30 | 4.20 | 4.03 |
| SIFTEC / MAM | 0.05 | 9.3 | 3.53 | 4.43 | 4.29 |

Strict gate (pixels where all four vanilla runs agree exactly and Brimstone differs at all):

| scene | limit | full | old-GPU | Vulkan |
|---|---|---|---|---|
| core/spawn | 18000 | 179 | 668 | 1896 |
| core/fly-through | 5900 | 243 | 304 | 94 |
| core/water and glass | 25500 | 1041 | 1095 | 934 |
| core/nether | 900 | 60 | 146 | 146 |
| core/mobs | 600 | 50 | 42 | 61 |
| core/occlusion | 20 | 0 | 0 | 0 |
| core/occlusion_move, x2297, x2305, open, closed, bare_* | 10-110 | 0 | 0 | 0 |
| core/occlusion_move.x2309 | 30 | 0 | 1 | 0 |
| SIFTEC/The Sift | 4900 | 1071 | 703 | 947 |
| SIFTEC/HUB | 12500 | 0 | 28 | 833 |
| SIFTEC/MAM | 155100 | 0 | 28 | 833 |

Visibility, per measured frame (full profile; the other two profiles agree within rounding):

| scene | sections visible | entities vanilla draws (tested, hidden) | block entities (tested, hidden) | cull ms (max) | layers missing | ray check: objects / rays / violations |
|---|---|---|---|---|---|---|
| spawn | 344 | 1 (0, 0) | 11 (1, 0) | 0.03 (0.08) | 0 | 0 / 0 / 0 |
| fly-through | 432 | 1 (0, 0) | 1.9 (0, 0) | 0.00 | 0 | 0 / 0 / 0 |
| water and glass | 139 | 1 (0, 0) | 0 | 0.00 | 0 | 0 / 0 / 0 |
| nether | 300 | 1 (0, 0) | 1 (1, 0) | 0.02 (0.05) | 0 | 0 / 0 / 0 |
| mobs | 107 | 97 (96, 0) | 0 | 0.15 (0.30) | 0 | 0 / 0 / 0 |
| occlusion | 49 | 11 (10, 2) | 9 (9, 4) | 0.07 (0.10) | 0 | 3,594 / 1,626,884 / 0 |
| occlusion_move (moving, wall opened and closed) | 51 | 10 (9, 2.4) | 6 (6, 2.3) | 0.06 (0.26) | 0 | 2,768 / 1,498,090 / 0 |
| SIFTEC The Sift / HUB / MAM | 244 / 38 / 55 | 1-3 (0, 0) | 0-1 (0, 0) | 0.00 | 0 | 0 / 0 / 0 |

- The player counts as one entity vanilla draws in every scene; Brimstone never tests it.
- The mob crowd stands in the open, so nothing there is hidden.
- In SIFTEC nothing is hidden either: its machines are Create's (left to Create and Flywheel), its scenes hold no
  allow-listed objects behind walls, and its packs don't replace vanilla block resources (the module was `on`, not
  deferred).

Counts against baseline: every gated count is within its baseline in every profile and both suites.
- **Occlusion scene** (baseline = the dry-run run, Brimstone drawing vanilla's set):
  - draw calls 158.1 / 167.5 / 157.5 against 159.5 / 169.7 / 161.1 (full / old-GPU / Vulkan);
  - upload bytes per frame (median) 100 KB / 98 KB / 100 KB against 126 / 124 / 126 KB.
- **occlusion_move:** upload bytes 82 KB against the dry run's 105 KB (memory is the only gated count there).

### Runs

1. `ddae18c` run 40079570978: dry run, sampling. Failed only on the new scene's missing limits, as planned.
2. `c6adfc8` run 40081970862: failed on SIFTEC The Sift old-GPU, because vanilla's own old-GPU screenshot caught F3+A's
   chat line faded. CI now clears chat with F3+D.
3. `5d7dc04` run 40083026856: green (version reviewed as `9f21a13`).
4. Review fixes, `2523819` run 40087083634: compile error (the type constants live in `EntityTypes` /
   `BlockEntityTypes` in 26.3). It was found once the CI-infrastructure fix published the build log (runs 40087964679
   and 40090080697).
5. `f64b961` run 40090286336: marked collection run. Showed the rebuilt static scene's ghast noise; no other failure
   than the planned ones.
6. `7849b9b` run 40091787069: marked collection run for the final `occlusion_move` layout; only the planned failures.
7. `a09678d` run 40093135028: green.

### Deviations from the doc and the brief

- **No section culling of Brimstone's own.** Vanilla already has the doc's frustum plus cave graph, and anything
  stricter could only drop sections vanilla draws, which the brief forbids. Brimstone keeps vanilla's set and audits
  it.
- **The occlusion test is a slab proof, not a general occlusion query.** It is exact where it says "hidden", but it
  hides less than a depth-buffer test would (no combining of partial occluders, generous boxes, allow-listed types
  only).
- **No worker threads** (cost 0.00-0.16 ms per frame in CI). The 2 ms budget caps the render-thread cost.
- **CI:**
  - new scenes `occlusion` and `occlusion_move` (with `ciShots` fixed-point screenshots and `measureSteps` commands
    during the bench), with their own limits and baselines;
  - the ray check is on in every Brimstone run;
  - a dry-run switch that is only for marked collection runs;
  - chat is cleared after F3+A;
  - compare.py gates the visibility block, dry run, deferral and ray-check warnings;
  - the compare job publishes ci-logs even after a failed build.
- **What CI dumps and publishes (correcting the earlier "nothing is published"):**
  - No game files (shaders, textures, data) are extracted or published.
  - As before (the known exception, still to decide), the build job publishes on the public `ci-logs` branch
    `build/javap-all.txt.gz`: `javap -p` signatures of every game and Fabric API class. It also publishes
    `build/bytecode.txt.gz`: `javap -c` bytecode of the game classes matched by `.github/dump-bytecode`.
  - Package 2 added 8 patterns to that list:
    - `client.renderer.(SectionOcclusionGraph.*|Octree.*|ViewArea)`;
    - `client.SectionUpdateTracker.*`;
    - `client.renderer.chunk.(VisGraph|VisibilitySet)`;
    - `client.renderer.entity.(EntityRenderer|EntityRenderDispatcher|LivingEntityRenderer|MobRenderer)`;
    - `client.renderer.blockentity.(BlockEntityRenderDispatcher|BlockEntityRenderer)`;
    - `client.renderer.culling.Frustum`;
    - `client.Camera`;
    - `client.multiplayer.ClientChunkCache.*`.
  - The full list is in `build/bytecode-classes.txt`.

### Choices Andre should confirm

- **The allow-lists and margins** (`docs/visibility-allowlist.md`). The extents come from the vanilla model layouts,
  not from 26.3 model bytecode, which isn't in the dump.
- **Deferring the whole module** while any pack supplies `minecraft` block textures, models or blockstates. This is
  simple and safe, but a texture pack that changes only grass would also turn hiding off.
- **The finer strict-limit rule for package 2's shots** (× 1.5, rounded up to 10, at least 10) and the static scene's
  limit of 20. The `x2309` shot is tight: a 35-pixel sliver of a chest against a limit of 30.
- **The ray check is view-independent rather than "against the drawn view"** (stronger, simpler).
- **Clearing chat (F3+D) in CI** before world screenshots.
- **Baselines are left as they were.** The occlusion scene's upload bytes could come down (126 KB to about 100 KB);
  the old-GPU draw-call baselines from package 1 still could too.

### Remaining trade-offs and open issues

- **The SIFTEC hologram scene's draw-call baseline on full (119.16) came from one sample at 30 fps, and the scene's
  average follows the runner's frame rate** (run 6 above: 144.24 at 23.6 fps, over the 10% gate, with Brimstone's own
  draws unchanged). On a slow runner that gate can fail without any change in Brimstone. Raising the baseline or
  changing what is gated is Andre's call (rules); options: take the baseline from the slow mode (about 155, as on
  old-GPU), gate this scene's draw calls on the median (166-170 in every run), or report them only, like the
  fly-through's.
- The fly-through has nothing to hide, so hiding while moving is proved in `occlusion_move`, not in the fly-through.
- The frames between a wall being built and its new mesh arriving are guarded by the freshness rule, but not measured
  against vanilla (see "What can't be measured").
- The `occlusion_move` limits come from one collection run.
- The SIFTEC factory and map scenes are still skipped (the selftest jar is not in SIFTEC's release), so the
  Create-heavy factory isn't in CI.

### Second review (of `39ea855`) and merge

No findings that break the doc or the rules. M-A (the `ghast` shot could fail on vanilla noise alone and proved
nothing) was handled by the coordinator: the shot and its limits removed (none raised). Left for Andre (MINOR):
- the resource-pack check looks at `textures/block`, `models/block` and `blockstates` only; packs using
  `atlases/blocks.json` sprite remaps, connected-texture mods (Continuity, `optifine/` properties) or mods changing
  block models in code are not covered; a stricter check would test each occluder's baked model and sprite
  transparency;
- the `x2309` gate only just covers its smallest object (a 20-35 px chest sliver against 30 px); the main
  `occlusion_move` shot has no object-free twin, so its smallest object wasn't measured;
- the static `occlusion` scene's strict limit is 20 px where package 2's finer rule gives 10 (kept at 20, the value
  the green runs were judged against; 20 is still a tenth of the smallest visible object there, the 200 px golem);
- the bench setups for `occlusion` and `occlusion_move` clear blocks and kill entities in a fixed area: run them in a
  copy of the world.

### Laptop bench (Andre, when back)

Same instances and steps as `docs/laptop-bench.md`, with this branch's jar (after the merge, the `latest` release).
Plain instance:

```
/brimstone bench occlusion confirm
/brimstone bench occlusion_move confirm
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
/brimstone bench occlusion_move confirm
/brimstone bench mobs confirm
/brimstone bench flythrough confirm
```

Paste the latest.log blocks (or the JSON files). The `visibility` line should say:
- `on`, `fallbacks 0`, `section layers missing 0`;
- in `occlusion`, about 2 entities and 4 block entities hidden per frame;
- in `occlusion_move`, about 2.4 and 2.3.

Note its `ms/frame` and `max` (the culling cost on the laptop's CPU) next to FPS and frame times. If it says
`deferred`, the log line names the pack that replaces vanilla block resources.

---

## Package 3: translucency

**Branch** `package-3` (not merged). **CI:** green run https://github.com/meakaandre-tech/Brimstone/actions/runs/40126212181 on `6d48fc7` (every profile, both suites, compare
passed), the last code change. A docs-only rerun (run 40128444322) failed one runner-speed-dependent count; see
"Runs" and "Open issues". **ci-logs** (overwritten by later runs): `compare/report.md` has every table below plus a "Translucency" table
per suite and profile; `runs/<suite>-brimstone-*/bench/*.json` have the `translucency` block;
`runs/core-*/shots/translucent_move*.png` and `water_glass_oit.png` are the new fixed-point shots,
`runs/siftec-*/shots/hologram.png` the Build Gun hologram.

**Done when (doc):** "Water, glass, ice and Build Gun holograms sorted correctly when the camera moves; sorting off the
main thread."

### What vanilla 26.3 already does (research, from the bytecode dump; details in `docs/terrain-research.md` 6a)

- Every translucent layer is sorted back to front when it is built (camera position at the start of the build task)
  and gets its own index list. The sort: quad centre = midpoint of the first and third corner, key = squared distance
  to the camera, stable merge sort, far first (ties keep build order).
- When the camera crosses a block, vanilla schedules `ResortTransparencyTask`s on its worker threads (nearby visible
  sections always, the rest round-robin), only for sections whose point of view changed. A task builds a new index
  list and hands only that list to `addSectionBuffersToUberBuffer(TRANSLUCENT, mesh, null, indices)`; vanilla uploads
  it at the end of the frame.
- Translucent sections are drawn in exactly the reverse of the visible-section list when vanilla sorts; with
  Improved Transparency on, vanilla doesn't sort (no resort tasks, `sortTranslucent` false) and draws in list order,
  in three passes (depth bounds, transmittance, accumulate) with the `OIT_TERRAIN` pipelines.
- On the plain path (Intel integrated) vanilla makes one draw per translucent section per pass.

### What was built (`terrain/`, `mixin/terrain/`, `assets/brimstone/shaders/core/terrain.vsh`)

1. **Translucent layers in Brimstone's buffers** (`Terrain.takeTranslucent`, `TerrainStore`). Where a freshly built
   section's translucent layer would go into vanilla's buffers, Brimstone converts its vertices as for solid and
   cutout (one exact format per layer, compact 16 bytes or wide 28; a layer with any vertex alpha below 255 goes wide)
   and keeps **vanilla's own sorted index list as it is**, in the same arena as the vertices. So the quad order inside
   a section is vanilla's, ties included, by construction.
2. **Sorting: vanilla's own, on vanilla's worker threads, when vanilla decides.** Each new index list a
   `ResortTransparencyTask` makes for a layer Brimstone holds is taken on that worker thread (copied), and at the end
   of the frame, where vanilla uploads its own, Brimstone writes **only that list**, in place over the old one
   (`TerrainStore.rewriteIndices`). Nothing is re-sorted on the render thread and no thread is added. A late list for
   a mesh vanilla has let go of is dropped (vanilla would draw nothing of it either). With Improved Transparency on
   vanilla schedules no resorts, so none happen.
3. **Draw order: vanilla's.** `TerrainRenderer.buildTranslucentRuns` takes the translucent layers of vanilla's visible
   sections in list order, reversed on frames where vanilla sorts (from vanilla's own `sortTranslucent` argument), and
   only merges *consecutive* sections that share format, arena, index type and (plain path) region into one draw call,
   so batching never reorders anything.
   - Plain path (no multi-draw indirect, Intel integrated): one `multiDrawIndexed` per run with per-section index
     offsets (`glMultiDrawElementsBaseVertex`); the interleaved form on Vulkan devices that have it; one draw per
     section without either.
   - Indirect path: one indirect multi-draw per run from the frame's one Brimstone command list (as package 1).
4. **Pipelines made from the game's** (`TerrainPipelines.derive`): for classic translucent and for each of the three
   Improved Transparency stages, Brimstone's pipeline is the game's multi-draw terrain pipeline for that draw with
   only the vertex shader (Brimstone's), its vertex layout and, on the plain path, the region uniform block swapped:
   the game's fragment shader, defines, blending, colour targets, depth state, culling and uniform blocks are taken
   from it unchanged. The vertex shader skips the lightmap in the stages that only need coverage (`OIT_ALPHA_ONLY`),
   as the game's does. Checked locally on CI's Mesa (`scratchpad/p3/harness3.py`, not in the repo): Brimstone's
   vertex stage and vanilla's, fed the same 200,000 vertices, give bit-identical outputs with and without
   `OIT_ALPHA_ONLY` (both formats), and with any alpha in the wide format.
5. **Brimstone draws translucent in vanilla's pass(es)**, right after vanilla's own part, from the same hook as solid
   and cutout; the hook now receives vanilla's pipeline overrides and tells classic, each Improved Transparency stage
   and the wireframe view apart (`TerrainPipelines.translucentMode`). An override it does not know turns the module
   off (fallback, logged).
6. **No mixing with vanilla's own translucent draws.** A visible section whose translucent layer vanilla still holds
   (meshed before Brimstone turned on) is sent back to vanilla's mesher once, so its next mesh comes to Brimstone
   (`TerrainRenderer.translucentOthers`); frames where both drew translucent are counted ("mixed frames"). A
   translucent layer Brimstone cannot hold (over 8 MB, no index list) turns the module off: a layer must never be split
   between vanilla and Brimstone, or the order between sections would no longer be vanilla's.
7. **Crash safety:** every translucent path runs inside the module's existing fallback (`Terrain.fail`: one ERROR line,
   terrain handed back to vanilla); CI fails on any fallback.
8. **Bench** (`translucency` block and log line, `TranslucencyStats`): translucent section layers and Brimstone's
   translucent draws per frame, vanilla's own translucent layers and mixed frames, Improved Transparency stage draws,
   vanilla's sorts while measuring (count, ms average/p95/max, how many on the render thread; `SortStateMixin` times
   `MeshData.SortState.buildSortedIndexBuffer`), new index lists taken and uploaded, and the check below.

### The per-frame check (CI: `-Dbrimstone.debug.verifyTranslucency=true` in every Brimstone run; `TranslucencyCheck`)

Every frame, against what vanilla would draw for the same camera:
- **section order:** the sections in Brimstone's translucent draw calls, read back from those calls, equal vanilla's
  order worked out separately from vanilla's visible list (reversed when vanilla sorts);
- **latest sort:** each section is drawn with the index list from vanilla's latest sort of that mesh (the camera
  position vanilla sorted for, captured where vanilla builds the sorting, `RenderSection.createVertexSorting`, must
  give the mesh's own point of view), unless a newer list is already on its way in, as it would be for vanilla;
- **back to front:** each index list Brimstone wrote names every quad once and orders them by non-increasing
  distance from that camera position (quad centres from the layer's vertices, computed separately; float tolerance;
  ties not checked, as their order is vanilla's own bytes).

compare.py fails on any miss (in or outside the measuring window), on any measured mixed frame, on a dry run, on a
translucent scene where Brimstone drew no translucent sections, and on an Improved Transparency scene not drawn with
it in every measured frame (and on others drawn with it).

### CI

- **`translucent_move`** (core): water in a glass tank, a wall of stained glass in stripes of six colours (faces
  between different colours are drawn, so many translucent quads overlap), ice inside the water and a single ice
  block in front of the stripes, white glass panes; the camera slides past at 1.5 blocks/s for the 20 s bench, so
  vanilla re-sorts as it crosses blocks (180 sorts per bench). Six fixed-point shots (`ciShots`), taken after the
  camera moved there: `x2404`, `x2414`, `x2424`, `low` (close, oblique), `back` (the start again, after the others),
  `bare_back` (the single ice block removed: it covers 8,148-8,157 pixels, far above the strict limit of 10).
- **`water_glass`** (existing) and **`water_glass_oit`**, **`translucent_move_oit`** (Improved Transparency on).
  **Choice: scenes, not a profile.** Improved Transparency is a client option, so the OIT scenes run in their own
  client session with `improvedTransparency:true` in options.txt (`clientOptions` in the scene file; `client-run.sh`
  groups sessions by render distance and client options; the bench sets and restores it on a laptop). A profile
  would have doubled every scene in every GPU profile; the scenes cover the translucent cases in all three GPU
  profiles at the cost of one more client session.
- **`hologram`** (SIFTEC): see below.
- Limits from the marked collection runs by the documented rules (`docs/ci-limits.md`): core scenes from run
  40115084815, the hologram from run 40123696828 (SIFTEC suite only in dry run). The pin mod holds the lightmap flicker and
  texture animation; the scenes clear their area by teleporting entities to y=-200.

### Build Gun holograms

SIFTEC draws them with Create's outliner (`BuildGunClient` and `PrefabPreview` call `Outliner.showCluster(...)` with a
checkered face texture), not as terrain, so they stay on their own path. The `hologram` scene checks that they still
look exactly the same over Brimstone's translucent water: the player holds the Build Gun, CI presses its next-tab key
(Tab, a new per-scene `ciKeys` list in `drive.py`, the same in every run), which picks the first Power building (the
Power Pole Mk.1), and its green outline stands in front of a water pool, high above the terrain.
- Not run on Vulkan: Mesa's lavapipe segfaults (`libvulkan_lvp.so`) as soon as the outliner draws, in vanilla's
  Vulkan run as much as Brimstone's (runs 40115084815 and 40119107821). The scene lists the two Vulkan runs in
  `ciSkipRuns`; compare.py leaves the Vulkan profile out for this scene only (strict gate on the other three vanilla
  runs).
- Rebuilt on the way: the first version held the Wormhole Gateway, whose outline (a 48x54 structure) was the first
  crash; the second and third (a glass tank with stained glass, then a plain pool) had vanilla a/b noise of 3.6% and
  3.3%, because ice formed at random on the water (water freezes at that altitude where block light is low; I first
  took the second version's noise for vanilla's own translucent order). The pool is now lit from below with sea
  lanterns (light 14 at the surface), and its noise is 0.003-0.03%.

### Evidence (run 40126212181)

Pixel differences under both gates (8/255: % of pixels; strict: pixels where every vanilla run agrees exactly and the
Brimstone run differs at all). Limits from `docs/ci-limits.md`; none changed for existing scenes.

| suite / shot | noise (vanilla a/b) % | limit % | full % | old-GPU % | Vulkan % | strict limit | full | old-GPU | Vulkan |
|---|---|---|---|---|---|---|---|---|---|
| core / water_glass | 0.0026 | 6.45 | 0.0029 | 0.0042 | 0.0053 | 25500 | 17 | 24 | 23 |
| core / water_glass_oit | 0.0044 | 0.3 | 0.0048 | 0.0037 | 0.0049 | 40 | 18 | 17 | 25 |
| core / translucent_move | 0.0001 | 0.3 | 0.0 | 0.0 | 0.0001 | 10 | 1 | 0 | 1 |
| core / translucent_move.x2404 | 0.0001 | 0.3 | 0.0001 | 0.0002 | 0.0001 | 10 | 1 | 1 | 2 |
| core / translucent_move.x2414 | 0.0003 | 0.3 | 0.0002 | 0.0002 | 0.0 | 10 | 1 | 2 | 1 |
| core / translucent_move.x2424 | 0.0 | 0.3 | 0.0 | 0.0004 | 0.0 | 10 | 1 | 3 | 1 |
| core / translucent_move.low | 0.0002 | 0.3 | 0.0008 | 0.0003 | 0.0004 | 20 | 7 | 4 | 3 |
| core / translucent_move.back | 0.0 | 0.3 | 0.0001 | 0.0002 | 0.0002 | 10 | 1 | 1 | 2 |
| core / translucent_move.bare_back | 0.0003 | 0.3 | 0.0004 | 0.0001 | 0.0 | 10 | 1 | 1 | 0 |
| core / translucent_move_oit | 0.0 | 0.3 | 0.0001 | 0.0004 | 0.0002 | 10 | 1 | 2 | 2 |
| core / translucent_move_oit.x2404 | 0.0001 | 0.3 | 0.0 | 0.0001 | 0.0001 | 10 | 0 | 2 | 2 |
| core / translucent_move_oit.x2414 | 0.0003 | 0.3 | 0.0001 | 0.0003 | 0.0002 | 10 | 1 | 0 | 4 |
| core / translucent_move_oit.x2424 | 0.0003 | 0.3 | 0.0003 | 0.0001 | 0.0004 | 10 | 1 | 1 | 2 |
| core / translucent_move_oit.low | 0.0005 | 0.3 | 0.0004 | 0.0005 | 0.0005 | 20 | 5 | 5 | 6 |
| core / translucent_move_oit.back | 0.0002 | 0.3 | 0.0002 | 0.0003 | 0.0001 | 10 | 1 | 1 | 0 |
| core / translucent_move_oit.bare_back | 0.0001 | 0.3 | 0.0001 | 0.0001 | 0.0001 | 10 | 0 | 1 | 0 |
| SIFTEC / hologram | 0.0282 | 0.55 | 0.2626 | 0.0257 | not run (lavapipe crash, vanilla too) | 21840 | 14333 | 9 | not run |
| SIFTEC / The Sift | 0.2298 | 1.15 | 0.1968 | 0.1886 | 0.1875 | 4900 | 863 | 486 | 763 |
| SIFTEC / HUB | 0.1879 | 8.75 | 1.7259 | 4.3373 | 4.4633 | 12500 | 16 | 405 | 796 |
| SIFTEC / MAM | 0.1092 | 9.3 | 1.6369 | 4.5525 | 4.5964 | 155100 | 16 | 405 | 296 |

The other scenes (spawn, fly-through, nether, mobs, occlusion, occlusion_move with its shots) passed both gates
as before; their numbers are in `compare/report.md`. The hologram's full-profile 14,333 strict pixels are the
outline's bright state (see "Deviations").

**Translucent draws per frame** (Brimstone's translucent section layers -> its draw calls for them; vanilla's own
path makes one draw per section layer per pass on old-GPU, and about one per pass on full and Vulkan):

| scene | old-GPU | full | Vulkan |
|---|---|---|---|
| spawn | 150 -> 14 | 150 -> 1 | (as full) |
| fly-through (not gated) | 186 -> 42 | 192 -> 31 | |
| water_glass | 4 -> 1 | 4 -> 1 | 4 -> 1 |
| translucent_move | 7 -> 5 | 7 -> 4 | 7 -> 4 |
| water_glass_oit (three stage passes) | 12 -> 3 | 12 -> 3 | 12 -> 3 |
| translucent_move_oit (three stage passes) | 21 -> 15 | 21 -> 12 | 21 -> 12 |
| SIFTEC The Sift | 6 -> 2 | 6 -> 1 | 6 -> 1 |
| SIFTEC hologram | 4 -> 1 | 4 -> 1 | not run |

Total draw calls per frame against baseline (old-GPU): water_glass 140.0 (457.8), water_glass_oit 145.0 (154.0),
translucent_move 142.0 (141.14, memory gated only), translucent_move_oit 155.0 (161.0), spawn and the rest well under.
Every gated count (draw calls, uploads, upload bytes, heap after GC, direct buffers) is within its baseline in every
profile and both suites. Heap after GC moved by at most about +1 MB (Vulkan spawn 258.22 against 257.2); direct buffers
by one or two 2 MB steps in a few scenes (hologram full 8.24 against 4.24, within the 4 MB step allowance); upload bytes
per frame are at or under the baselines in the gated scenes except water_glass_oit on full (15,744 against 14,688,
within the gate).

**Sorting** (vanilla's own `buildSortedIndexBuffer`, timed while measuring; 0 on the render thread in every scene and
profile): translucent_move 180 sorts per 20 s bench, average 0.10 / 0.15 / 0.14 ms (full / old-GPU / Vulkan), p95
0.13-0.23 ms, max 2.4-4.0 ms, 180 new index lists uploaded; fly-through about 860-900 sorts, average 0.03 ms;
occlusion_move 36 sorts, 32 lists uploaded. With Improved Transparency no sorts and no uploads, as in vanilla.

**Per-frame check:** 0 misses in every scene, profile and suite, in the measuring windows and since start-up
(translucent_move: 2,114-2,744 section layers checked per bench; spawn 23,848 on old-GPU), 0 mixed frames, 0 unchecked
entries in the translucent scenes.

**Memory:** translucent layers now live in Brimstone's arenas (wide for water, compact where exact: translucent_move's
live terrain 23.4-23.6 bytes per vertex against vanilla's 28); arenas are now vertex and index buffers.

### Runs

1. `168102e` run 40115084815: marked collection run (all Brimstone runs in translucency dry run). Only the planned
   failures, plus the SIFTEC Vulkan hologram crash (Wormhole Gateway outline, vanilla too).
2. `8251625` run 40119107821: core real (passed every gate), SIFTEC collection for the rebuilt hologram; Vulkan crash
   again (lavapipe, vanilla too) and hologram noise (ice forming on the water).
3. `32a4224` run 40121527770: core real (passed again), SIFTEC collection for the water-pool hologram with the Vulkan
   skip; hologram noise from ice forming.
4. `4cb5419` run 40123696828: core real (passed again), SIFTEC collection for the lit pool: clean, apart from the
   outline's brightness flip (noise).
5. `6d48fc7` run 40126212181: everything real, every profile and both suites: **green**.
6. `0db931b` run 40128444322 (the docs-only write-up, same code as run 5): **failed one count**: SIFTEC hologram draw
   calls on full, 144.24 against the baseline of 119.16 (a single dry-run sample, run 4). The runner ran that scene at
   23.6 fps instead of 30, and the scene's per-frame average follows the frame rate: frames that line up with a game
   tick draw about 50 more there (average about 119 at 30 fps, 144-155 at 22-24 fps; median 166-170 in every run,
   old-GPU likewise: 155.07 at 22 fps in run 4, 124.18 at 28.6 fps in run 5). Brimstone's own draws were identical in
   every run (terrain 2, translucent 1 per frame on full). The baseline was not raised and the gate not changed (see
   "Open issues").
7. This commit (docs only): its own run, see the hand-off.

### Deviations from the doc and the plan

- **Sorting is vanilla's own, not a new sorter.** The doc asks for translucent sections sorted back to front on a
  worker thread; vanilla 26.3 already does that on its worker threads, and Brimstone's picture must equal vanilla's
  including the order of ties. Brimstone therefore keeps vanilla's index lists byte for byte and takes every new one
  vanilla's resort task makes; it re-uploads only those lists. No new threads (the CPU is shared with the integrated
  server).
- **Fewer draw calls on the plain path, not always on the indirect path.** On old-GPU (vanilla's per-section path)
  translucent goes from one draw per section per pass to one per run: water and glass 4 to 1, translucent_move 7 to 5,
  with Improved Transparency 12 to 3 and 21 to 15 per frame. On full and Vulkan vanilla already draws all translucent
  with one indirect draw per pass; Brimstone needs one per run (format and arena changes split runs): 4 for
  translucent_move (12 with Improved Transparency). Totals stay within the baselines there.
- **Improved Transparency order matches vanilla when vanilla's translucent geometry fits in one of its 128 MiB heaps**
  (then vanilla draws in list order); with more, vanilla groups by heap, which Brimstone does not imitate.
- **The hologram scene's strict gate could not resolve the outline** until 2026-10-25 (limit 21,840 above the
  outline's size); fixed by the feature batch order pin, limit now 360 (see "Finishing package 3").
- **The hologram is not checked on Vulkan** (lavapipe segfaults drawing it, vanilla too).
- **CI:** new scenes and limits; per-session client options; per-scene `ciKeys` and `ciSkipRuns`; the translucency
  dry-run switch (marked collection runs only); compare.py's translucency gates and table.

### Choices Andre should confirm

- Keeping vanilla's sort (byte-identical order) over a Brimstone sorter.
- OIT as scenes (own client session) rather than a profile.
- The hologram scene: Build Gun with the first Power building picked by a CI key press, not run on Vulkan
  (lavapipe crash in vanilla too).
- Translucent index lists in the same arena as their vertices (arenas are now vertex + index buffers).
- A translucent layer Brimstone cannot hold turns the whole terrain module off (rather than leaving that layer to
  vanilla, which would break the order between sections).
- Translucent layers keep the per-layer compact/wide choice. Putting every translucent layer in the wide format
  would end runs only at arena (and plain-path region) changes, e.g. one indirect draw per pass on full and Vulkan
  instead of 4 (translucent_move) or 31 (fly-through), at 12 more bytes per vertex of the translucent layers that are
  now compact.
- The hologram scene's strict limit (above), and accepting that the hologram is not checked on Vulkan.
- Baselines left as they were; the old-GPU draw-call baselines (e.g. water_glass 457.8, measured 117-140) could
  come down.

### Remaining trade-offs and open issues

- The SIFTEC hologram scene's draw-call average follows the runner's frame rate; since 2026-10-25 that scene is
  gated on the median (Andre's decision; see "Finishing package 3").
- Right after the module turns on (and while terrain is handed back), sections vanilla still holds are drawn by
  vanilla before Brimstone's, so for those few frames the order between the two sets may not be vanilla's (counted as
  mixed frames; CI requires none in measured frames).
- The per-frame check reads Brimstone's own copy of each index list (what it wrote), not the GPU buffer.
- The hologram is not checked on Vulkan; Build Gun buildings other than the Power Pole Mk.1 are not in CI.
- SIFTEC factory and map run again since 2026-10-25 (map without a strict gate; see "Finishing package 3").

### Independent review and stop

Reviewed by a separate agent that had not seen the work (full text: `docs/review-package-3.md`). Verdict: the code
meets the done-when (sort order matches vanilla byte for byte, sorting stays on vanilla's workers, 0 per-frame check
misses, translucent shots within 7 strict pixels against limits of 10/20, no limit/baseline/check changed, trailers
on every commit, no raw GL/Vulkan, every new path falls back with a log line). Findings, not fixed because package 3
stopped on a decision only Andre can make:
- **B1 (blocker until accepted):** translucent draw calls go *up* against vanilla on full and Vulkan (batches break
  where the vertex format changes): `translucent_move` 129 vs 122.86, its Improved Transparency version 140 vs 131,
  fly-through on full 157 vs 123. Ungated, so CI doesn't see it. The doc says draw calls go down. Options: accept
  as a deviation; store translucent layers in the wide format (+12 bytes per vertex where compact today); or gate
  translucent draw calls (which then need one of the other two).
- **M1:** the hologram scene can't catch an error only where the outline overlaps water (about 3,900 px of a
  14,360 px outline; strict limit 21,840 is larger than the outline). Fix: move most of the outline over water, pin
  the outliner's two-state brightness in the CI pin mod, re-collect and set limits by the documented rule.
- **M2 (the stop):** the hologram draw-call average follows the runner's frame rate
  (avg ≈ 27.6 + 137.2 × min(1, 20/fps) across six runs); Brimstone's own draws are constant.
- Minor: move scenes' draw calls ungated (gating them would make B1 visible); the Improved Transparency check counts
  stage draws, not frames; `ciSkipRuns` accepts any run names; what `TranslucencyCheck` can't see. Note: the
  hologram's direct-buffer gate is one 2 MB step from failing (same single-sample problem).

**Decision for Andre (hologram gate).** Within the rules as written: (4) cap that scene at 20 fps through per-scene
client options and re-collect, so every frame carries a tick; (5) record draw calls separately for frames with and
without a tick, for every scene; (6) additionally gate Brimstone's own draw calls, which don't depend on frame rate.
Only Andre can choose: (1) baseline from more clean runs (144.24, still fails at 20 fps or below); (2) gate this
scene on the median (166 in all six runs); (3) report only. Option 4 changes the scene and re-collects its baseline
from scratch; I didn't take it unattended because B1 also needs Andre's call before merging (doc vs code on draw
calls, which affects later packages).

### Finishing package 3 (2026-10-25, after Andre's decisions)

Andre's decisions on the stop (2026-10-25): (1) gate the hologram scene's draw calls on the median, baseline = the
highest clean median from the runs on `4cb5419`, `6d48fc7` and later; (2) find why Create's outliner flips between
two brightness states, pin it in `ci/pin-mod` and re-collect the hologram's strict limit (must come out below the
outline's own size); (3) game class dumps to a 1-day Actions artifact, never `ci-logs`; (4) check the hologram's
direct buffers for a leak over a long run; (5) independent review, fix, merge. The design doc now records (1) and the
CI pins (Testing) and (3) (Rules, Mojang code).

**1. Hologram draw calls on the median.** `"ciDrawCallGate": "median"` on the scene; `compare.py` gates and records
that scene's per-frame median instead of its average (the report shows both). Every other gate is unchanged.

| run | commit | full: median (avg) | old-GPU: median (avg) | used |
|---|---|---|---|---|
| 40123696828 | `4cb5419` | 166 (119.16) | 170 (155.07) | no: a dry run (Brimstone drew vanilla's set and vanilla drew the translucent terrain, so the count isn't Brimstone's) |
| 40126212181 | `6d48fc7` | 166 (118.92) | 167 (124.18) | yes |
| 40128444322 | `0db931b` | 166 (144.24) | 167 (148.72) | yes (failed only on the average) |
| 40130610610 | `13f56fb` | 166 (142.45) | 167 (146.9) | yes (failed only on the average) |

Baselines: **full 166, old-GPU 167** (not run on Vulkan). Later runs: 166/167 up to run 40139975085; 173/174 in every run since SIFTEC's 2026-10-25 release (runs 40156711337 attempts 1-3 and 40164365265; averages 127-165), within the gate. The baseline was not raised; it went down by one draw (to 165 / 166) because the crosshair outline pin removes exactly one draw per frame in this scene (review finding m4).

**2. The outliner's two brightness states.** Two probe runs (branch-only diagnostics, since removed):
- run 40135570271 logged the outliner's entries and params (colour `ff5cd65c`, light `f000f0`, alpha 1, line width,
  removal countdown), the edge draws' pose and normal matrices, and the lightmap's inputs. All identical in the bright
  and the dim runs. The difference map showed that only the hologram's **far** edges change: the ones seen through its
  translucent checkered faces.
- run 40138168127 logged each frame's feature draws. Both of the outliner's render types are drawn in the game's
  outline feature phase, and their order differed by run: `ponder:outline_solid` (edges) then
  `ponder:outline_translucent_cull` (faces) in the dim runs, faces then edges in the bright ones. The phase groups
  submits into batches by render type in a map whose iteration order follows identity hash codes, so the order is
  fixed for a game start and differs between starts. Edges first: the faces are blended over the far edges (3.6%
  darker). Faces first: the edges overwrite the faces there.

The pin (`ci/pin-mod`, `FeatureBatchOrderMixin`): that batch map keeps insertion order (a `LinkedHashMap`), so batches
are drawn in the order they were first submitted, in every client run, vanilla and Brimstone alike. Run 40139105024
(SIFTEC noise collection, dry run): every run drew faces then edges; the vanilla runs agree on 100% of the hologram
frame. The pin holds the order of vanilla's own feature batches in every scene; Brimstone draws no features yet
(package 6 batches entities and block entities and must keep the vanilla order its own batching replaces).

**New strict limit.** The first re-collection (run 40139105024: vanilla b/a 7/4, Brimstone dry run 15/18, limit
30) had too few samples: the next full run (40139975085) failed old-GPU at 232 pixels. Two things were behind it:

- *The crosshair's block outline* moved by one pixel row from one frame to the next in the old-GPU profile (GL 3.3),
  in vanilla's own runs too. Probes: run 40143410574 (camera and the hologram's pose identical in every frame),
  run 40146057417 (three more shots 8 s apart: Brimstone's old-GPU run flipped within the run), run 40152673881
  (ten quick shots and every frame's draws and crosshair target logged: vanilla's old-GPU run flipped in 2 of 11
  shots, Brimstone's in 5 of 11, with the same draws and the same target block in every frame). Andre's decision:
  a CI-only pin (`BlockOutlineMixin`) leaves that outline out of every client run, vanilla and Brimstone alike;
  with it all eleven shots agreed in every profile (run 40154022133). **The block outline is therefore untested in
  CI: package 6 or 7 must add a scene that checks it with the outline drawn.**
- *Frame-to-frame noise in vanilla's own water* (15-27 pixels between shots of the same run) and *SIFTEC's minimap*
  (differs between runs). With Andre's OK the limit was re-collected over eleven shots per run (run 40155387068, dry
  run): vanilla b/a 221-234 and 9-22, Brimstone dry run up to 63 (full), 24 (old-GPU): 234 x 1.5 = 351, rounded up
  to 10: **360** (was 21,840; the outline covers about 14,500 pixels, about 3,900 of them over Brimstone's water).

The review's M1 (an error only where the outline overlaps the water would pass both gates) is answered by this:
such an error now has to stay under 360 pixels. The scene was not recomposed.

**SIFTEC's selftest jar is back in its release (2026-10-25)**, so the factory and map scenes run again. They had no
strict limits and no count baselines:
- factory: strict limit 100 by the rule (samples 0/0, dry run 2; run 40155387068); count baselines from run
  40156711337 attempts 1-3.
- map: the first strict limit (13,100, one run) was under-sampled (vanilla b/a up to 22,532 in later runs: SIFTEC's
  map shows whichever tiles it has scanned when it opens). Andre's time box: two client-side attempts to make it
  steady (wait until two captures 5 s apart are identical, run 40161774743; stand 60 s in the area first, run
  40162905190); vanilla runs stayed 35-58k pixels apart, so per his fallback the map has **no strict gate**
  (`"ciNoStrict"`, reported by compare.py), its 8/255 limit stays, and **package 7 (GUI) must give it real strict
  coverage**. Count baselines from attempts 1 and 3.
- `sift` upload bytes: vanilla's own counts, measured in the same runs by branch-only jobs with every Brimstone
  module off, jump in the same ~12.9 KB steps (43,120 / 68,936 / 47,504 / 55,980, then 43,120 / 55,972 / 34,316 /
  55,764, then 55,980 / 56,304 / 60,252 / 43,128 for vanilla a / b / old-GPU / Vulkan), so per Andre's rule it was
  re-baselined from the three runs: full 68,940, old-GPU 43,376, Vulkan 56,084.
- hologram direct buffers re-baselined to 8.24 MB (full) and 6.31 MB (old-GPU, from the runs clean for it), Andre's OK: map now runs right before it in
  the same session and the JVM's direct pool keeps the size it reaches there (see 4).

**3. Dumps.** The build job writes `javap-all.txt.gz`, `bytecode.txt.gz` and the class lists to the `game-dumps`
artifact (`retention-days: 1`); `ci-logs` carries only Brimstone's `build.log` (plus the client runs and the compare
report). `ci-logs` was republished without `build/` on 2026-10-25. `docs/backend.md` says where dumps are now.
Consequence: the agent workspace cannot download Actions artifacts (their storage host isn't reachable), so a future
chat that needs a dump must ask Andre to download `game-dumps` and attach it.

SIFTEC (not touched, read only): its CI can produce the same javap dumps (`.github/dump-signatures`,
`.github/dump-bytecode`, `.github/dump-bytecode-old`), published to its `ci-logs`; neither trigger file is present
now, so its current `ci-logs` has no class dumps. But its `.github/dump-data` is present, so its current `ci-logs`
carries `data.tar.gz`: game data files extracted from the Minecraft jar (worldgen and structure JSON matching
`*sulfur*`, `*spring*`, `structure/spring/*`, `tags/worldgen/*`). Its `ci-logs` also lists other mods' files,
lang and recipes (`pack/*`). Older force-pushed `ci-logs` commits may still hold earlier dumps.

**4. Direct buffers on the hologram scene.** A branch-only job (run 40135570271) benched the hologram scene for 900 s
in the full and old-GPU profiles with LWJGL's DebugAllocator and a memory line every 15 s:

| profile | java.nio direct MB (count) at 90 s / 450 s / 990 s | live LWJGL allocations from Brimstone's code | all live LWJGL allocations | heap after GC (bench end) |
|---|---|---|---|---|
| full | 4.24 (34) / 4.06 (31) / 4.06 (31) | 36,864 bytes in 5 allocations, unchanged from 90 s to the end | 145.38 MB, 30,789, unchanged | 468.26 MB |
| old-GPU | 3.73 (33) / 3.54 (30) / 3.54 (30) | 37,904 bytes in 6 allocations, unchanged | 144.98 MB, 30,784, unchanged | 467.88 MB |

No leak: nothing grows over 15 minutes. Brimstone allocates no java.nio direct buffers at all (its vertices and
translucent index lists are LWJGL `memAlloc`, invisible to the JVM's direct-buffer count; the arena copies are GPU
memory), and its live native memory at rest is its fixed draw scratch buffers. Translucent index lists:
`TerrainStore.stageIndices` copies each new list, `upload()` writes it over the old one in the arena
(`rewriteIndices`) and frees the copy, also when the write throws (the `finally`), and `release`/`dropStaged` free
queued copies. The regular runs' 5.48 to 8.24 MB on this scene: the SIFTEC scenes run in one client session (sift,
hub, mam, hologram), and the JVM's direct pool (vanilla's and its libraries', e.g. network buffers) grows in 2 MB
steps at some point in it and keeps that size; the step lands in mam or in hologram depending on the run (run
40130610610 full: 3.48, 3.48, 5.48, 5.48; old-GPU 4.24, 4.24, 4.24, 6.24; run 40135570271 full: 3.73 in all four).

**For package 5's plan (Andre, 2026-10-25):** make the SIFTEC factory's Create animation repeatable in CI (vanilla and
Brimstone alike; a striped part of one machine is in another phase in about one run in four, 315 pixels), then
re-collect the factory's strict limit (500 now) and lower it.

**5. Review.** Round 2 (fresh agent, `docs/review-package-3b.md`; of commit `a9444ac`, run 40164365265): done-when
holds (0 check misses, 0 render-thread sorts, translucent shots 0-7 px against 10-40, hologram 19/18 against 360);
Andre's decisions carried out; nothing else relaxed; diagnostics fully reverted; trailers on every commit. Fixed:
M1 (old-GPU hologram direct baseline from runs clean for it: 6.31, not 8.24), m3 (`ciNoStrict` only for scenes on an
allow-list in compare.py), m4 (hologram medians down by the one draw the crosshair pin removes: 165/166), m5 (stale
doc text). Left for Andre: B1 (translucent draw calls above vanilla on full and Vulkan) and M2 (sift upload bytes
can still step over the new gate); see the top of this file. Not changed: m1 (the move scenes' draw calls stay
memory-gated only), m2 (the Improved Transparency check counts stage draws, not frames).

**Runs (2026-10-25):** 40135570271 (permanent changes + first probes + the 15-minute memory job: green),
40138168127 (draw-order probe), 40139105024 (batch order pin, SIFTEC collection), 40139975085 (full run: failed the
first hologram limit on old-GPU), 40143410574, 40146057417 (attempts 1-3 failed on SIFTEC's missing release, not
counted; attempt 4 the repeated-shot probe), 40152673881 (per-frame probe), 40154022133 (crosshair outline pin test),
40155387068 (SIFTEC collection, eleven hologram shots), 40156711337 attempts 1-3 (full runs with vanilla's own sift
counts: green, red on the map's first strict limit and the hologram's old direct baseline, green), 40161774743 and
40162905190 (map steadiness attempts, collection), **40164365265 (final code, every profile and both suites:
green)**.

### The 16-byte vertex, arenas per layer and the vanilla-relative gates (2026-10-25, Andre's later decisions)

The second review (round 2) left two things for Andre: translucent draw calls above vanilla on full and Vulkan (B1)
and sift's upload bytes (M2, settled: baseline 81,788, see `docs/ci-limits.md`). His decisions and what came of them:

**One 16-byte vertex for every terrain layer** (replaces the 28-byte wide format and the per-layer choice):
`0 u16 x,y,z ((p+8) x 2048)`, `6 u16 section slot | block light << 8`, `8 u8 r,g,b, sky light`, `12 u16 u,v (65535 =
1.0)`. Positions off the 1/2048-block grid and UVs off the 1/65536 grid are rounded; on-grid vertices are
bit-identical (shader harness, three seeds: 0 differences); largest error 0.000245 block and 1.45e-5 UV, at most
0.06 px on screen. Vanilla's translucent index lists and order are kept as they are (16-bit lists widened to 32 bits,
`49fec37`, so neighbouring translucent sections share an index type). Bytes per vertex 16 against vanilla's 28; terrain
vertices in the fly-through 79.6 MB against 139.3 MB in vanilla's format (water_glass 11.8 against 20.7, spawn 19.9
against 34.9). Vanilla next to Brimstone at 4x (plant-heavy and water-edge views): no gaps, cracks or shimmering
edges; the differences are 1/255 steps on rounded geometry.

**Vertex alpha** (Andre: no alpha byte): a solid or cutout layer with a vertex alpha below 255 stays with vanilla; a
translucent layer with one hands all translucent terrain back to vanilla for the session (solid and cutout stay with
Brimstone); one WARN line names the section, the bench counts it (`alphaLayersKeptByVanilla`,
`translucentHandedBackForAlpha`) and CI fails if either is non-zero. No CI scene has such a layer. **Known limit,
for package 4 (Fabric Renderer API):** models that tint vertices with alpha (FRAPI emitters can) lose Brimstone's
terrain for that layer or, for translucent, for the session.

**Strict gate at >2/255** (Andre): the rounding moves interpolated colours by 1-2/255; limits = old + 1.5 x the
measured extra (`docs/ci-limits.md`, "The 16-byte vertex", with the samples and the median method).

**Arenas per layer, starting small** (Andre): solid, cutout and translucent each have arenas of their own (so a
layer's draws break only when that layer alone passes 64 MiB), and an arena's buffer starts at 4 MiB and doubles up to
64 MiB (a one-time GPU copy at the end of the frame where meshes are copied in; offsets never change; the game's TLSF
allocator splits the blocks it hands out from their front, so a filling arena's buffer stays close to what it holds). Terrain GPU memory reserved is
now 16-112 MB in the core scenes against vanilla's 256-416 MB (table in `docs/ci-limits.md`), gated in every scene.
**Known limit, for package 9:** a grown arena does not shrink until it is empty (the hologram scene keeps the 56 MB
grown in the map scene before it).

**Draw calls against vanilla in the same run**: three `measure-*` client runs per suite (Brimstone loaded, every
module off) stand in for vanilla, checked against vanilla's pictures with the same limits; every scene's draw calls
are gated against them on average and median (`docs/ci-limits.md`, "Against vanilla in the same run"). Fly-through:
123.07 against 123.07 (full) and 123.11 against 123.05 (Vulkan; the gate there is vanilla + 2 by Andre's rule);
translucent runs break 0 times per frame at arena or index-type changes (old-GPU's plain path breaks at regions, 24.5
per frame, as designed).

**sift**: its strict noise came from small sprites drifting over the canyon in every run, vanilla's too. Mob and
phantom spawning off for the scene plus sending the mobs away (19 entities) left them in place, so they are not
entities; the CI pin mod now makes no particles in the Sift dimension (`SiftParticlesMixin`, every client run).
The kinds held off, from the pin's log: `the_sift:sift_note` (the Sift's floating note particles, 25 in the scene)
and one `minecraft:poof`. With the pin, vanilla b against a is 0/0 and the measure runs 0 in runs 40185974374 and
40187878826, and the sift strict limit was re-collected by the rule (`docs/ci-limits.md`; runs 40185974374, 40187878826, 40189806229): **100 pixels** (was 4,900; samples 55-61 in all three runs).
The sift setup's mob spawning goes back on in the next scene (hub).

**Review round 3** (fresh agent, of `f8018c2`): done-when holds; Andre's decisions carried out; every limit recomputed
from the PNGs and matching. Fixed: B1 (sift limit and the progress notes not yet committed), M1 (nothing checked that
the measure runs really ran with Brimstone's modules off: now the log lines, the bench state and zero arenas are
required), M2 (the alpha hand-back's races: a translucent layer staged after it is refused under the store's lock,
later index lists go through `stageIndices`, one WARN), m1 (hologram skips measure-vk), m2 (fly-through allowance on
full and Vulkan only), m3 (stale text). N1-N3 are decisions 6, 7 and 5 at the top of this file; N4 (a UV of exactly
65535/65536 stored as 1.0) matters only for a 65536-pixel atlas.

**For package 8's plan (Andre, 2026-10-25):** a deterministic particle scene (particles are random per run; CI holds
them off in the Sift and nowhere else yet).

### Laptop bench (Andre, when back)

Same instances and steps as `docs/laptop-bench.md`, with this branch's jar (after the merge, the `latest` release).
Plain instance:

```
/brimstone bench water_glass confirm
/brimstone bench translucent_move confirm
/brimstone bench water_glass_oit confirm
/brimstone bench translucent_move_oit confirm
/brimstone bench spawn confirm
/brimstone bench flythrough confirm
```

The `_oit` scenes switch Improved Transparency on for the run and put it back afterwards. SIFTEC instance:

```
/brimstone bench hologram confirm
/brimstone bench sift confirm
```

For `hologram`, press Tab once with the Build Gun in hand before measuring starts (it picks the Power Pole Mk.1).
Paste the latest.log blocks (or the JSON files). The `translucency` line should say `vanilla's own 0.0, mixed frames
0` and `on the render thread 0`; note its draws per frame and the sort times (`sorts ... ms avg, p95, max`) next to
FPS and frame times.

---

## Hand-off

Working notes for a new chat picking this up: read this file, then `docs/backend.md`, `docs/ci-limits.md`,
`docs/laptop-bench.md` and the design doc. CI results are on the `ci-logs` branch (each run overwrites it; extract a
run as soon as it finishes if another is running). Local builds aren't possible in the agent workspace (no access to
Fabric's maven or Mojang); everything is built and tested in CI. A full CI run takes about 45 minutes (20 client runs).
Game class dumps are a 1-day Actions artifact (`game-dumps`) the workspace can't download; ask Andre to attach one if
needed. Scratch tools (not in the repo): a compile check against stubs generated from a javap dump
(`scratchpad/p3/stubgen/check.sh`; it does not cover `ci/pin-mod`), the shader harness (`scratchpad/p3/harness4.py`),
strict-sample scripts (`scratchpad/tools/strict2.py`, `samples.py`).

**Now:** HANDOFF_NOW
