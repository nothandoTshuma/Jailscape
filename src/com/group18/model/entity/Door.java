package com.group18.model.entity;

/**
 * Interface class implemented by any class which uses the door.
 * @ author nothandotshuma
 */
public interface Door {

    /**
     * This method checks if the user has the right key to open the door and if they do, it
     * returns True, otherwise it returns False.
     * @returns True, otherwise it returns False.
     */
    public boolean hasRightKey(User user);

    /**
     * This method checks if the door which the user is trying to open is already open, if it
     * does, it returns True, otherwise it returns False.
     * @return True, otherwise it returns False.
     */
    public boolean isDoorOpen();
}

