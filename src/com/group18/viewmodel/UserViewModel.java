package com.group18.viewmodel;

import com.group18.model.entity.User;
import javafx.scene.image.Image;

/**
 * The user View Model, acting as a bridge between the User object and what needs to be
 * represented in the front-end
 *
 * @author danielturato
 */
public class UserViewModel extends ViewModel {

    /**
     * The User associated with this View Model
     */
    private User user;

    /**
     * Creates a new User View Model
     * @param user The user object
     */
    public UserViewModel(User user) {
        super(new Image("resources/assets/Player/Idle/PlayerIdle_00.png"));
        this.user = user;
    }

    /**
     * Get the user associated with this View Model
     * @return The user
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the user for this view model
     * @param user The new user
     */
    public void setUser(User user) {
        this.user = user;
    }
}
