package com.meakaandre.quicksilver.mixin.frapi;

import net.minecraft.client.renderer.block.BlockModelLighter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Vanilla's per-thread block light cache, for Quicksilver's block lighting (package 4, as Indigo uses it). */
@Mixin(BlockModelLighter.class)
public interface BlockModelLighterAccessor {
    @Accessor("CACHE")
    static ThreadLocal<BlockModelLighter.Cache> quicksilver$getCache() {
        throw new AssertionError();
    }
}
