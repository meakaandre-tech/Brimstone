package com.meakaandre.quicksilver.logic;

import java.util.List;
import java.util.Map;

/**
 * The Logic module's optimisations: their ids and descriptions (the status line and the config file), which mixins
 * belong to each ({@link LogicMixins}), and the hooks each must have landed (checked when a server starts).
 */
final class Optimisations {
    private Optimisations() {
    }

    /** One optimisation: id, what it does, its mixins (simple names), and its hooks (target class, handler name). */
    private record Opt(String id, String what, List<String> mixins, List<String[]> hooks) {
    }

    private static final List<Opt> ALL = List.of(
        new Opt(SectionMasks.ID, "block collision sweeps skip, unread, positions that hold air and shell positions without an oversized shape (per-section masks)",
            List.of("BlockCollisionsMixin", "LevelChunkSectionMixin", "PalettedContainerMixin"),
            List.<String[]>of(new String[] {"net.minecraft.world.level.BlockCollisions", "call:quicksilver$skipEdges"},
                new String[] {"net.minecraft.world.level.chunk.LevelChunkSection", "quicksilver$mask"},
                new String[] {"net.minecraft.world.level.chunk.LevelChunkSection", "call:quicksilver$beforeSet"},
                new String[] {"net.minecraft.world.level.chunk.LevelChunkSection", "call:quicksilver$afterSet"},
                new String[] {"net.minecraft.world.level.chunk.PalettedContainer", "call:quicksilver$getAndSet"},
                new String[] {"net.minecraft.world.level.chunk.PalettedContainer", "call:quicksilver$getAndSetUnchecked"},
                new String[] {"net.minecraft.world.level.chunk.PalettedContainer", "call:quicksilver$set"},
                new String[] {"net.minecraft.world.level.chunk.PalettedContainer", "call:quicksilver$read"})),
        new Opt(EntitySections.ID, "entity sections near a small box looked up directly, in vanilla's order (Lithium's fast_retrieval)",
            List.of("EntitySectionStorageMixin"),
            List.<String[]>of(new String[] {"net.minecraft.world.level.entity.EntitySectionStorage", "call:quicksilver$sections"})),
        new Opt(EntityCollisions.ID, "lookups for entities to collide with like blocks answered from per-section counts when there are none",
            List.of("EntityGetterMixin", "EntitySectionMixin", "EntityTypeMixin", "ServerLevelEntitiesAccessor", "PersistentEntitySectionManagerAccessor"),
            List.<String[]>of(new String[] {"net.minecraft.world.level.EntityGetter", "call:quicksilver$noHardEntities"},
                new String[] {"net.minecraft.world.level.entity.EntitySection", "call:quicksilver$added"},
                new String[] {"net.minecraft.world.level.entity.EntitySection", "call:quicksilver$removed"},
                new String[] {"net.minecraft.world.entity.EntityType", "quicksilver$memo"},
                new String[] {"net.minecraft.server.level.ServerLevel", "quicksilver$entityManager"},
                new String[] {"net.minecraft.world.level.entity.PersistentEntitySectionManager", "quicksilver$sectionStorage"})));

    /** Each optimisation, in the order the status line lists them. */
    static void register(Map<String, String> m) {
        for (Opt o : ALL) m.put(o.id(), o.what());
    }

    /** Each mixin class (simple name) and the optimisation it belongs to. */
    static String owner(String mixinSimpleName) {
        for (Opt o : ALL) if (o.mixins().contains(mixinSimpleName)) return o.id();
        return null;
    }

    /** Hooks already found in place (target and name), so each is checked once. */
    private static final java.util.Set<String> CHECKED = java.util.concurrent.ConcurrentHashMap.newKeySet();

    /**
     * An optimisation that is on but whose hooks didn't land falls back (CI fails on it). Only classes the game has
     * already loaded (their mixins applied) are checked: nothing is loaded or looked up by reflection here, so this
     * can run on the server thread (at start and every few seconds, until every hook has been seen).
     */
    static void checkHooks() {
        for (Opt o : ALL) {
            Logic.Switch s = switchOf(o.id());
            if (s == null || !s.on) continue;
            for (String[] h : o.hooks()) {
                String key = h[0] + "#" + h[1];
                if (CHECKED.contains(key) || !LogicMixins.transformed(h[0])) continue;
                // "call:" an injection that must be called from the target's own code; otherwise a member it must have
                boolean ok = h[1].startsWith("call:") ? LogicMixins.called(h[0], h[1].substring(5)) : LogicMixins.member(h[0], h[1]);
                CHECKED.add(key);
                if (!ok) Logic.fallback(s, "hook " + h[1] + " did not land in " + h[0], null);
            }
        }
    }

    /** Whether every hook of every optimisation that is still on has been checked. */
    static boolean allChecked() {
        for (Opt o : ALL) {
            Logic.Switch s = switchOf(o.id());
            if (s == null || !s.on) continue;
            for (String[] h : o.hooks()) if (!CHECKED.contains(h[0] + "#" + h[1])) return false;
        }
        return true;
    }

    private static Logic.Switch switchOf(String id) {
        if (id.equals(SectionMasks.ID)) return SectionMasks.SWITCH;
        if (id.equals(EntitySections.ID)) return EntitySections.SWITCH;
        if (id.equals(EntityCollisions.ID)) return EntityCollisions.SWITCH;
        return null;
    }
}
