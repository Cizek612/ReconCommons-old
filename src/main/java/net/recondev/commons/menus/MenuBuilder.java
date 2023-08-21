package net.recondev.commons.menus;


import net.recondev.commons.menus.action.ClickAction;
import net.recondev.commons.menus.action.ClickRunnable;
import net.recondev.commons.menus.shape.Shape;
import net.recondev.commons.menus.shape.ShapeRectangle;
import net.recondev.commons.utils.ChancedReference;
import net.recondev.commons.utils.Pair;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.inventory.ItemStack;

@SuppressWarnings({"unused"})
public final class MenuBuilder {
    private int size = 27;
    private boolean canExpire = true;
    private boolean clickable;
    private String title = "&&&lLOOP GUI";
    private final Map<Integer, List<ClickAction>> clickActions = new HashMap<>();
    private final List<Pair<Shape, List<ChancedReference<ItemStack>>>> shapeCreation = new ArrayList<>();
    private final Map<Integer, ItemStack> specificItems = new HashMap<>();

    private MenuBuilder() {
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public MenuBuilder size(final int size) {
        this.size = size;
        return this;
    }

    public MenuBuilder clickable(final boolean clickable) {
        this.clickable = clickable;
        return this;
    }

    public MenuBuilder title(final String title) {
        this.title = title;
        return this;
    }

    public MenuBuilder expire(final boolean canExpire) {
        this.canExpire = canExpire;
        return this;
    }

    public MenuBuilder setAt(final int slot, final ItemStack itemStack) {
        this.specificItems.put(slot, itemStack);
        return this;
    }

    public MenuBuilder setAt(final int x, final int y, final ItemStack itemStack) {
        this.specificItems.put(MenuUtils.getSlotFromCartCoords(x, y), itemStack);
        return this;
    }

    public MenuBuilder filler(final ItemStack filler) {
        return this.filler(Lists.newArrayList(new ChancedReference<>(filler)));
    }

    public MenuBuilder filler(final ChancedReference<ItemStack> filler) {
        return this.filler(Lists.newArrayList(filler));
    }

    public MenuBuilder filler(final List<ChancedReference<ItemStack>> filler) {
        final Coordinate cartCoordsFromSlot = MenuUtils.getCartCoordsFromSlot(this.size - 1);
        final ShapeRectangle key = new ShapeRectangle(new Coordinate(1, 1), cartCoordsFromSlot);
        return this.drawShape(key, filler);
    }

    public MenuBuilder drawShape(final Shape shape, final ItemStack stack) {
        return this.drawShape(shape, new ChancedReference<>(stack));
    }

    public MenuBuilder drawShape(final Shape shape, final ChancedReference<ItemStack> reference) {
        return this.drawShape(shape, Lists.newArrayList(reference));
    }

    public MenuBuilder drawShape(final Shape shape, final List<ChancedReference<ItemStack>> reference) {
        this.shapeCreation.add(new Pair<>(shape, reference));
        return this;
    }

    public MenuBuilder addAction(int x, int y, ClickAction action) {
        return this.addAction(MenuUtils.getSlotFromCartCoords(x, y), action);
    }

    public MenuBuilder addAction(int x, int y, ClickRunnable runnable) {
        return this.addAction(MenuUtils.getSlotFromCartCoords(x, y), runnable);
    }

    public MenuBuilder addAction(int slot, ClickRunnable runnable) {
        return this.addAction(slot, new ClickAction(200L, runnable));
    }

    public MenuBuilder addAction(int slot, ClickAction action) {
        if (this.clickActions.containsKey(slot)) {
            this.clickActions.get(slot).add(action);
        } else {
            this.clickActions.put(slot, Lists.newArrayList(action));
        }
        return this;
    }

    public MenuSimple buildSimple(String title, int size) {
        ItemStack[] contents = new ItemStack[size];
        for (Pair<Shape, List<ChancedReference<ItemStack>>> shapeListPair : this.shapeCreation) {
            for (Coordinate coordinate : shapeListPair.getKey().getInRange()) {
                int slot = MenuUtils.getSlotFromCartCoords(coordinate.getX(), coordinate.getY());
                ItemStack found = null;
                while (found == null) {
                    ChancedReference<ItemStack> itemStackChancedReference = shapeListPair.getValue().get(ThreadLocalRandom.current().nextInt(shapeListPair.getValue().size()));
                    if (!itemStackChancedReference.chance()) continue;
                    found = itemStackChancedReference.getReference();
                }
                contents[slot] = found;
            }
        }
        MenuSimple menuSimple = new MenuSimple(title, size, this.clickable, this.canExpire, this.clickActions);
        menuSimple.setContents(contents);
        for (Map.Entry<Integer, ItemStack> items : this.specificItems.entrySet()) {
            menuSimple.setItemAt(items.getKey(), items.getValue());
        }
        return menuSimple;
    }

}