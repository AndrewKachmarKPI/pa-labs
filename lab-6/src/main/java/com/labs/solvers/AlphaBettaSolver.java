package com.labs.solvers;

import com.labs.domain.GameBoard;
import com.labs.domain.BoxBorderPosition;
import com.labs.domain.BoxBorderNode;

import java.util.*;
import java.util.stream.Collectors;


public class AlphaBettaSolver extends GameSolver {
    final static int ALPHA = Integer.MIN_VALUE, BETA = Integer.MAX_VALUE;
    private int depth;
    private long moveTime = 1900000000;

    public BoxBorderPosition getNextMove(GameBoard gameBoard, int color) {
        referenceColor = color;
        depth = 1;
        BoxBorderPosition best = null;
        while (depth <= gameBoard.getAvailableMoves().size()) {
            BoxBorderNode boxBorderNode = searchBestPosition(gameBoard, color, ALPHA, BETA, 0);
            best = boxBorderNode.getBoxBorderPosition();
            depth++;
        }
        return best;
    }

    private BoxBorderNode searchBestPosition(GameBoard gameBoard, int color, int alpha, int beta, int level) {
        System.out.println("SEARCH level" + level);
        if (level >= depth) {
            return new BoxBorderNode(null, heuristic(gameBoard, color));
        }
        List<BoxBorderPosition> possibleMoves = gameBoard.getAvailableMoves();
        if (possibleMoves.isEmpty()) {
            return new BoxBorderNode(null, heuristic(gameBoard, color));
        }

        int size = possibleMoves.size();
        List<BoxBorderNode> successors = generateSuccessors(gameBoard, color, possibleMoves, size);
        possibleMoves = successors.stream().map(BoxBorderNode::getBoxBorderPosition).collect(Collectors.toList());

        if (color == referenceColor) {
            Collections.reverse(possibleMoves);
            return minimize(gameBoard, color, alpha, beta, level, possibleMoves);
        } else {
            return maximize(gameBoard, color, alpha, beta, level, possibleMoves);
        }
    }

    private List<BoxBorderNode> generateSuccessors(GameBoard gameBoard, int color, List<BoxBorderPosition> possibleMoves, int size) {
        List<BoxBorderNode> successors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            GameBoard newGameBoard = gameBoard.getNewBoard(possibleMoves.get(i), color);
            int moveBy;
            if (newGameBoard.getScoreByColor(color) > gameBoard.getScoreByColor(color)) {
                moveBy = color;
            } else {
                moveBy = GameBoard.toggleColor(color);
            }
            successors.add(new BoxBorderNode(possibleMoves.get(i), heuristic(newGameBoard, moveBy)));
        }
        Collections.sort(successors);
        return successors;
    }

    private BoxBorderNode maximize(GameBoard gameBoard, int color, int alpha, int beta, int level, List<BoxBorderPosition> moves) {
        BoxBorderNode newBoxBorderNode = new BoxBorderNode(null, BETA);
        for (BoxBorderPosition move : moves) {
            GameBoard childGameBoard = gameBoard.getNewBoard(move, color);
            BoxBorderNode boxBorderNode;
            int childScore = childGameBoard.getScoreByColor(color);
            int currentScore = gameBoard.getScoreByColor(color);
            boolean isScoreEqual = false;
            if (childScore == currentScore) {
                boxBorderNode = searchBestPosition(childGameBoard, GameBoard.toggleColor(color), alpha, beta, level + 1);
                isScoreEqual = true;
            } else {
                boxBorderNode = searchBestPosition(childGameBoard, color, alpha, beta, level + 1);
            }

            int childUtility = boxBorderNode.getUtility();
            if (newBoxBorderNode.getUtility() > childUtility) {
                newBoxBorderNode.setUtility(childUtility);
                newBoxBorderNode.setBoxBorderPosition(move);
            }
            if (isScoreEqual && childUtility <= alpha) {
                return newBoxBorderNode;
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
            boolean isScoreEqual = false;
            if (childScore == currentScore) {
                boxBorderNode = searchBestPosition(child, GameBoard.toggleColor(color), alpha, beta, level + 1);
                isScoreEqual = true;
            } else {
                boxBorderNode = searchBestPosition(child, color, alpha, beta, level + 1);
            }

            int childUtility = boxBorderNode.getUtility();
            if (newBoxBorderNode.getUtility() < childUtility) {
                newBoxBorderNode.setUtility(childUtility);
                newBoxBorderNode.setBoxBorderPosition(move);
            }
            if (isScoreEqual && childUtility >= beta) {
                return newBoxBorderNode;
            }
            alpha = Math.max(alpha, newBoxBorderNode.getUtility());
        }
        return newBoxBorderNode;
    }
}
