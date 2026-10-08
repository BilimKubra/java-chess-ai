package com.bilimkubra.chess.core;

import com.bilimkubra.chess.pieces.King;
import com.bilimkubra.chess.pieces.Knight;
import com.bilimkubra.chess.pieces.Pawn;
import com.bilimkubra.chess.pieces.Piece;
import com.bilimkubra.chess.pieces.Queen;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private static Position sq(String name) {
        return Position.fromAlgebraic(name);
    }

    @Test
    void newBoardIsEmpty() {
        Board board = new Board();
        assertTrue(board.isEmpty(sq("e4")));
    }

    @Test
    void initialPositionHas32Pieces() {
        Board board = Board.initialPosition();
        int count = 0;
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                if (!board.isEmpty(new Position(file, rank))) {
                    count++;
                }
            }
        }
        assertEquals(32, count);
    }

    @Test
    void kingsAndQueensStartOnCorrectSquares() {
        Board board = Board.initialPosition();

        Piece whiteKing = board.getPiece(sq("e1"));
        assertInstanceOf(King.class, whiteKing);
        assertEquals(Color.WHITE, whiteKing.getColor());

        Piece blackQueen = board.getPiece(sq("d8"));
        assertInstanceOf(Queen.class, blackQueen);
        assertEquals(Color.BLACK, blackQueen.getColor());
    }

    @Test
    void pawnsAndKnightsAreInPlace() {
        Board board = Board.initialPosition();
        assertInstanceOf(Pawn.class, board.getPiece(sq("e2")));
        assertInstanceOf(Pawn.class, board.getPiece(sq("a7")));
        assertInstanceOf(Knight.class, board.getPiece(sq("g1")));
        assertTrue(board.isEmpty(sq("e4")));
    }

    @Test
    void setPiecePlacesAndRemovesPieces() {
        Board board = new Board();
        board.setPiece(sq("d4"), new Knight(Color.WHITE));
        assertFalse(board.isEmpty(sq("d4")));

        board.setPiece(sq("d4"), null);
        assertTrue(board.isEmpty(sq("d4")));
    }
}
