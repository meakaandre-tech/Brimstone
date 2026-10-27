package com.meakaandre.brimstone.cipin;

import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.state.BannerRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI only (package 8). A banner's flag sways with the world's game time, which keeps running while the time of day is
 * pinned; the moment a screenshot is taken decides where the flag is (core/occlusion_move.x2309 shows the occlusion
 * scene's banner edge-on through its window: 0 to 330 strict pixels between Brimstone's run and vanilla's in five
 * runs, whose game times differ by the benches Brimstone runs before it). The sway is held at its resting phase in
 * every CI client run, vanilla and Brimstone alike.
 */
@Mixin(BannerRenderer.class)
public abstract class BannerSwayMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
        at = @At("RETURN"))
    private void brimstoneCi$holdSway(BannerBlockEntity banner, BannerRenderState state, float partialTick, Vec3 camera,
                                      ModelFeatureRenderer.CrumblingOverlay overlay, CallbackInfo ci) {
        state.phase = 0.0F;
    }
}
