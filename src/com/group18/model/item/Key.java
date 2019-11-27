package com.group18.model.item;

import com.group18.model.Colour;
import com.group18.model.item.Collectable;

import static com.group18.model.Colour.*;

/**
 * Represents all types of keys
 * @author Riya Gupta
 */
public enum Key implements Collectable {
    RED_KEY(RED),
    GREEN_KEY(GREEN),
    BLUE_KEY(BLUE),
    YELLOW_KEY(YELLOW),
    BLACK_KEY(BLACK);

    /**
     * Holds the colour of key
     */
    private Colour colour;

    /**
     * Creates a new key
     * @param colour The colour of this key
     */
    Key(Colour colour) {
        this.colour = colour;
    }

    /**
     * This returns colour of key
     * @return The colour of this key
     */
    public Colour getColour() {
        return colour;
    }
}
