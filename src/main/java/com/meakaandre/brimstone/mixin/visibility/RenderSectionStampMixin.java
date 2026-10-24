package com.meakaandre.brimstone.mixin.visibility;

import com.meakaandre.brimstone.visibility.RenderSectionStamp;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** One int per section: the last visibility frame in which vanilla had it in its visible list. */
@Mixin(SectionRenderDispatcher.RenderSection.class)
public abstract class RenderSectionStampMixin implements RenderSectionStamp {
    @Unique
    private int brimstone$visibleFrame = -1;

    @Override
    public int brimstone$visibleFrame() {
        return brimstone$visibleFrame;
    }

    @Override
    public void brimstone$setVisibleFrame(int frame) {
        brimstone$visibleFrame = frame;
    }
}
