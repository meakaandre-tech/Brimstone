package com.meakaandre.quicksilver.mixin.counters;

import com.meakaandre.quicksilver.bench.GpuCounters;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Package 8: draws made while an atlas redraws its animated sprites (once per client tick) count as animation draws in
 * the bench. Count only. When Quicksilver's animation module draws instead (it cancels vanilla's method), it sets and
 * clears the phase itself.
 */
@Mixin(TextureAtlas.class)
public abstract class EffectsAnimationMixin {
    @Inject(method = "uploadAnimationFrames", at = @At("HEAD"))
    private void quicksilver$animationStart(CallbackInfo ci) {
        GpuCounters.phase(GpuCounters.Phase.ANIMATION);
    }

    @Inject(method = "uploadAnimationFrames", at = @At("RETURN"))
    private void quicksilver$animationEnd(CallbackInfo ci) {
        GpuCounters.phase(GpuCounters.Phase.OTHER);
    }
}
