package com.bilimkubra.chess.pieces;

import com.bilimkubra.chess.core.Color;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    @Test
    void eachPieceKnowsItsOwnValue() {
        assertEquals(100,   new Pawn(Color.WHITE).getValue());
        assertEquals(320,   new Knight(Color.WHITE).getValue());
        assertEquals(330,   new Bishop(Color.WHITE).getValue());
        assertEquals(500,   new Rook(Color.WHITE).getValue());
        assertEquals(900,   new Queen(Color.WHITE).getValue());
        assertEquals(20000, new King(Color.WHITE).getValue());
    }

    @Test
    void piecesCanBeTreatedAsPieceType() {
        // POLİMORFİZM: Farklı türleri aynı "Piece" listesinde tutuyoruz
        List<Piece> blackArmy = List.of(
                new Rook(Color.BLACK),
                new Knight(Color.BLACK),
                new Bishop(Color.BLACK),
                new Queen(Color.BLACK)
        );

        int total = 0;
        for (Piece piece : blackArmy) {
            total += piece.getValue();   // Hangi taş olduğunu bilmeden doğru değer gelir
            assertEquals(Color.BLACK, piece.getColor());
        }

        assertEquals(500 + 320 + 330 + 900, total);
    }

    @Test
    void pieceWithoutColorIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Knight(null));
    }
}
