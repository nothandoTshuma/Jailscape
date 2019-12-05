package com.group18.controller;

import com.group18.Main;
import com.group18.core.LevelSaver;
import com.group18.model.entity.User;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public abstract class AlertController extends BaseController {
    int currentLevel;

    private User user;

    private Stage currentAlertStage;

    public abstract void setup();

    void handleResetLevelButton(ActionEvent actionEvent) {
        user.resetInventory(currentLevel);
        LevelSaver.delete("./src/resources/saved-levels/" +
                         user.getUsername() + "-level-save" + currentLevel + ".txt");
        GameController.setUser(user);
        GameController.loadBaseLevel(currentLevel);
        MainMenuController.getMenuMusicPlayer().stop();
        currentAlertStage.hide();
        GameController gc = new GameController(Main.getPrimaryStage());
    }

    void handleCancelButton(ActionEvent actionEvent) {
        currentAlertStage.hide();
    }

    /**
     * Get the current level for this Menu Alert
     * @return The current level
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Set the current level for this Menu Alert
     * @param level The new current level
     */
    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setCurrentAlertStage(Stage currentAlertStage) {
        this.currentAlertStage = currentAlertStage;
    }

    public Stage getCurrentAlertStage() {
        return currentAlertStage;
    }
}
