package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;

import java.util.List;

public class Rook extends Piece {

    static final int[][] DIRECTIONS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };

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

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Position from) {
        return slideMoves(board, from, DIRECTIONS);
    }

}
