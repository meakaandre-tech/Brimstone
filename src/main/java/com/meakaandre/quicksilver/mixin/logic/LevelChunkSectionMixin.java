package com.meakaandre.quicksilver.mixin.logic;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.meakaandre.quicksilver.logic.SectionMasks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * P2-2, logic.block-collisions: a section's masks, kept up to date across its own block writes ({@link SectionMasks}).
 * The hooks pass a value through unchanged, so a write allocates no callback object; whether the masks were current
 * before the write is kept in a field (a section is written by one thread at a time).
 */
@Mixin(LevelChunkSection.class)
public abstract class LevelChunkSectionMixin implements SectionMasks.Holder {
    @Unique
    private SectionMasks.Mask quicksilver$masks;
    @Unique
    private boolean quicksilver$currentBefore;

    @Override
    public SectionMasks.Mask quicksilver$mask() {
        return quicksilver$masks;
    }

    @Override
    public void quicksilver$mask(SectionMasks.Mask m) {
        quicksilver$masks = m;
    }

    @ModifyVariable(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;",
        at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int quicksilver$beforeSet(int x) {
        boolean current = false;
        if (quicksilver$masks != null && SectionMasks.SWITCH.on) {
            try {
                current = SectionMasks.beforeSet((LevelChunkSection) (Object) this);
            } catch (Throwable t) {
                SectionMasks.SWITCH.fail(t);
            }
        }
        quicksilver$currentBefore = current;
        return x;
    }

    @ModifyReturnValue(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;",
        at = @At("RETURN"))
    private BlockState quicksilver$afterSet(BlockState previous, @Local(argsOnly = true, ordinal = 0) int x, @Local(argsOnly = true, ordinal = 1) int y,
                                            @Local(argsOnly = true, ordinal = 2) int z, @Local(argsOnly = true) BlockState state) {
        if (quicksilver$currentBefore && SectionMasks.SWITCH.on) {
            quicksilver$currentBefore = false;
            try {
                SectionMasks.afterSet((LevelChunkSection) (Object) this, true, x, y, z, state);
            } catch (Throwable t) {
                SectionMasks.SWITCH.fail(t);
            }
        }
        return previous;
    }
}
