package com.meakaandre.quicksilver.mixin.visibility;

import com.meakaandre.quicksilver.visibility.Visibility;
import net.minecraft.client.SectionUpdateTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Every section vanilla marks for a new mesh: the occlusion test distrusts it until a newer mesh is in. */
@Mixin(SectionUpdateTracker.class)
public abstract class SectionUpdateTrackerMixin {
    @Inject(method = "setDirty", at = @At("HEAD"))
    private void quicksilver$dirty(int x, int y, int z, boolean fromPlayer, CallbackInfo ci) {
        Visibility.sectionChanged(x, y, z);
    }
}
