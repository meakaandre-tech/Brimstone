package com.meakaandre.quicksilver.mesher;

import com.meakaandre.quicksilver.QuicksilverClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;

/**
 * CI only ({@code quicksilver.debug.verifyMesher}): Quicksilver's fluid mesher against vanilla's fluid renderer on
 * made-up neighbourhoods the CI worlds may never show (waterlogged slabs and stairs under air, glass, leaves, flowing
 * levels, lava next to water, ...). Each case is a 3x3x3 of random blocks around a fluid, meshed by both into the
 * 16-byte vertex; any difference is logged as a mesher check difference (CI fails on it). Runs once, on the first
 * meshing thread that checks a section.
 */
final class FluidSelfTest {
    private FluidSelfTest() {
    }

    private static final int CASES = 600;
    private static final java.util.concurrent.atomic.AtomicBoolean RAN = new java.util.concurrent.atomic.AtomicBoolean();

    static void runOnce(MeshWorker worker, FluidRenderer renderer, CardinalLighting lighting, LevelLightEngine engine) {
        if (!RAN.compareAndSet(false, true)) return;
        int differ = 0, drawn = 0, done = 0;
        try {
            List<BlockState> around = palette(), centres = centres();
            Random random = new Random(40L);
            FluidProbe.Fake level = new FluidProbe.Fake(lighting, engine);
            BlockPos centre = new BlockPos(8, 72, 8);
            for (int c = 0; c < CASES; c++) {
                level.blocks.clear();
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            if ((dx | dy | dz) == 0) continue;
                            BlockState s = around.get(random.nextInt(around.size()));
                            if (!s.isAir()) level.blocks.put(BlockPos.asLong(8 + dx, 72 + dy, 8 + dz), s);
                        }
                    }
                }
                BlockState state = centres.get(c % centres.size());
                level.blocks.put(centre.asLong(), state);
                FluidState fluid = state.getFluidState();
                String bad = worker.compareFluid(renderer, level, centre, state, fluid);
                done++;
                if (bad == null) continue;
                if (bad.isEmpty()) {
                    drawn++;
                    continue;
                }
                drawn++;
                differ++;
                if (differ <= 10) MesherCheck.warn("fluid self-test case " + c + " (" + describe(level, centre) + "): " + bad);
            }
            if (differ > 0) MesherStats.MISMATCHED.addAndGet(differ);
            QuicksilverClient.LOG.info("Quicksilver mesher: fluid self-test: {} cases ({} drawing something), {} differ from vanilla's fluid renderer",
                done, drawn, differ);
        } catch (Throwable t) {
            if (t instanceof VirtualMachineError e) throw e;
            MesherStats.MISMATCHED.incrementAndGet();
            MesherCheck.warn("fluid self-test could not run (after " + done + " cases): " + t);
        }
    }

    private static String describe(FluidProbe.Fake level, BlockPos centre) {
        StringBuilder b = new StringBuilder();
        for (var e : level.blocks.long2ObjectEntrySet()) {
            BlockPos p = BlockPos.of(e.getLongKey());
            if (b.length() > 0) b.append("; ");
            b.append(p.getX() - centre.getX()).append(',').append(p.getY() - centre.getY()).append(',').append(p.getZ() - centre.getZ())
                .append(' ').append(e.getValue());
        }
        return b.toString();
    }

    private static BlockState water(int level) {
        return Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, level);
    }

    private static List<BlockState> centres() {
        List<BlockState> l = new ArrayList<>();
        for (int i = 0; i <= 8; i++) l.add(water(i));
        l.add(Blocks.LAVA.defaultBlockState());
        l.add(Blocks.LAVA.defaultBlockState().setValue(LiquidBlock.LEVEL, 2));
        l.add(Blocks.OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP).setValue(BlockStateProperties.WATERLOGGED, true));
        l.add(Blocks.OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM).setValue(BlockStateProperties.WATERLOGGED, true));
        l.add(Blocks.STONE_STAIRS.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true));
        l.add(Blocks.OAK_FENCE.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true));
        l.add(Blocks.SEAGRASS.defaultBlockState());
        return l;
    }

    private static List<BlockState> palette() {
        List<BlockState> l = new ArrayList<>();
        for (int i = 0; i < 6; i++) l.add(Blocks.AIR.defaultBlockState());
        for (int i = 0; i < 4; i++) l.add(water(0));
        l.add(water(2));
        l.add(water(5));
        l.add(water(8));
        l.add(Blocks.LAVA.defaultBlockState());
        l.add(Blocks.STONE.defaultBlockState());
        l.add(Blocks.STONE.defaultBlockState());
        l.add(Blocks.GLASS.defaultBlockState());
        l.add(Blocks.OAK_LEAVES.defaultBlockState());
        l.add(Blocks.ICE.defaultBlockState());
        l.add(Blocks.OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));
        l.add(Blocks.OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM));
        l.add(Blocks.OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP).setValue(BlockStateProperties.WATERLOGGED, true));
        l.add(Blocks.OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM).setValue(BlockStateProperties.WATERLOGGED, true));
        l.add(Blocks.STONE_STAIRS.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true));
        l.add(Blocks.STONE_STAIRS.defaultBlockState());
        l.add(Blocks.OAK_FENCE.defaultBlockState());
        l.add(Blocks.SEAGRASS.defaultBlockState());
        return l;
    }
}
