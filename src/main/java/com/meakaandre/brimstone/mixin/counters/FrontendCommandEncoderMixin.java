package com.meakaandre.brimstone.mixin.counters;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.meakaandre.brimstone.bench.GpuCounters;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.frontend.FrontendCommandEncoder;
import java.nio.ByteBuffer;
import org.spongepowered.asm.mixin.Mixin;

/** Counts buffer and texture writes through 26.3's frontend command encoder (every backend). Count only. */
@Mixin(FrontendCommandEncoder.class)
public abstract class FrontendCommandEncoderMixin {
    @WrapMethod(method = "writeToBuffer")
    private void brimstone$writeToBuffer(GpuBufferSlice slice, ByteBuffer data, Operation<Void> original) {
        boolean outer = GpuCounters.enter();
        try {
            if (outer) GpuCounters.upload(GpuCounters.Upload.WRITE_BUFFER, data == null ? 0 : data.remaining());
            original.call(slice, data);
        } finally {
            GpuCounters.exit();
        }
    }

    /** Both NativeImage variants end here; the region is width x height texels of the texture's format. */
    @WrapMethod(method = "writeToTexture(Lcom/mojang/renderpearl/api/textures/GpuTexture;Ljava/nio/ByteBuffer;IIIIII)V")
    private void brimstone$writeToTexture(GpuTexture texture, ByteBuffer data, int mip, int layer, int x, int y, int width, int height,
                                          Operation<Void> original) {
        boolean outer = GpuCounters.enter();
        try {
            if (outer) GpuCounters.upload(GpuCounters.Upload.WRITE_TEXTURE, (long) width * height * texture.getFormat().blockSize());
            original.call(texture, data, mip, layer, x, y, width, height);
        } finally {
            GpuCounters.exit();
        }
    }
}
