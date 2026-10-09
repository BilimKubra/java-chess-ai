package com.bilimkubra.chess.ai;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Position;
import com.bilimkubra.chess.pieces.Piece;

/** En basit değerlendirme: Beyazın taş değerleri toplamı eksi siyahınki. */
public class MaterialEvaluator implements Evaluator {

    @Override
    public int evaluate(Board board) {
        int score = 0;
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece piece = board.getPiece(new Position(file, rank));
                if (piece == null) {
                    continue;
                }
                if (piece.getColor() == Color.WHITE) {
                    score += piece.getValue();
                } else {
                    score -= piece.getValue();
                }
            }
        }
        return score;
    }
}
