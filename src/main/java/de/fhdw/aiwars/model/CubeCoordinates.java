package de.fhdw.aiwars.model;

public class CubeCoordinates {

    private final int x;

    private final int y;

    private final int z;

    public CubeCoordinates(final Coordinates coordinates) {
        this.x = coordinates.getX() - (coordinates.getY() - (coordinates.getY() & 1)) / 2;
        this.z = coordinates.getY();
        this.y = -this.x - this.z;
    }

    public CubeCoordinates(final int x, final int y, final int z) {
        if (x + y + z != 0) {
            throw new IllegalArgumentException("Cube coordinates must satisfy x + y + z = 0!");
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CubeCoordinates add(final CubeCoordinates other) {
        return new CubeCoordinates(this.getX() + other.getX(), this.getY() + other.getY(), this.getZ() + other.getZ());
    }

    public int distance(final CubeCoordinates other) {
        return
            (
                Math.abs(this.getX() - other.getX())
                + Math.abs(this.getY() - other.getY())
                + Math.abs(this.getZ() - other.getZ())
            ) / 2;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof CubeCoordinates) {
            final CubeCoordinates other = (CubeCoordinates)o;
            return other.getX() == this.getX() && other.getY() == this.getY() && other.getZ() == this.getZ();
        }
        return false;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    @Override
    public int hashCode() {
        return this.getX() * 5 + this.getY() * 11 + this.getZ() * 13;
    }

    public CubeCoordinates multiply(final int scalar) {
        return new CubeCoordinates(scalar * this.getX(), scalar * this.getY(), scalar * this.getZ());
    }

    public CubeCoordinates negate() {
        return this.multiply(-1);
    }

    public CubeCoordinates subtract(final CubeCoordinates other) {
        return this.add(other.negate());
    }

    public Coordinates toOffsetCoordinates() {
        return new Coordinates(this.getX() + (this.getZ() - (this.getZ() & 1)) / 2, this.getZ());
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", this.getX(), this.getY(), this.getZ());
    }

}
