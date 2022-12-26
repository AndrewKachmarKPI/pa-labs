package com.labs.controllers;

import com.labs.DotsAndBoxesApplication;
import com.labs.domain.*;
import com.labs.enums.PlayerType;
import com.labs.service.GameService;
import com.labs.service.Observer;
import com.labs.serviceImpl.GameServiceImpl;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameController implements Observer {
    public TextField moveInput;
    public TextField firstPlayerAmount;
    public TextField secondPlayerAmount;
    public Button homeBtn;
    public BorderPane boardPanel;
    public Label winLabel;
    public Button reloadGameBtn;
    private List<Button> allButtons = new ArrayList<>();
    private List<BorderPane> allBoxes = new ArrayList<>();
    private static final String bgStyle = "-fx-background-color:";


    private GameService gameService;
    private GameProperties gameProperties;
    private GamePlayer currentPlayer;

    @FXML
    void initialize() {
        gameService = GameServiceImpl.getInstance();
        gameService.saveSettings(this);
        this.gameProperties = gameService.getGameProperties();

        setPlayerScore(firstPlayerAmount, gameProperties.getFirstPlayer());
        setPlayerScore(secondPlayerAmount, gameProperties.getSecondPlayer());

        buildGameField();
        gameService.startGame();
    }

    private void setPlayerScore(TextField amountInput, GamePlayer gamePlayer) {
        String inputStyle = "-fx-text-inner-color: " + getHexColor(gamePlayer.getColor());
        amountInput.setText(gamePlayer.getScore().toString());
        amountInput.setStyle(inputStyle);
        amountInput.setId(gamePlayer.getPlayerId());
    }

    public void onHomeButtonClick() throws IOException {
        gameService.clearGame();
        FXMLLoader fxmlLoader = new FXMLLoader(DotsAndBoxesApplication.class.getResource("index.fxml"));
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("dotsAndBoxes.css");
        stage.setScene(scene);
    }

    public void onReloadBtnClick() {
        allButtons = new ArrayList<>();
        allBoxes = new ArrayList<>();
        winLabel.setText("");
        firstPlayerAmount.setText("");
        secondPlayerAmount.setText("");
        moveInput.setText("");
        gameService = GameServiceImpl.getNewInstance();
        currentPlayer = null;
        gameProperties.getFirstPlayer().setScore(0);
        gameProperties.getSecondPlayer().setScore(0);
        gameService.saveSettings(gameProperties, this);
        buildGameField();
        gameService.startGame();
    }

    //BUILD FIELD
    private void buildGameField() {
        VBox gameBoard = new VBox();
        gameBoard.setAlignment(Pos.CENTER);
        int size = gameProperties.getGameFieldSize().getSize();
        for (int i = 0; i < size + 1; i++) {
            HBox horizontalLine = getHorizontalLine(i);
            if (isNotLastRow(i, size)) {
                HBox verticalLine = getVerticalLine(i);
                gameBoard.getChildren().addAll(horizontalLine, verticalLine);
            } else {
                gameBoard.getChildren().addAll(horizontalLine);
            }
        }
        BorderPane.setAlignment(gameBoard, Pos.CENTER);
        boardPanel.setCenter(gameBoard);
        gameService.buildGameBoard(allButtons, allBoxes, gameBoard);
    }

    private boolean isNotLastRow(int i, int size) {
        return i + 1 != size + 1;
    }

    private HBox getVerticalLine(int row) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        int size = gameProperties.getGameFieldSize().getSize();
        for (int i = 0; i < size + 1; i++) {
            Button button = getBorderButton(false, row, i);
            hBox.getChildren().add(button);
            if (isNotLastRow(i, size)) {
                hBox.getChildren().add(getBox(row, i));
            }
        }
        return hBox;
    }

    private HBox getHorizontalLine(int row) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        List<Node> rowNodes = new ArrayList<>();
        int size = gameProperties.getGameFieldSize().getSize();
        for (int i = 0; i < size + 1; i++) {
            rowNodes.add(getCircle());
            if (isNotLastRow(i, size)) {
                Button button = getBorderButton(true, row, i);
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

    private Button getBorderButton(boolean isHorizontal, int row, int col) {
        Button button = new Button();
        if (isHorizontal) {
            button.setMinHeight(Region.USE_PREF_SIZE);
            button.getStyleClass().add("h-btn");
            button.setId("H" + row + col);
        } else {
            button.getStyleClass().add("v-btn");
            button.setId("V" + row + col);
        }

        button.setStyle(bgStyle + getHexColor(Color.LIGHTGRAY));
        EventHandler<MouseEvent> selectBorder = this::onBorderSelect;
        EventHandler<MouseEvent> hoverBorder = this::onBorderHover;
        EventHandler<MouseEvent> unHover = this::onBorderUnHover;
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, selectBorder);
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, hoverBorder);
        button.addEventHandler(MouseEvent.MOUSE_EXITED, unHover);
        allButtons.add(button);
        return button;
    }

    private BorderPane getBox(int row, int col) {
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(70);
        rectangle.setHeight(70);
        rectangle.setFill(Color.TRANSPARENT);
        BorderPane borderPane = new BorderPane();
        borderPane.setId(row + String.valueOf(col));
        borderPane.setMinHeight(70);
        borderPane.setMinHeight(70);
        borderPane.setCenter(rectangle);
        allBoxes.add(borderPane);
        return borderPane;
    }


    private void onBorderHover(MouseEvent event) {
        Button button = getUiButtonById(((Button) event.getSource()).getId());
        if (!button.isDisabled() && currentPlayer.getType() != PlayerType.COMPUTER) {
            button.cursorProperty().set(Cursor.HAND);
            button.setStyle(bgStyle + getHexColor(currentPlayer.getColor()));
        }
    }

    private void onBorderUnHover(MouseEvent event) {
        Button button = getUiButtonById(((Button) event.getSource()).getId());
        if (!button.isDisabled() && currentPlayer.getType() != PlayerType.COMPUTER) {
            button.cursorProperty().set(Cursor.HAND);
            button.setStyle(bgStyle + getHexColor(Color.LIGHTGRAY));
        }
    }

    private Button getUiButtonById(String id) {
        return allButtons.stream().filter(btn -> btn.getId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("Btn not found"));
    }

    private String getHexColor(Color color) {
        return "#" + Integer.toHexString(color.hashCode()) + ";";
    }

    // ACTIONS
    private void onBorderSelect(MouseEvent event) {
        if (currentPlayer.getType() != PlayerType.COMPUTER) {
            gameService.selectBoxBorderByPlayer(((Button) event.getSource()).getId(), PlayerType.HUMAN);
        }
    }

    @Override
    public void onStopGame(GamePlayer gamePlayer) {
        winLabel.setText(gamePlayer.getPlayerId() + " wins with score ->" + gamePlayer.getScore());
    }

    @Override
    public void onPlayerChange(GamePlayer gamePlayer) {
        currentPlayer = gamePlayer;
        moveInput.setText(gamePlayer.getPlayerId());
    }

    @Override
    public void onPlayerScoreChange(String playerTitle, Integer score) {
        if (this.firstPlayerAmount.getId().equals(playerTitle)) {
            this.firstPlayerAmount.setText(score.toString());
        } else {
            this.secondPlayerAmount.setText(score.toString());
        }
    }
}
