package me.basiqueevangelist.worldspecificviewdistance.mixin;

import net.minecraft.network.packet.s2c.play.SimulationDistanceS2CPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import me.basiqueevangelist.worldspecificviewdistance.WSVDPersistentState;
import net.minecraft.network.packet.s2c.play.ChunkLoadDistanceS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin  {
	@Shadow private int viewDistance;
	@Shadow @Final private MinecraftServer server;
	@Shadow private int simulationDistance;
	@Unique private static final Logger LOGGER = LogManager.getLogger("WSVD/PlayerManagerMixin");

	/**
	 * @reason Completely replaces logic.
	 * @author BasiqueEvangelist
	 */
	@Overwrite
	public void setViewDistance(int viewDistance)
	{
		this.viewDistance = viewDistance;

		for (ServerWorld w : server.getWorlds()) {
			WSVDPersistentState state = WSVDPersistentState.getFrom(w);
			if (state.getLocalViewDistance() == 0)
			{
				for (ServerPlayerEntity spe : w.getPlayers())
				{
					spe.networkHandler.sendPacket(new ChunkLoadDistanceS2CPacket(viewDistance));
				}
				w.getChunkManager().applyViewDistance(viewDistance);
			}
		}
	}

	/**
	 * @reason Completely replaces logic.
	 * @author BasiqueEvangelist
	 */
	@Overwrite
	public void setSimulationDistance(int simulationDistance)
	{
		this.simulationDistance = simulationDistance;

		for (ServerWorld w : server.getWorlds()) {
			WSVDPersistentState state = WSVDPersistentState.getFrom(w);
			if (state.getLocalSimulationDistance() == 0)
			{
				for (ServerPlayerEntity spe : w.getPlayers())
				{
					spe.networkHandler.sendPacket(new SimulationDistanceS2CPacket(simulationDistance));
				}
				w.getChunkManager().applySimulationDistance(simulationDistance);
			}
		}
	}
}
