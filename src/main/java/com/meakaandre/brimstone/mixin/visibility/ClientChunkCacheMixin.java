package com.meakaandre.brimstone.mixin.visibility;

import com.meakaandre.brimstone.visibility.Visibility;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Chunk data arriving or leaving: the occlusion test distrusts that column and its neighbours until re-meshed. */
@Mixin(ClientChunkCache.class)
public abstract class ClientChunkCacheMixin {
    @Inject(method = "replaceWithPacketData", at = @At("HEAD"))
    private void brimstone$replaced(int x, int z, ClientboundLevelChunkPacketData data, CallbackInfoReturnable<LevelChunk> cir) {
        Visibility.columnChanged(x, z);
    }

    @Inject(method = "drop", at = @At("HEAD"))
    private void brimstone$dropped(ChunkPos pos, CallbackInfo ci) {
        Visibility.columnChanged(pos.x(), pos.z());
    }
}
