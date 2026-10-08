package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Color;

public class Bishop extends Piece {

    public Bishop(Color color) {
        super(color);
    }

    @Override
    public int getValue() {
        return 330;
    }

}
