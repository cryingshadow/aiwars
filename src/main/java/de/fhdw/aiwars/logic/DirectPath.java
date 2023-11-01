package de.fhdw.aiwars.logic;

import java.util.*;

import de.fhdw.aiwars.model.*;

public class DirectPath extends ArrayList<Coordinates> {

    private static final long serialVersionUID = -6447135337810049796L;

    public DirectPath(final Coordinates start, final Coordinates end) {
        for (
            final CubeDoubleCoordinates sample :
                new LineSamples(
                    new CubeDoubleCoordinates(new CubeCoordinates(start)),
                    new CubeDoubleCoordinates(new CubeCoordinates(end)),
                    start.distance(end)
                )
        ) {
            this.add(sample.roundToCubeCoordinates().toOffsetCoordinates());
        }
    }

}
