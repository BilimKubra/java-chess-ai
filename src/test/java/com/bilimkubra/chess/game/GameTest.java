package com.bilimkubra.chess.game;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.GameStatus;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;
import com.bilimkubra.chess.player.Player;
import com.bilimkubra.chess.player.RandomPlayer;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    /** Testler için: Önceden verilen hamleleri sırayla oynayan oyuncu. */
    private static class ScriptedPlayer implements Player {
        private final Deque<Move> moves = new ArrayDeque<>();

        ScriptedPlayer(String... uciMoves) {
            for (String uci : uciMoves) {
                moves.add(new Move(
                        Position.fromAlgebraic(uci.substring(0, 2)),
                        Position.fromAlgebraic(uci.substring(2, 4))));
            }
        }

        @Override
        public Move chooseMove(Board board, Color color) {
            return moves.poll();
        }

        @Override
        public String getName() {
            return "Senaryo";
        }
    }

    @Test
    void foolsMateEndsTheGameWithCheckmate() {
        Player white = new ScriptedPlayer("f2f3", "g2g4");
        Player black = new ScriptedPlayer("e7e5", "d8h4");

        Game game = new Game(Board.initialPosition(), white, black);
        GameStatus result = game.play(100, false);

        assertEquals(GameStatus.CHECKMATE, result);
        assertEquals(Color.WHITE, game.getSideToMove());   // mat olan taraf beyaz
    }

    @Test
    void twoRandomPlayersCanPlayWithoutErrors() {
        Game game = new Game(Board.initialPosition(),
                new RandomPlayer(new Random(1)), new RandomPlayer(new Random(2)));

        GameStatus result = game.play(200, false);

        assertTrue(List.of(GameStatus.ONGOING, GameStatus.CHECKMATE, GameStatus.STALEMATE)
                .contains(result));
    }
}
