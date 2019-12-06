package com.group18.model;

import com.group18.model.entity.Entity;

/**
 * Any cell that performs an action, should implement this interface
 * @author riyagupta
 */
public interface Actionable {

    /**
     * If a cell is actionable, then they must be able to toggle an action.
     * This method toggles that cell's specific action.
     * @param entity The entity in which the cell's action is toggled upon.
     */
    public void toggleAction(Entity entity);
}
