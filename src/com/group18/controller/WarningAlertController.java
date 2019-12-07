package com.group18.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Used to display warning alert's the User
 *
 * @author danielturato
 */
public class WarningAlertController extends BaseController {
    /**
     * The button, once pressed will hide the alert
     */
    @FXML
    private Button okayButton;

    /**
     * The label displaying the Alert message
     */
    @FXML
    private Label messageLabel;

    /**
     * The stage for this alert
     */
    private Stage alertStage;

    /**
     * Initialize the controller
     */
    public void initialize() {
        okayButton.setOnAction(e -> {
            buttonClick();
            alertStage.hide();
            loadFXMLScene("/scenes/NewUser.fxml", "Create new user");
        });
    };


    /**
     * Set the message to be displayed to the User
     * @param message The new message to be displayed
     */
    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    /**
     * Set the alert stage for this Alert
     * @param alertStage The new alert stage
     */
    public void setAlertStage(Stage alertStage) {
        this.alertStage = alertStage;
    }
}
