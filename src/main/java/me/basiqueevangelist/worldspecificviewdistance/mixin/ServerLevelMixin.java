package me.basiqueevangelist.worldspecificviewdistance.mixin;

import me.basiqueevangelist.worldspecificviewdistance.commands.DistanceUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.network.protocol.game.ClientboundSetSimulationDistancePacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
    protected ServerLevelMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "addPlayer", at = @At(value = "HEAD"), require = 1)
    public void onPlayerAdded(ServerPlayer player, CallbackInfo cb) {
        int viewDistance = DistanceUtils.resolveViewDistance((ServerLevel)(Object) this);
        player.connection.send(new ClientboundSetChunkCacheRadiusPacket(viewDistance - 1));

        int simulationDistance = DistanceUtils.resolveSimulationDistance((ServerLevel)(Object) this);
        player.connection.send(new ClientboundSetSimulationDistancePacket(simulationDistance - 1));
    }
    
    @Inject(method = "<init>*", at = @At(value = "RETURN"), require = 1)
    public void setViewDistanceOnCreate(CallbackInfo cb) {
        ServerChunkCache cache = (ServerChunkCache)getChunkSource();

        int viewDistance = DistanceUtils.resolveViewDistance((ServerLevel)(Object) this);
        cache.setViewDistance(viewDistance - 1);

        int simulationDistance = DistanceUtils.resolveSimulationDistance((ServerLevel)(Object) this);
        cache.setSimulationDistance(simulationDistance - 1);
    }
}
