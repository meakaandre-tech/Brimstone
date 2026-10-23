package com.meakaandre.brimstone.bench;

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
 * Mixin plugin for the counting mixins (their own config: not required, require 0, so a counter that no longer fits
 * the game is skipped instead of crashing it). After each target class is transformed it records which counting
 * handlers the class now calls, so the bench can list any counter that did not apply. Targets that never load (the
 * backend that is not in use) are listed as not loaded.
 */
public final class CounterAudit implements IMixinConfigPlugin {
    /** Every counting handler, by the target class it belongs in. */
    public static final Map<String, List<String>> EXPECTED = new LinkedHashMap<>();

    static {
        List<String> pass = List.of("brimstone$drawIndexed", "brimstone$draw", "brimstone$multiDrawIndexed", "brimstone$multiDrawIndexedSeparate",
            "brimstone$multiDraw", "brimstone$multiDrawSeparate", "brimstone$drawIndexedIndirect", "brimstone$drawIndirect");
        EXPECTED.put("com.mojang.renderpearl.frontend.FrontendRenderPass", pass);
        EXPECTED.put("com.mojang.renderpearl.frontend.FrontendCommandEncoder", List.of("brimstone$writeToBuffer", "brimstone$writeToTexture"));
        EXPECTED.put("com.mojang.renderpearl.frontend.FrontendGpuDevice", List.of("brimstone$createBuffer"));
        EXPECTED.put("com.mojang.renderpearl.frontend.FrontendGpuSurface", List.of("brimstone$present"));
        List<String> transientMemory = List.of("brimstone$allocateStaging", "brimstone$allocateGpuMapped", "brimstone$uploadStaging",
            "brimstone$uploadGpu", "brimstone$multiUploadStaging", "brimstone$multiUploadGpu");
        EXPECTED.put("com.mojang.renderpearl.backend.opengl.GlTransientMemory$PersistentMapping", transientMemory);
        EXPECTED.put("com.mojang.renderpearl.backend.opengl.GlTransientMemory$Fallback", transientMemory);
        EXPECTED.put("com.mojang.renderpearl.backend.vulkan.VulkanTransientMemory", transientMemory);
        EXPECTED.put("com.mojang.renderpearl.backend.opengl.GlBuffer$Direct", List.of("brimstone$map"));
        EXPECTED.put("com.mojang.renderpearl.backend.vulkan.VulkanGpuBuffer$Direct", List.of("brimstone$map"));
    }

    /** Handlers found in each transformed target. */
    private static final Map<String, Set<String>> APPLIED = new ConcurrentHashMap<>();

    /** "target#handler" for every expected handler missing from a target that has loaded. */
    public static Set<String> missing() {
        Set<String> out = new TreeSet<>();
        for (var e : EXPECTED.entrySet()) {
            Set<String> got = APPLIED.get(e.getKey());
            if (got == null) continue;
            for (String h : e.getValue()) if (!got.contains(h)) out.add(simple(e.getKey()) + "#" + h);
        }
        return out;
    }

    public static Set<String> applied() {
        Set<String> out = new TreeSet<>();
        APPLIED.forEach((t, hs) -> hs.forEach(h -> out.add(simple(t) + "#" + h)));
        return out;
    }

    public static Set<String> notLoaded() {
        Set<String> out = new TreeSet<>();
        for (String t : EXPECTED.keySet()) if (!APPLIED.containsKey(t)) out.add(simple(t));
        return out;
    }

    private static String simple(String cls) {
        return cls.substring(cls.lastIndexOf('.') + 1);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        List<String> expected = EXPECTED.get(targetClassName);
        if (expected == null) return;
        Set<String> found = APPLIED.computeIfAbsent(targetClassName, k -> ConcurrentHashMap.newKeySet());
        String owner = targetClass.name;
        for (MethodNode m : targetClass.methods) {
            for (AbstractInsnNode insn = m.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                if (insn instanceof MethodInsnNode call && call.owner.equals(owner)) {
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
