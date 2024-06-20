package me.basiqueevangelist.worldspecificviewdistance.component;

import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.level.LevelComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.level.LevelComponentInitializer;

public class WSVDComponents implements LevelComponentInitializer {
    public static final ComponentKey<GlobalDistanceComponent> GLOBAL_DISTANCE
        = ComponentRegistry.getOrCreate(
            Identifier.of("worldspecificviewdistance:global_distance"),
            GlobalDistanceComponent.class
        );

    @Override
    public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
        registry.register(GLOBAL_DISTANCE, unused -> new GlobalDistanceComponent());
    }
}
