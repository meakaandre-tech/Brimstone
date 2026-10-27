package com.meakaandre.brimstone.cipin;

import com.meakaandre.brimstone.cipinstate.Pins;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** CI only: the animation pin's run and hold are decided per texture tick ({@link Pins#textureTick}). */
@Mixin(TextureManager.class)
public abstract class TextureTickMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void brimstoneCi$textureTick(CallbackInfo ci) {
        Pins.textureTick(Minecraft.getInstance());
    }
}
