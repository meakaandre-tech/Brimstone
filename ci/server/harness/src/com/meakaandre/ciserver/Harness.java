package com.meakaandre.ciserver;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CI only (phase 2, P2-0): the server suite's driver, in every run alike. Two modes, chosen by system property:
 * <ul>
 * <li>{@code ci.harness.gen=<file>}: once the server has started, generates every chunk of each listed area to full,
 * one at a time, rows of z then x, then stops the server (which saves them). With {@code -Dmax.bg.threads=1} all
 * generation runs on one worker in the order the server thread asks, so the result doesn't depend on thread timing.</li>
 * <li>{@code ci.harness.run=<file>}: freezes the game, force-loads each listed area and waits until every chunk in it
 * is entity-ticking with its entities loaded, runs the setup commands, re-seeds every random source from fixed values,
 * then lets exactly N ticks run (with commands at given ticks), freezes again and stops the server (which saves).</li>
 * </ul>
 * Either way it writes {@code ci-harness-result.json} to {@code ci.harness.out} (default: the server directory):
 * milliseconds per tick over the run, heap after load and at the end (after a GC), start-up and world-load time.
 *
 * <p>P2-1: {@code -Dci.harness.heap=true} (the heap run) plays the run script's scene for {@code ci.harness.ticks}
 * ticks (12000, ten minutes of game time) sprinting, and writes the live heap and a class histogram right after load
 * and at the end ({@code histo-load.txt}, {@code histo-end.txt}). Script lines {@code random-ticks MIN_Y} (random ticks
 * pinned per chunk, only at or above MIN_Y: {@link Pins}) and {@code spawn CX CZ} (natural spawning in that chunk
 * every tick, from a pinned random) make the random-tick run.
 */
public final class Harness implements ModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger("ci_server_harness");

    private enum Stage { IDLE, GEN_SETTLE, LOADING, SETTLE, RUNNING, DONE }

    private record Area(ResourceKey<Level> dim, int x1, int z1, int x2, int z2) {
    }

    private record At(int tick, String command, boolean quicksilverOnly) {
    }

    private record Spawn(int cx, int cz) {
    }

    /** P2-1: the heap run (histograms, sprinting, no bench). */
    private static final boolean HEAP = Boolean.getBoolean("ci.harness.heap");
    private static final List<Spawn> spawns = new ArrayList<>();
    private static long monstersSpawned = 0, creaturesSpawned = 0;
    private static final int SPAWN_CAP = 24;  // per chunk and category over the run
    private static final java.util.Map<String, Integer> spawnedIn = new java.util.HashMap<>();
    static volatile long gameTime = 0;

    /** The GameTest server only: the CI pins in mixin/ChunkTickRandomMixin apply. */
    public static volatile boolean gameTestPins = false;
    private static Stage stage = Stage.IDLE;
    private static final List<Area> areas = new ArrayList<>();
    private static final List<String> setup = new ArrayList<>();
    private static final List<At> timed = new ArrayList<>();
    private static int ticks = 0, tick = 0, waited = 0, quiet = 0, settleWaited = 0;
    private static final int SETTLE_TICKS = 200;
    private static long tickStart = 0, startingAt = 0, startedAt = 0;
    private static long[] tickNanos = new long[0];
    private static final JsonObject result = new JsonObject();

    @Override
    public void onInitialize() {
        Trace.init();
        ServerLifecycleEvents.SERVER_STARTING.register(server -> startingAt = System.currentTimeMillis());
        ServerLifecycleEvents.SERVER_STARTED.register(Harness::started);
        ServerTickEvents.START_SERVER_TICK.register(Harness::startTick);
        ServerTickEvents.END_SERVER_TICK.register(Harness::endTick);
    }

    private static void started(MinecraftServer server) {
        startedAt = System.currentTimeMillis();
        long jvm = ManagementFactory.getRuntimeMXBean().getStartTime();
        result.addProperty("startupMs", startedAt - jvm);
        result.addProperty("worldLoadMs", startedAt - startingAt);
        result.addProperty("quicksilverLoaded", quicksilver());
        String gen = System.getProperty("ci.harness.gen");
        String run = System.getProperty("ci.harness.run");
        try {
            if (System.getProperty("fabric-api.gametest") != null) {
                // the GameTest server: no random ticks or natural spawning (they draw on the level's shared random
                // in whatever chunks happen to be loaded), and every random source from a fixed point
                for (String c : new String[] {"gamerule random_tick_speed 0", "gamerule spawn_mobs false"}) command(server, c);
                gameTestPins = true;
                reseed(server);
                result.addProperty("mode", "gametest");
                write();
                return;
            }
            if (gen != null) {
                generate(server, Path.of(gen));
            } else if (run != null) {
                prepareRun(server, Path.of(run));
            }
        } catch (Exception e) {
            LOG.error("CI HARNESS FAILED", e);
            result.addProperty("error", String.valueOf(e));
            write();
            server.halt(false);
        }
    }

    static boolean quicksilver() {
        FabricLoader l = FabricLoader.getInstance();
        return l.isModLoaded("quicksilver");
    }

    private static ServerLevel level(MinecraftServer server, String id) {
        ServerLevel level = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.parse(id)));
        if (level == null) throw new IllegalArgumentException("no dimension " + id);
        return level;
    }

    private static List<String> lines(Path file) throws Exception {
        List<String> out = new ArrayList<>();
        for (String l : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            l = l.strip();
            if (!l.isEmpty() && !l.startsWith("#")) out.add(l);
        }
        return out;
    }

    // ------------------------------------------------------------------ gen

    private static void generate(MinecraftServer server, Path spec) throws Exception {
        JsonArray done = new JsonArray();
        for (String line : lines(spec)) {
            String[] p = line.split("\\s+");
            ServerLevel level = level(server, p[0]);
            int x1 = Integer.parseInt(p[1]), z1 = Integer.parseInt(p[2]), x2 = Integer.parseInt(p[3]), z2 = Integer.parseInt(p[4]);
            long t0 = System.nanoTime();
            int n = (x2 - x1 + 1) * (z2 - z1 + 1);
            // One generation step at a time over the whole area, rows of z then x: every chunk reaches a step before
            // any chunk starts the next, so each request runs exactly one step and nothing depends on how fast the
            // worker and the server thread hand work to each other. The margin shrinks by one chunk per step, so a
            // step always finds its neighbours already at the step before it.
            List<ChunkStatus> steps = ChunkStatus.getStatusList();
            for (int i = 0; i < steps.size(); i++) {
                int m = steps.size() - 1 - i;
                for (int cz = z1 - m; cz <= z2 + m; cz++) {
                    for (int cx = x1 - m; cx <= x2 + m; cx++) level.getChunk(cx, cz, steps.get(i), true);
                }
            }
            long ms = (System.nanoTime() - t0) / 1_000_000;
            LOG.info("CI HARNESS GEN {}: {} chunks in {} ms", line, n, ms);
            JsonObject d = new JsonObject();
            d.addProperty("area", line);
            d.addProperty("chunks", n);
            d.addProperty("ms", ms);
            done.add(d);
        }
        result.add("generated", done);
        result.addProperty("mode", "gen");
        // the light engine works on its own thread: the server stops (and saves) only once it has nothing left to
        // do, so saved light doesn't depend on how far it had got
        server.tickRateManager().setFrozen(true);
        stage = Stage.GEN_SETTLE;
        LOG.info("CI HARNESS GEN: generated, waiting for the light engine");
    }

    /** True once no level's light engine has had work for 20 ticks in a row (counted by the caller's ticks). */
    private static boolean lightSettled(MinecraftServer server) {
        boolean busy = false;
        for (ServerLevel level : server.getAllLevels()) busy |= level.getChunkSource().getLightEngine().hasLightWork();
        quiet = busy ? 0 : quiet + 1;
        return quiet >= 20;
    }

    // ------------------------------------------------------------------ run

    private static void prepareRun(MinecraftServer server, Path script) throws Exception {
        for (String line : lines(script)) {
            String[] p = line.split("\\s+", 3);
            switch (p[0]) {
                case "area" -> {
                    String[] a = line.split("\\s+");
                    areas.add(new Area(ResourceKey.create(Registries.DIMENSION, Identifier.parse(a[1])),
                        Integer.parseInt(a[2]), Integer.parseInt(a[3]), Integer.parseInt(a[4]), Integer.parseInt(a[5])));
                }
                case "cmd" -> setup.add(line.substring(4).strip());
                case "ticks" -> ticks = Integer.parseInt(p[1]);
                case "at" -> timed.add(new At(Integer.parseInt(p[1]), p[2], false));
                case "qs-at" -> {
                    // the heap run measures the game, not Quicksilver's bench
                    if (!HEAP) timed.add(new At(Integer.parseInt(p[1]), p[2], true));
                }
                case "random-ticks" -> Pins.randomTickMinY = Integer.parseInt(p[1]);
                case "spawn-player" -> {
                    String[] a = line.split("\\s+");
                    spawnPlayerAt = new double[] {Double.parseDouble(a[1]), Double.parseDouble(a[2]), Double.parseDouble(a[3])};
                }
                case "spawn" -> {
                    String[] a = line.split("\\s+");
                    spawns.add(new Spawn(Integer.parseInt(a[1]), Integer.parseInt(a[2])));
                }
                default -> throw new IllegalArgumentException("unknown script line: " + line);
            }
        }
        if (Integer.getInteger("ci.harness.ticks") != null) ticks = Integer.getInteger("ci.harness.ticks");
        if (ticks <= 0) throw new IllegalArgumentException("script has no 'ticks N'");
        server.tickRateManager().setFrozen(true);
        for (Area a : areas) {
            ServerLevel level = server.getLevel(a.dim());
            if (level == null) throw new IllegalArgumentException("no dimension " + a.dim());
            for (int cz = a.z1(); cz <= a.z2(); cz++) {
                for (int cx = a.x1(); cx <= a.x2(); cx++) {
                    level.setChunkForced(cx, cz, true);
                    level.getChunk(cx, cz, ChunkStatus.FULL, true);
                }
            }
        }
        result.addProperty("mode", HEAP ? "heap" : "run");
        result.addProperty("ticks", ticks);
        stage = Stage.LOADING;
        LOG.info("CI HARNESS RUN: frozen, waiting for {} areas to load", areas.size());
    }

    private static boolean loaded(MinecraftServer server) {
        // the network check: a client must have joined before tick 0 and stays through the run
        if (Boolean.getBoolean("ci.harness.waitPlayer") && server.getPlayerCount() == 0) return false;
        for (Area a : areas) {
            ServerLevel level = server.getLevel(a.dim());
            // the area itself entity-ticking with its entities loaded; the ring around it block-ticking (chunks next
            // to force-loaded ones run their scheduled ticks too) and the ring outside that loaded, so no chunk that
            // ticks during the run starts ticking at a different moment in another run
            for (int cz = a.z1() - 2; cz <= a.z2() + 2; cz++) {
                for (int cx = a.x1() - 2; cx <= a.x2() + 2; cx++) {
                    int ring = Math.max(Math.max(a.x1() - cx, cx - a.x2()), Math.max(a.z1() - cz, cz - a.z2()));
                    if (ring <= 0) {
                        if (!level.areEntitiesLoaded(chunkKey(cx, cz))) return false;
                        if (!level.isPositionEntityTicking(new BlockPos(cx * 16 + 8, 64, cz * 16 + 8))) return false;
                    } else if (ring == 1) {
                        if (!level.shouldTickBlocksAt(chunkKey(cx, cz))) return false;
                    } else if (!level.hasChunk(cx, cz)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** Every random source from a fixed point: the seed counters, each level's random, each entity's by its UUID. */
    private static void reseed(MinecraftServer server) {
        SeedPin.reset(0x5EED_0000L);
        for (ServerLevel level : server.getAllLevels()) {
            level.getRandom().setSeed(SeedPin.mix(level.dimension().identifier().toString().hashCode()));
            for (Entity e : level.getAllEntities()) {
                UUID u = e.getUUID();
                e.getRandom().setSeed(SeedPin.mix(u.getMostSignificantBits() ^ u.getLeastSignificantBits()));
            }
        }
    }

    /** A chunk position packed as the game packs it: x in the low 32 bits, z in the high. */
    private static long chunkKey(int cx, int cz) {
        return (cx & 0xFFFFFFFFL) | (cz & 0xFFFFFFFFL) << 32;
    }

    private static int commandErrors = 0;

    /** Runs one command and logs its result; a command that fails (throws) is counted (gated by CI). */
    private static void command(MinecraftServer server, String cmd) {
        String c = cmd.startsWith("/") ? cmd.substring(1) : cmd;
        try {
            int r = server.getCommands().getDispatcher().execute(c, server.createCommandSourceStack().withSuppressedOutput());
            LOG.info("CI HARNESS CMD t={} result={} {}", tick, r, cmd);
        } catch (Exception e) {
            commandErrors++;
            LOG.warn("CI HARNESS CMD FAILED t={} {}: {}", tick, cmd, e.getMessage());
        }
        result.addProperty("commandErrors", commandErrors);
    }

    private static void startTick(MinecraftServer server) {
        tickStart = System.nanoTime();
        gameTime = server.overworld().getGameTime();
        if (stage != Stage.RUNNING) return;
        if (HEAP && tick == 0) command(server, "tick sprint " + ticks);
        for (At a : timed) {
            if (a.tick() != tick || (a.quicksilverOnly() && !quicksilver())) continue;
            if (a.command().startsWith("ci-train ")) {
                CiTrain.command(server, tick, a.command().substring("ci-train ".length()));
            } else {
                command(server, a.command());
            }
        }
    }

    /** A command for a harness helper (CiTrain): run and logged like the script's own, failures counted. */
    static void runQuiet(MinecraftServer server, int t, String cmd) {
        command(server, cmd);
    }

    private static void endTick(MinecraftServer server) {
        long dt = System.nanoTime() - tickStart;
        switch (stage) {
            case GEN_SETTLE -> {
                settleWaited++;
                if (!lightSettled(server) && settleWaited < 6000) return;
                result.addProperty("lightSettleTicks", settleWaited);
                if (settleWaited >= 6000) result.addProperty("error", "light engine still busy after 6000 ticks");
                stage = Stage.DONE;
                heap("heapAfterGenMB");
                write();
                LOG.info("CI HARNESS GEN DONE (light settled after {} ticks)", settleWaited);
                server.halt(false);
            }
            case LOADING -> {
                waited++;
                if (!loaded(server)) {
                    if (waited % 100 == 0) LOG.info("CI HARNESS RUN: still loading after {} ticks", waited);
                    // a client in CI can take a long time to start (Loom sets the game up first): 30 minutes
                    if (waited > (Boolean.getBoolean("ci.harness.waitPlayer") ? 36000 : 6000)) {
                        result.addProperty("error", "area not loaded after " + waited + " frozen ticks");
                        stage = Stage.DONE;
                        write();
                        server.halt(false);
                    }
                    return;
                }
                result.addProperty("loadWaitTicks", waited);
                for (String c : setup) command(server, c);
                quiet = 0;
                stage = Stage.SETTLE;
            }
            case SETTLE -> {
                // the setup's blocks change light: tick 0 waits (still frozen) until the light engine is done, and
                // always at least SETTLE_TICKS, so mods that tick through their own hooks even while the game is
                // frozen (Create's kinetics, SIFTEC's machines) get the same head start in every run
                settleWaited++;
                boolean settled = lightSettled(server);
                if ((!settled || settleWaited < SETTLE_TICKS) && settleWaited < 6000) return;
                result.addProperty("lightSettleTicks", settleWaited);
                if (settleWaited >= 6000) result.addProperty("error", "light engine still busy after 6000 ticks");
                heap("heapAfterLoadMB");
                if (HEAP) histogram("load");
                // tick 0: every random source from a fixed point, whatever order things loaded in
                reseed(server);
                tickNanos = new long[ticks];
                tick = 0;
                stage = Stage.RUNNING;
                server.tickRateManager().setFrozen(false);
                LOG.info("CI HARNESS RUN START after {} loading and {} light ticks: {} ticks", waited, settleWaited, ticks);
            }
            case RUNNING -> {
                // the first tick after unfreezing is the run's tick 0
                tickNanos[tick] = dt;
                CiTrain.tick(server, tick);
                spawn(server);
                tick++;
                if (tick >= ticks) {
                    server.tickRateManager().setFrozen(true);
                    stage = Stage.DONE;
                    long[] s = tickNanos.clone();
                    Arrays.sort(s);
                    long sum = 0;
                    int over = 0;
                    for (long v : s) {
                        sum += v;
                        if (v > 50_000_000L) over++;
                    }
                    result.addProperty("msptAvg", round(sum / 1e6 / s.length));
                    result.addProperty("msptP50", round(s[s.length / 2] / 1e6));
                    result.addProperty("msptP99", round(s[Math.min(s.length - 1, (int) Math.ceil(s.length * 0.99) - 1)] / 1e6));
                    result.addProperty("msptMax", round(s[s.length - 1] / 1e6));
                    result.addProperty("ticksOver50ms", over);
                    heap("heapEndMB");
                    removeSpawnPlayer(server);
                    if (HEAP) histogram("end");
                    if (Pins.on() || !spawns.isEmpty()) {
                        String r = Pins.summary() + " monsters=" + monstersSpawned + " creatures=" + creaturesSpawned;
                        result.addProperty("random", r);
                        LOG.info("CI RANDOM result={}", r);
                    }
                    String train = CiTrain.result(server);
                    if (train != null) {
                        result.addProperty("train", train);
                        LOG.info("CI TRAIN result={}", train);
                    }
                    write();
                    LOG.info("CI HARNESS RUN DONE: {} ticks, mspt avg {}", tick, result.get("msptAvg"));
                    server.halt(false);
                }
            }
            default -> {
            }
        }
    }

    /** Where the harness is, for trace lines: its stage, the run's tick, the game time. */
    static String where() {
        return "stage=" + stage + " t=" + tick + " gt=" + gameTime;
    }

    /**
     * Natural spawning in the random-tick run's spawn chunks, every tick, as the game spawns in a chunk near a player
     * (CI has no player, and without one the game spawns nowhere): a monster attempt every tick and a creature attempt
     * every 400 game ticks, as NaturalSpawner's own schedule, up to SPAWN_CAP mobs per chunk and category. The level's
     * random is re-seeded from the chunk and the game time first, so where mobs appear doesn't depend on every other
     * draw on it this tick. The same in vanilla's runs and Quicksilver's.
     */
    private static void spawn(MinecraftServer server) {
        if (spawns.isEmpty()) return;
        ServerLevel level = server.overworld();
        if (spawnPlayer == null) addSpawnPlayer(level);
        long t = level.getGameTime();
        for (Spawn s : spawns) {
            net.minecraft.world.level.chunk.LevelChunk chunk = level.getChunk(s.cx(), s.cz());
            for (net.minecraft.world.entity.MobCategory cat : new net.minecraft.world.entity.MobCategory[] {
                net.minecraft.world.entity.MobCategory.MONSTER, net.minecraft.world.entity.MobCategory.CREATURE}) {
                boolean creature = cat == net.minecraft.world.entity.MobCategory.CREATURE;
                if (creature && t % 400 != 0) continue;
                String key = s.cx() + "," + s.cz() + "/" + cat;
                if (spawnedIn.getOrDefault(key, 0) >= SPAWN_CAP) continue;
                level.getRandom().setSeed(SeedPin.mix(s.cx() * 31L + s.cz() * 1_000_003L + t * 0x9E3779B97F4A7C15L + cat.ordinal()));
                spawnCategory(cat, level, chunk, () -> {
                    spawnedIn.merge(key, 1, Integer::sum);
                    if (creature) creaturesSpawned++;
                    else monstersSpawned++;
                });
            }
        }
    }

    private static java.lang.reflect.Method spawnMethod = null;

    /**
     * NaturalSpawner#spawnCategoryForChunk(category, level, chunk, predicate, after-spawn callback), called through
     * reflection with its two callbacks as proxies (every spawn allowed, as the game's own predicate allows outside a
     * charged area; every spawn counted), so the harness doesn't depend on the callbacks' exact shapes.
     */
    private static void spawnCategory(net.minecraft.world.entity.MobCategory cat, ServerLevel level,
                                      net.minecraft.world.level.chunk.LevelChunk chunk, Runnable spawned) {
        try {
            if (spawnMethod == null) {
                for (java.lang.reflect.Method m : net.minecraft.world.level.NaturalSpawner.class.getDeclaredMethods()) {
                    if (m.getName().equals("spawnCategoryForChunk") && m.getParameterCount() == 5) spawnMethod = m;
                }
                if (spawnMethod == null) throw new NoSuchMethodException("NaturalSpawner.spawnCategoryForChunk/5");
                spawnMethod.setAccessible(true);
            }
            Class<?>[] t = spawnMethod.getParameterTypes();
            Object predicate = java.lang.reflect.Proxy.newProxyInstance(t[3].getClassLoader(), new Class<?>[] {t[3]},
                (proxy, m, args) -> m.getDeclaringClass() == Object.class ? objectMethod(proxy, m, args) : Boolean.TRUE);
            Object after = java.lang.reflect.Proxy.newProxyInstance(t[4].getClassLoader(), new Class<?>[] {t[4]}, (proxy, m, args) -> {
                if (m.getDeclaringClass() == Object.class) return objectMethod(proxy, m, args);
                spawned.run();
                return null;
            });
            spawnMethod.invoke(null, cat, level, chunk, predicate, after);
        } catch (Throwable e) {
            if (result.get("error") == null) {
                LOG.error("CI HARNESS FAILED: natural spawning", e);
                result.addProperty("error", "spawning: " + e);
            }
        }
    }

    private static Object objectMethod(Object proxy, java.lang.reflect.Method m, Object[] args) {
        return switch (m.getName()) {
            case "equals" -> proxy == args[0];
            case "hashCode" -> System.identityHashCode(proxy);
            default -> "ci-harness-spawn-callback";
        };
    }

    private static Object spawnPlayer = null;

    /**
     * The game spawns a mob only where a player is near enough (and at least 24 blocks away), and CI has none: a
     * Fabric API fake player, in creative mode at the run's player point (script line {@code spawn-player X Y Z}), is
     * put in the overworld's player list only (not in the world as an entity: it loads no chunks, isn't saved, and
     * never ticks), so the spawner and mobs' despawn checks find it as they would a real player. Random-tick run only.
     */
    @SuppressWarnings("unchecked")
    private static void addSpawnPlayer(ServerLevel level) {
        try {
            net.fabricmc.fabric.api.entity.FakePlayer p = net.fabricmc.fabric.api.entity.FakePlayer.get(level);
            p.setPos(spawnPlayerAt[0], spawnPlayerAt[1], spawnPlayerAt[2]);
            try {
                p.setGameMode(net.minecraft.world.level.GameType.CREATIVE);
            } catch (Throwable t) {
                LOG.warn("CI HARNESS spawn player: game mode not set", t);
            }
            java.lang.reflect.Field f = ServerLevel.class.getDeclaredField("players");
            f.setAccessible(true);
            ((List<Object>) f.get(level)).add(p);
            spawnPlayer = p;
            LOG.info("CI HARNESS spawn player at {} {} {}, creative {}", spawnPlayerAt[0], spawnPlayerAt[1], spawnPlayerAt[2], p.isCreative());
        } catch (Throwable t) {
            LOG.error("CI HARNESS FAILED: no spawn player", t);
            result.addProperty("error", "spawn player: " + t);
            spawnPlayer = Boolean.FALSE;
        }
    }

    @SuppressWarnings("unchecked")
    private static void removeSpawnPlayer(MinecraftServer server) {
        if (spawnPlayer == null || spawnPlayer == Boolean.FALSE) return;
        try {
            java.lang.reflect.Field f = ServerLevel.class.getDeclaredField("players");
            f.setAccessible(true);
            ((List<Object>) f.get(server.overworld())).remove(spawnPlayer);
        } catch (Throwable t) {
            LOG.warn("CI HARNESS spawn player not removed", t);
        }
    }

    private static double[] spawnPlayerAt = {0.5, 64, 0.5};

    /** The live heap's class histogram (the JVM's own, after a full GC): its top classes to histo-NAME.txt and the
     * live total to the result as heapLiveNAMEMB. */
    private static void histogram(String name) {
        try {
            javax.management.MBeanServer s = ManagementFactory.getPlatformMBeanServer();
            javax.management.ObjectName n = new javax.management.ObjectName("com.sun.management:type=DiagnosticCommand");
            String out = (String) s.invoke(n, "gcClassHistogram", new Object[] {new String[0]}, new String[] {String[].class.getName()});
            String[] rows = out.split("\n");
            StringBuilder top = new StringBuilder();
            for (int i = 0; i < rows.length; i++) {
                if (i < 400 || rows[i].startsWith("Total")) top.append(rows[i]).append('\n');
                if (rows[i].startsWith("Total")) {
                    String[] p = rows[i].trim().split("\\s+");
                    result.addProperty("heapLive" + Character.toUpperCase(name.charAt(0)) + name.substring(1) + "MB",
                        round(Long.parseLong(p[2]) / 1048576.0));
                }
            }
            Path dir = Path.of(System.getProperty("ci.harness.out", "."));
            Files.createDirectories(dir);
            Files.writeString(dir.resolve("histo-" + name + ".txt"), top.toString(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error("CI HARNESS histogram failed", e);
            result.addProperty("error", "histogram: " + e);
        }
    }

    // ------------------------------------------------------------------ output

    private static void heap(String key) {
        System.gc();
        Runtime rt = Runtime.getRuntime();
        result.addProperty(key, round((rt.totalMemory() - rt.freeMemory()) / 1048576.0));
    }

    private static double round(double v) {
        return Math.round(v * 1000) / 1000.0;
    }

    static synchronized void write() {
        try {
            Path dir = Path.of(System.getProperty("ci.harness.out", "."));
            Files.createDirectories(dir);
            Files.writeString(dir.resolve("ci-harness-result.json"),
                new GsonBuilder().setPrettyPrinting().create().toJson(result), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error("CI HARNESS could not write its result", e);
        }
    }
}
