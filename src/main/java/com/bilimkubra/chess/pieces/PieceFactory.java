package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Color;

/**
 * Factory (fabrika) deseni: Bir harften doğru taş nesnesini üretir.
 * Büyük harf beyaz, küçük harf siyah taştır (FEN gösterimi): 'N' -> beyaz at, 'q' -> siyah vezir.
 */
public final class PieceFactory {

    private PieceFactory() {
        // Sadece statik metot içerir; nesnesi oluşturulamaz.
    }

    public static Piece fromSymbol(char symbol) {
        Color color = Character.isUpperCase(symbol) ? Color.WHITE : Color.BLACK;
        return create(Character.toUpperCase(symbol), color);
    }

    public static Piece create(char upperSymbol, Color color) {
        return switch (upperSymbol) {
            case 'P' -> new Pawn(color);
            case 'N' -> new Knight(color);
            case 'B' -> new Bishop(color);
            case 'R' -> new Rook(color);
            case 'Q' -> new Queen(color);
            case 'K' -> new King(color);
            default -> throw new IllegalArgumentException("Bilinmeyen taş harfi: " + upperSymbol);
        };
    }
}
