package me.basiqueevangelist.worldspecificviewdistance.commands;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class CommandUtils {
    public static Component getMessage(String format, Object... args) {
        return Component.literal(String.format(format, args));
    }

    public static String getRegistryId(ServerLevel dim) {
        try {
            return dim.dimensionTypeRegistration().unwrapKey().orElseThrow().identifier().toString();
        } catch (Exception e) {
            return "<couldn't get dimension id due to exception: " + e + ">";
        }
    }
}
