package com.meakaandre.brimstone.frapi;

import com.meakaandre.brimstone.BrimstoneClient;
import com.mojang.blaze3d.vertex.QuadInstance;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadAtlas;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.model.FabricBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.render.AltModelBlockRenderer;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockModelLighter;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Brimstone's block tessellation for the Fabric Renderer API (chunk meshing, moving blocks): the model emits its
 * quads ({@link FabricBlockStateModel#emitQuads}), and each is culled, lit, tinted and written the way vanilla's
 * {@link ModelBlockRenderer} does it: smooth lighting as Indigo (the reference) does it ({@link FaceAo}), flat lighting
 * through vanilla's own {@link BlockModelLighter} (on the quad's vanilla {@link BakedQuad} where it still has one),
 * vanilla's tint sources and vanilla's light-emission rule. The API's per-quad properties come on top: its ambient
 * occlusion setting, vertex colours, lightmaps and emissive flag. One instance per meshing call (one thread).
 *
 * <p>A block's quads are held until its model has emitted them all. If the model throws, they are dropped, the
 * transforms it left pushed are cleared, and the block is drawn with vanilla's own {@link ModelBlockRenderer} instead
 * (logged once), so no quad is drawn twice.
 */
final class BlockRenderer implements AltModelBlockRenderer {
    private final boolean ambientOcclusion;
    private final boolean cull;
    private final BlockColors blockColors;
    private final BlockModelLighter lighter = new BlockModelLighter();
    private final FaceAo faceAo = new FaceAo(lighter);
    private final QuadInstance quadInstance = new QuadInstance();
    private final RandomSource random = RandomSource.createThreadLocalInstance(0L);
    private final BlockPos.MutableBlockPos scratch = new BlockPos.MutableBlockPos();
    private final MutableQuadViewImpl input = new MutableQuadViewImpl() {
        @Override
        protected void emitDirectly() {
            light(this);
        }
    };
    /** The block's finished quads, written out once its model has emitted them all. */
    private int[] pending = new int[QuadData.STRIDE * 16];
    private int pendingCount;
    private final Meshes.SlotQuad slot = new Meshes.SlotQuad();
    /** Any sprite, for the vanilla quads that vanilla's lighting gets (it never reads the sprite). */
    private TextureAtlasSprite lightingSprite;
    private ModelBlockRenderer vanilla;
    private static volatile boolean fallbackLogged;
    /** The renderer self-test's instance: its deliberate failures are not logged (nor use up the log-once). */
    boolean quiet;

    // the block being tessellated
    private QuadEmitter output;
    private float x, y, z;
    private BlockAndTintGetter level;
    private BlockPos pos;
    private BlockState state;
    private int cullChecked, cullHidden;
    private List<BlockTintSource> tintSources;
    private int tintIndex = -1, tintValue;

    BlockRenderer(boolean ambientOcclusion, boolean cull, BlockColors blockColors) {
        this.ambientOcclusion = ambientOcclusion;
        this.cull = cull;
        this.blockColors = blockColors;
    }

    @Override
    public void tesselateBlock(QuadEmitter output, float x, float y, float z, BlockAndTintGetter level, BlockPos pos, BlockState blockState,
                               BlockStateModel model, long seed) {
        Vec3 offset = blockState.getOffset(pos);
        this.output = output;
        this.x = x + (float) offset.x;
        this.y = y + (float) offset.y;
        this.z = z + (float) offset.z;
        this.level = level;
        this.pos = pos;
        this.state = blockState;
        cullChecked = 0;
        cullHidden = 0;
        tintSources = null;
        tintIndex = -1;
        pendingCount = 0;
        boolean done = false;
        try {
            random.setSeed(seed);
            ((FabricBlockStateModel) model).emitQuads(input, level, pos, blockState, random, this::hidden);
            done = true;
        } catch (Throwable t) {
            pendingCount = 0;
            input.clear();
            input.resetTransforms();
            if (!quiet && !fallbackLogged) {
                fallbackLogged = true;
                BrimstoneClient.LOG.warn("Brimstone renderer fallback: a block ({}) failed to tessellate; vanilla's block renderer draws it"
                    + " instead (logged once)", blockState, t);
            }
        } finally {
            this.level = null;
        }
        try {
            if (done) {
                Meshes.SlotQuad q = slot;
                for (int i = 0; i < pendingCount; i++) {
                    q.at(pending, i * QuadData.STRIDE, null, null);
                    output.copyFrom(q);
                    output.emit();
                }
            } else {
                tesselateVanilla(output, x, y, z, level, pos, blockState, model, seed);
            }
        } finally {
            pendingCount = 0;
            this.output = null;
        }
    }

    /** Whether the face of the block towards {@code face} is hidden by its neighbour (vanilla's face culling). */
    private boolean hidden(Direction face) {
        if (face == null || !cull) return false;
        int bit = 1 << face.ordinal();
        if ((cullChecked & bit) == 0) {
            cullChecked |= bit;
            if (!Block.shouldRenderFace(state, level.getBlockState(scratch.setWithOffset(pos, face)), face)) cullHidden |= bit;
        }
        return (cullHidden & bit) != 0;
    }

    private void light(MutableQuadViewImpl quad) {
        Direction cullFace = quad.cullFace();
        if (hidden(cullFace)) return;
        TriState aoSetting = quad.ambientOcclusion();
        boolean ao = aoSetting == TriState.TRUE ? ambientOcclusion
            : aoSetting == TriState.FALSE ? false : ambientOcclusion && state.getLightEmission() == 0;
        BakedQuad baked = quad.vanillaQuad();
        if (baked == null) {
            if (lightingSprite == null) lightingSprite = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(QuadAtlas.BLOCK.getId()).missingSprite();
            baked = Quads.forLighting(quad, lightingSprite);
        }
        QuadInstance qi = quadInstance;
        if (ao) {
            if (!faceAo.light(level, state, pos, quad, baked, qi)) lighter.prepareQuadAmbientOcclusion(level, state, pos, baked, qi);
        } else {
            int lightCoords = cullFace != null ? lighter.getLightCoords(state, level, scratch.setWithOffset(pos, cullFace)) : BlockModelLighter.CHECK_LIGHT;
            lighter.prepareQuadFlat(level, state, pos, lightCoords, baked, qi);
        }
        int tint = quad.tintIndex();
        if (tint != -1) qi.multiplyColor(tintColor(tint));
        boolean emissive = quad.emissive();
        // vanilla writes every vertex's light through its light-emission rule (QuadInstance.getLightCoordsWithEmission),
        // with the quad's emission; an API lightmap other than that emission is applied on top as a minimum
        int emission = baked.materialInfo().lightEmission();
        int vanillaLight = LightCoordsUtil.pack(emission, emission);
        if ((pendingCount + 1) * QuadData.STRIDE > pending.length) pending = Arrays.copyOf(pending, pending.length * 2);
        Meshes.SlotQuad out = slot;
        out.at(pending, pendingCount++ * QuadData.STRIDE, null, null);
        out.copyFrom(quad);
        for (int i = 0; i < 4; i++) {
            out.pos(i, quad.x(i) + x, quad.y(i) + y, quad.z(i) + z);
            int c = qi.getColor(i), qc = quad.color(i);
            out.color(i, qc == -1 ? c : ARGB.multiply(c, qc));
            int light = qi.getLightCoordsWithEmission(i, emission), ql = quad.lightmap(i);
            out.lightmap(i, emissive ? LightCoordsUtil.FULL_BRIGHT : ql == vanillaLight ? light : LightCoordsUtil.max(light, ql));
        }
    }

    /** The tint colour for a tint index, as vanilla computes it (the block's tint source at that index), cached per block. */
    private int tintColor(int index) {
        if (index == tintIndex) return tintValue;
        if (tintSources == null) tintSources = blockColors.getTintSources(state);
        int color = -1;
        if (index < tintSources.size()) {
            BlockTintSource source = tintSources.get(index);
            if (source != null) color = source.colorInWorld(state, level, pos);
        }
        tintIndex = index;
        tintValue = color;
        return color;
    }

    /** Vanilla's own block renderer for one block, its quads handed to the API's output as vanilla would write them. */
    private void tesselateVanilla(QuadEmitter output, float x, float y, float z, BlockAndTintGetter level, BlockPos pos, BlockState state,
                                  BlockStateModel model, long seed) {
        if (vanilla == null) vanilla = new ModelBlockRenderer(ambientOcclusion, cull, blockColors);
        vanilla.tesselateBlock((qx, qy, qz, quad, qi) -> {
            output.fromBakedQuad(quad);
            int emission = quad.materialInfo().lightEmission();
            for (int i = 0; i < 4; i++) {
                output.pos(i, quad.position(i).x() + qx, quad.position(i).y() + qy, quad.position(i).z() + qz);
                output.color(i, qi.getColor(i));
                output.lightmap(i, qi.getLightCoordsWithEmission(i, emission));
            }
            output.emit();
        }, x, y, z, level, pos, state, model, seed);
    }
}
