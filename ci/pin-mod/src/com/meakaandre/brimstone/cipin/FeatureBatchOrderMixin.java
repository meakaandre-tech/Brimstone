package com.meakaandre.brimstone.cipin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI only. A feature render phase (the game's outline phase among them) groups each kind of submit into batches by
 * render type, in a map whose iteration order follows the render types' identity hash codes, so it changes from one
 * game start to the next. Create's outliner submits the Build Gun hologram's translucent checkered faces and its solid
 * edges as two such batches in that phase: in some runs the faces are drawn before the edges, in others after, and the
 * far edges (seen through the faces) come out 3.6% darker or not. Here the batches keep the order they were first
 * submitted in, in every CI client run, vanilla and Brimstone alike, so that order (and the picture) no longer depends
 * on the run.
 */
@Mixin(targets = "net.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase$FeatureSubmits")
public abstract class FeatureBatchOrderMixin {
    @Shadow
    @Final
    @Mutable
    private Map<Object, List<Object>> batches;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void brimstoneCi$keepSubmitOrder(CallbackInfo ci) {
        batches = new LinkedHashMap<>(batches);
    }
}
