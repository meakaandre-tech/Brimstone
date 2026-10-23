package com.meakaandre.brimstone.mixin.counters;

import com.meakaandre.brimstone.bench.GpuCounters;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import java.nio.ByteBuffer;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    @Inject(method = "allocateStaging(JJIJJ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice$MappedView;", at = @At("HEAD"))
    private void brimstone$allocateStaging(long size, long alignment, int usage, long a, long b, CallbackInfoReturnable<GpuBufferSlice.MappedView> cir) {
        if (GpuCounters.enter()) GpuCounters.upload(GpuCounters.Upload.TRANSIENT, size);
    }

    @Inject(method = "allocateStaging(JJIJJ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice$MappedView;", at = @At("RETURN"))
    private void brimstone$allocateStagingEnd(long size, long alignment, int usage, long a, long b, CallbackInfoReturnable<GpuBufferSlice.MappedView> cir) {
        GpuCounters.exit();
    }

    @Inject(method = "allocateGpuMapped(JJIJJ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice$MappedView;", at = @At("HEAD"))
    private void brimstone$allocateGpuMapped(long size, long alignment, int usage, long a, long b, CallbackInfoReturnable<GpuBufferSlice.MappedView> cir) {
        if (GpuCounters.enter()) GpuCounters.upload(GpuCounters.Upload.TRANSIENT, size);
    }

    @Inject(method = "allocateGpuMapped(JJIJJ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice$MappedView;", at = @At("RETURN"))
    private void brimstone$allocateGpuMappedEnd(long size, long alignment, int usage, long a, long b, CallbackInfoReturnable<GpuBufferSlice.MappedView> cir) {
        GpuCounters.exit();
    }

    @Inject(method = "uploadStaging(Ljava/util/List;JIJJ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;", at = @At("HEAD"))
    private void brimstone$uploadStaging(List<ByteBuffer> data, long alignment, int usage, long a, long b, CallbackInfoReturnable<GpuBufferSlice> cir) {
        if (GpuCounters.enter()) GpuCounters.upload(GpuCounters.Upload.TRANSIENT, bytes(data));
    }

    @Inject(method = "uploadStaging(Ljava/util/List;JIJJ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;", at = @At("RETURN"))
    private void brimstone$uploadStagingEnd(List<ByteBuffer> data, long alignment, int usage, long a, long b, CallbackInfoReturnable<GpuBufferSlice> cir) {
        GpuCounters.exit();
    }

    @Inject(method = "uploadGpu(Ljava/util/List;JIJJ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;", at = @At("HEAD"))
    private void brimstone$uploadGpu(List<ByteBuffer> data, long alignment, int usage, long a, long b, CallbackInfoReturnable<GpuBufferSlice> cir) {
        if (GpuCounters.enter()) GpuCounters.upload(GpuCounters.Upload.TRANSIENT, bytes(data));
    }

    @Inject(method = "uploadGpu(Ljava/util/List;JIJJ)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;", at = @At("RETURN"))
    private void brimstone$uploadGpuEnd(List<ByteBuffer> data, long alignment, int usage, long a, long b, CallbackInfoReturnable<GpuBufferSlice> cir) {
        GpuCounters.exit();
    }

    @Inject(method = "multiUploadStaging(Ljava/util/List;JI)Ljava/util/List;", at = @At("HEAD"))
    private void brimstone$multiUploadStaging(List<ByteBuffer> data, long alignment, int usage, CallbackInfoReturnable<List<GpuBufferSlice>> cir) {
        if (GpuCounters.enter()) GpuCounters.upload(GpuCounters.Upload.TRANSIENT, bytes(data));
    }

    @Inject(method = "multiUploadStaging(Ljava/util/List;JI)Ljava/util/List;", at = @At("RETURN"))
    private void brimstone$multiUploadStagingEnd(List<ByteBuffer> data, long alignment, int usage, CallbackInfoReturnable<List<GpuBufferSlice>> cir) {
        GpuCounters.exit();
    }

    @Inject(method = "multiUploadGpu(Ljava/util/List;JI)Ljava/util/List;", at = @At("HEAD"))
    private void brimstone$multiUploadGpu(List<ByteBuffer> data, long alignment, int usage, CallbackInfoReturnable<List<GpuBufferSlice>> cir) {
        if (GpuCounters.enter()) GpuCounters.upload(GpuCounters.Upload.TRANSIENT, bytes(data));
    }

    @Inject(method = "multiUploadGpu(Ljava/util/List;JI)Ljava/util/List;", at = @At("RETURN"))
    private void brimstone$multiUploadGpuEnd(List<ByteBuffer> data, long alignment, int usage, CallbackInfoReturnable<List<GpuBufferSlice>> cir) {
        GpuCounters.exit();
    }

    private static long bytes(List<ByteBuffer> data) {
        long n = 0;
        if (data != null) for (ByteBuffer b : data) if (b != null) n += b.remaining();
        return n;
    }
}
