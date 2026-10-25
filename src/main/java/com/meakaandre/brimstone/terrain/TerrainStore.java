package com.meakaandre.brimstone.terrain;

import com.meakaandre.brimstone.mixin.terrain.RenderSectionInvoker;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.TlsfAllocator;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.pipeline.IndexType;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.chunk.TranslucencyPointOfView;
import net.minecraft.core.SectionPos;
import org.lwjgl.system.MemoryUtil;

/**
 * Where Brimstone keeps terrain: a few large GPU buffers ("arenas", {@link #ARENA_BYTES} each), sub-allocated with
 * the game's own TLSF allocator. Each layer of a section is one small copy (16-byte vertices); a translucent layer also
 * keeps vanilla's sorted index list, in the same arena as its vertices (package 3), and only that list is rewritten
 * when vanilla sorts the layer again. Allocations never move and arenas are never resized. An arena that empties is closed (except the first), so memory
 * follows what is loaded. Sections are grouped into regions of 8x4x8; a region keeps its sections in one arena where
 * it can, so the plain path draws a region with one call per layer.
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
        long used;
        boolean closed;

        Arena(GpuDevice device, int index) {
            this.buffer = device.createBuffer(() -> "Brimstone terrain " + index,
                GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_INDEX | GpuBuffer.USAGE_COPY_DST, ARENA_BYTES);
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

    /** One layer of one section's vertices, in an arena. */
    static final class Part {
        final Arena arena;
        final TlsfAllocator.Allocation allocation;
        final int baseVertex;
        final int vertices;

        Part(Arena arena, TlsfAllocator.Allocation allocation, int vertices) {
            this.arena = arena;
            this.allocation = allocation;
            this.baseVertex = (int) (allocation.getOffsetFromHeap() / CompactVertex.BYTES);
            this.vertices = vertices;
        }

        int indexCount() {
            return vertices / 4 * 6;
        }
    }

    /** A translucent layer's sorted index list (vanilla's bytes), in the same arena as the layer's vertices. */
    static final class IndexPart {
        final Arena arena;
        final TlsfAllocator.Allocation allocation;
        final IndexType type;
        final int count;

        IndexPart(Arena arena, TlsfAllocator.Allocation allocation, IndexType type, int count) {
            this.arena = arena;
            this.allocation = allocation;
            this.type = type;
            this.count = count;
        }

        long offset() {
            return allocation.getOffsetFromHeap();
        }
    }

    /** One layer of one section: its vertices, and for translucent its indices. */
    static final class Entry {
        Part part;
        final Region region;
        final int slot;
        final int originX, originY, originZ;
        IndexPart index;
        /** CI's translucency check only: what was last written to the index list, and the sort it came from. */
        TranslucencyCheck.Sorted sorted;
        boolean sortedChecked;

        Entry(Region region, int slot, int x, int y, int z) {
            this.region = region;
            this.slot = slot;
            this.originX = x;
            this.originY = y;
            this.originZ = z;
        }
    }

    /**
     * A layer on its way in. {@code split} null: an index list only (vanilla sorted a translucent layer Brimstone
     * holds again); {@code indices} null: solid or cutout. Buffers are memAlloc'd and owned here.
     */
    private record Staged(SectionRenderDispatcher.RenderSection section, CompiledSectionMesh mesh, ChunkSectionLayer layer,
                          int x, int y, int z, CompactVertex.Converted split, ByteBuffer indices, IndexType indexType, int indexCount,
                          TranslucencyCheck.Sorted sorted) {
        void free() {
            if (split != null) split.free();
            if (indices != null) MemoryUtil.memFree(indices);
        }

        boolean indicesOnly() {
            return split == null;
        }
    }

    public record Snapshot(int regions, int arenas, long arenaBytes, long usedBytes, long vertices) {
    }

    private static final List<Arena> ARENAS = new ArrayList<>();
    private static final Long2ObjectOpenHashMap<Region> REGIONS = new Long2ObjectOpenHashMap<>();
    /** Live meshes: index 0 solid, 1 cutout, 2 translucent. */
    static final Map<CompiledSectionMesh, Entry[]> ENTRIES = new IdentityHashMap<>();
    static final int LAYERS = 3, TRANSLUCENT = 2;
    /** Meshes whose translucent layer Brimstone held and which vanilla has let go of since: a late sort of one is dropped. */
    private static final Set<CompiledSectionMesh> RELEASED_TRANSLUCENT = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    private static final List<Staged> STAGED = new ArrayList<>();
    private static long liveVertices;
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
        return layer == ChunkSectionLayer.SOLID ? 0 : layer == ChunkSectionLayer.CUTOUT ? 1 : layer == ChunkSectionLayer.TRANSLUCENT ? 2 : -1;
    }

    static int generation() {
        synchronized (LOCK) {
            return generation;
        }
    }

    /**
     * Worker thread (or render thread for nearby sections): takes ownership of {@code split} and {@code indices}
     * (translucent only). False (and both freed) if the store was emptied since {@code gen} or the module no longer
     * takes meshes.
     */
    static boolean stage(int gen, SectionRenderDispatcher.RenderSection section, CompiledSectionMesh mesh, ChunkSectionLayer layer,
                         int x, int y, int z, CompactVertex.Converted split, ByteBuffer indices, IndexType indexType, int indexCount,
                         TranslucencyCheck.Sorted sorted) {
        Staged s = new Staged(section, mesh, layer, x, y, z, split, indices, indexType, indexCount, sorted);
        synchronized (LOCK) {
            if (gen != generation || !accepting || !Terrain.takingMeshes()) {
                s.free();
                return false;
            }
            STAGED.add(s);
            return true;
        }
    }

    /** What happened to a new index list for a translucent layer (vanilla sorted it again). */
    enum Resort { STAGED, NOT_OURS, DROPPED }

    /**
     * Worker thread: vanilla sorted a mesh's translucent layer again. If Brimstone holds that layer, the new index list
     * (copied here) replaces the old one at the end of the frame; if Brimstone held it and vanilla has since let go of
     * the mesh, the list is dropped (vanilla would draw nothing of it either); otherwise it is vanilla's.
     */
    static Resort stageIndices(int gen, CompiledSectionMesh mesh, ByteBuffer src, IndexType type, int count, TranslucencyCheck.Sorted sorted) {
        synchronized (LOCK) {
            Entry[] es = ENTRIES.get(mesh);
            boolean held = es != null && es[TRANSLUCENT] != null;
            if (!held) {
                for (Staged s : STAGED) {
                    if (s.mesh == mesh && s.layer == ChunkSectionLayer.TRANSLUCENT && !s.indicesOnly()) {
                        held = true;
                        break;
                    }
                }
            }
            if (!held) return RELEASED_TRANSLUCENT.contains(mesh) ? Resort.DROPPED : Resort.NOT_OURS;
            if (gen != generation || !accepting || !Terrain.takingMeshes()) return Resort.DROPPED;
            ByteBuffer copy = MemoryUtil.memAlloc(src.remaining());
            MemoryUtil.memCopy(src, copy);
            STAGED.add(new Staged(null, mesh, ChunkSectionLayer.TRANSLUCENT, 0, 0, 0, null, copy, type, count, sorted));
            return Resort.STAGED;
        }
    }

    /** Whether an index list for this mesh's translucent layer is waiting to be copied in (render thread, under the lock). */
    static boolean indicesPending(CompiledSectionMesh mesh) {
        for (Staged s : STAGED) if (s.mesh == mesh && s.indices != null) return true;
        return false;
    }

    /** Render thread, under the lock: closes buffers let go of since the last frame. */
    static void closePending() {
        for (GpuBuffer b : PENDING_CLOSE) b.close();
        PENDING_CLOSE.clear();
    }

    private static Part allocate(GpuDevice device, CommandEncoder encoder, Region region, ByteBuffer data, int vertices) {
        int stride = CompactVertex.BYTES;
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
            arena = newArena(device);
            a = arena.allocator.allocate(bytes, stride);
            if (a == null) throw new IllegalStateException("a fresh terrain arena refused " + bytes + " bytes");
        }
        if (region.preferred == null || region.preferred.closed || region.live == 0) region.preferred = arena;
        return write(encoder, arena, a, data, vertices);
    }

    private static Arena newArena(GpuDevice device) {
        Arena arena = new Arena(device, ARENAS.size());
        ARENAS.add(arena);
        return arena;
    }

    private static Part write(CommandEncoder encoder, Arena arena, TlsfAllocator.Allocation a, ByteBuffer data, int vertices) {
        try {
            encoder.writeToBuffer(arena.buffer.slice(a.getOffsetFromHeap(), data.remaining()), data);
        } catch (Throwable t) {
            arena.allocator.free(a);
            throw t;
        }
        arena.used += a.getSize();
        return new Part(arena, a, vertices);
    }

    /**
     * The arena translucent layers go to first (package 3): consecutive translucent sections in vanilla's order then
     * share an arena, so they draw as one run instead of breaking wherever their
     * regions' arenas alternate. Render thread, under the lock.
     */
    private static Arena translucentArena;

    /**
     * A translucent layer: its vertices and its index list go into one arena (the translucent arena, another with room
     * for both, or a new one, which then becomes the translucent arena), so one draw binds a single buffer for both.
     */
    private static void allocateTranslucent(GpuDevice device, CommandEncoder encoder, Region region, Entry e, ByteBuffer vertices,
                                            int vertexCount, ByteBuffer indices, IndexType type, int indexCount) {
        int stride = CompactVertex.BYTES;
        int vbytes = vertices.remaining(), ibytes = indices.remaining();
        List<Arena> order = new ArrayList<>(ARENAS.size() + 1);
        if (translucentArena != null && !translucentArena.closed) order.add(translucentArena);
        for (Arena c : ARENAS) if (c != translucentArena) order.add(c);
        for (int pass = 0; pass < 2; pass++) {
            if (pass == 1) order = List.of(newArena(device));
            for (Arena arena : order) {
                TlsfAllocator.Allocation va = arena.allocator.allocate(vbytes, stride);
                if (va == null) continue;
                TlsfAllocator.Allocation ia = arena.allocator.allocate(ibytes, 4);
                if (ia == null) {
                    arena.allocator.free(va);
                    continue;
                }
                if (translucentArena == null || translucentArena.closed || pass == 1) translucentArena = arena;
                if (region.preferred == null || region.preferred.closed || region.live <= 1) region.preferred = arena;
                Part p;
                try {
                    p = write(encoder, arena, va, vertices, vertexCount);
                } catch (Throwable t) {
                    arena.allocator.free(ia);
                    throw t;
                }
                e.part = p;
                try {
                    encoder.writeToBuffer(arena.buffer.slice(ia.getOffsetFromHeap(), ibytes), indices);
                } catch (Throwable t) {
                    arena.allocator.free(ia);
                    throw t;
                }
                arena.used += ia.getSize();
                e.index = new IndexPart(arena, ia, type, indexCount);
                return;
            }
        }
        throw new IllegalStateException("a fresh terrain arena refused a translucent layer of " + (vbytes + ibytes) + " bytes");
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
                    if (s.indicesOnly()) {
                        rewriteIndices(device, encoder, s);
                        s.free();
                        continue;
                    }
                    long key = regionKey(s.x, s.y, s.z);
                    Region region = REGIONS.get(key);
                    if (region == null) {
                        region = new Region(key, s.x & ~127, s.y & ~63, s.z & ~127);
                        REGIONS.put(key, region);
                    }
                    Entry e = new Entry(region, CompactVertex.sectionIndex(s.x, s.y, s.z), s.x, s.y, s.z);
                    region.live++;
                    Entry[] es = ENTRIES.computeIfAbsent(s.mesh, k -> new Entry[LAYERS]);
                    int li = layerIndex(s.layer);
                    if (es[li] != null) free(es[li]);
                    es[li] = e;
                    ByteBuffer v = s.split.data();
                    int count = s.split.vertices();
                    if (s.indices != null) {
                        allocateTranslucent(device, encoder, region, e, v, count, s.indices, s.indexType, s.indexCount);
                        e.sorted = s.sorted;
                    } else {
                        e.part = allocate(device, encoder, region, v, count);
                    }
                    liveVertices += count;
                    s.free();
                }
            } finally {
                // whatever wasn't copied (an exception part-way) is freed, never leaked
                for (int i = next; i < done.size(); i++) done.get(i).free();
            }
            // vanilla's own "uploaded" callback, which swaps the section's new mesh in once all its layers are there
            // and releases the old one (that re-enters this lock on this thread). A mesh vanilla let go of meanwhile
            // (a cancelled task) gets no callback, as vanilla skips its own uploads for it. A new index list only
            // replaces the one a live mesh already has: vanilla's own callback for it changes nothing then.
            for (Staged s : done) {
                if (s.indicesOnly()) continue;
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

    /** A new index list for a translucent layer Brimstone holds: written over the old one (same size), in its place. */
    private static void rewriteIndices(GpuDevice device, CommandEncoder encoder, Staged s) {
        Entry[] es = ENTRIES.get(s.mesh);
        Entry e = es == null ? null : es[TRANSLUCENT];
        if (e == null || e.index == null) {
            TranslucencyStats.resortDropped();
            return;
        }
        IndexPart ip = e.index;
        int bytes = s.indices.remaining();
        if (ip.type != s.indexType || ip.count != s.indexCount || (long) ip.count * ip.type.bytes != bytes) {
            throw new IllegalStateException("a new index list for a translucent layer does not match the old one (" + ip.count + " "
                + ip.type + " indices, now " + s.indexCount + " " + s.indexType + ", " + bytes + " bytes)");
        }
        encoder.writeToBuffer(ip.arena.buffer.slice(ip.offset(), bytes), s.indices);
        e.sorted = TranslucencyCheck.resorted(e.sorted, s.sorted);
        e.sortedChecked = false;
        TranslucencyStats.resortUploaded();
    }

    /** A mesh vanilla let go of (replaced, section reset, task cancelled). Any thread. */
    static void release(CompiledSectionMesh mesh) {
        synchronized (LOCK) {
            Entry[] es = ENTRIES.remove(mesh);
            if (es != null) {
                for (Entry e : es) if (e != null) free(e);
                if (es[TRANSLUCENT] != null) RELEASED_TRANSLUCENT.add(mesh);
            }
            if (!STAGED.isEmpty()) {
                STAGED.removeIf(s -> {
                    if (s.mesh != mesh) return false;
                    if (s.layer == ChunkSectionLayer.TRANSLUCENT) RELEASED_TRANSLUCENT.add(mesh);
                    s.free();
                    return true;
                });
            }
            if (!DROPPED.isEmpty()) DROPPED.removeIf(s -> s.mesh == mesh);
        }
    }

    private static void free(Entry e) {
        if (e.index != null) {
            IndexPart ip = e.index;
            e.index = null;
            ip.arena.used -= ip.allocation.getSize();
            ip.arena.allocator.free(ip.allocation);
            closeIfEmpty(ip.arena);
        }
        Part p = e.part;
        if (p != null) {
            e.part = null;
            p.arena.used -= p.allocation.getSize();
            p.arena.allocator.free(p.allocation);
            liveVertices -= p.vertices;
            closeIfEmpty(p.arena);
        }
        Region r = e.region;
        if (--r.live <= 0) {
            REGIONS.remove(r.key);
            if (r.ubo != null) PENDING_CLOSE.add(r.ubo);
            r.ubo = null;
        }
    }

    private static void closeIfEmpty(Arena a) {
        if (!a.closed && ARENAS.indexOf(a) > 0 && a.allocator.isCompletelyFree()) {
            // an empty arena goes (closed on the render thread before the next frame's draws)
            a.closed = true;
            ARENAS.remove(a);
            PENDING_CLOSE.add(a.buffer);
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
                s.free();
                // a dropped new index list needs nothing: vanilla meshes the section again
                if (!s.indicesOnly()) DROPPED.add(s);
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
    /**
     * Render thread, end of frame: lets go of every translucent layer Brimstone holds or has staged (translucent terrain
     * goes back to vanilla for the session; solid and cutout stay). Returns their sections (SectionPos longs), to be
     * marked dirty so vanilla meshes them again and keeps their translucent layers itself.
     */
    static LongOpenHashSet releaseTranslucent() {
        LongOpenHashSet out = new LongOpenHashSet();
        synchronized (LOCK) {
            for (var it = ENTRIES.entrySet().iterator(); it.hasNext(); ) {
                var en = it.next();
                Entry[] es = en.getValue();
                Entry e = es[TRANSLUCENT];
                if (e == null) continue;
                out.add(SectionPos.asLong(e.originX >> 4, e.originY >> 4, e.originZ >> 4));
                free(e);
                es[TRANSLUCENT] = null;
                RELEASED_TRANSLUCENT.add(en.getKey());
                if (es[0] == null && es[1] == null) it.remove();
            }
            STAGED.removeIf(st -> {
                if (st.layer != ChunkSectionLayer.TRANSLUCENT) return false;
                if (!st.indicesOnly()) out.add(SectionPos.asLong(st.x >> 4, st.y >> 4, st.z >> 4));
                st.free();
                return true;
            });
            translucentArena = null;
        }
        return out;
    }

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
            for (Staged s : STAGED) s.free();
            STAGED.clear();
            DROPPED.clear();
            ENTRIES.clear();
            RELEASED_TRANSLUCENT.clear();
            for (Region r : REGIONS.values()) if (r.ubo != null) PENDING_CLOSE.add(r.ubo);
            REGIONS.clear();
            for (Arena a : ARENAS) {
                a.closed = true;
                PENDING_CLOSE.add(a.buffer);
            }
            translucentArena = null;
            ARENAS.clear();
            closePending();
            liveVertices = 0;
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
            return new Snapshot(REGIONS.size(), ARENAS.size(), ARENAS.size() * ARENA_BYTES, used, liveVertices);
        }
    }
}
