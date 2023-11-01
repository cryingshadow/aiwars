package de.fhdw.aiwars.model;

import java.util.*;
import java.util.function.*;

import de.fhdw.aiwars.logic.*;

public enum Direction {

    EAST(c -> 1, c -> 0),

    NORTHEAST(c -> IsEven.isEven(c.getY()) ? 0 : 1, c -> 1),

    NORTHWEST(c -> IsEven.isEven(c.getY()) ? -1 : 0, c -> 1),

    SOUTHEAST(c -> IsEven.isEven(c.getY()) ? 0 : 1, c -> -1),

    SOUTHWEST(c -> IsEven.isEven(c.getY()) ? -1 : 0, c -> -1),

    WEST(c -> -1, c -> 0);

    public static Direction inverse(final Direction direction) {
        switch (direction) {
        case EAST:
            return WEST;
        case NORTHEAST:
            return SOUTHWEST;
        case NORTHWEST:
            return SOUTHEAST;
        case SOUTHEAST:
            return NORTHWEST;
        case SOUTHWEST:
            return NORTHEAST;
        case WEST:
            return EAST;
        default:
            throw new IllegalStateException("Not all directions have been checked!");
        }
    }

    public static Direction nextClockwise(final Direction direction) {
        switch (direction) {
        case EAST:
            return SOUTHEAST;
        case NORTHEAST:
            return EAST;
        case NORTHWEST:
            return NORTHEAST;
        case SOUTHEAST:
            return SOUTHWEST;
        case SOUTHWEST:
            return WEST;
        case WEST:
            return NORTHWEST;
        default:
            throw new IllegalStateException("Not all directions have been checked!");
        }
    }

    public static Direction previousClockwise(final Direction direction) {
        Direction res = direction;
        for (int i = 0; i < 5; i++) {
            res = Direction.nextClockwise(res);
        }
        return res;
    }

    public static Optional<Direction> getDirectionBetweenAdjacentCoordinates(
        final Coordinates from,
        final Coordinates to
    ) {
        for (final Direction direction : Direction.values()) {
            if (direction.getCoordinates(from).equals(to)) {
                return Optional.of(direction);
            }
        }
        return Optional.empty();
    }

    private final Function<Coordinates, Integer> getDiffX;

    private final Function<Coordinates, Integer> getDiffY;

    private Direction(final Function<Coordinates, Integer> getDiffX, final Function<Coordinates, Integer> getDiffY) {
        this.getDiffX = getDiffX;
        this.getDiffY = getDiffY;
    }

    public Coordinates getCoordinates(final Coordinates from) {
        final int resX = from.getX() + this.getDiffX.apply(from);
        final int resY = from.getY() + this.getDiffY.apply(from);
        return new Coordinates(resX, resY);
    }

    public Optional<Coordinates> getCoordinatesInMap(final Coordinates from, final GameMap map) {
        return this.getNeighbor(from, map, c -> c);
    }

    public Optional<GameField> getField(final Coordinates from, final GameMap map) {
        return this.getNeighbor(from, map, c -> map.get(c));
    }

    private <T> Optional<T> getNeighbor(
        final Coordinates from,
        final GameMap map,
        final Function<Coordinates, T> resultSelector
    ) {
        final Coordinates res = this.getCoordinates(from);
        if (map.containsKey(res)) {
            return Optional.of(resultSelector.apply(res));
        }
        return Optional.empty();
    }

}
