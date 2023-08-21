package net.recondev.commons.menus;

import java.util.List;

import net.recondev.commons.menus.action.ClickAction;
import net.recondev.commons.menus.action.CloseRunnable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public interface Menu extends Listener {
    ItemStack getItemAt(final int var1);

    default ItemStack getItemAt(final int x, final int y) {
        return this.getItemAt(MenuUtils.getSlotFromCartCoords(x, y));
    }

    ItemStack setItemAt(final int var1, final ItemStack var2);

    default ItemStack setItemAt(final int x, final int y, final ItemStack set) {
        return this.setItemAt(MenuUtils.getSlotFromCartCoords(x, y), set);
    }

    String getTitle();

    void setTitle(final String var1);

    int getSize();

    void setSize(final int var1);

    ItemStack[] getContents();

    void setContents(final ItemStack[] var1);

    boolean canClick();

    void setClickable(final boolean var1);

    boolean canExpire();

    void setCanExpire(final boolean var1);

    boolean isValid();

    void updateViewers();

    List<ClickAction> getActionsAt(final int var1);

    default List<ClickAction> getActionAt(final int x, final int y) {
        return this.getActionsAt(MenuUtils.getSlotFromCartCoords(x, y));
    }

    void addClickAction(int var1, ClickAction var2);

    void clearClickActions(int var1);

    void onClose(CloseRunnable var1);

    Inventory getGUI();

    default void shutdown() {
        this.getViewers().forEach(HumanEntity::closeInventory);
    }

    List<Player> getViewers();

    Menu clone();
}
