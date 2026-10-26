package com.meakaandre.brimstone.mixin.counters;

import com.meakaandre.brimstone.bench.GpuCounters;
import net.minecraft.client.renderer.SkyRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Package 8: draws made while vanilla draws the sky count as sky draws in the bench. Count only. */
@Mixin(SkyRenderer.class)
public abstract class EffectsSkyMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void brimstone$skyStart(CallbackInfo ci) {
        GpuCounters.phase(GpuCounters.Phase.SKY);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void brimstone$skyEnd(CallbackInfo ci) {
        GpuCounters.phase(GpuCounters.Phase.OTHER);
    }
}
