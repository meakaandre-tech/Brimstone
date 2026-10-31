package com.meakaandre.quicksilver.mixin.mesher;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.meakaandre.quicksilver.mesher.BiomeBlend;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Package 4c: vanilla's biome blend for tints keeps each position's biome while Quicksilver's mesher meshes a section
 * on this thread ({@code mesher.BiomeBlend}). The biome lookup is wrapped wherever vanilla's blend makes it (the call's
 * owner depends on how it was compiled, so each form is tried; none required: without the hook, the blend is vanilla's).
 */
@Mixin(ClientLevel.class)
abstract class ClientLevelTintMixin {
    @WrapOperation(method = "calculateBlockTint", require = 0,
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;"))
    private Holder<Biome> quicksilver$keptBiome(ClientLevel level, BlockPos pos, Operation<Holder<Biome>> original) {
        return BiomeBlend.biome(level, pos, original);
    }

    @WrapOperation(method = "calculateBlockTint", require = 0,
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;"))
    private Holder<Biome> quicksilver$keptBiomeLevel(Level level, BlockPos pos, Operation<Holder<Biome>> original) {
        return BiomeBlend.biome(level, pos, original);
    }

    @WrapOperation(method = "calculateBlockTint", require = 0,
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelReader;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;"))
    private Holder<Biome> quicksilver$keptBiomeReader(LevelReader level, BlockPos pos, Operation<Holder<Biome>> original) {
        return BiomeBlend.biome(level, pos, original);
    }
}
