package com.meakaandre.quicksilver.logic;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.util.Continuation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.phys.AABB;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * P2-2, logic.entity-collisions: a moving entity asks for the entities it collides with like solid blocks
 * (EntityGetter#getEntityCollisions). Only an entity whose class overrides {@code Entity#canBeCollidedWith(Entity)}
 * (boats, shulkers, minecarts and the like; vanilla's own version always answers no) can be one, unless the moving
 * entity's class overrides {@code canCollideWith(Entity)} (then anything may be). Each entity section counts the
 * entities it holds that may be hard; when every section vanilla's lookup would visit counts none, vanilla's lookup
 * finds no entity, and its answer (an empty list) is returned without walking the entities.
 *
 * <p>Which classes override what is read once at start-up, on the main thread, from the game jar's entity class files
 * (ASM, by name: no Class objects, no reflection, no threads; each of those asks the JVM for identity hashes, and on or
 * before the server thread that moves every identity hash it hands out later, P2-1's rule). A class that isn't one of
 * the game's entity classes (another mod's) counts as hard and as colliding with anything, so lookups near it, or by it,
 * take vanilla's path. Only for a server level without ender dragon parts.
 */
public final class EntityCollisions {
    public static final String ID = "entity-collisions";
    public static final Logic.Switch SWITCH = new Logic.Switch(ID);

    /** Kind bits: the class (or one between it and Entity) overrides canBeCollidedWith / canCollideWith. */
    static final byte HARD = 1, CUSTOM = 2, UNKNOWN = HARD | CUSTOM;

    private static final String ENTITY = "net/minecraft/world/entity/Entity";
    private static final String DESC = "(L" + ENTITY + ";)Z";

    /** The game's entity classes by binary name, and their kind bits (filled once at start-up, then only read). */
    private static Map<String, Byte> kinds = Map.of();

    /** Kept on each EntityType: the last class seen with it and that class's kind bits. */
    public interface TypeMemo {
        Class<?> quicksilver$memoClass();

        byte quicksilver$memoKind();

        void quicksilver$memo(Class<?> cls, byte kind);
    }

    /** The count of maybe-hard entities, kept on each EntitySection. */
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

    private EntityCollisions() {
    }

    /** Main thread, at start-up: reads the game's entity classes. */
    static void init() {
        if (!SWITCH.on) return;
        try {
            kinds = scan();
            // the game must look as expected: Entity itself plain, shulkers hard, minecarts colliding with anything
            byte entity = kinds.getOrDefault("net.minecraft.world.entity.Entity", (byte) -1);
            byte shulker = kinds.getOrDefault("net.minecraft.world.entity.monster.Shulker", (byte) -1);
            byte minecart = kinds.getOrDefault("net.minecraft.world.entity.vehicle.minecart.Minecart",
                kinds.getOrDefault("net.minecraft.world.entity.vehicle.Minecart", (byte) -1));
            if (entity != 0 || shulker < 0 || (shulker & HARD) == 0 || minecart < 0 || (minecart & CUSTOM) == 0) {
                Logic.fallback(SWITCH, "the game's entity classes don't look as expected (Entity " + entity + ", Shulker " + shulker
                    + ", Minecart " + minecart + ", " + kinds.size() + " classes)", null);
            }
        } catch (Throwable t) {
            Logic.fallback(SWITCH, "could not read the game's entity classes: " + t, t);
        }
    }

    private record Info(String superName, byte own) {
    }

    private static Map<String, Byte> scan() throws Exception {
        Map<String, Info> infos = new HashMap<>();
        var game = FabricLoader.getInstance().getModContainer("minecraft").orElseThrow();
        for (Path root : game.getRootPaths()) {
            Path dir = root.resolve("net/minecraft/world/entity");
            if (!Files.isDirectory(dir)) continue;
            try (Stream<Path> files = Files.walk(dir)) {
                for (Path p : (Iterable<Path>) files::iterator) {
                    if (!p.toString().endsWith(".class")) continue;
                    try (InputStream in = Files.newInputStream(p)) {
                        ClassReader r = new ClassReader(in);
                        Own own = new Own();
                        r.accept(own, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                        infos.put(r.getClassName(), new Info(r.getSuperName(), own.bits));
                    }
                }
            }
        }
        Map<String, Byte> out = new HashMap<>();
        for (String name : infos.keySet()) {
            byte bits = 0;
            String c = name;
            boolean entity = false;
            for (int depth = 0; c != null && depth < 64; depth++) {
                if (c.equals(ENTITY)) {
                    entity = true;
                    break;
                }
                Info i = infos.get(c);
                if (i == null) break;
                bits |= i.own();
                c = i.superName();
            }
            if (entity) out.put(name.replace('/', '.'), bits);
        }
        return out;
    }

    /** One class file's own declarations of the two methods. */
    private static final class Own extends ClassVisitor {
        byte bits;

        Own() {
            super(Opcodes.ASM9);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if ((access & Opcodes.ACC_STATIC) == 0 && desc.equals(DESC)) {
                if (name.equals("canBeCollidedWith")) bits |= HARD;
                if (name.equals("canCollideWith")) bits |= CUSTOM;
            }
            return null;
        }
    }

    /** The entity's kind bits (a class not among the game's entity classes: UNKNOWN). */
    static byte kind(Entity e) {
        Class<?> c = e.getClass();
        TypeMemo memo = (TypeMemo) e.getType();
        if (memo.quicksilver$memoClass() == c) return memo.quicksilver$memoKind();
        Byte k = kinds.get(c.getName());
        byte b = k == null ? UNKNOWN : k;
        memo.quicksilver$memo(c, b);
        return b;
    }

    /**
     * Whether an entity counts in its section: it may be collided with like a block. Once the optimisation has fallen
     * back the counts are no longer read, so nothing is counted.
     */
    public static boolean counts(Object o) {
        if (!SWITCH.on || !(o instanceof Entity e)) return false;
        try {
            return (kind(e) & HARD) != 0;
        } catch (Throwable t) {
            SWITCH.fail(t);
            return false;
        }
    }

    /**
     * Whether vanilla's lookup for {@code entity} in {@code box} (the box vanilla's getEntities call gets) would find no entity: true only when it can be told
     * from the section counts alone; false means vanilla's lookup runs as usual.
     */
    public static boolean none(Object getter, Entity entity, AABB box) {
        if (!(getter instanceof ServerLevel level)) return false;
        if (entity != null && (kind(entity) & CUSTOM) != 0) return false;
        if (!level.dragonParts().isEmpty()) return false;
        Any any = new Any();
        // the box vanilla's lookup is given (already widened by 1e-7 there), so the same sections are visited
        ((ManagerSections) ((LevelEntities) level).quicksilver$entityManager()).quicksilver$sectionStorage()
            .forEachAccessibleNonEmptySection(box, any);
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
