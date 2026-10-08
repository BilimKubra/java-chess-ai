package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Color;

public abstract class Piece {

    private final Color color;

    protected Piece(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("Taşın rengi boş olamaz");
        }
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    /** Taşın malzeme değeri (centipawn, piyon = 100). */
    public abstract int getValue();

    /** Taşın büyük harfli sembolü (K, Q, R, B, N, P). */
    protected abstract char getSymbol();

    /** Şablon metot: Beyaz taşlar büyük, siyah taşlar küçük harfle gösterilir. */
    @Override
    public String toString() {
        char symbol = getSymbol();
        return String.valueOf(color == Color.WHITE ? symbol : Character.toLowerCase(symbol));
    }

}
