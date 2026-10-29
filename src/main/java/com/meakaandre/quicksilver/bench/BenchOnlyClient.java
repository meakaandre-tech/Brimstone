package com.meakaandre.quicksilver.bench;

import com.meakaandre.quicksilver.QuicksilverClient;
import net.fabricmc.api.ClientModInitializer;

/**
 * Client entrypoint of the bench-only jar ({@code quicksilver-bench}, package 4b): the capability probe, the counters and
 * {@code /quicksilver bench} with its scenes, and none of Quicksilver's modules, so it measures whatever draws the game
 * (vanilla, or Sodium) the same way Quicksilver's own bench does. Not used by the full jar.
 */
public final class BenchOnlyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BenchModules.jar = "quicksilver-bench";
        QuicksilverClient.LOG.info("Quicksilver bench-only jar: /quicksilver bench, its counters and scenes; no Quicksilver module is in this jar");
        QuicksilverClient.init();
        // phase 2 (P2-0): the single-player server's numbers in the bench, as in the full jar (measurement only)
        com.meakaandre.quicksilver.phase2.ServerNumbers.init();
        new com.meakaandre.quicksilver.phase2.ClientTimes().onInitializeClient();
    }
}
