package com.meakaandre.brimstone.mixin.animation;

import com.meakaandre.brimstone.animation.StateSprite;
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
    private TextureAtlasSprite brimstone$sprite;

    @Override
    public TextureAtlasSprite brimstone$sprite() {
        return brimstone$sprite;
    }

    @Override
    public void brimstone$setSprite(TextureAtlasSprite sprite) {
        brimstone$sprite = sprite;
    }

    @Override
    public int brimstone$subFrame() {
        return subFrame;
    }

    @Override
    public void brimstone$setSubFrame(int value) {
        subFrame = value;
    }

    @Override
    public boolean brimstone$dirty() {
        return isDirty;
    }

    @Override
    public void brimstone$setDirty(boolean value) {
        isDirty = value;
    }
}
