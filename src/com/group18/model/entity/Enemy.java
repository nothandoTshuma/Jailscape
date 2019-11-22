package com.group18.model.entity;

import com.group18.model.Direction;

/**
 * Designs the behaviours for the Enemy.
 * @author nothandotshuma
 */
public abstract class Enemy extends Entity {

    /**
     * Abstract method to work out which direction the enemy should take.
     * @param user
     * @param board
     * @return Direction
     */
    public abstract Direction getNextDirection(User user, Board board);

}
