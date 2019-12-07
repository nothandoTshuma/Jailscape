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

/**
 * Used to show the High Scores achieved on each level
 *
 * @author frasergrandfield
 */
public class HighScoreController extends BaseController {

    /**
     * The button, that once pressed will exit out of this scene
     */
    @FXML
    private Button backButton;

    /**
     * Used to show the username in 1st score position for a level
     */
    @FXML
    private Label user1Label;

    /**
     * Used to show the username in 2nd score position for a level
     */
    @FXML
    private Label user2Label;

    /**
     * Used to show the username in 3rd score position for a level
     */
    @FXML
    private Label user3Label;

    /**
     * Used to show the time score in 1st position for a level
     */
    @FXML
    private Label score1Label;

    /**
     * Used to show the time score in 2nd position for a level
     */
    @FXML
    private Label score2Label;

    /**
     * Used to show the time score in 3rd position for a level
     */
    @FXML
    private Label score3Label;

    /**
     * The filter option menu, allowing the user to filter between which level
     * they wish to view the high scores for.
     */
    @FXML
    private ChoiceBox levelChoiceBox;

    /**
     * Used to store the 3 username's with the top 3 scores on a particular level
     */
    private List<String> topUserNames = new ArrayList<>();

    /**
     * Used to store the 3 scores of the top 3 scores on a particular level
     */
    private List<Long> topScores = new ArrayList<>();

    /**
     * The current selected user in the game
     */
    private User user;

    /**
     * Used to setup this controller prior to being shown to the user
     */
    public void initialize() {
        levelChoiceBox.getItems().add("Level 1");
        levelChoiceBox.getItems().add("Level 2");
        levelChoiceBox.getItems().add("Level 3");
        levelChoiceBox.getItems().add("Level 4");
        levelChoiceBox.getItems().add("Level 5");

        setLabelAlignments();

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

    /**
     * Set the current selected user in the game
     * @param user The new selected user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Set the alignments for all labels displayed
     */
    private void setLabelAlignments() {
        user1Label.setTextAlignment(TextAlignment.CENTER);
        user2Label.setTextAlignment(TextAlignment.CENTER);
        user3Label.setTextAlignment(TextAlignment.CENTER);
        score1Label.setTextAlignment(TextAlignment.CENTER);
        score2Label.setTextAlignment(TextAlignment.CENTER);
        score3Label.setTextAlignment(TextAlignment.CENTER);
    }

    /**
     * Handles the scores shown to user, based on the filter selection
     */
    private void handleLevelChoiceBoxAction() {
        if(levelChoiceBox.getSelectionModel().getSelectedItem().equals("Level 1")) {
            getThreeHighestScores(1);
            displayScores();
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("Level 2")) {
            getThreeHighestScores(2);
            displayScores();
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("Level 3")) {
            getThreeHighestScores(3);
            displayScores();
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("Level 4")) {
            getThreeHighestScores(4);
            displayScores();
        } else if (levelChoiceBox.getSelectionModel().getSelectedItem().equals("Level 5")) {
            getThreeHighestScores(5);
            displayScores();
        }
    }

    /**
     * Loads the Main Menu once the user clicks the back button
     */
    private void handleBackButtonAction() {
        loadMainMenu(user);
    }

    /**
     * Get's the three highest scores for the selected level
     * @param level The selected level
     */
    private void getThreeHighestScores(int level) {
        topUserNames.clear();
        topScores.clear();
        List<User> users = UserRepository.getAll();
        List<String> allUsernames = new ArrayList<>();
        List<Long> allScores = new ArrayList<>();

        // Get all potential times for each user who has reached the filter level
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getHighestLevel() >= level) {
                allUsernames.add(users.get(i).getUsername());
                allUsernames.add(users.get(i).getUsername());
                allUsernames.add(users.get(i).getUsername());
                allScores.add(users.get(i).getQuickestTimesFor(level)[0]);
                allScores.add(users.get(i).getQuickestTimesFor(level)[1]);
                allScores.add(users.get(i).getQuickestTimesFor(level)[2]);
            }
        }

        bubbleSort(allScores, allUsernames);
    }

    /**
     * Complete bubble sort on the completion times for the filtered level,
     * maintaining the link to their username. Then getting only the top 3.
     * @param scores The list that will hold the sorted scores
     * @param names The list that will usernames linked to the sorted scores
     */
    private void bubbleSort(List<Long> scores, List<String> names) {
        boolean sorted = false;
        int n = 0;

        while (!sorted) {
            sorted = true;

            for (int i = 0; i < scores.size() - n - 1; i++) {
                if (scores.get(i) > scores.get(i+1)) {
                    swap(scores, names, i, i+1);
                    sorted = false;
                }
            }
            n++;
        }

        for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i) != 0) {
                topScores.add(scores.get(i));
                topUserNames.add(names.get(i));
            }
        }
    }

    /**
     * Swap completion times & usernames in given lists
     * @param scores The scores list
     * @param names The names list
     * @param i The first index to be swapped
     * @param j The second index to be swapped
     */
    private void swap(List<Long> scores, List<String> names, int i, int j) {
        String tempName = names.get(i);
        names.set(i, names.get(j));
        names.set(j, tempName);

        Long tempTime = scores.get(i);
        scores.set(i, scores.get(j));
        scores.set(j, tempTime);
    }

    /**
     * Set the specific labels, displaying the top 3 scores for the selected level
     */
    private void displayScores() {
        int size = topUserNames.size();

        setBasicLabels();

        if (size >= 1) {
            user1Label.setText(topUserNames.get(0));
            score1Label.setText(getFormattedTime(topScores.get(0)));
        }

        if (size >= 2) {
            user2Label.setText(topUserNames.get(1));
            score2Label.setText(getFormattedTime(topScores.get(1)));
        }

        if (size >= 3) {
            user3Label.setText(topUserNames.get(2));
            score3Label.setText(getFormattedTime(topScores.get(2)));
        }

    }

    /**
     * Set the basic text of all leaderboard labels
     */
    private void setBasicLabels() {
        user1Label.setText("N/A");
        score1Label.setText("0");
        user2Label.setText("N/A");
        score2Label.setText("0");
        user3Label.setText("N/A");
        score3Label.setText("0");
    }

    /**
     * Used to formatted a time from milliseconds into minutes & seconds
     * Credit - https://stackoverflow.com/questions/17624335/converting-milliseconds-to-minutes-and-seconds
     * @param time The time to be formatted (ms)
     * @return The formatted time
     */
    private String getFormattedTime(Long time) {
        return String.format("%d : %d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
        );
    }

}

