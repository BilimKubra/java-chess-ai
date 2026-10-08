package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveGenerationTest {

    private static Position sq(String name) {
        return Position.fromAlgebraic(name);
    }

    private static Move mv(String from, String to) {
        return new Move(sq(from), sq(to));
    }

    /** Boş bir tahtaya tek taş koyup kaç hamlesi olduğunu sayar. */
    private static int movesOnEmptyBoard(Piece piece, String square) {
        Board board = new Board();
        board.setPiece(sq(square), piece);
        return piece.getPseudoLegalMoves(board, sq(square)).size();
    }

    @Test
    void initialPositionHas20MovesForEachSide() {
        // Satranç motorlarının bilinen doğrulama değeri (perft derinlik 1)
        Board board = Board.initialPosition();
        assertEquals(20, board.getPseudoLegalMoves(Color.WHITE).size());
        assertEquals(20, board.getPseudoLegalMoves(Color.BLACK).size());
    }

    @Test
    void knightJumpsOverPieces() {
        Board board = Board.initialPosition();
        List<Move> moves = board.getPiece(sq("b1")).getPseudoLegalMoves(board, sq("b1"));
        assertEquals(2, moves.size());
        assertTrue(moves.contains(mv("b1", "a3")));
        assertTrue(moves.contains(mv("b1", "c3")));
    }

    @Test
    void pieceMoveCountsOnEmptyBoard() {
        assertEquals(27, movesOnEmptyBoard(new Queen(Color.WHITE), "d4"));
        assertEquals(14, movesOnEmptyBoard(new Rook(Color.WHITE), "a1"));
        assertEquals(13, movesOnEmptyBoard(new Bishop(Color.WHITE), "d4"));
        assertEquals(8,  movesOnEmptyBoard(new King(Color.WHITE), "e4"));
        assertEquals(2,  movesOnEmptyBoard(new Knight(Color.WHITE), "a1"));
    }

    @Test
    void slidingPieceStopsAtOwnPieceAndCapturesEnemy() {
        Board board = new Board();
        board.setPiece(sq("a1"), new Rook(Color.WHITE));
        board.setPiece(sq("a4"), new Pawn(Color.WHITE));    // kendi taşı: önünde durur
        board.setPiece(sq("d1"), new Knight(Color.BLACK));  // rakip: alır ve durur

        List<Move> moves = board.getPiece(sq("a1")).getPseudoLegalMoves(board, sq("a1"));

        assertTrue(moves.contains(mv("a1", "a3")));
        assertFalse(moves.contains(mv("a1", "a4")));
        assertTrue(moves.contains(mv("a1", "d1")));
        assertFalse(moves.contains(mv("a1", "e1")));
        assertEquals(5, moves.size());
    }

    @Test
    void pawnMovesForwardAndCapturesDiagonally() {
        Board board = new Board();
        board.setPiece(sq("e4"), new Pawn(Color.WHITE));
        board.setPiece(sq("e5"), new Knight(Color.BLACK));  // önü kapalı
        board.setPiece(sq("d5"), new Pawn(Color.BLACK));    // çaprazda rakip

        List<Move> whiteMoves = board.getPiece(sq("e4")).getPseudoLegalMoves(board, sq("e4"));
        assertEquals(List.of(mv("e4", "d5")), whiteMoves);

        List<Move> blackMoves = board.getPiece(sq("d5")).getPseudoLegalMoves(board, sq("d5"));
        assertTrue(blackMoves.contains(mv("d5", "d4")));   // siyah aşağı gider
        assertTrue(blackMoves.contains(mv("d5", "e4")));   // ve çapraz alır
        assertEquals(2, blackMoves.size());
    }

    @Test
    void pawnCanMoveTwoSquaresOnlyFromStartRank() {
        Board board = Board.initialPosition();
        List<Move> moves = board.getPiece(sq("e2")).getPseudoLegalMoves(board, sq("e2"));
        assertTrue(moves.contains(mv("e2", "e3")));
        assertTrue(moves.contains(mv("e2", "e4")));
        assertEquals(2, moves.size());
    }
}
