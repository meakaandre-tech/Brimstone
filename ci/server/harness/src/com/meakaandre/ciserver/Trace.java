package com.meakaandre.ciserver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;

/**
 * CI only (P2-1, diagnosis of the pending scheduled ticks that varied between vanilla's own runs). With
 * {@code -Dci.harness.trace=x1,y1,z1,x2,y2,z2;...} (block boxes, inclusive) every scheduled block or fluid tick inside a
 * box is logged with the harness's stage, the run's tick, the game time, its trigger tick and the calls that scheduled
 * it; every chunk's post-processing and tick unpacking is logged too. Lines start {@code CI TRACE}; at most
 * {@link #MAX} lines per server start.
 */
public final class Trace {
    private static final int MAX = 6000;
    private static final List<int[]> BOXES = new ArrayList<>();
    private static int lines = 0;
    private static boolean on = false;

    private Trace() {
    }

    static void init() {
        String spec = System.getProperty("ci.harness.trace");
        if (spec == null || spec.isBlank()) return;
        for (String b : spec.split(";")) {
            String[] p = b.trim().split(",");
            int[] v = new int[6];
            for (int i = 0; i < 6; i++) v[i] = Integer.parseInt(p[i].trim());
            BOXES.add(v);
        }
        on = true;
        Harness.LOG.info("CI TRACE boxes {}", spec);
    }

    public static boolean on() {
        return on;
    }

    private static boolean inside(BlockPos p) {
        for (int[] b : BOXES) {
            if (p.getX() >= b[0] && p.getY() >= b[1] && p.getZ() >= b[2] && p.getX() <= b[3] && p.getY() <= b[4] && p.getZ() <= b[5]) return true;
        }
        return false;
    }

    private static synchronized void log(String s) {
        if (lines >= MAX) return;
        if (++lines == MAX) s += " (trace limit reached)";
        Harness.LOG.info("CI TRACE {} {}", Harness.where(), s);
    }

    public static void scheduled(Object type, BlockPos pos, long trigger) {
        if (!inside(pos)) return;
        String by = StackWalker.getInstance().walk(f -> f.skip(2).limit(7)
            .map(x -> x.getClassName().substring(x.getClassName().lastIndexOf('.') + 1) + "." + x.getMethodName())
            .collect(Collectors.joining("<")));
        log("sched " + type + " " + pos.getX() + "," + pos.getY() + "," + pos.getZ() + " trigger=" + trigger + " thread="
            + Thread.currentThread().getName() + " by=" + by);
    }

    public static void chunk(String what, int cx, int cz) {
        log(what + " chunk " + cx + "," + cz + " thread=" + Thread.currentThread().getName());
    }
}
