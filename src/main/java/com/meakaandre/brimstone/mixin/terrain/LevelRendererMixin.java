package com.meakaandre.brimstone.mixin.terrain;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.meakaandre.brimstone.terrain.TerrainRenderer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import java.util.List;
import net.minecraft.client.renderer.DynamicGpuData;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.state.OptionsRenderState;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Brimstone prepares its terrain draws as vanilla prepares its own (render thread, before the frame graph runs). On
 * vanilla's multi-draw indirect path, Brimstone's indirect commands and section data go into the same uploads as
 * vanilla's.
 */
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow
    @Final
    private OptionsRenderState optionsRenderState;

    @Inject(method = "prepareChunkRenders", at = @At("HEAD"))
    private void brimstone$preparePlain(Matrix4fc viewRotation, boolean sortTranslucent, CallbackInfoReturnable<ChunkSectionsToRender> cir) {
        TerrainRenderer.prepare(((LevelRenderer) (Object) this).visibleSections(), optionsRenderState.chunkSectionFadeInTime, false);
    }

    @Inject(method = "prepareChunkRendersIndirect", at = @At("HEAD"))
    private void brimstone$prepareIndirect(Matrix4fc viewRotation, boolean sortTranslucent, CallbackInfoReturnable<ChunkSectionsToRender> cir) {
        TerrainRenderer.prepare(((LevelRenderer) (Object) this).visibleSections(), optionsRenderState.chunkSectionFadeInTime, true);
    }

    @WrapOperation(method = "prepareChunkRendersIndirect", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/renderer/DynamicGpuData;writeChunkSectionCommands(Ljava/util/List;)[Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;"))
    private GpuBufferSlice[] brimstone$commands(DynamicGpuData data, List<List<DynamicGpuData.IndexedDraw>> lists,
                                                Operation<GpuBufferSlice[]> original) {
        List<List<DynamicGpuData.IndexedDraw>> all = TerrainRenderer.withCommands(lists);
        GpuBufferSlice[] slices = original.call(data, all);
        return all == lists ? slices : TerrainRenderer.commandsWritten(slices, lists.size());
    }

    @WrapOperation(method = "prepareChunkRendersIndirect", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/renderer/DynamicGpuData;writeChunkSectionsInstanced(Ljava/util/List;)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;"))
    private GpuBufferSlice brimstone$instances(DynamicGpuData data, List<DynamicGpuData.ChunkSectionInfo> sections,
                                               Operation<GpuBufferSlice> original) {
        List<DynamicGpuData.ChunkSectionInfo> all = TerrainRenderer.withInstances(sections);
        GpuBufferSlice slice = original.call(data, all);
        if (all != sections) TerrainRenderer.instancesWritten(slice);
        return slice;
    }
}
