package com.group18.core;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AddUserName {
    private String userName;
    private ArrayList<String> userNameList = new ArrayList<>();

    public AddUserName(String userName) {
        getUserNames();
        this.userName = userName;
    }

    private void getUserNames(){
        File file = new File("C:\\Users\\fraser\\IdeaProjects\\Jailscape\\src\\resources\\UserNames.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()){
                userNameList.add(scanner.nextLine());
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

    public void writeUserName(String userName) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter("C:\\Users\\fraser\\IdeaProjects\\Jailscape\\src\\resources\\UserNames.txt", true));
            output.append(userName);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
