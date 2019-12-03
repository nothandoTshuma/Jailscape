package com.group18.controller;

import com.group18.core.AddUserName;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CreateUserMenuController extends MenuController{
    @FXML TextField userNameTextField;
    @FXML Button saveButton;
    @FXML Button cancelButton;

    private String userName;

    public void initialize() {
        saveButton.setOnAction(e -> {
            handleCreateButtonAction();
        });

        cancelButton.setOnAction(e -> {
            handleBackButtonAction();
        });
    }

    private void handleCreateButtonAction(){
        if (!(userNameTextField.getCharacters().toString().equals(""))) {
            userName = userNameTextField.getCharacters().toString();
            AddUserName addUserName = new AddUserName(userName);
            if (!(addUserName.isUserNameExists())) {
                addUserName.writeUserName();
                loadFXMLScene("/resources/UserSelectionMenu.fxml", "User Selection");
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
        loadFXMLScene("/resources/UserSelectionMenu.fxml", "User Selection");
    }
}
