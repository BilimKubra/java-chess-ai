package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;

import java.util.List;

public class King extends Piece {

    private static final int[][] OFFSETS = {
            {1, 0}, {1, 1}, {0, 1}, {-1, 1},
            {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
    };

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

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Position from) {
        return stepMoves(board, from, OFFSETS);
    }

}
