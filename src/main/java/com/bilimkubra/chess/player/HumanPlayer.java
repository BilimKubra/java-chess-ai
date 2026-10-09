package com.bilimkubra.chess.player;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.core.Position;

import java.util.List;
import java.util.Scanner;

/** Hamlesini klavyeden "e2e4" biçiminde okuyan oyuncu. */
public class HumanPlayer implements Player {

    private final Scanner input;
    private final String name;

    public HumanPlayer(Scanner input, String name) {
        this.input = input;
        this.name = name;
    }

    @Override
    public Move chooseMove(Board board, Color color) {
        List<Move> legalMoves = board.getLegalMoves(color);
        while (true) {
            System.out.print(name + " (" + color + ") hamlen (ör. e2e4, terfi: e7e8q): ");
            String text = input.nextLine().trim().toLowerCase();

            Move move = parse(text);
            if (move == null) {
                System.out.println("Anlaşılamadı. Biçim: e2e4");
            } else if (!legalMoves.contains(move)) {
                System.out.println("Yasal değil: " + move);
            } else {
                return move;
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * "e2e4" metnini Move nesnesine çevirir; geçersizse null döndürür.
     * Terfi için 5. harf taşı belirtir: "e7e8q" (vezir), "e7e8n" (at)...
     */
    static Move parse(String text) {
        if (text.length() != 4 && text.length() != 5) {
            return null;
        }
        try {
            Position from = Position.fromAlgebraic(text.substring(0, 2));
            Position to = Position.fromAlgebraic(text.substring(2, 4));
            char promotion = (text.length() == 5)
                    ? Character.toUpperCase(text.charAt(4))
                    : Move.NO_PROMOTION;
            return new Move(from, to, promotion);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
