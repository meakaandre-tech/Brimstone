package com.meakaandre.quicksilver.mixin.debug;

import com.meakaandre.quicksilver.QuicksilverClient;
import com.mojang.renderpearl.api.device.DeviceInfo;
import com.mojang.renderpearl.api.device.HintsAndWorkarounds;
import com.mojang.renderpearl.backend.opengl.GlHeuristics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * CI only: with {@code -Dquicksilver.debug.forceIntelMdiIssue=true}, OpenGL devices get vanilla's own Intel workaround
 * flag (multi-draw indirect has known issues), so the path the school laptop takes runs on CI's Mesa driver. Without
 * the property this does nothing.
 */
@Mixin(GlHeuristics.class)
public abstract class GlHeuristicsMixin {
    @Inject(method = "createDeviceInfo", at = @At("RETURN"), cancellable = true)
    private void quicksilver$forceIntelMdiIssue(CallbackInfoReturnable<DeviceInfo> cir) {
        if (!Boolean.getBoolean("quicksilver.debug.forceIntelMdiIssue")) return;
        DeviceInfo i = cir.getReturnValue();
        HintsAndWorkarounds h = i.hintsAndWorkarounds();
        if (h.multiDrawIndirectHasKnownIssues()) return;
        QuicksilverClient.LOG.warn("Quicksilver debug: quicksilver.debug.forceIntelMdiIssue is set, marking multi-draw indirect as having known issues (vanilla's Intel workaround)");
        cir.setReturnValue(new DeviceInfo(i.name(), i.vendorName(), i.driverInfo(), i.isZZeroToOne(), i.backendName(), i.timestampPeriod(),
            i.limits(), i.features(), i.underlyingExtensions(),
            new HintsAndWorkarounds(h.writeToBufferIsSlow(), h.anisotropyHasKnownIssues(), h.isExplicitDepthRequired(), true), i.type()));
    }
}
