package com.group18.model.entity;

import com.group18.model.item.Collectable;
import com.group18.model.Colour;
import com.group18.model.item.ElementItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private final List<Collectable> inventory;
    /**
     * Instantiates a hashmap to store the top 3 quickest times the user has played.
     */
    private final Map<Integer, Long[]> quickestTimes;

    /**
     * Constructor to create a new user.
     * @param username The username the user wants
     */
    public User(String username) {
        this.username = username;
        this.inventory = new ArrayList<>();
        this.quickestTimes = new HashMap<>();
        this.highestLevel = 1;

        quickestTimes.put(highestLevel, new Long[3]);
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
    public List<Collectable> getInventory() {
        return inventory;
    }

    /**
     * Adds to the inventory, each time a user earns a collectable item.
     * @param item The item that the User wants to collect
     */
    public void addItem(Collectable item) {
        inventory.add(item);
    }

    /**
     *
     * @param colour The colour of key
     * @return Boolean value suggesting if this user has a key of a specific colour
     */
    public boolean hasKey(Colour colour) {
        //TODO add the rest of the code here..
        return false;
    }

    /**
     *
     * @param colour The colour of the key that needs to be consumed
     */
    public void consumeKey(Colour colour) {
        //TODO add the rest of the code here..
    }

    /**
     * Checks if an item is of type Collectable.
     * @param i The item, the user needs to check upon
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

    public boolean hasElementItem(ElementItem elementItem) {
        for (Collectable item : this.inventory) {
            if (item == elementItem) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds the new top 3 quickest times to the hashmap.
     * @param time The new time from the level
     * @param level The level the generated time came from
     */
    public void addQuickestTime(Long time, int level) {
        //TODO:dt What if the level is not in the map? Throw exception maybe?
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
    public Map <Integer, Long[]> getAllQuickestTimes() {
        return quickestTimes;
    }

    /**
     * Gets the top 3 quickest quickest times for a level.
     * @param level The level the user wants to get the quickest times for
     * @return quickestTimes
     */
    public Long[] getQuickestTimesFor(int level) {
        //TODO:dt What if the level is not in the map? Throw exception maybe?
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
        quickestTimes.put(++highestLevel, new Long[3]);
    }

    /**
     * Returns the highest level reached by the user.
     * @return highestLevel
     */
    public int getHighestLevel() {
        return highestLevel;
    }
}
