package com.group18.model.cell;

import com.group18.controller.GameController;
import com.group18.model.Actionable;
import com.group18.model.Colour;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;

import java.awt.*;

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
     * @param point This cells (x,y) coordinates
     */
    public ColourDoor(Colour colour, Point point) {
        super(point);
        this.colour = colour;
    }
    /**
     * represents whether user can open the door or not
     * @param user checks if it can open the door
     * @return boolean value suggesting if the door can be opened.
     */
    public boolean canOpen(User user) {
        return user.hasKey(colour, getLevel().getCurrentLevel());
    }

    public void toggleAction(Entity entity) {
        if (entity instanceof User) {
            User user = (User) entity;

            if (canOpen(user)) {
                user.consumeKey(colour, getLevel().getCurrentLevel());
                GameController.playSound("DoorOpen");
            }
        }
    }

    /**
     * Get the colour of this door
     * @return The colour of the door
     */
    public Colour getColour() {
        return colour;
    }
}
