package com.meakaandre.quicksilver.mesher;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.meakaandre.quicksilver.QuicksilverClient;
import com.meakaandre.quicksilver.terrain.CompactVertex;
import com.meakaandre.quicksilver.terrain.TerrainStats;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import org.lwjgl.system.MemoryUtil;

/**
 * CI's check of Quicksilver's mesher against vanilla's (debug property {@code quicksilver.debug.verifyMesher}, set in
 * every Quicksilver CI run; off otherwise). Each section Quicksilver meshes outside a bench is meshed a second time by
 * vanilla's own compiler (with Quicksilver's Fabric Renderer API renderer, as before package 4c), its layers converted
 * into the 16-byte vertex as the terrain module converted them before, and compared with Quicksilver's: the same layers,
 * byte for byte the same vertices in the same order and facing groups, the same translucent index list, the same
 * visibility between faces and the same block entities in the same order. So "no new picture differences" holds by
 * construction wherever the check ran. Benches are left out (their meshing times must not include the check); their
 * pictures are checked by the pixel gates.
 *
 * <p>Any difference logs a WARN line starting "Quicksilver mesher check:" (the first 40, with the section, layer, first
 * differing vertex decoded on both sides and the block Quicksilver made it from) and is counted in the bench's
 * {@code mesher} block; CI fails on any.
 */
final class MesherCheck {
    private static int warnings;

    private MesherCheck() {
    }

    /** Vanilla's compiler on the same section (not timed in the bench's meshing numbers). */
    static SectionCompiler.Results vanilla(SectionPos section, RenderSectionRegion region, VertexSorting sorting, SectionBufferBuilderPack pack,
                                           Operation<SectionCompiler.Results> original) {
        TerrainStats.untimed(true);
        try {
            return original.call(section, region, sorting, pack);
        } finally {
            TerrainStats.untimed(false);
        }
    }

    /**
     * Compares Quicksilver's mesh with vanilla's of the same section (from the same copy of the world, on the same thread,
     * one right after the other: also a like-for-like timing of the two meshers, when timed) and lets go of vanilla's.
     * Returns the difference, or null; the caller decides whether it counts (see {@code Mesher.compile}).
     */
    static String compareWith(MeshWorker worker, SectionPos section, SectionCompiler.Results mine, long mineNanos,
                              SectionCompiler.Results vanilla, long vanillaNanos, boolean vanillaFirst) {
        long t0 = System.nanoTime();
        boolean timed = mineNanos > 0;
        // vanilla's side ends where Quicksilver's does, "ready to upload": its layers turned into the 16-byte vertex as
        // the terrain module turns them when vanilla's compiler meshes (grouped by facing for solid and cutout, in order
        // for translucent; docs/ci-limits.md, "Package 4c: like-for-like timing")
        int slot = CompactVertex.sectionIndex(section.minBlockX(), section.minBlockY(), section.minBlockZ());
        CompactVertex.Converted[] converted = new CompactVertex.Converted[3];
        long c0 = System.nanoTime();
        for (int li = 0; li < 3; li++) {
            MeshData v = vanilla.renderedLayers.get(MeshWorker.LAYERS[li]);
            if (v != null) converted[li] = CompactVertex.convert(v.vertexBuffer(), slot, li != 2);
        }
        vanillaNanos += System.nanoTime() - c0;
        if (timed) {
            (vanillaFirst ? MesherStats.CHECK_MINE_SECOND : MesherStats.CHECK_MINE_FIRST).addAndGet(mineNanos);
            (vanillaFirst ? MesherStats.CHECK_VANILLA_FIRST : MesherStats.CHECK_VANILLA_SECOND).addAndGet(vanillaNanos);
            (vanillaFirst ? MesherStats.CHECKED_VANILLA_FIRST : MesherStats.CHECKED_MINE_FIRST).incrementAndGet();
            MesherStats.CHECK_MINE_NANOS.addAndGet(mineNanos);
            MesherStats.CHECK_VANILLA_NANOS.addAndGet(vanillaNanos);
            MesherStats.pair(mineNanos, vanillaNanos);
        }
        try {
            String bad = compare(worker.output, section, mine, vanilla, converted);
            if (timed) MesherStats.CHECKED.incrementAndGet();
            return bad;
        } catch (Throwable t) {
            if (t instanceof VirtualMachineError e) throw e;
            return "the check failed: " + t;
        } finally {
            for (MeshData m : vanilla.renderedLayers.values()) m.close();
            for (CompactVertex.Converted c : converted) if (c != null) c.free();
            if (timed) MesherStats.CHECK_NANOS.addAndGet(System.nanoTime() - t0 + vanillaNanos);
        }
    }

    static String where(SectionPos section) {
        return "section " + section.x() + " " + section.y() + " " + section.z() + ": ";
    }

    private static String compare(MesherOutput out, SectionPos section, SectionCompiler.Results mine, SectionCompiler.Results vanilla,
                                  CompactVertex.Converted[] converted) {
        int slot = CompactVertex.sectionIndex(section.minBlockX(), section.minBlockY(), section.minBlockZ());
        for (int li = 0; li < 3; li++) {
            ChunkSectionLayer layer = MeshWorker.LAYERS[li];
            MeshData v = vanilla.renderedLayers.get(layer);
            MesherOutput.LayerOut lo = out.layers[li];
            if (v == null && !lo.present) continue;
            if (v == null) return layer + ": Quicksilver has " + lo.vertices + " vertices, vanilla none";
            int vv = v.vertexBuffer().remaining() / CompactVertex.VANILLA_BYTES;
            if (!lo.present) return layer + ": vanilla has " + vv + " vertices, Quicksilver none";
            CompactVertex.Converted c = converted[li];
            if (c == null) return layer + ": vanilla's layer is not storable in the 16-byte vertex (" + vv + " vertices)";
            {
                if (c.vertices() != lo.vertices) return layer + ": " + lo.vertices + " vertices, vanilla " + c.vertices() + diff(out, li, c, lo);
                String d = diff(out, li, c, lo);
                if (!d.isEmpty()) return layer + ":" + d;
                if (c.positionsRounded() != lo.positionsRounded || c.uvsRounded() != lo.uvsRounded) {
                    return layer + ": rounded positions/UVs " + lo.positionsRounded + "/" + lo.uvsRounded + ", vanilla " + c.positionsRounded()
                        + "/" + c.uvsRounded();
                }
                if (!sameFaces(c.faces(), lo.faces)) return layer + ": facing groups " + faces(lo.faces) + ", vanilla " + faces(c.faces());
            }
            if (li == 2) {
                ByteBuffer vi = v.indexBuffer();
                MeshData m = mine.renderedLayers.get(layer);
                ByteBuffer mi = m == null ? null : m.indexBuffer();
                if (vi == null || mi == null) return layer + ": an index list missing (" + (mi == null ? "Quicksilver's" : "vanilla's") + ")";
                if (v.drawState().indexType() != m.drawState().indexType()) return layer + ": index type differs";
                if (vi.remaining() != mi.remaining() || !same(MemoryUtil.memAddress(vi), MemoryUtil.memAddress(mi), vi.remaining())) {
                    return layer + ": the sorted index list differs";
                }
            }
        }
        for (Direction a : Direction.values()) {
            for (Direction b : Direction.values()) {
                if (mine.visibilitySet.visibilityBetween(a, b) != vanilla.visibilitySet.visibilityBetween(a, b)) {
                    return "visibility between " + a + " and " + b + " differs";
                }
            }
        }
        List<?> mb = mine.blockEntities, vb = vanilla.blockEntities;
        if (mb.size() != vb.size()) return "block entities: " + mb.size() + ", vanilla " + vb.size();
        for (int i = 0; i < mb.size(); i++) if (mb.get(i) != vb.get(i)) return "block entity " + i + " differs";
        return null;
    }

    /** The first vertex that differs, decoded on both sides, with the block Quicksilver made it from; "" if none. */
    private static String diff(MesherOutput out, int li, CompactVertex.Converted c, MesherOutput.LayerOut lo) {
        long a = lo.data, b = MemoryUtil.memAddress(c.data());
        int n = Math.min(lo.vertices, c.vertices());
        for (int i = 0; i < n; i++) {
            long va = a + (long) i * CompactVertex.BYTES, vb = b + (long) i * CompactVertex.BYTES;
            if (!same(va, vb, CompactVertex.BYTES)) {
                long qa = a + (long) (i & ~3) * CompactVertex.BYTES, qb = b + (long) (i & ~3) * CompactVertex.BYTES;
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < 4; k++) sb.append(" [").append(k).append("] Q ").append(decode(qa + (long) k * CompactVertex.BYTES))
                    .append(" | V ").append(decode(qb + (long) k * CompactVertex.BYTES));
                return " vertex " + i + " (quad " + (i >> 2) + ") differs; Quicksilver's quad came from " + out.origin(li, i >> 2) + "; the quad:" + sb;
            }
        }
        return "";
    }

    private static boolean same(long a, long b, int bytes) {
        int i = 0;
        for (; i + 8 <= bytes; i += 8) if (MemoryUtil.memGetLong(a + i) != MemoryUtil.memGetLong(b + i)) return false;
        for (; i < bytes; i++) if (MemoryUtil.memGetByte(a + i) != MemoryUtil.memGetByte(b + i)) return false;
        return true;
    }

    private static String decode(long v) {
        int x = MemoryUtil.memGetShort(v) & 0xFFFF, y = MemoryUtil.memGetShort(v + 2) & 0xFFFF, z = MemoryUtil.memGetShort(v + 4) & 0xFFFF;
        int w = MemoryUtil.memGetShort(v + 6) & 0xFFFF;
        int r = MemoryUtil.memGetByte(v + 8) & 0xFF, g = MemoryUtil.memGetByte(v + 9) & 0xFF, bl = MemoryUtil.memGetByte(v + 10) & 0xFF;
        int sky = MemoryUtil.memGetByte(v + 11) & 0xFF;
        int u = MemoryUtil.memGetShort(v + 12) & 0xFFFF, vv = MemoryUtil.memGetShort(v + 14) & 0xFFFF;
        return String.format(java.util.Locale.ROOT, "pos %.5f %.5f %.5f, rgb %d %d %d, light block %d sky %d, uv %d %d",
            CompactVertex.decodePosition(x), CompactVertex.decodePosition(y), CompactVertex.decodePosition(z), r, g, bl, w >> 8, sky, u, vv);
    }

    private static boolean sameFaces(CompactVertex.Faces a, CompactVertex.Faces b) {
        if (a == null || b == null) return a == b;
        return Arrays.equals(a.start(), b.start()) && Arrays.equals(a.planeMin(), b.planeMin()) && Arrays.equals(a.planeMax(), b.planeMax());
    }

    private static String faces(CompactVertex.Faces f) {
        return f == null ? "none (made order)" : Arrays.toString(f.start());
    }

    static void warn(String what) {
        if (warnings < 40) {
            warnings++;
            QuicksilverClient.LOG.warn("Quicksilver mesher check: {}", what);
        }
    }
}
