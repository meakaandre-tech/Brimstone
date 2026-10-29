package com.meakaandre.quicksilver.phase2.mixin;

import com.meakaandre.quicksilver.phase2.NetworkBytes;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Phase 2 (P2-0), count-only: puts {@link NetworkBytes}' counter on every connection once its channel is active. */
@Mixin(Connection.class)
public abstract class ConnectionBytesMixin {
    @Inject(method = "channelActive", at = @At("TAIL"))
    private void quicksilver$countBytes(ChannelHandlerContext ctx, CallbackInfo ci) {
        NetworkBytes.attach(ctx.channel());
    }
}
