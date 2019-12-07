package com.group18.exception;

/**
 * An exception thrown when there is an invalid level inputted
 * @author danielturato nothandotshuma riyagupta
 */
public class InvalidLevelException extends IllegalArgumentException {

    /**
     * Creates a new InvalidLevelException
     * @param message The message associated with the exception
     */
    public InvalidLevelException(String message) {
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
