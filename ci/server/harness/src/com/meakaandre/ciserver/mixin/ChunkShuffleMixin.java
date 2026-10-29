package com.meakaandre.ciserver.mixin;

import com.meakaandre.ciserver.Harness;
import com.meakaandre.ciserver.SeedPin;
import java.util.List;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.server.level.ServerLevel;

/**
 * CI only, the GameTest server only. Each tick the server shuffles its list of ticking chunks with the level's shared
 * random, drawing one number per chunk, and how many chunks are loaded in the GameTest world depends on how fast they
 * loaded: every later draw on that random (a bone meal's growth) then differed between runs. Here the shuffle uses its
 * own random, seeded by the game time. Vanilla's and Quicksilver's runs alike.
 */
@Mixin(ServerChunkCache.class)
public abstract class ChunkShuffleMixin {
    @Shadow
    @Final
    ServerLevel level;

    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;shuffle(Ljava/util/List;Lnet/minecraft/util/RandomSource;)V"), require = 1)
    private void ciServer$ownShuffle(List<?> list, RandomSource shared) {
        RandomSource r = Harness.gameTestPins ? new LegacyRandomSource(SeedPin.mix(level.getGameTime())) : shared;
        Util.shuffle(list, r);
    }
}
