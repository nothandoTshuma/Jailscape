package com.group18.controller;

import com.group18.core.UserRepository;
import com.group18.exception.InvalidLevelException;
import com.group18.model.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class HighScoreController extends BaseController {
    @FXML Button backButton;
    @FXML Label user1Label;
    @FXML Label user2Label;
    @FXML Label user3Label;
    @FXML Label score1Label;
    @FXML Label score2Label;
    @FXML Label score3Label;
    @FXML ChoiceBox levelChoiceBox;

    private ArrayList<String> topUserNames = new ArrayList<>();
    private ArrayList<Long> topScores = new ArrayList<>();

    private User user;

    public void initialize() {
        levelChoiceBox.getItems().add("Level 1");
        levelChoiceBox.getItems().add("Level 2");
        levelChoiceBox.getItems().add("Level 3");
        levelChoiceBox.getItems().add("Level 4");
        levelChoiceBox.getItems().add("Level 5");

        setLabelAllignments();

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

    private void setLabelAllignments() {
        user1Label.setTextAlignment(TextAlignment.CENTER);
        user2Label.setTextAlignment(TextAlignment.CENTER);
        user3Label.setTextAlignment(TextAlignment.CENTER);
        score1Label.setTextAlignment(TextAlignment.CENTER);
        score2Label.setTextAlignment(TextAlignment.CENTER);
        score3Label.setTextAlignment(TextAlignment.CENTER);
    }

    private void handleLevelChoiceBoxAction() throws InvalidLevelException {
        if(levelChoiceBox.getSelectionModel().getSelectedItem().equals("Level 1")) {
            getThreeHighestScores(1);
            addToLabels();
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("Level 2")) {
            getThreeHighestScores(2);
            addToLabels();
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("Level 3")) {
            getThreeHighestScores(3);
            addToLabels();
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("Level 4")) {
            getThreeHighestScores(4);
            addToLabels();
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("Level 5")) {
            getThreeHighestScores(5);
            addToLabels();
        }
    }

    private void handleBackButtonAction() {
        loadMainMenu(user);
    }

    private void getThreeHighestScores(int level) throws InvalidLevelException {
        topUserNames.clear();
        topScores.clear();
        List<User> userList = UserRepository.getAll();
        ArrayList<String> userNamesList = new ArrayList<>();
        ArrayList<Long> scoresList = new ArrayList<>();

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getHighestLevel() >= level) {
                userNamesList.add(userList.get(i).getUsername());
                userNamesList.add(userList.get(i).getUsername());
                userNamesList.add(userList.get(i).getUsername());
                scoresList.add(userList.get(i).getQuickestTimesFor(level)[0]);
                scoresList.add(userList.get(i).getQuickestTimesFor(level)[1]);
                scoresList.add(userList.get(i).getQuickestTimesFor(level)[2]);
            }
        }
        bubbleSort(scoresList, userNamesList);

    }
    private void bubbleSort(ArrayList<Long> scores, ArrayList<String> name) {
        ArrayList<Long> arrScore = scores;
        ArrayList<String> arrName = name;
        int n = arrScore.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arrScore.get(j) > arrScore.get(j + 1)) {
                    Long tempScore = arrScore.get(j);
                    arrScore.set(j, arrScore.get(j + 1));
                    arrScore.set(j + 1, tempScore);

                    String tempName = arrName.get(j);
                    arrName.set(j, arrName.get(j + 1));
                    arrName.set(j + 1, tempName);
                }
            }
        }
        int j = 0;
        for(int i = 0; i < arrScore.size();i++) {
            if (j < 3 && arrScore.get(i) != 0) {
                topUserNames.add(arrName.get(i));
                topScores.add(arrScore.get(i));
                j++;
            }
        }
    }

    private void addToLabels() {
        if (topUserNames.size() == 3) {
            user1Label.setText(topUserNames.get(0));
            user2Label.setText(topUserNames.get(1));
            user3Label.setText(topUserNames.get(2));
            System.out.println(topScores.get(0));
            score1Label.setText(getFormattedTime(topScores.get(0)));
            score2Label.setText(getFormattedTime(topScores.get(1)));
            score3Label.setText(getFormattedTime(topScores.get(2)));
        } else if (topUserNames.size() == 2) {
            user1Label.setText(topUserNames.get(0));
            user2Label.setText(topUserNames.get(1));
            score1Label.setText(getFormattedTime(topScores.get(0)));
            score2Label.setText(getFormattedTime(topScores.get(1)));
            user3Label.setText("N/A");
            score3Label.setText("0");
        } else if (topUserNames.size() == 1) {
            user1Label.setText(topUserNames.get(0));
            score1Label.setText(getFormattedTime(topScores.get(0)));
            user2Label.setText("N/A");
            score2Label.setText("0");
            user3Label.setText("N/A");
            score3Label.setText("0");
        } else if (topUserNames.size() == 0) {
            user1Label.setText("N/A");
            score1Label.setText("0");
            user2Label.setText("N/A");
            score2Label.setText("0");
            user3Label.setText("N/A");
            score3Label.setText("0");
        }
    }

    /**
     * Credit *-*
     * @param finishTime
     * @return
     */
    private String getFormattedTime(Long finishTime) {
        return String.format("%d : %d",
                TimeUnit.MILLISECONDS.toMinutes(finishTime),
                TimeUnit.MILLISECONDS.toSeconds(finishTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finishTime))
        );
    }

    public void setUser(User user) {
        this.user = user;
    }
}

