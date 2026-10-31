package com.meakaandre.quicksilver.terrain;

import com.meakaandre.quicksilver.mixin.terrain.RenderSectionInvoker;
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
 * Where Quicksilver keeps terrain: a few GPU buffers ("arenas", up to {@link #ARENA_BYTES} each), sub-allocated with
 * the game's own TLSF allocator. An arena's allocator spans the full size from the start, but its buffer starts at
 * {@link #INITIAL_ARENA_BYTES} and doubles (a one-time GPU copy into the bigger buffer, at the end of the frame where
 * meshes are copied in) when an allocation reaches past it. The allocator splits the blocks it hands out from their front, so while an
 * arena fills its buffer stays close to what it holds; a grown buffer keeps its size until the arena empties (known
 * limit, package 9). Each layer of a section is one small copy (16-byte vertices); a translucent layer also
 * keeps vanilla's sorted index list, in the same arena as its vertices (package 3), and only that list is rewritten
 * when vanilla sorts the layer again. Each layer (solid, cutout, translucent) has arenas of its own, so a layer spans a
 * second arena only once that layer alone passes one arena, and the draws of a layer break only there (package 3).
 * Allocations never move (a grow copies the buffer, offsets stay). An arena that empties is closed (except a layer's last), so
 * memory follows what is loaded. Sections are grouped into regions of 8x4x8; a region keeps each layer of its sections
 * in one arena where it can, so the plain path draws a region with one call per layer.
 *
 * <p>Meshes are staged from the worker threads ({@link #stage}) and copied in on the render thread at the end of the
 * frame ({@link #upload}), where vanilla uploads its own. Everything here is guarded by one lock.
 */
public final class TerrainStore {
    static final long ARENA_BYTES = 64L << 20;
    /** An arena's buffer when it is made; it doubles when full, up to {@link #ARENA_BYTES} (package 3). */
    static final long INITIAL_ARENA_BYTES = 4L << 20;
    /** A layer bigger than this stays with vanilla (it would hog an arena). */
    public static final int MAX_LAYER_BYTES = (int) (ARENA_BYTES / 8);

    static final Object LOCK = new Object();

    static final class Arena {
        /** The arena's buffer, replaced by a bigger one when it grows (render thread, under the lock). */
        GpuBuffer buffer;
        /** Size of {@link #buffer}. */
        long capacity;
        final int index;
        final TlsfAllocator allocator;
        /** The layer this arena holds: 0 solid, 1 cutout, 2 translucent. */
        final int layer;
        long used;
        boolean closed;

        Arena(GpuDevice device, int index, int layer) {
            this.layer = layer;
            this.index = index;
            this.capacity = INITIAL_ARENA_BYTES;
            this.buffer = create(device, capacity);
            this.allocator = new TlsfAllocator(new TlsfAllocator.Heap(ARENA_BYTES));
        }

        private GpuBuffer create(GpuDevice device, long size) {
            return device.createBuffer(() -> "Quicksilver terrain " + index + " (" + LAYER_NAMES[layer] + ")",
                GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_INDEX | GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_COPY_SRC, size);
        }

        /**
         * Makes the buffer reach {@code end} bytes: doubles it (up to the arena's full size) and copies what it holds
         * into the bigger one, once; the old buffer is closed before the next frame's draws.
         */
        void ensure(CommandEncoder encoder, long end) {
            if (end <= capacity) return;
            long size = capacity;
            while (size < end) size *= 2;
            size = Math.min(size, ARENA_BYTES);
            GpuBuffer bigger = create(RenderSystem.getDevice(), size);
            try {
                encoder.copyToBuffer(buffer.slice(0, capacity), bigger.slice(0, capacity));
            } catch (Throwable t) {
                bigger.close();
                throw t;
            }
            PENDING_CLOSE.add(buffer);
            buffer = bigger;
            capacity = size;
            TerrainStats.ARENA_GROWS.incrementAndGet();
        }
    }

    static final class Region {
        final int originX, originY, originZ;
        final long key;
        int live;
        /** Per layer, the arena this region's sections of that layer go to first. */
        final Arena[] preferred = new Arena[LAYERS];
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
        /** Solid and cutout: the layer's quads grouped by facing (package 4b); null for translucent. */
        final CompactVertex.Faces faces;

        Part(Arena arena, TlsfAllocator.Allocation allocation, int vertices, CompactVertex.Faces faces) {
            this.arena = arena;
            this.allocation = allocation;
            this.baseVertex = (int) (allocation.getOffsetFromHeap() / CompactVertex.BYTES);
            this.vertices = vertices;
            this.faces = faces;
        }

        int indexCount() {
            return vertices / 4 * 6;
        }
    }

    /** A translucent layer's sorted index list (vanilla's indices, 32-bit), in the same arena as the layer's vertices. */
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
     * A layer on its way in. {@code split} null: an index list only (vanilla sorted a translucent layer Quicksilver
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

        /** Bytes this copies into an arena (vertices and index list). */
        long bytes() {
            return (split == null ? 0 : split.data().remaining()) + (indices == null ? 0 : indices.remaining());
        }
    }

    public record Snapshot(int regions, int arenas, long arenaBytes, long usedBytes, long vertices) {
    }

    private static final List<Arena> ARENAS = new ArrayList<>();
    private static final Long2ObjectOpenHashMap<Region> REGIONS = new Long2ObjectOpenHashMap<>();
    /** Live meshes: index 0 solid, 1 cutout, 2 translucent. */
    static final Map<CompiledSectionMesh, Entry[]> ENTRIES = new IdentityHashMap<>();
    static final int LAYERS = 3, TRANSLUCENT = 2;
    private static final String[] LAYER_NAMES = {"solid", "cutout", "translucent"};
    /** Meshes whose translucent layer Quicksilver held and which vanilla has let go of since: a late sort of one is dropped. */
    private static final Set<CompiledSectionMesh> RELEASED_TRANSLUCENT = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    private static final List<Staged> STAGED = new ArrayList<>();
    /** Staged meshes of sections seen in the visible list while waiting: the upload frame they were first seen in. */
    private static final Map<CompiledSectionMesh, Integer> VISIBLE_SINCE = new IdentityHashMap<>();
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
            REFUSED.clear();
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
            // a translucent layer after the hand-back (checked under the lock that releaseTranslucent takes) stays with
            // vanilla, so no section's translucent layer outlives the hand-back in Quicksilver
            if (gen != generation || !accepting || !Terrain.takingMeshes()
                || (layer == ChunkSectionLayer.TRANSLUCENT && !Terrain.takingTranslucent())) {
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
     * Worker thread: vanilla sorted a mesh's translucent layer again. If Quicksilver holds that layer, the new index list
     * ({@code ints}: vanilla's list as 32-bit indices, owned by the store from here on, freed if not staged) replaces the old one at the end of the frame; if Quicksilver held it and vanilla has since let go of
     * the mesh, the list is dropped (vanilla would draw nothing of it either); otherwise it is vanilla's.
     */
    static Resort stageIndices(int gen, CompiledSectionMesh mesh, ByteBuffer ints, int count, TranslucencyCheck.Sorted sorted) {
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
            if (!held || gen != generation || !accepting || !Terrain.takingMeshes()) {
                MemoryUtil.memFree(ints);
                if (!held) return RELEASED_TRANSLUCENT.contains(mesh) ? Resort.DROPPED : Resort.NOT_OURS;
                return Resort.DROPPED;
            }
            STAGED.add(new Staged(null, mesh, ChunkSectionLayer.TRANSLUCENT, 0, 0, 0, null, ints, IndexType.INT, count, sorted));
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

    /** A solid or cutout layer: into the region's arena for that layer, another arena of that layer, or a new one. */
    private static Part allocate(GpuDevice device, CommandEncoder encoder, Region region, int layer, ByteBuffer data, int vertices,
                                 CompactVertex.Faces faces) {
        int stride = CompactVertex.BYTES;
        int bytes = data.remaining();
        Arena pref = region.preferred[layer];
        Arena arena = null;
        TlsfAllocator.Allocation a = null;
        if (pref != null && !pref.closed) {
            a = pref.allocator.allocate(bytes, stride);
            if (a != null) arena = pref;
        }
        for (int i = 0; a == null && i < ARENAS.size(); i++) {
            Arena c = ARENAS.get(i);
            if (c == pref || c.layer != layer) continue;
            a = c.allocator.allocate(bytes, stride);
            if (a != null) arena = c;
        }
        if (a == null) {
            arena = newArena(device, layer);
            a = arena.allocator.allocate(bytes, stride);
            if (a == null) throw new IllegalStateException("a fresh terrain arena refused " + bytes + " bytes");
        }
        if (pref == null || pref.closed || region.live <= 1) region.preferred[layer] = arena;
        return write(encoder, arena, a, data, vertices, faces);
    }

    private static Arena newArena(GpuDevice device, int layer) {
        Arena arena = new Arena(device, ARENAS.size(), layer);
        ARENAS.add(arena);
        return arena;
    }

    private static Part write(CommandEncoder encoder, Arena arena, TlsfAllocator.Allocation a, ByteBuffer data, int vertices,
                              CompactVertex.Faces faces) {
        try {
            arena.ensure(encoder, a.getOffsetFromHeap() + a.getSize());
            encoder.writeToBuffer(arena.buffer.slice(a.getOffsetFromHeap(), data.remaining()), data);
        } catch (Throwable t) {
            arena.allocator.free(a);
            throw t;
        }
        arena.used += a.getSize();
        return new Part(arena, a, vertices, faces);
    }

    /**
     * A translucent layer: its vertices and its index list go into one translucent arena (the oldest with room for
     * both, or a new one), so one draw binds a single buffer for both, and consecutive translucent sections in vanilla's
     * order share an arena until the translucent layers alone pass one arena.
     */
    private static void allocateTranslucent(GpuDevice device, CommandEncoder encoder, Region region, Entry e, ByteBuffer vertices,
                                            int vertexCount, ByteBuffer indices, IndexType type, int indexCount) {
        int stride = CompactVertex.BYTES;
        int vbytes = vertices.remaining(), ibytes = indices.remaining();
        List<Arena> order = new ArrayList<>(ARENAS.size());
        for (Arena c : ARENAS) if (c.layer == TRANSLUCENT) order.add(c);
        for (int pass = 0; pass < 2; pass++) {
            if (pass == 1) order = List.of(newArena(device, TRANSLUCENT));
            for (Arena arena : order) {
                TlsfAllocator.Allocation va = arena.allocator.allocate(vbytes, stride);
                if (va == null) continue;
                TlsfAllocator.Allocation ia = arena.allocator.allocate(ibytes, 4);
                if (ia == null) {
                    arena.allocator.free(va);
                    continue;
                }
                Part p;
                try {
                    p = write(encoder, arena, va, vertices, vertexCount, null);
                } catch (Throwable t) {
                    arena.allocator.free(ia);
                    throw t;
                }
                e.part = p;
                try {
                    arena.ensure(encoder, ia.getOffsetFromHeap() + ia.getSize());
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

    /**
     * Render thread, end of frame (inside vanilla's terrain upload): copies staged meshes into the arenas, under the
     * frame's upload budget ({@link UploadBudget}, package 4b): meshes in priority order until the budget is spent;
     * the rest stay staged for the next frame, in their staging order. New index lists for translucent layers already
     * in are written at once.
     */
    static void upload() {
        synchronized (LOCK) {
            UploadBudget.tick();
            if (STAGED.isEmpty()) {
                UploadBudget.frame(0, 0, 0, 0, UploadBudget.bytes(), 0);
                return;
            }
            long t0 = System.nanoTime();
            List<Staged> all = new ArrayList<>(STAGED);
            STAGED.clear();
            // one group per mesh: its layers, then its new index lists, in staging order; index lists for meshes
            // already in go at once
            Map<CompiledSectionMesh, List<Staged>> byMesh = new IdentityHashMap<>();
            List<List<Staged>> groups = new ArrayList<>();
            List<Staged> loose = new ArrayList<>();
            for (Staged s : all) {
                List<Staged> g = byMesh.get(s.mesh);
                if (g == null) {
                    if (s.indicesOnly()) {
                        loose.add(s);
                        continue;
                    }
                    g = new ArrayList<>(2);
                    byMesh.put(s.mesh, g);
                    groups.add(g);
                }
                g.add(s);
            }
            int n = groups.size();
            double[] key = new double[n];
            Integer[] order = new Integer[n];
            for (int i = 0; i < n; i++) {
                Staged f = groups.get(i).get(0);
                key[i] = UploadBudget.priority(f.x, f.y, f.z);
                order[i] = i;
            }
            Arrays.sort(order, (a, b) -> Double.compare(key[a], key[b])); // stable: staging order on ties
            long budget = UploadBudget.bytes(), time = UploadBudget.nanos();
            long staged = 0;
            for (List<Staged> g : groups) for (Staged st : g) staged += st.bytes();
            // staged meshes are CPU memory: past the cap the budget takes the excess too (review N3)
            if (staged > UploadBudget.STAGED_CAP) budget = Math.max(budget, staged - UploadBudget.STAGED_CAP);
            int frame = UploadBudget.frameNumber();
            boolean full = false;
            int forced = 0;
            List<Staged> done = new ArrayList<>(all.size());
            Set<Staged> left = java.util.Collections.newSetFromMap(new IdentityHashMap<>());
            Set<Staged> processed = java.util.Collections.newSetFromMap(new IdentityHashMap<>());
            long bytes = 0;
            int meshes = 0, exempt = 0;
            boolean tookOne = false;
            Staged current = null;
            try {
                GpuDevice device = RenderSystem.getDevice();
                CommandEncoder encoder = device.createCommandEncoder();
                for (Staged s : loose) {
                    current = s;
                    bytes += s.bytes();
                    rewriteIndices(device, encoder, s);
                    s.free();
                    processed.add(s);
                    current = null;
                }
                for (int k = 0; k < n; k++) {
                    List<Staged> g = groups.get(order[k]);
                    Staged f = g.get(0);
                    boolean camera = UploadBudget.exempt(f.x, f.y, f.z);
                    // a visible section waits at most MAX_VISIBLE_WAIT_FRAMES frames from when it was first seen visible
                    boolean visible = UploadBudget.visible(f.x, f.y, f.z);
                    int waited = 0;
                    if (visible) {
                        Integer since = VISIBLE_SINCE.putIfAbsent(f.mesh, frame);
                        waited = since == null ? 0 : frame - since;
                    }
                    boolean overdue = visible && waited >= UploadBudget.MAX_VISIBLE_WAIT_FRAMES;
                    long gb = 0;
                    for (Staged s : g) gb += s.bytes();
                    if (!full && !camera && tookOne && (bytes + gb > budget || System.nanoTime() - t0 > time)) full = true;
                    if (full && !camera && !overdue) {
                        // over this frame's budget: this mesh waits (camera sections and overdue visible ones still go in)
                        left.addAll(g);
                        continue;
                    }
                    if (full && overdue) forced++;
                    VISIBLE_SINCE.remove(f.mesh);
                    if (visible) UploadBudget.visibleWait(waited);
                    for (Staged s : g) {
                        current = s;
                        if (s.indicesOnly()) {
                            rewriteIndices(device, encoder, s);
                        } else {
                            copyIn(device, encoder, s);
                            done.add(s);
                        }
                        s.free();
                        processed.add(s);
                        current = null;
                    }
                    bytes += gb;
                    meshes++;
                    if (camera) exempt++;
                    else tookOne = true;
                }
            } catch (Throwable t) {
                // the item that failed is freed (its allocations were released where they failed); everything not yet
                // copied goes back to the staged list, so the module's hand-back completes it through vanilla's callback
                if (current != null) current.free();
                for (Staged s : all) {
                    if (s != current && !processed.contains(s)) left.add(s);
                }
                for (Staged s : all) if (left.contains(s)) STAGED.add(s);
                // the layers fully copied in are complete: vanilla's callback for them, as on success
                try {
                    uploaded(done);
                } catch (Throwable u) {
                    t.addSuppressed(u);
                }
                throw t;
            }
            // what waits goes back in staging order (new meshes staged later come after it)
            for (Staged s : all) if (left.contains(s)) STAGED.add(s);
            UploadBudget.forced(forced, staged);
            UploadBudget.frame(bytes, meshes, exempt, groupsWaiting(), budget, System.nanoTime() - t0);
            uploaded(done);
        }
    }

    /**
     * Vanilla's own "uploaded" callback for each layer copied in, which swaps the section's new mesh in once all its
     * layers are there and releases the old one (that re-enters the lock on this thread). A mesh vanilla let go of
     * meanwhile (a cancelled task) gets no callback, as vanilla skips its own uploads for it.
     */
    private static void uploaded(List<Staged> done) {
        for (Staged s : done) {
            Entry[] es = ENTRIES.get(s.mesh);
            if (es == null || es[layerIndex(s.layer)] == null) continue;
            RenderSectionInvoker section = (RenderSectionInvoker) s.section;
            if (s.section.getSectionMesh().getCompileTaskStartTime() > s.mesh.getCompileTaskStartTime()) {
                // a newer mesh of this section is already in: this one must not replace it, it just goes
                section.quicksilver$releaseSectionMesh(s.mesh);
                continue;
            }
            section.quicksilver$vertexBufferUploaded(s.mesh, s.layer);
        }
    }

    /** Meshes still staged (under the lock). */
    private static int groupsWaiting() {
        if (STAGED.isEmpty()) return 0;
        Set<CompiledSectionMesh> meshes = java.util.Collections.newSetFromMap(new IdentityHashMap<>());
        for (Staged s : STAGED) if (!s.indicesOnly()) meshes.add(s.mesh);
        return meshes.size();
    }

    /** One staged layer into the arenas (under the lock, render thread). */
    private static void copyIn(GpuDevice device, CommandEncoder encoder, Staged s) {
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
            e.part = allocate(device, encoder, region, layerIndex(s.layer), v, count, s.split.faces());
        }
        liveVertices += count;
    }

    /** A new index list for a translucent layer Quicksilver holds: written over the old one (same size), in its place. */
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
            VISIBLE_SINCE.remove(mesh);
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
        if (!a.closed && a.allocator.isCompletelyFree() && ARENAS.stream().anyMatch(o -> o != a && o.layer == a.layer)) {
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
    /**
     * Package 4c: layers Quicksilver's mesher wrote that the store refused (terrain going back to vanilla, or the store
     * emptied since the section was meshed). Vanilla can't draw their vertices, so at the end of the frame each mesh is
     * completed without that layer (or let go of, if a newer mesh of its section is in) and its section meshed again.
     */
    private static final List<Staged> REFUSED = new ArrayList<>();

    /** A layer of Quicksilver's mesher the store did not take (any thread; see {@link #REFUSED}). */
    static void refuse(SectionRenderDispatcher.RenderSection section, CompiledSectionMesh mesh, ChunkSectionLayer layer, int x, int y, int z) {
        synchronized (LOCK) {
            if (!accepting) return;
            REFUSED.add(new Staged(section, mesh, layer, x, y, z, null, null, null, 0, null));
        }
    }

    /** Render thread, end of frame: completes the refused layers' meshes; returns their sections (to be meshed again). */
    static LongOpenHashSet finishRefused() {
        LongOpenHashSet sections = new LongOpenHashSet();
        List<Staged> refused;
        synchronized (LOCK) {
            if (REFUSED.isEmpty()) return sections;
            refused = new ArrayList<>(REFUSED);
            REFUSED.clear();
        }
        for (Staged s : refused) {
            sections.add(SectionPos.asLong(s.x >> 4, s.y >> 4, s.z >> 4));
            RenderSectionInvoker section = (RenderSectionInvoker) s.section;
            if (s.section.getSectionMesh().getCompileTaskStartTime() > s.mesh.getCompileTaskStartTime()) {
                section.quicksilver$releaseSectionMesh(s.mesh);
            } else {
                section.quicksilver$vertexBufferUploaded(s.mesh, s.layer);
            }
        }
        return sections;
    }
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
            VISIBLE_SINCE.clear();
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
                    section.quicksilver$releaseSectionMesh(s.mesh);
                } else {
                    section.quicksilver$vertexBufferUploaded(s.mesh, s.layer);
                }
            }
        }
        return sections;
    }

    /** The sections Quicksilver holds, as SectionPos longs (for handing them back to vanilla). */
    /**
     * Render thread, end of frame: lets go of every translucent layer Quicksilver holds or has staged (translucent terrain
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
            List<Staged> unfinished = new ArrayList<>();
            STAGED.removeIf(st -> {
                if (st.layer != ChunkSectionLayer.TRANSLUCENT) return false;
                if (!st.indicesOnly()) {
                    out.add(SectionPos.asLong(st.x >> 4, st.y >> 4, st.z >> 4));
                    unfinished.add(st);
                }
                st.free();
                return true;
            });
            // vanilla counted those layers as Quicksilver's: complete each through vanilla's own callback (its section
            // shows no translucent layer until vanilla meshes it again, it is marked dirty) or, if a newer mesh of the
            // section is already in, release it, as for a hand-back (finishDropped). Never left half-uploaded.
            for (Staged st : unfinished) {
                RELEASED_TRANSLUCENT.add(st.mesh);
                RenderSectionInvoker section = (RenderSectionInvoker) st.section;
                if (st.section.getSectionMesh().getCompileTaskStartTime() > st.mesh.getCompileTaskStartTime()) {
                    section.quicksilver$releaseSectionMesh(st.mesh);
                } else {
                    section.quicksilver$vertexBufferUploaded(st.mesh, st.layer);
                }
            }
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
            VISIBLE_SINCE.clear();
            DROPPED.clear();
            ENTRIES.clear();
            RELEASED_TRANSLUCENT.clear();
            for (Region r : REGIONS.values()) if (r.ubo != null) PENDING_CLOSE.add(r.ubo);
            REGIONS.clear();
            for (Arena a : ARENAS) {
                a.closed = true;
                PENDING_CLOSE.add(a.buffer);
            }
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
            long reserved = 0;
            for (Arena a : ARENAS) reserved += a.capacity;
            return new Snapshot(REGIONS.size(), ARENAS.size(), reserved, used, liveVertices);
        }
    }
}
