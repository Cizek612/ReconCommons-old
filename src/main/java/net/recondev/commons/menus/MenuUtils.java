package net.recondev.commons.menus;

@SuppressWarnings("unused")
public class MenuUtils {

    public static int getSlotFromCartCoords(final int x, final int y) {
        return (y - 1) * 9 + (x - 1);
    }
    public static Coordinate getCartCoordsFromSlot(final int slot) {
        int x = slot % 9 + 1;
        int y = slot / 9 + 1;
        return new Coordinate(x, y);
    }
}
