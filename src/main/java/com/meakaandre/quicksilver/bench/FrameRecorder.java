package com.meakaandre.quicksilver.bench;

import it.unimi.dsi.fastutil.longs.LongArrayList;

/**
 * Frame times and per-frame counts while a bench is measuring. A frame is the time between two presents; the counts
 * that happened in between belong to the frame that ends at the second present.
 */
public final class FrameRecorder {
    private static volatile boolean recording;
    private static long lastPresent;
    private static long[] lastDraws, lastUploads, lastBytes, lastPhase;

    static final LongArrayList FRAME_NANOS = new LongArrayList();
    static final LongArrayList DRAWS = new LongArrayList();
    static final LongArrayList UPLOADS = new LongArrayList();
    static final LongArrayList BYTES = new LongArrayList();
    static long[] startDraws, startUploads, startBytes, endDraws, endUploads, endBytes;
    /** Draws by {@link GpuCounters.Phase} at the first and last measured present (package 8). */
    static long[] startPhase, endPhase;

    private FrameRecorder() {
    }

    static void start() {
        for (BenchModules.Section sec : BenchModules.all()) sec.startWindow();
        EffectsStats.startWindow();
        com.meakaandre.quicksilver.gui.GuiStats.startWindow();
        FRAME_NANOS.clear();
        DRAWS.clear();
        UPLOADS.clear();
        BYTES.clear();
        lastPresent = 0;
        GpuCounters.enabled = true;
        lastDraws = startDraws = GpuCounters.snapshot(GpuCounters.DRAWS);
        lastUploads = startUploads = GpuCounters.snapshot(GpuCounters.UPLOADS);
        lastBytes = startBytes = GpuCounters.snapshot(GpuCounters.UPLOAD_BYTES);
        lastPhase = startPhase = GpuCounters.snapshot(GpuCounters.PHASE_DRAWS);
        endPhase = null;
        recording = true;
    }

    /** Stops at the last whole frame; the counts after it are left out like the partial frame itself. */
    static void stop() {
        recording = false;
        EffectsStats.stopWindow();
        GpuCounters.enabled = false;
        endPhase = lastPhase;
        com.meakaandre.quicksilver.gui.GuiStats.stopWindow();
        endDraws = lastDraws;
        endUploads = lastUploads;
        endBytes = lastBytes;
    }

    /** Called from the frontend surface's present, on the render thread. */
    public static void onPresent() {
        GpuCounters.resetDepth();
        com.meakaandre.quicksilver.gui.GuiStats.frameEnd();
        if (!recording) return;
        long now = System.nanoTime();
        long[] d = GpuCounters.snapshot(GpuCounters.DRAWS);
        long[] u = GpuCounters.snapshot(GpuCounters.UPLOADS);
        long[] b = GpuCounters.snapshot(GpuCounters.UPLOAD_BYTES);
        long[] ph = GpuCounters.snapshot(GpuCounters.PHASE_DRAWS);
        if (lastPresent != 0) {
            FRAME_NANOS.add(now - lastPresent);
            DRAWS.add(sum(d) - sum(lastDraws));
            UPLOADS.add(sum(u) - sum(lastUploads));
            BYTES.add(sum(b) - sum(lastBytes));
        } else {
            // counts before the first present belong to no measured frame
            startDraws = d;
            startUploads = u;
            startBytes = b;
            startPhase = ph;
        }
        lastPresent = now;
        lastDraws = d;
        lastUploads = u;
        lastBytes = b;
        lastPhase = ph;
    }

    static long sum(long[] a) {
        long s = 0;
        for (long v : a) s += v;
        return s;
    }
}
