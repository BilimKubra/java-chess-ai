package com.bilimkubra.chess.core;

import java.util.Objects;

/**
 * Tahtadaki tek bir kare, örneğin "e4".
 *
 * file = sütun (a..h -> 0..7), rank = satır (1..8 -> 0..7).
 *
 * KAPSÜLLEME: Alanlar private ve final. Dışarıdan doğrudan erişilemez ve
 * nesne oluşturulduktan sonra değiştirilemez (immutable). Kurucu metot
 * geçersiz bir kareyi (ör. "z9") reddeder, yani elimizdeki her Position
 * nesnesinin tahtada gerçek bir kare olduğundan emin olabiliriz.
 */
public final class Position {

    private final int file;
    private final int rank;

    public Position(int file, int rank) {
        if (!isOnBoard(file, rank)) {
            throw new IllegalArgumentException(
                    "Tahta dışında bir kare: file=" + file + ", rank=" + rank);
        }
        this.file = file;
        this.rank = rank;
    }

    /** "e4" gibi satranç gösterimini Position nesnesine çevirir. */
    public static Position fromAlgebraic(String square) {
        if (square == null || square.length() != 2) {
            throw new IllegalArgumentException("Geçersiz kare: " + square);
        }
        int file = square.charAt(0) - 'a';   // 'e' - 'a' = 4
        int rank = square.charAt(1) - '1';   // '4' - '1' = 3
        return new Position(file, rank);
    }

    /** Verilen koordinatın 8x8 tahtanın içinde olup olmadığını söyler. */
    public static boolean isOnBoard(int file, int rank) {
        return file >= 0 && file < 8 && rank >= 0 && rank < 8;
    }

    /**
     * Bu kareden (df, dr) kadar kaydırılmış karenin tahtada olup olmadığını söyler.
     * Taşların hamlelerini üretirken önce bunu kontrol edip sonra offset() çağıracağız.
     */
    public boolean canOffset(int df, int dr) {
        return isOnBoard(file + df, rank + dr);
    }

    /** Bu kareden (df, dr) kadar kaydırılmış yeni bir kare döndürür. */
    public Position offset(int df, int dr) {
        return new Position(file + df, rank + dr);
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    /** İki Position aynı kareyi gösteriyorsa eşittir. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position other)) return false;
        return file == other.file && rank == other.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }

    /** Kareyi "e4" biçiminde yazdırır. */
    @Override
    public String toString() {
        return "" + (char) ('a' + file) + (char) ('1' + rank);
    }
}
