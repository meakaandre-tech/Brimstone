package com.meakaandre.brimstone.cipin.diag;

import com.meakaandre.brimstone.cidiag.CiDiag;
import com.mojang.blaze3d.platform.Lighting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** DIAGNOSTIC (branch only): the lighting entry most recently bound. */
@Mixin(Lighting.class)
public abstract class DiagLightingMixin {
    @Inject(method = "setupFor", at = @At("HEAD"))
    private void brimstoneCi$diagSetup(Lighting.Entry entry, CallbackInfo ci) {
        CiDiag.lighting = String.valueOf(entry);
    }
}
