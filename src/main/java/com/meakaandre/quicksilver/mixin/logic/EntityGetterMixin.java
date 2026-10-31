package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.EntityCollisions;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * P2-2, logic.entity-collisions: inside getEntityCollisions, vanilla's entity lookup (with its own, already widened,
 * box) answers with an empty list when no section it would visit holds an entity that may be hard; vanilla then
 * returns its own empty answer. A redirect, so a call allocates no callback object.
 */
@Mixin(EntityGetter.class)
public interface EntityGetterMixin {
    @Redirect(method = "getEntityCollisions", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/EntityGetter;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"))
    private List<Entity> quicksilver$noHardEntities(EntityGetter getter, Entity entity, AABB box, Predicate<? super Entity> predicate) {
        if (EntityCollisions.SWITCH.on) {
            try {
                if (EntityCollisions.none(getter, entity, box)) return List.of();
            } catch (Throwable t) {
                EntityCollisions.SWITCH.fail(t);
            }
        }
        return getter.getEntities(entity, box, predicate);
    }
}
