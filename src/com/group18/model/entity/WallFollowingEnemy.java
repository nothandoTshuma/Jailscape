package com.group18.model.entity;

import com.group18.model.Direction;
import com.group18.model.Level;

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
        List<Direction> validDirections =
                Arrays.stream(Direction.values())
                        .filter(direction -> level.validMove(this, direction))
                        .filter(direction -> direction != IDLE)
                        .collect(Collectors.toList());

        // If there were no valid directions, this enemy is stuck
        if (validDirections.size() == 0) {
            return IDLE;
        }

        // If this enemy is not stuck, choose a random valid direction for them to move in
        Random random = new Random();
        int randomDirectionIdx = random.nextInt(validDirections.size());

        return validDirections.get(randomDirectionIdx);
    }
}
