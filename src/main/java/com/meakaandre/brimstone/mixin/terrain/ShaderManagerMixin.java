package com.meakaandre.brimstone.mixin.terrain;

import com.meakaandre.brimstone.terrain.Terrain;
import net.minecraft.client.renderer.ShaderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Resource reloads: Brimstone checks again whether a resource pack replaces terrain shader code it stands in for. */
@Mixin(ShaderManager.class)
public abstract class ShaderManagerMixin {
    @Inject(method = "apply", at = @At("TAIL"))
    private void brimstone$reloaded(CallbackInfo ci) {
        Terrain.resourcesReloaded();
    }
}
