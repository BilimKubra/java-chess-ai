package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Color;

public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color);
    }

    @Override
    public int getValue() {
        return 100;
    }

}
