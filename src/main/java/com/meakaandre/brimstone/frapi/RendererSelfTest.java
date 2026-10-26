package com.meakaandre.brimstone.frapi;

import com.meakaandre.brimstone.BrimstoneClient;
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
 * CI's check of the renderer's failure handling and API contract (package 4, {@code brimstone.debug.verifyRenderer}):
 * once the player is in a world, runs Brimstone's renderer on cases no CI scene has (a model that throws, a skipped
 * chunk layer, the mesh and normal contract) and logs "passed" or what failed; ci/compare.py requires "passed".
 */
public final class RendererSelfTest {
    private static boolean done;

    private RendererSelfTest() {
    }

    public static void tick(Minecraft mc) {
        if (done || mc.level == null || mc.player == null) return;
        done = true;
        List<String> failed = new ArrayList<>();
        int checks = 0;
        try {
            checks = run(mc, failed);
        } catch (Throwable t) {
            failed.add("the self-test threw " + t);
            BrimstoneClient.LOG.error("Brimstone renderer self-test threw", t);
        }
        if (failed.isEmpty()) BrimstoneClient.LOG.info("Brimstone renderer self-test: passed ({} checks)", checks);
        else BrimstoneClient.LOG.error("Brimstone renderer self-test FAILED: {}", String.join("; ", failed));
    }

    private static int checks;

    private static void check(boolean ok, String what, List<String> failed) {
        checks++;
        if (!ok) failed.add(what);
    }

    private static int run(Minecraft mc, List<String> failed) {
        checks = 0;
        check(Renderer.get() == BrimstoneRenderer.INSTANCE, "the API's renderer is not Brimstone's", failed);
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockStateModel model = mc.getModelManager().getBlockStateModelSet().get(stone);
        BlockPos pos = mc.player.blockPosition().above(2);

        // S1: a model that throws after emitting some quads is drawn once (by vanilla's renderer), and the transform it
        // left pushed does not reach the next block
        BlockRenderer renderer = new BlockRenderer(false, false, mc.getBlockColors());
        renderer.quiet = true;
        int[] count = {0};
        QuadEmitter out = BrimstoneRenderer.INSTANCE.quadEmitter(q -> count[0]++);
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
        MutableMesh mutable = BrimstoneRenderer.INSTANCE.mutableMesh();
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
        // vertex is not vanilla-like and Brimstone draws it, so vanilla gets 5 of the 6)
        split = BlockSubmitRenderer.split(List.of(submit(l -> cutout, List.of(), mesh))).vanilla();
        drawn = split.size() == 1 ? quads(split.get(0).modelParts()) : List.of();
        check(drawn.size() == 5 && drawn.get(0) == top, "a block model submit's mesh did not hand vanilla its own vanilla quad", failed);

        // S4: buffer() without a pose writes the exact face normal, not the light face's axis
        QuadEmitter angled = BrimstoneRenderer.INSTANCE.quadEmitter(q -> {
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
            throw new IllegalStateException("Brimstone renderer self-test: deliberate model failure");
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
