package com.meakaandre.brimstone.mixin.counters;

import com.meakaandre.brimstone.bench.GpuCounters;
import net.minecraft.client.renderer.feature.QuadParticleFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Package 8: draws made while vanilla draws its quad particles count as particle draws in the bench. Count only. */
@Mixin(QuadParticleFeatureRenderer.class)
public abstract class EffectsParticleDrawMixin {
    @Inject(method = "executeGroup", at = @At("HEAD"))
    private void brimstone$particlesStart(CallbackInfo ci) {
        GpuCounters.phase(GpuCounters.Phase.PARTICLES);
    }

    @Inject(method = "executeGroup", at = @At("RETURN"))
    private void brimstone$particlesEnd(CallbackInfo ci) {
        GpuCounters.phase(GpuCounters.Phase.OTHER);
    }
}
