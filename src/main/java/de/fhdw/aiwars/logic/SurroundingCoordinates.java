package de.fhdw.aiwars.logic;

import java.util.*;

import de.fhdw.aiwars.model.*;

public class SurroundingCoordinates extends ArrayList<Coordinates> {

    private static final long serialVersionUID = 10283063473303317L;

    public SurroundingCoordinates(final Coordinates from) {
        for (final Direction direction : Direction.values()) {
            this.add(direction.getCoordinates(from));
        }
    }

}
