package com.meakaandre.ciserver.mixin;

import com.meakaandre.ciserver.Harness;
import com.meakaandre.ciserver.SeedPin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI only, the GameTest server only. Each ticking chunk draws on the level's shared random (lightning, snow and
 * ice), and how many chunks tick there depends on how fast chunks loaded, so every later draw (a bone meal's growth in
 * the crop farm test) differed between runs. Here those draws come from a random seeded by the chunk's position and
 * the game time, so they no longer move the shared random. Vanilla's and Quicksilver's runs alike.
 */
@Mixin(ServerLevel.class)
public abstract class ChunkTickRandomMixin {
    private static final ThreadLocal<RandomSource> CHUNK = new ThreadLocal<>();

    @Inject(method = "tickChunk", at = @At("HEAD"))
    private void ciServer$chunkRandom(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        if (!Harness.gameTestPins) return;
        long t = ((ServerLevel) (Object) this).getGameTime();
        CHUNK.set(new LegacyRandomSource(SeedPin.mix(chunk.getPos().getMinBlockX() * 31L + chunk.getPos().getMinBlockZ() * 1_000_003L + t)));
    }

    @Inject(method = "tickChunk", at = @At("RETURN"))
    private void ciServer$chunkRandomDone(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        CHUNK.remove();
    }

    @Redirect(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"), require = 1)
    private int ciServer$pinnedNextInt(RandomSource shared, int bound) {
        RandomSource r = CHUNK.get();
        return (r != null ? r : shared).nextInt(bound);
    }
}
