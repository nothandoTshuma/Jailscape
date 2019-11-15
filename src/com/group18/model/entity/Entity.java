package com.group18.model.entity;

import com.group18.core.Direction;
import com.group18.model.cell.Cell;

import java.io.Serializable;

/**
 * Represents a general Entity in which all subclasses will inherit from
 * @author danielturato nothandotshuma riyagupta
 */
public abstract class Entity implements Cloneable, Serializable {
    /**
     * Stores the entities current cell position
     */
    private Cell currentCell;

    /**
     * Stores the entities current direction
     */
    private Direction direction;

    /**
     * Get the current entities cell
     * @return The entities current cell
     */
    public Cell getCurrentCell() {
        return currentCell;
    }

    /**
     * Sets the current entities cell
     * @param currentCell The new cell to be set
     */
    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    /**
     * Get the entities current direction
     * @return The current direction of the entity
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the entities current direction
     * @param direction The new direction to be set
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
