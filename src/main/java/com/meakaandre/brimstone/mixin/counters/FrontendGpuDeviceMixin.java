package com.meakaandre.brimstone.mixin.counters;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.meakaandre.brimstone.bench.GpuCounters;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.frontend.FrontendGpuDevice;
import java.nio.ByteBuffer;
import java.util.function.Supplier;
import org.spongepowered.asm.mixin.Mixin;

/** Counts buffers created with initial data (an upload) through 26.3's frontend device. Count only. */
@Mixin(FrontendGpuDevice.class)
public abstract class FrontendGpuDeviceMixin {
    @WrapMethod(method = "createBuffer(Ljava/util/function/Supplier;ILjava/nio/ByteBuffer;)Lcom/mojang/renderpearl/api/buffers/GpuBuffer;")
    private GpuBuffer brimstone$createBuffer(Supplier<String> label, int usage, ByteBuffer data, Operation<GpuBuffer> original) {
        boolean outer = GpuCounters.enter();
        try {
            if (outer) GpuCounters.upload(GpuCounters.Upload.CREATE_BUFFER, data == null ? 0 : data.remaining());
            return original.call(label, usage, data);
        } finally {
            GpuCounters.exit();
        }
    }
}
