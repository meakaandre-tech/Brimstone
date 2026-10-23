package com.meakaandre.brimstone.bench;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.meakaandre.brimstone.BrimstoneClient;
import com.meakaandre.brimstone.probe.Capabilities;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;

/** Turns a finished run into one block in latest.log and one JSON file in {@code <game dir>/brimstone/bench/}. */
final class BenchReport {
    static final String BEGIN = "===== BRIMSTONE BENCH BEGIN =====";
    static final String END = "===== BRIMSTONE BENCH END =====";
    private static final double MB = 1024.0 * 1024.0;

    /** Heap sampled once a tick while measuring; direct memory and the after-GC heap read at the end. */
    static final class Memory {
        long samples, heapUsedSum, heapUsedMax, heapCommittedMax;

        void sample() {
            Runtime rt = Runtime.getRuntime();
            long used = rt.totalMemory() - rt.freeMemory();
            samples++;
            heapUsedSum += used;
            heapUsedMax = Math.max(heapUsedMax, used);
            heapCommittedMax = Math.max(heapCommittedMax, rt.totalMemory());
        }
    }

    private BenchReport() {
    }

    static void write(String sceneId, int seconds, Scenes.Scene scene, Memory mem) throws Exception {
        Minecraft mc = Minecraft.getInstance();
        long[] frames = FrameRecorder.FRAME_NANOS.toLongArray();
        int n = frames.length;
        long[] sorted = frames.clone();
        Arrays.sort(sorted);
        long totalNanos = 0;
        for (long f : frames) totalNanos += f;

        JsonObject out = new JsonObject();
        out.addProperty("scene", sceneId);
        out.addProperty("title", scene == null ? "where the player stood" : scene.title);
        out.addProperty("seconds", seconds);
        out.addProperty("time", LocalDateTime.now().toString());
        out.addProperty("brimstone", version("brimstone"));
        out.addProperty("minecraft", version("minecraft"));

        JsonObject fps = new JsonObject();
        fps.addProperty("frames", n);
        fps.addProperty("average", n == 0 ? 0 : round(n * 1e9 / totalNanos));
        fps.addProperty("low1pct", low(sorted, 0.01));
        fps.addProperty("low01pct", low(sorted, 0.001));
        out.add("fps", fps);

        JsonObject ft = new JsonObject();
        ft.addProperty("p50ms", ms(pct(sorted, 0.50)));
        ft.addProperty("p95ms", ms(pct(sorted, 0.95)));
        ft.addProperty("p99ms", ms(pct(sorted, 0.99)));
        ft.addProperty("maxms", ms(n == 0 ? 0 : sorted[n - 1]));
        ft.addProperty("over50ms", Arrays.stream(frames).filter(f -> f > 50_000_000L).count());
        out.add("frameTime", ft);

        JsonObject gpu = new JsonObject();
        gpu.add("drawCallsPerFrame", perFrame(FrameRecorder.DRAWS));
        gpu.add("uploadsPerFrame", perFrame(FrameRecorder.UPLOADS));
        gpu.add("uploadBytesPerFrame", perFrame(FrameRecorder.BYTES));
        gpu.add("drawCallsByKind", byKind(GpuCounters.Draw.values(), FrameRecorder.startDraws, FrameRecorder.endDraws, n));
        gpu.add("uploadsByKind", byKind(GpuCounters.Upload.values(), FrameRecorder.startUploads, FrameRecorder.endUploads, n));
        gpu.add("uploadBytesByKind", byKind(GpuCounters.Upload.values(), FrameRecorder.startBytes, FrameRecorder.endBytes, n));
        out.add("gpu", gpu);
        out.add("meshing", null);

        JsonObject memory = new JsonObject();
        memory.addProperty("heapUsedAvgMB", mb(mem.samples == 0 ? 0 : mem.heapUsedSum / mem.samples));
        memory.addProperty("heapUsedMaxMB", mb(mem.heapUsedMax));
        memory.addProperty("heapCommittedMB", mb(mem.heapCommittedMax));
        memory.addProperty("heapMaxMB", mb(Runtime.getRuntime().maxMemory()));
        for (BufferPoolMXBean pool : ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class)) {
            memory.addProperty(pool.getName() + "BuffersMB", mb(pool.getMemoryUsed()));
            memory.addProperty(pool.getName() + "BuffersCount", pool.getCount());
        }
        memory.addProperty("nonHeapMB", mb(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed()));
        // after measuring, so the collection cannot touch the frame times
        System.gc();
        Runtime rt = Runtime.getRuntime();
        memory.addProperty("heapUsedAfterGcMB", mb(rt.totalMemory() - rt.freeMemory()));
        memory.addProperty("note", "direct = java.nio direct buffers; LWJGL MemoryUtil allocations are not visible to the JVM");
        out.add("memory", memory);

        Capabilities caps = BrimstoneClient.capabilities();
        out.addProperty("capabilities", caps == null ? "not probed" : caps.describe());
        JsonObject display = new JsonObject();
        display.addProperty("width", mc.getWindow().getWidth());
        display.addProperty("height", mc.getWindow().getHeight());
        display.addProperty("renderDistance", mc.options.renderDistance().get());
        display.addProperty("effectiveRenderDistance", mc.options.getEffectiveRenderDistance());
        display.addProperty("maxFps", mc.options.framerateLimit().get());
        display.addProperty("vsync", mc.options.enableVsync().get());
        out.add("display", display);
        JsonObject mods = modList();
        out.add("mods", mods);

        String block = logBlock(out, caps, mods);
        BrimstoneClient.LOG.info("\n{}", block);

        Path dir = FabricLoader.getInstance().getGameDir().resolve("brimstone").resolve("bench");
        Files.createDirectories(dir);
        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        Path file = dir.resolve(stamp + "-" + sceneId + ".json");
        Files.writeString(file, new GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(out), StandardCharsets.UTF_8);
        BrimstoneClient.LOG.info("BRIMSTONE BENCH END scene={} json={}", sceneId, file);
    }

    private static String logBlock(JsonObject o, Capabilities caps, JsonObject mods) {
        JsonObject fps = o.getAsJsonObject("fps"), ft = o.getAsJsonObject("frameTime"), gpu = o.getAsJsonObject("gpu");
        JsonObject mem = o.getAsJsonObject("memory"), disp = o.getAsJsonObject("display");
        StringBuilder b = new StringBuilder();
        b.append(BEGIN).append('\n');
        line(b, "scene", o.get("scene").getAsString() + " (" + o.get("title").getAsString() + "), " + o.get("seconds").getAsInt() + " s");
        line(b, "versions", "brimstone " + o.get("brimstone").getAsString() + ", minecraft " + o.get("minecraft").getAsString());
        line(b, "fps", String.format(Locale.ROOT, "avg %s, 1%% low %s, 0.1%% low %s (%d frames)",
            fps.get("average"), fps.get("low1pct"), fps.get("low01pct"), fps.get("frames").getAsInt()));
        line(b, "frame time ms", String.format(Locale.ROOT, "p50 %s, p95 %s, p99 %s, max %s, frames over 50 ms %s",
            ft.get("p50ms"), ft.get("p95ms"), ft.get("p99ms"), ft.get("maxms"), ft.get("over50ms")));
        line(b, "draw calls/frame", stats(gpu.getAsJsonObject("drawCallsPerFrame")) + "  by kind " + gpu.get("drawCallsByKind"));
        line(b, "uploads/frame", stats(gpu.getAsJsonObject("uploadsPerFrame")) + "  by kind " + gpu.get("uploadsByKind"));
        line(b, "upload bytes/frame", stats(gpu.getAsJsonObject("uploadBytesPerFrame")) + "  by kind " + gpu.get("uploadBytesByKind"));
        line(b, "heap MB", "used avg " + mem.get("heapUsedAvgMB") + ", max " + mem.get("heapUsedMaxMB") + ", after GC " + mem.get("heapUsedAfterGcMB")
            + ", committed " + mem.get("heapCommittedMB") + ", max allowed " + mem.get("heapMaxMB"));
        line(b, "off-heap MB", "direct buffers " + mem.get("directBuffersMB") + " (" + mem.get("directBuffersCount") + "), mapped "
            + mem.get("mappedBuffersMB") + ", non-heap " + mem.get("nonHeapMB"));
        line(b, "capabilities", caps == null ? "not probed" : caps.describe());
        line(b, "display", disp.get("width") + "x" + disp.get("height") + ", render distance " + disp.get("renderDistance")
            + " (effective " + disp.get("effectiveRenderDistance") + "), max fps " + disp.get("maxFps") + ", vsync " + disp.get("vsync"));
        line(b, "mods", mods.get("count") + " mods, list hash " + mods.get("hash").getAsString());
        line(b, "meshing", "not measured yet (no Brimstone mesher)");
        b.append(END);
        return b.toString();
    }

    private static void line(StringBuilder b, String key, String value) {
        b.append("  ").append(key).append(": ").append(value).append('\n');
    }

    private static String stats(JsonObject s) {
        return "avg " + s.get("avg") + ", median " + s.get("p50") + ", p95 " + s.get("p95") + ", max " + s.get("max");
    }

    private static JsonObject perFrame(LongArrayList values) {
        long[] v = values.toLongArray();
        long[] s = v.clone();
        Arrays.sort(s);
        long sum = 0;
        for (long x : v) sum += x;
        JsonObject o = new JsonObject();
        o.addProperty("avg", v.length == 0 ? 0 : round((double) sum / v.length));
        o.addProperty("p50", pct(s, 0.50));
        o.addProperty("p95", pct(s, 0.95));
        o.addProperty("max", s.length == 0 ? 0 : s[s.length - 1]);
        return o;
    }

    private static <E extends Enum<E>> JsonObject byKind(E[] kinds, long[] start, long[] end, int frames) {
        JsonObject o = new JsonObject();
        for (E k : kinds) {
            long d = end == null || start == null ? 0 : end[k.ordinal()] - start[k.ordinal()];
            o.addProperty(k.name().toLowerCase(Locale.ROOT), frames == 0 ? 0 : round((double) d / frames));
        }
        return o;
    }

    private static JsonObject modList() throws Exception {
        List<String> ids = FabricLoader.getInstance().getAllMods().stream()
            .map(m -> m.getMetadata().getId() + "@" + m.getMetadata().getVersion().getFriendlyString())
            .sorted().toList();
        byte[] h = MessageDigest.getInstance("SHA-256").digest(String.join("\n", ids).getBytes(StandardCharsets.UTF_8));
        JsonObject o = new JsonObject();
        o.addProperty("count", ids.size());
        o.addProperty("hash", HexFormat.of().formatHex(h, 0, 8));
        JsonArray list = new JsonArray();
        ids.forEach(list::add);
        o.add("list", list);
        return o;
    }

    private static String version(String id) {
        return FabricLoader.getInstance().getModContainer(id).map(ModContainer::getMetadata)
            .map(m -> m.getVersion().getFriendlyString()).orElse("?");
    }

    /** Average FPS over the slowest share of frames. */
    private static double low(long[] sorted, double share) {
        if (sorted.length == 0) return 0;
        int k = Math.max(1, (int) Math.ceil(sorted.length * share));
        long sum = 0;
        for (int i = sorted.length - k; i < sorted.length; i++) sum += sorted[i];
        return round(k * 1e9 / sum);
    }

    /** Nearest-rank percentile. */
    private static long pct(long[] sorted, double p) {
        if (sorted.length == 0) return 0;
        int i = (int) Math.ceil(p * sorted.length) - 1;
        return sorted[Math.max(0, Math.min(sorted.length - 1, i))];
    }

    private static double ms(long nanos) {
        return round(nanos / 1e6);
    }

    private static double mb(long bytes) {
        return round(bytes / MB);
    }

    private static double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
