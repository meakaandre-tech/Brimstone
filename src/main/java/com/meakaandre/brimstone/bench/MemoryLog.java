package com.meakaandre.brimstone.bench;

import com.meakaandre.brimstone.terrain.TerrainStore;
import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryUtil;

/**
 * DIAGNOSTIC (branch only, removed before merge). With -Dbrimstone.debug.memoryLog=SECONDS, logs one line every that
 * many seconds on the render thread: java.nio direct buffers, heap, Brimstone's terrain store, and, when LWJGL's
 * DebugAllocator is on, the live LWJGL allocations made from Brimstone's own code (memAlloc'd vertices and index
 * lists) against all tracked ones. Used once to check the hologram scene for a native memory leak over a long run.
 */
public final class MemoryLog {
    private static final long PERIOD = Long.getLong("brimstone.debug.memoryLog", 0L) * 1_000_000_000L;
    private static long next;
    private static long start;

    private MemoryLog() {
    }

    static void onPresent() {
        if (PERIOD <= 0) return;
        long now = System.nanoTime();
        if (start == 0) start = now;
        if (now < next) return;
        next = now + PERIOD;
        long directBytes = 0, directCount = 0;
        for (BufferPoolMXBean b : ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class)) {
            if ("direct".equals(b.getName())) {
                directBytes = b.getMemoryUsed();
                directCount = b.getCount();
            }
        }
        Runtime rt = Runtime.getRuntime();
        StringBuilder line = new StringBuilder("BRIMSTONE MEMLOG t=").append((now - start) / 1_000_000_000L)
            .append(" directMB=").append(round(directBytes)).append(" directCount=").append(directCount)
            .append(" heapUsedMB=").append(round(rt.totalMemory() - rt.freeMemory()));
        TerrainStore.Snapshot s = TerrainStore.snapshot();
        line.append(" arenas=").append(s.arenas()).append(" arenaUsedMB=").append(round(s.usedBytes()))
            .append(" regions=").append(s.regions()).append(' ').append(TerrainStore.diagSizes());
        if (Boolean.TRUE.equals(Configuration.DEBUG_MEMORY_ALLOCATOR.get(false))) {
            long[] t = new long[4];
            MemoryUtil.memReport((address, memory, threadId, threadName, stack) -> {
                t[0] += memory;
                t[1]++;
                if (stack != null) {
                    for (StackTraceElement e : stack) {
                        if (e.getClassName().startsWith("com.meakaandre.brimstone")) {
                            t[2] += memory;
                            t[3]++;
                            break;
                        }
                    }
                }
            });
            line.append(" lwjglLiveMB=").append(round(t[0])).append(" lwjglLiveCount=").append(t[1])
                .append(" brimstoneLiveBytes=").append(t[2]).append(" brimstoneLiveCount=").append(t[3]);
        }
        com.meakaandre.brimstone.BrimstoneClient.LOG.info(line.toString());
    }

    private static double round(long bytes) {
        return Math.round(bytes / 10485.76) / 100.0;
    }
}
