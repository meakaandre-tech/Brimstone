package com.meakaandre.brimstone.terrain;

import com.google.gson.JsonObject;
import com.meakaandre.brimstone.bench.GpuCounters;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * What Brimstone's terrain module did, for the bench. Totals since start-up (layers, vertices, precision) and, while a
 * bench measures ({@link GpuCounters#enabled}), meshing times and Brimstone's own terrain draws.
 */
public final class TerrainStats {
    static final AtomicLong LAYERS_TAKEN = new AtomicLong();
    static final AtomicLong LAYERS_KEPT_BY_VANILLA = new AtomicLong();
    /** Vertices converted, and how many of them had a position or a UV rounded to the 16-byte format's grid. */
    static final AtomicLong VERTICES = new AtomicLong();
    static final AtomicLong POSITIONS_ROUNDED = new AtomicLong();
    static final AtomicLong UVS_ROUNDED = new AtomicLong();
    /** Arena buffers grown into bigger ones since start-up. */
    static final AtomicLong ARENA_GROWS = new AtomicLong();

    // measuring window
    private static final LongArrayList COMPILE_NANOS = new LongArrayList();
    private static final AtomicLong TRANSCODE_NANOS = new AtomicLong();
    private static final AtomicLong TRANSCODED_LAYERS = new AtomicLong();
    static long windowDraws, windowSectionLayers;
    // package 4b: triangles Brimstone's terrain draws, against what drawing every layer whole (before 4b) would be
    private static long windowTriangles, windowTrianglesWhole, windowRanges, windowLayers, windowJumpFrames;
    private static final ThreadLocal<long[]> COMPILE_START = ThreadLocal.withInitial(() -> new long[1]);

    private TerrainStats() {
    }

    /** A bench starts measuring. */
    public static void startWindow() {
        synchronized (COMPILE_NANOS) {
            COMPILE_NANOS.clear();
        }
        TRANSCODE_NANOS.set(0);
        TRANSCODED_LAYERS.set(0);
        windowDraws = 0;
        windowSectionLayers = 0;
        windowTriangles = 0;
        windowTrianglesWhole = 0;
        windowRanges = 0;
        windowLayers = 0;
        windowJumpFrames = 0;
        UploadBudget.startWindow();
    }

    /** One section layer prepared for drawing while measuring (render thread): triangles drawn, whole, index ranges. */
    static void layerTriangles(long drawn, long whole, int ranges) {
        windowTriangles += drawn;
        windowTrianglesWhole += whole;
        windowRanges += ranges;
        windowLayers++;
    }

    /** A frame that drew every facing group because the camera jumped (render thread). */
    static void backFaceFrameSkipped() {
        if (GpuCounters.enabled) windowJumpFrames++;
    }

    /** The upload budget's measuring window (package 4b). */
    public static JsonObject uploadBudgetJson() {
        return UploadBudget.json();
    }

    /** Back-face skipping and triangles per measured frame (package 4b). */
    public static JsonObject backFacesJson(int frames) {
        JsonObject o = new JsonObject();
        String off = BackFaces.off();
        o.addProperty("state", off == null ? "on" : "off");
        o.addProperty("reason", off == null ? "on" : off);
        o.addProperty("trianglesPerFrame", frames == 0 ? 0 : round((double) windowTriangles / frames));
        o.addProperty("trianglesAllFacesPerFrame", frames == 0 ? 0 : round((double) windowTrianglesWhole / frames));
        o.addProperty("trianglesLeftOutPct", windowTrianglesWhole == 0 ? 0 : round(100.0 * (windowTrianglesWhole - windowTriangles) / windowTrianglesWhole));
        o.addProperty("rangesPerSectionLayer", windowLayers == 0 ? 0 : round((double) windowRanges / windowLayers));
        o.addProperty("framesAllFacesAfterJump", windowJumpFrames);
        // since start-up: layers kept in vanilla's order because an unaligned quad lies in an aligned quad's plane
        o.addProperty("layersKeptInVanillaOrder", CompactVertex.UNGROUPED_LAYERS.get());
        o.addProperty("note", "Brimstone's terrain triangles per frame (solid and cutout after leaving out facing groups that face away"
            + " from the camera, plus translucent whole); trianglesAllFacesPerFrame is the same frames with every layer drawn whole, as"
            + " before package 4b");
        return o;
    }

    /** Start of vanilla's SectionCompiler.compile (worker threads, or the render thread for nearby sections). */
    public static void compileStart() {
        COMPILE_START.get()[0] = GpuCounters.enabled ? System.nanoTime() : 0;
    }

    public static void compileEnd() {
        long[] s = COMPILE_START.get();
        if (s[0] != 0 && GpuCounters.enabled) {
            long d = System.nanoTime() - s[0];
            synchronized (COMPILE_NANOS) {
                COMPILE_NANOS.add(d);
            }
        }
        s[0] = 0;
    }

    static void converted(CompactVertex.Converted c) {
        VERTICES.addAndGet(c.vertices());
        POSITIONS_ROUNDED.addAndGet(c.positionsRounded());
        UVS_ROUNDED.addAndGet(c.uvsRounded());
    }

    static void transcoded(long nanos) {
        if (!GpuCounters.enabled) return;
        TRANSCODE_NANOS.addAndGet(nanos);
        TRANSCODED_LAYERS.incrementAndGet();
    }

    static void drew(int draws, int sectionLayers) {
        if (!GpuCounters.enabled) return;
        windowDraws += draws;
        windowSectionLayers += sectionLayers;
    }

    /** Meshing time per section in the measuring window: vanilla's mesher (with FRAPI) plus Brimstone's transcode. */
    public static JsonObject meshingJson() {
        long[] c;
        synchronized (COMPILE_NANOS) {
            c = COMPILE_NANOS.toLongArray();
        }
        Arrays.sort(c);
        JsonObject o = new JsonObject();
        o.addProperty("sections", c.length);
        long sum = 0;
        for (long v : c) sum += v;
        long layers = TRANSCODED_LAYERS.get();
        long transcode = TRANSCODE_NANOS.get();
        o.addProperty("transcodedLayers", layers);
        if (c.length == 0) {
            o.add("compileMsAvg", null);
            o.add("compileMsP50", null);
            o.add("compileMsP95", null);
            o.add("transcodeMsPerLayer", layers == 0 ? null : new com.google.gson.JsonPrimitive(round(transcode / 1e6 / layers)));
            o.add("msPerSection", null);
        } else {
            o.addProperty("compileMsAvg", round(sum / 1e6 / c.length));
            o.addProperty("compileMsP50", round(c[(c.length - 1) / 2] / 1e6));
            o.addProperty("compileMsP95", round(c[Math.min(c.length - 1, (int) Math.ceil(c.length * 0.95) - 1)] / 1e6));
            o.add("transcodeMsPerLayer", layers == 0 ? null : new com.google.gson.JsonPrimitive(round(transcode / 1e6 / layers)));
            o.addProperty("msPerSection", round((sum + transcode) / 1e6 / c.length));
        }
        o.addProperty("note", "vanilla's SectionCompiler on its worker threads (blocks through Brimstone's Fabric Renderer API"
            + " implementation), plus Brimstone's transcode into its 16-byte vertex on the same thread; sections meshed while measuring");
        return o;
    }

    /** State of the module and its buffers, and its draws per measured frame. */
    public static JsonObject terrainJson(int frames) {
        JsonObject o = new JsonObject();
        o.addProperty("state", Terrain.state().name().toLowerCase(java.util.Locale.ROOT));
        o.addProperty("reason", Terrain.reason());
        o.addProperty("fallbacks", Terrain.fallbacks());
        o.addProperty("plainDrawPath", TerrainRenderer.plainPathName());
        o.addProperty("lastDrawPath", TerrainRenderer.lastPathName());
        o.addProperty("vertexBytes", CompactVertex.BYTES);
        o.addProperty("vanillaVertexBytes", CompactVertex.VANILLA_BYTES);
        o.addProperty("regionSections", "8x4x8");
        TerrainStore.Snapshot s = TerrainStore.snapshot();
        o.addProperty("regions", s.regions());
        o.addProperty("arenas", s.arenas());
        o.addProperty("arenaMB", round(s.arenaBytes() / MB));
        o.addProperty("usedMB", round(s.usedBytes() / MB));
        o.addProperty("arenaGrows", ARENA_GROWS.get());
        o.addProperty("liveVertices", s.vertices());
        o.addProperty("vertexMB", round(s.vertices() * (double) CompactVertex.BYTES / MB));
        o.addProperty("vanillaEquivalentMB", round(s.vertices() * (double) CompactVertex.VANILLA_BYTES / MB));
        o.addProperty("layersTaken", LAYERS_TAKEN.get());
        o.addProperty("layersKeptByVanilla", LAYERS_KEPT_BY_VANILLA.get());
        // layers with a vertex alpha below 255 (Brimstone's vertex has no alpha byte); CI fails on any
        o.addProperty("alphaLayersKeptByVanilla", Terrain.ALPHA_LAYERS_KEPT_BY_VANILLA.get());
        o.addProperty("translucentHandedBackForAlpha", Terrain.translucentHandedBackForAlpha());
        long v = VERTICES.get();
        o.addProperty("verticesConverted", v);
        o.addProperty("verticesWithPositionRounded", POSITIONS_ROUNDED.get());
        o.addProperty("verticesWithUvRounded", UVS_ROUNDED.get());
        o.addProperty("positionRoundedPct", v == 0 ? 0 : round(POSITIONS_ROUNDED.get() * 100.0 / v));
        o.addProperty("drawCallsPerFrame", frames == 0 ? 0 : round((double) windowDraws / frames));
        o.addProperty("sectionLayersPerFrame", frames == 0 ? 0 : round((double) windowSectionLayers / frames));
        return o;
    }

    private static final double MB = 1024.0 * 1024.0;

    private static double round(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}
