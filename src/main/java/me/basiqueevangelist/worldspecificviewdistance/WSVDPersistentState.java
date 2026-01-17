package me.basiqueevangelist.worldspecificviewdistance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class WSVDPersistentState extends SavedData {
    public static final String ID = "worldspecificviewdistance";
    public static final SavedDataType<WSVDPersistentState> TYPE = new SavedDataType<>(
            ID, WSVDPersistentState::new,
        Packed.CODEC.xmap(WSVDPersistentState::unpackState, WSVDPersistentState::pack), DataFixTypes.LEVEL
    );

    private int localViewDistance;
    private int localSimulationDistance;

    public static WSVDPersistentState getFrom(ServerLevel w) {
        return getFrom(w.getDataStorage());
    }

    public static WSVDPersistentState getFrom(DimensionDataStorage mgr) {
        return mgr.computeIfAbsent(TYPE);
    }

    public int getLocalViewDistance() {
        return localViewDistance;
    }

    public int getLocalSimulationDistance() {
        return localSimulationDistance;
    }

    public void setLocalViewDistance(int viewDistance) {
        if (viewDistance != localViewDistance) {
            localViewDistance = viewDistance;
        }
    }

    public void setLocalSimulationDistance(int localSimulationDistance) {
        this.localSimulationDistance = localSimulationDistance;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    private static WSVDPersistentState unpackState(WSVDPersistentState.Packed packedState) {
        var state = new WSVDPersistentState();
        state.localViewDistance = packedState.localViewDistance;
        state.localSimulationDistance = packedState.localSimulationDistance;
        return state;
    }

    private static WSVDPersistentState.Packed pack(WSVDPersistentState state) {
        return new Packed(state.localViewDistance, state.localSimulationDistance);
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
