package com.meakaandre.brimstone.terrain;

import com.meakaandre.brimstone.mixin.terrain.RenderSectionInvoker;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.TlsfAllocator;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.device.GpuDevice;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.SectionPos;
import org.lwjgl.system.MemoryUtil;

/**
 * Where Brimstone keeps terrain: a few large GPU buffers ("arenas", {@link #ARENA_BYTES} each), sub-allocated with
 * the game's own TLSF allocator. Each layer of a section is one small copy (compact or wide);
 * allocations never move and arenas are never resized. An arena that empties is closed (except the first), so memory
 * follows what is loaded. Sections are grouped into regions of 8x4x8; a region keeps its sections in one arena where
 * it can, so the plain path draws a region with one call per format.
 *
 * <p>Meshes are staged from the worker threads ({@link #stage}) and copied in on the render thread at the end of the
 * frame ({@link #upload}), where vanilla uploads its own. Everything here is guarded by one lock.
 */
public final class TerrainStore {
    static final long ARENA_BYTES = 64L << 20;
    /** A layer bigger than this stays with vanilla (it would hog an arena). */
    static final int MAX_LAYER_BYTES = (int) (ARENA_BYTES / 8);

    static final Object LOCK = new Object();

    /** The two vertex formats. */
    static final int COMPACT = 0, WIDE = 1;

    static final class Arena {
        final GpuBuffer buffer;
        final TlsfAllocator allocator;
        long used;
        boolean closed;

        Arena(GpuDevice device, int index) {
            this.buffer = device.createBuffer(() -> "Brimstone terrain " + index,
                GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_COPY_DST, ARENA_BYTES);
            this.allocator = new TlsfAllocator(new TlsfAllocator.Heap(ARENA_BYTES));
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
            Arrays.fill(written, Float.NaN);
            Arrays.fill(pending, Float.NaN);
        }
    }

    /** One format's part of one layer of one section, in an arena. */
    static final class Part {
        final Arena arena;
        final TlsfAllocator.Allocation allocation;
        final int baseVertex;
        final int vertices;

        Part(Arena arena, TlsfAllocator.Allocation allocation, int stride, int vertices) {
            this.arena = arena;
            this.allocation = allocation;
            this.baseVertex = (int) (allocation.getOffsetFromHeap() / stride);
            this.vertices = vertices;
        }

        int indexCount() {
            return vertices / 4 * 6;
        }
    }

    /** One layer of one section: its compact and wide parts (either may be null). */
    static final class Entry {
        final Part[] parts = new Part[2];
        final Region region;
        final int slot;
        final int originX, originY, originZ;

        Entry(Region region, int slot, int x, int y, int z) {
            this.region = region;
            this.slot = slot;
            this.originX = x;
            this.originY = y;
            this.originZ = z;
        }
    }

    private record Staged(SectionRenderDispatcher.RenderSection section, CompiledSectionMesh mesh, ChunkSectionLayer layer,
                          int x, int y, int z, CompactVertex.Split split) {
    }

    public record Snapshot(int regions, int arenas, long arenaBytes, long usedBytes, long compactVertices, long wideVertices) {
    }

    private static final List<Arena> ARENAS = new ArrayList<>();
    private static final Long2ObjectOpenHashMap<Region> REGIONS = new Long2ObjectOpenHashMap<>();
    /** Live meshes: index 0 solid, 1 cutout. */
    static final Map<CompiledSectionMesh, Entry[]> ENTRIES = new IdentityHashMap<>();
    private static final List<Staged> STAGED = new ArrayList<>();
    private static final long[] LIVE_VERTICES = new long[2];
    private static final List<GpuBuffer> PENDING_CLOSE = new ArrayList<>();
    /** Bumped whenever the store is emptied or a section dispatcher comes or goes: older staged meshes are dropped. */
    private static int generation;
    /** Whether a live section dispatcher exists (meshes from a disposed one's late worker tasks are refused). */
    private static boolean accepting;

    /** A new section dispatcher (a world was entered, or the renderer reset). */
    static void dispatcherCreated() {
        synchronized (LOCK) {
            generation++;
            accepting = true;
        }
    }

    /** The section dispatcher is going away: nothing staged now or later by its workers may survive into the next. */
    static void dispatcherDisposed() {
        synchronized (LOCK) {
            accepting = false;
        }
        clear();
    }

    private TerrainStore() {
    }

    static int layerIndex(ChunkSectionLayer layer) {
        return layer == ChunkSectionLayer.SOLID ? 0 : layer == ChunkSectionLayer.CUTOUT ? 1 : -1;
    }

    static int generation() {
        synchronized (LOCK) {
            return generation;
        }
    }

    /**
     * Worker thread (or render thread for nearby sections): takes ownership of {@code split}. False (and the split
     * freed) if the store was emptied since {@code gen} or the module no longer takes meshes.
     */
    static boolean stage(int gen, SectionRenderDispatcher.RenderSection section, CompiledSectionMesh mesh, ChunkSectionLayer layer,
                         int x, int y, int z, CompactVertex.Split split) {
        synchronized (LOCK) {
            if (gen != generation || !accepting || !Terrain.takingMeshes()) {
                split.free();
                return false;
            }
            STAGED.add(new Staged(section, mesh, layer, x, y, z, split));
            return true;
        }
    }

    /** Render thread, under the lock: closes buffers let go of since the last frame. */
    static void closePending() {
        for (GpuBuffer b : PENDING_CLOSE) b.close();
        PENDING_CLOSE.clear();
    }

    private static Part allocate(GpuDevice device, CommandEncoder encoder, Region region, ByteBuffer data, int stride, int vertices) {
        int bytes = data.remaining();
        Arena arena = null;
        TlsfAllocator.Allocation a = null;
        if (region.preferred != null && !region.preferred.closed) {
            a = region.preferred.allocator.allocate(bytes, stride);
            if (a != null) arena = region.preferred;
        }
        for (int i = 0; a == null && i < ARENAS.size(); i++) {
            Arena c = ARENAS.get(i);
            if (c == region.preferred) continue;
            a = c.allocator.allocate(bytes, stride);
            if (a != null) arena = c;
        }
        if (a == null) {
            arena = new Arena(device, ARENAS.size());
            ARENAS.add(arena);
            a = arena.allocator.allocate(bytes, stride);
            if (a == null) throw new IllegalStateException("a fresh terrain arena refused " + bytes + " bytes");
        }
        if (region.preferred == null || region.preferred.closed || region.live == 0) region.preferred = arena;
        try {
            encoder.writeToBuffer(arena.buffer.slice(a.getOffsetFromHeap(), bytes), data);
        } catch (Throwable t) {
            arena.allocator.free(a);
            throw t;
        }
        arena.used += a.getSize();
        return new Part(arena, a, stride, vertices);
    }

    /** Render thread, end of frame (inside vanilla's terrain upload): copies staged meshes into the arenas. */
    static void upload() {
        synchronized (LOCK) {
            if (STAGED.isEmpty()) return;
            List<Staged> done = new ArrayList<>(STAGED);
            STAGED.clear();
            int next = 0;
            try {
                GpuDevice device = RenderSystem.getDevice();
                CommandEncoder encoder = device.createCommandEncoder();
                for (; next < done.size(); next++) {
                    Staged s = done.get(next);
                    long key = regionKey(s.x, s.y, s.z);
                    Region region = REGIONS.get(key);
                    if (region == null) {
                        region = new Region(key, s.x & ~127, s.y & ~63, s.z & ~127);
                        REGIONS.put(key, region);
                    }
                    Entry e = new Entry(region, CompactVertex.sectionIndex(s.x, s.y, s.z), s.x, s.y, s.z);
                    region.live++;
                    Entry[] es = ENTRIES.computeIfAbsent(s.mesh, k -> new Entry[2]);
                    int li = layerIndex(s.layer);
                    if (es[li] != null) free(es[li]);
                    es[li] = e;
                    CompactVertex.Split sp = s.split;
                    if (sp.compact() != null) {
                        e.parts[COMPACT] = allocate(device, encoder, region, sp.compact(), CompactVertex.BYTES, sp.compactVertices());
                        LIVE_VERTICES[COMPACT] += sp.compactVertices();
                    }
                    if (sp.wide() != null) {
                        e.parts[WIDE] = allocate(device, encoder, region, sp.wide(), CompactVertex.WIDE_BYTES, sp.wideVertices());
                        LIVE_VERTICES[WIDE] += sp.wideVertices();
                    }
                    sp.free();
                }
            } finally {
                // whatever wasn't copied (an exception part-way) is freed, never leaked
                for (int i = next; i < done.size(); i++) done.get(i).split.free();
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
                    s.split.free();
                    return true;
                });
            }
            if (!DROPPED.isEmpty()) DROPPED.removeIf(s -> s.mesh == mesh);
        }
    }

    private static void free(Entry e) {
        for (int f = 0; f < 2; f++) {
            Part p = e.parts[f];
            if (p == null) continue;
            p.arena.used -= p.allocation.getSize();
            p.arena.allocator.free(p.allocation);
            LIVE_VERTICES[f] -= p.vertices;
            Arena a = p.arena;
            if (!a.closed && ARENAS.indexOf(a) > 0 && a.allocator.isCompletelyFree()) {
                // an empty arena goes (closed on the render thread before the next frame's draws)
                a.closed = true;
                ARENAS.remove(a);
                PENDING_CLOSE.add(a.buffer);
            }
        }
        Region r = e.region;
        if (--r.live <= 0) {
            REGIONS.remove(r.key);
            if (r.ubo != null) PENDING_CLOSE.add(r.ubo);
            r.ubo = null;
        }
    }

    /**
     * Staged meshes dropped by a hand-back: vanilla already counts their layer as taken (it will not upload it), so on
     * the render thread each is either completed through vanilla's own callback (its section then shows nothing in
     * that layer until vanilla has meshed it again; the section is marked dirty) or, if a newer mesh of the section is
     * already in, released. Never left half-uploaded.
     */
    private static final List<Staged> DROPPED = new ArrayList<>();
    private static int droppedGeneration;

    /** Drops every staged mesh (they will not be copied in); {@link #finishDropped} hands them back to vanilla. */
    static void dropStaged() {
        synchronized (LOCK) {
            for (Staged s : STAGED) {
                s.split.free();
                DROPPED.add(s);
            }
            STAGED.clear();
            generation++;
            droppedGeneration = generation;
        }
    }

    /**
     * Render thread, end of frame: hands meshes dropped by {@link #dropStaged} back to vanilla, and returns their
     * sections (SectionPos longs) to be marked dirty. Nothing if the store was emptied since (the section dispatcher
     * went away, and vanilla released everything with it).
     */
    static LongOpenHashSet finishDropped() {
        LongOpenHashSet sections = new LongOpenHashSet();
        synchronized (LOCK) {
            if (DROPPED.isEmpty()) return sections;
            List<Staged> dropped = new ArrayList<>(DROPPED);
            DROPPED.clear();
            if (droppedGeneration != generation) return sections;
            for (Staged s : dropped) {
                sections.add(SectionPos.asLong(s.x >> 4, s.y >> 4, s.z >> 4));
                RenderSectionInvoker section = (RenderSectionInvoker) s.section;
                if (s.section.getSectionMesh().getCompileTaskStartTime() > s.mesh.getCompileTaskStartTime()) {
                    section.brimstone$releaseSectionMesh(s.mesh);
                } else {
                    section.brimstone$vertexBufferUploaded(s.mesh, s.layer);
                }
            }
        }
        return sections;
    }

    /** The sections Brimstone holds, as SectionPos longs (for handing them back to vanilla). */
    static LongOpenHashSet heldSections() {
        LongOpenHashSet out = new LongOpenHashSet();
        synchronized (LOCK) {
            for (Entry[] es : ENTRIES.values()) {
                for (Entry e : es) {
                    if (e != null) out.add(SectionPos.asLong(e.originX >> 4, e.originY >> 4, e.originZ >> 4));
                }
            }
        }
        return out;
    }

    static boolean isEmpty() {
        synchronized (LOCK) {
            return ENTRIES.isEmpty() && STAGED.isEmpty() && DROPPED.isEmpty();
        }
    }

    /** Render thread: drops everything (the dispatcher is going away, or the module stopped drawing). */
    static void clear() {
        synchronized (LOCK) {
            for (Staged s : STAGED) s.split.free();
            STAGED.clear();
            DROPPED.clear();
            ENTRIES.clear();
            for (Region r : REGIONS.values()) if (r.ubo != null) PENDING_CLOSE.add(r.ubo);
            REGIONS.clear();
            for (Arena a : ARENAS) {
                a.closed = true;
                PENDING_CLOSE.add(a.buffer);
            }
            ARENAS.clear();
            closePending();
            Arrays.fill(LIVE_VERTICES, 0);
            generation++;
        }
    }

    static long regionKey(int x, int y, int z) {
        return ((long) (x >> 7) & 0x3FFFFFL) << 42 | ((long) (z >> 7) & 0x3FFFFFL) << 20 | ((long) (y >> 6) & 0xFFFFFL);
    }

    public static Snapshot snapshot() {
        synchronized (LOCK) {
            long used = 0;
            for (Arena a : ARENAS) used += a.used;
            return new Snapshot(REGIONS.size(), ARENAS.size(), ARENAS.size() * ARENA_BYTES, used, LIVE_VERTICES[COMPACT], LIVE_VERTICES[WIDE]);
        }
    }
}
