/*
 * Adapted from FerriteCore (https://github.com/malte0811/FerriteCore, branch 26.1, build 9.0.0):
 * fastmap/FastMap.java, fastmap/FastMapKey.java and fastmap/BinaryFastMapKey.java.
 *
 * MIT License
 *
 * Copyright (c) 2020 malte0811
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Changes for Quicksilver (P2-1): the three classes merged into one; only the binary (bit-mask) key, with its keys as
 * int arrays and the value matrix as a plain array; a state's index is read from its property values once, when the
 * table is built, and the table is checked against vanilla's own neighbour arrays before they are dropped
 * (StateTables).
 */
package com.meakaandre.quicksilver.memory;

import java.util.Arrays;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * One block's (or fluid's) states in a matrix indexed by their property values, bit-packed: property i's value index
 * sits in bits [first[i], after[i]) of a state's index. Replacing one property's value is a mask and an or, so
 * BlockState#setValue needs this one table per block instead of every state's own neighbour arrays.
 */
public final class StateTable {
    final Property<?>[] properties;
    private final int[] numValues, first, after;
    private final Object[] states;

    StateTable(Property<?>[] properties) {
        this.properties = properties;
        int n = properties.length;
        numValues = new int[n];
        first = new int[n];
        after = new int[n];
        int factor = 1;
        for (int i = 0; i < n; i++) {
            int values = properties[i].getPossibleValues().size();
            int added = Mth.smallestEncompassingPowerOfTwo(values);
            int bitsBefore = Mth.log2(factor), bitsAdded = Mth.log2(added);
            if (bitsBefore + bitsAdded > 30) throw new IllegalStateException("too many states for a 30-bit index");
            numValues[i] = values;
            first[i] = bitsBefore;
            after[i] = bitsBefore + bitsAdded;
            factor *= added;
        }
        states = new Object[factor];
    }

    /** The index a state with these value indices has (each in the order of {@link #properties}). */
    int indexOf(int[] valueIndices) {
        int id = 0;
        for (int i = 0; i < valueIndices.length; i++) id |= valueIndices[i] << first[i];
        return id;
    }

    void put(int index, Object state) {
        if (states[index] != null) throw new IllegalStateException("two states at index " + index);
        states[index] = state;
    }

    /** The state with property {@code propertyIndex} set to value {@code valueIndex}, from the state at {@code index}. */
    public Object with(int index, int propertyIndex, int valueIndex) {
        if (valueIndex >= numValues[propertyIndex] || valueIndex < 0) return null;
        int keep = ~lowBits(after[propertyIndex]) | lowBits(first[propertyIndex]);
        Object s = states[(keep & index) | valueIndex << first[propertyIndex]];
        return s;
    }

    private static int lowBits(int n) {
        return n >= Integer.SIZE ? -1 : (1 << n) - 1;
    }

    @Override
    public String toString() {
        return "StateTable" + Arrays.toString(properties);
    }
}
