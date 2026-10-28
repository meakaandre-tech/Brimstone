package com.meakaandre.quicksilver.mixin.counters;

import com.meakaandre.quicksilver.bench.GpuCounters;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Counts buffers mapped for writing (the CPU then fills them), in the OpenGL and Vulkan buffers. Count only. */
@Mixin(targets = {
    "com.mojang.renderpearl.backend.opengl.GlBuffer$Direct",
    "com.mojang.renderpearl.backend.vulkan.VulkanGpuBuffer$Direct"
})
public abstract class BufferMapMixin {
    private static final String MAP = "map(JJZZ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice$MappedView;";

    @Inject(method = MAP, at = @At("HEAD"))
    private void quicksilver$map(long offset, long length, boolean read, boolean write, CallbackInfoReturnable<GpuBufferSlice.MappedView> cir) {
        if (GpuCounters.enter() && write) GpuCounters.upload(GpuCounters.Upload.MAP_WRITE, length);
    }

    @Inject(method = MAP, at = @At("RETURN"))
    private void quicksilver$mapEnd(long offset, long length, boolean read, boolean write, CallbackInfoReturnable<GpuBufferSlice.MappedView> cir) {
        GpuCounters.exit();
    }
}
