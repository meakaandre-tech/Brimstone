package com.meakaandre.quicksilver.cipin;

import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.world.entity.animal.chicken.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI only. A chicken flaps its wings by an amount that depends on its last few ticks of falling, so the mobs scene's
 * chickens are caught with their wings up in one run and down in the next (up to about 700 strict pixels between two
 * vanilla runs). Held at rest (wings folded) in every CI client run, vanilla and Quicksilver alike.
 */
@Mixin(ChickenRenderer.class)
public abstract class ChickenFlapMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/chicken/Chicken;Lnet/minecraft/client/renderer/entity/state/ChickenRenderState;F)V",
            at = @At("TAIL"))
    private void quicksilverCi$foldWings(Chicken chicken, ChickenRenderState state, float partialTick, CallbackInfo ci) {
        state.flap = 0.0F;
        state.flapSpeed = 0.0F;
    }
}
