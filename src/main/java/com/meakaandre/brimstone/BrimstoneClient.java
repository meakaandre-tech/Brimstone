package com.meakaandre.brimstone;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BrimstoneClient implements ClientModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger("Brimstone");

    @Override
    public void onInitializeClient() {
        LOG.info("Brimstone loaded");
    }
}
