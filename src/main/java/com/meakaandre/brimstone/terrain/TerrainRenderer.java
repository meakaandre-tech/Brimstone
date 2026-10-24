package com.meakaandre.brimstone.terrain;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.device.DeviceFeatures;
import com.mojang.renderpearl.api.device.DeviceInfo;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.DynamicGpuData;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.util.Util;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;

/**
 * Draws Brimstone's terrain, through renderpearl only (both backends). Per frame:
 *
 * <ul>
 * <li>{@link #prepare}, on the render thread as vanilla starts preparing its own terrain draws (before the frame
 * graph runs, no render pass open): picks the sections vanilla found visible whose layers Brimstone holds, builds one
 * draw list per layer, and writes the little that changed (a region's fade-in values).</li>
 * <li>{@link #draw}, inside vanilla's terrain pass right after vanilla draws its own part of each layer (solid, then
 * cutout), with vanilla's uniforms and samplers already bound.</li>
 * </ul>
 *
 * How it draws follows what vanilla does on the device:
 * <ul>
 * <li>INDIRECT, on frames where vanilla draws terrain with multi-draw indirect (vanilla trusts the driver with it):
 * one indirect multi-draw per layer and arena; section data in the same instance stream and command upload as
 * vanilla's.</li>
 * <li>PLAIN, everywhere else (Intel integrated graphics, where vanilla distrusts multi-draw indirect; drivers without
 * it): one direct multi-draw per layer per region of 8x4x8 sections, section origins from a small uniform block per
 * region that is only rewritten when a section's fade-in value changes. OpenGL always has these multi-draws
 * (glMultiDrawElementsBaseVertex, core since 3.2); on Vulkan the interleaved kind (VK_EXT_multi_draw) is used when the
 * device has it; with neither, one draw per section and layer, as vanilla does.</li>
 * </ul>
 */
public final class TerrainRenderer {
    enum Path { PLAIN_SEPARATE, PLAIN_INTERLEAVED, PLAIN_SINGLE, INDIRECT }

    /** One region's sections in one arena, for one layer (plain path). */
    private static final class RegionDraw {
        final TerrainStore.Region region;
        /** The region's uniform block as of this frame (a worker may drop the region before the draw). */
        GpuBuffer ubo;
        int[] counts = new int[16];
        int[] bases = new int[16];
        int n;

        RegionDraw(TerrainStore.Region region) {
            this.region = region;
        }

        void add(int count, int base) {
            if (n == counts.length) {
                counts = Arrays.copyOf(counts, n * 2);
                bases = Arrays.copyOf(bases, n * 2);
            }
            counts[n] = count;
            bases[n] = base;
            n++;
        }
    }

    /** Everything one layer draws from one arena. */
    private static final class ArenaDraws {
        final TerrainStore.Arena arena;
        final List<RegionDraw> regions = new ArrayList<>();
        final Map<TerrainStore.Region, RegionDraw> byRegion = new IdentityHashMap<>();
        final List<DynamicGpuData.IndexedDraw> commands = new ArrayList<>();
        GpuBufferSlice commandSlice;

        ArenaDraws(TerrainStore.Arena arena) {
            this.arena = arena;
        }
    }

    // this frame
    private static Path path;
    private static boolean prepared;
    @SuppressWarnings("unchecked")
    private static final List<ArenaDraws>[] FRAME = new List[] {new ArrayList<ArenaDraws>(), new ArrayList<ArenaDraws>()};
    private static final List<DynamicGpuData.ChunkSectionInfo> INSTANCES = new ArrayList<>();
    private static GpuBufferSlice instanceSlice;
    private static int vanillaCommandLists;
    private static int vanillaInstances;
    private static int maxIndexCount;
    private static Path lastPath;

    // reused CPU-side arrays for direct multi-draws (render thread only)
    private static IntBuffer counts = MemoryUtil.memAllocInt(1024);
    private static IntBuffer bases = MemoryUtil.memAllocInt(1024);
    private static IntBuffer interleaved = MemoryUtil.memAllocInt(3 * 1024);
    private static PointerBuffer zeroOffsets = MemoryUtil.memCallocPointer(1024);
    private static ByteBuffer uboScratch;

    private TerrainRenderer() {
    }

    private static Path plainPath(DeviceInfo info) {
        DeviceFeatures f = info.features();
        if (f.multiDrawDirectSeparate()) return Path.PLAIN_SEPARATE;
        if (f.multiDrawDirectInterleaved() && info.limits().maxMultiDrawDirectInterleavedDrawCount() > 1) return Path.PLAIN_INTERLEAVED;
        return Path.PLAIN_SINGLE;
    }

    static String plainPathName() {
        try {
            return name(plainPath(RenderSystem.getDevice().getDeviceInfo()));
        } catch (Throwable t) {
            return "unknown";
        }
    }

    static String lastPathName() {
        Path p = lastPath;
        return p == null ? "none" : name(p);
    }

    private static String name(Path p) {
        return switch (p) {
            case PLAIN_SEPARATE -> "plain, one multi-draw per region and layer";
            case PLAIN_INTERLEAVED -> "plain, one interleaved multi-draw per region and layer";
            case PLAIN_SINGLE -> "plain, one draw per section and layer";
            case INDIRECT -> "indirect, one multi-draw per arena and layer";
        };
    }

    /**
     * Render thread, as vanilla starts preparing this frame's terrain draws. {@code indirect}: vanilla draws terrain
     * with multi-draw indirect this frame.
     */
    public static void prepare(List<SectionRenderDispatcher.RenderSection> visible, double fadeSeconds, boolean indirect) {
        prepared = false;
        Terrain.activateIfNeeded();
        if (!Terrain.active()) return;
        try {
            prepareFrame(visible, fadeSeconds, indirect);
            prepared = true;
        } catch (Throwable t) {
            Terrain.fail("preparing terrain draws", t);
        }
    }

    private static void prepareFrame(List<SectionRenderDispatcher.RenderSection> visible, double fadeSeconds, boolean indirect) {
        // after a resource reload the pipelines are compiled again here, before any render pass is open
        String broken = TerrainPipelines.compileAll();
        if (broken != null) throw new IllegalStateException(broken);
        GpuDevice device = RenderSystem.getDevice();
        path = indirect ? Path.INDIRECT : plainPath(device.getDeviceInfo());
        boolean plain = path != Path.INDIRECT;
        FRAME[0].clear();
        FRAME[1].clear();
        INSTANCES.clear();
        instanceSlice = null;
        vanillaCommandLists = -1;
        maxIndexCount = 0;
        long now = Util.getMillis();
        long fadeMs = Util.toMillis(fadeSeconds);
        List<TerrainStore.Region> touched = new ArrayList<>();

        synchronized (TerrainStore.LOCK) {
            TerrainStore.closePending();
            for (SectionRenderDispatcher.RenderSection section : visible) {
                TerrainStore.Entry[] es = TerrainStore.ENTRIES.get(section.getSectionMesh());
                if (es == null) continue;
                TerrainStore.Entry any = es[0] != null ? es[0] : es[1];
                if (any == null) continue;
                float visibility = section.getVisibility(now, fadeMs);
                int instance = INSTANCES.size();
                if (plain) {
                    TerrainStore.Region r = any.region;
                    if (!r.touched) {
                        r.touched = true;
                        touched.add(r);
                    }
                    r.pending[any.slot] = visibility;
                } else {
                    INSTANCES.add(new DynamicGpuData.ChunkSectionInfo(any.originX, any.originY, any.originZ, visibility));
                }
                for (int li = 0; li < 2; li++) {
                    TerrainStore.Entry e = es[li];
                    if (e == null) continue;
                    ArenaDraws ad = arenaDraws(li, e.arena);
                    int count = e.indexCount();
                    maxIndexCount = Math.max(maxIndexCount, count);
                    if (plain) {
                        RegionDraw rd = ad.byRegion.get(e.region);
                        if (rd == null) {
                            rd = new RegionDraw(e.region);
                            ad.byRegion.put(e.region, rd);
                            ad.regions.add(rd);
                        }
                        rd.add(count, e.baseVertex);
                    } else {
                        ad.commands.add(new DynamicGpuData.IndexedDraw(count, 1, 0, e.baseVertex, instance));
                    }
                }
            }
            CommandEncoder encoder = null;
            for (TerrainStore.Region r : touched) {
                r.touched = false;
                encoder = writeRegion(device, encoder, r);
            }
            for (List<ArenaDraws> l : FRAME) for (ArenaDraws ad : l) for (RegionDraw rd : ad.regions) rd.ubo = rd.region.ubo;
        }
        if (maxIndexCount > 0) RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS).requestIndexCount(maxIndexCount);
    }

    private static ArenaDraws arenaDraws(int li, TerrainStore.Arena arena) {
        for (ArenaDraws ad : FRAME[li]) if (ad.arena == arena) return ad;
        ArenaDraws ad = new ArenaDraws(arena);
        FRAME[li].add(ad);
        return ad;
    }

    /** Writes a region's uniform block if it is new or a section's fade-in value changed; clears what was pending. */
    private static CommandEncoder writeRegion(GpuDevice device, CommandEncoder encoder, TerrainStore.Region r) {
        boolean changed = r.ubo == null;
        if (r.ubo == null) {
            r.ubo = device.createBuffer(() -> "Brimstone terrain region", GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST,
                TerrainPipelines.REGION_UBO_BYTES);
        }
        for (int i = 0; i < 256 && !changed; i++) {
            float v = r.pending[i];
            if (!Float.isNaN(v) && Float.compare(v, r.written[i]) != 0) changed = true;
        }
        if (changed) {
            if (uboScratch == null) uboScratch = MemoryUtil.memAlloc(TerrainPipelines.REGION_UBO_BYTES);
            ByteBuffer b = uboScratch;
            b.clear();
            b.putInt(0, r.originX).putInt(4, r.originY).putInt(8, r.originZ).putInt(12, 0);
            for (int i = 0; i < 256; i++) {
                float v = r.pending[i];
                if (Float.isNaN(v)) v = Float.isNaN(r.written[i]) ? 1f : r.written[i];
                r.written[i] = v;
                b.putFloat(16 + i * 4, v);
            }
            if (encoder == null) encoder = device.createCommandEncoder();
            encoder.writeToBuffer(r.ubo.slice(), b.position(0).limit(TerrainPipelines.REGION_UBO_BYTES));
        }
        Arrays.fill(r.pending, Float.NaN);
        return encoder;
    }

    /**
     * Vanilla, preparing multi-draw indirect, writes its indirect commands: Brimstone's lists go into the same upload.
     * Brimstone's sections will follow vanilla's in the instance stream, so their base instances move past vanilla's
     * (every vanilla section has at least one draw, so the highest base instance tells how many there are).
     */
    public static List<List<DynamicGpuData.IndexedDraw>> withCommands(List<List<DynamicGpuData.IndexedDraw>> vanilla) {
        if (!prepared || path != Path.INDIRECT) return vanilla;
        try {
            return addCommands(vanilla);
        } catch (Throwable t) {
            prepared = false;
            Terrain.fail("preparing indirect terrain draws", t);
            return vanilla;
        }
    }

    private static List<List<DynamicGpuData.IndexedDraw>> addCommands(List<List<DynamicGpuData.IndexedDraw>> vanilla) {
        int shift = 0;
        for (List<DynamicGpuData.IndexedDraw> l : vanilla) for (DynamicGpuData.IndexedDraw d : l) shift = Math.max(shift, d.baseInstance() + 1);
        vanillaInstances = shift;
        vanillaCommandLists = vanilla.size();
        List<List<DynamicGpuData.IndexedDraw>> all = new ArrayList<>(vanilla);
        for (List<ArenaDraws> l : FRAME) {
            for (ArenaDraws ad : l) {
                if (shift != 0) {
                    for (int i = 0; i < ad.commands.size(); i++) {
                        DynamicGpuData.IndexedDraw d = ad.commands.get(i);
                        ad.commands.set(i, new DynamicGpuData.IndexedDraw(d.indexCount(), 1, 0, d.baseVertex(), d.baseInstance() + shift));
                    }
                }
                all.add(ad.commands);
            }
        }
        return all;
    }

    /** Takes the slices of Brimstone's command lists; hands vanilla back the slices of its own. */
    public static GpuBufferSlice[] commandsWritten(GpuBufferSlice[] all, int vanillaLists) {
        if (!prepared || path != Path.INDIRECT || vanillaCommandLists != vanillaLists) return all;
        int k = vanillaCommandLists;
        for (List<ArenaDraws> l : FRAME) for (ArenaDraws ad : l) ad.commandSlice = all[k++];
        return Arrays.copyOf(all, vanillaCommandLists);
    }

    /** Vanilla writes its section instance data: Brimstone's goes into the same upload, after vanilla's. */
    public static List<DynamicGpuData.ChunkSectionInfo> withInstances(List<DynamicGpuData.ChunkSectionInfo> vanilla) {
        if (!prepared || path != Path.INDIRECT || INSTANCES.isEmpty()) return vanilla;
        if (vanillaCommandLists < 0 || vanilla.size() != vanillaInstances) {
            prepared = false;
            Terrain.fail("preparing indirect terrain draws", new IllegalStateException(
                "vanilla wrote " + vanilla.size() + " section instances, its commands use " + vanillaInstances));
            return vanilla;
        }
        List<DynamicGpuData.ChunkSectionInfo> all = new ArrayList<>(vanilla.size() + INSTANCES.size());
        all.addAll(vanilla);
        all.addAll(INSTANCES);
        return all;
    }

    public static void instancesWritten(GpuBufferSlice slice) {
        if (prepared && path == Path.INDIRECT) instanceSlice = slice;
    }

    /**
     * Render thread, in vanilla's terrain pass right after vanilla drew its own part of {@code layer}. Vanilla has
     * bound the terrain uniforms, the block atlas and the lightmap.
     */
    public static void draw(ChunkSectionLayer layer, RenderPass pass, boolean wireframe) {
        if (!prepared || !Terrain.active()) return;
        int li = TerrainStore.layerIndex(layer);
        if (li < 0) return;
        List<ArenaDraws> list = FRAME[li];
        if (list.isEmpty()) return;
        try {
            boolean plain = path != Path.INDIRECT;
            CompiledRenderPipeline pipeline;
            if (wireframe) {
                pipeline = TerrainPipelines.wireframe(plain);
                if (pipeline == null) return;
            } else {
                pipeline = TerrainPipelines.compiled(plain
                    ? (li == 0 ? TerrainPipelines.PLAIN_SOLID : TerrainPipelines.PLAIN_CUTOUT)
                    : (li == 0 ? TerrainPipelines.INSTANCED_SOLID : TerrainPipelines.INSTANCED_CUTOUT));
            }
            RenderSystem.AutoStorageIndexBuffer quads = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
            GpuBuffer indexBuffer = quads.getBuffer();
            if (indexBuffer == null || !quads.hasStorage(maxIndexCount)) throw new IllegalStateException("the shared quad index buffer is too small");
            pass.setPipeline(pipeline);
            pass.setIndexBuffer(indexBuffer, quads.type());
            int draws = plain ? drawPlain(pass, list) : drawIndirect(pass, list);
            lastPath = path;
            int sectionLayers = 0;
            for (ArenaDraws ad : list) {
                sectionLayers += ad.commands.size();
                for (RegionDraw rd : ad.regions) sectionLayers += rd.n;
            }
            TerrainStats.drew(draws, sectionLayers);
        } catch (Throwable t) {
            prepared = false;
            Terrain.fail("drawing terrain", t);
        }
    }

    private static int drawPlain(RenderPass pass, List<ArenaDraws> list) {
        int draws = 0;
        int maxInterleaved = path == Path.PLAIN_INTERLEAVED
            ? RenderSystem.getDevice().getDeviceInfo().limits().maxMultiDrawDirectInterleavedDrawCount() : 0;
        for (ArenaDraws ad : list) {
            pass.setVertexBuffer(0, ad.arena.buffer.slice());
            for (RegionDraw rd : ad.regions) {
                int n = rd.n;
                if (n == 0) continue;
                pass.setUniform("BrimRegion", rd.ubo);
                switch (path) {
                    case PLAIN_SEPARATE -> {
                        ensureCapacity(n);
                        counts.clear().put(rd.counts, 0, n).flip();
                        bases.clear().put(rd.bases, 0, n).flip();
                        zeroOffsets.clear().limit(n);
                        pass.multiDrawIndexed(zeroOffsets, counts, bases, n);
                        draws++;
                    }
                    case PLAIN_INTERLEAVED -> {
                        for (int done = 0; done < n; ) {
                            int m = Math.min(n - done, maxInterleaved);
                            ensureCapacity(m);
                            interleaved.clear();
                            for (int i = done; i < done + m; i++) interleaved.put(0).put(rd.counts[i]).put(rd.bases[i]);
                            interleaved.flip();
                            pass.multiDrawIndexed(interleaved, 1, 0, m);
                            done += m;
                            draws++;
                        }
                    }
                    default -> {
                        for (int i = 0; i < n; i++) {
                            pass.drawIndexed(rd.counts[i], 1, 0, rd.bases[i], 0);
                            draws++;
                        }
                    }
                }
            }
        }
        return draws;
    }

    private static int drawIndirect(RenderPass pass, List<ArenaDraws> list) {
        if (instanceSlice == null) throw new IllegalStateException("no section instance data this frame");
        int draws = 0;
        pass.setVertexBuffer(1, instanceSlice);
        int max = Math.max(1, RenderSystem.getDevice().getDeviceInfo().limits().maxDrawIndirectDrawCount());
        for (ArenaDraws ad : list) {
            int count = ad.commands.size();
            if (count == 0) continue;
            if (ad.commandSlice == null) throw new IllegalStateException("no indirect commands this frame");
            pass.setVertexBuffer(0, ad.arena.buffer.slice());
            GpuBuffer buffer = ad.commandSlice.buffer();
            long offset = ad.commandSlice.offset();
            for (int done = 0; done < count; ) {
                int n = Math.min(max, count - done);
                pass.drawIndexedIndirect(buffer.slice(offset + (long) done * RenderPass.INDIRECT_INDEXED_DRAW_SIZE,
                    (long) n * RenderPass.INDIRECT_INDEXED_DRAW_SIZE), n);
                done += n;
                draws++;
            }
        }
        return draws;
    }

    private static void ensureCapacity(int n) {
        if (counts.capacity() < n) {
            int c = Math.max(n, counts.capacity() * 2);
            MemoryUtil.memFree(counts);
            MemoryUtil.memFree(bases);
            MemoryUtil.memFree(zeroOffsets);
            MemoryUtil.memFree(interleaved);
            counts = MemoryUtil.memAllocInt(c);
            bases = MemoryUtil.memAllocInt(c);
            zeroOffsets = MemoryUtil.memCallocPointer(c);
            interleaved = MemoryUtil.memAllocInt(3 * c);
        }
    }
}
