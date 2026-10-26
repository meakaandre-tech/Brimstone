package com.meakaandre.brimstone.animation;

/**
 * Which animated block-atlas sprites a section mesh shows (mixed into {@code CompiledSectionMesh}), read from its
 * vertices as each layer goes to the GPU: the animation table's generation they refer to (0: none read yet, -1:
 * unknown, so the mesh counts as showing every animated sprite) and their slots (slot - 1, sorted; null if none).
 */
public interface MeshSprites {
    int brimstone$animGen();

    int[] brimstone$animIds();

    void brimstone$setAnim(int gen, int[] ids);
}
