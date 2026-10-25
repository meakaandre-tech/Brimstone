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

    /** One layer, converted, in a memAlloc'd buffer; how many of its vertices had a position or UV rounded. */
    record Converted(ByteBuffer data, int vertices, int positionsRounded, int uvsRounded) {
        void free() {
            MemoryUtil.memFree(data);
        }
    }

    private CompactVertex() {
    }

    /** Null if vanilla's terrain vertex is what this transcoder reads; otherwise what differs. */
    static String checkVanillaFormat() {
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
        int bytes = src.remaining();
        if (bytes == 0 || bytes % (4 * VANILLA_BYTES) != 0) return null;
        int vertices = bytes / VANILLA_BYTES;
        long in = MemoryUtil.memAddress(src);
        for (int v = 0; v < vertices; v++) if (!storable(in + (long) v * VANILLA_BYTES)) return null;
        ByteBuffer out = MemoryUtil.memAlloc(vertices * BYTES);
        long o = MemoryUtil.memAddress(out);
        int positions = 0, uvs = 0;
        for (int v = 0; v < vertices; v++) {
            int r = pack(in + (long) v * VANILLA_BYTES, o + (long) v * BYTES, slot);
            if ((r & 1) != 0) positions++;
            if ((r & 2) != 0) uvs++;
        }
        return new Converted(out, vertices, positions, uvs);
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
