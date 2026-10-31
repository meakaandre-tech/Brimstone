package com.meakaandre.quicksilver.mesher;

import org.lwjgl.system.MemoryUtil;

/** A growable native buffer, kept and reused by one meshing thread (memAlloc'd; freed when its thread has ended, see {@code Mesher.sweep}). */
final class NativeBuffer {
    long address;
    int capacity;
    int size;

    NativeBuffer(int initial) {
        capacity = initial;
        address = MemoryUtil.nmemAlloc(initial);
        if (address == 0) throw new OutOfMemoryError("Quicksilver mesher: could not allocate " + initial + " bytes");
    }

    void clear() {
        size = 0;
    }

    /** Address of {@code bytes} more bytes at the end (the buffer grows if needed; earlier addresses may move). */
    long reserve(int bytes) {
        int need = size + bytes;
        if (need > capacity) grow(need);
        long at = address + size;
        size = need;
        return at;
    }

    private void grow(int need) {
        int cap = capacity;
        while (cap < need) cap = cap > (1 << 29) ? Integer.MAX_VALUE - 64 : cap * 2;
        long a = MemoryUtil.nmemRealloc(address, cap);
        if (a == 0) throw new OutOfMemoryError("Quicksilver mesher: could not grow a buffer to " + cap + " bytes");
        address = a;
        capacity = cap;
    }

    void free() {
        if (address != 0) MemoryUtil.nmemFree(address);
        address = 0;
        capacity = 0;
        size = 0;
    }
}
