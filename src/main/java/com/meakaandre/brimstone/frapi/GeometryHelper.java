/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Brimstone (package 4): adapted from Fabric API's Indigo renderer (fabric-renderer-indigo, Fabric API 0.161.0+26.3),
// GeometryHelper and NormalHelper.computeFaceNormal, under the Apache License 2.0 above (see NOTICE). Changes: only the
// parts Brimstone's block lighting uses, in one class; the nominal face is passed in (Brimstone stores it in the quad).

package com.meakaandre.brimstone.frapi;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Vec3i;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/** Quad geometry exactly as Indigo works it out: face normal, light face and shape flags. */
final class GeometryHelper {
    private GeometryHelper() {
    }

    /** set when a quad touches all four corners of a unit cube. */
    static final int CUBIC_FLAG = 1;

    /** set when a quad is parallel to (but not necessarily on) its light face. */
    static final int AXIS_ALIGNED_FLAG = CUBIC_FLAG << 1;

    /** set when a quad is coplanar with its light face. Implies {@link #AXIS_ALIGNED_FLAG} */
    static final int LIGHT_FACE_FLAG = AXIS_ALIGNED_FLAG << 1;

    private static final float EPS_MIN = 0.0001f;
    private static final float EPS_MAX = 1.0f - EPS_MIN;

    /** Combination of {@link #AXIS_ALIGNED_FLAG}, {@link #LIGHT_FACE_FLAG} and {@link #CUBIC_FLAG}. */
    static int computeShapeFlags(QuadView quad, Direction lightFace) {
        int bits = 0;

        if (isQuadParallelToFace(lightFace, quad)) {
            bits |= AXIS_ALIGNED_FLAG;

            if (isParallelQuadOnFace(lightFace, quad)) {
                bits |= LIGHT_FACE_FLAG;
            }
        }

        if (isQuadCubic(lightFace, quad)) {
            bits |= CUBIC_FLAG;
        }

        return bits;
    }

    static boolean isQuadParallelToFace(Direction face, QuadView quad) {
        int i = face.getAxis().ordinal();
        final float val = quad.posByIndex(0, i);
        return equal(val, quad.posByIndex(1, i)) && equal(val, quad.posByIndex(2, i)) && equal(val, quad.posByIndex(3, i));
    }

    /** Mth.equal: within 1.0e-5. */
    private static boolean equal(float a, float b) {
        return Math.abs(b - a) < 1.0E-5F;
    }

    static boolean isParallelQuadOnFace(Direction lightFace, QuadView quad) {
        final float x = quad.posByIndex(0, lightFace.getAxis().ordinal());
        return lightFace.getAxisDirection() == AxisDirection.POSITIVE ? x >= EPS_MAX : x <= EPS_MIN;
    }

    static boolean isQuadCubic(Direction lightFace, QuadView quad) {
        int a, b;

        switch (lightFace) {
        case EAST:
        case WEST:
            a = 1;
            b = 2;
            break;
        case UP:
        case DOWN:
            a = 0;
            b = 2;
            break;
        case SOUTH:
        case NORTH:
            a = 1;
            b = 0;
            break;
        default:
            return false;
        }

        return confirmSquareCorners(a, b, quad);
    }

    private static boolean confirmSquareCorners(int aCoordinate, int bCoordinate, QuadView quad) {
        int flags = 0;

        for (int i = 0; i < 4; i++) {
            final float a = quad.posByIndex(i, aCoordinate);
            final float b = quad.posByIndex(i, bCoordinate);

            if (a <= EPS_MIN) {
                if (b <= EPS_MIN) {
                    flags |= 1;
                } else if (b >= EPS_MAX) {
                    flags |= 2;
                } else {
                    return false;
                }
            } else if (a >= EPS_MAX) {
                if (b <= EPS_MIN) {
                    flags |= 4;
                } else if (b >= EPS_MAX) {
                    flags |= 8;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        return flags == 15;
    }

    /** The face a normal points to most (as vanilla's {@code BakedQuad.direction()}, by Indigo's tie-break). */
    static Direction lightFace(Vector3fc normal) {
        switch (longestAxis(normal.x(), normal.y(), normal.z())) {
        case X:
            return normal.x() > 0 ? Direction.EAST : Direction.WEST;
        case Y:
            return normal.y() > 0 ? Direction.UP : Direction.DOWN;
        case Z:
            return normal.z() > 0 ? Direction.SOUTH : Direction.NORTH;
        default:
            return Direction.UP;
        }
    }

    static Axis longestAxis(float normalX, float normalY, float normalZ) {
        Axis result = Axis.Y;
        float longest = Math.abs(normalY);
        float a = Math.abs(normalX);

        if (a > longest) {
            result = Axis.X;
            longest = a;
        }

        return Math.abs(normalZ) > longest ? Axis.Z : result;
    }

    /** The vertex in the first cubic corner of the quad's light face (for a cubic quad, whatever its vertex order). */
    static int firstCubicVertex(QuadView quad) {
        final float x = quad.x(0);
        final float y = quad.y(0);
        final float z = quad.z(0);
        final boolean a;
        final boolean b;

        switch (quad.lightFace()) {
            case DOWN -> {
                a = x > EPS_MIN;
                b = z < EPS_MAX;
            }
            case UP -> {
                a = x > EPS_MIN;
                b = z > EPS_MIN;
            }
            case NORTH -> {
                a = x < EPS_MAX;
                b = y < EPS_MAX;
            }
            case SOUTH -> {
                a = x > EPS_MIN;
                b = y < EPS_MAX;
            }
            case WEST -> {
                a = z > EPS_MIN;
                b = y < EPS_MAX;
            }
            case EAST -> {
                a = z < EPS_MAX;
                b = y < EPS_MAX;
            }
            default -> {
                return 0;
            }
        }

        int result = 0;

        if (a) {
            result ^= 1;
        }

        if (b) {
            result ^= 3;
        }

        return result;
    }

    /**
     * The quad's face normal: the nominal face's unit vector if the quad is parallel to it, else the normal of the plane
     * through its diagonals.
     */
    static void computeFaceNormal(Vector3f saveTo, QuadView q, Direction nominalFace) {
        if (nominalFace != null && isQuadParallelToFace(nominalFace, q)) {
            Vec3i vec = nominalFace.getUnitVec3i();
            saveTo.set(vec.getX(), vec.getY(), vec.getZ());
            return;
        }

        final float x0 = q.x(0);
        final float y0 = q.y(0);
        final float z0 = q.z(0);
        final float x1 = q.x(1);
        final float y1 = q.y(1);
        final float z1 = q.z(1);
        final float x2 = q.x(2);
        final float y2 = q.y(2);
        final float z2 = q.z(2);
        final float x3 = q.x(3);
        final float y3 = q.y(3);
        final float z3 = q.z(3);

        final float dx0 = x2 - x0;
        final float dy0 = y2 - y0;
        final float dz0 = z2 - z0;
        final float dx1 = x3 - x1;
        final float dy1 = y3 - y1;
        final float dz1 = z3 - z1;

        float normX = dy0 * dz1 - dz0 * dy1;
        float normY = dz0 * dx1 - dx0 * dz1;
        float normZ = dx0 * dy1 - dy0 * dx1;

        float l = (float) Math.sqrt(normX * normX + normY * normY + normZ * normZ);

        if (l != 0) {
            normX /= l;
            normY /= l;
            normZ /= l;
        }

        saveTo.set(normX, normY, normZ);
    }

    /** NormalHelper.packNormal: three signed bytes, x lowest (vanilla's normal format). */
    static int packNormal(float x, float y, float z) {
        x = clamp(x);
        y = clamp(y);
        z = clamp(z);
        return ((int) (x * 127.0f) & 0xFF) | (((int) (y * 127.0f) & 0xFF) << 8) | (((int) (z * 127.0f) & 0xFF) << 16);
    }

    private static float clamp(float v) {
        return v < -1 ? -1 : Math.min(v, 1);
    }

    /** NormalHelper.unpackNormalX/Y/Z. */
    static float unpackNormal(int packed, int component) {
        return ((byte) ((packed >>> (8 * component)) & 0xFF)) * (1.0f / 127.0f);
    }
}
