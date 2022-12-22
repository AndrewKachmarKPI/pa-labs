package com.labs.service;

import com.labs.domain.GameBoard;
import com.labs.domain.GameProperties;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

public interface GameService {
    String HORIZONTAL = "H";
    String VERTICAL = "V";

    void startGame(GameProperties gameProperties);

    GameProperties getGameProperties();

    GameBoard getGameBoard();

    GameBoard buildGameBoard(List<Button> buttons, List<BorderPane> boxes, VBox gameBoard);
}
