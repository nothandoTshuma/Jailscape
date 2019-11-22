package com.group18.model;

import static com.group18.model.Colour.*;

/**
 * Represents all types of keys
 * @author Riya Gupta
 */
public enum Key {
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
     * @param colour
     */
    Key(Colour colour) {
        this.colour = colour;
    }

    /**
     * This returns colour of key
     * @return colour
     */
    public Colour getColour() {
        return colour;
    }
}
