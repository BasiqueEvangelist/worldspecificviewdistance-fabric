package me.basiqueevangelist.worldspecificviewdistance.mixin;

import me.basiqueevangelist.worldspecificviewdistance.WSVDPersistentState;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.network.protocol.game.ClientboundSetSimulationDistancePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.*;

@Mixin(PlayerList.class)
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

		for (ServerLevel w : server.getAllLevels()) {
			WSVDPersistentState state = WSVDPersistentState.getFrom(w);
			if (state.getLocalViewDistance() == 0)
			{
				for (ServerPlayer spe : w.players())
				{
					spe.connection.send(new ClientboundSetChunkCacheRadiusPacket(viewDistance));
				}
				w.getChunkSource().setViewDistance(viewDistance);
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

		for (ServerLevel w : server.getAllLevels()) {
			WSVDPersistentState state = WSVDPersistentState.getFrom(w);
			if (state.getLocalSimulationDistance() == 0)
			{
				for (ServerPlayer spe : w.players())
				{
					spe.connection.send(new ClientboundSetSimulationDistancePacket(simulationDistance));
				}
				w.getChunkSource().setSimulationDistance(simulationDistance);
			}
		}
	}
}
