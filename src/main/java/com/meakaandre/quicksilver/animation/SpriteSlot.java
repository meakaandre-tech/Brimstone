package com.meakaandre.quicksilver.animation;

/**
 * Quicksilver's view of a block-atlas sprite (mixed into {@code TextureAtlasSprite}): its slot in the animation table
 * (0: not an animated sprite of the current block atlas) and its UV rectangle, read without going through the sprite's
 * getters (which count as a use).
 */
public interface SpriteSlot {
    int quicksilver$slot();

    void quicksilver$setSlot(int slot);

    float quicksilver$u0();

    float quicksilver$u1();

    float quicksilver$v0();

    float quicksilver$v1();
}
