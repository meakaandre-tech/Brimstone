package com.meakaandre.ciserver.mixin;

import com.meakaandre.ciserver.Trace;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** CI only (P2-1, diagnosis): when each chunk is post-processed (its generation's pending updates applied, fluids
 * ticked) and starts ticking, against the harness's stage and the run's tick ({@link Trace}). */
@Mixin(LevelChunk.class)
public abstract class ChunkTraceMixin {
    @Inject(method = "postProcessGeneration", at = @At("HEAD"))
    private void ciServer$tracePostProcess(ServerLevel level, CallbackInfo ci) {
        if (Trace.on()) Trace.chunk("postprocess", ((LevelChunk) (Object) this).getPos().getMinBlockX() >> 4, ((LevelChunk) (Object) this).getPos().getMinBlockZ() >> 4);
    }

    @Inject(method = "unpackTicks", at = @At("HEAD"))
    private void ciServer$traceUnpack(long gameTime, CallbackInfo ci) {
        if (Trace.on()) Trace.chunk("unpackticks@" + gameTime, ((LevelChunk) (Object) this).getPos().getMinBlockX() >> 4, ((LevelChunk) (Object) this).getPos().getMinBlockZ() >> 4);
    }
}
