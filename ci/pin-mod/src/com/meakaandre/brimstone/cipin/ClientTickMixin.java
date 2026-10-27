package com.meakaandre.brimstone.cipin;

import com.meakaandre.brimstone.cipinstate.Pins;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** CI only (package 8): the pin areas' state, worked out at the start of each client tick ({@link Pins}). */
@Mixin(Minecraft.class)
public abstract class ClientTickMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void brimstoneCi$pinAreas(CallbackInfo ci) {
        Pins.clientTick((Minecraft) (Object) this);
    }
}
