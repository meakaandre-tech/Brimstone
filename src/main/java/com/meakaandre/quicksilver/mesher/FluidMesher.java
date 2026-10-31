package com.meakaandre.quicksilver.mesher;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Quicksilver's fluid mesher (design doc, package 4c: "its own fluid mesher, drawing exactly what vanilla's does").
 *
 * <p>Written from a description of what a fluid block looks like in the game, in Quicksilver's own structure, not
 * from the order of vanilla's code (Andre, 2026-10-30):
 * <ul>
 * <li><b>Which faces.</b> Each of the six faces faces a neighbour. A face is a candidate unless that neighbour holds the
 * same fluid or the fluid's own block closes that side (its occlusion shape on that side). The top is the exception:
 * only the same fluid above hides it as a candidate. A candidate is drawn unless the neighbour's facing side covers it
 * up to the height the fluid reaches there (the top: its lowest corner; a side: its higher corner; the bottom: 8/9). A
 * fluid with no candidate draws nothing (and its model and tint are never asked for).</li>
 * <li><b>The surface.</b> A fluid with the same fluid above fills its block. Otherwise each corner's height blends the
 * block's own level with the two neighbours that share the corner and the diagonal one: any of them that is full makes
 * the corner full; levels of 0.8 or more weigh ten times; neighbours of another fluid count only as a floor (solid) or
 * not at all (open). The surface sits a hair (1/1000) under those heights.</li>
 * <li><b>Texture.</b> A still surface shows the still sprite whole; a moving one shows the middle of the flowing sprite
 * turned to the flow's direction. Sides show the upper half of the flowing sprite down to the surface at each end, or
 * the overlay sprite where the side faces glass-like blocks or leaves. The bottom shows the still sprite whole.</li>
 * <li><b>Colour and light.</b> The fluid's tint times the dimension's face shading (top, bottom; sides the top's
 * shading times the north or west face's); light is the brighter of the block's and the block above's, per channel.</li>
 * <li><b>Both sides.</b> Sides are drawn from both directions unless they show the overlay; the top also from below
 * when the fluid says it can be seen from underneath there.</li>
 * </ul>
 * Every value is computed as the game computes it (the same float operations), so the vertices are the same bit for bit;
 * CI checks that per section, in a 600-case self-test and once per fluid against vanilla's renderer. It reads the
 * section snapshot instead of the live region; no other mod's code.
 *
 * <p>A fluid that a mod draws its own way through Fabric API's fluid rendering ({@code FluidRenderingRegistry} has a
 * handler for it) is left to vanilla's renderer, which hands it to that handler; its vertices still go straight into
 * the 16-byte vertex ({@link CompactConsumer}).
 */
final class FluidMesher {
    /** How far the surface and the side planes sit inside the block. */
    private static final float INSET = 0.001F;
    /** The height up to which a block below can hide the bottom. */
    private static final float BOTTOM_COVER = 0.8888889F;

    /** The four corners of the block's top, in the order the top face is drawn: north-west, south-west, south-east, north-east. */
    private static final int NW = 0, SW = 1, SE = 2, NE = 3;
    private static final int[] CORNER_X = {0, 0, 1, 1}, CORNER_Z = {0, 1, 1, 0};
    /** The flowing sprite's offsets from its centre per corner, in multiples of (cos, sin) of the flow's angle: u then v. */
    private static final int[][] FLOW_U = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}}, FLOW_V = {{-1, 1}, {1, 1}, {1, -1}, {-1, -1}};

    /** The sides: the corner where the face starts and where it ends (seen from outside), and which way it faces. */
    private enum Side {
        NORTH(Direction.NORTH, NW, NE), EAST(Direction.EAST, NE, SE), SOUTH(Direction.SOUTH, SE, SW), WEST(Direction.WEST, SW, NW);

        final Direction dir;
        final int from, to;

        Side(Direction dir, int from, int to) {
            this.dir = dir;
            this.from = from;
            this.to = to;
        }
    }

    private static final Side[] SIDES = Side.values();
    /** The top seen from below, and the bottom: the corners the other way round. */
    private static final int[] REVERSED = {NW, NE, SE, SW};

    private final SectionWriter w;
    private final BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
    private final BlockPos.MutableBlockPos q = new BlockPos.MutableBlockPos();

    // the block being drawn
    private SectionSnapshot level;
    private BlockPos pos;
    private Fluid fluid;
    private final BlockState[] around = new BlockState[6];
    private final FluidState[] aroundFluid = new FluidState[6];
    private final boolean[] candidate = new boolean[6];
    private final float[] corner = new float[4], u = new float[4], v = new float[4];
    private float bx, by, bz;
    private int quadVertex;
    private int layer;

    FluidMesher(SectionWriter w) {
        this.w = w;
    }

    /** Whether Quicksilver draws this fluid itself (otherwise vanilla's renderer does, through a mod's handler). */
    static boolean ownFluid(FluidState fluid) {
        return FluidRenderingRegistry.getOverride(fluid.getType()) == null;
    }

    static int layerIndex(ChunkSectionLayer layer) {
        return layer == ChunkSectionLayer.SOLID ? SectionWriter.SOLID : layer == ChunkSectionLayer.CUTOUT ? SectionWriter.CUTOUT
            : layer == ChunkSectionLayer.TRANSLUCENT ? SectionWriter.TRANSLUCENT : -1;
    }

    /** Draws the fluid at {@code pos} into the section being written. */
    void tesselate(FluidRenderer renderer, SectionSnapshot level, BlockPos pos, BlockState own, FluidState fluidState) {
        this.level = level;
        this.pos = pos;
        this.fluid = fluidState.getType();
        try {
            if (!findCandidates(own)) return;
            draw(renderer, own, fluidState);
        } finally {
            this.level = null;
            this.pos = null;
        }
    }

    // ---- which faces

    /** Reads the six neighbours and marks the candidate faces; false if there is none. */
    private boolean findCandidates(BlockState own) {
        boolean any = false;
        for (Direction d : Direction.values()) {
            int i = d.ordinal();
            BlockState n = level.getBlockState(p.setWithOffset(pos, d));
            around[i] = n;
            aroundFluid[i] = n.getFluidState();
            boolean c = !aroundFluid[i].getType().isSame(fluid);
            if (c && d != Direction.UP) c = !covers(own, d.getOpposite(), 1.0F);
            if (c && d == Direction.DOWN) c = !covers(n, Direction.DOWN, BOTTOM_COVER);
            candidate[i] = c;
            any |= c;
        }
        return any;
    }

    /**
     * Whether {@code block}'s side that faces back towards a fluid face pointing {@code face} hides that face up to
     * {@code height} (a full side hides any face, a top only when the fluid reaches the top of its block).
     */
    private static boolean covers(BlockState block, Direction face, float height) {
        VoxelShape side = block.getFaceOcclusionShape(face.getOpposite());
        if (side == Shapes.empty()) return false;
        if (side == Shapes.block()) return face != Direction.UP || height == 1.0F;
        return Shapes.blockOccludes(Shapes.box(0.0, 0.0, 0.0, 1.0, height, 1.0), side, face);
    }

    // ---- the surface

    /** The fluid's level at {@code at} for a block of it, 1 under the same fluid; 0 open, -1 solid for anything else. */
    private float levelAt(BlockPos at, BlockState block, FluidState there) {
        if (!fluid.isSame(there.getType())) return block.isSolid() ? -1.0F : 0.0F;
        BlockState above = level.getBlockState(q.set(at.getX(), at.getY() + 1, at.getZ()));
        return fluid.isSame(above.getFluidState().getType()) ? 1.0F : there.getOwnHeight();
    }

    private float sideLevel(Direction d) {
        return levelAt(p.setWithOffset(pos, d), around[d.ordinal()], aroundFluid[d.ordinal()]);
    }

    /** Corner heights into {@link #corner}, from the block's own level and its neighbours'. */
    private void surface(BlockState own, FluidState ownFluid) {
        float self = levelAt(pos, own, ownFluid);
        if (self >= 1.0F) {
            java.util.Arrays.fill(corner, 1.0F);
            return;
        }
        float north = sideLevel(Direction.NORTH), south = sideLevel(Direction.SOUTH);
        float east = sideLevel(Direction.EAST), west = sideLevel(Direction.WEST);
        corner[NE] = blend(self, north, east, 1, -1);
        corner[NW] = blend(self, north, west, -1, -1);
        corner[SE] = blend(self, south, east, 1, 1);
        corner[SW] = blend(self, south, west, -1, 1);
    }

    /**
     * One corner: the block's own level {@code self}, the neighbours along z ({@code alongZ}) and x ({@code alongX}) that
     * share the corner, and (when either of them holds fluid) the diagonal one at ({@code dx}, {@code dz}). The sum adds
     * the diagonal first, then the block itself, then the x and z neighbours.
     */
    private float blend(float self, float alongZ, float alongX, int dx, int dz) {
        if (alongX >= 1.0F || alongZ >= 1.0F) return 1.0F;
        float sum = 0.0F, weight = 0.0F;
        if (alongX > 0.0F || alongZ > 0.0F) {
            BlockPos d = q.set(pos.getX() + dx, pos.getY(), pos.getZ() + dz);
            BlockState diagonal = level.getBlockState(d);
            float h = levelAt(d, diagonal, diagonal.getFluidState());
            if (h >= 1.0F) return 1.0F;
            if (h >= 0.8F) {
                sum += h * 10.0F;
                weight += 10.0F;
            } else if (h >= 0.0F) {
                sum += h;
                weight++;
            }
        }
        for (int k = 0; k < 3; k++) {
            float h = k == 0 ? self : k == 1 ? alongX : alongZ;
            if (h >= 0.8F) {
                sum += h * 10.0F;
                weight += 10.0F;
            } else if (h >= 0.0F) {
                sum += h;
                weight++;
            }
        }
        return sum / weight;
    }

    // ---- drawing

    private void draw(FluidRenderer renderer, BlockState own, FluidState fluidState) {
        // the model and tint only now: a fluid surrounded by itself never gets here, and the tint is a biome blend
        FluidModel model = renderer.fluidModels.get(fluidState);
        TextureAtlasSprite still = model.stillMaterial().sprite();
        TextureAtlasSprite flowing = model.flowingMaterial().sprite();
        TextureAtlasSprite overlay = model.overlayMaterial() == null ? null : model.overlayMaterial().sprite();
        layer = layerIndex(model.layer());
        BlockTintSource tintSource = model.tintSource();
        int tint = tintSource == null ? -1 : tintSource.colorInWorld(own, level, pos);
        float r = (float) (tint >> 16 & 0xFF) / 255.0F, g = (float) (tint >> 8 & 0xFF) / 255.0F, b = (float) (tint & 0xFF) / 255.0F;
        CardinalLighting shading = level.cardinalLighting();
        float topShade = shading.byFace(Direction.UP);

        surface(own, fluidState);
        bx = (float) (pos.getX() & 15);
        by = (float) (pos.getY() & 15);
        bz = (float) (pos.getZ() & 15);
        boolean bottomDrawn = candidate[Direction.DOWN.ordinal()];
        float floor = bottomDrawn ? INSET : 0.0F;

        if (candidate[Direction.UP.ordinal()]) {
            float lowest = Math.min(Math.min(corner[NW], corner[SW]), Math.min(corner[SE], corner[NE]));
            if (!covers(around[Direction.UP.ordinal()], Direction.UP, lowest)) {
                for (int c = 0; c < 4; c++) corner[c] -= INSET;
                drawTop(fluidState, still, flowing, ARGB.colorFromFloat(1.0F, topShade * r, topShade * g, topShade * b), lightAt(pos));
            }
        }
        if (bottomDrawn) {
            float shade = shading.byFace(Direction.DOWN);
            drawBottom(still, floor, ARGB.colorFromFloat(1.0F, shade * r, shade * g, shade * b), lightAt(p.setWithOffset(pos, Direction.DOWN)));
        }
        int sideLight = lightAt(pos);
        for (Side side : SIDES) {
            if (!candidate[side.dir.ordinal()]) continue;
            float hFrom = corner[side.from], hTo = corner[side.to];
            BlockState neighbour = level.getBlockState(p.setWithOffset(pos, side.dir));
            if (covers(neighbour, side.dir, Math.max(hFrom, hTo))) continue;
            boolean glassLike = false;
            TextureAtlasSprite sprite = flowing;
            if (overlay != null) {
                Block nb = neighbour.getBlock();
                if (FluidRenderingRegistry.isBlockTransparent(nb) || nb instanceof LeavesBlock) {
                    sprite = overlay;
                    glassLike = true;
                }
            }
            float face = side.dir.getAxis() == Direction.Axis.Z ? shading.byFace(Direction.NORTH) : shading.byFace(Direction.WEST);
            int color = ARGB.colorFromFloat(1.0F, topShade * face * r, topShade * face * g, topShade * face * b);
            drawSide(side, sprite, hFrom, hTo, floor, color, sideLight, !glassLike);
        }
    }

    /** The surface: four corners at their heights, the texture still or turned to the flow. */
    private void drawTop(FluidState fluidState, TextureAtlasSprite still, TextureAtlasSprite flowing, int color, int light) {
        float[] u = this.u, v = this.v;
        Vec3 flow = fluidState.getFlow(level, pos);
        if (flow.x == 0.0 && flow.z == 0.0) {
            for (int c = 0; c < 4; c++) {
                u[c] = still.getU(CORNER_X[c] == 0 ? 0.0F : 1.0F);
                v[c] = still.getV(CORNER_Z[c] == 0 ? 0.0F : 1.0F);
            }
        } else {
            float angle = (float) Mth.atan2(flow.z, flow.x) - (float) (Math.PI / 2);
            float sin = Mth.sin(angle) * 0.25F, cos = Mth.cos(angle) * 0.25F;
            for (int c = 0; c < 4; c++) {
                u[c] = flowing.getU(0.5F + (FLOW_U[c][0] * cos + FLOW_U[c][1] * sin));
                v[c] = flowing.getV(0.5F + (FLOW_V[c][0] * cos + FLOW_V[c][1] * sin));
            }
        }
        for (int c = 0; c < 4; c++) topCorner(c, u, v, color, light);
        if (fluidState.shouldRenderBackwardUpFace(level, p.setWithOffset(pos, Direction.UP))) {
            for (int c : REVERSED) topCorner(c, u, v, color, light);
        }
    }

    private void topCorner(int c, float[] u, float[] v, int color, int light) {
        vertex(bx + (float) CORNER_X[c], by + corner[c], bz + (float) CORNER_Z[c], color, u[c], v[c], light);
    }

    /** The bottom: flat at the floor, the still sprite whole. */
    private void drawBottom(TextureAtlasSprite still, float floor, int color, int light) {
        float y = by + floor;
        for (int c : REVERSED) {
            vertex(bx + (float) CORNER_X[c], y, bz + (float) CORNER_Z[c], color, CORNER_X[c] == 0 ? still.getU0() : still.getU1(),
                CORNER_Z[c] == 0 ? still.getV0() : still.getV1(), light);
        }
    }

    /**
     * A side: from corner {@code side.from} to {@code side.to} on the plane just inside the block's face, the sprite's left
     * half (u 0 to 0.5) from the surface down to the floor, the v at each end following its height.
     */
    private void drawSide(Side side, TextureAtlasSprite sprite, float hFrom, float hTo, float floor, int color, int light, boolean bothWays) {
        float xFrom = onPlane(bx, CORNER_X[side.from], side.dir.getStepX()), zFrom = onPlane(bz, CORNER_Z[side.from], side.dir.getStepZ());
        float xTo = onPlane(bx, CORNER_X[side.to], side.dir.getStepX()), zTo = onPlane(bz, CORNER_Z[side.to], side.dir.getStepZ());
        float uFrom = sprite.getU(0.0F), uTo = sprite.getU(0.5F);
        float vFrom = sprite.getV((1.0F - hFrom) * 0.5F), vTo = sprite.getV((1.0F - hTo) * 0.5F), vFloor = sprite.getV(0.5F);
        float top0 = by + hFrom, top1 = by + hTo, bottom = by + floor;
        vertex(xFrom, top0, zFrom, color, uFrom, vFrom, light);
        vertex(xTo, top1, zTo, color, uTo, vTo, light);
        vertex(xTo, bottom, zTo, color, uTo, vFloor, light);
        vertex(xFrom, bottom, zFrom, color, uFrom, vFloor, light);
        if (bothWays) {
            vertex(xFrom, top0, zFrom, color, uFrom, vFrom, light);
            vertex(xFrom, bottom, zFrom, color, uFrom, vFloor, light);
            vertex(xTo, bottom, zTo, color, uTo, vFloor, light);
            vertex(xTo, top1, zTo, color, uTo, vTo, light);
        }
    }

    /**
     * A side's coordinate along x or z: the corner's ({@code base + whole}), moved just inside the block on the axis the
     * side faces ({@code step} is that axis's direction, 0 for the other axis).
     */
    private static float onPlane(float base, int whole, int step) {
        float c = base + (float) whole;
        return step == 0 ? c : c - (float) step * INSET;
    }

    /** The brighter of the light at {@code at} and above it, per channel (block, sky). */
    private int lightAt(BlockPos at) {
        int a = level.fluidLight(at);
        int b = level.fluidLight(q.setWithOffset(at, Direction.UP));
        return Math.max(a & 0xFF, b & 0xFF) | Math.max(a >> 16 & 0xFF, b >> 16 & 0xFF) << 16;
    }

    private void vertex(float x, float y, float z, int color, float u, float v, int light) {
        int c = quadVertex;
        w.x[c] = x;
        w.y[c] = y;
        w.z[c] = z;
        w.color[c] = color;
        w.u[c] = u;
        w.v[c] = v;
        w.light[c] = light;
        if (++quadVertex == 4) {
            quadVertex = 0;
            if (layer < 0) {
                if (w.unstorable == null) w.unstorable = "a fluid in a layer other than solid, cutout or translucent";
            } else {
                w.emit(layer);
            }
        }
    }
}
