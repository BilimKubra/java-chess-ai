package com.bilimkubra.chess.core;

import com.bilimkubra.chess.pieces.Bishop;
import com.bilimkubra.chess.pieces.King;
import com.bilimkubra.chess.pieces.Knight;
import com.bilimkubra.chess.pieces.Pawn;
import com.bilimkubra.chess.pieces.Piece;
import com.bilimkubra.chess.pieces.Queen;
import com.bilimkubra.chess.pieces.Rook;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Board {

    /** Oynanmış bir hamlenin, geri alınabilmesi için gereken tüm bilgisi. */
    private record MoveRecord(Move move, Piece moved, Piece captured) {
    }

    private final Piece[][] squares = new Piece[8][8];
    private final Deque<MoveRecord> history = new ArrayDeque<>();

    /** Standart satranç başlangıç dizilimiyle hazır bir tahta üretir. */
    public static Board initialPosition() {
        Board board = new Board();
        board.placeBackRank(Color.WHITE, 0);
        board.placePawns(Color.WHITE, 1);
        board.placePawns(Color.BLACK, 6);
        board.placeBackRank(Color.BLACK, 7);
        return board;
    }

    public Piece getPiece(Position position) {
        return squares[position.getFile()][position.getRank()];
    }

    public void setPiece(Position position, Piece piece) {
        squares[position.getFile()][position.getRank()] = piece;
    }

    public boolean isEmpty(Position position) {
        return getPiece(position) == null;
    }

    /** Hamleyi tahtada oynar ve geri alınabilmesi için geçmişe kaydeder. */
    public void makeMove(Move move) {
        Piece moved = getPiece(move.getFrom());
        if (moved == null) {
            throw new IllegalArgumentException("Başlangıç karesinde taş yok: " + move.getFrom());
        }
        Piece captured = getPiece(move.getTo());

        setPiece(move.getTo(), moved);
        setPiece(move.getFrom(), null);

        history.push(new MoveRecord(move, moved, captured));
    }

    /** Son oynanan hamleyi geri alır; alınan taş varsa yerine koyar. */
    public void undoMove() {
        if (history.isEmpty()) {
            throw new IllegalStateException("Geri alınacak hamle yok");
        }
        MoveRecord last = history.pop();

        setPiece(last.move().getFrom(), last.moved());
        setPiece(last.move().getTo(), last.captured());
    }

    /** Verilen renkteki tüm taşların sözde-yasal hamleleri. */
    public List<Move> getPseudoLegalMoves(Color color) {
        List<Move> moves = new ArrayList<>();
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece piece = squares[file][rank];
                if (piece != null && piece.getColor() == color) {
                    moves.addAll(piece.getPseudoLegalMoves(this, new Position(file, rank)));
                }
            }
        }
        return moves;
    }

    /** Tahtayı beyazın bakış açısından (8. sıra üstte) metin olarak çizer. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int rank = 7; rank >= 0; rank--) {
            sb.append(rank + 1).append("  ");
            for (int file = 0; file < 8; file++) {
                Piece piece = squares[file][rank];
                sb.append(piece == null ? "." : piece.toString()).append(' ');
            }
            sb.append('\n');
        }
        sb.append("\n   a b c d e f g h\n");
        return sb.toString();
    }

    private void placePawns(Color color, int rank) {
        for (int file = 0; file < 8; file++) {
            setPiece(new Position(file, rank), new Pawn(color));
        }
    }

    private void placeBackRank(Color color, int rank) {
        Piece[] pieces = {
                new Rook(color), new Knight(color), new Bishop(color), new Queen(color),
                new King(color), new Bishop(color), new Knight(color), new Rook(color)
        };
        for (int file = 0; file < 8; file++) {
            setPiece(new Position(file, rank), pieces[file]);
        }
    }

}
