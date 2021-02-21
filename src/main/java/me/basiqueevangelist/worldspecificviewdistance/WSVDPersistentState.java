package me.basiqueevangelist.worldspecificviewdistance;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

public class WSVDPersistentState extends PersistentState {
    public static String ID = "worldspecificviewdistance";

    private int localViewDistance;

    public WSVDPersistentState() {
        super(ID);
    }

    public static WSVDPersistentState getFrom(ServerWorld w) {
        return getFrom(w.getPersistentStateManager());
    }

    public static WSVDPersistentState getFrom(PersistentStateManager mgr) {
        return mgr.getOrCreate(WSVDPersistentState::new, ID);
    }

    public int getLocalViewDistance() {
        return localViewDistance;
    }

    public void setLocalViewDistance(int viewDistance) {
        if (viewDistance != localViewDistance) {
            localViewDistance = viewDistance;
        }
    }

    @Override
    public void fromTag(CompoundTag tag) {
        localViewDistance = tag.getInt("LocalViewDistance");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("LocalViewDistance", localViewDistance);
        return tag;
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}
