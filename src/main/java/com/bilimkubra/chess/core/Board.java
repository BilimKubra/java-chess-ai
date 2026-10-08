package com.bilimkubra.chess.core;

import com.bilimkubra.chess.pieces.Piece;

public class Board {

    private final Piece[][] squares = new Piece[8][8];

    public Piece getPiece(Position position) {
        return squares[position.getFile()][position.getRank()];
    }

    public void setPiece(Position position, Piece piece) {
        squares[position.getFile()][position.getRank()] = piece;
    }

    public boolean isEmpty(Position position) {
        return getPiece(position) == null;
    }

}
