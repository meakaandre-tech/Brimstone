package com.meakaandre.brimstone.mixin.counters;

import com.meakaandre.brimstone.bench.GpuCounters;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.frontend.FrontendCommandEncoder;
import java.nio.ByteBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Counts buffer and texture writes through 26.3's frontend command encoder (every backend). Count only. */
@Mixin(FrontendCommandEncoder.class)
public abstract class FrontendCommandEncoderMixin {
    @Inject(method = "writeToBuffer", at = @At("HEAD"))
    private void brimstone$writeToBuffer(GpuBufferSlice slice, ByteBuffer data, CallbackInfo ci) {
        if (GpuCounters.enter()) GpuCounters.upload(GpuCounters.Upload.WRITE_BUFFER, data == null ? 0 : data.remaining());
    }

    @Inject(method = "writeToBuffer", at = @At("RETURN"))
    private void brimstone$writeToBufferEnd(GpuBufferSlice slice, ByteBuffer data, CallbackInfo ci) {
        GpuCounters.exit();
    }

    /** Both NativeImage variants end here; the region is width x height texels of the texture's format. */
    @Inject(method = "writeToTexture(Lcom/mojang/renderpearl/api/textures/GpuTexture;Ljava/nio/ByteBuffer;IIIIII)V", at = @At("HEAD"))
    private void brimstone$writeToTexture(GpuTexture texture, ByteBuffer data, int mip, int layer, int x, int y, int width, int height, CallbackInfo ci) {
        if (GpuCounters.enter()) GpuCounters.upload(GpuCounters.Upload.WRITE_TEXTURE, (long) width * height * texture.getFormat().blockSize());
    }

    @Inject(method = "writeToTexture(Lcom/mojang/renderpearl/api/textures/GpuTexture;Ljava/nio/ByteBuffer;IIIIII)V", at = @At("RETURN"))
    private void brimstone$writeToTextureEnd(GpuTexture texture, ByteBuffer data, int mip, int layer, int x, int y, int width, int height, CallbackInfo ci) {
        GpuCounters.exit();
    }
}
