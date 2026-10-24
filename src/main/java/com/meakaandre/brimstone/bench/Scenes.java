package com.meakaandre.brimstone.bench;

import com.google.gson.Gson;
import com.meakaandre.brimstone.BrimstoneClient;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;

/**
 * The benchmark scenes: one data file per suite in {@code brimstone/scenes/} inside the jar. A suite that needs
 * another mod names it in {@code requiresMod} and is only offered when that mod is loaded. CI reads the same files,
 * so a scene set up by the bench and a scene set up for a CI screenshot run the same commands. Later packages add
 * scenes (or suites) there.
 */
public final class Scenes {
    /** One suite file. */
    public static final class Suite {
        public String suite;
        public String requiresMod;
        /** Sent before every scene's setup. */
        public List<String> preamble = new ArrayList<>();
        public List<Scene> scenes = new ArrayList<>();
    }

    /** One scene: commands that pin time, weather, position and camera, then how to measure it. */
    public static final class Scene {
        public String id;
        public String title;
        /** Set when the scene cannot be set up yet: the bench and CI skip it and say why. */
        public String skip;
        /** A mod this scene's setup needs on top of its suite's; without it the scene is not offered. */
        public String requiresMod;
        public int renderDistance = 8;
        public int settleSeconds = 20;
        /** Commands sent as the player after the suite's preamble; "#wait N" pauses N seconds. */
        public List<String> setup = new ArrayList<>();
        /** Blocks per second for a scripted fly-through from where setup leaves the camera; null to stand still. */
        public double[] velocity;
        /**
         * Commands sent as the player while measuring, each at its time (seconds after measuring starts), e.g. to open
         * a wall or move an entity during the bench (package 2's moving occlusion pass). Null for none.
         */
        public List<Step> measureSteps;
        /** A screen the scene measures, opened by using the block in front of the camera, or by a setup command. */
        public Screen screen;
        /**
         * Client options the scene needs (package 3: {@code improvedTransparency}). CI writes them into options.txt for
         * the scene's session; the bench sets them for the run and puts them back afterwards. Null for none.
         */
        public java.util.Map<String, String> clientOptions;
        transient Suite owner;

        public List<String> preamble() {
            return owner.preamble;
        }
    }

    public static final class Step {
        public double at;
        public String command;
    }

    public static final class Screen {
        /** Part of the screen's class name, to know it is open. */
        public String expect;
        /** What the player is asked to do when the screen is opened by hand (right-click). */
        public String prompt;
        /** The key CI presses for it (xdotool name); absent when a setup command opens it. */
        public String ciKey;
    }

    private static List<Suite> loaded;

    private Scenes() {
    }

    /** The suites usable in this instance (those whose required mod, if any, is loaded). */
    public static synchronized List<Suite> suites() {
        if (loaded == null) {
            List<Suite> out = new ArrayList<>();
            Path dir = FabricLoader.getInstance().getModContainer("brimstone").flatMap(m -> m.findPath("brimstone/scenes")).orElse(null);
            if (dir != null) {
                try (Stream<Path> files = Files.list(dir)) {
                    for (Path p : files.filter(f -> f.toString().endsWith(".json")).sorted().toList()) {
                        try (Reader r = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
                            Suite s = new Gson().fromJson(r, Suite.class);
                            if (s.requiresMod != null && !FabricLoader.getInstance().isModLoaded(s.requiresMod)) continue;
                            for (Scene sc : s.scenes) sc.owner = s;
                            out.add(s);
                        }
                    }
                } catch (Exception e) {
                    BrimstoneClient.LOG.error("Brimstone could not read its bench scenes", e);
                }
            }
            loaded = out;
        }
        return loaded;
    }

    public static Scene find(String id) {
        for (Suite s : suites()) for (Scene sc : s.scenes) if (sc.id.equals(id)) return sc;
        return null;
    }

    /** Null if the scene can run in this instance, otherwise why not. */
    public static String unavailable(Scene sc) {
        if (sc.skip != null) return sc.skip;
        if (sc.requiresMod != null && !FabricLoader.getInstance().isModLoaded(sc.requiresMod)) return "needs the mod " + sc.requiresMod;
        return null;
    }

    public static List<String> ids() {
        List<String> ids = new ArrayList<>();
        for (Suite s : suites()) for (Scene sc : s.scenes) if (unavailable(sc) == null) ids.add(sc.id);
        return ids;
    }
}
