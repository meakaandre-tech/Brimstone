# Quicksilver P2 research: Lithium, FerriteCore, ModernFix, C2ME, Krypton (target: Fabric 26.3)

Checked 2026-10-28 against shallow clones of each repo (history deepened, nothing modified), plus clones of
Velocity, FlowSched, night-config, libdeflate, lwjgl3ify and Paper (used only to check their licences).
Tags: **[C: source]** = confirmed by that source. **[L]** = likely, not verified, needs the CI bytecode dump or a measurement.

## 0. Things that change the plan

- **ModernFix has no Fabric build for 26.x.** The `26.1` branch has `enabled_platforms=neoforge` and `minecraft_version=26.1.2`,
  and the Fabric subproject was deleted on 2025-12-26 (commits `9c4da7fa`, `d0fe9d60`). There is no 26.3 branch. [C: repo]
- **FerriteCore stops at 26.1** (build 9.0.0, 2026-03-24). It has no 26.3 branch. [C: `git ls-remote`]
- **Single player runs over an in-memory connection.** Krypton's own prepender mixin returns `LocalFrameEncoder` when
  `local` is true [C: krypton `prepender/ConnectionMixin`]. Vanilla also skips compression and encryption for memory
  connections [L, check `ServerLoginPacketListenerImpl` in the dump]. So the Krypton-type phase gives almost nothing in single player.
- **Lithium 0.26.1 for 26.3 turned `mixin.util.block_tracking` off by default** because it was "causing chunk corruption"
  (`df55a926`, 2026-09-16). That also switches off `world.chunk_ticking.random_block_ticking` and
  `experimental.entity.block_caching.*`, which depend on it. [C: commit + mixin-config.md]
- **Vanilla worldgen is not deterministic run to run** (MC-55596), so a byte-for-byte worldgen diff fails even when comparing
  vanilla with vanilla. [C: C2ME README "Vanilla parity"] Compare per-block state the way C2ME's MIT-licensed
  `tests/world-diff/ComparisonSession.java` does, on single-threaded or pinned regions. Accept only the known noise.
  **Measured in P2-0:** with one worldgen thread (`-Dmax.bg.threads=1`), generation one step at a time over the area,
  and saving only once the light engine is idle, vanilla's runs agree byte for byte (432 chunks core, 432 SIFTEC, 144
  Terralith, three vanilla runs); see docs/ci-limits.md, "Server suite". Threaded generation is not covered by this.

## 1. Licences

| Mod | Main licence (file) | Parts under other licences or third-party code |
|---|---|---|
| Lithium | LGPL-3.0 text in `LICENSE.md`; `fabric.mod.json`: `LGPL-3.0-only` | None found in headers or notices. Note it is "-only", not "or later". |
| FerriteCore | MIT, `LICENSE` ("Copyright (c) 2020 malte0811") | README image is CC BY-SA 3.0. It is only linked, not bundled. |
| ModernFix | LGPL-3.0-or-later, `LICENSE`; mods.toml says "GNU LGPL 3.0" | Config system derived from Sodium (LGPL-3.0, says README). `textures/StbStitcher.java` from GTNH lwjgl3ify (LGPL-3.0 [C: its LICENSE]). `util/TimeFormatter.java` "derived from Guava" (Apache-2.0, compatible one way). `bugfix/paper_chunk_patches/SortedArraySetMixin.java` is marked "Paper start". Paper is GPL-3.0 unless the author is on Paper's MIT list [C: Paper LICENSE.md], so **do not copy it**. |
| C2ME | `LICENSE.md`: MIT (`licenses/LICENSE-MIT.txt`), except `c2me-opts-accel-opencl/`, which is **All Rights Reserved** | That OpenCL module is also commented out in `settings.gradle` and excluded from jar-in-jar. `c2me-opts-natives-math/src/c/includes/FastNoiseLite.h` is MIT (Jordan Peck). `flibc.c` contains musl code (MIT). Submodule FlowSched is MIT [C]. Jar-in-jar: night-config (LGPL-3.0 [C]), asyncutil, exp4j, jctools, caffeine, mixinsquared (licences not checked; usually Apache-2.0 or MIT [L]). All source headers are MIT "Copyright (c) 2021-2026 ishland" (yumi licenser plugin). |
| Krypton | LGPL-3.0, `LICENSE`; `fabric.mod.json`: `LGPL-3.0` | **It bundles `com.velocitypowered:velocity-native:3.4.0-SNAPSHOT` via jar-in-jar, and that library is GPL-3.0** [C: Velocity `LICENSE`, `HEADER.txt`, and the header of `native/.../VelocityCompressor.java`]. Its native `.so` files statically link libdeflate (MIT [C]) and use OpenSSL. The README says Krypton "derives from work done in Velocity and Paper", and `Varint21FrameDecoderMixin` is "optimized packet splitting from Velocity 1.1.0", so GPL-derived. |

What this means for Quicksilver (LGPL-3.0):
- Code from the MIT projects (FerriteCore, C2ME except opencl, FlowSched, FastNoiseLite, musl) can come in if the notices are kept.
- Code from the LGPL projects (Lithium, ModernFix, lwjgl3ify, Sodium-derived) is compatible.
- **Leave out**: velocity-native and any Velocity- or Paper-derived Krypton code (GPL-3.0), the Paper `SortedArraySet` patch, and all of `c2me-opts-accel-opencl` (ARR).
- Network work must be clean-room. Use the JDK's `Deflater`/`Inflater` and `Cipher`, or bind libdeflate (MIT) yourself.

## 2–5. Per mod

### Lithium (develop, 0.26.1, MC 26.3)
**What it optimises** (`lithium-fabric-mixin-config.md`, about 180 options):
- Entity movement and collisions: `entity.collisions.*`, `shapes.*`, `world.raycast`, `entity.fast_retrieval`, `math.*`, `world.inline_*`, `chunk.no_locking`, `chunk.no_validation`, `world.chunk_access`.
- AI: `ai.pathing`, `ai.poi*`, `ai.sensor*`, `ai.task*`, `ai.useless_*`, `entity.inactive_navigations`.
- Block entities and blocks: `block.hopper` (caching, notifications, sleeping), `world.block_entity_ticking.sleeping.*` (furnace, brewing stand, campfire, crafter, shulker, chest anim, sculk), `block.redstone_wire`, `block.fluid.flow`, `block.moving_block_shapes`.
- World ticking: `world.tick_scheduler`, `world.chunk_ticking.*`, `world.explosions.*`, `world.game_events`, `world.combined_heightmap_update`, `world.temperature_cache`.
- Collections and allocations: `collections.*`, `alloc.*`, `cached_hashcode`, `profiler`.
- Chunk data: `chunk.palette` (hash palette), `chunk.serialization` (palette compaction on save), `gen.cached_generator_settings`.
- Client: `client_tick.*`. Experimental (parent off): `experimental.entity.block_caching`, `item_entity_merging`.

**Already done by vanilla, or removed upstream:**
- FluidState caching in BlockState: removed as "added to vanilla in 22w42a". [C: `669b26d8`]
- `entity.fast_hand_swing`: removed in "Update to 26.3-rc-3" (`2cfe4bd2`). It skipped `updateSwingTime` when the entity was not swinging. The commit gives no reason; vanilla 26.3 probably has the same early exit or a changed method. [L]
- `collections.gamerules`: removed in the 1.21.11 update (`9878adc2`). Probably because of vanilla's gamerule rework. [L]
- `collections.fluid_submersion`, `entity.collisions.fluid` and `experimental...fluid_pushing`: all removed in "Partial update to 26.1 snapshot" (`ba089f52`). No reason given; probably a vanilla rewrite of entity–fluid handling. [L]
- `alloc.blockstate`, `alloc.chunk_ticking`, `collections.goals`, `world.explosions.cache_exposure`, `alloc.enum_values.living_entity`: dropped during the 1.21.3 port (`a3351e88`, `62336514`). Reason not stated. [L]
- `experimental...block_touching` and `fire_lava_touching`: removed at 1.21.5 (`b4c2fd8a`, comment in `d0efa5aa`). [C: removed; reason L]
- `ai.task.memory_change_counting`: removed in 26.1 and then put back (`12087670`), so it still matters. [C]
- `gen.chunk_region`: removed as "incompatible", not because vanilla does it (`b7cfd53a`). [C]

**What still matters:**
- Dedicated server and integrated server: everything above helps MSPT, because the integrated server runs the same tick.
  Create factories gain most from hopper and block-entity sleeping, entity collisions, item entities, tick scheduler and redstone.
- Heap: `chunk.palette` helps a little. Start-up: `util.initialization`/`BlockInfoInitializer` adds a small one-off cost.

**Differs from vanilla** (the doc lists these under "Non-vanilla behavior"):
- `ai.poi.fast_portals`: fewer border chunks loaded, so entity cannons can behave differently.
- `entity.framed_maps`: map decoration edge case.
- `minimal_nonvanilla.*`, default on:
  - frog sensor visibility timing
  - `collisions.empty_space` (1e-7 position differences)
  - `spawning` (floating-point summation order)
  - `block_entity_ticking.support_cache` (powered hopper)
  - `expiring_chunk_tickets` (chunk unload order)
- `world.explosions.block_raycast.skip_air`: blocks placed during an explosion survive it, and a floating rail no longer protects the air below it.

All of these fail an exact tick-by-tick world diff and must be gated behind a "vanilla-exact" config flag. Worldgen output is
untouched: `gen.cached_generator_settings` only caches sea level.

**Create compatibility:**
- The `world.raycast` fast path skips Create/Ponder custom `Level`s ("intentionally excludes custom level implementations of
  create / ponder", `BlockGetterMixin`; issue CaffeineMC/lithium#663).
- The Ponder fluid-pushing incompatibility (#622, Create-Fabric #1718) was fixed on Ponder's side (`fb51ae07`).
- Lesson: any block-access shortcut must handle Create's contraption and Ponder virtual levels, by checking `instanceof ServerLevel`/client level.

### FerriteCore (26.1, 9.0.0)
**What it optimises** (`summary.md`, `FerriteConfig`): all memory, few CPU wins.
1. `fastmap` (`replaceNeighborLookup` + `replacePropertyMap`): one shared FastMap per block replaces each state's neighbour
   table and property storage.
2. `blockstatecache` (`blockstateCacheDeduplication`): deduplicates collision and render VoxelShapes, including mod-cached
   shape internals.
3. `datacomponents` (`dataComponentPatch`): empty data-component patches.
4. `threaddetec` (opt-in, "very rare crashes"): smaller ThreadingDetector per PalettedContainer.
5. `compactFastMap` (opt-in).

**Already done by vanilla, or removed upstream:**
- All model-side optimisations were removed at 1.21.5 because "the model system has been rewritten entirely". That covers
  multipart predicates, MRL strings, multipart dedup, quad dedup and model sides. [C: changelog 8.0.0, `372b8d7`; removed
  for good in 26.1 (`db0d76c`)] Whether vanilla's rewrite already deduplicates as well is not verified. [L]
- Vanilla has shrunk state storage twice:
  - 1.20.5: properties went to `Reference2ObjectArrayMap` ("possible because Mojang changed the types", `1d4d67c`).
  - 26.1: properties are now `Comparable<?>[] propertyValues` and neighbours `S[][]` arrays (the 26.1 shadow fields in
    `FastMapStateHolderMixin` against the earlier `Map<Property,S[]> neighbours`). [C: code diff `54eebac`]
  - So the `fastmap` saving is much smaller than the 600+170 MB in summary.md, which was measured on 1.16. Measure it on 26.3. [L]

**What still matters:**
- Heap on both server and client, and the integrated server holds two copies of chunk data in single player.
- With a large modpack (Create adds many states), fastmap and shape dedup are still worth having [L, measure].
- No effect on MSPT or worldgen output; it is a pure representation change.

**Differs from vanilla:** in behaviour, nothing. But `initializeNeighbors` throws `UnsupportedOperationException` for
non-singleton states, and the `populateNeighborTable` fallback option is gone. Any mod reading the vanilla neighbour arrays
directly will crash.

**Create compatibility:** no specific issues found in the history. Grep Create Fly for `neighbours`/`propertyValues` access.

### ModernFix (26.1, NeoForge only)
**What it optimises** (mixin dirs and `en_us.json` option docs):
- **Load and start-up**:
  - `dynamic_resources` (on-demand models)
  - `model_optimizations`, `resourcepacks` (cached pack listings), `dedicated_reload_executor`
  - `dynamic_dfu`, `cache_strongholds`, `memoize_creative_tab_build`, `lazy_search_tree_registry`, `datapack_reload_exceptions`
  - `suspend_integrated_server_during_load`, `thread_priorities` (for weak CPUs)
- **Memory**:
  - `compact_mojang_registries`, `compact_entity_models`, `compact_imposterprotochunks`, `release_protochunks`
  - `deduplicate_wall_shapes`, `attribute_supplier_dedup`, `dynamic_languages`, `dynamic_structure_manager`
  - `compress_unihex_font`, `encoder_cache_leak`
- **Runtime**: `worldgen_allocation`, `ticking_chunk_alloc`, `optimize_surface_rules`, `chunk_meshing`, `tag_id_caching`.
- **Bug fixes**: `world_leaks`, `chunk_deadlock`, `concurrency`, `paper_chunk_patches`, `missing_block_entities`, `unsafe_modded_shape_caches`.
- **Forge only**: `capability_list_compaction`, `forge_registry_alloc`, and some `feature.*`.

**Already done by vanilla, or removed upstream:**
- Structure-locate optimisation removed "as Mojang fixed it in 1.20.5". [C: `a4359e1a`]
- DFU start-up: "Mojang has significantly optimized DFU initialization" since 1.19.4. [C: LazyDFU Modrinth page]
- Spawn chunks were removed by vanilla in 1.21.9 (25w31a). [C: minecraft.wiki Spawn chunk history] So `remove_spawn_chunks`
  is moot; it no longer appears in the 26.1 source, only in the language files.
- Also removed:
  - `state_definition_construct`: "no longer applies to 26.1" (`565fe3d5`)
  - `skip_redundant_saves` (`53349cbd`)
  - `ticking_chunk_alloc` ChunkHolder part: "obsolete in snapshots" (`b2e3ae82`)
  - `dynamic_sounds` (not worth it)
  - `nbt_memory_usage`
  - `deduplicate_climate_parameters` [C: commits]
- The biome-condition optimiser was rewritten "inspired by 26.2 changes", so vanilla 26.2 took a simpler version of it. [C: `a6c03e99`]
- TODO.txt is still open on `cache_strongholds`, `compact_mojang_registries`, `faster_texture_stitching` and the sculk
  deadlock fix ("looks unnecessary"). [C: TODO.txt]

**What still matters:**
- Single player on a laptop: loading time for the client model and resource reload (dynamic resources are the big one for
  Create's models), `suspend_integrated_server_during_load`, `thread_priorities`.
- Dedicated server: `cache_strongholds`, protochunk release, `worldgen_allocation`, leak fixes.
- Everything above is NeoForge code against the 26.1 API, so a Fabric 26.3 version is a port or a rewrite.

**Differs from vanilla:** `cache_strongholds` saves extra data into the world. `release_protochunks` had a worldgen crash
(`494203ef`). `dynamic_resources` changes model lifetime and breaks mods that hold baked models (it has compat patches for CTM,
LDLib, RS and others). Worldgen output: none intended.

**Create compatibility:** a Flywheel BufferBuilder workaround (`5ff2951b`, from the Forge era). `paper_chunk_patches` is
disabled automatically when C2ME is present.

### C2ME (dev/26.3.0)
**What it optimises** (one module each, `settings.gradle`):
- **Chunk system and IO**: `rewrites-chunk-system` (FlowSched scheduler, async serialisation, POI unloading, low-memory mode),
  `rewrites-chunkio`, `opts-chunkio` (NBT cache limits), `rewrites-chunk-serializer` (GC-free serialiser), `opts-scheduling`
  (task ordering, idle and mid-tick tasks, auto-save), `threading-lighting` (ScalableLux).
- **Worldgen CPU**:
  - `opts-math` (Perlin)
  - `opts-natives-math` (SIMD C, restored partially in 26.3, `8f0a0b04`)
  - `opts-worldgen-vanilla` (aquifer, structure-weight sampler, thread-local caches)
  - `opts-worldgen-general` (non-atomic random), `opts-allocs` (NBT, predicates, pooling)
- **Correctness**: `fixes-*-threading-issues` (thread-safety patches and checks), `fixes-worldgen-vanilla-bugs`
  (`ensure_chunk_status_before_callback`).
- **Distance**: `notickvd` (view distance decoupled from simulation), `client-uncapvd`.
- **Disabled in 26.3**: `opts-dfc` (density-function compiler; restored `c02fe8f4`, then disabled again in snapshot-10
  `3981e27f`), `opts-src`, `opts-worldgen-biome-cache`, `accel-opencl`, `threading-scheduling`.

**Already done by vanilla, or removed upstream:**
- The `c2me-threading-worldgen` module was removed at the 1.21 port (`e90a54e8`) and replaced by the chunk-system rewrite,
  which adapted to "new chunk statuses in 1.21-pre2" (`725dddf8`). [C] Vanilla's own 1.21 chunk-status/step refactor [L, no changelog found].
- Lighting: vanilla 1.20 adopted Starlight's core ideas (level propagation and a separate sky light). It is "~2x slower" in
  propagation tests but "fast enough". [C: Spottedleaf gist] C2ME uses ScalableLux only for threading.
- "Use vanilla simulation distance tracking" (`827ed69d`). Mid-tick chunk tasks are off by default from 2026-09 (`186c0b3e`).
- Vanilla still has bugs C2ME fixes: a race in `getNextEntityId` (`615baf86`, 2026-08), MC-276863 ghost mushrooms, and a
  POI memory leak (config comment). [C]

**What still matters:**
- Weak laptop in single player: chunk generation and loading stutter is usually the worst pain while exploring.
- The single-thread wins work even on 2–4 cores: math, allocations, aquifer, the serialiser, IO off the main thread. Threaded
  scheduling gains little with few cores and costs FPS, because the integrated server shares the CPU with the client. [L]
- Dedicated server: big gains for pregeneration and many players.

**Differs from vanilla (flag for the world-diff gate):**
- `chunkSystem.fluidPostProcessingToScheduledTick=true` and `filterFluidPostProcessing=true` by default. Fluid post-processing
  moves to scheduled ticks or is filtered, so the state of freshly generated fluids can differ. **Worldgen-visible.**
- `suppressGhostMushrooms=true` deliberately removes vanilla bug MC-276863 output. **Worldgen-visible.**
- Natives-math Perlin writes `float *output` and `float outputScale` (`exports.c`), so results may not be bit-identical to
  vanilla's double path. [L, diff it] Also the `opts-math` perm-table change (`361361d`).
- Parity has broken before: "worldgen parity issue after 25w45a" (`clampedLerp` argument swap, `dcb8d981`). Every MC update
  needs a fresh diff.
- `notickvd`, `allowPOIUnloading`, `syncPlayerTickets`, auto-save mode and threaded scheduling change timing and order.
  Not output by design, but entity, ticket and unload order differ. `delayFullChunkEvents` is off on Fabric (on would
  "almost certainly break vanilla behavior").
- `fixes.enforceSafeWorldRandomAccess` crashes on purpose when a mod misuses the world random.

**Create compatibility:** nothing specific found. In general, modded worldgen "may cause compatibility issues", and the
aquifer and structure-weight optimisations already ship incompatibility lists (`cavetweaks`, `porting_lib`). Test Create
Fly's ore and worldgen features with the safe-access check on.

### Krypton (master 0.3.2, MC 26.3)
**What it optimises** (`krypton.mixins.json`, 10 mixins):
- Pipeline: libdeflate compression at level 4 (vanilla default is 6) [L for vanilla's level], native OpenSSL AES/CFB8, a
  faster VarInt frame decoder and prepender.
- Micro-optimisations: VarInt sizing, UTF-8 string size, `ServerEntity` empty-passenger list.
- Security: legacy-query and "nullping" drop on inactive channels.
- Netty: allocator `maxOrder=9` (4 MiB arenas instead of 16 MiB), leak detector override.
- Note: `default_config.json` still lists `flusb_consolidation.*` and `pipeline.*_natives`, but no code reads them. [C: grep]

**Already done by vanilla, or removed upstream:**
- In 1.20.2 Krypton "had to axe a bunch of the mixins … most is now (mercifully) redundant with Mojang adding batch
  packets". That removed flush consolidation, chunk-send avoid-work, the entity tracker and the PacketByteBuf micro-optimisations.
  [C: `5fd6a75`]
- Vanilla 1.20.2 added chunk batching by bandwidth and "gameplay packets are now packed into bigger TCP packets". [C: minecraft.net 1.20.2 changelog]
- The CustomPayload memory leak was fixed in vanilla (MC-121884). [C: same changelog]

**What still matters:**
- Dedicated server: CPU per player for compression and encryption, and Netty heap (`maxOrder` saves about 12 MiB per arena).
- Single player: about zero. Local connection, no compression or encryption [L for compression/encryption, C for framing].

**Differs from vanilla:** compression level 4 changes the compressed bytes on the wire, but the decompressed content is
identical. No world effect.

**Create compatibility:** none known. Large contraption packets are fine; Krypton added a property for oversized packets (`5fb9eba`).

## Recommended order for P2-1..P2-6

Ranked by payoff on a weak laptop in single player (integrated server and client share 2–4 cores and a small heap) and by
how likely each phase is to break vanilla parity:

| New | Was | Phase | Why |
|---|---|---|---|
| P2-1 | P2-1 | Memory (FerriteCore-type) | **Keep first.** Low risk, purely a representation change, helps heap on both sides in single player. Do fastmap, shape dedup and data-component patches; leave out `threaddetec`. Measure first: vanilla 26.1 shrank state storage, so the gain is smaller than summary.md claims. |
| P2-2 | P2-3 | Logic I: movement, collisions, block and chunk access (Lithium) | **Move up.** This is the biggest MSPT payoff for the integrated server and the base Logic II builds on. Ship only vanilla-exact parts by default; `minimal_nonvanilla` and `skip_air` go behind a flag. Do not port `block_tracking` until Lithium's 26.3 corruption bug is understood. Keep the Create/Ponder custom-level exclusion. |
| P2-3 | P2-4 | Logic II: AI, pathfinding, hoppers, block-entity sleeping (Lithium) | **Move up.** Create factories run on hoppers, funnels, furnaces and item entities, so this is the main MSPT win for this pack. Medium risk: comparator and wake-up bugs keep appearing upstream (`c6f8e3cf`, `e8c6b397`), so each one needs gametests. |
| P2-4 | P2-6 (part) | Chunks A: IO and serialisation off the main thread, allocation and math optimisations that keep output identical (C2ME-type) | **Split C2ME and do the safe half earlier.** Chunk stutter while exploring is the worst pain on a weak laptop, and these parts don't need many cores. Gate each optimisation on a per-block world diff (C2ME's MIT `world-diff` approach) on fixed seeds. Skip natives and fluid post-processing changes. |
| P2-5 | P2-2 | Start-up and loading (ModernFix-type) | **Move down.** There is no Fabric 26.x upstream to port, so it is original work. Vanilla already fixed DFU, structure locate and spawn chunks, and the 1.21.5 model rewrite made the old model hacks obsolete. Profile 26.3 + Create Fly start-up first, then pick the 2–3 measured hotspots (likely resource and model reload, `suspend_integrated_server_during_load`, `thread_priorities`). One-off cost, so lower value than MSPT. |
| P2-6 | P2-5 + P2-6 (rest) | Network (Krypton-type) **and** Chunks B: threaded scheduling and generation | **Last.** Network does almost nothing in single player and its best upstream code is GPL (velocity-native), so it needs a clean-room rewrite and only helps dedicated servers. Threaded generation and scheduling carry the most risk to worldgen order and parity, gain least on 2–4 cores, and can be skipped if A is enough. |

If the design must keep exactly six one-topic phases, the minimum change is to swap the old P2-2 (start-up) behind both Lithium
phases and move Network to the end, after Chunks. Keep Chunks last within its own phase.

Before P2-2, build a "vanilla-exact" CI gate:
- a world diff (block states, block entities, entities)
- tick-replay gametests
- the bytecode dump to settle the [L] items above.

## Sources
- Repos as cloned (commit hashes above); Velocity, FlowSched, night-config, libdeflate, Paper, lwjgl3ify licence files.
- [Minecraft 1.20.2 changelog](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-20-2)
- [Spottedleaf, Starlight 1.20 gist](https://gist.github.com/Spottedleaf/6cc1acdd03a9b7ac34699bf5e8f1b85c)
- [LazyDFU](https://modrinth.com/mod/lazydfu)
- [Spawn chunk history, minecraft.wiki mirror](https://wiki.ronlab.site/content/minecraftwiki_en_all_maxi_2025-11/Spawn_chunk)
- Modrinth API was blocked by the proxy, so release lists on Modrinth were not checked.
