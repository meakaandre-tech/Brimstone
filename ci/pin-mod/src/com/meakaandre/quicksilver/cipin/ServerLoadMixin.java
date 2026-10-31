package com.meakaandre.quicksilver.cipin;

import java.util.function.BooleanSupplier;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CI only (package 4c, the single-player run): with {@code -Dquicksilver.ci.serverLoadMs=N} the integrated server works
 * (busy, on its own thread) for N ms at the start of every tick, standing in for a heavy factory, so the server falls
 * behind and Quicksilver's server-aware meshing has to back off. Off (0) in every other run.
 */
@Mixin(MinecraftServer.class)
public abstract class ServerLoadMixin {
    private static final long LOAD_NS = Long.getLong("quicksilver.ci.serverLoadMs", 0L) * 1_000_000L;
    private static final Logger LOG = LoggerFactory.getLogger("quicksilver_ci_pin");
    private static boolean logged;
    private static volatile long sink;

    // inside the tick that ServerAware times (Fabric's start and end tick events wrap tickServer's body): the load
    // goes into tickChildren, which tickServer calls between them (run 41014914208: at tickServer's head it fell
    // outside, and the server's measured tick averaged 9 ms)
    @Inject(method = "tickChildren", at = @At("HEAD"), require = 0)
    private void quicksilverCi$load(BooleanSupplier haveTime, CallbackInfo ci) {
        if (LOAD_NS <= 0) return;
        if (!logged) {
            logged = true;
            LOG.info("Quicksilver CI pin: server load {} ms per tick", LOAD_NS / 1_000_000L);
        }
        long end = System.nanoTime() + LOAD_NS;
        long x = sink;
        while (System.nanoTime() < end) x = x * 6364136223846793005L + 1442695040888963407L;
        sink = x;
    }
}
