package de.fhdw.aiwars.model;

import java.util.*;

public class GameMap extends LinkedHashMap<Coordinates, GameField> {

    private static final long serialVersionUID = -604155324838726409L;

    private final int height;

    private final int width;

    public GameMap(final int width, final int height) {
        super();
        this.width = width;
        this.height = height;
    }

    private GameMap(final Map<Coordinates, GameField> map, final int width, final int height) {
        super(map);
        this.width = width;
        this.height = height;
    }

    public GameMap copy() {
        return new GameMap(this, this.getWidth(), this.getHeight());
    }

    public Optional<GameField> getField(final Coordinates coordinates) {
        if (this.containsKey(coordinates)) {
            return Optional.of(this.get(coordinates));
        }
        return Optional.empty();
    }

    public Optional<GameField> getField(final int x, final int y) {
        return this.getField(new Coordinates(x, y));
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    public String toString() {
        return String.format("%d x %d: %s", this.getWidth(), this.getHeight(), super.toString());
    }

}
