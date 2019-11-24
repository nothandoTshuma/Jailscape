package com.group18.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LevelsMenuController extends MenuController {
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
