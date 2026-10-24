package com.meakaandre.brimstone.mixin.visibility;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** The box vanilla's own frustum test uses for an entity, and whether vanilla culls that entity at all. */
@Mixin(EntityRenderer.class)
public interface EntityRendererInvoker {
    @Invoker("getBoundingBoxForCulling")
    AABB brimstone$cullingBox(Entity entity, float partialTick);

    @Invoker("affectedByCulling")
    boolean brimstone$affectedByCulling(Entity entity);
}
