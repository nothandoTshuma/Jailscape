package com.group18.controller;

import com.group18.Main;
import com.group18.core.AddUserName;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateUserMenuController {
    @FXML TextField userNameTextField;
    @FXML Button createButton;
    @FXML Button backButton;

    private String userName;

    public void initialize() {
        createButton.setOnAction(e -> {
            handleCreateButtonAction();
        });

        backButton.setOnAction(e -> {
            handleBackButtonAction();
        });
    }

    private void handleCreateButtonAction(){
        if (!(userNameTextField.getCharacters().toString().equals(""))) {
            userName = userNameTextField.getCharacters().toString();
            AddUserName addUserName = new AddUserName(userName);
            if (!(addUserName.isUserNameExists())) {
                addUserName.writeUserName("\n" + userName);
                loadUserSelectionMenu();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login");
                alert.setHeaderText("Error!");
                alert.setContentText(userName + " has already been taken, try again.");
                alert.showAndWait();
            }
        }
    }

    private void handleBackButtonAction(){
        loadUserSelectionMenu();
    }

    private void loadUserSelectionMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/UserSelectionMenu.fxml"));
            BorderPane editRoot = fxmlLoader.load();
            Scene editScene = new Scene(editRoot, 600, 400);
            Stage editStage = Main.getPrimaryStage();
            editStage.setScene(editScene);
            editStage.setTitle("User Selection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
