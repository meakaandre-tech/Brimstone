package com.meakaandre.quicksilver.cipin;

import com.meakaandre.quicksilver.cipinstate.Pins;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * CI only (package 8). In the pin areas ({@link Pins}) a particle is the same in every run, vanilla and Quicksilver
 * alike: the random numbers its provider draws (size, colour, sprite) come from where and what it is, and while the
 * player is in an area the particles are drawn at partial tick 0 (they are not ticked either, see
 * {@link ParticleTickMixin}), so the moment a screenshot catches doesn't matter.
 */
@Mixin(ParticleEngine.class)
public abstract class ParticleAreaMixin {
    @Shadow
    protected ClientLevel level;
    @Shadow
    @Final
    private RandomSource random;

    @Inject(method = "createParticle", at = @At("HEAD"))
    private void quicksilverCi$seedProvider(ParticleOptions options, double x, double y, double z, double dx, double dy, double dz,
                                          CallbackInfoReturnable<Particle> cir) {
        if (Pins.area(level, x, y, z) == Pins.NONE) return;
        random.setSeed(Pins.seed(String.valueOf(BuiltInRegistries.PARTICLE_TYPE.getKey(options.getType())), x, y, z, dx, dy, dz));
    }

    @ModifyVariable(method = "extract", at = @At("HEAD"), argsOnly = true)
    private float quicksilverCi$frozenPartialTick(float partialTick) {
        return Pins.playerArea != Pins.NONE ? 0.0F : partialTick;
    }
}
