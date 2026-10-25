# 26.3 terrain pipeline (research for Brimstone's terrain renderer)

Sources: `dump/javap-all.txt` (signatures), `dump/bytecode2.txt` (bytecode; line numbers below are from this file),
Brimstone CI client logs (`scratchpad/cilogs*`). **Fact** = read from bytecode/signatures/logs. **Guess** = inferred,
marked as such.

## 0. What the bytecode dump does not cover

`.github/dump-bytecode` uses `chunk\.ChunkSectionsToRender$`, so the nested classes
`ChunkSectionsToRender$DrawIndirect` / `$DrawSeparate` have **no bytecode** (their `render(...)` bodies are unknown).
Also missing (signatures only): `DynamicGpuData*`, `RenderPipelines`, `DefaultVertexFormat`, `SectionCompiler`,
`CompiledSectionMesh`, `ChunkSectionLayer(Group)`, `UberGpuBuffer`, `StagingBuffer`, `BufferBuilder`, `MeshData`,
`LevelExtractor`, `ModelBlockRenderer`, `BlockModelLighter`, `RenderPipeline` static inits. Static-init values
(vertex format layouts, pipeline shader paths, semantic name strings, enum constructor args) are therefore unknown.
Suggested regex additions are in section 11.

## 1. Meshing

**Classes and flow (fact):**

- `LevelRenderer.render(...)` ends each frame with `compileSections(CameraRenderState)` (bytecode 88612), then
  `SectionRenderDispatcher.uploadTerrainBuffersToGpu()` under `lock()` (88836-88855, profiler "uploadTerrainBuffers"),
  then `SectionOcclusionGraph.update(...)` (88655). So these run **after** that frame's frame graph has executed.
- `LevelRenderer.compileSections` walks a list of `state.level.SectionUpdateRenderState(long sectionNode, boolean
  playerChanged, RenderSectionRegion region)`. For each one it looks up `ViewArea.getRenderSection(long)` and calls
  either `RenderSection.compileSync(region)` (when `PrioritizeChunkUpdates` is NEARBY and distSqr < 768, or
  PLAYER_AFFECTED and `playerChanged`) or `RenderSection.compileAsync(region)`. Then it calls
  `scheduleTranslucentSectionResort(camPos)`.
- `RenderSection.compileAsync` → `createCompileTask` (cancels the previous tasks; `isRecompile` = mesh != UNCOMPILED) →
  `SectionRenderDispatcher.schedule(task)`, which does `queue.add(task)` (`SectionTaskDynamicQueue`, synchronized) and
  `executor.execute(this::runTask)`.
- **Executor:** `SectionRenderDispatcher` is built in `LevelRenderer.invalidateCompiledGeometry` with
  `Util.backgroundExecutor()` (a `TracingExecutor`; "Worker-Main-N" threads in logs), `RenderBuffers`, a new
  `SectionCompiler(ao, cutoutLeaves, ModelManager.getBlockStateModelSet(), getFluidStateModelSet(), blockColors)`
  and `SectionOcclusionGraph::schedulePropagationFrom` as `onSectionMeshUpdate` (91303-91328). Later reloads call
  `setCompiler(SectionCompiler)`.
- `runTask()` (worker): `queue.poll(cameraPosition)` → `bufferPool.acquire()` (`SectionBufferBuilderPool`, one
  `SectionBufferBuilderPack` = one `ByteBufferBuilder` per `ChunkSectionLayer`) → `task.doTask(pack)` → `clearAll()`
  or `discardAll()` → `release`, then reschedules itself. `compileSync` instead uses `fixedBuffers` on the render thread.
- `RenderSection$CompileTask.doTask(SectionBufferBuilderPack)` (94661ff): profiler zone "Compile Section" →
  `SectionCompiler.compile(SectionPos, RenderSectionRegion, VertexSorting, SectionBufferBuilderPack)` with
  `createVertexSorting(sectionPos, cameraPos)` = `VertexSorting.byDistance(cam - sectionMinBlock)` → wraps the result in
  `new CompiledSectionMesh(TranslucencyPointOfView.of(cam, node), results, startTimeNs)`.
  - If `results.renderedLayers` is empty, it sets `CompiledSectionMesh.EMPTY` (or the compiled mesh when there are
    block entities) at once.
  - Otherwise, for each `(ChunkSectionLayer, MeshData)` it calls `addSectionBuffersToUberBuffer(layer, mesh,
    meshData.vertexBuffer(), meshData.indexBuffer())` and **spins** (`Thread.onSpinWait`) while that returns false
    on a non-render thread (staging buffer full), then `MeshData.close()`.
- **Region data:** `RenderRegionCache.createRegion(ClientLevel, long)` → `RenderSectionRegion` (a
  `BlockAndTintGetter` over `SectionCopy[]`: copies of `PalettedContainer<BlockState>` plus block entities, plus
  `LevelLightEngine` and `CardinalLighting`). Who builds the regions: probably `LevelExtractor` during extraction
  (guess, since `SectionUpdateRenderState` carries the region).
- **`SectionCompiler`** (signatures): `compile(...)` returns `SectionCompiler$Results { List<BlockEntity>
  blockEntities; Map<ChunkSectionLayer, MeshData> renderedLayers; VisibilitySet visibilitySet; MeshData$SortState
  transparencyState; }`. `getOrBeginLayer(Map<ChunkSectionLayer,BufferBuilder>, pack, layer)` lazily starts a
  `BufferBuilder(pack.buffer(layer), QUADS(guess), layer.vertexFormat())`. Lambdas `lambda$compile$0/1(Map, pack,
  float x, float y, float z, BakedQuad, QuadInstance)` are the `BlockQuadOutput` sinks for `ModelBlockRenderer`, and
  `lambda$compile$2(...) -> VertexConsumer` is the `FluidRenderer$Output`. `VisGraph` builds the face-to-face
  `VisibilitySet`.
- **Output:** per layer, one `MeshData` (`ByteBufferBuilder$Result` vertex bytes + optional index bytes +
  `DrawState(VertexFormat, vertexCount, indexCount, PrimitiveTopology, IndexType)`). For translucent geometry,
  `MeshData.sortQuads(ByteBufferBuilder, VertexSorting)` gives a `SortState(CompactVectorArray centroids, IndexType)`
  stored in `Results.transparencyState` and gives the mesh its own index buffer (guess for the exact call site; the
  `SectionDraw.hasCustomIndexBuffer` flag and `ResortTransparencyTask` prove that translucent has its own indices).
- **Layers (fact):** `net.minecraft.client.renderer.chunk.ChunkSectionLayer` enum `SOLID`, `CUTOUT`, `TRANSLUCENT`;
  ctor `(RenderPipeline pipeline, RenderPipeline multiDrawPipeline, int bufferSize, boolean translucent)`;
  `pipeline(boolean multiDraw)`, `vertexFormat()`, `byTransparency(Transparency)`. `ChunkSectionLayerGroup` enum
  `OPAQUE`, `TRANSLUCENT` with `layers()` (guess: OPAQUE = {SOLID, CUTOUT}, TRANSLUCENT = {TRANSLUCENT}).
- **Mesh swap:** a `CompiledSectionMesh` only becomes the `RenderSection.sectionMesh` once every layer's vertex and
  index uploads have finished: the upload callbacks → `checkSectionMesh` → `setSectionMesh` (fires
  `onSectionMeshUpdate`, sets the fade-in `uploadedTime`) and `releaseSectionMesh(old)` (closes the old mesh and does
  `UberGpuBuffer.removeAllocation(old)` for every layer).

## 2. Vertex and index format

- **Fact:** a terrain layer's vertex format is `ChunkSectionLayer.vertexFormat()`, which the code reads back as
  `layer.pipeline(false).getVertexFormatBinding(0)` (SectionRenderDispatcher `lambda$new$0`, extractSectionDrawGroups).
  `DefaultVertexFormat` has `BLOCK` and `CHUNK_DATA_INSTANCED` plus semantic-name constants (`POSITION`, `COLOR`,
  `UV0`, `UV1`, `UV2`, `UV3`, `NORMAL`, `LINE_WIDTH`, `CHUNK_POSITION`, `CHUNK_VISIBILITY`). `BufferBuilder` has
  `blockFormat`/`entityFormat` fast paths and `putPackedUv`, `uvShort`, `putNormals`.
- **Byte size: unknown from the dump.** Guess: terrain uses `DefaultVertexFormat.BLOCK`. The `TerrainUniform` block
  carries the atlas width/height (`DynamicGpuData$TerrainTransform(Matrix4fc modelView, int atlasW, int atlasH)`), which
  suggests UV0 is stored as packed integer texel coordinates (`putPackedUv`) and not as 2×float. Get the real layout at
  runtime: `RenderPipelines.SOLID_TERRAIN.getVertexFormatBinding(0).getElements()` / `.getVertexSize()`
  (`VertexFormatElement(name, offset, GpuFormat)`). The uber vertex heap is aligned to `getVertexSize()` (below).
- **Index (fact):** SOLID/CUTOUT have no index data of their own (`SectionDraw.hasCustomIndexBuffer == false`). They
  draw with the **shared sequential quad index buffer** `RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS)`
  (`RenderSystem$AutoStorageIndexBuffer`, starts as `IndexType.SHORT`, grows via `IndexType.least(n)`, log "Growing
  IndexBuffer: Old limit {}, new limit {}."). `prepareChunkRenders*` call `requestIndexCount(maxIndices)` with the
  largest per-section `indexCount`, and every draw uses `firstIndex = 0` with `baseVertex = vertexOffset /
  vertexSize`. TRANSLUCENT has its own sorted index buffer in the per-layer index uber buffer, with `firstIndex =
  indexOffset / indexType.bytes`.

## 3. Upload and buffers: vanilla already uses big shared buffers

- **Fact:** `SectionRenderDispatcher` ctor (93532ff): `StagingBuffer.create("Chunk", device, 102760448)` (98 MiB;
  `StagingBuffer$PersistentlyMapped` on a `MappableRingBuffer`, or `$Cpu`), and per `ChunkSectionLayer` a
  `SectionUberBuffers(vertex, index)`:
  - `new UberGpuBuffer<SectionMesh>(label, 32 /*USAGE_VERTEX*/, 134217728 /*128 MiB heap*/, vertexSize, staging)`
  - `new UberGpuBuffer<SectionMesh>(label, 64 /*USAGE_INDEX*/, 33554432 /*32 MiB heap*/, 8, staging)`
- `UberGpuBuffer` is a list of heaps (`Pair<TlsfAllocator, UberGpuBufferHeap>`); each heap is one `GpuBuffer`. It uses
  **TLSF suballocation** keyed by the `SectionMesh` object: `addAllocation(key, UploadCallback, ByteBuffer)` stages
  the bytes (`StagingBuffer.tryAppend`), and `uploadStagedAllocations(GpuDevice, StagingBuffer$Uploader)` on the render
  thread copies them (`StagingBuffer.copyTo(CommandEncoder, GpuBuffer, ...)`) and runs the callbacks.
  `getAllocation(key)`, `getGpuBuffer(Allocation)`, `removeAllocation(key)`. There is no per-section `GpuBuffer`.
- `SectionRenderDispatcher.getRenderSectionSlice(SectionMesh, ChunkSectionLayer)` → `RenderSectionBufferSlice(GpuBuffer
  vertexBuffer, long vertexOffset, GpuBuffer indexBuffer, long indexOffset)`. Returns null if the mesh isn't allocated.
- **The log lines (fact from CI logs, mapping to fields is a guess):** they come from
  `DynamicGpuDataStorageMapped.resizeBuffers(int)` / `DynamicGpuDataStorageNonMapped.resizeBuffers(int)` (both have a
  `LOGGER` and `lambda$resizeBuffers$0` building the message from `label`). These are **per-frame ring buffers**
  (`MappableRingBuffer`: N `GpuBuffer`s + `GpuFence`s) owned by `DynamicGpuData` (`RenderSystem.getDynamicUniforms()`),
  and they double when one frame writes past capacity. They hold only per-frame draw data, not meshes:
  - "Chunk Sections UBO": `DynamicGpuData.chunkSections` in UBO mode (`writeChunkSections(ChunkSectionInfo...)` →
    `GpuBufferSlice[]`, one 16-byte-ish slice per section). Seen only in the `*-old` CI profile (GL 3.3, no MDI).
  - "Chunk Sections Instanced": the same `chunkSections` field (it is non-final `DynamicGpuDataStorage`) in vertex mode,
    via `writeChunkSectionsInstanced(List<ChunkSectionInfo>)` → one slice used as an instanced vertex stream.
  - "Chunk Sections Command Buffer": `chunkSectionsCommandBuffer` (`DynamicGpuDataStorageMapped<IndexedDraw>`), via
    `writeChunkSectionCommands(List<List<IndexedDraw>>)` → one indirect-args slice per draw group.
    `IndexedDraw(indexCount, instanceCount, firstIndex, baseVertex, baseInstance)` = 20 bytes
    (`RenderPass.INDIRECT_INDEXED_DRAW_SIZE = 20`).
  - "Dynamic Transforms UBO" is `transforms` (entities etc.).
- **So yes: vanilla 26.3 already has the large-shared-buffer + multi-draw-indirect path** for terrain.

## 4. Per-frame drawing

**Choosing the path (fact, 88225-88330 and 88609-88689):**
```
LevelRenderer.<init>: multiDrawIndirectAvailable =
    limits.maxDrawIndirectDrawCount() > 0 && features.nonZeroFirstInstance()
    && !hintsAndWorkarounds.multiDrawIndirectHasKnownIssues();
LevelRenderer.render: usingMultiDrawIndirectForTerrain =
    multiDrawIndirectAvailable && levelRenderState.shouldUseMultiDrawIndirectForTerrain;
chunks = using ? prepareChunkRendersIndirect(viewRot, !useImprovedTransparency)
               : prepareChunkRenders(viewRot, !useImprovedTransparency);
addMainPass(frameGraph, preparedFrame, fog, chunks, ...)
```
- `DeviceFeatures.multiDrawIndirect` itself is **not** read by LevelRenderer. It matters only through
  `maxDrawIndirectDrawCount`: GL `GlHeuristics.createDeviceInfo` sets it to `Integer.MAX_VALUE` with
  `GL_ARB_multi_draw_indirect`, `1` with only `GL_ARB_draw_indirect`, else `0`. `FrontendRenderPass.drawIndexedIndirect`
  throws "drawCount must be one when device does not support multiDrawIndirect". Open question: how
  `DrawIndirect.render` copes with draw_indirect but no multi_draw_indirect (no bytecode).
- `LevelRenderState.shouldUseMultiDrawIndirectForTerrain`: probably copied from the debug toggle
  `Minecraft.multiDrawIndirect` (public, `true` in the ctor) by `LevelExtractor` (guess; that class isn't dumped).
  `LevelRenderer.isChunkRenderingUsingMultiDrawIndirect()` exposes the result.
- GL features (fact, ctor order `wireframeFillMode, shaderDrawParameters, multiDrawDirectInterleaved,
  multiDrawDirectSeparate, multiDrawIndirect, drawIndirect, nonZeroFirstInstance, persistentMapping`):
  GL = `(true, ext(shader_draw_parameters), false, true, ext(MDI), ext(draw_indirect), ext(base_instance),
  ext(buffer_storage))`; Vulkan = `(wireframe set, true, MULTI_DRAW set, false, true, true, true, true)`.
  So **GL always has `multiDrawDirectSeparate`** (`glMultiDrawElementsBaseVertex`, core 3.2), and Vulkan never does.

**Building draws, `LevelRenderer.extractSectionDrawGroups(boolean sortTranslucent, List<ChunkSectionInfo> out,
Map<ChunkSectionLayer, List<ChunkDrawGroup>>)` (89915ff, fact):** under `SectionRenderDispatcher.lock()`, for every
`RenderSection` in `visibleSections` and every layer: `SectionMesh.getSectionDraw(layer)` +
`getRenderSectionSlice(mesh, layer)`. The section gets one `ChunkSectionInfo(originX, originY, originZ,
getVisibility(nowMs, fadeInMs))` (its index in `out` becomes `baseInstance`). Draws are grouped into
`LevelRenderer$ChunkDrawGroup(GpuBufferSlice vertexBuffer, GpuBufferSlice indexBuffer|null, IndexType|null,
List<IndexedDraw>)` keyed by hash(vertex GpuBuffer, index GpuBuffer, IndexType), which in practice means one group
per (layer, uber heap). For a sorted translucent layer, only consecutive sections merge, which keeps distance order.
The method returns the max shared-index count. `IndexedDraw(indexCount, 1, firstIndex, baseVertex, baseInstance)`.

**Path A: `prepareChunkRendersIndirect` → `ChunkSectionsToRender$DrawIndirect` (fact):**
`writeChunkSectionCommands(all groups' draws)` → one indirect slice per group →
`GpuMultiDrawIndexedIndirect(vertexBuffer, indexBuffer, indexType, indirectCommandBuffer, drawCount)`;
`writeTerrainTransform(viewRot, atlasW, atlasH)`; `writeChunkSectionsInstanced(infos)`. Translucent group and draw
lists are reversed when sorting (back to front). Guess for `DrawIndirect.render`: it binds slot 0 = uber vertex heap,
slot 1 = the instanced `ChunkSectionInfo` stream (`CHUNK_DATA_INSTANCED`, step rate per instance; `baseInstance`
picks the section, which is why `nonZeroFirstInstance` is required), sets the index buffer (group's or the shared one),
`setPipeline(layer.multiDrawPipeline)` and calls `drawIndexedIndirect(slice, drawCount)` once per group. **That is
about 1 draw call per layer per 128 MiB heap.** GL backend: `glMultiDrawElementsIndirect` / Vulkan
`vkCmdDrawIndexedIndirect` (bytecode 27068 / 51546).

**Path B: `prepareChunkRenders` → `ChunkSectionsToRender$DrawSeparate` (fact):**
`writeChunkSections(infos[])` → `GpuBufferSlice[]` (one UBO slice per section). One
`RenderPass$Draw<GpuBufferSlice[]>(slot 0, vertexGpuBuffer, indexGpuBuffer|null, indexType, firstIndex, indexCount,
baseVertex, (arr, uploader) -> uploader.setUniform("ChunkSection", arr[baseInstance]))` per section per layer
(`lambda$prepareChunkRenders$2`, 91781). Guess for `DrawSeparate.render`: `pass.drawMultipleIndexed(draws,
sharedIndexBuffer, sharedType, List.of("ChunkSection"), chunkSectionInfos)` with `layer.pipeline(false)`.
`FrontendRenderPass.drawMultipleIndexed` runs the uniform consumer, rebinds the vertex/index buffer if needed and
makes **one `RenderPassBackend.drawIndexed` per section per layer** (fact, 61422-61460). This is the path used on
Intel iGPUs (`multiDrawIndirectHasKnownIssues`), GL 3.3 without the extensions, and devices without base instance.

**Common part, `ChunkSectionsToRender.renderLayers(...)` (fact, 93450ff):**
`pass.setUniform("TerrainUniform", terrainTransformUBO)`, `setUniform("Sampler0", blockAtlasView, chunkLayerSampler)`,
`setUniform("Sampler2", GameRenderer.lightmap(), SamplerCache.getClampToEdge(LINEAR))`, then for each layer
`pushDebugGroup(label)` → `render(layer, pass, sharedIndexBuffer|null, sharedIndexType|null, wireframePipeline,
wireframeMultidrawPipeline)` → `popDebugGroup`.
- `renderGroup(ChunkSectionLayerGroup, RenderPass, GpuSampler, GpuTextureView atlas, boolean wireframe)` passes
  `RenderPipelines.WIREFRAME` / `WIREFRAME_MULTIDRAW` as overrides when wireframe is on.
- `renderOit(GpuSampler, OitStage, OitRenderPassProvider$Parameters, GpuTextureView atlas, GpuTextureView lightmap)`
  makes its own pass (`OitRenderPassProvider.createRenderPass`, label "Terrain") and uses
  `RenderPipelines.OIT_TERRAIN` / `OIT_TERRAIN_MULTIDRAW` (`OitPipelineSet.getPipeline(stage)`), for the TRANSLUCENT group.

**Where it's called (fact):** `lambda$addMainPass$0` creates the main `RenderPass` (main colour + depth),
`RenderSystem.bindDefaultUniforms(pass)` (`Projection`, `Fog`, `Globals`, `Lighting`), then
`executeSolid(chunks, frame, pass)` → `chunks.renderGroup(OPAQUE, pass, chunkLayerSampler, atlas, wireframe)`
(profiler "solidTerrain"), then the solid features. Next comes either `executeClassicTransparency` (same pass;
`renderGroup(TRANSLUCENT, ...)`, "translucentTerrain") or, with Improved Transparency on, `executeOit(chunks, frame)`
(separate passes per `OitStage` DEPTH_BOUNDS / TRANSMITTANCE / ACCUMULATE → `chunks.renderOit(...)`).
No push constants are used for terrain (no `pushConstants` call on this path).

## 5. Visibility

- `SectionOcclusionGraph` (BFS over `RenderSection`s through `VisibilitySet`s): `update(CameraRenderState, fov,
  ChunkLoadingRenderState)` runs on the render thread at the end of `LevelRenderer.render`. A full rebuild
  (`scheduleFullUpdate`) runs as `CompletableFuture.runAsync(..., Util.backgroundExecutor())`; otherwise
  `runPartialUpdate` uses the `nextSectionsToPropagateFrom` queue (filled by `schedulePropagationFrom` when a mesh
  is swapped in). The result is an `Octree` in `GraphState.storage()`.
- Frustum: `SectionOcclusionGraph.addSectionsInFrustum(Frustum, List<RenderSection> visible, List<RenderSection>
  nearby)` → `Octree.visitNodes(visitor, offsetFrustum(frustum), int)`; `consumeFrustumUpdate()` flags when it must be
  redone. The lists are `LevelRenderer.visibleSections` / `nearbyVisibleSections` (public getters
  `visibleSections()`, `nearbyVisibleSections()`). Guess: the caller is `LevelExtractor.applyFrustum(Frustum)`
  (private, in the extract phase); `Camera.prepareCullFrustum` builds the frustum.
- `extractSectionDrawGroups` is the only consumer for drawing. There is no GPU culling in vanilla.

## 6. Translucent sorting

- Trigger (fact): `LevelRenderer.compileSections` → `scheduleTranslucentSectionResort(Vec3 cam)` (render thread, after
  each frame's draw). It does nothing unless the camera's block position changed. It always checks
  `nearbyVisibleSections`, then `max(size/8, 15)`-ish sections of `visibleSections` per frame (round-robin; guess).
  `scheduleResort(section, pov, cam, ...)` calls `RenderSection.resortTransparency()` when the
  `TranslucencyPointOfView` changed (or is axis-aligned and forced), nothing is pending, the section has translucent
  geometry, and **`!GameRenderer.useImprovedTransparency()`**. With OIT on, there is no CPU sorting at all.
- Work (fact): a `RenderSection$ResortTransparencyTask` goes on the same worker queue. It runs
  `MeshData$SortState.buildSortedIndexBuffer(pack.buffer(TRANSLUCENT), VertexSorting.byDistance(cam - origin))` and
  re-uploads **only the index buffer** via `addSectionBuffersToUberBuffer(TRANSLUCENT, mesh, null, indices)`, then
  `setTranslucencyPointOfView`. The initial sort happens at compile time, by the camera position at task start.

### 6a. Translucent details for package 3 (fact, from `dump/bytecode3.txt`)

- **Every translucent layer has its own sorted index list.** `SectionCompiler.compile` calls `MeshData.sortQuads` for
  the TRANSLUCENT layer whenever it has geometry; the quads are sorted by the camera position at compile time
  (`RenderSection.createVertexSorting(SectionPos, Vec3)` = `VertexSorting.byDistance(cam - sectionMin)` as floats).
- **The sort:** quad centre = midpoint of the quad's first and third corner (`MeshData.decodeQuadCentroids`, floats
  in a `CompactVectorArray`); key = squared distance to the camera; `IntArrays.mergeSort` (stable) with the comparator
  reversed, so far first and equal distances keep the order the quads were built in. `SortState.buildSortedIndexBuffer`
  writes 6 indices per quad in that order.
- **Resorting:** `ResortTransparencyTask.doTask` (on vanilla's worker queue) skips unless the mesh's
  `TranslucencyPointOfView` changed (or is axis-aligned), then builds the new list and hands it to
  `addSectionBuffersToUberBuffer(TRANSLUCENT, mesh, null, indices)` (vertices `null` = index list only), spinning
  while that returns false, then sets the mesh's point of view. The bytes are copied into vanilla's staging buffer at
  once (the task closes its buffer right after) and uploaded in `uploadTerrainBuffersToGpu` at the end of the frame.
  The index callback with only indices sets the "index uploaded" flag and does not re-check the mesh.
- **`addSectionBuffersToUberBuffer`** takes vanilla's `copyLock` itself (an injection at HEAD runs before it).
- **Draw order (`extractSectionDrawGroups` + `prepareChunkRenders*`):** with `sortTranslucent` (Improved Transparency
  off) a translucent draw only joins the previous group if it is the same (buffers, index type), and both the group
  list and each group's draws are reversed: translucent is drawn in exactly the reverse of `visibleSections`. Without
  it (Improved Transparency on) groups are keyed by a hash of (vertex buffer, index buffer, index type) and drawn in
  order of first appearance; with one translucent heap (128 MiB) that is `visibleSections` order.
- **Improved Transparency:** `executeOit` calls `ChunkSectionsToRender.renderOit` once per `OitStage` (DEPTH_BOUNDS,
  TRANSMITTANCE, ACCUMULATE), each in its own pass from `OitRenderPassProvider.createRenderPass`; `renderOit` runs the
  same `renderLayers` for the TRANSLUCENT group and passes `OIT_TERRAIN.getPipeline(stage)` and
  `OIT_TERRAIN_MULTIDRAW.getPipeline(stage)` in the slots the wireframe view uses. These pipelines use the game's
  `core/terrain` shaders with OIT defines; with `OIT_ALPHA_ONLY` (depth bounds, transmittance) the vertex stage skips
  the lightmap (`vertexColor = Color`) and no `Sampler2` is bound. `GameRenderer.useImprovedTransparency()` = the
  option and not wireframe. The option is `improvedTransparency` in options.txt (default false).
- **Classic translucent pipeline:** `TRANSLUCENT_TERRAIN(_MULTIDRAW)` = terrain snippet + `BlendFunction.TRANSLUCENT`
  + `ALPHA_CUTOUT 0.1`, default depth state.

## 7. Lighting, AO, tint: all on the CPU at mesh time

- `ModelBlockRenderer(boolean ao, boolean cull, BlockColors)`: `tesselateBlock(BlockQuadOutput, x, y, z,
  BlockAndTintGetter, BlockPos, BlockState, BlockStateModel, long seed)` → `tesselateAmbientOcclusion` /
  `tesselateFlat` → `putQuadWithTint` (tint via `BlockColors`/`BlockTintSource`, cached per block).
- AO and light: `BlockModelLighter` (`prepareQuadAmbientOcclusion(...)`, `prepareQuadFlat(..., int light, ...)`,
  `getLightCoords`, a thread-local `Cache`, directional shade `getDirectionalBrightness(CardinalLighting, ...)`) writes
  per-vertex colour and packed light into `com.mojang.blaze3d.vertex.QuadInstance` (`color0..3`, `lightCoords0..3`,
  `overlayCoords`). `VertexConsumer.putBlockBakedQuad(x, y, z, BakedQuad, QuadInstance)` writes the vertices.
- Fluids: `FluidRenderer.tesselate(BlockAndTintGetter, BlockPos, FluidRenderer$Output, BlockState, FluidState)`.
- Shader side: the light coordinates are looked up in the lightmap texture (`Sampler2`), plus `Fog`, the per-section
  fade (`visibility`), and the cutout alpha threshold (`ALPHA_CUTOUT_THRESHOLD_CUTOUT_TERRAIN`).

## 8. FRAPI / Indigo in this version

- `net.fabricmc.fabric.mixin.client.renderer.block.render.SectionCompilerMixin` (fact, signatures):
  `beforeLoopCompile(SectionPos, RenderSectionRegion, VertexSorting, SectionBufferBuilderPack, CIR<Results>,
  Map<ChunkSectionLayer,BufferBuilder>, LocalRef<AltModelBlockRenderer>, LocalRef<QuadEmitter>)` sets up an
  `AltModelBlockRenderer` (`Renderer.get().altModelBlockRenderer(ao, cull, blockColors)`) and a `QuadEmitter`
  (`Renderer.quadEmitter(consumer)`, which writes into `getOrBeginLayer(...)`, `@Shadow`ed). `tesselateBlockProxy(...)`
  redirects `ModelBlockRenderer.tesselateBlock(...)` inside `SectionCompiler.compile` to
  `AltModelBlockRenderer.tesselateBlock(QuadEmitter, x, y, z, level, pos, state, model, seed)`.
- Indigo: `impl.client.indigo.renderer.render.AltModelBlockRendererImpl` (own `AoCalculator`, `FlatLighter`, tint
  cache), `IndigoRenderer`. `mixin.blockgetter.client.RenderSectionRegionMixin` adds render-data lookup to the region.
- Fabric rendering events: `mixin.client.rendering.LevelRendererMixin` has `prepareChunkRenders(CIR<ChunkSectionsToRender>)`
  (captures the object for `LevelTerrainRenderContext.sectionsToRender()`; which of the two prepare methods it
  targets is unknown) and **`wrapRenderOpaqueTerrain` / `wrapRenderTranslucentTerrain`: `@WrapOperation` on the
  `ChunkSectionsToRender.renderGroup(...)` calls** in `executeSolid` / `executeClassicTransparency`. They fire
  `LevelRenderEvents.START_MAIN`, `AFTER_OPAQUE_TERRAIN`, `BEFORE/AFTER_TRANSLUCENT_TERRAIN`.
  So Brimstone should keep those call sites (cancel or replace *inside* `renderGroup`, not at the caller).

## 9. Hook points for Brimstone

**(a) Take over drawing, keep vanilla meshing.**
1. Build our own draw list: `@Inject(at=HEAD, cancellable)` on
   `public ChunkSectionsToRender LevelRenderer.prepareChunkRenders(Matrix4fc, boolean)` **and**
   `public ChunkSectionsToRender LevelRenderer.prepareChunkRendersIndirect(Matrix4fc, boolean)`, or `@WrapOperation`
   on both calls in `render(GraphicsResourceAllocator, boolean, CameraRenderState, GpuBufferSlice, Vector4f, boolean,
   boolean)`. Returning our own object requires subclassing `ChunkSectionsToRender` (its ctor
   `private ChunkSectionsToRender(GpuBufferSlice, int)` needs a class-tweaker `accessible`/`extendable` entry). The
   less invasive option: let vanilla build its object, and do our work in (2).
2. Draw: `@Inject(at=HEAD, cancellable)` on `public void ChunkSectionsToRender.renderGroup(ChunkSectionLayerGroup,
   RenderPass, GpuSampler, GpuTextureView, boolean)` and `public void ChunkSectionsToRender.renderOit(GpuSampler,
   OitStage, OitRenderPassProvider$Parameters, GpuTextureView, GpuTextureView)` (or the private
   `renderLayers(ChunkSectionLayer[], GpuSampler, RenderPass, GpuTextureView, GpuTextureView, RenderPipeline,
   RenderPipeline)`). We get vanilla's pass with the default UBOs already bound, and Fabric's wrap events still fire.
3. Data sources (all public): `LevelRenderer.visibleSections()`, `RenderSection.getSectionMesh()` /
   `getRenderOrigin()` / `getVisibility(long, long)`, `SectionMesh.getSectionDraw(ChunkSectionLayer)` →
   `SectionDraw(indexCount, indexType, hasCustomIndexBuffer)`, `LevelRenderer.sectionRenderDispatcher()
   .getRenderSectionSlice(SectionMesh, ChunkSectionLayer)` → `(GpuBuffer, vertexOffset, GpuBuffer, indexOffset)`,
   `lock()/unlock()` around reads, `RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS)`,
   `RenderSystem.getDynamicUniforms().writeTerrainTransform/writeChunkSections/writeChunkSectionsInstanced/
   writeChunkSectionCommands`, `ChunkSectionLayer.pipeline(boolean)`.
4. Where the win is: on MDI devices vanilla already makes about 1 indirect draw per (layer, heap). The real gain is
   **path B** (Intel iGPU via `multiDrawIndirectHasKnownIssues`, GL 3.3 old drivers), where vanilla makes one
   `drawIndexed` plus a UBO rebind per section per layer. Options there: `RenderPass.multiDrawIndexed(PointerBuffer
   indexOffsets, IntBuffer counts, IntBuffer baseVertices, int drawCount)` (`multiDrawDirectSeparate`, always true on
   GL) per heap. The section origin then has to come from somewhere other than a per-draw UBO:
   `gl_DrawID`/`shaderDrawParameters` (feature flag), or our own vertex format with a region-relative position
   (needs our own pipeline, so it falls under (b)). Brimstone's forced Intel MDI flag (`GlHeuristicsMixin`) just
   switches vanilla to path A.
   The capability flags live in `LevelRenderer.multiDrawIndirectAvailable` (private final, set in the ctor) and
   `usingMultiDrawIndirectForTerrain` (private, set per frame).

**(b) Replace meshing later.**
- `public SectionCompiler$Results SectionCompiler.compile(SectionPos, RenderSectionRegion, VertexSorting,
  SectionBufferBuilderPack)`. `SectionCompiler` is a public, non-final class with a public ctor, so you can subclass
  it and install it with `SectionRenderDispatcher.setCompiler(SectionCompiler)` or a `@Redirect` of `new
  SectionCompiler(...)` in `LevelRenderer.invalidateCompiledGeometry(ClientLevel, Options, Camera, BlockColors)`.
  Keep calling `Renderer.get().altModelBlockRenderer(...)` for FRAPI models (Fabric's mixin targets vanilla's
  `compile` body and won't apply to an override).
- Lower level: `RenderSection$CompileTask.doTask(SectionBufferBuilderPack)` (package-private class) and the private
  `RenderSection.addSectionBuffersToUberBuffer(ChunkSectionLayer, CompiledSectionMesh, ByteBuffer, ByteBuffer)`, which
  goes from mesh bytes into vanilla's uber buffers. A custom vertex format can't go through vanilla's uber buffers
  (their alignment is the vanilla vertex size) or pipelines. It needs our own allocator (`UberGpuBuffer` is public
  and reusable: `new UberGpuBuffer<>(name, usage, heapSize, align, StagingBuffer.create(...))`) plus our own
  `RenderPipeline.builder(...)`.
- Region source: `RenderSectionRegion` (public getters plus a static `index(...)`), `SectionCopy`.

## 10. What the terrain shaders need (from the Java side)

- Pipelines: `RenderPipelines.SOLID_TERRAIN`, `CUTOUT_TERRAIN`, `TRANSLUCENT_TERRAIN` and their `*_MULTIDRAW`;
  `OIT_TERRAIN`, `OIT_TERRAIN_MULTIDRAW` (`OitPipelineSet` per `OitStage`); `WIREFRAME`, `WIREFRAME_MULTIDRAW`.
  Snippets: `TERRAIN_SNIPPET`, `MULTIDRAW_TERRAIN_SNIPPET`, `GLOBALS_SNIPPET`, `MATRICES_FOG_SNIPPET`, `OIT_*_SNIPPET`.
  Constants: `ALPHA_CUTOUT_THRESHOLD_CUTOUT_TERRAIN` (a shader define, guess).
- Uniform blocks: `TerrainUniform` (mat4 modelView + int atlasWidth/atlasHeight; `TERRAIN_TRANSFORM_UBO_SIZE`),
  `ChunkSection` (ivec3 origin + float visibility; `CHUNK_SECTION_UBO_SIZE`; non-MDI only), `Projection`, `Fog`,
  `Globals`, `Lighting`. The MDI pipelines take the same section data as instanced vertex attributes (semantics
  `CHUNK_POSITION_SEMANTIC_NAME`, `CHUNK_VISIBILITY_SEMANTIC_NAME`; format `CHUNK_DATA_INSTANCED`; guess on binding 1).
- Samplers: `Sampler0` (block atlas `TextureAtlas.LOCATION_BLOCKS`, `chunkLayerSampler` = clamp/linear/mips,
  anisotropic when enabled), `Sampler2` (lightmap, clamp linear). OIT adds `DepthBoundsSampler` and the OIT targets.
- Shaders are GLSL compiled to SPIR-V (`GlslCompiler`), recompiled for GL. To get the real sources and layout, read
  at runtime: `pipeline.getShaders()` (vertex/fragment `Identifier`s, resolved through the resource manager),
  `getShaderDefines()`, `getBindGroupLayouts()`, `getVertexFormatBindings()`, `pushConstantSize()`.

## 11. Next steps for the dump (to settle the guesses)

Add to `.github/dump-bytecode`: `chunk\.ChunkSectionsToRender.*`, `chunk\.(SectionCompiler.*|CompiledSectionMesh|
ChunkSectionLayer.*|RenderRegionCache|RenderSectionRegion)`, `renderer\.(DynamicGpuData.*|RenderPipelines|
SectionBufferBuilder.*|MappableRingBuffer)`, `renderer\.extract\.LevelExtractor`, `renderer\.block\.(ModelBlockRenderer|
BlockModelLighter)`, `com\.mojang\.blaze3d\.vertex\.`, plus `-constants` for `DefaultVertexFormat` string names.
Also have CI dump the `assets/minecraft/shaders/` entries that `getShaders()` names.
