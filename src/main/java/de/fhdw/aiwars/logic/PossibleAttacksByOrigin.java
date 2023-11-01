package de.fhdw.aiwars.logic;

import de.fhdw.aiwars.model.*;

public class PossibleAttacksByOrigin extends PossibleAttacksByCoordinates {

    private static final long serialVersionUID = 9082211142684953161L;

    public PossibleAttacksByOrigin(final GameMap map, final int player) {
        super(map, player, (from, to) -> from);
    }

}
