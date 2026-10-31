package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.SectionMasks;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * P2-2, logic.block-collisions: every write to a container counts up its write number, so masks built from it before
 * are built again ({@link SectionMasks}). Every method that changes a container's contents: its three setters and
 * reading it from the network, each with a handler of its own, so the hook check sees each one land.
 */
@Mixin(PalettedContainer.class)
public abstract class PalettedContainerMixin implements SectionMasks.Writes {
    @Unique
    private int quicksilver$writeCount;

    @Override
    public int quicksilver$writes() {
        return quicksilver$writeCount;
    }

    // (the setters' first int argument passed through unchanged: no callback object per write)
    @ModifyVariable(method = "getAndSet", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int quicksilver$getAndSet(int first) {
        quicksilver$writeCount++;
        return first;
    }

    @ModifyVariable(method = "getAndSetUnchecked", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int quicksilver$getAndSetUnchecked(int first) {
        quicksilver$writeCount++;
        return first;
    }

    @ModifyVariable(method = "set", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int quicksilver$set(int first) {
        quicksilver$writeCount++;
        return first;
    }

    @Inject(method = "read", at = @At("HEAD"))
    private void quicksilver$read(CallbackInfo ci) {
        quicksilver$writeCount++;
    }
}
