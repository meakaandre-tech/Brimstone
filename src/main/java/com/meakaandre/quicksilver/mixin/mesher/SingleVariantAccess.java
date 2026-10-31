package com.meakaandre.quicksilver.mixin.mesher;

import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Package 4c: the one part of vanilla's single-variant block model (to tell a block whose every quad has a cull face). */
@Mixin(SingleVariant.class)
public interface SingleVariantAccess {
    @Accessor("model")
    BlockStateModelPart quicksilver$part();
}
