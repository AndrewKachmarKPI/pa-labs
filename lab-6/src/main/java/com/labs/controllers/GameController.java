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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {
    public TextField moveInput;
    public TextField firstPlayerAmount;
    public TextField secondPlayerAmount;
    public Button homeBtn;
    public GridPane pane;
    public BorderPane dotsPane;
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
        dotsPane.setPrefSize(size, size);

        BorderPane borderPane = new BorderPane();
        Node topNode = getHorizontalNode();
        Node bottomNode = getHorizontalNode();
        Node leftNode = getBorderEdge(false);
        Node rightNode = getBorderEdge(false);
        BorderPane.setAlignment(topNode, Pos.CENTER);
        BorderPane.setAlignment(bottomNode, Pos.CENTER);
        BorderPane.setAlignment(leftNode, Pos.CENTER);
        BorderPane.setAlignment(rightNode, Pos.CENTER);

        borderPane.setTop(topNode);
        borderPane.setBottom(bottomNode);
        borderPane.setCenter(new Rectangle(40, 40, Color.CORAL));
        borderPane.setLeft(leftNode);
        borderPane.setRight(rightNode);

        dotsPane.setCenter(borderPane);
    }

    private BorderPane getBox() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(getBoxBoarder(true));
        borderPane.setBottom(getBoxBoarder(true));
        borderPane.setLeft(getBoxBoarder(false));
        borderPane.setRight(getBoxBoarder(false));
        borderPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return borderPane;
    }

    private Node getBoxBoarder(boolean isHorizontal) {
        Button button = new Button();
        if (isHorizontal) {
            button.setPrefSize(Double.MAX_VALUE, 5);
        } else {
            button.setPrefSize(5, Double.MAX_VALUE);
        }
        return button;
    }

    private Circle getCircle() {
        Circle circle = new Circle(8, Color.BLACK);
        circle.getStyleClass().add("circle");
        return circle;
    }

    private Node getHorizontalNode() {
        return new HBox(getCircle(), getBorderEdge(true), getCircle());
    }

    private Node getBorderEdge(boolean isHorizontal) {
        Button button = new Button();
        if (isHorizontal) {
            button.setPrefSize(40, 5);
        } else {
            button.setPrefSize(5, 40);
        }
        return button;
    }
}
