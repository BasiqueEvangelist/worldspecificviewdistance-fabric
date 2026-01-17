package me.basiqueevangelist.worldspecificviewdistance.component;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.Component;

public class GlobalDistanceComponent implements Component {
    public int globalViewDistance = 0;
    public int globalSimulationDistance = 0;

    @Override
    public void readData(ValueInput readView) {
        readView.getInt("GlobalViewDistance").ifPresent(integer -> this.globalViewDistance = integer);
        readView.getInt("GlobalSimulationDistance").ifPresent(integer -> this.globalSimulationDistance = integer);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putInt("GlobalViewDistance", globalViewDistance);
        writeView.putInt("GlobalSimulationDistance", globalSimulationDistance);
    }
}
