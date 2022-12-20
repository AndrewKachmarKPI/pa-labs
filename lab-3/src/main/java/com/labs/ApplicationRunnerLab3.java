package com.labs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationRunnerLab3 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("btreeDBPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 610, 400);
        stage.setTitle("Btree DB");
        scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
