package com.meakaandre.brimstone.visibility;

import com.meakaandre.brimstone.BrimstoneClient;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.chunk.SectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Util;

/**
 * When each section last changed on the client, so the occlusion test only trusts a section's blocks once the mesh
 * vanilla draws for it was built after that change: a section counts as "clean" only if its mesh's compile task started
 * after the last change (the same comparison vanilla makes for its transient blocks, with the same clock,
 * {@link Util#getNanos}). This relies on vanilla making block changes only between frames (packets and ticks), never
 * between a frame's extract, where the section copies for meshing are taken, and its render, where the compile tasks
 * (and their start times) are created; that holds in 26.3.
 *
 * <p>Changes come from every dirty mark vanilla makes ({@code SectionUpdateTracker.setDirty}), from block changes that
 * vanilla decides need no new mesh (the block and its neighbours are marked here anyway), and from chunk data arriving
 * or being dropped (the column and its neighbours). A reset (new level, all sections rebuilt) makes every older mesh
 * untrusted.
 *
 * <p>Entries are only needed while an older mesh could still be drawn. When a map fills up ({@link #CAP}), entries are
 * dropped whose section has since got a newer mesh, or has left the view area (vanilla resets a section's mesh when its
 * slot moves to another position, so whatever mesh it gets later is newer). Only if that frees too little does
 * everything count as changed until rebuilt (conservative), with a log line. Render thread only.
 */
final class SectionChanges {
    static final int CAP = 1 << 16;
    private static final Long2LongOpenHashMap SECTIONS = new Long2LongOpenHashMap();
    private static final Long2LongOpenHashMap COLUMNS = new Long2LongOpenHashMap();
    private static long resetNanos;
    private static final BlockPos.MutableBlockPos POS = new BlockPos.MutableBlockPos();

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

    static synchronized int size() {
        return SECTIONS.size() + COLUMNS.size();
    }

    private static long columnKey(int x, int z) {
        return ((long) x << 32) ^ (z & 0xffffffffL);
    }

    private static void put(Long2LongOpenHashMap map, long key) {
        if (map.size() >= CAP && !map.containsKey(key)) {
            prune();
            if (map.size() >= CAP * 3 / 4) {
                int n = map.size();
                reset();
                BrimstoneClient.LOG.warn("Brimstone visibility: {} recent section changes are tracked, too many to keep; every mesh built"
                    + " before now is distrusted until vanilla rebuilds it (nothing is hidden behind it meanwhile)", n);
            }
        }
        map.put(key, Util.getNanos());
    }

    /** Drops entries no mesh that could still be drawn depends on. */
    private static void prune() {
        Minecraft mc = Minecraft.getInstance();
        ViewArea view = mc.levelRenderer == null ? null : mc.levelRenderer.viewArea();
        if (view == null || mc.level == null) {
            SECTIONS.clear();
            COLUMNS.clear();
            resetNanos = Util.getNanos();
            return;
        }
        for (ObjectIterator<Long2LongMap.Entry> it = SECTIONS.long2LongEntrySet().fastIterator(); it.hasNext(); ) {
            Long2LongMap.Entry e = it.next();
            if (superseded(view, e.getLongKey(), e.getLongValue())) it.remove();
        }
        int minY = mc.level.getMinSectionY(), maxY = mc.level.getMaxSectionY();
        for (ObjectIterator<Long2LongMap.Entry> it = COLUMNS.long2LongEntrySet().fastIterator(); it.hasNext(); ) {
            Long2LongMap.Entry e = it.next();
            int cx = (int) (e.getLongKey() >> 32), cz = (int) e.getLongKey();
            boolean all = true;
            for (int y = minY; y <= maxY && all; y++) all = superseded(view, SectionPos.asLong(cx, y, cz), e.getLongValue());
            if (all) it.remove();
        }
    }

    /** No mesh older than {@code changed} can be drawn for this section any more. */
    private static boolean superseded(ViewArea view, long node, long changed) {
        SectionRenderDispatcher.RenderSection rs = view.getRenderSectionAt(POS.set(
            SectionPos.sectionToBlockCoord(SectionPos.x(node)), SectionPos.sectionToBlockCoord(SectionPos.y(node)),
            SectionPos.sectionToBlockCoord(SectionPos.z(node))));
        if (rs == null || rs.getSectionNode() != node) return true;
        SectionMesh mesh = rs.getSectionMesh();
        return mesh != null && mesh.getCompileTaskStartTime() > changed;
    }
}
