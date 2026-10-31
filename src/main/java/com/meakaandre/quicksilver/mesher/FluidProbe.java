package com.meakaandre.quicksilver.mesher;

import com.meakaandre.quicksilver.QuicksilverClient;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

/**
 * Whether Quicksilver's fluid mesher draws a fluid exactly as vanilla's renderer does, found out once per fluid and
 * model the first time one is meshed: a few made-up neighbourhoods around it (at different places in the 16x16 grid,
 * with air, stone, glass and the same fluid around) are meshed by both into the 16-byte vertex and compared. A fluid
 * that differs anywhere (a mod's fluid drawn in a way Quicksilver's mesher doesn't know, such as a texture spread over
 * several blocks), or whose drawing fails on the made-up level, goes through vanilla's renderer for the session, as a
 * fluid with a Fabric API handler does. The answer is kept per fluid and model, so a resource reload asks again.
 */
final class FluidProbe {
    private FluidProbe() {
    }

    private static final int CASES = 12;
    private record Key(Fluid fluid, Object model) {
    }

    private static final ConcurrentHashMap<Key, Boolean> OWN = new ConcurrentHashMap<>();

    /** Whether the fluid at hand is Quicksilver's to draw ({@code worker}: the meshing thread's, for its probe kit). */
    static boolean own(MeshWorker worker, FluidRenderer renderer, BlockAndTintGetter region, FluidState fluid) {
        // per meshing thread, the last answer by identity first (every fluid block asks: no record or hash per block)
        Object model = renderer.fluidModels.get(fluid);
        if (fluid.getType() == worker.probedFluid && model == worker.probedModel) return worker.probedOwn;
        Key key = new Key(fluid.getType(), model);
        Boolean known = OWN.get(key);
        if (known != null) {
            worker.probedFluid = fluid.getType();
            worker.probedModel = model;
            worker.probedOwn = known;
            return known;
        }
        String bad;
        long t0 = System.nanoTime();
        try {
            bad = probe(worker.prober(), renderer, region.cardinalLighting(), region.getLightEngine(), fluid);
        } catch (Throwable t) {
            if (t instanceof VirtualMachineError e) throw e;
            bad = "drawing it on a made-up level failed: " + t;
        } finally {
            // once per fluid and session: left out of the like-for-like timing (vanilla has no such step)
            worker.probeNanos += System.nanoTime() - t0;
        }
        boolean own = bad == null;
        if (OWN.putIfAbsent(key, own) == null) {
            if (own) {
                QuicksilverClient.LOG.info("Quicksilver mesher: Quicksilver's fluid mesher draws {} (the same as vanilla's renderer in {} made-up cases)",
                    net.minecraft.core.registries.BuiltInRegistries.FLUID.getKey(fluid.getType()), CASES);
            } else {
                QuicksilverClient.LOG.info("Quicksilver mesher: {} goes through vanilla's fluid renderer ({})", net.minecraft.core.registries.BuiltInRegistries.FLUID.getKey(fluid.getType()), bad);
            }
        }
        return OWN.get(key);
    }

    private static String probe(MeshWorker kit, FluidRenderer renderer, CardinalLighting lighting, LevelLightEngine engine, FluidState fluid) {
        BlockState centreState = fluid.createLegacyBlock();
        BlockState[] around = {Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(),
            Blocks.STONE.defaultBlockState(), Blocks.GLASS.defaultBlockState(), centreState};
        Random random = new Random(7L);
        Fake level = new Fake(lighting, engine);
        int drawn = 0;
        for (int c = 0; c < CASES; c++) {
            level.blocks.clear();
            // spread over the 16x16 grid and both signs, so position-dependent drawing shows
            BlockPos centre = new BlockPos(random.nextInt(64) - 32, 72, random.nextInt(64) - 32);
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if ((dx | dy | dz) == 0) continue;
                        // the first cases: the fluid alone in air (every face drawn)
                        BlockState s = c < 2 ? around[0] : around[random.nextInt(around.length)];
                        if (!s.isAir()) level.blocks.put(BlockPos.asLong(centre.getX() + dx, centre.getY() + dy, centre.getZ() + dz), s);
                    }
                }
            }
            level.blocks.put(centre.asLong(), centreState);
            String bad = kit.compareFluid(renderer, level, centre, centreState, fluid);
            if (bad == null) continue;
            drawn++;
            if (!bad.isEmpty()) return "case " + c + ": " + bad;
        }
        return drawn == 0 ? "nothing drawn in any made-up case" : null;
    }

    /** A tiny made-up level: the case's blocks, air elsewhere, light varying by position, a fixed tint. */
    static final class Fake implements BlockAndTintGetter {
        final Long2ObjectOpenHashMap<BlockState> blocks = new Long2ObjectOpenHashMap<>();
        private final CardinalLighting lighting;
        private final LevelLightEngine engine;

        Fake(CardinalLighting lighting, LevelLightEngine engine) {
            this.lighting = lighting;
            this.engine = engine;
        }

        @Override
        public BlockState getBlockState(BlockPos pos) {
            BlockState s = blocks.get(pos.asLong());
            return s == null ? Blocks.AIR.defaultBlockState() : s;
        }

        @Override
        public FluidState getFluidState(BlockPos pos) {
            return getBlockState(pos).getFluidState();
        }

        @Override
        public BlockEntity getBlockEntity(BlockPos pos) {
            return null;
        }

        @Override
        public int getHeight() {
            return 384;
        }

        @Override
        public int getMinY() {
            return -64;
        }

        @Override
        public CardinalLighting cardinalLighting() {
            return lighting;
        }

        @Override
        public LevelLightEngine getLightEngine() {
            return engine;
        }

        @Override
        public int getBrightness(LightLayer layer, BlockPos pos) {
            int h = pos.getX() * 7 + pos.getY() * 3 + pos.getZ() * 5 + (layer == LightLayer.SKY ? 4 : 0);
            return Math.floorMod(h, 16);
        }

        @Override
        public int getBlockTint(BlockPos pos, ColorResolver resolver) {
            return 0xFF3F76E4;
        }
    }
}
