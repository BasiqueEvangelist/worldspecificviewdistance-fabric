package me.basiqueevangelist.worldspecificviewdistance.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.basiqueevangelist.worldspecificviewdistance.WSVDPersistentState;
import me.basiqueevangelist.worldspecificviewdistance.component.WSVDComponents;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.network.packet.s2c.play.ChunkLoadDistanceS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class ViewDistanceCommand {
    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        commandDispatcher.register(
            literal("viewdistance")
                .then(literal("set")
                    .requires((src) -> src.hasPermissionLevel(2))
                    .then(literal("global")
                        .then(argument("viewDistance", IntegerArgumentType.integer(0, 255))
                            .executes(ViewDistanceCommand::setGlobalViewDistance)))
                    .then(argument("dimension", DimensionArgumentType.dimension())
                        .then(argument("viewDistance", IntegerArgumentType.integer(0, 255))
                            .executes(ViewDistanceCommand::setWorldViewDistance))))
                .then(literal("get")
                    .then(literal("global")
                        .executes(ViewDistanceCommand::getGlobalViewDistance))
                    .then(argument("dimension", DimensionArgumentType.dimension())
                        .executes(ViewDistanceCommand::getWorldViewDistance))));
    }

    public static int setWorldViewDistance(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        int viewdist = IntegerArgumentType.getInteger(ctx,"viewDistance");
        ServerCommandSource src = ctx.getSource();
        ServerWorld w = DimensionArgumentType.getDimensionArgument(ctx, "dimension");

        WSVDPersistentState state = WSVDPersistentState.getFrom(w);
        state.setLocalViewDistance(viewdist);

        for (ServerPlayerEntity spe : w.getPlayers()) {
            spe.networkHandler.sendPacket(new ChunkLoadDistanceS2CPacket(viewdist == 0 ? w.getServer().getPlayerManager().getViewDistance() : viewdist-1));
        }

        w.getChunkManager().applyViewDistance(viewdist == 0 ? w.getServer().getPlayerManager().getViewDistance(): viewdist-1);

        src.sendFeedback(() -> CommandUtils.getMessage(
    		"Set view distance of world %s to %d",
            CommandUtils.getRegistryId(w), viewdist), true);
        return 1;
    }

    public static int getWorldViewDistance(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource src = ctx.getSource();
        ServerWorld w = DimensionArgumentType.getDimensionArgument(ctx, "dimension");

        WSVDPersistentState state = WSVDPersistentState.getFrom(w);
        int viewDist = state.getLocalViewDistance();

        if (viewDist != 0) {
            src.sendFeedback(() -> CommandUtils.getMessage(
                "View distance of world %s is %d",
                CommandUtils.getRegistryId(w), viewDist), false);
        }
        else {
            src.sendFeedback(() -> CommandUtils.getMessage("View distance of world %s is unspecified (currently %d)",
                CommandUtils.getRegistryId(w), src.getServer().getPlayerManager().getViewDistance()+1), false);
        }

        return 1;
    }

    public static int getGlobalViewDistance(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource src = ctx.getSource();
        int viewDist = src.getServer().getPlayerManager().getViewDistance() + 1;

        src.sendFeedback(() -> CommandUtils.getMessage("Server-wide view distance is currently %d", viewDist), false);

        return 1;
    }

    public static int setGlobalViewDistance(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource src = ctx.getSource();
        int viewDist = IntegerArgumentType.getInteger(ctx,"viewDistance");

        if (src.getServer().isDedicated()) {
            src.getServer().getPlayerManager().setViewDistance(viewDist - 1);

            src.sendFeedback(() -> CommandUtils.getMessage("Set server-wide view distance to %d", viewDist), true);
        } else {
            var component = WSVDComponents.GLOBAL_DISTANCE.get(src.getServer().getSaveProperties());

            component.globalViewDistance = viewDist;

            if (viewDist != 0) {
                src.sendFeedback(() -> CommandUtils.getMessage("Set save view distance to %d", viewDist), true);
            } else {
                src.sendFeedback(() -> CommandUtils.getMessage("Unset save view distance"), true);
            }
        }
        return 1;
    }


}
