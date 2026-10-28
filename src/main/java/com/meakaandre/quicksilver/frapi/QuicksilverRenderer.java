package com.meakaandre.quicksilver.frapi;

import com.meakaandre.quicksilver.QuicksilverClient;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.render.AltModelBlockRenderer;
import net.fabricmc.fabric.api.client.renderer.v1.render.submit.ExtendedBlockModelSubmit;
import net.fabricmc.fabric.api.client.renderer.v1.render.submit.ExtendedItemSubmit;
import net.fabricmc.fabric.api.client.rendering.v1.FeatureRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.color.block.BlockColors;

/**
 * Quicksilver's implementation of the Fabric Renderer API (package 4), in place of Fabric's Indigo: Quicksilver's
 * {@code fabric.mod.json} declares {@code fabric-renderer-api-v1:contains_renderer}, so Indigo stands aside, and this
 * registers from Quicksilver's main entrypoint, which runs before every mod's client entrypoint. Quads and meshes are
 * Quicksilver's own encoding ({@link QuadData}); blocks are lit and tinted exactly as Indigo does it
 * ({@link BlockRenderer}, with Indigo's code under the Apache License 2.0, see NOTICE); items and block models drawn as
 * features go through vanilla's own feature renderers for vanilla quads and Quicksilver's for API meshes
 * ({@link ItemSubmitRenderer}, {@link BlockSubmitRenderer}).
 *
 * <p>{@code config/quicksilver.properties}, {@code block-renderer=off} (read at start-up): every block and item is drawn
 * by vanilla's own code instead (the renderer still registers, since Indigo has stood aside); what only the API can do
 * is lost.
 *
 * <p>Only one renderer can be registered: with another renderer mod present, whichever registers second fails. Quicksilver
 * logs one error naming the other mod and which renderer won; Sodium is declared incompatible outright.
 */
public final class QuicksilverRenderer implements Renderer {
    public static final QuicksilverRenderer INSTANCE = new QuicksilverRenderer();

    private QuicksilverRenderer() {
    }

    private static volatile boolean blockRendererOff;

    /** Whether Quicksilver's block renderer is switched off in its config (every block and item drawn by vanilla's code). */
    static boolean blockRendererOff() {
        return blockRendererOff;
    }

    /** Reads {@code block-renderer} from {@code config/quicksilver.properties}, writing the file with its default if missing. */
    private static boolean readBlockRendererOff() {
        Path file = FabricLoader.getInstance().getConfigDir().resolve("quicksilver.properties");
        Properties p = new Properties();
        try {
            if (Files.exists(file)) {
                try (InputStream in = Files.newInputStream(file)) {
                    p.load(in);
                }
            } else {
                p.setProperty("block-renderer", "on");
                Files.createDirectories(file.getParent());
                try (OutputStream out = Files.newOutputStream(file)) {
                    p.store(out, "Quicksilver. block-renderer: on (Quicksilver's Fabric Renderer API renderer) or off (every block and"
                        + " item drawn by vanilla's own code; features only the Fabric Renderer API offers are lost). Needs a restart.");
                }
            }
        } catch (IOException e) {
            QuicksilverClient.LOG.warn("Quicksilver: could not read or write {}; block-renderer stays on", file, e);
        }
        String v = p.getProperty("block-renderer", "on").trim();
        if (!v.equalsIgnoreCase("on") && !v.equalsIgnoreCase("off")) {
            QuicksilverClient.LOG.warn("Quicksilver: {} has block-renderer={}, which is neither on nor off; the block renderer stays on", file, v);
        }
        return v.equalsIgnoreCase("off");
    }

    /**
     * Start-up, from the main entrypoint on the client: before any client entrypoint can ask the API for its renderer.
     * In CI's measure runs (vanilla's stand-in) it stays off, and CI's jar for them does not declare
     * {@code contains_renderer}, so Indigo draws there as in the vanilla runs.
     */
    public static void register() {
        if (Boolean.getBoolean("quicksilver.ci.measureOnly")) {
            QuicksilverClient.LOG.warn("Quicksilver renderer: off, quicksilver.ci.measureOnly is set (Indigo draws)");
            return;
        }
        boolean registered = false;
        try {
            Renderer.register(INSTANCE);
            registered = true;
        } catch (Throwable t) {
            // a warning: the one error is reportOtherRenderers' (naming the other mod) when one declares itself the renderer
            QuicksilverClient.LOG.warn("Quicksilver renderer fallback: could not register its Fabric Renderer API implementation (another"
                + " renderer registered first; that renderer draws instead)", t);
        } finally {
            reportOtherRenderers(registered);
        }
        if (!registered) return;
        blockRendererOff = readBlockRendererOff();
        if (blockRendererOff) {
            QuicksilverClient.LOG.warn("Quicksilver block renderer: off (config/quicksilver.properties, block-renderer=off; read at start-up):"
                + " every block and item is drawn by vanilla's own code, so what only the Fabric Renderer API can do is lost (mods'"
                + " API meshes lose their own lighting, colours, emissive and ambient occlusion settings and are drawn as plain"
                + " vanilla quads)");
        }
        try {
            FeatureRendererRegistry.register(ExtendedBlockModelSubmit.TYPE, BlockSubmitRenderer::new);
            FeatureRendererRegistry.register(ExtendedItemSubmit.TYPE, ItemSubmitRenderer::new);
        } catch (Throwable t) {
            QuicksilverClient.LOG.error("Quicksilver renderer fallback: its feature renderers could not be registered; items and block"
                + " models drawn through the Fabric Renderer API will not draw", t);
            return;
        }
        QuicksilverClient.LOG.info("Quicksilver: Fabric Renderer API implementation registered (in place of Indigo)");
    }

    /** One error naming every other mod that declares itself the Fabric Renderer API's renderer, and which renderer won. */
    private static void reportOtherRenderers(boolean quicksilverWon) {
        try {
            List<String> others = new ArrayList<>();
            for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
                ModMetadata m = mod.getMetadata();
                if ("quicksilver".equals(m.getId())) continue;
                // the test Indigo makes to stand aside: the key is present, whatever its value
                if (m.containsCustomValue("fabric-renderer-api-v1:contains_renderer")) others.add(m.getName() + " (" + m.getId() + ")");
            }
            if (!others.isEmpty()) {
                QuicksilverClient.LOG.error("Quicksilver renderer conflict: {} also declare{} itself the Fabric Renderer API's renderer; only one can"
                    + " draw: {}", String.join(", ", others), others.size() == 1 ? "s" : "",
                    quicksilverWon ? "Quicksilver's renderer won (it registered first); the other renderer's registration will fail"
                        : "the other renderer won (it registered first); Quicksilver's renderer is not used");
            }
        } catch (Throwable t) {
            if (t instanceof VirtualMachineError e) throw e;
            QuicksilverClient.LOG.warn("Quicksilver: could not check the other mods for a second renderer", t);
        }
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
