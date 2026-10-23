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
                .executes(c -> run(c, "here", DEFAULT_SECONDS, true))
                .then(ClientCommands.argument("scene", StringArgumentType.word())
                    .suggests((c, b) -> {
                        List<String> ids = new ArrayList<>(Scenes.ids());
                        ids.add("here");
                        return SharedSuggestionProvider.suggest(ids, b);
                    })
                    .executes(c -> run(c, StringArgumentType.getString(c, "scene"), DEFAULT_SECONDS, false))
                    .then(ClientCommands.literal("confirm")
                        .executes(c -> run(c, StringArgumentType.getString(c, "scene"), DEFAULT_SECONDS, true)))
                    .then(ClientCommands.argument("seconds", IntegerArgumentType.integer(5, 1800))
                        .executes(c -> run(c, StringArgumentType.getString(c, "scene"), IntegerArgumentType.getInteger(c, "seconds"), false))
                        .then(ClientCommands.literal("confirm")
                            .executes(c -> run(c, StringArgumentType.getString(c, "scene"), IntegerArgumentType.getInteger(c, "seconds"), true)))))));
    }

    private static int run(CommandContext<FabricClientCommandSource> c, String id, int seconds, boolean confirmed) {
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
            String why = Scenes.unavailable(scene);
            if (why != null) {
                c.getSource().sendError(Component.literal("[Brimstone] Scene '" + id + "' can't run here: " + why));
                return 0;
            }
            if (!confirmed) {
                c.getSource().sendFeedback(Component.literal("[Brimstone] Scene '" + id + "' changes this world: it sets the gamerules"
                    + " advance_time, advance_weather and send_command_feedback to false (they stay that way), sets the time and"
                    + " weather, changes your game mode, teleports you, and runs its setup commands (they can place blocks and"
                    + " summon mobs). It also sets your render distance to " + scene.renderDistance + " while it runs."
                    + " Use a world you don't play in. To go ahead: /brimstone bench " + id + (seconds == DEFAULT_SECONDS ? "" : " " + seconds) + " confirm"));
                return 0;
            }
        }
        BenchRun.start(scene, seconds);
        return 1;
    }
}
