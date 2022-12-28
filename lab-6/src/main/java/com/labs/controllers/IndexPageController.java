package com.labs.controllers;

import com.labs.DotsAndBoxesApplication;
import com.labs.domain.GameBoard;
import com.labs.domain.GamePlayer;
import com.labs.domain.GameProperties;
import com.labs.enums.FieldSize;
import com.labs.enums.GameDifficulty;
import com.labs.enums.PlayerType;
import com.labs.service.GameConstants;
import com.labs.service.GameService;
import com.labs.serviceImpl.GameServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class IndexPageController {
    private GameService gameService;
    @FXML
    public Button playBtn;
    @FXML
    public ColorPicker firstPlayerColor;
    @FXML
    public ComboBox<PlayerType> firstPlayerType;
    @FXML
    public ColorPicker secondPlayerColor;
    @FXML
    public ComboBox<PlayerType> secondPlayerType;
    @FXML
    public ComboBox<GameDifficulty> gameComplexityComboBox;
    @FXML
    public ComboBox<String> fieldSizeComboBox;


    @FXML
    void initialize() {
        setGameComplexityComboBox();
        setPlayerTypeComboBoxes();
        setDefaultPlayerColors();
        setFieldSizeComboBox();
        gameService = GameServiceImpl.getInstance();
    }

    private void setGameComplexityComboBox() {
        ObservableList<GameDifficulty> gameComplexities = FXCollections.observableArrayList(GameDifficulty.values());
        gameComplexityComboBox.setItems(gameComplexities);
    }

    private void setPlayerTypeComboBoxes() {
        ObservableList<PlayerType> playerTypes = FXCollections.observableArrayList(PlayerType.values());
        firstPlayerType.setItems(playerTypes);
        secondPlayerType.setItems(playerTypes);
    }

    private void setDefaultPlayerColors() {
        firstPlayerColor.setValue(Color.RED);
        secondPlayerColor.setValue(Color.LIME);
    }

    private void setFieldSizeComboBox() {
        List<String> sizes = Arrays.stream(FieldSize.values()).map(FieldSize::getTitle).collect(Collectors.toList());
        ObservableList<String> filedSizes = FXCollections.observableArrayList(sizes);
        fieldSizeComboBox.setItems(filedSizes);
    }

    @FXML
    public void onPlayButtonClick() throws IOException {
        if (isSettingsFormValid()) {
            GamePlayer firstPlayer = GamePlayer.builder()
                    .colorIndex(GameConstants.FIRST_PLAYER)
                    .type(PlayerType.valueOf(firstPlayerType.getValue().toString()))
                    .color(firstPlayerColor.getValue())
                    .playerId("Player 1")
                    .score(0).build();
            GamePlayer secondPlayer = GamePlayer.builder()
                    .colorIndex(GameConstants.SECOND_PLAYER)
                    .type(PlayerType.valueOf(secondPlayerType.getValue().toString()))
                    .color(secondPlayerColor.getValue())
                    .playerId("Player 2")
                    .score(0).build();
            GameProperties gameProperties = GameProperties.builder()
                    .firstPlayer(firstPlayer)
                    .secondPlayer(secondPlayer)
                    .gameDifficulty(gameComplexityComboBox.getValue())
                    .gameFieldSize(FieldSize.getByTitle(fieldSizeComboBox.getValue())).build();
            gameService.saveSettings(gameProperties);

            FXMLLoader fxmlLoader = new FXMLLoader(DotsAndBoxesApplication.class.getResource("dots-and-boxes.fxml"));
            Stage stage = (Stage) playBtn.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("dotsAndBoxes.css");
            stage.setScene(scene);
        }
    }

    private boolean isSettingsFormValid() {
        return !firstPlayerType.getSelectionModel().isEmpty() &&
                !secondPlayerType.getSelectionModel().isEmpty() &&
                !gameComplexityComboBox.selectionModelProperty().get().isEmpty() &&
                !fieldSizeComboBox.selectionModelProperty().get().isEmpty();
    }
}
