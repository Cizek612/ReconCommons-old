package net.recondev.commons.builders;


import net.recondev.commons.support.XMaterial;
import net.recondev.commons.utils.ColorUtil;
import net.recondev.commons.utils.MathUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@SuppressWarnings("unused")
@Deprecated
public class ItemBuilder {
    private final ItemStack item;

    private Integer amount;

    private String skullOwner;

    private String name;

    private List<String> lore;

    private boolean glow;


    private Color color;

    private boolean hideFlags;

    private List<Integer> durability;

    public List<Integer> getDurability() {
        return this.durability;
    }

    public ItemBuilder(final String material, final Integer amount) {
        Material parsedMaterial;
        try {
            parsedMaterial = XMaterial.matchXMaterial(material).get().parseItem().getType();
        } catch (final Exception e) {
            parsedMaterial = XMaterial.DIAMOND.parseMaterial();
        }
        this.item = new ItemStack(parsedMaterial, amount);
    }

    public ItemBuilder(final Material material, final Integer amount) {
        this.item = new ItemStack(material, amount);
    }

    public ItemBuilder(final ItemStack item, final Integer amount, final String skullOwner, final String name, final List<String> lore, final boolean glow) {
        this.item = item;
        this.amount = amount;
        this.skullOwner = skullOwner;
        this.name = name;
        this.lore = lore;
        this.glow = glow;
    }

    public ItemBuilder clone() {
        return new ItemBuilder(this.item, this.amount, this.skullOwner, this.name, this.lore, this.glow);
    }

    public ItemBuilder setAmount(final int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setDurability(final Object dur) {
        if (dur instanceof Integer)
            this.item.setDurability((short)((Integer)dur).intValue());
        if (dur instanceof Short)
            this.item.setDurability((Short) dur);
        if (dur instanceof List)
            this.durability = (List<Integer>)dur;
        return this;
    }

    public ItemBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder setLore(final List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder setGlow(final boolean bool) {
        this.glow = bool;
        return this;
    }


    public ItemBuilder setHideFlags(final boolean hideFlags) {
        this.hideFlags = hideFlags;
        return this;
    }

    public ItemBuilder setColor(final int color) {
        this.color = Color.fromRGB(color);
        return this;
    }

    public boolean isSkull() {
        return this.item.getType().toString().equals("SKULL_ITEM") || this.item.getType().toString().equals("PLAYER_HEAD");
    }

    public ItemBuilder setSkull(final String id) {
        this.skullOwner = id;
        return this;
    }

    public ItemStack parseSkull(final ItemStack item, final String owner) {
        if (!isSkull())
            return item;
        final SkullMeta itemMeta = (SkullMeta)item.getItemMeta();
        final GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", owner));
        Field profileField;
        try {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        } catch (final IllegalArgumentException|IllegalAccessException|NoSuchFieldException|SecurityException e) {
            e.printStackTrace();
        }
        if (owner.length() <= 16)
            itemMeta.setOwner(owner);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack parse(final PlaceholderReplacer replacer) {
        if (replacer == null)
            return parse();
        final List<String> itemLore = new ArrayList<>();
        this.lore.forEach(line -> itemLore.add(getColor(replacer.parse(line))));
        ItemStack parsed = new ItemStack(this.item.getType(), (this.amount == null) ? this.item.getAmount() : this.amount);
        parsed.setDurability(this.item.getDurability());
        if (isSkull() && this.skullOwner != null && !this.skullOwner.isEmpty() && !this.skullOwner.equals(" "))
            parsed = parseSkull(parsed, this.skullOwner);
        ItemMeta itemMeta = parsed.getItemMeta();
        itemMeta.setDisplayName(getColor(replacer.parse(this.name)));
        itemMeta.setLore(itemLore);
        if (this.hideFlags)
            itemMeta.addItemFlags(ItemFlag.values());
        parsed.setItemMeta(itemMeta);
        if (this.glow) {
            parsed.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            itemMeta = parsed.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            parsed.setItemMeta(itemMeta);
        }
        try {
            final LeatherArmorMeta armorMeta = (LeatherArmorMeta)parsed.getItemMeta();
            armorMeta.setColor(this.color);
            parsed.setItemMeta(armorMeta);
        } catch (final Exception ignored) {}
        if (this.skullOwner != null)
            parsed = parseSkull(parsed, replacer.parse(this.skullOwner));
        if (this.durability == null || this.durability.isEmpty())
            return parsed;
        parsed.setDurability((short) MathUtils.getRandom(this.durability).intValue());
        return parsed;
    }

    public ItemStack parse() {
        final List<String> itemLore = new ArrayList<>();
        this.lore.forEach(line -> itemLore.add(getColor(line)));
        ItemStack parsed = new ItemStack(this.item.getType(), (this.amount == null) ? this.item.getAmount() : this.amount);
        parsed.setDurability(this.item.getDurability());
        ItemMeta itemMeta = parsed.getItemMeta();
        itemMeta.setDisplayName(getColor(this.name));
        itemMeta.setLore(itemLore);
        if (this.hideFlags)
            itemMeta.addItemFlags(ItemFlag.values());
        parsed.setItemMeta(itemMeta);
        if (this.glow) {
            parsed.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            itemMeta = parsed.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            parsed.setItemMeta(itemMeta);
        }
        try {
            final LeatherArmorMeta armorMeta = (LeatherArmorMeta)parsed.getItemMeta();
            armorMeta.setColor(this.color);
            parsed.setItemMeta(armorMeta);
        } catch (final Exception ignored) {}
        if (this.skullOwner != null)
            parsed = parseSkull(parsed, this.skullOwner);
        if (this.durability == null || this.durability.isEmpty())
            return parsed;
        parsed.setDurability((short)MathUtils.getRandom(this.durability).intValue());
        return parsed;
    }

    public ItemStack update(final ItemStack item, final PlaceholderReplacer replacer) {
        final List<String> itemLore = new ArrayList<>();
        this.lore.forEach(line -> itemLore.add(getColor(replacer.parse(line))));
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(getColor(replacer.parse(this.name)));
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack update(final ItemStack item) {
        final List<String> itemLore = new ArrayList<>();
        this.lore.forEach(line -> itemLore.add(getColor(line)));
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(getColor(this.name));
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public String getColor(final String args) {
        return ColorUtil.colorize(args);
    }
}
