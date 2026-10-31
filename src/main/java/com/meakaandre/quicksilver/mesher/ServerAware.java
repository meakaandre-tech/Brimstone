package com.meakaandre.quicksilver.mesher;

import com.google.gson.JsonObject;
import com.meakaandre.quicksilver.QuicksilverClient;
import com.meakaandre.quicksilver.bench.BenchRun;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

/**
 * Server-aware meshing threads (design doc, Beyond Sodium, part of package 4c): in single player the integrated server
 * shares the CPU with meshing. While the server falls behind on its ticks (its tick takes most of its tick budget),
 * meshing backs off to one section at a time, so machines and the world don't lag; when the server has room again it
 * goes back to vanilla's number of meshing threads and catches up. The camera's own nearby sections that vanilla meshes
 * on the render thread are never held back.
 *
 * <p>How it holds back: vanilla starts each section's meshing from its dispatcher's task loop on its worker threads; a
 * loop run that would start one more section while the limit is reached returns at once, and the render thread starts
 * the loop again each frame while sections wait and the limit allows (see {@code SectionRenderDispatcherMesherMixin}).
 */
public final class ServerAware {
    private ServerAware() {
    }

    /** Back off when the server's tick takes this much of its budget (averaged), and come back below the second. */
    private static final double BEHIND = 0.8, CAUGHT_UP = 0.6;
    /** Debug: never back off (CI's A/B runs and the laptop's comparison). */
    private static final boolean DISABLED = Boolean.getBoolean("quicksilver.debug.serverAwareOff")
        || Boolean.getBoolean("quicksilver.ci.measureOnly");

    private static volatile boolean behind;
    private static volatile double tickMsAvg = -1;
    private static volatile float tickBudgetMs = 50f;
    private static long tickStart;
    /** Meshing loop runs in progress (each meshes at most one section). */
    private static final AtomicInteger RUNNING = new AtomicInteger();
    /** Set when a loop run returned because of the limit: the render thread starts it again when the limit allows. */
    static volatile boolean heldBack;
    static volatile boolean hookSeen;

    // counts since start-up, and in the current bench
    private static final AtomicLong HELD = new AtomicLong(), BENCH_HELD = new AtomicLong();
    private static final AtomicLong RESTARTS = new AtomicLong();
    // while behind, in the current bench: sections meshed, the most loop runs at once, and the time no run was meshing
    // although runs had been turned away (work waiting), which the restart in exit() is there to keep near zero
    private static final AtomicLong BENCH_MESHED_BEHIND = new AtomicLong(), BENCH_IDLE_BEHIND_NS = new AtomicLong();
    private static final AtomicInteger BENCH_MAX_RUNNING_BEHIND = new AtomicInteger();
    private static volatile long idleSince;
    // time behind inside the current bench: each tick's span (end to end) counted when the tick ended behind
    private static long lastTickEndNs, benchBehindOnlyNs;
    private static long behindSinceNs, behindNanos, benchBehindNanos, benchStartNs;
    private static int ticks, benchTicks, benchTicksBehind;
    private static double benchTickMsSum;

    /** Client start: listens to the integrated server's ticks (a dedicated server's events never reach a client). */
    public static void init() {
        ServerTickEvents.START_SERVER_TICK.register(s -> tickStart = System.nanoTime());
        ServerTickEvents.END_SERVER_TICK.register(ServerAware::endTick);
        // a new world starts with no history: the last world's average must not hold meshing back
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPED.register(s -> {
            behind = false;
            tickMsAvg = -1;
            tickStart = 0;
        });
        logOnce();
    }

    private static void endTick(MinecraftServer server) {
        if (tickStart == 0 || server.isDedicatedServer()) return;
        double ms = (System.nanoTime() - tickStart) / 1e6;
        float budget;
        try {
            budget = server.tickRateManager().millisecondsPerTick();
        } catch (Throwable t) {
            budget = 50f;
        }
        tickBudgetMs = budget;
        double avg = tickMsAvg < 0 ? ms : tickMsAvg * 0.8 + ms * 0.2;
        tickMsAvg = avg;
        ticks++;
        boolean was = behind;
        boolean now = was ? avg > budget * CAUGHT_UP : avg > budget * BEHIND;
        long t = System.nanoTime();
        if (now && !was) behindSinceNs = t;
        if (!now && was) {
            behindNanos += t - behindSinceNs;
            // an idle stretch still open when the server catches up ends here: no time after it counts as behind
            long since = idleSince;
            if (since != 0) {
                idleSince = 0;
                if (BenchRun.running()) BENCH_IDLE_BEHIND_NS.addAndGet(t - since);
            }
        }
        behind = now && !DISABLED;
        long prevEnd = lastTickEndNs;
        lastTickEndNs = t;
        if (BenchRun.running()) {
            // the span since the last tick ended counts when the server was behind during it (the state before this
            // tick's end), and only from the bench's start
            if (was && prevEnd != 0) benchBehindOnlyNs += t - Math.max(prevEnd, benchStartNs);
            benchTicks++;
            benchTickMsSum += ms;
            if (now) benchTicksBehind++;
        }
    }

    /** How many sections may be meshing at once right now (vanilla's own number unless the server is behind). */
    static int limit() {
        return behind && singlePlayer() ? 1 : Integer.MAX_VALUE;
    }

    private static boolean singlePlayer() {
        return net.minecraft.client.Minecraft.getInstance().getSingleplayerServer() != null;
    }

    /** A meshing loop run starts on a worker: false if it must return at once (the limit is reached). */
    public static boolean enter() {
        hookSeen = true;
        int lim = limit();
        while (true) {
            int r = RUNNING.get();
            if (r >= lim) {
                heldBack = true;
                HELD.incrementAndGet();
                if (BenchRun.running()) BENCH_HELD.incrementAndGet();
                return false;
            }
            if (RUNNING.compareAndSet(r, r + 1)) {
                if (behind && BenchRun.running()) {
                    long since = idleSince;
                    if (since != 0) {
                        idleSince = 0;
                        BENCH_IDLE_BEHIND_NS.addAndGet(System.nanoTime() - since);
                    }
                    BENCH_MAX_RUNNING_BEHIND.accumulateAndGet(r + 1, Math::max);
                }
                return true;
            }
        }
    }

    /**
     * A meshing loop run that {@link #enter} let through ended: true if the caller should start the loop again (runs
     * were turned away meanwhile and the limit allows one now). Vanilla's loop starts its next run before this one has
     * ended, so with a limit of one that next run is turned away; without this, meshing would wait for the render
     * thread's restart at the end of the frame.
     */
    public static boolean exit() {
        int left = RUNNING.decrementAndGet();
        if (left == 0 && behind && heldBack && BenchRun.running()) idleSince = System.nanoTime();
        if (heldBack && left < limit()) {
            heldBack = false;
            RESTARTS.incrementAndGet();
            return true;
        }
        return false;
    }

    /** Render thread, once a frame: how many loop runs to start again (sections were held back and the limit allows). */
    public static int restarts() {
        if (!heldBack) return 0;
        int lim = limit();
        int free = lim == Integer.MAX_VALUE ? 4 : lim - RUNNING.get();
        if (free <= 0) return 0;
        heldBack = false;
        RESTARTS.addAndGet(free);
        return free;
    }

    /** A section Quicksilver's mesher meshed (any thread): counted when the server is behind. */
    static void meshed() {
        if (behind && BenchRun.running()) BENCH_MESHED_BEHIND.incrementAndGet();
    }

    public static void startBench() {
        BENCH_HELD.set(0);
        BENCH_MESHED_BEHIND.set(0);
        BENCH_IDLE_BEHIND_NS.set(0);
        BENCH_MAX_RUNNING_BEHIND.set(0);
        idleSince = 0;
        benchBehindOnlyNs = 0;
        benchTicks = benchTicksBehind = 0;
        benchTickMsSum = 0;
        benchBehindNanos = behindNanos;
        benchStartNs = System.nanoTime();
    }

    static JsonObject json() {
        JsonObject o = new JsonObject();
        o.addProperty("state", DISABLED ? (Boolean.getBoolean("quicksilver.ci.measureOnly") ? "off (measure only (CI))" : "off (quicksilver.debug.serverAwareOff)")
            : hookSeen ? "on" : "no meshing seen yet");
        o.addProperty("singlePlayer", singlePlayer());
        o.addProperty("behindNow", behind);
        o.addProperty("serverTickBudgetMs", tickBudgetMs);
        o.addProperty("serverTickMsAvgNow", Math.round(tickMsAvg * 1000) / 1000.0);
        o.addProperty("benchServerTicks", benchTicks);
        o.addProperty("benchServerTickMsAvg", benchTicks == 0 ? 0 : Math.round(benchTickMsSum / benchTicks * 1000) / 1000.0);
        o.addProperty("benchTicksWithMeshingBackedOff", benchTicksBehind);
        o.addProperty("benchMeshingRunsHeldBack", BENCH_HELD.get());
        o.addProperty("meshingRunsHeldBackSinceStart", HELD.get());
        o.addProperty("loopRestarts", RESTARTS.get());
        o.addProperty("benchSectionsMeshedWhileBehind", BENCH_MESHED_BEHIND.get());
        o.addProperty("benchMostRunsAtOnceWhileBehind", BENCH_MAX_RUNNING_BEHIND.get());
        long since = idleSince, idle = BENCH_IDLE_BEHIND_NS.get() + (since != 0 && behind ? System.nanoTime() - since : 0);
        o.addProperty("benchMsBehindWithNoRunMeshingButWorkWaiting", Math.round(idle / 1e3) / 1000.0);
        o.addProperty("benchMsBehind", Math.round(benchBehindOnlyNs / 1e3) / 1000.0);
        return o;
    }

    static void logOnce() {
        QuicksilverClient.LOG.info("Quicksilver mesher: server-aware meshing threads {} (back off to one section at a time while the integrated"
            + " server's ticks take over {}% of their budget, back to vanilla's threads below {}%)", DISABLED ? "off (debug)" : "on",
            Math.round(BEHIND * 100), Math.round(CAUGHT_UP * 100));
    }
}
