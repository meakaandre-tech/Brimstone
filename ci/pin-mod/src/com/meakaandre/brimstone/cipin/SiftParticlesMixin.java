package com.meakaandre.brimstone.cipin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * CI only. In the Sift dimension (only the SIFTEC suite's sift scene goes there), small sprites drift over the canyon at
 * random places, so every run's screenshot has them somewhere else, in vanilla's runs as much as Brimstone's (package
 * 3). No particle is made there in any CI client run, vanilla and Brimstone alike; the kinds held off are logged.
 */
@Mixin(ParticleEngine.class)
public abstract class SiftParticlesMixin {
    private static final Logger LOG = LoggerFactory.getLogger("brimstone_ci_pin");
    private static final Map<String, AtomicInteger> HELD = new ConcurrentHashMap<>();

    @Shadow
    protected ClientLevel level;

    private boolean brimstoneCi$inSift() {
        return level != null && "the_sift".equals(level.dimension().identifier().getNamespace());
    }

    private static void brimstoneCi$held(String kind) {
        int n = HELD.computeIfAbsent(kind, k -> new AtomicInteger()).incrementAndGet();
        if (n == 1 || n % 500 == 0) LOG.info("Brimstone CI pin: particles held off in the Sift: {}", HELD);
    }

    @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true)
    private void brimstoneCi$noSiftParticle(ParticleOptions options, double x, double y, double z, double dx, double dy, double dz,
                                            CallbackInfoReturnable<Particle> cir) {
        if (!brimstoneCi$inSift()) return;
        brimstoneCi$held(String.valueOf(BuiltInRegistries.PARTICLE_TYPE.getKey(options.getType())));
        cir.setReturnValue(null);
    }

    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void brimstoneCi$noSiftParticleAdded(Particle particle, CallbackInfo ci) {
        if (!brimstoneCi$inSift()) return;
        brimstoneCi$held(particle.getClass().getName());
        ci.cancel();
    }
}
