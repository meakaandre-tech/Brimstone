package com.meakaandre.quicksilver.terrain;

import com.meakaandre.quicksilver.QuicksilverClient;
import com.meakaandre.quicksilver.mixin.terrain.RenderSectionInvoker;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import com.mojang.renderpearl.api.pipeline.IndexType;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.SectionPos;
import org.lwjgl.system.MemoryUtil;

/**
 * Quicksilver's terrain module (package 1): takes the meshes vanilla builds on its worker threads, converts each layer
 * into its 16-byte vertex (off-grid positions and UVs rounded to its grid, see {@link CompactVertex}), keeps them in
 * large GPU buffers and draws them with few calls. Since package 3 that includes translucent layers: Quicksilver keeps
 * vanilla's sorted index list with each, takes every new list vanilla's own sorting makes for it (on vanilla's worker
 * threads, when vanilla decides to sort) and draws translucent sections in vanilla's order.
 *
 * <p>States:
 * <ul>
 * <li>STARTING until a world's first frames prove its hooks, the vertex format it reads, that no resource pack
 * replaces the shader code its own vertex shader stands in for, and its pipelines; then ACTIVE.</li>
 * <li>DEFERRED while a resource pack replaces that shader code: vanilla draws all terrain; checked again on every
 * resource reload.</li>
 * <li>OFF for the rest of the session after anything unexpected: an exception in its code, a GPU error reported by the
 * device's debug output while it drew or uploaded, a pipeline that stops compiling. One ERROR log line.</li>
 * </ul>
 *
 * <p>Handing terrain back to vanilla (OFF or DEFERRED): no new meshes are taken, staged ones are dropped, and every
 * section Quicksilver holds is marked dirty so vanilla meshes it again into its own buffers. When the cause was not in
 * Quicksilver's drawing (meshing, upload bookkeeping), Quicksilver keeps drawing what it holds until vanilla's new mesh of
 * each section replaces it, so nothing disappears; otherwise (an error while drawing, a pack override) it stops
 * drawing at once and those sections are missing for the moment vanilla takes to mesh them again.
 */
public final class Terrain {
    public enum State { STARTING, ACTIVE, DEFERRED, OFF }

    private static volatile State state = State.STARTING;
    private static volatile String reason = "waiting for the first frame";
    private static final AtomicInteger FALLBACKS = new AtomicInteger();
    private static int framesWithoutDrawHook;

    /** Set when terrain must go back to vanilla at the next safe point (end of frame). */
    private static volatile boolean handBack;
    /** While handing back: whether Quicksilver may keep drawing what it holds until vanilla replaced it. */
    private static volatile boolean keepDrawing;
    /**
     * Set when a translucent layer had a vertex colour alpha other than 255 (Quicksilver's vertex has no alpha byte):
     * translucent terrain goes back to vanilla for the rest of the session, solid and cutout stay with Quicksilver.
     */
    private static volatile boolean translucentToVanilla;
    private static volatile boolean translucentReleasePending;
    static final java.util.concurrent.atomic.AtomicInteger ALPHA_LAYERS_KEPT_BY_VANILLA = new java.util.concurrent.atomic.AtomicInteger();
    private static final java.util.concurrent.atomic.AtomicBoolean OPAQUE_ALPHA_WARNED = new java.util.concurrent.atomic.AtomicBoolean();
    private static final java.util.concurrent.atomic.AtomicBoolean TRANSLUCENT_ALPHA_WARNED = new java.util.concurrent.atomic.AtomicBoolean();
    /** Set by a resource reload: check packs again before the next frame. */
    private static volatile boolean resourcesChanged;

    /** Debug (CI only): Quicksilver uses its plain path even on frames where vanilla uses multi-draw indirect. */
    static final boolean FORCE_PLAIN = Boolean.getBoolean("quicksilver.debug.forcePlainTerrain");
    /**
     * Debug (CI noise collection only, package 3): translucent layers stay with vanilla, so a Quicksilver run differs
     * from vanilla only by noise. CI fails any run with it set.
     */
    static final boolean TRANSLUCENT_DRY_RUN = Boolean.getBoolean("quicksilver.debug.translucencyDryRun");

    private Terrain() {
    }

    public static State state() {
        return state;
    }

    public static String reason() {
        return reason;
    }

    public static int fallbacks() {
        return FALLBACKS.get();
    }

    static boolean active() {
        return state == State.ACTIVE;
    }

    /**
     * Whether every visible section's solid and cutout layers are drawn this frame, by vanilla or by Quicksilver (so the
     * visibility module may trust the faces in them). False only while terrain is being handed back and Quicksilver has
     * stopped drawing what it still holds.
     */
    public static boolean drawsAllItHolds() {
        State s = state;
        if (s == State.ACTIVE || s == State.STARTING) return !handBack || keepDrawing;
        return keepDrawing || TerrainStore.isEmpty();
    }

    /** Whether new meshes go to Quicksilver. */
    static boolean takingMeshes() {
        return state == State.ACTIVE && !handBack;
    }

    /** Whether new translucent layers go to Quicksilver. */
    static boolean takingTranslucent() {
        return takingMeshes() && !TRANSLUCENT_DRY_RUN && !translucentToVanilla;
    }

    /**
     * Whether Quicksilver takes every layer of a new mesh, translucent included (package 4c: only then may Quicksilver's
     * own mesher mesh a section, since only this module can draw its vertex).
     */
    public static boolean takingAllLayers() {
        return takingTranslucent();
    }

    /** Whether translucent terrain went back to vanilla for the session because of a vertex alpha (bench and CI). */
    public static boolean translucentHandedBackForAlpha() {
        return translucentToVanilla;
    }

    /** Whether Quicksilver draws what it holds this frame. */
    static boolean drawing() {
        return state == State.ACTIVE || keepDrawing;
    }

    /**
     * Render thread, as a frame's terrain preparation starts: proves everything once, then turns on; re-checks
     * resource packs after a reload. Never throws: anything unexpected turns the module off.
     */
    static void beginFrame() {
        try {
            if (resourcesChanged) {
                resourcesChanged = false;
                if (state == State.ACTIVE || state == State.DEFERRED) {
                    String pack = TerrainResources.override();
                    if (pack != null && state == State.ACTIVE) defer(pack);
                    else if (pack == null && state == State.DEFERRED) {
                        state = State.STARTING;
                        reason = "resource packs changed";
                    }
                }
            }
            if (state == State.STARTING) activate();
        } catch (Throwable t) {
            fail("starting", t, false);
        }
    }

    private static void activate() {
        if (Boolean.getBoolean("quicksilver.ci.measureOnly")) {
            // CI's measure runs (package 3): vanilla draws all terrain, the bench measures vanilla's own counts
            state = State.OFF;
            reason = "measure only (CI)";
            QuicksilverClient.LOG.warn("Quicksilver terrain: off, quicksilver.ci.measureOnly is set (vanilla draws all terrain)");
            return;
        }
        if (!TerrainRenderer.drawHookSeen) {
            // the draw hook runs inside vanilla's terrain pass: wait until it has run once (vanilla draws meanwhile)
            if (++framesWithoutDrawHook < 600) return;
        }
        String why = TerrainRenderer.drawHookSeen ? TerrainMixins.loadTargetsAndCheck()
            : "hooks that did not apply: ChunkSectionsToRender#quicksilver$drawLayer (did not run in 600 frames)";
        if (why == null && !RenderSectionInvoker.class.isAssignableFrom(SectionRenderDispatcher.RenderSection.class)) {
            why = "the RenderSection invoker did not apply";
        }
        if (why == null) why = CompactVertex.checkVanillaFormat();
        if (why == null) {
            String pack = TerrainResources.override();
            if (pack != null) {
                defer(pack);
                return;
            }
        }
        if (why == null) why = TerrainPipelines.compileAll();
        if (why != null) {
            state = State.OFF;
            reason = why;
            FALLBACKS.incrementAndGet();
            QuicksilverClient.LOG.error("Quicksilver terrain: turned off before its first frame, vanilla draws all terrain (fallback: {})", why);
            return;
        }
        reason = "on";
        state = State.ACTIVE;
        TerrainResources.logAtlas();
        QuicksilverClient.LOG.info("Quicksilver terrain: on ({}-byte vertex for every layer, off-grid positions and UVs rounded to its grid"
                + " (at most 1/4096 block); vanilla's is {}; solid, cutout and translucent layers (translucent with vanilla's own sorted index lists,"
                + " drawn in vanilla's order); plain draw path: {}; GPU error check: {})",
            CompactVertex.BYTES, CompactVertex.VANILLA_BYTES, TerrainRenderer.plainPathName(),
            TerrainGpuErrors.describe());
        if (FORCE_PLAIN) QuicksilverClient.LOG.warn("Quicksilver debug: quicksilver.debug.forcePlainTerrain is set, Quicksilver's terrain uses its plain path on every frame");
        if (TRANSLUCENT_DRY_RUN) QuicksilverClient.LOG.warn("Quicksilver translucency: DRY RUN (noise collection): translucent layers stay with vanilla");
        if (TranslucencyCheck.ENABLED) QuicksilverClient.LOG.info("Quicksilver translucency: per-frame check on (debug; every frame Quicksilver's translucent draws are checked against vanilla's order and latest sort)");
    }

    /** A resource pack replaces shader code Quicksilver stands in for: vanilla draws all terrain until packs change. */
    private static void defer(String pack) {
        state = State.DEFERRED;
        reason = pack;
        QuicksilverClient.LOG.warn("Quicksilver terrain: deferring to vanilla, which draws all terrain: {}", pack);
        startHandBack(false);
    }

    /** Called by the resource reload (render thread). */
    public static void resourcesReloaded() {
        resourcesChanged = true;
    }

    /**
     * Something went wrong: off for good, with one log line, and terrain goes back to vanilla. {@code drawingOk}: the
     * cause was not in drawing, so Quicksilver may keep drawing what it holds until vanilla has replaced it. Any thread.
     */
    static void fail(String where, Throwable t, boolean drawingOk) {
        synchronized (Terrain.class) {
            if (state == State.OFF) {
                if (!drawingOk) keepDrawing = false;
                return;
            }
            state = State.OFF;
            reason = where + ": " + t;
        }
        FALLBACKS.incrementAndGet();
        QuicksilverClient.LOG.error("Quicksilver terrain: turned off, vanilla draws terrain again (fallback in {})", where, t);
        startHandBack(drawingOk);
    }

    private static void startHandBack(boolean drawingOk) {
        keepDrawing = drawingOk;
        handBack = true;
        try {
            TerrainStore.dropStaged();
        } catch (Throwable t) {
            QuicksilverClient.LOG.error("Quicksilver terrain: could not drop staged meshes", t);
        }
    }

    /**
     * Render thread, end of frame (no render pass open): when terrain goes back to vanilla, marks every section
     * Quicksilver holds or had staged dirty so vanilla meshes it again, hands staged meshes back to vanilla, and drops
     * Quicksilver's buffers once nothing is left to draw (at once if Quicksilver may not keep drawing).
     */
    private static void handBackIfNeeded() {
        try {
            if (translucentReleasePending) {
                translucentReleasePending = false;
                LongOpenHashSet released = TerrainStore.releaseTranslucent();
                Minecraft mc = Minecraft.getInstance();
                if (mc.levelExtractor != null) {
                    for (long s : released) mc.levelExtractor.setSectionDirty(SectionPos.x(s), SectionPos.y(s), SectionPos.z(s));
                }
                QuicksilverClient.LOG.info("Quicksilver terrain: translucent layers of {} sections handed back to vanilla", released.size());
            }
            if (handBack) {
                handBack = false;
                LongOpenHashSet held = TerrainStore.heldSections();
                LongOpenHashSet staged = TerrainStore.finishDropped();
                if (!keepDrawing) TerrainStore.clear();
                Minecraft mc = Minecraft.getInstance();
                LongOpenHashSet all = new LongOpenHashSet(held);
                all.addAll(staged);
                if (mc.levelExtractor != null) {
                    for (long s : all) mc.levelExtractor.setSectionDirty(SectionPos.x(s), SectionPos.y(s), SectionPos.z(s));
                }
                QuicksilverClient.LOG.info("Quicksilver terrain: {} sections handed back to vanilla, {} of them with a mesh still on its"
                        + " way in ({})", all.size(), staged.size(),
                    keepDrawing ? "Quicksilver draws what it holds until vanilla's new meshes are in" : "missing until vanilla has meshed them");
            } else if (state == State.OFF || state == State.DEFERRED) {
                if (!keepDrawing) {
                    // a draw error during a keep-drawing hand-back: stop drawing and let go of the buffers now
                    if (!TerrainStore.isEmpty()) TerrainStore.clear();
                } else if (TerrainStore.isEmpty()) {
                    keepDrawing = false;
                    TerrainStore.clear();
                }
            }
        } catch (Throwable t) {
            keepDrawing = false;
            QuicksilverClient.LOG.error("Quicksilver terrain: could not hand terrain back to vanilla cleanly", t);
            try {
                TerrainStore.clear();
                Minecraft mc = Minecraft.getInstance();
                if (mc.levelExtractor != null) mc.levelExtractor.allChanged();
            } catch (Throwable ignored) {
                // nothing more to try
            }
        }
    }

    /**
     * A worker thread (or the render thread for a nearby section) is about to put one layer of a freshly built mesh,
     * or a translucent layer's new index list ({@code vertices} null: vanilla sorted it again), into vanilla's buffers.
     * True if Quicksilver took it instead.
     */
    public static boolean take(SectionRenderDispatcher.RenderSection section, ChunkSectionLayer layer, CompiledSectionMesh mesh,
                               ByteBuffer vertices, ByteBuffer indices) {
        int li = TerrainStore.layerIndex(layer);
        if (li < 0 || !takingMeshes()) return false;
        if (li == TerrainStore.TRANSLUCENT) return takeTranslucent(section, mesh, vertices, indices);
        if (vertices == null || indices != null) return false;
        try {
            int gen = TerrainStore.generation();
            SectionMesh.SectionDraw draw = mesh.getSectionDraw(layer);
            if (draw == null || draw.hasCustomIndexBuffer()) return false;
            int count = vertices.remaining() / CompactVertex.VANILLA_BYTES;
            if ((long) count * CompactVertex.BYTES > TerrainStore.MAX_LAYER_BYTES || draw.indexCount() != count / 4 * 6) {
                TerrainStats.LAYERS_KEPT_BY_VANILLA.incrementAndGet();
                return false;
            }
            long t0 = System.nanoTime();
            long node = section.getSectionNode();
            int x = SectionPos.sectionToBlockCoord(SectionPos.x(node));
            int y = SectionPos.sectionToBlockCoord(SectionPos.y(node));
            int z = SectionPos.sectionToBlockCoord(SectionPos.z(node));
            if (CompactVertex.hasAlpha(vertices)) {
                // no alpha byte in Quicksilver's vertex: an opaque layer can stay with vanilla on its own (depth sorts it)
                ALPHA_LAYERS_KEPT_BY_VANILLA.incrementAndGet();
                if (OPAQUE_ALPHA_WARNED.compareAndSet(false, true)) {
                    QuicksilverClient.LOG.warn("Quicksilver terrain: a {} layer (section {} {} {}) has vertex colours with alpha below 255, which"
                        + " Quicksilver's vertex does not hold; vanilla keeps such layers (likely a mod's tinted model; counted in the bench)",
                        layer, x >> 4, y >> 4, z >> 4);
                }
                TerrainStats.LAYERS_KEPT_BY_VANILLA.incrementAndGet();
                return false;
            }
            // solid and cutout: quads grouped by facing, so groups facing away from the camera can be left out (package 4b)
            CompactVertex.Converted split = CompactVertex.convert(vertices, CompactVertex.sectionIndex(x, y, z), true);
            if (split == null) {
                TerrainStats.LAYERS_KEPT_BY_VANILLA.incrementAndGet();
                return false;
            }
            TerrainStats.converted(split);
            // the index flag first: once staged, the render thread may report the vertices uploaded at any moment,
            // and vanilla only swaps the mesh in when both flags are set (vanilla sets it too for a layer without
            // indices, so a mesh handed back to vanilla below is unaffected)
            mesh.setIndexBufferUploaded(layer);
            if (!TerrainStore.stage(gen, section, mesh, layer, x, y, z, split, null, null, 0, null)) return false;
            TerrainStats.LAYERS_TAKEN.incrementAndGet();
            TerrainStats.transcoded(System.nanoTime() - t0);
            return true;
        } catch (Throwable t) {
            fail("meshing", t, true);
            return false;
        }
    }

    /**
     * A translucent layer: with its vertices (a fresh mesh, sorted by vanilla as it was built) or only a new index list
     * (vanilla's resort task, on its worker thread, re-sorted the layer for a new camera position). Quicksilver keeps
     * vanilla's index lists as they are (16-bit ones widened to 32 bits), so the quad order is exactly vanilla's, ties included. A layer Quicksilver
     * can't hold makes the whole module hand terrain back (a translucent layer must never be split between vanilla's
     * draws and Quicksilver's, or the order between sections would no longer be vanilla's).
     */
    private static boolean takeTranslucent(SectionRenderDispatcher.RenderSection section, CompiledSectionMesh mesh, ByteBuffer vertices,
                                           ByteBuffer indices) {
        if (TRANSLUCENT_DRY_RUN) return false;
        // after the hand-back (alpha below 255), new layers stay with vanilla; a new index list still goes through
        // stageIndices, which drops it for a mesh Quicksilver held (vanilla has no vertices of it) and leaves the rest
        if (translucentToVanilla && vertices != null) return false;
        try {
            int gen = TerrainStore.generation();
            SectionMesh.SectionDraw draw = mesh.getSectionDraw(ChunkSectionLayer.TRANSLUCENT);
            if (draw == null) return false;
            if (indices == null || !draw.hasCustomIndexBuffer()) {
                // vanilla always sorts translucent layers into an index list of their own; one without it is not what
                // Quicksilver was built for, and it must not stay with vanilla next to Quicksilver's (see above)
                throw new IllegalStateException("a translucent layer without its own sorted index list");
            }
            IndexType type = draw.indexType();
            int indexCount = draw.indexCount();
            if ((long) indexCount * type.bytes != indices.remaining()) {
                throw new IllegalStateException("a translucent index list has " + indices.remaining() + " bytes for " + indexCount + " " + type + " indices");
            }
            TranslucencyCheck.Sorted sorted = TranslucencyCheck.sortedFor(section, vertices, indices, type);
            if (vertices == null) {
                TerrainStore.Resort r = TerrainStore.stageIndices(gen, mesh, intIndices(indices, type, indexCount), indexCount, sorted);
                if (r == TerrainStore.Resort.NOT_OURS) return false;
                TranslucencyStats.resortTaken(r == TerrainStore.Resort.STAGED);
                return true;
            }
            int count = vertices.remaining() / CompactVertex.VANILLA_BYTES;
            if ((long) count * CompactVertex.BYTES > TerrainStore.MAX_LAYER_BYTES || indexCount != count / 4 * 6) {
                throw new IllegalStateException("a translucent layer Quicksilver cannot hold (" + count + " vertices, " + indexCount + " indices)");
            }
            long t0 = System.nanoTime();
            long node = section.getSectionNode();
            int x = SectionPos.sectionToBlockCoord(SectionPos.x(node));
            int y = SectionPos.sectionToBlockCoord(SectionPos.y(node));
            int z = SectionPos.sectionToBlockCoord(SectionPos.z(node));
            if (CompactVertex.hasAlpha(vertices)) {
                // the order of translucent sections must stay one renderer's: all translucent terrain goes back to vanilla
                translucentToVanilla = true;
                translucentReleasePending = true;
                ALPHA_LAYERS_KEPT_BY_VANILLA.incrementAndGet();
                if (TRANSLUCENT_ALPHA_WARNED.compareAndSet(false, true)) QuicksilverClient.LOG.warn("Quicksilver terrain: a translucent layer (section {} {} {}) has vertex colours with alpha below 255, which"
                    + " Quicksilver's vertex does not hold; translucent terrain goes back to vanilla for the rest of the session, solid and cutout"
                    + " stay with Quicksilver (likely a mod's tinted model)", x >> 4, y >> 4, z >> 4);
                return false;
            }
            CompactVertex.Converted split = CompactVertex.convert(vertices, CompactVertex.sectionIndex(x, y, z));
            if (split == null) throw new IllegalStateException("a translucent layer Quicksilver's vertex format cannot hold (" + count
                + " vertices: not whole quads, or a position, UV or alpha out of its range)");
            ByteBuffer copy = intIndices(indices, type, indexCount);
            TerrainStats.converted(split);
            // as for solid and cutout: both flags must be set before vanilla can swap the mesh in, and Quicksilver
            // copies both the vertices and the index list in one go at the end of the frame
            mesh.setIndexBufferUploaded(ChunkSectionLayer.TRANSLUCENT);
            if (!TerrainStore.stage(gen, section, mesh, ChunkSectionLayer.TRANSLUCENT, x, y, z, split, copy, IndexType.INT, indexCount, sorted)) return false;
            TerrainStats.LAYERS_TAKEN.incrementAndGet();
            TranslucencyStats.layerTaken();
            TerrainStats.transcoded(System.nanoTime() - t0);
            return true;
        } catch (Throwable t) {
            fail("translucent meshing", t, true);
            return false;
        }
    }

    /**
     * Package 4c: a layer Quicksilver's own mesher wrote, in the 16-byte vertex already grouped by facing (solid and
     * cutout) or in the order made (translucent, whose index list {@code indices} vanilla sorted), handed over where
     * vanilla's would go to the GPU. Nothing is converted: the vertices are copied as they are into a staged buffer.
     * Always taken (vanilla could not draw these vertices): if the store refuses it (terrain going back to vanilla, the
     * store emptied), the mesh is completed without that layer at the end of the frame and the section meshed again.
     */
    public static void takeMeshed(SectionRenderDispatcher.RenderSection section, ChunkSectionLayer layer, CompiledSectionMesh mesh,
                                  ByteBuffer owned, int vertices, int positionsRounded, int uvsRounded, CompactVertex.Faces faces,
                                  ByteBuffer indices, float[] centroids) {
        long node = section.getSectionNode();
        int x = SectionPos.sectionToBlockCoord(SectionPos.x(node));
        int y = SectionPos.sectionToBlockCoord(SectionPos.y(node));
        int z = SectionPos.sectionToBlockCoord(SectionPos.z(node));
        int gen = TerrainStore.generation();
        boolean translucent = layer == ChunkSectionLayer.TRANSLUCENT;
        // the buffers this method owns until TerrainStore.stage takes them over (staged or refused): freed on any failure
        ByteBuffer ownedVertices = owned, ownedIndices = null;
        try {
            long t0 = System.nanoTime();
            // the mesher's own buffer, taken over as it is (package 4c: no copy here)
            CompactVertex.Converted split = new CompactVertex.Converted(owned, vertices, positionsRounded, uvsRounded, faces);
            ByteBuffer idx = null;
            IndexType type = null;
            int indexCount = 0;
            TranslucencyCheck.Sorted sorted = null;
            if (translucent) {
                SectionMesh.SectionDraw draw = mesh.getSectionDraw(layer);
                if (draw == null || indices == null || !draw.hasCustomIndexBuffer()) {
                    throw new IllegalStateException("a translucent layer without its own sorted index list");
                }
                type = draw.indexType();
                indexCount = draw.indexCount();
                if ((long) indexCount * type.bytes != indices.remaining() || indexCount != vertices / 4 * 6) {
                    throw new IllegalStateException("a translucent index list has " + indices.remaining() + " bytes for " + indexCount + " " + type
                        + " indices and " + vertices + " vertices");
                }
                sorted = TranslucencyCheck.sortedFor(section, centroids, indices, type);
                idx = intIndices(indices, type, indexCount);
                ownedIndices = idx;
                type = IndexType.INT;
            }
            TerrainStats.converted(split);
            mesh.setIndexBufferUploaded(layer);
            ownedVertices = null;
            ownedIndices = null;
            boolean staged = TerrainStore.stage(gen, section, mesh, layer, x, y, z, split, idx, type, indexCount, sorted);
            if (staged) {
                TerrainStats.LAYERS_TAKEN.incrementAndGet();
                if (translucent) TranslucencyStats.layerTaken();
            } else {
                TerrainStore.refuse(section, mesh, layer, x, y, z);
            }
            TerrainStats.transcoded(System.nanoTime() - t0);
        } catch (Throwable t) {
            if (ownedVertices != null) MemoryUtil.memFree(ownedVertices);
            if (ownedIndices != null) MemoryUtil.memFree(ownedIndices);
            mesh.setIndexBufferUploaded(layer);
            TerrainStore.refuse(section, mesh, layer, x, y, z);
            fail(translucent ? "translucent meshing" : "meshing", t, true);
        }
    }

    /**
     * A copy of vanilla's sorted index list as 32-bit indices (memAlloc'd; the caller owns it): the same indices in the
     * same order, only a 16-bit list widened, so that consecutive translucent sections share one index type and draw as
     * one run (vanilla picks 16 or 32 bits per section by its vertex count).
     */
    private static ByteBuffer intIndices(ByteBuffer src, IndexType type, int count) {
        ByteBuffer out = MemoryUtil.memAlloc(count * 4);
        if (type == IndexType.INT) {
            MemoryUtil.memCopy(src, out);
        } else {
            int p = src.position();
            for (int i = 0; i < count; i++) out.putInt(i * 4, src.getShort(p + i * 2) & 0xFFFF);
        }
        return out;
    }

    public static void released(SectionMesh mesh) {
        if (mesh instanceof CompiledSectionMesh m) {
            try {
                TerrainStore.release(m);
            } catch (Throwable t) {
                fail("releasing a mesh", t, false);
            }
        }
    }

    /** Render thread, end of frame, in vanilla's terrain upload. */
    public static void upload() {
        try {
            LongOpenHashSet refused = TerrainStore.finishRefused();
            if (!refused.isEmpty()) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.levelExtractor != null) {
                    for (long s : refused) mc.levelExtractor.setSectionDirty(SectionPos.x(s), SectionPos.y(s), SectionPos.z(s));
                }
                TerrainStats.REFUSED_LAYERS.addAndGet(refused.size());
            }
        } catch (Throwable t) {
            fail("completing refused meshes", t, false);
        }
        if (takingMeshes()) {
            try {
                TerrainGpuErrors.Mark mark = TerrainGpuErrors.mark();
                TerrainStore.upload();
                String err = TerrainGpuErrors.since(mark);
                if (err != null) fail("upload", new IllegalStateException("GPU error: " + err), false);
            } catch (Throwable t) {
                fail("upload", t, false);
            }
        }
        handBackIfNeeded();
    }

    /** Vanilla made a section dispatcher (entering a world). */
    public static void dispatcherCreated() {
        try {
            TerrainStore.dispatcherCreated();
        } catch (Throwable t) {
            fail("dispatcher start", t, false);
        }
    }

    /** Vanilla's section dispatcher is going away (leaving the world, reloading): drop everything. */
    public static void disposed() {
        try {
            TerrainStore.dispatcherDisposed();
        } catch (Throwable t) {
            fail("dispose", t, false);
        }
    }
}
