package de.fhdw.aiwars;

import de.fhdw.aiwars.control.*;
import de.fhdw.aiwars.control.samples.*;
import de.fhdw.aiwars.model.*;

public class TestData {

    private final static GameMap ADVANCED_MAP;
    private final static GameMap DEFAULT_MAP;

    static {
        DEFAULT_MAP = new GameMap(2, 2);
        TestData.DEFAULT_MAP.put(new Coordinates(0, 0), new GameField(0, 5));
        TestData.DEFAULT_MAP.put(new Coordinates(1, 0), new GameField(Game.NO_PLAYER, 1));
        TestData.DEFAULT_MAP.put(new Coordinates(1, 1), new GameField(1, 5));

        ADVANCED_MAP = new GameMap(5,5);
        TestData.ADVANCED_MAP.put(new Coordinates(0,0), new GameField(0,5));
        TestData.ADVANCED_MAP.put(new Coordinates(1,0), new GameField(Game.NO_PLAYER,1));
        TestData.ADVANCED_MAP.put(new Coordinates(1,1), new GameField(1,5));
        TestData.ADVANCED_MAP.put(new Coordinates(2,0), new GameField(Game.NO_PLAYER,1));
        TestData.ADVANCED_MAP.put(new Coordinates(2,1), new GameField(Game.NO_PLAYER,1));
        TestData.ADVANCED_MAP.put(new Coordinates(2,2), new GameField(2,5));
        TestData.ADVANCED_MAP.put(new Coordinates(3,0), new GameField(Game.NO_PLAYER,1));
        TestData.ADVANCED_MAP.put(new Coordinates(3,1), new GameField(Game.NO_PLAYER,1));
        TestData.ADVANCED_MAP.put(new Coordinates(3,2), new GameField(Game.NO_PLAYER,1));
    }

    public static GameMap getAdvancedMap() {
        return TestData.ADVANCED_MAP.copy();
    }

    public static Player[] getAllStrategiesPlayers() {
        return new Player[]{new JustGrow(), new SingleAttack(), new SimpleStrategy()};
    }

    public static GameMap getDefaultMap() {
        return TestData.DEFAULT_MAP.copy();
    }
    public static Player[] getDefaultPlayers() {
        return new Player[]{new JustGrow(), new SingleAttack()};
    }

}
