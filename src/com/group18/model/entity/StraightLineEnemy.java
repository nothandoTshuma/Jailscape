package com.group18.model.entity;

import com.group18.model.Direction;
import com.group18.model.Level;
import javafx.geometry.Orientation;

import static com.group18.model.Direction.*;

/**
 * This models an Enemy who can only move in a straight line at a particular orientation.
 * @author danielturato
 */
public class StraightLineEnemy extends Enemy {

    /**
     * The orientation in which this enemy moves in
     */
    private Orientation orientation;

    /**
     * Creates a new StraightLineEnemy
     * @param orientation The orientation in which this enemy moves in
     */
    public StraightLineEnemy(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Attempts to calculate their next direction based on their orientation
     * @param user The current user playing on the level
     * @param level The level this enemy is associated with
     * @return Their next direction
     */
    @Override
    public Direction getNextDirection(User user, Level level) {
        Direction currentDirection = this.getDirection();

        // If their direction is IDLE, then based on a previous calculation they must be stuck.
        if (currentDirection == IDLE) {
            return IDLE;
        }

        // If the direction they last moved in was valid, attempt to keep moving in that direction
        if (level.validMove(this, currentDirection)) {
            return currentDirection;
        } else {
            // Try and switch direction if they can't move in their previous direction
            Direction newDirection = switchDirection();
            if (level.validMove(this, newDirection)) {
                return newDirection;
            }

            // If they can't move, then they must be stuck and remain IDLE.
            return IDLE;
        }
    }

    /**
     * Set the orientation of this Enemy
     * @param direction The direction it is currently moving in
     */
    public void setOrientation(Direction direction) {
        if (direction == LEFT || direction == RIGHT) {
            this.orientation = Orientation.HORIZONTAL;
        } else if (direction == UP || direction == DOWN) {
            this.orientation = Orientation.VERTICAL;
        } else {
            this.orientation = Orientation.HORIZONTAL;
        }
    }

    /**
     * Get the orientation of this Enemy
     * @return The orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Switch their current direction, based on their orientation
     * @return Their possible new direction
     */
    private Direction switchDirection() {
        Direction currentDirection = this.getDirection();

        // If their direction is IDLE, there will be no switch so they remain idle
        if (currentDirection == IDLE) {
            return IDLE;
        }

        if (this.orientation == Orientation.HORIZONTAL) {
            return currentDirection == LEFT ? RIGHT : LEFT;
        }

        return currentDirection == UP ? DOWN : UP;
    }
}
