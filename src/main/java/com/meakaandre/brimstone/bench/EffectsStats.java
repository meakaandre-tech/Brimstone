package com.meakaandre.brimstone.bench;

import com.google.gson.JsonObject;
import java.util.Locale;

/**
 * Package 8's measurements of vanilla's effects, for the bench: draw calls per frame by what they draw for (texture
 * animation, particles, sky, weather, clouds; {@link GpuCounters.Phase}), live particles, particles drawn per frame
 * and the render thread's time in vanilla's particle tick and extraction. Counting only; nothing here changes what is
 * drawn. Render thread.
 */
public final class EffectsStats {
    private static boolean measuring;
    private static long ticks, aliveSum, aliveMax, tickNanos, tickStart;
    private static long extracts, drawnSum, extractNanos, extractStart;

    private EffectsStats() {
    }

    static void startWindow() {
        ticks = aliveSum = aliveMax = tickNanos = 0;
        extracts = drawnSum = extractNanos = 0;
        measuring = true;
    }

    static void stopWindow() {
        measuring = false;
    }

    public static void particleTickStart() {
        if (measuring) tickStart = System.nanoTime();
    }

    /** After vanilla's particle tick: how many particles are alive. */
    public static void particleTickEnd(int alive) {
        if (!measuring || tickStart == 0) return;
        tickNanos += System.nanoTime() - tickStart;
        tickStart = 0;
        ticks++;
        aliveSum += alive;
        aliveMax = Math.max(aliveMax, alive);
    }

    public static void extractStart() {
        if (measuring) extractStart = System.nanoTime();
    }

    /** After vanilla's particle extraction: how many particles it kept for drawing (inside the frustum). */
    public static void extractEnd(int drawn) {
        if (!measuring || extractStart == 0) return;
        extractNanos += System.nanoTime() - extractStart;
        extractStart = 0;
        extracts++;
        drawnSum += drawn;
    }

    static JsonObject json(int frames) {
        JsonObject o = new JsonObject();
        JsonObject draws = new JsonObject();
        for (GpuCounters.Phase p : GpuCounters.Phase.values()) {
            long[] a = FrameRecorder.startPhase, z = FrameRecorder.endPhase;
            long d = a == null || z == null ? 0 : z[p.ordinal()] - a[p.ordinal()];
            draws.addProperty(p.name().toLowerCase(Locale.ROOT), frames == 0 ? 0 : round((double) d / frames));
        }
        o.add("drawCallsPerFrame", draws);
        JsonObject particles = new JsonObject();
        particles.addProperty("aliveAvg", ticks == 0 ? 0 : round((double) aliveSum / ticks));
        particles.addProperty("aliveMax", aliveMax);
        particles.addProperty("drawnPerFrame", extracts == 0 ? 0 : round((double) drawnSum / extracts));
        particles.addProperty("tickMsPerTick", ticks == 0 ? 0 : round(tickNanos / 1e6 / ticks));
        particles.addProperty("extractMsPerFrame", extracts == 0 ? 0 : round(extractNanos / 1e6 / extracts));
        o.add("particles", particles);
        return o;
    }

    static String line(JsonObject o) {
        JsonObject p = o.getAsJsonObject("particles");
        return "draws/frame " + o.get("drawCallsPerFrame") + "; particles alive avg " + p.get("aliveAvg") + " (max " + p.get("aliveMax")
            + "), drawn/frame " + p.get("drawnPerFrame") + ", tick " + p.get("tickMsPerTick") + " ms/tick, extract "
            + p.get("extractMsPerFrame") + " ms/frame";
    }

    private static double round(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}
