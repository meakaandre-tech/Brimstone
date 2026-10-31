package com.meakaandre.quicksilver.mesher;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.ARGB;

/**
 * A vertex consumer that writes into one layer of the section being meshed, in the 16-byte vertex: for vertices made
 * by vanilla code Quicksilver hands its output to (a mod's fluid handler through vanilla's fluid renderer). It keeps
 * what vanilla's terrain vertex buffer keeps (position, colour, UV, packed light; normals and the overlay are not part
 * of the terrain vertex) and ends a vertex when the next one starts, as vanilla's buffer does; every fourth vertex
 * completes a quad. {@link #flush} ends the last vertex after a block.
 */
final class CompactConsumer implements VertexConsumer {
    private final SectionWriter w;
    private final int layer;
    /** Vertices of the current quad already ended, and whether one is being written. */
    private int done;
    private boolean open;
    private float x, y, z, u, v;
    private int color = -1, light;

    CompactConsumer(SectionWriter w, int layer) {
        this.w = w;
        this.layer = layer;
    }

    private void end() {
        if (!open) return;
        open = false;
        int c = done;
        w.x[c] = x;
        w.y[c] = y;
        w.z[c] = z;
        w.color[c] = color;
        w.u[c] = u;
        w.v[c] = v;
        w.light[c] = light;
        if (++done == 4) {
            done = 0;
            w.emit(layer);
        }
    }

    /** Ends the vertex being written; a quad left unfinished makes the section unstorable here. */
    void flush() {
        end();
        if (done != 0) {
            done = 0;
            if (w.unstorable == null) w.unstorable = "a fluid handler's vertices that are not whole quads";
        }
    }

    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        end();
        open = true;
        this.x = x;
        this.y = y;
        this.z = z;
        color = -1;
        u = 0;
        v = 0;
        light = 0;
        return this;
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int a) {
        color = ARGB.color(a, r, g, b);
        return this;
    }

    @Override
    public VertexConsumer setColor(int argb) {
        color = argb;
        return this;
    }

    @Override
    public VertexConsumer setUv(float u, float v) {
        this.u = u;
        this.v = v;
        return this;
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        return this;
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        light = (u & 0xFFFF) | (v & 0xFFFF) << 16;
        return this;
    }

    @Override
    public VertexConsumer setUv3(float u, float v) {
        return this;
    }

    @Override
    public VertexConsumer setNormal(float x, float y, float z) {
        return this;
    }

    @Override
    public VertexConsumer setLineWidth(float width) {
        return this;
    }
}
