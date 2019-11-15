package com.group18.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelParser {
    private ArrayList<String> level = new ArrayList<>();

    public LevelParser(String filename) {
        scanFile(filename);
    }

    private void scanFile(String filename) {
        try(Scanner scanner = new Scanner(new File(filename));){
            while(scanner.hasNext()){
                level.add(scanner.next());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }

    public ArrayList<String> getLevel() {
        return level;
    }
}
