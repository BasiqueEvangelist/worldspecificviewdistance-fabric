package me.basiqueevangelist.worldspecificviewdistance.mixin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import me.basiqueevangelist.worldspecificviewdistance.WSVDPersistentState;
import me.basiqueevangelist.worldspecificviewdistance.commands.CommandUtils;
import net.minecraft.network.packet.s2c.play.ChunkLoadDistanceS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin  {
	@Shadow private int viewDistance;
	@Shadow @Final private MinecraftServer server;
	@Unique private static final Logger LOGGER = LogManager.getLogger("ChunkyThings/PlayerManagerMixin");

	/**
	 * @reason Completely replaces logic.
	 * @author BasiqueEvangelist
	 */
	@Overwrite
	public void setViewDistance(int viewDistance)
	{
		LOGGER.debug("Setting global view distance to {}", viewDistance + 1);
		this.viewDistance = viewDistance;

		for (ServerWorld w : server.getWorlds()) {
			WSVDPersistentState state = WSVDPersistentState.getFrom(w);
			if (state.getLocalViewDistance() == 0)
			{
				LOGGER.debug("Setting {}'s view distance to {}", CommandUtils.getRegistryId(server, w.getDimension()), viewDistance + 1);
				for (ServerPlayerEntity spe : w.getPlayers())
				{
					LOGGER.debug("Setting {}'s view distance to {}", spe.getEntityName(), viewDistance + 1);
					spe.networkHandler.sendPacket(new ChunkLoadDistanceS2CPacket(viewDistance));
				}
				w.getChunkManager().applyViewDistance(viewDistance);
			}
		}

		LOGGER.debug("Set global view distance to {}", viewDistance + 1);
	}
}
