package com.meakaandre.brimstone.mixin.counters;

import com.meakaandre.brimstone.gui.GuiStats;
import java.util.List;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Counts what the game's GUI pass draws (package 7, {@link GuiStats}): where its preparation and its draw pass start
 * and end; at the start of the draw pass the bench (only while measuring) reads the sorted elements. Count only.
 */
@Mixin(GuiRenderer.class)
public abstract class GuiRendererMixin {
    @Shadow
    @Final
    private List<?> draws;

    @Shadow
    @Final
    private GuiRenderState renderState;

    @Inject(method = "render", at = @At("HEAD"))
    private void brimstone$guiRender(CallbackInfo ci) {
        GuiStats.renderStart();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void brimstone$guiRenderEnd(CallbackInfo ci) {
        GuiStats.renderEnd();
    }

    @Inject(method = "draw", at = @At("HEAD"))
    private void brimstone$guiDraw(CallbackInfo ci) {
        GuiStats.drawStart(draws.size(), renderState);
    }

    @Inject(method = "draw", at = @At("RETURN"))
    private void brimstone$guiDrawEnd(CallbackInfo ci) {
        GuiStats.drawEnd();
    }
}
