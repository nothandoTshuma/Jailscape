package com.group18.controller;

import com.group18.core.UserRepository;
import com.group18.model.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CreateUserController extends BaseController {
    @FXML TextField userNameTextField;
    @FXML Button saveButton;
    @FXML Button cancelButton;

    public void initialize() {
        saveButton.setOnAction(e -> {
            buttonClick();
            handleCreateButtonAction();
        });

        cancelButton.setOnAction(e -> {
            buttonClick();
            handleBackButtonAction();
        });
    }

    private void handleCreateButtonAction(){
        if (!(userNameTextField.getCharacters().toString().equals(""))) {
            String userName = userNameTextField.getCharacters().toString();
            User user = new User(userName);
            UserRepository.save(user);
            loadFXMLScene("/scenes/UserSelectionMenu.fxml", "User Selection");
        }
    }

    private void handleBackButtonAction(){
        loadFXMLScene("/scenes/UserSelectionMenu.fxml", "User Selection");
    }

}
