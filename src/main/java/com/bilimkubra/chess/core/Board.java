package com.bilimkubra.chess.core;

import com.bilimkubra.chess.pieces.King;
import com.bilimkubra.chess.pieces.Pawn;
import com.bilimkubra.chess.pieces.Piece;
import com.bilimkubra.chess.pieces.PieceFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Board {

    /** Standart başlangıç pozisyonunun FEN gösterimi. */
    public static final String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -";

    /** Dört rok hakkı. Değişmez (record); her hamlede yenisi üretilir. */
    private record CastlingRights(boolean whiteKingside, boolean whiteQueenside,
                                  boolean blackKingside, boolean blackQueenside) {

        static final CastlingRights NONE = new CastlingRights(false, false, false, false);

        static CastlingRights fromFen(String text) {
            return new CastlingRights(text.contains("K"), text.contains("Q"),
                    text.contains("k"), text.contains("q"));
        }

        boolean allows(Color color, boolean kingside) {
            if (color == Color.WHITE) {
                return kingside ? whiteKingside : whiteQueenside;
            }
            return kingside ? blackKingside : blackQueenside;
        }

        /** Şah veya kale ilk karesinden oynarsa (ya da kale alınırsa) ilgili hak kaybolur. */
        CastlingRights afterMove(Position from, Position to) {
            return new CastlingRights(
                    whiteKingside && untouched(from, to, "e1", "h1"),
                    whiteQueenside && untouched(from, to, "e1", "a1"),
                    blackKingside && untouched(from, to, "e8", "h8"),
                    blackQueenside && untouched(from, to, "e8", "a8"));
        }

        private static boolean untouched(Position from, Position to, String... squares) {
            for (String square : squares) {
                Position p = Position.fromAlgebraic(square);
                if (p.equals(from) || p.equals(to)) {
                    return false;
                }
            }
            return true;
        }
    }

    /** Oynanmış bir hamlenin, geri alınabilmesi için gereken tüm bilgisi. */
    private record MoveRecord(Move move, Piece moved, Piece captured, Position capturedSquare,
                              CastlingRights previousCastlingRights, Position previousEnPassantTarget) {
    }

    private final Piece[][] squares = new Piece[8][8];
    private final Deque<MoveRecord> history = new ArrayDeque<>();
    private CastlingRights castlingRights = CastlingRights.NONE;
    private Position enPassantTarget = null;

    /** Standart satranç başlangıç dizilimiyle hazır bir tahta üretir. */
    public static Board initialPosition() {
        return fromFen(START_FEN);
    }

    /**
     * FEN metninden tahta üretir. Örnek: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -"
     * Kullanılan alanlar: taş dizilimi, rok hakları, geçerken alma karesi.
     */
    public static Board fromFen(String fen) {
        String[] parts = fen.trim().split("\\s+");
        Board board = new Board();

        String[] rows = parts[0].split("/");
        if (rows.length != 8) {
            throw new IllegalArgumentException("FEN 8 satır içermeli: " + fen);
        }
        for (int i = 0; i < 8; i++) {
            int rank = 7 - i;
            int file = 0;
            for (char c : rows[i].toCharArray()) {
                if (Character.isDigit(c)) {
                    file += c - '0';
                } else {
                    board.setPiece(new Position(file, rank), PieceFactory.fromSymbol(c));
                    file++;
                }
            }
        }

        if (parts.length > 2) {
            board.castlingRights = CastlingRights.fromFen(parts[2]);
        }
        if (parts.length > 3 && !parts[3].equals("-")) {
            board.enPassantTarget = Position.fromAlgebraic(parts[3]);
        }
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

    /** Geçerken alma ile gidilebilecek kare (yoksa null). */
    public Position getEnPassantTarget() {
        return enPassantTarget;
    }

    /** Hamleyi tahtada oynar ve geri alınabilmesi için geçmişe kaydeder. */
    public void makeMove(Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();
        Piece moved = getPiece(from);
        if (moved == null) {
            throw new IllegalArgumentException("Başlangıç karesinde taş yok: " + from);
        }

        Piece captured = getPiece(to);
        Position capturedSquare = to;

        // Geçerken alma: piyon boş hedef kareye çapraz gidiyor, alınan piyon yanında duruyor
        if (moved instanceof Pawn && to.equals(enPassantTarget) && captured == null) {
            capturedSquare = new Position(to.getFile(), from.getRank());
            captured = getPiece(capturedSquare);
            setPiece(capturedSquare, null);
        }

        history.push(new MoveRecord(move, moved, captured, capturedSquare, castlingRights, enPassantTarget));

        setPiece(from, null);
        setPiece(to, move.isPromotion() ? PieceFactory.create(move.getPromotion(), moved.getColor()) : moved);

        // Rok: şah iki kare gidince kale de şahın öbür yanına geçer
        if (isCastling(moved, move)) {
            moveCastlingRook(move, false);
        }

        // Piyon iki kare ilerlediyse, geçtiği kare geçerken alma hedefi olur
        enPassantTarget = null;
        if (moved instanceof Pawn && Math.abs(to.getRank() - from.getRank()) == 2) {
            enPassantTarget = new Position(from.getFile(), (from.getRank() + to.getRank()) / 2);
        }

        castlingRights = castlingRights.afterMove(from, to);
    }

    /** Son oynanan hamleyi geri alır; alınan taşı, rok kalesini ve hakları eski hâline getirir. */
    public void undoMove() {
        if (history.isEmpty()) {
            throw new IllegalStateException("Geri alınacak hamle yok");
        }
        MoveRecord last = history.pop();
        Move move = last.move();

        if (isCastling(last.moved(), move)) {
            moveCastlingRook(move, true);
        }

        setPiece(move.getTo(), null);
        setPiece(move.getFrom(), last.moved());
        if (last.captured() != null) {
            setPiece(last.capturedSquare(), last.captured());
        }

        castlingRights = last.previousCastlingRights();
        enPassantTarget = last.previousEnPassantTarget();
    }

    /** Verilen renkteki tüm taşların sözde-yasal hamleleri (rok hariç). */
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

    /** Verilen kare, verilen renk tarafından tehdit ediliyor mu? (Kare boş olsa bile.) */
    public boolean isSquareAttacked(Position square, Color attacker) {
        Piece original = getPiece(square);
        if (original == null) {
            // Piyonlar boş kareye çapraz gidemez; geçici bir hedef koyarak çapraz tehdidi de görürüz
            setPiece(square, new Pawn(attacker.opposite()));
        }
        boolean attacked = false;
        for (Move move : getPseudoLegalMoves(attacker)) {
            if (move.getTo().equals(square)) {
                attacked = true;
                break;
            }
        }
        setPiece(square, original);
        return attacked;
    }

    /** Verilen rengin şahı şu an rakip tarafından tehdit ediliyor mu? */
    public boolean isInCheck(Color color) {
        Position kingSquare = findKing(color);
        return kingSquare != null && isSquareAttacked(kingSquare, color.opposite());
    }

    /** Yasal hamleler: Oynandıktan sonra kendi şahını tehdit altında bırakmayan hamleler. */
    public List<Move> getLegalMoves(Color color) {
        List<Move> candidates = getPseudoLegalMoves(color);
        candidates.addAll(getCastlingMoves(color));

        List<Move> legalMoves = new ArrayList<>();
        for (Move move : candidates) {
            makeMove(move);
            if (!isInCheck(color)) {
                legalMoves.add(move);
            }
            undoMove();
        }
        return legalMoves;
    }

    /**
     * Sırası gelen tarafa göre oyunun durumu.
     * Yasal hamle yoksa: şah tehdit altındaysa mat, değilse pat.
     */
    public GameStatus getStatus(Color sideToMove) {
        if (!getLegalMoves(sideToMove).isEmpty()) {
            return GameStatus.ONGOING;
        }
        return isInCheck(sideToMove) ? GameStatus.CHECKMATE : GameStatus.STALEMATE;
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

    // ---- Rok ----

    private List<Move> getCastlingMoves(Color color) {
        List<Move> moves = new ArrayList<>();
        int rank = (color == Color.WHITE) ? 0 : 7;
        Position kingSquare = new Position(4, rank);
        if (!(getPiece(kingSquare) instanceof King) || isInCheck(color)) {
            return moves;
        }
        Color enemy = color.opposite();

        // Kısa rok: f ve g boş ve tehdit altında değil
        if (castlingRights.allows(color, true)
                && isEmpty(new Position(5, rank)) && isEmpty(new Position(6, rank))
                && !isSquareAttacked(new Position(5, rank), enemy)
                && !isSquareAttacked(new Position(6, rank), enemy)) {
            moves.add(new Move(kingSquare, new Position(6, rank)));
        }

        // Uzun rok: b, c, d boş; şahın geçtiği c ve d tehdit altında değil
        if (castlingRights.allows(color, false)
                && isEmpty(new Position(1, rank)) && isEmpty(new Position(2, rank))
                && isEmpty(new Position(3, rank))
                && !isSquareAttacked(new Position(3, rank), enemy)
                && !isSquareAttacked(new Position(2, rank), enemy)) {
            moves.add(new Move(kingSquare, new Position(2, rank)));
        }
        return moves;
    }

    private static boolean isCastling(Piece moved, Move move) {
        return moved instanceof King
                && Math.abs(move.getTo().getFile() - move.getFrom().getFile()) == 2;
    }

    /** Rokta kaleyi taşır (undo = true ise geri taşır). */
    private void moveCastlingRook(Move move, boolean undo) {
        int rank = move.getFrom().getRank();
        boolean kingside = move.getTo().getFile() == 6;
        Position rookHome = new Position(kingside ? 7 : 0, rank);
        Position rookCastled = new Position(kingside ? 5 : 3, rank);

        Position from = undo ? rookCastled : rookHome;
        Position to = undo ? rookHome : rookCastled;
        setPiece(to, getPiece(from));
        setPiece(from, null);
    }

    private Position findKing(Color color) {
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece piece = squares[file][rank];
                if (piece instanceof King && piece.getColor() == color) {
                    return new Position(file, rank);
                }
            }
        }
        return null;
    }

}
