package com.labs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DotsAndBoxesApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DotsAndBoxesApplication.class.getResource("index.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Dots and Boxes!");
        stage.setScene(scene);
        scene.getStylesheets().add("dotsAndBoxes.css");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
