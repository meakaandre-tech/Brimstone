package com.meakaandre.ciserver.mixin;

import com.meakaandre.ciserver.SeedPin;
import net.minecraft.world.level.levelgen.RandomSupport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** CI only: every "unique" seed comes from {@link SeedPin}, so unseeded random sources repeat between runs. */
@Mixin(RandomSupport.class)
public abstract class UniqueSeedMixin {
    @Inject(method = "generateUniqueSeed", at = @At("HEAD"), cancellable = true)
    private static void ciServer$pinnedSeed(CallbackInfoReturnable<Long> cir) {
        cir.setReturnValue(SeedPin.next());
    }
}
