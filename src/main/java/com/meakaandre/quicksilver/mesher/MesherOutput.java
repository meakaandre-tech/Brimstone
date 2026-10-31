package com.meakaandre.quicksilver.mesher;

import com.meakaandre.quicksilver.terrain.CompactVertex;

/**
 * What the last section a thread meshed left for the moment vanilla hands its layers to the GPU upload (on the same
 * thread, right after the compile): each layer's 16-byte vertices and how they are grouped, so the terrain module takes
 * them as they are. A layer is matched by where vanilla's mesh object keeps its bytes, so vanilla's own meshes (from a
 * section Quicksilver handed to vanilla's compiler) never match.
 */
final class MesherOutput {
    static final class LayerOut {
        boolean present, taken;
        /** Where vanilla's mesh for this layer keeps its vertex bytes (what the upload hook receives). */
        long vertexAddress;
        int vertexBytes;
        /** The 16-byte vertices, ready to upload: {@link #owned}'s bytes. */
        long data;
        /** The buffer holding them (memAlloc'd): handed to the terrain module when taken, freed here otherwise. */
        java.nio.ByteBuffer owned;
        int bytes;
        int vertices, positionsRounded, uvsRounded;
        CompactVertex.Faces faces;
        float[] centroids;

        void clear() {
            if (owned != null) org.lwjgl.system.MemoryUtil.memFree(owned);
            owned = null;
            present = taken = false;
            vertexAddress = data = 0;
            vertexBytes = bytes = vertices = positionsRounded = uvsRounded = 0;
            faces = null;
            centroids = null;
        }
    }

    final LayerOut[] layers = {new LayerOut(), new LayerOut(), new LayerOut()};
    /** The writer that made them (CI's check reads which block each quad came from). */
    SectionWriter writer;

    /** Which block Quicksilver made quad {@code stored} of layer {@code li} from (section-relative coordinates). */
    String origin(int li, int stored) {
        return writer == null ? "?" : writer.layers[li].originOf(stored, layers[li].faces);
    }
    boolean active;
    Object spriteTable;
    long[] found;

    void clear() {
        active = false;
        spriteTable = null;
        found = null;
        for (LayerOut l : layers) l.clear();
    }
}
