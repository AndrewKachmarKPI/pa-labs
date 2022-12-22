package com.labs.serviceImpl;

import com.labs.domain.*;
import com.labs.service.GameService;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GameServiceImpl implements GameService {
    private static GameServiceImpl gameInstance;
    private GameProperties gameProperties;
    private GameBoard gameBoard;
    private static final String bgStyle = "-fx-background-color:";

    public static GameServiceImpl getInstance() {
        if (gameInstance == null) {
            gameInstance = new GameServiceImpl();
        }
        return gameInstance;
    }

    @Override
    public void startGame(GameProperties gameProperties) {
        this.gameProperties = gameProperties;
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
    public void selectBoxBorder(String boxBorderId) {
        for (GameBox gameBox : gameBoard.getGameBoxList()) {
            Optional<BoxBorder> box = gameBox.getBoxBorders().stream()
                    .filter(boxBorder -> boxBorder.getButton().getId().equals(boxBorderId))
                    .filter(boxBorder -> !boxBorder.isSelected())
                    .findFirst();
            if (box.isPresent()) {
                box.get().setSelected(true);
                box.get().setSelectedBy(currentPlayer().getTitle());
                box.get().getButton().setStyle(bgStyle + "#000000");
                box.get().getButton().setDisable(true);
            }
            if (box.isPresent() && gameBox.isAllBorderBoxSelected()) {
                closeGameBox(gameBox);
            }
        }
    }


    private void closeGameBox(GameBox gameBox) {
        gameBox.closeGameBox(currentPlayer());
        if (gameBoard.isAllBoxesClosed()) {
            stopGame();
        }
    }

    private boolean isAllBoxesClosed() {
        return gameBoard.getGameBoxList().stream().noneMatch(GameBox::isNotOccupied);
    }

    private void stopGame() {
        System.out.println("STOP GAME");
    }

    private GameBox getGameBox(BorderPane box, List<BoxBorder> boxBorders) {
        return GameBox.builder()
                .isOccupied(false)
                .occupiedBy("")
                .box(box)
                .boxBorders(boxBorders).build();
    }

    private GamePlayer currentPlayer() {
        return gameProperties.getFirstPlayer();
    }
}
