package com.meakaandre.quicksilver.mixin.memory;

import com.meakaandre.quicksilver.memory.StateTable;
import com.meakaandre.quicksilver.memory.StateTables;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** P2-1, memory.state-neighbours: a state with a shared table answers setValue from it ({@link StateTables}). */
@Mixin(StateHolder.class)
public abstract class StateHolderMixin implements StateTables.Holder {
    @Unique
    private StateTable quicksilver$table;
    @Unique
    private int quicksilver$index;

    @Override
    public void quicksilver$setTable(StateTable table, int index) {
        quicksilver$table = table;
        quicksilver$index = index;
    }

    @Override
    public StateTable quicksilver$table() {
        return quicksilver$table;
    }

    @Inject(method = "setValueInternal", at = @At("HEAD"), cancellable = true)
    private void quicksilver$tableLookup(Property<?> property, int propertyIndex, Comparable<?> value, CallbackInfoReturnable<Object> cir) {
        StateTable table = quicksilver$table;
        if (table == null) return;
        int v = StateTables.valueIndex(property, value);
        if (v < 0) return;  // not an allowed value: vanilla's own error, thrown before it reads any array
        Object state = table.with(quicksilver$index, propertyIndex, v);
        if (state == null) throw new IllegalStateException("Quicksilver state table: no state for " + property + "=" + value + " in " + table);
        cir.setReturnValue(state);
    }
}
