package com.meakaandre.brimstone.frapi;

import com.meakaandre.brimstone.BrimstoneClient;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.render.AltModelBlockRenderer;
import net.fabricmc.fabric.api.client.renderer.v1.render.submit.ExtendedBlockModelSubmit;
import net.fabricmc.fabric.api.client.renderer.v1.render.submit.ExtendedItemSubmit;
import net.fabricmc.fabric.api.client.rendering.v1.FeatureRendererRegistry;
import net.minecraft.client.color.block.BlockColors;

/**
 * Brimstone's implementation of the Fabric Renderer API (package 4), in place of Fabric's Indigo: Brimstone's
 * {@code fabric.mod.json} declares {@code fabric-renderer-api-v1:contains_renderer}, so Indigo stands aside, and this
 * registers from Brimstone's main entrypoint, which runs before every mod's client entrypoint. Quads and meshes are
 * Brimstone's own encoding ({@link QuadData}); blocks are lit as Indigo lights them (smooth lighting, {@link FaceAo}) or
 * by vanilla's own code (flat lighting), tinted and written as vanilla does it ({@link BlockRenderer}); items and block
 * models drawn as features go through vanilla's own feature renderers for vanilla quads and Brimstone's for API meshes
 * ({@link ItemSubmitRenderer}, {@link BlockSubmitRenderer}).
 *
 * <p>Only one renderer can be registered: with another renderer mod present, whichever registers second fails. If
 * Brimstone is second, it logs that and the other renderer draws.
 */
public final class BrimstoneRenderer implements Renderer {
    public static final BrimstoneRenderer INSTANCE = new BrimstoneRenderer();

    private BrimstoneRenderer() {
    }

    /**
     * Start-up, from the main entrypoint on the client: before any client entrypoint can ask the API for its renderer.
     * In CI's measure runs (vanilla's stand-in) it stays off, and CI's jar for them does not declare
     * {@code contains_renderer}, so Indigo draws there as in the vanilla runs.
     */
    public static void register() {
        if (Boolean.getBoolean("brimstone.ci.measureOnly")) {
            BrimstoneClient.LOG.warn("Brimstone renderer: off, brimstone.ci.measureOnly is set (Indigo draws)");
            return;
        }
        try {
            Renderer.register(INSTANCE);
        } catch (Throwable t) {
            BrimstoneClient.LOG.error("Brimstone renderer fallback: could not register its Fabric Renderer API implementation (another"
                + " renderer mod registered first; that renderer draws instead)", t);
            return;
        }
        try {
            FeatureRendererRegistry.register(ExtendedBlockModelSubmit.TYPE, BlockSubmitRenderer::new);
            FeatureRendererRegistry.register(ExtendedItemSubmit.TYPE, ItemSubmitRenderer::new);
        } catch (Throwable t) {
            BrimstoneClient.LOG.error("Brimstone renderer fallback: its feature renderers could not be registered; items and block"
                + " models drawn through the Fabric Renderer API will not draw", t);
            return;
        }
        BrimstoneClient.LOG.info("Brimstone: Fabric Renderer API implementation registered (in place of Indigo)");
    }

    @Override
    public QuadEmitter quadEmitter(Consumer<? super MutableQuadView> consumer) {
        return new MutableQuadViewImpl() {
            @Override
            protected void emitDirectly() {
                consumer.accept(this);
            }
        };
    }

    @Override
    public MutableMesh mutableMesh() {
        return new Meshes.Mutable();
    }

    @Override
    public AltModelBlockRenderer altModelBlockRenderer(boolean ambientOcclusion, boolean cull, BlockColors blockColors) {
        return new BlockRenderer(ambientOcclusion, cull, blockColors);
    }
}
