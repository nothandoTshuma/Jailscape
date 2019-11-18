package com.group18.model.cell;

import com.group18.exception.InvalidMoveException;
import com.group18.model.Coordinate;
import com.group18.model.Level;
import com.group18.model.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to represent an individual cell on a board of cells. Any cell type will inherit from this class.
 * @author danielturato
 */
public abstract class Cell implements Cloneable {

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
    private List<Entity> currentEntities;


    /**
     * Used to setup basic fields that each Cell needs.
     */
    protected Cell() {
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
     * Set all the current entities on the cell
     * @param currentEntities The new entities
     */
    public void setCurrentEntities(List<Entity> currentEntities) {
        this.currentEntities = currentEntities;
    }

    public void placePlayer() throws InvalidMoveException {
        //TODO:drt - User class needs implementation
    }

    public void placeEnemy() throws InvalidMoveException {
        //TODO:drt - Enemy class needs implementation
    }

    public boolean hasPlayer() {
        //TODO:drt - User class needs implementation
        return false;
    }

    public boolean hasEnemy() {
        //TODO:drt - Enemy class needs implementation
        return false;
    }

    public boolean hasPlayerAndEnemy() {
        //TODO:drt - User & Enemy classes need implementation
        return false;
    }
}
