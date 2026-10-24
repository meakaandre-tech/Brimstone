package com.meakaandre.brimstone.cipin.diag;

import com.meakaandre.brimstone.cidiag.CiDiag;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** DIAGNOSTIC (branch only): the lightmap's inputs each frame. */
@Mixin(LightmapRenderStateExtractor.class)
public abstract class DiagLightmapMixin {
    @Inject(method = "extract", at = @At("TAIL"))
    private void brimstoneCi$diagExtract(LightmapRenderState s, float partialTick, CallbackInfo ci) {
        CiDiag.frame++;
        CiDiag.log("lightmap", "needsUpdate=" + s.needsUpdate + " blockFactor=" + CiDiag.r(s.blockFactor) + " blockTint=" + s.blockLightTint
            + " skyFactor=" + CiDiag.r(s.skyFactor) + " skyColor=" + s.skyLightColor + " ambient=" + s.ambientColor
            + " brightness=" + CiDiag.r(s.brightness) + " darkness=" + CiDiag.r(s.darknessEffectScale)
            + " nightVision=" + CiDiag.r(s.nightVisionEffectIntensity) + " boss=" + CiDiag.r(s.bossOverlayWorldDarkening));
    }
}
