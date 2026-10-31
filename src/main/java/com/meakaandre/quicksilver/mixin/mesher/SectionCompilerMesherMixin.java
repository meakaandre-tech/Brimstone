package com.meakaandre.quicksilver.mixin.mesher;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.meakaandre.quicksilver.mesher.Mesher;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Package 4c: Quicksilver's mesher in place of the body of vanilla's section compiler (vanilla's body still runs for a
 * section Quicksilver hands to it, and for every section while the mesher is off). Vanilla's private block-entity step
 * is called from Quicksilver's mesher for every block entity, so what mods hook there keeps working.
 */
@Mixin(SectionCompiler.class)
public abstract class SectionCompilerMesherMixin {
    @Shadow
    @Final
    private boolean ambientOcclusion;

    @Shadow
    @Final
    private BlockColors blockColors;

    @Shadow
    private void handleBlockEntity(SectionCompiler.Results results, BlockEntity blockEntity) {
        throw new AssertionError();
    }

    @WrapMethod(method = "compile")
    private SectionCompiler.Results quicksilver$mesh(SectionPos section, RenderSectionRegion region, VertexSorting sorting,
                                                    SectionBufferBuilderPack pack, Operation<SectionCompiler.Results> original) {
        return Mesher.compile((SectionCompiler) (Object) this, ambientOcclusion, blockColors, section, region, sorting, pack, original,
            (results, blockEntity) -> handleBlockEntity(results, blockEntity));
    }
}
