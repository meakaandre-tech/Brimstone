package com.meakaandre.brimstone.mixin.terrain;

import com.meakaandre.brimstone.terrain.Terrain;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Where a freshly meshed section's layers go to the GPU (on the worker thread that meshed it), and where vanilla lets
 * go of a mesh. Brimstone takes the solid and cutout layers it can hold exactly; everything else continues into
 * vanilla's own buffers unchanged.
 */
@Mixin(SectionRenderDispatcher.RenderSection.class)
public abstract class RenderSectionMixin {
    @Inject(method = "addSectionBuffersToUberBuffer", at = @At("HEAD"), cancellable = true)
    private void brimstone$take(ChunkSectionLayer layer, CompiledSectionMesh mesh, ByteBuffer vertices, ByteBuffer indices,
                                CallbackInfoReturnable<Boolean> cir) {
        if (Terrain.take((SectionRenderDispatcher.RenderSection) (Object) this, layer, mesh, vertices, indices)) cir.setReturnValue(true);
    }

    @Inject(method = "releaseSectionMesh", at = @At("HEAD"))
    private void brimstone$release(SectionMesh mesh, CallbackInfo ci) {
        Terrain.released(mesh);
    }
}
