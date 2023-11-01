package de.fhdw.aiwars.logic;

import java.util.*;

import de.fhdw.aiwars.model.*;

public class CoordinatesByPlayer extends LinkedHashMap<Integer, List<Coordinates>> {

    private static final long serialVersionUID = -3725923324898640739L;

    public CoordinatesByPlayer(final GameMap map) {
        for (final Map.Entry<Coordinates, GameField> entry : map.entrySet()) {
            final Integer player = entry.getValue().getPlayer();
            if (!this.containsKey(player)) {
                this.put(player, new ArrayList<Coordinates>());
            }
            this.get(player).add(entry.getKey());
        }
    }

}
