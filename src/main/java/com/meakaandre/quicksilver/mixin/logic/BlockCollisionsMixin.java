package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.EdgeSkip;
import net.minecraft.core.Cursor3D;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** P2-2, logic.block-collisions: shell positions in sections with no oversized state are skipped unread ({@link EdgeSkip}). */
@Mixin(BlockCollisions.class)
public abstract class BlockCollisionsMixin implements EdgeSkip.Memo {
    @Shadow
    @Final
    private CollisionGetter collisionGetter;
    @Unique
    private long quicksilver$key = Long.MIN_VALUE;
    @Unique
    private boolean quicksilver$plain;

    @Override
    public long quicksilver$sectionKey() {
        return quicksilver$key;
    }

    @Override
    public boolean quicksilver$sectionPlain() {
        return quicksilver$plain;
    }

    @Override
    public void quicksilver$remember(long key, boolean plain) {
        quicksilver$key = key;
        quicksilver$plain = plain;
    }

    @Redirect(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Cursor3D;getNextType()I"))
    private int quicksilver$skipEdges(Cursor3D cursor) {
        int type = cursor.getNextType();
        // 0: inside the box, always read; 3: a corner, which vanilla drops unread anyway
        if ((type == 1 || type == 2) && EdgeSkip.SWITCH.on) {
            try {
                if (EdgeSkip.skip(this, this.collisionGetter, cursor.nextX(), cursor.nextY(), cursor.nextZ())) return 3;
            } catch (Throwable t) {
                EdgeSkip.SWITCH.fail(t);
            }
        }
        return type;
    }
}
