package com.group18.controller;

import com.group18.core.DeleteUserFromFile;
import com.group18.core.FileReader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.ArrayList;


public class UserSelectionController extends MenuController {
    @FXML ListView<String> usersListView;
    @FXML Button goButton;
    @FXML Button createButton;
    @FXML Button deleteButton;
    @FXML Button exitButton;

    private ObservableList observableList = FXCollections.observableArrayList();
    private String chosenUserName;

    public void initialize(){
        goButton.setOnAction(e -> {
            handleGoButtonAction();
        });

        createButton.setOnAction(e -> {
            handleCreateButtonAction();
        });

        deleteButton.setOnAction(e -> {
            handleDeleteButtonAction();
        });

        exitButton.setOnAction(e -> {
            handleExitButtonAction();
        });
        loadData();
    }

    private void loadData(){
        observableList.removeAll(observableList);
        FileReader parser = new FileReader("./src/resources/UserNames.txt");
        ArrayList<String> list = parser.getArray();
        for (int i = 0; i < list.size(); i++) {
            observableList.add(list.get(i));
        }
        usersListView.getItems().addAll(observableList);
    }

    private void handleGoButtonAction(){
       if (usersListView.getSelectionModel().getSelectedItem() != null) {
           chosenUserName = usersListView.getSelectionModel().getSelectedItem();
           loadFXMLScene("/resources/MainMenu.fxml", "Main Menu");
       }
    }

    private void handleCreateButtonAction(){
        loadFXMLScene("/resources/CreateUserMenu.fxml", "Create User");
    }

    private void handleDeleteButtonAction(){
        if (usersListView.getSelectionModel().getSelectedItem() != null) {
            chosenUserName = usersListView.getSelectionModel().getSelectedItem();
            DeleteUserFromFile deleteUserFromFile = new DeleteUserFromFile(chosenUserName);
            usersListView.getItems().clear();
            loadData();
        }
    }

    private void handleExitButtonAction(){
        Platform.exit();
        System.exit(0);
    }

}
