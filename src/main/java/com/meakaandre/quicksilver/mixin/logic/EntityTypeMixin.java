package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.EntityCollisions;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** P2-2, logic.entity-collisions: the last class seen with this type and its kind bits ({@link EntityCollisions}). */
@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements EntityCollisions.TypeMemo {
    @Unique
    private volatile Class<?> quicksilver$class;
    @Unique
    private byte quicksilver$kindBits;

    @Override
    public Class<?> quicksilver$memoClass() {
        return quicksilver$class;
    }

    @Override
    public byte quicksilver$memoKind() {
        return quicksilver$kindBits;
    }

    @Override
    public void quicksilver$memo(Class<?> cls, byte kind) {
        // the bits first: another thread (the client's) that sees the new class reads its bits
        quicksilver$kindBits = kind;
        quicksilver$class = cls;
    }
}
