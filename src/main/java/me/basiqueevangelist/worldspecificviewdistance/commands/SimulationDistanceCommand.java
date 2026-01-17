package me.basiqueevangelist.worldspecificviewdistance.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.basiqueevangelist.worldspecificviewdistance.WSVDPersistentState;
import me.basiqueevangelist.worldspecificviewdistance.component.WSVDComponents;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.protocol.game.ClientboundSetSimulationDistancePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class SimulationDistanceCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        commandDispatcher.register(
            literal("simulationdistance")
                .then(literal("set")
                    .requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
                    .then(literal("global")
                        .then(argument("simulationDistance", IntegerArgumentType.integer(0, 255))
                            .executes(SimulationDistanceCommand::setGlobalSimulationDistance)))
                    .then(argument("dimension", DimensionArgument.dimension())
                        .then(argument("simulationDistance", IntegerArgumentType.integer(0, 255))
                            .executes(SimulationDistanceCommand::setWorldSimulationDistance))))
                .then(literal("get")
                    .then(literal("global")
                        .executes(SimulationDistanceCommand::getGlobalSimulationDistance))
                    .then(argument("dimension", DimensionArgument.dimension())
                        .executes(SimulationDistanceCommand::getWorldSimulationDistance))));
    }

    public static int setWorldSimulationDistance(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        int simDist = IntegerArgumentType.getInteger(ctx,"simulationDistance");
        CommandSourceStack src = ctx.getSource();
        ServerLevel w = DimensionArgument.getDimension(ctx, "dimension");

        WSVDPersistentState state = WSVDPersistentState.getFrom(w);
        state.setLocalSimulationDistance(simDist);

        for (ServerPlayer spe : w.players()) {
            spe.connection.send(new ClientboundSetSimulationDistancePacket(simDist == 0 ? w.getServer().getPlayerList().getSimulationDistance() : simDist - 1));
        }

        w.getChunkSource().setSimulationDistance(simDist == 0 ? w.getServer().getPlayerList().getSimulationDistance(): simDist - 1);

        src.sendSuccess(() -> CommandUtils.getMessage(
    		"Set simulation distance of world %s to %d",
            CommandUtils.getRegistryId(w), simDist), true);
        return 1;
    }

    public static int getWorldSimulationDistance(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack src = ctx.getSource();
        ServerLevel w = DimensionArgument.getDimension(ctx, "dimension");

        WSVDPersistentState state = WSVDPersistentState.getFrom(w);
        int simDist = state.getLocalSimulationDistance();

        if (simDist != 0) {
            src.sendSuccess(() -> CommandUtils.getMessage(
                "Simulation distance of world %s is %d",
                CommandUtils.getRegistryId(w), simDist), false);
        }
        else {
            src.sendSuccess(() -> CommandUtils.getMessage("Simulation distance of world %s is unspecified (currently %d)",
                CommandUtils.getRegistryId(w), src.getServer().getPlayerList().getSimulationDistance() + 1), false);
        }

        return 1;
    }

    public static int getGlobalSimulationDistance(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        int simDist = src.getServer().getPlayerList().getSimulationDistance() + 1;

        src.sendSuccess(() -> CommandUtils.getMessage("Server-wide simulation distance is currently %d", simDist), false);

        return 1;
    }

    public static int setGlobalSimulationDistance(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        int simDist = IntegerArgumentType.getInteger(ctx,"simulationDistance");

        if (src.getServer().isDedicatedServer()) {
            src.getServer().getPlayerList().setSimulationDistance(simDist - 1);

            src.sendSuccess(() -> CommandUtils.getMessage("Set server-wide simulation distance to %d", simDist), true);
        } else {
            var component = WSVDComponents.GLOBAL_DISTANCE.get(src.getServer().getWorldData());

            component.globalSimulationDistance = simDist;

            if (simDist != 0) {
                src.sendSuccess(() -> CommandUtils.getMessage("Set save simulation distance to %d", simDist), true);
            } else {
                src.sendSuccess(() -> CommandUtils.getMessage("Unset save simulation distance"), true);
            }
        }

        return 1;
    }


}
