package com.meakaandre.brimstone.cipin.diag;

import com.meakaandre.brimstone.cidiag.CiDiag;
import java.util.Map;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** DIAGNOSTIC (branch only): the outliner's entries when it submits them (removal countdown, params). */
@Mixin(targets = "com.zurrtum.create.client.catnip.outliner.Outliner")
public abstract class DiagOutlinerMixin {
    @Inject(method = "submitOutlines", at = @At("HEAD"))
    private void brimstoneCi$diagSubmit(CallbackInfo ci) {
        Object map = CiDiag.field(this, "outlines");
        if (!(map instanceof Map<?, ?> m)) return;
        StringBuilder sb = new StringBuilder();
        synchronized (m) {
            for (Map.Entry<?, ?> e : m.entrySet()) {
                Object entry = e.getValue();
                Object outline = CiDiag.field(entry, "outline");
                Object params = CiDiag.field(outline, "params");
                sb.append(e.getKey()).append("{ttl=").append(CiDiag.field(entry, "ticksTillRemoval"))
                    .append(" type=").append(outline.getClass().getSimpleName())
                    .append(" color=").append(Integer.toHexString((Integer) CiDiag.field(params, "color")))
                    .append(" alpha=").append(CiDiag.field(params, "alpha"))
                    .append(" lightmap=").append(Integer.toHexString((Integer) CiDiag.field(params, "lightmap")))
                    .append(" lineWidth=").append(CiDiag.field(params, "lineWidth"))
                    .append(" fadeLineWidth=").append(CiDiag.field(params, "fadeLineWidth"))
                    .append(" face=").append(CiDiag.field(params, "faceTexture")).append("} ");
            }
        }
        CiDiag.log("outliner", sb.toString());
    }
}
