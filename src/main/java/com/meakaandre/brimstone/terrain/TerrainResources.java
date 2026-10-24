package com.meakaandre.brimstone.terrain;

import com.meakaandre.brimstone.BrimstoneClient;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.client.renderer.texture.TextureAtlas;

/** What Brimstone checks in the game's resources before it draws terrain. */
final class TerrainResources {
    /** The pack that holds the game's own resources. */
    private static final String VANILLA_PACK = "vanilla";

    /**
     * The game's terrain vertex shader and everything it includes: Brimstone's vertex shader stands in for these, so
     * if a resource pack (or a mod's resources) replaces any of them, Brimstone's picture would no longer match.
     */
    private static final List<Identifier> STOOD_IN_FOR = List.of(
        Identifier.withDefaultNamespace("shaders/core/terrain.vsh"),
        Identifier.withDefaultNamespace("shaders/include/fog.glsl"),
        Identifier.withDefaultNamespace("shaders/include/globals.glsl"),
        Identifier.withDefaultNamespace("shaders/include/projection.glsl"),
        Identifier.withDefaultNamespace("shaders/include/sample_lightmap.glsl"),
        Identifier.withDefaultNamespace("shaders/include/terrainglobals.glsl"),
        Identifier.withDefaultNamespace("shaders/include/chunksection.glsl"));

    private TerrainResources() {
    }

    /** Null if all of them come from the game's own pack; else which file comes from which pack. */
    static String override() {
        var manager = Minecraft.getInstance().getResourceManager();
        for (Identifier id : STOOD_IN_FOR) {
            Optional<Resource> r = manager.getResource(id);
            if (r.isEmpty()) return id + " is missing from the resources";
            String pack = r.get().sourcePackId();
            if (!VANILLA_PACK.equals(pack)) return id + " is replaced by the pack '" + pack + "'";
        }
        return null;
    }

    /** One log line when the block atlas is too large for compact UVs to hold every 1/16 texel. */
    static void logAtlas() {
        try {
            GpuTextureView view = Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).getTextureView();
            int w = view.getWidth(0), h = view.getHeight(0);
            if (w > 4096 || h > 4096) {
                BrimstoneClient.LOG.info("Brimstone terrain: the block atlas is {}x{}, wider than the 4096 compact UVs hold exactly;"
                    + " quads whose UVs aren't on their grid take the wide vertex format", w, h);
            }
        } catch (Throwable t) {
            BrimstoneClient.LOG.warn("Brimstone terrain: could not read the block atlas size (UVs are checked per vertex anyway)", t);
        }
    }
}
