package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.EntityCollisions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** P2-2, logic.entity-collisions: the entity manager's section storage. */
@Mixin(PersistentEntitySectionManager.class)
public interface PersistentEntitySectionManagerAccessor extends EntityCollisions.ManagerSections {
    @Override
    @Accessor("sectionStorage")
    EntitySectionStorage<Entity> quicksilver$sectionStorage();
}
