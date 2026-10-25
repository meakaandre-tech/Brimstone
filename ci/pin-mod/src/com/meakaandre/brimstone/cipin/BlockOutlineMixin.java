package com.meakaandre.brimstone.cipin;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI only. The outline the game draws around the block under the crosshair moves by one pixel row from one frame to
 * the next in the old-GPU profile (GL 3.3), in vanilla's own runs as much as Brimstone's (run 40152673881: two of
 * eleven hologram shots in vanilla's old-GPU run, five in Brimstone's, the same frame-to-frame draws and the same
 * target block in every frame), so whichever frame a screenshot catches decides those pixels. It is not drawn in any
 * CI client run, vanilla and Brimstone alike.
 */
@Mixin(LevelRenderer.class)
public abstract class BlockOutlineMixin {
    @Inject(method = "submitBlockOutline", at = @At("HEAD"), cancellable = true)
    private void brimstoneCi$noBlockOutline(CallbackInfo ci) {
        ci.cancel();
    }
}
