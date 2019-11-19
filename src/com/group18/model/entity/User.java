package com.group18.model.entity;

import com.group18.model.Collectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Designs the behaviours of User.
 * @author nothandotshuma
 */
public class User extends Entity {
    /**
     * Stores the number of tokens the user gets.
     */
    private int tokens;
    /**
     * Stores the highest level achieved by the user.
     */
    private int highestLevel;
    /**
     * Stores the name of the user.
     */
    private final String username;
    /**
     * Instantiates an arraylist to store the user's inventory.
     */
    private final ArrayList<Collectable> inventory = new ArrayList<>();
    /**
     * Instantiates a hashmap to store the top 3 quickest times the user has played.
     */
    private final Map<Integer, Long[]> quickestTimes = new HashMap<>();

    /**
     * Constructor to create a new user.
     * @param username
     */
    public User(String username) {
        this.username = username;
        this.highestLevel = 1;
    }

    /**
     * Gets the tokens the user has earned.
     * @return tokens
     */
    public int getTokens() {
        return tokens;
    }

    /**
     * Increments the number of tokens the user earns, each time they earn any.
     */
    public void addToken() {
        tokens++;
    }

    /**
     * Returns a list of the inventory the user has earned.
     * @return inventory
     */
    public ArrayList<Collectable> getInventory() {
        return inventory;
    }

    /**
     * Adds to the inventory, each time a user earns a collectable item.
     * @param item
     */
    public void addItem(Collectable item) {
        inventory.add(item);
    }

    /**
     *
     * @param Colour
     * @return
     */
    public boolean hasKey(colour Colour) {
        //TODO add the rest of the code here..
        return false;
    }

    /**
     *
     * @param Colour
     */
    public void consumeKey(colour Colour) {
        //TODO add the rest of the code here..
    }

    /**
     * Checks if an item is of type Collectable.
     * @param i
     * @return boolean value depending on whether it is collectable or not.
     */
    public boolean hasItem(Class<? extends Collectable> i) {
        for (Collectable item : this.inventory) {
            if (i.isInstance(item)) {
                return true;
            }
        }
       return false;
    }

    /**
     * Adds the new top 3 quickest times to the hashmap.
     * @param time
     * @param level
     */
    public void addQuickestTime(Long time, int level) {
        Long[] levelArray = getQuickestTimesFor(level);
        if (levelArray[2] > time){
            levelArray[2] = time;
        } else if (levelArray[1] > time) {
            levelArray[1] = time;
        } else if (levelArray[0] > time) {
            levelArray[0] = time;
        }
    }

    /**
     * Returns the user's quickest times.
     * @return quickestTimes
     */
    public Map <Integer,Long[]> getAllQuickestTimes() {
        return quickestTimes;
    }

    /**
     * Gets the top 3 quickest quickest times for a level.
     * @param level
     * @return quickestTimes
     */
    public Long[] getQuickestTimesFor(int level) {
        return this.quickestTimes.get(level);
    }

    /**
     * Returns the username of the user.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Increments the level each time the user passes a stage.
     */
    public void incrementLevel() {
        highestLevel++;
    }

    /**
     * Returns the highest level reached by the user.
     * @return highestLevel
     */
    public int getHighestLevel() {
        return highestLevel;
    }
}
