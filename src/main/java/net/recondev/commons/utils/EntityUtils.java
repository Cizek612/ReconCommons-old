package net.recondev.commons.utils;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityUtils {

    public static Entity spawnMobFromConfig(final FileConfiguration config, final String path, final Location location) {
        final Entity entity = location.getWorld().spawnEntity(location, EntityType.valueOf(config.getString(path + ".Type")));
        entity.setCustomName(ColorUtil.colorize(config.getString(path + ".Name")));
        final LivingEntity mob = (LivingEntity)entity;
        mob.setHealth(config.getDouble(path + ".Health"));
        final ItemStack helmet = ItemUtils.getItemFromConfig(config, path + ".Equipment.HELMET").parse();
        final ItemStack chestplate = ItemUtils.getItemFromConfig(config, path + ".Equipment.CHESTPLATE").parse();
        final ItemStack leggings = ItemUtils.getItemFromConfig(config, path + ".Equipment.LEGGINGS").parse();
        final ItemStack boots = ItemUtils.getItemFromConfig(config, path + ".Equipment.BOOTS").parse();
        final ItemStack heldItem = ItemUtils.getItemFromConfig(config, path + ".Held-Item").parse();
        for (final String enchantString : config.getStringList(path + ".Equipment.Enchantments")) {
            final String[] enchantSplit = enchantString.split(":");
            final Enchantment enchant = Enchantment.getByName(enchantSplit[0]);
            final int level = Integer.parseInt(enchantSplit[1]);
            helmet.addUnsafeEnchantment(enchant, level);
            chestplate.addUnsafeEnchantment(enchant, level);
            leggings.addUnsafeEnchantment(enchant, level);
            boots.addUnsafeEnchantment(enchant, level);
        }
        for (final String enchantString : config.getStringList(path + ".Held-Item.Enchantments")) {
            final String[] enchantSplit = enchantString.split(":");
            final Enchantment enchant = Enchantment.getByName(enchantSplit[0]);
            final int level = Integer.parseInt(enchantSplit[1]);
            heldItem.addUnsafeEnchantment(enchant, level);
        }
        for (final String effectString : config.getStringList(path + ".Effects")) {
            final String[] effectSplit = effectString.split(":");
            final PotionEffectType effect = PotionEffectType.getByName(effectSplit[0]);
            final int level = Integer.parseInt(effectSplit[1]);
            mob.addPotionEffect(new PotionEffect(effect, 999999999, level));
        }
        mob.getEquipment().setHelmet(helmet);
        mob.getEquipment().setChestplate(chestplate);
        mob.getEquipment().setLeggings(leggings);
        mob.getEquipment().setBoots(boots);
        mob.getEquipment().setItemInHand(heldItem);
        return mob;
    }

}
