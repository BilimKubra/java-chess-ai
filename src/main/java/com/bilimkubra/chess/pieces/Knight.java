package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Color;

public class Knight extends Piece {

    public Knight(Color color) {
        super(color);
    }

    @Override
    public int getValue() {
        return 320;
    }

}
