package com.meakaandre.brimstone.visibility;

import com.meakaandre.brimstone.BrimstoneClient;
import com.meakaandre.brimstone.bench.GpuCounters;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * CI's pop-in check for hidden entities and block entities ({@code -Dbrimstone.debug.verifyVisibility=true}): every
 * object the occlusion test hides is checked independently, with rays from the camera to a grid of points on the box
 * Brimstone claimed hidden (16 per face, the corners and the centre). A point inside vanilla's frustum that a ray reaches
 * without first entering an opaque cube through a drawn face is a violation: part of the object would be on screen and
 * unoccluded. It uses the same opaque-cube rule as the test but none of its geometry (no slab, no rectangle), so it
 * catches a wrong proof. Each violation is logged (the first 20) and counted in the bench; CI fails on any.
 */
final class VisibilityCheck implements Ray.Visitor {
    private static final int GRID = 4;
    /** Rays per frame at most; objects past it are counted as unchecked. */
    private static final int RAYS_PER_FRAME = 50_000;
    private static final Direction[] TOWARD_NEGATIVE = {Direction.WEST, Direction.DOWN, Direction.NORTH};
    private static final Direction[] TOWARD_POSITIVE = {Direction.EAST, Direction.UP, Direction.SOUTH};
    private static final VisibilityCheck INSTANCE = new VisibilityCheck();
    private static int raysThisFrame;
    private static int logged;

    private ClientLevel level;
    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    private VisibilityCheck() {
    }

    static void newFrame() {
        raysThisFrame = 0;
    }

    static void check(String what, AABB box, double ex, double ey, double ez, Frustum frustum, ClientLevel level) {
        if (box == null || level == null) return;
        int points = 6 * GRID * GRID + 9;
        if (raysThisFrame + points > RAYS_PER_FRAME) {
            if (GpuCounters.enabled) VisibilityStats.unverifiedObjects++;
            return;
        }
        INSTANCE.level = level;
        double[] lo = {box.minX, box.minY, box.minZ}, hi = {box.maxX, box.maxY, box.maxZ};
        double[] p = new double[3];
        int rays = 0;
        String seen = null;
        // faces: 4x4 grid on each
        outer:
        for (int a = 0; a < 3; a++) {
            int u = a == 0 ? 1 : 0, w = a == 2 ? 1 : 2;
            for (int side = 0; side < 2; side++) {
                p[a] = side == 0 ? lo[a] : hi[a];
                for (int i = 0; i < GRID; i++) {
                    for (int j = 0; j < GRID; j++) {
                        p[u] = lo[u] + (hi[u] - lo[u]) * (i + 0.5) / GRID;
                        p[w] = lo[w] + (hi[w] - lo[w]) * (j + 0.5) / GRID;
                        if (!onScreen(frustum, p)) continue;
                        rays++;
                        if (!INSTANCE.blocked(ex, ey, ez, p)) {
                            seen = fmt(p);
                            break outer;
                        }
                    }
                }
            }
        }
        if (seen == null) {
            for (int m = 0; m < 9; m++) {
                if (m < 8) {
                    p[0] = (m & 1) == 0 ? lo[0] : hi[0];
                    p[1] = (m & 2) == 0 ? lo[1] : hi[1];
                    p[2] = (m & 4) == 0 ? lo[2] : hi[2];
                } else {
                    p[0] = (lo[0] + hi[0]) / 2;
                    p[1] = (lo[1] + hi[1]) / 2;
                    p[2] = (lo[2] + hi[2]) / 2;
                }
                if (!onScreen(frustum, p)) continue;
                rays++;
                if (!INSTANCE.blocked(ex, ey, ez, p)) {
                    seen = fmt(p);
                    break;
                }
            }
        }
        raysThisFrame += rays;
        if (GpuCounters.enabled) {
            VisibilityStats.verifiedObjects++;
            VisibilityStats.verifiedRays += rays;
            if (seen != null) VisibilityStats.violations++;
        }
        if (seen != null && logged < 20) {
            logged++;
            BrimstoneClient.LOG.warn("Brimstone visibility check: {} was hidden, but a ray from the camera ({}, {}, {}) reaches {} on its box {}",
                what, ex, ey, ez, seen, box);
        }
    }

    /** Vanilla's frustum test in world coordinates, as vanilla uses it for entities, on a tiny box at the point. */
    private static boolean onScreen(Frustum frustum, double[] p) {
        return frustum.isVisible(new AABB(p[0] - 1e-3, p[1] - 1e-3, p[2] - 1e-3, p[0] + 1e-3, p[1] + 1e-3, p[2] + 1e-3));
    }

    private static String fmt(double[] p) {
        return String.format(java.util.Locale.ROOT, "(%.2f, %.2f, %.2f)", p[0], p[1], p[2]);
    }

    private boolean blocked(double ex, double ey, double ez, double[] p) {
        return Ray.walk(ex, ey, ez, p[0], p[1], p[2], this);
    }

    /** Blocked when the ray enters an opaque cube through a face vanilla draws (against the cell it comes from). */
    @Override
    public boolean cross(int axis, int k, int dir, int px, int py, int pz) {
        int cx = px, cy = py, cz = pz;
        if (axis == 0) cx += dir;
        else if (axis == 1) cy += dir;
        else cz += dir;
        if (level.isOutsideBuildHeight(cy)) return false;
        BlockState c = level.getBlockState(pos.set(cx, cy, cz));
        if (!Occlusion.occluder(c)) return false;
        BlockState n = level.getBlockState(pos.set(px, py, pz));
        return Block.shouldRenderFace(c, n, dir > 0 ? TOWARD_NEGATIVE[axis] : TOWARD_POSITIVE[axis]);
    }
}
