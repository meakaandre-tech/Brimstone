package com.meakaandre.quicksilver.memory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * P2-1, {@code memory.shape-dedup}: block shapes shared between block states that have equal ones (the idea is
 * FerriteCore's "blockstatecache" deduplication, MIT; this is Quicksilver's own code). With many blocks (Create's
 * especially) most of a modded server's shape memory is copies: every state builds its own collision and occlusion
 * shapes and, from them, its own face shapes.
 *
 * <p>Right after a state fills its cache (BlockStateBase#initCache, mixin/memory/BlockStateBaseMixin), every shape it
 * holds is compared, field by field, with the ones already seen; an equal one keeps its own identity but takes the
 * first one's internals (point lists, voxel data and its face-shape cache), as FerriteCore does, so the copies' internals
 * can be collected. A state's per-face occlusion array then holds the first equal face shape in each slot and, when a
 * fully filled array equals one already seen, is that array. Each state's per-side sturdiness array is shared the same
 * way. Shapes are immutable once built, so equal internals behave the same; the singletons Shapes.block() and
 * Shapes.empty() are never replaced or shared into.
 *
 * <p>The fields are found by type (VoxelShape, VoxelShape[] and boolean[] in BlockStateBase and its cache), so nothing
 * here names a field of the game's. The tables used for comparing are dropped once the server has started.
 */
public final class ShapeDedup {
    public static final String ID = "shape-dedup";

    // keyed by class name: a Class key would ask for its identity hash (see hash)
    private static final Map<String, Field[]> FIELDS = new HashMap<>();
    private static Map<Key, VoxelShape> shapes = new HashMap<>();
    private static Map<ArrayKey, VoxelShape[]> arrays = new HashMap<>();
    private static Map<BoolKey, boolean[]> bools = new HashMap<>();
    private static Field[] stateShapes, stateArrays, cacheShapes, cacheArrays, cacheBools;
    private static Field cacheField;
    private static boolean found = false;
    static long states = 0, shared = 0, arraysShared = 0, slotsShared = 0, boolsShared = 0;

    private ShapeDedup() {
    }

    private static Field[] byType(Class<?> c, Class<?> type) {
        List<Field> out = new ArrayList<>();
        for (Field f : c.getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers()) && f.getType() == type) {
                f.setAccessible(true);
                out.add(f);
            }
        }
        return out.toArray(new Field[0]);
    }

    private static void find() {
        if (found) return;
        found = true;
        Class<?> base = BlockBehaviour.BlockStateBase.class;
        stateShapes = byType(base, VoxelShape.class);
        stateArrays = byType(base, VoxelShape[].class);
        for (Field f : base.getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers()) && f.getType().getName().endsWith("$Cache")) {
                if (cacheField != null) throw new IllegalStateException("BlockStateBase has more than one cache field");
                cacheField = f;
                f.setAccessible(true);
            }
        }
        if (cacheField == null) throw new IllegalStateException("BlockStateBase has no cache field");
        cacheShapes = byType(cacheField.getType(), VoxelShape.class);
        cacheArrays = byType(cacheField.getType(), VoxelShape[].class);
        cacheBools = byType(cacheField.getType(), boolean[].class);
    }

    /** A state has just filled its cache. */
    public static synchronized void afterInitCache(BlockBehaviour.BlockStateBase state) {
        if (!Memory.on(ID)) return;
        try {
            find();
            if (shapes == null) {  // a later cache rebuild (a resource reload): fresh tables, dropped again at the next start
                shapes = new HashMap<>();
                arrays = new HashMap<>();
                bools = new HashMap<>();
            }
            states++;
            for (Field f : stateShapes) share((VoxelShape) f.get(state));
            for (Field f : stateArrays) {
                VoxelShape[] a = (VoxelShape[]) f.get(state);
                VoxelShape[] d = dedupArray(a);
                if (d != a) f.set(state, d);
            }
            Object cache = cacheField.get(state);
            if (cache == null) return;
            for (Field f : cacheShapes) share((VoxelShape) f.get(cache));
            for (Field f : cacheArrays) {
                VoxelShape[] a = (VoxelShape[]) f.get(cache);
                VoxelShape[] d = dedupArray(a);
                if (d != a) f.set(cache, d);
            }
            for (Field f : cacheBools) {
                boolean[] b = (boolean[]) f.get(cache);
                if (b == null) continue;
                boolean[] d = bools.computeIfAbsent(new BoolKey(b), k -> b);
                if (d != b) {
                    f.set(cache, d);
                    boolsShared++;
                }
            }
        } catch (Throwable t) {
            Memory.fallback(ID, "could not share the shapes of " + state, t);
        }
    }

    /** After start-up, and after any later rebuild (each server and client tick): the comparison tables go (the shared
     * shapes stay). */
    public static synchronized void dropTables() {
        if (shapes == null) return;
        shapes = null;
        arrays = null;
        bools = null;
    }

    static String counts() {
        return states + " states, " + shared + " shapes sharing internals, " + slotsShared + " face slots and " + arraysShared
            + " face arrays shared, " + boolsShared + " sturdiness arrays shared";
    }

    private static boolean singleton(VoxelShape s) {
        return s == Shapes.block() || s == Shapes.empty();
    }

    /** The first shape seen that equals this one (this one, if it is the first). */
    private static VoxelShape canonical(VoxelShape s) throws IllegalAccessException {
        if (s == null || singleton(s)) return s;
        return shapes.computeIfAbsent(new Key(s), k -> s);
    }

    /** An equal shape seen before lends this one its internals. */
    private static void share(VoxelShape s) throws IllegalAccessException {
        VoxelShape c = canonical(s);
        if (c == null || c == s) return;
        for (Class<?> k = s.getClass(); k != null && k != Object.class; k = k.getSuperclass()) {
            for (Field f : fields(k)) {
                if (f.getType().isPrimitive()) continue;
                Object mine = f.get(s), theirs = f.get(c);
                // a cache the first one hasn't filled yet but this one has: the first one takes it
                if (theirs == null && mine != null && f.getType() == VoxelShape[].class) {
                    f.set(c, mine);
                    theirs = mine;
                }
                if (mine != theirs) f.set(s, theirs);
            }
        }
        shared++;
    }

    /** A per-face shape array: each slot the first equal shape seen, and a fully filled array the first equal one. */
    private static VoxelShape[] dedupArray(VoxelShape[] a) throws IllegalAccessException {
        if (a == null) return null;
        boolean full = true;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == null) {
                full = false;
                continue;
            }
            share(a[i]);
            VoxelShape c = canonical(a[i]);
            if (c != a[i]) {
                a[i] = c;
                slotsShared++;
            }
        }
        if (!full) return a;  // filled lazily later: this state's own
        VoxelShape[] d = arrays.computeIfAbsent(new ArrayKey(a.clone()), k -> a);
        if (d != a) arraysShared++;
        return d;
    }

    // ------------------------------------------------------------------ comparing shapes field by field

    private static Field[] fields(Class<?> c) {
        return FIELDS.computeIfAbsent(c.getName(), k -> {
            List<Field> out = new ArrayList<>();
            for (Field f : c.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers())) continue;
                f.setAccessible(true);
                out.add(f);
            }
            return out.toArray(new Field[0]);
        });
    }

    /** Whether a field is a cache (a shape's per-face shapes): never compared. */
    private static boolean cache(Field f) {
        return f.getType() == VoxelShape[].class;
    }

    static boolean same(Object a, Object b) throws IllegalAccessException {
        if (a == b) return true;
        if (a == null || b == null || a.getClass() != b.getClass()) return false;
        if (a instanceof Enum<?>) return false;
        if (a instanceof VoxelShape || a instanceof DiscreteVoxelShape) {
            for (Class<?> k = a.getClass(); k != null && k != Object.class; k = k.getSuperclass()) {
                for (Field f : fields(k)) {
                    if (cache(f)) continue;
                    Object x = f.get(a), y = f.get(b);
                    if (f.getType().isPrimitive() ? !x.equals(y) : !same(x, y)) return false;
                }
            }
            return true;
        }
        if (a instanceof BitSet || a instanceof java.util.List<?> || a instanceof Number || a instanceof String || a instanceof Boolean) return a.equals(b);
        if (a.getClass().isArray()) return Arrays.deepEquals(new Object[] {a}, new Object[] {b});
        return a.equals(b);
    }

    static int hash(Object a, int depth) throws IllegalAccessException {
        if (a == null) return 0;
        if (depth > 8) return a.getClass().getName().hashCode();
        if (a instanceof VoxelShape || a instanceof DiscreteVoxelShape) {
            int h = a.getClass().getName().hashCode();
            for (Class<?> k = a.getClass(); k != null && k != Object.class; k = k.getSuperclass()) {
                for (Field f : fields(k)) {
                    if (cache(f)) continue;
                    Object x = f.get(a);
                    h = 31 * h + (f.getType().isPrimitive() ? x.hashCode() : hash(x, depth + 1));
                }
            }
            return h;
        }
        // never an object's identity hash: asking for one changes every identity hash the server thread hands out after it
        // (hash sets of blocks and entities would then iterate in another order than vanilla's)
        if (a instanceof Enum<?> e) return e.ordinal();
        if (a instanceof Number || a instanceof String || a instanceof Boolean || a instanceof BitSet
            || a instanceof it.unimi.dsi.fastutil.doubles.DoubleList || a instanceof it.unimi.dsi.fastutil.ints.IntList) return a.hashCode();
        if (a.getClass().isArray() && a.getClass().getComponentType().isPrimitive()) {
            int h = 1;
            for (int i = 0, n = java.lang.reflect.Array.getLength(a); i < n; i++) h = 31 * h + java.lang.reflect.Array.get(a, i).hashCode();
            return h;
        }
        return a.getClass().getName().hashCode();
    }

    /** A shape compared and hashed by its contents. */
    private static final class Key {
        final VoxelShape shape;
        final int hash;

        Key(VoxelShape shape) throws IllegalAccessException {
            this.shape = shape;
            this.hash = ShapeDedup.hash(shape, 0);
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object o) {
            try {
                return o instanceof Key k && k.hash == hash && same(shape, k.shape);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /** A face array: its shapes are already the first equal ones seen, so equal arrays hold the same shapes. Hashed by
     * the shapes' contents, never their identity (see hash). */
    private static final class ArrayKey {
        final VoxelShape[] shapes;
        final int hash;

        ArrayKey(VoxelShape[] shapes) throws IllegalAccessException {
            this.shapes = shapes;
            int h = 1;
            for (VoxelShape s : shapes) h = 31 * h + (s == Shapes.block() ? 1 : s == Shapes.empty() ? 2 : ShapeDedup.hash(s, 0));
            this.hash = h;
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ArrayKey k) || k.hash != hash || k.shapes.length != shapes.length) return false;
            for (int i = 0; i < shapes.length; i++) if (shapes[i] != k.shapes[i]) return false;
            return true;
        }
    }

    private record BoolKey(boolean[] values) {
        @Override
        public boolean equals(Object o) {
            return o instanceof BoolKey k && Arrays.equals(values, k.values);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(values);
        }
    }
}
