package com.labs.domain;

import javafx.scene.layout.VBox;
import lombok.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class GameBoard {
    private VBox gameBoard;
    private List<GameBox> gameBoxList;

    public final static int RED = 0;
    public final static int BLUE = 1;
    public final static int BLACK = 2;
    public final static int BLANK = 3;

    private int[][] horizontalBorders, verticalBorders, boxBorders;
    private int size, redScore, blueScore;

    public GameBoard(int size) {
        horizontalBorders = new int[size - 1][size];
        verticalBorders = new int[size][size - 1];
        boxBorders = new int[size - 1][size - 1];
        fill(horizontalBorders);
        fill(verticalBorders);
        fill(boxBorders);
        this.size = size;
        redScore = blueScore = 0;
    }

    public GameBoard copyGameBoard() {
        GameBoard newGameBoard = new GameBoard(size);
        for (int i = 0; i < (size - 1); i++) {
            if (size >= 0) {
                System.arraycopy(horizontalBorders[i], 0, newGameBoard.horizontalBorders[i], 0, size);
            }
        }

        for (int i = 0; i < size; i++) {
            System.arraycopy(verticalBorders[i], 0, newGameBoard.verticalBorders[i], 0, size - 1);
        }

        for (int i = 0; i < (size - 1); i++) {
            System.arraycopy(boxBorders[i], 0, newGameBoard.boxBorders[i], 0, size - 1);
        }

        newGameBoard.redScore = redScore;
        newGameBoard.blueScore = blueScore;
        return newGameBoard;
    }


    private void fill(int[][] array) {
        for (int[] ints : array) {
            Arrays.fill(ints, GameBoard.BLANK);
        }
    }


    public int getScoreByColor(int color) {
        if (color == RED) return redScore;
        else return blueScore;
    }

    public static int toggleColor(int color) {
        if (color == RED)
            return BLUE;
        else
            return RED;
    }

    public List<BoxBorderPosition> getAvailableMoves() {
        List<BoxBorderPosition> ret = new ArrayList<>();
        for (int i = 0; i < (size - 1); i++) {
            for (int j = 0; j < size; j++) {
                if (horizontalBorders[i][j] == BLANK) {
                    ret.add(new BoxBorderPosition(i, j, true));
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < (size - 1); j++) {
                if (verticalBorders[i][j] == BLANK) {
                    ret.add(new BoxBorderPosition(i, j, false));
                }
            }
        }
        return ret;
    }

    public ArrayList<Point> setHorizontalBorders(int x, int y, int color) {
        horizontalBorders[x][y] = BLACK;
        ArrayList<Point> ret = new ArrayList<>();
        if (y < (size - 1) && verticalBorders[x][y] == BLACK && verticalBorders[x + 1][y] == BLACK && horizontalBorders[x][y + 1] == BLACK) {
            boxBorders[x][y] = color;
            ret.add(new Point(x, y));
            if (color == RED) redScore++;
            else blueScore++;
        }
        if (y > 0 && verticalBorders[x][y - 1] == BLACK && verticalBorders[x + 1][y - 1] == BLACK && horizontalBorders[x][y - 1] == BLACK) {
            boxBorders[x][y - 1] = color;
            ret.add(new Point(x, y - 1));
            if (color == RED) redScore++;
            else blueScore++;
        }
        return ret;
    }

    public ArrayList<Point> setVerticalBorders(int x, int y, int color) {
        verticalBorders[x][y] = BLACK;
        ArrayList<Point> ret = new ArrayList<Point>();
        if (x < (size - 1) && horizontalBorders[x][y] == BLACK && horizontalBorders[x][y + 1] == BLACK && verticalBorders[x + 1][y] == BLACK) {
            boxBorders[x][y] = color;
            ret.add(new Point(x, y));
            if (color == RED) redScore++;
            else blueScore++;
        }
        if (x > 0 && horizontalBorders[x - 1][y] == BLACK && horizontalBorders[x - 1][y + 1] == BLACK && verticalBorders[x - 1][y] == BLACK) {
            boxBorders[x - 1][y] = color;
            ret.add(new Point(x - 1, y));
            if (color == RED) redScore++;
            else blueScore++;
        }
        return ret;
    }

    public boolean isComplete() {
        return (redScore + blueScore) == (size - 1) * (size - 1);
    }

    public int getWinner() {
        if (redScore > blueScore) return RED;
        else if (redScore < blueScore) return BLUE;
        else return BLANK;
    }

    public GameBoard getNewBoard(BoxBorderPosition boxBorderPosition, int color) {
        GameBoard ret = copyGameBoard();
        if (boxBorderPosition.isHorizontal()) {
            ret.setHorizontalBorders(boxBorderPosition.getXPos(), boxBorderPosition.getYPos(), color);
        } else {
            ret.setVerticalBorders(boxBorderPosition.getXPos(), boxBorderPosition.getYPos(), color);
        }
        return ret;
    }

    private int getBorderNumber(int i, int j) {
        int count = 0;
        if (horizontalBorders[i][j] == BLACK) count++;
        if (horizontalBorders[i][j + 1] == BLACK) count++;
        if (verticalBorders[i][j] == BLACK) count++;
        if (verticalBorders[i + 1][j] == BLACK) count++;
        return count;
    }

    public int getBoxBordersNumber(int nSides) {
        int count = 0;
        for (int i = 0; i < (size - 1); i++) {
            for (int j = 0; j < (size - 1); j++) {
                if (getBorderNumber(i, j) == nSides) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isAllBoxesClosed() {
        return gameBoxList.stream().noneMatch(GameBox::isNotOccupied);
    }
}
