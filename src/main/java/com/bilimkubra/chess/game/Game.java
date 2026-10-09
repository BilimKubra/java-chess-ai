package com.bilimkubra.chess.game;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.GameStatus;
import com.bilimkubra.chess.core.Move;
import com.bilimkubra.chess.player.Player;

/** Bir satranç oyununu baştan sona yöneten sınıf. */
public class Game {

    private final Board board;
    private final Player white;
    private final Player black;
    private Color sideToMove = Color.WHITE;

    public Game(Board board, Player white, Player black) {
        this.board = board;
        this.white = white;
        this.black = black;
    }

    /**
     * Oyun bitene veya hamle sınırına ulaşılana kadar oynatır.
     * @param maxPlies en fazla kaç yarım hamle (beyaz + siyah ayrı sayılır) oynanacağı
     * @param printBoard her hamlede tahta ekrana çizilsin mi
     * @return oyunun son durumu
     */
    public GameStatus play(int maxPlies, boolean printBoard) {
        for (int ply = 0; ply < maxPlies; ply++) {
            GameStatus status = board.getStatus(sideToMove);
            if (status != GameStatus.ONGOING) {
                announce(status);
                return status;
            }

            if (printBoard) {
                System.out.println(board);
            }

            Player current = (sideToMove == Color.WHITE) ? white : black;
            Move move = current.chooseMove(board, sideToMove);
            board.makeMove(move);
            if (printBoard) {
                System.out.println(current.getName() + " oynadı: " + move + "\n");
            }

            sideToMove = sideToMove.opposite();
        }
        System.out.println("Hamle sınırına ulaşıldı.");
        return GameStatus.ONGOING;
    }

    public Color getSideToMove() {
        return sideToMove;
    }

    private void announce(GameStatus status) {
        System.out.println(board);
        if (status == GameStatus.CHECKMATE) {
            Player winner = (sideToMove == Color.WHITE) ? black : white;
            System.out.println("ŞAH MAT! Kazanan: " + winner.getName());
        } else {
            System.out.println("PAT! Oyun berabere.");
        }
    }
}
