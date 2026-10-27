package com.meakaandre.brimstone.mixin.animation;

import com.meakaandre.brimstone.animation.Animation;
import com.meakaandre.brimstone.animation.SpriteSlot;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Reading a baked quad's UVs counts as a use of its sprite (items, block models drawn outside terrain, Create's and
 * Flywheel's model caches; section meshing is left out by its scope). A field check and nothing more unless the sprite
 * is an animated block-atlas sprite.
 */
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin {
    @Shadow
    @Final
    private BakedQuad.MaterialInfo materialInfo;

    @Inject(method = {"packedUV0()J", "packedUV1()J", "packedUV2()J", "packedUV3()J", "packedUV(I)J"}, at = @At("HEAD"))
    private void brimstone$quadUsed(CallbackInfoReturnable<Long> cir) {
        BakedQuad.MaterialInfo m = materialInfo;
        TextureAtlasSprite s = m == null ? null : m.sprite();
        if (s == null) return;
        int slot = ((SpriteSlot) s).brimstone$slot();
        if (slot != 0) Animation.spriteUsed(s, slot);
    }
}
