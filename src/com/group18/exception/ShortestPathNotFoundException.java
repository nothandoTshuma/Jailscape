package com.group18.exception;

/**
 * Thrown when a shortest path is not found in the game
 * @author danielturato
 */
public class ShortestPathNotFoundException extends Exception {
    /**
     * Create a new ShortestPathNotFoundException
     * @param message The message of this exception
     */
    public ShortestPathNotFoundException(String message) {
        super(message);
    }

    /**
     * Get the message of this exception
     * @return The exception's message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
