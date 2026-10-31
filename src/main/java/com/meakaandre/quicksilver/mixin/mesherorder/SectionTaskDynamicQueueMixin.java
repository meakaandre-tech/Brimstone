package com.meakaandre.quicksilver.mixin.mesherorder;

import com.meakaandre.quicksilver.mesher.MeshPriority;
import java.util.List;
import net.minecraft.client.renderer.chunk.SectionTaskDynamicQueue;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Package 4c: the next section vanilla's meshing threads take, nearest and visible first ({@link MeshPriority}); vanilla's
 * own choice when the module is off. Its own mixin config, so a queue that no longer looks as expected only leaves
 * vanilla's order in place.
 */
@Mixin(SectionTaskDynamicQueue.class)
public abstract class SectionTaskDynamicQueueMixin {
    @Shadow
    @Final
    private List<Object> tasks;

    @Shadow
    private int recompileQuota;

    @Inject(method = "poll", at = @At("HEAD"), cancellable = true)
    private void quicksilver$visibleFirst(Vec3 camera, CallbackInfoReturnable<Object> cir) {
        synchronized (this) {
            int[] quota = {recompileQuota};
            int i = MeshPriority.choose(tasks, camera, quota);
            if (i == -2) return;
            recompileQuota = quota[0];
            cir.setReturnValue(i < 0 ? null : tasks.remove(i));
        }
    }
}
