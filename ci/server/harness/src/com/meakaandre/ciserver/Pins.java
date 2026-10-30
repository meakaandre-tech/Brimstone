package com.meakaandre.ciserver;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.LegacyRandomSource;

/**
 * CI only (P2-1): the random-tick pin of the random-tick run (scripted-run-core-random.txt, script line
 * {@code random-ticks MIN_Y}). In that run a chunk's random ticks draw on a random of their own, seeded by the chunk's
 * position and the game time, instead of the level's shared random (whose draws depend on every other loaded chunk,
 * and on light the light engine's thread may not have finished), and only the sections at or above MIN_Y tick
 * (the test scenes on the platform; the natural terrain below stays still). Precipitation (ice, snow, cauldrons) is
 * pinned the same way and only lands at or above MIN_Y. The same in vanilla's runs and Quicksilver's.
 *
 * <p>The hooks are in mixin/PinnedRandomTickMixin (its own optional mixin config): each counts what it did, and the
 * random-tick run fails in CI unless every hook ran ({@link #summary}).
 */
public final class Pins {
    /** Sections at or above this block y random-tick in the random-tick run; MIN_VALUE: the pin is off. */
    public static volatile int randomTickMinY = Integer.MIN_VALUE;

    /** The chunk being ticked and its own random (set at the head of ServerLevel#tickChunk). */
    public static final ThreadLocal<RandomSource> CHUNK_RANDOM = new ThreadLocal<>();
    public static final ThreadLocal<LevelChunk> CHUNK = new ThreadLocal<>();

    /** What each hook did over the run: random positions drawn, random ticks given the pinned random, sections let
     * through and held still, precipitation let through and held. */
    public static long positions, ticks, sectionsOn, sectionsOff, precipOn, precipOff;

    private Pins() {
    }

    public static boolean on() {
        return randomTickMinY != Integer.MIN_VALUE;
    }

    /** The chunk's random for this game time (a fixed mix of both): what every pinned draw in it uses. */
    public static RandomSource chunkRandom(LevelChunk chunk, long gameTime) {
        long x = chunk.getPos().getMinBlockX(), z = chunk.getPos().getMinBlockZ();
        return new LegacyRandomSource(SeedPin.mix(x * 31L + z * 1_000_003L + gameTime * 0x9E3779B97F4A7C15L));
    }

    /** As Level#getBlockRandomPos, from the chunk's own random. */
    public static BlockPos randomPos(RandomSource r, int x, int y, int z, int mask) {
        return new BlockPos(x + (r.nextInt(16) & mask), y + (r.nextInt(16) & mask), z + (r.nextInt(16) & mask));
    }

    public static String summary() {
        return "positions=" + positions + " ticks=" + ticks + " sectionsOn=" + sectionsOn + " sectionsOff=" + sectionsOff
            + " precipOn=" + precipOn + " precipOff=" + precipOff;
    }
}
