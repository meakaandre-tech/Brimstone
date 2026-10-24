package com.meakaandre.brimstone.terrain;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import com.mojang.renderpearl.api.vertex.VertexFormatElement;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import org.lwjgl.system.MemoryUtil;

/**
 * Brimstone's two terrain vertex formats, and the conversion of vanilla's vertices into one of them per layer. Every vertex
 * Brimstone draws gives the shader exactly the values vanilla's would; nothing is rounded.
 *
 * <p><b>Compact, 16 bytes</b> (vanilla's BLOCK vertex has 28), for layers whose every vertex it holds exactly:
 * <pre>
 *  0  u16 x, y, z        position in the section as (p + 8) * 2048, i.e. -8 to 24 blocks in 1/2048 steps
 *  6  u16 slot | bl << 8 slot: which of the region's 8x4x8 sections; bl: block light 0..240
 *  8  u8  r, g, b, sky   vanilla's baked colour (alpha must be 255) and sky light 0..240
 * 12  u16 u, v           atlas UV in 1/65536 steps; 65535 stands for exactly 1.0
 * </pre>
 * A vertex fits when its position and UV are on those grids (checked by decoding the stored value with the same float
 * operations the shader uses and comparing it with vanilla's float, bit for bit) and its alpha is 255. Light outside
 * 0..240 is stored clamped: the shader's lightmap lookup clamps to the same texel either way.
 *
 * <p><b>Wide, 32 bytes</b>, for every other layer (one with plant offsets, fluid surfaces, rotated model parts, UVs of atlases
 * wider than 4096): vanilla's 28 bytes unchanged, then u16 slot and two bytes of padding.
 */
public final class CompactVertex {
    public static final int BYTES = 16;
    public static final int WIDE_BYTES = 32;
    public static final int VANILLA_BYTES = 28;

    static final VertexFormat FORMAT = VertexFormat.builder(0)
        .addAttribute("BrimCell", GpuFormat.RGBA16_UINT)
        .addAttribute("BrimTint", GpuFormat.RGBA8_UNORM)
        .addAttribute("BrimTexel", GpuFormat.RG16_UINT)
        .build();

    static final VertexFormat WIDE_FORMAT = VertexFormat.builder(0)
        .addAttribute("BrimCorner", GpuFormat.RGB32_FLOAT)
        .addAttribute("BrimShade", GpuFormat.RGBA8_UNORM)
        .addAttribute("BrimAtlasUv", GpuFormat.RG32_FLOAT)
        .addAttribute("BrimLight", GpuFormat.RG16_SINT)
        .addAttribute("BrimSlot", GpuFormat.RG16_UINT)
        .build();

    /** Why a layer took the wide format (for the bench): position, UV or alpha. */
    static final int WHY_POSITION = 1, WHY_UV = 2, WHY_ALPHA = 4;

    /** One layer, converted: either compact or wide vertices (the other null), in a memAlloc'd buffer. */
    record Split(ByteBuffer compact, int compactVertices, ByteBuffer wide, int wideVertices, int why) {
        void free() {
            if (compact != null) MemoryUtil.memFree(compact);
            if (wide != null) MemoryUtil.memFree(wide);
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
        for (ChunkSectionLayer layer : new ChunkSectionLayer[] {ChunkSectionLayer.SOLID, ChunkSectionLayer.CUTOUT}) {
            if (!f.equals(layer.vertexFormat())) return layer + " does not use the BLOCK vertex format";
        }
        if (FORMAT.getVertexSize() != BYTES) return "compact format is " + FORMAT.getVertexSize() + " bytes";
        if (WIDE_FORMAT.getVertexSize() != WIDE_BYTES) return "wide format is " + WIDE_FORMAT.getVertexSize() + " bytes";
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
     * Converts a layer (vanilla's BLOCK vertices, position to limit of {@code src}): into the compact format if every
     * vertex of it round-trips exactly, otherwise all of it into the wide format. One format per layer keeps vanilla's
     * quad order, which coplanar quads at equal depth rely on. Null if the layer isn't whole quads.
     */
    static Split split(ByteBuffer src, int slot) {
        int bytes = src.remaining();
        if (bytes == 0 || bytes % (4 * VANILLA_BYTES) != 0) return null;
        int vertices = bytes / VANILLA_BYTES;
        long in = MemoryUtil.memAddress(src);
        int why = 0;
        for (int v = 0; v < vertices && why != (WHY_POSITION | WHY_UV | WHY_ALPHA); v++) why |= misfit(in + (long) v * VANILLA_BYTES);
        if (why == 0) {
            ByteBuffer compact = MemoryUtil.memAlloc(vertices * BYTES);
            long c = MemoryUtil.memAddress(compact);
            for (int v = 0; v < vertices; v++) packCompact(in + (long) v * VANILLA_BYTES, c + (long) v * BYTES, slot);
            return new Split(compact, vertices, null, 0, 0);
        }
        ByteBuffer wide = MemoryUtil.memAlloc(vertices * WIDE_BYTES);
        long w = MemoryUtil.memAddress(wide);
        for (int v = 0; v < vertices; v++, w += WIDE_BYTES) {
            MemoryUtil.memCopy(in + (long) v * VANILLA_BYTES, w, VANILLA_BYTES);
            MemoryUtil.memPutShort(w + 28, (short) slot);
            MemoryUtil.memPutShort(w + 30, (short) 0);
        }
        return new Split(null, 0, wide, vertices, why);
    }

    /** 0 if the vertex at {@code a} round-trips exactly through the compact format; else the WHY_ bits. */
    private static int misfit(long a) {
        int why = 0;
        for (int k = 0; k < 3; k++) {
            float p = MemoryUtil.memGetFloat(a + 4L * k);
            int s = positionStep(p);
            if (s < 0 || decodePosition(s) != p || Float.floatToRawIntBits(decodePosition(s)) != Float.floatToRawIntBits(p)) why |= WHY_POSITION;
        }
        for (int k = 0; k < 2; k++) {
            float t = MemoryUtil.memGetFloat(a + 16 + 4L * k);
            int s = uvStep(t);
            if (s < 0 || Float.floatToRawIntBits(decodeUv(s)) != Float.floatToRawIntBits(t)) why |= WHY_UV;
        }
        if (MemoryUtil.memGetByte(a + 15) != (byte) 0xFF) why |= WHY_ALPHA;
        return why;
    }

    private static void packCompact(long a, long o, int slot) {
        MemoryUtil.memPutShort(o, (short) positionStep(MemoryUtil.memGetFloat(a)));
        MemoryUtil.memPutShort(o + 2, (short) positionStep(MemoryUtil.memGetFloat(a + 4)));
        MemoryUtil.memPutShort(o + 4, (short) positionStep(MemoryUtil.memGetFloat(a + 8)));
        MemoryUtil.memPutShort(o + 6, (short) (slot | light(MemoryUtil.memGetShort(a + 24)) << 8));
        MemoryUtil.memPutByte(o + 8, MemoryUtil.memGetByte(a + 12));
        MemoryUtil.memPutByte(o + 9, MemoryUtil.memGetByte(a + 13));
        MemoryUtil.memPutByte(o + 10, MemoryUtil.memGetByte(a + 14));
        MemoryUtil.memPutByte(o + 11, (byte) light(MemoryUtil.memGetShort(a + 26)));
        MemoryUtil.memPutShort(o + 12, (short) uvStep(MemoryUtil.memGetFloat(a + 16)));
        MemoryUtil.memPutShort(o + 14, (short) uvStep(MemoryUtil.memGetFloat(a + 20)));
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

    /** Nearest 1/65536 step (65535 only for exactly 1.0), or -1 outside 0..1. */
    static int uvStep(float t) {
        if (t == 1.0f) return 65535;
        if (!(t >= 0f && t < 1f)) return -1;
        return Math.min(65534, Math.round(t * 65536f));
    }

    /** What the shader computes from a stored UV step. */
    static float decodeUv(int step) {
        return step == 65535 ? 1.0f : step * (1f / 65536f);
    }

    private static int light(short s) {
        return Math.max(0, Math.min(240, s));
    }
}
