package com.group18.controller;

import com.group18.Main;
import com.group18.model.cell.Wall;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

public class LevelsController extends BaseController {

    private static final Logger LOGGER = Logger.getLogger("LevelsController");

    @FXML
    Button backButton;

    @FXML
    Button levelOneButton;

    @FXML
    Button levelTwoButton;

    @FXML
    Button levelThreeButton;

    @FXML
    Button levelFourButton;

    @FXML
    Button levelFiveButton;

    @FXML
    QuadCurve levelOneCurve;

    @FXML
    QuadCurve levelTwoCurve;

    @FXML
    QuadCurve levelThreeCurve;

    @FXML
    QuadCurve levelFourCurve;

    private User user;

    private Stage alertStage;

    public void initialize() {
        setButtonActions();
    }

    public void setup(User user) {
        setUser(user);
        int highestLevel = user.getHighestLevel();

        if (highestLevel == 1) {
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
        } else if (highestLevel == 2) {
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
        } else if (highestLevel == 3) {
            levelThreeCurve.setStroke(Color.web("#A50104"));
            levelFourCurve.setStroke(Color.web("#A50104"));
            levelOneButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
            levelTwoButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
            levelFourButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
            levelFourButton.setDisable(true);
            levelFiveButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
            levelFiveButton.setDisable(true);
        } else if (highestLevel == 4) {
            levelFourCurve.setStroke(Color.web("#A50104"));
            levelOneButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
            levelTwoButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
            levelThreeButton.setStyle("-fx-outer-border: black; -fx-background-color: #27d070");
            levelFiveButton.setStyle("-fx-outer-border: black; -fx-background-color: grey; -fx-opacity: 1.0");
            levelFiveButton.setDisable(true);
        } else if (highestLevel == 5) {

        }
    }

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

    private void handleBackButtonAction() {
        loadMainMenu(user);
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public Stage getAlertStage() {
        return alertStage;
    }

}
