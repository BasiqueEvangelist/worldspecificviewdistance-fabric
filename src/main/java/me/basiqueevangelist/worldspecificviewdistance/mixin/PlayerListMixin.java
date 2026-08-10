package me.basiqueevangelist.worldspecificviewdistance.mixin;

import me.basiqueevangelist.worldspecificviewdistance.WSVDSavedData;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.network.protocol.game.ClientboundSetSimulationDistancePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.*;

@Mixin(PlayerList.class)
public class PlayerListMixin {
	@Shadow private int viewDistance;
	@Shadow @Final private MinecraftServer server;
	@Shadow private int simulationDistance;

	/**
	 * @reason Completely replaces logic.
	 * @author BasiqueEvangelist
	 */
	@Overwrite
	public void setViewDistance(int viewDistance)
	{
		this.viewDistance = viewDistance;

		for (ServerLevel w : this.server.getAllLevels()) {
			WSVDSavedData data = WSVDSavedData.getFrom(w);
			if (data.getLocalViewDistance() == 0)
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

		for (ServerLevel w : this.server.getAllLevels()) {
			WSVDSavedData data = WSVDSavedData.getFrom(w);
			if (data.getLocalSimulationDistance() == 0)
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
