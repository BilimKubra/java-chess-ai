package com.bilimkubra.chess.ai;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionalEvaluatorTest {

    private final Evaluator evaluator = new PositionalEvaluator();

    private static Position sq(String name) {
        return Position.fromAlgebraic(name);
    }

    @Test
    void initialPositionIsBalanced() {
        assertEquals(0, evaluator.evaluate(Board.initialPosition()));
    }

    @Test
    void centralPawnMoveImprovesTheScore() {
        Board board = Board.initialPosition();
        board.makeMove(new Move(sq("e2"), sq("e4")));
        assertTrue(evaluator.evaluate(board) > 0);
    }

    @Test
    void knightInTheCenterIsBetterThanInTheCorner() {
        Board center = Board.fromFen("4k3/8/8/8/3N4/8/8/4K3 w - -");
        Board corner = Board.fromFen("4k3/8/8/8/8/8/8/N3K3 w - -");
        assertTrue(evaluator.evaluate(center) > evaluator.evaluate(corner));
    }

    @Test
    void tablesAreMirroredForBlack() {
        Board whiteKnight = Board.fromFen("4k3/8/8/8/3N4/8/8/4K3 w - -");
        Board blackKnight = Board.fromFen("4k3/8/8/3n4/8/8/8/4K3 w - -");
        assertEquals(evaluator.evaluate(whiteKnight), -evaluator.evaluate(blackKnight));
    }

    @Test
    void aiNoLongerOpensWithAnEdgePawn() {
        AlphaBetaPlayer ai = new AlphaBetaPlayer(new PositionalEvaluator(), 3);
        Move first = ai.chooseMove(Board.initialPosition(), Color.WHITE);
        assertNotEquals("a2a3", first.toString());
    }
}
