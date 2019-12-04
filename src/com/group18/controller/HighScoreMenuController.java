package com.group18.controller;

import com.group18.core.FileReader;
import com.group18.core.UserRepository;
import com.group18.exception.InvalidLevelException;
import com.group18.model.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.util.*;
import java.util.stream.Collectors;

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
            try {
                handleLevelChoiceBoxAction();
            } catch (InvalidLevelException ex) {
                ex.printStackTrace();
            }
        });

        backButton.setOnAction(e -> {
            handleBackButtonAction();
        });

    }

    private void handleLevelChoiceBoxAction() throws InvalidLevelException {
        if(levelChoiceBox.getSelectionModel().getSelectedItem().equals("level1")) {
            Map<String, Long> topThreeScores = getThreeHighestScores(1);
            addToLabels(topThreeScores);
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("level2")) {
            Map<String, Long> topThreeScores = getThreeHighestScores(2);
            addToLabels(topThreeScores);
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("level3")) {
            Map<String, Long> topThreeScores = getThreeHighestScores(3);
            addToLabels(topThreeScores);
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("level4")) {
            Map<String, Long> topThreeScores = getThreeHighestScores(4);
            addToLabels(topThreeScores);
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("level5")) {
            Map<String, Long> topThreeScores = getThreeHighestScores(5);
            addToLabels(topThreeScores);
        }
    }

    private void handleBackButtonAction() {
        loadFXMLScene("/resources/MainMenu.fxml", "Main Menu");
    }

    private Map<String, Long> getThreeHighestScores(int level) throws InvalidLevelException {
        List<User> userList = UserRepository.getAll();

        Map<String,Long> quickestTimes = new HashMap<>();

        for (int i = 0; i < userList.size(); i++) {
            quickestTimes.put(userList.get(i).getUsername(), userList.get(i).getQuickestTimesFor(level)[2]);
        }

        Map<String, Long> topThreeTimes = new TreeMap<>();
        while (topThreeTimes.size() < 3) {
            String username = "";
            Long time = -1L;
            for (Map.Entry<String, Long> keySet : quickestTimes.entrySet()) {
                if (keySet.getValue() > time) {
                    username = keySet.getKey();
                    time = keySet.getValue();
                }
            }
            topThreeTimes.put(username, time);
            quickestTimes.remove(username);
        }

        return topThreeTimes.entrySet().stream()
                                        .sorted(Map.Entry.comparingByValue())
                                        .collect(
                                                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    private void addToLabels(Map<String, Long> topThreeScores) {
        ArrayList<String> userNameList = new ArrayList<>();
        ArrayList<Long> scoreList = new ArrayList<>();

        for (Map.Entry<String, Long> keySet : topThreeScores.entrySet()) {
            userNameList.add(keySet.getKey());
            scoreList.add(keySet.getValue());
        }
        user1Label.setText(userNameList.get(0));
        user2Label.setText(userNameList.get(1));
        user3Label.setText(userNameList.get(2));
        score1Label.setText(String.valueOf(scoreList.get(0)));
        score2Label.setText(String.valueOf(scoreList.get(0)));
        score3Label.setText(String.valueOf(scoreList.get(0)));

    }

}
