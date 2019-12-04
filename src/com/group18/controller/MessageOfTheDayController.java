package com.group18.controller;

import com.group18.service.MessageOfTheDayService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MessageOfTheDayController extends MenuController {
    @FXML Button okayButton;
    @FXML Button copyTextButton;
    @FXML Label messageLabel;

    public void initialize() {
        setMessage();

        okayButton.setOnAction(e -> {
            handleOkayButtonAction();
        });

        copyTextButton.setOnAction(e -> {
            handleCopyTextButtonAction();
        });
    }

    private void setMessage() {
        String message = MessageOfTheDayService.getMessageOfTheDay();
        messageLabel.setText(message);
    }

    private void handleOkayButtonAction() {
        loadFXMLScene("/resources/UserSelectionMenu.fxml", "User Selection Menu");
    }

    private void handleCopyTextButtonAction() {

    }

}
