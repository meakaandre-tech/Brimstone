package com.meakaandre.quicksilver.mixin.counters;

import com.meakaandre.quicksilver.bench.EffectsStats;
import java.util.Map;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.minecraft.client.renderer.state.level.ParticlesRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Package 8: vanilla's particle tick and extraction, timed and counted for the bench. Measures only. */
@Mixin(ParticleEngine.class)
public abstract class EffectsParticleEngineMixin {
    @Shadow
    @Final
    private Map<ParticleRenderType, ParticleGroup<?>> particles;

    @Inject(method = "tick", at = @At("HEAD"))
    private void quicksilver$tickStart(CallbackInfo ci) {
        EffectsStats.particleTickStart();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void quicksilver$tickEnd(CallbackInfo ci) {
        int alive = 0;
        for (ParticleGroup<?> g : particles.values()) alive += g.size();
        EffectsStats.particleTickEnd(alive);
    }

    @Inject(method = "extract", at = @At("HEAD"))
    private void quicksilver$extractStart(CallbackInfo ci) {
        EffectsStats.extractStart();
    }

    @Inject(method = "extract", at = @At("RETURN"))
    private void quicksilver$extractEnd(ParticlesRenderState state, net.minecraft.client.renderer.culling.Frustum frustum,
                                      net.minecraft.client.Camera camera, float partialTick, CallbackInfo ci) {
        int drawn = 0;
        for (ParticleGroupRenderState g : state.particles) {
            if (g instanceof QuadParticleCountAccessor q) drawn += q.quicksilver$particleCount();
        }
        EffectsStats.extractEnd(drawn);
    }
}
