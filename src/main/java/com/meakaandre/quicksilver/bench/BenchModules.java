package com.meakaandre.quicksilver.bench;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * What the bench reports about Quicksilver's own modules (package 4b). The bench itself (the command, the counters, the
 * scenes, frame times, draw calls, uploads, memory, effects and GUI counts) needs none of them, so the bench-only jar
 * ({@code quicksilver-bench}) carries this package without any module; the full jar registers a section per module at
 * start-up ({@code com.meakaandre.quicksilver.BenchSections}).
 */
public final class BenchModules {
    /** One block of the bench's JSON (under {@link #key}) and, optionally, one line of its log block. */
    public interface Section {
        String key();

        /** A bench starts measuring. */
        default void startWindow() {
        }

        /** A bench starts, before its setup (package 4c: counts over the whole bench). */
        default void startBench() {
        }

        JsonElement json(int frames);

        /** The log line for this section's JSON, or null for none. */
        default String line(JsonObject o) {
            return null;
        }
    }

    private static final List<Section> SECTIONS = new CopyOnWriteArrayList<>();

    /** Quicksilver's terrain arenas for the GPU memory block: {reserved, used} bytes; null when no module holds terrain. */
    public static volatile Supplier<long[]> arenas;

    /** Which jar this is: "quicksilver" (every module) or "quicksilver-bench" (measures only). */
    public static volatile String jar = "quicksilver";

    private BenchModules() {
    }

    public static void register(Section s) {
        SECTIONS.add(s);
    }

    public static Section section(String key, Runnable startWindow, java.util.function.IntFunction<JsonElement> json,
                                  java.util.function.Function<JsonObject, String> line) {
        return new Section() {
            @Override
            public String key() {
                return key;
            }

            @Override
            public void startWindow() {
                if (startWindow != null) startWindow.run();
            }

            @Override
            public JsonElement json(int frames) {
                return json.apply(frames);
            }

            @Override
            public String line(JsonObject o) {
                return line == null ? null : line.apply(o);
            }
        };
    }

    /** A bench starts (before its setup). */
    static void startBench() {
        for (Section s : SECTIONS) {
            try {
                s.startBench();
            } catch (Throwable t) {
                com.meakaandre.quicksilver.QuicksilverClient.LOG.warn("Quicksilver bench: {} could not start", s.key(), t);
            }
        }
    }

    static List<Section> all() {
        return SECTIONS;
    }
}
