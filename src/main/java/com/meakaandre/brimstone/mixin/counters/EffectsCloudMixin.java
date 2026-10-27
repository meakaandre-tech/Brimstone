package com.meakaandre.brimstone.mixin.counters;

import com.meakaandre.brimstone.bench.GpuCounters;
import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Package 8: draws made while vanilla draws clouds count as cloud draws in the bench. Count only. */
@Mixin(CloudRenderer.class)
public abstract class EffectsCloudMixin {
    private static final String RENDER = "render(Lnet/minecraft/client/CloudStatus;Lcom/mojang/renderpearl/api/commands/RenderPass;)V";

    @Inject(method = {RENDER, "renderOit"}, at = @At("HEAD"))
    private void brimstone$cloudsStart(CallbackInfo ci) {
        GpuCounters.phase(GpuCounters.Phase.CLOUDS);
    }

    @Inject(method = {RENDER, "renderOit"}, at = @At("RETURN"))
    private void brimstone$cloudsEnd(CallbackInfo ci) {
        GpuCounters.phase(GpuCounters.Phase.OTHER);
    }
}
