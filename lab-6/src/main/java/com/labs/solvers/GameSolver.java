package com.labs.solvers;

import com.labs.domain.GameBoard;
import com.labs.domain.BoxBorderPosition;

public abstract class GameSolver {
    protected int referenceColor;
    private final static int cScore = 20;
    private final static int cThree = 15;
    private final static int cTwo = 1;

    protected int heuristic(final GameBoard gameBoard, int color) {
        int value;
        if (referenceColor == GameBoard.FIRST_PLAYER) {
            value = cScore * gameBoard.getFirstPlayerScore() - cScore * gameBoard.getSecondPlayerScore();
        } else {
            value = cScore * gameBoard.getSecondPlayerScore() - cScore * gameBoard.getFirstPlayerScore();
        }
        if (referenceColor == color) {
            value += cThree * gameBoard.getSelectedBoxesNumber(3) - cTwo * gameBoard.getSelectedBoxesNumber(2);
        } else {
            value -= cThree * gameBoard.getSelectedBoxesNumber(3) - cTwo * gameBoard.getSelectedBoxesNumber(2);
        }
        return value;
    }

    public abstract BoxBorderPosition getNextMove(final GameBoard gameBoard, int color);
}
