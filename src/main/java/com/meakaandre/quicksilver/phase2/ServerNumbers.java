package com.meakaandre.quicksilver.phase2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

/**
 * Phase 2 (P2-0), measurement only: the server's numbers for the bench, on a dedicated server and on single player's
 * integrated server alike. Milliseconds per tick (average, median, 99th percentile, max, ticks over 50 ms) over the
 * bench's last N seconds, heap (used, and after a GC when the bench asks), start-up and world-load time, and bytes on
 * the wire ({@link NetworkBytes}).
 *
 * <p>On a dedicated server, {@code /quicksilver bench [seconds]} (or {@code /qs bench}) (from the console or by an operator) measures the
 * server alone and writes {@code quicksilver/bench/<time>-server.json} plus a block in the log. In single player the
 * client's {@code /quicksilver bench} adds these numbers as its "server" section.
 */
public final class ServerNumbers {
    public static final String BEGIN = "===== QUICKSILVER SERVER BENCH BEGIN =====";
    public static final String END = "===== QUICKSILVER SERVER BENCH END =====";
    private static final int CAP = 72_000; // an hour of ticks at 20 per second
    // allocated when a server first ticks (a client that never opens a single-player world doesn't pay for them)
    private static long[] DUR, AT, BYTES;
    private static int head = 0, size = 0;
    private static long tickStart = 0;
    private static volatile long startingMs = 0, startedMs = 0;
    private static volatile boolean dedicated = false;
    /** Client start-up to the first title screen and to the first world joined, set by {@link ClientTimes}; 0 until then. */
    static volatile long clientStartupMs = 0, clientFirstJoinMs = 0;
    private static volatile boolean off = false;

    // a dedicated server's own bench: due when the wall clock passes benchEnd
    private static long benchEnd = 0;
    private static int benchSeconds = 0;
    private static CommandSourceStack benchSource = null;

    private ServerNumbers() {
    }

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            synchronized (ServerNumbers.class) {
                head = 0;
                size = 0;
            }
            startingMs = System.currentTimeMillis();
            dedicated = server.isDedicatedServer();
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> startedMs = System.currentTimeMillis());
        ServerTickEvents.START_SERVER_TICK.register(server -> tickStart = System.nanoTime());
        ServerTickEvents.END_SERVER_TICK.register(ServerNumbers::endTick);
        CommandRegistrationCallback.EVENT.register((dispatcher, ctx, selection) -> {
            if (selection != Commands.CommandSelection.DEDICATED) return; // single player: the client's bench
            for (String name : new String[] {"quicksilver", "qs"}) {
                dispatcher.register(Commands.literal(name).requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                    .then(Commands.literal("bench")
                        .executes(c -> bench(c, 60))
                        .then(Commands.argument("seconds", IntegerArgumentType.integer(5, 3600))
                            .executes(c -> bench(c, IntegerArgumentType.getInteger(c, "seconds"))))));
            }
        });
    }

    private static void endTick(MinecraftServer server) {
        if (off) return;
        try {
            long now = System.nanoTime();
            long bytes = NetworkBytes.SERVER_IN.sum() + NetworkBytes.SERVER_OUT.sum();
            synchronized (ServerNumbers.class) {
                if (DUR == null) {
                    DUR = new long[CAP];
                    AT = new long[CAP];
                    BYTES = new long[CAP];
                }
                DUR[head] = now - tickStart;
                AT[head] = now;
                BYTES[head] = bytes;
                head = (head + 1) % CAP;
                if (size < CAP) size++;
            }
            if (benchSource != null && System.currentTimeMillis() >= benchEnd) finishBench(server);
        } catch (Throwable t) {
            off = true;
            NetworkBytes.LOG.warn("Quicksilver: server numbers off ({}); the game is unaffected", t.toString());
        }
    }

    private static int bench(CommandContext<CommandSourceStack> c, int seconds) {
        if (benchSource != null) {
            c.getSource().sendSystemMessage(Component.literal("[Quicksilver] A server bench is already running."));
            return 0;
        }
        benchSeconds = seconds;
        benchEnd = System.currentTimeMillis() + seconds * 1000L;
        benchSource = c.getSource();
        NetworkBytes.LOG.info("QUICKSILVER SERVER BENCH START seconds={}", seconds);
        c.getSource().sendSystemMessage(Component.literal("[Quicksilver] Server bench: measuring for " + seconds + " s."));
        return 1;
    }

    private static void finishBench(MinecraftServer server) {
        CommandSourceStack src = benchSource;
        benchSource = null;
        JsonObject out = new JsonObject();
        out.addProperty("kind", "server");
        out.addProperty("seconds", benchSeconds);
        out.addProperty("time", LocalDateTime.now().toString());
        out.addProperty("quicksilver", version("quicksilver"));
        out.addProperty("minecraft", version("minecraft"));
        JsonObject s = json(benchSeconds, true);
        s.addProperty("players", server.getPlayerCount());
        out.add("server", s);
        NetworkBytes.LOG.info("\n{}\n  {}\n{}", BEGIN, line(s), END);
        try {
            Path dir = FabricLoader.getInstance().getGameDir().resolve("quicksilver").resolve("bench");
            Files.createDirectories(dir);
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            Path file = dir.resolve(stamp + "-server.json");
            Files.writeString(file, new GsonBuilder().setPrettyPrinting().create().toJson(out), StandardCharsets.UTF_8);
            NetworkBytes.LOG.info("QUICKSILVER SERVER BENCH END json={}", file);
            src.sendSystemMessage(Component.literal("[Quicksilver] Server bench done: " + file.getFileName()));
        } catch (Exception e) {
            NetworkBytes.LOG.error("Quicksilver server bench could not write its report", e);
        }
    }

    /**
     * The server's numbers over the last {@code seconds} of ticks. {@code gc}: also the heap after a full GC (only a
     * bench asks for it). Called on the client thread for single player's bench: the tick record is synchronized.
     */
    public static JsonObject json(int seconds, boolean gc) {
        JsonObject o = new JsonObject();
        long now = System.nanoTime(), from = now - seconds * 1_000_000_000L;
        long[] d;
        long bytesFrom = -1, bytesTo = -1;
        synchronized (ServerNumbers.class) {
            int n = 0;
            long[] tmp = new long[DUR == null ? 0 : size];
            for (int i = 0; i < tmp.length; i++) {
                int k = Math.floorMod(head - size + i, CAP);
                if (AT[k] < from) continue;
                if (bytesFrom < 0) bytesFrom = BYTES[k];
                bytesTo = BYTES[k];
                tmp[n++] = DUR[k];
            }
            d = Arrays.copyOf(tmp, n);
        }
        o.addProperty("side", startedMs == 0 ? "none" : dedicated ? "dedicated" : "integrated");
        o.addProperty("ticks", d.length);
        o.addProperty("tps", round(d.length / (double) seconds));
        if (d.length > 0) {
            Arrays.sort(d);
            long sum = 0;
            int over = 0;
            for (long v : d) {
                sum += v;
                if (v > 50_000_000L) over++;
            }
            o.addProperty("msptAvg", round(sum / 1e6 / d.length));
            o.addProperty("msptP50", round(d[d.length / 2] / 1e6));
            o.addProperty("msptP99", round(d[Math.min(d.length - 1, (int) Math.ceil(d.length * 0.99) - 1)] / 1e6));
            o.addProperty("msptMax", round(d[d.length - 1] / 1e6));
            o.addProperty("ticksOver50ms", over);
        }
        Runtime rt = Runtime.getRuntime();
        o.addProperty("heapUsedMB", mb(rt.totalMemory() - rt.freeMemory()));
        if (gc && dedicated) {
            // in single player the client's bench takes its own heap after GC (one heap for both sides)
            System.gc();
            o.addProperty("heapUsedAfterGcMB", mb(rt.totalMemory() - rt.freeMemory()));
        }
        long jvm = ManagementFactory.getRuntimeMXBean().getStartTime();
        if (dedicated && startedMs > 0) o.addProperty("startupMs", startedMs - jvm);
        if (clientStartupMs > 0) o.addProperty("clientStartupMs", clientStartupMs);
        if (clientFirstJoinMs > 0) o.addProperty("clientFirstJoinMs", clientFirstJoinMs);
        if (startedMs > 0) o.addProperty("worldLoadMs", startedMs - startingMs);
        JsonObject net = new JsonObject();
        net.addProperty("counting", NetworkBytes.active());
        net.addProperty("serverBytesInWindow", bytesTo < 0 ? 0 : bytesTo - bytesFrom);
        net.addProperty("serverBytesIn", NetworkBytes.SERVER_IN.sum());
        net.addProperty("serverBytesOut", NetworkBytes.SERVER_OUT.sum());
        net.addProperty("clientBytesIn", NetworkBytes.CLIENT_IN.sum());
        net.addProperty("clientBytesOut", NetworkBytes.CLIENT_OUT.sum());
        if (startedMs > 0 && !dedicated) net.addProperty("note", "single player talks to its own server in memory: no bytes on the wire");
        o.add("network", net);
        return o;
    }

    /** One log line for a "server" section (the client bench's or the dedicated server's). */
    public static String line(JsonObject s) {
        if (s == null || !s.has("side")) return "none";
        if ("none".equals(s.get("side").getAsString())) {
            JsonObject n = s.getAsJsonObject("network");
            return String.format(Locale.ROOT, "no server in this game (a remote server); client start-up ms to title %s, to first join %s; "
                + "client bytes in %s, out %s", s.get("clientStartupMs"), s.get("clientFirstJoinMs"), n.get("clientBytesIn"), n.get("clientBytesOut"));
        }
        JsonObject n = s.getAsJsonObject("network");
        return String.format(Locale.ROOT, "%s, %s ticks (%s tps), mspt avg %s, p50 %s, p99 %s, max %s, over 50 ms %s; heap MB used %s%s; "
                + "start-up ms %s, world load ms %s; bytes in window %s (server in %s, out %s; client in %s, out %s)",
            s.get("side").getAsString(), s.get("ticks"), s.get("tps"), s.get("msptAvg"), s.get("msptP50"), s.get("msptP99"), s.get("msptMax"),
            s.get("ticksOver50ms"), s.get("heapUsedMB"), s.has("heapUsedAfterGcMB") ? ", after GC " + s.get("heapUsedAfterGcMB") : "",
            s.has("startupMs") ? s.get("startupMs") : s.get("clientStartupMs") + " (to first join " + s.get("clientFirstJoinMs") + ")",
            s.get("worldLoadMs"), n.get("serverBytesInWindow"),
            n.get("serverBytesIn"), n.get("serverBytesOut"), n.get("clientBytesIn"), n.get("clientBytesOut"));
    }

    private static String version(String id) {
        return FabricLoader.getInstance().getModContainer(id).map(m -> m.getMetadata().getVersion().getFriendlyString()).orElse("?");
    }

    private static double mb(long bytes) {
        return round(bytes / 1048576.0);
    }

    private static double round(double v) {
        return Math.round(v * 1000) / 1000.0;
    }
}
