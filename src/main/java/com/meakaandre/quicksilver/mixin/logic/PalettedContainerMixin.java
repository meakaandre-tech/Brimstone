package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.SectionMasks;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(method = "getAndSet", at = @At("HEAD"))
    private void quicksilver$getAndSet(CallbackInfoReturnable<Object> cir) {
        quicksilver$writeCount++;
    }

    @Inject(method = "getAndSetUnchecked", at = @At("HEAD"))
    private void quicksilver$getAndSetUnchecked(CallbackInfoReturnable<Object> cir) {
        quicksilver$writeCount++;
    }

    @Inject(method = "set", at = @At("HEAD"))
    private void quicksilver$set(CallbackInfo ci) {
        quicksilver$writeCount++;
    }

    @Inject(method = "read", at = @At("HEAD"))
    private void quicksilver$read(CallbackInfo ci) {
        quicksilver$writeCount++;
    }
}
