package de.fhdw.aiwars.model;

public class Move {

    private final int amount;

    private final Coordinates from;

    private final Direction to;

    public Move(final Coordinates from, final Direction to, final int amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }

    public Coordinates getFrom() {
        return this.from;
    }

    public Direction getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s: %d", this.getFrom(), this.getTo(), this.getAmount());
    }

}
