package com.group18.controller;

import com.group18.core.FileReader;
import com.group18.core.UserRepository;
import com.group18.exception.InvalidLevelException;
import com.group18.model.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.List;

public class HighScoreMenuController extends MenuController {
    @FXML Button backButton;
    @FXML Label user1Label;
    @FXML Label user2Label;
    @FXML Label user3Label;
    @FXML Label score1Label;
    @FXML Label score2Label;
    @FXML Label score3Label;
    @FXML ChoiceBox levelChoiceBox;

    public void initialize() {
        levelChoiceBox.getItems().add("level1");
        levelChoiceBox.getItems().add("level2");
        levelChoiceBox.getItems().add("level3");
        levelChoiceBox.getItems().add("level4");
        levelChoiceBox.getItems().add("level5");

        levelChoiceBox.setOnAction(e -> {
            handleLevelChoiceBoxAction();
        });

        backButton.setOnAction(e -> {
            handleBackButtonAction();
        });

    }

    private void handleLevelChoiceBoxAction() {
        if(levelChoiceBox.getSelectionModel().getSelectedItem().equals("level1")) {
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("level2")) {
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("level3")) {
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("level4")) {
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("level5")) {
        }
    }

    private void handleBackButtonAction() {
        loadFXMLScene("/resources/MainMenu.fxml", "Main Menu");
    }

    private String[][] getThreeHighestScores(int level) throws InvalidLevelException {
        String[][] usersAndScore = new String[3][2];
        List<User> userList = UserRepository.getAll();
        String[][] tempList = new String[userList.size()][];
        for (int i = 0; i < userList.size(); i++) {
            tempList[i][0] = userList.get(i).getUsername();
            tempList[i][1] = String.valueOf(userList.get(i).getQuickestTimesFor(level));
        }

        return  usersAndScore;
    }

}
