package com.meakaandre.brimstone.animation;

/**
 * Brimstone's view of a block-atlas sprite (mixed into {@code TextureAtlasSprite}): its slot in the animation table
 * (0: not an animated sprite of the current block atlas) and its UV rectangle, read without going through the sprite's
 * getters (which count as a use).
 */
public interface SpriteSlot {
    int brimstone$slot();

    void brimstone$setSlot(int slot);

    float brimstone$u0();

    float brimstone$u1();

    float brimstone$v0();

    float brimstone$v1();
}
