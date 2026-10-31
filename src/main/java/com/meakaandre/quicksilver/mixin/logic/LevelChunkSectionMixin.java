package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.SectionMasks;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** P2-2, logic.block-collisions: a section's masks ({@link SectionMasks}). */
@Mixin(LevelChunkSection.class)
public abstract class LevelChunkSectionMixin implements SectionMasks.Holder {
    @Unique
    private SectionMasks.Mask quicksilver$masks;

    @Override
    public SectionMasks.Mask quicksilver$mask() {
        return quicksilver$masks;
    }

    @Override
    public void quicksilver$mask(SectionMasks.Mask m) {
        quicksilver$masks = m;
    }
}
