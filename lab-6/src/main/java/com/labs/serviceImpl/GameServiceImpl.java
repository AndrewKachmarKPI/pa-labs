package com.labs.serviceImpl;

import com.labs.domain.*;
import com.labs.enums.PlayerType;
import com.labs.domain.GameBoard;
import com.labs.domain.BoxBorderPosition;
import com.labs.service.GameService;
import com.labs.service.Observer;
import com.labs.solvers.AlphaBettaSolver;
import com.labs.solvers.GameSolver;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.List;

public class GameServiceImpl implements GameService {
    private GameProperties gameProperties;
    private GameBoard gameBoard;
    private GamePlayer currentPlayer;
    private List<BoxBorderPosition> selectedPositions;

    private static GameServiceImpl gameInstance;
    private List<Observer> observers = new ArrayList<>();

    public static GameServiceImpl getInstance() {
        if (gameInstance == null) {
            gameInstance = new GameServiceImpl();
        }
        return gameInstance;
    }

    public static GameServiceImpl getNewInstance() {
        return new GameServiceImpl();
    }


    @Override
    public void saveSettings(GameProperties gameProperties, Observer observer) {
        this.observers.add(observer);
        this.saveSettings(gameProperties);
    }

    @Override
    public void saveSettings(GameProperties gameProperties) {
        this.gameProperties = gameProperties;
    }

    @Override
    public void saveSettings(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void startGame() {
        setGameSolverForPlayer(gameProperties.getFirstPlayer());
        setGameSolverForPlayer(gameProperties.getSecondPlayer());

        notifyPlayerChange();
        checkNextMove();
    }

    @Override
    public GameProperties getGameProperties() {
        return gameProperties;
    }

    @Override
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public GameBoard buildGameBoard(List<Button> buttons, List<BorderPane> boxes, VBox gameBoard) {
        List<GameBox> gameBoxList = new ArrayList<>();
        for (BorderPane box : boxes) {
            Integer boxRow = Integer.valueOf(box.getId().split("")[0]);
            Integer boxCol = Integer.valueOf(box.getId().split("")[1]);

            List<BoxBorder> boxBorders = new ArrayList<>();
            Button topBorder = buttons.stream()
                    .filter(button -> button.getId().startsWith(HORIZONTAL))
                    .filter(button -> {
                        String rawId = button.getId().replace(HORIZONTAL, "");
                        Integer btnRow = Integer.valueOf(rawId.split("")[0]);
                        Integer btnCol = Integer.valueOf(rawId.split("")[1]);
                        return btnRow.equals(boxRow) && (btnCol.equals(boxCol));
                    }).findFirst().orElseThrow(() -> new RuntimeException("Button not found"));
            Button bottomBorder = buttons.stream()
                    .filter(button -> button.getId().startsWith(HORIZONTAL))
                    .filter(button -> {
                        String rawId = button.getId().replace(HORIZONTAL, "");
                        Integer btnRow = Integer.valueOf(rawId.split("")[0]);
                        Integer btnCol = Integer.valueOf(rawId.split("")[1]);
                        return (btnRow - 1) == (boxRow) && (btnCol.equals(boxCol));
                    }).findFirst().orElseThrow(() -> new RuntimeException("Button not found"));
            Button leftBorder = buttons.stream()
                    .filter(button -> button.getId().startsWith(VERTICAL))
                    .filter(button -> {
                        String rawId = button.getId().replace(VERTICAL, "");
                        Integer btnRow = Integer.valueOf(rawId.split("")[0]);
                        Integer btnCol = Integer.valueOf(rawId.split("")[1]);
                        return Objects.equals(btnRow, boxRow) && (btnCol.equals(boxCol));
                    }).findFirst().orElseThrow(() -> new RuntimeException("Button not found"));
            Button rightBorder = buttons.stream()
                    .filter(button -> button.getId().startsWith(VERTICAL))
                    .filter(button -> {
                        String rawId = button.getId().replace(VERTICAL, "");
                        Integer btnRow = Integer.valueOf(rawId.split("")[0]);
                        Integer btnCol = Integer.valueOf(rawId.split("")[1]);
                        return Objects.equals(btnRow, boxRow) && (btnCol - 1) == boxCol;
                    }).findFirst().orElseThrow(() -> new RuntimeException("Button not found"));

            boxBorders.add(new BoxBorder(topBorder));
            boxBorders.add(new BoxBorder(bottomBorder));
            boxBorders.add(new BoxBorder(leftBorder));
            boxBorders.add(new BoxBorder(rightBorder));

            gameBoxList.add(getGameBox(box, boxBorders));
        }
        int size = gameProperties.getGameFieldSize().getSize() + 1;
        this.gameBoard = new GameBoard(size);
        this.gameBoard.setGameBoardVBox(gameBoard);
        this.gameBoard.setGameBoxes(gameBoxList);
        this.selectedPositions = new ArrayList<>();
        this.currentPlayer = gameProperties.getFirstPlayer();
        return this.gameBoard;
    }

    private void checkNextMove() {
        if (!gameBoard.isAllBoxesClosed()) {
            GameSolver solver = currentPlayer.getGameSolver();
            if (currentPlayer.getType() == PlayerType.COMPUTER) {
                BoxBorderPosition boxBorderPosition = solver.getNextMove(gameBoard, currentPlayer.getColorIndex());
                selectBoxBorder(boxBorderPosition);
                selectBoxBorder(boxBorderPosition.getBorderPosition(), PlayerType.COMPUTER);
            }
        } else {
            stopGame();
        }
    }

    private void changeCurrentPlayer() {
        if (gameProperties.getFirstPlayer().getPlayerId().equals(currentPlayer.getPlayerId())) {
            currentPlayer = this.gameProperties.getSecondPlayer();
        } else {
            currentPlayer = this.gameProperties.getFirstPlayer();
        }
        notifyPlayerChange();
    }


    private BoxBorderPosition getBoxBorderPosition(String boxBorderId) {
        BoxBorderPosition boxBorderPosition = new BoxBorderPosition();
        if (boxBorderId.contains(HORIZONTAL)) {
            boxBorderPosition.setHorizontal(true);
            boxBorderId = boxBorderId.replace(HORIZONTAL, "");
        } else {
            boxBorderPosition.setHorizontal(false);
            boxBorderId = boxBorderId.replace(VERTICAL, "");
        }
        boxBorderPosition.setXPos(Integer.parseInt(boxBorderId.split("")[1]));
        boxBorderPosition.setYPos(Integer.parseInt(boxBorderId.split("")[0]));
        return boxBorderPosition;
    }

    @Override
    public void selectBoxBorder(String boxBorderId, PlayerType playerType) {
        System.out.println("SELECT BOX BORDER");
        boolean isBoxClosed = false;
        if (playerType == PlayerType.HUMAN) {
            selectBoxBorder(getBoxBorderPosition(boxBorderId));
        }
        for (GameBox gameBox : gameBoard.getGameBoxes()) {
            Optional<BoxBorder> box = gameBox.getBoxBorders().stream()
                    .filter(boxBorder -> boxBorder.getId().equals(boxBorderId) && !boxBorder.isSelected())
                    .findFirst();
            box.ifPresent(boxBorder -> boxBorder.selectBorder(currentPlayer));

            if (box.isPresent() && gameBox.isAllBorderBoxSelected()) {
                gameBox.closeGameBox(currentPlayer);
                isBoxClosed = true;
            }
        }
        if (!isBoxClosed) {
            changeCurrentPlayer();
        }
        updatePlayersScore();
        checkNextMove();
    }

    private void selectBoxBorder(BoxBorderPosition borderPosition) {
        if (!selectedPositions.contains(borderPosition)) {
            if (borderPosition.isHorizontal()) {
                gameBoard.setHorizontalBorder(borderPosition.getXPos(), borderPosition.getYPos(), currentPlayer.getColorIndex());
            } else {
                gameBoard.setVerticalBorder(borderPosition.getXPos(), borderPosition.getYPos(), currentPlayer.getColorIndex());
            }
            selectedPositions.add(borderPosition);
        }
    }

    private void stopGame() {
        for (Observer observer : observers) {
            observer.onStopGame(getWinPlayer());
        }
    }

    private GamePlayer getWinPlayer() {
        GamePlayer firstPlayer = gameProperties.getFirstPlayer();
        GamePlayer secondPlayer = gameProperties.getSecondPlayer();
        GamePlayer gamePlayer = null;
        if (firstPlayer.getScore() > secondPlayer.getScore()) {
            gamePlayer = firstPlayer;
        } else if ((secondPlayer.getScore() > firstPlayer.getScore())) {
            gamePlayer = secondPlayer;
        }
        return gamePlayer;
    }

    private void updatePlayersScore() {
        GamePlayer firstPlayer = gameProperties.getFirstPlayer();
        GamePlayer secondPlayer = gameProperties.getSecondPlayer();
        firstPlayer.setScore(gameBoard.getScoreByColor(firstPlayer.getColorIndex()));
        secondPlayer.setScore(gameBoard.getScoreByColor(secondPlayer.getColorIndex()));
        notifyPlayerScoreChange(firstPlayer);
        notifyPlayerScoreChange(secondPlayer);
    }

    private GameBox getGameBox(BorderPane box, List<BoxBorder> boxBorders) {
        return GameBox.builder()
                .isOccupied(false)
                .occupiedBy("")
                .box(box)
                .boxBorders(boxBorders).build();
    }

    private void setGameSolverForPlayer(GamePlayer gamePlayer) {
        if (gamePlayer.getType() == PlayerType.COMPUTER) {
            gamePlayer.setGameSolver(new AlphaBettaSolver());
        }
    }

    @Override
    public void notifyPlayerChange() {
        for (Observer observer : observers) {
            observer.onPlayerChange(currentPlayer);
        }
    }

    @Override
    public void notifyPlayerScoreChange(GamePlayer gamePlayer) {
        for (Observer observer : observers) {
            observer.onPlayerScoreChange(gamePlayer.getPlayerId(), gamePlayer.getScore());
        }
    }

    @Override
    public void clearGame() {
        gameInstance = new GameServiceImpl();
    }
}
