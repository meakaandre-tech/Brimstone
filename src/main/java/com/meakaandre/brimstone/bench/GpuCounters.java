package com.meakaandre.brimstone.bench;

import java.util.concurrent.atomic.LongAdder;

/**
 * Running totals of what goes through 26.3's graphics layer, filled by the counting mixins. Counting only: nothing
 * here changes what is drawn. Totals only move while {@link #enabled} is set (during a bench run).
 *
 * <p>Uploads nest (an upload into transient memory may stage through a mapped block, a buffer write may stage through
 * transient memory), so only the outermost upload call on a thread is counted: {@link #enter()} at the start of each
 * counted method, {@link #exit()} at each of its returns, and {@link #resetDepth()} at every frame boundary.
 */
public final class GpuCounters {
    /** Kinds of draw call, one per backend entry point. */
    public enum Draw { SINGLE, MULTI, INDIRECT }

    /** Kinds of CPU-to-GPU upload. */
    public enum Upload { WRITE_BUFFER, WRITE_TEXTURE, CREATE_BUFFER, TRANSIENT, MAP_WRITE }

    public static volatile boolean enabled;

    static final LongAdder[] DRAWS = adders(Draw.values().length);
    static final LongAdder[] UPLOADS = adders(Upload.values().length);
    static final LongAdder[] UPLOAD_BYTES = adders(Upload.values().length);

    private static final ThreadLocal<int[]> DEPTH = ThreadLocal.withInitial(() -> new int[1]);

    private GpuCounters() {
    }

    public static void draw(Draw kind) {
        if (enabled) DRAWS[kind.ordinal()].increment();
    }

    /** Marks entry into an upload call; true if it is the outermost one on this thread (the one to count). */
    public static boolean enter() {
        return DEPTH.get()[0]++ == 0;
    }

    public static void exit() {
        int[] d = DEPTH.get();
        if (d[0] > 0) d[0]--;
    }

    /** At a frame boundary no upload is in progress on this thread: clears a depth an exception left behind. */
    public static void resetDepth() {
        DEPTH.get()[0] = 0;
    }

    public static void upload(Upload kind, long bytes) {
        if (!enabled) return;
        UPLOADS[kind.ordinal()].increment();
        if (bytes > 0) UPLOAD_BYTES[kind.ordinal()].add(bytes);
    }

    static long[] snapshot(LongAdder[] adders) {
        long[] out = new long[adders.length];
        for (int i = 0; i < adders.length; i++) out[i] = adders[i].sum();
        return out;
    }

    private static LongAdder[] adders(int n) {
        LongAdder[] a = new LongAdder[n];
        for (int i = 0; i < n; i++) a[i] = new LongAdder();
        return a;
    }
}
