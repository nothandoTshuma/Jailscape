package com.group18.core;

import com.group18.model.cell.Cell;

import java.util.ArrayList;

public class LevelLoader {

    public static final String LEVEL_DIR = "";

    public static void loadLevel(int level) {
        int levelHeight;
        int levelWidth;
        ArrayList<String> levelLine =  FileReader.getFileLines(LEVEL_DIR + "level" + level + ".txt");
        String[] tempList = levelLine.get(0).split(",");
        levelHeight = Integer.valueOf(tempList[0]);
        levelWidth = Integer.valueOf(tempList[1]);
    }
}
