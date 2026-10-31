package com.meakaandre.quicksilver.mesher;

import com.meakaandre.quicksilver.frapi.LightLookup;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import java.util.Arrays;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;

/**
 * One section and the ring of blocks around it (18x18x18), copied once into flat arrays on the meshing thread (design
 * doc, package 4c: section snapshot): meshing reads blocks, light and tints through here. Its source is vanilla's own
 * region copy ({@link RenderSectionRegion}, made when the section was scheduled); anything outside the 18x18x18 box,
 * block entities and their render data, biomes, tints and the light engine itself are asked of that region, exactly as
 * vanilla's mesher asks it (so light and biomes come from the live level, as in vanilla), and kept once read.
 *
 * <p>What is cached, and why each is exact:
 * <ul>
 * <li>block states of the whole box, read once;</li>
 * <li>raw sky and block light per position, read from the region's light engine on first use;</li>
 * <li>packed light and shade brightness for the block renderer's lighting ({@link LightLookup}): vanilla's light formula
 * over the raw light read once per position, and the shade per position, the values vanilla's per-thread light cache
 * gives during meshing;</li>
 * <li>the face test: vanilla's {@code Block.shouldRenderFace} answer per pair of states and face (a pure function of
 * them), so each pair is worked out once per section;</li>
 * <li>packed light per position for fluids, and biome tints per position and colour resolver, as vanilla's region and
 * level give them.</li>
 * </ul>
 */
public final class SectionSnapshot implements BlockAndTintGetter, LightLookup {
    static final int SIZE = 18, AREA = SIZE * SIZE, VOLUME = AREA * SIZE;

    /** Vanilla's region copy (or, in CI's fluid self-test, a small made-up level). */
    private BlockAndTintGetter region;
    /** Block coordinates of the box's lowest corner (the section origin minus one). */
    private int minX, minY, minZ;

    final BlockState[] states = new BlockState[VOLUME];
    /** Each position's state as an index into {@link #distinct}. */
    private final short[] stateIndex = new short[VOLUME];
    private final Reference2IntOpenHashMap<BlockState> indexOf = new Reference2IntOpenHashMap<>();
    private BlockState[] distinct = new BlockState[64];
    private int distinctCount;

    // raw light, -1 until read
    private final byte[] sky = new byte[VOLUME], block = new byte[VOLUME];
    // fluid light (LightCoordsUtil.getLightCoords(level, pos)), valid where fluidLightKnown
    private final int[] fluidLight = new int[VOLUME];
    private final boolean[] fluidLightKnown = new boolean[VOLUME];

    // the face test: 0 not known, 1 drawn, 2 hidden; [(a * FACE_TABLE_MAX + b) * 6 + face] for state indices below
    // FACE_TABLE_MAX, a map beyond
    private static final int FACE_TABLE_MAX = 96;
    private final byte[] faceTable = new byte[FACE_TABLE_MAX * FACE_TABLE_MAX * 6];
    private boolean faceMapUsed;
    private final Long2ByteOpenHashMap faceMap = new Long2ByteOpenHashMap();

    // tints of the section's own 16x16x16 blocks, per colour resolver (the first few resolvers asked)
    private static final int TINT_RESOLVERS = 6;
    private final ColorResolver[] tintResolver = new ColorResolver[TINT_RESOLVERS];
    private final int[][] tint = new int[TINT_RESOLVERS][4096];
    private final long[][] tintKnown = new long[TINT_RESOLVERS][64];
    private int tintResolvers;

    // packed raw light (block and sky, before a block's own emission) and shade brightness per position, read on first use
    private final int[] rawLight = new int[VOLUME];
    private final boolean[] rawLightKnown = new boolean[VOLUME];
    private final float[] shade = new float[VOLUME];
    /** The packed raw light vanilla's light formula reads, from this snapshot (in the box: read once, then kept). */
    private final LightCoordsUtil.BrightnessGetter rawGetter = (level, pos) -> rawLight(pos);

    private final BlockPos.MutableBlockPos scratch = new BlockPos.MutableBlockPos();
    /** Biome lookups of vanilla's tint blending for this section, made once per position. */
    final BiomeBlend blend = new BiomeBlend();

    /** Per distinct state: whether {@link HiddenBlocks} found its model plain (0 not asked yet, 1 plain, 2 not). */
    byte[] statePlain = new byte[64];

    /** Box index step to the neighbour on each face (by {@link Direction#ordinal()}). */
    private static final int[] NEIGHBOUR = new int[6];

    static {
        for (Direction d : Direction.values()) NEIGHBOUR[d.ordinal()] = (d.getStepY() * SIZE + d.getStepZ()) * SIZE + d.getStepX();
    }

    SectionSnapshot() {
        indexOf.defaultReturnValue(-1);
        faceMap.defaultReturnValue((byte) 0);
    }

    /**
     * Starts the section at block origin {@code (x, y, z)} (meshing thread). Block states are read from vanilla's region
     * the first time each position is asked for and kept: the section's own blocks once each, as vanilla reads them, and
     * of the ring only what culling, lighting and fluids look at (a mostly empty section reads little of it).
     */
    void load(BlockAndTintGetter region, int x, int y, int z) {
        if (this.region != null) Arrays.fill(states, null); // not released: nothing of the last section may stay
        this.region = region;
        minX = x - 1;
        minY = y - 1;
        minZ = z - 1;
        // the face table rows the previous section used
        int prev = Math.min(distinctCount, FACE_TABLE_MAX);
        for (int a = 0; a < prev; a++) Arrays.fill(faceTable, a * FACE_TABLE_MAX * 6, (a * FACE_TABLE_MAX + prev) * 6, (byte) 0);
        if (faceMapUsed) {
            faceMap.clear();
            faceMapUsed = false;
        }
        Arrays.fill(statePlain, 0, Math.min(distinctCount, statePlain.length), (byte) 0);
        indexOf.clear();
        distinctCount = 0;
        lastState = null;
        Arrays.fill(stateIndex, (short) -1);
        Arrays.fill(sky, (byte) -1);
        Arrays.fill(block, (byte) -1);
        Arrays.fill(fluidLightKnown, false);
        for (int r = 0; r < tintResolvers; r++) {
            Arrays.fill(tintKnown[r], 0L);
            tintResolver[r] = null;
        }
        tintResolvers = 0;
        Arrays.fill(rawLightKnown, false);
        Arrays.fill(shade, Float.NaN);
        blend.begin();
    }

    /** Lets go of the region (and with it the copied world) once a section is meshed. */
    void release() {
        region = null;
        blend.end();
        Arrays.fill(states, null);
        Arrays.fill(distinct, 0, distinctCount, null);
        indexOf.clear();
        // the next section starts with no tint known (load() clears the slots this section used)
        for (int r = 0; r < tintResolvers; r++) tintResolver[r] = null;
    }

    BlockAndTintGetter region() {
        return region;
    }

    /** Index into the box's arrays, or -1 outside it. */
    private int index(int x, int y, int z) {
        int lx = x - minX, ly = y - minY, lz = z - minZ;
        if ((lx | ly | lz) < 0 || lx >= SIZE || ly >= SIZE || lz >= SIZE) return -1;
        return (ly * SIZE + lz) * SIZE + lx;
    }

    private int index(BlockPos pos) {
        return index(pos.getX(), pos.getY(), pos.getZ());
    }

    /** Box index of a block of the section itself, by section-relative coordinates 0..15. */
    static int localIndex(int x, int y, int z) {
        return ((y + 1) * SIZE + z + 1) * SIZE + x + 1;
    }

    /** The block at box index {@code i}, read from the region the first time. */
    private BlockState at(int i) {
        BlockState s = states[i];
        if (s != null) return s;
        int lx = i % SIZE, lz = i / SIZE % SIZE, ly = i / AREA;
        s = region.getBlockState(scratch.set(minX + lx, minY + ly, minZ + lz));
        states[i] = s;
        return s;
    }

    // the last state given an index (runs of one state, air above all, skip the map)
    private BlockState lastState;
    private int lastIndex;

    /** The distinct-state index of the block at box index {@code i}, worked out when first needed (the face test). */
    private int idx(int i) {
        int k = stateIndex[i];
        if (k >= 0) return k;
        BlockState s = at(i);
        if (s == lastState) {
            k = lastIndex;
        } else {
            k = indexOf.getInt(s);
            if (k < 0) {
                k = distinctCount++;
                if (k == distinct.length) distinct = Arrays.copyOf(distinct, k * 2);
                if (k >= statePlain.length) statePlain = Arrays.copyOf(statePlain, Math.max(k + 1, statePlain.length * 2));
                distinct[k] = s;
                indexOf.put(s, k);
            }
            lastState = s;
            lastIndex = k;
        }
        stateIndex[i] = (short) k;
        return k;
    }

    /** The distinct-state index of the block at box index {@code i}. */
    int stateIndexAt(int i) {
        return idx(i);
    }

    /** A block of the section itself, by section-relative coordinates 0..15. */
    BlockState local(int x, int y, int z) {
        return at(((y + 1) * SIZE + z + 1) * SIZE + x + 1);
    }

    // ---- BlockAndTintGetter

    @Override
    public BlockState getBlockState(BlockPos pos) {
        int i = index(pos);
        return i >= 0 ? at(i) : region.getBlockState(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return getBlockState(pos).getFluidState();
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return region.getBlockEntity(pos);
    }

    @Override
    public int getHeight() {
        return region.getHeight();
    }

    @Override
    public int getMinY() {
        return region.getMinY();
    }

    @Override
    public CardinalLighting cardinalLighting() {
        return region.cardinalLighting();
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return region.getLightEngine();
    }

    @Override
    public int getBrightness(LightLayer layer, BlockPos pos) {
        int i = index(pos);
        if (i < 0) return region.getBrightness(layer, pos);
        byte[] a = layer == LightLayer.SKY ? sky : block;
        int v = a[i];
        if (v < 0) {
            v = region.getBrightness(layer, pos);
            a[i] = (byte) v;
        }
        return v;
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver resolver) {
        int lx = pos.getX() - minX - 1, ly = pos.getY() - minY - 1, lz = pos.getZ() - minZ - 1;
        if ((lx | ly | lz) < 0 || lx >= 16 || ly >= 16 || lz >= 16) return region.getBlockTint(pos, resolver);
        int r = 0;
        while (r < tintResolvers && tintResolver[r] != resolver) r++;
        if (r == tintResolvers) {
            if (r == TINT_RESOLVERS) return region.getBlockTint(pos, resolver);
            tintResolver[r] = resolver;
            tintResolvers++;
        }
        int i = (ly << 8) | (lz << 4) | lx;
        long[] known = tintKnown[r];
        if ((known[i >> 6] & (1L << i)) != 0) return tint[r][i];
        int c = region.getBlockTint(pos, resolver);
        tint[r][i] = c;
        known[i >> 6] |= 1L << i;
        return c;
    }

    // Fabric API's block getter extensions: vanilla's region answers (render data is copied when the region is made)

    @Override
    public Object getBlockEntityRenderData(BlockPos pos) {
        return region.getBlockEntityRenderData(pos);
    }

    @Override
    public boolean hasBiomes() {
        return region.hasBiomes();
    }

    @Override
    public Holder<Biome> getBiomeFabric(BlockPos pos) {
        return region.getBiomeFabric(pos);
    }

    // ---- lighting (LightLookup)

    /**
     * Packed light for {@code state} at {@code pos}: vanilla's light formula (its emissive blocks and light emission) over
     * the raw light at {@code pos}, which is read from the region once per position. This is what vanilla's per-thread
     * light cache gives during meshing: it caches the raw light per position, not the result, so a block asking for a
     * neighbour's position with its own state (flat lighting) gets its own answer.
     */
    @Override
    public int lightCoords(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return LightCoordsUtil.getLightCoords(rawGetter, this, state, pos);
    }

    /** Raw packed light (block, sky) at {@code pos}, as vanilla's default brightness getter reads it. */
    private int rawLight(BlockPos pos) {
        int i = index(pos);
        if (i < 0) return LightCoordsUtil.pack(getBrightness(LightLayer.BLOCK, pos), getBrightness(LightLayer.SKY, pos));
        if (rawLightKnown[i]) return rawLight[i];
        int v = LightCoordsUtil.pack(getBrightness(LightLayer.BLOCK, pos), getBrightness(LightLayer.SKY, pos));
        rawLight[i] = v;
        rawLightKnown[i] = true;
        return v;
    }

    /** Shade brightness at {@code pos} (callers pass the state at {@code pos}, so it is one value per position). */
    @Override
    public float shade(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        int i = index(pos);
        if (i < 0 || at(i) != state) return state.getShadeBrightness(this, pos);
        float v = shade[i];
        if (Float.isNaN(v)) {
            v = state.getShadeBrightness(this, pos);
            shade[i] = v;
        }
        return v;
    }

    /** Packed light for fluids at {@code pos} (vanilla's {@code LightCoordsUtil.getLightCoords(level, pos)}). */
    int fluidLight(BlockPos pos) {
        int i = index(pos);
        if (i < 0) return LightCoordsUtil.getLightCoords(this, pos);
        if (fluidLightKnown[i]) return fluidLight[i];
        int v = LightCoordsUtil.getLightCoords(this, pos);
        fluidLight[i] = v;
        fluidLightKnown[i] = true;
        return v;
    }

    @Override
    public boolean hiddenFace(BlockState state, BlockPos pos, Direction face) {
        int i = index(pos);
        int nx = pos.getX() + face.getStepX(), ny = pos.getY() + face.getStepY(), nz = pos.getZ() + face.getStepZ();
        int j = index(nx, ny, nz);
        if (i < 0 || j < 0 || at(i) != state) {
            return !Block.shouldRenderFace(state, getBlockState(scratch.set(nx, ny, nz)), face);
        }
        return hidden(i, j, face);
    }

    /** The face test for the block at box index {@code i} of the section itself (its neighbours are all in the box). */
    boolean hiddenLocal(int i, Direction face) {
        return hidden(i, i + NEIGHBOUR[face.ordinal()], face);
    }

    private boolean hidden(int i, int j, Direction face) {
        BlockState si = at(i), sj = at(j);
        int a = idx(i), b = idx(j), f = face.ordinal();
        byte known;
        if (a < FACE_TABLE_MAX && b < FACE_TABLE_MAX) {
            int k = (a * FACE_TABLE_MAX + b) * 6 + f;
            known = faceTable[k];
            if (known == 0) {
                known = Block.shouldRenderFace(si, sj, face) ? (byte) 1 : (byte) 2;
                faceTable[k] = known;
            }
        } else {
            long k = ((long) a << 32) | ((long) b << 3) | f;
            known = faceMap.get(k);
            if (known == 0) {
                known = Block.shouldRenderFace(si, sj, face) ? (byte) 1 : (byte) 2;
                faceMap.put(k, known);
                faceMapUsed = true;
            }
        }
        return known == 2;
    }
}
