package com.bilimkubra.chess.ai;

import com.bilimkubra.chess.core.Board;

/**
 * Bir pozisyonun ne kadar iyi olduğunu tahmin eden fonksiyon.
 * Pozitif değer beyazın, negatif değer siyahın üstün olduğunu gösterir (centipawn).
 */
public interface Evaluator {

    int evaluate(Board board);
}
