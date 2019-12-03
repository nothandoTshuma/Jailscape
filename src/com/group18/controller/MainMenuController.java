package com.group18.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainMenuController extends MenuController {
    @FXML Button exitButton;
    @FXML Button exitImgButton;
    @FXML Button levelsButton;
    @FXML Button levelsImgButton;
    @FXML Button leaderBoardButton;
    @FXML Button leaderBoardImgButton;
    @FXML Button usersButton;
    @FXML Button usersImgButton;
    @FXML Button startButton;
    @FXML Button startImgButton;

    public void initialize() {
        exitButton.setOnAction(e -> {
            handleExitButtonAction();
        });

        exitImgButton.setOnAction(e -> {
            handleExitButtonAction();
        });

        levelsButton.setOnAction(e -> {
            handleLevelsButtonAction();
        });

        levelsImgButton.setOnAction(e -> {
            handleLevelsButtonAction();
        });

        leaderBoardButton.setOnAction(e -> {
            handleHighScoreButtonAction();
        });

        leaderBoardImgButton.setOnAction(e -> {
            handleHighScoreButtonAction();
        });

        usersButton.setOnAction(e -> {
            handleChangeUserButtonAction();
        });

        usersImgButton.setOnAction(e -> {
            handleChangeUserButtonAction();
        });

        startButton.setOnAction(e -> {
            handleStartButtonAction();
        });

        startImgButton.setOnAction(e -> {
            handleStartButtonAction();
        });
    }

    private void handleLevelsButtonAction() {
        loadFXMLScene("/resources/LevelsMenu.fxml", "Levels Menu");
    }

    private void handleHighScoreButtonAction() {
        loadFXMLScene("/resources/HighScoreMenu.fxml", "High Score Menu");
    }

    private void handleExitButtonAction() {
        Platform.exit();
        System.exit(0);
    }

    private void handleChangeUserButtonAction() {
        loadFXMLScene("/resources/UserSelectionMenu.fxml", "User Selection");
    }

    private void handleStartButtonAction() {
        int highestLevel = UserSelectionController.user.getHighestLevel();

    }
}
