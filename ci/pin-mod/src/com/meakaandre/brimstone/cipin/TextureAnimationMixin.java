package com.meakaandre.brimstone.cipin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI only. Animated block textures (water, lava, fire, portals, sea grass...) advance one frame every few ticks from
 * when the texture was loaded, so two identical runs catch them on different frames and differ by chance (the
 * fly-through's water surface reached its strict pixel limit on noise alone). Every animated texture is held on its
 * first frame in every CI client run, vanilla and Brimstone alike: its first tick leaves the state as it is (frame 0,
 * still dirty, so the atlas gets frame 0 drawn once, as vanilla does), every later tick is skipped and the dirty flag
 * cleared, so nothing is redrawn into the atlas afterwards. Brimstone's own handling of animated textures (package 8)
 * needs its own check with animation running.
 */
@Mixin(targets = "net.minecraft.client.renderer.texture.SpriteContents$AnimationState")
public abstract class TextureAnimationMixin {
    @Shadow
    private boolean isDirty;

    @Unique
    private boolean brimstoneCi$firstFrameDrawn;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void brimstoneCi$holdFirstFrame(CallbackInfo ci) {
        if (brimstoneCi$firstFrameDrawn) isDirty = false;
        brimstoneCi$firstFrameDrawn = true;
        ci.cancel();
    }
}
