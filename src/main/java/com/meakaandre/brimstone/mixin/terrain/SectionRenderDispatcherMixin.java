package com.meakaandre.brimstone.mixin.terrain;

import com.meakaandre.brimstone.terrain.Terrain;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Brimstone's terrain uploads happen where vanilla's do (render thread, end of frame), and end with the dispatcher. */
@Mixin(SectionRenderDispatcher.class)
public abstract class SectionRenderDispatcherMixin {
    @Inject(method = "uploadTerrainBuffersToGpu", at = @At("TAIL"))
    private void brimstone$upload(CallbackInfo ci) {
        Terrain.upload();
    }

    @Inject(method = "dispose", at = @At("HEAD"))
    private void brimstone$dispose(CallbackInfo ci) {
        Terrain.disposed();
    }
}
