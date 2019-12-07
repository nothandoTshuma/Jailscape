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
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

/**
 * The controller controlling the behaviour required of the Main Menu
 *
 * @author frasergrandfield danielturato
 */
public class MainMenuController extends BaseController {

    /**
     * Used to output important exceptions/messages to the console
     */
    private static final Logger LOGGER = Logger.getLogger("MainMenuController");

    /**
     * The current music player
     */
    private static MediaPlayer menuMusicPlayer;

    /**
     * The button, that once pressed will exit the game
     */
    @FXML
    private Button exitButton;

    /**
     * The button, that once pressed will exit the game
     */
    @FXML
    private Button exitImgButton;

    /**
     * The button, that once pressed will bring the user onto the level selection scene
     */
    @FXML
    private Button levelsButton;

    /**
     * The button, that once pressed will bring the user onto the level selection scene
     */
    @FXML
    private Button levelsImgButton;

    /**
     * The button, that once pressed will bring the user onto the leaderboard scene
     */
    @FXML
    private Button leaderBoardButton;

    /**
     * The button, that once pressed will bring the user onto the leaderboard scene
     */
    @FXML
    private Button leaderBoardImgButton;

    /**
     * The button, that once pressed will bring the user onto the user selection scene
     */
    @FXML
    private Button usersButton;

    /**
     * The button, that once pressed will bring the user onto the user selection scene
     */
    @FXML
    private Button usersImgButton;

    /**
     * The button, that once pressed will start the game on the user's current highest level
     */
    @FXML
    private Button startButton;

    /**
     * The button, that once pressed will start the game on the user's current highest level
     */
    @FXML
    private Button startImgButton;

    /**
     * The current selected user in the game
     */
    private User user;

    /**
     * The current alert stage possibly being display
     */
    private Stage menuAlertStage;

    /**
     * Initialize this controller
     */
    public void initialize() {
        playSound("MenuMusic");

        setupExitButtons();
        setupLevelSelectionsButtons();
        setupLeaderboardButtons();
        setupUserSelectionButtons();
        setupStartButtons();
    }

    /**
     * Get the current music player
     * @return The music player
     */
    public static MediaPlayer getMenuMusicPlayer() {
        return menuMusicPlayer;
    }

    /**
     * Set the current music player
     * @param menuMusicPlayer The new music player
     */
    public static void setMenuMusicPlayer(MediaPlayer menuMusicPlayer) {
        MainMenuController.menuMusicPlayer = menuMusicPlayer;
    }

    /**
     * Set's the current selected user in the game
     * @param user The new current user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get's the current menu alert stage
     * @return The menu alert stage
     */
    public Stage getMenuAlertStage() {
        return menuAlertStage;
    }


    /**
     * Used to setup the start button actions
     */
    private void setupStartButtons() {
        startButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleStartButtonAction();
        });

        startImgButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleStartButtonAction();
        });
    }

    /**
     * Used to setup the user selection button actions
     */
    private void setupUserSelectionButtons() {
        usersButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleChangeUserButtonAction();
        });

        usersImgButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleChangeUserButtonAction();
        });
    }

    /**
     * Used to setup the leaderboard selection button actions
     */
    private void setupLeaderboardButtons() {
        leaderBoardButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleHighScoreButtonAction();
        });

        leaderBoardImgButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleHighScoreButtonAction();
        });
    }

    /**
     * Used to setup the level selection button actions
     */
    private void setupLevelSelectionsButtons() {
        levelsButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleLevelsButtonAction();
        });

        levelsImgButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleLevelsButtonAction();
        });
    }

    /**
     * Used to setup the exit button actions
     */
    private void setupExitButtons() {
        exitButton.setOnAction(e -> {
            handleExitButtonAction();
            playSound("ButtonClick");
        });

        exitImgButton.setOnAction(e -> {
            playSound("ButtonClick");
            handleExitButtonAction();
        });
    }

    /**
     * Handles the loading of the Level Selection Scene
     */
    private void handleLevelsButtonAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainMenuController.class.getResource("/scenes/LevelsMenu.fxml"));
            BorderPane levelsMenu = fxmlLoader.load();
            LevelsController levelsController = fxmlLoader.getController();
            levelsController.setup(user);
            showScene(levelsMenu, "Level Selection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the loading of the Leaderboard scene
     */
    private void handleHighScoreButtonAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainMenuController.class.getResource("/scenes/Leaderboard.fxml"));
            BorderPane borderPane = fxmlLoader.load();
            HighScoreController controller = fxmlLoader.getController();
            controller.setUser(user);
            showScene(borderPane, "Leaderboard");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handles the exit of the program
     */
    private void handleExitButtonAction() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Handles the loading of the User Selection Scene
     */
    private void handleChangeUserButtonAction() {
        loadFXMLScene("/scenes/UserSelectionMenu.fxml", "User Selection");
    }

    /**
     * Handles the loading of an alert to the user, upon pressing start
     */
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

}
