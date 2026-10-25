package com.meakaandre.brimstone.terrain;

import com.google.gson.JsonObject;
import com.meakaandre.brimstone.bench.GpuCounters;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * What Brimstone did with translucent terrain (package 3), for the bench: vanilla's sorting (how long it takes on which
 * thread, how many new index lists Brimstone took and uploaded), Brimstone's translucent draws per frame, frames drawn
 * with Improved Transparency, frames where vanilla still drew translucent sections of its own next to Brimstone's,
 * and CI's per-frame check ({@link TranslucencyCheck}). Window counts are kept while a bench measures.
 */
public final class TranslucencyStats {
    // since start-up
    private static final AtomicLong LAYERS_TAKEN = new AtomicLong();
    private static final AtomicLong RESORTS_TAKEN = new AtomicLong();
    private static final AtomicLong RESORTS_DROPPED = new AtomicLong();
    private static long resortsUploaded;
    private static long violationsSinceStart, mixedFramesSinceStart, handedOverSinceStart;

    // measuring window
    private static final LongArrayList SORT_NANOS = new LongArrayList();
    private static final AtomicLong SORTS_ON_RENDER_THREAD = new AtomicLong();
    private static final AtomicLong WINDOW_RESORTS_TAKEN = new AtomicLong();
    private static long windowResortsUploaded;
    private static long frames, drawCalls, sectionLayers, oitFrames, mixedFrames, vanillaSectionLayers, handedOver;
    private static long checkFrames, entriesChecked, entriesUnchecked, violations;
    private static long breaksArena, breaksIndexType, breaksRegion;
    private static final ThreadLocal<long[]> SORT_START = ThreadLocal.withInitial(() -> new long[1]);

    private TranslucencyStats() {
    }

    public static void startWindow() {
        synchronized (SORT_NANOS) {
            SORT_NANOS.clear();
        }
        SORTS_ON_RENDER_THREAD.set(0);
        WINDOW_RESORTS_TAKEN.set(0);
        windowResortsUploaded = 0;
        frames = drawCalls = sectionLayers = oitFrames = mixedFrames = vanillaSectionLayers = handedOver = 0;
        checkFrames = entriesChecked = entriesUnchecked = violations = 0;
        breaksArena = breaksIndexType = breaksRegion = 0;
    }

    /** {@code MeshData.SortState.buildSortedIndexBuffer}: vanilla sorts a translucent layer's quads (any thread). */
    public static void sortStart() {
        SORT_START.get()[0] = GpuCounters.enabled ? System.nanoTime() : 0;
    }

    public static void sortEnd() {
        long[] s = SORT_START.get();
        if (s[0] != 0 && GpuCounters.enabled) {
            long d = System.nanoTime() - s[0];
            synchronized (SORT_NANOS) {
                SORT_NANOS.add(d);
            }
            if (RenderSystem.isOnRenderThread()) SORTS_ON_RENDER_THREAD.incrementAndGet();
        }
        s[0] = 0;
    }

    static void layerTaken() {
        LAYERS_TAKEN.incrementAndGet();
    }

    static void resortTaken(boolean staged) {
        if (staged) {
            RESORTS_TAKEN.incrementAndGet();
            if (GpuCounters.enabled) WINDOW_RESORTS_TAKEN.incrementAndGet();
        } else {
            RESORTS_DROPPED.incrementAndGet();
        }
    }

    static void resortDropped() {
        RESORTS_DROPPED.incrementAndGet();
    }

    /** Render thread, under the store's lock. */
    static void resortUploaded() {
        resortsUploaded++;
        if (GpuCounters.enabled) windowResortsUploaded++;
    }

    /**
     * Render thread, once per frame as translucent draws are prepared: section layers Brimstone draws, those vanilla
     * still draws itself, and how many sections were sent back to vanilla's mesher so Brimstone takes them over.
     */
    static void frame(int brimstoneLayers, int vanillaLayers, int handed) {
        boolean mixed = brimstoneLayers > 0 && vanillaLayers > 0;
        if (mixed) mixedFramesSinceStart++;
        handedOverSinceStart += handed;
        if (!GpuCounters.enabled) return;
        frames++;
        sectionLayers += brimstoneLayers;
        vanillaSectionLayers += vanillaLayers;
        handedOver += handed;
        if (mixed) mixedFrames++;
    }

    static void drew(int calls) {
        if (!GpuCounters.enabled) return;
        drawCalls += calls;
    }

    /** Render thread: a translucent run ended because the next section layer is in another arena, index type or region. */
    static void runBreak(boolean arena, boolean indexType, boolean region) {
        if (!GpuCounters.enabled) return;
        if (arena) breaksArena++;
        if (indexType) breaksIndexType++;
        if (region) breaksRegion++;
    }

    static void oitFrame() {
        if (GpuCounters.enabled) oitFrames++;
    }

    static void checkFrame() {
        if (GpuCounters.enabled) checkFrames++;
    }

    static void entryChecked() {
        if (GpuCounters.enabled) entriesChecked++;
    }

    static void entryUnchecked() {
        if (GpuCounters.enabled) entriesUnchecked++;
    }

    static void violation() {
        violationsSinceStart++;
        if (GpuCounters.enabled) violations++;
    }

    public static JsonObject json() {
        long[] c;
        synchronized (SORT_NANOS) {
            c = SORT_NANOS.toLongArray();
        }
        Arrays.sort(c);
        long sum = 0;
        for (long v : c) sum += v;
        JsonObject o = new JsonObject();
        o.addProperty("dryRun", Terrain.TRANSLUCENT_DRY_RUN);
        o.addProperty("layersTaken", LAYERS_TAKEN.get());
        o.addProperty("framesMeasured", frames);
        o.addProperty("sectionLayersPerFrame", per(sectionLayers));
        o.addProperty("drawCallsPerFrame", per(drawCalls));
        o.addProperty("runBreaksPerFrameArena", per(breaksArena));
        o.addProperty("runBreaksPerFrameIndexType", per(breaksIndexType));
        o.addProperty("runBreaksPerFrameRegion", per(breaksRegion));
        o.addProperty("vanillaSectionLayersPerFrame", per(vanillaSectionLayers));
        o.addProperty("mixedFrames", mixedFrames);
        o.addProperty("mixedFramesSinceStart", mixedFramesSinceStart);
        o.addProperty("sectionsHandedOver", handedOver);
        o.addProperty("sectionsHandedOverSinceStart", handedOverSinceStart);
        o.addProperty("oitFrames", oitFrames);
        o.addProperty("sorts", c.length);
        o.addProperty("sortsOnRenderThread", SORTS_ON_RENDER_THREAD.get());
        o.add("sortMsAvg", c.length == 0 ? null : new com.google.gson.JsonPrimitive(round(sum / 1e6 / c.length)));
        o.add("sortMsP95", c.length == 0 ? null : new com.google.gson.JsonPrimitive(round(c[Math.min(c.length - 1, (int) Math.ceil(c.length * 0.95) - 1)] / 1e6)));
        o.add("sortMsMax", c.length == 0 ? null : new com.google.gson.JsonPrimitive(round(c[c.length - 1] / 1e6)));
        o.addProperty("sortMsTotal", round(sum / 1e6));
        o.addProperty("resortsTaken", WINDOW_RESORTS_TAKEN.get());
        o.addProperty("resortsUploaded", windowResortsUploaded);
        o.addProperty("resortsTakenSinceStart", RESORTS_TAKEN.get());
        o.addProperty("resortsUploadedSinceStart", resortsUploaded);
        o.addProperty("resortsDroppedSinceStart", RESORTS_DROPPED.get());
        o.addProperty("verify", TranslucencyCheck.ENABLED);
        o.addProperty("checkFrames", checkFrames);
        o.addProperty("entriesChecked", entriesChecked);
        o.addProperty("entriesUnchecked", entriesUnchecked);
        o.addProperty("violations", violations);
        o.addProperty("violationsSinceStart", violationsSinceStart);
        o.addProperty("note", "sorts: vanilla's own (MeshData.SortState.buildSortedIndexBuffer) while measuring, on vanilla's worker threads"
            + " unless counted as render thread (vanilla's synchronous builds of nearby sections); Brimstone takes each new index list"
            + " and uploads only that list at the end of the frame, where vanilla uploads its own");
        return o;
    }

    public static String line(JsonObject o) {
        return (o.get("dryRun").getAsBoolean() ? "DRY RUN, " : "") + "section layers/frame " + o.get("sectionLayersPerFrame")
            + " in " + o.get("drawCallsPerFrame") + " draws (vanilla's own " + o.get("vanillaSectionLayersPerFrame") + ", mixed frames "
            + o.get("mixedFrames") + "), OIT frames " + o.get("oitFrames") + ", sorts " + o.get("sorts") + " (ms avg " + o.get("sortMsAvg")
            + ", p95 " + o.get("sortMsP95") + ", max " + o.get("sortMsMax") + "; on the render thread " + o.get("sortsOnRenderThread")
            + "), new index lists uploaded " + o.get("resortsUploaded") + ", check " + (o.get("verify").getAsBoolean() ? "on" : "off")
            + " (" + o.get("entriesChecked") + " checked, " + o.get("violations") + " violations, " + o.get("violationsSinceStart") + " since start-up)";
    }

    private static double per(long v) {
        return frames == 0 ? 0 : round((double) v / frames);
    }

    private static double round(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}
