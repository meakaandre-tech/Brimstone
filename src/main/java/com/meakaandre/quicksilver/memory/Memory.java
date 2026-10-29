package com.meakaandre.quicksilver.memory;

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
 * Phase 2, P2-1: the Memory module. Each optimisation is a small set of mixins with its own switch in
 * {@code config/quicksilver.properties} ({@code memory=on|off} for the whole module, {@code memory.<name>=on|off} for
 * one optimisation; read once at start-up, before any mixin applies). A switched-off optimisation's mixins are not
 * applied at all ({@link MemoryMixins}). With FerriteCore installed the whole module stays off (it does the same).
 *
 * <p>Crash safety (the phase 2 design's rule): when an optimisation can't be used (a hook that didn't apply, a check
 * that failed, an exception while it sets itself up) it turns itself off, logs one line starting
 * {@value #FALLBACK} and leaves vanilla's own data and code in place. CI fails on any such line.
 */
public final class Memory {
    public static final Logger LOG = LoggerFactory.getLogger("quicksilver");
    public static final String FALLBACK = "Quicksilver memory fallback:";

    /** The optimisations, in the order the status line lists them, with what each one saves. */
    public static final Map<String, String> OPTIMISATIONS = new LinkedHashMap<>();

    static {
        OPTIMISATIONS.put("state-neighbours", "one shared table per block for BlockState#setValue in place of every state's own neighbour arrays");
        OPTIMISATIONS.put("shape-dedup", "equal block shapes, face shapes and sturdiness arrays shared between block states");
        OPTIMISATIONS.put("datafixer-caches", "the data fixer's rewrite caches emptied once the server has started");
        OPTIMISATIONS.put("bench-buffers", "the server bench's tick history kept only while a bench runs");
    }

    private enum State { ON, OFF, FALLBACK }

    private static final Map<String, State> STATE = new LinkedHashMap<>();
    private static final Map<String, String> WHY = new LinkedHashMap<>();
    private static volatile boolean loaded = false;
    private static String moduleOff = null;

    private Memory() {
    }

    /** Reads the switches (once; the mixin plugin calls this before any of the module's mixins apply). */
    static synchronized void load() {
        if (loaded) return;
        loaded = true;
        Properties p = read();
        if (Boolean.getBoolean("quicksilver.ci.measureOnly")) {
            // CI's measure runs stand in for vanilla: every Quicksilver module off
            moduleOff = "quicksilver.ci.measureOnly is set";
        } else if (FabricLoader.getInstance().isModLoaded("ferritecore")) {
            moduleOff = "FerriteCore is installed (it does the same; remove it whenever you like)";
        } else if (!"on".equalsIgnoreCase(p.getProperty("memory", "on").trim())) {
            moduleOff = "memory=off in config/quicksilver.properties";
        }
        for (String id : OPTIMISATIONS.keySet()) {
            boolean on = moduleOff == null && "on".equalsIgnoreCase(p.getProperty("memory." + id, "on").trim());
            STATE.put(id, on ? State.ON : State.OFF);
            if (!on) WHY.put(id, moduleOff != null ? moduleOff : "memory." + id + "=off");
        }
        if (moduleOff != null) LOG.info("Quicksilver memory: off, {}", moduleOff);
    }

    /** Whether an optimisation is switched on and hasn't fallen back. */
    public static synchronized boolean on(String id) {
        load();
        return STATE.getOrDefault(id, State.OFF) == State.ON;
    }

    /** Turns an optimisation off for the rest of the session: one log line (CI fails on it), vanilla's code from now on. */
    public static synchronized void fallback(String id, String reason, Throwable t) {
        load();
        if (STATE.get(id) != State.ON) return;
        STATE.put(id, State.FALLBACK);
        WHY.put(id, reason);
        if (t != null) LOG.warn("{} {} off ({}); vanilla's code runs instead", FALLBACK, id, reason, t);
        else LOG.warn("{} {} off ({}); vanilla's code runs instead", FALLBACK, id, reason);
    }

    /** Main entrypoint: the bench switch, and the status line once a server has started (CI reads it). */
    public static void init() {
        load();
        com.meakaandre.quicksilver.phase2.ServerNumbers.keepAlways = !on("bench-buffers");
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            // an optimisation that is on but did nothing means its hook didn't run: a fallback (CI fails on it)
            if (on(StateTables.ID) && StateTables.blocks == 0) fallback(StateTables.ID, "no block got a table (the StateDefinition hook did not run)", null);
            if (on(ShapeDedup.ID) && ShapeDedup.states == 0) fallback(ShapeDedup.ID, "no state shared its shapes (the initCache hook did not run)", null);
            ShapeDedup.dropTables();
            DatafixerCaches.started(server);
            LOG.info("Quicksilver memory: {}; state tables: {}; shapes: {}; data fixer caches emptied: {}", status(), StateTables.counts(),
                ShapeDedup.counts(), DatafixerCaches.counts());
        });
        // a later rebuild of the states' caches (a /reload) fills the shape tables again: dropped at the next tick
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(server -> ShapeDedup.dropTables());
    }

    /** One line: every optimisation's state, for the log at start-up (and CI). */
    public static synchronized String status() {
        load();
        StringJoiner j = new StringJoiner(", ");
        for (var e : STATE.entrySet()) {
            String s = e.getValue().name().toLowerCase(java.util.Locale.ROOT);
            j.add(e.getKey() + "=" + s + (e.getValue() == State.ON ? "" : " (" + WHY.get(e.getKey()) + ")"));
        }
        return j.toString();
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
            if (!p.containsKey("memory")) {
                p.setProperty("memory", "on");
                changed = true;
            }
            for (String id : OPTIMISATIONS.keySet()) {
                if (!p.containsKey("memory." + id)) {
                    p.setProperty("memory." + id, "on");
                    changed = true;
                }
            }
            if (changed) {
                Files.createDirectories(file.getParent());
                try (OutputStream out = Files.newOutputStream(file)) {
                    p.store(out, "Quicksilver. block-renderer: on (Quicksilver's Fabric Renderer API renderer) or off (every block and item drawn"
                        + " by vanilla's own code). memory: on or off (the Memory module); memory.<name>: on or off (one of its optimisations)."
                        + " Each needs a restart.");
                }
            }
        } catch (IOException e) {
            LOG.warn("Quicksilver memory: could not read or write {}; every memory optimisation stays on", file, e);
        }
        return p;
    }
}
