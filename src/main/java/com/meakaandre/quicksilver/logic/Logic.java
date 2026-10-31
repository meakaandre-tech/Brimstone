package com.meakaandre.quicksilver.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Phase 2, P2-2: the Logic module (entity movement, collisions, block and chunk access). Each optimisation is a small
 * set of mixins with its own switch in {@code config/quicksilver.properties} ({@code logic=on|off} for the whole
 * module, {@code logic.<name>=on|off} for one optimisation; read once at start-up, before any mixin applies). A
 * switched-off optimisation's mixins are not applied at all ({@link LogicMixins}). With Lithium installed the whole
 * module stays off (it does the same jobs).
 *
 * <p>Every optimisation here gives exactly vanilla's results (the same game, tick for tick: CI's world diffs, GameTests
 * and random-tick run). None of them asks for an identity hash or changes the iteration order of any of vanilla's
 * collections (P2-1's rule).
 *
 * <p>Crash safety (the phase 2 design's rule): when an optimisation can't be used (a hook that didn't apply, an
 * exception inside it) it turns itself off, logs one line starting {@value #FALLBACK}, and vanilla's code runs from
 * then on. CI fails on any such line.
 */
public final class Logic {
    public static final Logger LOG = LoggerFactory.getLogger("quicksilver");
    public static final String FALLBACK = "Quicksilver logic fallback:";

    /** The optimisations, in the order the status line lists them, with what each one does. */
    public static final Map<String, String> OPTIMISATIONS = new LinkedHashMap<>();

    private enum State { ON, OFF, FALLBACK }

    private static final Map<String, State> STATE = new LinkedHashMap<>();
    private static final Map<String, String> WHY = new LinkedHashMap<>();
    private static volatile boolean loaded = false;
    private static String moduleOff = null;

    private Logic() {
    }

    /** Reads the switches (once; the mixin plugin calls this before any of the module's mixins apply). */
    static synchronized void load() {
        if (loaded) return;
        loaded = true;
        Optimisations.register(OPTIMISATIONS);
        Properties p = read();
        if (Boolean.getBoolean("quicksilver.ci.measureOnly")) {
            // CI's measure runs stand in for vanilla: every Quicksilver module off
            moduleOff = "quicksilver.ci.measureOnly is set";
        } else if (FabricLoader.getInstance().isModLoaded("lithium")) {
            moduleOff = "Lithium is installed (it does the same; remove it whenever you like)";
        } else if (!"on".equalsIgnoreCase(p.getProperty("logic", "on").trim())) {
            moduleOff = "logic=off in config/quicksilver.properties";
        }
        for (String id : OPTIMISATIONS.keySet()) {
            boolean on = moduleOff == null && "on".equalsIgnoreCase(p.getProperty("logic." + id, "on").trim());
            STATE.put(id, on ? State.ON : State.OFF);
            if (!on) WHY.put(id, moduleOff != null ? moduleOff : "logic." + id + "=off");
        }
        if (moduleOff != null) LOG.info("Quicksilver logic: off, {}", moduleOff);
    }

    /** Whether an optimisation is switched on and hasn't fallen back. Cheap: read on hot paths through {@link Switch}. */
    public static synchronized boolean on(String id) {
        load();
        return STATE.getOrDefault(id, State.OFF) == State.ON;
    }

    /** Turns an optimisation off for the rest of the session: one log line (CI fails on it), vanilla's code from now on. */
    public static void fallback(Switch s, String reason, Throwable t) {
        synchronized (Logic.class) {
            load();
            if (STATE.get(s.id) != State.ON) return;
            STATE.put(s.id, State.FALLBACK);
            WHY.put(s.id, reason);
        }
        s.on = false;
        if (t != null) LOG.warn("{} {} off ({}); vanilla's code runs instead", FALLBACK, s.id, reason, t);
        else LOG.warn("{} {} off ({}); vanilla's code runs instead", FALLBACK, s.id, reason);
    }

    /**
     * One optimisation's switch as a plain field, read on every call of a hot path: on at start-up if the
     * optimisation is, and cleared for good by {@link #fallback}.
     */
    public static final class Switch {
        public final String id;
        public volatile boolean on;

        public Switch(String id) {
            this.id = id;
            this.on = Logic.on(id);
        }

        /** An exception inside the optimisation: this switch off with one line, and the caller runs vanilla's code. */
        public void fail(Throwable t) {
            fallback(this, "exception: " + t, t);
        }
    }

    /** Main entrypoint: the status line once a server has started (CI reads it). */
    public static void init() {
        load();
        // the reflection thread of logic.entity-collisions, made here (the main thread), never by the server thread
        EntityCollisions.init();
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTED.register(server -> report());
    }

    /** The status line (CI reads it). An optimisation that is on but whose hooks didn't land falls back here. */
    public static void report() {
        Optimisations.checkHooks();
        if (moduleOff != null) {
            LOG.info("Quicksilver logic: off, {}", moduleOff);
            return;
        }
        LOG.info("Quicksilver logic: {}", status());
    }

    /** One line: every optimisation's state, for the log at start-up (and CI). */
    public static synchronized String status() {
        load();
        StringJoiner j = new StringJoiner(", ");
        for (var e : STATE.entrySet()) {
            String s = e.getValue().name().toLowerCase(java.util.Locale.ROOT);
            j.add(e.getKey() + "=" + s + (e.getValue() == State.ON ? "" : " (" + WHY.get(e.getKey()) + ")"));
        }
        return j.length() == 0 ? "on (no optimisations yet)" : j.toString();
    }

    private static Properties read() {
        Path file = FabricLoader.getInstance().getConfigDir().resolve("quicksilver.properties");
        Properties p = new Properties();
        try {
            if (Files.exists(file)) {
                try (InputStream in = Files.newInputStream(file)) {
                    p.load(in);
                }
            }
            boolean changed = false;
            if (!p.containsKey("logic")) {
                p.setProperty("logic", "on");
                changed = true;
            }
            for (String id : OPTIMISATIONS.keySet()) {
                if (!p.containsKey("logic." + id)) {
                    p.setProperty("logic." + id, "on");
                    changed = true;
                }
            }
            if (changed) {
                Files.createDirectories(file.getParent());
                try (OutputStream out = Files.newOutputStream(file)) {
                    p.store(out, "Quicksilver. block-renderer: on (Quicksilver's Fabric Renderer API renderer) or off (every block and item drawn"
                        + " by vanilla's own code). memory: on or off (the Memory module); memory.<name>: on or off (one of its optimisations)."
                        + " logic: on or off (the Logic module); logic.<name>: on or off (one of its optimisations). Each needs a restart.");
                }
            }
        } catch (IOException e) {
            LOG.warn("Quicksilver logic: could not read or write {}; every logic optimisation stays on", file, e);
        }
        return p;
    }
}
