package de.fhdw.aiwars.logic;

import java.util.*;
import java.util.logging.*;

import de.fhdw.aiwars.model.*;

public class MoveValidator {

    private static final Logger LOG = Logger.getLogger(MoveValidator.class.getName());

    private final List<Move> moves;

    private final int player;

    public MoveValidator(final int player, final List<Move> moves) {
        this.player = player;
        this.moves = moves;
    }

    public boolean validMoves(final GameMap map) {
        final Map<Coordinates, Integer> moveFrom = new LinkedHashMap<Coordinates, Integer>();
        for (final Move move : this.moves) {
            final Coordinates from = move.getFrom();
            final int amount = move.getAmount();
            if (amount < 1) {
                MoveValidator.LOG.log(
                    Level.WARNING,
                    String.format(
                        "Player %d tried to move a non-positive amount %d from %s!",
                        this.player,
                        amount,
                        from
                    )
                );
                return false;
            }
            final Optional<GameField> field = map.getField(from);
            if (!field.isPresent()) {
                MoveValidator.LOG.log(
                    Level.WARNING,
                    String.format("Player %d tried to move from non-existing field at %s!", this.player, from)
                );
                return false;
            }
            if (field.get().getPlayer() != this.player) {
                MoveValidator.LOG.log(
                    Level.WARNING,
                    String.format("Player %d tried to move from a foreign field at %s!", this.player, from)
                );
                return false;
            }
            final Optional<GameField> targetField = move.getTo().getField(from, map);
            if (!targetField.isPresent()) {
                MoveValidator.LOG.log(
                    Level.WARNING,
                    String.format(
                        "Player %d tried to move to a non-existing field from %s in direction %s!",
                        this.player,
                        from,
                        move.getTo()
                    )
                );
                return false;
            }
            if (!moveFrom.containsKey(from)) {
                moveFrom.put(from, 0);
            }
            moveFrom.put(from, moveFrom.get(from) + move.getAmount());
        }
        for (final Map.Entry<Coordinates, Integer> entry : moveFrom.entrySet()) {
            final GameField field = map.getField(entry.getKey()).get();
            if (field.getAmount() < entry.getValue()) {
                MoveValidator.LOG.log(
                    Level.WARNING,
                    String.format(
                        "Player %d tried to move %d armies from %s, but there are only %d!",
                        this.player,
                        entry.getValue(),
                        entry.getKey(),
                        field.getAmount()
                    )
                );
                return false;
            }
        }
        return true;
    }

}
