package me.basiqueevangelist.worldspecificviewdistance.mixin.client;

import me.basiqueevangelist.worldspecificviewdistance.component.WSVDComponents;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer {
    public IntegratedServerMixin() {
        super(null, null, null, null, null, null, null, null);
    }

    @ModifyVariable(method = "tickServer", at = @At(value = "LOAD", ordinal = 0), index = 4)
    private int makeIntegratedServerNotMald(int viewDist) {
        var component = WSVDComponents.GLOBAL_DISTANCE.get(worldData);

        if (component.globalViewDistance != 0) {
            return component.globalViewDistance - 1;
        } else {
            return viewDist;
        }
    }

    @ModifyVariable(method = "tickServer", at = @At(value = "LOAD", ordinal = 0), index = 5)
    private int makeIntegratedServerNotMaldTwo(int simDist) {
        var component = WSVDComponents.GLOBAL_DISTANCE.get(worldData);

        if (component.globalSimulationDistance != 0) {
            return component.globalSimulationDistance - 1;
        } else {
            return simDist;
        }
    }
}
