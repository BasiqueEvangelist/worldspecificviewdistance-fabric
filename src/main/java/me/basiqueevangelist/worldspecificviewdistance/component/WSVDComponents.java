package me.basiqueevangelist.worldspecificviewdistance.component;

import net.minecraft.resources.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v8.leveldata.LevelDataComponentFactoryRegistry;
import org.ladysnake.cca.api.v8.leveldata.LevelDataComponentInitializer;

public class WSVDComponents implements LevelDataComponentInitializer {
    public static final ComponentKey<GlobalDistanceComponent> GLOBAL_DISTANCE
        = ComponentRegistry.getOrCreate(
            Identifier.parse("worldspecificviewdistance:global_distance"),
            GlobalDistanceComponent.class
        );

    @Override
    public void registerLevelDataComponentFactories(LevelDataComponentFactoryRegistry registry) {
        registry.register(GLOBAL_DISTANCE, _ -> new GlobalDistanceComponent());
    }
}
