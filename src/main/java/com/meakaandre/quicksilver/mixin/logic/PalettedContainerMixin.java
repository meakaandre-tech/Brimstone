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
 * reading it from the network.
 */
@Mixin(PalettedContainer.class)
public abstract class PalettedContainerMixin implements SectionMasks.Writes {
    @Unique
    private int quicksilver$writeCount;

    @Override
    public int quicksilver$writes() {
        return quicksilver$writeCount;
    }

    @Inject(method = {"getAndSet", "getAndSetUnchecked"}, at = @At("HEAD"))
    private void quicksilver$swapped(CallbackInfoReturnable<Object> cir) {
        quicksilver$writeCount++;
    }

    @Inject(method = {"set", "read"}, at = @At("HEAD"))
    private void quicksilver$written(CallbackInfo ci) {
        quicksilver$writeCount++;
    }
}
