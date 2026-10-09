package com.bilimkubra.chess.core;

import com.bilimkubra.chess.pieces.Pawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MakeUndoMoveTest {

    private static Position sq(String name) {
        return Position.fromAlgebraic(name);
    }

    private static Move mv(String from, String to) {
        return new Move(sq(from), sq(to));
    }

    @Test
    void makeMoveMovesThePiece() {
        Board board = Board.initialPosition();
        board.makeMove(mv("e2", "e4"));

        assertTrue(board.isEmpty(sq("e2")));
        assertInstanceOf(Pawn.class, board.getPiece(sq("e4")));
    }

    @Test
    void undoRestoresThePositionIncludingCapturedPieces() {
        Board board = Board.initialPosition();
        String before = board.toString();

        board.makeMove(mv("e2", "e4"));
        board.makeMove(mv("d7", "d5"));
        board.makeMove(mv("e4", "d5"));   // beyaz piyon siyah piyonu alır

        board.undoMove();
        board.undoMove();
        board.undoMove();

        assertEquals(before, board.toString());
    }

    @Test
    void cannotMoveFromEmptySquare() {
        Board board = Board.initialPosition();
        assertThrows(IllegalArgumentException.class, () -> board.makeMove(mv("e4", "e5")));
    }

    @Test
    void cannotUndoWithoutHistory() {
        Board board = Board.initialPosition();
        assertThrows(IllegalStateException.class, board::undoMove);
    }
}
