package com.group18.model.cell;

import com.group18.exception.InvalidMoveException;
import com.group18.model.Coordinate;
import com.group18.model.Level;
import com.group18.model.entity.Enemy;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to represent an individual cell on a board of cells. Any cell type will inherit from this class.
 * @author danielturato
 */
public abstract class Cell {

    /**
     * Each cell is assigned a Level and can only have 1 level at one time.
     */
    private Level level;

    /**
     * The cell's current position (x,y) in relation to all other cells on the board
     */
    private Coordinate coordinate;

    /**
     * All the current entities on this cell
     */
    List<Entity> currentEntities;


    /**
     * Used to setup basic fields that each Cell needs.
     */
    Cell() {
        this.currentEntities = new ArrayList<>();
    }

    /**
     * Get the level the cell is assigned to
     * @return The level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Set the new level the cell will be assigned to
     * @param level The new level
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * Get the cells (x,y) position coordinates
     * @return The cell's current position
     */
    public Coordinate getPosition() {
        return coordinate;
    }

    /**
     * Set the cells current coordinate
     * @param coordinate The new position
     */
    public void setPosition(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Get all the current entities on the cell
     * @return The current entities
     */
    public List<Entity> getCurrentEntities() {
        return currentEntities;
    }

    /**
     * Used to place a user on to this cell. Subclasses will provide their implementation.
     * @param user The user to be placed
     * @throws InvalidMoveException Possible exception if it's not a valid placement
     */
    public abstract void placePlayer(User user) throws InvalidMoveException;

    /**
     * Used to place a enemy on to this cell. Subclasses will provide their implementation.
     * @param enemy The enemy to be placed
     * @throws InvalidMoveException Possible exception if it's not a valid placement.
     */
    public abstract void placeEnemy(Enemy enemy) throws InvalidMoveException;

    /**
     * Used to check if this cell has a player on itself.
     * @return Boolean value representing if the player is on this cell.
     */
    public abstract boolean hasPlayer();

    /**
     * Used to check if this cell has a enemy on itself
     * @return Boolean value representing if the player is on this cell
     */
    public abstract boolean hasEnemy();

    /**
     * Used to check if this cell has both an enemy and a player on itself
     * @return Boolean value representing if a player & enemy is on this cell.
     */
    public abstract boolean hasPlayerAndEnemy();

    /**
     * Checks if this cell has an entity of a certain type
     * @param e The entity type check
     * @return Boolean value representing if the cell has a certain class type
     */
     boolean hasEntity(Class<? extends Entity> e) {
        for (Entity entity : this.currentEntities) {
            if (entity.getClass().isInstance(e)) {
                return true;
            }
        }

        return false;
    }
}
