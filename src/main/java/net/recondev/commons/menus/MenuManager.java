package net.recondev.commons.menus;


import net.recondev.commons.ReconCommons;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
public final class MenuManager extends BukkitRunnable {
    private static MenuManager instance;
    private final List<Menu> allMenus;
    private final BukkitTask managerTask;

    public MenuManager() {
        instance = this;
        this.managerTask = this.runTaskTimer(ReconCommons.getCommons(), 0L, 1L);
        this.allMenus = new ArrayList<>();
    }

    public void addMenu(final Menu menu) {
        this.allMenus.add(menu);
    }

    public void run() {
        final Iterator<Menu> menuIterator = this.allMenus.iterator();
        while (menuIterator.hasNext()) {
            final Menu menu = menuIterator.next();
            if (menu.canExpire() && menu.getViewers().isEmpty()) {
                HandlerList.unregisterAll(menu);
                menu.shutdown();
                menuIterator.remove();
            }
        }
    }

    public static MenuManager getInstance() {
        return instance;
    }
}
