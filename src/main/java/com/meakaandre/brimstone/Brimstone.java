package com.meakaandre.brimstone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Main entrypoint. On the client it registers Brimstone's Fabric Renderer API implementation (package 4): the loader
 * runs every mod's main entrypoint before any client entrypoint, so no mod's client initialiser can ask the API for
 * its renderer before there is one. On a dedicated server it does nothing (no client class is loaded).
 */
public final class Brimstone implements ModInitializer {
    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            com.meakaandre.brimstone.frapi.BrimstoneRenderer.register();
        }
    }
}
