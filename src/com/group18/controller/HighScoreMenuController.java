package com.group18.controller;

import com.group18.core.FileReader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.util.ArrayList;

public class HighScoreMenuController extends MenuController {
    @FXML Button backButton;
    @FXML Label level1Label;
    @FXML Label level2Label;
    @FXML Label level3Label;
    @FXML Label level4Label;
    @FXML Label level5Label;

    private final String TOP_3_HIGHSCORES_FILE = "./src/resources/Top3HighScores.txt";
    private ArrayList<String> highScoresList = new ArrayList<>();

    public void initialize() {
        displayHighScores();

        backButton.setOnAction(e -> {
            handleBackButtonAction();
        });

    }

    private void handleBackButtonAction() {
        loadFXMLScene("/resources/MainMenu.fxml", "Main Menu");
    }

    private void displayHighScores() {
        highScoresList = FileReader.getFileLines(TOP_3_HIGHSCORES_FILE);
        String[] levelList = highScoresList.get(0).split(",");
        level1Label.setText("Level 1: \n" + levelList[0] + " " + levelList[1] + "\n" + levelList[2] + " " + levelList[3] + "\n" + levelList[4] + " " + levelList[5]);
        levelList = highScoresList.get(1).split(",");
        level2Label.setText("Level 2: \n" + levelList[0] + " " + levelList[1] + "\n" + levelList[2] + " " + levelList[3] + "\n" + levelList[4] + " " + levelList[5]);
        levelList = highScoresList.get(2).split(",");
        level3Label.setText("Level 3: \n" + levelList[0] + " " + levelList[1] + "\n" + levelList[2] + " " + levelList[3] + "\n" + levelList[4] + " " + levelList[5]);
        levelList = highScoresList.get(3).split(",");
        level4Label.setText("Level 4: \n" + levelList[0] + " " + levelList[1] + "\n" + levelList[2] + " " + levelList[3] + "\n" + levelList[4] + " " + levelList[5]);
        levelList = highScoresList.get(4).split(",");
        level5Label.setText("Level 5: \n" + levelList[0] + " " + levelList[1] + "\n" + levelList[2] + " " + levelList[3] + "\n" + levelList[4] + " " + levelList[5]);
    }
}
