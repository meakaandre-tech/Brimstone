package com.meakaandre.quicksilver.mixin.counters;

import com.meakaandre.quicksilver.bench.GpuCounters;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.frontend.FrontendGpuDevice;
import java.nio.ByteBuffer;
import java.util.function.Supplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Counts buffers created with initial data (an upload) through 26.3's frontend device. Count only. */
@Mixin(FrontendGpuDevice.class)
public abstract class FrontendGpuDeviceMixin {
    private static final String CREATE = "createBuffer(Ljava/util/function/Supplier;ILjava/nio/ByteBuffer;)Lcom/mojang/renderpearl/api/buffers/GpuBuffer;";

    @Inject(method = CREATE, at = @At("HEAD"))
    private void quicksilver$createBuffer(Supplier<String> label, int usage, ByteBuffer data, CallbackInfoReturnable<GpuBuffer> cir) {
        if (GpuCounters.enter()) GpuCounters.upload(GpuCounters.Upload.CREATE_BUFFER, data == null ? 0 : data.remaining());
    }

    @Inject(method = CREATE, at = @At("RETURN"))
    private void quicksilver$createBufferEnd(Supplier<String> label, int usage, ByteBuffer data, CallbackInfoReturnable<GpuBuffer> cir) {
        GpuCounters.exit();
    }
}
