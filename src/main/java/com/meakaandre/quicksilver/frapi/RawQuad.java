package com.meakaandre.quicksilver.frapi;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;

/**
 * Package 4c: Quicksilver's mesher reads a finished quad of Quicksilver's renderer straight from its backing array
 * instead of copying it through the {@link QuadView} getters vertex by vertex. Only for quads of this renderer
 * ({@link #of}); the layout is {@link QuadData}'s.
 */
public final class RawQuad {
    private RawQuad() {
    }

    /** Offsets of a vertex's fields from the vertex's start, and the distance between vertices. */
    public static final int X = QuadData.X, Y = QuadData.Y, Z = QuadData.Z, U = QuadData.U, V = QuadData.V, COLOR = QuadData.COLOR,
        LIGHT = QuadData.LIGHT, VERTEX_STRIDE = QuadData.VERTEX_STRIDE;

    /** Whether {@code q} is one of this renderer's quads (then {@link #data} and {@link #firstVertex} apply). */
    public static boolean of(QuadView q) {
        return q instanceof QuadViewImpl;
    }

    public static int[] data(QuadView q) {
        return ((QuadViewImpl) q).data;
    }

    /** Index of the first vertex's first field in {@link #data}. */
    public static int firstVertex(QuadView q) {
        return QuadData.vertex(((QuadViewImpl) q).base, 0);
    }
}
