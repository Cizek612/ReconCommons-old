package net.recondev.commons.menus;


import net.recondev.commons.ReconCommons;
import net.recondev.commons.menus.action.ClickAction;
import net.recondev.commons.menus.action.CloseRunnable;
import net.recondev.commons.menus.marker.NoClickHolder;
import net.recondev.commons.utils.ColorUtil;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@SuppressWarnings("unused")
public abstract class MenuAbstract implements Menu {
    private final Map<Integer, List<ClickAction>> actionMap;
    private Inventory backingInventory;
    private String title;
    private boolean canExpire;
    private int size;
    private boolean clickable;
    private boolean valid = true;
    private boolean recreate;
    private CloseRunnable onClose;
    private final Set<Integer> currentlyTemp = new HashSet<>();

    public MenuAbstract(final String title, final int size, final boolean clickable, final boolean canExpire, final Map<Integer, List<ClickAction>> actionMap) {
        this.backingInventory = Bukkit.createInventory(clickable ? new NoClickHolder() : null, size, ColorUtil.colorize(title));
        this.canExpire = canExpire;
        this.size = size;
        this.clickable = clickable;
        this.actionMap = actionMap;
        Bukkit.getServer().getPluginManager().registerEvents(this, ReconCommons.getCommons());
        MenuManager.getInstance().addMenu(this);
    }

    @Override
    public void onClose(final CloseRunnable runnable) {
        this.onClose = runnable;
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    @Override
    public void shutdown() {
        Menu.super.shutdown();
        this.valid = false;
    }

    @Override
    public Inventory getGUI() {
        return this.backingInventory;
    }

    public void setTempItem(final int slot, final ItemStack temp, final int duration) {
        if (this.currentlyTemp.contains(slot)) {
            return;
        }
        final ItemStack oldItem = this.backingInventory.getItem(slot);
        this.backingInventory.setItem(slot, temp);
        this.currentlyTemp.add(slot);
        Bukkit.getScheduler().runTaskLater(ReconCommons.getCommons(), () -> {
            this.backingInventory.setItem(slot, oldItem);
            this.currentlyTemp.remove(slot);
        }, duration);
    }

    @Override
    public List<ClickAction> getActionsAt(final int slot) {
        return this.actionMap.get(slot);
    }

    @Override
    public void addClickAction(final int slot, final ClickAction clickAction) {
        if (this.actionMap.containsKey(slot)) {
            this.actionMap.get(slot).add(clickAction);
        } else {
            this.actionMap.put(slot, Lists.newArrayList(clickAction));
        }
    }

    @Override
    public void clearClickActions(final int slot) {
        this.actionMap.remove(slot);
    }

    @Override
    public ItemStack getItemAt(final int slot) {
        return this.backingInventory.getItem(slot);
    }

    @Override
    public ItemStack setItemAt(final int slot, final ItemStack set) {
        final ItemStack there = this.getItemAt(slot);
        this.backingInventory.setItem(slot, set);
        return there;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
        this.recreate = true;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setSize(final int size) {
        this.size = size;
        this.recreate = true;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public ItemStack[] getContents() {
        return this.backingInventory.getContents();
    }

    @Override
    public void setContents(final ItemStack[] contents) {
        if (contents.length != this.backingInventory.getSize()) {
            final ItemStack[] padded = new ItemStack[this.backingInventory.getSize()];
            System.arraycopy(contents, 0, padded, 0, padded.length);
            this.backingInventory.setContents(padded);
        } else {
            this.backingInventory.setContents(contents);
        }
    }

    @Override
    public boolean canClick() {
        return this.clickable;
    }

    @Override
    public void setClickable(final boolean clickable) {
        this.clickable = clickable;
    }

    @Override
    public boolean canExpire() {
        return this.canExpire;
    }

    @Override
    public void setCanExpire(final boolean canExpire) {
        this.canExpire = canExpire;
    }

    @Override
    public List<Player> getViewers() {
        final ArrayList<Player> toReturn = new ArrayList<>();
        this.backingInventory.getViewers().forEach(z -> {
            if (z instanceof Player) {
                toReturn.add((Player)z);
            }
        });
        return toReturn;
    }

    @Override
    public void updateViewers() {
        if (this.recreate) {
            final List<Player> viewing = this.getViewers();
            viewing.forEach(HumanEntity::closeInventory);
            final ItemStack[] contents = this.getContents();
            this.backingInventory = Bukkit.createInventory(this.clickable ? new NoClickHolder() : null, this.size, ColorUtil.colorize(this.title));
            this.backingInventory.setContents(contents);
            viewing.forEach(z -> z.openInventory(this.backingInventory));
        } else {
            this.getViewers().forEach(Player::updateInventory);
        }
    }

    @Override
    public Menu clone() {
        return null;
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        final Inventory clickedInventory;
        if (!this.backingInventory.getViewers().contains(event.getWhoClicked())) {
            return;
        }
        if (!this.clickable) {
            event.setCancelled(true);
        }
        if ((clickedInventory = event.getClickedInventory()) == null) {
            return;
        }
        if (clickedInventory != event.getWhoClicked().getOpenInventory().getTopInventory()) {
            return;
        }
        if (this.actionMap.containsKey(event.getSlot())) {
            this.actionMap.get(event.getSlot()).forEach(z -> z.attemptClick(event));
        }
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent event) {
        if (this.onClose != null && this.backingInventory.equals(event.getInventory())) {
            this.onClose.run(event);
        }
    }
}