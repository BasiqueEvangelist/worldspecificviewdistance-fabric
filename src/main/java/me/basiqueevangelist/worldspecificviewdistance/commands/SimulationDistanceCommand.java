package me.basiqueevangelist.worldspecificviewdistance.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.basiqueevangelist.worldspecificviewdistance.WSVDPersistentState;
import me.basiqueevangelist.worldspecificviewdistance.component.WSVDComponents;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.network.packet.s2c.play.SimulationDistanceS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class SimulationDistanceCommand {
    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        commandDispatcher.register(
            literal("simulationdistance")
                .then(literal("set")
                    .requires((src) -> src.hasPermissionLevel(2))
                    .then(literal("global")
                        .then(argument("simulationDistance", IntegerArgumentType.integer(0, 255))
                            .executes(SimulationDistanceCommand::setGlobalSimulationDistance)))
                    .then(argument("dimension", DimensionArgumentType.dimension())
                        .then(argument("simulationDistance", IntegerArgumentType.integer(0, 255))
                            .executes(SimulationDistanceCommand::setWorldSimulationDistance))))
                .then(literal("get")
                    .then(literal("global")
                        .executes(SimulationDistanceCommand::getGlobalSimulationDistance))
                    .then(argument("dimension", DimensionArgumentType.dimension())
                        .executes(SimulationDistanceCommand::getWorldSimulationDistance))));
    }

    public static int setWorldSimulationDistance(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        int simDist = IntegerArgumentType.getInteger(ctx,"simulationDistance");
        ServerCommandSource src = ctx.getSource();
        ServerWorld w = DimensionArgumentType.getDimensionArgument(ctx, "dimension");

        WSVDPersistentState state = WSVDPersistentState.getFrom(w);
        state.setLocalSimulationDistance(simDist);

        for (ServerPlayerEntity spe : w.getPlayers()) {
            spe.networkHandler.sendPacket(new SimulationDistanceS2CPacket(simDist == 0 ? w.getServer().getPlayerManager().getSimulationDistance() : simDist - 1));
        }

        w.getChunkManager().applySimulationDistance(simDist == 0 ? w.getServer().getPlayerManager().getSimulationDistance(): simDist - 1);

        src.sendFeedback(CommandUtils.getMessage(
    		"Set simulation distance of world %s to %d",
            CommandUtils.getRegistryId(src.getServer(), w.getDimension()), simDist), true);
        return 1;
    }

    public static int getWorldSimulationDistance(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource src = ctx.getSource();
        ServerWorld w = DimensionArgumentType.getDimensionArgument(ctx, "dimension");

        WSVDPersistentState state = WSVDPersistentState.getFrom(w);
        int simDist = state.getLocalSimulationDistance();

        if (simDist != 0) {
            src.sendFeedback(CommandUtils.getMessage(
                "Simulation distance of world %s is %d",
                CommandUtils.getRegistryId(src.getServer(), w.getDimension()), simDist), false);
        }
        else {
            src.sendFeedback(CommandUtils.getMessage("Simulation distance of world %s is unspecified (currently %d)",
                CommandUtils.getRegistryId(src.getServer(), w.getDimension()), src.getServer().getPlayerManager().getSimulationDistance() + 1), false);
        }

        return 1;
    }

    public static int getGlobalSimulationDistance(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource src = ctx.getSource();
        int simDist = src.getServer().getPlayerManager().getSimulationDistance() + 1;

        src.sendFeedback(CommandUtils.getMessage("Server-wide simulation distance is currently %d", simDist), false);

        return 1;
    }

    public static int setGlobalSimulationDistance(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource src = ctx.getSource();
        int simDist = IntegerArgumentType.getInteger(ctx,"simulationDistance");

        if (src.getServer().isDedicated()) {
            src.getServer().getPlayerManager().setSimulationDistance(simDist - 1);

            src.sendFeedback(CommandUtils.getMessage("Set server-wide simulation distance to %d", simDist), true);
        } else {
            var component = WSVDComponents.GLOBAL_DISTANCE.get(src.getServer().getSaveProperties());

            component.globalSimulationDistance = simDist;

            if (simDist != 0) {
                src.sendFeedback(CommandUtils.getMessage("Set save simulation distance to %d", simDist), true);
            } else {
                src.sendFeedback(CommandUtils.getMessage("Unset save simulation distance"), true);
            }
        }

        return 1;
    }


}
