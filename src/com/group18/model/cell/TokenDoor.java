package com.group18.model.cell;

import com.group18.controller.GameController;
import com.group18.model.Actionable;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;

import java.awt.*;

/**
 * represents the door which requires tokens to pass through
 * @author RIYA GUPTA
 */
public class TokenDoor extends Wall implements Door, Actionable {
    /**
     * represents number of tokens
     */
    private final int tokens;

    /**
     * Creates a new TokenDoor
     * @param tokens The number of tokens needed to open this door.
     * @param point The (x,y) coordinates this TokenDoor is on
     */
    public TokenDoor(int tokens, Point point) {
        super(point);
        this.tokens = tokens;
    }

    /**
     * represents whether user can open the door or not
     * @param user checks if it can open the door
     * @return boolean value suggesting if the door can be opened.
     */
    public boolean canOpen(User user) {

        return user.getTokens() >= this.tokens;
    }


    @Override
    public void toggleAction(Entity entity) {
        if (entity instanceof User) {
            User user = (User) entity;

            if (user.getTokens() >= this.tokens) {
                GameController.playSound("DoorOpen");
            }
        }
    }

    /**
     * Get the ammount of tokens needed to open this door
     * @return Get the tokens for this door
     */
    public int getTokens() {
        return tokens;
    }
}
