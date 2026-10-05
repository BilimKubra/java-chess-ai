package com.bilimkubra.chess.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Move sınıfı için testler.
 * Bu testleri geçecek Move sınıfını sen yazacaksın.
 */
class MoveTest {

    @Test
    void storesFromAndTo() {
        Position e2 = Position.fromAlgebraic("e2");
        Position e4 = Position.fromAlgebraic("e4");

        Move move = new Move(e2, e4);

        assertEquals(e2, move.getFrom());
        assertEquals(e4, move.getTo());
    }

    @Test
    void toStringUsesSimpleNotation() {
        Move move = new Move(Position.fromAlgebraic("g1"), Position.fromAlgebraic("f3"));
        assertEquals("g1f3", move.toString());
    }

    @Test
    void movesWithSameSquaresAreEqual() {
        Move a = new Move(Position.fromAlgebraic("e2"), Position.fromAlgebraic("e4"));
        Move b = new Move(Position.fromAlgebraic("e2"), Position.fromAlgebraic("e4"));
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void nullSquaresAreRejected() {
        Position e2 = Position.fromAlgebraic("e2");
        assertThrows(IllegalArgumentException.class, () -> new Move(null, e2));
        assertThrows(IllegalArgumentException.class, () -> new Move(e2, null));
    }

    @Test
    void fromAndToCannotBeTheSameSquare() {
        Position e2 = Position.fromAlgebraic("e2");
        assertThrows(IllegalArgumentException.class, () -> new Move(e2, e2));
    }
}
