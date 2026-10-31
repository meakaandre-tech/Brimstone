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
                new String[] {"net.minecraft.world.level.chunk.PalettedContainer", "call:quicksilver$swapped"},
                new String[] {"net.minecraft.world.level.chunk.PalettedContainer", "call:quicksilver$written"})),
        new Opt(EntitySections.ID, "entity sections near a small box looked up directly, in vanilla's order (Lithium's fast_retrieval)",
            List.of("EntitySectionStorageMixin"),
            List.<String[]>of(new String[] {"net.minecraft.world.level.entity.EntitySectionStorage", "call:quicksilver$sections"})),
        new Opt(EntityCollisions.ID, "lookups for entities to collide with like blocks answered from per-section counts when there are none",
            List.of("EntityGetterMixin", "EntitySectionMixin", "EntityTypeMixin", "ServerLevelEntitiesAccessor", "PersistentEntitySectionManagerAccessor"),
            List.<String[]>of(new String[] {"net.minecraft.world.level.EntityGetter", "call:quicksilver$noHardEntities"},
                new String[] {"net.minecraft.world.level.entity.EntitySection", "call:quicksilver$added"},
                new String[] {"net.minecraft.world.level.entity.EntitySection", "call:quicksilver$removed"},
                new String[] {"net.minecraft.world.entity.EntityType", "quicksilver$kind"},
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

    /** At server start: an optimisation that is on but whose hooks didn't land falls back (CI fails on it). */
    static void checkHooks() {
        for (Opt o : ALL) {
            Logic.Switch s = switchOf(o.id());
            if (s == null || !s.on) continue;
            for (String[] h : o.hooks()) {
                try {
                    // loading the class applies its mixins (and records the handlers that landed)
                    Class.forName(h[0], false, Optimisations.class.getClassLoader());
                } catch (Throwable t) {
                    Logic.fallback(s, "class " + h[0] + " not found", t);
                    continue;
                }
                // "call:" an injection that must be called from the target's own code; otherwise a member it must have
                boolean ok = h[1].startsWith("call:") ? LogicMixins.called(h[0], h[1].substring(5)) : LogicMixins.member(h[0], h[1]);
                if (!ok) Logic.fallback(s, "hook " + h[1] + " did not land in " + h[0], null);
            }
        }
    }

    private static Logic.Switch switchOf(String id) {
        if (id.equals(SectionMasks.ID)) return SectionMasks.SWITCH;
        if (id.equals(EntitySections.ID)) return EntitySections.SWITCH;
        if (id.equals(EntityCollisions.ID)) return EntityCollisions.SWITCH;
        return null;
    }
}
