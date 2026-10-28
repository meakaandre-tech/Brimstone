package com.meakaandre.quicksilver.mixin.counters;

import com.mojang.blaze3d.vertex.UberGpuBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** One layer's vertex and index buffers in vanilla's section dispatcher. Read only. */
@Mixin(targets = "net.minecraft.client.renderer.chunk.SectionRenderDispatcher$SectionUberBuffers")
public interface SectionUberBuffersAccessor {
    @Accessor("vertexBuffer")
    UberGpuBuffer<?> quicksilver$vertexBuffer();

    @Accessor("indexBuffer")
    UberGpuBuffer<?> quicksilver$indexBuffer();
}
