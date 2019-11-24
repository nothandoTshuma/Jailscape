package com.group18.model;

import com.group18.exception.InvalidMoveException;
import com.group18.model.cell.Cell;
import com.group18.model.cell.Door;
import com.group18.model.cell.Wall;
import com.group18.model.entity.Enemy;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;

import java.awt.Point;


/**
 * This class represents each level in Jailscape.
 * Will act as a link between the backend & frontend.
 * @author danielturato
 */
public class Level   {

    /**
     * The board for this level, holding Level.rows x Level.columns of cells.
     */
    private final Cell[][] board;

    /**
     * Creates a new level
     * @param board The board for this level.
     */
    public Level(Cell[][] board) {
        this.board = board;
    }

    /**
     * Used to check if the entities next move will be a valid one.
     * @param entity The entity wanting to move
     * @param direction The direction the entity wants to move in
     * @return A boolean value suggesting if the move is valid.
     */
    public boolean validMove(Entity entity, Direction direction) {
        Point newPosition =
                calculateNewPosition(entity.getCurrentCell().getPosition(), direction);

        return validMoveToCell(getCell(newPosition), entity);
    }

    /**
     * Here, we attempt to move a user in a direction they wish to move in
     * @param user The user who wishes to move
     * @param direction The direction in which the user wishes to move in
     * @throws InvalidMoveException If the user is unable to move in that direction, this is thrown.
     */
    public void movePlayer(User user, Direction direction) throws InvalidMoveException {
        if (validMove(user, direction)) {
            Point newPosition = calculateNewPosition(user.getCurrentCell().getPosition(), direction);

            Cell newCell = getCell(newPosition);
            Cell oldCell = user.getCurrentCell();

            oldCell.removeEntity(user);

            // At this point, the move has been deemed valid.
            // Therefore, the newCell must be either be a ground cell or a door which can be opened.
            if (newCell instanceof Door) {
                //TODO: if door is coloured, we need to consume the key colour
                //TODO: then trigger animations/sounds registering the door is unlocked
                //TODO: then we to recalculate & re-verify the user can reach at least +1 in multiple directions
                //TODO: if they can't then they shouldn't be able to open the door
                //TODO: but if they can , then continue
                //TODO: then move the user.
            } else {
                newCell.placePlayer(user);
                user.setCurrentCell(newCell);
            }

        } else {
            throw new InvalidMoveException();
        }
    }

    public void moveEnemy(Enemy enemy, Direction direction) {}

    public boolean isEnemyClose(User user) {
        return false;
    }

    /**
     * Get a cell, given a pair of coordinates
     * @param point The point in which the cell is located
     * @return The cell instance at the given Point.
     */
    public Cell getCell(Point point) {
        int x = (int) point.getX();
        int y = (int) point.getY();

        return this.board[y][x];
    }

    /**
     * Get the whole board of cells
     * @return The board of cells in the Game.
     */
    public Cell[][] getBoard() {
        return this.board;
    }

    private void removeCell(Point point) {}

    public void replaceCell(Point point, Cell newCell) {}

    /**
     * Used to check if an entity is able to move on to this cell.
     * @param cell The cell in which the entity wants to move on to
     * @param entity The entity that wishes to move
     * @return A boolean value, suggesting if the entity is able to move to the requested cell.
     */
    private boolean validMoveToCell(Cell cell, Entity entity) {

        if (entity instanceof User) {
            if (cell.hasPlayer()) {
                return false;
            }

            if (cell instanceof Wall) {
                if (cell instanceof Door) {
                    //TODO:drt - Can they unlock the door
                } else {
                    return false;
                }
            }

            return true;
        }

        return !(cell instanceof Wall);
    }

    /**
     * Calculates the position Point based on an entities previous position and the direction they wish to move
     * @param oldPosition The entities old position
     * @param direction The entities direction they wish to move in
     * @return The new Point position the entity will end up at.
     */
    public Point calculateNewPosition(Point oldPosition, Direction direction) {
        int oldX = (int) oldPosition.getX();
        int oldY = (int) oldPosition.getY();
        Point newPosition = new Point(oldX, oldY);

        switch (direction) {
            case UP:
                newPosition.translate(0,1);
                break;
            case DOWN:
                newPosition.translate(0,-1);
                break;
            case LEFT:
                newPosition.translate(-1,0);
                break;
            case RIGHT:
                newPosition.translate(1,0);
                break;
            default:
                //TODO:drt - Probably want to throw invalid direction error here.
                break;
        }

        return newPosition;
    }


}