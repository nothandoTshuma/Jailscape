package com.group18.controller;

import com.group18.core.FileReader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.util.ArrayList;

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
