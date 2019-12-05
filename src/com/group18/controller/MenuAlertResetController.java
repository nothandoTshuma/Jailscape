package com.group18.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MenuAlertResetController extends AlertController {
    @FXML
    private Button startLevelButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label levelLabel;

    public void initialize() {
        cancelButton.setOnAction(e -> {
            buttonClick();
            handleCancelButton(e);
        });

        startLevelButton.setOnAction(e -> {
            buttonClick();
            handleResetLevelButton(e);
        });
    }

    @Override
    public void setup() {
        levelLabel.setText("Level " + currentLevel);

        if (currentLevel > 5) {
            startLevelButton.setDisable(true);
        }
    }
}
