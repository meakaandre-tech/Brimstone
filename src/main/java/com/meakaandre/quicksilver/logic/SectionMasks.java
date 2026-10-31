package com.meakaandre.quicksilver.logic;

import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;

/**
 * P2-2, logic.block-collisions. Vanilla's block collision sweep (BlockCollisions: every entity move, the supporting-block
 * check every tick, the in-wall check, space-empty tests) walks a box one block wider than the colliding box on every
 * side and reads the block at each position. It keeps a position only when (a) inside the box: the block's collision
 * shape is not empty and touches the box; (b) on a face of the outer shell: also the block has a large collision shape;
 * (c) on an edge of the shell: also the block is a moving piston; corners it drops unread.
 *
 * <p>Each chunk section here gets two masks of its 4096 positions, built from its block states when a sweep first needs
 * them: which positions hold anything but air, and which hold a state with a large collision shape or a moving piston.
 * A sweep then drops, unread, every position that holds air (air's collision shape is always empty) and every shell
 * position that holds neither kind of state. Those are exactly positions vanilla reads and then drops, so the same
 * shapes come out in the same order.
 *
 * <p>The masks are exact for the states the section's block container holds when they are used: every write to a
 * container (each of its write methods) counts up a number kept on it, and masks built at another count, or for
 * another container, are built again. Only a server level's loaded chunks inside the world's height are used; anything
 * else is left to vanilla. A sweep runs within one call and nothing changes blocks while it runs, so it keeps the masks
 * it looked up per section.
 */
public final class SectionMasks {
    public static final String ID = "block-collisions";
    public static final Logic.Switch SWITCH = new Logic.Switch(ID);

    /** A section that holds only air: no position can be kept. */
    static final Mask ALL_AIR = new Mask(new long[64], new long[64], null, 0);

    private SectionMasks() {
    }

    /** The masks of one section, valid for one container at one write count. */
    public record Mask(long[] solid, long[] big, PalettedContainer<BlockState> container, int writes) {
        boolean solid(int i) {
            return (solid[i >>> 6] & (1L << i)) != 0;
        }

        boolean big(int i) {
            return (big[i >>> 6] & (1L << i)) != 0;
        }
    }

    /** Counts the writes to a block container (PalettedContainer, mixin). */
    public interface Writes {
        int quicksilver$writes();
    }

    /** The masks kept on a section (LevelChunkSection, mixin). */
    public interface Holder {
        Mask quicksilver$mask();

        void quicksilver$mask(Mask m);
    }

    /** Per sweep: the last section asked about and its masks (BlockCollisions, mixin). */
    public interface Memo {
        long quicksilver$sectionKey();

        Mask quicksilver$sectionMask();

        void quicksilver$remember(long key, Mask m);
    }

    /**
     * The type the sweep should see for this position: vanilla's own (0 inside, 1 face, 2 edge, 3 corner), or 3 when
     * the position can't be kept, so the sweep moves on without reading it.
     */
    public static int type(Memo memo, CollisionGetter getter, int x, int y, int z, int type) {
        // a debug world's chunks answer reads from the debug generator, not their sections
        if (type == 3 || !(getter instanceof ServerLevel level) || level.isDebug()) return type;
        int cx = SectionPos.blockToSectionCoord(x), cz = SectionPos.blockToSectionCoord(z);
        long key = SectionPos.asLong(cx, SectionPos.blockToSectionCoord(y), cz);
        Mask m;
        if (key == memo.quicksilver$sectionKey()) {
            m = memo.quicksilver$sectionMask();
        } else {
            m = null;
            LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
            if (chunk != null) {
                int i = chunk.getSectionIndex(y);
                LevelChunkSection[] sections = chunk.getSections();
                if (i >= 0 && i < sections.length && sections[i] != null) m = mask(sections[i]);
            }
            memo.quicksilver$remember(key, m);
        }
        if (m == null) return type;
        int i = (y & 15) << 8 | (z & 15) << 4 | (x & 15);
        if (!m.solid(i)) return 3;
        if (type != 0 && !m.big(i)) return 3;
        return type;
    }

    /** The section's masks, built again if its container changed since. */
    static Mask mask(LevelChunkSection section) {
        if (section.hasOnlyAir()) return ALL_AIR;
        PalettedContainer<BlockState> states = section.getStates();
        int writes = ((Writes) states).quicksilver$writes();
        Holder h = (Holder) section;
        Mask m = h.quicksilver$mask();
        if (m != null && m.container() == states && m.writes() == writes) return m;
        long[] solid = new long[64], big = new long[64];
        for (int y = 0; y < 16; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    BlockState st = states.get(x, y, z);
                    int i = y << 8 | z << 4 | x;
                    if (!st.isAir()) solid[i >>> 6] |= 1L << i;
                    if (oversized(st)) big[i >>> 6] |= 1L << i;
                }
            }
        }
        m = new Mask(solid, big, states, writes);
        h.quicksilver$mask(m);
        return m;
    }

    private static boolean oversized(BlockState s) {
        return s.hasLargeCollisionShape() || s.is(Blocks.MOVING_PISTON);
    }

    /**
     * Before a section's setBlockState: whether its masks are up to date (they are kept up to date across the write).
     */
    public static boolean beforeSet(LevelChunkSection section) {
        Mask m = ((Holder) section).quicksilver$mask();
        PalettedContainer<BlockState> states = section.getStates();
        return m != null && m.container() == states && m.writes() == ((Writes) states).quicksilver$writes();
    }

    /** After a section's setBlockState: the written position's bits, and the masks marked current again. */
    public static void afterSet(LevelChunkSection section, boolean wasCurrent, int x, int y, int z, BlockState state) {
        if (!wasCurrent) return;
        Holder h = (Holder) section;
        Mask m = h.quicksilver$mask();
        PalettedContainer<BlockState> states = section.getStates();
        if (m == null || m.container() != states) return;
        int i = (y & 15) << 8 | (z & 15) << 4 | (x & 15);
        long bit = 1L << i;
        if (state.isAir()) m.solid()[i >>> 6] &= ~bit;
        else m.solid()[i >>> 6] |= bit;
        if (oversized(state)) m.big()[i >>> 6] |= bit;
        else m.big()[i >>> 6] &= ~bit;
        h.quicksilver$mask(new Mask(m.solid(), m.big(), states, ((Writes) states).quicksilver$writes()));
    }
}
