package com.group18.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * The controller which controls Alert's that only give the user an option
 * to start a new fresh base level.
 *
 * @author danielturato
 */
public class NewLevelAlertController extends AlertController {

    /**
     * The button, once clicked will start the game at a specific keve
     */
    @FXML
    private Button startLevelButton;

    /**
     * The button, once clicked will hide this alert
     */
    @FXML
    private Button cancelButton;

    /**
     * This label shows the specific level the alert is for
     */
    @FXML
    private Label levelLabel;

    /**
     * Initialize this controller
     */
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

    /**
     * Setup fields in this controller pre-initialization
     */
    @Override
    public void setup() {
        levelLabel.setText("Level " + currentLevel);

        if (currentLevel > 5) {
            startLevelButton.setDisable(true);
        }
    }
}
