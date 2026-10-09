package com.bilimkubra.chess.player;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;

import java.util.List;
import java.util.Random;

/** Yasal hamleler arasından rastgele seçen en basit "yapay zekâ". */
public class RandomPlayer implements Player {

    private final Random random;

    public RandomPlayer() {
        this(new Random());
    }

    /** Testlerde aynı sonucu almak için sabit tohumlu (seed) Random verilebilir. */
    public RandomPlayer(Random random) {
        this.random = random;
    }

    @Override
    public Move chooseMove(Board board, Color color) {
        List<Move> legalMoves = board.getLegalMoves(color);
        if (legalMoves.isEmpty()) {
            throw new IllegalStateException("Yasal hamle yok");
        }
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }

    @Override
    public String getName() {
        return "Rastgele Oyuncu";
    }
}
