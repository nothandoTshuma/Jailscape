package com.group18;

import com.group18.core.ResourceRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends javafx.application.Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        ResourceRepository.createResourceMap();
        try {
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/scenes/MessageOfTheDayDisplay.fxml"));
            Scene scene = new Scene(root, 600, 400);
            setPrimaryStage(primaryStage);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Message Of The Day");
            primaryStage.show();
       } catch (IOException e) {
           e.printStackTrace();
        }
    }

    private static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


}