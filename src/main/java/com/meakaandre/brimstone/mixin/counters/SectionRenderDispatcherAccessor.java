package com.meakaandre.brimstone.mixin.counters;

import java.util.Map;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Vanilla's terrain buffers per layer (for the bench's terrain GPU memory). Read only. */
@Mixin(SectionRenderDispatcher.class)
public interface SectionRenderDispatcherAccessor {
    /** Values are {@code SectionRenderDispatcher.SectionUberBuffers}, read through {@link SectionUberBuffersAccessor}. */
    @Accessor("chunkUberBuffers")
    Map<?, ?> brimstone$chunkUberBuffers();
}
