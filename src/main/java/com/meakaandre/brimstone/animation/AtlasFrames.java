package com.meakaandre.brimstone.animation;

import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.List;
import net.minecraft.client.renderer.texture.SpriteContents;

/** What Brimstone reads of a texture atlas (mixed into {@code TextureAtlas}). */
public interface AtlasFrames {
    List<SpriteContents.AnimationState> brimstone$animationStates();

    GpuTextureView[] brimstone$mipViews();

    int brimstone$maxMipLevel();

    int brimstone$width();

    int brimstone$height();
}
