package de.fhdw.aiwars.control.samples;

import java.util.*;

import de.fhdw.aiwars.control.*;
import de.fhdw.aiwars.logic.*;
import de.fhdw.aiwars.model.*;

public class SimpleStrategy implements Player {

    static int computeAvailableAmountForAttack(
        final Map<Coordinates, Integer> alreadyUsed,
        final List<PossibleAttack> possibleAttacks
    ) {
        return
            possibleAttacks
            .stream()
            .reduce(
                0,
                (amount, attack) ->
                    amount + attack.getFromField().getAmount() - alreadyUsed.getOrDefault(attack.getFrom(), 0),
                Integer::sum
            );
    }

    private static void addAttacksAndUpdateAlreadyUsed(
        final List<Move> res,
        final Map<Coordinates, Integer> alreadyUsed,
        final List<PossibleAttack> possibleAttacks,
        final int totalAmount
    ) {
        int amountLeft = totalAmount;
        for (final PossibleAttack attack : possibleAttacks) {
            final int amount =
                Math.min(attack.getFromField().getAmount() - alreadyUsed.getOrDefault(attack.getFrom(), 0), amountLeft);
            if (amount > 0) {
                amountLeft -= amount;
                SimpleStrategy.addMoveAndUpdateAlreadyUsed(
                    res,
                    alreadyUsed,
                    new Move(attack.getFrom(), attack.getTo(), amount)
                );
            }
        }
    }

    private static void addMoveAndUpdateAlreadyUsed(
        final List<Move> res,
        final Map<Coordinates, Integer> alreadyUsed,
        final Move move
    ) {
        res.add(move);
        alreadyUsed.put(move.getFrom(), alreadyUsed.getOrDefault(move.getFrom(), 0) + move.getAmount());
    }

    private static void computeAttacks(final List<Move> res, final GameMap map, final int playerId) {
        final Map<Coordinates, Integer> alreadyUsed = new LinkedHashMap<Coordinates, Integer>();
        for (
            final Map.Entry<Coordinates, List<PossibleAttack>> possibleAttacksEntry :
                new PossibleAttacksByTarget(map, playerId).entrySet()
        ) {
            final Coordinates attackTarget = possibleAttacksEntry.getKey();
            final List<PossibleAttack> possibleAttacks = possibleAttacksEntry.getValue();
            final int availableAmountForAttack =
                SimpleStrategy.computeAvailableAmountForAttack(alreadyUsed, possibleAttacks);
            final int defenseAmount = map.get(attackTarget).getAmount();
            if (availableAmountForAttack > defenseAmount) {
                SimpleStrategy.addAttacksAndUpdateAlreadyUsed(res, alreadyUsed, possibleAttacks, defenseAmount + 1);
            }
        }
        if (res.isEmpty()) {
            for (
                final Map.Entry<Coordinates, List<PossibleAttack>> possibleAttacksEntry :
                    new PossibleAttacksByTarget(map, playerId).entrySet()
            ) {
                final Coordinates attackTarget = possibleAttacksEntry.getKey();
                final List<PossibleAttack> possibleAttacks = possibleAttacksEntry.getValue();
                final int availableAmountForAttack =
                    SimpleStrategy.computeAvailableAmountForAttack(alreadyUsed, possibleAttacks);
                final int defenseAmount = map.get(attackTarget).getAmount();
                if (availableAmountForAttack == defenseAmount) {
                    SimpleStrategy.addAttacksAndUpdateAlreadyUsed(res, alreadyUsed, possibleAttacks, defenseAmount);
                }
            }
        }
    }

    private static int computeAvailableArmies(final int player, final GameField field, final FieldChange change) {
        return
            field.getPlayer() == player ?
                field.getAmount() + change.getIn() - change.getOut() :
                    Turn.computeAmountAfterBattle(change.getIn(), field.getAmount());
    }

    private static void computeBalancingMoves(
        final List<Move> res,
        final GameMap map,
        final int playerId
    ) {
        final FieldChanges fieldChanges = new FieldChanges(res, map);
        final CoordinatesByPlayer coordinatesByPlayer = new CoordinatesByPlayer(map);
        final List<Move> toAdd = new ArrayList<Move>();
        do {
            res.addAll(toAdd);
            toAdd.clear();
            for (final Coordinates coordinates : coordinatesByPlayer.getOrDefault(playerId, Collections.emptyList())) {
                final SurroundingFields surroundings = new SurroundingFields(map, coordinates);
                FieldChange currentChange = fieldChanges.getOrDefault(coordinates, new FieldChange());
                final GameField currentField = map.get(coordinates);
                int availableArmies = currentField.getAmount() + currentChange.getIn() - currentChange.getOut();
                int numOfFields = 1;
                for (final Map.Entry<Direction, GameField> surrounding : surroundings.entrySet()) {
                    final GameField surroundingField = surrounding.getValue();
                    final Direction direction = surrounding.getKey();
                    final Coordinates surroundingCoordinates = direction.getCoordinatesInMap(coordinates, map).get();
                    final FieldChange change = fieldChanges.getOrDefault(surroundingCoordinates, new FieldChange());
                    if (surroundingField.getPlayer() != playerId && change.getIn() <= surroundingField.getAmount()) {
                        continue;
                    }
                    numOfFields++;
                    availableArmies += SimpleStrategy.computeAvailableArmies(playerId, surroundingField, change);
                }
                final int averageArmies = Math.min(availableArmies / numOfFields, 90);
                for (final Map.Entry<Direction, GameField> surrounding : surroundings.entrySet()) {
                    final GameField surroundingField = surrounding.getValue();
                    final Direction direction = surrounding.getKey();
                    final Coordinates surroundingCoordinates = direction.getCoordinatesInMap(coordinates, map).get();
                    final FieldChange change = fieldChanges.getOrDefault(surroundingCoordinates, new FieldChange());
                    if (surroundingField.getPlayer() != playerId && change.getIn() <= surroundingField.getAmount()) {
                        continue;
                    }
                    final int currentAmount = currentField.getAmount() + currentChange.getIn() - currentChange.getOut();
                    final int otherAmount = SimpleStrategy.computeAvailableArmies(playerId, surroundingField, change);
                    final int moveAmount = Math.min(currentAmount - averageArmies, averageArmies - otherAmount);
                    if (moveAmount > 0) {
                        final int availableAmount = currentField.getAmount() - currentChange.getOut();
                        final int actualMoveAmount = Math.min(moveAmount, availableAmount);
                        if (actualMoveAmount > 0) {
                            toAdd.add(new Move(coordinates, direction, actualMoveAmount));
                            currentChange = currentChange.addOut(actualMoveAmount);
                            fieldChanges.put(coordinates, currentChange);
                            fieldChanges.put(
                                surroundingCoordinates,
                                fieldChanges.getOrDefault(
                                    surroundingCoordinates,
                                    new FieldChange()
                                ).addIn(actualMoveAmount)
                            );
                        }
                    }
                }
            }
        } while(!toAdd.isEmpty());
    }

    private static void computeBigArmyMoves(final List<Move> res, final GameMap map, final int playerId) {
        final FieldChanges fieldChanges = new FieldChanges(res, map);
        final CoordinatesByPlayer coordinatesByPlayer = new CoordinatesByPlayer(map);
        final Coordinates enemyCoordinates = SimpleStrategy.getEnemyCoordinates(coordinatesByPlayer, playerId);
        final List<Move> toAdd = new ArrayList<Move>();
        do {
            res.addAll(toAdd);
            toAdd.clear();
            for (final Coordinates coordinates : coordinatesByPlayer.getOrDefault(playerId, Collections.emptyList())) {
                final FieldChange currentChange = fieldChanges.getOrDefault(coordinates, new FieldChange());
                final GameField currentField = map.get(coordinates);
                final int availableArmies =
                    SimpleStrategy.computeAvailableArmies(playerId, currentField, currentChange);
                if (availableArmies > 90) {
                    final ShortestPath path = new ShortestPath(map, coordinates, enemyCoordinates);
                    final int moveAmount =
                        Math.min(availableArmies - 90, currentField.getAmount() - currentChange.getOut());
                    if (moveAmount > 0) {
                        final Coordinates to = path.get(1);
                        toAdd.add(
                            new Move(
                                coordinates,
                                Direction.getDirectionBetweenAdjacentCoordinates(coordinates, to).get(),
                                moveAmount
                            )
                        );
                        fieldChanges.put(
                            coordinates,
                            fieldChanges.getOrDefault(coordinates, new FieldChange()).addOut(moveAmount)
                        );
                        fieldChanges.put(to, fieldChanges.getOrDefault(to, new FieldChange()).addIn(moveAmount));
                    }
                }
            }
        } while (!toAdd.isEmpty());
    }

    private static Coordinates getEnemyCoordinates(final CoordinatesByPlayer coordinatesByPlayer, final int playerId) {
        for (final Map.Entry<Integer, List<Coordinates>> entry : coordinatesByPlayer.entrySet()) {
            if (entry.getKey().equals(playerId)) {
                continue;
            }
            return entry.getValue().get(0);
        }
        throw new IllegalArgumentException("Coordinates map does not contain enemy coordinates!");
    }

    @Override
    public List<Move> turn(final GameMap map, final int playerId) {
        final List<Move> res = new ArrayList<Move>();
        SimpleStrategy.computeAttacks(res, map, playerId);
        SimpleStrategy.computeBalancingMoves(res, map, playerId);
        SimpleStrategy.computeBigArmyMoves(res, map, playerId);
        return res;
    }

}
