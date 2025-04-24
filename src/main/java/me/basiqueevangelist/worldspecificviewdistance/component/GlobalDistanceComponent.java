package me.basiqueevangelist.worldspecificviewdistance.component;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;

public class GlobalDistanceComponent implements Component {
    public int globalViewDistance = 0;
    public int globalSimulationDistance = 0;

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.getInt("GlobalViewDistance").ifPresent(integer -> this.globalViewDistance = integer);
        tag.getInt("GlobalSimulationDistance").ifPresent(integer -> this.globalSimulationDistance = integer);
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("GlobalViewDistance", globalViewDistance);
        tag.putInt("GlobalSimulationDistance", globalSimulationDistance);
    }
}
