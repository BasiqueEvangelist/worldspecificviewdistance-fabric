package me.basiqueevangelist.worldspecificviewdistance;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

public class WSVDPersistentState extends PersistentState {
    public static String ID = "worldspecificviewdistance";

    private int localViewDistance;
    private int localSimulationDistance;

    public static WSVDPersistentState getFrom(ServerWorld w) {
        return getFrom(w.getPersistentStateManager());
    }

    public static WSVDPersistentState getFrom(PersistentStateManager mgr) {
        return mgr.getOrCreate(WSVDPersistentState::fromNbt, WSVDPersistentState::new, ID);
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

    public static WSVDPersistentState fromNbt(NbtCompound tag) {
        WSVDPersistentState state = new WSVDPersistentState();
        state.localViewDistance = tag.getInt("LocalViewDistance");
        state.localSimulationDistance = tag.getInt("LocalSimulationDistance");
        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putInt("LocalViewDistance", localViewDistance);
        tag.putInt("LocalSimulationDistance", localSimulationDistance);
        return tag;
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}
