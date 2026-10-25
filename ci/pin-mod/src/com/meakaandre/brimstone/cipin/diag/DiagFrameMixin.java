package com.meakaandre.brimstone.cipin.diag;

import com.meakaandre.brimstone.cidiag.CiDiag;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** DIAGNOSTIC (branch only): frame boundary for the per-frame draw log. */
@Mixin(LightmapRenderStateExtractor.class)
public abstract class DiagFrameMixin {
    @Inject(method = "extract", at = @At("TAIL"))
    private void brimstoneCi$diagFrame(LightmapRenderState s, float partialTick, CallbackInfo ci) {
        CiDiag.frameStart(partialTick);
    }
}
