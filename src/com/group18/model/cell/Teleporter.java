package com.group18.model.cell;

import com.group18.exception.InvalidMoveException;
import com.group18.model.Actionable;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;

/**
 * Models a teleporter cell, in which users can teleport to a partner cell
 * @author danielturato nothandotshuma riyagupta
 */
public class Teleporter extends Ground implements Actionable {

    /**
     * This cells teleporter partner
     */
    private Teleporter partner;

    /**
     * Creates a new teleporter cell
     * @param partner The partner of this teleporter
     */
    public Teleporter(Teleporter partner) {
        this.partner = partner;
    }

    /**
     * Toggles this teleporters action, by teleporting the user to the partner cell
     * @param entity The entity in which the cell's action is toggled upon.
     */
    @Override
    public void toggleAction(Entity entity) {
        if (entity instanceof User) {
            removeEntity(entity);
            try {
                partner.placePlayer((User) entity);
            } catch (InvalidMoveException ex) {
                //TODO - not sure what to do now
            }
        }
    }
}
