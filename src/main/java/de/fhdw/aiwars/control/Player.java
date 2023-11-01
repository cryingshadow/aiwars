package de.fhdw.aiwars.control;

import java.util.*;

import de.fhdw.aiwars.model.*;

public interface Player {

    List<Move> turn(GameMap map, int playerId);

}
