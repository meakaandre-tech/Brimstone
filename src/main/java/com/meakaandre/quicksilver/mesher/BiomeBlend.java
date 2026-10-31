package com.meakaandre.quicksilver.mesher;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

/**
 * Biome lookups of one section's tint blending, made once per position (design doc, package 4c: lighting and tint
 * caches, "biome blend once per column"). Vanilla blends a tinted block's colour over the columns around it, looking up
 * the biome of each; neighbouring blocks share almost all of those positions, and the lookup (the biome zoom and the
 * chunk's biome palette) is most of the blend's cost. While Quicksilver's mesher meshes a section on a thread, vanilla's
 * own blend on that thread ({@code ClientLevel.calculateBlockTint}, reached through the level's tint cache as before)
 * gets each position's biome from here: looked up from the level the first time, then kept until the section is done.
 * Vanilla's blend itself is untouched (its positions, colours, sums and its tint cache), so the tints are vanilla's.
 */
public final class BiomeBlend {
    private static final ThreadLocal<BiomeBlend> ACTIVE = new ThreadLocal<>();
    static final AtomicLong LOOKUPS = new AtomicLong(), KEPT = new AtomicLong(), CHECKED = new AtomicLong(), CHANGED = new AtomicLong();
    /** Set once vanilla's blend has asked for a biome through Quicksilver's hook (see {@code ClientLevelTintMixin}). */
    static volatile boolean hookSeen;

    private final Long2ObjectOpenHashMap<Holder<Biome>> biomes = new Long2ObjectOpenHashMap<>(4096);
    private long lookups, kept;
    /** The thread's blend before this one (a fluid probe meshes inside a section): put back at the end. */
    private BiomeBlend previous;

    /** This thread meshes a section from now on (Quicksilver's mesher). */
    void begin() {
        biomes.clear();
        lookups = kept = 0;
        BiomeBlend outer = ACTIVE.get();
        previous = outer == this ? null : outer;
        ACTIVE.set(this);
    }

    /** The section is done: vanilla's blend on this thread looks biomes up itself again. */
    void end() {
        if (previous != null) ACTIVE.set(previous);
        else ACTIVE.remove();
        previous = null;
        biomes.clear();
        LOOKUPS.addAndGet(lookups);
        KEPT.addAndGet(kept);
    }

    /**
     * Vanilla's blend asks for the biome at {@code pos}: kept from earlier in this section, or looked up through
     * {@code lookup} (vanilla's own call, on {@code self}) and kept. Outside Quicksilver's meshing, always vanilla's call.
     */
    public static Holder<Biome> biome(Object self, BlockPos pos, Operation<Holder<Biome>> lookup) {
        hookSeen = true;
        BiomeBlend b = ACTIVE.get();
        if (b == null) return lookup.call(self, pos);
        long k = pos.asLong();
        Holder<Biome> h = b.biomes.get(k);
        if (h != null) {
            b.kept++;
            // CI: the mesh check can't see a wrong kept biome (both meshers read tints through the level's tint cache),
            // so a kept biome is also compared with a fresh lookup now and then
            if (Mesher.VERIFY && (b.kept & 63) == 0) {
                Holder<Biome> fresh = lookup.call(self, pos);
                CHECKED.incrementAndGet();
                if (!java.util.Objects.equals(fresh, h)) {
                    if (unloadedNear(self, pos)) {
                        // the chunk went away while the section was meshed (its biomes read as the default now), as
                        // vanilla's own lookups would see at that moment: the world changed, not the kept biome
                        CHANGED.incrementAndGet();
                    } else {
                        MesherStats.MISMATCHED.incrementAndGet();
                        MesherCheck.warn("a kept biome " + h + " where the level now gives " + fresh + " at " + pos.toShortString());
                    }
                }
            }
            return h;
        }
        h = lookup.call(self, pos);
        b.lookups++;
        if (h != null) b.biomes.put(k, h);
        return h;
    }

    /**
     * Whether a chunk the biome lookup at {@code pos} may read (its zoom reaches a few blocks around) is not there now: on
     * the client an unloaded chunk reads as an empty chunk (whose biome is the default), so both ways are tested.
     */
    private static boolean unloadedNear(Object self, BlockPos pos) {
        if (!(self instanceof net.minecraft.world.level.LevelReader level)) return false;
        for (int dx = -8; dx <= 8; dx += 8) {
            for (int dz = -8; dz <= 8; dz += 8) {
                int cx = (pos.getX() + dx) >> 4, cz = (pos.getZ() + dz) >> 4;
                if (!level.hasChunk(cx, cz)) return true;
                if (level.getChunk(cx, cz) instanceof net.minecraft.world.level.chunk.EmptyLevelChunk) return true;
            }
        }
        return false;
    }

    static String state() {
        return hookSeen ? "vanilla's blend, biomes kept per section" : "vanilla's (the hook into its blend not seen)";
    }
}
