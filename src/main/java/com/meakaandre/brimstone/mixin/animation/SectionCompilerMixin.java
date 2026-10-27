package com.meakaandre.brimstone.mixin.animation;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.meakaandre.brimstone.animation.Animation;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Vanilla's section meshing (FRAPI's included) reads sprite and quad UVs to build meshes: those reads are not uses
 * outside terrain (which animated sprites a mesh shows is read from the finished mesh), so they happen in a scope that
 * also ends when meshing throws.
 */
@Mixin(SectionCompiler.class)
public abstract class SectionCompilerMixin {
    @WrapMethod(method = "compile")
    private SectionCompiler.Results brimstone$compileScope(SectionPos pos, RenderSectionRegion region, VertexSorting sorting,
                                                           SectionBufferBuilderPack pack, Operation<SectionCompiler.Results> original) {
        Animation.compileHookSeen = true;
        Animation.notAUseEnter();
        try {
            return original.call(pos, region, sorting, pack);
        } finally {
            Animation.notAUseExit();
        }
    }
}
