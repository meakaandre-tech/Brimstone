package com.meakaandre.brimstone.terrain;

import com.meakaandre.brimstone.bench.GpuCounters;
import com.meakaandre.brimstone.visibility.VisibilityStats;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.device.DeviceFeatures;
import com.mojang.renderpearl.api.device.DeviceInfo;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.minecraft.client.renderer.DynamicGpuData;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionMesh;
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
 * cutout, then translucent), with vanilla's uniforms and samplers already bound; with Improved Transparency on,
 * translucent is drawn in each of vanilla's three stage passes, with that stage's pipeline.</li>
 * </ul>
 *
 * <p>Translucent (package 3) is drawn in exactly vanilla's order: vanilla's visible-section list, reversed (back to
 * front) on frames where vanilla sorts translucent terrain (Improved Transparency off), each section with vanilla's
 * own sorted index list. Brimstone's draw calls cover runs of consecutive sections in that order that share a
 * region (plain path) and arena, so batching never changes the order.
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
        /** Where this list's commands start in the frame's one Brimstone command list. */
        int commandStart;

        ArenaDraws(TerrainStore.Arena arena) {
            this.arena = arena;
        }
    }

    /** Consecutive translucent section layers (in vanilla's order) that one draw call covers. */
    private static final class TRun {
        final TerrainStore.Arena arena;
        final IndexType type;
        final TerrainStore.Region region;
        /** The region's uniform block as of this frame (plain path). */
        GpuBuffer ubo;
        int[] counts = new int[8];
        int[] bases = new int[8];
        /** Where each section layer's indices start in the arena, in bytes. */
        long[] offsets = new long[8];
        int n;
        final List<DynamicGpuData.IndexedDraw> commands = new ArrayList<>();
        int commandStart;
        /** CI's check only: the section layers this run draws, in order. */
        final List<CompiledSectionMesh> meshes = new ArrayList<>();
        final List<TerrainStore.Entry> entries = new ArrayList<>();

        TRun(TerrainStore.Arena arena, IndexType type, TerrainStore.Region region) {
            this.arena = arena;
            this.type = type;
            this.region = region;
        }

        boolean fits(TerrainStore.Arena arena, IndexType type, TerrainStore.Region region) {
            return this.arena == arena && this.type == type && this.region == region;
        }

        void add(int count, int base, long offset) {
            if (n == counts.length) {
                counts = Arrays.copyOf(counts, n * 2);
                bases = Arrays.copyOf(bases, n * 2);
                offsets = Arrays.copyOf(offsets, n * 2);
            }
            counts[n] = count;
            bases[n] = base;
            offsets[n] = offset;
            n++;
        }
    }

    // this frame
    private static Path path;
    /** This frame's translucent draws, in vanilla's order, and whether vanilla sorts translucent this frame. */
    private static final List<TRun> TRUNS = new ArrayList<>();
    private static boolean sortTranslucent;
    // per frame, reused: translucent layers in vanilla's visible order, their meshes and instances
    private static final List<TerrainStore.Entry> TL_ENTRIES = new ArrayList<>();
    private static final List<CompiledSectionMesh> TL_MESHES = new ArrayList<>();
    private static final it.unimi.dsi.fastutil.ints.IntArrayList TL_INSTANCES = new it.unimi.dsi.fastutil.ints.IntArrayList();
    // visible sections whose translucent layer Brimstone doesn't hold (vanilla may)
    private static final List<SectionRenderDispatcher.RenderSection> TL_OTHER = new ArrayList<>();
    /** Sections sent back to vanilla's mesher so Brimstone takes their translucent layer over: node to that mesh's build time. */
    private static final Long2LongOpenHashMap HANDED_OVER = new Long2LongOpenHashMap();
    private static boolean prepared;
    @SuppressWarnings("unchecked")
    /** Draw lists, index layer (0 solid, 1 cutout). */
    private static final List<ArenaDraws>[] FRAME = new List[] {new ArrayList<ArenaDraws>(), new ArrayList<ArenaDraws>()};
    private static final List<DynamicGpuData.ChunkSectionInfo> INSTANCES = new ArrayList<>();
    private static GpuBufferSlice instanceSlice;
    /** This frame's one Brimstone command list (indirect path), all layers and arenas back to back, and its slice. */
    private static final List<DynamicGpuData.IndexedDraw> COMMANDS = new ArrayList<>();
    private static GpuBufferSlice commandSlice;
    private static int vanillaCommandLists;
    private static int vanillaInstances;
    private static int maxIndexCount;
    private static Path lastPath;
    /** Set the first time the draw hook (a MixinExtras wrap, applied late) runs. */
    static volatile boolean drawHookSeen;

    // reused CPU-side arrays for direct multi-draws (render thread only)
    private static IntBuffer counts = MemoryUtil.memAllocInt(1024);
    private static IntBuffer bases = MemoryUtil.memAllocInt(1024);
    private static IntBuffer interleaved = MemoryUtil.memAllocInt(3 * 1024);
    private static PointerBuffer zeroOffsets = MemoryUtil.memCallocPointer(1024);
    private static PointerBuffer offsets = MemoryUtil.memCallocPointer(1024);
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
     * with multi-draw indirect this frame; {@code sort}: vanilla draws translucent back to front this frame (Improved
     * Transparency off).
     */
    public static void prepare(List<SectionRenderDispatcher.RenderSection> visible, double fadeSeconds, boolean indirect, boolean sort) {
        prepared = false;
        Terrain.beginFrame();
        if (!Terrain.drawing()) return;
        try {
            sortTranslucent = sort;
            prepareFrame(visible, fadeSeconds, indirect && !Terrain.FORCE_PLAIN);
            prepared = true;
        } catch (Throwable t) {
            Terrain.fail("preparing terrain draws", t, false);
        }
    }

    private static void prepareFrame(List<SectionRenderDispatcher.RenderSection> visible, double fadeSeconds, boolean indirect) {
        // after a resource reload the pipelines are compiled again here, before any render pass is open
        String broken = TerrainPipelines.compileAll();
        if (broken != null) throw new IllegalStateException(broken);
        GpuDevice device = RenderSystem.getDevice();
        path = indirect ? Path.INDIRECT : plainPath(device.getDeviceInfo());
        boolean plain = path != Path.INDIRECT;
        for (List<ArenaDraws> l : FRAME) l.clear();
        TRUNS.clear();
        TL_ENTRIES.clear();
        TL_MESHES.clear();
        TL_INSTANCES.clear();
        TL_OTHER.clear();
        INSTANCES.clear();
        instanceSlice = null;
        COMMANDS.clear();
        commandSlice = null;
        vanillaCommandLists = -1;
        maxIndexCount = 0;
        long now = Util.getMillis();
        long fadeMs = Util.toMillis(fadeSeconds);
        List<TerrainStore.Region> touched = new ArrayList<>();

        boolean audit = GpuCounters.enabled;
        int audited = 0;
        AUDIT_MESHES.clear();
        AUDIT_LAYERS.clear();
        synchronized (TerrainStore.LOCK) {
            TerrainStore.closePending();
            for (SectionRenderDispatcher.RenderSection section : visible) {
                SectionMesh mesh = section.getSectionMesh();
                TerrainStore.Entry[] es = TerrainStore.ENTRIES.get(mesh);
                if (audit) audited += audit(mesh, es);
                TerrainStore.Entry tl = es == null ? null : es[TerrainStore.TRANSLUCENT];
                if (tl == null && mesh.getSectionDraw(ChunkSectionLayer.TRANSLUCENT) != null) TL_OTHER.add(section);
                if (es == null) continue;
                TerrainStore.Entry any = es[0] != null ? es[0] : es[1] != null ? es[1] : es[2];
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
                    {
                        TerrainStore.Part part = e.part;
                        if (part == null) continue;
                        ArenaDraws ad = arenaDraws(li, part.arena);
                        int count = part.indexCount();
                        maxIndexCount = Math.max(maxIndexCount, count);
                        if (plain) {
                            RegionDraw rd = ad.byRegion.get(e.region);
                            if (rd == null) {
                                rd = new RegionDraw(e.region);
                                ad.byRegion.put(e.region, rd);
                                ad.regions.add(rd);
                            }
                            rd.add(count, part.baseVertex);
                        } else {
                            ad.commands.add(new DynamicGpuData.IndexedDraw(count, 1, 0, part.baseVertex, instance));
                        }
                    }
                }
                if (tl != null && tl.index != null && mesh instanceof CompiledSectionMesh cm) {
                    TL_ENTRIES.add(tl);
                    TL_MESHES.add(cm);
                    TL_INSTANCES.add(instance);
                }
            }
            buildTranslucentRuns(plain);
            if (TranslucencyCheck.ENABLED) {
                List<CompiledSectionMesh> drawnMeshes = new ArrayList<>(TL_MESHES.size());
                List<TerrainStore.Entry> drawn = new ArrayList<>(TL_ENTRIES.size());
                for (TRun run : TRUNS) {
                    drawnMeshes.addAll(run.meshes);
                    drawn.addAll(run.entries);
                }
                TranslucencyCheck.frame(visible, sortTranslucent, drawnMeshes, drawn);
            }
            CommandEncoder encoder = null;
            for (TerrainStore.Region r : touched) {
                r.touched = false;
                encoder = writeRegion(device, encoder, r);
            }
            for (List<ArenaDraws> l : FRAME) for (ArenaDraws ad : l) for (RegionDraw rd : ad.regions) rd.ubo = rd.region.ubo;
            for (TRun run : TRUNS) if (run.region != null) run.ubo = run.region.ubo;
        }
        if (maxIndexCount > 0) RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS).requestIndexCount(maxIndexCount);
        if (audit) VisibilityStats.terrainFrame(audited, missingFromVanilla());
        translucentOthers();
    }

    /**
     * This frame's translucent runs, in vanilla's order: the visible list as is, or reversed when vanilla sorts. A run
     * goes on while the next section layer shares its arena, index type and (plain path) region.
     */
    private static void buildTranslucentRuns(boolean plain) {
        int n = TL_ENTRIES.size();
        TRun run = null;
        for (int k = 0; k < n; k++) {
            int i = sortTranslucent ? n - 1 - k : k;
            TerrainStore.Entry e = TL_ENTRIES.get(i);
            TerrainStore.Part part = e.part;
            TerrainStore.IndexPart ip = e.index;
            if (part == null || ip.arena != part.arena) throw new IllegalStateException("a translucent layer's vertices and indices are not in one arena");
            TerrainStore.Region region = plain ? e.region : null;
            if (run == null || !run.fits(part.arena, ip.type, region)) {
                run = new TRun(part.arena, ip.type, region);
                TRUNS.add(run);
            }
            run.add(ip.count, part.baseVertex, ip.offset());
            if (TranslucencyCheck.ENABLED) {
                run.meshes.add(TL_MESHES.get(i));
                run.entries.add(e);
            }
            if (!plain) {
                run.commands.add(new DynamicGpuData.IndexedDraw(ip.count, 1, (int) (ip.offset() / ip.type.bytes), part.baseVertex, TL_INSTANCES.getInt(i)));
            }
        }
    }

    /**
     * Visible sections with translucent geometry that Brimstone doesn't hold: vanilla draws them (meshed before
     * Brimstone turned on, or while it was handing terrain back). Vanilla draws its part of the layer before Brimstone's,
     * so in a frame with both the order between the two sets would not be vanilla's: such a section is sent back to
     * vanilla's mesher once, so that its next mesh comes to Brimstone. Counted in the bench (mixed frames).
     */
    private static void translucentOthers() {
        int vanillaHeld = 0, handed = 0;
        if (!TL_OTHER.isEmpty()) {
            SectionRenderDispatcher dispatcher = net.minecraft.client.Minecraft.getInstance().levelRenderer.sectionRenderDispatcher();
            if (dispatcher != null) {
                List<SectionRenderDispatcher.RenderSection> held = new ArrayList<>();
                dispatcher.lock();
                try {
                    for (SectionRenderDispatcher.RenderSection s : TL_OTHER) {
                        if (dispatcher.getRenderSectionSlice(s.getSectionMesh(), ChunkSectionLayer.TRANSLUCENT) != null) held.add(s);
                    }
                } finally {
                    dispatcher.unlock();
                }
                vanillaHeld = held.size();
                var extractor = net.minecraft.client.Minecraft.getInstance().levelExtractor;
                if (Terrain.takingTranslucent() && extractor != null) {
                    for (SectionRenderDispatcher.RenderSection s : held) {
                        long node = s.getSectionNode();
                        long built = s.getSectionMesh().getCompileTaskStartTime();
                        if (HANDED_OVER.containsKey(node) && HANDED_OVER.get(node) == built) continue;
                        HANDED_OVER.put(node, built);
                        extractor.setSectionDirty(net.minecraft.core.SectionPos.x(node), net.minecraft.core.SectionPos.y(node), net.minecraft.core.SectionPos.z(node));
                        handed++;
                    }
                }
            }
            TL_OTHER.clear();
        }
        if (HANDED_OVER.size() > 65536) HANDED_OVER.clear();
        TranslucencyStats.frame(TL_ENTRIES.size(), vanillaHeld, handed);
    }

    // pop-in audit (only while a bench measures): solid, cutout and translucent layers of vanilla's visible sections
    // that have geometry must be drawn by Brimstone (in this frame's draw lists) or by vanilla (in its buffers)
    private static final List<SectionMesh> AUDIT_MESHES = new ArrayList<>();
    private static final List<ChunkSectionLayer> AUDIT_LAYERS = new ArrayList<>();
    private static final ChunkSectionLayer[] AUDIT_LAYER_ORDER = {ChunkSectionLayer.SOLID, ChunkSectionLayer.CUTOUT, ChunkSectionLayer.TRANSLUCENT};

    /** Counts this section's layers with geometry; remembers those Brimstone doesn't hold, for vanilla's check. */
    private static int audit(SectionMesh mesh, TerrainStore.Entry[] es) {
        int n = 0;
        for (int li = 0; li < TerrainStore.LAYERS; li++) {
            if (mesh.getSectionDraw(AUDIT_LAYER_ORDER[li]) == null) continue;
            n++;
            TerrainStore.Entry e = es == null ? null : es[li];
            if (e == null || e.part == null || (li == TerrainStore.TRANSLUCENT && e.index == null)) {
                AUDIT_MESHES.add(mesh);
                AUDIT_LAYERS.add(AUDIT_LAYER_ORDER[li]);
            }
        }
        return n;
    }

    /** Of the layers Brimstone doesn't hold, those vanilla's buffers don't hold either (under vanilla's own lock). */
    private static int missingFromVanilla() {
        if (AUDIT_MESHES.isEmpty()) return 0;
        SectionRenderDispatcher dispatcher = net.minecraft.client.Minecraft.getInstance().levelRenderer.sectionRenderDispatcher();
        if (dispatcher == null) return AUDIT_MESHES.size();
        int missing = 0;
        dispatcher.lock();
        try {
            for (int i = 0; i < AUDIT_MESHES.size(); i++) {
                if (dispatcher.getRenderSectionSlice(AUDIT_MESHES.get(i), AUDIT_LAYERS.get(i)) == null) missing++;
            }
        } finally {
            dispatcher.unlock();
        }
        AUDIT_MESHES.clear();
        AUDIT_LAYERS.clear();
        return missing;
    }

    private static ArenaDraws arenaDraws(int list, TerrainStore.Arena arena) {
        for (ArenaDraws ad : FRAME[list]) if (ad.arena == arena) return ad;
        ArenaDraws ad = new ArenaDraws(arena);
        FRAME[list].add(ad);
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
            Terrain.fail("preparing indirect terrain draws", t, false);
            return vanilla;
        }
    }

    private static List<List<DynamicGpuData.IndexedDraw>> addCommands(List<List<DynamicGpuData.IndexedDraw>> vanilla) {
        int shift = 0;
        for (List<DynamicGpuData.IndexedDraw> l : vanilla) for (DynamicGpuData.IndexedDraw d : l) shift = Math.max(shift, d.baseInstance() + 1);
        vanillaInstances = shift;
        vanillaCommandLists = vanilla.size();
        // one list for all of Brimstone's draws this frame (one slice, no per-list padding); each layer and arena
        // draws its own range of it
        COMMANDS.clear();
        for (List<ArenaDraws> l : FRAME) {
            for (ArenaDraws ad : l) {
                ad.commandStart = COMMANDS.size();
                for (DynamicGpuData.IndexedDraw d : ad.commands) {
                    COMMANDS.add(shift == 0 ? d : new DynamicGpuData.IndexedDraw(d.indexCount(), 1, 0, d.baseVertex(), d.baseInstance() + shift));
                }
            }
        }
        for (TRun run : TRUNS) {
            run.commandStart = COMMANDS.size();
            for (DynamicGpuData.IndexedDraw d : run.commands) {
                COMMANDS.add(shift == 0 ? d : new DynamicGpuData.IndexedDraw(d.indexCount(), 1, d.firstIndex(), d.baseVertex(), d.baseInstance() + shift));
            }
        }
        if (COMMANDS.isEmpty()) return vanilla;
        List<List<DynamicGpuData.IndexedDraw>> all = new ArrayList<>(vanilla.size() + 1);
        all.addAll(vanilla);
        all.add(COMMANDS);
        return all;
    }

    /** Takes the slice of Brimstone's command list; hands vanilla back the slices of its own. */
    public static GpuBufferSlice[] commandsWritten(GpuBufferSlice[] all, int vanillaLists) {
        if (!prepared || path != Path.INDIRECT || vanillaCommandLists != vanillaLists || all.length != vanillaLists + 1) return all;
        commandSlice = all[vanillaLists];
        return Arrays.copyOf(all, vanillaLists);
    }

    /** Vanilla writes its section instance data: Brimstone's goes into the same upload, after vanilla's. */
    public static List<DynamicGpuData.ChunkSectionInfo> withInstances(List<DynamicGpuData.ChunkSectionInfo> vanilla) {
        if (!prepared || path != Path.INDIRECT || INSTANCES.isEmpty()) return vanilla;
        if (vanillaCommandLists < 0 || vanilla.size() != vanillaInstances) {
            prepared = false;
            Terrain.fail("preparing indirect terrain draws", new IllegalStateException(
                "vanilla wrote " + vanilla.size() + " section instances, its commands use " + vanillaInstances), false);
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
    public static void draw(ChunkSectionLayer layer, RenderPass pass, RenderPipeline override, RenderPipeline overrideMultidraw) {
        drawHookSeen = true;
        if (!prepared || !Terrain.drawing()) return;
        int li = TerrainStore.layerIndex(layer);
        if (li < 0) return;
        if (li == TerrainStore.TRANSLUCENT) {
            drawTranslucent(pass, override, overrideMultidraw);
            return;
        }
        boolean wireframe = override != null || overrideMultidraw != null;
        if (FRAME[li].isEmpty()) return;
        try {
            TerrainGpuErrors.Mark mark = TerrainGpuErrors.mark();
            boolean plain = path != Path.INDIRECT;
            RenderSystem.AutoStorageIndexBuffer quads = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
            GpuBuffer indexBuffer = quads.getBuffer();
            if (indexBuffer == null || !quads.hasStorage(maxIndexCount)) throw new IllegalStateException("the shared quad index buffer is too small");
            int draws = 0, sectionLayers = 0;
            {
                List<ArenaDraws> list = FRAME[li];
                CompiledRenderPipeline pipeline;
                if (wireframe) {
                    pipeline = TerrainPipelines.wireframe(plain);
                } else {
                    pipeline = TerrainPipelines.compiled(TerrainPipelines.pipeline(plain, li == 1));
                }
                if (pipeline == null) return;
                pass.setPipeline(pipeline);
                pass.setIndexBuffer(indexBuffer, quads.type());
                draws += plain ? drawPlain(pass, list) : drawIndirect(pass, list);
                for (ArenaDraws ad : list) {
                    sectionLayers += ad.commands.size();
                    for (RegionDraw rd : ad.regions) sectionLayers += rd.n;
                }
            }
            lastPath = path;
            TerrainStats.drew(draws, sectionLayers);
            String err = TerrainGpuErrors.since(mark);
            if (err != null) {
                prepared = false;
                Terrain.fail("drawing terrain", new IllegalStateException("GPU error: " + err), false);
            }
        } catch (Throwable t) {
            prepared = false;
            Terrain.fail("drawing terrain", t, false);
        }
    }

    /**
     * Translucent, right after vanilla's own part of it (in the classic pass, or in one of the Improved Transparency
     * stage passes): Brimstone's runs in order, each with the pipeline made from the one vanilla draws this mode with.
     */
    private static void drawTranslucent(RenderPass pass, RenderPipeline override, RenderPipeline overrideMultidraw) {
        int mode = TerrainPipelines.translucentMode(override, overrideMultidraw);
        if (mode >= 1) TranslucencyStats.oitFrame();
        if (TRUNS.isEmpty()) return;
        try {
            if (mode == -2) throw new IllegalStateException("vanilla drew translucent terrain with a pipeline Brimstone does not know: " + override + ", " + overrideMultidraw);
            TerrainGpuErrors.Mark mark = TerrainGpuErrors.mark();
            boolean plain = path != Path.INDIRECT;
            if (!plain) {
                if (vanillaCommandLists < 0) throw new IllegalStateException("hook that did not apply: LevelRenderer#brimstone$commands");
                if (instanceSlice == null) throw new IllegalStateException("hook that did not apply: LevelRenderer#brimstone$instances");
                if (commandSlice == null) throw new IllegalStateException("no indirect commands this frame");
                pass.setVertexBuffer(1, instanceSlice);
            }
            int maxInterleaved = path == Path.PLAIN_INTERLEAVED
                ? RenderSystem.getDevice().getDeviceInfo().limits().maxMultiDrawDirectInterleavedDrawCount() : 0;
            int maxIndirect = Math.max(1, RenderSystem.getDevice().getDeviceInfo().limits().maxDrawIndirectDrawCount());
            CompiledRenderPipeline current = null;
            int draws = 0, sectionLayers = 0;
            for (TRun run : TRUNS) {
                CompiledRenderPipeline pipeline;
                if (mode == -1) {
                    pipeline = TerrainPipelines.wireframe(plain);
                    if (pipeline == null) continue;
                } else {
                    RenderPipeline p = TerrainPipelines.translucent(mode, plain);
                    if (p == null) throw new IllegalStateException("no translucent pipeline");
                    pipeline = TerrainPipelines.compiled(p);
                }
                if (pipeline != current) {
                    pass.setPipeline(pipeline);
                    current = pipeline;
                }
                pass.setVertexBuffer(0, run.arena.buffer.slice());
                pass.setIndexBuffer(run.arena.buffer, run.type);
                int n = run.n;
                sectionLayers += n;
                if (!plain) {
                    GpuBuffer buffer = commandSlice.buffer();
                    long base = commandSlice.offset() + (long) run.commandStart * RenderPass.INDIRECT_INDEXED_DRAW_SIZE;
                    for (int done = 0; done < n; ) {
                        int m = Math.min(maxIndirect, n - done);
                        pass.drawIndexedIndirect(buffer.slice(base + (long) done * RenderPass.INDIRECT_INDEXED_DRAW_SIZE,
                            (long) m * RenderPass.INDIRECT_INDEXED_DRAW_SIZE), m);
                        done += m;
                        draws++;
                    }
                    continue;
                }
                pass.setUniform("BrimRegion", run.ubo);
                switch (path) {
                    case PLAIN_SEPARATE -> {
                        ensureCapacity(n);
                        counts.clear().put(run.counts, 0, n).flip();
                        bases.clear().put(run.bases, 0, n).flip();
                        offsets.clear();
                        for (int i = 0; i < n; i++) offsets.put(run.offsets[i]);
                        offsets.flip();
                        pass.multiDrawIndexed(offsets, counts, bases, n);
                        draws++;
                    }
                    case PLAIN_INTERLEAVED -> {
                        for (int done = 0; done < n; ) {
                            int m = Math.min(n - done, maxInterleaved);
                            ensureCapacity(m);
                            interleaved.clear();
                            for (int i = done; i < done + m; i++) {
                                interleaved.put((int) (run.offsets[i] / run.type.bytes)).put(run.counts[i]).put(run.bases[i]);
                            }
                            interleaved.flip();
                            pass.multiDrawIndexed(interleaved, 1, 0, m);
                            done += m;
                            draws++;
                        }
                    }
                    default -> {
                        for (int i = 0; i < n; i++) {
                            pass.drawIndexed(run.counts[i], 1, (int) (run.offsets[i] / run.type.bytes), run.bases[i], 0);
                            draws++;
                        }
                    }
                }
            }
            lastPath = path;
            TerrainStats.drew(draws, sectionLayers);
            TranslucencyStats.drew(draws);
            String err = TerrainGpuErrors.since(mark);
            if (err != null) {
                prepared = false;
                Terrain.fail("drawing translucent terrain", new IllegalStateException("GPU error: " + err), false);
            }
        } catch (Throwable t) {
            prepared = false;
            Terrain.fail("drawing translucent terrain", t, false);
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
        if (vanillaCommandLists < 0) throw new IllegalStateException("hook that did not apply: LevelRenderer#brimstone$commands");
        if (instanceSlice == null) throw new IllegalStateException("hook that did not apply: LevelRenderer#brimstone$instances");
        int draws = 0;
        pass.setVertexBuffer(1, instanceSlice);
        int max = Math.max(1, RenderSystem.getDevice().getDeviceInfo().limits().maxDrawIndirectDrawCount());
        for (ArenaDraws ad : list) {
            int count = ad.commands.size();
            if (count == 0) continue;
            if (commandSlice == null) throw new IllegalStateException("no indirect commands this frame");
            pass.setVertexBuffer(0, ad.arena.buffer.slice());
            GpuBuffer buffer = commandSlice.buffer();
            long offset = commandSlice.offset() + (long) ad.commandStart * RenderPass.INDIRECT_INDEXED_DRAW_SIZE;
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
            MemoryUtil.memFree(offsets);
            MemoryUtil.memFree(interleaved);
            counts = MemoryUtil.memAllocInt(c);
            bases = MemoryUtil.memAllocInt(c);
            zeroOffsets = MemoryUtil.memCallocPointer(c);
            offsets = MemoryUtil.memCallocPointer(c);
            interleaved = MemoryUtil.memAllocInt(3 * c);
        }
    }
}
