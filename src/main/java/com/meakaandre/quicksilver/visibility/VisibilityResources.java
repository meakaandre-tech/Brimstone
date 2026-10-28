package com.meakaandre.quicksilver.visibility;

import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;

/**
 * The occlusion test trusts vanilla's full opaque blocks to look like vanilla's: in 26.3 the layer a quad is drawn in
 * comes from its texture's transparency, so a pack (resource pack or a mod's resources) that gives such a block
 * see-through texels or another model would let things show through that the test hides. The simplest sound rule,
 * like package 1's check of the terrain shaders: if any block texture, block model or blockstate in the
 * {@code minecraft} namespace comes from a pack other than the game's own, hide nothing (the module defers to vanilla
 * and says so in the log), and check again after every resource reload.
 */
final class VisibilityResources {
    private static final String VANILLA_PACK = "vanilla";
    private static final String[] FOLDERS = {"textures/block", "models/block", "blockstates"};

    private VisibilityResources() {
    }

    /** Null if every minecraft block texture, model and blockstate comes from the game's own pack; else which and from where. */
    static String override() {
        var manager = Minecraft.getInstance().getResourceManager();
        int checked = 0;
        for (String folder : FOLDERS) {
            Map<Identifier, Resource> found = manager.listResources(folder, id -> "minecraft".equals(id.getNamespace()));
            for (Map.Entry<Identifier, Resource> e : found.entrySet()) {
                checked++;
                String pack = e.getValue().sourcePackId();
                if (!VANILLA_PACK.equals(pack)) return "the pack '" + pack + "' supplies " + e.getKey();
            }
        }
        if (checked == 0) return "no vanilla block resources were found to check";
        return null;
    }
}
