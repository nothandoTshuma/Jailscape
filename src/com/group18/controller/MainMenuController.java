package com.group18.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainMenuController extends MenuController {
    @FXML Button exitButton;
    @FXML Button levelsButton;
    @FXML Button highScoreButton;
    @FXML Label userNameLabel;
    @FXML Button changeUserButton;

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

        changeUserButton.setOnAction(e -> {
            handleChangeUserButtonAction();
        });

        userNameLabel.setText("User Name: Test");
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
}
