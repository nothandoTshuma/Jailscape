package com.group18.model.entity;

import com.group18.model.Direction;
import com.group18.model.Level;

/**
 * Designs the behaviours for the Enemy.
 * @author nothandotshuma
 */
public abstract class Enemy extends Entity {

    /**
     * Abstract method to work out which direction the enemy should take.
     * @param user The current user playing on the level
     * @param level The level this enemy is associated with
     * @return Direction The direction they wish to move
     */
    public abstract Direction getNextDirection(User user, Level level);

}
