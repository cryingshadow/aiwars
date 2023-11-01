package de.fhdw.aiwars.model;

public class GameField {

    private final int amount;

    private final int player;

    public GameField(final int player, final int amount) {
        this.player = player;
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }

    public int getPlayer() {
        return this.player;
    }

    @Override
    public String toString() {
        return String.format("[%d: %d]", this.getPlayer(), this.getAmount());
    }

}
