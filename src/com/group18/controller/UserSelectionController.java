package com.group18.controller;

import com.group18.core.DeleteUserFromFile;
import com.group18.core.FileReader;
import com.group18.core.UserRepository;
import com.group18.model.entity.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;


public class UserSelectionController extends MenuController {


    @FXML Button exitButton;
    @FXML Button goButton;
    @FXML Button createProfileButton;
    @FXML Button deleteButton;
    @FXML ListView userListView;


    private ObservableList observableList = FXCollections.observableArrayList();
    private String chosenUserName;
    public static User user;

    public void initialize(){
        goButton.setOnAction(e -> {
            handleGoButtonAction();
        });

        createProfileButton.setOnAction(e -> {
            handleCreateButtonAction();
        });

        exitButton.setOnAction(e -> {
            handleExitButtonAction();
        });

        deleteButton.setOnAction(e -> {
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
           user = UserRepository.get("./src/resources/users/" + chosenUserName + ".ser");
           loadFXMLScene("/resources/MainMenu.fxml", "Main Menu");
       }
    }

    private void handleCreateButtonAction(){
        loadFXMLScene("/resources/NewUser.fxml", "Create User");
    }

    private void handleExitButtonAction(){
        Platform.exit();
        System.exit(0);
    }

    private void handleDeleteButtonAction() {
        if (userListView.getSelectionModel().getSelectedItem() != null) {
            chosenUserName = (String) userListView.getSelectionModel().getSelectedItem();
            UserRepository.delete(chosenUserName + ".ser");
            loadData();
        }
    }
}
