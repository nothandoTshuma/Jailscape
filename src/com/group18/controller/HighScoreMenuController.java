package com.group18.controller;

import com.group18.core.FileReader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class HighScoreMenuController extends MenuController {
    @FXML Button backButton;
    @FXML Label Level1Label;
    @FXML Label Level2Label;
    @FXML Label Level3Label;
    @FXML Label Level4Label;
    @FXML Label Level5Label;

    private final String TOP_3_HIGHSCORES_FILE = "/resources/Top3HighScores.txt";
    private ArrayList<String> highScoresList = new ArrayList<>();

    public void initialize() {
        FileReader reader = new FileReader(TOP_3_HIGHSCORES_FILE);
        highScoresList = reader.getArray();


        backButton.setOnAction(e -> {
            handleBackButtonAction();
        });

    }

    private void handleBackButtonAction() {
        loadFXMLScene("/resources/MainMenu.fxml", "Main Menu");
    }
}
