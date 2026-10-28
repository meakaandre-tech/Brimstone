package com.meakaandre.quicksilver.visibility;

/**
 * Walks the block grid along a segment, one cell boundary at a time (the standard voxel traversal: step along the axis
 * whose next boundary is nearest). Each step crosses one plane {@code axis = k} from cell {@code prev} into the
 * neighbouring cell; the visitor sees every crossing in order and can stop the walk.
 */
final class Ray {
    interface Visitor {
        /**
         * The segment crosses the plane {@code axis = k} going in direction {@code dir} (+1 or -1), from the cell at
         * (px, py, pz) into the next one along {@code axis}. Return true to stop.
         */
        boolean cross(int axis, int k, int dir, int px, int py, int pz);
    }

    private Ray() {
    }

    /** True if the visitor stopped the walk before the segment's end. */
    static boolean walk(double x0, double y0, double z0, double x1, double y1, double z1, Visitor v) {
        int x = (int) Math.floor(x0), y = (int) Math.floor(y0), z = (int) Math.floor(z0);
        double dx = x1 - x0, dy = y1 - y0, dz = z1 - z0;
        int sx = dx > 0 ? 1 : dx < 0 ? -1 : 0;
        int sy = dy > 0 ? 1 : dy < 0 ? -1 : 0;
        int sz = dz > 0 ? 1 : dz < 0 ? -1 : 0;
        double tx = sx > 0 ? (x + 1 - x0) / dx : sx < 0 ? (x0 - x) / -dx : Double.POSITIVE_INFINITY;
        double ty = sy > 0 ? (y + 1 - y0) / dy : sy < 0 ? (y0 - y) / -dy : Double.POSITIVE_INFINITY;
        double tz = sz > 0 ? (z + 1 - z0) / dz : sz < 0 ? (z0 - z) / -dz : Double.POSITIVE_INFINITY;
        double ddx = sx != 0 ? 1 / Math.abs(dx) : Double.POSITIVE_INFINITY;
        double ddy = sy != 0 ? 1 / Math.abs(dy) : Double.POSITIVE_INFINITY;
        double ddz = sz != 0 ? 1 / Math.abs(dz) : Double.POSITIVE_INFINITY;
        // a segment crosses at most |dx|+|dy|+|dz|+3 boundaries; the cap only guards against NaN input
        int left = (int) Math.min(1 << 20, Math.abs(dx) + Math.abs(dy) + Math.abs(dz) + 4);
        while (left-- > 0) {
            if (tx <= ty && tx <= tz) {
                if (tx > 1) return false;
                int px = x;
                x += sx;
                tx += ddx;
                if (v.cross(0, sx > 0 ? x : px, sx, px, y, z)) return true;
            } else if (ty <= tz) {
                if (ty > 1) return false;
                int py = y;
                y += sy;
                ty += ddy;
                if (v.cross(1, sy > 0 ? y : py, sy, x, py, z)) return true;
            } else {
                if (tz > 1) return false;
                int pz = z;
                z += sz;
                tz += ddz;
                if (v.cross(2, sz > 0 ? z : pz, sz, x, y, pz)) return true;
            }
        }
        return false;
    }
}
