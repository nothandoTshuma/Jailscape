package com.group18.core;

import com.group18.controller.GameController;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class CreateLevelLayout {

    private static Image groundImage = new Image("http://www.citem.gov.ph/images/red-box/red-box-logo.jpg");
    private static Image wallImage = new Image("https://www.yourlocalplumber.co.uk/wp-content/uploads/2016/12/greensquare.jpg");
    private static Image sle = new Image("http://sohme.com/wp-content/uploads/2015/07/blue.png");
    private static ArrayList<String> level;
    private static int levelWidth;
    private static int levelHeight;


    public static Pane createLevel(String file) {
        Pane layout = new Pane();
        level = FileReader.getFileLines(file);
        String[] tempList = level.get(0).split(",");
        levelWidth = Integer.parseInt(tempList[0]);
        levelHeight = Integer.parseInt(tempList[1]);
        GameController.setLevelSize(levelWidth, levelHeight);
        layout.getChildren().add(addCells());
        layout.getChildren().add(addEntitys());
        layout.setMinSize(levelWidth * 64, levelHeight * 64);
        layout.setPrefSize(levelWidth * 64, levelHeight * 64);
        layout.setMaxSize(levelWidth * 64, levelHeight * 64);
        return layout;
    }

    private static Group addCells() {
        Group g = new Group();
        int k = 1;

        for (int i = 0; i < levelWidth; i++) {
            for (int j = 0; j < levelHeight; j++) {
                String[] tempCellList = level.get(k).split(",");
                k++;
                switch(tempCellList[0]) {
                    case "WC":
                        g.getChildren().add(createImage(wallImage, j, i));
                        break;
                    case "GC":
                        g.getChildren().add(createImage(groundImage, j, i));
                        break;
                    default:
                }
            }
        }
        return g;
    }

    private static Group addEntitys() {
        Group g = new Group();
        int k = 1;

        for (int i = 0; i < levelWidth; i++) {
            for (int j = 0; j < levelHeight; j++) {
                String[] tempCellList = level.get(k).split(",");
                k++;
                switch(tempCellList[1]) {
                    case "U":
                        GameController.setPlayerPos(j, i);
                        break;
                    case "SLE":
                        g.getChildren().add(createImage(sle, j, i));
                        break;
                    default:
                }
            }
        }
        return g;
    }

    private static ImageView createImage(Image img, int j, int i) {
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(64);
        imgView.setFitWidth(64);
        imgView.setX(j * 64);
        imgView.setY(i * 64);
        return imgView;
    }
}
