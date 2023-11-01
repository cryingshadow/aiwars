package de.fhdw.aiwars.logic;

import java.util.*;

import de.fhdw.aiwars.model.*;

public class FieldChanges extends LinkedHashMap<Coordinates, FieldChange> {

    private static final long serialVersionUID = -7992974916562549673L;

    public FieldChanges(final List<Move> moves, final GameMap map) {
        for (final Move move : moves) {
            final Coordinates from = move.getFrom();
            final Coordinates to = move.getTo().getCoordinatesInMap(from, map).get();
            final int amount = move.getAmount();
            this.put(from, this.getOrDefault(from, new FieldChange()).addOut(amount));
            this.put(to, this.getOrDefault(to, new FieldChange()).addIn(amount));
        }
    }

}
