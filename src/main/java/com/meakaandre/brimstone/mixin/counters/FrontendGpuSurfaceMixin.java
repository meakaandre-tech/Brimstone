package com.meakaandre.brimstone.mixin.counters;

import com.meakaandre.brimstone.bench.FrameRecorder;
import com.mojang.renderpearl.frontend.FrontendGpuSurface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** One call per presented frame: the frame boundary for frame times and per-frame counts. Observes only. */
@Mixin(FrontendGpuSurface.class)
public abstract class FrontendGpuSurfaceMixin {
    @Inject(method = "present", at = @At("HEAD"))
    private void brimstone$present(CallbackInfo ci) {
        FrameRecorder.onPresent();
    }
}
