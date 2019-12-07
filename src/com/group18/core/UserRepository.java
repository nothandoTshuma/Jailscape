package com.group18.core;

import com.group18.model.entity.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

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
    private static final String USER_DIRECTORY = "./src/resources/users";

    /**
     * The logger which will allows us to output errors in a nice format
     */
    private static final Logger LOGGER = Logger.getLogger("UserRepository");

    /**
     * Load all possible user profiles that have been saved
     * @return A list of user profiles
     */
    public static List<User> getAll() {
        File directory = new File(USER_DIRECTORY);
        File[] directoryFiles = directory.listFiles();
        List<User> users = new ArrayList<>();
        if (directoryFiles != null) {
            for (File file : directoryFiles) {
                String fileName = file.getName();
                User user = get(USER_DIRECTORY + "/" + fileName);

                if (user != null) {
                    users.add(user);
                }
            }
        }

        return users;
    }

    /**
     * Checks if there's already a user with a particular username
     * @param username The user we're checking on
     * @return Boolean suggests that the user exists
     */
    public static boolean userExists(String username) {
        File directory = new File(USER_DIRECTORY);
        File[] directoryFiles = directory.listFiles();
        if (directoryFiles != null) {
            for (File file : directoryFiles) {
                if (file.getName().equals(username + ".ser")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Serialize a User object
     * @param user The user to be serialized.
     */
    public static void save(User user) {
        String fileName = USER_DIRECTORY + "/" + user.getUsername() + ".ser";
        delete("fileName");

        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream outputStream = new ObjectOutputStream(file);

            outputStream.writeObject(user);

            outputStream.close();
            file.close();
        } catch (IOException ex) {
            LOGGER.log(WARNING, "The user is trying to create a User that already exists", ex);
        }
    }

    /**
     * Deserialize a User file
     * @param fileName The filename that stores the User object
     * @return A user object
     */
    public static User get(String fileName) {
        User user = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream outputStream = new ObjectInputStream(fileInputStream);

            user = (User) outputStream.readObject();

            fileInputStream.close();
            outputStream.close();

        } catch (IOException ex) {
            LOGGER.log(WARNING, String.format("The file %s does not exist!", ex));
        } catch (ClassNotFoundException ex) {
            LOGGER.log(WARNING, "There is no User object", ex);
        }

        return user;
    }

    /**
     * Deletes a user, deleting the serialised file
     * @param fileName The filename holding the user data
     */
    public static void delete(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(USER_DIRECTORY + "/" + fileName));
        } catch (IOException ex) {
            LOGGER.log(WARNING, "This user has now been deleted", ex);
        }
    }

}