package com.meakaandre.quicksilver.bench;

import com.meakaandre.quicksilver.QuicksilverClient;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

/**
 * One bench run, stepped once per client tick: set the scene up, let it settle, (wait for its screen,) measure, report.
 * It sends the scene's commands as the player and sets the render distance, then puts the render distance back.
 * It never reads or fakes input: the camera is pinned by setting the player's pose, and a screen that needs a
 * right-click waits for the player to open it.
 */
public final class BenchRun {
    private enum Stage { SETUP, SETTLE, WAIT_SCREEN, MEASURE, DONE }

    private static BenchRun current;

    private final Scenes.Scene scene;
    private final String sceneId;
    private final int seconds;
    private final Deque<String> steps = new ArrayDeque<>();
    private Stage stage = Stage.SETUP;
    private long waitUntil;
    private int previousRenderDistance = -1;
    private Boolean previousImprovedTransparency;
    private long measureStart;
    private int nextStep;
    private double px, py, pz;
    private float yaw, pitch;
    private final BenchReport.Memory memory = new BenchReport.Memory();

    private BenchRun(Scenes.Scene scene, int seconds) {
        this.scene = scene;
        this.sceneId = scene == null ? "here" : scene.id;
        this.seconds = seconds;
        if (scene != null) {
            steps.addAll(scene.preamble());
            steps.addAll(scene.setup);
        }
    }

    public static boolean running() {
        return current != null;
    }

    /** Starts a run; null scene measures where the player stands. */
    public static void start(Scenes.Scene scene, int seconds) {
        com.meakaandre.quicksilver.phase2.ServerNumbers.record(); // P2-1: the server's tick history from here on
        BenchRun run = new BenchRun(scene, seconds);
        current = run;
        Minecraft mc = Minecraft.getInstance();
        if (scene != null) {
            run.previousRenderDistance = mc.options.renderDistance().get();
            mc.options.renderDistance().set(scene.renderDistance);
            String oit = scene.clientOptions == null ? null : scene.clientOptions.get("improvedTransparency");
            if (oit != null && Boolean.parseBoolean(oit) != mc.options.improvedTransparency().get()) {
                run.previousImprovedTransparency = mc.options.improvedTransparency().get();
                mc.options.improvedTransparency().set(Boolean.parseBoolean(oit));
                QuicksilverClient.LOG.info("Quicksilver bench: Improved Transparency set to {} for this scene (put back afterwards)", oit);
            }
        }
        QuicksilverClient.LOG.info("QUICKSILVER BENCH START scene={} seconds={}", run.sceneId, seconds);
        say("Bench " + run.sceneId + ": setting up" + (scene == null ? "" : " (needs commands allowed)") + ", then " + seconds + " s of measuring.");
    }

    public static void tick(Minecraft mc) {
        if (current != null) current.step(mc);
    }

    private void step(Minecraft mc) {
        LocalPlayer player = mc.player;
        if (player == null) {
            abort("left the world");
            return;
        }
        long now = System.nanoTime();
        switch (stage) {
            case SETUP -> {
                if (now < waitUntil) return;
                // one command per tick keeps them in order and lets "#wait" pause between them
                String next = steps.poll();
                if (next == null) {
                    stage = Stage.SETTLE;
                    waitUntil = now + (scene == null ? 0 : scene.settleSeconds) * 1_000_000_000L;
                } else if (next.startsWith("#wait ")) {
                    waitUntil = now + (long) (Double.parseDouble(next.substring(6).trim()) * 1e9);
                } else if (next.startsWith("#")) {
                    // a step for CI's driver only (e.g. "#chunks"): nothing to send
                } else {
                    player.connection.sendCommand(next);
                }
            }
            case SETTLE -> {
                if (now < waitUntil) return;
                if (scene != null && scene.screen != null && !screenOpen(mc)) {
                    stage = Stage.WAIT_SCREEN;
                    waitUntil = now + 120_000_000_000L;
                    QuicksilverClient.LOG.info("QUICKSILVER BENCH WAITING FOR SCREEN scene={}", sceneId);
                    if (scene.screen.prompt != null) say(scene.screen.prompt + " to start measuring.");
                    return;
                }
                beginMeasure(player, now);
            }
            case WAIT_SCREEN -> {
                if (screenOpen(mc)) {
                    beginMeasure(player, now);
                } else if (now > waitUntil) {
                    abort("the " + scene.screen.expect + " screen was not opened within 2 minutes");
                }
            }
            case MEASURE -> {
                pin(player, now);
                runSteps(player, now);
                memory.sample();
                if (now - measureStart >= seconds * 1_000_000_000L) finish(mc);
            }
            case DONE -> {
            }
        }
    }

    private boolean screenOpen(Minecraft mc) {
        Screen s = mc.gui.screen();
        return s != null && s.getClass().getName().toLowerCase(Locale.ROOT).contains(scene.screen.expect.toLowerCase(Locale.ROOT));
    }

    private void beginMeasure(LocalPlayer player, long now) {
        px = player.getX();
        py = player.getY();
        pz = player.getZ();
        yaw = player.getYRot();
        pitch = player.getXRot();
        stage = Stage.MEASURE;
        measureStart = now;
        nextStep = 0;
        FrameRecorder.start();
        memory.start();
        QuicksilverClient.LOG.info("QUICKSILVER BENCH MEASURING scene={} directBuffersMB={}", sceneId,
            Math.round(BenchReport.Memory.direct() / 10485.76) / 100.0);
    }

    /** Sends the scene's measure-time commands whose time has come (one per tick at most, in order). */
    private void runSteps(LocalPlayer player, long now) {
        if (scene == null || scene.measureSteps == null || nextStep >= scene.measureSteps.size()) return;
        Scenes.Step s = scene.measureSteps.get(nextStep);
        if ((now - measureStart) / 1e9 >= s.at) {
            nextStep++;
            QuicksilverClient.LOG.info("QUICKSILVER BENCH STEP scene={} t={} command={}", sceneId, s.at, s.command);
            player.connection.sendCommand(s.command);
        }
    }

    /** Keeps the camera where the scene put it, or moves it along the scripted path. */
    private void pin(LocalPlayer player, long now) {
        double t = (now - measureStart) / 1e9;
        double[] v = scene == null ? null : scene.velocity;
        double x = px, y = py, z = pz;
        if (v != null && v.length == 3) {
            x += v[0] * t;
            y += v[1] * t;
            z += v[2] * t;
        }
        player.setDeltaMovement(0, 0, 0);
        player.setPos(x, y, z);
        player.setYRot(yaw);
        player.setXRot(pitch);
        player.setYHeadRot(yaw);
    }

    private void finish(Minecraft mc) {
        FrameRecorder.stop();
        stage = Stage.DONE;
        current = null;
        restore(mc);
        try {
            BenchReport.write(sceneId, seconds, scene, memory);
            say("Bench " + sceneId + " done: results are in latest.log and quicksilver/bench/.");
        } catch (Exception e) {
            QuicksilverClient.LOG.error("Quicksilver bench could not write its report", e);
            say("Bench " + sceneId + " failed to write its report: " + e);
        }
    }

    private void abort(String why) {
        FrameRecorder.stop();
        stage = Stage.DONE;
        current = null;
        restore(Minecraft.getInstance());
        QuicksilverClient.LOG.warn("QUICKSILVER BENCH ABORTED scene={}: {}", sceneId, why);
        say("Bench " + sceneId + " stopped: " + why + ".");
    }

    private void restore(Minecraft mc) {
        if (previousRenderDistance > 0) mc.options.renderDistance().set(previousRenderDistance);
        if (previousImprovedTransparency != null) mc.options.improvedTransparency().set(previousImprovedTransparency);
        if (scene != null && scene.screen != null && mc.gui.screen() != null) mc.gui.setScreen(null);
    }

    static void say(String text) {
        LocalPlayer p = Minecraft.getInstance().player;
        if (p != null) Minecraft.getInstance().gui.hud.getChat().addClientSystemMessage(Component.literal("[Quicksilver] " + text));
    }
}
