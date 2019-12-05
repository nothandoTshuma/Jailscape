package com.group18.model.cell;

import com.group18.controller.GameController;
import com.group18.exception.InvalidMoveException;
import com.group18.model.Actionable;
import com.group18.model.item.Collectable;
import com.group18.model.Level;
import com.group18.model.entity.Enemy;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;
import com.group18.model.item.Key;

import java.awt.*;
import java.util.List;

/**
 * Represents a ground cell in the game. Any enemy or player can move on to this cell.
 * @author danielturato
 */
public class Ground extends Cell implements Actionable {

    /**
     * An collectable item which can be stored on this cell.
     */
    private Collectable item;

    /**
     * Creates a basic Ground cell, not linking a level or coordinate
     */
    public Ground() {
        super();
    }

    /**
     * Set the item that's currently on the cell
     * @param item The new item
     */
    public void setItem(Collectable item) {
        this.item = item;
    }

    /**
     * Gets the item currently on this cell
     * @return The current item on this cell
     */
    public Collectable getItem() {
        return item;
    }

    /**
     * Creates a new Ground cell
     * @param coordinates It's coordinates in relation to all cells with this Level
     */
    public Ground(Point coordinates) {
        super(coordinates);
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
            throw new InvalidMoveException("You can not have two players on the same cell");
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
     * Checks if this cell has both a player and enemy on it
     * @return Boolean value suggesting if the cell has both a player and enemy on it.
     */
    @Override
    public boolean hasPlayerAndEnemy() {
        return hasPlayer() && hasEnemy();
    }

    /**
     * Check to see if this cell contains an item
     * @return Boolean value suggesting an item is on this cell
     */
    public boolean hasItem() {
        return this.item != null;
    }

    /**
     * If a user is on this cell, an action is toggled to pick up the current item on this cell
     * @param entity The entity the action is toggled upon
     */
    @Override
    public void toggleAction(Entity entity) {
        if (entity instanceof User && hasItem()) {
            if (this.getItem() instanceof Key) {
                if (((Key) this.getItem()) == Key.TOKEN_KEY) {
                    GameController.playSound("PickupCoin");
                    ((User) entity).addToken();
                } else {
                    GameController.playSound("PickupItem");
                    ((User) entity).addItem(this.item, getLevel().getCurrentLevel());
                }
            } else {
                GameController.playSound("PickupItem");
                ((User) entity).addItem(this.item, getLevel().getCurrentLevel());
            }
            setItem(null);
        }
    }
}
