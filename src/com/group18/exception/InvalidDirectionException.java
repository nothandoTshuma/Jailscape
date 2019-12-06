package com.group18.exception;

/**
 * An exception thrown when there is an invalid direction inputted
 * @author danielturato nothandotshuma riyagupta
 */
public class InvalidDirectionException extends IllegalArgumentException {

    /**
     * Creates a new InvalidDirectionException
     * @param message The message associated with the exception
     */
    public InvalidDirectionException(String message) {
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
