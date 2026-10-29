package com.meakaandre.quicksilver.phase2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.util.concurrent.atomic.LongAdder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Phase 2 (P2-0), measurement only: bytes on the wire per side, counted by a handler at the socket end of each
 * connection's pipeline (after compression and encryption, so what the network really carries). A connection over
 * the in-memory channel of single player carries packets, not bytes, and counts nothing. Never changes a packet.
 */
public final class NetworkBytes {
    static final Logger LOG = LoggerFactory.getLogger("Quicksilver");
    private static final String NAME = "quicksilver_bytes";
    public static final LongAdder SERVER_IN = new LongAdder(), SERVER_OUT = new LongAdder();
    public static final LongAdder CLIENT_IN = new LongAdder(), CLIENT_OUT = new LongAdder();
    private static volatile boolean off = false;

    private NetworkBytes() {
    }

    /** Called when a connection's channel becomes active; a channel accepted by a server has a parent. */
    public static void attach(Channel ch) {
        if (off) return;
        try {
            if (ch.pipeline().get(NAME) == null) ch.pipeline().addFirst(NAME, new Counter(ch.parent() != null));
        } catch (Throwable t) {
            off = true;
            LOG.warn("Quicksilver: network byte counter off ({}); the game is unaffected", t.toString());
        }
    }

    public static boolean active() {
        return !off;
    }

    private static final class Counter extends ChannelDuplexHandler {
        private final LongAdder in, out;

        Counter(boolean server) {
            in = server ? SERVER_IN : CLIENT_IN;
            out = server ? SERVER_OUT : CLIENT_OUT;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof ByteBuf b) in.add(b.readableBytes());
            super.channelRead(ctx, msg);
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            if (msg instanceof ByteBuf b) out.add(b.readableBytes());
            super.write(ctx, msg, promise);
        }
    }
}
