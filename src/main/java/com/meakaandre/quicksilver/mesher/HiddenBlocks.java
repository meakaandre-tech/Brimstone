package com.meakaandre.quicksilver.mesher;

import com.meakaandre.quicksilver.QuicksilverClient;
import com.meakaandre.quicksilver.mixin.mesher.SimpleModelWrapperAccess;
import com.meakaandre.quicksilver.mixin.mesher.SingleVariantAccess;
import com.meakaandre.quicksilver.mixin.mesher.WeightedVariantsAccess;
import java.util.IdentityHashMap;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.renderer.block.dispatch.WeightedVariants;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.core.Direction;
import net.minecraft.util.random.Weighted;

/**
 * Package 4c: blocks that draw nothing because every face is hidden (design doc: hidden faces culled with a cached
 * per-block face test). A block is left out without asking its model only when the result is certain: its model is one
 * of vanilla's plain models exactly (a single variant, or weighted variants of them, each one plain part with vanilla's
 * own quad list, so no mod's model and no Fabric Renderer API mesh), every quad of it has a cull face (none drawn
 * whatever its neighbours), and the face test hides all six faces. Then vanilla's renderer would draw nothing either.
 */
final class HiddenBlocks {
    private static final Direction[] DIRS = Direction.values();
    /** Off when mods change vanilla's plain model classes, or when they don't look as expected. */
    static volatile boolean off;
    private static volatile boolean failed;

    private final IdentityHashMap<BlockStateModel, Boolean> plain = new IdentityHashMap<>();
    private Object models;
    static final java.util.concurrent.atomic.LongAdder SKIPPED = new java.util.concurrent.atomic.LongAdder();

    /** Whether the block at box index {@code i} of the section certainly draws nothing (see the class comment). */
    boolean drawsNothing(Object modelSet, BlockStateModel model, SectionSnapshot snap, int i) {
        if (off) return false;
        if (modelSet != models) {
            plain.clear();
            models = modelSet;
        }
        try {
            // per distinct state of the section first (one model per state), then per model
            int k = snap.stateIndexAt(i);
            byte known = snap.statePlain[k];
            if (known == 0) {
                Boolean p = plain.get(model);
                if (p == null) {
                    p = allCulled(model, 0);
                    plain.put(model, p);
                }
                known = p ? (byte) 1 : (byte) 2;
                snap.statePlain[k] = known;
            }
            if (known != 1) return false;
            for (Direction d : DIRS) if (!snap.hiddenLocal(i, d)) return false;
            SKIPPED.increment();
            return true;
        } catch (Throwable t) {
            if (t instanceof VirtualMachineError e) throw e;
            off = true;
            if (!failed) {
                failed = true;
                QuicksilverClient.LOG.warn("Quicksilver mesher: hidden blocks are asked of their models again (vanilla's plain models don't look as"
                    + " expected)", t);
            }
            return false;
        }
    }

    private static boolean allCulled(BlockStateModel model, int depth) {
        if (depth > 4) return false;
        if (model.getClass() == SingleVariant.class) return plainPart(((SingleVariantAccess) model).quicksilver$part());
        if (model.getClass() == WeightedVariants.class) {
            for (Weighted<BlockStateModel> w : ((WeightedVariantsAccess) model).quicksilver$list().unwrap()) {
                if (!allCulled(w.value(), depth + 1)) return false;
            }
            return true;
        }
        return false;
    }

    private static boolean plainPart(BlockStateModelPart part) {
        if (part.getClass() != SimpleModelWrapper.class) return false;
        QuadCollection quads = ((SimpleModelWrapperAccess) part).quicksilver$quads();
        return quads.getClass() == QuadCollection.class && quads.getQuads(null).isEmpty();
    }
}
