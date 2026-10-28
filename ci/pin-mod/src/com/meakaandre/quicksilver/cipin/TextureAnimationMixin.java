package com.meakaandre.quicksilver.cipin;

import com.meakaandre.quicksilver.cipinstate.Pins;
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
 * first frame in every CI client run, vanilla and Quicksilver alike: its first tick leaves the state as it is (frame 0,
 * still dirty, so the atlas gets frame 0 drawn once, as vanilla does), every later tick is skipped and the dirty flag
 * cleared, so nothing is redrawn into the atlas afterwards.
 *
 * <p>Package 8 (Quicksilver's animated textures only when seen) needs animation running: in the animation scene's area
 * ({@link Pins}) every animated texture goes back to frame 0 on entering (and on leaving), then runs for a fixed number
 * of ticks (vanilla's own tick) and is held again, so every run reaches the same frame; while the scene's bench step
 * sets its marker block, it runs freely.
 */
@Mixin(targets = "net.minecraft.client.renderer.texture.SpriteContents$AnimationState")
public abstract class TextureAnimationMixin {
    @Shadow
    private boolean isDirty;
    @Shadow
    private int frame;
    @Shadow
    private int subFrame;

    @Unique
    private boolean quicksilverCi$firstFrameDrawn;
    @Unique
    private int quicksilverCi$resets;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void quicksilverCi$holdFirstFrame(CallbackInfo ci) {
        int resets = Pins.animationResets;
        if (resets != quicksilverCi$resets) {
            // back to the first frame, drawn into the atlas once more (vanilla redraws a dirty state)
            quicksilverCi$resets = resets;
            frame = 0;
            subFrame = 0;
            isDirty = true;
            quicksilverCi$firstFrameDrawn = true;
            ci.cancel();
            return;
        }
        if (Pins.animationRuns) return;
        if (quicksilverCi$firstFrameDrawn) isDirty = false;
        quicksilverCi$firstFrameDrawn = true;
        ci.cancel();
    }
}
