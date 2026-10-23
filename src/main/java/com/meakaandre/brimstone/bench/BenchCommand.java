package com.meakaandre.brimstone.bench;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

/** {@code /brimstone bench [scene] [seconds]}: no scene, or "here", measures where the player stands. */
public final class BenchCommand {
    public static final int DEFAULT_SECONDS = 60;

    private BenchCommand() {
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommands.literal("brimstone")
            .then(ClientCommands.literal("bench")
                .executes(c -> run(c, "here", DEFAULT_SECONDS))
                .then(ClientCommands.argument("scene", StringArgumentType.word())
                    .suggests((c, b) -> {
                        List<String> ids = new ArrayList<>(Scenes.ids());
                        ids.add("here");
                        return SharedSuggestionProvider.suggest(ids, b);
                    })
                    .executes(c -> run(c, StringArgumentType.getString(c, "scene"), DEFAULT_SECONDS))
                    .then(ClientCommands.argument("seconds", IntegerArgumentType.integer(5, 1800))
                        .executes(c -> run(c, StringArgumentType.getString(c, "scene"), IntegerArgumentType.getInteger(c, "seconds")))))));
    }

    private static int run(CommandContext<FabricClientCommandSource> c, String id, int seconds) {
        if (BenchRun.running()) {
            c.getSource().sendError(Component.literal("[Brimstone] A bench is already running."));
            return 0;
        }
        Scenes.Scene scene = null;
        if (!id.equals("here")) {
            scene = Scenes.find(id);
            if (scene == null) {
                c.getSource().sendError(Component.literal("[Brimstone] No scene '" + id + "' here. Scenes: " + String.join(", ", Scenes.ids()) + ", here"));
                return 0;
            }
            if (scene.skip != null) {
                c.getSource().sendError(Component.literal("[Brimstone] Scene '" + id + "' is skipped: " + scene.skip));
                return 0;
            }
        }
        BenchRun.start(scene, seconds);
        return 1;
    }
}
