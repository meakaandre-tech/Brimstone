package com.meakaandre.quicksilver.mesher;

import com.google.gson.JsonObject;
import com.meakaandre.quicksilver.QuicksilverClient;
import com.meakaandre.quicksilver.bench.BenchRun;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.phys.Vec3;

/**
 * Which waiting section vanilla's meshing threads take next (design doc, package 4c: nearest and visible first).
 * Vanilla takes the nearest waiting section, alternating between first meshes and re-meshes; Quicksilver makes the same
 * choice among the sections the camera can see this frame (vanilla's visible list) whenever any of them waits, and
 * among all of them otherwise. Only the order changes: every section is still meshed, by the same mesher.
 *
 * <p>The choice is vanilla's own rule (nearest by distance to the section's centre; a re-mesh first when it is nearer
 * than the nearest first mesh and the re-mesh allowance of 2 is not used up, otherwise the nearest first mesh), applied
 * to the visible ones first. If vanilla's queue does not look as expected, the module stays out of the way.
 */
public final class MeshPriority {
    private MeshPriority() {
    }

    private static final boolean DISABLED = Boolean.getBoolean("quicksilver.debug.vanillaMeshOrder") || Boolean.getBoolean("quicksilver.ci.measureOnly");
    /** This frame's visible sections: a new set each frame, never changed after, so a worker reading an older one is safe. */
    private static volatile LongOpenHashSet visible = new LongOpenHashSet();
    static volatile boolean off;
    static volatile boolean seen;
    private static final AtomicLong VISIBLE_PICKS = new AtomicLong(), ALL_PICKS = new AtomicLong(), BENCH_VISIBLE = new AtomicLong(), BENCH_ALL = new AtomicLong();

    /** Render thread, once a frame: the sections vanilla found visible this frame. */
    public static void frame() {
        if (DISABLED || off) return;
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc.levelRenderer == null) return;
            List<SectionRenderDispatcher.RenderSection> list = mc.levelRenderer.visibleSections();
            LongOpenHashSet s = new LongOpenHashSet(Math.max(16, list.size()));
            for (int i = 0, n = list.size(); i < n; i++) s.add(list.get(i).getSectionNode());
            visible = s;
        } catch (Throwable t) {
            off("reading vanilla's visible sections", t);
        }
    }

    /**
     * Vanilla's queue is about to hand out its next task: the index in {@code tasks} to take (cancelled tasks removed
     * first, as vanilla does), -1 for none, or -2 to let vanilla choose. {@code quota}: vanilla's re-mesh allowance, read
     * and updated as vanilla updates it ({@code quota[0]}).
     */
    public static int choose(List<?> tasks, Vec3 camera, int[] quota) {
        if (DISABLED || off) return -2;
        try {
            seen = true;
            LongOpenHashSet vis = visible;
            int bestFirst = -1, bestRe = -1, visFirst = -1, visRe = -1;
            double dFirst = Double.MAX_VALUE, dRe = Double.MAX_VALUE, vdFirst = Double.MAX_VALUE, vdRe = Double.MAX_VALUE;
            for (int i = 0; i < tasks.size(); i++) {
                Object t = tasks.get(i);
                TaskFields f = fields(t);
                if (f.cancelled(t)) {
                    tasks.remove(i--);
                    continue;
                }
                SectionRenderDispatcher.RenderSection section = (SectionRenderDispatcher.RenderSection) f.sectionOf(t);
                BlockPos o = section.getRenderOrigin();
                double d = o.distToCenterSqr(camera);
                boolean re = f.recompileOf(t);
                boolean v = vis.contains(SectionPos.asLong(o.getX() >> 4, o.getY() >> 4, o.getZ() >> 4));
                if (re) {
                    if (d < dRe) {
                        dRe = d;
                        bestRe = i;
                    }
                    if (v && d < vdRe) {
                        vdRe = d;
                        visRe = i;
                    }
                } else {
                    if (d < dFirst) {
                        dFirst = d;
                        bestFirst = i;
                    }
                    if (v && d < vdFirst) {
                        vdFirst = d;
                        visFirst = i;
                    }
                }
            }
            boolean anyVisible = visFirst >= 0 || visRe >= 0;
            int first = anyVisible ? visFirst : bestFirst, re = anyVisible ? visRe : bestRe;
            double df = anyVisible ? vdFirst : dFirst, dr = anyVisible ? vdRe : dRe;
            int pick;
            if (re >= 0 && (first < 0 || quota[0] > 0 && dr < df)) {
                quota[0]--;
                pick = re;
            } else {
                quota[0] = 2;
                pick = first;
            }
            if (pick >= 0) {
                (anyVisible ? VISIBLE_PICKS : ALL_PICKS).incrementAndGet();
                if (BenchRun.running()) (anyVisible ? BENCH_VISIBLE : BENCH_ALL).incrementAndGet();
            }
            return pick;
        } catch (Throwable t) {
            off("choosing the next section to mesh", t);
            return -2;
        }
    }

    /**
     * What Quicksilver reads from vanilla's meshing task, found once by type (vanilla's task class is not public): its
     * section (the task's enclosing section), its cancelled flag (an atomic flag, or a boolean named for it) and
     * whether it re-meshes a section that has a mesh (the other boolean). Anything else: the order stays vanilla's.
     */
    private record TaskFields(java.lang.invoke.VarHandle section, java.lang.invoke.VarHandle cancelled, boolean atomic,
                              java.lang.invoke.VarHandle recompile) {
        boolean cancelled(Object t) {
            return atomic ? ((java.util.concurrent.atomic.AtomicBoolean) cancelled.get(t)).get() : (boolean) cancelled.get(t);
        }

        Object sectionOf(Object t) {
            return section.get(t);
        }

        boolean recompileOf(Object t) {
            return (boolean) recompile.get(t);
        }
    }

    private static final java.util.Map<Class<?>, TaskFields> FIELDS = new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * The task's fields, found once by type and name (read through VarHandles: this runs under vanilla's queue lock for
     * every waiting task). The cancelled flag: the one field named for it (an atomic flag or a boolean); the re-mesh
     * flag: the only other boolean. Anything else, or more than one candidate: the order stays vanilla's.
     */
    private static TaskFields fields(Object task) {
        return FIELDS.computeIfAbsent(task.getClass(), c -> {
            java.lang.reflect.Field section = null, cancelled = null, recompile = null;
            int sections = 0, cancels = 0, booleans = 0;
            for (Class<?> k = c; k != null && k != Object.class; k = k.getSuperclass()) {
                for (java.lang.reflect.Field g : k.getDeclaredFields()) {
                    if (java.lang.reflect.Modifier.isStatic(g.getModifiers())) continue;
                    Class<?> type = g.getType();
                    String name = g.getName().toLowerCase(java.util.Locale.ROOT);
                    if (type == SectionRenderDispatcher.RenderSection.class) {
                        section = g;
                        sections++;
                    } else if ((type == java.util.concurrent.atomic.AtomicBoolean.class || type == boolean.class) && name.contains("cancel")) {
                        cancelled = g;
                        cancels++;
                    } else if (type == boolean.class) {
                        recompile = g;
                        booleans++;
                    }
                }
            }
            if (sections != 1 || cancels != 1 || booleans != 1) {
                throw new IllegalStateException("vanilla's meshing task does not look as expected (" + c.getName() + ": " + sections
                    + " sections, " + cancels + " cancelled flags, " + booleans + " other booleans)");
            }
            try {
                java.lang.invoke.MethodHandles.Lookup l = java.lang.invoke.MethodHandles.privateLookupIn(c, java.lang.invoke.MethodHandles.lookup());
                TaskFields f = new TaskFields(handle(l, section), handle(l, cancelled), cancelled.getType() != boolean.class, handle(l, recompile));
                QuicksilverClient.LOG.info("Quicksilver mesher: nearest-and-visible-first order reads {}'s section, cancelled and re-mesh flags ({}, {}, {})",
                    c.getSimpleName(), section.getName(), cancelled.getName(), recompile.getName());
                return f;
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("vanilla's meshing task's fields are not reachable", e);
            }
        });
    }

    private static java.lang.invoke.VarHandle handle(java.lang.invoke.MethodHandles.Lookup own, java.lang.reflect.Field f) throws ReflectiveOperationException {
        java.lang.invoke.MethodHandles.Lookup l = f.getDeclaringClass() == own.lookupClass() ? own
            : java.lang.invoke.MethodHandles.privateLookupIn(f.getDeclaringClass(), java.lang.invoke.MethodHandles.lookup());
        return l.unreflectVarHandle(f);
    }

    private static void off(String where, Throwable t) {
        if (off) return;
        off = true;
        QuicksilverClient.LOG.error("Quicksilver mesher: nearest-and-visible-first order off, vanilla's order from now on (fallback in {})", where, t);
    }

    public static void startBench() {
        BENCH_VISIBLE.set(0);
        BENCH_ALL.set(0);
    }

    static JsonObject json() {
        JsonObject o = new JsonObject();
        o.addProperty("state", DISABLED ? "off (debug or measure only)" : off ? "off (fallback)" : seen ? "on" : "not seen");
        o.addProperty("benchPicksVisible", BENCH_VISIBLE.get());
        o.addProperty("benchPicksNoneVisibleWaiting", BENCH_ALL.get());
        o.addProperty("picksVisibleSinceStart", VISIBLE_PICKS.get());
        o.addProperty("picksNoneVisibleWaitingSinceStart", ALL_PICKS.get());
        return o;
    }
}
