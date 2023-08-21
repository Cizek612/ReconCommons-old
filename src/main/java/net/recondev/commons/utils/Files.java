package net.recondev.commons.utils;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@SuppressWarnings("unused")
public class Files {

    public static File file(final String name, final JavaPlugin plugin) {

        final File file = new File(plugin.getDataFolder().getAbsoluteFile() + "/" + name);

        if (!file.exists()) {
            file.getParentFile().mkdir();
            plugin.saveResource(name, false);
        }

        return file;
    }

    public static File file(final String directory, final String name, final JavaPlugin plugin) {

        final File file = new File(plugin.getDataFolder().getAbsoluteFile() + "/" + directory + "/" + name);

        if (!file.exists()) {
            file.getParentFile().mkdir();
            plugin.saveResource(name, false);
        }

        return file;
    }
}
