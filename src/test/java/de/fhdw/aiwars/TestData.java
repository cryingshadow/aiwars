package de.fhdw.aiwars;

import de.fhdw.aiwars.control.*;
import de.fhdw.aiwars.control.samples.*;
import de.fhdw.aiwars.model.*;

public class TestData {

    private final static GameMap DEFAULT_MAP;

    static {
        DEFAULT_MAP = new GameMap(2, 2);
        TestData.DEFAULT_MAP.put(new Coordinates(0, 0), new GameField(0, 5));
        TestData.DEFAULT_MAP.put(new Coordinates(1, 0), new GameField(Game.NO_PLAYER, 1));
        TestData.DEFAULT_MAP.put(new Coordinates(1, 1), new GameField(1, 5));
    }

    public static GameMap getDefaultMap() {
        return TestData.DEFAULT_MAP.copy();
    }

    public static Player[] getDefaultPlayers() {
        return new Player[]{new JustGrow(), new SingleAttack()};
    }

}
