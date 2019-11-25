package com.group18.core;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {
    private ArrayList<String> arrayList = new ArrayList<>();

    public FileReader(String filename) {
        scanFile(filename);
    }

    private void scanFile(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                String[] userNameSplitList = scanner.nextLine().split(",");
                arrayList.add(userNameSplitList[0]);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Can't find file: " + filename);
        }
    }

    public ArrayList<String> getArray() {
        return arrayList;
    }
}