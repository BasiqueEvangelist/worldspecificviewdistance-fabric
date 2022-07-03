package me.basiqueevangelist.worldspecificviewdistance.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;

public class GlobalDistanceComponent implements Component {
    public int globalViewDistance = 0;
    public int globalSimulationDistance = 0;

    @Override
    public void readFromNbt(NbtCompound tag) {
        globalViewDistance = tag.getInt("GlobalViewDistance");
        globalSimulationDistance = tag.getInt("GlobalSimulationDistance");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("GlobalViewDistance", globalViewDistance);
        tag.putInt("GlobalSimulationDistance", globalSimulationDistance);
    }
}
