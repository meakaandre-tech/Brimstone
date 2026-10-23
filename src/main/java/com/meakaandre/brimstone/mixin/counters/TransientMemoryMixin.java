package com.meakaandre.brimstone.mixin.counters;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.meakaandre.brimstone.bench.GpuCounters;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import java.nio.ByteBuffer;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Counts uploads into 26.3's per-frame transient memory, in each implementation: OpenGL's persistent-mapping and
 * fallback paths (26.3 picks one from the driver) and Vulkan's. The default interface methods all end in these.
 * Mapped allocations count their size: the caller fills them from the CPU. Count only.
 */
@Mixin(targets = {
    "com.mojang.renderpearl.backend.opengl.GlTransientMemory$PersistentMapping",
    "com.mojang.renderpearl.backend.opengl.GlTransientMemory$Fallback",
    "com.mojang.renderpearl.backend.vulkan.VulkanTransientMemory"
})
public abstract class TransientMemoryMixin {
    @WrapMethod(method = "allocateStaging(JJIJJ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice$MappedView;")
    private GpuBufferSlice.MappedView brimstone$allocateStaging(long size, long alignment, int usage, long a, long b,
                                                               Operation<GpuBufferSlice.MappedView> original) {
        boolean outer = GpuCounters.enter();
        try {
            if (outer) GpuCounters.upload(GpuCounters.Upload.TRANSIENT, size);
            return original.call(size, alignment, usage, a, b);
        } finally {
            GpuCounters.exit();
        }
    }

    @WrapMethod(method = "allocateGpuMapped(JJIJJ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice$MappedView;")
    private GpuBufferSlice.MappedView brimstone$allocateGpuMapped(long size, long alignment, int usage, long a, long b,
                                                                 Operation<GpuBufferSlice.MappedView> original) {
        boolean outer = GpuCounters.enter();
        try {
            if (outer) GpuCounters.upload(GpuCounters.Upload.TRANSIENT, size);
            return original.call(size, alignment, usage, a, b);
        } finally {
            GpuCounters.exit();
        }
    }

    @WrapMethod(method = "uploadStaging(Ljava/util/List;JIJJ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;")
    private GpuBufferSlice brimstone$uploadStaging(List<ByteBuffer> data, long alignment, int usage, long a, long b,
                                                   Operation<GpuBufferSlice> original) {
        boolean outer = GpuCounters.enter();
        try {
            if (outer) GpuCounters.upload(GpuCounters.Upload.TRANSIENT, bytes(data));
            return original.call(data, alignment, usage, a, b);
        } finally {
            GpuCounters.exit();
        }
    }

    @WrapMethod(method = "uploadGpu(Ljava/util/List;JIJJ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;")
    private GpuBufferSlice brimstone$uploadGpu(List<ByteBuffer> data, long alignment, int usage, long a, long b,
                                               Operation<GpuBufferSlice> original) {
        boolean outer = GpuCounters.enter();
        try {
            if (outer) GpuCounters.upload(GpuCounters.Upload.TRANSIENT, bytes(data));
            return original.call(data, alignment, usage, a, b);
        } finally {
            GpuCounters.exit();
        }
    }

    @WrapMethod(method = "multiUploadStaging(Ljava/util/List;JI)Ljava/util/List;")
    private List<GpuBufferSlice> brimstone$multiUploadStaging(List<ByteBuffer> data, long alignment, int usage,
                                                              Operation<List<GpuBufferSlice>> original) {
        boolean outer = GpuCounters.enter();
        try {
            if (outer) GpuCounters.upload(GpuCounters.Upload.TRANSIENT, bytes(data));
            return original.call(data, alignment, usage);
        } finally {
            GpuCounters.exit();
        }
    }

    @WrapMethod(method = "multiUploadGpu(Ljava/util/List;JI)Ljava/util/List;")
    private List<GpuBufferSlice> brimstone$multiUploadGpu(List<ByteBuffer> data, long alignment, int usage,
                                                          Operation<List<GpuBufferSlice>> original) {
        boolean outer = GpuCounters.enter();
        try {
            if (outer) GpuCounters.upload(GpuCounters.Upload.TRANSIENT, bytes(data));
            return original.call(data, alignment, usage);
        } finally {
            GpuCounters.exit();
        }
    }

    private static long bytes(List<ByteBuffer> data) {
        long n = 0;
        if (data != null) for (ByteBuffer b : data) if (b != null) n += b.remaining();
        return n;
    }
}
