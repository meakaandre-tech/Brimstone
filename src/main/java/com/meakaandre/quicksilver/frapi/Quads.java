package com.meakaandre.quicksilver.frapi;

import com.meakaandre.quicksilver.QuicksilverClient;
import com.meakaandre.quicksilver.mixin.frapi.ItemFeatureRendererInvoker;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.client.renderer.v1.sprite.FabricTextureAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/** Shared helpers for API quads: conversion to vanilla quads, light, tint and glint output. */
final class Quads {
    private Quads() {
    }

    /**
     * Whether a quad carries nothing a vanilla quad cannot: white vertex colours, no vertex normals, not emissive, no
     * foil override, and the same lightmap on every vertex as a vanilla light emission (block and sky alike). Such a
     * quad (a vanilla block model's, which the API emits into a mesh in entity contexts) is drawn by vanilla's own code
     * as a vanilla quad, if it also faces along an axis: vanilla gives a quad its face's axis as normal, Indigo (the
     * reference) its geometric normal, which differ for quads such as a cross model's (lit by that normal in entity
     * and item shaders).
     */
    static boolean vanillaLike(QuadView quad) {
        if (quad.emissive() || quad.foilType() != null) return false;
        Vector3fc n = quad.faceNormal(), axis = quad.lightFace().getUnitVec3f();
        if (n.x() * axis.x() + n.y() * axis.y() + n.z() * axis.z() < 1f - 1.0e-5f) return false;
        int l = quad.lightmap(0);
        if (LightCoordsUtil.pack(LightCoordsUtil.block(l), LightCoordsUtil.block(l)) != l) return false;
        for (int i = 0; i < 4; i++) {
            if (quad.color(i) != -1 || quad.hasNormal(i) || quad.lightmap(i) != l) return false;
        }
        return true;
    }

    /**
     * A vanilla quad with this quad's geometry, UVs and material data, facing its nominal face where it has one (a
     * vanilla quad's own facing, kept through {@code fromBakedQuad}), else its light face. A quad unchanged from a
     * vanilla quad (as meshes keep it) is that quad; otherwise the sprite is the one known for the quad, or the atlas's
     * sprite under its UVs.
     */
    static BakedQuad toBaked(QuadView quad) {
        TextureAtlasSprite sprite = null;
        Direction nominal = quad.nominalFace();
        if (quad instanceof QuadViewImpl v) {
            BakedQuad exact = v.exactQuad();
            if (exact != null) return exact;
            sprite = v.knownSprite();
            nominal = v.storedNominalFace();
        }
        if (sprite == null) {
            sprite = ((FabricTextureAtlas) Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(quad.atlas().getId())).spriteFinder().find(quad);
        }
        return build(quad, sprite, nominal != null ? nominal : quad.lightFace());
    }

    /**
     * A vanilla quad for vanilla's block lighting only, which reads its positions, facing (the light face) and shade
     * direction, never its sprite: {@code sprite} is any sprite.
     */
    static BakedQuad forLighting(QuadView quad, TextureAtlasSprite sprite) {
        return build(quad, sprite, quad.lightFace());
    }

    private static BakedQuad build(QuadView quad, TextureAtlasSprite sprite, Direction direction) {
        int emission = 0;
        for (int i = 0; i < 4; i++) {
            int l = quad.lightmap(i);
            int e = Math.min(LightCoordsUtil.block(l), LightCoordsUtil.sky(l));
            emission = i == 0 ? e : Math.min(emission, e);
        }
        BakedQuad.MaterialInfo info = new BakedQuad.MaterialInfo(sprite, quad.chunkLayer(), quad.itemRenderType(), quad.itemGlintRenderType(),
            quad.itemGlintSpecialRenderType(), quad.tintIndex(), quad.shadeDirectionOverride(), emission);
        return new BakedQuad(quad.copyPos(0, new Vector3f()), quad.copyPos(1, new Vector3f()), quad.copyPos(2, new Vector3f()), quad.copyPos(3, new Vector3f()),
            UVPair.pack(quad.u(0), quad.v(0)), UVPair.pack(quad.u(1), quad.v(1)), UVPair.pack(quad.u(2), quad.v(2)), UVPair.pack(quad.u(3), quad.v(3)),
            direction, info);
    }

    /**
     * The world's (or the submit's) light at a vertex combined with the quad's own minimum lightmap: vanilla's
     * light-emission rule where the quad's lightmap is a vanilla emission (block and sky alike, as a vanilla quad's
     * is), else the larger of each.
     */
    static int withQuadLight(int light, int quadLight) {
        if (quadLight == 0) return light;
        int block = LightCoordsUtil.block(quadLight), sky = LightCoordsUtil.sky(quadLight);
        if (block == sky && LightCoordsUtil.pack(block, sky) == quadLight) return LightCoordsUtil.lightCoordsWithEmission(light, block);
        return LightCoordsUtil.max(light, quadLight);
    }

    private static volatile boolean tintInvokerFailed, foilInvokerFailed, featureFallbackLogged;

    /**
     * A feature submit Quicksilver could not handle (its mesh, or a mod's render type function, threw): logged once, the
     * caller draws what vanilla can of it. An error of the JVM itself is not caught.
     */
    static void featureFallback(String what, Throwable t) {
        if (t instanceof VirtualMachineError e) throw e;
        if (!featureFallbackLogged) {
            featureFallbackLogged = true;
            QuicksilverClient.LOG.warn("Quicksilver renderer fallback: {} threw; vanilla's renderer draws its vanilla quads only, its API"
                + " mesh is not drawn (logged once)", what, t);
        }
    }

    /** An item layer's tint colour for a tint index, through vanilla's own lookup where it is reachable. */
    static int layerColor(int[] tintLayers, int tintIndex) {
        if (!tintInvokerFailed) {
            try {
                return ItemFeatureRendererInvoker.quicksilver$getLayerColorSafe(tintLayers, tintIndex);
            } catch (Throwable t) {
                tintInvokerFailed = true;
                QuicksilverClient.LOG.warn("Quicksilver renderer fallback: vanilla's item tint lookup is not reachable; API mesh items read"
                    + " the tint layers directly", t);
            }
        }
        return tintIndex >= 0 && tintIndex < tintLayers.length ? tintLayers[tintIndex] : -1;
    }

    /** The decal pose vanilla uses for special foil (compass and clock glint), through vanilla's own code. */
    static PoseStack.Pose foilDecalPose(ItemDisplayContext context, PoseStack.Pose pose) {
        if (!foilInvokerFailed) {
            try {
                return ItemFeatureRendererInvoker.quicksilver$computeFoilDecalPose(context, pose);
            } catch (Throwable t) {
                foilInvokerFailed = true;
                QuicksilverClient.LOG.warn("Quicksilver renderer fallback: vanilla's special-foil decal pose is not reachable; API mesh"
                    + " items use the item's pose for it", t);
            }
        }
        return pose;
    }

    /** The inverses of a decal pose that the sheeted decal UVs need, worked out once per submit (plus scratch vectors). */
    static final class DecalInverse {
        final Matrix4f pose = new Matrix4f();
        final Matrix3f normal = new Matrix3f();
        private final Vector3f faceNormal = new Vector3f(), position = new Vector3f(), ownNormal = new Vector3f();

        DecalInverse set(PoseStack.Pose decalPose) {
            pose.set(decalPose.pose()).invert();
            normal.set(decalPose.normal()).invert();
            return this;
        }
    }

    /**
     * Writes a quad as vanilla's {@code VertexConsumer.putBakedQuadWithGlint} writes a vanilla one: through the pose,
     * plus the sheeted decal UVs for the glint from the decal pose's inverses (normals as {@link QuadViewImpl#buffer}).
     */
    static void bufferWithGlint(QuadView quad, int overlayCoords, PoseStack.Pose pose, DecalInverse decal, VertexConsumer consumer) {
        Matrix4f m = pose.pose();
        Vector3f normal = pose.transformNormal(quad.faceNormal(), decal.faceNormal);
        Vector3f p = decal.position, own = decal.ownNormal;
        for (int i = 0; i < 4; i++) {
            m.transformPosition(quad.copyPos(i, p), p);
            Vector3f n = quad.hasNormal(i) ? pose.transformNormal(quad.normalX(i), quad.normalY(i), quad.normalZ(i), own) : normal;
            consumer.addVertex(p.x(), p.y(), p.z(), quad.color(i), quad.u(i), quad.v(i), overlayCoords, quad.lightmap(i), n.x(), n.y(), n.z());
            SheetedDecalTextureGenerator.setSheetedDecalUv(p, n, decal.pose, decal.normal, 1f / 128f, consumer);
        }
    }
}
