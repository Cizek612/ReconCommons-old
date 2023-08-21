package net.recondev.commons.utils;

import net.recondev.commons.builders.ItemBuilder;
import org.bukkit.configuration.file.FileConfiguration;

public class ItemUtils {

    public static ItemBuilder getItemFromConfig(final FileConfiguration config, final String path) {
        return new ItemBuilder(config.getString(path + ".Material", "BARRIER"), 1).setDurability(config.get(path + ".Durability", 0)).setGlow(config.getBoolean(path + ".Glow", false)).setName(config.getString(path + ".Name", "Invalid Name")).setLore(config.getStringList(path + ".Lore")).setSkull(config.getString(path + ".Skull", "")).setAmount(config.getInt(path + ".Amount", 1)).setColor(config.getInt(path + ".Color", 5));
    }

}
