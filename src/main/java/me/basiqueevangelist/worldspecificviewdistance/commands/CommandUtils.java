package me.basiqueevangelist.worldspecificviewdistance.commands;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class CommandUtils {
    public static Text getMessage(String format, Object... args) {
        return Text.literal(String.format(format, args));
    }

    public static String getRegistryId(ServerWorld dim) {
        try {
            return dim.getDimensionEntry().getKey().orElseThrow().getValue().toString();
        } catch (Exception e) {
            return "<couldn't get dimension id due to exception: " + e + ">";
        }
    }
}
