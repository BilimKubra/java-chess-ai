package com.bilimkubra.chess.ai;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimax ile aynı hamleyi bulan, ama sonucu etkilemeyecek dalları
 * hiç incelemeyerek (budayarak) çok daha hızlı çalışan yapay zekâ.
 */
public class AlphaBetaPlayer implements Player {

    static final int MATE_SCORE = MinimaxPlayer.MATE_SCORE;

    private final Evaluator evaluator;
    private final int depth;
    private long nodesSearched;

    public AlphaBetaPlayer(Evaluator evaluator, int depth) {
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

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        Move bestMove = null;

        for (Move move : orderMoves(board, board.getLegalMoves(color))) {
            board.makeMove(move);
            int score = alphaBeta(board, depth - 1, alpha, beta, color.opposite());
            board.undoMove();

            if (maximizing && score > alpha) {
                alpha = score;
                bestMove = move;
            } else if (!maximizing && score < beta) {
                beta = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    /**
     * @param alpha beyazın (MAX) şimdiye kadar garanti ettiği en iyi skor
     * @param beta  siyahın (MIN) şimdiye kadar garanti ettiği en iyi skor
     */
    private int alphaBeta(Board board, int depth, int alpha, int beta, Color sideToMove) {
        nodesSearched++;

        List<Move> moves = board.getLegalMoves(sideToMove);

        if (moves.isEmpty()) {
            if (board.isInCheck(sideToMove)) {
                int mate = MATE_SCORE + depth;
                return (sideToMove == Color.WHITE) ? -mate : mate;
            }
            return 0;
        }

        if (depth == 0) {
            return evaluator.evaluate(board);
        }

        if (sideToMove == Color.WHITE) {
            int best = Integer.MIN_VALUE;
            for (Move move : orderMoves(board, moves)) {
                board.makeMove(move);
                best = Math.max(best, alphaBeta(board, depth - 1, alpha, beta, Color.BLACK));
                board.undoMove();

                alpha = Math.max(alpha, best);
                if (alpha >= beta) {
                    break;   // beta kesmesi: siyah bu dala zaten izin vermez
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (Move move : orderMoves(board, moves)) {
                board.makeMove(move);
                best = Math.min(best, alphaBeta(board, depth - 1, alpha, beta, Color.WHITE));
                board.undoMove();

                beta = Math.min(beta, best);
                if (alpha >= beta) {
                    break;   // alpha kesmesi: beyaz bu dala zaten izin vermez
                }
            }
            return best;
        }
    }

    /**
     * Hamle sıralama: Taş alan hamleler önce denenir, değerli taşı alanlar en önce.
     * İyi hamleler önce denenirse daha çok dal budanır.
     */
    private List<Move> orderMoves(Board board, List<Move> moves) {
        List<Move> ordered = new ArrayList<>(moves);
        ordered.sort((a, b) -> Integer.compare(captureValue(board, b), captureValue(board, a)));
        return ordered;
    }

    private int captureValue(Board board, Move move) {
        return board.isEmpty(move.getTo()) ? 0 : board.getPiece(move.getTo()).getValue();
    }

    public long getNodesSearched() {
        return nodesSearched;
    }

    @Override
    public String getName() {
        return "Alpha-Beta AI (derinlik " + depth + ")";
    }
}
