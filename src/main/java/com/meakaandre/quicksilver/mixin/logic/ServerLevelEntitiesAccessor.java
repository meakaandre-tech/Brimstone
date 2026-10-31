package com.meakaandre.quicksilver.mixin.logic;

import com.meakaandre.quicksilver.logic.EntityCollisions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** P2-2, logic.entity-collisions: the server level's entity manager. */
@Mixin(ServerLevel.class)
public interface ServerLevelEntitiesAccessor extends EntityCollisions.LevelEntities {
    @Override
    @Accessor("entityManager")
    PersistentEntitySectionManager<Entity> quicksilver$entityManager();
}
