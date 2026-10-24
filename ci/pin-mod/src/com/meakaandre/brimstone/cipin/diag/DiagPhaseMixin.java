package com.meakaandre.brimstone.cipin.diag;

import com.meakaandre.brimstone.cidiag.CiDiag;
import com.mojang.renderpearl.api.commands.RenderPass;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** DIAGNOSTIC (branch only): marks which feature phase each draw belongs to. */
@Mixin(FeatureRenderDispatcher.PreparedFrame.class)
public abstract class DiagPhaseMixin {
    @Inject(method = "executeSolid", at = @At("HEAD"))
    private void brimstoneCi$s(RenderPass pass, CallbackInfo ci) { CiDiag.draw("|SOLID|"); }

    @Inject(method = "executeTranslucent", at = @At("HEAD"))
    private void brimstoneCi$t(RenderPass pass, CallbackInfo ci) { CiDiag.draw("|TRANSLUCENT|"); }

    @Inject(method = "executeTranslucentAfterTerrain", at = @At("HEAD"))
    private void brimstoneCi$ta(RenderPass pass, CallbackInfo ci) { CiDiag.draw("|TRANSLUCENT_AFTER_TERRAIN|"); }

    @Inject(method = "executeSeeThrough", at = @At("HEAD"))
    private void brimstoneCi$st(RenderPass pass, CallbackInfo ci) { CiDiag.draw("|SEE_THROUGH|"); }

    @Inject(method = "executeAlwaysOnTop", at = @At("HEAD"))
    private void brimstoneCi$aot(RenderPass pass, CallbackInfo ci) { CiDiag.draw("|ALWAYS_ON_TOP|"); }

    @Inject(method = "executeOutline", at = @At("HEAD"))
    private void brimstoneCi$o(RenderPass pass, CallbackInfo ci) { CiDiag.draw("|OUTLINE|"); }
}
