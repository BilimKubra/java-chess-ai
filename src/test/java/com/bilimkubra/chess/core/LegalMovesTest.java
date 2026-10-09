package com.bilimkubra.chess.core;

import com.bilimkubra.chess.pieces.King;
import com.bilimkubra.chess.pieces.Queen;
import com.bilimkubra.chess.pieces.Rook;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LegalMovesTest {

    private static Position sq(String name) {
        return Position.fromAlgebraic(name);
    }

    private static Move mv(String from, String to) {
        return new Move(sq(from), sq(to));
    }

    @Test
    void initialPositionIsNotCheckAndHas20LegalMoves() {
        Board board = Board.initialPosition();
        assertFalse(board.isInCheck(Color.WHITE));
        assertEquals(20, board.getLegalMoves(Color.WHITE).size());
    }

    @Test
    void pinnedRookCanOnlyMoveAlongThePin() {
        Board board = new Board();
        board.setPiece(sq("e1"), new King(Color.WHITE));
        board.setPiece(sq("e2"), new Rook(Color.WHITE));
        board.setPiece(sq("e8"), new Rook(Color.BLACK));

        List<Move> legal = board.getLegalMoves(Color.WHITE);

        assertFalse(legal.contains(mv("e2", "a2")));   // açmazdaki kale yana gidemez
        assertTrue(legal.contains(mv("e2", "e8")));    // ama saldıranı alabilir
        assertEquals(10, legal.size());                // 6 kale + 4 şah hamlesi
    }

    @Test
    void whenInCheckOnlyMovesThatEscapeAreLegal() {
        Board board = new Board();
        board.setPiece(sq("e1"), new King(Color.WHITE));
        board.setPiece(sq("a2"), new Rook(Color.WHITE));
        board.setPiece(sq("e5"), new Queen(Color.BLACK));

        assertTrue(board.isInCheck(Color.WHITE));

        List<Move> legal = board.getLegalMoves(Color.WHITE);
        assertTrue(legal.contains(mv("a2", "e2")));    // araya girerek engelle
        assertFalse(legal.contains(mv("a2", "a8")));   // şahı korumayan hamle yasak
        assertEquals(5, legal.size());
    }

    @Test
    void foolsMateLeavesWhiteWithNoLegalMoves() {
        // Satranç tarihinin en kısa matı: 1.f3 e5 2.g4 Vh4#
        Board board = Board.initialPosition();
        board.makeMove(mv("f2", "f3"));
        board.makeMove(mv("e7", "e5"));
        board.makeMove(mv("g2", "g4"));
        board.makeMove(mv("d8", "h4"));

        assertTrue(board.isInCheck(Color.WHITE));
        assertTrue(board.getLegalMoves(Color.WHITE).isEmpty());
    }
}
