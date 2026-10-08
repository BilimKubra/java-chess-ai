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

    /**
     * Taşın malzeme değeri (centipawn cinsinden, piyon = 100).
     * Her alt sınıf kendi değerini döndürmek zorundadır.
     */
    public abstract int getValue();

}
