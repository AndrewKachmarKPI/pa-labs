package com.labs.domain;

import com.labs.service.GameConstants;
import javafx.scene.layout.VBox;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GameBoard implements GameConstants {
    private VBox gameBoardVBox;
    private List<GameBox> gameBoxes;

    private int[][] horizontalBorders, verticalBorders, boxBorders;
    private int size, firstPlayerScore, secondPlayerScore;

    public GameBoard(int size) {
        this.size = size;
        horizontalBorders = new int[size - 1][size];
        verticalBorders = new int[size][size - 1];
        boxBorders = new int[size - 1][size - 1];
        fillBordersWithEmpty(horizontalBorders);
        fillBordersWithEmpty(verticalBorders);
        fillBordersWithEmpty(boxBorders);
        firstPlayerScore = 0;
        secondPlayerScore = 0;
    }

    public GameBoard copyGameBoard() {
        GameBoard newGameBoard = new GameBoard(size);
        for (int i = 0; i < (size - 1); i++) {
            System.arraycopy(horizontalBorders[i], 0, newGameBoard.horizontalBorders[i], 0, size);
        }
        for (int i = 0; i < size; i++) {
            System.arraycopy(verticalBorders[i], 0, newGameBoard.verticalBorders[i], 0, size - 1);
        }
        for (int i = 0; i < (size - 1); i++) {
            System.arraycopy(boxBorders[i], 0, newGameBoard.boxBorders[i], 0, size - 1);
        }
        newGameBoard.firstPlayerScore = firstPlayerScore;
        newGameBoard.secondPlayerScore = secondPlayerScore;
        newGameBoard.gameBoardVBox = gameBoardVBox;
        newGameBoard.gameBoxes = gameBoxes;
        return newGameBoard;
    }

    private void fillBordersWithEmpty(int[][] borders) {
        for (int[] pos : borders) {
            Arrays.fill(pos, GameBoard.EMPTY);
        }
    }

    public int getScoreByPlayerIndex(int player) {
        int score;
        if (player == FIRST_PLAYER) {
            score = firstPlayerScore;
        } else {
            score = secondPlayerScore;
        }
        return score;
    }

    public static int togglePlayer(int moveBy) {
        if (moveBy == FIRST_PLAYER) {
            return SECOND_PLAYER;
        } else {
            return FIRST_PLAYER;
        }
    }

    public List<BoxBorderPosition> getAvailableMoves() {
        List<BoxBorderPosition> availableMoves = new ArrayList<>();
        availableMoves.addAll(getHorizontalMoves());
        availableMoves.addAll(getVerticalMoves());
        return availableMoves;
    }

    private List<BoxBorderPosition> getHorizontalMoves() {
        List<BoxBorderPosition> availableMoves = new ArrayList<>();
        for (int i = 0; i < (size - 1); i++) {
            for (int j = 0; j < size; j++) {
                if (horizontalBorders[i][j] == EMPTY) {
                    availableMoves.add(new BoxBorderPosition(i, j, true));
                }
            }
        }
        return availableMoves;
    }

    private List<BoxBorderPosition> getVerticalMoves() {
        List<BoxBorderPosition> availableMoves = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < (size - 1); j++) {
                if (verticalBorders[i][j] == EMPTY) {
                    availableMoves.add(new BoxBorderPosition(i, j, false));
                }
            }
        }
        return availableMoves;
    }

    public void setHorizontalBorder(int x, int y, int color) {
        horizontalBorders[x][y] = SELECTED;
        if (y < (size - 1) && verticalBorders[x][y] == SELECTED && verticalBorders[x + 1][y] == SELECTED && horizontalBorders[x][y + 1] == SELECTED) {
            boxBorders[x][y] = color;
            updateScore(color);
        }
        if (y > 0 && verticalBorders[x][y - 1] == SELECTED && verticalBorders[x + 1][y - 1] == SELECTED && horizontalBorders[x][y - 1] == SELECTED) {
            boxBorders[x][y - 1] = color;
            updateScore(color);
        }
    }

    public void setVerticalBorder(int x, int y, int color) {
        verticalBorders[x][y] = SELECTED;
        if (x < (size - 1) && horizontalBorders[x][y] == SELECTED && horizontalBorders[x][y + 1] == SELECTED && verticalBorders[x + 1][y] == SELECTED) {
            boxBorders[x][y] = color;
            updateScore(color);
        }
        if (x > 0 && horizontalBorders[x - 1][y] == SELECTED && horizontalBorders[x - 1][y + 1] == SELECTED && verticalBorders[x - 1][y] == SELECTED) {
            boxBorders[x - 1][y] = color;
            updateScore(color);
        }
    }

    private void updateScore(int color) {
        if (color == FIRST_PLAYER) {
            firstPlayerScore++;
        } else {
            secondPlayerScore++;
        }
    }

    public GameBoard getNewBoard(BoxBorderPosition boxBorderPosition, int color) {
        GameBoard board = copyGameBoard();
        if (boxBorderPosition.isHorizontal()) {
            board.setHorizontalBorder(boxBorderPosition.getXPos(), boxBorderPosition.getYPos(), color);
        } else {
            board.setVerticalBorder(boxBorderPosition.getXPos(), boxBorderPosition.getYPos(), color);
        }
        return board;
    }

    private int getSelectedBorderNumbers(int i, int j) {
        int count = 0;
        if (horizontalBorders[i][j] == SELECTED || horizontalBorders[i][j + 1] == SELECTED ||
                verticalBorders[i][j] == SELECTED || verticalBorders[i + 1][j] == SELECTED) {
            count++;
        }
        return count;
    }

    public int getSelectedBoxesNumber(int compareNumber) {
        int count = 0;
        int size = this.size - 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getSelectedBorderNumbers(i, j) == compareNumber) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isAllBoxesClosed() {
        return gameBoxes.stream().noneMatch(GameBox::isNotOccupied);
    }
}
