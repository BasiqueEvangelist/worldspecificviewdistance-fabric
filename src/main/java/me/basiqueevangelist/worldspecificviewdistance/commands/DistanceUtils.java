package me.basiqueevangelist.worldspecificviewdistance.commands;

import me.basiqueevangelist.worldspecificviewdistance.WSVDPersistentState;
import me.basiqueevangelist.worldspecificviewdistance.component.GlobalDistanceComponent;
import me.basiqueevangelist.worldspecificviewdistance.component.WSVDComponents;
import net.minecraft.server.world.ServerWorld;

public final class DistanceUtils {
    private DistanceUtils() {

    }

    public static int resolveViewDistance(ServerWorld world) {
        WSVDPersistentState state = WSVDPersistentState.getFrom(world);
        GlobalDistanceComponent globalDist = WSVDComponents.GLOBAL_DISTANCE.get(world.getServer().getSaveProperties());

        int viewDistance = state.getLocalViewDistance();

        if (viewDistance != 0)
            return viewDistance;

        viewDistance = globalDist.globalViewDistance;

        if (viewDistance != 0)
            return viewDistance;

        return world.getServer().getPlayerManager().getViewDistance() + 1;
    }

    public static int resolveSimulationDistance(ServerWorld world) {
        WSVDPersistentState state = WSVDPersistentState.getFrom(world);
        GlobalDistanceComponent globalDist = WSVDComponents.GLOBAL_DISTANCE.get(world.getServer().getSaveProperties());

        int simDistance = state.getLocalSimulationDistance();

        if (simDistance != 0)
            return simDistance;

        simDistance = globalDist.globalSimulationDistance;

        if (simDistance != 0)
            return simDistance;

        return world.getServer().getPlayerManager().getSimulationDistance() + 1;
    }
}
