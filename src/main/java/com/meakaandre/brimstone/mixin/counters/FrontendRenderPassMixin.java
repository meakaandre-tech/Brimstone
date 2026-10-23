package com.meakaandre.brimstone.mixin.counters;

import com.meakaandre.brimstone.bench.GpuCounters;
import com.mojang.renderpearl.frontend.FrontendRenderPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Counts draw calls where 26.3's frontend render pass hands them to the backend (OpenGL or Vulkan alike): one count
 * per backend call, just before it. Count only.
 */
@Mixin(FrontendRenderPass.class)
public abstract class FrontendRenderPassMixin {
    private static final String BACKEND = "Lcom/mojang/renderpearl/backend/api/RenderPassBackend;";

    @Inject(method = {"drawIndexed(IIIII)V", "drawMultipleIndexed"},
        at = @At(value = "INVOKE", target = BACKEND + "drawIndexed(IIIII)V"))
    private void brimstone$drawIndexed(CallbackInfo ci) {
        GpuCounters.draw(GpuCounters.Draw.SINGLE);
    }

    @Inject(method = "draw(IIII)V", at = @At(value = "INVOKE", target = BACKEND + "draw(IIII)V"))
    private void brimstone$draw(CallbackInfo ci) {
        GpuCounters.draw(GpuCounters.Draw.SINGLE);
    }

    @Inject(method = "multiDrawIndexed(Ljava/nio/IntBuffer;III)V",
        at = @At(value = "INVOKE", target = BACKEND + "multiDrawIndexed(Ljava/nio/IntBuffer;III)V"))
    private void brimstone$multiDrawIndexed(CallbackInfo ci) {
        GpuCounters.draw(GpuCounters.Draw.MULTI);
    }

    @Inject(method = "multiDrawIndexed(Lorg/lwjgl/PointerBuffer;Ljava/nio/IntBuffer;Ljava/nio/IntBuffer;I)V",
        at = @At(value = "INVOKE", target = BACKEND + "multiDrawIndexed(Lorg/lwjgl/PointerBuffer;Ljava/nio/IntBuffer;Ljava/nio/IntBuffer;I)V"))
    private void brimstone$multiDrawIndexedSeparate(CallbackInfo ci) {
        GpuCounters.draw(GpuCounters.Draw.MULTI);
    }

    @Inject(method = "multiDraw(Ljava/nio/IntBuffer;III)V",
        at = @At(value = "INVOKE", target = BACKEND + "multiDraw(Ljava/nio/IntBuffer;III)V"))
    private void brimstone$multiDraw(CallbackInfo ci) {
        GpuCounters.draw(GpuCounters.Draw.MULTI);
    }

    @Inject(method = "multiDraw(Ljava/nio/IntBuffer;Ljava/nio/IntBuffer;I)V",
        at = @At(value = "INVOKE", target = BACKEND + "multiDraw(Ljava/nio/IntBuffer;Ljava/nio/IntBuffer;I)V"))
    private void brimstone$multiDrawSeparate(CallbackInfo ci) {
        GpuCounters.draw(GpuCounters.Draw.MULTI);
    }

    @Inject(method = "drawIndexedIndirect", at = @At(value = "INVOKE", target = BACKEND + "drawIndexedIndirect(Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;I)V"))
    private void brimstone$drawIndexedIndirect(CallbackInfo ci) {
        GpuCounters.draw(GpuCounters.Draw.INDIRECT);
    }

    @Inject(method = "drawIndirect", at = @At(value = "INVOKE", target = BACKEND + "drawIndirect(Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;I)V"))
    private void brimstone$drawIndirect(CallbackInfo ci) {
        GpuCounters.draw(GpuCounters.Draw.INDIRECT);
    }
}
