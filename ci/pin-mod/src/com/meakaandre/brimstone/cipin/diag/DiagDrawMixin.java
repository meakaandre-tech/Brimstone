package com.meakaandre.brimstone.cipin.diag;

import com.meakaandre.brimstone.cidiag.CiDiag;
import com.mojang.renderpearl.api.commands.RenderPass;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** DIAGNOSTIC (branch only): the order of the frame's feature draws (render type name and pipeline sort key). */
@Mixin(PreparedRenderType.class)
public abstract class DiagDrawMixin {
    @Inject(method = "drawFromBuffer", at = @At("HEAD"))
    private void brimstoneCi$diagDraw(StagedVertexBuffer.ExecuteInfo info, RenderPass pass, CallbackInfo ci) {
        PreparedRenderType self = (PreparedRenderType) (Object) this;
        CiDiag.draw(self.name() + "#" + self.pipeline().getSortKey());
    }

    @Inject(method = "drawFromBufferOit", at = @At("HEAD"))
    private void brimstoneCi$diagDrawOit(StagedVertexBuffer.ExecuteInfo info, OitStage stage, RenderPass pass, CallbackInfo ci) {
        PreparedRenderType self = (PreparedRenderType) (Object) this;
        CiDiag.draw(self.name() + "#" + self.pipeline().getSortKey() + "@" + stage);
    }
}
