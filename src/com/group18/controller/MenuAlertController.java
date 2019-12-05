package com.group18.controller;

import com.group18.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

/**
 * The controller handling the Main Menu alert's
 *
 * @author danielturato
 */
public class MenuAlertController extends AlertController {
    @FXML
    private Label levelLabel;

    @FXML
    private Button resetLevelButton;

    @FXML
    private Button loadLevelButton;

    @FXML
    private Button cancelButton;


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

    public void setup() {
        levelLabel.setText("Level " + currentLevel);

        if (currentLevel > 5) {
            resetLevelButton.setDisable(true);
            loadLevelButton.setDisable(true);
        }
    }


    private void handleLoadLevelButton(ActionEvent actionEvent) {
        GameController.setUser(getUser());
        GameController.loadSavedLevel(currentLevel);
        getCurrentAlertStage().hide();

        MainMenuController.getMenuMusicPlayer().stop();
        GameController gc = new GameController(Main.getPrimaryStage());
    }


}
