package com.bilimkubra.chess.player;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RandomPlayerTest {

    @Test
    void alwaysChoosesALegalMove() {
        Board board = Board.initialPosition();
        Player player = new RandomPlayer(new Random(42));

        for (int i = 0; i < 50; i++) {
            Move move = player.chooseMove(board, Color.WHITE);
            assertTrue(board.getLegalMoves(Color.WHITE).contains(move));
        }
    }

    @Test
    void sameSeedGivesSameMove() {
        Board board = Board.initialPosition();
        Move first = new RandomPlayer(new Random(7)).chooseMove(board, Color.WHITE);
        Move second = new RandomPlayer(new Random(7)).chooseMove(board, Color.WHITE);
        assertEquals(first, second);
    }
}
