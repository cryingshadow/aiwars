package de.fhdw.aiwars.logic;

import java.util.*;

import de.fhdw.aiwars.model.*;

public class ShortestPath extends ArrayList<Coordinates> {

    private static class LinkedCoordinates {

        private final LinkedCoordinates from;

        private final Coordinates to;

        public LinkedCoordinates(final LinkedCoordinates from, final Coordinates to) {
            this.from = from;
            this.to = to;
        }

    }

    private static final long serialVersionUID = 4587792498181332714L;

    public ShortestPath(final GameMap map, final Coordinates start, final Coordinates end) {
        if (start.equals(end)) {
            this.add(start);
            return;
        }
        final Set<Coordinates> visited = new LinkedHashSet<Coordinates>();
        final Queue<LinkedCoordinates> queue = new ArrayDeque<LinkedCoordinates>();
        queue.offer(new LinkedCoordinates(null, start));
        visited.add(start);
        while (!queue.isEmpty()) {
            final LinkedCoordinates current = queue.poll();
            final SurroundingCoordinates surroundings = new SurroundingCoordinates(current.to);
            for (final Coordinates surroundingCoordinates : surroundings) {
                if (surroundingCoordinates.equals(end)) {
                    this.fillPath(new LinkedCoordinates(current, surroundingCoordinates));
                    return;
                } else if (!visited.contains(surroundingCoordinates) && map.containsKey(surroundingCoordinates)) {
                    visited.add(surroundingCoordinates);
                    queue.offer(new LinkedCoordinates(current, surroundingCoordinates));
                }
            }
        }
    }

    private void fillPath(final LinkedCoordinates end) {
        final LinkedList<Coordinates> linkedList = new LinkedList<Coordinates>();
        LinkedCoordinates current = end;
        while (current != null) {
            linkedList.addFirst(current.to);
            current = current.from;
        }
        this.addAll(linkedList);
    }

}
