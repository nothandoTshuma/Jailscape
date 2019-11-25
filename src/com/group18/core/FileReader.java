package com.group18.core;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    public static ArrayList<String> getFileLines(String filename) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNext()) {
                arrayList.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Can't find file: " + filename);
        }
        return arrayList;
    }

}