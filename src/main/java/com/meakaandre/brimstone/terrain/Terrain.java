package com.meakaandre.brimstone.terrain;

import com.meakaandre.brimstone.BrimstoneClient;
import com.meakaandre.brimstone.mixin.terrain.RenderSectionInvoker;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.BlockPos;

/**
 * Brimstone's terrain module (package 1): takes the solid and cutout meshes vanilla builds on its worker threads,
 * transcodes them into a 16-byte vertex, keeps them in large GPU buffers and draws them with few calls. Translucent
 * terrain, and any layer it can't take exactly, stay with vanilla.
 *
 * <p>State: STARTING until the first frame of a world proves its mixins, the vertex format it reads and its shaders;
 * then ACTIVE. Anything unexpected (an exception in its code, a shader that doesn't compile) turns it OFF for the rest
 * of the session with one log line, drops its buffers and has vanilla rebuild every section, so vanilla draws all
 * terrain again.
 */
public final class Terrain {
    public enum State { STARTING, ACTIVE, OFF }

    private static volatile State state = State.STARTING;
    private static volatile String reason = "waiting for the first frame";
    private static final AtomicInteger FALLBACKS = new AtomicInteger();

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

    /** Render thread, start of a frame's terrain preparation: proves everything once, then turns on. */
    static void activateIfNeeded() {
        if (state != State.STARTING) return;
        String why = TerrainMixins.loadTargetsAndCheck();
        if (why == null && !RenderSectionInvoker.class.isAssignableFrom(SectionRenderDispatcher.RenderSection.class)) {
            why = "the RenderSection invoker did not apply";
        }
        if (why == null) why = CompactVertex.checkVanillaFormat();
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
        // sections vanilla meshed before this frame stay in its buffers until rebuilt: rebuild them all once
        rebuild = true;
        BrimstoneClient.LOG.info("Brimstone terrain: on ({}-byte vertex, vanilla's is {}; solid and cutout layers; plain draw path: {})",
            CompactVertex.BYTES, CompactVertex.VANILLA_BYTES, TerrainRenderer.plainPathName());
    }

    /**
     * Something went wrong: off for good, with one log line; vanilla rebuilds every section so it draws all terrain
     * again. Any thread.
     */
    static void fail(String where, Throwable t) {
        synchronized (Terrain.class) {
            if (state == State.OFF) return;
            state = State.OFF;
            reason = where + ": " + t;
        }
        FALLBACKS.incrementAndGet();
        handBack = true;
        rebuild = true;
        BrimstoneClient.LOG.error("Brimstone terrain: turned off, vanilla draws terrain again (fallback in {})", where, t);
    }

    /** Set when the module turned off: its buffers go. */
    private static volatile boolean handBack;
    /** Set when every section must be meshed again (the module turned on, or off). */
    private static volatile boolean rebuild;

    /**
     * Render thread, at the end of a frame (no render pass open): when the module turned off, drops its buffers;
     * when it turned on or off, has vanilla rebuild every section (what F3+A does), so each section ends up with
     * whoever draws terrain now.
     */
    private static void rebuildIfNeeded() {
        if (!rebuild) return;
        rebuild = false;
        try {
            if (handBack) {
                handBack = false;
                TerrainStore.clear();
            }
            Minecraft mc = Minecraft.getInstance();
            if (mc.levelExtractor != null) mc.levelExtractor.allChanged();
        } catch (Throwable t) {
            BrimstoneClient.LOG.error("Brimstone terrain: could not have vanilla rebuild the terrain", t);
        }
    }

    /**
     * A worker thread (or the render thread for a nearby section) is about to put one layer of a freshly built mesh
     * into vanilla's buffers. True if Brimstone took it instead.
     */
    public static boolean take(SectionRenderDispatcher.RenderSection section, ChunkSectionLayer layer, CompiledSectionMesh mesh,
                               ByteBuffer vertices, ByteBuffer indices) {
        if (state != State.ACTIVE || vertices == null || indices != null) return false;
        if (TerrainStore.layerIndex(layer) < 0) return false;
        try {
            SectionMesh.SectionDraw draw = mesh.getSectionDraw(layer);
            if (draw == null || draw.hasCustomIndexBuffer()) return false;
            int count = vertices.remaining() / CompactVertex.VANILLA_BYTES;
            if ((long) count * CompactVertex.BYTES > TerrainStore.MAX_LAYER_BYTES || draw.indexCount() != count / 4 * 6) {
                TerrainStats.LAYERS_KEPT_BY_VANILLA.incrementAndGet();
                return false;
            }
            long t0 = System.nanoTime();
            BlockPos o = section.getRenderOrigin();
            int x = o.getX(), y = o.getY(), z = o.getZ();
            ByteBuffer compact = CompactVertex.transcode(vertices, CompactVertex.sectionIndex(x, y, z));
            if (compact == null) {
                TerrainStats.LAYERS_KEPT_BY_VANILLA.incrementAndGet();
                return false;
            }
            // the index flag first: once staged, the render thread may report the vertices uploaded at any moment,
            // and vanilla only swaps the mesh in when both flags are set
            mesh.setIndexBufferUploaded(layer);
            TerrainStore.stage(section, mesh, layer, x, y, z, compact, count);
            TerrainStats.LAYERS_TAKEN.incrementAndGet();
            TerrainStats.transcoded(System.nanoTime() - t0);
            return true;
        } catch (Throwable t) {
            fail("meshing", t);
            return false;
        }
    }

    public static void released(SectionMesh mesh) {
        if (mesh instanceof CompiledSectionMesh m) {
            try {
                TerrainStore.release(m);
            } catch (Throwable t) {
                fail("releasing a mesh", t);
            }
        }
    }

    /** Render thread, end of frame, in vanilla's terrain upload. */
    public static void upload() {
        if (state != State.OFF) {
            try {
                TerrainStore.upload();
            } catch (Throwable t) {
                fail("upload", t);
            }
        }
        rebuildIfNeeded();
    }

    /** Vanilla's section dispatcher is going away (leaving the world, reloading): drop everything. */
    public static void disposed() {
        try {
            TerrainStore.clear();
        } catch (Throwable t) {
            fail("dispose", t);
        }
    }
}
