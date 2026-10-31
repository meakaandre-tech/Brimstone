/*
 * Quicksilver: P2-2, logic.entity-sections. Adapted from Lithium (https://github.com/CaffeineMC/lithium, branch
 * develop at 383d299, common/src/main/java/net/caffeinemc/mods/lithium/mixin/entity/fast_retrieval/
 * EntitySectionStorageMixin.java), Copyright (C) CaffeineMC and Lithium's contributors, licensed under the GNU Lesser
 * General Public License v3.0 only (LICENSE, COPYING). Changed for Quicksilver: its own switch and fallback; a section is
 * visited when it is not empty (vanilla's own test; Lithium tests its own size counter).
 */
package com.meakaandre.quicksilver.mixin.logic;

import com.llamalad7.mixinextras.sugar.Local;
import com.meakaandre.quicksilver.logic.EntitySections;
import net.minecraft.core.SectionPos;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.util.Continuation;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Vanilla finds the entity sections touching a box by walking, for each section column x in the box, every loaded
 * section id with that x in its sorted set (hundreds of ids across the loaded area) and keeping those inside the box.
 * For a box at most four sections wide this looks each section of the box up directly instead, in the order vanilla's
 * sorted set gives them: x ascending; then z, then y, each as an unsigned number (non-negative values ascending, then
 * negative ones ascending), which is how they sit in the packed id vanilla sorts by.
 */
@Mixin(EntitySectionStorage.class)
public abstract class EntitySectionStorageMixin<T extends EntityAccess> {
    @Shadow
    public abstract EntitySection<T> getSection(long sectionPos);

    @Inject(method = "forEachAccessibleNonEmptySection", cancellable = true,
        at = @At(value = "INVOKE_ASSIGN", shift = At.Shift.AFTER, target = "Lnet/minecraft/core/SectionPos;posToSectionCoord(D)I", ordinal = 5))
    private void quicksilver$sections(AABB box, AbortableIterationConsumer<EntitySection<T>> action, CallbackInfo ci,
                                      @Local(ordinal = 0) int minX, @Local(ordinal = 1) int minY, @Local(ordinal = 2) int minZ,
                                      @Local(ordinal = 3) int maxX, @Local(ordinal = 4) int maxY, @Local(ordinal = 5) int maxZ) {
        if (!EntitySections.SWITCH.on) return;
        // wide boxes: vanilla's walk is cheaper than looking up many sections that mostly don't exist
        if (maxX >= minX + 4 || maxZ >= minZ + 4) return;
        ci.cancel();
        for (int x = minX; x <= maxX; x++) {
            for (int z = Math.max(minZ, 0); z <= maxZ; z++) {
                if (this.quicksilver$column(x, minY, maxY, z, action).shouldAbort()) return;
            }
            int bound = Math.min(-1, maxZ);
            for (int z = minZ; z <= bound; z++) {
                if (this.quicksilver$column(x, minY, maxY, z, action).shouldAbort()) return;
            }
        }
    }

    @Unique
    private Continuation quicksilver$column(int x, int minY, int maxY, int z, AbortableIterationConsumer<EntitySection<T>> action) {
        Continuation r = Continuation.CONTINUE;
        for (int y = Math.max(minY, 0); y <= maxY; y++) {
            if ((r = this.quicksilver$visit(SectionPos.asLong(x, y, z), action)).shouldAbort()) return r;
        }
        int bound = Math.min(-1, maxY);
        for (int y = minY; y <= bound; y++) {
            if ((r = this.quicksilver$visit(SectionPos.asLong(x, y, z), action)).shouldAbort()) return r;
        }
        return r;
    }

    @Unique
    private Continuation quicksilver$visit(long pos, AbortableIterationConsumer<EntitySection<T>> action) {
        EntitySection<T> section = this.getSection(pos);
        if (section != null && !section.isEmpty() && section.getStatus().isAccessible()) return action.accept(section);
        return Continuation.CONTINUE;
    }
}
