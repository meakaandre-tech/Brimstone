package com.meakaandre.brimstone.frapi;

import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.commands.RenderPass;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.client.renderer.v1.render.submit.ExtendedBlockModelSubmit;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.feature.BlockModelFeatureRenderer;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRenderer;
import net.minecraft.client.renderer.feature.RenderTypeFeatureRenderer;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;

/**
 * Block models drawn as features with the Fabric Renderer API in place (package 4): the API turns every block model
 * submit into an {@link ExtendedBlockModelSubmit} (vanilla model parts plus an API mesh, and a render type per chunk
 * layer). Vanilla parts go to vanilla's own {@link BlockModelFeatureRenderer}: as one vanilla submit when every layer
 * the parts use maps to the same render type (vanilla's own case), else one per layer with the parts narrowed to that
 * layer's quads. Mesh quads that carry nothing a vanilla quad cannot (every vanilla block model's: the API emits block
 * models in entity contexts into meshes) go the same way, as vanilla quads (the very ones, where the mesh kept them);
 * the others are drawn by {@link MeshBlocks} the way vanilla draws a block model quad, plus the API's per-quad
 * properties. Every call is forwarded to both, one-for-one.
 */
final class BlockSubmitRenderer implements FeatureRenderer<ExtendedBlockModelSubmit> {
    private static final ChunkSectionLayer[] LAYERS = ChunkSectionLayer.values();
    private static final Direction[] FACES_AND_NULL = {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, null};

    private final BlockModelFeatureRenderer vanilla = new BlockModelFeatureRenderer();
    private final MeshBlocks meshes = new MeshBlocks();
    private final Map<List<ExtendedBlockModelSubmit>, Split> splits = new IdentityHashMap<>();

    record Split(List<BlockModelFeatureRenderer.Submit> vanilla, List<ExtendedBlockModelSubmit> meshes) {
    }

    /** A model part narrowed to the quads of one chunk layer (each face's list made once). */
    static final class LayerPart implements BlockStateModelPart {
        private final BlockStateModelPart part;
        private final ChunkSectionLayer layer;
        @SuppressWarnings("unchecked")
        private final List<BakedQuad>[] byFace = new List[FACES_AND_NULL.length];

        LayerPart(BlockStateModelPart part, ChunkSectionLayer layer) {
            this.part = part;
            this.layer = layer;
        }

        @Override
        public List<BakedQuad> getQuads(Direction face) {
            int i = face == null ? FACES_AND_NULL.length - 1 : face.ordinal();
            List<BakedQuad> out = byFace[i];
            if (out == null) {
                List<BakedQuad> all = part.getQuads(face);
                out = new ArrayList<>(all.size());
                for (BakedQuad q : all) if (q.materialInfo().layer() == layer) out.add(q);
                byFace[i] = out;
            }
            return out;
        }

        @Override
        public boolean useAmbientOcclusion() {
            return part.useAmbientOcclusion();
        }

        @Override
        public Material.Baked particleMaterial() {
            return part.particleMaterial();
        }

        @Override
        public int materialFlags() {
            return part.materialFlags();
        }
    }

    static Split split(List<ExtendedBlockModelSubmit> submits) {
        List<BlockModelFeatureRenderer.Submit> v = new ArrayList<>(submits.size());
        List<ExtendedBlockModelSubmit> m = new ArrayList<>();
        for (ExtendedBlockModelSubmit s : submits) {
            int before = v.size();
            try {
                splitOne(s, v, m);
            } catch (Throwable t) {
                Quads.featureFallback("a block model submit", t);
                while (v.size() > before) v.removeLast();
                if (m.size() > 0 && m.getLast() == s) m.removeLast();
                try {
                    if (!s.modelParts().isEmpty()) addVanilla(s, s.modelParts(), v);
                } catch (Throwable t2) {
                    // its render type function itself throws: nothing of it can be drawn
                    Quads.featureFallback("a block model submit's render type function", t2);
                    while (v.size() > before) v.removeLast();
                }
            }
        }
        return new Split(v, m);
    }

    private static void splitOne(ExtendedBlockModelSubmit s, List<BlockModelFeatureRenderer.Submit> v, List<ExtendedBlockModelSubmit> m) {
        {
            if (!s.modelParts().isEmpty()) addVanilla(s, s.modelParts(), v);
            if (s.mesh() != null && s.mesh().size() > 0) {
                // the mesh's vanilla-like quads (every vanilla block model's: the API emits block models in entity
                // contexts into meshes) to vanilla as a model part of their own, the rest to MeshBlocks
                List<BakedQuad> quads = new ArrayList<>(s.mesh().size());
                boolean[] own = {false};
                s.mesh().forEach(q -> {
                    if (Quads.vanillaLike(q)) quads.add(Quads.toBaked(q));
                    else own[0] = true;
                });
                if (!quads.isEmpty()) addVanilla(s, List.of(new QuadsPart(quads)), v);
                if (own[0]) m.add(s);
            }
        }
    }

    /** Converted mesh quads as a model part (all under no cull face, in the mesh's order). */
    record QuadsPart(List<BakedQuad> quads) implements BlockStateModelPart {
        @Override
        public List<BakedQuad> getQuads(Direction face) {
            return face == null ? quads : List.of();
        }

        @Override
        public boolean useAmbientOcclusion() {
            return false;
        }

        @Override
        public Material.Baked particleMaterial() {
            return null;
        }

        @Override
        public int materialFlags() {
            return 0;
        }
    }

    private static void addVanilla(ExtendedBlockModelSubmit s, List<BlockStateModelPart> modelParts, List<BlockModelFeatureRenderer.Submit> out) {
        Function<ChunkSectionLayer, RenderType> types = s.renderTypeFunction();
        RenderType[] typeOf = new RenderType[LAYERS.length];
        boolean allSame = true;
        for (ChunkSectionLayer layer : LAYERS) {
            typeOf[layer.ordinal()] = types.apply(layer);
            if (typeOf[layer.ordinal()] != typeOf[0]) allSame = false;
        }
        // one render type for every layer (vanilla's own case, and the API's for block models): no need to look at the quads
        RenderType single = typeOf[0];
        boolean same = true;
        boolean[] used = null;
        if (!allSame) {
            used = new boolean[LAYERS.length];
            for (BlockStateModelPart part : modelParts) {
                for (Direction face : FACES_AND_NULL) {
                    for (BakedQuad q : part.getQuads(face)) used[q.materialInfo().layer().ordinal()] = true;
                }
            }
            // the render type of every layer the parts use, a null one (layer skipped) counting as a type of its own
            boolean first = true;
            for (ChunkSectionLayer layer : LAYERS) {
                if (!used[layer.ordinal()]) continue;
                RenderType t = typeOf[layer.ordinal()];
                if (first) {
                    single = t;
                    first = false;
                } else if (t != single) {
                    same = false;
                }
            }
        }
        if (same) {
            if (single != null) {
                out.add(new BlockModelFeatureRenderer.Submit(s.pose(), single, modelParts, s.tintLayers(), s.lightCoords(), s.overlayCoords(),
                    s.tintColor(), s.sheetedDecalPose()));
            }
            return;
        }
        for (ChunkSectionLayer layer : LAYERS) {
            if (!used[layer.ordinal()]) continue;
            RenderType t = typeOf[layer.ordinal()];
            if (t == null) continue;
            List<BlockStateModelPart> parts = new ArrayList<>(modelParts.size());
            for (BlockStateModelPart part : modelParts) parts.add(new LayerPart(part, layer));
            out.add(new BlockModelFeatureRenderer.Submit(s.pose(), t, parts, s.tintLayers(), s.lightCoords(), s.overlayCoords(), s.tintColor(),
                s.sheetedDecalPose()));
        }
    }

    private Split splitFor(List<ExtendedBlockModelSubmit> submits) {
        Split s = splits.get(submits);
        return s != null ? s : split(submits);
    }

    @Override
    public void beginPrepare(FeatureFrameContext context) {
        vanilla.beginPrepare(context);
        meshes.beginPrepare(context);
    }

    @Override
    public void prepareGroup(FeatureFrameContext context, List<ExtendedBlockModelSubmit> submits, boolean flag) {
        Split s = split(submits);
        splits.put(submits, s);
        vanilla.prepareGroup(context, s.vanilla, flag);
        meshes.prepareGroup(context, s.meshes, flag);
    }

    @Override
    public void finishPrepare(FeatureFrameContext context) {
        vanilla.finishPrepare(context);
        meshes.finishPrepare(context);
    }

    @Override
    public void executeGroup(FeatureFrameContext context, OitStage stage, RenderPass pass, int group, List<ExtendedBlockModelSubmit> submits, boolean flag) {
        Split s = splitFor(submits);
        vanilla.executeGroup(context, stage, pass, group, s.vanilla, flag);
        meshes.executeGroup(context, stage, pass, group, s.meshes, flag);
    }

    @Override
    public void finishExecute(FeatureFrameContext context) {
        vanilla.finishExecute(context);
        meshes.finishExecute(context);
        splits.clear();
    }

    @Override
    public void close() {
        vanilla.close();
        meshes.close();
    }

    /** API mesh quads in block model submits. */
    static final class MeshBlocks extends RenderTypeFeatureRenderer<ExtendedBlockModelSubmit> {
        private final MutableQuadViewImpl scratch = new MutableQuadViewImpl() {
            @Override
            protected void emitDirectly() {
            }
        };

        /** The current submit's decal UV wrapper (breaking overlay) and the vertex builder it wraps. */
        private VertexConsumer lastBuilder, lastDecal;

        @Override
        protected void buildGroup(FeatureFrameContext context, List<ExtendedBlockModelSubmit> submits) {
            for (ExtendedBlockModelSubmit s : submits) {
                lastBuilder = lastDecal = null;
                try {
                    s.mesh().forEach(q -> {
                        if (!Quads.vanillaLike(q)) buffer(s, q);
                    });
                } catch (Throwable t) {
                    Quads.featureFallback("a block model submit's mesh", t);
                }
            }
            lastBuilder = lastDecal = null;
        }

        private void buffer(ExtendedBlockModelSubmit s, QuadView quad) {
            RenderType type = s.renderTypeFunction().apply(quad.chunkLayer());
            if (type == null) return;
            VertexConsumer consumer = getVertexBuilder(type);
            if (s.sheetedDecalPose() != null) {
                if (consumer != lastBuilder) {
                    lastBuilder = consumer;
                    lastDecal = new SheetedDecalTextureGenerator(consumer, s.sheetedDecalPose(), 1.0f);
                }
                consumer = lastDecal;
            }
            int tint = s.tintColor() != -1 && s.tintColor() != 0 ? s.tintColor() : Quads.layerColor(s.tintLayers(), quad.tintIndex());
            MutableQuadViewImpl q = scratch;
            q.copyFrom(quad);
            for (int i = 0; i < 4; i++) {
                int c = quad.color(i);
                q.color(i, tint == -1 ? c : c == -1 ? tint : ARGB.multiply(c, tint));
                q.lightmap(i, quad.emissive() ? LightCoordsUtil.FULL_BRIGHT : Quads.withQuadLight(s.lightCoords(), quad.lightmap(i)));
            }
            q.buffer(s.overlayCoords(), s.pose(), consumer);
            q.clear();
        }
    }
}
