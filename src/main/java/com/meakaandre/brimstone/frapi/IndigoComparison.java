package com.meakaandre.brimstone.frapi;

import com.meakaandre.brimstone.BrimstoneClient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.client.renderer.v1.render.AltModelBlockRenderer;
import net.minecraft.client.color.block.BlockColors;

/**
 * DIAGNOSTIC (branch only, removed before review): in a measure run (Indigo is the renderer) with
 * {@code -Dbrimstone.debug.compareIndigo=true}, every block Indigo tessellates is also tessellated by Brimstone's
 * {@link BlockRenderer} and the two outputs are compared exactly (positions, colours, lightmaps); Indigo's is drawn.
 */
public final class IndigoComparison implements Renderer {
    private static final AtomicInteger BLOCKS = new AtomicInteger(), DIFFERENT = new AtomicInteger(), LOGGED = new AtomicInteger();
    private final Renderer indigo;

    private IndigoComparison(Renderer indigo) {
        this.indigo = indigo;
    }

    public static void install() {
        try {
            Class<?> manager = Class.forName("net.fabricmc.fabric.impl.client.renderer.RendererManager");
            Field f = manager.getDeclaredField("activeRenderer");
            f.setAccessible(true);
            Renderer original = (Renderer) f.get(null);
            f.set(null, new IndigoComparison(original));
            BrimstoneClient.LOG.warn("Brimstone renderer DIAGNOSTIC: comparing Brimstone's block renderer with {}", original.getClass().getName());
        } catch (Throwable t) {
            BrimstoneClient.LOG.error("Brimstone renderer DIAGNOSTIC: could not install the comparison", t);
        }
    }

    @Override
    public QuadEmitter quadEmitter(Consumer<? super MutableQuadView> consumer) {
        return indigo.quadEmitter(consumer);
    }

    @Override
    public MutableMesh mutableMesh() {
        return indigo.mutableMesh();
    }

    @Override
    public AltModelBlockRenderer altModelBlockRenderer(boolean ambientOcclusion, boolean cull, BlockColors blockColors) {
        AltModelBlockRenderer theirs = indigo.altModelBlockRenderer(ambientOcclusion, cull, blockColors);
        AltModelBlockRenderer ours = new BlockRenderer(ambientOcclusion, cull, blockColors);
        List<String> a = new ArrayList<>(), b = new ArrayList<>();
        QuadEmitter captureTheirs = indigo.quadEmitter(q -> a.add(describe(q)));
        QuadEmitter captureOurs = new MutableQuadViewImpl() {
            @Override
            protected void emitDirectly() {
                b.add(describe(this));
            }
        };
        return (output, x, y, z, level, pos, state, model, seed) -> {
            a.clear();
            b.clear();
            try {
                theirs.tesselateBlock(captureTheirs, x, y, z, level, pos, state, model, seed);
                ours.tesselateBlock(captureOurs, x, y, z, level, pos, state, model, seed);
                int n = BLOCKS.incrementAndGet();
                if (!a.equals(b)) {
                    int d = DIFFERENT.incrementAndGet();
                    if (LOGGED.incrementAndGet() <= 300) {
                        StringBuilder s = new StringBuilder();
                        int shown = 0;
                        for (int i = 0; i < Math.max(a.size(), b.size()) && shown < 3; i++) {
                            String p = i < a.size() ? a.get(i) : "-", q = i < b.size() ? b.get(i) : "-";
                            if (!p.equals(q)) {
                                s.append("\n  #").append(i).append(" indigo: ").append(p).append("\n  #").append(i).append(" brimstone: ").append(q);
                                shown++;
                            }
                        }
                        BrimstoneClient.LOG.info("INDIGO COMPARISON diff #{} (of {} blocks) {} at {} ao={} quads {}/{}:{}", d, n, state, pos, ambientOcclusion,
                            a.size(), b.size(), s);
                    }
                }
                if (n % 50000 == 0) BrimstoneClient.LOG.info("INDIGO COMPARISON progress: {} blocks, {} different", n, DIFFERENT.get());
            } catch (Throwable t) {
                if (LOGGED.incrementAndGet() <= 300) BrimstoneClient.LOG.info("INDIGO COMPARISON threw at {} {}", state, pos, t);
            }
            theirs.tesselateBlock(output, x, y, z, level, pos, state, model, seed);
        };
    }

    public static void summary() {
        BrimstoneClient.LOG.info("INDIGO COMPARISON summary: {} blocks compared, {} different", BLOCKS.get(), DIFFERENT.get());
    }

    private static String describe(QuadView q) {
        StringBuilder s = new StringBuilder();
        s.append(q.chunkLayer()).append(' ');
        for (int i = 0; i < 4; i++) {
            s.append(String.format("[%s %s %s c=%08x l=%08x]", Float.toHexString(q.x(i)), Float.toHexString(q.y(i)), Float.toHexString(q.z(i)),
                q.color(i), q.lightmap(i)));
        }
        return s.toString();
    }
}
