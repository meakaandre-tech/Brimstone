package com.meakaandre.quicksilver.logic;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.util.Continuation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.phys.AABB;

/**
 * P2-2, logic.entity-collisions: a moving entity asks for the entities it collides with like solid blocks
 * (EntityGetter#getEntityCollisions). Only an entity whose class overrides {@code Entity#canBeCollidedWith(Entity)}
 * (boats, shulkers, minecarts and the like; vanilla's own version always answers no) can be one, unless the moving
 * entity's class overrides {@code canCollideWith(Entity)} (then anything may be). Each entity section counts the
 * entities it holds whose class overrides canBeCollidedWith; when every section vanilla's lookup would visit counts
 * none, vanilla's lookup finds no entity, and its answer (an empty list) is returned without walking the entities.
 *
 * <p>Only for a server level without ender dragon parts (they are added to every lookup outside the sections). Which
 * methods a class overrides is found by reflection on a thread of the module's own (made at start-up): reflection can
 * ask the JVM for a class's identity hash, and on the server thread that would move every identity hash it hands out
 * later (P2-1's rule). Each answer is kept on the entity's type, for that class.
 */
public final class EntityCollisions {
    public static final String ID = "entity-collisions";
    public static final Logic.Switch SWITCH = new Logic.Switch(ID);

    private static final String BE = "canBeCollidedWith";
    private static final String DO = "canCollideWith";

    /** What a class overrides: its own canBeCollidedWith (it can be hard), its own canCollideWith (it collides with anything). */
    public record Kind(Class<?> cls, boolean hard, boolean custom) {
    }

    /** Kept on each EntityType: the last class seen with that type and what it overrides. */
    public interface TypeMemo {
        Kind quicksilver$kind();

        void quicksilver$kind(Kind kind);
    }

    /** The count of hard entities, kept on each EntitySection. */
    public interface SectionCount {
        int quicksilver$hard();
    }

    /** ServerLevel's entity manager (accessor mixin). */
    public interface LevelEntities {
        net.minecraft.world.level.entity.PersistentEntitySectionManager<Entity> quicksilver$entityManager();
    }

    /** PersistentEntitySectionManager's section storage (accessor mixin). */
    public interface ManagerSections {
        EntitySectionStorage<Entity> quicksilver$sectionStorage();
    }

    private static ExecutorService scanner;

    private EntityCollisions() {
    }

    /** Main thread, at start-up: the reflection thread. */
    static void init() {
        if (!SWITCH.on) return;
        scanner = Executors.newSingleThreadExecutor(new Named());
        // started now, from this thread, so the server thread never creates it
        try {
            scanner.submit(new Noop()).get();
        } catch (Exception e) {
            Logic.fallback(SWITCH, "could not start its reflection thread: " + e, e);
        }
    }

    private static final class Named implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Quicksilver class scan");
            t.setDaemon(true);
            return t;
        }
    }

    private static final class Noop implements Runnable {
        @Override
        public void run() {
        }
    }

    private static final class Scan implements java.util.function.Supplier<Kind> {
        private final Class<?> cls;

        Scan(Class<?> cls) {
            this.cls = cls;
        }

        @Override
        public Kind get() {
            return new Kind(cls, overrides(cls, BE), overrides(cls, DO));
        }
    }

    /** Whether a class below Entity declares {@code name(Entity)} itself (unreadable classes count as overriding). */
    private static boolean overrides(Class<?> c, String name) {
        try {
            for (; c != null && c != Entity.class; c = c.getSuperclass()) {
                for (java.lang.reflect.Method m : c.getDeclaredMethods()) {
                    if (m.getName().equals(name) && m.getParameterCount() == 1 && m.getParameterTypes()[0] == Entity.class) return true;
                }
            }
            return false;
        } catch (Throwable t) {
            return true;
        }
    }

    /** What the entity's class overrides (found once per type and class, on the reflection thread). */
    public static Kind kind(Entity e) {
        Class<?> c = e.getClass();
        TypeMemo memo = (TypeMemo) e.getType();
        Kind k = memo.quicksilver$kind();
        if (k != null && k.cls() == c) return k;
        if (scanner == null) throw new IllegalStateException("no reflection thread");
        try {
            k = CompletableFuture.supplyAsync(new Scan(c), scanner).get();
        } catch (Exception ex) {
            throw new IllegalStateException("class scan failed for " + c.getName(), ex);
        }
        memo.quicksilver$kind(k);
        return k;
    }

    /**
     * Whether an entity counts in its section: its class can be collided with like a block. Once the optimisation has
     * fallen back the counts are no longer read, so nothing is counted.
     */
    public static boolean counts(Object o) {
        if (!SWITCH.on || !(o instanceof Entity e)) return false;
        try {
            return kind(e).hard();
        } catch (Throwable t) {
            SWITCH.fail(t);
            return false;
        }
    }

    /**
     * Whether vanilla's lookup for {@code entity} in {@code box} would find no entity: true only when it can be told
     * from the section counts alone; false means vanilla's lookup runs as usual.
     */
    public static boolean none(Object getter, Entity entity, AABB box) {
        if (!(getter instanceof ServerLevel level)) return false;
        if (entity != null && kind(entity).custom()) return false;
        if (!level.dragonParts().isEmpty()) return false;
        if (box.getSize() < 1.0E-7) return false;  // vanilla answers without a lookup: leave it to vanilla
        Any any = new Any();
        ((ManagerSections) ((LevelEntities) level).quicksilver$entityManager()).quicksilver$sectionStorage()
            .forEachAccessibleNonEmptySection(box.inflate(1.0E-7), any);
        return !any.found;
    }

    private static final class Any implements AbortableIterationConsumer<EntitySection<Entity>> {
        boolean found;

        @Override
        public Continuation accept(EntitySection<Entity> section) {
            if (((SectionCount) section).quicksilver$hard() > 0) {
                found = true;
                return Continuation.ABORT;
            }
            return Continuation.CONTINUE;
        }
    }
}
