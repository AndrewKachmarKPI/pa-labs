package com.labs.solvers;

import com.labs.domain.GameBoard;
import com.labs.domain.BoxBorderPosition;
import com.labs.domain.BoxBorderNode;

import java.util.*;


public class AlphaBettaSolver extends GameSolver {
    final static int ALPHA = Integer.MIN_VALUE, BETA = Integer.MAX_VALUE;
    private int maxLevel;

    public BoxBorderPosition getNextMove(GameBoard gameBoard, int color) {
        referenceColor = color;
        maxLevel = 1;
        BoxBorderPosition best = null;
        while (maxLevel <= gameBoard.getAvailableMoves().size()) {
            BoxBorderNode boxBorderNode = dfs(gameBoard, color, ALPHA, BETA, 0);
            best = boxBorderNode.getBoxBorderPosition();
            maxLevel++;
        }
        return best;
    }

    private BoxBorderNode dfs(GameBoard gameBoard, int color, int alpha, int beta, int level) {
        if (level >= maxLevel) {
            return new BoxBorderNode(null, heuristic(gameBoard, color));
        }
        List<BoxBorderPosition> possibleMoves = gameBoard.getAvailableMoves();
        int size = possibleMoves.size();

        if (size == 0) {
            return new BoxBorderNode(null, heuristic(gameBoard, color));
        }

        //TODO ?
        Collections.shuffle(possibleMoves);
        BoxBorderNode[] neighbours = new BoxBorderNode[size];
        for (int i = 0; i < size; i++) {
            GameBoard newGameBoard = gameBoard.getNewBoard(possibleMoves.get(i), color);
            neighbours[i] = new BoxBorderNode(possibleMoves.get(i), heuristic(newGameBoard, (newGameBoard.getScoreByColor(color) > gameBoard.getScoreByColor(color) ? color : GameBoard.toggleColor(color))));
        }
        Arrays.sort(neighbours);
        possibleMoves = new ArrayList<>();
        if (referenceColor != color) {
            for (int i = 0; i < size; i++) {
                possibleMoves.add(neighbours[i].getBoxBorderPosition());
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                possibleMoves.add(neighbours[i].getBoxBorderPosition());
            }
        }

        if (color == referenceColor) {
            return minimize(gameBoard, color, alpha, beta, level, possibleMoves);
        } else {
            return maximize(gameBoard, color, alpha, beta, level, possibleMoves);
        }
    }

    private BoxBorderNode maximize(GameBoard gameBoard, int color, int alpha, int beta, int level, List<BoxBorderPosition> moves) {
        BoxBorderNode newBoxBorderNode = new BoxBorderNode(null, BETA);

        for (BoxBorderPosition move : moves) {
            GameBoard childGameBoard = gameBoard.getNewBoard(move, color);
            BoxBorderNode boxBorderNode;
            int childScore = childGameBoard.getScoreByColor(color);
            int currentScore = gameBoard.getScoreByColor(color);
            boolean flag = false;
            if (childScore == currentScore) {
                boxBorderNode = dfs(childGameBoard, GameBoard.toggleColor(color), alpha, beta, level + 1);
                flag = true;
            } else{
                boxBorderNode = dfs(childGameBoard, color, alpha, beta, level + 1);
            }

            int childUtility = boxBorderNode.getUtility();
            if (newBoxBorderNode.getUtility() > childUtility) {
                newBoxBorderNode.setUtility(childUtility);
                newBoxBorderNode.setBoxBorderPosition(move);
            }
            if (flag){
                if (childUtility <= alpha){
                    return newBoxBorderNode;
                }
            }

            beta = Math.min(beta, newBoxBorderNode.getUtility());
        }
        return newBoxBorderNode;
    }

    private BoxBorderNode minimize(GameBoard gameBoard, int color, int alpha, int beta, int level, List<BoxBorderPosition> moves) {
        BoxBorderNode newBoxBorderNode = new BoxBorderNode(null, ALPHA);

        for (BoxBorderPosition move : moves) {
            GameBoard child = gameBoard.getNewBoard(move, color);
            BoxBorderNode boxBorderNode;
            int childScore = child.getScoreByColor(color), currentScore = gameBoard.getScoreByColor(color);
            boolean flag = false;
            if (childScore == currentScore) {
                boxBorderNode = dfs(child, GameBoard.toggleColor(color), alpha, beta, level + 1);
                flag = true;
            } else
                boxBorderNode = dfs(child, color, alpha, beta, level + 1);

            int childUtility = boxBorderNode.getUtility();
            if (newBoxBorderNode.getUtility() < childUtility) {
                newBoxBorderNode.setUtility(childUtility);
                newBoxBorderNode.setBoxBorderPosition(move);
            }
            if (flag)
                if (childUtility >= beta)
                    return newBoxBorderNode;

            alpha = Math.max(alpha, newBoxBorderNode.getUtility());
        }
        return newBoxBorderNode;
    }
}
