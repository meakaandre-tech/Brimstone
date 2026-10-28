package com.meakaandre.quicksilver.animation;

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
 * Mixin plugin for the animation module's mixins (their own config: not required, require 0, so a hook that no longer
 * fits the game is skipped instead of crashing it). It records which hooks landed; the module only turns on when every
 * one did ({@link #loadTargetsAndCheck}): without the use hooks it could leave out a sprite something draws. The
 * {@code @WrapMethod} scopes are applied by MixinExtras later: the section meshing one is checked when it runs, the sprite
 * finder one is optional (without it Fabric's finder only makes every sprite count as used outside terrain).
 */
public final class AnimationMixins implements IMixinConfigPlugin {
    static final Map<String, List<String>> EXPECTED = new LinkedHashMap<>();

    static {
        EXPECTED.put("net.minecraft.client.renderer.texture.TextureAtlas", List.of("quicksilver$uploaded", "quicksilver$uploadFrames"));
        EXPECTED.put("net.minecraft.client.renderer.texture.TextureAtlasSprite", List.of("quicksilver$stateCreated", "quicksilver$used"));
        EXPECTED.put("net.minecraft.client.resources.model.geometry.BakedQuad", List.of("quicksilver$quadUsed"));
        EXPECTED.put("net.minecraft.client.renderer.LevelRenderer", List.of("quicksilver$animPrepare"));
    }

    /**
     * Handlers injected into several methods: how many of the target's methods must call each (a getter or accessor
     * that no longer matches would otherwise leave a hole in "used outside terrain").
     */
    static final Map<String, Integer> METHODS = Map.of("quicksilver$used", 8, "quicksilver$quadUsed", 5, "quicksilver$animPrepare", 2);

    private static final Map<String, Set<String>> APPLIED = new ConcurrentHashMap<>();

    static String loadTargetsAndCheck() {
        for (String target : EXPECTED.keySet()) {
            try {
                Class.forName(target, false, AnimationMixins.class.getClassLoader());
            } catch (Throwable t) {
                return "could not load " + target + ": " + t;
            }
        }
        Set<String> missing = new TreeSet<>();
        for (var e : EXPECTED.entrySet()) {
            Set<String> got = APPLIED.getOrDefault(e.getKey(), Set.of());
            for (String h : e.getValue()) if (!got.contains(h)) missing.add(e.getKey().substring(e.getKey().lastIndexOf('.') + 1) + "#" + h);
        }
        if (!MeshSprites.class.isAssignableFrom(net.minecraft.client.renderer.chunk.CompiledSectionMesh.class)) missing.add("CompiledSectionMesh (fields)");
        if (!StateSprite.class.isAssignableFrom(net.minecraft.client.renderer.texture.SpriteContents.AnimationState.class)) missing.add("AnimationState (fields)");
        return missing.isEmpty() ? null : "hooks that did not apply: " + String.join(", ", missing);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        List<String> expected = EXPECTED.get(targetClassName);
        if (expected == null) return;
        Set<String> found = APPLIED.computeIfAbsent(targetClassName, k -> ConcurrentHashMap.newKeySet());
        Map<String, Integer> callers = new java.util.HashMap<>();
        for (MethodNode m : targetClass.methods) {
            Set<String> inThis = new java.util.HashSet<>();
            for (AbstractInsnNode insn = m.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                if (insn instanceof MethodInsnNode call && call.owner.equals(targetClass.name)) {
                    for (String h : expected) if (call.name.endsWith(h)) inThis.add(h);
                }
            }
            for (String h : inThis) callers.merge(h, 1, Integer::sum);
        }
        for (var e : callers.entrySet()) {
            if (e.getValue() >= METHODS.getOrDefault(e.getKey(), 1)) found.add(e.getKey());
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
