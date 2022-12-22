package com.labs.serviceImpl;

import com.labs.domain.Box;
import com.labs.domain.BoxBorder;
import com.labs.domain.GameBoard;
import com.labs.domain.GameProperties;
import com.labs.service.GameService;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameServiceImpl implements GameService {
    private static GameServiceImpl gameInstance;
    private GameProperties gameProperties;
    private GameBoard gameBoard;

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
        GameBoard board = GameBoard.builder()
                .size(gameProperties.getGameFieldSize().getSize())
                .gameBoard(gameBoard).build();
        List<Box> boxList = new ArrayList<>();
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

            boxList.add(getGameBox(box, boxBorders));
        }
        boxList.forEach(System.out::println);
        return board;
    }


    private Box getGameBox(BorderPane box, List<BoxBorder> boxBorders) {
        return Box.builder()
                .isOccupied(false)
                .occupiedBy("")
                .box(box)
                .boxBorders(boxBorders).build();
    }
}
