package com.bilimkubra.chess.core;

import java.util.Objects;

public final class Move {

    /** Terfi yoksa kullanılan değer. */
    public static final char NO_PROMOTION = ' ';

    private final Position from;
    private final Position to;
    private final char promotion;

    public Move(Position from, Position to) {
        this(from, to, NO_PROMOTION);
    }

    /** Piyon terfisi için: promotion = 'Q', 'R', 'B' veya 'N'. */
    public Move(Position from, Position to, char promotion) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Hamlenin başlangıç ve bitiş karesi boş olamaz");
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException("Taş aynı kareye hamle yapamaz: " + from);
        }
        if (promotion != NO_PROMOTION && "QRBN".indexOf(promotion) < 0) {
            throw new IllegalArgumentException("Geçersiz terfi taşı: " + promotion);
        }
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public char getPromotion() {
        return promotion;
    }

    public boolean isPromotion() {
        return promotion != NO_PROMOTION;
    }

    @Override
    public String toString() {
        String uci = from.toString() + to.toString();
        return isPromotion() ? uci + Character.toLowerCase(promotion) : uci;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move other)) return false;
        return from.equals(other.from) && to.equals(other.to) && promotion == other.promotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, promotion);
    }

}
