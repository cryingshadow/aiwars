package de.fhdw.aiwars.logic;

import java.util.*;

import de.fhdw.aiwars.control.*;
import de.fhdw.aiwars.model.*;

public class ActivePlayers extends LinkedHashSet<Integer> {

    private static final long serialVersionUID = 3709039305024092974L;

    public ActivePlayers(final GameMap map) {
        for (final GameField field : map.values()) {
            this.add(field.getPlayer());
        }
        this.remove(Game.NO_PLAYER);
    }

}
