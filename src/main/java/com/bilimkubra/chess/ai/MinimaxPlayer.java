package com.bilimkubra.chess.ai;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.player.Player;

import java.util.List;

/**
 * Minimax algoritmasıyla belirli bir derinliğe kadar ileriyi düşünen yapay zekâ.
 * Beyaz skoru büyütmeye (MAX), siyah küçültmeye (MIN) çalışır.
 */
public class MinimaxPlayer implements Player {

    /** Mat skoru: Herhangi bir malzeme farkından çok daha büyük olmalı. */
    static final int MATE_SCORE = 1_000_000;

    private final Evaluator evaluator;
    private final int depth;
    private long nodesSearched;

    public MinimaxPlayer(Evaluator evaluator, int depth) {
        if (depth < 1) {
            throw new IllegalArgumentException("Derinlik en az 1 olmalı");
        }
        this.evaluator = evaluator;
        this.depth = depth;
    }

    @Override
    public Move chooseMove(Board board, Color color) {
        nodesSearched = 0;
        boolean maximizing = (color == Color.WHITE);

        Move bestMove = null;
        int bestScore = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Move move : board.getLegalMoves(color)) {
            board.makeMove(move);
            int score = minimax(board, depth - 1, color.opposite());
            board.undoMove();

            boolean better = maximizing ? score > bestScore : score < bestScore;
            if (better) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    /**
     * Pozisyonun değerini, iki tarafın da en iyi oynadığını varsayarak hesaplar.
     * @param depth daha kaç yarım hamle ileriye bakılacak
     * @param sideToMove sırası gelen taraf
     */
    private int minimax(Board board, int depth, Color sideToMove) {
        nodesSearched++;

        List<Move> moves = board.getLegalMoves(sideToMove);

        // Oyun bittiyse: mat veya pat
        if (moves.isEmpty()) {
            if (board.isInCheck(sideToMove)) {
                int mate = MATE_SCORE + depth;   // daha erken mat = daha büyük skor
                return (sideToMove == Color.WHITE) ? -mate : mate;
            }
            return 0;
        }

        // Derinlik bitti: pozisyonu değerlendir
        if (depth == 0) {
            return evaluator.evaluate(board);
        }

        if (sideToMove == Color.WHITE) {
            int best = Integer.MIN_VALUE;
            for (Move move : moves) {
                board.makeMove(move);
                best = Math.max(best, minimax(board, depth - 1, Color.BLACK));
                board.undoMove();
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (Move move : moves) {
                board.makeMove(move);
                best = Math.min(best, minimax(board, depth - 1, Color.WHITE));
                board.undoMove();
            }
            return best;
        }
    }

    /** Son aramada incelenen pozisyon sayısı (performans ölçümü için). */
    public long getNodesSearched() {
        return nodesSearched;
    }

    @Override
    public String getName() {
        return "Minimax AI (derinlik " + depth + ")";
    }
}
