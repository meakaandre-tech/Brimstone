package com.meakaandre.brimstone.cipin.diag;

import com.meakaandre.brimstone.cidiag.CiDiag;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** DIAGNOSTIC (branch only): what Create's outliner draws the hologram's edges with, at draw time. */
@Mixin(targets = "com.zurrtum.create.client.catnip.outliner.BlockClusterOutline$EdgesRenderState")
public abstract class DiagEdgesMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void brimstoneCi$diagEdges(PoseStack.Pose pose, VertexConsumer buffer, CallbackInfo ci) {
        Matrix3f n = new Matrix3f(pose.normal());
        Matrix4f p = new Matrix4f(pose.pose());
        CiDiag.log("edges", "lighting=" + CiDiag.lighting + " color=" + Integer.toHexString((Integer) CiDiag.field(this, "color"))
            + " lightmap=" + Integer.toHexString((Integer) CiDiag.field(this, "lightmap")) + " lineWidth=" + CiDiag.field(this, "lineWidth")
            + " disableNormals=" + CiDiag.field(this, "disableNormals") + " buffer=" + buffer.getClass().getName()
            + " normal=[" + CiDiag.r(n.m00) + "," + CiDiag.r(n.m01) + "," + CiDiag.r(n.m02) + ";" + CiDiag.r(n.m10) + "," + CiDiag.r(n.m11) + ","
            + CiDiag.r(n.m12) + ";" + CiDiag.r(n.m20) + "," + CiDiag.r(n.m21) + "," + CiDiag.r(n.m22) + "]"
            + " pose=[" + CiDiag.r(p.m00()) + "," + CiDiag.r(p.m11()) + "," + CiDiag.r(p.m22()) + "; t=" + CiDiag.r(p.m30()) + "," + CiDiag.r(p.m31()) + "," + CiDiag.r(p.m32()) + "]");
    }
}
