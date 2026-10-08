package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color);
    }

    @Override
    public int getValue() {
        return 100;
    }

    @Override
    protected char getSymbol() {
        return 'P';
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();
        int forward = getColor().forwardDirection();
        int startRank = (getColor() == Color.WHITE) ? 1 : 6;

        // 1) Bir kare ileri: sadece boşsa
        if (from.canOffset(0, forward)) {
            Position oneStep = from.offset(0, forward);
            if (board.isEmpty(oneStep)) {
                moves.add(new Move(from, oneStep));

                // 2) İlk hamlede iki kare ileri: arada ve hedefte taş olmamalı
                if (from.getRank() == startRank) {
                    Position twoSteps = from.offset(0, 2 * forward);
                    if (board.isEmpty(twoSteps)) {
                        moves.add(new Move(from, twoSteps));
                    }
                }
            }
        }

        // 3) Çapraz alma: sadece rakip taş varsa
        for (int side : new int[]{-1, 1}) {
            if (from.canOffset(side, forward)) {
                Position target = from.offset(side, forward);
                if (isEnemy(board.getPiece(target))) {
                    moves.add(new Move(from, target));
                }
            }
        }

        return moves;
    }

}
