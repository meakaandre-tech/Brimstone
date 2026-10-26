package com.meakaandre.brimstone.mixin.animation;

import com.meakaandre.brimstone.animation.Animation;
import com.meakaandre.brimstone.animation.AtlasFrames;
import com.meakaandre.brimstone.bench.GpuCounters;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.List;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Where the block atlas takes new contents (the animation table is built from its animated sprites) and where an atlas
 * redraws its changed animated sprites on each client tick (Brimstone's animation module does it instead for the
 * block atlas while it is on, redrawing only what is seen).
 */
@Mixin(TextureAtlas.class)
public abstract class TextureAtlasMixin implements AtlasFrames {
    @Shadow
    private List<SpriteContents.AnimationState> animatedTexturesStates;
    @Shadow
    private GpuTextureView[] mipViews;
    @Shadow
    private int maxMipLevel;
    @Shadow
    private int width;
    @Shadow
    private int height;

    @Inject(method = "upload", at = @At("RETURN"))
    private void brimstone$uploaded(CallbackInfo ci) {
        Animation.atlasUploaded((TextureAtlas) (Object) this);
    }

    @Inject(method = "uploadAnimationFrames", at = @At("HEAD"), cancellable = true)
    private void brimstone$uploadFrames(CallbackInfo ci) {
        if (Animation.uploadFrames((TextureAtlas) (Object) this)) {
            // the bench's animation phase (set at this method's head by the counters) ends here, as vanilla's body is skipped
            GpuCounters.phase(GpuCounters.Phase.OTHER);
            ci.cancel();
        }
    }

    @Override
    public List<SpriteContents.AnimationState> brimstone$animationStates() {
        return animatedTexturesStates;
    }

    @Override
    public GpuTextureView[] brimstone$mipViews() {
        return mipViews;
    }

    @Override
    public int brimstone$maxMipLevel() {
        return maxMipLevel;
    }

    @Override
    public int brimstone$width() {
        return width;
    }

    @Override
    public int brimstone$height() {
        return height;
    }
}
