package com.meakaandre.brimstone.cipin;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI only (package 7). A focused text box's cursor blinks with the wall clock since the box was focused, so whichever
 * moment a screenshot catches decides whether the chat line's cursor shows (the SIFTEC hud scene has the chat open).
 * Each frame the box counts as focused just now, so the cursor always shows, in every CI client run, vanilla and
 * Brimstone alike.
 */
@Mixin(EditBox.class)
public abstract class EditBoxCursorMixin {
    @Shadow
    private long focusedTime;

    @Inject(method = "extractWidgetRenderState", at = @At("HEAD"))
    private void brimstoneCi$holdCursor(CallbackInfo ci) {
        focusedTime = Util.getMillis();
    }
}
