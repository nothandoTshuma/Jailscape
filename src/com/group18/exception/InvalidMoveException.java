package com.group18.exception;

/**
 * An exception thrown when an entity attempts to make an invalid move
 * @author danielturato nothandotshuma riyagupta
 */
public class InvalidMoveException extends Exception {

    /**
     * Creates a new InvalidMoveException
     * @param message The message associated with the exception
     */
    public InvalidMoveException(String message) {
        super(message);
    }

    /**
     * Get this exceptions message
     * @return The message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
