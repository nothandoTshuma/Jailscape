package com.group18.core;

import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AddUserName {
    private String userName;
    private ArrayList<String> userNameList = new ArrayList<>();
    private final String FIRST_LEVEL = ",1";

    public AddUserName(String userName) {
        getUserNames();
        this.userName = userName;
    }

    private void getUserNames(){
        File file = new File("./src/resources/UserNames.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()){
                String[] userNameSplitList = scanner.nextLine().split(",");
                userNameList.add(userNameSplitList[0]);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserNameExists(){
        Boolean exists = false;
        for (int i = 0; i< userNameList.size(); i++) {
            if (userNameList.get(i).equals(userName)) {
                exists = true;
            }
        }
        return exists;
    }

    public void writeUserName() {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter("./src/resources/UserNames.txt", true));
            output.append(userName + FIRST_LEVEL + "\n");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
