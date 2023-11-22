package de.fhdw.aiwars.control.samples;

import java.util.*;

import de.fhdw.aiwars.control.*;
import de.fhdw.aiwars.model.*;

public class SingleAttack implements Player {

    @Override
    public List<Move> turn(final GameMap map, final int playerId) {
        for (final Map.Entry<Coordinates, GameField> entry : map.entrySet()) {
            if (entry.getValue().getPlayer() != playerId) {
                continue;
            }
            final Coordinates from = entry.getKey();
            final GameField fromField = entry.getValue();
            for (final Direction direction : Direction.values()) {
                final Optional<GameField> targetField = direction.getField(from, map);
                if (!targetField.isPresent()) {
                    continue;
                }
                final GameField actualTargetField = targetField.get();
                if (
                    actualTargetField.getPlayer() == playerId || actualTargetField.getAmount() >= fromField.getAmount()
                ) {
                    continue;
                }
                return Collections.singletonList(new Move(from, direction, fromField.getAmount()));
            }
        }
        return Collections.emptyList();
    }

}
