package com.meakaandre.brimstone.mixin.animation;

import com.meakaandre.brimstone.animation.Animation;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * As vanilla prepares a frame's terrain draws (render thread, its visible sections known, before the frame graph draws
 * anything): stale animated sprites the frame shows are redrawn first.
 */
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Inject(method = {"prepareChunkRenders", "prepareChunkRendersIndirect"}, at = @At("HEAD"))
    private void brimstone$animPrepare(Matrix4fc viewRotation, boolean sortTranslucent, CallbackInfoReturnable<ChunkSectionsToRender> cir) {
        Animation.beforeTerrain(((LevelRenderer) (Object) this).visibleSections());
    }
}
