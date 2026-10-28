package com.meakaandre.quicksilver.mixin.counters;

import com.mojang.blaze3d.vertex.TlsfAllocator;
import com.mojang.blaze3d.vertex.UberGpuBuffer;
import java.util.List;
import java.util.Map;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Vanilla's heaps in one of its terrain buffers: how many, their size, and what is allocated in them. Read only. */
@Mixin(UberGpuBuffer.class)
public interface UberGpuBufferAccessor {
    @Accessor("nodes")
    List<?> quicksilver$nodes();

    @Accessor("heapSize")
    int quicksilver$heapSize();

    @Accessor("allocationMap")
    Map<?, TlsfAllocator.Allocation> quicksilver$allocationMap();
}
