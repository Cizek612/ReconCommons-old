package net.recondev.commons.menus;

public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "x:" + this.x + " y:" + this.y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Coordinate)) {
            return false;
        }
        final Coordinate other = (Coordinate) o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getX() != other.getX()) {
            return false;
        }
        return this.getY() == other.getY();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Coordinate;
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        return result;
    }
}