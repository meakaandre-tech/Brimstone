package com.meakaandre.quicksilver.bench;

import com.google.gson.JsonObject;
import com.meakaandre.quicksilver.mixin.counters.SectionRenderDispatcherAccessor;
import com.meakaandre.quicksilver.mixin.counters.SectionUberBuffersAccessor;
import com.meakaandre.quicksilver.mixin.counters.UberGpuBufferAccessor;
import com.mojang.blaze3d.vertex.TlsfAllocator;
import com.mojang.blaze3d.vertex.UberGpuBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;

/**
 * GPU memory for terrain, for the bench (package 3): reserved (buffers made) and used (allocated in them), for vanilla's
 * own heaps (its section dispatcher's vertex and index buffers, one set per layer) and for Quicksilver's arenas. CI
 * compares the total reserved in a Quicksilver run with the total in vanilla's run of the same profile. Render thread.
 */
public final class TerrainGpuMemory {
    private TerrainGpuMemory() {
    }

    public static JsonObject json() {
        JsonObject o = new JsonObject();
        long vReserved = 0, vUsed = 0;
        String problem = null;
        SectionRenderDispatcher d = Minecraft.getInstance().levelRenderer.sectionRenderDispatcher();
        if (d != null) {
            d.lock();
            try {
                for (Object layer : ((SectionRenderDispatcherAccessor) d).quicksilver$chunkUberBuffers().values()) {
                    SectionUberBuffersAccessor b = (SectionUberBuffersAccessor) layer;
                    for (UberGpuBuffer<?> u : new UberGpuBuffer<?>[] {b.quicksilver$vertexBuffer(), b.quicksilver$indexBuffer()}) {
                        UberGpuBufferAccessor a = (UberGpuBufferAccessor) u;
                        vReserved += (long) a.quicksilver$nodes().size() * a.quicksilver$heapSize();
                        for (TlsfAllocator.Allocation al : a.quicksilver$allocationMap().values()) vUsed += al.getSize();
                    }
                }
            } catch (Throwable t) {
                problem = String.valueOf(t);
            } finally {
                d.unlock();
            }
        }
        java.util.function.Supplier<long[]> arenas = BenchModules.arenas;
        long[] a = arenas == null ? new long[2] : arenas.get();
        o.addProperty("vanillaHeapsReservedMB", problem == null ? round(vReserved / MB) : null);
        o.addProperty("vanillaHeapsUsedMB", problem == null ? round(vUsed / MB) : null);
        o.addProperty("quicksilverArenasReservedMB", round(a[0] / MB));
        o.addProperty("quicksilverArenasUsedMB", round(a[1] / MB));
        o.addProperty("reservedMB", problem == null ? round((vReserved + a[0]) / MB) : null);
        o.addProperty("usedMB", problem == null ? round((vUsed + a[1]) / MB) : null);
        if (problem != null) o.addProperty("problem", "vanilla's terrain heaps could not be read: " + problem);
        return o;
    }

    private static final double MB = 1024.0 * 1024.0;

    private static double round(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}
