package com.meakaandre.brimstone.visibility;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/**
 * Mixin plugin for the visibility module's mixins (their own config: not required, require 0, so a hook that no longer
 * fits the game is skipped instead of crashing it). It records which change-tracking hooks landed; the module only
 * turns on when every one did ({@link #loadTargetsAndCheck}), since without them it could trust a stale mesh. The
 * {@code @WrapOperation} hooks are applied by MixinExtras later; if they don't land, nothing is ever hidden.
 */
public final class VisibilityMixins implements IMixinConfigPlugin {
    static final Map<String, List<String>> EXPECTED = new LinkedHashMap<>();

    static {
        EXPECTED.put("net.minecraft.client.renderer.extract.LevelExtractor",
            List.of("brimstone$entitiesStart", "brimstone$blockDirty", "brimstone$allChanged", "brimstone$setLevel",
                "brimstone$reloaded"));
        EXPECTED.put("net.minecraft.client.SectionUpdateTracker", List.of("brimstone$dirty"));
        EXPECTED.put("net.minecraft.client.multiplayer.ClientChunkCache", List.of("brimstone$replaced", "brimstone$dropped"));
    }

    private static final Map<String, Set<String>> APPLIED = new ConcurrentHashMap<>();

    static String loadTargetsAndCheck() {
        for (String target : EXPECTED.keySet()) {
            try {
                Class.forName(target, false, VisibilityMixins.class.getClassLoader());
            } catch (Throwable t) {
                return "could not load " + target + ": " + t;
            }
        }
        Set<String> missing = new TreeSet<>();
        for (var e : EXPECTED.entrySet()) {
            Set<String> got = APPLIED.getOrDefault(e.getKey(), Set.of());
            for (String h : e.getValue()) if (!got.contains(h)) missing.add(e.getKey().substring(e.getKey().lastIndexOf('.') + 1) + "#" + h);
        }
        return missing.isEmpty() ? null : "hooks that did not apply: " + String.join(", ", missing);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        List<String> expected = EXPECTED.get(targetClassName);
        if (expected == null) return;
        Set<String> found = APPLIED.computeIfAbsent(targetClassName, k -> ConcurrentHashMap.newKeySet());
        for (MethodNode m : targetClass.methods) {
            for (AbstractInsnNode insn = m.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                if (insn instanceof MethodInsnNode call && call.owner.equals(targetClass.name)) {
                    for (String h : expected) if (call.name.endsWith(h)) found.add(h);
                }
            }
        }
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
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
