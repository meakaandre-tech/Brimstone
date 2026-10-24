package com.meakaandre.brimstone.visibility;

import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ByteOpenHashMap;
import java.util.List;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * Brimstone's occlusion test: proves that nothing inside a box can be seen from the camera. Conservative by
 * construction: it only answers "hidden" when every line of sight from the camera to every point of the box ends on a
 * drawn, opaque block face first.
 *
 * <p>The proof is a slab: a plane {@code axis = k} between the camera and the box (both strictly on either side) such
 * that every line from the camera to the box crosses that plane inside a rectangle whose every cell, on the far side of
 * the plane, is
 * <ul>
 * <li>a full opaque cube: a vanilla block that vanilla itself treats as opaque for its cave graph and face culling
 * ({@link BlockState#isSolidRender()}), drawn as a plain model, with no random offset;</li>
 * <li>with its face toward the camera actually drawn: vanilla's own face culling ({@link Block#shouldRenderFace}) keeps
 * that face against the cell in front of it;</li>
 * <li>in a section vanilla draws this frame (in its visible list), fully faded in, whose mesh was built after the last
 * change to anything that mesh depends on ({@link SectionChanges}), so the drawn face is the one the world has now.</li>
 * </ul>
 * A line entering such a cell through that face meets the face, which is drawn, opaque, and nearer than the box (the box
 * lies beyond the plane), so the depth test hides whatever of the box lies on that line.
 *
 * <p>The camera is taken as a small box ({@link #EYE_MARGIN} on every side) so view bobbing, which moves the eye a little
 * in the view matrix, stays inside it; the rectangle is the bound of all segments from that box to the target box (the
 * crossing point is monotone in every corner coordinate, so the extremes are at corners). Candidate planes come from the
 * segment between the two centres: if a plane can prove anything, the centre segment crosses it in one of its cells, so
 * only planes where the centre segment enters such a cell are tried in full.
 *
 * <p>Render thread only. A new instance of the per-frame state is set up by {@link #beginFrame}.
 */
final class Occlusion implements Ray.Visitor {
    /** Half the size of the box around the camera (blocks). View bobbing moves the eye by about 0.1 at most. */
    static final double EYE_MARGIN = 0.25;
    private static final double EPS = 1e-4;
    /** A rectangle with more cells than this is not tried (too costly; conservative). */
    private static final int MAX_CELLS = 4096;

    private static final Direction[] TOWARD_NEGATIVE = {Direction.WEST, Direction.DOWN, Direction.NORTH};
    private static final Direction[] TOWARD_POSITIVE = {Direction.EAST, Direction.UP, Direction.SOUTH};

    /** Per block state: 1 opaque full cube Brimstone trusts, 2 not. Kept for the session (block states don't change). */
    private static final Reference2ByteOpenHashMap<BlockState> OCCLUDERS = new Reference2ByteOpenHashMap<>();

    private ClientLevel level;
    private ViewArea viewArea;
    private int frame;
    private long nowMs, fadeMs;
    /** Per section this frame: 1 its blocks may occlude, 2 not. */
    private final Long2ByteOpenHashMap eligible = new Long2ByteOpenHashMap();
    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    private final BlockPos.MutableBlockPos pos2 = new BlockPos.MutableBlockPos();

    // the current test
    private final double[] eyeMin = new double[3], eyeMax = new double[3];
    private final double[] boxMin = new double[3], boxMax = new double[3];
    private final int[] cell = new int[3];
    private final int[] near = new int[3];
    int planesTried;

    /** Full rectangle checks per object at most (each is up to {@link #MAX_CELLS} cells); past it, not hidden. */
    private static final int MAX_PLANES = 32;

    /** Forgets the per-state answers (resource reload). */
    static void clearCache() {
        OCCLUDERS.clear();
    }

    /** A block state that is a full opaque cube Brimstone trusts to hide what is behind it. */
    static boolean occluder(BlockState state) {
        byte v = OCCLUDERS.getByte(state);
        if (v == 0) {
            boolean ok = state.isSolidRender() && state.getRenderShape() == RenderShape.MODEL && !state.hasOffsetFunction()
                && "minecraft".equals(BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace());
            v = ok ? (byte) 1 : (byte) 2;
            OCCLUDERS.put(state, v);
        }
        return v == 1;
    }

    /** Render thread, once a frame, after vanilla found this frame's visible sections. */
    void beginFrame(ClientLevel level, ViewArea viewArea, List<SectionRenderDispatcher.RenderSection> visible, int frame, long fadeMs) {
        this.level = level;
        this.viewArea = viewArea;
        this.frame = frame;
        this.fadeMs = fadeMs;
        this.nowMs = net.minecraft.util.Util.getMillis();
        eligible.clear();
        for (int i = 0, n = visible.size(); i < n; i++) ((RenderSectionStamp) visible.get(i)).brimstone$setVisibleFrame(frame);
    }

    /** True if nothing in {@code box} can be seen from the camera at (ex, ey, ez). */
    boolean hidden(double ex, double ey, double ez, AABB box) {
        eyeMin[0] = ex - EYE_MARGIN;
        eyeMin[1] = ey - EYE_MARGIN;
        eyeMin[2] = ez - EYE_MARGIN;
        eyeMax[0] = ex + EYE_MARGIN;
        eyeMax[1] = ey + EYE_MARGIN;
        eyeMax[2] = ez + EYE_MARGIN;
        boxMin[0] = box.minX;
        boxMin[1] = box.minY;
        boxMin[2] = box.minZ;
        boxMax[0] = box.maxX;
        boxMax[1] = box.maxY;
        boxMax[2] = box.maxZ;
        for (int a = 0; a < 3; a++) if (!Double.isFinite(boxMin[a]) || !Double.isFinite(boxMax[a])) return false;
        planesTried = 0;
        return Ray.walk(ex, ey, ez, (box.minX + box.maxX) * 0.5, (box.minY + box.maxY) * 0.5, (box.minZ + box.maxZ) * 0.5, this);
    }

    /** A crossing of the centre segment: try the plane if it separates the two boxes and its centre cell blocks. */
    @Override
    public boolean cross(int axis, int k, int dir, int px, int py, int pz) {
        if (dir > 0) {
            if (!(eyeMax[axis] <= k && k < boxMin[axis])) return false;
        } else if (!(eyeMin[axis] >= k && k > boxMax[axis])) {
            return false;
        }
        near[0] = px;
        near[1] = py;
        near[2] = pz;
        cell[0] = px;
        cell[1] = py;
        cell[2] = pz;
        cell[axis] += dir;
        if (!blocks(cell[0], cell[1], cell[2], near[0], near[1], near[2], axis, dir)) return false;
        if (++planesTried > MAX_PLANES) return false;
        return covered(axis, k, dir);
    }

    /** Every cell of the plane's rectangle blocks. */
    private boolean covered(int a, int k, int dir) {
        int u = a == 0 ? 1 : 0;
        int w = a == 2 ? 1 : 2;
        double uLo = Double.POSITIVE_INFINITY, uHi = Double.NEGATIVE_INFINITY;
        double wLo = Double.POSITIVE_INFINITY, wHi = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 2; i++) {
            double ea = i == 0 ? eyeMin[a] : eyeMax[a];
            for (int j = 0; j < 2; j++) {
                double ta = j == 0 ? boxMin[a] : boxMax[a];
                double s = (k - ea) / (ta - ea);
                // the crossing point grows with both end points' other coordinates: min with both minima, max with both maxima
                double p = eyeMin[u] + s * (boxMin[u] - eyeMin[u]);
                uLo = Math.min(uLo, p);
                p = eyeMax[u] + s * (boxMax[u] - eyeMax[u]);
                uHi = Math.max(uHi, p);
                p = eyeMin[w] + s * (boxMin[w] - eyeMin[w]);
                wLo = Math.min(wLo, p);
                p = eyeMax[w] + s * (boxMax[w] - eyeMax[w]);
                wHi = Math.max(wHi, p);
            }
        }
        int u0 = (int) Math.floor(uLo - EPS), u1 = (int) Math.floor(uHi + EPS);
        int w0 = (int) Math.floor(wLo - EPS), w1 = (int) Math.floor(wHi + EPS);
        if ((long) (u1 - u0 + 1) * (w1 - w0 + 1) > MAX_CELLS) return false;
        int layer = dir > 0 ? k : k - 1;
        int nearLayer = dir > 0 ? k - 1 : k;
        for (int iu = u0; iu <= u1; iu++) {
            for (int iw = w0; iw <= w1; iw++) {
                cell[a] = layer;
                cell[u] = iu;
                cell[w] = iw;
                near[a] = nearLayer;
                near[u] = iu;
                near[w] = iw;
                if (!blocks(cell[0], cell[1], cell[2], near[0], near[1], near[2], a, dir)) return false;
            }
        }
        return true;
    }

    /**
     * The cell (cx, cy, cz) is an opaque full cube in a trusted section and its face toward the neighbour (nx, ny, nz),
     * which lies on the camera's side along {@code axis}, is drawn.
     */
    private boolean blocks(int cx, int cy, int cz, int nx, int ny, int nz, int axis, int dir) {
        if (level.isOutsideBuildHeight(cy)) return false;
        if (!sectionTrusted(SectionPos.asLong(cx >> 4, cy >> 4, cz >> 4), cx, cy, cz)) return false;
        BlockState c = level.getBlockState(pos.set(cx, cy, cz));
        if (!occluder(c)) return false;
        BlockState n = level.getBlockState(pos2.set(nx, ny, nz));
        return Block.shouldRenderFace(c, n, dir > 0 ? TOWARD_NEGATIVE[axis] : TOWARD_POSITIVE[axis]);
    }

    /** The section is drawn this frame, fully faded in, and its mesh shows the blocks the world has now. */
    private boolean sectionTrusted(long node, int bx, int by, int bz) {
        byte v = eligible.get(node);
        if (v == 0) {
            v = checkSection(node, bx, by, bz) ? (byte) 1 : (byte) 2;
            eligible.put(node, v);
        }
        return v == 1;
    }

    private boolean checkSection(long node, int bx, int by, int bz) {
        SectionRenderDispatcher.RenderSection rs = viewArea.getRenderSectionAt(pos.set(bx, by, bz));
        if (rs == null || rs.getSectionNode() != node) return false;
        if (((RenderSectionStamp) rs).brimstone$visibleFrame() != frame) return false;
        SectionMesh mesh = rs.getSectionMesh();
        if (!(mesh instanceof CompiledSectionMesh) || mesh == CompiledSectionMesh.UNCOMPILED || mesh == CompiledSectionMesh.EMPTY) return false;
        if (mesh.getCompileTaskStartTime() <= SectionChanges.lastChange(node)) return false;
        return rs.getVisibility(nowMs, fadeMs) >= 1.0f;
    }
}
