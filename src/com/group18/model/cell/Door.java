package com.group18.model.cell;

import com.group18.model.entity.User;

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
    public boolean canOpen(User user);


}

