package net.recondev.commons.abstracts;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public abstract class ReconListener<T extends JavaPlugin> implements Listener {

    private final T plugin;

    public ReconListener(final T plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


}