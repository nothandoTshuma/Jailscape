package com.group18.model.cell;

import com.group18.exception.InvalidMoveException;
import com.group18.model.Coordinate;
import com.group18.model.Level;
import com.group18.model.entity.Enemy;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;

import java.util.List;

/**
 * Represents a ground cell in the game. Any enemy or player can move on to this cell.
 * @author danielturato
 */
public class Ground extends Cell {

    //TODO:drt - Allow items to be stored on this cell

    /**
     * Creates a basic Ground cell, not linking a level or coordinate
     */
    public Ground() {
        super();
    }

    /**
     * Creates a new Ground cell
     * @param level The level this cell is associated with
     * @param coordinate It's coordinate in relation to all cells with this Level
     */
    public Ground(Level level, Coordinate coordinate) {
        super(level, coordinate);
    }

    /**
     * Set all the current entities on the cell
     * @param currentEntities The new entities
     */
    public void setCurrentEntities(List<Entity> currentEntities) {
        this.currentEntities = currentEntities;
    }


    /**
     * Places the user on to this cell
     * @param user The user to be placed
     * @throws InvalidMoveException Throws this exception if there is a user already on this cell
     */
    @Override
    public void placePlayer(User user) throws InvalidMoveException {
        if (hasPlayer()) {
            throw new InvalidMoveException();
        }

        this.currentEntities.add(user);
    }

    /**
     * Places the enemy on to this cell
     * @param enemy The enemy to be placed
     * @throws InvalidMoveException Potential for this exception to be thrown.
     */
    @Override
    public void placeEnemy(Enemy enemy) throws InvalidMoveException {
        this.currentEntities.add(enemy);
    }

    /**
     * Checks if this cell has a player on it
     * @return Boolean value suggesting if the cell has a player on it
     */
    @Override
    public boolean hasPlayer() {
        return hasEntity(User.class);
    }

    /**
     * Checks if this cell has a enemy on it
     * @return Boolean value suggesting if the cell has a enemy on it
     */
    @Override
    public boolean hasEnemy() {
        return hasEntity(Enemy.class);
    }

    /**
     * Checks if this cell has both a player & enemy on it
     * @return Boolean value suggesting if the cell has both a player & enemy on it.
     */
    @Override
    public boolean hasPlayerAndEnemy() {
        return hasPlayer() && hasEnemy();
    }
}
