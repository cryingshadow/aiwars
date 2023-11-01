package de.fhdw.aiwars.control;

import java.util.*;

import de.fhdw.aiwars.logic.*;
import de.fhdw.aiwars.model.*;

public class Turn {

    public static int computeAmountAfterBattle(final int a, final int b) {
        return a - (b * b / a);
    }

    public static int growArmy(final int amount) {
        if (amount < 100) {
            return Math.min(99, amount + Math.max(1, (int)Math.sqrt(amount)));
        } else {
            return Math.max(99, amount - 2 * (int)Math.sqrt(amount - 99));
        }
    }

    private final List<Move> moves;

    private final int player;

    private final MoveValidator validator;

    public Turn(final int player, final List<Move> moves) {
        this.player = player;
        this.moves = moves;
        this.validator = new MoveValidator(player, moves);
    }

    public void applyMoves(final GameMap map) {
        if (this.validator.validMoves(map)) {
            for (final Map.Entry<Coordinates, FieldChange> changeEntry : new FieldChanges(this.moves, map).entrySet()) {
                this.applyChange(map, changeEntry.getKey(), changeEntry.getValue());
            }
        }
        this.growArmies(map);
    }

    private void applyChange(final GameMap currentMap, final Coordinates coordinates, final FieldChange change) {
        final GameField oldField = currentMap.get(coordinates);
        final GameField newField;
        if (oldField.getPlayer() == this.player) {
            newField = new GameField(this.player, oldField.getAmount() - change.getOut() + change.getIn());
        } else {
            final int fromAmountAfterBattle = Turn.computeAmountAfterBattle(change.getIn(), oldField.getAmount());
            if (fromAmountAfterBattle > 0) {
                newField = new GameField(this.player, fromAmountAfterBattle);
            } else {
                final int toAmountAfterBattle = Turn.computeAmountAfterBattle(oldField.getAmount(), change.getIn());
                newField = new GameField(oldField.getPlayer(), toAmountAfterBattle);
            }
        }
        currentMap.put(coordinates, newField);
    }

    private void growArmies(final GameMap currentMap) {
        for (final Map.Entry<Coordinates, GameField> entry : currentMap.entrySet()) {
            final GameField currentField = entry.getValue();
            if (currentField.getPlayer() == this.player) {
                final int newAmount = Turn.growArmy(currentField.getAmount());
                entry.setValue(new GameField(this.player, newAmount));
            }
        }
    }

}
