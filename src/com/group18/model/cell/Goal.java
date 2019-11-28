package com.group18.model.cell;

import com.group18.model.Actionable;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;

import java.awt.*;

import static com.group18.model.State.*;

/**
 *  A cell modelling a Goal in which if the user reaches, wins the level
 *  @author danielturato nothandotshuma riyagupta
 */
public class Goal extends Ground implements Actionable {

    /**
     * Creates a new Goal cell
     * @param point This cells (x,y) coordinates
     */
    public Goal(Point point) {
        super(point);
    }

    /**
     * Updates the game state, if the user reaches this cell
     * @param entity The entity in which the cell's action is toggled upon.
     */
    @Override
    public void toggleAction(Entity entity) {
        if (entity instanceof User) {
            this.getLevel().updateState(LEVEL_WON);
        }
    }
}
