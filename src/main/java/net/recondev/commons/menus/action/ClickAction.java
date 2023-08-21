package net.recondev.commons.menus.action;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.event.inventory.InventoryClickEvent;

@SuppressWarnings("unused")
public class ClickAction {
    private final long cooldownLength;
    private final ClickRunnable clickRunnable;
    private final Map<UUID, Long> clickTimes;

    public ClickAction(final ClickRunnable clickRunnable) {
        this(0L, clickRunnable);
    }

    public ClickAction(final long cooldownLength, final ClickRunnable clickRunnable) {
        this.cooldownLength = cooldownLength;
        this.clickRunnable = clickRunnable;
        this.clickTimes = new HashMap<>();
    }

    public void attemptClick(final InventoryClickEvent event) {
        if (!this.clickTimes.containsKey(event.getWhoClicked().getUniqueId())) {
            this.clickRunnable.click(event);
            if (this.cooldownLength > 0L) {
                this.clickTimes.put(event.getWhoClicked().getUniqueId(), System.currentTimeMillis());
            }
        } else if (System.currentTimeMillis() - this.clickTimes.get(event.getWhoClicked().getUniqueId()) < this.cooldownLength) {
            this.clickRunnable.clickCooldown(event);
        } else {
            this.clickRunnable.click(event);
            this.clickTimes.put(event.getWhoClicked().getUniqueId(), System.currentTimeMillis());
        }
    }
}
