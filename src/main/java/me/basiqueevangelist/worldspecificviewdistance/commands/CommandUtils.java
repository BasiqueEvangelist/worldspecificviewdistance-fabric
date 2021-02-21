package me.basiqueevangelist.worldspecificviewdistance.commands;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.world.dimension.DimensionType;

import java.util.Objects;

public class CommandUtils {
    public static LiteralText getMessage(String format, Object... args) {
        return new LiteralText(String.format(format, args));
    }

    public static String getRegistryId(MinecraftServer s, DimensionType dim) {
        return Objects.requireNonNull(s.getRegistryManager().getDimensionTypes().getId(dim)).toString();
    }
}
