package com.group18.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class HighScoreMenuController extends MenuController {
    @FXML Button backButton;

    public void initialize() {
        backButton.setOnAction(e -> {
            handleBackButtonAction();
        });
    }

    private void handleBackButtonAction() {
        loadFXMLScene("/resources/MainMenu.fxml", "Main Menu");
    }

}
