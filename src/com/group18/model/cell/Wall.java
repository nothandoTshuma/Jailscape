package com.group18.model.cell;

import com.group18.exception.InvalidMoveException;
import com.group18.model.Level;
import com.group18.model.entity.Enemy;
import com.group18.model.entity.User;

import java.awt.*;

/**
 * Represents a Wall cell in the game. No users or enemies can be placed on this cell.
 * @author danielturato
 */
public class Wall extends Cell {

    /**
     * Creates a basic Wall cell, calling Super on Cell's constructor
     */
    public Wall() {
        super();
    }

    /**
     * Creates a new Wall Cell
     * @param level The level the cell is associated with
     * @param coordinates The coordinate of this cell in relation to others for this level.
     */
    public Wall(Level level, Point coordinates)  {
        super(level, coordinates);
    }

    /**
     * If you try and place a user on this cell, an error will always been thrown
     * @param user The user to be placed
     * @throws InvalidMoveException Thrown always
     */
    @Override
    public void placePlayer(User user) throws InvalidMoveException {
        throw new InvalidMoveException("You can not place a user on a Wall cell");
    }

    /**
     * If you try and place a enemy on this cell, an error will always been thrown
     * @param enemy The enemy to be placed
     * @throws InvalidMoveException Thrown always
     */
    @Override
    public void placeEnemy(Enemy enemy) throws InvalidMoveException {
        throw new InvalidMoveException("You can not place an enemy on a Wall cell");
    }

    /**
     * Check to see if this cell has a player
     * @return False always, as no player can move to this cell.
     */
    @Override
    public boolean hasPlayer() {
        return false;
    }

    /**
     * Check to see if this cell has a enemy
     * @return False always, as no enemy can move to this cell.
     */
    @Override
    public boolean hasEnemy() {
        return false;
    }

    /**
     * Check to see if this cell has a player and a enemy
     * @return False always, as no player or enemy can move to this cell.
     */
    @Override
    public boolean hasPlayerAndEnemy() {
        return false;
    }
}
