package com.labs.controllers;

import com.labs.DotsAndBoxesApplication;
import com.labs.domain.GameProperties;
import com.labs.service.GameService;
import com.labs.serviceImpl.GameServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {
    public TextField moveInput;
    public TextField firstPlayerAmount;
    public TextField secondPlayerAmount;
    public Button homeBtn;
    private GameService gameService;
    private GameProperties gameProperties;

    @FXML
    void initialize() {
        gameService = GameServiceImpl.getInstance();
        gameProperties = gameService.getGameProperties();

        System.out.println(gameProperties);
        setStartAmountInput(firstPlayerAmount, gameProperties.getFirstPlayerColor().hashCode());
        setStartAmountInput(secondPlayerAmount, gameProperties.getSecondPlayerColor().hashCode());
    }

    private void setStartAmountInput(TextField amountInput, Integer colorHashCode) {
        String inputStyle = "-fx-text-inner-color: " + "#" + Integer.toHexString(colorHashCode) + ";";
        amountInput.setText("0");
        amountInput.setStyle(inputStyle);
    }

    public void onHomeButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DotsAndBoxesApplication.class.getResource("index.fxml"));
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("dotsAndBoxes.css");
        stage.setScene(scene);
    }
}
