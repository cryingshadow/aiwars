package de.fhdw.aiwars.model;

public class Coordinates {

    private final int x;

    private final int y;

    public Coordinates(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int distance(final Coordinates other) {
        return new CubeCoordinates(this).distance(new CubeCoordinates(other));
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Coordinates) {
            final Coordinates other = (Coordinates)o;
            return other.getX() == this.getX() && other.getY() == this.getY();
        }
        return false;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public int hashCode() {
        return this.getX() * 3 + this.getY() * 7;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", this.getX(), this.getY());
    }

}
