package me.basiqueevangelist.worldspecificviewdistance.mixin;

import java.util.function.Supplier;

import me.basiqueevangelist.worldspecificviewdistance.commands.DistanceUtils;
import net.minecraft.network.packet.s2c.play.SimulationDistanceS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.MutableWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.s2c.play.ChunkLoadDistanceS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "addPlayer", at = @At(value = "HEAD"), require = 1)
    public void onPlayerAdded(ServerPlayerEntity player, CallbackInfo cb) {
        int viewDistance = DistanceUtils.resolveViewDistance((ServerWorld)(Object) this);
        player.networkHandler.sendPacket(new ChunkLoadDistanceS2CPacket(viewDistance - 1));

        int simulationDistance = DistanceUtils.resolveSimulationDistance((ServerWorld)(Object) this);
        player.networkHandler.sendPacket(new SimulationDistanceS2CPacket(simulationDistance - 1));
    }
    
    @Inject(method = "<init>*", at = @At(value = "RETURN"), require = 1)
    public void setViewDistanceOnCreate(CallbackInfo cb) {
        ServerChunkManager cmgr = (ServerChunkManager)getChunkManager();

        int viewDistance = DistanceUtils.resolveViewDistance((ServerWorld)(Object) this);
        cmgr.applyViewDistance(viewDistance - 1);

        int simulationDistance = DistanceUtils.resolveSimulationDistance((ServerWorld)(Object) this);
        cmgr.applySimulationDistance(simulationDistance - 1);
    }
}
