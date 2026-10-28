package com.meakaandre.quicksilver.terrain;

import com.mojang.renderpearl.api.pipeline.PolygonMode;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;

/**
 * Skip back faces (package 4b): each solid and cutout layer's quads are stored grouped by facing ({@link
 * CompactVertex.Faces}), and a frame leaves out the axis groups that face away from the camera for the whole section.
 * The GPU would cull every one of those triangles anyway (the terrain pipelines cull back faces), so the picture is
 * unchanged: the gain is the vertex work for triangles that were never going to be drawn. Unaligned quads (plants,
 * slopes, rotated model parts) are always drawn; translucent layers keep vanilla's sorted order and are never split.
 *
 * <p>A +X group is left out only if the camera is at least {@link #MARGIN} blocks (plus twice how far the camera moved
 * since the last frame) on the negative side of the lowest plane any of its quads lies in, and likewise for the other
 * groups. The margin covers what moves the drawn eye away from the camera position Quicksilver reads: view bobbing
 * (well under a block), and a camera position from the previous frame. Without a camera position, after a jump of more
 * than {@link #JUMP} blocks, in a frame whose terrain pipelines do not cull back faces, or with
 * {@code -Dquicksilver.debug.drawAllFaces=true}, every group is drawn.
 *
 * <p>The groups that stay are index ranges inside the same draw: on the plain path entries of the same multi-draw, on
 * the indirect path one command per section layer covering the span from the first group kept to the last (more
 * commands would raise the bytes uploaded every frame), so draw calls and uploads don't rise. Where the device has no
 * multi-draw (one draw per section and layer, {@code PLAIN_SINGLE}), the same one span is drawn.
 * Idea common to terrain renderers (Sodium does something similar); written from the game's geometry only.
 */
final class BackFaces {
    /** Blocks of slack between the camera and a plane before a group facing away from it is left out. */
    static final double MARGIN = 1.0;
    /** A camera move larger than this between two frames (a teleport) draws every group that frame. */
    static final double JUMP = 16.0;
    static final boolean DEBUG_DRAW_ALL = Boolean.getBoolean("quicksilver.debug.drawAllFaces");

    private static volatile double camX, camY, camZ;
    private static volatile boolean cameraSeen;
    /** Set by the camera hook, cleared by each frame that reads it: a frame without a new position draws every group. */
    private static volatile boolean cameraFresh;
    /** Set when a frame was drawn with vanilla's wireframe view: the next frames draw every group (a debug view). */
    private static volatile boolean wireframe;
    /** Set when leaving groups out would have split a draw on this device's multi-draw limit: off for the session. */
    private static volatile String limitHit;
    // render thread: this frame's camera and margin, and the last frame's camera
    private static double fx, fy, fz, margin;
    private static double lastX, lastY, lastZ;
    private static boolean lastValid;
    /** Why groups are not left out, for the bench (null: they are). */
    private static volatile String off = "no frame yet";

    private BackFaces() {
    }

    /** The camera position of the frame being extracted (render thread). */
    static void camera(double x, double y, double z) {
        camX = x;
        camY = y;
        camZ = z;
        cameraSeen = true;
        cameraFresh = true;
    }

    /** Render thread: this frame drew terrain in vanilla's wireframe view (or not). */
    static void wireframeFrame(boolean on) {
        wireframe = on;
    }

    /**
     * Render thread: leaving groups out would make a draw list longer than the device takes in one multi-draw, so
     * draw calls could rise: from now on every group is drawn (one log line).
     */
    static void drawLimitHit(String what) {
        if (limitHit != null) return;
        limitHit = what;
        com.meakaandre.quicksilver.QuicksilverClient.LOG.warn("Quicksilver terrain: facing groups are drawn whole from now on: {}", what);
    }

    static boolean cameraSeen() {
        return cameraSeen;
    }

    /** This frame's camera, or null if unknown (upload order). */
    static double[] frameCamera() {
        return cameraSeen ? new double[] {camX, camY, camZ} : null;
    }

    static String off() {
        return off;
    }

    /**
     * Render thread, as a frame's terrain draws are prepared: whether this frame leaves out groups facing away. Both
     * of Quicksilver's solid and cutout pipelines for the path must cull back faces and fill polygons.
     */
    static boolean beginFrame(RenderPipeline solid, RenderPipeline cutout) {
        if (DEBUG_DRAW_ALL) {
            off = "debug: quicksilver.debug.drawAllFaces";
            return false;
        }
        if (!cameraSeen) {
            off = "no camera position (the camera hook did not run)";
            lastValid = false;
            return false;
        }
        boolean fresh = cameraFresh;
        cameraFresh = false;
        if (!fresh) {
            // the hook did not run for this frame (another mod skipped it?): an old position could leave out faces
            // that face the camera now
            off = "camera position not updated this frame";
            lastValid = false;
            return false;
        }
        if (limitHit != null) {
            off = "draw lists would be longer than this device's multi-draw limit (" + limitHit + ")";
            return false;
        }
        if (wireframe) {
            off = "vanilla's wireframe view";
            lastValid = false;
            return false;
        }
        // Quicksilver's own solid and cutout pipelines (made like the game's terrain pipelines) must cull back faces
        if (!culls(solid) || !culls(cutout)) {
            off = "the terrain pipelines do not cull back faces";
            return false;
        }
        double x = camX, y = camY, z = camZ;
        double moved = lastValid ? Math.sqrt((x - lastX) * (x - lastX) + (y - lastY) * (y - lastY) + (z - lastZ) * (z - lastZ)) : 0;
        boolean first = !lastValid;
        lastX = x;
        lastY = y;
        lastZ = z;
        lastValid = true;
        if (first) {
            off = "first frame";
            return false;
        }
        if (moved > JUMP) {
            TerrainStats.backFaceFrameSkipped();
            return false;
        }
        fx = x;
        fy = y;
        fz = z;
        margin = MARGIN + 2 * moved;
        off = null;
        return true;
    }

    private static boolean culls(RenderPipeline p) {
        return p != null && p.isCull() && p.getPolygonMode() == PolygonMode.FILL;
    }

    /**
     * One solid or cutout layer as index ranges (quad counts in {@code count}, first quads in {@code first}); returns
     * how many. With {@code skip} false, or a layer without facing groups, one range of the whole layer.
     */
    static int ranges(TerrainStore.Part part, TerrainStore.Entry e, boolean skip, boolean single, int[] count, int[] first) {
        CompactVertex.Faces f = part.faces;
        int quads = part.vertices / 4;
        if (!skip || f == null) {
            count[0] = quads;
            first[0] = 0;
            return 1;
        }
        double cx = fx - e.originX, cy = fy - e.originY, cz = fz - e.originZ;
        int n = 0;
        int runStart = -1, runEnd = -1;
        for (int g = 0; g < CompactVertex.GROUPS; g++) {
            int q = f.quads(g);
            if (q == 0) continue; // an empty group splits nothing
            if (mayFace(f, g, cx, cy, cz)) {
                if (runStart < 0) runStart = f.start()[g];
                runEnd = f.start()[g + 1];
            } else if (runStart >= 0) {
                count[n] = runEnd - runStart;
                first[n] = runStart;
                n++;
                runStart = -1;
            }
        }
        if (runStart >= 0) {
            count[n] = runEnd - runStart;
            first[n] = runStart;
            n++;
        }
        if (single && n > 1) {
            // one draw per section and layer on this device: the span from the first group kept to the last
            int end = first[n - 1] + count[n - 1];
            count[0] = end - first[0];
            n = 1;
        }
        if (n == 0) {
            count[0] = 0;
            first[0] = 0;
        }
        return n;
    }

    /** Whether group {@code g} may face the camera at {@code c} (relative to the section origin). */
    private static boolean mayFace(CompactVertex.Faces f, int g, double cx, double cy, double cz) {
        if (g == CompactVertex.UNALIGNED) return true;
        int a = CompactVertex.axis(g);
        double c = a == 0 ? cx : a == 1 ? cy : cz;
        // +axis: front side is above the plane; -axis: below it
        return CompactVertex.positive(g) ? c >= f.planeMin()[g] - margin : c <= f.planeMax()[g] + margin;
    }
}
