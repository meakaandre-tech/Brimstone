package com.meakaandre.quicksilver.frapi;

import com.meakaandre.quicksilver.QuicksilverClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.model.FabricBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.render.submit.ExtendedBlockModelSubmit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.feature.BlockModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * CI's check of the renderer's failure handling and API contract (package 4, {@code quicksilver.debug.verifyRenderer}):
 * once the player is in a world, runs Quicksilver's renderer on cases no CI scene has (a model that throws, a skipped
 * chunk layer, the mesh and normal contract) and logs "passed" or what failed; ci/compare.py requires "passed".
 */
public final class RendererSelfTest {
    private static boolean done;

    private RendererSelfTest() {
    }

    public static void tick(Minecraft mc) {
        if (done || mc.level == null || mc.player == null) return;
        done = true;
        if (QuicksilverRenderer.blockRendererOff()) {
            // every block and item goes to vanilla's code: nothing of Quicksilver's renderer to test (CI's off session)
            QuicksilverClient.LOG.info("Quicksilver renderer self-test: not run (block renderer off)");
            return;
        }
        List<String> failed = new ArrayList<>();
        int checks = 0;
        try {
            checks = run(mc, failed);
        } catch (Throwable t) {
            failed.add("the self-test threw " + t);
            QuicksilverClient.LOG.error("Quicksilver renderer self-test threw", t);
        }
        if (failed.isEmpty()) QuicksilverClient.LOG.info("Quicksilver renderer self-test: passed ({} checks)", checks);
        else QuicksilverClient.LOG.error("Quicksilver renderer self-test FAILED: {}", String.join("; ", failed));
    }

    private static int checks;

    private static void check(boolean ok, String what, List<String> failed) {
        checks++;
        if (!ok) failed.add(what);
    }

    private static int run(Minecraft mc, List<String> failed) {
        checks = 0;
        check(Renderer.get() == QuicksilverRenderer.INSTANCE, "the API's renderer is not Quicksilver's", failed);
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockStateModel model = mc.getModelManager().getBlockStateModelSet().get(stone);
        BlockPos pos = mc.player.blockPosition().above(2);

        // S1: a model that throws after emitting some quads is drawn once (by vanilla's renderer), and the transform it
        // left pushed does not reach the next block
        BlockRenderer renderer = new BlockRenderer(false, false, mc.getBlockColors());
        renderer.quiet = true;
        int[] count = {0};
        QuadEmitter out = QuicksilverRenderer.INSTANCE.quadEmitter(q -> count[0]++);
        renderer.tesselateBlock(out, 0, 0, 0, mc.level, pos, stone, model, 42L);
        int plain = count[0];
        check(plain > 0, "stone drew no quads", failed);
        count[0] = 0;
        renderer.tesselateBlock(out, 0, 0, 0, mc.level, pos, stone, new ThrowingModel(model), 42L);
        check(count[0] == plain, "a model that threw drew " + count[0] + " quads, not " + plain + " (drawn twice?)", failed);
        count[0] = 0;
        renderer.tesselateBlock(out, 0, 0, 0, mc.level, pos, stone, model, 42L);
        check(count[0] == plain, "the block after a model that threw drew " + count[0] + " quads, not " + plain + " (transform left pushed?)", failed);

        // S3: a vanilla quad through a mesh comes back as the very same quad; a changed one does not
        List<BlockStateModelPart> parts = new ArrayList<>();
        model.collectParts(RandomSource.create(42L), parts);
        BakedQuad top = parts.get(0).getQuads(Direction.UP).get(0);
        MutableMesh mutable = QuicksilverRenderer.INSTANCE.mutableMesh();
        QuadEmitter e = mutable.emitter();
        e.cullFace(Direction.UP);
        e.fromBakedQuad(top);
        e.emit();
        e.fromBakedQuad(top);
        e.uv(0, 0.5f, 0.5f);
        e.emit();
        // every other change to what a vanilla quad stores drops the kept quad too
        e.fromBakedQuad(top);
        e.tintIndex(top.materialInfo().tintIndex() + 7);
        e.emit();
        e.fromBakedQuad(top);
        e.lightmap(2, 0xF000F0);
        e.emit();
        e.fromBakedQuad(top);
        e.chunkLayer(top.materialInfo().layer() == ChunkSectionLayer.TRANSLUCENT ? ChunkSectionLayer.SOLID : ChunkSectionLayer.TRANSLUCENT);
        e.emit();
        e.fromBakedQuad(top);
        e.pos(1, e.x(1) + 0.25f, e.y(1), e.z(1));
        e.emit();
        Mesh mesh = mutable.immutableCopy();
        List<BakedQuad> back = new ArrayList<>();
        mesh.forEach(q -> back.add(Quads.toBaked(q)));
        check(back.size() == 6 && back.get(0) == top, "an unchanged vanilla quad through a mesh is not the same quad", failed);
        check(back.size() == 6 && back.get(1) != top && back.get(1).packedUV(0) != top.packedUV(0), "a vanilla quad with new UVs through a mesh kept the old quad", failed);
        check(back.size() == 6 && back.get(2) != top && back.get(2).materialInfo().tintIndex() != top.materialInfo().tintIndex()
            && back.get(3) != top && back.get(4) != top && back.get(4).materialInfo().layer() != top.materialInfo().layer()
            && back.get(5) != top && !back.get(5).position(1).equals(top.position(1)), "a changed vanilla quad (tint, light, layer or position) through a mesh kept the old quad", failed);
        // N4: emit() on a mutable mesh's in-place cursor leaves the quad as it is
        mutable.forEachMutable(q -> ((QuadEmitter) q).emit());
        List<BakedQuad> again = new ArrayList<>();
        mutable.forEach(q -> again.add(Quads.toBaked(q)));
        check(again.size() == 6 && again.get(0) == top, "emit() on a mutable mesh's cursor changed the quad", failed);

        // S7: a layer whose render type is null is skipped, not drawn with another layer's type
        TextureAtlasSprite sprite = top.materialInfo().sprite();
        RenderType cutout = Sheets.cutoutBlockItemSheet();
        BakedQuad solidQuad = quad(sprite, ChunkSectionLayer.SOLID, cutout), cutoutQuad = quad(sprite, ChunkSectionLayer.CUTOUT, cutout);
        List<BlockStateModelPart> mixed = List.of(new BlockSubmitRenderer.QuadsPart(List.of(solidQuad)),
            new BlockSubmitRenderer.QuadsPart(List.of(cutoutQuad)));
        List<BlockModelFeatureRenderer.Submit> split = BlockSubmitRenderer.split(List.of(submit(l -> l == ChunkSectionLayer.CUTOUT ? cutout : null, mixed, null))).vanilla();
        List<BakedQuad> drawn = split.size() == 1 ? quads(split.get(0).modelParts()) : List.of();
        check(split.size() == 1 && split.get(0).renderType() == cutout && drawn.equals(List.of(cutoutQuad)),
            "with the solid layer skipped, " + split.size() + " submits drew " + drawn.size() + " quads (want 1 submit, the cutout quad)", failed);
        split = BlockSubmitRenderer.split(List.of(submit(l -> cutout, mixed, null))).vanilla();
        check(split.size() == 1 && split.get(0).modelParts() == mixed, "one render type for every layer did not give vanilla's single submit", failed);
        // a block model submit's mesh: vanilla gets its vanilla quads as they are (the quad with its own light on one
        // vertex is not vanilla-like and Quicksilver draws it, so vanilla gets 5 of the 6)
        split = BlockSubmitRenderer.split(List.of(submit(l -> cutout, List.of(), mesh))).vanilla();
        drawn = split.size() == 1 ? quads(split.get(0).modelParts()) : List.of();
        check(drawn.size() == 5 && drawn.get(0) == top, "a block model submit's mesh did not hand vanilla its own vanilla quad", failed);

        // the vanilla path (block-renderer=off, and a model that throws) hands vanilla's own output on unchanged: tinted,
        // cutout and light-emitting blocks, compared quad by quad with vanilla's ModelBlockRenderer
        for (BlockState st : List.of(Blocks.GRASS_BLOCK.defaultBlockState(), Blocks.OAK_LEAVES.defaultBlockState(),
            Blocks.GLOWSTONE.defaultBlockState(), Blocks.TORCH.defaultBlockState())) {
            BlockStateModel m = mc.getModelManager().getBlockStateModelSet().get(st);
            List<String> want = new ArrayList<>(), got = new ArrayList<>();
            new net.minecraft.client.renderer.block.ModelBlockRenderer(false, false, mc.getBlockColors()).tesselateBlock((qx, qy, qz, q, qi) -> {
                StringBuilder sb = new StringBuilder();
                int em = q.materialInfo().lightEmission();
                for (int i = 0; i < 4; i++) {
                    sb.append(q.position(i).x() + qx).append(',').append(q.position(i).y() + qy).append(',').append(q.position(i).z() + qz)
                        .append(' ').append(Integer.toHexString(qi.getColor(i))).append(' ').append(Integer.toHexString(qi.getLightCoordsWithEmission(i, em))).append(';');
                }
                want.add(sb.toString());
            }, 0, 0, 0, mc.level, pos, st, m, 42L);
            renderer.tesselateVanilla(QuicksilverRenderer.INSTANCE.quadEmitter(q -> {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    sb.append(q.x(i)).append(',').append(q.y(i)).append(',').append(q.z(i)).append(' ').append(Integer.toHexString(q.color(i)))
                        .append(' ').append(Integer.toHexString(q.lightmap(i))).append(';');
                }
                got.add(sb.toString());
            }), 0, 0, 0, mc.level, pos, st, m, 42L);
            check(!want.isEmpty() && want.equals(got), "the vanilla path changed " + st + "'s quads (" + got.size() + " against vanilla's " + want.size() + ")", failed);
        }

        // Indigo's quad geometry: a 45-degree quad's light face (x before z, y before x), a mesh quad's face normal read back
        // packed, a copy keeping it, and a vanilla quad at full light emission made emissive
        QuadEmitter g = QuicksilverRenderer.INSTANCE.quadEmitter(q -> {
        });
        g.pos(0, 0, 0, 0).pos(1, 0, 1, 0).pos(2, 1, 1, 1).pos(3, 1, 0, 1);
        check(g.lightFace().getAxis() == Direction.Axis.X, "a quad facing x and z equally has light face " + g.lightFace() + " (Indigo's: along x)", failed);
        g.pos(0, 0, 0, 0).pos(1, 1, 1, 0).pos(2, 1, 1, 1).pos(3, 0, 0, 1);
        check(g.lightFace().getAxis() == Direction.Axis.Y, "a quad facing x and y equally has light face " + g.lightFace() + " (Indigo's: along y)", failed);
        MutableMesh gm = QuicksilverRenderer.INSTANCE.mutableMesh();
        QuadEmitter ge = gm.emitter();
        ge.pos(0, 0, 0, 0).pos(1, 0, 1, 0).pos(2, 1, 1, 0.3f).pos(3, 1, 0, 0.3f);
        Vector3f exactNormal = new Vector3f().set(ge.faceNormal());
        ge.emit();
        Vector3f[] read = new Vector3f[2];
        gm.immutableCopy().forEach(q -> {
            read[0] = new Vector3f().set(q.faceNormal());
            QuadEmitter c = QuicksilverRenderer.INSTANCE.quadEmitter(x -> {
            });
            c.copyFrom(q);
            read[1] = new Vector3f().set(c.faceNormal());
        });
        int packed = GeometryHelper.packNormal(exactNormal.x, exactNormal.y, exactNormal.z);
        Vector3f quantised = new Vector3f(GeometryHelper.unpackNormal(packed, 0), GeometryHelper.unpackNormal(packed, 1), GeometryHelper.unpackNormal(packed, 2));
        check(read[0] != null && read[0].equals(quantised) && !quantised.equals(exactNormal), "a mesh quad's face normal is " + read[0]
            + ", not its packed " + quantised + " (as Indigo's meshes keep it)", failed);
        check(read[1] != null && read[1].equals(quantised), "a copy of a mesh quad has face normal " + read[1] + ", not the mesh's " + quantised, failed);
        BakedQuad.MaterialInfo glow = new BakedQuad.MaterialInfo(sprite, ChunkSectionLayer.SOLID, cutout, cutout, cutout, -1, null, 15);
        g.fromBakedQuad(new BakedQuad(new Vector3f(0, 1, 0), new Vector3f(0, 1, 1), new Vector3f(1, 1, 1), new Vector3f(1, 1, 0), 0L, 0L, 0L, 0L,
            Direction.UP, glow));
        boolean emissive15 = g.emissive();
        g.fromBakedQuad(top);
        check(emissive15 && (top.materialInfo().lightEmission() == 15 || !g.emissive()), "fromBakedQuad's emissive flag does not follow light emission 15", failed);

        // S4: buffer() without a pose writes the exact face normal, not the light face's axis
        QuadEmitter angled = QuicksilverRenderer.INSTANCE.quadEmitter(q -> {
        });
        angled.pos(0, 0, 0, 0).pos(1, 0, 1, 0).pos(2, 1, 1, 1).pos(3, 1, 0, 1);
        Vector3fc n = angled.faceNormal();
        NormalRecorder rec = new NormalRecorder();
        angled.buffer(0, rec);
        check(rec.count == 4 && rec.maxDiff(n) < 1.0e-5f && Math.abs(Math.abs(n.x()) - (float) Math.sqrt(0.5)) < 1.0e-5f,
            "buffer() wrote normal " + rec.last + " for an angled quad with face normal " + n, failed);
        return checks;
    }

    private static BakedQuad quad(TextureAtlasSprite sprite, ChunkSectionLayer layer, RenderType type) {
        BakedQuad.MaterialInfo info = new BakedQuad.MaterialInfo(sprite, layer, type, type, type, -1, null, 0);
        return new BakedQuad(new Vector3f(0, 1, 0), new Vector3f(0, 1, 1), new Vector3f(1, 1, 1), new Vector3f(1, 1, 0), 0L, 0L, 0L, 0L,
            Direction.UP, info);
    }

    private static ExtendedBlockModelSubmit submit(Function<ChunkSectionLayer, RenderType> types, List<BlockStateModelPart> parts, Mesh mesh) {
        return new ExtendedBlockModelSubmit(new PoseStack().last(), types, parts, mesh, new int[0], 0, 0, -1, null);
    }

    private static List<BakedQuad> quads(List<BlockStateModelPart> parts) {
        List<BakedQuad> out = new ArrayList<>();
        for (BlockStateModelPart p : parts) {
            for (Direction d : Direction.values()) out.addAll(p.getQuads(d));
            out.addAll(p.getQuads(null));
        }
        return out;
    }

    /** A model that emits its delegate's quads, pushes a transform that drops every quad, and throws. */
    private record ThrowingModel(BlockStateModel delegate) implements BlockStateModel, FabricBlockStateModel {
        @Override
        public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
            delegate.collectParts(random, parts);
        }

        @Override
        public Material.Baked particleMaterial() {
            return delegate.particleMaterial();
        }

        @Override
        public int materialFlags() {
            return delegate.materialFlags();
        }

        @Override
        public void emitQuads(QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random,
                              Predicate<Direction> cullTest) {
            ((FabricBlockStateModel) delegate).emitQuads(emitter, level, pos, state, random, cullTest);
            emitter.pushTransform(q -> false);
            throw new IllegalStateException("Quicksilver renderer self-test: deliberate model failure");
        }
    }

    /** Keeps the normals written to it. */
    private static final class NormalRecorder implements VertexConsumer {
        int count;
        final float[] normals = new float[12];
        String last = "none";

        float maxDiff(Vector3fc n) {
            float max = 0;
            for (int i = 0; i < count && i < 4; i++) {
                max = Math.max(max, Math.abs(normals[i * 3] - n.x()) + Math.abs(normals[i * 3 + 1] - n.y()) + Math.abs(normals[i * 3 + 2] - n.z()));
            }
            return max;
        }

        @Override
        public void addVertex(float x, float y, float z, int color, float u, float v, int overlay, int light, float nx, float ny, float nz) {
            if (count < 4) {
                normals[count * 3] = nx;
                normals[count * 3 + 1] = ny;
                normals[count * 3 + 2] = nz;
            }
            count++;
            last = "(" + nx + ", " + ny + ", " + nz + ")";
        }

        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            return this;
        }

        @Override
        public VertexConsumer setColor(int r, int g, int b, int a) {
            return this;
        }

        @Override
        public VertexConsumer setColor(int color) {
            return this;
        }

        @Override
        public VertexConsumer setUv(float u, float v) {
            return this;
        }

        @Override
        public VertexConsumer setUv1(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer setUv2(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer setUv3(float u, float v) {
            return this;
        }

        @Override
        public VertexConsumer setNormal(float x, float y, float z) {
            return this;
        }

        @Override
        public VertexConsumer setLineWidth(float width) {
            return this;
        }
    }
}
