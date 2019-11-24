package com.group18.controller;

import com.group18.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML Button exitButton;
    @FXML Button levelsButton;
    @FXML Button highScoreButton;

    public void initialize() {
        exitButton.setOnAction(e -> {
            handleExitButtonAction();
        });

        levelsButton.setOnAction(e -> {
            handleLevelsButtonAction();
        });

        highScoreButton.setOnAction(e -> {
            handleHighScoreButtonAction();
        });
    }

    private void handleLevelsButtonAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/LevelsMenu.fxml"));
            BorderPane editRoot = fxmlLoader.load();
            Scene editScene = new Scene(editRoot, 600, 400);
            Stage editStage = Main.getPrimaryStage();
            editStage.setScene(editScene);
            editStage.setTitle("Levels Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleHighScoreButtonAction(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/HighScoreMenu.fxml"));
            BorderPane editRoot = fxmlLoader.load();
            Scene editScene = new Scene(editRoot, 600, 400);
            Stage editStage = Main.getPrimaryStage();
            editStage.setScene(editScene);
            editStage.setTitle("Levels Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleExitButtonAction(){
        Platform.exit();
        System.exit(0);
    }
}
