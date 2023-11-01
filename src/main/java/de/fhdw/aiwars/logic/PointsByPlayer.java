package de.fhdw.aiwars.logic;

import java.util.*;

import de.fhdw.aiwars.model.*;

public class PointsByPlayer extends LinkedHashMap<Integer, Integer> {

    private static final long serialVersionUID = -4344222251136206696L;

    public PointsByPlayer(final GameMap map) {
        for (final GameField field : map.values()) {
            this.put(field.getPlayer(), this.getOrDefault(field.getPlayer(), 0) + 10 + field.getAmount());
        }
    }

}
