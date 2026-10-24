package com.meakaandre.brimstone.cidiag;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * DIAGNOSTIC (branch only, removed before merge): finds what makes Create's outliner draw the Build Gun hologram in one
 * of two brightness states. Logs a line whenever a watched state changes, and every 5 s regardless, to latest.log.
 */
public final class CiDiag {
    public static volatile String lighting = "?";
    private static final Map<String, String> LAST = new HashMap<>();
    private static final Map<String, Long> LAST_TIME = new HashMap<>();
    private static final Map<String, Integer> LINES = new HashMap<>();
    public static long frame;

    private CiDiag() {
    }

    public static synchronized void log(String key, String state) {
        long now = System.nanoTime();
        String prev = LAST.get(key);
        Long t = LAST_TIME.get(key);
        if (state.equals(prev) && t != null && now - t < 5_000_000_000L) return;
        int n = LINES.merge(key, 1, Integer::sum);
        if (n > 1500) return;
        LAST.put(key, state);
        LAST_TIME.put(key, now);
        System.out.println("BRIMSTONE CIDIAG " + key + (state.equals(prev) ? " (same)" : " CHANGED") + " frame=" + frame + " " + state);
    }

    private static final StringBuilder DRAWS = new StringBuilder();

    /** Render thread: one feature draw (or phase marker) of this frame. */
    public static void draw(String what) {
        if (DRAWS.length() < 20000) DRAWS.append(what).append(' ');
    }

    /** Render thread, frame start: logs the last frame's draw order when it drew Create's outline. */
    public static void frameStart() {
        String s = DRAWS.toString();
        DRAWS.setLength(0);
        if (s.contains("outline")) log("draworder", s);
    }

    public static Object field(Object o, String name) {
        for (Class<?> c = o.getClass(); c != null; c = c.getSuperclass()) {
            try {
                Field f = c.getDeclaredField(name);
                f.setAccessible(true);
                return f.get(o);
            } catch (NoSuchFieldException e) {
                // superclass
            } catch (Throwable e) {
                return "err:" + e;
            }
        }
        return "none";
    }

    public static String r(float f) {
        return String.valueOf(Math.round(f * 10000f) / 10000f);
    }
}
