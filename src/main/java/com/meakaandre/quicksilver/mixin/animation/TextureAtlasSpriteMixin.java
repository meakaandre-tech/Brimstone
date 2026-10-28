package com.meakaandre.quicksilver.mixin.animation;

import com.meakaandre.quicksilver.animation.Animation;
import com.meakaandre.quicksilver.animation.SpriteSlot;
import com.meakaandre.quicksilver.animation.StateSprite;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Each sprite knows its slot in the animation table (0 for every sprite that is not an animated sprite of the current
 * block atlas), and reading its UVs or atlas position counts as a use of it ({@link Animation#spriteUsed}; a field
 * check and nothing more for every other sprite).
 */
@Mixin(TextureAtlasSprite.class)
public abstract class TextureAtlasSpriteMixin implements SpriteSlot {
    @Shadow
    @Final
    private float u0;
    @Shadow
    @Final
    private float u1;
    @Shadow
    @Final
    private float v0;
    @Shadow
    @Final
    private float v1;

    @Unique
    private volatile int quicksilver$slot;

    @Inject(method = "createAnimationState", at = @At("RETURN"))
    private void quicksilver$stateCreated(CallbackInfoReturnable<SpriteContents.AnimationState> cir) {
        SpriteContents.AnimationState s = cir.getReturnValue();
        if (s != null) ((StateSprite) s).quicksilver$setSprite((TextureAtlasSprite) (Object) this);
    }

    @Inject(method = {"getU0()F", "getU1()F", "getV0()F", "getV1()F", "getU(F)F", "getV(F)F", "getX()I", "getY()I"}, at = @At("HEAD"))
    private void quicksilver$used(CallbackInfoReturnable<?> cir) {
        int slot = quicksilver$slot;
        if (slot != 0) Animation.spriteUsed((TextureAtlasSprite) (Object) this, slot);
    }

    @Override
    public int quicksilver$slot() {
        return quicksilver$slot;
    }

    @Override
    public void quicksilver$setSlot(int slot) {
        quicksilver$slot = slot;
    }

    @Override
    public float quicksilver$u0() {
        return u0;
    }

    @Override
    public float quicksilver$u1() {
        return u1;
    }

    @Override
    public float quicksilver$v0() {
        return v0;
    }

    @Override
    public float quicksilver$v1() {
        return v1;
    }
}
