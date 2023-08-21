package net.recondev.commons.builders.improved;


import net.recondev.commons.builders.PlaceholderReplacer;
import net.recondev.commons.support.XEnchantment;
import net.recondev.commons.support.XMaterial;
import net.recondev.commons.utils.ColorUtil;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings({"unused"})
public class ItemBuilder {

    private final ItemStack item;
    private List<XMaterial> materials;
    private Integer amount;
    private String skullOwner;
    private String potionType;
    private String name;
    private List<String> lore;
    private boolean glow;
    private Map<String, String> nbtTags;

    private List<String> enchantments;
    private boolean hideItemFlags;

    @Getter
    private List<Integer> durability;
    @Getter
    private int customModelData;

    public ItemBuilder(ItemStack itemStack) {
        item = itemStack.clone();
    }

    public ItemBuilder(String material, Integer amount) {
        Material parsedMaterial;
        try {
            parsedMaterial = XMaterial.matchXMaterial(material).get().parseItem().getType();
        } catch (Exception e) {
            parsedMaterial = XMaterial.DIAMOND.parseMaterial();
        }


        item = new ItemStack(parsedMaterial, amount);
    }

    public ItemBuilder(Object material, Integer amount) {
        materials = new ArrayList<>();
        this.amount = amount;
        if (material instanceof String) {
            XMaterial parsedMaterial = XMaterial.matchXMaterial(material.toString()).get();
            if (parsedMaterial == null) {
                parsedMaterial = XMaterial.DIAMOND;
            }

            materials.add(parsedMaterial);
        } else if (material instanceof List) {
            List<String> materialList = (List<String>) material;
            for (String materialString : materialList) {
                XMaterial parsedMaterial = XMaterial.matchXMaterial(materialString).get();
                if (parsedMaterial == null) {
                    parsedMaterial = XMaterial.DIAMOND;
                }

                materials.add(parsedMaterial);
            }
        }

        if (materials.isEmpty()) {
            this.item = XMaterial.DIAMOND.parseItem();
        } else {
            this.item = materials.get(ThreadLocalRandom.current().nextInt(0, materials.size())).parseItem();
        }
        this.item.setAmount(amount);
    }

    public ItemBuilder(Material material, Integer amount) {
        item = new ItemStack(material, amount);
    }

    public ItemBuilder(ItemStack item, Integer amount, int customModelData, String potionType, String skullOwner, String name, List<String> lore, boolean glow, Map<String, String> nbtTags) {
        this.item = item;
        this.amount = amount;
        this.customModelData = customModelData;
        this.skullOwner = skullOwner;
        this.name = name;
        this.lore = lore;
        this.glow = glow;
        this.nbtTags = nbtTags;
    }

    public ItemBuilder clone() {
        return new ItemBuilder(item, amount, customModelData, potionType, skullOwner, name, lore, glow, nbtTags);
    }

    public ItemBuilder setCustomModelData(int data) {
        this.customModelData = data;
        return this;
    }

    public ItemBuilder setHideItemFlags(boolean bool) {
        this.hideItemFlags = bool;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setPotionType(final String potionType) {
        this.potionType = potionType;
        return this;
    }

    public ItemBuilder setDurability(Object dur) {
        if (dur instanceof Integer) {
            item.setDurability((short) (int) dur);
        }
        if (dur instanceof Short) {
            item.setDurability((short) dur);
        }
        if (dur instanceof List) {
            durability = (List<Integer>) dur;
        }
        return this;
    }

    public ItemBuilder addEnchantment(String enchantment) {
        if (enchantments == null) enchantments = new ArrayList<>();
        enchantments.add(enchantment);
        return this;
    }

    public ItemBuilder setEnchantments(List<String> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder setGlow(boolean bool) {
        this.glow = bool;
        return this;
    }

    public ItemBuilder addNBTTag(String key, String value) {
        if (nbtTags == null) nbtTags = new HashMap<>();
        nbtTags.put(key, value);
        return this;
    }

    public boolean isSkull() {
        return item.getType().toString().equals("SKULL_ITEM") || item.getType().toString().equals("PLAYER_HEAD");
    }

    public ItemBuilder setSkull(String id) {
        skullOwner = id;
        return this;
    }

    public ItemStack parseSkull(ItemStack item, String owner) {
        if (!(isSkull())) return item;

        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", owner));
        Field profileField = null;
        try {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        if (owner.length() <= 16) {
            itemMeta.setOwner(owner);
        }

        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack parse(PlaceholderReplacer replacer) {

        if (replacer == null) return this.parse();

        final List<String> itemLore = new LinkedList<>();

        for (final String line : this.lore) {
            itemLore.add(getColor(replacer.parse(line)));
        }

        ItemStack parsed = new ItemStack(this.item.getType(), (this.amount == null ? this.item.getAmount() : this.amount));

        if (!(this.materials == null || this.materials.isEmpty())) {
            parsed = this.materials.get(ThreadLocalRandom.current().nextInt(0, materials.size())).parseItem();
            parsed.setAmount((this.amount == null ? this.item.getAmount() : this.amount));
        }

        if (this.enchantments != null) {
            for (final String enchantment : enchantments) {
                final String[] split = enchantment.split(":");
                final int amplifier = Integer.parseInt(split[1]);

                final Optional<XEnchantment> xEnchantment = XEnchantment.matchXEnchantment(enchantment);

                if (xEnchantment.isPresent()) {
                    parsed.addUnsafeEnchantment(Objects.requireNonNull(xEnchantment.get().getEnchant()), amplifier);
                }
            }
        }

        parsed.setDurability(this.item.getDurability());
        if (isSkull() && (!(this.skullOwner == null || this.skullOwner.isEmpty() || this.skullOwner.equals(" "))))
            parsed = parseSkull(parsed, this.skullOwner);

        ItemMeta itemMeta = parsed.getItemMeta();

        itemMeta.setDisplayName(getColor(replacer.parse(this.name)));
        itemMeta.setLore(itemLore);

        if (this.hideItemFlags) itemMeta.addItemFlags(ItemFlag.values());

        parsed.setItemMeta(itemMeta);

        if (this.glow) {
            parsed.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            itemMeta = parsed.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            parsed.setItemMeta(itemMeta);
        }


        if (this.skullOwner != null) parsed = parseSkull(parsed, this.skullOwner);

        ItemMeta newItemMeta = parsed.getItemMeta();

        try {
            newItemMeta.setCustomModelData(this.customModelData);
        } catch (NoSuchMethodError ignored) {
        }

        parsed.setItemMeta(newItemMeta);

        if (this.durability == null || this.durability.isEmpty()) return parsed;
        parsed.setDurability((short) (int) this.durability.get(ThreadLocalRandom.current().nextInt(0, durability.size())));
        return applyPotionMeta(parsed);
    }

    public ItemStack applyPotionMeta(ItemStack itemStack) {
        try {
            if (this.potionType == null || this.potionType.isEmpty()) return itemStack;

            PotionType potionBase = PotionType.valueOf(this.potionType.toUpperCase());

            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
            potionMeta.setBasePotionData(new PotionData(potionBase));

            itemStack.setItemMeta(potionMeta);
        } catch (Exception ignored) {
        }
        return itemStack;
    }

    public ItemStack parse() {
        return parse(new PlaceholderReplacer());
    }

    public String getColor(final String args) {
        return ColorUtil.colorize(args);
    }
}