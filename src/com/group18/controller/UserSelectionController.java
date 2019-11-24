package com.group18.controller;
import com.group18.Main;

import com.group18.core.DeleteUserFromFile;
import com.group18.core.LevelParser;
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
import java.util.ArrayList;


public class UserSelectionController {
    @FXML ListView<String> usersListView;
    @FXML Button goButton;
    @FXML Button createButton;
    @FXML Button deleteButton;
    @FXML Button exitButton;

    ObservableList observableList = FXCollections.observableArrayList();

    private String choosenUserName;

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
        LevelParser parser = new LevelParser("./src/resources/UserNames.txt");
        ArrayList<String> list = parser.getLevel();
        for (int i = 0; i < list.size(); i++) {
            observableList.add(list.get(i));
        }
        usersListView.getItems().addAll(observableList);
    }

    private void handleGoButtonAction(){
       if (usersListView.getSelectionModel().getSelectedItem() != null) {
           choosenUserName = usersListView.getSelectionModel().getSelectedItem();
           try {
               FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/MainMenu.fxml"));
               BorderPane editRoot = fxmlLoader.load();
               Scene editScene = new Scene(editRoot, 600, 400);
               Stage editStage = Main.getPrimaryStage();
               editStage.setScene(editScene);
               editStage.setTitle("Main Menu");
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }

    private void handleCreateButtonAction(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/CreateUserMenu.fxml"));
            BorderPane editRoot = fxmlLoader.load();
            Scene editScene = new Scene(editRoot, 600, 400);
            Stage editStage = Main.getPrimaryStage();
            editStage.setScene(editScene);
            editStage.setTitle("Create User");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteButtonAction(){
        if (usersListView.getSelectionModel().getSelectedItem() != null) {
            choosenUserName = usersListView.getSelectionModel().getSelectedItem();
            DeleteUserFromFile deleteUserFromFile = new DeleteUserFromFile(choosenUserName);
            usersListView.getItems().clear();
            loadData();
        }
    }

    private void handleExitButtonAction(){
        Platform.exit();
        System.exit(0);
    }

    public String getChoosenUserName(){
        return choosenUserName;
    }
}
