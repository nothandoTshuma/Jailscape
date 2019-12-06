package com.group18.controller;

import com.group18.Main;
import com.group18.model.entity.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

public class MainMenuController extends BaseController {

    private static final Logger LOGGER = Logger.getLogger("MainMenuController");

    @FXML Button exitButton;
    @FXML Button exitImgButton;
    @FXML Button levelsButton;
    @FXML Button levelsImgButton;
    @FXML Button leaderBoardButton;
    @FXML Button leaderBoardImgButton;
    @FXML Button usersButton;
    @FXML Button usersImgButton;
    @FXML Button startButton;
    @FXML Button startImgButton;

    private User user;

    private Stage menuAlertStage;

    private static MediaPlayer menuMusicPlayer;

    public void initialize() {
        playSound("MenuMusic");
        exitButton.setOnAction(e -> {
            handleExitButtonAction();
            playSound("ButtonClick");
        });

        exitImgButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleExitButtonAction();
        });

        levelsButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleLevelsButtonAction();
        });

        levelsImgButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleLevelsButtonAction();
        });

        leaderBoardButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleHighScoreButtonAction();
        });

        leaderBoardImgButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleHighScoreButtonAction();
        });

        usersButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleChangeUserButtonAction();
        });

        usersImgButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleChangeUserButtonAction();
        });

        startButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleStartButtonAction();
        });

        startImgButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleStartButtonAction();
        });
    }

    private void handleLevelsButtonAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainMenuController.class.getResource("/scenes/LevelsMenu.fxml"));
            BorderPane levelsMenu = fxmlLoader.load();
            LevelsController levelsController = fxmlLoader.getController();
            levelsController.setup(user);
            Scene editScene = new Scene(levelsMenu, 600, 400);
            Stage editStage = Main.getPrimaryStage();
            editStage.setScene(editScene);
            editStage.setTitle("Main Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleHighScoreButtonAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainMenuController.class.getResource("/scenes/Leaderboard.fxml"));
            BorderPane borderPane = fxmlLoader.load();
            HighScoreController controller = fxmlLoader.getController();
            controller.setUser(user);
            Scene editScene = new Scene(borderPane, 600, 400);
            Stage editStage = Main.getPrimaryStage();
            editStage.setScene(editScene);
        } catch (IOException ex) {
            //TODO:drt - handle
        }
    }

    private void handleExitButtonAction() {
        Platform.exit();
        System.exit(0);
    }

    private void handleChangeUserButtonAction() {
        loadFXMLScene("/scenes/UserSelectionMenu.fxml", "User Selection");
    }

    private void handleStartButtonAction() {
        try {
            File saveFile = new File(
                    String.format("./src/resources/saved-levels/%s-level-save%s.txt",
                            user.getUsername(), user.getHighestLevel()));

            FXMLLoader loader = saveFile.exists() ?
                    new FXMLLoader(getClass().getResource("/scenes/MenuAlert.fxml")) :
                    new FXMLLoader(getClass().getResource("/scenes/MenuAlertReset.fxml"));
            BorderPane alert = loader.load();
            AlertController controller = loader.getController();
            controller.setCurrentLevel(user.getHighestLevel());
            controller.setUser(user);
            controller.setup();

            Stage alertStage = new Stage(StageStyle.UNDECORATED);
            alertStage.initOwner(Main.getPrimaryStage());
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.setScene(new Scene(alert));
            menuAlertStage = alertStage;
            controller.setCurrentAlertStage(menuAlertStage);
            alertStage.show();
        } catch (IOException ex) {
            LOGGER.log(WARNING, "Unable to show this alert", ex);
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stage getMenuAlertStage() {
        return menuAlertStage;
    }

    public static MediaPlayer getMenuMusicPlayer() {
        return menuMusicPlayer;
    }

    public static void setMenuMusicPlayer(MediaPlayer menuMusicPlayer) {
        MainMenuController.menuMusicPlayer = menuMusicPlayer;
    }
}
