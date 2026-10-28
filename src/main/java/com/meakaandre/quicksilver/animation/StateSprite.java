package com.meakaandre.quicksilver.animation;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/** The sprite an animation state belongs to, and its tick within the current frame (mixed into {@code SpriteContents.AnimationState}). */
public interface StateSprite {
    TextureAtlasSprite quicksilver$sprite();

    void quicksilver$setSprite(TextureAtlasSprite sprite);

    /** Ticks into the current frame (vanilla's {@code subFrame}). */
    int quicksilver$subFrame();

    void quicksilver$setSubFrame(int subFrame);

    /** Vanilla's "frame changed, redraw" flag. */
    boolean quicksilver$dirty();

    void quicksilver$setDirty(boolean dirty);
}
