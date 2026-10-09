package com.bilimkubra.chess.ai;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Position;
import com.bilimkubra.chess.pieces.Bishop;
import com.bilimkubra.chess.pieces.King;
import com.bilimkubra.chess.pieces.Knight;
import com.bilimkubra.chess.pieces.Pawn;
import com.bilimkubra.chess.pieces.Piece;
import com.bilimkubra.chess.pieces.Queen;
import com.bilimkubra.chess.pieces.Rook;

/**
 * Malzeme + konum: Her taş, bulunduğu kareye göre bonus veya ceza alır.
 * Tablolar beyazın bakış açısından yazılmıştır (ilk satır = 8. sıra).
 * Kaynak: chessprogramming.org/Simplified_Evaluation_Function
 */
public class PositionalEvaluator implements Evaluator {

    private static final int[][] PAWN = {
            {  0,  0,  0,  0,  0,  0,  0,  0},
            { 50, 50, 50, 50, 50, 50, 50, 50},
            { 10, 10, 20, 30, 30, 20, 10, 10},
            {  5,  5, 10, 25, 25, 10,  5,  5},
            {  0,  0,  0, 20, 20,  0,  0,  0},
            {  5, -5,-10,  0,  0,-10, -5,  5},
            {  5, 10, 10,-20,-20, 10, 10,  5},
            {  0,  0,  0,  0,  0,  0,  0,  0}
    };

    private static final int[][] KNIGHT = {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}
    };

    private static final int[][] BISHOP = {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20}
    };

    private static final int[][] ROOK = {
            {  0,  0,  0,  0,  0,  0,  0,  0},
            {  5, 10, 10, 10, 10, 10, 10,  5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            {  0,  0,  0,  5,  5,  0,  0,  0}
    };

    private static final int[][] QUEEN = {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}
    };

    private static final int[][] KING = {
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            { 20, 20,  0,  0,  0,  0, 20, 20},
            { 20, 30, 10,  0,  0, 10, 30, 20}
    };

    /** Kompozisyon: Malzeme hesabını kendisi yapmaz, MaterialEvaluator'a devreder. */
    private final Evaluator material = new MaterialEvaluator();

    @Override
    public int evaluate(Board board) {
        int score = material.evaluate(board);
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece piece = board.getPiece(new Position(file, rank));
                if (piece == null) {
                    continue;
                }
                int bonus = squareBonus(piece, file, rank);
                score += (piece.getColor() == Color.WHITE) ? bonus : -bonus;
            }
        }
        return score;
    }

    /** Taşın bulunduğu kare için tablo değeri. Siyah için tablo dikey aynalanır. */
    static int squareBonus(Piece piece, int file, int rank) {
        int row = (piece.getColor() == Color.WHITE) ? 7 - rank : rank;
        return tableFor(piece)[row][file];
    }

    private static int[][] tableFor(Piece piece) {
        if (piece instanceof Pawn) return PAWN;
        if (piece instanceof Knight) return KNIGHT;
        if (piece instanceof Bishop) return BISHOP;
        if (piece instanceof Rook) return ROOK;
        if (piece instanceof Queen) return QUEEN;
        if (piece instanceof King) return KING;
        throw new IllegalArgumentException("Bilinmeyen taş: " + piece);
    }
}
