package com.meakaandre.brimstone.bench;

import com.meakaandre.brimstone.BrimstoneClient;
import net.fabricmc.api.ClientModInitializer;

/**
 * Client entrypoint of the bench-only jar ({@code brimstone-bench}, package 4b): the capability probe, the counters and
 * {@code /brimstone bench} with its scenes, and none of Brimstone's modules, so it measures whatever draws the game
 * (vanilla, or Sodium) the same way Brimstone's own bench does. Not used by the full jar.
 */
public final class BenchOnlyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BenchModules.jar = "brimstone-bench";
        BrimstoneClient.LOG.info("Brimstone bench-only jar: /brimstone bench, its counters and scenes; no Brimstone module is in this jar");
        BrimstoneClient.init();
    }
}
