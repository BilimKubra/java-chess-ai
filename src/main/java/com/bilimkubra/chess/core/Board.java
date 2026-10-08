package com.bilimkubra.chess.core;

import com.bilimkubra.chess.pieces.Bishop;
import com.bilimkubra.chess.pieces.King;
import com.bilimkubra.chess.pieces.Knight;
import com.bilimkubra.chess.pieces.Pawn;
import com.bilimkubra.chess.pieces.Piece;
import com.bilimkubra.chess.pieces.Queen;
import com.bilimkubra.chess.pieces.Rook;

public class Board {

    private final Piece[][] squares = new Piece[8][8];

    /** Standart satranç başlangıç dizilimiyle hazır bir tahta üretir. */
    public static Board initialPosition() {
        Board board = new Board();
        board.placeBackRank(Color.WHITE, 0);
        board.placePawns(Color.WHITE, 1);
        board.placePawns(Color.BLACK, 6);
        board.placeBackRank(Color.BLACK, 7);
        return board;
    }

    public Piece getPiece(Position position) {
        return squares[position.getFile()][position.getRank()];
    }

    public void setPiece(Position position, Piece piece) {
        squares[position.getFile()][position.getRank()] = piece;
    }

    public boolean isEmpty(Position position) {
        return getPiece(position) == null;
    }

    private void placePawns(Color color, int rank) {
        for (int file = 0; file < 8; file++) {
            setPiece(new Position(file, rank), new Pawn(color));
        }
    }

    private void placeBackRank(Color color, int rank) {
        Piece[] pieces = {
                new Rook(color), new Knight(color), new Bishop(color), new Queen(color),
                new King(color), new Bishop(color), new Knight(color), new Rook(color)
        };
        for (int file = 0; file < 8; file++) {
            setPiece(new Position(file, rank), pieces[file]);
        }
    }

}
