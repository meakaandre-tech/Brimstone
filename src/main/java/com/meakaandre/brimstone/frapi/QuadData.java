package com.meakaandre.brimstone.frapi;

import com.meakaandre.brimstone.BrimstoneClient;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.renderer.rendertype.RenderType;

/**
 * Brimstone's encoding of one quad of the Fabric Renderer API (package 4): a run of {@link #STRIDE} ints in an
 * {@code int[]}, used by emitters (one quad) and meshes (many quads back to back).
 *
 * <p>Header: {@link #BITS} (packed properties, below), {@link #TINT}, {@link #TAG}, three render type ids
 * ({@link #ITEM_RT}, {@link #GLINT_RT}, {@link #GLINT_SPECIAL_RT}: indexes into {@link #renderType}), {@link #NORMALS}
 * (which vertices carry their own normal, bits 0-3), {@link #FACE_NORMAL} (the packed face normal). Then four
 * vertices of {@link #VERTEX_STRIDE} ints: x, y, z, u, v (float bits), colour (ARGB), lightmap, normal (three signed
 * bytes, x lowest).
 */
final class QuadData {
    static final int BITS = 0, TINT = 1, TAG = 2, ITEM_RT = 3, GLINT_RT = 4, GLINT_SPECIAL_RT = 5, NORMALS = 6, FACE_NORMAL = 7;
    static final int HEADER = 8;
    static final int X = 0, Y = 1, Z = 2, U = 3, V = 4, COLOR = 5, LIGHT = 6, NORMAL = 7;
    static final int VERTEX_STRIDE = 8;
    static final int STRIDE = HEADER + 4 * VERTEX_STRIDE;

    // BITS: each field's shift and mask (directions as 3D data value 0-5, 6 = none)
    static final int NOMINAL_SHIFT = 0, CULL_SHIFT = 3, SHADE_DIR_SHIFT = 6, LAYER_SHIFT = 9, AO_SHIFT = 13, FOIL_SHIFT = 15;
    static final int DIR_MASK = 7, LAYER_MASK = 15, AO_MASK = 3, FOIL_MASK = 3;
    static final int EMISSIVE = 1 << 17, ANIMATED = 1 << 18, SHADE_VANILLA = 1 << 19, ATLAS_ITEM = 1 << 20;
    // the quad's geometry as worked out last (light face, shape flags), kept with the packed face normal (FACE_NORMAL)
    static final int LIGHT_FACE_SHIFT = 21, GEOMETRY_SHIFT = 24, GEOMETRY_MASK = 7;
    static final int NO_DIR = 6;

    /** Render types by id; replaced (never changed in place) when one is added, so reads need no lock. */
    private static volatile RenderType[] renderTypes = new RenderType[0];
    private static final Map<RenderType, Integer> RENDER_TYPE_IDS = new IdentityHashMap<>();
    private static final int MANY_RENDER_TYPES = 32;

    private QuadData() {
    }

    /** A small id for a render type (the API allows only a dozen item sheets), stable for the session. */
    static int renderTypeId(RenderType type) {
        RenderType[] known = renderTypes;
        for (int i = 0; i < known.length; i++) if (known[i] == type) return i;
        synchronized (RENDER_TYPE_IDS) {
            Integer id = RENDER_TYPE_IDS.get(type);
            if (id == null) {
                RenderType[] grown = java.util.Arrays.copyOf(renderTypes, renderTypes.length + 1);
                id = grown.length - 1;
                grown[id] = type;
                RENDER_TYPE_IDS.put(type, id);
                renderTypes = grown;
                if (id == MANY_RENDER_TYPES) {
                    // the API allows only Sheets' item render types; a mod making its own per call would grow this forever
                    BrimstoneClient.LOG.warn("Brimstone renderer: over {} distinct item render types set on API quads (a mod may be"
                        + " creating them per call; each new one costs memory and time)", MANY_RENDER_TYPES);
                }
            }
            return id;
        }
    }

    static RenderType renderType(int id) {
        return renderTypes[id];
    }

    static int vertex(int base, int vertex) {
        return base + HEADER + vertex * VERTEX_STRIDE;
    }

    static int packNormal(float x, float y, float z) {
        return (pack(x) & 0xFF) | (pack(y) & 0xFF) << 8 | (pack(z) & 0xFF) << 16;
    }

    private static int pack(float f) {
        return (int) (Math.max(-1f, Math.min(1f, f)) * 127f);
    }

    static float unpackNormal(int packed, int component) {
        return ((byte) (packed >> (8 * component))) * (1.0f / 127.0f);
    }
}
