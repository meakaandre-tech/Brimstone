package com.meakaandre.quicksilver.memory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/**
 * Mixin plugin for the Memory module's config (quicksilver.memory.mixins.json: not required, require 0, so a hook that
 * no longer fits the game is skipped instead of crashing it). An optimisation that is switched off has none of its
 * mixins applied ({@link #shouldApplyMixin}); each hook that lands is recorded ({@link #applied}), and an
 * optimisation checks its hooks before it changes anything ({@link Memory#fallback} otherwise).
 */
public final class MemoryMixins implements IMixinConfigPlugin {
    public static final String STATE_HOLDER = "net.minecraft.world.level.block.state.StateHolder";

    /** Each mixin class (simple name) and the optimisation it belongs to. */
    private static final Map<String, String> OWNER = Map.of(
        "StateHolderMixin", StateTables.ID,
        "StateDefinitionMixin", StateTables.ID);

    private static final Map<String, Set<String>> APPLIED = new ConcurrentHashMap<>();

    /** Whether a hook (a handler method of this config's mixins) landed in its target class. */
    public static boolean applied(String target, String handler) {
        try {
            Class.forName(target, false, MemoryMixins.class.getClassLoader());
        } catch (Throwable t) {
            return false;
        }
        return APPLIED.getOrDefault(target, Set.of()).contains(handler);
    }

    @Override
    public void onLoad(String mixinPackage) {
        Memory.load();
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String id = OWNER.get(mixinClassName.substring(mixinClassName.lastIndexOf('.') + 1));
        return id != null && Memory.on(id);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        Set<String> found = APPLIED.computeIfAbsent(targetClassName, k -> ConcurrentHashMap.newKeySet());
        for (MethodNode m : targetClass.methods) {
            for (AbstractInsnNode insn = m.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                if (insn instanceof MethodInsnNode call && call.owner.equals(targetClass.name) && call.name.contains("quicksilver$")) {
                    found.add(call.name.substring(call.name.indexOf("quicksilver$")));
                }
            }
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
