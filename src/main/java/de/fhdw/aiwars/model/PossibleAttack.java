package de.fhdw.aiwars.model;

public class PossibleAttack {

    private final Coordinates from;

    private final GameField fromField;

    private final GameField targetField;

    private final Direction to;

    public PossibleAttack(
        final Coordinates from,
        final Direction to,
        final GameField fromField,
        final GameField targetField
    ) {
        this.from = from;
        this.to = to;
        this.fromField = fromField;
        this.targetField = targetField;
    }

    public Coordinates getFrom() {
        return this.from;
    }

    public GameField getFromField() {
        return this.fromField;
    }

    public GameField getTargetField() {
        return this.targetField;
    }

    public Direction getTo() {
        return this.to;
    };

}
