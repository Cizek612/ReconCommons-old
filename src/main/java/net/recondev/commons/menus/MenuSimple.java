package net.recondev.commons.menus;


import net.recondev.commons.menus.action.ClickAction;

import java.util.List;
import java.util.Map;
@SuppressWarnings("unused")
public class MenuSimple extends MenuAbstract {
    public MenuSimple(final String title, final int size, final boolean clickable, final boolean canExpire, final Map<Integer, List<ClickAction>> actionMap) {
        super(title, size, clickable, canExpire, actionMap);
    }
}