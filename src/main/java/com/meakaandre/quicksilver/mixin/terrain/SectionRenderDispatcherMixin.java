package com.meakaandre.quicksilver.mixin.terrain;

import com.meakaandre.quicksilver.terrain.Terrain;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Quicksilver's terrain uploads happen where vanilla's do (render thread, end of frame), and end with the dispatcher. */
@Mixin(SectionRenderDispatcher.class)
public abstract class SectionRenderDispatcherMixin {
    @Inject(method = "uploadTerrainBuffersToGpu", at = @At("TAIL"))
    private void quicksilver$upload(CallbackInfo ci) {
        Terrain.upload();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void quicksilver$created(CallbackInfo ci) {
        Terrain.dispatcherCreated();
    }

    @Inject(method = "dispose", at = @At("HEAD"))
    private void quicksilver$dispose(CallbackInfo ci) {
        Terrain.disposed();
    }

    /** Again once vanilla has cleared its queue: a worker task that was still running may have staged meanwhile. */
    @Inject(method = "dispose", at = @At("TAIL"))
    private void quicksilver$disposeEnd(CallbackInfo ci) {
        Terrain.disposed();
    }
}
