package com.meakaandre.brimstone.terrain;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import com.mojang.renderpearl.api.vertex.VertexFormatElement;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import org.lwjgl.system.MemoryUtil;

/**
 * Brimstone's terrain vertex: 16 bytes where vanilla's BLOCK vertex has 28.
 *
 * <pre>
 *  0  u16 x, u16 y, u16 z   position in the section, (p + 8) * 2048: -8 to 24 blocks in 1/2048 steps
 *  6  u16 slot | light << 8  slot: which of the region's 8x4x8 sections; light: block light 0..240 (vanilla UV2.x)
 *  8  u8  r, g, b           vanilla's baked colour (AO, shade, tint)
 * 11  u8  sky light         0..240, vanilla's UV2.y
 * 12  u16 u, u16 v          atlas UV in 1/65536, 65535 meaning 1.0 (exact on 1/16-texel steps of atlases up to 4096)
 *
 * Light outside 0..240 is clamped: the terrain shader clamps lightmap coordinates to that range anyway.
 * </pre>
 *
 * Positions on the 1/2048-block grid (every model position on vanilla's 1/16-pixel grid) and UVs on the
 * 1/65536 grid come back bit-exact in the shader. A layer with any vertex that would not come back exactly (random
 * plant offsets, rotated models, UVs off the grid), or that doesn't fit (alpha other than 255, positions outside the
 * range), is not transcoded: vanilla keeps it and draws it as before. The bench counts both.
 */
public final class CompactVertex {
    public static final int BYTES = 16;
    public static final int VANILLA_BYTES = 28;

    static final VertexFormat FORMAT = VertexFormat.builder(0)
        .addAttribute("BrimPosition", GpuFormat.RGBA16_UINT)
        .addAttribute("BrimColor", GpuFormat.RGBA8_UNORM)
        .addAttribute("BrimUv", GpuFormat.RG16_UINT)
        .build();

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
     * Turns vanilla's BLOCK vertices (position to limit of {@code src}) into compact ones, in a new buffer from
     * {@link MemoryUtil#memAlloc} that the caller frees. Null if any vertex doesn't fit; nothing is allocated then.
     */
    static ByteBuffer transcode(ByteBuffer src, int section) {
        int bytes = src.remaining();
        if (bytes == 0 || bytes % VANILLA_BYTES != 0) return null;
        int count = bytes / VANILLA_BYTES;
        if (count % 4 != 0) return null;
        long in = MemoryUtil.memAddress(src);
        ByteBuffer out = MemoryUtil.memAlloc(count * BYTES);
        long o = MemoryUtil.memAddress(out);
        for (int i = 0; i < count; i++, in += VANILLA_BYTES, o += BYTES) {
            float fx = MemoryUtil.memGetFloat(in), fy = MemoryUtil.memGetFloat(in + 4), fz = MemoryUtil.memGetFloat(in + 8);
            float fu = MemoryUtil.memGetFloat(in + 16), fv = MemoryUtil.memGetFloat(in + 20);
            int x = position(fx), y = position(fy), z = position(fz);
            int u = uv(fu), v = uv(fv);
            if ((x | y | z | u | v) < 0 || MemoryUtil.memGetByte(in + 15) != (byte) 0xFF) {
                MemoryUtil.memFree(out);
                TerrainStats.KEPT_RANGE.incrementAndGet();
                return null;
            }
            if (x / 2048f - 8f != fx || y / 2048f - 8f != fy || z / 2048f - 8f != fz
                || decodeUv(u) != fu || decodeUv(v) != fv) {
                // not on the grid (a random plant offset, a rotated model): vanilla keeps this layer, exactly
                MemoryUtil.memFree(out);
                TerrainStats.KEPT_PRECISION.incrementAndGet();
                return null;
            }
            int block = light(MemoryUtil.memGetShort(in + 24));
            int sky = light(MemoryUtil.memGetShort(in + 26));
            MemoryUtil.memPutShort(o, (short) x);
            MemoryUtil.memPutShort(o + 2, (short) y);
            MemoryUtil.memPutShort(o + 4, (short) z);
            MemoryUtil.memPutShort(o + 6, (short) (section | block << 8));
            MemoryUtil.memPutByte(o + 8, MemoryUtil.memGetByte(in + 12));
            MemoryUtil.memPutByte(o + 9, MemoryUtil.memGetByte(in + 13));
            MemoryUtil.memPutByte(o + 10, MemoryUtil.memGetByte(in + 14));
            MemoryUtil.memPutByte(o + 11, (byte) sky);
            MemoryUtil.memPutShort(o + 12, (short) u);
            MemoryUtil.memPutShort(o + 14, (short) v);
        }
        TerrainStats.VERTICES.addAndGet(count);
        return out;
    }

    /** -1 if outside -8..24 blocks. */
    private static int position(float p) {
        if (!(p >= -8f && p < 24f)) return -1;
        return Math.min(65535, Math.round((p + 8f) * 2048f));
    }

    /**
     * Atlas UV in 1/65536 steps; 65535 stands for exactly 1.0 (the right or bottom edge of the atlas), so a value
     * that would round to 65535 without being 1.0 can't be stored and the layer stays with vanilla (-1). Outside
     * 0..1 also -1.
     */
    private static int uv(float t) {
        if (t == 1.0f) return 65535;
        if (!(t >= 0f && t < 1f)) return -1;
        int k = Math.round(t * 65536f);
        return k >= 65535 ? -1 : k;
    }

    /** What the shader reads back for a stored UV. */
    static float decodeUv(int k) {
        return k == 65535 ? 1.0f : k / 65536f;
    }

    /** The terrain shader clamps light coordinates to 0..240 (lightmap texel centres), so clamping here is exact. */
    private static int light(short s) {
        return Math.max(0, Math.min(240, s));
    }
}
