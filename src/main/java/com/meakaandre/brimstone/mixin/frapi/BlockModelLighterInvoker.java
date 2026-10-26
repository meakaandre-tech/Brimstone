package com.meakaandre.brimstone.mixin.frapi;

import net.minecraft.client.renderer.block.BlockModelLighter;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.level.CardinalLighting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Vanilla's own per-face brightness for smooth-lit quads, for Brimstone's full-face smooth lighting (package 4). */
@Mixin(BlockModelLighter.class)
public interface BlockModelLighterInvoker {
    @Invoker("getDirectionalBrightness")
    static float brimstone$getDirectionalBrightness(CardinalLighting lighting, BakedQuad quad, Direction direction) {
        throw new AssertionError();
    }
}
