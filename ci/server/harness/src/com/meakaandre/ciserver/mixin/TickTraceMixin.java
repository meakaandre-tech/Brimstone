package com.meakaandre.ciserver.mixin;

import com.meakaandre.ciserver.Trace;
import net.minecraft.world.ticks.LevelTicks;
import net.minecraft.world.ticks.ScheduledTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** CI only (P2-1, diagnosis): every scheduled block or fluid tick inside the trace boxes ({@link Trace}). */
@Mixin(LevelTicks.class)
public abstract class TickTraceMixin {
    @Inject(method = "schedule", at = @At("HEAD"))
    private void ciServer$traceSchedule(ScheduledTick<?> tick, CallbackInfo ci) {
        if (Trace.on()) Trace.scheduled(tick.type(), tick.pos(), tick.triggerTick());
    }
}
