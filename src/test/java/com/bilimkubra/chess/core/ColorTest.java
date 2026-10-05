package com.bilimkubra.chess.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorTest {

    @Test
    void oppositeSwitchesSides() {
        assertEquals(Color.BLACK, Color.WHITE.opposite());
        assertEquals(Color.WHITE, Color.BLACK.opposite());
    }

    @Test
    void pawnsMoveInOppositeDirections() {
        assertEquals(1, Color.WHITE.forwardDirection());
        assertEquals(-1, Color.BLACK.forwardDirection());
    }
}
