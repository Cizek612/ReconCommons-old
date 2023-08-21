package net.recondev.commons.utils;


import net.recondev.commons.command.CommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")

public class CommandUtils {

    private static SimpleCommandMap simpleCommandMap;

    private static void setupSimpleCommandMap() {
        final SimplePluginManager simplePluginManager = (SimplePluginManager)Bukkit.getServer().getPluginManager();
        Field field = null;
        try {
            field = SimplePluginManager.class.getDeclaredField("commandMap");
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        field.setAccessible(true);
        try {
            simpleCommandMap = (SimpleCommandMap)field.get(simplePluginManager);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Command> getKnownCommands() {
        try {
            final Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);
            return (Map)field.get(simpleCommandMap);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void registerCommand(final CommandBuilder command) {
        simpleCommandMap.register("Recon", command);
    }

    public static void unregisterCommand(final CommandBuilder command) {
        command.unregister(simpleCommandMap);
        final HashMap<String, Command> knownCommands = new HashMap<>(CommandUtils.getKnownCommands());
        for (final Map.Entry entry : knownCommands.entrySet()) {
            if (!((String)entry.getKey()).equalsIgnoreCase(command.getName())) continue;
            CommandUtils.getKnownCommands().remove(entry.getKey());
        }
    }

    static {
        CommandUtils.setupSimpleCommandMap();
    }
}
