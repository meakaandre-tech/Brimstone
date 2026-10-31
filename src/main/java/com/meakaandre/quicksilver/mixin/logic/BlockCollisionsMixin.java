package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.SectionMasks;
import net.minecraft.core.Cursor3D;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * P2-2, logic.block-collisions: positions that can't be kept are dropped unread ({@link SectionMasks}). For every
 * position vanilla would look at (not a corner), the sweep's own getChunk is called first, as vanilla calls it right
 * after asking the type; for a position kept, vanilla's own call then finds the same column in the sweep's one-column
 * cache, so the level sees exactly vanilla's chunk lookups.
 */
@Mixin(BlockCollisions.class)
public abstract class BlockCollisionsMixin implements SectionMasks.Memo {
    @Shadow
    @Final
    private CollisionGetter collisionGetter;
    @Unique
    private long quicksilver$key = Long.MIN_VALUE;
    @Unique
    private LevelChunkSection quicksilver$sectionNow;

    @Shadow
    protected abstract BlockGetter getChunk(int x, int z);

    @Override
    public long quicksilver$sectionKey() {
        return quicksilver$key;
    }

    @Override
    public LevelChunkSection quicksilver$section() {
        return quicksilver$sectionNow;
    }

    @Override
    public void quicksilver$remember(long key, LevelChunkSection section) {
        quicksilver$key = key;
        quicksilver$sectionNow = section;
    }

    @Redirect(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Cursor3D;getNextType()I"))
    private int quicksilver$skipEdges(Cursor3D cursor) {
        int type = cursor.getNextType();
        // a corner: vanilla drops it unread and calls nothing
        if (type == 3 || !SectionMasks.SWITCH.on) return type;
        if (!(this.collisionGetter instanceof ServerLevel level) || level.isDebug()) return type;
        try {
            int x = cursor.nextX(), z = cursor.nextZ();
            return SectionMasks.type(this, level, this.getChunk(x, z), x, cursor.nextY(), z, type);
        } catch (Throwable t) {
            SectionMasks.SWITCH.fail(t);
            return type;
        }
    }
}
