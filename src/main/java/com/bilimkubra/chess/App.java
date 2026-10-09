package com.bilimkubra.chess;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.game.Game;
import com.bilimkubra.chess.player.HumanPlayer;
import com.bilimkubra.chess.player.Player;
import com.bilimkubra.chess.player.RandomPlayer;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        Player white = new HumanPlayer(keyboard, "Kübra");
        Player black = new RandomPlayer();

        new Game(Board.initialPosition(), white, black).play(500, true);
    }

}
