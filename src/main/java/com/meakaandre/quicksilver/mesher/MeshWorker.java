package com.meakaandre.quicksilver.mesher;

import com.meakaandre.quicksilver.terrain.CompactVertex;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.render.AltModelBlockRenderer;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.lwjgl.system.MemoryUtil;

/**
 * One meshing thread's mesher (vanilla's worker threads, or the render thread for the sections vanilla builds there):
 * its section snapshot, writer, block and fluid renderers and buffers, kept and reused from section to section.
 *
 * <p>{@link #compile} walks the section's blocks in vanilla's order (x fastest, then y, then z), and for each block does
 * what vanilla's section compiler does: the visibility graph, the fluid, then the block model through the Fabric
 * Renderer API's block renderer, as Fabric API's hook into vanilla's compiler does it. Quads go straight into the
 * 16-byte vertex ({@link SectionWriter}). Block entities are collected as they come and handed to vanilla's own
 * {@code handleBlockEntity} (where mods such as Flywheel take theirs over) in vanilla's order once the section is known
 * to be Quicksilver's, so a section that goes to vanilla's compiler instead never hands them over twice.
 */
final class MeshWorker {
    final SectionSnapshot snapshot = new SectionSnapshot();
    final SectionWriter writer = new SectionWriter();
    private final FluidMesher fluids = new FluidMesher(writer);
    private final CompactConsumer[] fluidOut = {
        new CompactConsumer(writer, SectionWriter.SOLID), new CompactConsumer(writer, SectionWriter.CUTOUT),
        new CompactConsumer(writer, SectionWriter.TRANSLUCENT)};
    private final FluidRenderer.Output fluidOutput = layer -> {
        int i = FluidMesher.layerIndex(layer);
        if (i < 0) throw new IllegalStateException("a fluid in layer " + layer);
        return fluidOut[i];
    };
    private final ByteBufferBuilder buffers = new ByteBufferBuilder(1 << 20);
    final MesherOutput output = new MesherOutput();
    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    private final java.util.ArrayList<BlockEntity> blockEntities = new java.util.ArrayList<>();
    final HiddenBlocks hidden = new HiddenBlocks();

    // the block renderer and its emitter, for the renderer, light setting and colours they were made with
    private Renderer renderer;
    private boolean rendererAo;
    private BlockColors rendererColors;
    private AltModelBlockRenderer blocks;
    private QuadEmitter emitter;

    /** The last fluid and model {@link FluidProbe} answered for on this thread, and its answer. */
    Object probedFluid, probedModel;
    boolean probedOwn;

    /** Time the last {@link #compile} spent finding out whether fluids are Quicksilver's to draw (once per fluid). */
    long probeNanos;

    /** Set while the last {@link #compile} hands its block entities over (and after): no vanilla fallback then. */
    boolean handedBlockEntities;

    /** Why the last {@link #compile} returned null (the section goes to vanilla's compiler). */
    String declined;

    private AltModelBlockRenderer blockRenderer(boolean ao, BlockColors colors) {
        Renderer r = Renderer.get();
        if (blocks == null || r != renderer || ao != rendererAo || colors != rendererColors) {
            renderer = r;
            rendererAo = ao;
            rendererColors = colors;
            // as Fabric API's hook into vanilla's compiler: cull on, each quad into the layer it asks for
            blocks = r.altModelBlockRenderer(ao, true, colors);
            java.util.function.Consumer<net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView> sink = quad -> {
                int layer = FluidMesher.layerIndex(quad.chunkLayer());
                SectionWriter w = writer;
                if (layer < 0) {
                    if (w.unstorable == null) w.unstorable = "a quad in layer " + quad.chunkLayer();
                    return;
                }
                if (com.meakaandre.quicksilver.frapi.RawQuad.of(quad)) {
                    // Quicksilver's renderer: read straight from the quad's array (no copy per vertex)
                    w.emitRaw(layer, com.meakaandre.quicksilver.frapi.RawQuad.data(quad), com.meakaandre.quicksilver.frapi.RawQuad.firstVertex(quad),
                        com.meakaandre.quicksilver.frapi.RawQuad.VERTEX_STRIDE, com.meakaandre.quicksilver.frapi.RawQuad.X,
                        com.meakaandre.quicksilver.frapi.RawQuad.Y, com.meakaandre.quicksilver.frapi.RawQuad.Z, com.meakaandre.quicksilver.frapi.RawQuad.U,
                        com.meakaandre.quicksilver.frapi.RawQuad.V, com.meakaandre.quicksilver.frapi.RawQuad.COLOR, com.meakaandre.quicksilver.frapi.RawQuad.LIGHT);
                    return;
                }
                for (int c = 0; c < 4; c++) {
                    w.x[c] = quad.x(c);
                    w.y[c] = quad.y(c);
                    w.z[c] = quad.z(c);
                    w.color[c] = quad.color(c);
                    w.u[c] = quad.u(c);
                    w.v[c] = quad.v(c);
                    w.light[c] = quad.lightmap(c);
                }
                w.emit(layer);
            };
            emitter = r instanceof com.meakaandre.quicksilver.frapi.QuicksilverRenderer ? com.meakaandre.quicksilver.frapi.QuicksilverRenderer.directEmitter(sink)
                : r.quadEmitter(sink);
        }
        return blocks;
    }

    /**
     * Meshes a section; null if it can't be held in the 16-byte vertex (then vanilla's compiler meshes it, as before),
     * with the reason in {@link #declined}. {@code handleBlockEntity}: vanilla's own, on the compiler that asked.
     */
    SectionCompiler.Results compile(SectionCompiler compiler, boolean ao, BlockColors colors, BlockStateModelSet models, FluidRenderer fluidRenderer,
                                    SectionPos section, RenderSectionRegion region, VertexSorting sorting,
                                    BiConsumer<SectionCompiler.Results, BlockEntity> handleBlockEntity) {
        declined = null;
        handedBlockEntities = false;
        probeNanos = 0;
        output.clear();
        int ox = section.minBlockX(), oy = section.minBlockY(), oz = section.minBlockZ();
        SectionSnapshot snap = snapshot;
        SectionWriter w = writer;
        try {
            long t0 = System.nanoTime();
            snap.load(region, ox, oy, oz);
            long t1 = System.nanoTime();
            w.begin(CompactVertex.sectionIndex(ox, oy, oz));
            AltModelBlockRenderer blockRenderer = blockRenderer(ao, colors);
            QuadEmitter out = emitter;
            SectionCompiler.Results results = new SectionCompiler.Results();
            VisGraph visGraph = new VisGraph();
            BlockPos.MutableBlockPos p = pos;
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    for (int x = 0; x < 16; x++) {
                        BlockState state = snap.local(x, y, z);
                        p.set(ox + x, oy + y, oz + z);
                        if (state.isSolidRender()) visGraph.setOpaque(p);
                        if (state.hasBlockEntity()) {
                            BlockEntity be = region.getBlockEntity(p);
                            if (be != null) blockEntities.add(be);
                        }
                        FluidState fluid = state.getFluidState();
                        w.origin = x | y << 4 | z << 8 | 4096;
                        if (!fluid.isEmpty()) {
                            if (!Mesher.fluidsThroughVanilla && FluidMesher.ownFluid(fluid) && FluidProbe.own(this, fluidRenderer, region, fluid)) {
                                fluids.tesselate(fluidRenderer, snap, p, state, fluid);
                            } else {
                                // vanilla's renderer (and a mod's handler through it) sees vanilla's region, as in vanilla's compiler
                                fluidRenderer.tesselate(region, p, fluidOutput, state, fluid);
                                for (CompactConsumer c : fluidOut) c.flush();
                            }
                        }
                        w.origin = x | y << 4 | z << 8;
                        if (state.getRenderShape() == RenderShape.MODEL) {
                            BlockStateModel model = models.get(state);
                            if (!hidden.drawsNothing(models, model, snap, SectionSnapshot.localIndex(x, y, z))) {
                                blockRenderer.tesselateBlock(out, x, y, z, snap, p, state, model, state.getSeed(p));
                            }
                        }
                        if (w.unstorable != null) {
                            declined = w.unstorable;
                            return null;
                        }
                    }
                }
            }
            long t2 = System.nanoTime();
            results.visibilitySet = visGraph.resolve();
            if (!finish(results, sorting)) return null;
            MesherStats.phases(t1 - t0, t2 - t1, System.nanoTime() - t2);
            // vanilla's own block entity step, in vanilla's order, once the section is known to be Quicksilver's (where
            // mods take block entities over, e.g. Flywheel): a section handed to vanilla's compiler gets it from there
            handedBlockEntities = true;
            for (int i = 0; i < blockEntities.size(); i++) handleBlockEntity.accept(results, blockEntities.get(i));
            output.writer = w;
            output.found = w.sprites.found;
            output.spriteTable = w.sprites.table;
            output.active = true;
            return results;
        } finally {
            snap.release();
            blockEntities.clear();
        }
    }

    /** Puts each layer's vertices into a buffer vanilla's mesh bookkeeping can carry, and sorts the translucent one. */
    private boolean finish(SectionCompiler.Results results, VertexSorting sorting) {
        ByteBufferBuilder bb = buffers;
        bb.clear();
        MeshData[] meshes = new MeshData[SectionWriter.LAYERS];
        for (int li = 0; li < SectionWriter.LAYERS; li++) {
            SectionWriter.Layer l = writer.layers[li];
            if (l.quads == 0) continue;
            int vertices = l.quads * 4;
            if ((long) vertices * CompactVertex.BYTES > com.meakaandre.quicksilver.terrain.TerrainStore.MAX_LAYER_BYTES) {
                declined = "a layer larger than Quicksilver's arenas hold";
                for (MeshData m : meshes) if (m != null) m.close();
                return false;
            }
            MeshData.DrawState draw = new MeshData.DrawState(DefaultVertexFormat.BLOCK, vertices, vertices / 4 * 6, PrimitiveTopology.QUADS,
                IndexType.least(vertices));
            MesherOutput.LayerOut lo = output.layers[li];
            lo.vertices = vertices;
            lo.positionsRounded = l.positionsRounded;
            lo.uvsRounded = l.uvsRounded;
            // the 16-byte vertices, ready to upload: written once, into a buffer the terrain module takes over as it is
            // (no copy at the upload); solid and cutout grouped by facing, translucent in the order made
            java.nio.ByteBuffer owned = MemoryUtil.memAlloc(l.bytes());
            lo.owned = owned;
            lo.data = MemoryUtil.memAddress(owned);
            lo.bytes = l.bytes();
            lo.faces = l.copyTo(lo.data);
            if (li != SectionWriter.TRANSLUCENT) {
                // vanilla's mesh object only carries the size here: vanilla never reads a layer the terrain module took
                bb.reserve(l.bytes());
                meshes[li] = new MeshData(bb.build(), draw);
            } else {
                // vanilla's mesh carries the quads' positions as vanilla had them (only its sort reads them), and vanilla
                // sorts them as it sorts its own translucent layers: the sort state and index list are vanilla's
                int bytes = vertices * CompactVertex.VANILLA_BYTES;
                long at = bb.reserve(bytes);
                MemoryUtil.memSet(at, 0, bytes);
                float[] p = l.positions;
                for (int v = 0; v < vertices; v++) {
                    long o = at + (long) v * CompactVertex.VANILLA_BYTES;
                    MemoryUtil.memPutFloat(o, p[v * 3]);
                    MemoryUtil.memPutFloat(o + 4, p[v * 3 + 1]);
                    MemoryUtil.memPutFloat(o + 8, p[v * 3 + 2]);
                }
                MeshData mesh = new MeshData(bb.build(), draw);
                results.transparencyState = mesh.sortQuads(bb, sorting);
                meshes[li] = mesh;
                lo.centroids = centroids(p, l.quads);
            }
        }
        for (int li = 0; li < SectionWriter.LAYERS; li++) {
            MeshData m = meshes[li];
            if (m == null) continue;
            results.renderedLayers.put(LAYERS[li], m);
            // where vanilla will hand this layer back (its buffers no longer move: every reserve is done)
            MesherOutput.LayerOut lo = output.layers[li];
            lo.vertexAddress = MemoryUtil.memAddress(m.vertexBuffer());
            lo.vertexBytes = m.vertexBuffer().remaining();
            lo.present = true;
        }
        return true;
    }

    static final ChunkSectionLayer[] LAYERS = {ChunkSectionLayer.SOLID, ChunkSectionLayer.CUTOUT, ChunkSectionLayer.TRANSLUCENT};

    /** Quad centres as vanilla's sort and CI's translucency check take them: the midpoint of the first and third corner. */
    private static float[] centroids(float[] p, int quads) {
        float[] c = new float[quads * 3];
        for (int q = 0; q < quads; q++) {
            int a = q * 12, b = a + 6;
            for (int k = 0; k < 3; k++) c[q * 3 + k] = (p[a + k] + p[b + k]) * 0.5f;
        }
        return c;
    }

    /**
     * CI's fluid self-test: the fluid at {@code pos} of {@code level} meshed by Quicksilver's fluid mesher and by
     * vanilla's renderer, both into the 16-byte vertex. Null: neither drew anything; empty: the same; else the difference.
     */
    String compareFluid(FluidRenderer renderer, net.minecraft.client.renderer.block.BlockAndTintGetter level, BlockPos pos, BlockState state,
                        FluidState fluid) {
        SectionWriter w = writer;
        try {
            snapshot.load(level, pos.getX() & ~15, pos.getY() & ~15, pos.getZ() & ~15);
            w.begin(0);
            fluids.tesselate(renderer, snapshot, pos, state, fluid);
            byte[] mine = w.dump();
            String mineBad = w.unstorable;
            w.begin(0);
            renderer.tesselate(level, pos, fluidOutput, state, fluid);
            for (CompactConsumer c : fluidOut) c.flush();
            byte[] theirs = w.dump();
            String theirBad = w.unstorable;
            if (!java.util.Objects.equals(mineBad, theirBad)) return "unstorable: Quicksilver " + mineBad + ", vanilla " + theirBad;
            if (java.util.Arrays.equals(mine, theirs)) {
                for (SectionWriter.Layer l : w.layers) if (l.quads > 0) return "";
                return null;
            }
            int at = java.util.Arrays.mismatch(mine, theirs);
            return "Quicksilver " + hex(mine, at) + " | vanilla " + hex(theirs, at) + " (layer bytes differ at " + at + " of " + mine.length + "/"
                + theirs.length + ")";
        } finally {
            snapshot.release();
            w.begin(0);
        }
    }

    private static String hex(byte[] b, int at) {
        StringBuilder s = new StringBuilder();
        for (int i = Math.max(0, at - 8); i < Math.min(b.length, at + 24); i++) s.append(String.format("%02x", b[i] & 0xFF));
        return s.toString();
    }

    private MeshWorker prober;

    /** A second mesher of this thread's, for {@link FluidProbe} (its writer and snapshot stay out of the section's way). */
    MeshWorker prober() {
        if (prober == null) prober = new MeshWorker();
        return prober;
    }

    void free() {
        output.clear();
        if (prober != null) prober.free();
        writer.free();
        buffers.close();
    }
}
