package com.meakaandre.quicksilver.mixin.terrain;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.meakaandre.quicksilver.terrain.TerrainRenderer;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Quicksilver draws its part of each terrain layer right after vanilla draws its own, in vanilla's pass and with the
 * uniforms vanilla bound for terrain. Fabric's terrain events, which wrap the whole group, still fire around both. The
 * same call carries vanilla's pipeline overrides: its wireframe view, or (translucent with Improved Transparency) the
 * terrain pipeline of the stage being drawn, in that stage's own pass.
 */
@Mixin(ChunkSectionsToRender.class)
public abstract class ChunkSectionsToRenderMixin {
    @WrapOperation(method = "renderLayers", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;render(Lnet/minecraft/client/renderer/chunk/ChunkSectionLayer;Lcom/mojang/renderpearl/api/commands/RenderPass;Lcom/mojang/renderpearl/api/buffers/GpuBuffer;Lcom/mojang/renderpearl/api/pipeline/IndexType;Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)V"))
    private void quicksilver$drawLayer(ChunkSectionsToRender self, ChunkSectionLayer layer, RenderPass pass, GpuBuffer indexBuffer,
                                     IndexType indexType, RenderPipeline wireframe, RenderPipeline wireframeMultidraw,
                                     Operation<Void> original) {
        original.call(self, layer, pass, indexBuffer, indexType, wireframe, wireframeMultidraw);
        TerrainRenderer.draw(layer, pass, wireframe, wireframeMultidraw);
    }
}
