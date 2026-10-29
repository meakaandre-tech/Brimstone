package com.meakaandre.ciserver.mixin;

import com.meakaandre.ciserver.Pins;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * CI only (P2-1), the random-tick run only ({@link Pins}): random ticks and precipitation draw on the chunk's own
 * pinned random, and only the sections at or above the run's MIN_Y tick. Inactive (vanilla's code path, unchanged)
 * everywhere else. In an optional mixin config: each hook counts what it did, and CI fails the random-tick run unless
 * every count is above zero, so a hook that stops applying can't pass silently.
 */
@Mixin(ServerLevel.class)
public abstract class PinnedRandomTickMixin {
    @Redirect(method = "tickChunk", at = @At(value = "INVOKE", target = "getBlockRandomPos(IIII)Lnet/minecraft/core/BlockPos;"))
    private BlockPos ciServer$pinnedPos(ServerLevel self, int x, int y, int z, int mask) {
        RandomSource r = Pins.on() ? Pins.CHUNK_RANDOM.get() : null;
        if (r == null) return self.getBlockRandomPos(x, y, z, mask);
        Pins.positions++;
        return Pins.randomPos(r, x, y, z, mask);
    }

    @Redirect(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;isRandomlyTicking()Z"))
    private boolean ciServer$pinnedSection(LevelChunkSection section) {
        boolean ticking = section.isRandomlyTicking();
        LevelChunk chunk = Pins.CHUNK.get();
        if (!ticking || !Pins.on() || chunk == null) return ticking;
        LevelChunkSection[] sections = chunk.getSections();
        for (int i = 0; i < sections.length; i++) {
            if (sections[i] != section) continue;
            boolean on = SectionPos.sectionToBlockCoord(chunk.getSectionYFromSectionIndex(i)) >= Pins.randomTickMinY;
            if (on) Pins.sectionsOn++;
            else Pins.sectionsOff++;
            return on;
        }
        return ticking;
    }

    @ModifyArg(method = "tickChunk", at = @At(value = "INVOKE",
        target = "randomTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"), index = 2)
    private RandomSource ciServer$pinnedTickRandom(RandomSource shared) {
        RandomSource r = Pins.on() ? Pins.CHUNK_RANDOM.get() : null;
        if (r == null) return shared;
        Pins.ticks++;
        return r;
    }

    @Redirect(method = "tickChunk", at = @At(value = "INVOKE", target = "tickPrecipitation(Lnet/minecraft/core/BlockPos;)V"))
    private void ciServer$pinnedPrecipitation(ServerLevel self, BlockPos pos) {
        if (!Pins.on()) {
            self.tickPrecipitation(pos);
            return;
        }
        if (self.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() >= Pins.randomTickMinY) {
            Pins.precipOn++;
            self.tickPrecipitation(pos);
        } else {
            Pins.precipOff++;
        }
    }
}
