package com.group18.controller;

import com.group18.Main;
import com.group18.model.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

/**
 * Controls the level selection for the game
 *
 * @author danielturato
 */
public class LevelsController extends BaseController {

    /**
     * Used to output specific exceptions/messages to the console
     */
    private static final Logger LOGGER = Logger.getLogger("LevelsController");

    /**
     * The back button, that once clicked with hide the scene
     */
    @FXML
    Button backButton;

    /**
     * The button, that once clicked will ask the user if they wish to play level one
     */
    @FXML
    Button levelOneButton;

    /**
     * The button, that once clicked will ask the user if they wish to play level two
     */
    @FXML
    Button levelTwoButton;

    /**
     * The button, that once clicked will ask the user if they wish to play level three
     */
    @FXML
    Button levelThreeButton;

    /**
     * The button, that once clicked will ask the user if they wish to play level four
     */
    @FXML
    Button levelFourButton;

    /**
     * The button, that once clicked will ask the user if they wish to play level five
     */
    @FXML
    Button levelFiveButton;

    /**
     * Shows if the user can progress from level one onto level two
     */
    @FXML
    QuadCurve levelOneCurve;

    /**
     * Shows if the user can progress from level two onto level three
     */
    @FXML
    QuadCurve levelTwoCurve;

    /**
     * Shows if the user can progress from level three onto level four
     */
    @FXML
    QuadCurve levelThreeCurve;

    /**
     * Shows if the user can progress from level four onto level five
     */
    @FXML
    QuadCurve levelFourCurve;

    /**
     * Holds the current selected user in the game
     */
    private User user;

    /**
     * The current alert stage that can be shown
     */
    private Stage alertStage;

    /**
     * Initialize this controller
     */
    public void initialize() {
        setButtonActions();
    }

    /**
     * Setup all basic UI elements needed for this controller
     * @param user The current selected user
     */
    public void setup(User user) {
        setUser(user);
        int highestLevel = user.getHighestLevel();

        if (highestLevel == 1) {
            newUser();
        } else if (highestLevel == 2) {
            levelTwoOnwards();
        } else if (highestLevel == 3) {
            levelThreeOnwards();
        } else if (highestLevel == 4) {
            levelFourOnwards();
        } else if (highestLevel == 5) {
            lastLevel();
        }
    }

    /**
     * Set the UI styles if the user is on the last level
     */
    private void lastLevel() {
        levelOneButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
        levelTwoButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
        levelThreeButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
        levelFourButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
    }

    /**
     * Set the UI styles if the user has completed level one, two & three
     */
    private void levelFourOnwards() {
        levelFourCurve.setStroke(Color.web("#A50104"));
        levelOneButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
        levelTwoButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
        levelThreeButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
        levelFiveButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
        levelFiveButton.setDisable(true);
    }

    /**
     * Set the UI styles if the user has completed level one & two
     */
    private void levelThreeOnwards() {
        levelThreeCurve.setStroke(Color.web("#A50104"));
        levelFourCurve.setStroke(Color.web("#A50104"));
        levelOneButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
        levelTwoButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
        levelFourButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
        levelFourButton.setDisable(true);
        levelFiveButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
        levelFiveButton.setDisable(true);
    }

    /**
     * Set the UI styles if the user has completed level one
     */
    private void levelTwoOnwards() {
        levelTwoCurve.setStroke(Color.web("#A50104"));
        levelThreeCurve.setStroke(Color.web("#A50104"));
        levelFourCurve.setStroke(Color.web("#A50104"));
        levelOneButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
        levelThreeButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
        levelThreeButton.setDisable(true);
        levelFourButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
        levelFourButton.setDisable(true);
        levelFiveButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
        levelFiveButton.setDisable(true);
    }

    /**
     * Set the base styles if no levels have been completed
     */
    private void newUser() {
        levelOneCurve.setStroke(Color.web("#A50104"));
        levelTwoCurve.setStroke(Color.web("#A50104"));
        levelThreeCurve.setStroke(Color.web("#A50104"));
        levelFourCurve.setStroke(Color.web("#A50104"));
        levelTwoButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
        levelTwoButton.setDisable(true);
        levelThreeButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
        levelThreeButton.setDisable(true);
        levelFourButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
        levelFourButton.setDisable(true);
        levelFiveButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
        levelFiveButton.setDisable(true);
    }

    /**
     * Get the current selected user
     * @return The current selected user
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Set the current selected user
     * @param user The new selected user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get the current alert stage
     * @return The current alert stage
     */
    public Stage getAlertStage() {
        return alertStage;
    }


    /**
     * Setup all the button actions for this controller
     */
    private void setButtonActions() {
        backButton.setOnAction(e -> {
            handleBackButtonAction();
        });

        levelOneButton.setOnAction(e -> {
            buttonClick();
            showAlertFor(1);
        });

        levelTwoButton.setOnAction(e -> {
            buttonClick();
            showAlertFor(2);
        });

        levelThreeButton.setOnAction(e -> {
           buttonClick();
           showAlertFor(3);
        });

        levelFourButton.setOnAction(e -> {
            buttonClick();
            showAlertFor(4);
        });

        levelFiveButton.setOnAction(e -> {
            buttonClick();
            showAlertFor(5);
        });
    }

    /**
     * Load the Main Menu once the user clicks the back button
     */
    private void handleBackButtonAction() {
        loadMainMenu(user);
    }

    /**
     * Show a specific alert, depending if the user has a saved file for the level selected
     * @param level The requested level to be played
     */
    private void showAlertFor(int level) {
        try {
            File saveFile = new File(
                    String.format("./src/resources/saved-levels/%s-level-save%s.txt",
                            user.getUsername(), level));

            FXMLLoader loader = saveFile.exists() ?
                    new FXMLLoader(getClass().getResource("/scenes/MenuAlert.fxml")) :
                    new FXMLLoader(getClass().getResource("/scenes/MenuAlertReset.fxml"));
            BorderPane alert = loader.load();
            AlertController controller = loader.getController();
            controller.setUser(user);
            controller.setCurrentLevel(level);
            controller.setup();

            Stage alertStage = new Stage(StageStyle.UNDECORATED);
            alertStage.initOwner(Main.getPrimaryStage());
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.setScene(new Scene(alert));
            this.alertStage = alertStage;
            controller.setCurrentAlertStage(alertStage);
            alertStage.show();

        } catch (IOException ex) {
            LOGGER.log(WARNING, "Unable to show this alert", ex);
        }
    }

}
