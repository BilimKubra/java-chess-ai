package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;

import java.util.ArrayList;
import java.util.List;

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

    /** Vezir = Fil + Kale: İki taşın yönlerini birlikte kullanır. */
    @Override
    public List<Move> getPseudoLegalMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();
        moves.addAll(slideMoves(board, from, Rook.DIRECTIONS));
        moves.addAll(slideMoves(board, from, Bishop.DIRECTIONS));
        return moves;
    }

}
