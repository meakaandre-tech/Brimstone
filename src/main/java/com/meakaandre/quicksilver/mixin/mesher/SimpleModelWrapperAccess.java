package com.meakaandre.quicksilver.mixin.mesher;

import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Package 4c: the quads of vanilla's plain model part (to tell a block whose every quad has a cull face). */
@Mixin(SimpleModelWrapper.class)
public interface SimpleModelWrapperAccess {
    @Accessor("quads")
    QuadCollection quicksilver$quads();
}
