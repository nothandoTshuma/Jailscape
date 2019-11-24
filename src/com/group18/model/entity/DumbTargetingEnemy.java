package com.group18.model.entity;

import com.group18.model.Direction;
import com.group18.model.Level;

import java.awt.*;
import java.util.Random;

import static com.group18.model.Direction.*;

/**
 * An enemy which will move in the direction that gets the closest to the User,
 * regardless if this makes them idle
 * @author danielturato
 */
public class DumbTargetingEnemy extends Enemy {

    /**
     * Calculate this enemy's next direction, based on the shortest path to the User
     * regardless if their direction will make themselves idle.
     * @param user The current user playing on the level
     * @param level The level this enemy is associated with
     * @return Their next direction
     */
    @Override
    public Direction getNextDirection(User user, Level level) {
        Point currentPosition = this.getCurrentCell().getPosition();
        Point userPosition = user.getCurrentCell().getPosition();

        // Get the difference between the Users (x,y) coordinates to the this enemy
        int xDistance = (int) (userPosition.getX() - currentPosition.getX());
        int yDistance = (int) (userPosition.getY() - currentPosition.getY());

        // Calculate the absolute difference to find which is the smallest difference
        int absXDistance = Math.abs(xDistance);
        int absYDistance = Math.abs(yDistance);

        // Here, this enemy must be on the Users cell
        if (absXDistance == 0 && absYDistance == 0) {
            return IDLE;
        }

        // Here, it does not matter which direction we move in as both X & Y distances are equal
        // Therefore, we choose a random axis to move along
        if (absXDistance == absYDistance) {
            return calculateRandomDirection(xDistance, yDistance, level);
        }

        // If we are on the same X axis as the User but different Y axis,
        // therefore we only want to move on the Y axis
        if (absXDistance == 0) {
            return calculateVerticalDirection(yDistance, level);
        }

        // If we are on the same Y axis as the User but different X axis,
        // therefore we only want to move on the X axis
        if (absYDistance == 0) {
            return calculateHorizontalDirection(xDistance, level);
        }

        // Here, we must be on a completely different X & Y axis to the User.
        // Therefore, we check which axis has the shortest distance to the User
        // Then return a suitable direction, moving along that axis.
        if (absXDistance < absYDistance) {

            return calculateHorizontalDirection(xDistance, level);
        }

        return calculateVerticalDirection(yDistance, level);
    }


    /**
     * Calculate the vertical direction that is needed based on an Y axis value
     * @param distance The Y value distance
     * @param level The level in which this enemy is associated with
     * @return A suitable direction moving along the vertical plane
     */
    private Direction calculateVerticalDirection(int distance, Level level) {
        if (distance < 0) {
            return level.validMove(this, UP) ? UP : IDLE;
        }

        return level.validMove(this, DOWN) ? DOWN : IDLE;
    }

    /**
     * Calculate the horizontal direction that is needed based on a X axis value
     * @param distance The X value distance
     * @param level The level in which this enemy is associated with
     * @return A suitable direction moving along the horizontal plane
     */
    private Direction calculateHorizontalDirection(int distance, Level level) {
        if (distance < 0) {
            return level.validMove(this, LEFT) ? LEFT : IDLE;
        }

        return level.validMove(this, RIGHT) ? RIGHT : IDLE;
    }

    /**
     * Choose a direction based on a random axis to move to along. Assuming both distances are equal
     * @param xDistance The distance on the X axis to the user
     * @param yDistance The distance on the Y axis to the user
     * @param level The level in which this enemy is associated with
     * @return A suitable direction moving along a random axis
     */
    private Direction calculateRandomDirection(int xDistance, int yDistance, Level level) {
        Random random = new Random();
        int axis = random.nextInt(2);

        // If random value was 0 move on the X axis. Otherwise move on the Y axis.
        return axis == 0 ? calculateHorizontalDirection(xDistance, level) :
                           calculateVerticalDirection(yDistance, level);
    }
}






