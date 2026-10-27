package com.meakaandre.brimstone.terrain;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import com.mojang.renderpearl.api.vertex.VertexFormatElement;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import org.lwjgl.system.MemoryUtil;

/**
 * Brimstone's terrain vertex format, and the conversion of vanilla's vertices into it. One 16-byte format for every
 * terrain layer, solid, cutout and translucent (design doc, Terrain mesher, decided 2026-10-25; it replaces package 1's
 * per-layer choice between this and a 28-byte copy of vanilla's floats).
 *
 * <p><b>16 bytes</b> (vanilla's BLOCK vertex has 28):
 * <pre>
 *  0  u16 x, y, z        position in the section as (p + 8) * 2048, i.e. -8 to 24 blocks in 1/2048 steps
 *  6  u16 slot | bl << 8 slot: which of the region's 8x4x8 sections; bl: block light 0..240
 *  8  u8  r, g, b, sky   vanilla's baked colour (its alpha must be 255) and sky light 0..240
 * 12  u16 u, v           atlas UV in 1/65536 steps; 65535 stands for exactly 1.0
 * </pre>
 * Positions and UVs on those grids (every plain solid block) are stored exactly: decoding the stored value with the
 * shader's float operations gives vanilla's float bit for bit. Values off the grid (plant offsets, fluid heights, some
 * model and UV values) are rounded to the nearest step: half a step of position (1/4096 block, plus float rounding:
 * 0.000245 block measured) and of UV (1/131072; up to 1/65536 between the last step and 1.0). Light outside 0..240
 * is stored clamped: the shader's lightmap lookup clamps to the same texel either way. A layer with a position or UV
 * outside those ranges, or a colour alpha other than 255, can't be stored ({@link #convert} returns null). There is no
 * alpha byte (vanilla's terrain colours always have alpha 255; the slot keeps the plain path's merged draws on GL 3.3):
 * an opaque layer with alpha below 255 stays with vanilla, and a translucent one sends all translucent terrain back to
 * vanilla for the session ({@link Terrain}, Andre's decision 2026-10-25).
 */
public final class CompactVertex {
    public static final int BYTES = 16;
    public static final int VANILLA_BYTES = 28;

    static final VertexFormat FORMAT = VertexFormat.builder(0)
        .addAttribute("BrimCell", GpuFormat.RGBA16_UINT)
        .addAttribute("BrimTint", GpuFormat.RGBA8_UNORM)
        .addAttribute("BrimTexel", GpuFormat.RG16_UINT)
        .build();

    /**
     * One layer, converted, in a memAlloc'd buffer; how many of its vertices had a position or UV rounded. For a solid
     * or cutout layer, {@code faces} says how its quads are grouped by facing (null: one group in vanilla's order).
     */
    record Converted(ByteBuffer data, int vertices, int positionsRounded, int uvsRounded, Faces faces) {
        void free() {
            MemoryUtil.memFree(data);
        }
    }

    /**
     * Facing groups (package 4b, skip back faces): a solid or cutout layer's quads stored grouped by the direction they
     * face, each group a contiguous range of quads in the layer (so one index range of the shared quad index list),
     * groups in the order {@link #UNALIGNED}, +X, +Y, +Z, -X, -Y, -Z. Inside a group the quads keep vanilla's order.
     *
     * <p>A quad is in an axis group only if, on the 16-byte format's stored grid (what the GPU draws), all four corners
     * lie in one plane {@code axis = p} and every corner's triangle (0-1-2, 1-2-3, 2-3-0, 3-0-1) that has an area winds
     * the same way, so whichever way the quad is split into triangles, every triangle faces the same side. Everything
     * else (plants, slopes, rotated model parts, degenerate quads) is unaligned and always drawn. The side is the
     * right-hand normal of the corners in order, which is the side the GPU treats as the front (vanilla's blocks wind
     * their outward faces that way). For each axis group, {@code planeMin}/{@code planeMax} are the lowest and highest
     * plane positions of its quads, in blocks from the section origin, as the shader decodes them.
     */
    record Faces(int[] start, float[] planeMin, float[] planeMax) {
        /** Quads in group {@code g}; group {@code g} starts at quad {@code start[g]}, {@code start[GROUPS]} is the layer's quad count. */
        int quads(int g) {
            return start[g + 1] - start[g];
        }
    }

    static final int GROUPS = 7, UNALIGNED = 0;

    /** Axis (0 x, 1 y, 2 z) of an axis group 1-6. */
    static int axis(int g) {
        return (g - 1) % 3;
    }

    /** Whether an axis group faces the positive direction of its axis (groups 1-3). */
    static boolean positive(int g) {
        return g <= 3;
    }

    private CompactVertex() {
    }

    /** Null if vanilla's terrain vertex is what this transcoder reads; otherwise what differs. */
    public static String checkVanillaFormat() {
        VertexFormat f = DefaultVertexFormat.BLOCK;
        if (f.getVertexSize() != VANILLA_BYTES) return "BLOCK vertex is " + f.getVertexSize() + " bytes";
        String bad = element(f, "Position", 0, GpuFormat.RGB32_FLOAT);
        if (bad == null) bad = element(f, "Color", 12, GpuFormat.RGBA8_UNORM);
        if (bad == null) bad = element(f, "UV0", 16, GpuFormat.RG32_FLOAT);
        if (bad == null) bad = element(f, "UV2", 24, GpuFormat.RG16_SINT);
        if (bad != null) return bad;
        for (ChunkSectionLayer layer : new ChunkSectionLayer[] {ChunkSectionLayer.SOLID, ChunkSectionLayer.CUTOUT, ChunkSectionLayer.TRANSLUCENT}) {
            if (!f.equals(layer.vertexFormat())) return layer + " does not use the BLOCK vertex format";
        }
        if (FORMAT.getVertexSize() != BYTES) return "Brimstone's format is " + FORMAT.getVertexSize() + " bytes";
        return null;
    }

    private static String element(VertexFormat f, String name, int offset, GpuFormat format) {
        VertexFormatElement e = f.getElement(name);
        if (e == null) return "BLOCK has no " + name;
        if (e.offset() != offset || e.format() != format) return "BLOCK " + name + " is " + e.format() + " at " + e.offset();
        return null;
    }

    /** Which section of its region (8x4x8 sections, 128x64x128 blocks) a section origin is. */
    static int sectionIndex(int originX, int originY, int originZ) {
        return ((originX >> 4) & 7) | (((originY >> 4) & 3) << 3) | (((originZ >> 4) & 7) << 5);
    }

    /**
     * Converts a layer (vanilla's BLOCK vertices, position to limit of {@code src}) into the 16-byte format, rounding
     * off-grid positions and UVs to the nearest step and keeping vanilla's vertex order (so its quad order and, for
     * translucent layers, its index lists stay valid). Null if the layer isn't whole quads or a vertex can't be stored
     * (position or UV out of range, alpha other than 255).
     */
    static Converted convert(ByteBuffer src, int slot) {
        return convert(src, slot, false);
    }

    /**
     * As {@link #convert(ByteBuffer, int)}; with {@code group} (solid and cutout layers, never translucent, whose index
     * lists name vanilla's vertex order) the quads are stored grouped by facing ({@link Faces}).
     */
    /** Solid and cutout layers kept in vanilla's order because an unaligned quad lies in an aligned quad's plane. */
    static final java.util.concurrent.atomic.AtomicLong UNGROUPED_LAYERS = new java.util.concurrent.atomic.AtomicLong();

    static Converted convert(ByteBuffer src, int slot, boolean group) {
        int bytes = src.remaining();
        if (bytes == 0 || bytes % (4 * VANILLA_BYTES) != 0) return null;
        int vertices = bytes / VANILLA_BYTES;
        long in = MemoryUtil.memAddress(src);
        for (int v = 0; v < vertices; v++) if (!storable(in + (long) v * VANILLA_BYTES)) return null;
        int quads = vertices / 4;
        // where each quad goes: in vanilla's order, or grouped by facing (stable inside a group)
        int[] dest = null;
        Faces faces = null;
        byte[] g = null;
        int[] plane = null, count = null;
        if (group) {
            g = new byte[quads];
            plane = new int[quads];
            count = new int[GROUPS];
            // unaligned quads that are flat (within one grid step) on an axis: axis + 1 (0: none), and that extent
            byte[] flatAxis = new byte[quads];
            int[] flatLo = new int[quads], flatHi = new int[quads];
            for (int q = 0; q < quads; q++) {
                long base = in + (long) q * 4 * VANILLA_BYTES;
                int k = classify(base, plane, q, flatAxis, flatLo, flatHi);
                g[q] = (byte) k;
                count[k]++;
            }
            if (count[UNALIGNED] > 0 && count[UNALIGNED] < quads && coplanar(g, plane, flatAxis, flatLo, flatHi)) {
                // an unaligned quad lies in (or within a step of) a plane an aligned quad uses: grouping would draw it
                // before that quad and could change which wins where they overlap at equal depth, so the layer keeps
                // vanilla's order (every quad drawn, as before package 4b)
                group = false;
                UNGROUPED_LAYERS.incrementAndGet();
            }
        }
        if (group) {
            int[] start = new int[GROUPS + 1];
            for (int k = 0; k < GROUPS; k++) start[k + 1] = start[k] + count[k];
            float[] min = new float[GROUPS], max = new float[GROUPS];
            java.util.Arrays.fill(min, Float.POSITIVE_INFINITY);
            java.util.Arrays.fill(max, Float.NEGATIVE_INFINITY);
            int[] next = java.util.Arrays.copyOf(start, GROUPS);
            dest = new int[quads];
            for (int q = 0; q < quads; q++) {
                int k = g[q];
                dest[q] = next[k]++;
                if (k != UNALIGNED) {
                    float p = decodePosition(plane[q]);
                    if (p < min[k]) min[k] = p;
                    if (p > max[k]) max[k] = p;
                }
            }
            faces = new Faces(start, min, max);
        }
        ByteBuffer out = MemoryUtil.memAlloc(vertices * BYTES);
        long o = MemoryUtil.memAddress(out);
        int positions = 0, uvs = 0;
        for (int v = 0; v < vertices; v++) {
            int to = dest == null ? v : dest[v >> 2] * 4 + (v & 3);
            int r = pack(in + (long) v * VANILLA_BYTES, o + (long) to * BYTES, slot);
            if ((r & 1) != 0) positions++;
            if ((r & 2) != 0) uvs++;
        }
        return new Converted(out, vertices, positions, uvs, faces);
    }

    /**
     * The facing group of the quad whose first vanilla vertex is at {@code base}, from its positions on the stored grid
     * (exactly what the GPU draws); for an axis group the plane's stored step goes to {@code plane[q]}.
     */
    private static int classify(long base, int[] plane, int q, byte[] flatAxis, int[] flatLo, int[] flatHi) {
        int[] s = new int[12];
        for (int c = 0; c < 4; c++) {
            for (int k = 0; k < 3; k++) s[c * 3 + k] = positionStep(MemoryUtil.memGetFloat(base + (long) c * VANILLA_BYTES + 4L * k));
        }
        int g = facing(s, plane, q);
        if (g == UNALIGNED) {
            for (int k = 0; k < 3; k++) {
                int lo = Math.min(Math.min(s[k], s[3 + k]), Math.min(s[6 + k], s[9 + k]));
                int hi = Math.max(Math.max(s[k], s[3 + k]), Math.max(s[6 + k], s[9 + k]));
                if (hi - lo <= 1) {
                    flatAxis[q] = (byte) (k + 1);
                    flatLo[q] = lo;
                    flatHi[q] = hi;
                    break;
                }
            }
        }
        return g;
    }

    /**
     * Whether an unaligned quad that is flat on an axis lies within one grid step of a plane that an aligned quad of
     * the same axis uses (then reordering by group could change which of the two wins at equal depth).
     */
    private static boolean coplanar(byte[] g, int[] plane, byte[] flatAxis, int[] flatLo, int[] flatHi) {
        it.unimi.dsi.fastutil.ints.IntOpenHashSet[] planes = new it.unimi.dsi.fastutil.ints.IntOpenHashSet[3];
        for (int k = 0; k < 3; k++) planes[k] = new it.unimi.dsi.fastutil.ints.IntOpenHashSet();
        for (int q = 0; q < g.length; q++) if (g[q] != UNALIGNED) planes[axis(g[q])].add(plane[q]);
        for (int q = 0; q < g.length; q++) {
            int a = flatAxis[q] - 1;
            if (a < 0) continue;
            for (int p = flatLo[q] - 1; p <= flatHi[q] + 1; p++) if (planes[a].contains(p)) return true;
        }
        return false;
    }

    /** The facing group of a quad's four stored corner positions ({@code s}: x, y, z per corner). */
    private static int facing(int[] s, int[] plane, int q) {
        int axis = -1;
        for (int k = 0; k < 3; k++) {
            if (s[k] == s[3 + k] && s[k] == s[6 + k] && s[k] == s[9 + k]) {
                if (axis >= 0) return UNALIGNED; // flat on two axes: a line or a point
                axis = k;
            }
        }
        if (axis < 0) return UNALIGNED;
        // the two other axes, in the cyclic order that makes their 2D cross product the normal's component on axis
        int u = (axis + 1) % 3, w = (axis + 2) % 3;
        int sign = 0;
        for (int c = 0; c < 4; c++) {
            int a = c * 3, b = ((c + 1) & 3) * 3, d = ((c + 2) & 3) * 3;
            long n = (long) (s[b + u] - s[a + u]) * (s[d + w] - s[a + w]) - (long) (s[b + w] - s[a + w]) * (s[d + u] - s[a + u]);
            if (n == 0) continue;
            int sg = n > 0 ? 1 : -1;
            if (sign != 0 && sg != sign) return UNALIGNED; // twisted: its triangles face both ways
            sign = sg;
        }
        if (sign == 0) return UNALIGNED; // no area
        plane[q] = s[axis];
        return sign > 0 ? 1 + axis : 4 + axis;
    }

    /** Whether any vertex of a layer has a colour alpha other than 255 (Brimstone's vertex has no alpha byte). */
    static boolean hasAlpha(ByteBuffer src) {
        long in = MemoryUtil.memAddress(src);
        int vertices = src.remaining() / VANILLA_BYTES;
        for (int v = 0; v < vertices; v++) if (MemoryUtil.memGetByte(in + (long) v * VANILLA_BYTES + 15) != (byte) 0xFF) return true;
        return false;
    }

    /** Whether a vertex fits the format at all: position in -8..24, UV in 0..1, alpha 255. */
    private static boolean storable(long a) {
        for (int k = 0; k < 3; k++) if (positionStep(MemoryUtil.memGetFloat(a + 4L * k)) < 0) return false;
        for (int k = 0; k < 2; k++) if (uvStep(MemoryUtil.memGetFloat(a + 16 + 4L * k)) < 0) return false;
        return MemoryUtil.memGetByte(a + 15) == (byte) 0xFF;
    }

    /** Packs one vertex; bit 0 set if a position component was rounded, bit 1 if a UV component was. */
    private static int pack(long a, long o, int slot) {
        int rounded = 0;
        for (int k = 0; k < 3; k++) {
            float p = MemoryUtil.memGetFloat(a + 4L * k);
            int s = positionStep(p);
            if (Float.floatToRawIntBits(decodePosition(s)) != Float.floatToRawIntBits(p)) rounded |= 1;
            MemoryUtil.memPutShort(o + 2L * k, (short) s);
        }
        MemoryUtil.memPutShort(o + 6, (short) (slot | light(MemoryUtil.memGetShort(a + 24)) << 8));
        MemoryUtil.memPutByte(o + 8, MemoryUtil.memGetByte(a + 12));
        MemoryUtil.memPutByte(o + 9, MemoryUtil.memGetByte(a + 13));
        MemoryUtil.memPutByte(o + 10, MemoryUtil.memGetByte(a + 14));
        MemoryUtil.memPutByte(o + 11, (byte) light(MemoryUtil.memGetShort(a + 26)));
        for (int k = 0; k < 2; k++) {
            float t = MemoryUtil.memGetFloat(a + 16 + 4L * k);
            int s = uvStep(t);
            if (Float.floatToRawIntBits(decodeUv(s)) != Float.floatToRawIntBits(t)) rounded |= 2;
            MemoryUtil.memPutShort(o + 12 + 2L * k, (short) s);
        }
        return rounded;
    }

    /** Nearest 1/2048 step from -8, or -1 outside -8..24. */
    static int positionStep(float p) {
        if (!(p >= -8f && p < 24f)) return -1;
        return Math.min(65535, Math.round((p + 8f) * 2048f));
    }

    /** What the shader computes from a stored step: step * 2^-11 - 8 (both operations exact in float). */
    static float decodePosition(int step) {
        return step * (1f / 2048f) - 8f;
    }

    /**
     * Nearest UV step, or -1 outside 0..1. Steps 0..65534 decode to step / 65536 and 65535 to exactly 1.0, so a UV
     * between 65534/65536 and 1.0 goes to whichever of the two is nearer.
     */
    static int uvStep(float t) {
        if (t == 1.0f) return 65535;
        if (!(t >= 0f && t < 1f)) return -1;
        int s = Math.round(t * 65536f);
        if (s >= 65535) s = t >= 65535f / 65536f ? 65535 : 65534;
        return s;
    }

    /** What the shader computes from a stored UV step. */
    static float decodeUv(int step) {
        return step == 65535 ? 1.0f : step * (1f / 65536f);
    }

    private static int light(short s) {
        return Math.max(0, Math.min(240, s));
    }
}
