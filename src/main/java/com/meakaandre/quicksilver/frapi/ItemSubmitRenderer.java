package com.meakaandre.quicksilver.frapi;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.commands.RenderPass;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.client.renderer.v1.render.submit.ExtendedItemSubmit;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRenderer;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.feature.RenderTypeFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;

/**
 * Items drawn as features with the Fabric Renderer API in place (package 4): the API turns every item submit into an
 * {@link ExtendedItemSubmit} (vanilla quads plus an API mesh). Vanilla quads go to vanilla's own
 * {@link ItemFeatureRenderer}, as vanilla submits with the same fields, so they are drawn exactly as vanilla draws
 * them, together with the mesh quads that carry nothing a vanilla quad cannot (a model's {@code MeshQuadCollection};
 * vanilla item models' quads stay vanilla quads), as vanilla quads; outlines likewise, with all the mesh's quads (an
 * outline is one colour). The other mesh quads are drawn by {@link MeshItems} the way vanilla draws an item quad, plus
 * the API's per-quad properties (vertex colours, lightmaps, emissive, foil override). Every call is forwarded to both,
 * one-for-one, so each sees the same sequence of groups as this renderer.
 *
 * <p>Within one group, vanilla's renderer draws all its submits before {@link MeshItems} draws its own, so in a
 * translucent group the back-to-front order between a vanilla-drawn and a mesh-drawn submit is not kept (only items
 * with translucent API mesh quads that vanilla cannot draw are affected; vanilla's items have none).
 */
final class ItemSubmitRenderer implements FeatureRenderer<ExtendedItemSubmit> {
    private final ItemFeatureRenderer vanilla = new ItemFeatureRenderer();
    private final MeshItems meshes = new MeshItems();
    /** Each prepared group's split, by the identity of the list it came in (a group may be executed more than once). */
    private final Map<List<ExtendedItemSubmit>, Split> splits = new IdentityHashMap<>();

    private record Split(List<ItemFeatureRenderer.Submit> vanilla, List<ExtendedItemSubmit> meshes) {
    }

    private static Split split(List<ExtendedItemSubmit> submits) {
        List<ItemFeatureRenderer.Submit> v = new ArrayList<>(submits.size());
        List<ExtendedItemSubmit> m = new ArrayList<>();
        for (ExtendedItemSubmit s : submits) {
            int before = v.size();
            try {
                splitOne(s, v, m);
            } catch (Throwable t) {
                Quads.featureFallback("an item submit", t);
                while (v.size() > before) v.removeLast();
                if (m.size() > 0 && m.getLast() == s) m.removeLast();
                if (!s.quads().isEmpty()) {
                    v.add(new ItemFeatureRenderer.Submit(s.pose(), s.displayContext(), s.lightCoords(), s.overlayCoords(), s.outlineColor(),
                        s.tintLayers(), s.quads(), s.foilType()));
                }
            }
        }
        return new Split(v, m);
    }

    private static void splitOne(ExtendedItemSubmit s, List<ItemFeatureRenderer.Submit> v, List<ExtendedItemSubmit> m) {
        {
            if (s.outlineColor() != 0) {
                List<BakedQuad> quads = new ArrayList<>(s.quads());
                s.mesh().forEach(q -> quads.add(Quads.toBaked(q)));
                RendererStats.MESH_QUADS_VANILLA.add(s.mesh().size());
                v.add(new ItemFeatureRenderer.Submit(s.pose(), s.displayContext(), s.lightCoords(), s.overlayCoords(), s.outlineColor(),
                    s.tintLayers(), quads, s.foilType()));
                return;
            }
            // vanilla quads and the mesh's vanilla-like quads to vanilla
            List<BakedQuad> quads = s.quads();
            boolean[] own = {false};
            if (s.mesh().size() > 0) {
                List<BakedQuad> all = new ArrayList<>(quads);
                s.mesh().forEach(q -> {
                    // with the block renderer off, every quad (vanilla's code draws everything)
                    if (QuicksilverRenderer.blockRendererOff() || Quads.vanillaLike(q)) all.add(Quads.toBaked(q));
                    else own[0] = true;
                });
                RendererStats.MESH_QUADS_VANILLA.add(all.size() - quads.size());
                quads = all;
            }
            if (!quads.isEmpty()) {
                v.add(new ItemFeatureRenderer.Submit(s.pose(), s.displayContext(), s.lightCoords(), s.overlayCoords(), 0, s.tintLayers(), quads,
                    s.foilType()));
            }
            if (own[0]) m.add(s);
        }
    }

    private Split splitFor(List<ExtendedItemSubmit> submits) {
        Split s = splits.get(submits);
        return s != null ? s : split(submits);
    }

    @Override
    public void beginPrepare(FeatureFrameContext context) {
        vanilla.beginPrepare(context);
        meshes.beginPrepare(context);
    }

    @Override
    public void prepareGroup(FeatureFrameContext context, List<ExtendedItemSubmit> submits, boolean flag) {
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
    public void executeGroup(FeatureFrameContext context, OitStage stage, RenderPass pass, int group, List<ExtendedItemSubmit> submits, boolean flag) {
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

    /** The main pass of API mesh quads in item submits. */
    static final class MeshItems extends RenderTypeFeatureRenderer<ExtendedItemSubmit> {
        private final MutableQuadViewImpl scratch = new MutableQuadViewImpl() {
            @Override
            protected void emitDirectly() {
            }
        };

        private final Quads.DecalInverse decal = new Quads.DecalInverse();
        /** Whether {@link #decal} holds the current submit's special-foil decal pose. */
        private boolean decalReady;

        @Override
        protected void buildGroup(FeatureFrameContext context, List<ExtendedItemSubmit> submits) {
            for (ExtendedItemSubmit s : submits) {
                decalReady = false;
                try {
                    s.mesh().forEach(q -> {
                        if (!Quads.vanillaLike(q)) buffer(s, q);
                    });
                } catch (Throwable t) {
                    Quads.featureFallback("an item submit's mesh", t);
                }
            }
        }

        private void buffer(ExtendedItemSubmit s, QuadView quad) {
            RendererStats.MESH_QUADS_QUICKSILVER.increment();
            ItemStackRenderState.FoilType foil = quad.foilType() != null ? quad.foilType() : s.foilType();
            RenderType type = switch (foil) {
                case NONE -> quad.itemRenderType();
                case STANDARD -> quad.itemGlintRenderType();
                case SPECIAL -> quad.itemGlintSpecialRenderType();
            };
            VertexConsumer consumer = getVertexBuilder(type);
            int layer = Quads.layerColor(s.tintLayers(), quad.tintIndex());
            MutableQuadViewImpl q = scratch;
            q.copyFrom(quad);
            for (int i = 0; i < 4; i++) {
                int c = quad.color(i);
                q.color(i, layer == -1 ? c : c == -1 ? layer : ARGB.multiply(c, layer));
                q.lightmap(i, quad.emissive() ? LightCoordsUtil.FULL_BRIGHT : Quads.withQuadLight(s.lightCoords(), quad.lightmap(i)));
            }
            if (foil == ItemStackRenderState.FoilType.SPECIAL) {
                if (!decalReady) {
                    decal.set(Quads.foilDecalPose(s.displayContext(), s.pose()));
                    decalReady = true;
                }
                Quads.bufferWithGlint(q, s.overlayCoords(), s.pose(), decal, consumer);
            } else {
                q.buffer(s.overlayCoords(), s.pose(), consumer);
            }
            q.clear();
        }
    }
}
