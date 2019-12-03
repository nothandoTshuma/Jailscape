package com.group18;

import com.group18.controller.GameController;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
       try {
           BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/resources/MessageOfTheDayDisplay.fxml"));
            Scene scene = new Scene(root, 600, 400);
            this.primaryStage = primaryStage;
            primaryStage.setScene(scene);
            primaryStage.setTitle("Message Of The Day");
            primaryStage.show();
       } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


}