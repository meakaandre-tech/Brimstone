package com.meakaandre.brimstone.cipin.diag;

import com.meakaandre.brimstone.cidiag.CiDiag;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** DIAGNOSTIC (branch only): the hologram faces' pose, full precision. */
@Mixin(targets = "com.zurrtum.create.client.catnip.outliner.BlockClusterOutline$FacesRenderState")
public abstract class DiagFacesMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void brimstoneCi$diagFaces(PoseStack.Pose pose, VertexConsumer buffer, CallbackInfo ci) {
        Matrix4f p = pose.pose();
        CiDiag.log("faces", "t=" + p.m30() + "," + p.m31() + "," + p.m32() + " faces=" + ((java.util.Map<?, ?>) CiDiag.field(this, "visibleFaces")).size());
    }
}
