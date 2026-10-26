package com.meakaandre.brimstone.animation;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/** The sprite an animation state belongs to, and its tick within the current frame (mixed into {@code SpriteContents.AnimationState}). */
public interface StateSprite {
    TextureAtlasSprite brimstone$sprite();

    void brimstone$setSprite(TextureAtlasSprite sprite);

    /** Ticks into the current frame (vanilla's {@code subFrame}). */
    int brimstone$subFrame();

    void brimstone$setSubFrame(int subFrame);

    /** Vanilla's "frame changed, redraw" flag. */
    boolean brimstone$dirty();

    void brimstone$setDirty(boolean dirty);
}
