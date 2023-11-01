package de.fhdw.aiwars.logic;

import java.util.*;

import de.fhdw.aiwars.model.*;

public class SurroundingFields extends LinkedHashMap<Direction, GameField> {

    private static final long serialVersionUID = 8485879705926100783L;

    public SurroundingFields(final GameMap map, final Coordinates from) {
        for (final Direction direction : Direction.values()) {
            final Optional<Coordinates> coordinates = direction.getCoordinatesInMap(from, map);
            if (coordinates.isPresent()) {
                this.put(direction, map.get(coordinates.get()));
            }
        }
    }

}
