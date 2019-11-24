package com.group18.model.entity;

import com.group18.model.Direction;
import com.group18.model.Level;
import com.group18.model.cell.Cell;
import com.group18.model.cell.Wall;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.group18.model.Direction.*;

/**
 * An enemy that randomly follows the path in front of them
 * @author danielturato
 */
public class WallFollowingEnemy extends Enemy {

    /**
     * Calculates their next movement direction, based on surrounding cells in the level.
     * @param user The current user playing on the level
     * @param level The level this enemy is associated with
     * @return Their next direction to move in
     */
    @Override
    public Direction getNextDirection(User user, Level level) {
        // Collect all valid possible directions this enemy can move in
        List<Direction> validDirections = getValidDirections(level);

        // Attempt to get a valid direction, which is only possible if there
        // were found to be 2 valid directions
        Direction possibleDirection = getValidDirection(validDirections);
        if (possibleDirection != null) {
            return possibleDirection;
        }

        // Here, the enemy must be at a crossroad, but we must eliminate those directions which
        // would take this enemy away from a wall.
        eliminateInvalidDirections(validDirections, level);

        // If valid directions still contain 4 or 3 possible valid directions
        // then choose a random direction different from their previous direction
        if (validDirections.size() >= 3) {
            validDirections.remove(reverseDirection(this.getDirection()));
            Random random = new Random();
            return validDirections.get(random.nextInt(validDirections.size()-1));
        }

        // However, if valid directions is now below 3 then we can re-calculate a valid position
        // from those left.
        return getValidDirection(validDirections);
    }

    /**
     * Get a valid direction out of a list of possible valid directions
     * @param validDirections Possible valid directions
     * @return A possible direction or null if no possible direction was found
     */
    private Direction getValidDirection(List<Direction> validDirections) {
        // If there were no valid directions, this enemy is stuck
        if (validDirections.size() == 0) {
            return IDLE;
        }

        //If there's only 1 valid direction, then they must be at a dead end and turn around
        if (validDirections.size() == 1) {
            return validDirections.get(0);
        }

        //If there's 2 valid directions, they could be in multiple different positions
        if (validDirections.size() == 2) {
            //A single path surrounded by walls
            // in which then they should just continue in their previous direction
            if (validDirections.containsAll(Arrays.asList(UP, DOWN)) ||
                    validDirections.containsAll(Arrays.asList(LEFT, RIGHT))) {

                return this.getDirection();
            }

            //A corner in which valid directions maybe (UP, RIGHT), (DOWN, RIGHT), (LEFT, DOWN), (LEFT, UP),
            //here the enemy should try and move in the direction that is not opposite their previous move
            Direction oppositeDirection = reverseDirection(this.getDirection());
            validDirections.remove(oppositeDirection);

            return validDirections.get(0);
        }

        return null;
    }

    /**
     * Eliminate directions which aren't deemed possible
     * @param validDirections A list of possible valid directions
     * @param level The level this enemy is currently on
     */
    private void eliminateInvalidDirections(List<Direction> validDirections, Level level) {
        Point currentPosition = this.getCurrentCell().getPosition();

        // Loop through each possible valid direction to move in
        for (Direction direction : validDirections) {
            int x = (int) currentPosition.getX();
            int y = (int) currentPosition.getY();

            switch (direction) {
                case LEFT:
                    x--;
                    break;
                case RIGHT:
                    x++;
                    break;
                case UP:
                    y--;
                    break;
                case DOWN:
                    y++;
                    break;
                default:
                    break;
            }

            //Get the adjacent cells from that possible new position (removing old position)
            List<Cell> adjacentCells = level.getAdjacentCells(new Point(x, y));
            adjacentCells.remove(this.getCurrentCell());

            // Now loop through the adjacent cells and check there's at least one wall adjacent to the
            // possible new position
            boolean isValidDirection = false;
            for (Cell cell : adjacentCells) {
                if (cell instanceof Wall) {
                    isValidDirection = true;
                }
            }

            // If the possible direction lands them in a cell with no adjacent wall cells,
            // then this direction is no longer valid.
            if (!(isValidDirection)) {
                validDirections.remove(direction);
            }
        }
    }

    /**
     * Generate a starter list of valid directions for this enemy to move in
     * @param level The level this enemy is on
     * @return A starter list of possible valid directions
     */
    private List<Direction> getValidDirections(Level level) {
        return Arrays.stream(Direction.values())
                .filter(direction -> level.validMove(this, direction))
                .filter(direction -> direction != IDLE)
                .collect(Collectors.toList());
    }


    /**
     * Reverse the direction of a particular direction
     * @param previousDirection The direction to be reversed
     * @return The reversed direction
     */
    private Direction reverseDirection(Direction previousDirection) {
        Direction newDirection;

        switch (previousDirection) {
            case UP:
                newDirection = DOWN;
                break;
            case DOWN:
                newDirection = UP;
                break;
            case LEFT:
                newDirection = RIGHT;
                break;
            case RIGHT:
                newDirection = LEFT;
                break;
            default:
                newDirection = IDLE;
                break;
        }

        return newDirection;
    }

}




