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
 * registers at client start-up. Quads and meshes are Brimstone's own encoding ({@link QuadData}); block lighting,
 * tinting and vertex output follow vanilla's own code ({@link BlockRenderer}); items and block models drawn as
 * features go through vanilla's own feature renderers for vanilla quads and Brimstone's for API meshes
 * ({@link ItemSubmitRenderer}, {@link BlockSubmitRenderer}).
 */
public final class BrimstoneRenderer implements Renderer {
    public static final BrimstoneRenderer INSTANCE = new BrimstoneRenderer();

    private BrimstoneRenderer() {
    }

    /** Client start-up, before anything asks for the renderer. */
    public static void register() {
        Renderer.register(INSTANCE);
        FeatureRendererRegistry.register(ExtendedBlockModelSubmit.TYPE, BlockSubmitRenderer::new);
        FeatureRendererRegistry.register(ExtendedItemSubmit.TYPE, ItemSubmitRenderer::new);
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
