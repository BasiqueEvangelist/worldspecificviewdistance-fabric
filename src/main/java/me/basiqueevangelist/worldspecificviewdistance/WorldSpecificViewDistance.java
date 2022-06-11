package me.basiqueevangelist.worldspecificviewdistance;

import me.basiqueevangelist.worldspecificviewdistance.commands.SimulationDistanceCommand;
import me.basiqueevangelist.worldspecificviewdistance.commands.ViewDistanceCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class WorldSpecificViewDistance implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(ViewDistanceCommand::register);
		CommandRegistrationCallback.EVENT.register(SimulationDistanceCommand::register);
	}
}
