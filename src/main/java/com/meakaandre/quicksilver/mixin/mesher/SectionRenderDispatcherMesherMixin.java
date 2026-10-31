package com.meakaandre.quicksilver.mixin.mesher;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.meakaandre.quicksilver.mesher.ServerAware;
import net.minecraft.util.Util;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Package 4c, server-aware meshing threads ({@link ServerAware}): vanilla's task loop on its worker threads meshes one
 * section per run; while the integrated server is behind, a run that would go over the limit returns at once, and the
 * render thread starts the loop again at the end of each frame while sections were held back and the limit allows.
 */
@Mixin(SectionRenderDispatcher.class)
public abstract class SectionRenderDispatcherMesherMixin {
    @Shadow
    private void runTask() {
        throw new AssertionError();
    }

    @WrapMethod(method = "runTask")
    private void quicksilver$serverAware(Operation<Void> original) {
        if (!ServerAware.enter()) return;
        try {
            original.call();
        } finally {
            if (ServerAware.exit()) Util.backgroundExecutor().execute(() -> runTask());
        }
    }

    @Inject(method = "uploadTerrainBuffersToGpu", at = @At("TAIL"))
    private void quicksilver$restart(CallbackInfo ci) {
        com.meakaandre.quicksilver.mesher.MeshPriority.frame();
        com.meakaandre.quicksilver.mesher.Mesher.sweep();
        int n = ServerAware.restarts();
        for (int i = 0; i < n; i++) Util.backgroundExecutor().execute(() -> runTask());
    }
}
