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
    /** Vertices meshed into each format, and layers that went wide because of position, UV or alpha. */
    static final AtomicLong COMPACT_VERTICES = new AtomicLong();
    static final AtomicLong WIDE_VERTICES = new AtomicLong();
    static final AtomicLong WIDE_FOR_POSITION = new AtomicLong();
    static final AtomicLong WIDE_FOR_UV = new AtomicLong();
    static final AtomicLong WIDE_FOR_ALPHA = new AtomicLong();
    /** Translucent vertices meshed (all stored wide since package 3's review) and how many would have fit compact. */
    static final AtomicLong TRANSLUCENT_VERTICES = new AtomicLong();
    static final AtomicLong TRANSLUCENT_COMPACT_FIT = new AtomicLong();
    private static volatile boolean uvLogged;

    // measuring window
    private static final LongArrayList COMPILE_NANOS = new LongArrayList();
    private static final AtomicLong TRANSCODE_NANOS = new AtomicLong();
    private static final AtomicLong TRANSCODED_LAYERS = new AtomicLong();
    static long windowDraws, windowSectionLayers;
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

    static void translucentSplit(int vertices, boolean wouldFitCompact) {
        TRANSLUCENT_VERTICES.addAndGet(vertices);
        if (wouldFitCompact) TRANSLUCENT_COMPACT_FIT.addAndGet(vertices);
    }

    static void split(CompactVertex.Split sp) {
        COMPACT_VERTICES.addAndGet(sp.compactVertices());
        WIDE_VERTICES.addAndGet(sp.wideVertices());
        int why = sp.why();
        if ((why & CompactVertex.WHY_POSITION) != 0) WIDE_FOR_POSITION.incrementAndGet();
        if ((why & CompactVertex.WHY_UV) != 0) {
            WIDE_FOR_UV.incrementAndGet();
            if (!uvLogged) {
                uvLogged = true;
                com.meakaandre.brimstone.BrimstoneClient.LOG.info("Brimstone terrain: first layer with UVs off the compact grid;"
                    + " it takes the wide vertex format (exact), as will any others (counted in the bench's terrain block)");
            }
        }
        if ((why & CompactVertex.WHY_ALPHA) != 0) WIDE_FOR_ALPHA.incrementAndGet();
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
        o.addProperty("note", "vanilla's SectionCompiler on its worker threads (FRAPI models through Indigo), plus Brimstone's"
            + " transcode into its 16-byte vertex on the same thread; sections meshed while measuring");
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
        long live = s.compactVertices() + s.wideVertices();
        o.addProperty("wideVertexBytes", CompactVertex.WIDE_BYTES);
        o.addProperty("liveVertices", live);
        o.addProperty("liveCompactVertices", s.compactVertices());
        o.addProperty("liveWideVertices", s.wideVertices());
        o.addProperty("widePct", live == 0 ? 0 : round(s.wideVertices() * 100.0 / live));
        o.addProperty("bytesPerVertex", live == 0 ? 0 : round((s.compactVertices() * (double) CompactVertex.BYTES
            + s.wideVertices() * (double) CompactVertex.WIDE_BYTES) / live));
        o.addProperty("vanillaEquivalentMB", round(live * (double) CompactVertex.VANILLA_BYTES / MB));
        o.addProperty("layersTaken", LAYERS_TAKEN.get());
        o.addProperty("layersKeptByVanilla", LAYERS_KEPT_BY_VANILLA.get());
        o.addProperty("verticesMeshedCompact", COMPACT_VERTICES.get());
        o.addProperty("verticesMeshedWide", WIDE_VERTICES.get());
        o.addProperty("layersWithWideForPosition", WIDE_FOR_POSITION.get());
        o.addProperty("layersWithWideForUv", WIDE_FOR_UV.get());
        o.addProperty("layersWithWideForAlpha", WIDE_FOR_ALPHA.get());
        long tv = TRANSLUCENT_VERTICES.get(), tfit = TRANSLUCENT_COMPACT_FIT.get();
        o.addProperty("translucentVerticesMeshed", tv);
        o.addProperty("translucentVerticesFitCompact", tfit);
        // translucent layers are stored wide (28 bytes); with the per-layer choice they would average this many bytes
        o.addProperty("translucentBytesPerVertexIfPerLayer", tv == 0 ? 0 : round((tfit * (double) CompactVertex.BYTES
            + (tv - tfit) * (double) CompactVertex.WIDE_BYTES) / tv));
        o.addProperty("drawCallsPerFrame", frames == 0 ? 0 : round((double) windowDraws / frames));
        o.addProperty("sectionLayersPerFrame", frames == 0 ? 0 : round((double) windowSectionLayers / frames));
        return o;
    }

    private static final double MB = 1024.0 * 1024.0;

    private static double round(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}
