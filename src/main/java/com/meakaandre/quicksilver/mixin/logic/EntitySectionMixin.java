package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.EntityCollisions;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** P2-2, logic.entity-collisions: each section counts the entities it holds that can be collided with like blocks. */
@Mixin(EntitySection.class)
public abstract class EntitySectionMixin<T extends EntityAccess> implements EntityCollisions.SectionCount {
    @Unique
    private int quicksilver$hardCount;

    @Override
    public int quicksilver$hard() {
        return quicksilver$hardCount;
    }

    @Inject(method = "add", at = @At("RETURN"))
    private void quicksilver$added(T entity, CallbackInfo ci) {
        if (EntityCollisions.counts(entity)) quicksilver$hardCount++;
    }

    @Inject(method = "remove", at = @At("RETURN"))
    private void quicksilver$removed(T entity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && EntityCollisions.counts(entity)) quicksilver$hardCount--;
    }
}
