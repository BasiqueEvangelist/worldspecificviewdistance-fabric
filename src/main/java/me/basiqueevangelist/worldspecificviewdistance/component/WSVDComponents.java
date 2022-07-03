package me.basiqueevangelist.worldspecificviewdistance.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import net.minecraft.util.Identifier;

public class WSVDComponents implements LevelComponentInitializer {
    public static final ComponentKey<GlobalDistanceComponent> GLOBAL_DISTANCE
        = ComponentRegistry.getOrCreate(
            new Identifier("worldspecificviewdistance:global_distance"),
            GlobalDistanceComponent.class
        );

    @Override
    public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
        registry.register(GLOBAL_DISTANCE, unused -> new GlobalDistanceComponent());
    }
}
