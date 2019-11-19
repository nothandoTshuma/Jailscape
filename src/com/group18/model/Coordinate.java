package com.group18.model;

/**
 * Models a coordinate in the game (x,y)
 * @author danielturato
 */
public class Coordinate {

    /**
     * Holds the value for the X axis
     */
    private int x;

    /**
     * Holds the value for the Y axis
     */
    private int y;

    /**
     * Generate a new Coordinate object by passing in the X,Y values
     * @param x The X axis
     * @param y The Y axis
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the X value
     * @return The X value
     */
    public int getX() {
        return x;
    }

    /**
     * Set the X value
     * @param x The new X value
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the Y value
     * @return The Y value
     */
    public int getY() {
        return y;
    }

    /**
     * Set the Y value
     * @param y The new Y value
     */
    public void setY(int y) {
        this.y = y;
    }
}
