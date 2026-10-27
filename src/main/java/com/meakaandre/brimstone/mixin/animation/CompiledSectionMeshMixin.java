package com.meakaandre.brimstone.mixin.animation;

import com.meakaandre.brimstone.animation.MeshSprites;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** Which animated block-atlas sprites a section mesh shows (read as its layers go to the GPU). */
@Mixin(CompiledSectionMesh.class)
public abstract class CompiledSectionMeshMixin implements MeshSprites {
    @Unique
    private volatile int brimstone$animGen;
    @Unique
    private volatile int[] brimstone$animIds;

    @Override
    public int brimstone$animGen() {
        return brimstone$animGen;
    }

    @Override
    public int[] brimstone$animIds() {
        return brimstone$animIds;
    }

    @Override
    public void brimstone$setAnim(int gen, int[] ids) {
        brimstone$animIds = ids;
        brimstone$animGen = gen;
    }
}
