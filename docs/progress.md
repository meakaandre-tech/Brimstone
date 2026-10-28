# Brimstone progress

## Where things stand

**For Andre (2026-10-26), tracks:**

| track | branch | status |
|---|---|---|
| package 3 (translucency) | merged | `143defe`, main green (run 40197881761), `latest` rebuilt |
| quick CI | merged | `7a8e678`: main full run 40247412514 green, `latest` rebuilt from it; branch full run 40242749965 and quick run 40245473211 green |
| package 4 (Fabric Renderer API) | merged | `ccf4750` (squash of `package-4`), main full run 40409788393 green, `latest` rebuilt |
| package 7a (GUI measurement) | merged | `7597832` (squash of `package-7`, counters and scenes only; package 7's done-when not met; **7b deferred until after package 6**, with inventory tooltips, F3 and long chat measured first). HUD: 23 GUI draws per frame, 11 states (6 pipelines, 7 textures, 2 scissor areas), identical to vanilla |
| package 4b (terrain speed-ups) | `package-4b` | not merged (Andre's check), `0fe3631` green full run 40619534606: upload budget, skip back faces, bench-only jar; see its section |
| core/mobs strict limit | main | 600 -> 10 (chicken wing pin; samples 0 in four full runs) |
| baselines after 4, 7a, 8 | merged | `2f6c545`: 66 draw-call baselines lowered to the merged build's clean values; chicken wing pin (mobs strict samples now 0) |
| animation pin fix | merged | `cea5bfb`: the animation pin counts texture ticks, not client ticks (the cause of a flake in core/animation's measure runs); **main full run 40526712029 green, `latest` rebuilt** |
| package 8 (effects) | merged | `478fd69` (squash of `package-8`, with your four additions), main full run 40432198119 green, `latest` rebuilt |

**Quick CI** (decisions on your behalf): quick mode compares Brimstone old-GPU with vanilla-a (vanilla's GL 3.3 and
4.5 pictures differ no more than vanilla a and b do) using the full mode's limits, the strict check on the pixels where
vanilla a and b agree; the measure-run gates are skipped in quick mode and the report says so. Main's logs are on
`ci-logs-main`, the old `ci-logs` branch is no longer written. A quick run takes 15-25 minutes, not 10 (the core
client runs alone take about 15). siftec/mam is gated against vanilla on the median (its Vulkan average failed on
runner speed with equal medians on the branch's first full run; your rule). Full-mode equivalence: the new
compare.py gave identical failures, data and reports to the old one on real and injected results.
**Smallest thing each scene must catch** is in `docs/ci-limits.md`; existing limits larger than it (not changed,
for you): spawn (a section at mid distance), flythrough, water_glass (both also covered by exact per-frame checks),
mobs (a chicken), factory (one ladder strip), hub and mam (a text line, an item icon; package 7 works on these).

Decisions made on your behalf while you were away, package 3 (each also in its section):
1. **Strict extras measured on medians:** extra = median of five 16-byte runs minus median of five runs before the
   switch, largest over the profiles (not the largest single run, which carried run-wide noise that vanilla shows
   too; that would have raised spawn by 2,834 and hub by 1,638). Every run passes the new limits (`docs/ci-limits.md`).
2. **Permanent `measure-*` CI runs** (Brimstone loaded, every module off) to measure vanilla in the same run: your
   memory gate and same-run draw-call gate need vanilla's numbers and vanilla runs have no bench. They are checked
   against vanilla's pictures, and that their modules really stayed off (review round 3).
3. **Fly-through vanilla + 2** (your rule, full and Vulkan only): Vulkan averaged 123.11 against 123.05 in one run;
   Brimstone's terrain draws are exactly vanilla's 3, the 0.06 is in other draws.
4. **Draw calls on the median for sift, water_glass, water_glass_oit, nether, translucent_move and mobs** (your
   rule): each failed only on the average with equal medians, the two runs at different speeds near 20 fps (e.g.
   sift 154.0 at 15.1 fps against 135.58 at 22.64 fps; mobs on Vulkan 134.0 at 18.81 against 115.65 at 23.72). The
   fly-through, spawn, occlusion scenes, translucent_move_oit and the other SIFTEC scenes stay on both.
5. **sift particles:** the CI pin holds off every particle in the Sift dimension (1-36 `the_sift:sift_note` per run, one
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

**Branch** `package-1` (merged). **CI:** green run https://github.com/meakaandre-tech/Brimstone/actions/runs/40072856079
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

**Branch** `package-2` (merged). **CI:** green run https://github.com/meakaandre-tech/Brimstone/actions/runs/40093135028
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

**Merged** to main as `c0749aa` (trigger commit dropped). Main's first run after it failed on noise, not on
Brimstone: the Vulkan fly-through strict gate (7384 px against 5900) caught animated water drifting between runs,
and one bare-scene count (72 against 70) came from loot dropped by `kill`. Fix `ced21b3` (CI only): a CI-pin mixin
holds texture animation on its first frame in every run (vanilla and Brimstone), the draw-call baselines went down
by exactly the draws the pin removes (table in `docs/ci-limits.md`), and scenes now clear areas by teleporting
entities below the world instead of killing them (now they leave no drops). No limit or baseline was raised.
Main green: https://github.com/meakaandre-tech/Brimstone/actions/runs/40108769128, `latest` released from `ced21b3`.
(Since then the scenes teleport entities rather than kill them, so the note above about killing applies to that.)

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

**Branch** `package-3`, squash-merged to main (see the top of this file). **Superseded in part:** the vertex format
below (16 or 28 bytes per layer, alpha going wide) and the deviations and choices about it were replaced on 2026-10-25
by one 16-byte vertex for every layer, see "The 16-byte vertex, arenas per layer and the vanilla-relative gates" at
the end of this section. **CI (first round):** green run https://github.com/meakaandre-tech/Brimstone/actions/runs/40126212181 on `6d48fc7` (every profile, both suites, compare
passed). A docs-only rerun (run 40128444322) failed one runner-speed-dependent count; see
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
**Known limit, for package 9:** a grown arena does not shrink until it is empty (the hologram scene keeps the 56-72 MB
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
The kinds held off, from the pin's log: `the_sift:sift_note` (the Sift's floating note particles, 1-36 per run)
and one `minecraft:poof`. With the pin, vanilla b against a is 0/0 and the measure runs 0 in runs 40185974374 and
40187878826, and the sift strict limit was re-collected by the rule (`docs/ci-limits.md`; runs 40185974374, 40187878826, 40189806229): **100 pixels** (was 4,900; samples 55-61 in all three runs).
The sift setup's mob spawning goes back on in the next scene (hub).

**Review round 3** (fresh agent, of `f8018c2`): done-when holds; Andre's decisions carried out; every limit recomputed
from the PNGs and matching. Fixed: B1 (sift limit and the progress notes not yet committed), M1 (nothing checked that
the measure runs really ran with Brimstone's modules off: now the log lines, the bench state and zero arenas are
required), M2 (the alpha hand-back's races: a translucent layer staged after it is refused under the store's lock,
later index lists go through `stageIndices`, one WARN), m1 (hologram skips measure-vk), m2 (fly-through allowance on
full and Vulkan only), m3 (stale text). N1-N3 are decisions 6, 7 and 5 at the top of this file; N4 (a UV of exactly
65535/65536 stored as 1.0) matters only for a 65536-pixel atlas. **Round 4** (fresh agent, of `aadfb35`): round 3's
fixes hold (it mutation-tested the measure-run checks and recounted sift from the PNGs); fixed: staged translucent
layers at the alpha hand-back are completed through vanilla's callback (or released) instead of only freed, the
measure runs' strict check fails on a missing screenshot, both alpha WARNs are guarded, stale scene notes and docs.

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

## Package 4: Fabric Renderer API (branch `package-4`, not merged: Andre reviews it first)

Done when (design doc): "Full FRAPI in place of Indigo; Create connected textures and every SIFTEC model draw the
same as with Indigo."

### Plan (the first cut; "What was built" and "Review fixes" supersede it where they differ)

What the API needs from a renderer in Fabric API 0.161.0+26.3 (read from its `fabric-renderer-api-v1` sources and the
javap dump): `Renderer` (`quadEmitter`, `mutableMesh`, `altModelBlockRenderer`) and feature renderers for the API's
two submit types (`ExtendedBlockModelSubmit`, `ExtendedItemSubmit`); the API's own mixins route chunk meshing, moving
blocks (pistons), block models drawn as features (block displays, minecart blocks) and every item through them. A mod
that declares `fabric-renderer-api-v1:contains_renderer` makes Indigo stand aside.

1. Quads and meshes: Brimstone's own encoding (`frapi/QuadData`: 40 ints a quad), `MutableQuadViewImpl` (the
   `QuadEmitter`, with the transform stack), `Meshes` (mutable and immutable).
2. Blocks (`BlockRenderer`): first cut lit with vanilla's own `BlockModelLighter`; superseded (Indigo's lighting, below).
3. Features: vanilla quads go to vanilla's own `ItemFeatureRenderer` / `BlockModelFeatureRenderer` (converted to their
   submits; every lifecycle call forwarded one-for-one); API mesh quads are drawn as vanilla draws such a quad, with
   vanilla's own tint lookup and special-foil decal pose through invokers.
4. CI: every scene compares Brimstone's renderer (Brimstone runs) with Indigo (vanilla runs).

**Clean room and Indigo (Andre, 2026-10-26):** Indigo is part of Fabric API, which is Apache-2.0, so the clean-room
rule does not apply to it (the design doc's clean-room note says so): its code may be read and reused with
attribution. Until then it was not read (its smooth lighting was measured black-box, see the history below); since
then Brimstone's block lighting is Indigo's own code, adapted with its Apache License headers kept and a `NOTICE` file
(shipped in the jar as `NOTICE_brimstone`). The clean-room rule still applies to every other mod.

### What was built (`src/main/java/com/meakaandre/brimstone/frapi/`, `mixin/frapi/`)

- `QuadData`, `QuadViewImpl`, `MutableQuadViewImpl`, `Meshes`: the API's quads, emitters and meshes in Brimstone's
  own encoding (40 ints a quad), with the transform stack, sprite and material data. Quad geometry is Indigo's
  (`GeometryHelper`): the face normal (the nominal face's axis when the quad lies parallel to it, else the plane through
  its diagonals), the light face (Indigo's tie-break), the shape flags, and mesh quads keep their packed face normal,
  light face and flags as Indigo's meshes do.
- `BlockRenderer` (chunk meshing, pistons): the model emits its quads; each is culled with vanilla's
  `Block.shouldRenderFace`, then shaded, tinted and moved into place exactly as Indigo's `AltModelBlockRendererImpl`
  does it: smooth lighting `AoCalculator` (with `AoFace`, `AoFaceData`), flat lighting `FlatLighter`, Indigo's tint
  cache (block tint sources and Fabric's `BlockColorRegistry` factories), vanilla's per-thread light cache. Indigo's
  configuration is fixed to its defaults (ambient occlusion mode hybrid; the smooth-lighting offset, mean-light and
  exterior-vertex fixes on), since Indigo's config file is not read once Brimstone draws.
  A block whose model throws is drawn by vanilla's `ModelBlockRenderer` (logged once, nothing drawn twice).
- Adapted from Indigo (Apache-2.0, headers kept, listed in `NOTICE`): `AoCalculator`, `AoFace`, `AoFaceData`,
  `FlatLighter`, `GeometryHelper` and the shading and tinting in `BlockRenderer`.
- `ItemSubmitRenderer`, `BlockSubmitRenderer`: the API's item and block-model submits. Vanilla quads, and mesh quads
  carrying nothing a vanilla quad can't (white, no own normals, vanilla light, not emissive, facing along an axis), go to
  vanilla's own `ItemFeatureRenderer` / `BlockModelFeatureRenderer`; the rest are drawn as vanilla draws such a quad,
  with the quad's face normal where it has none of its own. Vanilla's tint lookup and foil decal pose through invokers.
- `fabric.mod.json` declares `fabric-renderer-api-v1:contains_renderer`, so Indigo stands aside, and `breaks` Sodium;
  Brimstone's main entrypoint (`Brimstone`, client only) registers the renderer, before any mod's client entrypoint
  runs. Any other mod that declares `contains_renderer` is named in one error line saying which renderer won.
- `config/brimstone.properties`, `block-renderer=on|off` (written with `on` if missing, read at start-up, needs a
  restart): off sends every block (`BlockRenderer` straight to vanilla's `ModelBlockRenderer`) and every item and block
  model feature (all mesh quads to vanilla's feature renderers as vanilla quads) through vanilla's code, and logs what is
  lost (mods' API meshes lose their own lighting, colours, emissive and ambient occlusion settings). With smooth
  lighting on, blocks then show vanilla's own smooth lighting, which differs slightly from Indigo's. The renderer itself
  still registers: Indigo has stood aside for the whole game. The bench reports which path blocks and mesh quads took
  (`renderer`), and CI checks it: none Brimstone's in the off session, no block vanilla's in the others.
- CI: compare.py checks, per client session, that each vanilla run drew with Indigo, each Brimstone run with
  Brimstone's renderer (not Indigo, no "Brimstone renderer fallback:" line, renderer self-test passed), and each
  measure run with Indigo (CI takes `contains_renderer` out of the measure run's jar; Brimstone's renderer stays off
  with measureOnly). The session whose scene sets `brimstoneConfig` `block-renderer=off` (ci/client-run.sh writes
  it to `config/brimstone.properties`) must start with the block renderer off; every other session must not.
- `RendererSelfTest` (CI, `brimstone.debug.verifyRenderer`, once in the world): a model that throws after emitting
  quads, the transform it leaves pushed, a skipped chunk layer in a block model submit, vanilla quads through meshes
  (kept as the same quads; dropped on any change), `emit()` on a mutable mesh cursor, and `buffer()`'s normal; "not
  run" in the block-renderer-off session.
- New CI scenes (package 4): `core/flat_plants` (a garden of cross, angled and tinted plants with smooth lighting
  off), `siftec/factory_flat` (the factory with smooth lighting off), `core/renderer_off` (a stone-brick hut with
  slabs, stairs, a window and a block display, block renderer off, smooth lighting off) and `siftec/create_ct`
  (Create connected textures, below).

### Create connected textures (done-when)

`siftec/create_ct` shows, in one picture: a 5x4 wall of `create:andesite_casing` with a 3-block return at its west end
(connections around a corner), a 5x4 wall of `create:brass_casing` and a 3x3 floor patch of it in front, a 5x4 wall of
`create:copper_casing` with `minecraft:stone` set in at its centre left (CT around a non-CT block), and a 5x4 wall of
`create:framed_glass`; each wall ends against `minecraft:stone` or `minecraft:glass`, which do not connect. Create
draws these through the Fabric Renderer API, so Brimstone's renderer tessellates them in Brimstone's runs and Indigo in
the vanilla runs, in every profile (full, old-GPU, Vulkan). CI hides the GUI for this picture (SIFTEC's minimap is run
to run noise larger than a single connected-texture border). Result (runs 40279156080 and 40287221751): in full,
old-GPU and Vulkan, 0.0% of pixels over 8/255 and 0 strict pixels against the vanilla runs, the same as vanilla
against itself.

### Exactness against Indigo

A branch-only diagnostic (run 40274749083, removed in the next commit) ran Brimstone's `BlockRenderer` next to Indigo's
on every block Indigo tessellated in the six measure runs (Indigo is the renderer there: chunk meshing and moving
blocks, the `altModelBlockRenderer` path; not the item and block-model feature paths, which are unchanged), smooth and
flat lighting, all core and SIFTEC scenes: about 4.7, 5.35 and 5.1 million blocks (core full, old-GPU, Vulkan) and 0.8, 0.85 and 0.7
million (SIFTEC): **0 different** (every vertex's position, colour and lightmap identical). What the pictures still
show against the vanilla runs is the 16-byte vertex's rounding (package 3, Andre's decision), mostly 1/255 on
off-grid plant geometry (see docs/ci-limits.md).

### History: Indigo's smooth lighting measured black-box (before Andre's 2026-10-26 decision)

Before Indigo's code could be read, its smooth lighting was measured black-box (a branch-only diagnostic ran Indigo as
the renderer and Brimstone's block renderer beside it; the rules were fitted offline, `FaceAo`), reproducing every quad
facing along an axis but 11 one-step cases and quads at an angle to one light or colour step. `FaceAo` is replaced by
Indigo's own code; the residual one-step differences, Brimstone's own light-face tie-break and vanilla's flat lighting
of cross and angled quads are gone with it.

### Review fixes (review: `scratchpad/review-package-4.md`, 7 should-fix, 14 nits)

- **S1** block fallback: a block's quads are held until its model has emitted them all; if it throws they are dropped,
  its pushed transforms cleared, and vanilla's renderer draws the block (nothing drawn twice).
- **S2** registration from a main entrypoint (client only): every main entrypoint runs before any client entrypoint,
  so no mod can ask for the renderer first (CI log: Brimstone's registration precedes Indigo's client entrypoint
  standing aside). A failed registration of the renderer or of its feature renderers is an error line, and fails CI.
- **S3** allocation: meshes keep each quad's unchanged vanilla quad and known sprite, so vanilla block models drawn in
  entity contexts reach vanilla's renderer as the same quads (no conversion, no sprite lookup); one render type for
  every layer skips the layer scan; block lighting of non-vanilla quads uses a constant sprite (vanilla's lighting
  never reads it); narrowed parts, glint decal inverses, decal wrappers and posed-buffer vectors are no longer per quad.
- **S4** `QuadView.buffer` without a pose writes the exact face normal (the API's contract); no terrain shader reads
  terrain normals, so the picture is unchanged.
- **S5** every renderer fallback logs "Brimstone renderer fallback:" (tessellation, face brightness, item tint and
  foil-pose invokers, registration, feature submits); any such line fails a Brimstone run.
- **S6** measure runs draw with Indigo (above); docs/ci-limits.md says so.
- **S7** a layer whose render type is null is skipped, not drawn with another layer's type.
- Nits fixed: N2 (animated reset without a sprite), N3 (decoded mesh quads report their light face as nominal face),
  N4 (mesh cursor `emit()` does nothing), N5 (a warning above 32 item render types), N6 (separate, logged invoker
  failures), N7 (per-quad allocations on the item path), N9 (comment), N12 (stale javadocs, progress plan, bench
  note), N13 (branch trigger dropped in the rebase). Not changed then: N1 and N8 (both about `FaceAo`, since replaced by
  Indigo's own lighting code, which settles them), N10 (whether `executeGroup` uses the list is unknown without
  bytecode; the cache is bounded and cleared per frame), N11 (one atlas lookup per meshing call off the render thread,
  as vanilla's own mesher reads sprites; benign).
- Second review (a fresh agent over the whole package diff, after the fixes): no blockers. Fixed: feature submits
  now fall back too (a throwing mesh or render type function draws the submit's vanilla quads only, logged, instead of
  crashing), a JVM error is no longer swallowed by the block fallback, `atlas()` changes drop the known sprite, CI
  checks the renderer lines per client session, the self-test checks more kinds of change, and these docs. Not
  changed: `vanillaLike` runs twice per mesh quad (small), normals stay quantised to bytes (the API's own precision).
  The flat lighting of cross quads it raised went to Andre, and is now Indigo's own (Andre's decisions, below).
- Third review (a fresh agent over `d37c587..d7e21a9`, Andre's decisions; findings in
  `scratchpad/review4c/findings.md`): no blocker; the port matches Indigo line by line. Fixed: the Apache-2.0 text
  (`LICENSE-APACHE-2.0`, shipped in the jar) and headers plus NOTICE entries on every file with Indigo-derived logic
  (`QuadViewImpl`, `MutableQuadViewImpl`, `Meshes`, `QuadData` too); the new scenes' samples, limits and median gates
  recorded in docs/ci-limits.md and `ci/pixel-noise-samples.json`; count baselines for the four new scenes, and
  compare.py now fails a scene with no committed baseline entry; the off switch checked through the bench's renderer
  report (CI fails if any block or mesh quad takes Brimstone's path with it off, or any block vanilla's with it on);
  the self-test compares the vanilla path quad by quad with vanilla's renderer for tinted, cutout and emitting blocks
  and checks Indigo's geometry (45-degree light faces, a mesh quad's packed face normal and its copy, emissive at light
  emission 15); a decoded mutable-mesh quad's nominal face is its light face; a copy from a mesh no longer drops the
  kept vanilla quad; conflict detection uses Indigo's own test (the key present) and logs one error; an unknown
  `block-renderer` value is warned about; `create_ct` requires Create; notes and docs. Not changed: the `breaks` text
  (fabric.mod.json cannot carry one); `renderer_off`'s blocks (the self-test covers tinted, cutout and emitting blocks
  on that path exactly, without a picture-changing scene edit that would need fresh noise runs).
- Andre's decisions on the review (2026-10-26), all carried out: (1) Indigo's code may be used (Apache-2.0, with
  attribution): block lighting is now Indigo's, exactly; (2) CI scenes with smooth lighting off (`flat_plants`,
  `factory_flat`) against Indigo; (3) no runtime off switch, but a start-up config option (`block-renderer=off`),
  tested in CI (`renderer_off`); (4) `breaks` Sodium, and one error naming any other mod that declares itself the
  renderer; (5) a Create connected-texture scene (`create_ct`).

### Deviations and choices to confirm

- **Indigo is the reference** for block lighting, as the doc's done-when says ("draw the same as with Indigo"); since
  Andre's 2026-10-26 decision it is Indigo's own code (Apache-2.0, attributed), exact on every block compared.
- **Indigo's configuration fixed to its defaults**: a player's `config/fabric/indigo-renderer.properties` (ambient
  occlusion mode and the lighting fixes) is not read once Brimstone draws.
- **The 16-byte vertex's rounding** (package 3, Andre's decision) is what the new scenes' strict limits allow for
  besides vanilla's own noise; it is measured per scene (docs/ci-limits.md).
- **`breaks` Sodium**: Fabric Loader's own incompatibility message is shown (fabric.mod.json cannot carry a custom
  text); the README says why (Brimstone replaces Sodium and is the renderer itself). Other renderer mods are not named
  in `breaks` (only Sodium was asked for); one error line names any of them at start-up and says which renderer won.
- **`block-renderer=off` still registers Brimstone's renderer** (Indigo has stood aside for the game), and routes every
  block and feature through vanilla's code; the self-test does not run then.
- **Vertex alpha below 255** (package 3's known limit) is unchanged: such a layer stays with vanilla's terrain
  (correct picture; Brimstone's terrain path is lost for it). No CI scene has one.

### Evidence

- Green CI runs on the branch: 40221609346 (`4861a6c`), 40225361741 (`86a3f5f`), 40227499408 (`81ecd03`), 40230147788
  (`248e3df`), 40233004611 (`1022f62`, the last with the diagnostic: 2.2 and 2.8 million blocks compared, 2,870 and
  2,521 differing, every one by at most one colour or light step). The diagnostic is removed in the next commit.
- Green full run 40236725811 (`2347de6`) before the review. After rebasing on main's quick CI (one squashed commit)
  and the review fixes: quick run 40248977377 (`bf673ee`) and full run 40251612353 (`2e4dcbc`) green: every Brimstone
  session registered Brimstone's renderer and passed the self-test with no fallback line, every measure and vanilla
  session drew with Indigo, and the same-run draw-call and memory gates passed against the Indigo measure runs; full run
  40256275410 (`d37c587`) green after the second review.
- Andre's decisions: quick run 40270542386 (`8eb4284`); diagnostic full run 40274749083 (`3420d85`: Brimstone's block
  renderer identical to Indigo's on every block compared); noise runs 40279156080 (`b343aa6`) and full run
  40287221751 (`d7e21a9`) **green**, with every new scene passing: flat_plants 225 / 225 / 217 strict pixels (limit
  350, the 16-byte vertex's rounding), renderer_off, factory_flat and create_ct 0 in every profile. The final full run
  after the third review's fixes and the rebase is given in the hand-off to the lead.

---

## Package 8: effects

**Branch** `package-8` (not merged; Andre reviews it). CI logs: `ci-logs-package-8`.

**Done when (doc):** "Particles, weather, sky, clouds, fog, The Sift's dimension effects, animated textures only when
seen." Feature rows: particles and weather (as vanilla/Sodium do, written from vanilla's code and measurements),
animated textures only when seen (texture animation work skipped for sprites not on screen); "custom dimension
effects, sky and fog hooks keep working". The picture matches vanilla exactly in every profile.

### Plan

1. **Measure first** (bench, every run): draw calls per frame that vanilla spends on texture animation, particles,
   sky, weather and clouds (draws counted while the game is inside each of those renderers), live and drawn particles,
   particle tick and extract time. What vanilla 26.3 already does (from the class signatures): particles are frustum
   culled at extraction into packed arrays and drawn a few draws per layer from one staged buffer; the sky comes from
   prebuilt buffers; clouds rebuild their mesh only when the cloud cell changes; weather builds its columns only while
   it rains; animated textures are redrawn into the block atlas on every client tick, one render pass per mip level and
   one draw per changed sprite per level, whether or not anything on screen shows them (package 2's pin measured
   that at 24-95 of 120-240 draw calls per frame in the core scenes and about 180-270 in SIFTEC's).
2. **Animated textures only when seen** (the one effect with real work to save): a new module, `animation`.
   - Which animated block-atlas sprites each section mesh shows: read from the finished mesh itself (each quad's UV
     centre looked up in a table of the animated sprites' rectangles) on the worker thread, where the mesh goes to
     the GPU; so it holds for vanilla, FRAPI and modded block models alike.
   - "Seen" in a frame: in a mesh of a section in vanilla's visible list, or used anywhere outside terrain (a sprite
     whose UVs are read outside section meshing, through the sprite's UV getters or a baked quad's UVs: items,
     particles, entities, block entities, Create and Flywheel caches; sticky until the atlas reloads).
   - On each client tick, sprites that changed are redrawn only if seen in the last frame; the others are marked
     stale. Before the next frame's terrain is drawn (vanilla's visible list is known by then, no render pass is open)
     every stale sprite that is now seen is redrawn first; a sprite first used outside terrain while stale is redrawn
     on the spot. So whatever samples the atlas sees exactly vanilla's frame. A stale sprite that could not be redrawn
     before its first use is counted ("late") and CI fails on any.
   - Fallbacks: hooks missing, vertex format different, measure-only runs: off, vanilla redraws everything. Anything
     unexpected: off for the session with one ERROR line, every stale sprite redrawn at once.
3. **Particles, weather, sky, clouds, fog, The Sift**: optimise only where the measurement shows work to save;
   otherwise left to vanilla (their hooks and the Sift's sky and fog then keep working by construction; the sift scene
   checks it).
4. **CI checks with real content:**
   - `core/particles`: 66 kinds of particle at fixed places in front of a wall. The CI pin mod makes them the same
     in every run, vanilla and Brimstone alike, inside that scene's area only: their random numbers seeded from where
     they are made, frozen (not ticked, drawn at partial tick 0) while the player is in the area, removed when the
     player leaves it.
   - `core/animation`: animated blocks in front of the camera and behind it. The pin starts every animation from frame
     0 when the player enters the area, lets it run exactly a fixed number of ticks and holds it there, so vanilla and
     Brimstone reach the same frame; the blocks behind the camera are shown by extra shots after turning around (stale
     sprites that must be caught up), plus a block particle of an animated block (a use outside terrain). During the
     bench a marker block lets animation run freely, so the counts show vanilla's animation work against Brimstone's.
   - compare.py: animation module on with no fallback and no late redraw in Brimstone runs, off in measure runs, and
     skips and catch-ups seen in the animation scene.
5. Independent review, fixes, rebase on main, green full run, docs.


### Measurements (vanilla, from its measure runs; run 40267202902, full profile, draw calls per frame)

| scene | texture animation | particles | sky | weather | clouds | particles alive / drawn |
|---|---|---|---|---|---|---|
| core/spawn | 115.0 | 0.44 | 3 | 0 | 0 (off in CI) | 86 / 0.8 |
| core/water_glass | 86.3 | 1 | 3 | 0 | 0 | 52 / 52 |
| core/nether | 87.2 | 0.8 | 0 | 0 | 0 | 43 / 8 |
| core/mobs, occlusion, occlusion_move | 76.6-76.7 | 0 | 3 | 0 | 0 | 0 |
| core/particles | 76.6 | 4 | 3 | 0 | 0 | 66 / 66 (the bench's own setup; 132 before the bare shot was added) |
| core/animation (animation running) | 136.4 | 1 | 3 | 0 | 0 | 2 / 2 |
| siftec/sift, map | 140.0 | 0 | 3-4 | 0 | 0 | 0 |
| siftec/factory, hub, mam, hologram | 94-109 | 0-1 | 3 | 0 | 0 | 0-162 |

So: texture animation is 40-60% of all of vanilla's draw calls in every scene, even with CI's pin holding animation
still (vanilla redraws its interpolated block sprites, 23 in the core suite and 28 in SIFTEC's, on every tick, and the
rest on every tick their frame changes). Particles (frustum culled at extraction, a few draws per layer), the sky
(prebuilt buffers, 3-4 draws), weather (only while it rains) and clouds (mesh rebuilt only on a new cloud cell) cost
1-4 draws each and a fraction of a millisecond (particle tick 0.001-0.16 ms per tick, extraction 0.001-0.07 ms per
frame): nothing there worth replacing, so Brimstone leaves particles, weather, sky, clouds, fog and the Sift's
dimension effects entirely to vanilla (their hooks keep working by construction; the sift scene shows SIFTEC's sky and
fog matching vanilla).

### What was built

1. **Effects counters** (`bench/EffectsStats.java`, `mixin/counters/Effects*Mixin.java`): every bench reports draw calls
   per frame by what they draw for (texture animation, particles, sky, weather, clouds, the rest), particles alive and
   drawn per frame, particle tick and extraction time. Count-only mixins in the counters config, in the counter audit
   (CI fails on a missing one). `compare/report.md` has an "Effects" table per suite and profile, vanilla (measure run)
   next to Brimstone.
2. **Animation module** (`animation/`, `mixin/animation/`; `brimstone.animation.mixins.json`): animated block-atlas
   sprites are redrawn only when seen.
   - Which animated sprites a section mesh shows is read from the mesh itself on the worker thread, as each layer goes
     to the GPU (`RenderSection.addSectionBuffersToUberBuffer`, the hook terrain already has, before anything takes the
     vertices): each quad's UV centre looked up in a grid of the animated sprites' rectangles (half a texel of margin).
     So it holds for vanilla, FRAPI and modded block models alike. 0.03-0.06 ms per layer.
   - A sprite whose UVs are read outside section meshing (the sprite's UV and position getters, a baked quad's UVs)
     counts as used outside terrain until the atlas reloads: items, particles, entities, block entities, screen
     overlays, Create's and Flywheel's model caches. Section meshing (a MixinExtras method wrapper, so the scope also
     ends when meshing throws) and Fabric API's sprite finder indexing every sprite are not uses.
   - Each client tick, sprites that changed are redrawn only if seen in the last frame (in a visible section's mesh or
     used outside terrain); the others are marked stale. Vanilla's animation state keeps ticking exactly as before.
   - Before each frame's terrain (vanilla's visible list known, nothing drawn yet, no render pass open), every stale
     sprite the frame shows is redrawn; a stale sprite's first use outside terrain redraws it on the spot. A stale
     sprite is drawn with vanilla's own `drawToAtlas`, as on the tick that changed it: with the state's redraw flag set
     and, for a whole-frame animation, at tick 0 of its frame (vanilla only draws then, and only binds the frame then;
     learned from CI's "Can't draw without a render pipeline", runs 40250087042-40257302632).
   - A stale sprite that could not be redrawn before something may have drawn from it (first used inside a render pass
     or on another thread) is counted as late; CI fails on any. None in any run.
   - Off in measure runs (`brimstone.ci.measureOnly`); off with one ERROR line and every stale sprite redrawn at once
     after anything unexpected; CI fails on any fallback. Debug property `brimstone.debug.animationUses` (set in CI's
     Brimstone runs) logs each sprite's first use outside terrain with where it came from and whether it was stale.
   - Result (run 40267202902, full): animation draws per frame 0-10 against vanilla's 77-140 in the pinned scenes, 25.7
     against 136.4 in the animation scene with animation running; total draw calls per frame in the animation scene
     33 (median 44) against vanilla's 144 (179).
3. **CI pins** (`ci/pin-mod`, every run, vanilla and Brimstone alike; `Pins` holds the areas): particles in the particle
   and animation areas seeded from where and what they are (the provider's and the particle's own random numbers),
   not ticked and drawn at partial tick 0 while the player is in the area, removed once the player leaves; in the
   animation area every animated texture back to frame 0 on entering and leaving, then exactly 70 ticks of animation,
   then held (free while the scene's bench step sets its marker block). Also a banner sway pin (see deviations).
4. **Scenes** (`core.json`): `particles` (66 particle commands, 49 particles visible at age 0, 3 blocks in front of the camera, plus a `bare` shot
   without them) and `animation` (animated blocks in front of the camera and 34 blocks behind it, a block particle of
   a command block whose sprite is seen nowhere else; shots `behind` and `front`). Everything there is built after
   the 70 ticks, so Brimstone left every changed sprite out and must redraw each before it is drawn: when the front
   blocks' sections first show, on the particle's first use, and when the `behind` shot walks to the far blocks.
   While measuring, the scene's steps run the before-terrain and first-use paths again with animation running (a
   block particle and a block whose sprites only the far blocks show); CI requires sprites left out, a redraw before
   terrain and a redraw on first use inside the measured window (`ciRequiresAnimationSkip`; the bench counts catch-ups
   by path). The core world pregenerates their area.
   Negative control: in run 40253963481 the module's redraw of stale sprites was still broken (it fell back and redrew
   them wrongly); the animation shots then differed from vanilla on 5,881 (front) and 3,179 (behind) strict pixels
   against a limit of 10.
5. **compare.py**: the animation module on, no fallback, no late redraw in Brimstone runs; off in measure runs; the
   scene requirement above; the Effects table.

### Deviations and decisions

- **Clouds and weather: no new scene.** CI keeps clouds off and the weather clear, and Brimstone doesn't touch either
  (nor the sky, fog or the Sift's dimension effects); a deterministic cloud or rain scene would test vanilla against
  itself. Brimstone's animation redraws are the only new draws and happen before the frame's terrain, at a point where
  vanilla's own tick-time redraws also happen.
- **Banner sway pin** (`BannerSwayMixin`): core/occlusion_move.x2309 sees the occlusion scene's banner edge-on, and its
  flag sways with game time, which Brimstone's benches shift: 0 to 330 strict pixels against a limit of 32 in five
  runs, vanilla a/b 0-7. Held still in every run instead of raising the limit; banner sway is therefore untested in
  CI (package 6's entities and block entities may want a scene with it running).
- **Existing draw-call baselines left as they are** (they now have 70-130 draws of room): for the lead's hand merge of
  packages 4, 7 and 8; lowering them is for Andre to decide.
- **No independent review by a fresh agent from inside this session**: the session that built the package has no tool
  to start another agent; it reviewed its own diff against the rules (below) and the lead should run the independent
  review before merging.

### Evidence

- Full run 40275109388 (`7036ccd`, before the rebase): **passed**, every gate in both suites. New scenes: vanilla runs
  and measure runs agree exactly with each other; Brimstone 0 strict pixels on particles, particles.bare and
  animation.behind, 1 (the lantern edge) on animation and animation.front, in every profile. The animation module was
  on in every Brimstone run with no fallback and no late redraw; the animation scene's three catch-up paths ran.
- Full run 40267202902 (`22aa238`): every gate passed but the then missing limits of the new scenes.
- Quick runs 40250087042, 40253963481, 40257302632, 40260148458, 40263318499, 40271672678 (iteration; see the
  commits).
- Full run 40281351708 (`2b98814`, rebased on main `0caa6ca`): **passed**. Then the review fixes below and their own
  final full run (see "Final run").

### Final run

Full run 40293774852 on `da3614e` (rebased on main `d65510a`, with the review fixes): **passed**, every gate in both
suites; the animation scene's bench ran all three catch-up paths inside its measured window (quick run 40288502378:
12,510 left out, 2 redrawn before terrain, 1 on first use, 0 late).

### Review

Independent review by a headless `claude -p` reviewer with read-only tools (no agent tool in the package's session),
of `2b98814` and run 40281351708; findings kept in the session's scratchpad (`p8/review-findings.md`). Verdict "request
changes": F1 must be fixed, F2 documented, F3 and F4 cheap to fix. What was done:
- **F1 (major) fixed:** the scene requirement read since-start totals that the whole client session shares (spawn
  alone had left out 21,138 sprites before the animation scene). The bench now counts catch-ups by path (on a tick,
  before terrain, on first use) inside the measured window, the animation scene's steps make the before-terrain and
  first-use paths run there, and compare.py gates on those.
- **F2 (major) documented** (Known limits): a first use outside terrain inside a render pass or on another thread
  can't be redrawn before that draw; it is counted as late and CI fails on any, but CI only exercises render-thread
  first uses outside a pass (none was late in any run).
- **F3 fixed:** a batch whose redraw throws is marked stale again, so the redraw of every stale sprite after the
  failure covers it.
- **F4 fixed:** a handler injected into several methods (8 sprite getters, 5 baked-quad UV accessors, 2 terrain
  prepares) counts as applied only when every target method calls it; otherwise the module stays off.
- **F5 fixed:** after a failure on another thread, the redraw of every stale sprite also happens at a first use on the
  render thread outside a pass, not only before the next frame's terrain.
- **F6:** the negative control is the earlier broken run (40253963481: 5,881 and 3,179 strict pixels; see Scenes).
- **F7 (banner pin):** left for Andre (Choices to confirm): the evidence is Brimstone against vanilla only; the pin
  removes banner sway from CI's coverage.
- **F8:** the new scenes' direct-buffer baselines differ between the two full runs (4.24 and 5.99 MB on full, the
  JVM's 2 MB pool steps, as in package 3's notes); highest value kept by the rule. Existing draw-call baselines not
  lowered (Choices to confirm).
- **F9:** Fabric API's renderer module (`SpriteFinderImpl`, Apache-2.0, not Indigo) was opened only to see where its
  sprite finder is built; no code taken. Listed for the lead.
- **F10 fixed:** doc counts (66 commands, 49 visible, 66 alive in the bench), run references.
- **F11 (note):** UV reads before the block atlas's upload finishes are not uses; recorded in Known limits.

### Andre's follow-ups (2026-10-27) and the rebase onto package 4

- `core/animated_items`: animated block sprites shown only outside terrain (the hotbar and held magma block, a sea
  lantern, prismarine, lava and water buckets; an item frame and a block display holding magma; a burning cow), in a
  third pin area where animation runs 70 ticks from frame 0 like the animation scene. Every one of those sprites is
  stale at its first use and redrawn on the spot (quick run 40404276347: magma, fire_0, fire_1, sea_lantern and
  prismarine each redrawn on first use, 0 late; Brimstone identical to vanilla). CI's late gate covers every scene,
  this one included.
- `siftec/factory`: a Create fluid tank full of lava floats in front of the camera (the shot had no lava).
- `core/particles`: strict limit 4 (was 10), below the smallest particle (9 pixels).
- Rebased onto main with package 4 (`ccf47501`) as one squashed commit (the branch before it is tagged locally as
  `p8-pre-squash`). Package 4's `flat_plants` and `renderer_off` scenes stand where the particle scene and its pin
  area were (x 2480-2560), so the particle scene and its area moved 300 blocks east (x 2800-2880); the core world now
  pregenerates x 2496-2959 for package 8's scenes. Shared files merged by hand, keeping both packages' additions:
  `ci/client-run.sh` (both debug properties), `BenchReport` (renderer, effects and animation lines), `fabric.mod.json`
  (both mixin configs), the core baselines and `core.json` (main's entries plus package 8's three scenes).

**Package 4's block renderer stays inside the not-a-use scope** (full run 40412755923 on the rebased branch, `7059fbe`;
Brimstone's block renderer on in every Brimstone session, e.g. 1.9-3.6 million blocks meshed by it per session, 0 by
vanilla's): animated sprites "used outside terrain" stay at 0-1 in every terrain scene (as before the rebase), so no
terrain sprite leaks out of the scope; Brimstone's animation draws per frame (vanilla's in brackets, full profile):
spawn 5.0 (115.0), flythrough 5.0 (115.0), nether 10.0 (97.0), water_glass, mobs, occlusion, translucent_move, the OIT
scenes, flat_plants, renderer_off and particles 0.0 (76.6-115.0); the animation scene with animation running while
measuring 32.3 (136.4), the same as before the rebase (32.3 in run 40293774852); animated_items 28.0 (136.4), with 9
sprites used outside terrain (the scene's own). That run failed only on compare's renderer-registration check reading a
truncated copy of one client log (core brimstone-full session 5's `client-5.log` holds only the last 34 lines; that
session's console log shows the renderer registered and its self-test passed) and on the then missing baselines of
animated_items, added from it.

**Green full run on the rebased branch: 40425375596** (`eb3db84`, on main `ccf4750` with package 4): every gate in both
suites. Brimstone's animation draws per frame (full / old-GPU / Vulkan): spawn and flythrough 5.0 / 5.0 / 5.0, nether
9.9 / 10.0 / 10.0, every other pinned scene (package 4's flat_plants and renderer_off included) 0.0, against vanilla's
76.6-115.0; animation (animation running) 32.3 in every profile against 136.4; animated_items 28.0 against 136.4-136.6,
identical to vanilla (0 strict pixels in every profile), 0 late redraws. SIFTEC's factory: the lava tank's sprite is
read by Create's fluid renderer on the render thread (logged as a use outside terrain), 0 strict pixels, 0 late.

### For package 6's plan (Andre, 2026-10-27)

CI now holds banner sway still in every run (`BannerSwayMixin`, package 8), so banner animation is untested: package 6
(entities and block entities) must add a scene with banner sway running (made repeatable, as the animation scene's pin
does for textures) and check Brimstone's banners against vanilla there.

### Choices to confirm (Andre)

1. The banner sway CI pin (`BannerSwayMixin`) instead of re-collecting occlusion_move.x2309's strict limit.
2. Existing draw-call baselines left as they are (70-130 draws of room per scene since the animation module); lower
   them after the packages 4, 7 and 8 merges?
3. No cloud or rain scene, as Brimstone leaves clouds, weather, sky and fog to vanilla.
4. Particles, weather, sky and clouds not optimised (vanilla spends 1-4 draws on each).

### Known limits

- A first use outside terrain of a stale sprite inside a render pass or on another thread (an item, entity or block
  entity whose UVs are read while a pass is open; a Create or Flywheel worker) is drawn one frame with the stale frame:
  it is counted as late (CI fails on any; none in any run), redrawn at the next safe point, but not prevented. CI's
  scenes only exercise render-thread first uses outside a pass.
- UV reads made before the block atlas's upload finishes (during the resource reload) are not uses.

- A mod that copies an animated sprite's UVs without going through the sprite's getters or a baked quad (e.g. reading
  the fields through its own accessor) would see a stale frame while nothing else shows that sprite.
- One particle of the 49 visible in the particle scene (9 pixels) is smaller than the strict limit's floor (10).
- Animated sprites of other atlases (items, particles, GUI) are left to vanilla; the module handles the block atlas
  (56 animated sprites in the core suite, 119 with SIFTEC's pack).

### Risks

- Vanilla's AnimationState internals (where the frame's interpolation goes) are not in the bytecode dump: the design
  only relies on drawing the current state with vanilla's own `drawToAtlas`, and the animation scene checks the
  result against vanilla.
- A mod that copies an animated sprite's UVs without the getters or baked quads would see a stale frame; the hooks
  cover vanilla's paths and every mod path that reads UVs through the game's classes.
- New scenes need limits and baselines collected by the documented rules (at least three runs).

---

## Package 7a: GUI measurement

**Branch** `package-7` (not merged; Andre: merges after packages 4 and 8 as "7a: GUI measurement", its counters and
scenes, without claiming GUI batching). **CI logs:** `ci-logs-package-7`.

**Package 7's done-when (doc) is NOT met:** "Batched GUI, text and item icons; SIFTEC's HUD, map, HUB, MAM and Ponder
screens unchanged." The screens are unchanged and now checked strictly, but nothing is batched: no GUI batching code
was written. 7a delivers the measurement (the `gui` bench block) and the CI scenes; batching is the separate plan "7b"
below, for Andre to decide from the numbers.

Original scope:
Fewer draw calls for GUI, text and item icons with an identical picture; the GUI scenes able to see a wrong GUI
element (strict limit below the smallest thing each must catch, `docs/ci-limits.md`); the map strict-gated if its
noise can be pinned; the block outline checked in CI again.

### Plan

1. **Measure first.** Vanilla 26.3's `GuiRenderer` already does much of what a GUI batcher does: every element goes
   into one vertex buffer, elements are put into layers by overlap and sorted by scissor, pipeline and texture inside a
   layer, runs of the same state merge into one draw, and item icons are drawn once into an item atlas and blitted.
   A count-only mixin on `GuiRenderer` adds a `gui` block to the bench (both in Brimstone and measure runs, so vanilla's
   numbers come from the same run): the GUI pass's draw calls per frame, its preparation draws (item atlas,
   picture-in-picture), elements, distinct states (the fewest draws any reordering could reach) and the draws a
   batcher would leave if each element joined the latest earlier draw of its state that nothing in between overlaps
   (what Brimstone could realistically win). If that leaves little, report it with the numbers and keep the package
   minimal (brief: ask the lead before building something large).
2. **Make the GUI scenes able to see a GUI element.** Sources found in the package-4 run's screenshots (vanilla a
   against b): the dev launcher's random player name (`Player<digits>`), which picks the default skin (the
   first-person arm, ~40,000 pixels) and is printed on the HUB and MAM screens (`<player>'s Company`); the MAM's
   selected tab has an enchantment glint whose phase follows the wall clock (~450 pixels). Pins, the same in every
   run: `--username BrimstoneCI`, `glintSpeed:0.0`. Then re-collect hub and mam by the standing rule.
3. **Map.** Its tiles differ pixel by pixel between runs: the area round 300 -200 is generated in each run (SIFTEC's
   map records chunks as they load) and the markers sit relative to a `spreadplayers` landing that varies by a block.
   Pins: that area is pre-generated in the SIFTEC CI world (`ci/siftec-world.sh`), the player is put on the ground at
   a fixed spot, random ticks are off in the CI world (water freezing, grass spreading). If the vanilla runs then
   agree, give the map a strict limit by the rule.
4. **Block outline.** CI's pin mod draws it again round one block (a new core scene, `outline`), so it is checked; its
   old-GPU row flicker is noise to be sampled.
5. **Ponder.** A scene only if it can be made deterministic (Create's `/ponder <item>` client command opens it; most
   scenes keep moving: belts, kinetics, the nixie scene's dancing parrot).

**Risks:** vanilla leaves too little to batch (then the package is mostly CI coverage); the map's server-side tiles
may still differ; new pins change the core and SIFTEC pictures for vanilla and Brimstone alike (limits re-collected
only by the rule).

### Outcome: in the measured scenes vanilla already groups GUI draws; no batcher built

The `gui` bench block (count-only `GuiRendererMixin`, `gui/GuiStats.java`; reported by compare.py next to vanilla's
numbers from the measure runs) on run 40254531258, full profile (Brimstone and vanilla equal within 0.03 draws per
frame in every profile and run):

| scene | all draw calls/frame (median) | GUI pass draws/frame | item atlas / PiP draws | elements | distinct states (floor) | reordered without changing the picture (text as overlapping nothing) |
|---|---|---|---|---|---|---|
| core scenes (HUD in spectator) | 121-135 | 1 | 0 | 1 | 1 | 1 (1) |
| core outline (creative HUD) | 127 | 3 | 0 | 4 | 3 | 3 (3) |
| siftec factory, sift | 151-153 | 5 | 0 | 20-21 | 3 | 5 (3) |
| siftec hub | 161 | 13.87 | 0 | 571 | 6 | 13.87 (8) |
| siftec mam | 162 | 13.86 | 1 | 110 | 7 | 13.86 (8) |
| siftec map | 170 | 23 | 0 | 317 | 12 | 21 (15) |
| siftec hologram | 173 | 18 | 0 | 128 | 12 | 18 (12) |
| siftec ponder (Create's Ponder index; run 40273668937) | 227 | 82.6 | 0.1 | 203 | 73 | 81.6 (75) |

Vanilla 26.3's `GuiRenderer` already does much of what a GUI batcher (ImmediatelyFast's job in older versions) did:
one vertex buffer for every element, elements placed into layers by overlap and sorted by scissor, pipeline and
texture inside a layer, runs of one state merged into one draw, item icons drawn once into an item atlas and blitted,
text as glyph quads in the same buffer. What was measured, and only that: **reordering** (joining each element to the
latest earlier draw of its state that nothing in between overlaps) saves 2 draws per frame in the map (of 170) and 1
in the Ponder index (of 227), none elsewhere. If text glyphs carried bounds (they do not), up to about 6 more in hub
and mam (13.9 -> 8, about 3.7% of 161) and 8 in the map would be reachable.

**Gaps, not measured or not modelled** (review of `5ee7696`):
- The Ponder index's 73 states differ almost only by **texture**: 72 distinct texture setups per frame for 6 pipelines
  and 1 scissor area (run 40288414898): Create draws each item box from a texture of its own. A batcher that put those
  into one atlas could merge up to about 70 of its 83 GUI draws; that technique (and dropping scissors for quads
  wholly inside them) is not modelled here.
- Heavy GUIs are not in CI: the F3 overlay, a full inventory or creative tab with tooltips, chat history, item-list
  mods.

So no GUI batching code was written (the brief: keep it minimal when vanilla leaves little); the `gui` block stays
(reported, not gated). Andre's decision (2026-10-27): this branch merges as "7a: GUI measurement"; batching is the
"7b" plan below.

### 7b: GUI batching (deferred, not built)

**Deferred (Andre, 2026-10-28): revisit after package 6**, with three heavy GUIs measured first: the inventory with
tooltips, the F3 overlay and a long chat history (scenes for each, vanilla's numbers from the measure runs). Until
then nothing below is built.


Where the GUI pass's draws go, from the `gui` counters (Brimstone and vanilla equal within 0.03 draws per frame; full
profile; whole-frame draws are vanilla's median per frame; with package 8's animated textures Brimstone's whole frames
are smaller, e.g. hud 31 against vanilla's 171, run 40444035980):

| scene | whole frame | GUI pass | elements | states (pipelines / texture setups / scissor areas) | reordered (text as overlapping nothing) |
|---|---|---|---|---|---|
| core scenes, spectator HUD | 121-135 | 1 | 1 | 1 (1 / 1 / 1) | 1 (1) |
| hub | 161 | 13.9 | 572 | 6 (6 / 5 / 1) | 13.9 (8) |
| mam | 162 | 13.9 | 110 | 7 (6 / 6 / 1) | 13.9 (8) |
| map | 170 | 23 | 317 | 12 (5 / 6 / 3) | 21 (15) |
| hud (hotbar with items, open chat, compass, minimap; runs 40436256016, 40444035980) | 171 | 23 | 279 | 11 (6 / 7 / 2) | 23 (13) |
| ponder (Create's Ponder index) | 227 | 82.6 | 203 | 73 (6 / 72 / 1) | 81.6 (75) |

Where batching would actually save draws, roughly:
1. **Per-item textures (Create's Ponder index): about 70 draws per frame.** Its 73 states are 72 texture setups:
   each item box is drawn from a texture of its own. Copying those into one atlas (or drawing them through the game's
   item atlas) would let the boxes merge into a few draws: 82.6 -> about 12 GUI draws, about 30% of that frame's 227.
   The biggest and the only large win measured; it touches Create's (Ponder's) own rendering path, so it needs a
   Ponder-specific hook that falls back to vanilla's path.
2. **Text-aware reordering: up to about 6-10 draws per frame** in screens that alternate panels and text (hud 23 ->
   13, map 21 -> 15, hub and mam 13.9 -> 8). Glyphs carry no bounds today; a batcher would need each text run's
   bounds (from the game's text render state) to move text past panels it does not overlap. About 4-6% of those
   frames.
3. **Plain reordering: 0-2 draws** (map 2, Ponder 1, nothing elsewhere). Not worth code on its own.
4. **Not measured yet:** the F3 overlay (alternating fills and text per line), a full inventory or creative tab with
   tooltips, a long chat history, item-list mods (none in SIFTEC's pack). A 7b would add scenes for these first; they
   are the classic cases where older versions needed a GUI batcher.

On real hardware a GUI draw is cheap next to terrain and entities, so outside Ponder the frame-time gain would be
small; the laptop bench would tell. 7b's done-when would be the doc's: fewer GUI draws per frame against vanilla in
these scenes, every GUI scene's strict picture unchanged (limits 10).

### What was built

1. **`gui` bench block** (above).
2. **CI pins** (every client run, vanilla and Brimstone alike): `--username BrimstoneCI` (the random dev name picked
   the arm's skin and was printed on the HUB and MAM screens), `glintSpeed:0.0` (the MAM tab's glint),
   `menuBackgroundBlurriness:0` (the menu blur behind the Ponder index differs between Vulkan and OpenGL); in the
   SIFTEC CI world: the map scene's area pre-generated, `random_tick_speed 0`, `mob_griefing false`; the map scene's
   player put at a fixed spot (was `spreadplayers`).
3. **Strict limits** (`docs/ci-limits.md`, "GUI scenes, the block outline and Ponder"): hub 12,506 -> **10**, mam
   155,106 -> **10**, map not gated -> **10** (`ciNoStrict` removed), 8/255 limits 8.75 / 9.3 / 11.35 -> **0.25**.
   Samples with the pins: vanilla b/a and a/b 0/0, every Brimstone and measure run 0.
4. **Block outline back in CI:** core scene `outline` (the pin mod draws the crosshair outline round its one stone
   block only) with a `bare` shot in adventure mode (no outline): the outline covers 2,318 pixels; limit 10.
5. **Ponder:** SIFTEC scene `ponder`, Create's Ponder index opened with `/ponder index` typed in chat (drive.py
   `ciChat`); the Ponder scenes themselves keep moving, so the index is what is checked. Limit 10.
6. **HUD scene** (SIFTEC `hud`, after the hologram scene, before create_ct which hides the GUI): normal play in creative
   with nine hotbar items, the chat open with its last 6 lines (eight `tellraw` lines; `chatHeightFocused:0.25` in CI),
   SIFTEC's compass strip and minimap, on a gray concrete platform high above 400 -110 facing a gray concrete wall.
   Pins: the pin mod holds a focused text box's cursor on (`EditBoxCursorMixin`); drive.py clears the chat (F3+D,
   `ciClearChat`) before the setup, so Brimstone and measure runs (whose benches add chat messages) show the same
   scrollbar; drive.py presses a screen's key again every 15 s while a bench waits for it (one measure run's key press
   was lost). Placement: the map scene's markers (doubled in bench runs) stay off the minimap and, facing south, the
   compass; concrete because Vulkan samples a textured wall differently from GL.
   GUI numbers there (Brimstone / vanilla, every profile, runs 40436256016 to 40454752063): 23 / 23 GUI draws per frame,
   279 elements, 11 distinct states: 6 pipelines, 7 texture setups, 2 scissor areas; reordering leaves 23 (13 with
   text as overlapping nothing). Whole frame: Brimstone 31, vanilla 171 (package 8's animated textures). Limits 10
   (strict) and 0.25 (8/255); the strict check sees 44% of this frame (Vulkan shades the wall differently).
7. **Rebased on main after packages 4 and 8** (squashed first; the history is on the local branch
   `package-7-presquash`). Shared CI files hand-merged keeping all three packages' additions; the core `outline`
   scene moved from x 2500 to x 2680 (package 4's flat_plants clears 2483-2517 there, and the pin mod's outline block
   could have been under its crosshair).

### Decisions made on Andre's behalf

- No GUI batcher: reordering wins at most 2 draws/frame (map) and 1 (Ponder index); the texture gap in Ponder and the
  heavy GUIs are open (above).
- The finer limit rule (largest sample x 1.5, rounded up to 10, at least 10) for the GUI scenes and the outline, as a
  wrong glyph covers only a few dozen pixels; all samples were 0 (ponder 0-5).
- 8/255 limits of hub, mam and map re-collected by package 0's rule (0.25), lowered.
- CI world pins (random ticks and mob griefing off, map area pre-generated) and client pins (fixed player name, glint
  speed 0, menu blur 0); each the same for vanilla and Brimstone.
- `outline` gated against vanilla on the median (it failed only on the average at a different frame rate, equal
  medians; Andre's rule). `ponder` is gated on average and median like hub.
- Ponder: the index screen, not a Ponder scene (scenes keep animating); it runs before the map scene and looks up.

### Evidence and runs

**Full run after the review fixes: https://github.com/meakaandre-tech/Brimstone/actions/runs/40297472649 on `64b7865`:
green**; before the review: run 40276630375 on `2801baa`, green, every profile and both suites. Strict counts there: hub, mam, map, ponder, outline, outline.bare
0 in every Brimstone and measure run, vanilla b/a and a/b 0/0; the GUI pass counts identical between Brimstone and
vanilla in every scene.

**Final full run, rebased on main after packages 4 and 8 (with the HUD scene):
https://github.com/meakaandre-tech/Brimstone/actions/runs/40459438787 on `d30b966`: green.** Runs since the rebase:
40436256016 (failed: factory_flat showed the map markers twice), 40444035980 (green), 40450860976 (failed: a hole in
the hud wall in a measure run), 40454752063 (green), 40459438787 (green, final). The branch was rebased once more on
main's docs-only `57a02bc` after it (no code change).

Runs: 40249146134 (measurement + pins: green), 40254531258 (mob griefing off, ponder and outline.bare added: green),
40260765722 (menu blur off: green), 40260847921 (outline.bare in adventure; failed on ponder's provisional limit: the
minimap edge), 40266027605 (limits; failed on ponder (map markers twice in bench runs) and outline's Vulkan average),
40269199055 (ponder moved; failed: no terrain in view), 40273668937 (green), 40276630375 (green, reviewed),
40288414898 (review fixes: green), 40297472649 (green).

### Review

Independent review (fresh agent, of `5ee7696`; `scratchpad/rv7/review-package-7.md`): the Brimstone code only counts,
the counters agree with vanilla's own draw list, the pins apply to every run alike, every limit and baseline went down
or is new. Fixed: B1 (ponder was gated median-only without a frame-rate swing: now average and median, like hub), S1
(ponder's limits and baselines re-derived from the three runs of its final setup), S2 (the batching claim limited to
what was measured, gaps named, `gui` reports distinct pipelines, textures and scissors), S3 (stale docs), N1 (the
per-element hook is gone: the elements are read once per frame and only while a bench measures; outside it nothing is
allocated), N2 (the `gui` note says the menu blur's draws count as GUI draws), N3 (render-thread comment). Recorded,
not changed: N4 (typing in chat can misfire into keybinds; it would fail the scene, never pass it), N5 (the CI world's
gamerules are only in the world-build artifact, not ci-logs), N6 (not tested: the menu blur and the glint's motion),
N7 (hub and mam count baselines are loose from earlier packages: for the lead), N8 (outline flicker, below).

### Known limits

- The old-GPU crosshair-outline row flicker (why the pin exists) did not show in eight runs; if it shows in the
  outline scene, its limit of 10 fails on vanilla noise and must be re-collected (still far below the 2,318-pixel
  outline).
- mam's strict check covers 88-98% of the frame (vanilla runs differ by 1/255 on the sky and floor behind the screen);
  the screen itself is covered.

### Laptop bench (Andre, when back)

Nothing new to bench for speed (no rendering change). The `gui` line of any `/brimstone bench` block shows the GUI
pass's draws per frame.

---

## Package 4b: terrain speed-ups (branch `package-4b`, not merged: Andre reviews it first)

**Done when (doc):** "pixel checks unchanged in every profile; triangles drawn per frame down in every scene (reported);
fly-through frame-time p99 and frames over 50 ms down on the laptop bench, and in CI the largest upload bytes in one
frame down; draw calls at or under vanilla on full and Vulkan (the direction groups are ranges inside the same
multi-draw, not extra draws)." Plus the bench-only jar and a laptop-bench section for vanilla, Sodium and Brimstone.

### What was built (`terrain/BackFaces`, `terrain/UploadBudget`, `CompactVertex`, `TerrainStore.upload`, `bench/BenchModules`)

1. **Skip back faces.** Solid and cutout layers are stored with their quads grouped by facing (unaligned, +X, +Y, +Z,
   -X, -Y, -Z; vanilla's order kept inside each group), classified on the 16-byte grid the GPU draws: a quad is in an
   axis group only if its four corners lie in one axis plane and every corner triangle winds the same way, so any split
   into triangles faces one side; everything else (plants, slopes, rotated parts, degenerate quads) is unaligned and
   always drawn. Each frame the axis groups that face away from the camera for the whole section (camera at least
   1 block, plus twice the camera's move since the last frame, behind the nearest plane of the group) are left out; the
   groups that stay are index ranges of the shared quad index list: on the plain multi-draw paths (old-GPU, the laptop)
   entries of the same multi-draw; on the indirect path (and where a device has no multi-draw) one span per section layer
   from the first kept group to the last, since each extra range there would be one more indirect command uploaded every
   frame (see run 6). Group order unaligned, +X, +Y, +Z, -X, -Z, -Y: bottoms last, so a span trims them most often. Translucent layers are untouched (vanilla's sorted lists). Every group is
   drawn when the pipelines don't cull, without a camera position, after a jump of more than 16 blocks, and with
   `-Dbrimstone.debug.drawAllFaces=true`. The camera position comes from a new optional terrain hook on
   `LevelExtractor.extractVisibleEntities`. Common idea (Sodium does something similar); no Sodium code read or used.
2. **Upload budget.** At the end of each frame staged meshes go into the arenas in priority order: the camera's section
   and its 26 neighbours always (never held back), then sections in vanilla's visible list, then the rest, nearest
   first; a mesh's layers always together; a translucent layer's new index list for a mesh already in at once. Budget:
   64 KiB per ms of measured frame time (average over the last frames), between 256 KiB and 2 MiB, and an eighth of the
   frame time (1-6 ms); the first mesh outside the camera's sections goes in every frame even when it alone is over, so
   nothing waits for ever. What waits stays staged in its order; on an exception everything not yet copied goes back to
   the staged list, so the hand-back completes it through vanilla's callback.
3. **Bench-only jar** `brimstone-bench` (Gradle `benchJar`, `src/bench/fabric.mod.json`, mod id `brimstone_bench`,
   `breaks` Brimstone): the bench command, counters, capability probe and scenes, with no module. The bench's module
   blocks moved into a registry (`bench/BenchModules`; the full jar registers them in `BenchSections`, same JSON keys).
   CI builds it, runs it in two new full-mode core runs (`bench-plain`, `bench-sodium` with Sodium 0.9.2 from Modrinth;
   spawn, flythrough, water_glass, nether), and the release job publishes it as `brimstone-bench-26.3.jar` next to
   `brimstone-26.3.jar` (on the merge to main). `docs/laptop-bench.md`: "Vanilla, Sodium and Brimstone side by side".
4. **Bench and CI.** New bench blocks `backFaces` (triangles per frame drawn and with every face, index ranges per
   section layer) and `uploadBudget` (bytes per frame and the largest frame, budget, sections waiting, upload ms).
   compare.py fails a Brimstone bench whose triangles aren't below the every-face count, or whose budget or camera hook
   is off, and reports a "Package 4b" table per suite and profile and a "Bench-only jar" table. CI shots wait for the
   upload queue: Brimstone runs log when meshes wait and when they caught up (`brimstone.debug.logUploadQueue`) and
   `ci/drive.py` waits for "caught up" before each screenshot (quick run 40588031898 caught one water section still
   waiting after F3+A in the spawn shot, ~1,300 px, under the steady-shot check's 1%); runs without the line don't wait.

5. **Review fixes** (below): a camera position that wasn't updated this frame, vanilla's wireframe view, or index ranges
   longer than the device's multi-draw limit draw every group; a layer where an unaligned quad lies within one grid
   step of an aligned quad's plane keeps vanilla's quad order (`layersKeptInVanillaOrder`); a visible section's mesh
   waits at most 15 frames from when it is first seen visible, then goes in whatever the budget (gated); past 64 MiB
   of staged meshes the budget takes the excess.

### Evidence (full run 40593712100 on `59801d9`, before the review fixes; main for "before": run 40585312798 on `14c74fd`)

Every gate passed in both suites and every profile: pixels (8/255 and strict) within their limits, the same as main
within run noise (strict spawn 296 / 230 / 310 against main's 296 / 250 / 235; flythrough 47 / 47 / 51 against
53 / 53 / 57), draw calls at or under vanilla's measure run in every scene and profile, no limit or baseline changed.

Triangles per frame are Brimstone's terrain triangles (solid and cutout after leaving groups out, plus translucent whole);
in brackets the same frames with every face drawn, which is what Brimstone drew before 4b. Largest upload bytes in one
frame: the frame with the most bytes uploaded of all kinds (the bench's `gpu.uploadBytesPerFrame.max`), before
(main) and after, per profile; vanilla from this run's measure-full. Frames over 50 ms on llvmpipe (5-30 fps) say
little; the laptop bench decides that part of the done-when.

| suite / scene | triangles/frame, full: after (every face, = before) | left out | largest upload bytes in one frame, before -> after (full / old-GPU / Vulkan) | vanilla, full | frames over 50 ms, full: before -> after | sections waiting max |
|---|---|---|---|---|---|---|
| core / animated_items | 2,272 (4,890) | 54% | 79,704 -> 79,704 / 79,236 -> 79,144 / 79,864 -> 79,772 | 123,000 | 0 -> 0 | 0 |
| core / animation | 3,218 (5,632) | 43% | 86,812 -> 86,812 / 86,324 -> 86,324 / 86,812 -> 86,812 | 150,176 | 0 -> 0 | 0 |
| core / flat_plants | 238,881 (437,794) | 45% | 271,768 -> 10,580 / 2,164 -> 1,828 / 10,016 -> 10,468 | 271,600 | 37 -> 13 | 0 |
| core / flythrough | 560,468 (897,108) | 38% | 4,481,644 -> 2,319,320 / 4,227,204 -> 2,107,564 / 4,543,640 -> 2,124,020 | 7,380,064 | 156 -> 175 | 71 |
| core / mobs | 167,241 (302,673) | 45% | 616,348 -> 616,324 / 818,356 -> 610,516 / 616,348 -> 616,324 | 616,364 | 4 -> 0 | 0 |
| core / nether | 267,554 (475,408) | 44% | 261,684 -> 196,016 / 232,000 -> 252,596 / 181,240 -> 251,508 | 384,352 | 134 -> 5 | 0 |
| core / occlusion | 46,235 (81,246) | 43% | 474,624 -> 273,348 / 179,144 -> 448,996 / 100,408 -> 263,164 | 411,048 | 0 -> 0 | 0 |
| core / occlusion_move | 50,639 (89,015) | 43% | 652,736 -> 657,268 / 642,684 -> 647,200 / 652,792 -> 678,628 | 1,157,680 | 0 -> 0 | 0 |
| core / outline | 24,238 (40,423) | 40% | 6,300 -> 6,300 / 5,012 -> 5,012 / 6,300 -> 6,300 | 14,812 | 0 -> 0 | 0 |
| core / particles | 1,927 (4,215) | 54% | 9,204 -> 9,204 / 8,772 -> 8,772 / 9,204 -> 9,204 | 9,204 | 0 -> 0 | 0 |
| core / renderer_off | 150,643 (272,587) | 45% | 7,292 -> 7,352 / 1,380 -> 1,380 / 7,292 -> 7,352 | 7,272 | 2 -> 1 | 0 |
| core / spawn | 446,601 (684,783) | 35% | 687,372 -> 539,848 / 485,284 -> 338,836 / 365,720 -> 540,356 | 1,054,544 | 230 -> 241 | 0 |
| core / translucent_move | 200,787 (356,275) | 44% | 765,976 -> 767,100 / 758,724 -> 759,300 / 820,756 -> 766,948 | 1,333,236 | 34 -> 10 | 0 |
| core / translucent_move_oit | 201,067 (356,581) | 44% | 742,580 -> 748,344 / 738,692 -> 743,300 / 742,524 -> 748,008 | 1,301,072 | 222 -> 217 | 0 |
| core / water_glass | 214,820 (385,714) | 44% | 150,824 -> 17,708 / 8,996 -> 9,892 / 17,464 -> 17,372 | 17,068 | 86 -> 13 | 0 |
| core / water_glass_oit | 215,234 (386,456) | 44% | 17,464 -> 18,156 / 10,004 -> 8,548 / 17,240 -> 19,052 | 16,508 | 241 -> 242 | 0 |
| siftec / create_ct | 2,538 (5,313) | 52% | 1,596 -> 1,596 / 1,124 -> 1,124 / 1,596 -> 1,596 | 1,596 | 1 -> 0 | 0 |
| siftec / factory | 25,071 (45,892) | 45% | 94,716 -> 94,716 / 136,784 -> 136,792 / 139,276 -> 139,368 | 155,244 | 9 -> 8 | 0 |
| siftec / factory_flat | 25,071 (45,891) | 45% | 97,700 -> 97,700 / 139,776 -> 139,776 / 142,352 -> 142,360 | 158,228 | 1 -> 2 | 0 |
| siftec / hologram | 3,912 (6,875) | 43% | 75,664 -> 74,072 / 74,244 -> 75,260 / not run | 75,440 | 35 -> 31 | 0 |
| siftec / hub | 33,473 (58,846) | 43% | 84,940 -> 84,960 / 83,332 -> 83,332 / 84,940 -> 84,960 | 84,940 | 7 -> 7 | 0 |
| siftec / hud | 261,917 (454,024) | 42% | 71,672 -> 81,680 / 59,972 -> 59,972 / 72,776 -> 83,764 | 71,544 | 316 -> 346 | 0 |
| siftec / mam | 66,787 (117,208) | 43% | 304,164 -> 304,276 / 39,492 -> 39,492 / 42,112 -> 42,132 | 304,256 | 0 -> 5 | 0 |
| siftec / map | 368,169 (630,685) | 42% | 46,360 -> 57,876 / 30,924 -> 30,924 / 46,284 -> 57,876 | 46,440 | 211 -> 217 | 0 |
| siftec / ponder | 10 (12) | 17% | 60,840 -> 60,860 / 60,804 -> 60,804 / 60,840 -> 60,860 | 60,840 | 1 -> 4 | 0 |
| siftec / sift | 145,930 (205,045) | 29% | 16,632 -> 20,672 / 4,148 -> 4,148 / 16,632 -> 20,672 | 16,632 | 309 -> 316 | 0 |

- **Triangles:** down 29-54% in every scene and profile (fly-through 37.5%, spawn 34.8%; ponder 10 of 12, the index
  screen in front of almost no terrain).
- **Upload spike:** fly-through's largest frame halves in every profile (full 4.48 -> 2.32 MB, old-GPU 4.23 -> 2.11,
  Vulkan 4.54 -> 2.12; vanilla 7.38 MB); in the other scenes nothing is meshing while measuring, so their largest frames
  are other uploads and stay where they were (flat_plants' and water_glass's 0.27 MB and 0.15 MB before were one-off
  mesh frames).
- **Draw calls:** full and Vulkan at or under vanilla in every scene (fly-through 13.06 against 123.06). Old-GPU against
  main: spawn 49.37 against 47.46, nether 32.59 against 31.08; the rise is all translucent runs breaking at region borders
  (spawn: translucent draws 14 -> 17 with 150 -> 152.7 translucent section layers and 314 -> 344 visible sections), not
  solid or cutout (equal). Main's own runs put old-GPU spawn at 46.0, 47.46, 48.37 and 49.0 (runs 40526712029,
  40585312798, 40471457759, 40516650792), so it is run-to-run noise in what is visible; the review fixes' run is another
  sample (below).
- **Bench-only jar:** both runs passed; `bench-sodium` loaded `sodium@0.9.2+mc26.3` and `brimstone_bench`, every counter
  applied. CI's numbers (llvmpipe): plain spawn 9.3 fps, Sodium 19.1; fly-through 5.6 and 14.8; Sodium's draws are
  counted (129-159 per frame), so it draws through renderpearl.
- The spawn shot's dark-grey water hole (quick run 40588031898, which led to the shot wait) is vanilla's own late
  section: the same hole shows in this run's vanilla-old, measure-full and measure-old and in main's vanilla-a (review
  N8). It was noise, not the budget; the shot wait stays (it is what makes a waiting section impossible to photograph).

### Runs

1. `20e3ef6` quick run 40588031898: green (the spawn hole above in the old-GPU shot, within limits).
2. `59801d9` full run 40593712100: green (reviewed).
3. `b317196` full run 40605316942 (review fixes): every picture, count and 4b gate passed, but compare failed on log
   checks in every Brimstone and measure run's first session (renderer registration, self-test, measure-only lines):
   the run crossed midnight UTC and the game rolled `latest.log` over into a dated `.log.gz`, so the session's early
   lines were not in the collected log (measure runs, which have none of 4b's log lines, failed the same way). CI fix,
   no check changed: `ci/client-run.sh` keeps the session's rolled parts in front of `latest.log`, and `ci/drive.py`
   follows a rolled-over `latest.log`. Its numbers: visible sections waited at most 7 frames (fly-through old-GPU;
   bound 15, nothing forced past the budget), no layer needed the coplanar fallback, fly-through's largest upload frame
   2.21 / 2.11 / 2.13 MB, old-GPU spawn 49.11 draws per frame (median 49; main's spread 46-49).
4. `c23b9ff` full run 40610247676: every picture, count and 4b gate passed; compare failed only on core's block-renderer-off
   check in session 4 of the three Brimstone runs: at start-up the game archives a leftover log into a dated `.log.gz`,
   which the new collection prepended (session 4's file began with session 3's renderer_off log). Fixed in
   `ci/client-run.sh`: a session's log is kept from its own last "Loading Minecraft" line on. No check changed.
5. **`0fe3631` full run 40619534606: green** (every gate, both suites, every profile, both bench-only runs). Fly-through's
   largest upload frame 2.31 / 2.11 / 2.12 MB (main 4.48 / 4.23 / 4.54, vanilla 7.38); triangles per frame down 35-54%
   in the core scenes (fly-through full 552,438 of 884,736); visible sections waited at most 9 frames (bound 15).
   Old-GPU draw calls against main's run 40585312798: spawn 49 against 47 (median), fly-through 66 against 64; the
   fly-through's terrain draws are identical (57.37 per frame) and its totals were 64-68 in main's last runs; spawn's
   +3 are translucent runs breaking at region borders over a larger visible set, and main read 48 and 49 there in runs
   40481430766 and 40516650792. Run-to-run noise in what is visible, not the facing groups.

The commit after `0fe3631` is docs only (this list and the hand-off), `[skip ci]`.
6. **Upload bytes on the indirect path.** The rename branch's run 40624564347 (4b plus only the rename) failed core full
   spawn's upload-bytes gate: median 46,732 bytes per frame against a gate of 41,107 (baseline 22,476). 4b's own green
   run 40619534606 already had 40,776 (full) and 41,492 (Vulkan) there against main's 21,004 and 28,044: a real rise from
   4b, passing by 331 bytes, not noise. Cause: on the indirect path every index range is an indirect command (20 bytes)
   uploaded each frame, about 2 ranges per section layer. Fix (no baseline changed): one span per section layer on the
   indirect path, bottoms ordered last so the span trims them; the plain multi-draw paths keep every range (CPU-side
   arrays, nothing uploaded). Final run: see the hand-off.

### Review (fresh agent, of `59801d9` and run 40593712100; `scratchpad/review-4b.md` in the session)

No blocker. Fixed: S1 (`sectionLayersPerFrame` counted index ranges: counts section layers again), S2 (a camera
position not updated in a frame was still used: now that frame draws every group), S3 (reordering could flip an
equal-depth winner between an unaligned quad lying in an aligned quad's plane and that quad: such layers keep vanilla's
order), S4 (the wait of visible sections was unbounded and unmeasured: bounded at 15 frames, reported and gated; a shot
wait that times out fails), S6 (the report header was written before the bench-only checks), S7 (these docs and
`docs/ci-limits.md`), N1 (wireframe view draws every group; the cull check is against Brimstone's own pipelines), N2
(index ranges longer than the device's multi-draw limit turn grouping off for the session, so draw calls can't rise),
N3 (staged-memory cap), N5, N6, N7, N9. S5 (old-GPU draw calls) answered with main's own spread above. Not changed: N4
(boxed sort keys, once per frame), N10 (BrimstoneClient in the bench jar names classes it lacks; only in the full jar's
entrypoint method, which the bench jar never calls).

### Deviations and choices to confirm

1. **Margin 1 block** (plus twice the camera's move since the last frame; all groups after a jump of 16 blocks).
2. **Budget numbers:** 64 KiB per ms of frame time, 256 KiB to 2 MiB; an eighth of the frame time, 1-6 ms; at most 15
   frames' wait for a visible section; 64 MiB staged cap.
3. **CI shots wait for the upload queue** (Brimstone runs only; vanilla uploads at once). Without it, a section still
   waiting after F3+A could be photographed; with it, the budget's delay shows only in the bench (`uploadBudget`).
4. **Bench-only jar**: `brimstone-bench`, mod id `brimstone_bench`, breaks Brimstone; published on the next merge to main
   (the release job runs on main only). Sodium in CI is downloaded from Modrinth for the run, never committed.
5. **The camera hook is optional** (not in the terrain module's required hooks): without it the module still runs, with
   every group drawn and uploads in staging order; CI fails such a run.

### Known limits

- Done-when on the laptop (fly-through p99 and frames over 50 ms down) is for Andre's bench; CI's llvmpipe frame times are
  too noisy to show it.
- A layer is grouped or not as a whole; the coplanar test is per layer, conservative (no overlap test).
- Grouping by facing is solid and cutout only; translucent keeps vanilla's sorted lists.

### Laptop bench (Andre, when back)

`docs/laptop-bench.md`, "Vanilla, Sodium and Brimstone side by side": the same plain instance three ways (bench-only jar,
bench-only jar plus Sodium 0.9.2, Brimstone), seed 12345, new world each time, `flythrough`, `spawn`, `water_glass`,
`nether`, `mobs`. The fly-through's `frame time ms` (p99, frames over 50 ms) decides 4b's laptop done-when; the
Brimstone instance's `backFaces` and `uploadBudget` lines show the triangles left out and the sections that waited.

---

## Hand-off

Working notes for a new chat picking this up: read this file, then `docs/backend.md`, `docs/ci-limits.md`,
`docs/laptop-bench.md` and the design doc. CI results are on each branch's `ci-logs-<branch>` branch (main's on
`ci-logs-main`; each run overwrites its branch's). Branch pushes run quick mode (core suite, vanilla a and b,
Brimstone old-GPU, about 10 minutes); main, `[full]` commits and manual runs the full matrix (`docs/ci-limits.md`,
"Quick CI"). A package merges only on a green full run, so no branch-only CI commit is needed any more. Local builds aren't possible in the agent workspace (no access to
Fabric's maven or Mojang); everything is built and tested in CI. A full CI run takes about 30-40 minutes (20 client runs; longer when the SIFTEC world cache is rebuilt).
Game class dumps are a 1-day Actions artifact (`game-dumps`) the workspace can't download; ask Andre to attach one if
needed. Scratch tools (not in the repo): a compile check against stubs generated from a javap dump
(`scratchpad/p3/stubgen/check.sh`; it does not cover `ci/pin-mod`), the shader harness (`scratchpad/p3/harness4.py`),
strict-sample scripts (`scratchpad/tools/strict2.py`, `samples.py`).

**Now:** package 4b on branch `package-4b`, green full run 40619534606 on `0fe3631`, not merged (Andre checks it first);
the rename to Quicksilver on its own branch `rename-quicksilver`, on top of 4b. The repo is now
meakaandre-tech/Quicksilver (GitHub redirects the old URL).
Local agent note: client logs are collected per game start (`ci/client-run.sh`), so a run crossing midnight UTC keeps
the rolled-over part.
