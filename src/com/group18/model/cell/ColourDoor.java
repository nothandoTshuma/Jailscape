package com.group18.model.cell;

import com.group18.model.Actionable;
import com.group18.model.Colour;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;

/**
 * Represents the coloured door which requires same coloured key same to be opened.
 * @author RIYA GUPTA
 * */
public class ColourDoor extends Wall implements Door, Actionable {
     /**
     * represents the colour of the door
     */
    private final Colour colour;

    /**
     * represents constructor that takes the colour of the door and colour of key needed to open the door.
     * @param colour returns the colour needed to open the door.
     */
    public ColourDoor(Colour colour) {
        this.colour = colour;
    }
    /**
     * represents whether user can open the door or not
     * @param user checks if it can open the door
     * @return boolean value suggesting if the door can be opened.
     */
    public boolean canOpen(User user) {
        return user.hasKey(colour);
    }

    public void toggleAction(Entity entity) {
        if (entity instanceof User) {
            User user = (User) entity;

            if (canOpen(user)) {
                user.consumeKey(colour);
                //TODO add animations links
            }
        }
    }

}
