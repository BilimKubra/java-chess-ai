package com.bilimkubra.chess.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Hamle üreticisini, satranç programlamada standart kabul edilen
 * perft sayılarıyla doğrular. Kaynak: chessprogramming.org/Perft_Results
 */
class PerftTest {

    /** Verilen derinlikte oluşabilecek tüm pozisyonları sayar. */
    private static long perft(Board board, Color sideToMove, int depth) {
        if (depth == 0) {
            return 1;
        }
        long count = 0;
        for (Move move : board.getLegalMoves(sideToMove)) {
            board.makeMove(move);
            count += perft(board, sideToMove.opposite(), depth - 1);
            board.undoMove();
        }
        return count;
    }

    @Test
    void startingPosition() {
        Board board = Board.initialPosition();
        assertEquals(20, perft(board, Color.WHITE, 1));
        assertEquals(400, perft(board, Color.WHITE, 2));
        assertEquals(8_902, perft(board, Color.WHITE, 3));
    }

    @Test
    void kiwipete() {
        Board board = Board.fromFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
        assertEquals(48, perft(board, Color.WHITE, 1));
        assertEquals(2_039, perft(board, Color.WHITE, 2));
    }

    @Test
    void enPassantAndChecks() {
        Board board = Board.fromFen("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
        assertEquals(14, perft(board, Color.WHITE, 1));
        assertEquals(191, perft(board, Color.WHITE, 2));
        assertEquals(2_812, perft(board, Color.WHITE, 3));
    }

    @Test
    void promotionsAndCastling() {
        Board board = Board.fromFen("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        assertEquals(6, perft(board, Color.WHITE, 1));
        assertEquals(264, perft(board, Color.WHITE, 2));
    }
}
