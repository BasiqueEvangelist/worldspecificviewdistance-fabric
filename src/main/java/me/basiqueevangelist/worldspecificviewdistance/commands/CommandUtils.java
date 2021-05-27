package me.basiqueevangelist.worldspecificviewdistance.commands;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

public class CommandUtils {
    public static LiteralText getMessage(String format, Object... args) {
        return new LiteralText(String.format(format, args));
    }

    public static String getRegistryId(MinecraftServer s, DimensionType dim) {
        try {
            return s.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY).getId(dim).toString();
        } catch (Exception e) {
            return "<couldn't get dimension id due to exception: " + e + ">";
        }
    }
}
