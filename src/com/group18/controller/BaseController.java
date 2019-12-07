package com.group18.controller;

import com.group18.Main;
import com.group18.model.entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * The base controller that provides default functionality for all JavaFX controllers
 *
 * @author danielturato frasergrandfield
 */
public abstract class BaseController {

    /**
     * Play a specific sound to the user
     * @param soundName The name of the sound
     */
    public static void playSound(String soundName) {
        Media sound = new Media(new File("./src/resources/sounds/"+soundName+".wav").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        if (soundName.equals("MenuMusic")) {
            MainMenuController.setMenuMusicPlayer(mediaPlayer);
        }

        if (soundName.equals("BackgroundMusic")) {
            GameController.setBackgroundMusicPlayer(mediaPlayer);
        }
        mediaPlayer.play();
    }

    /**
     * Used to load an FXML file, setting a specific title
     * @param FXMLFile The file path of the FXML file
     * @param title The title to be set on the scene
     */
    void loadFXMLScene(String FXMLFile, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BaseController.class.getResource(FXMLFile));
            BorderPane editRoot = fxmlLoader.load();
            Scene editScene = new Scene(editRoot, 600, 400);
            Stage editStage = Main.getPrimaryStage();
            editStage.setScene(editScene);
            editStage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to load the Main Menu scene & controller
     * @param user The selected user in the game
     */
    void loadMainMenu(User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scenes/MainMenu.fxml"));
            BorderPane mainMenu = fxmlLoader.load();
            MainMenuController mainMenuController = fxmlLoader.getController();
            mainMenuController.setUser(user);
            showScene(mainMenu, "Main Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a scene on the Primary Stage
     * @param parent The parent node in the scene
     * @param title The title of the scene
     */
    void showScene(Parent parent, String title) {
        Scene editScene = new Scene(parent, 600, 400);
        Stage editStage = Main.getPrimaryStage();
        editStage.setScene(editScene);
        editStage.setTitle(title);
    }

    /**
     * Playing the button click sound to the user
     */
    static void buttonClick() {
        playSound("ButtonClick");
    }


}
