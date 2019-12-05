package com.group18.controller;

import com.group18.Main;
import com.group18.core.LevelSaver;
import com.group18.core.UserRepository;
import com.group18.model.entity.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class UserSelectionController extends BaseController {


    @FXML Button exitButton;
    @FXML Button goButton;
    @FXML Button createProfileButton;
    @FXML Button deleteButton;
    @FXML ListView userListView;

    private ObservableList observableList = FXCollections.observableArrayList();
    private String chosenUserName;

    private User user;

    public void initialize(){
        goButton.setOnAction(e -> {
            buttonClick();
            handleGoButtonAction();
        });

        createProfileButton.setOnAction(e -> {
            buttonClick();
            handleCreateButtonAction();
        });

        exitButton.setOnAction(e -> {
            buttonClick();
            handleExitButtonAction();
        });

        deleteButton.setOnAction(e -> {
            buttonClick();
            handleDeleteButtonAction();
        });

        loadData();
    }

    private void loadData(){
        userListView.getItems().clear();
        observableList.removeAll(observableList);
        List<User> userList = UserRepository.getAll();
        for (int i = 0; i < userList.size(); i++) {
            observableList.add(userList.get(i).getUsername());
        }
        userListView.getItems().addAll(observableList);
    }

    private void handleGoButtonAction(){
       if (userListView.getSelectionModel().getSelectedItem() != null) {
           chosenUserName = (String) userListView.getSelectionModel().getSelectedItem();
           this.user = UserRepository.get("./src/resources/users/" + chosenUserName + ".ser");
           loadMainMenu(user);
       }
    }

    private void handleCreateButtonAction(){
        loadFXMLScene("/scenes/NewUser.fxml", "Create User");
    }

    private void handleExitButtonAction(){
        Platform.exit();
        System.exit(0);
    }

    private void handleDeleteButtonAction() {
        if (userListView.getSelectionModel().getSelectedItem() != null) {
            chosenUserName = (String) userListView.getSelectionModel().getSelectedItem();
            UserRepository.delete(chosenUserName + ".ser");

            String baseSavedFileDir =
                    "./resources/saved-files/" + user.getUsername() + "-level-save";

            for (int i = 1; i <=3; i++) {
                LevelSaver.delete(baseSavedFileDir + i + ".txt");
            }

            loadData();
        }
    }

}
