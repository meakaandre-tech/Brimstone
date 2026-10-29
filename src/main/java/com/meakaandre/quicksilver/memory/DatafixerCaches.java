package com.meakaandre.quicksilver.memory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.server.MinecraftServer;

/**
 * P2-1, {@code memory.datafixer-caches}: the data fixer's rewrite caches emptied when it has been idle. Each time the
 * game upgrades old data (a structure file from an older version, as Terralith ships, or an old chunk), DataFixerUpper
 * works out a rewrite for that type and version and keeps it, with every intermediate type it built, in static maps
 * for the rest of the session (about 60 MB with Terralith). Here, once the server has started and then every five
 * minutes of ticks, if no new rewrite was cached since the last look, those maps are emptied (Type's REWRITE_CACHE and
 * Fold's HMAP_CACHE and HMAP_APPLY_CACHE; the in-flight map is left alone). A later upgrade works its rewrite out
 * again, taking a little longer once; the upgraded data is the same (the maps are pure memoisation). The maps are found
 * by name in DataFixerUpper (MIT, Mojang); if any is missing the optimisation falls back and nothing is cleared.
 */
public final class DatafixerCaches {
    public static final String ID = "datafixer-caches";
    private static final int PERIOD = 6000;  // ticks
    private static final List<Map<?, ?>> MAPS = new ArrayList<>();
    private static boolean found = false;
    private static long lastSize = -1, cleared = 0, emptied = 0;
    private static int ticks = 0;

    private DatafixerCaches() {
    }

    private static void find() throws ReflectiveOperationException {
        if (found) return;
        found = true;
        add("com.mojang.datafixers.types.Type", "REWRITE_CACHE");
        add("com.mojang.datafixers.functions.Fold", "HMAP_CACHE");
        add("com.mojang.datafixers.functions.Fold", "HMAP_APPLY_CACHE");
    }

    private static void add(String cls, String name) throws ReflectiveOperationException {
        Field f = Class.forName(cls).getDeclaredField(name);
        if (!Modifier.isStatic(f.getModifiers()) || !Map.class.isAssignableFrom(f.getType())) {
            throw new IllegalStateException(cls + "." + name + " is not a static map");
        }
        f.setAccessible(true);
        MAPS.add((Map<?, ?>) f.get(null));
    }

    private static long size() {
        long n = 0;
        for (Map<?, ?> m : MAPS) n += m.size();
        return n;
    }

    /** Server started: empty the maps now. */
    static void started(MinecraftServer server) {
        if (!Memory.on(ID)) return;
        try {
            find();
            clear();
        } catch (Throwable t) {
            Memory.fallback(ID, "the data fixer's caches could not be found", t);
        }
    }

    /** Every server tick: every PERIOD ticks, empty the maps if nothing new was cached since the last look. */
    static void tick(MinecraftServer server) {
        if (++ticks < PERIOD || !Memory.on(ID) || !found) return;
        ticks = 0;
        try {
            long n = size();
            if (n > 0 && n == lastSize) clear();
            lastSize = size();
        } catch (Throwable t) {
            Memory.fallback(ID, "the data fixer's caches could not be emptied", t);
        }
    }

    private static void clear() {
        long n = size();
        for (Map<?, ?> m : MAPS) m.clear();
        if (n > 0) {
            cleared++;
            emptied += n;
        }
        lastSize = 0;
    }

    static String counts() {
        return cleared + " times, " + emptied + " entries";
    }
}
