package com.meakaandre.brimstone.mixin.counters;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.meakaandre.brimstone.bench.GpuCounters;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import org.spongepowered.asm.mixin.Mixin;

/** Counts buffers mapped for writing (the CPU then fills them), in the OpenGL and Vulkan buffers. Count only. */
@Mixin(targets = {
    "com.mojang.renderpearl.backend.opengl.GlBuffer$Direct",
    "com.mojang.renderpearl.backend.vulkan.VulkanGpuBuffer$Direct"
})
public abstract class BufferMapMixin {
    @WrapMethod(method = "map(JJZZ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice$MappedView;")
    private GpuBufferSlice.MappedView brimstone$map(long offset, long length, boolean read, boolean write,
                                                   Operation<GpuBufferSlice.MappedView> original) {
        boolean outer = GpuCounters.enter();
        try {
            if (outer && write) GpuCounters.upload(GpuCounters.Upload.MAP_WRITE, length);
            return original.call(offset, length, read, write);
        } finally {
            GpuCounters.exit();
        }
    }
}
