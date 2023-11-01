package de.fhdw.aiwars.control.samples;

import java.util.*;

import org.testng.*;
import org.testng.annotations.Test;

import de.fhdw.aiwars.model.*;

public class SimpleStrategyTest {

    @Test
    public void computeAvailableAmountForAttackSimple() {
        final Map<Coordinates, Integer> alreadyUsed = new LinkedHashMap<Coordinates, Integer>();
        final List<PossibleAttack> possibleAttacks =
            Arrays.asList(
                new PossibleAttack(new Coordinates(0, 0), Direction.EAST, new GameField(0, 1), new GameField(1, 1))
            );
        Assert.assertEquals(SimpleStrategy.computeAvailableAmountForAttack(alreadyUsed, possibleAttacks), 1);
    }

    @Test
    public void computeAvailableAmountForAttackWithoutUsed() {
        final Map<Coordinates, Integer> alreadyUsed = new LinkedHashMap<Coordinates, Integer>();
        final List<PossibleAttack> possibleAttacks =
            Arrays.asList(
                new PossibleAttack(new Coordinates(0, 0), Direction.EAST, new GameField(0, 2), new GameField(1, 1)),
                new PossibleAttack(new Coordinates(2, 0), Direction.WEST, new GameField(0, 3), new GameField(1, 1))
            );
        Assert.assertEquals(SimpleStrategy.computeAvailableAmountForAttack(alreadyUsed, possibleAttacks), 5);
    }

    @Test
    public void computeAvailableAmountForAttackWithUsed() {
        final Map<Coordinates, Integer> alreadyUsed = new LinkedHashMap<Coordinates, Integer>();
        alreadyUsed.put(new Coordinates(2, 0), 2);
        final List<PossibleAttack> possibleAttacks =
            Arrays.asList(
                new PossibleAttack(new Coordinates(0, 0), Direction.EAST, new GameField(0, 2), new GameField(1, 1)),
                new PossibleAttack(new Coordinates(2, 0), Direction.WEST, new GameField(0, 3), new GameField(1, 1))
            );
        Assert.assertEquals(SimpleStrategy.computeAvailableAmountForAttack(alreadyUsed, possibleAttacks), 3);
    }

    @Test
    public void computeAvailableAmountForAttackWithUsedTwice() {
        final Map<Coordinates, Integer> alreadyUsed = new LinkedHashMap<Coordinates, Integer>();
        alreadyUsed.put(new Coordinates(0, 0), 1);
        alreadyUsed.put(new Coordinates(2, 0), 2);
        final List<PossibleAttack> possibleAttacks =
            Arrays.asList(
                new PossibleAttack(new Coordinates(0, 0), Direction.EAST, new GameField(0, 2), new GameField(1, 1)),
                new PossibleAttack(new Coordinates(2, 0), Direction.WEST, new GameField(0, 3), new GameField(1, 1))
            );
        Assert.assertEquals(SimpleStrategy.computeAvailableAmountForAttack(alreadyUsed, possibleAttacks), 2);
    }

}
