package de.fhdw.aiwars.logic;

import java.util.*;

import de.fhdw.aiwars.model.*;

public class LineSamples extends ArrayList<CubeDoubleCoordinates> {

    private static final long serialVersionUID = 4828735833836041647L;

    public LineSamples(final CubeDoubleCoordinates from, final CubeDoubleCoordinates to, final int numOfSamples) {
        final CubeDoubleCoordinates diff = to.subtract(from);
        for (int i = 0; i < numOfSamples; i++) {
            this.add(from.add(diff.multiply(((double)i)/numOfSamples)));
        }
    }

}
