package com.group18.model.cell;

import com.group18.controller.GameController;
import com.group18.model.Actionable;
import com.group18.model.ElementType;
import com.group18.model.State;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;

import java.awt.*;

import static com.group18.model.State.*;

/**
 * Represents special cell which requires special elements for user to move.
 * @author RIYA GUPTA
 */
public class Element extends Ground implements Actionable {
    /**
     * stores the element type.
     */
    private final ElementType elementType;

    /**
     * sets the current element type
     * @param elementType represents the new element type.
     * @param point This cells (x,y) coordinates
     */
    public Element(ElementType elementType, Point point) {
        super(point);
        this.elementType = elementType;
    }

    public void toggleAction(Entity entity) {
        if(entity instanceof User) {
            User user = (User) entity;
            if (!(user.hasElementItem(this.elementType.getElementItem()))) {
                GameController.triggerAlert("You walked into an element without its item", State.LEVEL_LOST);
            }

            switch (elementType) {
                case WATER:
                    GameController.playSound("WaterSplash");
                    break;
                case FIRE:
                    GameController.playSound("FireCrackle");
                    break;
                default:
                    break;
            }
        }
    }

    public ElementType getElementType() {
        return elementType;
    }
}
