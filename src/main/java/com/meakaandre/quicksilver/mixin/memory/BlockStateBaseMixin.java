package com.meakaandre.quicksilver.mixin.memory;

import com.meakaandre.quicksilver.memory.ShapeDedup;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** P2-1, memory.shape-dedup: a state's shapes are shared with equal ones once it has filled its cache ({@link ShapeDedup}). */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    @Inject(method = "initCache", at = @At("RETURN"))
    private void quicksilver$shareShapes(CallbackInfo ci) {
        ShapeDedup.afterInitCache((BlockBehaviour.BlockStateBase) (Object) this);
    }
}
