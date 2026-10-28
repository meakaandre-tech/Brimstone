package com.meakaandre.quicksilver.animation;

import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.List;
import net.minecraft.client.renderer.texture.SpriteContents;

/** What Quicksilver reads of a texture atlas (mixed into {@code TextureAtlas}). */
public interface AtlasFrames {
    List<SpriteContents.AnimationState> quicksilver$animationStates();

    GpuTextureView[] quicksilver$mipViews();

    int quicksilver$maxMipLevel();

    int quicksilver$width();

    int quicksilver$height();
}
