package com.group18.core;

import com.group18.model.entity.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * The UserRepository holding all player level completion data.
 * Here we can save new users, retrieve users and delete users
 *
 * @author danielturato
 */
public class UserRepository {

    /**
     * The directory in which user data will be stored
     */
    private static final String USER_DIRECTORY = "";

    /**
     * Load all possible user profiles that have been saved
     * @return A list of user profiles
     */
    public static List<User> loadAllUsers() {
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
     * Serialize a User object
     * @param user The user to be serialized.
     */
    public static void saveUser(User user) {
        String fileName = USER_DIRECTORY + "/" + user.getUsername() + ".ser";

        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream outputStream = new ObjectOutputStream(file);

            // Method for serialization of object
            outputStream.writeObject(user);

            outputStream.close();
            file.close();
        } catch (IOException ex) {
            // TODO:drt - File already exists
        }
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

    /**
     * Deletes a user, deleting the serialised file
     * @param fileName The filename holding the user data
     */
    public static void deleteUser(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(USER_DIRECTORY + "/" + fileName));
        } catch (IOException ex) {
            // TODO:drt - handle error
        }
    }

}
