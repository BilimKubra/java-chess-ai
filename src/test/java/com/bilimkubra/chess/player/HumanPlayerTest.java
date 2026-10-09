package com.bilimkubra.chess.player;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class HumanPlayerTest {

    @Test
    void parsesValidInput() {
        Move expected = new Move(Position.fromAlgebraic("e2"), Position.fromAlgebraic("e4"));
        assertEquals(expected, HumanPlayer.parse("e2e4"));
    }

    @Test
    void rejectsMalformedInput() {
        assertNull(HumanPlayer.parse("e2"));
        assertNull(HumanPlayer.parse("z9a1"));
        assertNull(HumanPlayer.parse("e2e2"));
    }

    @Test
    void asksAgainUntilALegalMoveIsEntered() {
        // Kullanıcı sırasıyla: yasal olmayan, anlamsız, sonra doğru bir hamle yazıyor
        Scanner fakeKeyboard = new Scanner("e2e5\nmerhaba\n  E2E4  \n");
        Player human = new HumanPlayer(fakeKeyboard, "Test");

        Move chosen = human.chooseMove(Board.initialPosition(), Color.WHITE);

        assertEquals("e2e4", chosen.toString());
    }
}
