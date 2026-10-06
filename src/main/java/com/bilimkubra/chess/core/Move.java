package com.bilimkubra.chess.core;

import java.util.Objects;

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

    @Override
    public String toString() {
        return from.toString() + to.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move other)) return false;
        return from.equals(other.from) && to.equals(other.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

}
