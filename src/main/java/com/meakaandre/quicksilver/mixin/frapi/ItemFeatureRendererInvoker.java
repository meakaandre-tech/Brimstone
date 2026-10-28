package com.meakaandre.quicksilver.mixin.frapi;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Vanilla's own item tint lookup and special-foil decal pose, for API meshes drawn as items (package 4). */
@Mixin(ItemFeatureRenderer.class)
public interface ItemFeatureRendererInvoker {
    @Invoker("getLayerColorSafe")
    static int quicksilver$getLayerColorSafe(int[] tintLayers, int tintIndex) {
        throw new AssertionError();
    }

    @Invoker("computeFoilDecalPose")
    static PoseStack.Pose quicksilver$computeFoilDecalPose(ItemDisplayContext context, PoseStack.Pose pose) {
        throw new AssertionError();
    }
}
