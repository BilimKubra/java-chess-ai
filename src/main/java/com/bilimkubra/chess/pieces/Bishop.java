package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;

import java.util.List;

public class Bishop extends Piece {

    static final int[][] DIRECTIONS = {
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    public Bishop(Color color) {
        super(color);
    }

    @Override
    public int getValue() {
        return 330;
    }

    @Override
    protected char getSymbol() {
        return 'B';
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Position from) {
        return slideMoves(board, from, DIRECTIONS);
    }

}
