package me.basiqueevangelist.worldspecificviewdistance.mixin.client;

import com.mojang.datafixers.DataFixer;
import me.basiqueevangelist.worldspecificviewdistance.component.WSVDComponents;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ApiServices;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.net.Proxy;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer {
    public IntegratedServerMixin(Thread serverThread, LevelStorage.Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader, Proxy proxy, DataFixer dataFixer, ApiServices apiServices, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory) {
        super(serverThread, session, dataPackManager, saveLoader, proxy, dataFixer, apiServices, worldGenerationProgressListenerFactory);
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
