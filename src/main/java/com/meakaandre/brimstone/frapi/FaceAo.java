package com.meakaandre.brimstone.frapi;

import com.meakaandre.brimstone.BrimstoneClient;
import com.meakaandre.brimstone.mixin.frapi.BlockModelLighterInvoker;
import com.mojang.blaze3d.vertex.QuadInstance;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockModelLighter;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Smooth lighting of a block quad as the Fabric Renderer API's reference implementation (Indigo) lights it, which the
 * design doc names as the picture to match. Measured black-box against Indigo's output (Indigo's code was not read).
 * Each corner of the face averages the shade and light of a centre block,
 * the two blocks beside it towards the corner, and the block diagonally between them: around the block in front of the
 * face for a quad on the block's face, else around the block itself (whose own light is the centre's). The face is the
 * quad's light face (for a quad not facing along an axis, such as a cross model's, the axis its normal points to most).
 * A vertex takes its corner's values, or a mix of the four by its position on the face (light per kind, rounded down).
 * Differences from vanilla's own {@link BlockModelLighter}:
 * <ul>
 * <li>the diagonal block counts when either of the two side blocks lets light through ({@link BlockState#isLightPermeable});
 * vanilla checks the blocks one further out from the face instead (both otherwise use the same side block's values);</li>
 * <li>an opaque block that gives no light of its own takes the lowest light of the other samples, per light kind
 * (block and sky), whatever light the world stores in it; vanilla gives every zero sample the light of the block in
 * front of the face.</li>
 * </ul>
 * Everything else (shade per block, the directional brightness of the light face, the light of each block) is vanilla's own.
 */
final class FaceAo {
    private static final float EPS = 1.0e-4f;
    private static volatile boolean invokerFailed;

    private final BlockModelLighter lighter;
    private final BlockPos.MutableBlockPos scratch = new BlockPos.MutableBlockPos();
    private final float[] cornerShade = new float[4];
    private final int[] cornerLight = new int[4];

    FaceAo(BlockModelLighter lighter) {
        this.lighter = lighter;
    }

    /**
     * Lights {@code quad} into {@code qi} (light and colour per vertex, then the face's brightness); false (nothing
     * written) if vanilla's face brightness is not reachable.
     */
    boolean light(BlockAndTintGetter level, BlockState state, BlockPos pos, QuadView quad, BakedQuad baked, QuadInstance qi) {
        if (invokerFailed) return false;
        Direction face = quad.lightFace();
        int ax = face.getAxis().ordinal();
        float plane = coord(quad, 0, ax);
        boolean parallel = true;
        for (int i = 1; i < 4; i++) {
            if (Math.abs(coord(quad, i, ax) - plane) > EPS) parallel = false;
        }
        float brightness;
        try {
            brightness = BlockModelLighterInvoker.brimstone$getDirectionalBrightness(level.cardinalLighting(), baked, face);
        } catch (Throwable t) {
            invokerFailed = true;
            BrimstoneClient.LOG.warn("Brimstone renderer fallback: vanilla's face brightness is not reachable; smooth lighting is vanilla's", t);
            return false;
        }
        float depth = face.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1f - plane : plane;
        boolean onFace = parallel && depth <= EPS;
        int u = ax == 0 ? 1 : 0, v = ax == 2 ? 1 : 2; // the face's two in-plane axes (x, y, z = 0, 1, 2): u is x or y, v is y or z
        // samples around the block in front of the face for a quad on the block's face, else around the block itself
        int bx = pos.getX(), by = pos.getY(), bz = pos.getZ();
        Sample centre;
        if (onFace) {
            bx += face.getStepX();
            by += face.getStepY();
            bz += face.getStepZ();
            centre = sample(level, bx, by, bz);
        } else {
            int l = lighter.getLightCoords(state, level, pos);
            centre = new Sample(state.getShadeBrightness(level, pos), l, dark(state), state.isLightPermeable());
        }
        // the face's four corners: index (su > 0 ? 1 : 0) | (sv > 0 ? 2 : 0)
        for (int corner = 0; corner < 4; corner++) {
            int su = (corner & 1) != 0 ? 1 : -1, sv = (corner & 2) != 0 ? 1 : -1;
            int ux = u == 0 ? su : 0, uy = u == 1 ? su : 0, vy = v == 1 ? sv : 0, vz = v == 2 ? sv : 0;
            Sample a = sample(level, bx + ux, by + uy, bz);
            Sample b = sample(level, bx, by + vy, bz + vz);
            // the diagonal block if light can get to it past either side, else the side along x (down, up and south faces)
            // or y (north, west and east faces), as vanilla's table picks it; u is x for down, up, north and south
            Sample c = a.clear || b.clear ? sample(level, bx + ux, by + uy + vy, bz + vz) : face == Direction.NORTH ? b : a;
            cornerShade[corner] = (a.shade + b.shade + c.shade + centre.shade) * 0.25f;
            cornerLight[corner] = blend(a.light, a.dark, b.light, b.dark, c.light, c.dark, centre.light, centre.dark);
        }
        // each vertex: its corner's values, or between the corners by its position on the face (along u, then v)
        for (int i = 0; i < 4; i++) {
            float pu = unit(coord(quad, i, u)), pv = unit(coord(quad, i, v));
            float shade = mix(cornerShade[0], cornerShade[1], cornerShade[2], cornerShade[3], pu, pv);
            int light = 0;
            for (int shift = 0; shift <= 16; shift += 16) {
                float l = mix((cornerLight[0] >>> shift) & 0xFF, (cornerLight[1] >>> shift) & 0xFF, (cornerLight[2] >>> shift) & 0xFF,
                    (cornerLight[3] >>> shift) & 0xFF, pu, pv);
                light |= ((int) l & 0xFF) << shift;
            }
            qi.setLightCoords(i, light);
            qi.setColor(i, ARGB.gray(shade));
        }
        qi.scaleColor(brightness);
        return true;
    }

    /**
     * Between four corner values (u-, u+ at v-; u-, u+ at v+): along u on both sides, then along v; a vertex at a
     * corner or an edge takes those values exactly.
     */
    private static float mix(float c0, float c1, float c2, float c3, float pu, float pv) {
        return lerp(lerp(c0, c1, pu), lerp(c2, c3, pu), pv);
    }

    private static float lerp(float a, float b, float t) {
        return t == 0f ? a : t == 1f ? b : a + (b - a) * t;
    }

    private static float coord(QuadView quad, int vertex, int axis) {
        return axis == 0 ? quad.x(vertex) : axis == 1 ? quad.y(vertex) : quad.z(vertex);
    }

    /** A vertex coordinate as a weight between the face's two sides: clamped to the block, snapped to its edges. */
    private static float unit(float p) {
        if (p <= EPS) return 0f;
        if (p >= 1f - EPS) return 1f;
        return p;
    }

    /** Four samples' light, per light kind: dark samples take the lowest of the others, then the mean. */
    private static int blend(int l0, boolean d0, int l1, boolean d1, int l2, boolean d2, int l3, boolean d3) {
        int out = 0;
        for (int shift = 0; shift <= 16; shift += 16) {
            int a = (l0 >>> shift) & 0xFF, b = (l1 >>> shift) & 0xFF, c = (l2 >>> shift) & 0xFF, d = (l3 >>> shift) & 0xFF;
            int min = Integer.MAX_VALUE;
            if (!d0) min = Math.min(min, a);
            if (!d1) min = Math.min(min, b);
            if (!d2) min = Math.min(min, c);
            if (!d3) min = Math.min(min, d);
            if (min == Integer.MAX_VALUE) min = 0;
            if (d0) a = min;
            if (d1) b = min;
            if (d2) c = min;
            if (d3) d = min;
            out |= (((a + b + c + d) >> 2) & 0xFF) << shift;
        }
        return out;
    }

    private Sample sample(BlockAndTintGetter level, int x, int y, int z) {
        BlockPos p = scratch.set(x, y, z);
        BlockState s = level.getBlockState(p);
        int l = lighter.getLightCoords(s, level, p);
        return new Sample(s.getShadeBrightness(level, p), l, dark(s), s.isLightPermeable());
    }

    /** An opaque block that gives no light of its own: its light (whatever the world stores in it) is not used. */
    private static boolean dark(BlockState s) {
        return s.isSolidRender() && s.getLightEmission() == 0;
    }

    /** One block's shade, light, whether its light is not used ({@link #dark}), and whether light passes through it. */
    private record Sample(float shade, int light, boolean dark, boolean clear) {
    }
}
