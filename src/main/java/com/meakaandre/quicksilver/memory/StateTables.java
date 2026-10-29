package com.meakaandre.quicksilver.memory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * P2-1, {@code memory.state-neighbours} (the idea and the table from FerriteCore's "fastmap", MIT: see StateTable and
 * NOTICE). Vanilla gives every block state (and fluid state) its own neighbour arrays, one array per property holding
 * the state for each of that property's values, so BlockState#setValue is an array lookup. Here each block gets one
 * shared {@link StateTable} and each state keeps only its index in it.
 *
 * <p>Vanilla builds its arrays as usual. Once a StateDefinition has made all of a block's states
 * (mixin/memory/StateDefinitionMixin), the table is built and checked against vanilla's arrays, every value of every
 * property of every state; only when every entry matches are the states switched to the table and vanilla's arrays
 * dropped (left to the garbage collector). Anything unexpected (a hook that didn't apply, a field that isn't where it
 * was, a single mismatch) keeps vanilla's arrays and turns the optimisation off with one fallback line. setValue is
 * hooked at the head of StateHolder#setValueInternal (mixin/memory/StateHolderMixin): a state with a table answers from
 * it, any other runs vanilla's code. Everything else about states (their values, codecs, ids) is untouched.
 */
public final class StateTables {
    public static final String ID = "state-neighbours";

    /** What a state carries (StateHolderMixin). */
    public interface Holder {
        void quicksilver$setTable(StateTable table, int index);

        StateTable quicksilver$table();
    }

    private static Field neighbours, keys;
    private static boolean checked = false;
    static long blocks = 0, states = 0, singletons = 0;

    private StateTables() {
    }

    /** Vanilla's neighbour arrays (the one S[][] field of StateHolder) and its properties (the one Property[]). */
    private static void findFields() {
        if (checked) return;
        checked = true;
        List<Field> nb = new ArrayList<>(), ks = new ArrayList<>();
        for (Field f : StateHolder.class.getDeclaredFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            // S[][], erased to the bound of S (StateHolder[][] in 26.3)
            if (f.getType().isArray() && f.getType().getComponentType().isArray()
                && !f.getType().getComponentType().getComponentType().isPrimitive()) nb.add(f);
            if (f.getType() == Property[].class) ks.add(f);
        }
        if (nb.size() != 1 || ks.size() != 1) {
            throw new IllegalStateException("StateHolder has " + nb.size() + " neighbour-array fields and " + ks.size()
                + " property-array fields (one of each expected)");
        }
        neighbours = nb.get(0);
        keys = ks.get(0);
        neighbours.setAccessible(true);
        keys.setAccessible(true);
    }

    /** A StateDefinition has made these states (all of one block or fluid): build, check and switch to the table. */
    public static synchronized void built(Collection<?> all) {
        if (!Memory.on(ID)) return;
        if (all.size() <= 1) {
            singletons++;
            return;
        }
        List<StateHolder<?, ?>> list = new ArrayList<>(all.size());
        for (Object o : all) list.add((StateHolder<?, ?>) o);
        int dropped = 0;
        try {
            if (!MemoryMixins.applied(MemoryMixins.STATE_HOLDER, "quicksilver$tableLookup")) {
                Memory.fallback(ID, "the setValue hook (StateHolder#setValueInternal) did not apply", null);
                return;
            }
            findFields();
            Property<?>[] props = (Property<?>[]) keys.get(list.get(0));
            StateTable table = new StateTable(props);
            int[] index = new int[list.size()];
            for (int s = 0; s < list.size(); s++) {
                StateHolder<?, ?> state = list.get(s);
                if (!java.util.Arrays.equals((Property<?>[]) keys.get(state), props)) throw new IllegalStateException("states of " + table + " differ in properties");
                int[] values = new int[props.length];
                for (int p = 0; p < props.length; p++) values[p] = valueIndex(props[p], value(state, props[p]));
                index[s] = table.indexOf(values);
                table.put(index[s], state);
            }
            // every neighbour vanilla stores must be what the table gives
            for (int s = 0; s < list.size(); s++) {
                Object[][] nb = (Object[][]) neighbours.get(list.get(s));
                if (nb == null || nb.length != props.length) {
                    throw new IllegalStateException("vanilla's neighbour arrays of " + list.get(s) + " are " + (nb == null ? "missing" : nb.length + " long"));
                }
                for (int p = 0; p < props.length; p++) {
                    int n = props[p].getPossibleValues().size();
                    if (nb[p] == null || nb[p].length != n) throw new IllegalStateException("neighbours of " + list.get(s) + " for " + props[p] + " have the wrong length");
                    for (int v = 0; v < n; v++) {
                        if (nb[p][v] != table.with(index[s], p, v)) {
                            throw new IllegalStateException("the table differs from vanilla at " + list.get(s) + ", " + props[p] + " value " + v);
                        }
                    }
                }
            }
            for (int s = 0; s < list.size(); s++) ((Holder) list.get(s)).quicksilver$setTable(table, index[s]);
            for (StateHolder<?, ?> state : list) {
                neighbours.set(state, null);
                dropped++;
            }
            blocks++;
            states += list.size();
        } catch (Throwable t) {
            // before any array was dropped, every state goes back to vanilla's arrays; after, the checked tables stay
            if (dropped == 0) {
                for (StateHolder<?, ?> state : list) {
                    if (state instanceof Holder h && h.quicksilver$table() != null) h.quicksilver$setTable(null, 0);
                }
            }
            Memory.fallback(ID, "could not build or check the table for " + list.get(0), t);
        }
    }

    /** For the status line: how many blocks and states use tables. */
    static String counts() {
        return blocks + " blocks, " + states + " states (" + singletons + " with one state, unchanged)";
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static int valueIndex(Property property, Comparable value) {
        return property.getInternalIndex(value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Comparable<?> value(StateHolder state, Property property) {
        return state.getValue(property);
    }
}
