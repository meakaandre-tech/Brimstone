package com.meakaandre.brimstone;

import com.meakaandre.brimstone.bench.BenchCommand;
import com.meakaandre.brimstone.bench.BenchRun;
import com.meakaandre.brimstone.probe.Capabilities;
import com.meakaandre.brimstone.probe.CapabilityProbe;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client entrypoint. Package 0 only measures: the capability probe, the counters and the bench. Later modules read
 * {@link #capabilities()} to pick their fast path or fallback; nothing does yet. On a dedicated server nothing here
 * runs and no mixin applies (they are all client mixins).
 */
public final class BrimstoneClient implements ClientModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger("Brimstone");

    private static volatile Capabilities capabilities;

    /** What the device offers, read once at start-up; null until then. Read-only. */
    public static Capabilities capabilities() {
        return capabilities;
    }

    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            try {
                capabilities = CapabilityProbe.run();
                LOG.info("Brimstone capabilities: {}", capabilities.describe());
            } catch (Throwable t) {
                LOG.error("Brimstone capability probe failed", t);
            }
        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> BenchCommand.register(dispatcher));
        ClientTickEvents.END_CLIENT_TICK.register(BenchRun::tick);
        // CI: the renderer's self-test, once in a world (package 4)
        if (Boolean.getBoolean("brimstone.debug.verifyRenderer")) {
            ClientTickEvents.END_CLIENT_TICK.register(com.meakaandre.brimstone.frapi.RendererSelfTest::tick);
        }
    }
}
