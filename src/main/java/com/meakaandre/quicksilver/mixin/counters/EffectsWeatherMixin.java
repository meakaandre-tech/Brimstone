package com.meakaandre.quicksilver.mixin.counters;

import com.meakaandre.quicksilver.bench.GpuCounters;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Package 8: draws made while vanilla draws rain or snow count as weather draws in the bench. Count only. */
@Mixin(WeatherEffectRenderer.class)
public abstract class EffectsWeatherMixin {
    private static final String RENDER = "render(Lnet/minecraft/client/renderer/state/level/WeatherRenderState;Lcom/mojang/renderpearl/api/commands/RenderPass;)V";

    @Inject(method = {RENDER, "renderOit"}, at = @At("HEAD"))
    private void quicksilver$weatherStart(CallbackInfo ci) {
        GpuCounters.phase(GpuCounters.Phase.WEATHER);
    }

    @Inject(method = {RENDER, "renderOit"}, at = @At("RETURN"))
    private void quicksilver$weatherEnd(CallbackInfo ci) {
        GpuCounters.phase(GpuCounters.Phase.OTHER);
    }
}
