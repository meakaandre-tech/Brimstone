package com.meakaandre.quicksilver.phase2;

import java.lang.management.ManagementFactory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screens.TitleScreen;

/**
 * Phase 2 (P2-0), measurement only: the client's start-up time, from the JVM's start to the first title screen, and
 * to the first world joined (a launch straight into a world, as CI's, never shows the title screen).
 */
public final class ClientTimes implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, w, h) -> {
            if (ServerNumbers.clientStartupMs == 0 && screen instanceof TitleScreen) {
                ServerNumbers.clientStartupMs = System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime();
                NetworkBytes.LOG.info("Quicksilver: client start-up to the title screen {} ms", ServerNumbers.clientStartupMs);
            }
        });
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (ServerNumbers.clientFirstJoinMs == 0) {
                ServerNumbers.clientFirstJoinMs = System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime();
            }
        });
    }
}
