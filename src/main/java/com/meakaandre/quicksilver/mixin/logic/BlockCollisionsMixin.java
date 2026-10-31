package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.SectionMasks;
import net.minecraft.core.Cursor3D;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** P2-2, logic.block-collisions: positions that can't be kept are dropped unread ({@link SectionMasks}). */
@Mixin(BlockCollisions.class)
public abstract class BlockCollisionsMixin implements SectionMasks.Memo {
    @Shadow
    @Final
    private CollisionGetter collisionGetter;
    @Unique
    private long quicksilver$key = Long.MIN_VALUE;
    @Unique
    private SectionMasks.Mask quicksilver$mask;

    @Override
    public long quicksilver$sectionKey() {
        return quicksilver$key;
    }

    @Override
    public SectionMasks.Mask quicksilver$sectionMask() {
        return quicksilver$mask;
    }

    @Override
    public void quicksilver$remember(long key, SectionMasks.Mask m) {
        quicksilver$key = key;
        quicksilver$mask = m;
    }

    @Redirect(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Cursor3D;getNextType()I"))
    private int quicksilver$skipEdges(Cursor3D cursor) {
        int type = cursor.getNextType();
        if (!SectionMasks.SWITCH.on) return type;
        try {
            return SectionMasks.type(this, this.collisionGetter, cursor.nextX(), cursor.nextY(), cursor.nextZ(), type);
        } catch (Throwable t) {
            SectionMasks.SWITCH.fail(t);
            return type;
        }
    }
}
