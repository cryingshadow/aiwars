package de.fhdw.aiwars.logic;

import java.util.*;
import java.util.function.*;

import de.fhdw.aiwars.model.*;

public abstract class PossibleAttacksByCoordinates extends LinkedHashMap<Coordinates, List<PossibleAttack>> {

    private static final long serialVersionUID = -5799366024238933291L;

    public PossibleAttacksByCoordinates(
        final GameMap map,
        final int player,
        final BiFunction<Coordinates, Coordinates, Coordinates> coordinatesSelector
    ) {
        for (final Map.Entry<Coordinates, GameField> entry : map.entrySet()) {
            final GameField fromField = entry.getValue();
            if (fromField.getPlayer() != player) {
                continue;
            }
            final Coordinates from = entry.getKey();
            for (final Map.Entry<Direction, GameField> surrounding : new SurroundingFields(map, from).entrySet()) {
                final GameField targetField = surrounding.getValue();
                if (targetField.getPlayer() != player) {
                    final Direction direction = surrounding.getKey();
                    final Coordinates selectedCoordinates =
                        coordinatesSelector.apply(from, direction.getCoordinatesInMap(from, map).get());
                    if (!this.containsKey(selectedCoordinates)) {
                        this.put(selectedCoordinates, new ArrayList<PossibleAttack>());
                    }
                    this.get(selectedCoordinates).add(new PossibleAttack(from, direction, fromField, targetField));
                }
            }
        }
    }

}
