package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {

    private final Color color;

    protected Piece(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("Taşın rengi boş olamaz");
        }
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    /** Taşın malzeme değeri (centipawn, piyon = 100). */
    public abstract int getValue();

    /** Taşın büyük harfli sembolü (K, Q, R, B, N, P). */
    protected abstract char getSymbol();

    /**
     * Bu taşın verilen kareden yapabileceği hamleler.
     * Şahın tehdit altında kalıp kalmadığına henüz bakılmaz (sözde-yasal hamleler).
     */
    public abstract List<Move> getPseudoLegalMoves(Board board, Position from);

    /** Şablon metot: Beyaz taşlar büyük, siyah taşlar küçük harfle gösterilir. */
    @Override
    public String toString() {
        char symbol = getSymbol();
        return String.valueOf(color == Color.WHITE ? symbol : Character.toLowerCase(symbol));
    }

    // ---- Alt sınıfların ortak kullandığı yardımcı metotlar ----

    /** Atlayan taşlar (At, Şah): Her yöne sadece bir adım gider. */
    protected List<Move> stepMoves(Board board, Position from, int[][] offsets) {
        List<Move> moves = new ArrayList<>();
        for (int[] offset : offsets) {
            if (!from.canOffset(offset[0], offset[1])) {
                continue;
            }
            Position to = from.offset(offset[0], offset[1]);
            if (canLandOn(board, to)) {
                moves.add(new Move(from, to));
            }
        }
        return moves;
    }

    /** Kayan taşlar (Fil, Kale, Vezir): Bir taşa çarpana veya tahta bitene kadar gider. */
    protected List<Move> slideMoves(Board board, Position from, int[][] directions) {
        List<Move> moves = new ArrayList<>();
        for (int[] direction : directions) {
            Position current = from;
            while (current.canOffset(direction[0], direction[1])) {
                current = current.offset(direction[0], direction[1]);
                if (board.isEmpty(current)) {
                    moves.add(new Move(from, current));
                } else {
                    if (isEnemy(board.getPiece(current))) {
                        moves.add(new Move(from, current));
                    }
                    break;
                }
            }
        }
        return moves;
    }

    /** Hedef kare boşsa veya rakip taş varsa oraya gidilebilir. */
    protected boolean canLandOn(Board board, Position to) {
        return board.isEmpty(to) || isEnemy(board.getPiece(to));
    }

    /** Verilen taş, bu taşın rakibi mi? */
    protected boolean isEnemy(Piece other) {
        return other != null && other.getColor() != color;
    }

}
