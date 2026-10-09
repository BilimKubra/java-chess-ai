package com.bilimkubra.chess.ai;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;
import com.bilimkubra.chess.pieces.King;
import com.bilimkubra.chess.pieces.Pawn;
import com.bilimkubra.chess.pieces.Queen;
import com.bilimkubra.chess.pieces.Rook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxPlayerTest {

    private static Position sq(String name) {
        return Position.fromAlgebraic(name);
    }

    private static Move mv(String from, String to) {
        return new Move(sq(from), sq(to));
    }

    private static MinimaxPlayer ai(int depth) {
        return new MinimaxPlayer(new MaterialEvaluator(), depth);
    }

    @Test
    void capturesAnUndefendedQueen() {
        Board board = new Board();
        board.setPiece(sq("e1"), new King(Color.WHITE));
        board.setPiece(sq("e8"), new King(Color.BLACK));
        board.setPiece(sq("a1"), new Rook(Color.WHITE));
        board.setPiece(sq("a6"), new Queen(Color.BLACK));

        assertEquals(mv("a1", "a6"), ai(1).chooseMove(board, Color.WHITE));
    }

    @Test
    void findsBackRankMateInOne() {
        Board board = new Board();
        board.setPiece(sq("g1"), new King(Color.WHITE));
        board.setPiece(sq("a1"), new Rook(Color.WHITE));
        board.setPiece(sq("g8"), new King(Color.BLACK));
        board.setPiece(sq("f7"), new Pawn(Color.BLACK));
        board.setPiece(sq("g7"), new Pawn(Color.BLACK));
        board.setPiece(sq("h7"), new Pawn(Color.BLACK));

        assertEquals(mv("a1", "a8"), ai(2).chooseMove(board, Color.WHITE));
    }

    @Test
    void depthTwoAvoidsPoisonedPawnThatDepthOneTakes() {
        // d5 piyonu e6 piyonu tarafından korunuyor: Vxd5 oynanırsa exd5 ile vezir gider
        Board board = new Board();
        board.setPiece(sq("e1"), new King(Color.WHITE));
        board.setPiece(sq("e8"), new King(Color.BLACK));
        board.setPiece(sq("d1"), new Queen(Color.WHITE));
        board.setPiece(sq("d5"), new Pawn(Color.BLACK));
        board.setPiece(sq("e6"), new Pawn(Color.BLACK));

        assertEquals(mv("d1", "d5"), ai(1).chooseMove(board, Color.WHITE));      // açgözlü
        assertNotEquals(mv("d1", "d5"), ai(2).chooseMove(board, Color.WHITE));   // bir adım ileriyi görür
    }

    @Test
    void searchGrowsExponentiallyWithDepth() {
        MinimaxPlayer depth1 = ai(1);
        MinimaxPlayer depth2 = ai(2);
        depth1.chooseMove(Board.initialPosition(), Color.WHITE);
        depth2.chooseMove(Board.initialPosition(), Color.WHITE);

        assertEquals(20, depth1.getNodesSearched());    // 20 hamle
        assertEquals(420, depth2.getNodesSearched());   // 20 + 20×20
    }
}
