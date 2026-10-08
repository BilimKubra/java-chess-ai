package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;

import java.util.List;

public class Knight extends Piece {

    private static final int[][] OFFSETS = {
            {1, 2}, {2, 1}, {2, -1}, {1, -2},
            {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}
    };

    public Knight(Color color) {
        super(color);
    }

    @Override
    public int getValue() {
        return 320;
    }

    @Override
    protected char getSymbol() {
        return 'N';
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Position from) {
        return stepMoves(board, from, OFFSETS);
    }

}
