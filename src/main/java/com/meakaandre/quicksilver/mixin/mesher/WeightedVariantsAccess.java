package com.meakaandre.quicksilver.mixin.mesher;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.WeightedVariants;
import net.minecraft.util.random.WeightedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Package 4c: the variants of vanilla's weighted block model (to tell a block whose every quad has a cull face). */
@Mixin(WeightedVariants.class)
public interface WeightedVariantsAccess {
    @Accessor("list")
    WeightedList<BlockStateModel> quicksilver$list();
}
