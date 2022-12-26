package com.labs.solvers;

import com.labs.domain.GameBoard;
import com.labs.domain.BoxBorderPosition;
import com.labs.enums.GameDifficulty;

public abstract class GameSolver {
    protected int currentPlayer;
    private final static int cScore = 20;
    private final static int cThree = 15;
    private final static int cTwo = 1;

    protected int countHeuristic(GameBoard gameBoard, int moveBy) {
        int cost;
        if (currentPlayer == GameBoard.FIRST_PLAYER) {
            cost = cScore * gameBoard.getFirstPlayerScore() - cScore * gameBoard.getSecondPlayerScore();
        } else {
            cost = cScore * gameBoard.getSecondPlayerScore() - cScore * gameBoard.getFirstPlayerScore();
        }
        if (currentPlayer == moveBy) {
            cost += cThree * gameBoard.getSelectedBoxesNumber(3) - cTwo * gameBoard.getSelectedBoxesNumber(2);
        } else {
            cost -= cThree * gameBoard.getSelectedBoxesNumber(3) - cTwo * gameBoard.getSelectedBoxesNumber(2);
        }
        return cost;
    }

    public abstract BoxBorderPosition getNextMove(final GameBoard gameBoard, int moveBy, GameDifficulty difficulty);
}
