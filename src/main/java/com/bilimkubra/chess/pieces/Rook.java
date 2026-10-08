package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Color;

public class Rook extends Piece {

    public Rook(Color color) {
        super(color);
    }

    @Override
    public int getValue() {
        return 500;
    }

    @Override
    protected char getSymbol() {
        return 'R';
    }

}
