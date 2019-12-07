package com.group18.controller;

import com.group18.service.MessageOfTheDayService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * This controller is used to display the message of the day
 *
 * @author frasergrandfield
 */
public class MessageOfTheDayController extends BaseController {

    /**
     * The button, once pressed will bring the user to the User Selection scene
     */
    @FXML
    private Button okayButton;

    /**
     * The button, once pressed will copy the message of the day to the user's
     * clipboard.
     */
    @FXML
    private Button copyTextButton;

    /**
     * Used to display the message of the day
     */
    @FXML
    private Label messageLabel;

    /**
     * Initialize this controller
     */
    public void initialize() {
        setMessage();

        okayButton.setOnAction(e -> {
            handleOkayButtonAction();
        });

        copyTextButton.setOnAction(e -> {
            handleCopyTextButtonAction();
        });
    }

    /**
     * Set the message of the day
     */
    private void setMessage() {
        String message = MessageOfTheDayService.getMessageOfTheDay();
        messageLabel.setText(message);
    }

    /**
     * Handle the loading of the User Selection Scene
     */
    private void handleOkayButtonAction() {
        buttonClick();
        loadFXMLScene("/scenes/UserSelectionMenu.fxml", "User Selection Menu");
    }

    /**
     * Used to copy the message of the day to the user's clipboard
     * Credit - https://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
     */
    private void handleCopyTextButtonAction() {
        buttonClick();

        String messageOfTheDay = messageLabel.getText();
        StringSelection stringSelection = new StringSelection(messageOfTheDay);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

}
