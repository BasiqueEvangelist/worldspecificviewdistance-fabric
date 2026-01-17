package me.basiqueevangelist.worldspecificviewdistance.commands;

import me.basiqueevangelist.worldspecificviewdistance.WSVDSavedData;
import me.basiqueevangelist.worldspecificviewdistance.component.GlobalDistanceComponent;
import me.basiqueevangelist.worldspecificviewdistance.component.WSVDComponents;
import net.minecraft.server.level.ServerLevel;

public final class DistanceUtils {
    private DistanceUtils() {

    }

    public static int resolveViewDistance(ServerLevel level) {
        WSVDSavedData data = WSVDSavedData.getFrom(level);
        GlobalDistanceComponent globalDist = WSVDComponents.GLOBAL_DISTANCE.get(level.getServer().getWorldData());

        int viewDistance = data.getLocalViewDistance();

        if (viewDistance != 0)
            return viewDistance;

        viewDistance = globalDist.globalViewDistance;

        if (viewDistance != 0)
            return viewDistance;

        return level.getServer().getPlayerList().getViewDistance() + 1;
    }

    public static int resolveSimulationDistance(ServerLevel level) {
        WSVDSavedData data = WSVDSavedData.getFrom(level);
        GlobalDistanceComponent globalDist = WSVDComponents.GLOBAL_DISTANCE.get(level.getServer().getWorldData());

        int simDistance = data.getLocalSimulationDistance();

        if (simDistance != 0)
            return simDistance;

        simDistance = globalDist.globalSimulationDistance;

        if (simDistance != 0)
            return simDistance;

        return level.getServer().getPlayerList().getSimulationDistance() + 1;
    }
}
