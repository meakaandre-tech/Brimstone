package com.meakaandre.brimstone.terrain;

import com.meakaandre.brimstone.BrimstoneClient;
import com.mojang.renderpearl.api.pipeline.IndexType;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;
import java.util.List;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.chunk.TranslucencyPointOfView;
import net.minecraft.core.SectionPos;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.MemoryUtil;

/**
 * CI's per-frame check of Brimstone's translucent draws (debug property {@code brimstone.debug.verifyTranslucency}, set
 * in every Brimstone CI run; off otherwise, and then it keeps nothing). Each frame it checks, against what vanilla
 * would draw for the same camera:
 * <ul>
 * <li><b>section order</b>: Brimstone's translucent draws, as batched into its draw calls, follow vanilla's order:
 * vanilla's visible-section list, reversed (back to front) when vanilla sorts, i.e. when Improved Transparency is
 * off;</li>
 * <li><b>latest sort</b>: the index list Brimstone draws for each section comes from the camera position of the
 * sort vanilla last made for that mesh (vanilla's own record of it, the mesh's point of view), unless a newer list
 * is already on its way in, as it would be for vanilla;</li>
 * <li><b>back to front</b>: each index list Brimstone wrote names every quad of the layer once, ordered by
 * non-increasing distance from the camera position of its sort (quad centre = midpoint of its first and third
 * corner, as vanilla sorts; a small tolerance for float rounding; ties are not checked: their order is vanilla's own
 * bytes).</li>
 * </ul>
 * Any miss logs a WARN line starting "Brimstone translucency check:" (the first 20) and is counted in the bench's
 * translucency block; CI fails on either.
 */
public final class TranslucencyCheck {
    public static final boolean ENABLED = Boolean.getBoolean("brimstone.debug.verifyTranslucency");

    /** Where an index list came from: the camera of vanilla's sort and the quads' centres (section-relative). */
    record Sorted(float[] centroids, byte[] indices, IndexType type, float camX, float camY, float camZ, TranslucencyPointOfView pov) {
    }

    /** The section and camera position vanilla last built a sorting for on this thread. */
    private static final ThreadLocal<Object[]> CAMERA = ThreadLocal.withInitial(() -> new Object[2]);
    private static int warnings;

    private TranslucencyCheck() {
    }

    /** {@code RenderSection.createVertexSorting}: vanilla is about to sort a section's translucent quads for this camera. */
    public static void sortCamera(SectionPos section, Vec3 camera) {
        if (!ENABLED || section == null || camera == null) return;
        Object[] c = CAMERA.get();
        c[0] = section.asLong();
        c[1] = camera;
    }

    /** Worker thread, as a translucent layer or its new index list is taken: what the check needs later. Null when off. */
    static Sorted sortedFor(SectionRenderDispatcher.RenderSection section, ByteBuffer vertices, ByteBuffer indices, IndexType type) {
        if (!ENABLED) return null;
        long node = section.getSectionNode();
        Object[] c = CAMERA.get();
        byte[] idx = new byte[indices.remaining()];
        MemoryUtil.memByteBuffer(MemoryUtil.memAddress(indices), idx.length).get(idx);
        float[] centroids = vertices == null ? null : centroids(vertices);
        if (!(c[0] instanceof Long n) || n != node || !(c[1] instanceof Vec3 cam)) {
            return new Sorted(centroids, idx, type, Float.NaN, Float.NaN, Float.NaN, null);
        }
        // the same section-relative camera position vanilla's sorting is built from
        float x = (float) (cam.x - SectionPos.sectionToBlockCoord(SectionPos.x(node)));
        float y = (float) (cam.y - SectionPos.sectionToBlockCoord(SectionPos.y(node)));
        float z = (float) (cam.z - SectionPos.sectionToBlockCoord(SectionPos.z(node)));
        return new Sorted(centroids, idx, type, x, y, z, TranslucencyPointOfView.of(cam, node));
    }

    /** A new index list for a layer whose quads (centres) are known from when it was built. */
    static Sorted resorted(Sorted old, Sorted now) {
        if (old == null || now == null) return now;
        return new Sorted(old.centroids, now.indices, now.type != null ? now.type : old.type, now.camX, now.camY, now.camZ, now.pov);
    }

    /** Quad centres from vanilla's BLOCK vertices: midpoint of each quad's first and third corner. */
    private static float[] centroids(ByteBuffer vertices) {
        int stride = CompactVertex.VANILLA_BYTES;
        int quads = vertices.remaining() / stride / 4;
        long a = MemoryUtil.memAddress(vertices);
        float[] out = new float[quads * 3];
        for (int q = 0; q < quads; q++) {
            long v0 = a + (long) q * 4 * stride, v2 = v0 + 2L * stride;
            for (int k = 0; k < 3; k++) out[q * 3 + k] = (MemoryUtil.memGetFloat(v0 + 4L * k) + MemoryUtil.memGetFloat(v2 + 4L * k)) * 0.5f;
        }
        return out;
    }

    /**
     * Render thread, under the store's lock, as this frame's translucent draws are prepared. {@code visible}: vanilla's
     * visible-section list for this frame (the list vanilla's own draws are built from); {@code drawn}: the meshes and
     * entries in the order Brimstone's draw calls will draw them, taken from those draw calls.
     */
    static void frame(List<SectionRenderDispatcher.RenderSection> visible, boolean sorted, List<CompiledSectionMesh> drawnMeshes, List<TerrainStore.Entry> drawn) {
        TranslucencyStats.checkFrame();
        // vanilla's order, worked out on its own: every visible section whose translucent layer Brimstone holds
        List<CompiledSectionMesh> expected = new java.util.ArrayList<>();
        for (SectionRenderDispatcher.RenderSection section : visible) {
            if (!(section.getSectionMesh() instanceof CompiledSectionMesh m)) continue;
            TerrainStore.Entry[] es = TerrainStore.ENTRIES.get(m);
            if (es != null && es[TerrainStore.TRANSLUCENT] != null && es[TerrainStore.TRANSLUCENT].index != null) expected.add(m);
        }
        int n = expected.size();
        boolean order = n == drawnMeshes.size();
        for (int i = 0; order && i < n; i++) order = expected.get(sorted ? n - 1 - i : i) == drawnMeshes.get(i);
        if (!order) violation("section order: Brimstone draws " + drawnMeshes.size() + " translucent section layers in an order other than vanilla's ("
            + n + " expected, " + (sorted ? "back to front" : "vanilla's list order") + ")");
        for (int i = 0; i < drawn.size(); i++) {
            TerrainStore.Entry e = drawn.get(i);
            CompiledSectionMesh mesh = drawnMeshes.get(i);
            Sorted s = e.sorted;
            TranslucencyStats.entryChecked();
            if (s == null || s.pov == null) {
                TranslucencyStats.entryUnchecked();
                continue;
            }
            if (mesh.isDifferentPointOfView(s.pov) && !TerrainStore.indicesPending(mesh)) {
                violation("latest sort: section " + e.originX + " " + e.originY + " " + e.originZ + " is drawn with an index list from an older sort than vanilla's latest");
            }
            if (!e.sortedChecked) {
                e.sortedChecked = true;
                String bad = backToFront(e, s);
                if (bad != null) violation("back to front: section " + e.originX + " " + e.originY + " " + e.originZ + ": " + bad);
            }
        }
    }

    private static String backToFront(TerrainStore.Entry e, Sorted s) {
        if (s.centroids == null || s.type == null) {
            TranslucencyStats.entryUnchecked();
            return null;
        }
        int quads = s.centroids.length / 3;
        int count = s.indices.length / s.type.bytes;
        if (count != quads * 6 || e.index == null || e.index.count != count) return count + " indices for " + quads + " quads";
        ByteBuffer b = ByteBuffer.wrap(s.indices).order(ByteOrder.nativeOrder());
        BitSet seen = new BitSet(quads);
        float prev = Float.POSITIVE_INFINITY;
        for (int q = 0; q < quads; q++) {
            int quad = -1;
            for (int k = 0; k < 6; k++) {
                int i = q * 6 + k;
                int v = s.type == IndexType.SHORT ? b.getShort(i * 2) & 0xFFFF : b.getInt(i * 4);
                if (quad < 0) quad = v >> 2;
                if (v >> 2 != quad || quad >= quads) return "indices " + (q * 6) + ".." + (q * 6 + 5) + " do not name one quad of the layer";
            }
            if (seen.get(quad)) return "quad " + quad + " is drawn twice";
            seen.set(quad);
            float dx = s.centroids[quad * 3] - s.camX, dy = s.centroids[quad * 3 + 1] - s.camY, dz = s.centroids[quad * 3 + 2] - s.camZ;
            float d = dx * dx + dy * dy + dz * dz;
            if (d > prev + 1e-3f * Math.max(1f, prev)) return "quad " + quad + " (distance squared " + d + ") is drawn after a nearer one (" + prev + ")";
            prev = d;
        }
        return null;
    }

    private static void violation(String what) {
        TranslucencyStats.violation();
        if (warnings < 20) {
            warnings++;
            BrimstoneClient.LOG.warn("Brimstone translucency check: {}", what);
        }
    }
}
