package com.meakaandre.quicksilver.mixin.memory;

import com.meakaandre.quicksilver.memory.StateTables;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** P2-1, memory.state-neighbours: once a block's (or fluid's) states exist, they switch to a shared table ({@link StateTables}). */
@Mixin(StateDefinition.class)
public abstract class StateDefinitionMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void quicksilver$tables(CallbackInfo ci) {
        StateTables.built(((StateDefinition<?, ?>) (Object) this).getPossibleStates());
    }
}
