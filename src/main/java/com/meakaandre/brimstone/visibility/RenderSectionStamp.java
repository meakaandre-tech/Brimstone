package com.meakaandre.brimstone.visibility;

/**
 * Added to vanilla's RenderSection by a mixin: the last frame (Brimstone's visibility frame counter) in which the
 * section was in vanilla's visible list, so the occlusion test can ask "does vanilla draw this section now" without a
 * hash set per frame.
 */
public interface RenderSectionStamp {
    int brimstone$visibleFrame();

    void brimstone$setVisibleFrame(int frame);
}
