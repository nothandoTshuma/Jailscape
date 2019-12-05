package com.group18.controller;

import com.group18.service.MessageOfTheDayService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class MessageOfTheDayController extends BaseController {
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
        buttonClick();
        loadFXMLScene("/scenes/UserSelectionMenu.fxml", "User Selection Menu");
    }

    private void handleCopyTextButtonAction() {
        buttonClick();

        String messageOfTheDay = messageLabel.getText();
        StringSelection stringSelection = new StringSelection(messageOfTheDay);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

}
