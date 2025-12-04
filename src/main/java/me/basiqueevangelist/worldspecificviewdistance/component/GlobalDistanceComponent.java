package me.basiqueevangelist.worldspecificviewdistance.component;

import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

import org.ladysnake.cca.api.v3.component.Component;

public class GlobalDistanceComponent implements Component {
    public int globalViewDistance = 0;
    public int globalSimulationDistance = 0;

    @Override
    public void readData(ReadView readView) {
        readView.getOptionalInt("GlobalViewDistance").ifPresent(integer -> this.globalViewDistance = integer);
        readView.getOptionalInt("GlobalSimulationDistance").ifPresent(integer -> this.globalSimulationDistance = integer);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putInt("GlobalViewDistance", globalViewDistance);
        writeView.putInt("GlobalSimulationDistance", globalSimulationDistance);
    }
}
