package com.group18.controller;

import com.group18.Main;
import com.group18.model.entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public abstract class BaseController {

    static void loadFXMLScene(String FXMLFile, String title) {
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

    void loadMainMenu(User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scenes/MainMenu.fxml"));
            BorderPane mainMenu = fxmlLoader.load();
            MainMenuController mainMenuController = fxmlLoader.getController();
            mainMenuController.setUser(user);
            Scene editScene = new Scene(mainMenu, 600, 400);
            Stage editStage = Main.getPrimaryStage();
            editStage.setScene(editScene);
            editStage.setTitle("Main Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Playing the button click sound to the user
     */
    static void buttonClick() {
        playSound("ButtonClick");
    }

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
}
