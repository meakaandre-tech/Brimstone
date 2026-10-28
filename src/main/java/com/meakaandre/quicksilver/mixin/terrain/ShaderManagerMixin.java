package com.meakaandre.quicksilver.mixin.terrain;

import com.meakaandre.quicksilver.terrain.Terrain;
import net.minecraft.client.renderer.ShaderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Resource reloads: Quicksilver checks again whether a resource pack replaces terrain shader code it stands in for. */
@Mixin(ShaderManager.class)
public abstract class ShaderManagerMixin {
    @Inject(method = "apply", at = @At("TAIL"))
    private void quicksilver$reloaded(CallbackInfo ci) {
        Terrain.resourcesReloaded();
    }
}
