package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.EntityCollisions;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** P2-2, logic.entity-collisions: what the last class seen with this type overrides ({@link EntityCollisions#kind}). */
@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements EntityCollisions.TypeMemo {
    @Unique
    private volatile EntityCollisions.Kind quicksilver$kind;

    @Override
    public EntityCollisions.Kind quicksilver$kind() {
        return quicksilver$kind;
    }

    @Override
    public void quicksilver$kind(EntityCollisions.Kind kind) {
        quicksilver$kind = kind;
    }
}
