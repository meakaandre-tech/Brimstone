package com.meakaandre.brimstone.frapi;

import com.meakaandre.brimstone.mixin.frapi.ItemFeatureRendererInvoker;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.client.renderer.v1.sprite.FabricTextureAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
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
     * quad (every vanilla model's, through the API) is drawn by vanilla's own code as a vanilla quad, if it also faces
     * along an axis: vanilla gives a quad its face's axis as normal, Indigo (the reference) its geometric normal, which
     * differ for quads such as a cross model's (lit by that normal in entity and item shaders).
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
     * A vanilla quad with this quad's geometry, UVs and material data ({@code sprite} if known, else the atlas's),
     * facing its light face, or with {@code nominal} its nominal face where it has one (a vanilla quad's own facing,
     * kept through {@code fromBakedQuad}).
     */
    static BakedQuad toBaked(QuadView quad, TextureAtlasSprite sprite, boolean nominal) {
        if (sprite == null) {
            sprite = ((FabricTextureAtlas) Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(quad.atlas().getId())).spriteFinder().find(quad);
        }
        // the quad's own facing where it still has one (a vanilla quad's, through fromBakedQuad), else its light face
        net.minecraft.core.Direction direction = nominal && quad.nominalFace() != null ? quad.nominalFace() : quad.lightFace();
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

    private static volatile boolean invokerFailed;

    /** An item layer's tint colour for a tint index, through vanilla's own lookup where it is reachable. */
    static int layerColor(int[] tintLayers, int tintIndex) {
        if (!invokerFailed) {
            try {
                return ItemFeatureRendererInvoker.brimstone$getLayerColorSafe(tintLayers, tintIndex);
            } catch (Throwable t) {
                invokerFailed = true;
            }
        }
        return tintIndex >= 0 && tintIndex < tintLayers.length ? tintLayers[tintIndex] : -1;
    }

    /** The decal pose vanilla uses for special foil (compass and clock glint), through vanilla's own code. */
    static PoseStack.Pose foilDecalPose(ItemDisplayContext context, PoseStack.Pose pose) {
        try {
            return ItemFeatureRendererInvoker.brimstone$computeFoilDecalPose(context, pose);
        } catch (Throwable t) {
            invokerFailed = true;
            return pose;
        }
    }

    /**
     * Writes a quad as vanilla's {@code VertexConsumer.putBakedQuadWithGlint} writes a vanilla one: through the pose,
     * plus the sheeted decal UVs for the glint from the decal pose (normals as {@link QuadViewImpl#buffer}).
     */
    static void bufferWithGlint(QuadView quad, int overlayCoords, PoseStack.Pose pose, PoseStack.Pose decalPose, VertexConsumer consumer) {
        Vector3fc face = quad.faceNormal();
        Matrix4f m = pose.pose();
        Vector3f normal = pose.transformNormal(face, new Vector3f());
        Matrix4f inversePose = new Matrix4f(decalPose.pose()).invert();
        Matrix3f inverseNormal = new Matrix3f(decalPose.normal()).invert();
        for (int i = 0; i < 4; i++) {
            Vector3f p = m.transformPosition(quad.copyPos(i, new Vector3f()), new Vector3f());
            Vector3f n = quad.hasNormal(i) ? pose.transformNormal(quad.normalX(i), quad.normalY(i), quad.normalZ(i), new Vector3f()) : normal;
            consumer.addVertex(p.x(), p.y(), p.z(), quad.color(i), quad.u(i), quad.v(i), overlayCoords, quad.lightmap(i), n.x(), n.y(), n.z());
            SheetedDecalTextureGenerator.setSheetedDecalUv(p, n, inversePose, inverseNormal, 1f / 128f, consumer);
        }
    }
}
