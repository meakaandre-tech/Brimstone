package com.meakaandre.brimstone.mixin.terrain;

import com.meakaandre.brimstone.terrain.TranslucencyStats;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Times vanilla's translucent sorting (as a layer is built and each time it is sorted again) for the bench. Measures only. */
@Mixin(MeshData.SortState.class)
public abstract class SortStateMixin {
    @Inject(method = "buildSortedIndexBuffer", at = @At("HEAD"))
    private void brimstone$sortStart(CallbackInfoReturnable<ByteBufferBuilder.Result> cir) {
        TranslucencyStats.sortStart();
    }

    @Inject(method = "buildSortedIndexBuffer", at = @At("RETURN"))
    private void brimstone$sortEnd(CallbackInfoReturnable<ByteBufferBuilder.Result> cir) {
        TranslucencyStats.sortEnd();
    }
}
