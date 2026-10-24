package com.meakaandre.brimstone.terrain;

import com.meakaandre.brimstone.mixin.terrain.RenderSectionInvoker;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.TlsfAllocator;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.device.GpuDevice;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.lwjgl.system.MemoryUtil;

/**
 * Where Brimstone keeps terrain: a few large GPU buffers ("arenas", {@link #ARENA_BYTES} each, created when needed
 * and kept), sub-allocated with the game's own TLSF allocator. A section's solid and cutout meshes go into an arena
 * as one small copy each; they never move and arenas are never re-allocated. Sections are grouped into regions of
 * 8x4x8; a region keeps its sections in one arena where it can, so the plain path draws a region with one call.
 *
 * <p>Meshes are staged from the worker threads ({@link #stage}) and copied in on the render thread at the end of the
 * frame ({@link #upload}), where vanilla uploads its own. Everything here is guarded by one lock.
 */
public final class TerrainStore {
    static final long ARENA_BYTES = 64L << 20;
    /** A layer bigger than this stays with vanilla (it would hog an arena). */
    static final int MAX_LAYER_BYTES = (int) (ARENA_BYTES / 8);

    static final Object LOCK = new Object();

    static final class Arena {
        final GpuBuffer buffer;
        final TlsfAllocator allocator;
        final TlsfAllocator.Heap heap;
        long used;

        Arena(GpuDevice device, int index) {
            this.buffer = device.createBuffer(() -> "Brimstone terrain " + index,
                GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_COPY_DST, ARENA_BYTES);
            this.heap = new TlsfAllocator.Heap(ARENA_BYTES);
            this.allocator = new TlsfAllocator(heap);
        }
    }

    static final class Region {
        final int originX, originY, originZ;
        final long key;
        int live;
        Arena preferred;
        /** The plain path's uniform block for this region (origin, fade-in per section); made when first drawn. */
        GpuBuffer ubo;
        /** What {@link #ubo} holds now, per section of the region (NaN: never written). */
        final float[] written = new float[256];
        /** This frame's fade-in values of the region's visible sections (NaN: not visible); render thread only. */
        final float[] pending = new float[256];
        boolean touched;

        Region(long key, int x, int y, int z) {
            this.key = key;
            this.originX = x;
            this.originY = y;
            this.originZ = z;
            java.util.Arrays.fill(written, Float.NaN);
            java.util.Arrays.fill(pending, Float.NaN);
        }
    }

    /** One layer of one section, in an arena. */
    static final class Entry {
        final Arena arena;
        final TlsfAllocator.Allocation allocation;
        final int baseVertex;
        final int vertices;
        final Region region;
        final int slot;
        final int originX, originY, originZ;

        Entry(Arena arena, TlsfAllocator.Allocation allocation, int vertices, Region region, int slot, int x, int y, int z) {
            this.arena = arena;
            this.allocation = allocation;
            this.baseVertex = (int) (allocation.getOffsetFromHeap() / CompactVertex.BYTES);
            this.vertices = vertices;
            this.region = region;
            this.slot = slot;
            this.originX = x;
            this.originY = y;
            this.originZ = z;
        }

        int indexCount() {
            return vertices / 4 * 6;
        }
    }

    private record Staged(SectionRenderDispatcher.RenderSection section, CompiledSectionMesh mesh, ChunkSectionLayer layer,
                          int x, int y, int z, ByteBuffer data, int vertices) {
    }

    public record Snapshot(int regions, int arenas, long arenaBytes, long usedBytes, long vertices) {
    }

    private static final List<Arena> ARENAS = new ArrayList<>();
    private static final Long2ObjectOpenHashMap<Region> REGIONS = new Long2ObjectOpenHashMap<>();
    /** Live meshes: index 0 solid, 1 cutout. */
    static final Map<CompiledSectionMesh, Entry[]> ENTRIES = new IdentityHashMap<>();
    private static final List<Staged> STAGED = new ArrayList<>();
    private static long liveVertices;
    private static final List<GpuBuffer> PENDING_CLOSE = new ArrayList<>();

    /** Render thread, under the lock: closes buffers of regions that emptied since the last frame. */
    static void closePending() {
        for (GpuBuffer b : PENDING_CLOSE) b.close();
        PENDING_CLOSE.clear();
    }

    private TerrainStore() {
    }

    static int layerIndex(ChunkSectionLayer layer) {
        return layer == ChunkSectionLayer.SOLID ? 0 : layer == ChunkSectionLayer.CUTOUT ? 1 : -1;
    }

    /** Worker thread (or render thread for nearby sections): takes ownership of {@code data}. */
    static void stage(SectionRenderDispatcher.RenderSection section, CompiledSectionMesh mesh, ChunkSectionLayer layer,
                      int x, int y, int z, ByteBuffer data, int vertices) {
        synchronized (LOCK) {
            STAGED.add(new Staged(section, mesh, layer, x, y, z, data, vertices));
        }
    }

    /** Render thread, end of frame (inside vanilla's terrain upload): copies staged meshes into the arenas. */
    static void upload() {
        synchronized (LOCK) {
            List<Staged> done;
            if (STAGED.isEmpty()) return;
            GpuDevice device = RenderSystem.getDevice();
            CommandEncoder encoder = device.createCommandEncoder();
            done = new ArrayList<>(STAGED);
            STAGED.clear();
            for (Staged s : done) {
                int bytes = s.data.remaining();
                long key = regionKey(s.x, s.y, s.z);
                Region region = REGIONS.get(key);
                if (region == null) {
                    region = new Region(key, s.x & ~127, s.y & ~63, s.z & ~127);
                    REGIONS.put(key, region);
                }
                Arena arena = null;
                TlsfAllocator.Allocation a = null;
                if (region.preferred != null) {
                    a = region.preferred.allocator.allocate(bytes, CompactVertex.BYTES);
                    if (a != null) arena = region.preferred;
                }
                for (int i = 0; a == null && i < ARENAS.size(); i++) {
                    Arena c = ARENAS.get(i);
                    if (c == region.preferred) continue;
                    a = c.allocator.allocate(bytes, CompactVertex.BYTES);
                    if (a != null) arena = c;
                }
                if (a == null) {
                    arena = new Arena(device, ARENAS.size());
                    ARENAS.add(arena);
                    a = arena.allocator.allocate(bytes, CompactVertex.BYTES);
                    if (a == null) throw new IllegalStateException("a fresh terrain arena refused " + bytes + " bytes");
                }
                if (region.preferred == null || region.live == 0) region.preferred = arena;
                encoder.writeToBuffer(arena.buffer.slice(a.getOffsetFromHeap(), bytes), s.data);
                MemoryUtil.memFree(s.data);
                arena.used += a.getSize();
                Entry e = new Entry(arena, a, s.vertices, region, CompactVertex.sectionIndex(s.x, s.y, s.z), s.x, s.y, s.z);
                Entry[] es = ENTRIES.computeIfAbsent(s.mesh, k -> new Entry[2]);
                int li = layerIndex(s.layer);
                if (es[li] != null) free(es[li]);
                es[li] = e;
                region.live++;
                liveVertices += s.vertices;
            }
            // vanilla's own "uploaded" callback, which swaps the section's new mesh in once all its layers are there
            // and releases the old one (that re-enters this lock on this thread). A mesh vanilla let go of meanwhile
            // (a cancelled task) gets no callback, as vanilla skips its own uploads for it.
            for (Staged s : done) {
                Entry[] es = ENTRIES.get(s.mesh);
                if (es == null || es[layerIndex(s.layer)] == null) continue;
                RenderSectionInvoker section = (RenderSectionInvoker) s.section;
                if (s.section.getSectionMesh().getCompileTaskStartTime() > s.mesh.getCompileTaskStartTime()) {
                    // a newer mesh of this section is already in: this one must not replace it, it just goes
                    section.brimstone$releaseSectionMesh(s.mesh);
                    continue;
                }
                section.brimstone$vertexBufferUploaded(s.mesh, s.layer);
            }
        }
    }

    /** A mesh vanilla let go of (replaced, section reset, task cancelled). Any thread. */
    static void release(CompiledSectionMesh mesh) {
        synchronized (LOCK) {
            Entry[] es = ENTRIES.remove(mesh);
            if (es != null) {
                for (Entry e : es) if (e != null) free(e);
            }
            if (!STAGED.isEmpty()) {
                STAGED.removeIf(s -> {
                    if (s.mesh != mesh) return false;
                    MemoryUtil.memFree(s.data);
                    return true;
                });
            }
        }
    }

    private static void free(Entry e) {
        e.arena.used -= e.allocation.getSize();
        e.arena.allocator.free(e.allocation);
        liveVertices -= e.vertices;
        Region r = e.region;
        if (--r.live <= 0) {
            REGIONS.remove(r.key);
            // may run on a worker thread: GPU buffers are closed on the render thread, at the next frame
            if (r.ubo != null) PENDING_CLOSE.add(r.ubo);
            r.ubo = null;
        }
    }

    /** Render thread: drops everything (the dispatcher is going away, or the module turned off). */
    static void clear() {
        synchronized (LOCK) {
            for (Staged s : STAGED) MemoryUtil.memFree(s.data);
            STAGED.clear();
            ENTRIES.clear();
            for (Region r : REGIONS.values()) if (r.ubo != null) PENDING_CLOSE.add(r.ubo);
            REGIONS.clear();
            closePending();
            for (Arena a : ARENAS) a.buffer.close();
            ARENAS.clear();
            liveVertices = 0;
        }
    }

    static long regionKey(int x, int y, int z) {
        return ((long) (x >> 7) & 0x3FFFFFL) << 42 | ((long) (z >> 7) & 0x3FFFFFL) << 20 | ((long) (y >> 6) & 0xFFFFFL);
    }

    public static Snapshot snapshot() {
        synchronized (LOCK) {
            long used = 0;
            for (Arena a : ARENAS) used += a.used;
            return new Snapshot(REGIONS.size(), ARENAS.size(), ARENAS.size() * ARENA_BYTES, used, liveVertices);
        }
    }
}
