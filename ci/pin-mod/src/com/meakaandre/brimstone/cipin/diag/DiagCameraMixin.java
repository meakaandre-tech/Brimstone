package com.meakaandre.brimstone.cipin.diag;

import com.meakaandre.brimstone.cidiag.CiDiag;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** DIAGNOSTIC (branch only): the camera position each frame, full precision. */
@Mixin(Camera.class)
public abstract class DiagCameraMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void brimstoneCi$diagCamera(CameraRenderState state, DeltaTracker delta, CallbackInfo ci) {
        Vec3 p = state.pos;
        CiDiag.log("camera", "pos=" + p.x + "," + p.y + "," + p.z);
    }
}
