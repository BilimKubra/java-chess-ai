package com.bilimkubra.chess.core;

/**
 * Satrançtaki iki taraf: beyaz ve siyah.
 *
 * enum, sabit ve sınırlı sayıda değeri olan bir türdür. Renk için String
 * ("white") kullanmak yerine enum kullanırsak yazım hatası yapmamız imkânsız
 * olur; derleyici sadece WHITE ve BLACK değerlerine izin verir.
 */
public enum Color {
    WHITE,
    BLACK;

    /** Karşı tarafın rengini döndürür. Sıra değiştirirken kullanacağız. */
    public Color opposite() {
        return this == WHITE ? BLACK : WHITE;
    }

    /**
     * Piyonların ilerleme yönü: beyaz yukarı (+1), siyah aşağı (-1).
     * Bu bilgiyi renge koymak, Pawn sınıfında if/else yazmamızı engeller.
     */
    public int forwardDirection() {
        return this == WHITE ? 1 : -1;
    }
}
