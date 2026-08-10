package me.basiqueevangelist.worldspecificviewdistance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;

public class WSVDSavedData extends SavedData {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("worldspecificviewdistance", "worldspecificviewdistance");
    public static final SavedDataType<WSVDSavedData> TYPE = new SavedDataType<>(
            ID, WSVDSavedData::new,
        Packed.CODEC.xmap(WSVDSavedData::unpackData, WSVDSavedData::pack), DataFixTypes.LEVEL
    );

    private int localViewDistance;
    private int localSimulationDistance;

    public static WSVDSavedData getFrom(ServerLevel w) {
        return getFrom(w.getDataStorage());
    }

    public static WSVDSavedData getFrom(SavedDataStorage mgr) {
        return mgr.computeIfAbsent(TYPE);
    }

    public int getLocalViewDistance() {
        return this.localViewDistance;
    }

    public int getLocalSimulationDistance() {
        return this.localSimulationDistance;
    }

    public void setLocalViewDistance(int viewDistance) {
        if (viewDistance != this.localViewDistance) {
            this.localViewDistance = viewDistance;
        }
    }

    public void setLocalSimulationDistance(int localSimulationDistance) {
        this.localSimulationDistance = localSimulationDistance;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    private static WSVDSavedData unpackData(WSVDSavedData.Packed packedData) {
        var state = new WSVDSavedData();
        state.localViewDistance = packedData.localViewDistance;
        state.localSimulationDistance = packedData.localSimulationDistance;
        return state;
    }

    private static WSVDSavedData.Packed pack(WSVDSavedData data) {
        return new Packed(data.localViewDistance, data.localSimulationDistance);
    }

    public record Packed(
            int localViewDistance, int localSimulationDistance
    ) {
        public static final Codec<Packed> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                Codec.INT.fieldOf("local_view_distance").forGetter(Packed::localViewDistance),
                                Codec.INT.fieldOf("local_simulation_distance").forGetter(Packed::localSimulationDistance)
                        )
                        .apply(instance, Packed::new)
        );
    }
}
