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

class AlphaBetaPlayerTest {

    private static Position sq(String name) {
        return Position.fromAlgebraic(name);
    }

    private static Move mv(String from, String to) {
        return new Move(sq(from), sq(to));
    }

    private static AlphaBetaPlayer ai(int depth) {
        return new AlphaBetaPlayer(new MaterialEvaluator(), depth);
    }

    @Test
    void findsTheSameTacticsAsMinimax() {
        // Korumasız vezir
        Board hanging = new Board();
        hanging.setPiece(sq("e1"), new King(Color.WHITE));
        hanging.setPiece(sq("e8"), new King(Color.BLACK));
        hanging.setPiece(sq("a1"), new Rook(Color.WHITE));
        hanging.setPiece(sq("a6"), new Queen(Color.BLACK));
        assertEquals(mv("a1", "a6"), ai(2).chooseMove(hanging, Color.WHITE));

        // Arka sıra matı
        Board mate = new Board();
        mate.setPiece(sq("g1"), new King(Color.WHITE));
        mate.setPiece(sq("a1"), new Rook(Color.WHITE));
        mate.setPiece(sq("g8"), new King(Color.BLACK));
        mate.setPiece(sq("f7"), new Pawn(Color.BLACK));
        mate.setPiece(sq("g7"), new Pawn(Color.BLACK));
        mate.setPiece(sq("h7"), new Pawn(Color.BLACK));
        assertEquals(mv("a1", "a8"), ai(3).chooseMove(mate, Color.WHITE));
    }

    @Test
    void avoidsPoisonedPawn() {
        Board board = new Board();
        board.setPiece(sq("e1"), new King(Color.WHITE));
        board.setPiece(sq("e8"), new King(Color.BLACK));
        board.setPiece(sq("d1"), new Queen(Color.WHITE));
        board.setPiece(sq("d5"), new Pawn(Color.BLACK));
        board.setPiece(sq("e6"), new Pawn(Color.BLACK));

        assertNotEquals(mv("d1", "d5"), ai(2).chooseMove(board, Color.WHITE));
    }

    @Test
    void worksForBlackToo() {
        Board board = new Board();
        board.setPiece(sq("e1"), new King(Color.WHITE));
        board.setPiece(sq("e8"), new King(Color.BLACK));
        board.setPiece(sq("h4"), new Queen(Color.WHITE));
        board.setPiece(sq("h8"), new Rook(Color.BLACK));

        assertEquals(mv("h8", "h4"), ai(3).chooseMove(board, Color.BLACK));
    }

    @Test
    void searchesFarFewerNodesThanMinimax() {
        MinimaxPlayer minimax = new MinimaxPlayer(new MaterialEvaluator(), 3);
        AlphaBetaPlayer alphaBeta = ai(3);

        minimax.chooseMove(Board.initialPosition(), Color.WHITE);
        alphaBeta.chooseMove(Board.initialPosition(), Color.WHITE);

        System.out.printf("Derinlik 3 -> Minimax: %d düğüm, Alpha-Beta: %d düğüm%n",
                minimax.getNodesSearched(), alphaBeta.getNodesSearched());

        assertTrue(alphaBeta.getNodesSearched() * 5 < minimax.getNodesSearched());
    }
}
