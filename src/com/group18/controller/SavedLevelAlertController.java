package com.group18.controller;

import com.group18.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


/**
 * The controller handling the Main Menu alert's where they can load a saved level
 *
 * @author danielturato
 */
public class SavedLevelAlertController extends AlertController {

    /**
     * The label displaying a specific level
     */
    @FXML
    private Label levelLabel;

    /**
     * The button, once clicked will reset a previous saved level and start the
     * shown level from the start.
     */
    @FXML
    private Button resetLevelButton;

    /**
     * The button, once clicked will load a saved level file
     */
    @FXML
    private Button loadLevelButton;

    /**
     * The button, once clicked will hide the alert
     */
    @FXML
    private Button cancelButton;

    /**
     * Initialize this controller
     */
    @FXML
    public void initialize() {
        resetLevelButton.setOnAction(e -> {
            buttonClick();
            handleResetLevelButton(e);
        });
        loadLevelButton.setOnAction(e -> {
            buttonClick();
            handleLoadLevelButton(e);
        });
        cancelButton.setOnAction(e -> {
            buttonClick();
            handleCancelButton(e);
        });
    }

    /**
     * Setup fields for this controller pre-initialization
     */
    public void setup() {
        levelLabel.setText("Level " + currentLevel);

        if (currentLevel > 5) {
            resetLevelButton.setDisable(true);
            loadLevelButton.setDisable(true);
        }
    }


    /**
     * Handle the loading of a saved level file
     * @param actionEvent The action even associated with the button click
     */
    private void handleLoadLevelButton(ActionEvent actionEvent) {
        GameController.setUser(getUser());
        GameController.loadSavedLevel(currentLevel);
        getCurrentAlertStage().hide();

        MainMenuController.getMenuMusicPlayer().stop();
        GameController gc = new GameController(Main.getPrimaryStage());
    }


}
