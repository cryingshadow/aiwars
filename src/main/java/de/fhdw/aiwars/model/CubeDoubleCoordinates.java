package de.fhdw.aiwars.model;

public class CubeDoubleCoordinates {

    private final double x;

    private final double y;

    private final double z;

    public CubeDoubleCoordinates(final CubeCoordinates coordinates) {
        this(coordinates.getX(), coordinates.getY(), coordinates.getZ());
    }

    public CubeDoubleCoordinates(final double x, final double y, final double z) {
        if (Math.abs(x + y + z) > 0.0000001) {
            throw new IllegalArgumentException("Cube coordinates must satisfy x + y + z = 0!");
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CubeDoubleCoordinates add(final CubeDoubleCoordinates other) {
        return
            new CubeDoubleCoordinates(
                this.getX() + other.getX(),
                this.getY() + other.getY(),
                this.getZ() + other.getZ()
            );
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof CubeDoubleCoordinates) {
            final CubeDoubleCoordinates other = (CubeDoubleCoordinates)o;
            return other.getX() == this.getX() && other.getY() == this.getY() && other.getZ() == this.getZ();
        }
        return false;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    @Override
    public int hashCode() {
        return (int)(this.getX() * 5 + this.getY() * 11 + this.getZ() * 13);
    }

    public CubeDoubleCoordinates multiply(final double scalar) {
        return new CubeDoubleCoordinates(scalar * this.getX(), scalar * this.getY(), scalar * this.getZ());
    }

    public CubeDoubleCoordinates negate() {
        return this.multiply(-1.0);
    }

    public CubeCoordinates roundToCubeCoordinates() {
        final int roundedX = (int)Math.round(this.getX());
        final int roundedY = (int)Math.round(this.getY());
        final int roundedZ = (int)Math.round(this.getZ());
        final double diffX = Math.abs(roundedX - this.getX());
        final double diffY = Math.abs(roundedY - this.getY());
        final double diffZ = Math.abs(roundedZ - this.getZ());
        if (diffX > diffY && diffX > diffZ) {
            return new CubeCoordinates(-roundedY - roundedZ, roundedY, roundedZ);
        } else if (diffY > diffZ) {
            return new CubeCoordinates(roundedX, -roundedX - roundedZ, roundedZ);
        } else {
            return new CubeCoordinates(roundedX, roundedY, -roundedX - roundedY);
        }
    }

    public CubeDoubleCoordinates subtract(final CubeDoubleCoordinates other) {
        return this.add(other.negate());
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", this.getX(), this.getY(), this.getZ());
    }

}
