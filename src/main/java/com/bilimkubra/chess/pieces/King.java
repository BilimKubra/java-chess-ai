package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Color;

public class King extends Piece {

    public King(Color color) {
        super(color);
    }

    @Override
    public int getValue() {
        return 20000;
    }

    @Override
    protected char getSymbol() {
        return 'K';
    }

}
