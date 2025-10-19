package me.basiqueevangelist.worldspecificviewdistance.mixin.client;

import me.basiqueevangelist.worldspecificviewdistance.component.WSVDComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer {
    public IntegratedServerMixin() {
        super(null, null, null, null, null, null, null, null);
    }

    @ModifyVariable(method = "tick", at = @At(value = "LOAD", ordinal = 0), index = 5)
    private int makeIntegratedServerNotMald(int viewDist) {
        var component = WSVDComponents.GLOBAL_DISTANCE.get(saveProperties);

        if (component.globalViewDistance != 0) {
            return component.globalViewDistance - 1;
        } else {
            return viewDist;
        }
    }

    @ModifyVariable(method = "tick", at = @At(value = "LOAD", ordinal = 0), index = 6)
    private int makeIntegratedServerNotMaldTwo(int simDist) {
        var component = WSVDComponents.GLOBAL_DISTANCE.get(saveProperties);

        if (component.globalSimulationDistance != 0) {
            return component.globalSimulationDistance - 1;
        } else {
            return simDist;
        }
    }
}
