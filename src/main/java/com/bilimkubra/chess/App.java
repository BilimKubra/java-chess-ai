package com.bilimkubra.chess;

import com.bilimkubra.chess.core.Board;

public class App {

    public static void main(String[] args) {
        Board board = Board.initialPosition();
        System.out.println(board);
    }

}
