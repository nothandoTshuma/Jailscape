package com.group18.core;

import com.group18.model.entity.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * The UserRepository holding all player level completion data.
 * Here we can save new users, retrieve users and delete users
 */
public class UserRepository implements Serializable {

    private static final String USER_DIRECTORY = "";

    public static List<User> loadUsers() {
        File directory = new File(USER_DIRECTORY);
        File[] directoryFiles = directory.listFiles();
        List<User> users = new ArrayList<>();

        if (directoryFiles != null) {
            for (File file : directoryFiles) {
                String fileName = file.getName();
                User user = readUser(fileName);

                if (user != null) {
                    users.add(user);
                }
            }
        }

        return users;
    }

    /**
     * Deserialize a User file
     * @param fileName The filename that stores the User object
     * @return A user object
     */
    public static User readUser(String fileName) {
        User user = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream outputStream = new ObjectInputStream(fileInputStream);

            user = (User) outputStream.readObject();

            fileInputStream.close();
            outputStream.close();

        } catch (IOException ex) {
            //TODO - handle error
        } catch (ClassNotFoundException ex) {
            //TODO - handle error
        }

        return user;
    }

    public static void deleteUser(String fileName) {
//        Path path
//        try {
//            Files.deleteIfExists(USER_DIRECTORY + "/" + fileName);
//        }
    }

}
