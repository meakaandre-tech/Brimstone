package com.meakaandre.quicksilver.mixin.visibility;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** The box vanilla's own frustum test uses for an entity, and whether vanilla culls that entity at all. */
@Mixin(EntityRenderer.class)
public interface EntityRendererInvoker {
    @Invoker("getBoundingBoxForCulling")
    AABB quicksilver$cullingBox(Entity entity, float partialTick);

    @Invoker("affectedByCulling")
    boolean quicksilver$affectedByCulling(Entity entity);
}
