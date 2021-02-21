package me.basiqueevangelist.worldspecificviewdistance.mixin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntity {
    @Shadow public ServerPlayNetworkHandler networkHandler;
    @Shadow @Final public MinecraftServer server;
    private static Logger LOGGER = LogManager.getLogger("ChunkyThings/ServerPlayerEntityMixin");

    public ServerPlayerEntityMixin() {
        super(null, null, 0, null);
        throw new UnsupportedOperationException("lolwut");
    }


//    @Inject(method = "changeDimension", at = @At(value = "HEAD"), require = 1)
//    public void changePlayerViewDistance(DimensionType newDimension, CallbackInfoReturnable<Entity> cb) {
//        ServerWorld w = (ServerWorld)world;
//        ChunkyThingsPersistentState state = ChunkyThingsPersistentState.getFrom(w);
//        PlayerManager mgr = server.getPlayerManager();
//
//        LOGGER.debug("Player {} is travelling to {}", getEntityName(), CommandUtils.getRegistryId(world.getDimension().getType()));
//
//        int viewDistance = state.getLocalViewDistance();
//        if (viewDistance == 0)
//            viewDistance = mgr.getViewDistance()+1;
//
//        LOGGER.debug("Setting {}'s view distance to {}", getEntityName(), viewDistance);
//
//        networkHandler.sendPacket(new ChunkLoadDistanceS2CPacket(viewDistance-1));
//    }

    @Override
    @Shadow public boolean isSpectator() { return false; }

    @Override
    @Shadow public boolean isCreative() { return false; }
}
