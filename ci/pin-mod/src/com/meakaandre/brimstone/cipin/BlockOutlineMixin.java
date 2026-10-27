package com.meakaandre.brimstone.cipin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI only. The outline the game draws around the block under the crosshair moves by one pixel row from one frame to
 * the next in the old-GPU profile (GL 3.3), in vanilla's own runs as much as Brimstone's (run 40152673881: two of
 * eleven hologram shots in vanilla's old-GPU run, five in Brimstone's, the same frame-to-frame draws and the same
 * target block in every frame), so whichever frame a screenshot catches decides those pixels. It is not drawn in any
 * CI client run, vanilla and Brimstone alike, except round the one block the core suite's outline scene looks at
 * (package 7), where it is drawn as usual so CI still checks it.
 */
@Mixin(LevelRenderer.class)
public abstract class BlockOutlineMixin {
    /** The stone block of the core suite's {@code outline} scene. */
    private static final BlockPos OUTLINE_SCENE_BLOCK = new BlockPos(2680, 200, -1988);

    @Inject(method = "submitBlockOutline", at = @At("HEAD"), cancellable = true)
    private void brimstoneCi$noBlockOutline(PoseStack poseStack, SubmitNodeCollector collector, LevelRenderState state, CallbackInfo ci) {
        BlockOutlineRenderState outline = state.blockOutlineRenderState;
        if (outline == null || !OUTLINE_SCENE_BLOCK.equals(outline.pos())) ci.cancel();
    }
}
