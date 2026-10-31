package com.meakaandre.quicksilver.logic;

import java.util.function.Predicate;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

/**
 * P2-2, logic.block-collisions: vanilla's block collision sweep (BlockCollisions) walks a box one block wider than the
 * colliding box on every side, so oversized shapes (fences, walls, a moving piston) reaching in from outside are found.
 * Each position in that outer shell is read and then dropped unless its state has a large collision shape (one face of
 * the shell) or is a moving piston (an edge); corners are dropped unread. When a chunk section's palette holds no state
 * that is either (every state in a section is in its palette), no shell position in that section can be kept, so it is
 * reported as a corner and the loop moves on without reading it: the same shapes come out, in the same order.
 *
 * <p>Only for a server level, and only when the position's chunk is loaded and inside the world's height; anything else
 * is left to vanilla. The answer is kept per sweep and section (a sweep runs within one call; nothing changes blocks
 * while it runs). No reflection and no lambdas (nothing here asks the JVM for an identity hash).
 */
public final class EdgeSkip {
    public static final String ID = "block-collisions";
    public static final Logic.Switch SWITCH = new Logic.Switch(ID);

    /** States a shell position can be kept for (the two tests vanilla's sweep makes there). */
    private static final Predicate<BlockState> OVERSIZED = new Oversized();

    private EdgeSkip() {
    }

    private static final class Oversized implements Predicate<BlockState> {
        @Override
        public boolean test(BlockState s) {
            return s.hasLargeCollisionShape() || s.is(Blocks.MOVING_PISTON);
        }
    }

    /** Per-sweep memory: the last section asked about and its answer (fields added to BlockCollisions). */
    public interface Memo {
        long quicksilver$sectionKey();

        boolean quicksilver$sectionPlain();

        void quicksilver$remember(long key, boolean plain);
    }

    /** Whether the shell position (type 1 or 2) can be skipped: its section holds no state the sweep could keep there. */
    public static boolean skip(Memo memo, CollisionGetter getter, int x, int y, int z) {
        if (!(getter instanceof ServerLevel level)) return false;
        int cx = SectionPos.blockToSectionCoord(x), cz = SectionPos.blockToSectionCoord(z);
        long key = SectionPos.asLong(cx, SectionPos.blockToSectionCoord(y), cz);
        if (key == memo.quicksilver$sectionKey()) return memo.quicksilver$sectionPlain();
        boolean plain = false;
        LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
        if (chunk != null) {
            int i = chunk.getSectionIndex(y);
            LevelChunkSection[] sections = chunk.getSections();
            if (i >= 0 && i < sections.length && sections[i] != null) plain = !sections[i].maybeHas(OVERSIZED);
        }
        memo.quicksilver$remember(key, plain);
        return plain;
    }
}
