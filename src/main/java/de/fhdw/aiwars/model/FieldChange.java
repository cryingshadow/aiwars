package de.fhdw.aiwars.model;

public class FieldChange {

    private final int in;

    private final int out;

    public FieldChange() {
        this(0, 0);
    }

    public FieldChange(final int in, final int out) {
        this.in = in;
        this.out = out;
    }

    public int getIn() {
        return this.in;
    }

    public int getOut() {
        return this.out;
    }

    public FieldChange addIn(final int amount) {
        return new FieldChange(this.getIn() + amount, this.getOut());
    }

    public FieldChange addOut(final int amount) {
        return new FieldChange(this.getIn(), this.getOut() + amount);
    }

}
