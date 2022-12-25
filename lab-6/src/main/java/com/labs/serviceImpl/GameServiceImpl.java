package com.labs.serviceImpl;

import com.labs.domain.*;
import com.labs.domainNew.Board;
import com.labs.domainNew.Edge;
import com.labs.enums.PlayerType;
import com.labs.service.GameService;
import com.labs.service.Observer;
import com.labs.solvers.AlphaBettaSolver;
import com.labs.solvers.MiniMaxSolver;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GameServiceImpl implements GameService {
    private static GameServiceImpl gameInstance;

    private GameProperties gameProperties;
    private GameBoard gameBoard;
    private String currentPlayerTitle;
    private static final String bgStyle = "-fx-background-color:";
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
        board = new Board(gameProperties.getGameFieldSize().getSize());    //TODO REMOVE
        setGameSolverForPlayer(gameProperties.getFirstPlayer());
        setGameSolverForPlayer(gameProperties.getSecondPlayer());

        this.currentPlayerTitle = gameProperties.getFirstPlayer().getTitle();
        notifyPlayerChange();
        checkNextMove();
    }

    private void checkNextMove() {
        if (gameBoard.isAllBoxesClosed()) {
            stopGame();
        } else {
            GamePlayer currentPlayer = getCurrentPlayer();
            if (currentPlayer.getType() == PlayerType.COMPUTER) {
                Edge edge = currentPlayer.getGameSolver().getNextMove(board, turn);
                System.out.println("FOUND->" + edge);
                selectBoxBorderByPlayer(edge.toString());
            }
        }
    }


    private void changeCurrentPlayer() {
        if (this.gameProperties.getFirstPlayer().getTitle().equals(currentPlayerTitle)) {
            turn = Board.RED;
            currentPlayerTitle = this.gameProperties.getSecondPlayer().getTitle();
        } else {
            turn = Board.BLUE;
            currentPlayerTitle = this.gameProperties.getFirstPlayer().getTitle();
        }
        notifyPlayerChange();
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
        this.gameBoard = GameBoard.builder()
                .size(gameProperties.getGameFieldSize().getSize())
                .gameBoard(gameBoard)
                .gameBoxList(gameBoxList).build();
        return this.gameBoard;
    }

    @Override
    public void selectBoxBorderByPlayer(String boxBorderId) {
        boolean isClosed = false;
        for (GameBox gameBox : gameBoard.getGameBoxList()) {
            Optional<BoxBorder> box = gameBox.getBoxBorders().stream()
                    .filter(boxBorder -> boxBorder.getId().equals(boxBorderId))
                    .filter(boxBorder -> !boxBorder.isSelected())
                    .findFirst();
            if (box.isPresent()) {
                box.get().setSelected(true);
                box.get().setSelectedBy(getCurrentPlayer().getType().toString());
                box.get().getButton().setStyle(bgStyle + "#000000");
                box.get().getButton().setDisable(true);
            }
            if (box.isPresent() && gameBox.isAllBorderBoxSelected()) {
                gameBox.closeGameBox(getCurrentPlayer());
                GamePlayer gamePlayer = getCurrentPlayer();
                gamePlayer.updateScore();
                isClosed = true;
            }
        }
        if (!isClosed) {
            changeCurrentPlayer();
        }
        onSelectBorder(boxBorderId);    //TODO REMOVE
        checkNextMove();
    }

    private void stopGame() {
        for (Observer observer : observers) {
            observer.onStopGame(getWinPlayer());
        }
    }

    private GamePlayer getWinPlayer() {
        GamePlayer firstPlayer = gameProperties.getFirstPlayer();
        GamePlayer secondPlayer = gameProperties.getSecondPlayer();
        return firstPlayer.getScore() > secondPlayer.getScore() ? firstPlayer : secondPlayer;
    }

    private GameBox getGameBox(BorderPane box, List<BoxBorder> boxBorders) {
        return GameBox.builder()
                .isOccupied(false)
                .occupiedBy("")
                .box(box)
                .boxBorders(boxBorders).build();
    }

    private GamePlayer getCurrentPlayer() {
        GamePlayer gamePlayer;
        if (this.gameProperties.getFirstPlayer().getTitle().equals(currentPlayerTitle)) {
            gamePlayer = this.gameProperties.getFirstPlayer();
        } else {
            gamePlayer = this.gameProperties.getSecondPlayer();
        }
        return gamePlayer;
    }

    private void setGameSolverForPlayer(GamePlayer gamePlayer) {
//        GameBoardNode gameBoardNode = GameBoardNode.builder()
//                .moveBy(getCurrentPlayer().getType())
//                .humanScore(0)
//                .computerScore(0)
//                .boardId(UUID.randomUUID().toString())
//                .currentState(gameBoard.getGameBoxList())
//                .successors(new ArrayList<>())
//                .depth(0)
//                .functionCost(0).build();
//        AlphaBettaSolver gameSolver = new AlphaBettaSolver(gameBoardNode, gameProperties.getGameDifficulty());
        int n = gameProperties.getGameFieldSize().getSize();    //TODO REMOVE
        isSetHEdge = new boolean[n - 1][n];    //TODO REMOVE
        isSetVEdge = new boolean[n][n - 1];    //TODO REMOVE
        switch (gamePlayer.getType()) {
            case HUMAN: {
                gamePlayer.setGameSolver(null);
                break;
            }
            case COMPUTER: {
                gamePlayer.setGameSolver(new MiniMaxSolver());
                break;
            }
        }
    }

    @Override
    public void notifyPlayerChange() {
        for (Observer observer : observers) {
            observer.onPlayerChange(getCurrentPlayer());
        }
    }

    @Override
    public void notifyPlayerScoreChange(GamePlayer gamePlayer) {
        for (Observer observer : observers) {
            observer.onPlayerScoreChange(gamePlayer.getTitle(), gamePlayer.getScore());
        }
    }

    @Override
    public void clearGame() {
        gameInstance = new GameServiceImpl();
    }

    private int getRandomNumber(int max, int min) {
        return (int) (Math.random() * (max - min) + min);
    }


    //TODO WRITE ALGORITHM

    //TODO REMOVE
    private Board board;
    private boolean[][] isSetHEdge, isSetVEdge;
    private int turn;

    private void onSelectBorder(String boxBorderId) {
        Edge edge = getEdge(boxBorderId);
        int x = edge.getX(), y = edge.getY();
        if (edge.isHorizontal()) {
            if (isSetHEdge[x][y]) return;
            board.setHEdge(x, y, turn);
            isSetHEdge[x][y] = true;
        } else {
            if (isSetVEdge[x][y]) return;
            board.setVEdge(x, y, turn);
            isSetVEdge[x][y] = true;
        }
    }

    private Edge getEdge(String boxBorderId) {
        Edge edge = new Edge();
        if (boxBorderId.contains(HORIZONTAL)) {
            boxBorderId = boxBorderId.replace(HORIZONTAL, "");
            edge.setHorizontal(true);
        } else {
            boxBorderId = boxBorderId.replace(VERTICAL, "");
            edge.setHorizontal(false);
        }
        edge.setX(Integer.parseInt(boxBorderId.split("")[0]));
        edge.setY(Integer.parseInt(boxBorderId.split("")[1]));
        return edge;
    }
}
