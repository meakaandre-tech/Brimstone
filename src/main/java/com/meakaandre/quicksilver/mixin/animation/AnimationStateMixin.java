package com.meakaandre.quicksilver.mixin.animation;

import com.meakaandre.quicksilver.animation.StateSprite;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/** The sprite an animation state was made for, so the animation table can pair each state with its sprite. */
@Mixin(SpriteContents.AnimationState.class)
public abstract class AnimationStateMixin implements StateSprite {
    @Shadow
    private int subFrame;
    @Shadow
    private boolean isDirty;

    @Unique
    private TextureAtlasSprite quicksilver$sprite;

    @Override
    public TextureAtlasSprite quicksilver$sprite() {
        return quicksilver$sprite;
    }

    @Override
    public void quicksilver$setSprite(TextureAtlasSprite sprite) {
        quicksilver$sprite = sprite;
    }

    @Override
    public int quicksilver$subFrame() {
        return subFrame;
    }

    @Override
    public void quicksilver$setSubFrame(int value) {
        subFrame = value;
    }

    @Override
    public boolean quicksilver$dirty() {
        return isDirty;
    }

    @Override
    public void quicksilver$setDirty(boolean value) {
        isDirty = value;
    }
}
