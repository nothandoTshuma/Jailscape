package com.group18.model;

import com.group18.model.item.ElementItem;

import static com.group18.model.item.ElementItem.*;

/**
 * Represents different types of Elements
 * @author Riya Gupta
 */
public enum ElementType {
    WATER(FLIPPERS),
    FIRE(FIRE_BOOTS),
    ICE(ICE_SKATES);

    /**
     * Holds the element item
     */
    private ElementItem elementItem;

    /**
     * Creates a new element item
     * @param elementItem represents the new element item.
     */
    ElementType(ElementItem elementItem)
    {
        this.elementItem = elementItem;
    }

    /**
     * Returns the element item
     * @return elementItem
     */
    public ElementItem getElementItem() {
        return elementItem;
    }
}
