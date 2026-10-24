package com.meakaandre.brimstone.visibility;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Util;

/**
 * When each section last changed on the client, so the occlusion test only trusts a section's blocks once the mesh
 * vanilla draws for it was built after that change: a section counts as "clean" only if its mesh's compile task started
 * after the last change (the same comparison vanilla makes for its transient blocks, with the same clock,
 * {@link Util#getNanos}).
 *
 * <p>Changes come from every dirty mark vanilla makes ({@code SectionUpdateTracker.setDirty}), from block changes that
 * vanilla decides need no new mesh (the block and its neighbours are marked here anyway), and from chunk data arriving
 * or being dropped (the column and its neighbours). A reset (new level, all sections rebuilt) makes every older mesh
 * untrusted. Render thread only (the client's main thread), synchronized all the same.
 */
final class SectionChanges {
    /** Above this many entries the maps are cleared and everything older counts as changed (conservative). */
    private static final int CAP = 1 << 16;
    private static final Long2LongOpenHashMap SECTIONS = new Long2LongOpenHashMap();
    private static final Long2LongOpenHashMap COLUMNS = new Long2LongOpenHashMap();
    private static long resetNanos;

    private SectionChanges() {
    }

    static synchronized void section(int x, int y, int z) {
        put(SECTIONS, SectionPos.asLong(x, y, z));
    }

    /** A block changed: its section and every section within one block of it. */
    static synchronized void block(int bx, int by, int bz) {
        for (int x = (bx - 1) >> 4; x <= (bx + 1) >> 4; x++) {
            for (int y = (by - 1) >> 4; y <= (by + 1) >> 4; y++) {
                for (int z = (bz - 1) >> 4; z <= (bz + 1) >> 4; z++) put(SECTIONS, SectionPos.asLong(x, y, z));
            }
        }
    }

    /** Chunk data arrived or was dropped: the column and its neighbours (their meshes depend on it). */
    static synchronized void column(int cx, int cz) {
        for (int x = cx - 1; x <= cx + 1; x++) for (int z = cz - 1; z <= cz + 1; z++) put(COLUMNS, columnKey(x, z));
    }

    static synchronized void reset() {
        SECTIONS.clear();
        COLUMNS.clear();
        resetNanos = Util.getNanos();
    }

    /** The last time anything that section's mesh depends on changed, as far as Brimstone saw. */
    static synchronized long lastChange(long section) {
        long t = Math.max(resetNanos, SECTIONS.get(section));
        return Math.max(t, COLUMNS.get(columnKey(SectionPos.x(section), SectionPos.z(section))));
    }

    private static long columnKey(int x, int z) {
        return ((long) x << 32) ^ (z & 0xffffffffL);
    }

    private static void put(Long2LongOpenHashMap map, long key) {
        if (map.size() >= CAP) {
            reset();
        }
        map.put(key, Util.getNanos());
    }
}
