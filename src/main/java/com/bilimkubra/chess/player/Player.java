package com.bilimkubra.chess.player;

import com.bilimkubra.chess.core.Board;
import com.bilimkubra.chess.core.Color;
import com.bilimkubra.chess.core.Move;

/**
 * Satranç oynayabilen her şey: insan, rastgele oyuncu veya yapay zekâ.
 * Oyun, karşısında kim olduğunu bilmeden sadece bu arayüzle konuşur.
 */
public interface Player {

    /** Verilen tahtada, verilen renk için oynanacak hamleyi seçer. */
    Move chooseMove(Board board, Color color);

    /** Ekranda gösterilecek oyuncu adı. */
    String getName();
}
