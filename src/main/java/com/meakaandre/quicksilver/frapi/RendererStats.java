package com.meakaandre.quicksilver.frapi;

import com.google.gson.JsonObject;
import java.util.concurrent.atomic.LongAdder;

/**
 * Which path the Fabric Renderer API's blocks and mesh quads took since start-up (package 4), for the bench JSON: CI
 * checks that with {@code block-renderer=off} nothing went through Quicksilver's own paths, and with it on nothing went
 * to vanilla's block renderer.
 */
public final class RendererStats {
    private RendererStats() {
    }

    /** Blocks tessellated by Quicksilver's block renderer / by vanilla's (switched off, or a model that threw). */
    static final LongAdder BLOCKS_QUICKSILVER = new LongAdder(), BLOCKS_VANILLA = new LongAdder();
    /** API mesh quads in feature submits drawn by Quicksilver's mesh renderers / handed to vanilla's as vanilla quads. */
    static final LongAdder MESH_QUADS_QUICKSILVER = new LongAdder(), MESH_QUADS_VANILLA = new LongAdder();

    public static JsonObject json() {
        JsonObject o = new JsonObject();
        o.addProperty("blockRendererOff", QuicksilverRenderer.blockRendererOff());
        o.addProperty("blocksQuicksilver", BLOCKS_QUICKSILVER.sum());
        o.addProperty("blocksVanilla", BLOCKS_VANILLA.sum());
        o.addProperty("meshQuadsQuicksilver", MESH_QUADS_QUICKSILVER.sum());
        o.addProperty("meshQuadsVanilla", MESH_QUADS_VANILLA.sum());
        return o;
    }

    public static String line(JsonObject o) {
        return "block renderer " + (o.get("blockRendererOff").getAsBoolean() ? "off" : "on") + ", blocks Quicksilver/vanilla "
            + o.get("blocksQuicksilver") + "/" + o.get("blocksVanilla") + ", mesh quads Quicksilver/vanilla " + o.get("meshQuadsQuicksilver")
            + "/" + o.get("meshQuadsVanilla");
    }
}
