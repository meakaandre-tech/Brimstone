package com.meakaandre.quicksilver.frapi;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockModelLighter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Where the block renderer's lighting reads a position's packed light and shade brightness (package 4c). By default
 * vanilla's per-thread light cache, as Indigo reads it; a level that implements this interface (Quicksilver's section
 * snapshot, {@code mesher.SectionSnapshot}) answers instead, with the same values from its own copy of the section.
 * The level's own face test works the same way ({@link #hiddenFace}).
 */
public interface LightLookup {
    /** Packed light at {@code pos} for {@code state}, as vanilla's cache gives it. */
    int lightCoords(BlockState state, BlockAndTintGetter level, BlockPos pos);

    /** Shade brightness (ambient occlusion) at {@code pos} for {@code state}, as vanilla's cache gives it. */
    float shade(BlockState state, BlockAndTintGetter level, BlockPos pos);

    /**
     * Whether {@code state}'s face towards {@code face} is hidden by its neighbour: the same answer as vanilla's
     * {@code Block.shouldRenderFace(state, neighbour, face)} negated.
     */
    boolean hiddenFace(BlockState state, BlockPos pos, Direction face);

    /** Vanilla's light cache, as Indigo reads it. */
    static LightLookup of(BlockModelLighter.Cache cache) {
        return new LightLookup() {
            @Override
            public int lightCoords(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                return cache.getLightCoords(state, level, pos);
            }

            @Override
            public float shade(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                return cache.getShadeBrightness(state, level, pos);
            }

            @Override
            public boolean hiddenFace(BlockState state, BlockPos pos, Direction face) {
                throw new UnsupportedOperationException("only a level answers the face test");
            }
        };
    }
}
