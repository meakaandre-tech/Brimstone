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

// Quicksilver (package 4): Quicksilver's block renderer (LGPL-3.0-only, like the rest of Quicksilver), with the block
// shading and tinting of Fabric API's Indigo renderer (fabric-renderer-indigo, Fabric API 0.161.0+26.3,
// render/AltModelBlockRendererImpl: shadeQuad, tintQuad and its tint cache) adapted under the Apache License 2.0
// above (see NOTICE). Changes: Quicksilver's quad classes; quads are shaded in a held copy rather than as a transform on
// the output emitter; Indigo's ambient occlusion mode fixed to its default (hybrid).

package com.meakaandre.quicksilver.frapi;

import com.meakaandre.quicksilver.QuicksilverClient;
import com.meakaandre.quicksilver.mixin.frapi.BlockModelLighterAccessor;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.ShadeMode;
import net.fabricmc.fabric.api.client.renderer.v1.model.FabricBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.render.AltModelBlockRenderer;
import net.fabricmc.fabric.api.client.renderer.v1.render.ExtraLightCoordsUtil;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockTintsFactory;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockModelLighter;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Quicksilver's block tessellation for the Fabric Renderer API (chunk meshing, moving blocks): the model emits its quads
 * ({@link FabricBlockStateModel#emitQuads}), and each is culled (vanilla's {@link Block#shouldRenderFace}), lit, tinted
 * and moved into place exactly as Indigo, the API's reference renderer, does it (smooth lighting {@link AoCalculator},
 * flat lighting {@link FlatLighter}, Indigo's tint cache). One instance per meshing call (one thread).
 *
 * <p>A block's quads are held until its model has emitted them all. If the model throws, they are dropped, the
 * transforms it left pushed are cleared, and the block is drawn with vanilla's own {@link ModelBlockRenderer} instead
 * (logged once), so no quad is drawn twice. With Quicksilver's block renderer switched off in its config, every block
 * goes that way.
 */
final class BlockRenderer implements AltModelBlockRenderer {
    private final boolean ambientOcclusion;
    private final boolean cull;
    private final BlockColors blockColors;
    private final AoCalculator aoCalc;
    private final FlatLighter flatLighter;
    private final RandomSource random = RandomSource.createThreadLocalInstance(0L);
    private final BlockPos.MutableBlockPos scratch = new BlockPos.MutableBlockPos();
    private final MutableQuadViewImpl input = new MutableQuadViewImpl() {
        @Override
        protected void emitDirectly() {
            light(this);
        }
    };
    /** The block's finished quads, written out once its model has emitted them all. */
    private int[] pending = new int[QuadData.STRIDE * 16];
    private int pendingCount;
    private final Meshes.SlotQuad slot = new Meshes.SlotQuad();
    private ModelBlockRenderer vanilla;
    private static volatile boolean fallbackLogged, cacheLogged;
    /** The renderer self-test's instance: its deliberate failures are not logged (nor use up the log-once). */
    boolean quiet;

    // the block being tessellated
    private float offsetX, offsetY, offsetZ;
    private BlockAndTintGetter level;
    private BlockPos pos;
    private BlockState state;
    private boolean defaultAo;
    private int cullChecked, cullHidden;

    // Indigo's tint cache
    private int tintCacheIndex = -1;
    private int tintCacheValue;
    private boolean tintSourcesInitialized;
    private final List<BlockTintSource> tintSources = new ArrayList<>();
    private final IntList computedTintValues = new IntArrayList();

    BlockRenderer(boolean ambientOcclusion, boolean cull, BlockColors blockColors) {
        this.ambientOcclusion = ambientOcclusion;
        this.cull = cull;
        this.blockColors = blockColors;
        BlockModelLighter.Cache lightCache = lightCache();
        aoCalc = new AoCalculator(lightCache);
        flatLighter = new FlatLighter(lightCache);
    }

    /** Vanilla's per-thread light cache (on while sections are meshed), as Indigo uses it; a fresh one if not reachable. */
    private static BlockModelLighter.Cache lightCache() {
        try {
            return BlockModelLighterAccessor.quicksilver$getCache().get();
        } catch (Throwable t) {
            if (t instanceof VirtualMachineError e) throw e;
            if (!cacheLogged) {
                cacheLogged = true;
                QuicksilverClient.LOG.warn("Quicksilver renderer fallback: vanilla's block light cache is not reachable; blocks are lit"
                    + " without it (the same light, slower)", t);
            }
            return new BlockModelLighter.Cache();
        }
    }

    @Override
    public void tesselateBlock(QuadEmitter output, float x, float y, float z, BlockAndTintGetter level, BlockPos pos, BlockState blockState,
                               BlockStateModel model, long seed) {
        if (QuicksilverRenderer.blockRendererOff()) {
            if (!quiet) RendererStats.BLOCKS_VANILLA.increment();
            tesselateVanilla(output, x, y, z, level, pos, blockState, model, seed);
            return;
        }
        Vec3 offset = blockState.getOffset(pos);
        offsetX = (float) (x + offset.x);
        offsetY = (float) (y + offset.y);
        offsetZ = (float) (z + offset.z);
        this.level = level;
        this.pos = pos;
        this.state = blockState;
        defaultAo = ambientOcclusion && blockState.getLightEmission() == 0;
        cullChecked = 0;
        cullHidden = 0;
        aoCalc.prepare(level, blockState, pos);
        pendingCount = 0;
        boolean done = false;
        try {
            random.setSeed(seed);
            ((FabricBlockStateModel) model).emitQuads(input, level, pos, blockState, random, this::hidden);
            done = true;
        } catch (Throwable t) {
            if (t instanceof VirtualMachineError e) throw e;
            pendingCount = 0;
            input.clear();
            input.resetTransforms();
            if (!quiet && !fallbackLogged) {
                fallbackLogged = true;
                QuicksilverClient.LOG.warn("Quicksilver renderer fallback: a block ({}) failed to tessellate; vanilla's block renderer draws it"
                    + " instead (logged once)", blockState, t);
            }
        } finally {
            this.level = null;
            aoCalc.clear();
            resetTintCache();
        }
        try {
            if (done) {
                if (!quiet) RendererStats.BLOCKS_QUICKSILVER.increment();
                Meshes.SlotQuad q = slot;
                for (int i = 0; i < pendingCount; i++) {
                    q.at(pending, i * QuadData.STRIDE, null, null, false);
                    output.copyFrom(q);
                    output.emit();
                }
            } else {
                if (!quiet) RendererStats.BLOCKS_VANILLA.increment();
                tesselateVanilla(output, x, y, z, level, pos, blockState, model, seed);
            }
        } finally {
            pendingCount = 0;
        }
    }

    /** Whether the face of the block towards {@code face} is hidden by its neighbour (vanilla's face culling). */
    private boolean hidden(Direction face) {
        if (face == null || !cull) return false;
        int bit = 1 << face.ordinal();
        if ((cullChecked & bit) == 0) {
            cullChecked |= bit;
            if (!Block.shouldRenderFace(state, level.getBlockState(scratch.setWithOffset(pos, face)), face)) cullHidden |= bit;
        }
        return (cullHidden & bit) != 0;
    }

    /** One emitted quad: culled, then shaded, tinted and moved into place in a held copy (Indigo's transform). */
    private void light(MutableQuadViewImpl quad) {
        if (hidden(quad.cullFace())) return;
        if ((pendingCount + 1) * QuadData.STRIDE > pending.length) pending = Arrays.copyOf(pending, pending.length * 2);
        Meshes.SlotQuad q = slot;
        q.at(pending, pendingCount++ * QuadData.STRIDE, null, null, false);
        q.copyFrom(quad);
        shadeQuad(q, ambientOcclusion && quad.ambientOcclusion().orElse(defaultAo), quad.emissive(), quad.shadeMode() == ShadeMode.VANILLA);
        int tintIndex = q.tintIndex();
        if (tintIndex != -1) q.multiplyColor(getTintColor(level, state, pos, tintIndex));
        q.translate(offsetX, offsetY, offsetZ);
    }

    private void shadeQuad(MutableQuadViewImpl quad, boolean ao, boolean emissive, boolean vanillaShade) {
        // routines below have a bit of copy-paste code reuse to avoid conditional execution inside a hot loop
        if (ao) {
            aoCalc.compute(quad, vanillaShade);

            if (emissive) {
                for (int i = 0; i < 4; i++) {
                    quad.color(i, ARGB.scaleRGB(quad.color(i), aoCalc.ao[i]));
                    quad.lightmap(i, LightCoordsUtil.FULL_BRIGHT);
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    quad.color(i, ARGB.scaleRGB(quad.color(i), aoCalc.ao[i]));
                    quad.lightmap(i, ExtraLightCoordsUtil.smoothMax(quad.lightmap(i), aoCalc.light[i]));
                }
            }
        } else {
            if (emissive) {
                quad.lightmap(LightCoordsUtil.FULL_BRIGHT, LightCoordsUtil.FULL_BRIGHT, LightCoordsUtil.FULL_BRIGHT, LightCoordsUtil.FULL_BRIGHT);
            } else {
                quad.minLightmap(flatLighter.light(level, state, pos, quad));
            }

            flatLighter.applyDirectionalBrightness(level.cardinalLighting(), quad, vanillaShade);
        }
    }

    private void configureTintCache(BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
        List<BlockTintSource> sources = blockColors.getTintSources(blockState);
        int tintSourceCount = sources.size();

        if (tintSourceCount > 0) {
            this.tintSources.addAll(sources);

            for (int i = 0; i < tintSourceCount; ++i) {
                computedTintValues.add(-1);
            }
        } else {
            final BlockTintsFactory factory = BlockColorRegistry.getFactory(blockState);

            if (factory != null) {
                factory.collect(blockState, level, pos, computedTintValues);
            }

            if (!this.computedTintValues.isEmpty()) {
                for (int i = 0; i < this.computedTintValues.size(); i++) {
                    this.tintSources.add(null);
                }
            }
        }
    }

    private int computeTintColor(BlockAndTintGetter level, BlockState state, BlockPos pos, int tintIndex) {
        if (!tintSourcesInitialized) {
            configureTintCache(state, level, pos);
            tintSourcesInitialized = true;
        }

        if (tintIndex >= tintSources.size()) {
            return -1;
        } else {
            BlockTintSource tintSource = tintSources.set(tintIndex, null);

            if (tintSource != null) {
                int computedTintValue = tintSource.colorInWorld(state, level, pos);
                computedTintValues.set(tintIndex, computedTintValue);
                return computedTintValue;
            } else {
                return computedTintValues.getInt(tintIndex);
            }
        }
    }

    private int getTintColor(BlockAndTintGetter level, BlockState state, BlockPos pos, int tintIndex) {
        if (tintCacheIndex == tintIndex) {
            return tintCacheValue;
        } else {
            int tintColor = computeTintColor(level, state, pos, tintIndex);
            tintCacheIndex = tintIndex;
            tintCacheValue = tintColor;
            return tintColor;
        }
    }

    private void resetTintCache() {
        tintCacheIndex = -1;

        if (tintSourcesInitialized) {
            tintSources.clear();
            computedTintValues.clear();
            tintSourcesInitialized = false;
        }
    }

    /** Vanilla's own block renderer for one block, its quads handed to the API's output as vanilla would write them. */
    void tesselateVanilla(QuadEmitter output, float x, float y, float z, BlockAndTintGetter level, BlockPos pos, BlockState state,
                                  BlockStateModel model, long seed) {
        if (vanilla == null) vanilla = new ModelBlockRenderer(ambientOcclusion, cull, blockColors);
        vanilla.tesselateBlock((qx, qy, qz, quad, qi) -> {
            output.fromBakedQuad(quad);
            int emission = quad.materialInfo().lightEmission();
            for (int i = 0; i < 4; i++) {
                output.pos(i, quad.position(i).x() + qx, quad.position(i).y() + qy, quad.position(i).z() + qz);
                output.color(i, qi.getColor(i));
                output.lightmap(i, qi.getLightCoordsWithEmission(i, emission));
            }
            output.emit();
        }, x, y, z, level, pos, state, model, seed);
    }
}
