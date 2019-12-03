package com.group18.controller;

import com.group18.core.DeleteUserFromFile;
import com.group18.core.FileReader;
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
    @FXML ListView userListView;


    private ObservableList observableList = FXCollections.observableArrayList();
    private String chosenUserName;

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
        loadData();
    }

    private void loadData(){
        observableList.removeAll(observableList);
        ArrayList<String> list = getUserNames(FileReader.getFileLines("./src/resources/UserNames.txt"));
        for (int i = 0; i < list.size(); i++) {
            observableList.add(list.get(i));
        }
        userListView.getItems().addAll(observableList);
    }

    private void handleGoButtonAction(){
       if (userListView.getSelectionModel().getSelectedItem() != null) {
           chosenUserName = (String) userListView.getSelectionModel().getSelectedItem();
           loadFXMLScene("/resources/MainMenu.fxml", "Main Menu");
       }
    }

    private void handleCreateButtonAction(){
        loadFXMLScene("/resources/CreateUserMenu.fxml", "Create User");
    }

    private void handleExitButtonAction(){
        Platform.exit();
        System.exit(0);
    }

    private ArrayList<String> getUserNames(ArrayList<String> list) {
        ArrayList<String> userNameList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String[] tempList = list.get(i).split(",");
            userNameList.add(tempList[0]);
        }
        return userNameList;
    }
}
