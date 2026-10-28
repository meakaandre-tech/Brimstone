package com.meakaandre.quicksilver.mixin.animation;

import com.meakaandre.quicksilver.animation.MeshSprites;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** Which animated block-atlas sprites a section mesh shows (read as its layers go to the GPU). */
@Mixin(CompiledSectionMesh.class)
public abstract class CompiledSectionMeshMixin implements MeshSprites {
    @Unique
    private volatile int quicksilver$animGen;
    @Unique
    private volatile int[] quicksilver$animIds;

    @Override
    public int quicksilver$animGen() {
        return quicksilver$animGen;
    }

    @Override
    public int[] quicksilver$animIds() {
        return quicksilver$animIds;
    }

    @Override
    public void quicksilver$setAnim(int gen, int[] ids) {
        quicksilver$animIds = ids;
        quicksilver$animGen = gen;
    }
}
