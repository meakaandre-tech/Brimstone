package com.meakaandre.quicksilver.cipin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI only. The game varies block light by a small random "flicker" every tick. Wherever block light is above zero
 * (in the nether, most of the frame: lava lights it) that shifts the whole picture by one colour level from one
 * moment to the next, so two identical runs differ by chance. Held at zero (its resting value) in every CI client
 * run, vanilla and Quicksilver alike.
 */
@Mixin(targets = "net.minecraft.client.renderer.LightmapRenderStateExtractor")
public abstract class LightmapFlickerMixin {
    @Shadow
    private float blockLightFlicker;

    @Inject(method = "tick", at = @At("TAIL"))
    private void quicksilverCi$holdFlicker(CallbackInfo ci) {
        blockLightFlicker = 0.0F;
    }
}
