package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Color;

public class Queen extends Piece {

    public Queen(Color color) {
        super(color);
    }

    @Override
    public int getValue() {
        return 900;
    }

    @Override
    protected char getSymbol() {
        return 'Q';
    }

}
