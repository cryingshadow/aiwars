package de.fhdw.aiwars.logic;

import java.util.*;

import de.fhdw.aiwars.model.*;

public class ArmiesByPlayer extends LinkedHashMap<Integer, Integer> {

    private static final long serialVersionUID = 5290771482595429703L;

    public ArmiesByPlayer(final GameMap map) {
        for (final GameField field : map.values()) {
            final Integer player = field.getPlayer();
            this.put(player, this.getOrDefault(player, 0) + field.getAmount());
        }
    }

}
