package com.meakaandre.ciserver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * CI only. Vanilla seeds every unseeded random source (each entity's, each level's own, a mob's spawn rolls) from the
 * clock and a global counter, so two runs of the same world never repeat. Here each thread gets its own counter and
 * every seed is a fixed mix of the thread's name and that counter: a thread that asks for seeds in the same order gets
 * the same seeds in every run. The server thread does almost all of it; with max.bg.threads=1 the one worldgen
 * worker is repeatable too. Applied in vanilla's runs and Quicksilver's alike.
 */
public final class SeedPin {
    private static final ConcurrentHashMap<String, AtomicLong> COUNTERS = new ConcurrentHashMap<>();
    private static volatile long epoch = 0x51C5_17EDL;

    private SeedPin() {
    }

    public static long next() {
        String thread = Thread.currentThread().getName();
        long n = COUNTERS.computeIfAbsent(thread, t -> new AtomicLong()).getAndIncrement();
        return mix(epoch ^ thread.hashCode() * 0x9E3779B97F4A7C15L ^ n * 0xC2B2AE3D27D4EB4FL);
    }

    /** Starts every thread's sequence again from a new fixed point (the scripted run's tick 0). */
    public static void reset(long newEpoch) {
        COUNTERS.clear();
        epoch = newEpoch;
    }

    public static long mix(long z) {
        z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
        return z ^ (z >>> 31);
    }
}
