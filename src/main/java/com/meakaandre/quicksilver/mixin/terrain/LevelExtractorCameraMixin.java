package com.meakaandre.quicksilver.mixin.terrain;

import com.meakaandre.quicksilver.terrain.TerrainRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The camera position of the frame being extracted (package 4b): the terrain module leaves out facing groups that face
 * away from it and uploads meshes nearest first. Optional: without it every group is drawn and uploads keep their order.
 */
@Mixin(LevelExtractor.class)
public abstract class LevelExtractorCameraMixin {
    @Inject(method = "extractVisibleEntities", at = @At("HEAD"))
    private void quicksilver$camera(Camera camera, Frustum frustum, DeltaTracker deltaTracker, LevelRenderState state, CallbackInfo ci) {
        var p = camera.position();
        TerrainRenderer.camera(p.x, p.y, p.z);
    }
}
