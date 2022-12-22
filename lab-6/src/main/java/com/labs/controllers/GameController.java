package com.labs.controllers;

import com.labs.DotsAndBoxesApplication;
import com.labs.domain.GamePlayer;
import com.labs.domain.GameProperties;
import com.labs.enums.FieldSize;
import com.labs.enums.GameComplexity;
import com.labs.enums.PlayerType;
import com.labs.service.GameService;
import com.labs.serviceImpl.GameServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    public TextField moveInput;
    public TextField firstPlayerAmount;
    public TextField secondPlayerAmount;
    public Button homeBtn;
    public BorderPane boardPanel;
    private GameService gameService;
    private GameProperties gameProperties;

    @FXML
    void initialize() {
        gameService = GameServiceImpl.getInstance();
        GamePlayer firstPlayer = GamePlayer.builder()
                .type(PlayerType.HUMAN)
                .color(Color.CORAL)
                .score(0).build();
        GamePlayer secondPlayer = GamePlayer.builder()
                .type(PlayerType.HUMAN)
                .color(Color.RED)
                .score(0).build();
        this.gameProperties = GameProperties.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .gameComplexity(GameComplexity.EASY)
                .gameFieldSize(FieldSize.S).build();
        gameService.startGame(gameProperties);

        System.out.println(gameProperties);
        setPlayerScore(firstPlayerAmount, gameProperties.getFirstPlayer());
        setPlayerScore(secondPlayerAmount, gameProperties.getSecondPlayer());

        buildGameField();
    }

    private void setPlayerScore(TextField amountInput, GamePlayer gamePlayer) {
        String inputStyle = "-fx-text-inner-color: " + "#" + Integer.toHexString(gamePlayer.getColor().hashCode()) + ";";
        amountInput.setText(gamePlayer.getScore().toString());
        amountInput.setStyle(inputStyle);
    }

    public void onHomeButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DotsAndBoxesApplication.class.getResource("index.fxml"));
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("dotsAndBoxes.css");
        stage.setScene(scene);
    }


    private void buildGameField() {
        int size = gameProperties.getGameFieldSize().getSize() * 10;
        double colWidth = ((double) 1 / gameProperties.getGameFieldSize().getSize()) * 100;

        VBox vBox = new VBox();
        for (int i = 0; i < gameProperties.getGameFieldSize().getSize(); i++) {
            HBox hor = getHorizontalLine();
            HBox ver = getVerticalLine();
            vBox.getChildren().addAll(hor, ver);
        }
        vBox.getChildren().add(getHorizontalLine());
        boardPanel.setCenter(vBox);
    }

    private HBox getVerticalLine() {
        HBox hBox = new HBox();
        int size = gameProperties.getGameFieldSize().getSize();
        for (int i = 0; i < size + 1; i++) {
            Button button = getBorderButton(false);
            hBox.getChildren().add(button);
            if (i + 1 != size + 1) {
                hBox.getChildren().add(getBox());
            }
        }
        return hBox;
    }

    private HBox getHorizontalLine() {
        HBox hBox = new HBox();
        List<Node> rowNodes = new ArrayList<>();
        int size = gameProperties.getGameFieldSize().getSize();
        for (int i = 0; i < size + 1; i++) {
            rowNodes.add(getCircle());
            if (i + 1 != size + 1) {
                Button button = getBorderButton(true);
                rowNodes.add(button);
            }
        }
        hBox.getChildren().addAll(rowNodes);
        return hBox;
    }


    private Circle getCircle() {
        Circle circle = new Circle(8, Color.BLACK);
        circle.setLayoutX(10);
        return circle;
    }

    private Button getBorderButton(boolean isHorizontal) {
        Button button = new Button();
        if (isHorizontal) {
            button.setMinHeight(Button.USE_PREF_SIZE);
            button.getStyleClass().add("h-btn");
        } else {
            button.getStyleClass().add("v-btn");
        }
        return button;
    }

    private BorderPane getBox() {
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(70);
        rectangle.setHeight(70);
        rectangle.setFill(Color.GREEN);
        BorderPane borderPane = new BorderPane();
        borderPane.setMinHeight(70);
        borderPane.setMinHeight(70);
        borderPane.setCenter(rectangle);
        return borderPane;
    }
}
