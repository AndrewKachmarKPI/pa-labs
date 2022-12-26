package com.labs.service;

import com.labs.domain.GameBoard;
import com.labs.domain.GamePlayer;
import com.labs.domain.GameProperties;
import com.labs.enums.PlayerType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

public interface GameService {
    String HORIZONTAL = "H";
    String VERTICAL = "V";

    void startGame();

    void saveSettings(Observer observer);

    void saveSettings(GameProperties gameProperties, Observer observer);

    void saveSettings(GameProperties gameProperties);

    GameProperties getGameProperties();

    GameBoard getGameBoard();

    GameBoard buildGameBoard(List<Button> buttons, List<BorderPane> boxes, VBox gameBoard);

    void selectBoxBorderByPlayer(String boxBorderId, PlayerType playerType);


    void notifyPlayerChange();

    void notifyPlayerScoreChange(GamePlayer gamePlayer);

    void clearGame();
}
