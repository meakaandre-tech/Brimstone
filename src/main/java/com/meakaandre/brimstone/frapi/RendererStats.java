package com.meakaandre.brimstone.frapi;

import com.google.gson.JsonObject;
import java.util.concurrent.atomic.LongAdder;

/**
 * Which path the Fabric Renderer API's blocks and mesh quads took since start-up (package 4), for the bench JSON: CI
 * checks that with {@code block-renderer=off} nothing went through Brimstone's own paths, and with it on nothing went
 * to vanilla's block renderer.
 */
public final class RendererStats {
    private RendererStats() {
    }

    /** Blocks tessellated by Brimstone's block renderer / by vanilla's (switched off, or a model that threw). */
    static final LongAdder BLOCKS_BRIMSTONE = new LongAdder(), BLOCKS_VANILLA = new LongAdder();
    /** API mesh quads in feature submits drawn by Brimstone's mesh renderers / handed to vanilla's as vanilla quads. */
    static final LongAdder MESH_QUADS_BRIMSTONE = new LongAdder(), MESH_QUADS_VANILLA = new LongAdder();

    public static JsonObject json() {
        JsonObject o = new JsonObject();
        o.addProperty("blockRendererOff", BrimstoneRenderer.blockRendererOff());
        o.addProperty("blocksBrimstone", BLOCKS_BRIMSTONE.sum());
        o.addProperty("blocksVanilla", BLOCKS_VANILLA.sum());
        o.addProperty("meshQuadsBrimstone", MESH_QUADS_BRIMSTONE.sum());
        o.addProperty("meshQuadsVanilla", MESH_QUADS_VANILLA.sum());
        return o;
    }

    public static String line(JsonObject o) {
        return "block renderer " + (o.get("blockRendererOff").getAsBoolean() ? "off" : "on") + ", blocks Brimstone/vanilla "
            + o.get("blocksBrimstone") + "/" + o.get("blocksVanilla") + ", mesh quads Brimstone/vanilla " + o.get("meshQuadsBrimstone")
            + "/" + o.get("meshQuadsVanilla");
    }
}
