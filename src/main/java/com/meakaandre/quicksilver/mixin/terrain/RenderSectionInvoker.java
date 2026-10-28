package com.meakaandre.quicksilver.mixin.terrain;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Vanilla's own "this layer's vertices are on the GPU" callback, which Quicksilver calls once a layer it took is in its
 * buffers, so vanilla swaps the new mesh in exactly as it does for its own uploads.
 */
@Mixin(SectionRenderDispatcher.RenderSection.class)
public interface RenderSectionInvoker {
    @Invoker("vertexBufferUploadCallback")
    void quicksilver$vertexBufferUploaded(CompiledSectionMesh mesh, ChunkSectionLayer layer);

    @Invoker("releaseSectionMesh")
    void quicksilver$releaseSectionMesh(net.minecraft.client.renderer.chunk.SectionMesh mesh);
}
