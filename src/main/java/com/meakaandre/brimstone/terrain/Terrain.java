package com.meakaandre.brimstone.terrain;

import com.meakaandre.brimstone.BrimstoneClient;
import com.meakaandre.brimstone.mixin.terrain.RenderSectionInvoker;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.SectionPos;

/**
 * Brimstone's terrain module (package 1): takes the solid and cutout meshes vanilla builds on its worker threads,
 * converts each layer into its compact 16-byte vertex or, where that wouldn't hold every vertex exactly, a wide 32-byte one,
 * keeps them in large GPU buffers and draws them with few calls. Translucent terrain stays with vanilla.
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
 * section Brimstone holds is marked dirty so vanilla meshes it again into its own buffers. When the cause was not in
 * Brimstone's drawing (meshing, upload bookkeeping), Brimstone keeps drawing what it holds until vanilla's new mesh of
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
    /** While handing back: whether Brimstone may keep drawing what it holds until vanilla replaced it. */
    private static volatile boolean keepDrawing;
    /** Set by a resource reload: check packs again before the next frame. */
    private static volatile boolean resourcesChanged;

    /** Debug (CI only): Brimstone uses its plain path even on frames where vanilla uses multi-draw indirect. */
    static final boolean FORCE_PLAIN = Boolean.getBoolean("brimstone.debug.forcePlainTerrain");

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

    /** Whether new meshes go to Brimstone. */
    static boolean takingMeshes() {
        return state == State.ACTIVE && !handBack;
    }

    /** Whether Brimstone draws what it holds this frame. */
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
        if (!TerrainRenderer.drawHookSeen) {
            // the draw hook runs inside vanilla's terrain pass: wait until it has run once (vanilla draws meanwhile)
            if (++framesWithoutDrawHook < 600) return;
        }
        String why = TerrainRenderer.drawHookSeen ? TerrainMixins.loadTargetsAndCheck()
            : "hooks that did not apply: ChunkSectionsToRender#brimstone$drawLayer (did not run in 600 frames)";
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
            BrimstoneClient.LOG.error("Brimstone terrain: turned off before its first frame, vanilla draws all terrain (fallback: {})", why);
            return;
        }
        reason = "on";
        state = State.ACTIVE;
        TerrainResources.logAtlas();
        BrimstoneClient.LOG.info("Brimstone terrain: on ({}-byte vertex for layers it holds exactly, {}-byte vanilla layout plus section"
                + " slot for the rest; vanilla's is {}; solid and cutout layers; plain draw path: {}; GPU error check: {})",
            CompactVertex.BYTES, CompactVertex.WIDE_BYTES, CompactVertex.VANILLA_BYTES, TerrainRenderer.plainPathName(),
            TerrainGpuErrors.describe());
        if (FORCE_PLAIN) BrimstoneClient.LOG.warn("Brimstone debug: brimstone.debug.forcePlainTerrain is set, Brimstone's terrain uses its plain path on every frame");
    }

    /** A resource pack replaces shader code Brimstone stands in for: vanilla draws all terrain until packs change. */
    private static void defer(String pack) {
        state = State.DEFERRED;
        reason = pack;
        BrimstoneClient.LOG.warn("Brimstone terrain: deferring to vanilla, which draws all terrain: {}", pack);
        startHandBack(false);
    }

    /** Called by the resource reload (render thread). */
    public static void resourcesReloaded() {
        resourcesChanged = true;
    }

    /**
     * Something went wrong: off for good, with one log line, and terrain goes back to vanilla. {@code drawingOk}: the
     * cause was not in drawing, so Brimstone may keep drawing what it holds until vanilla has replaced it. Any thread.
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
        BrimstoneClient.LOG.error("Brimstone terrain: turned off, vanilla draws terrain again (fallback in {})", where, t);
        startHandBack(drawingOk);
    }

    private static void startHandBack(boolean drawingOk) {
        keepDrawing = drawingOk;
        handBack = true;
        try {
            TerrainStore.dropStaged();
        } catch (Throwable t) {
            BrimstoneClient.LOG.error("Brimstone terrain: could not drop staged meshes", t);
        }
    }

    /**
     * Render thread, end of frame (no render pass open): when terrain goes back to vanilla, marks every section
     * Brimstone holds or had staged dirty so vanilla meshes it again, hands staged meshes back to vanilla, and drops
     * Brimstone's buffers once nothing is left to draw (at once if Brimstone may not keep drawing).
     */
    private static void handBackIfNeeded() {
        try {
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
                BrimstoneClient.LOG.info("Brimstone terrain: {} sections handed back to vanilla, {} of them with a mesh still on its"
                        + " way in ({})", all.size(), staged.size(),
                    keepDrawing ? "Brimstone draws what it holds until vanilla's new meshes are in" : "missing until vanilla has meshed them");
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
            BrimstoneClient.LOG.error("Brimstone terrain: could not hand terrain back to vanilla cleanly", t);
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
     * A worker thread (or the render thread for a nearby section) is about to put one layer of a freshly built mesh
     * into vanilla's buffers. True if Brimstone took it instead.
     */
    public static boolean take(SectionRenderDispatcher.RenderSection section, ChunkSectionLayer layer, CompiledSectionMesh mesh,
                               ByteBuffer vertices, ByteBuffer indices) {
        if (!takingMeshes() || vertices == null || indices != null) return false;
        if (TerrainStore.layerIndex(layer) < 0) return false;
        try {
            int gen = TerrainStore.generation();
            SectionMesh.SectionDraw draw = mesh.getSectionDraw(layer);
            if (draw == null || draw.hasCustomIndexBuffer()) return false;
            int count = vertices.remaining() / CompactVertex.VANILLA_BYTES;
            if ((long) count * CompactVertex.WIDE_BYTES > TerrainStore.MAX_LAYER_BYTES || draw.indexCount() != count / 4 * 6) {
                TerrainStats.LAYERS_KEPT_BY_VANILLA.incrementAndGet();
                return false;
            }
            long t0 = System.nanoTime();
            long node = section.getSectionNode();
            int x = SectionPos.sectionToBlockCoord(SectionPos.x(node));
            int y = SectionPos.sectionToBlockCoord(SectionPos.y(node));
            int z = SectionPos.sectionToBlockCoord(SectionPos.z(node));
            CompactVertex.Split split = CompactVertex.split(vertices, CompactVertex.sectionIndex(x, y, z));
            if (split == null) {
                TerrainStats.LAYERS_KEPT_BY_VANILLA.incrementAndGet();
                return false;
            }
            TerrainStats.split(split);
            // the index flag first: once staged, the render thread may report the vertices uploaded at any moment,
            // and vanilla only swaps the mesh in when both flags are set (vanilla sets it too for a layer without
            // indices, so a mesh handed back to vanilla below is unaffected)
            mesh.setIndexBufferUploaded(layer);
            if (!TerrainStore.stage(gen, section, mesh, layer, x, y, z, split)) return false;
            TerrainStats.LAYERS_TAKEN.incrementAndGet();
            TerrainStats.transcoded(System.nanoTime() - t0);
            return true;
        } catch (Throwable t) {
            fail("meshing", t, true);
            return false;
        }
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
