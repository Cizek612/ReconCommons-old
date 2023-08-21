package net.recondev.commons.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@SuppressWarnings("unused")
public class YMLConfig extends YamlConfiguration {
    private final File file;

    public YMLConfig(final File file) {
        this.file = file;
    }

    public static YMLConfig loadConfiguration(final File file) {
        try {
            final YMLConfig config = new YMLConfig(file);
            config.load(file);
            return config;
        } catch (final Throwable var2) {
            var2.printStackTrace();
            throw new NullPointerException();
        }
    }
    public void save() {
        try {
            this.save(this.file);
        } catch (final Throwable ignored) {}
    }

    public ConfigurationSection getConfigurationSection(final String path) {
        return super.getConfigurationSection(path);
    }
}