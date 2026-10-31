package com.meakaandre.quicksilver.mixin.logic;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.meakaandre.quicksilver.logic.SectionMasks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** P2-2, logic.block-collisions: a section's masks, kept up to date across its own block writes ({@link SectionMasks}). */
@Mixin(LevelChunkSection.class)
public abstract class LevelChunkSectionMixin implements SectionMasks.Holder {
    @Unique
    private SectionMasks.Mask quicksilver$masks;

    @Override
    public SectionMasks.Mask quicksilver$mask() {
        return quicksilver$masks;
    }

    @Override
    public void quicksilver$mask(SectionMasks.Mask m) {
        quicksilver$masks = m;
    }

    @Inject(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"))
    private void quicksilver$beforeSet(int x, int y, int z, BlockState state, boolean lock, CallbackInfoReturnable<BlockState> cir,
                                       @Share("current") LocalBooleanRef current) {
        current.set(quicksilver$masks != null && SectionMasks.beforeSet((LevelChunkSection) (Object) this));
    }

    @Inject(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("RETURN"))
    private void quicksilver$afterSet(int x, int y, int z, BlockState state, boolean lock, CallbackInfoReturnable<BlockState> cir,
                                      @Share("current") LocalBooleanRef current) {
        if (current.get()) SectionMasks.afterSet((LevelChunkSection) (Object) this, true, x, y, z, state);
    }
}
