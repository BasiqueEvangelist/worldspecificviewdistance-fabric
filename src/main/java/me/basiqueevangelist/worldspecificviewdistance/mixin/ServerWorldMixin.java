package me.basiqueevangelist.worldspecificviewdistance.mixin;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.basiqueevangelist.worldspecificviewdistance.WSVDPersistentState;
import me.basiqueevangelist.worldspecificviewdistance.commands.CommandUtils;
import net.minecraft.network.packet.s2c.play.ChunkLoadDistanceS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> registryEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
        super(properties, registryRef, registryEntry, profiler, isClient, debugWorld, seed);
    }

    @Shadow public abstract PersistentStateManager getPersistentStateManager();
    @Shadow @Final private MinecraftServer server;

    private static final Logger LOGGER = LogManager.getLogger("WSVD/ServerWorldMixin");

    @Inject(method = "addPlayer", at = @At(value = "HEAD"), require = 1)
    public void onPlayerAdded(ServerPlayerEntity player, CallbackInfo cb) {
        WSVDPersistentState state = WSVDPersistentState.getFrom(getPersistentStateManager());
        PlayerManager mgr = server.getPlayerManager();

        LOGGER.debug("Player {} added to {}", player.getEntityName(), CommandUtils.getRegistryId(server, getDimension()));

        int viewDistance = state.getLocalViewDistance();
        if (viewDistance == 0)
            viewDistance = mgr.getViewDistance() + 1;

        LOGGER.debug("Setting {}'s view distance to {}", player.getEntityName(), viewDistance);

        player.networkHandler.sendPacket(new ChunkLoadDistanceS2CPacket(viewDistance - 1));
    }
    
    @Inject(method = "<init>*", at = @At(value = "RETURN"), require = 1)
    public void setViewDistanceOnCreate(CallbackInfo cb) {
        ServerChunkManager cmgr = (ServerChunkManager)getChunkManager();
        WSVDPersistentState state = WSVDPersistentState.getFrom(getPersistentStateManager());
        PlayerManager pmgr = server.getPlayerManager();

        LOGGER.debug("World {} is loaded", CommandUtils.getRegistryId(server, getDimension()));

        int viewDistance = state.getLocalViewDistance();
        if (viewDistance == 0)
            viewDistance = pmgr.getViewDistance() + 1;

        LOGGER.debug("Setting {}'s view distance to {}", CommandUtils.getRegistryId(server, getDimension()), viewDistance);

        cmgr.applyViewDistance(viewDistance - 1);
    }
}
