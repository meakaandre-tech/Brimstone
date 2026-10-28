package com.meakaandre.quicksilver.mixin.visibility;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.meakaandre.quicksilver.visibility.Visibility;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Where vanilla picks the entities and block entities to draw (the extract phase, render thread): after vanilla's own
 * tests say "draw", Quicksilver's occlusion test may still say "nothing can see it". Also tracks block and chunk changes
 * for the test, and level resets.
 */
@Mixin(LevelExtractor.class)
public abstract class LevelExtractorMixin {
    /** This frame's visible sections are known (applyFrustum ran before); entities come first, then block entities. */
    @Inject(method = "extractVisibleEntities", at = @At("HEAD"))
    private void quicksilver$entitiesStart(Camera camera, Frustum frustum, DeltaTracker deltaTracker, LevelRenderState state, CallbackInfo ci) {
        Visibility.beginFrame(camera, frustum);
    }

    @WrapOperation(method = "extractVisibleEntities", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/renderer/extract/LevelExtractor;isEntityVisible(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDDFJ)Z"))
    private boolean quicksilver$entityVisible(LevelExtractor self, Entity entity, Frustum frustum, double camX, double camY, double camZ,
                                            float partialTick, long fadeMs, Operation<Boolean> original) {
        boolean vanilla = original.call(self, entity, frustum, camX, camY, camZ, partialTick, fadeMs);
        return Visibility.entityVisible(entity, vanilla, camX, camY, camZ, partialTick);
    }

    /** The first call is the loop over the visible sections' block entities; the second (global ones) is left alone. */
    @WrapOperation(method = "extractVisibleBlockEntities", at = @At(value = "INVOKE", ordinal = 0,
        target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;tryExtractRenderState(Lnet/minecraft/world/level/block/entity/BlockEntity;FLnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;Z)Lnet/minecraft/client/renderer/blockentity/state/BlockEntityRenderState;"))
    private BlockEntityRenderState quicksilver$blockEntity(BlockEntityRenderDispatcher dispatcher, BlockEntity blockEntity, float partialTick,
                                                         ModelFeatureRenderer.CrumblingOverlay crumbling, boolean flag,
                                                         Operation<BlockEntityRenderState> original) {
        if (!Visibility.blockEntityVisible(dispatcher, blockEntity)) return null;
        return original.call(dispatcher, blockEntity, partialTick, crumbling, flag);
    }

    /** A block change vanilla may decide needs no new mesh: the occlusion test distrusts the area anyway. */
    @Inject(method = "setBlockDirty(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;)V",
        at = @At("HEAD"))
    private void quicksilver$blockDirty(BlockPos pos, BlockState oldState, BlockState newState, CallbackInfo ci) {
        Visibility.blockChanged(pos);
    }

    @Inject(method = "allChanged", at = @At("HEAD"))
    private void quicksilver$allChanged(CallbackInfo ci) {
        Visibility.reset();
    }

    /** Resource packs changed: the module checks again whether vanilla's block resources are vanilla's. */
    @Inject(method = "onResourceManagerReload", at = @At("HEAD"))
    private void quicksilver$reloaded(net.minecraft.server.packs.resources.ResourceManager manager, CallbackInfo ci) {
        Visibility.resourcesReloaded();
    }

    @Inject(method = "setLevel", at = @At("HEAD"))
    private void quicksilver$setLevel(ClientLevel level, CallbackInfo ci) {
        Visibility.reset();
    }
}
