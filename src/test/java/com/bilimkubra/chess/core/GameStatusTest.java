package com.bilimkubra.chess.core;

import com.bilimkubra.chess.pieces.King;
import com.bilimkubra.chess.pieces.Queen;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameStatusTest {

    private static Position sq(String name) {
        return Position.fromAlgebraic(name);
    }

    private static Move mv(String from, String to) {
        return new Move(sq(from), sq(to));
    }

    @Test
    void gameIsOngoingAtStart() {
        assertEquals(GameStatus.ONGOING, Board.initialPosition().getStatus(Color.WHITE));
    }

    @Test
    void foolsMateIsCheckmate() {
        Board board = Board.initialPosition();
        board.makeMove(mv("f2", "f3"));
        board.makeMove(mv("e7", "e5"));
        board.makeMove(mv("g2", "g4"));
        board.makeMove(mv("d8", "h4"));

        assertEquals(GameStatus.CHECKMATE, board.getStatus(Color.WHITE));
    }

    @Test
    void cornerKingWithNoMovesAndNoCheckIsStalemate() {
        Board board = new Board();
        board.setPiece(sq("a8"), new King(Color.BLACK));
        board.setPiece(sq("b6"), new Queen(Color.WHITE));
        board.setPiece(sq("h1"), new King(Color.WHITE));

        assertEquals(GameStatus.STALEMATE, board.getStatus(Color.BLACK));
    }
}
