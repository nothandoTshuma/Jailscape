package com.group18.controller;

import com.group18.Main;
import com.group18.core.UserRepository;
import com.group18.model.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

/**
 * The controller used to create a new user to be used in the game
 *
 * @author frasergrandfield
 */
public class CreateUserController extends BaseController {

    /**
     * Used to log exceptions/messages to the console
     */
    private static final Logger LOGGER = Logger.getLogger("CreateUserController");

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

            try {
                if (UserRepository.userExists(userName)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/AlertMessage.fxml"));
                    BorderPane alert = loader.load();
                    WarningAlertController controller = loader.getController();
                    controller.setMessage("You are trying to create a user that already exists!");

                    Stage alertStage = new Stage(StageStyle.TRANSPARENT);
                    controller.setAlertStage(alertStage);
                    alertStage.initOwner(Main.getPrimaryStage());
                    alertStage.initModality(Modality.APPLICATION_MODAL);
                    alertStage.setScene(new Scene(alert));
                    alertStage.show();

                } else {
                    User user = new User(userName);
                    UserRepository.save(user);
                    loadFXMLScene("/scenes/UserSelectionMenu.fxml", "User Selection");
                }
            } catch (IOException ex) {
                LOGGER.log(WARNING, "There was a problem loading an FXML file", ex);
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
