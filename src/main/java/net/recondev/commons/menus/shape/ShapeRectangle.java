package net.recondev.commons.menus.shape;


import net.recondev.commons.menus.Coordinate;
import net.recondev.commons.menus.MenuUtils;

import java.util.LinkedList;
import java.util.List;
@SuppressWarnings("unused")
public class ShapeRectangle implements Shape {
    private List<Coordinate> inRange = new LinkedList<Coordinate>();

    public ShapeRectangle(final Coordinate low, final Coordinate high) {
        for (int x = low.getX(); x <= high.getX(); ++x) {
            for (int y = low.getY(); y <= high.getY(); ++y) {
                this.inRange.add(new Coordinate(x, y));
            }
        }
    }

    public ShapeRectangle(final int lowSlot, final int highSlot) {
        this(MenuUtils.getCartCoordsFromSlot(lowSlot), MenuUtils.getCartCoordsFromSlot(highSlot));
    }

    @Override
    public List<Coordinate> getInRange() {
        return this.inRange;
    }
}
