package com.meakaandre.brimstone.mixin.counters;

import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Package 8: how many particles vanilla extracted for drawing (bench only). */
@Mixin(QuadParticleRenderState.class)
public interface QuadParticleCountAccessor {
    @Accessor("particleCount")
    int brimstone$particleCount();
}
