package com.meakaandre.quicksilver.mesher;

import com.meakaandre.quicksilver.terrain.CompactVertex;
import java.util.Arrays;
import org.lwjgl.system.MemoryUtil;

/**
 * Writes a section's quads straight into Quicksilver's 16-byte terrain vertex ({@link CompactVertex}; design doc, package
 * 4c: no conversion step). Solid and cutout quads go into one buffer per facing group as they are written (package 4b's
 * groups: unaligned, +X, +Y, +Z, -X, -Z, -Y, vanilla's order kept inside each), classified on the stored grid with the
 * same rule as {@link CompactVertex#convert}; translucent quads stay in the order they were made, which vanilla's sorted
 * index lists refer to.
 *
 * <p>The caller fills the four corners ({@link #x} ... {@link #light}) and calls {@link #emit}. The values are vanilla's:
 * what vanilla's mesher would have put into its vertex buffer (position floats, ARGB colour, UV floats, packed light),
 * so the stored bytes are what converting vanilla's vertices gives, bit for bit.
 */
final class SectionWriter {
    static final int SOLID = 0, CUTOUT = 1, TRANSLUCENT = 2, LAYERS = 3;
    private static final int QUAD = 4 * CompactVertex.BYTES;

    // the quad being written
    final float[] x = new float[4], y = new float[4], z = new float[4], u = new float[4], v = new float[4];
    final int[] color = new int[4], light = new int[4];

    private int slot;
    /** The block the next quads come from (section-relative x | y << 4 | z << 8, plus 1 << 12 for a fluid): for CI's check. */
    int origin;
    /** Why this section cannot be written (null while it can): a vertex out of the format's range, a colour alpha. */
    String unstorable;

    final Layer[] layers = {new Layer(true), new Layer(true), new Layer(false)};

    /** Animated sprites of the section: UV centres are looked up as quads are written (package 8). */
    final SpriteScan sprites = new SpriteScan();

    private final int[] steps = new int[12], uvSteps = new int[8];
    private final int[] out = new int[2];

    void begin(int slot) {
        this.slot = slot;
        unstorable = null;
        for (Layer l : layers) l.clear();
        sprites.begin();
    }

    /** Writes the quad in {@link #x} ... {@link #light} to {@code layer}. */
    void emit(int layer) {
        int[] s = steps, t = uvSteps;
        int posRounded = 0, uvRounded = 0;
        // every value worked out once: the grid steps, whether they round, and that the quad can be stored at all
        for (int c = 0; c < 4; c++) {
            float px = x[c], py = y[c], pz = z[c], pu = u[c], pv = v[c];
            int sx = CompactVertex.positionStep(px), sy = CompactVertex.positionStep(py), sz = CompactVertex.positionStep(pz);
            if ((sx | sy | sz) < 0) {
                if (unstorable == null) unstorable = "a position outside -8..24 blocks";
                return;
            }
            int su = CompactVertex.uvStep(pu), sv = CompactVertex.uvStep(pv);
            if ((su | sv) < 0) {
                if (unstorable == null) unstorable = "a UV outside 0..1";
                return;
            }
            if ((color[c] >>> 24) != 0xFF) {
                if (unstorable == null) unstorable = "a vertex colour alpha below 255";
                return;
            }
            s[c * 3] = sx;
            s[c * 3 + 1] = sy;
            s[c * 3 + 2] = sz;
            t[c * 2] = su;
            t[c * 2 + 1] = sv;
            if (Float.floatToRawIntBits(CompactVertex.decodePosition(sx)) != Float.floatToRawIntBits(px)
                || Float.floatToRawIntBits(CompactVertex.decodePosition(sy)) != Float.floatToRawIntBits(py)
                || Float.floatToRawIntBits(CompactVertex.decodePosition(sz)) != Float.floatToRawIntBits(pz)) posRounded++;
            if (Float.floatToRawIntBits(CompactVertex.decodeUv(su)) != Float.floatToRawIntBits(pu)
                || Float.floatToRawIntBits(CompactVertex.decodeUv(sv)) != Float.floatToRawIntBits(pv)) uvRounded++;
        }
        sprites.quad(u, v);
        Layer l = layers[layer];
        l.origin(origin);
        l.positionsRounded += posRounded;
        l.uvsRounded += uvRounded;
        long at;
        if (l.grouped) {
            int g = CompactVertex.facingOfSteps(s, out);
            int plane = out[0];
            byte flat = 0;
            int lo = 0, hi = 0;
            if (g == CompactVertex.UNALIGNED) {
                flat = (byte) CompactVertex.flatOfSteps(s, out);
                lo = out[0];
                hi = out[1];
            }
            at = l.addGrouped(g, plane, flat, lo, hi);
        } else {
            at = l.addOrdered(x, y, z);
        }
        int sl = slot;
        for (int c = 0; c < 4; c++) {
            int lt = light[c], col = color[c];
            // vanilla's vertex keeps the packed light as two shorts (block, sky); the 16-byte vertex clamps each to 0..240
            long first = (s[c * 3] & 0xFFFFL) | (s[c * 3 + 1] & 0xFFFFL) << 16 | (s[c * 3 + 2] & 0xFFFFL) << 32
                | ((sl | CompactVertex.light((short) (lt & 0xFFFF)) << 8) & 0xFFFFL) << 48;
            long second = (col >> 16 & 0xFFL) | (col >> 8 & 0xFFL) << 8 | (col & 0xFFL) << 16
                | (CompactVertex.light((short) ((lt >>> 16) & 0xFFFF)) & 0xFFL) << 24
                | (t[c * 2] & 0xFFFFL) << 32 | (t[c * 2 + 1] & 0xFFFFL) << 48;
            long o = at + (long) c * CompactVertex.BYTES;
            if (LITTLE_ENDIAN) {
                MemoryUtil.memPutLong(o, first);
                MemoryUtil.memPutLong(o + 8, second);
            } else {
                MemoryUtil.memPutShort(o, (short) first);
                MemoryUtil.memPutShort(o + 2, (short) (first >>> 16));
                MemoryUtil.memPutShort(o + 4, (short) (first >>> 32));
                MemoryUtil.memPutShort(o + 6, (short) (first >>> 48));
                MemoryUtil.memPutByte(o + 8, (byte) second);
                MemoryUtil.memPutByte(o + 9, (byte) (second >>> 8));
                MemoryUtil.memPutByte(o + 10, (byte) (second >>> 16));
                MemoryUtil.memPutByte(o + 11, (byte) (second >>> 24));
                MemoryUtil.memPutShort(o + 12, (short) (second >>> 32));
                MemoryUtil.memPutShort(o + 14, (short) (second >>> 48));
            }
        }
    }

    /**
     * As {@link #emit}, for a quad of Quicksilver's renderer read straight from its backing array ({@code d}, the first
     * vertex at {@code v0}, {@code stride} ints between vertices; offsets as in {@code frapi.RawQuad}): the same checks
     * and the same bytes, without copying the quad into {@link #x} ... {@link #light} first.
     */
    void emitRaw(int layer, int[] d, int v0, int stride, int ox, int oy, int oz, int ou, int ov, int ocolor, int olight) {
        int[] s = steps, t = uvSteps;
        int posRounded = 0, uvRounded = 0;
        float uSum = 0, vSum = 0;
        for (int c = 0; c < 4; c++) {
            int b = v0 + c * stride;
            float px = Float.intBitsToFloat(d[b + ox]), py = Float.intBitsToFloat(d[b + oy]), pz = Float.intBitsToFloat(d[b + oz]);
            float pu = Float.intBitsToFloat(d[b + ou]), pv = Float.intBitsToFloat(d[b + ov]);
            int sx = CompactVertex.positionStep(px), sy = CompactVertex.positionStep(py), sz = CompactVertex.positionStep(pz);
            if ((sx | sy | sz) < 0) {
                if (unstorable == null) unstorable = "a position outside -8..24 blocks";
                return;
            }
            int su = CompactVertex.uvStep(pu), sv = CompactVertex.uvStep(pv);
            if ((su | sv) < 0) {
                if (unstorable == null) unstorable = "a UV outside 0..1";
                return;
            }
            if ((d[b + ocolor] >>> 24) != 0xFF) {
                if (unstorable == null) unstorable = "a vertex colour alpha below 255";
                return;
            }
            s[c * 3] = sx;
            s[c * 3 + 1] = sy;
            s[c * 3 + 2] = sz;
            t[c * 2] = su;
            t[c * 2 + 1] = sv;
            uSum += pu;
            vSum += pv;
            if (Float.floatToRawIntBits(CompactVertex.decodePosition(sx)) != Float.floatToRawIntBits(px)
                || Float.floatToRawIntBits(CompactVertex.decodePosition(sy)) != Float.floatToRawIntBits(py)
                || Float.floatToRawIntBits(CompactVertex.decodePosition(sz)) != Float.floatToRawIntBits(pz)) posRounded++;
            if (Float.floatToRawIntBits(CompactVertex.decodeUv(su)) != Float.floatToRawIntBits(pu)
                || Float.floatToRawIntBits(CompactVertex.decodeUv(sv)) != Float.floatToRawIntBits(pv)) uvRounded++;
        }
        sprites.quadSums(uSum, vSum);
        Layer l = layers[layer];
        l.origin(origin);
        l.positionsRounded += posRounded;
        l.uvsRounded += uvRounded;
        long at;
        if (l.grouped) {
            int g = CompactVertex.facingOfSteps(s, out);
            int plane = out[0];
            byte flat = 0;
            int lo = 0, hi = 0;
            if (g == CompactVertex.UNALIGNED) {
                flat = (byte) CompactVertex.flatOfSteps(s, out);
                lo = out[0];
                hi = out[1];
            }
            at = l.addGrouped(g, plane, flat, lo, hi);
        } else {
            for (int c = 0; c < 4; c++) {
                int b = v0 + c * stride;
                x[c] = Float.intBitsToFloat(d[b + ox]);
                y[c] = Float.intBitsToFloat(d[b + oy]);
                z[c] = Float.intBitsToFloat(d[b + oz]);
            }
            at = l.addOrdered(x, y, z);
        }
        int sl = slot;
        for (int c = 0; c < 4; c++) {
            int b = v0 + c * stride;
            write(at + (long) c * CompactVertex.BYTES, s, t, c, sl, d[b + olight], d[b + ocolor]);
        }
    }

    /** One vertex's 16 bytes at {@code o}, from its steps and its vanilla light and colour. */
    private static void write(long o, int[] s, int[] t, int c, int sl, int lt, int col) {
        // vanilla's vertex keeps the packed light as two shorts (block, sky); the 16-byte vertex clamps each to 0..240
        long first = (s[c * 3] & 0xFFFFL) | (s[c * 3 + 1] & 0xFFFFL) << 16 | (s[c * 3 + 2] & 0xFFFFL) << 32
            | ((sl | CompactVertex.light((short) (lt & 0xFFFF)) << 8) & 0xFFFFL) << 48;
        long second = (col >> 16 & 0xFFL) | (col >> 8 & 0xFFL) << 8 | (col & 0xFFL) << 16
            | (CompactVertex.light((short) ((lt >>> 16) & 0xFFFF)) & 0xFFL) << 24
            | (t[c * 2] & 0xFFFFL) << 32 | (t[c * 2 + 1] & 0xFFFFL) << 48;
        if (LITTLE_ENDIAN) {
            MemoryUtil.memPutLong(o, first);
            MemoryUtil.memPutLong(o + 8, second);
        } else {
            MemoryUtil.memPutShort(o, (short) first);
            MemoryUtil.memPutShort(o + 2, (short) (first >>> 16));
            MemoryUtil.memPutShort(o + 4, (short) (first >>> 32));
            MemoryUtil.memPutShort(o + 6, (short) (first >>> 48));
            MemoryUtil.memPutByte(o + 8, (byte) second);
            MemoryUtil.memPutByte(o + 9, (byte) (second >>> 8));
            MemoryUtil.memPutByte(o + 10, (byte) (second >>> 16));
            MemoryUtil.memPutByte(o + 11, (byte) (second >>> 24));
            MemoryUtil.memPutShort(o + 12, (short) (second >>> 32));
            MemoryUtil.memPutShort(o + 14, (short) (second >>> 48));
        }
    }

    /** Native byte order is little-endian: a vertex's two halves are written as two longs (the same bytes). */
    private static final boolean LITTLE_ENDIAN = java.nio.ByteOrder.nativeOrder() == java.nio.ByteOrder.LITTLE_ENDIAN;

    void free() {
        for (Layer l : layers) l.free();
    }

    /** Everything written since {@link #begin}, layer by layer and group by group (CI's fluid self-test compares it). */
    byte[] dump() {
        java.io.ByteArrayOutputStream o = new java.io.ByteArrayOutputStream();
        for (Layer l : layers) {
            o.write(l.quads & 0xFF);
            o.write(l.quads >> 8 & 0xFF);
            NativeBuffer[] bufs = l.grouped ? l.group : new NativeBuffer[] {l.ordered};
            for (NativeBuffer b : bufs) {
                o.write(b.size / QUAD & 0xFF);
                for (int i = 0; i < b.size; i++) o.write(MemoryUtil.memGetByte(b.address + i));
            }
        }
        return o.toByteArray();
    }

    /** One layer's quads while a section is written. */
    static final class Layer {
        final boolean grouped;
        int quads;
        int positionsRounded, uvsRounded;
        // solid and cutout: one buffer per facing group, and each quad's group, index in it and plane
        final NativeBuffer[] group;
        final int[] groupQuads = new int[CompactVertex.GROUPS];
        final float[] planeMin = new float[CompactVertex.GROUPS], planeMax = new float[CompactVertex.GROUPS];
        byte[] quadGroup = new byte[256];
        int[] quadPlane = new int[256];
        byte[] flatAxis = new byte[256];
        int[] flatLo = new int[256], flatHi = new int[256];
        // translucent: the quads in order, and their corner positions as vanilla had them (for vanilla's sort)
        final NativeBuffer ordered;
        float[] positions = new float[256 * 12];
        /** Each quad's block, in the order made (see {@link SectionWriter#origin}). */
        int[] origins = new int[256];

        void origin(int o) {
            if (quads == origins.length) origins = Arrays.copyOf(origins, quads * 2);
            origins[quads] = o;
        }

        /** The block the quad stored at {@code stored} came from, for CI's check. */
        String originOf(int stored, CompactVertex.Faces faces) {
            int made = stored;
            if (grouped && faces != null) {
                made = -1;
                int[] next = new int[CompactVertex.GROUPS];
                for (int q = 0; q < quads && made < 0; q++) {
                    int g = quadGroup[q];
                    if (faces.start()[g] + next[g]++ == stored) made = q;
                }
            }
            if (made < 0 || made >= quads) return "(no quad " + stored + ")";
            int o = origins[made];
            return ((o & 4096) != 0 ? "the fluid at " : "the block at ") + (o & 15) + " " + (o >> 4 & 15) + " " + (o >> 8 & 15) + " (made as quad " + made + ")";
        }

        Layer(boolean grouped) {
            this.grouped = grouped;
            if (grouped) {
                group = new NativeBuffer[CompactVertex.GROUPS];
                for (int g = 0; g < group.length; g++) group[g] = new NativeBuffer(g == 0 ? 64 * QUAD : 16 * QUAD);
                ordered = null;
            } else {
                group = null;
                ordered = new NativeBuffer(64 * QUAD);
            }
        }

        void clear() {
            quads = 0;
            positionsRounded = uvsRounded = 0;
            if (grouped) {
                for (NativeBuffer b : group) b.clear();
                Arrays.fill(groupQuads, 0);
                Arrays.fill(planeMin, Float.POSITIVE_INFINITY);
                Arrays.fill(planeMax, Float.NEGATIVE_INFINITY);
            } else {
                ordered.clear();
            }
        }

        long addGrouped(int g, int plane, byte flat, int lo, int hi) {
            int q = quads++;
            if (q == quadGroup.length) {
                int n = q * 2;
                quadGroup = Arrays.copyOf(quadGroup, n);
                quadPlane = Arrays.copyOf(quadPlane, n);
                flatAxis = Arrays.copyOf(flatAxis, n);
                flatLo = Arrays.copyOf(flatLo, n);
                flatHi = Arrays.copyOf(flatHi, n);
            }
            quadGroup[q] = (byte) g;
            quadPlane[q] = plane;
            flatAxis[q] = flat;
            flatLo[q] = lo;
            flatHi[q] = hi;
            if (g != CompactVertex.UNALIGNED) {
                float p = CompactVertex.decodePosition(plane);
                if (p < planeMin[g]) planeMin[g] = p;
                if (p > planeMax[g]) planeMax[g] = p;
            }
            groupQuads[g]++;
            return group[g].reserve(QUAD);
        }

        long addOrdered(float[] x, float[] y, float[] z) {
            int q = quads++;
            if ((q + 1) * 12 > positions.length) positions = Arrays.copyOf(positions, positions.length * 2);
            int p = q * 12;
            for (int c = 0; c < 4; c++) {
                positions[p++] = x[c];
                positions[p++] = y[c];
                positions[p++] = z[c];
            }
            return ordered.reserve(QUAD);
        }

        int bytes() {
            return quads * QUAD;
        }

        /**
         * Copies the layer's vertices to {@code dest} in the order they are drawn: grouped by facing, or (when an
         * unaligned quad lies within a grid step of a plane an aligned quad of that axis uses, where reordering could
         * change which of the two wins at equal depth) in the order they were made, as {@link CompactVertex#convert}
         * does. Returns the facing groups, or null for a layer kept in the order made (translucent ones always).
         */
        CompactVertex.Faces copyTo(long dest) {
            if (!grouped) {
                MemoryUtil.memCopy(ordered.address, dest, (long) quads * QUAD);
                return null;
            }
            int unaligned = groupQuads[CompactVertex.UNALIGNED];
            if (unaligned > 0 && unaligned < quads
                && CompactVertex.coplanar(quadGroup, quadPlane, flatAxis, flatLo, flatHi, quads)) {
                CompactVertex.UNGROUPED_LAYERS.incrementAndGet();
                int[] next = new int[CompactVertex.GROUPS];
                for (int q = 0; q < quads; q++) {
                    int g = quadGroup[q];
                    MemoryUtil.memCopy(group[g].address + (long) next[g]++ * QUAD, dest + (long) q * QUAD, QUAD);
                }
                return null;
            }
            int[] start = new int[CompactVertex.GROUPS + 1];
            long o = dest;
            for (int g = 0; g < CompactVertex.GROUPS; g++) {
                start[g + 1] = start[g] + groupQuads[g];
                long n = (long) groupQuads[g] * QUAD;
                if (n > 0) MemoryUtil.memCopy(group[g].address, o, n);
                o += n;
            }
            return new CompactVertex.Faces(start, planeMin.clone(), planeMax.clone());
        }

        void free() {
            if (group != null) for (NativeBuffer b : group) b.free();
            if (ordered != null) ordered.free();
        }
    }
}
