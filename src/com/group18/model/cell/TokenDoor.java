package com.group18.model.cell;

import com.group18.model.Actionable;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;

/**
 * represents the door which requires tokens to pass through
 * @author RIYA GUPTA
 */
public class TokenDoor extends Wall implements Actionable {
    /**
     * represents number of tokens
     */
    private final int tokens;

    /**
     * returns number of tokens
     */
    public TokenDoor(int tokens)
    {
        this.tokens = tokens;
    }

    /**
     * represents whether user can open the door or not
     * @param user checks if it can open the door
     * @return boolean value suggesting if the door can be opened.
     */
    public boolean canOpen(User user)
    {
        return user.getTokens() >= this.tokens;
    }


    /**
     *
     * @param entity
     */
    public void toggleAction(Entity entity)
    {
       //TODO later
    }

}
