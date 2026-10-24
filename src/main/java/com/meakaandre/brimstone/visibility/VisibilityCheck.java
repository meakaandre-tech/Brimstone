package com.meakaandre.brimstone.visibility;

import com.meakaandre.brimstone.BrimstoneClient;
import com.meakaandre.brimstone.bench.GpuCounters;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * CI's per-frame check of hidden entities and block entities ({@code -Dbrimstone.debug.verifyVisibility=true}): every
 * object the occlusion test hides is checked independently, with rays from the camera to points on the box Brimstone
 * claimed hidden: a grid on each face with at most half a block between points, edges and corners included. A ray that
 * reaches its point without first entering an opaque cube through a drawn face is a violation.
 *
 * <p>The check is view-independent, like the test itself: it doesn't ask whether a point is on screen, so no camera
 * transform (frustum, bobbing, damage tilt) can make it skip a point; a box Brimstone hid must be unreachable from
 * everywhere. It uses the same opaque-cube rule as the test but none of its geometry, so it catches a wrong proof. It
 * reads the world, not the drawn mesh, so it cannot catch a mesh that is older than the world (that is what the
 * test's section-freshness rule is for; see docs/progress.md). It is a sampled check, not a proof.
 *
 * <p>Every violation is counted (in and outside a bench's measuring window) and logged (the first 50); CI fails on any.
 */
final class VisibilityCheck implements Ray.Visitor {
    /** Largest gap between sample points on a face (blocks). */
    private static final double SPACING = 0.5;
    /** Rays per frame at most; objects past it are counted as unchecked (CI fails on any). */
    private static final int RAYS_PER_FRAME = 100_000;
    private static final Direction[] TOWARD_NEGATIVE = {Direction.WEST, Direction.DOWN, Direction.NORTH};
    private static final Direction[] TOWARD_POSITIVE = {Direction.EAST, Direction.UP, Direction.SOUTH};
    private static final VisibilityCheck INSTANCE = new VisibilityCheck();
    private static int raysThisFrame;
    private static int logged;
    /** Since start-up, whether a bench measured or not. */
    static long totalViolations, totalUnverified;

    private ClientLevel level;
    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    private VisibilityCheck() {
    }

    static void newFrame() {
        raysThisFrame = 0;
    }

    static void check(String what, AABB box, double ex, double ey, double ez, ClientLevel level) {
        if (box == null || level == null) return;
        double[] lo = {box.minX, box.minY, box.minZ}, hi = {box.maxX, box.maxY, box.maxZ};
        int[] n = new int[3];
        for (int a = 0; a < 3; a++) n[a] = Math.max(1, (int) Math.ceil((hi[a] - lo[a]) / SPACING));
        int points = 2 * ((n[1] + 1) * (n[2] + 1) + (n[0] + 1) * (n[2] + 1) + (n[0] + 1) * (n[1] + 1));
        if (raysThisFrame + points > RAYS_PER_FRAME) {
            totalUnverified++;
            if (GpuCounters.enabled) VisibilityStats.unverifiedObjects++;
            return;
        }
        INSTANCE.level = level;
        double[] p = new double[3];
        int rays = 0;
        String seen = null;
        outer:
        for (int a = 0; a < 3; a++) {
            int u = a == 0 ? 1 : 0, w = a == 2 ? 1 : 2;
            for (int side = 0; side < 2; side++) {
                p[a] = side == 0 ? lo[a] : hi[a];
                for (int i = 0; i <= n[u]; i++) {
                    for (int j = 0; j <= n[w]; j++) {
                        p[u] = lo[u] + (hi[u] - lo[u]) * i / n[u];
                        p[w] = lo[w] + (hi[w] - lo[w]) * j / n[w];
                        rays++;
                        if (!Ray.walk(ex, ey, ez, p[0], p[1], p[2], INSTANCE)) {
                            seen = String.format(java.util.Locale.ROOT, "(%.2f, %.2f, %.2f)", p[0], p[1], p[2]);
                            break outer;
                        }
                    }
                }
            }
        }
        raysThisFrame += rays;
        if (GpuCounters.enabled) {
            VisibilityStats.verifiedObjects++;
            VisibilityStats.verifiedRays += rays;
        }
        if (seen != null) {
            totalViolations++;
            if (GpuCounters.enabled) VisibilityStats.violations++;
            if (logged < 50) {
                logged++;
                BrimstoneClient.LOG.warn("Brimstone visibility check: {} was hidden, but a ray from the camera ({}, {}, {}) reaches {} on its box {}",
                    what, ex, ey, ez, seen, box);
            }
        }
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
