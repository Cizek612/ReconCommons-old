package net.recondev.commons.menus.action;

import net.recondev.commons.utils.ColorUtil;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickRunnable {
    void click(final InventoryClickEvent var1);

    default void clickCooldown(final InventoryClickEvent event) {
        event.getWhoClicked().sendMessage(ColorUtil.colorize("&cYou can't click this yet!"));
    }
}
