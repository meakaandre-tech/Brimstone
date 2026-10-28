package com.meakaandre.quicksilver.mixin.animation;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.meakaandre.quicksilver.animation.Animation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Fabric API's sprite finder reads every sprite's UVs once to index the atlas (lazily, on whichever thread first asks):
 * that is not a use of any sprite, so it happens in a scope whose reads don't count.
 */
@Mixin(targets = "net.fabricmc.fabric.impl.client.renderer.SpriteFinderImpl$Node")
public abstract class SpriteFinderNodeMixin {
    @WrapMethod(method = "add")
    private void quicksilver$indexScope(TextureAtlasSprite sprite, Operation<Void> original) {
        Animation.notAUseEnter();
        try {
            original.call(sprite);
        } finally {
            Animation.notAUseExit();
        }
    }
}
