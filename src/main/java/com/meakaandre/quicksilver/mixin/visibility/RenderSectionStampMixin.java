package com.meakaandre.quicksilver.mixin.visibility;

import com.meakaandre.quicksilver.visibility.RenderSectionStamp;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** One int per section: the last visibility frame in which vanilla had it in its visible list. */
@Mixin(SectionRenderDispatcher.RenderSection.class)
public abstract class RenderSectionStampMixin implements RenderSectionStamp {
    @Unique
    private int quicksilver$visibleFrame = -1;

    @Override
    public int quicksilver$visibleFrame() {
        return quicksilver$visibleFrame;
    }

    @Override
    public void quicksilver$setVisibleFrame(int frame) {
        quicksilver$visibleFrame = frame;
    }
}
