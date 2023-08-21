package net.recondev.commons.builders.improved;

import net.recondev.commons.builders.PlaceholderReplacer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ItemUtils {

    public static ItemBuilder getItem(final FileConfiguration config, final String path) {
        return new ItemBuilder(config.get(path + ".Material", "DIAMOND"), 1)
                .setDurability(config.get(path + ".Durability", 0))
                .setGlow(config.getBoolean(path + ".Glow", false))
                .setName(config.getString(path + ".Name", "Name Error"))
                .setLore(config.getStringList(path + ".Lore"))
                .setSkull(config.getString(path + ".Skull", ""))
                .setAmount(config.getInt(path + ".Amount", 1))
                .setPotionType(config.getString(path + ".Potion-Type"))
                .setEnchantments(config.getStringList(path + ".Enchantments"))
                .setHideItemFlags(config.getBoolean(path + ".Hide-Item-Flags", false))
                .setCustomModelData(config.getInt(path + ".Custom-Model-Data", 0));
    }

    public static ItemBuilder getItem(final FileConfiguration config, final String path, final PlaceholderReplacer placeholderReplacer) {
        return new ItemBuilder(config.get(path + ".Material", "DIAMOND"), 1)
                .setDurability(config.get(path + ".Durability", 0))
                .setGlow(config.getBoolean(path + ".Glow", false))
                .setName(config.getString(path + ".Name", "Name Error"))
                .setLore(placeholderReplacer.parse(config.getStringList(path + ".Lore")))
                .setSkull(config.getString(path + ".Skull", ""))
                .setAmount(config.getInt(path + ".Amount", 1))
                .setPotionType(config.getString(path + ".Potion-Type"))
                .setEnchantments(config.getStringList(path + ".Enchantments"))
                .setHideItemFlags(config.getBoolean(path + ".Hide-Item-Flags", false))
                .setCustomModelData(config.getInt(path + ".Custom-Model-Data", 0));
    }

    public static ItemBuilder getItem(final ConfigurationSection section) {
        return new ItemBuilder(section.getString("Material", "AIR"), 1)
                .setDurability(section.getInt("Durability"))
                .setGlow(section.getBoolean("Glow"))
                .setName(section.getString("Name", "???"))
                .setLore(section.getStringList("Lore"))
                .setSkull(section.getString(".Skull", ""))
                .setAmount(section.getInt(".Amount", 1))
                .setPotionType(section.getString(".Potion-Type"))
                .setCustomModelData(section.getInt("Custom-Model-Data", 0))
                .setEnchantments(section.getStringList("Enchantments"))
                .setHideItemFlags(section.getBoolean("Hide-Item-Flags"));
    }
}
