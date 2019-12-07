package com.group18.controller;

import com.group18.core.UserRepository;
import com.group18.model.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * The controller used to create a new user to be used in the game
 *
 * @author frasergrandfield
 */
public class CreateUserController extends BaseController {

    /**
     * The text field holding the potential new user's username
     */
    @FXML
    private TextField userNameTextField;

    /**
     * A button that once clicked, will create a new user
     */
    @FXML
    private Button saveButton;

    /**
     * A button that once clicked, will cancel the creation of a new user
     */
    @FXML
    private Button cancelButton;

    /**
     * Used to initialize basic functionality for this controller
     */
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

    /**
     * Handles the creation of a new user, once the button clicks the create button
     */
    private void handleCreateButtonAction() {
        if (!(userNameTextField.getCharacters().toString().equals(""))) {
            String userName = userNameTextField.getCharacters().toString();

            if (UserRepository.userExists(userName)) {
                //TODO:drt - Show alert
            } else {
                User user = new User(userName);
                UserRepository.save(user);
                loadFXMLScene("/scenes/UserSelectionMenu.fxml", "User Selection");
            }

        }
    }

    /**
     * Re-directs the user back to the user selection scene, if they wish to go back
     */
    private void handleBackButtonAction(){
        loadFXMLScene("/scenes/UserSelectionMenu.fxml", "User Selection");
    }

}
