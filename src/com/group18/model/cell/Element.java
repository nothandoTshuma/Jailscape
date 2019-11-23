package com.group18.model.cell;

import com.group18.model.Actionable;
import com.group18.model.ElementType;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;

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
     */
    public Element(ElementType elementType) {
        this.elementType = elementType;
    }

    public void toggleAction(Entity entity) {
        if(entity instanceof User) {
            User user = (User) entity;

            if (!(user.hasElementItem(this.elementType.getElementItem()))) {
                //TODO - needs implementation
            }
        }
    }

    public ElementType getElementType() {
        return elementType;
    }
}
