package com.meakaandre.quicksilver.mesher;

import com.google.gson.JsonObject;
import com.meakaandre.quicksilver.bench.BenchRun;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** What Quicksilver's mesher did, for the bench and CI: since start-up, and over the current bench. */
public final class MesherStats {
    static final AtomicLong FALLBACKS = new AtomicLong();
    private static final AtomicLong MESHED = new AtomicLong(), MESHED_NANOS = new AtomicLong();
    private static final AtomicLong BENCH_MESHED = new AtomicLong(), BENCH_VANILLA = new AtomicLong();
    private static final Map<String, AtomicLong> VANILLA = new ConcurrentHashMap<>();
    // CI's per-section check
    static final AtomicLong CHECKED = new AtomicLong(), MISMATCHED = new AtomicLong(), CHECK_SKIPPED = new AtomicLong(), CHANGED_DURING_CHECK = new AtomicLong();
    static final AtomicLong CHECK_NANOS = new AtomicLong(), CHECK_MINE_NANOS = new AtomicLong(), CHECK_VANILLA_NANOS = new AtomicLong();
    static final AtomicLong CHECK_MINE_FIRST = new AtomicLong(), CHECK_MINE_SECOND = new AtomicLong(), CHECK_VANILLA_FIRST = new AtomicLong(),
        CHECK_VANILLA_SECOND = new AtomicLong(), CHECKED_MINE_FIRST = new AtomicLong(), CHECKED_VANILLA_FIRST = new AtomicLong();

    private MesherStats() {
    }

    // every like-for-like pair since start-up (ns): Quicksilver's and vanilla's compiler's time for the same section
    private static long[] pairMine = new long[4096], pairVanilla = new long[4096];
    private static int pairs;

    static synchronized void pair(long mine, long vanilla) {
        if (pairs == pairMine.length) {
            if (pairs >= 1 << 22) return;
            pairMine = java.util.Arrays.copyOf(pairMine, pairs * 2);
            pairVanilla = java.util.Arrays.copyOf(pairVanilla, pairs * 2);
        }
        pairMine[pairs] = mine;
        pairVanilla[pairs] = vanilla;
        pairs++;
    }

    /** Medians (ms) of Quicksilver's and vanilla's like-for-like times since start-up, and the number of pairs. */
    private static synchronized double[] medians() {
        if (pairs == 0) return new double[] {0, 0, 0};
        long[] m = java.util.Arrays.copyOf(pairMine, pairs), v = java.util.Arrays.copyOf(pairVanilla, pairs);
        java.util.Arrays.sort(m);
        java.util.Arrays.sort(v);
        return new double[] {median(m) / 1e6, median(v) / 1e6, pairs};
    }

    private static double median(long[] sorted) {
        int n = sorted.length;
        return n % 2 == 1 ? sorted[n / 2] : (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0;
    }

    static void meshed(long nanos) {
        ServerAware.meshed();
        MESHED.incrementAndGet();
        MESHED_NANOS.addAndGet(nanos);
        if (BenchRun.running()) BENCH_MESHED.incrementAndGet();
    }

    /** A section vanilla's compiler meshed while the mesher is on, and why. */
    static void vanilla(String why) {
        VANILLA.computeIfAbsent(why == null ? "unknown" : why, k -> new AtomicLong()).incrementAndGet();
        if (BenchRun.running()) BENCH_VANILLA.incrementAndGet();
    }

    private static final AtomicLong PH_N = new AtomicLong(), PH_SNAP = new AtomicLong(), PH_MESH = new AtomicLong(), PH_FINISH = new AtomicLong();

    /** Where a section's meshing time goes: the snapshot copy, the blocks and fluids, the visibility graph and buffers. */
    static void phases(long snapshot, long mesh, long finish) {
        PH_N.incrementAndGet();
        PH_SNAP.addAndGet(snapshot);
        PH_MESH.addAndGet(mesh);
        PH_FINISH.addAndGet(finish);
    }

    static void checkSkipped() {
        CHECK_SKIPPED.incrementAndGet();
    }

    public static void startBench() {
        BENCH_MESHED.set(0);
        BENCH_VANILLA.set(0);
        ServerAware.startBench();
        MeshPriority.startBench();
    }

    public static JsonObject json() {
        JsonObject o = new JsonObject();
        o.addProperty("state", Mesher.state());
        o.addProperty("reason", Mesher.reason());
        o.addProperty("fallbacks", FALLBACKS.get());
        o.addProperty("fluids", Mesher.fluidsThroughVanilla ? "vanilla's renderer" : "Quicksilver's");
        long m = MESHED.get();
        o.addProperty("sectionsMeshed", m);
        o.addProperty("msPerSectionSinceStart", m == 0 ? 0 : Math.round(MESHED_NANOS.get() / 1e3 / m) / 1000.0);
        long n = Math.max(1, PH_N.get());
        o.addProperty("msSnapshotPerSection", Math.round(PH_SNAP.get() / 1e3 / n) / 1000.0);
        o.addProperty("msBlocksAndFluidsPerSection", Math.round(PH_MESH.get() / 1e3 / n) / 1000.0);
        o.addProperty("msFinishPerSection", Math.round(PH_FINISH.get() / 1e3 / n) / 1000.0);
        o.addProperty("hiddenBlocksLeftOutSinceStart", HiddenBlocks.SKIPPED.sum());
        o.addProperty("hiddenBlocks", HiddenBlocks.off ? "asked of their models" : "left out");
        o.addProperty("meshingThreadsNow", Mesher.workersLive());
        o.addProperty("endedThreadsFreed", Mesher.WORKERS_FREED.get());
        o.addProperty("biomeTints", BiomeBlend.state());
        o.addProperty("biomeLookupsSinceStart", BiomeBlend.LOOKUPS.get());
        o.addProperty("biomeLookupsKeptSinceStart", BiomeBlend.KEPT.get());
        o.addProperty("keptBiomesChecked", BiomeBlend.CHECKED.get());
        o.addProperty("keptBiomesWhereTheChunkWentAway", BiomeBlend.CHANGED.get());
        o.addProperty("benchSectionsMeshed", BENCH_MESHED.get());
        o.addProperty("benchSectionsToVanilla", BENCH_VANILLA.get());
        JsonObject v = new JsonObject();
        for (Map.Entry<String, AtomicLong> e : new TreeMap<>(VANILLA).entrySet()) v.addProperty(e.getKey(), e.getValue().get());
        o.add("sectionsToVanillaSinceStart", v);
        o.addProperty("checked", CHECKED.get());
        o.addProperty("mismatched", MISMATCHED.get());
        o.addProperty("differedOnceThenSameWhenMeshedAgain", CHANGED_DURING_CHECK.get());
        o.addProperty("checkSkippedDuringBenches", CHECK_SKIPPED.get());
        long c = CHECKED.get();
        o.addProperty("checkMsPerSection", c == 0 ? 0 : Math.round(CHECK_NANOS.get() / 1e3 / c) / 1000.0);
        o.addProperty("check", Mesher.VERIFY ? "on" : "off");
        // like-for-like: every checked section meshed by both, one after the other on the same thread from the same copy
        o.addProperty("checkedQuicksilverMsPerSection", c == 0 ? 0 : Math.round(CHECK_MINE_NANOS.get() / 1e3 / c) / 1000.0);
        o.addProperty("checkedVanillaCompilerMsPerSection", c == 0 ? 0 : Math.round(CHECK_VANILLA_NANOS.get() / 1e3 / c) / 1000.0);
        long mf = Math.max(1, CHECKED_MINE_FIRST.get()), vf = Math.max(1, CHECKED_VANILLA_FIRST.get());
        o.addProperty("checkedQuicksilverFirst", "Quicksilver " + Math.round(CHECK_MINE_FIRST.get() / 1e3 / mf) / 1000.0 + " then vanilla "
            + Math.round(CHECK_VANILLA_SECOND.get() / 1e3 / mf) / 1000.0 + " ms (" + CHECKED_MINE_FIRST.get() + " sections)");
        o.addProperty("checkedVanillaFirst", "vanilla " + Math.round(CHECK_VANILLA_FIRST.get() / 1e3 / vf) / 1000.0 + " then Quicksilver "
            + Math.round(CHECK_MINE_SECOND.get() / 1e3 / vf) / 1000.0 + " ms (" + CHECKED_VANILLA_FIRST.get() + " sections)");
        double[] med = medians();
        // CI's gate (Andre, 2026-10-30): Quicksilver's median at or under vanilla's compiler's, same run, same thread
        o.addProperty("checkedMedianQuicksilverMs", Math.round(med[0] * 1000) / 1000.0);
        o.addProperty("checkedMedianVanillaCompilerMs", Math.round(med[1] * 1000) / 1000.0);
        o.addProperty("checkedPairs", (long) med[2]);
        o.add("serverAware", ServerAware.json());
        o.add("order", MeshPriority.json());
        return o;
    }

    public static String line(JsonObject o) {
        return o.get("state").getAsString() + (o.get("state").getAsString().equals("on") ? "" : " (" + o.get("reason").getAsString() + ")")
            + ", fallbacks " + o.get("fallbacks") + ", fluids " + o.get("fluids").getAsString() + ", this bench " + o.get("benchSectionsMeshed")
            + " sections meshed by Quicksilver and " + o.get("benchSectionsToVanilla") + " by vanilla's compiler; since start-up "
            + o.get("sectionsMeshed") + " at " + o.get("msPerSectionSinceStart") + " ms each" + (Mesher.VERIFY ? ", check: " + o.get("checked")
            + " sections checked, " + o.get("mismatched") + " mismatched" : "") + "; server-aware " + sa(o.getAsJsonObject("serverAware"));
    }

    private static String sa(JsonObject s) {
        return s.get("state").getAsString() + " (single player " + s.get("singlePlayer") + ", server tick ms avg " + s.get("benchServerTickMsAvg")
            + " of " + s.get("serverTickBudgetMs") + ", ticks with meshing backed off " + s.get("benchTicksWithMeshingBackedOff") + " of "
            + s.get("benchServerTicks") + ", meshing runs held back " + s.get("benchMeshingRunsHeldBack") + ")";
    }
}
