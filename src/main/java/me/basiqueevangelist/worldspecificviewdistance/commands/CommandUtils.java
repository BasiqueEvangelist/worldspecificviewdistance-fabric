package me.basiqueevangelist.worldspecificviewdistance.commands;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.world.dimension.DimensionType;

public class CommandUtils {
    public static LiteralText getMessage(String format, Object... args) {
        return new LiteralText(String.format(format, args));
    }

    public static String getRegistryId(MinecraftServer s, DimensionType dim) {
        try {
            return s.getRegistryManager().getDimensionTypes().getId(dim).toString();
        } catch (NullPointerException npe) {
            return "<couldn't get dimension id due to NPE>";
        }
    }
}
