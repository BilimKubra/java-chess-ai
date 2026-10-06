package com.bilimkubra.chess.core;

public final class Move {

    private final Position from;
    private final Position to;

    public Move(Position from, Position to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Hamlenin başlangıç ve bitiş karesi boş olamaz");
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException("Taş aynı kareye hamle yapamaz: " + from);
        }
        this.from = from;
        this.to = to;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

}
