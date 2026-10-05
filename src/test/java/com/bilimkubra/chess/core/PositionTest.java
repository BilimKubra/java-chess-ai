package com.bilimkubra.chess.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void fromAlgebraicConvertsToCorrectCoordinates() {
        Position e4 = Position.fromAlgebraic("e4");
        assertEquals(4, e4.getFile());
        assertEquals(3, e4.getRank());
    }

    @Test
    void toStringReturnsAlgebraicNotation() {
        assertEquals("a1", new Position(0, 0).toString());
        assertEquals("h8", new Position(7, 7).toString());
    }

    @Test
    void positionsWithSameSquareAreEqual() {
        assertEquals(Position.fromAlgebraic("d5"), new Position(3, 4));
    }

    @Test
    void offFieldSquareIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Position(8, 0));
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraic("z9"));
    }

    @Test
    void offsetMovesToNeighbourSquare() {
        Position e4 = Position.fromAlgebraic("e4");
        assertEquals(Position.fromAlgebraic("f6"), e4.offset(1, 2));   // at hamlesi
        assertFalse(new Position(0, 0).canOffset(-1, 0));               // a1'in solu yok
    }
}
