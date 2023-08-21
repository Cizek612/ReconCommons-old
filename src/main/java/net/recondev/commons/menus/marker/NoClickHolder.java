package net.recondev.commons.menus.marker;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class NoClickHolder implements InventoryHolder {
    @NotNull
    public Inventory getInventory() {
        return null;
    }
}
