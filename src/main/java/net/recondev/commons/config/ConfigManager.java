package net.recondev.commons.config;

import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ConfigManager<T extends JavaPlugin> {
    private final T plugin;
    private final Map<String, YMLConfig> cachedConfigurationMap = new HashMap<>();

    public void loadConfiguration(final String file) {
        final File fileToLoad = new File(this.plugin.getDataFolder(), file + ".yml");
        if (!fileToLoad.exists()) {
            this.plugin.saveResource(file + ".yml", true);
        }
        File loadFile = new File(this.plugin.getDataFolder(), file + ".yml");
        System.out.println(loadFile);
        final YMLConfig configuration = YMLConfig.loadConfiguration(loadFile);
        this.cachedConfigurationMap.put(file, configuration);
    }

    public void loadConfiguration(final String... files) {
        int var3 = files.length;

        for (final String file : files) {
            this.loadConfiguration(file);
        }
    }

    public YMLConfig getConfig(final String file) {
        if (!this.cachedConfigurationMap.containsKey(file)) {
            this.loadConfiguration(file);
        }

        return this.cachedConfigurationMap.get(file);
    }

    public ConfigManager(final T plugin) {
        this.plugin = plugin;
    }

    public T getPlugin() {
        return this.plugin;
    }

    public Map<String, YMLConfig> getCachedConfigurationMap() {
        return this.cachedConfigurationMap;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ConfigManager)) {
            return false;
        } else {
            final ConfigManager<?> other = (ConfigManager) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                final Object this$plugin = this.getPlugin();
                final Object other$plugin = other.getPlugin();
                if (this$plugin == null) {
                    if (other$plugin != null) {
                        return false;
                    }
                } else if (!this$plugin.equals(other$plugin)) {
                    return false;
                }

                final Object this$cachedConfigurationMap = this.getCachedConfigurationMap();
                final Object other$cachedConfigurationMap = other.getCachedConfigurationMap();
                if (this$cachedConfigurationMap == null) {
                    return other$cachedConfigurationMap == null;
                } else return this$cachedConfigurationMap.equals(other$cachedConfigurationMap);
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ConfigManager;
    }

    public int hashCode() {
        final boolean PRIME = true;
        int result = 1;
        final Object $plugin = this.getPlugin();
        result = result * 59 + ($plugin == null ? 43 : $plugin.hashCode());
        final Object $cachedConfigurationMap = this.getCachedConfigurationMap();
        result = result * 59 + ($cachedConfigurationMap == null ? 43 : $cachedConfigurationMap.hashCode());
        return result;
    }

    public String toString() {
        return "ConfigManager(plugin=" + this.getPlugin() + ", cachedConfigurationMap=" + this.getCachedConfigurationMap() + ")";
    }
}
