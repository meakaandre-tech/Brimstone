package com.meakaandre.brimstone.mixin.terrain;

import com.meakaandre.brimstone.terrain.TerrainStats;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Times vanilla's mesher (FRAPI included) for the bench. Measures only. */
@Mixin(SectionCompiler.class)
public abstract class SectionCompilerMixin {
    @Inject(method = "compile", at = @At("HEAD"))
    private void brimstone$compileStart(CallbackInfoReturnable<SectionCompiler.Results> cir) {
        TerrainStats.compileStart();
    }

    @Inject(method = "compile", at = @At("RETURN"))
    private void brimstone$compileEnd(CallbackInfoReturnable<SectionCompiler.Results> cir) {
        TerrainStats.compileEnd();
    }
}
