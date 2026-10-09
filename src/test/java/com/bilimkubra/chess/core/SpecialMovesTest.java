package com.bilimkubra.chess.core;

import com.bilimkubra.chess.pieces.King;
import com.bilimkubra.chess.pieces.Pawn;
import com.bilimkubra.chess.pieces.Queen;
import com.bilimkubra.chess.pieces.Rook;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpecialMovesTest {

    private static Position sq(String name) {
        return Position.fromAlgebraic(name);
    }

    private static Move mv(String from, String to) {
        return new Move(sq(from), sq(to));
    }

    // ---- Terfi ----

    @Test
    void pawnOnSeventhRankHasFourPromotionChoices() {
        Board board = Board.fromFen("4k3/P7/8/8/8/8/8/4K3 w - -");
        List<Move> legal = board.getLegalMoves(Color.WHITE);

        assertTrue(legal.contains(new Move(sq("a7"), sq("a8"), 'Q')));
        assertTrue(legal.contains(new Move(sq("a7"), sq("a8"), 'N')));
        assertFalse(legal.contains(mv("a7", "a8")));   // terfisiz ilerleme yasak
    }

    @Test
    void promotionReplacesPawnAndUndoRestoresIt() {
        Board board = Board.fromFen("4k3/P7/8/8/8/8/8/4K3 w - -");

        board.makeMove(new Move(sq("a7"), sq("a8"), 'Q'));
        assertInstanceOf(Queen.class, board.getPiece(sq("a8")));

        board.undoMove();
        assertInstanceOf(Pawn.class, board.getPiece(sq("a7")));
        assertTrue(board.isEmpty(sq("a8")));
    }

    // ---- Geçerken alma ----

    @Test
    void enPassantCaptureRemovesThePawnBesideIt() {
        Board board = Board.fromFen("4k3/8/8/3pP3/8/8/8/4K3 w - d6");
        String before = board.toString();

        assertTrue(board.getLegalMoves(Color.WHITE).contains(mv("e5", "d6")));

        board.makeMove(mv("e5", "d6"));
        assertTrue(board.isEmpty(sq("d5")));               // alınan piyon d5'teydi
        assertInstanceOf(Pawn.class, board.getPiece(sq("d6")));

        board.undoMove();
        assertEquals(before, board.toString());
    }

    @Test
    void enPassantIsOnlyAvailableImmediately() {
        Board board = Board.initialPosition();
        board.makeMove(mv("e2", "e4"));
        board.makeMove(mv("a7", "a6"));
        board.makeMove(mv("e4", "e5"));
        board.makeMove(mv("d7", "d5"));   // siyah iki kare: e5'in yanına geldi

        assertTrue(board.getLegalMoves(Color.WHITE).contains(mv("e5", "d6")));

        board.makeMove(mv("a2", "a3"));
        board.makeMove(mv("a6", "a5"));   // bir tur geçti: hak düştü

        assertFalse(board.getLegalMoves(Color.WHITE).contains(mv("e5", "d6")));
    }

    // ---- Rok ----

    @Test
    void castlingMovesBothKingAndRookAndUndoRestoresThem() {
        Board board = Board.fromFen("r3k2r/8/8/8/8/8/8/R3K2R w KQkq -");
        List<Move> legal = board.getLegalMoves(Color.WHITE);
        assertTrue(legal.contains(mv("e1", "g1")));   // kısa rok
        assertTrue(legal.contains(mv("e1", "c1")));   // uzun rok

        board.makeMove(mv("e1", "g1"));
        assertInstanceOf(King.class, board.getPiece(sq("g1")));
        assertInstanceOf(Rook.class, board.getPiece(sq("f1")));
        assertTrue(board.isEmpty(sq("h1")));

        board.undoMove();
        assertInstanceOf(King.class, board.getPiece(sq("e1")));
        assertInstanceOf(Rook.class, board.getPiece(sq("h1")));
        assertTrue(board.isEmpty(sq("f1")));
    }

    @Test
    void cannotCastleThroughAnAttackedSquare() {
        // Siyah kale f2'de: f1 karesini tehdit ediyor
        Board board = Board.fromFen("4k3/8/8/8/8/8/5r2/R3K2R w KQ -");
        List<Move> legal = board.getLegalMoves(Color.WHITE);

        assertFalse(legal.contains(mv("e1", "g1")));
        assertTrue(legal.contains(mv("e1", "c1")));
    }

    @Test
    void castlingRightIsLostAfterTheKingMoves() {
        Board board = Board.fromFen("r3k2r/8/8/8/8/8/8/R3K2R w KQkq -");
        board.makeMove(mv("e1", "f1"));
        board.makeMove(mv("e8", "f8"));
        board.makeMove(mv("f1", "e1"));   // şah yerine döndü, ama hak bir kez kaybedildi
        board.makeMove(mv("f8", "e8"));

        assertFalse(board.getLegalMoves(Color.WHITE).contains(mv("e1", "g1")));
    }
}
