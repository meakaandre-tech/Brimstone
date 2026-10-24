package com.meakaandre.brimstone.terrain;

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
 * Mixin plugin for the terrain module's mixins (their own config: not required, require 0, so a hook that no longer
 * fits the game is skipped instead of crashing it). It records which hooks landed in each target; the module only
 * turns on when every one of them did ({@link #problem}), otherwise vanilla draws all terrain.
 */
public final class TerrainMixins implements IMixinConfigPlugin {
    static final Map<String, List<String>> EXPECTED = new LinkedHashMap<>();

    static {
        EXPECTED.put("net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection",
            List.of("brimstone$take", "brimstone$release"));
        EXPECTED.put("net.minecraft.client.renderer.chunk.SectionRenderDispatcher",
            List.of("brimstone$upload", "brimstone$created", "brimstone$dispose", "brimstone$disposeEnd"));
        EXPECTED.put("net.minecraft.client.renderer.ShaderManager", List.of("brimstone$reloaded"));
        EXPECTED.put("net.minecraft.client.renderer.chunk.SectionCompiler",
            List.of("brimstone$compileStart", "brimstone$compileEnd"));
        EXPECTED.put("net.minecraft.client.renderer.LevelRenderer",
            List.of("brimstone$preparePlain", "brimstone$prepareIndirect"));
        // The @WrapOperation hooks (LevelRenderer#brimstone$commands and #brimstone$instances,
        // ChunkSectionsToRender#brimstone$drawLayer) are applied by MixinExtras after this plugin's postApply, so
        // they are checked when they run instead: the module only turns on once the draw hook has run, and an
        // indirect frame whose upload hooks didn't run turns it off (TerrainRenderer).
    }

    private static final Map<String, Set<String>> APPLIED = new ConcurrentHashMap<>();

    /** Loads every target class (so its mixins have run), then: null if every hook landed, else which are missing. */
    static String loadTargetsAndCheck() {
        for (String target : EXPECTED.keySet()) {
            try {
                Class.forName(target, false, TerrainMixins.class.getClassLoader());
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
