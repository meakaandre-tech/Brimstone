package com.meakaandre.quicksilver.cipin;

import com.meakaandre.quicksilver.cipinstate.Pins;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** CI only (package 8): the pin areas' state, worked out at the start of each client tick ({@link Pins}). */
@Mixin(Minecraft.class)
public abstract class ClientTickMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void quicksilverCi$pinAreas(CallbackInfo ci) {
        Pins.clientTick((Minecraft) (Object) this);
    }
}
