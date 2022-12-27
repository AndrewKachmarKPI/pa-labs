package com.labs.solvers;

import com.labs.domain.GameBoard;
import com.labs.domain.BoxBorderPosition;
import com.labs.domain.BoxBorderNode;
import com.labs.enums.GameDifficulty;
import com.labs.service.GameConstants;

import java.util.*;
import java.util.stream.Collectors;


public class AlphaBettaSolver implements GameConstants {
    private int currentPlayer;
    private final static int ALPHA = Integer.MIN_VALUE, BETA = Integer.MAX_VALUE;
    private int depth;
    private long startTime;

    public BoxBorderPosition getNextMove(GameBoard gameBoard, int moveBy, GameDifficulty difficulty) {
        depth = 1;
        startTimer();
        currentPlayer = moveBy;
        return searchPosition(gameBoard, moveBy, difficulty);
    }

    private BoxBorderPosition searchPosition(GameBoard gameBoard, int moveBy, GameDifficulty difficulty) {
        BoxBorderPosition position = new BoxBorderPosition();
        switch (difficulty) {
            case EASY: {
                position = randomSearch(gameBoard);
                break;
            }
            case MEDIUM: {
                position = greedySearch(gameBoard);
                break;
            }
            case HARD: {
                position = deepSearch(gameBoard, moveBy);
                break;
            }
        }
        return position;
    }

    private BoxBorderPosition randomSearch(GameBoard gameBoard) {
        Random rand = new Random();
        List<BoxBorderPosition> possibleMoves = gameBoard.getAvailableMoves();
        return possibleMoves.get(rand.nextInt(possibleMoves.size()));
    }


    private BoxBorderPosition deepSearch(GameBoard gameBoard, int moveBy) {
        BoxBorderPosition borderPosition = new BoxBorderPosition();
        while (depth <= gameBoard.getAvailableMoves().size()) {
            BoxBorderNode boxBorderNode = searchBestPosition(gameBoard, moveBy, ALPHA, BETA, 0);
            if (isTimeOut()) {
                break;
            }
            borderPosition = boxBorderNode.getBoxBorderPosition();
            depth++;
        }
        return borderPosition;
    }

    private BoxBorderPosition greedySearch(GameBoard gameBoard) {
        List<BoxBorderPosition> availableMoves = gameBoard.getAvailableMoves();
        List<Integer> boardsUtility = new ArrayList<>();
        Collections.shuffle(availableMoves);
        for (BoxBorderPosition availableMove : availableMoves) {
            GameBoard board = gameBoard.getNewBoard(availableMove, currentPlayer);
            int moveBy = currentPlayer;
            if (board.getScoreByPlayerIndex(currentPlayer) < gameBoard.getScoreByPlayerIndex(currentPlayer)) {
                moveBy = GameBoard.togglePlayer(currentPlayer);
            }
            boardsUtility.add(countUtility(board, moveBy));
        }
        Integer bestBoard = Collections.max(boardsUtility);
        return availableMoves.get(boardsUtility.indexOf(bestBoard));
    }

    private BoxBorderNode searchBestPosition(GameBoard gameBoard, int moveBy, int alpha, int beta, int level) {
        if (level < depth) {
            List<BoxBorderPosition> availableMoves = gameBoard.getAvailableMoves();
            if (availableMoves.isEmpty()) {
                return new BoxBorderNode(null, countUtility(gameBoard, moveBy));
            }
            List<BoxBorderNode> successors = generateSuccessors(gameBoard, moveBy, availableMoves, availableMoves.size());
            availableMoves = successors.stream().map(BoxBorderNode::getBoxBorderPosition).collect(Collectors.toList());
            if (moveBy == currentPlayer) {
                return minimize(gameBoard, moveBy, alpha, beta, level, availableMoves);
            } else {
                return maximize(gameBoard, moveBy, alpha, beta, level, availableMoves);
            }
        } else {
            return new BoxBorderNode(null, countUtility(gameBoard, moveBy));
        }
    }

    private List<BoxBorderNode> generateSuccessors(GameBoard gameBoard, int playerIndex, List<BoxBorderPosition> possibleMoves, int size) {
        List<BoxBorderNode> successors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            GameBoard newGameBoard = gameBoard.getNewBoard(possibleMoves.get(i), playerIndex);
            int moveBy = playerIndex;
            if (newGameBoard.getScoreByPlayerIndex(playerIndex) < gameBoard.getScoreByPlayerIndex(playerIndex)) {
                moveBy = GameBoard.togglePlayer(playerIndex);
            }
            successors.add(new BoxBorderNode(possibleMoves.get(i), countUtility(newGameBoard, moveBy)));
        }
        Collections.sort(successors);
        return successors;
    }

    private BoxBorderNode maximize(GameBoard gameBoard, int moveBy, int alpha, int beta, int level, List<BoxBorderPosition> moves) {
        BoxBorderNode newBoxBorderNode = new BoxBorderNode(null, BETA);
        for (BoxBorderPosition move : moves) {
            GameBoard childGameBoard = gameBoard.getNewBoard(move, moveBy);
            BoxBorderNode boxBorderNode;
            boolean isScoreEqual = false;
            if (childGameBoard.getScoreByPlayerIndex(moveBy) == gameBoard.getScoreByPlayerIndex(moveBy)) {
                boxBorderNode = searchBestPosition(childGameBoard, GameBoard.togglePlayer(moveBy), alpha, beta, level + 1);
                isScoreEqual = true;
            } else {
                boxBorderNode = searchBestPosition(childGameBoard, moveBy, alpha, beta, level + 1);
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

    private BoxBorderNode minimize(GameBoard gameBoard, int moveBy, int alpha, int beta, int level, List<BoxBorderPosition> moves) {
        BoxBorderNode newBoxBorderNode = new BoxBorderNode(null, ALPHA);

        for (BoxBorderPosition move : moves) {
            GameBoard child = gameBoard.getNewBoard(move, moveBy);
            BoxBorderNode boxBorderNode;
            int childScore = child.getScoreByPlayerIndex(moveBy), currentScore = gameBoard.getScoreByPlayerIndex(moveBy);
            boolean isScoreEqual = false;
            if (childScore == currentScore) {
                boxBorderNode = searchBestPosition(child, GameBoard.togglePlayer(moveBy), alpha, beta, level + 1);
                isScoreEqual = true;
            } else {
                boxBorderNode = searchBestPosition(child, moveBy, alpha, beta, level + 1);
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

    private void startTimer() {
        startTime = System.nanoTime();
    }

    private boolean isTimeOut() {
        return (System.nanoTime() - startTime) > moveTime;
    }

    private int countUtility(GameBoard gameBoard, int moveBy) {
        int cost;
        if (currentPlayer == GameBoard.FIRST_PLAYER) {
            cost = gameBoard.getFirstPlayerScore() - gameBoard.getSecondPlayerScore();
        } else {
            cost = gameBoard.getSecondPlayerScore() - gameBoard.getFirstPlayerScore();
        }
        if (currentPlayer == moveBy) {
            cost += gameBoard.getSelectedBoxesNumber(3) - gameBoard.getSelectedBoxesNumber(2);
        } else {
            cost -= gameBoard.getSelectedBoxesNumber(3) - gameBoard.getSelectedBoxesNumber(2);
        }
        return cost;
    }
}
