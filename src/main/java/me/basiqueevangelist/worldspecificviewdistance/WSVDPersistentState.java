package me.basiqueevangelist.worldspecificviewdistance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.PersistentStateType;

public class WSVDPersistentState extends PersistentState {
    public static final String ID = "worldspecificviewdistance";
    public static final PersistentStateType<WSVDPersistentState> TYPE = new PersistentStateType<>(
            ID, context -> new WSVDPersistentState(), context -> {
        var state = new WSVDPersistentState();
        return WSVDPersistentState.Packed.CODEC.xmap(state::unpackState, WSVDPersistentState::pack);
    }, DataFixTypes.LEVEL
    );

    private int localViewDistance;
    private int localSimulationDistance;

    public static WSVDPersistentState getFrom(ServerWorld w) {
        return getFrom(w.getPersistentStateManager());
    }

    public static WSVDPersistentState getFrom(PersistentStateManager mgr) {
        return mgr.getOrCreate(TYPE);
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

    private WSVDPersistentState unpackState(WSVDPersistentState.Packed packedState) {
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
