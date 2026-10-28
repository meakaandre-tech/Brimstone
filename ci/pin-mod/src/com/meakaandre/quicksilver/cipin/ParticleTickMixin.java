package com.meakaandre.quicksilver.cipin;

import com.meakaandre.quicksilver.cipinstate.Pins;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI only (package 8). A particle in a pin area ({@link Pins}) is not ticked (it doesn't move, age or die) while the
 * player is in the same area, and is removed once the player is elsewhere, so a scene shows exactly the particles its
 * setup made, in every run, vanilla and Quicksilver alike.
 */
@Mixin(targets = "net.minecraft.client.particle.ParticleGroup")
public abstract class ParticleTickMixin {
    @Inject(method = "tickParticle", at = @At("HEAD"), cancellable = true)
    private void quicksilverCi$freeze(Particle particle, CallbackInfo ci) {
        AABB b = particle.getBoundingBox();
        int area = Pins.area(Minecraft.getInstance().level, (b.minX + b.maxX) * 0.5, (b.minY + b.maxY) * 0.5, (b.minZ + b.maxZ) * 0.5);
        if (area == Pins.NONE) return;
        if (area != Pins.playerArea) {
            // gone with its next tick, through vanilla's own removal
            particle.remove();
            return;
        }
        ci.cancel();
    }
}
