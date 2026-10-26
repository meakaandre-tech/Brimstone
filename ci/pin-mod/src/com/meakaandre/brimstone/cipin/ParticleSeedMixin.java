package com.meakaandre.brimstone.cipin;

import com.meakaandre.brimstone.cipinstate.Pins;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI only (package 8). A particle made in a pin area ({@link Pins}) seeds its own random numbers (which its constructor
 * uses for size, colour, roll and lifetime) from where and what it is, before its own constructor reads them, so it is
 * the same in every run, vanilla and Brimstone alike.
 */
@Mixin(Particle.class)
public abstract class ParticleSeedMixin {
    @Shadow
    @Final
    protected RandomSource random;

    @Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDD)V", at = @At("RETURN"))
    private void brimstoneCi$seed(ClientLevel level, double x, double y, double z, CallbackInfo ci) {
        if (Pins.area(level, x, y, z) == Pins.NONE) return;
        random.setSeed(Pins.seed(getClass().getName(), x, y, z, 0, 0, 0));
    }
}
