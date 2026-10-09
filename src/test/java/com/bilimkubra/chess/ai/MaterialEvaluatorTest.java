package com.bilimkubra.chess.ai;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;
import com.bilimkubra.chess.pieces.King;
import com.bilimkubra.chess.pieces.Rook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaterialEvaluatorTest {

    private final Evaluator evaluator = new MaterialEvaluator();

    private static Position sq(String name) {
        return Position.fromAlgebraic(name);
    }

    @Test
    void initialPositionIsBalanced() {
        assertEquals(0, evaluator.evaluate(Board.initialPosition()));
    }

    @Test
    void missingBlackQueenFavorsWhite() {
        Board board = Board.initialPosition();
        board.setPiece(sq("d8"), null);
        assertEquals(900, evaluator.evaluate(board));
    }

    @Test
    void extraBlackRookFavorsBlack() {
        Board board = new Board();
        board.setPiece(sq("e1"), new King(Color.WHITE));
        board.setPiece(sq("e8"), new King(Color.BLACK));
        board.setPiece(sq("a8"), new Rook(Color.BLACK));
        assertEquals(-500, evaluator.evaluate(board));
    }

    @Test
    void capturingChangesTheScore() {
        Board board = Board.initialPosition();
        board.makeMove(new Move(sq("e2"), sq("e4")));
        board.makeMove(new Move(sq("d7"), sq("d5")));
        board.makeMove(new Move(sq("e4"), sq("d5")));   // beyaz bir piyon alır

        assertEquals(100, evaluator.evaluate(board));

        board.undoMove();                               // geri alınca eşitlik geri gelir
        assertEquals(0, evaluator.evaluate(board));
    }
}
