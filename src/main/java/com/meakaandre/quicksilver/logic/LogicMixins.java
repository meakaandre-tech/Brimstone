package com.meakaandre.quicksilver.logic;

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
 * Mixin plugin for the Logic module's config (quicksilver.logic.mixins.json: not required, require 0, so a hook that no
 * longer fits the game is skipped instead of crashing it). An optimisation that is switched off has none of its mixins
 * applied ({@link #shouldApplyMixin}); the handlers that land are recorded ({@link #applied}), and each optimisation
 * checks its hooks at server start ({@link Optimisations#checkHooks}).
 */
public final class LogicMixins implements IMixinConfigPlugin {
    /** Per target class: the handlers some other method of the merged class calls (an injection that landed). */
    private static final Map<String, Set<String>> CALLED = new ConcurrentHashMap<>();
    /** Per target class: the quicksilver$ members it has (accessors, added interface methods). */
    private static final Map<String, Set<String>> MEMBERS = new ConcurrentHashMap<>();

    /** Whether an injection handler (its name ends with {@code quicksilver$<name>}) is called in the target class. */
    public static boolean called(String target, String handler) {
        return CALLED.getOrDefault(target, Set.of()).contains(handler);
    }

    /** Whether the target class has a member named (or ending with) {@code quicksilver$<name>}. */
    public static boolean member(String target, String name) {
        return MEMBERS.getOrDefault(target, Set.of()).contains(name);
    }

    @Override
    public void onLoad(String mixinPackage) {
        Logic.load();
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String id = Optimisations.owner(mixinClassName.substring(mixinClassName.lastIndexOf('.') + 1));
        return id != null && Logic.on(id);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // the quicksilver$ members of the merged class, and the ones another of its methods calls (a landed injection)
        Set<String> members = MEMBERS.computeIfAbsent(targetClassName, k -> ConcurrentHashMap.newKeySet());
        Set<String> called = CALLED.computeIfAbsent(targetClassName, k -> ConcurrentHashMap.newKeySet());
        for (MethodNode m : targetClass.methods) {
            int i = m.name.indexOf("quicksilver$");
            if (i >= 0) members.add(m.name.substring(i));
            if (i >= 0) continue;
            for (AbstractInsnNode insn = m.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                if (insn instanceof MethodInsnNode call && call.owner.equals(targetClass.name)) {
                    int j = call.name.indexOf("quicksilver$");
                    if (j >= 0) called.add(call.name.substring(j));
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
        if (Boolean.getBoolean("quicksilver.debug.logicShape") && targetClassName.endsWith(".BlockCollisions")) {
            // names only (method and field names and the methods each calls), to find where a hook belongs
            StringBuilder b = new StringBuilder();
            for (org.objectweb.asm.tree.FieldNode f : targetClass.fields) b.append(" field ").append(f.name).append(' ').append(f.desc).append(';');
            for (MethodNode m : targetClass.methods) {
                java.util.TreeSet<String> calls = new java.util.TreeSet<>();
                for (AbstractInsnNode insn = m.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                    if (insn instanceof MethodInsnNode c) calls.add(c.owner.substring(c.owner.lastIndexOf('/') + 1) + "." + c.name);
                }
                b.append(" method ").append(m.name).append(m.desc).append(" calls ").append(calls).append(';');
            }
            Logic.LOG.info("Quicksilver logic shape {}:{}", targetClassName, b);
        }
    }
}
