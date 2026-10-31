package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.EntityCollisions;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** P2-2, logic.entity-collisions: vanilla's empty answer when no section near the box holds a hard entity. */
@Mixin(EntityGetter.class)
public interface EntityGetterMixin {
    @Inject(method = "getEntityCollisions", at = @At("HEAD"), cancellable = true)
    private void quicksilver$noHardEntities(Entity entity, AABB box, CallbackInfoReturnable<List<VoxelShape>> cir) {
        if (!EntityCollisions.SWITCH.on) return;
        try {
            if (EntityCollisions.none(this, entity, box)) cir.setReturnValue(List.of());
        } catch (Throwable t) {
            EntityCollisions.SWITCH.fail(t);
        }
    }
}
