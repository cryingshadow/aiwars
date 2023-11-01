package de.fhdw.aiwars.logic;

import de.fhdw.aiwars.model.*;

public class PossibleAttacksByTarget extends PossibleAttacksByCoordinates {

    private static final long serialVersionUID = -178120965148183770L;

    public PossibleAttacksByTarget(final GameMap map, final int player) {
        super(map, player, (from, to) -> to);
    }

}
