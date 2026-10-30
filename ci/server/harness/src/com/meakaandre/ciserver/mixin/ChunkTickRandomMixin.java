package com.meakaandre.ciserver.mixin;

import com.meakaandre.ciserver.Harness;
import com.meakaandre.ciserver.Pins;
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
 * CI only, the GameTest server and the random-tick run (P2-1). Each ticking chunk draws on the level's shared random
 * (lightning, snow and ice), and how many chunks tick there depends on how fast chunks loaded, so every later draw (a
 * bone meal's growth in the crop farm test) differed between runs. Here those draws come from a random seeded by the
 * chunk's position and the game time, so they no longer move the shared random. In the random-tick run the same
 * random also feeds the random ticks themselves (mixin/PinnedRandomTickMixin, {@link Pins}). Vanilla's and
 * Quicksilver's runs alike.
 */
@Mixin(ServerLevel.class)
public abstract class ChunkTickRandomMixin {
    @Inject(method = "tickChunk", at = @At("HEAD"))
    private void ciServer$chunkRandom(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        long t = ((ServerLevel) (Object) this).getGameTime();
        if (Pins.on()) {
            Pins.CHUNK.set(chunk);
            Pins.CHUNK_RANDOM.set(Pins.chunkRandom(chunk, t));
            return;
        }
        if (!Harness.gameTestPins) return;
        Pins.CHUNK_RANDOM.set(new LegacyRandomSource(SeedPin.mix(chunk.getPos().getMinBlockX() * 31L + chunk.getPos().getMinBlockZ() * 1_000_003L + t)));
    }

    @Inject(method = "tickChunk", at = @At("RETURN"))
    private void ciServer$chunkRandomDone(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        Pins.CHUNK_RANDOM.remove();
        Pins.CHUNK.remove();
    }

    @Redirect(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"), require = 1)
    private int ciServer$pinnedNextInt(RandomSource shared, int bound) {
        RandomSource r = Pins.CHUNK_RANDOM.get();
        return (r != null ? r : shared).nextInt(bound);
    }
}
