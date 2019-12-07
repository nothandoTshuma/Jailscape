package com.group18.core;


import com.group18.model.Level;
import com.group18.model.cell.*;
import com.group18.model.entity.*;
import com.group18.model.item.Collectable;
import com.group18.model.item.ElementItem;
import com.group18.model.item.Key;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static com.group18.model.item.ElementItem.FIRE_BOOTS;
import static com.group18.model.item.ElementItem.ICE_SKATES;
import static java.util.logging.Level.WARNING;

/**
 * This class creates a saved level file for a specific file
 *
 * @author danielturato
 */
public class LevelSaver {

    /**
     * The directory which will hold all user-saved level files
     */
    public static final String SAVED_LEVEL_DIRECTORY = "./src/resources/saved-levels/";

    /**
     * Used to log out important errors/messages to the console
     */
    private static final Logger LOGGER = Logger.getLogger("LevelSaver");

    /**
     * Save a level in progress so user's can reload a saved level.
     * @param levelNumber The level number that is being saved
     * @param level The level object that is being saved
     * @param user The user associated with the level
     * @param currentTime The current elapsed time of the level so far
     */
    public static void saveLevel(int levelNumber, Level level, User user, Long currentTime) {
        String levelFileName =
                String.format("%s%s-level-save%s.txt", SAVED_LEVEL_DIRECTORY, user.getUsername(), levelNumber);
        delete(levelFileName);
        createFile(level, levelFileName, user, currentTime);
    }

    /**
     * Delete a previous saved level, if it already exists
     * @param levelFileName The level file to be deleted
     */
    public static void delete(String levelFileName) {
        try {
            Files.deleteIfExists(Paths.get(levelFileName));
        } catch (IOException ex) {
            LOGGER.log(WARNING, "There was a problem deleting this file: " + levelFileName, ex);
        }
    }

    private static void createFile(Level level, String levelFileName, User user, Long currentTime) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(levelFileName))) {
            Cell[][] cells = level.getBoard();
            writer.write(String.format("%d,%d", level.getBoardHeight(), level.getBoardWidth()));
            writer.newLine();
            writer.write(String.valueOf(currentTime));
            writer.newLine();
            char delimiter = ',';
            for (int i = 0; i < level.getBoardHeight(); i++) {
                for (int j = 0; j < level.getBoardWidth(); j++) {
                    StringBuilder cellBuilder = new StringBuilder();
                    Cell currentCell = cells[i][j];
                    int entities = currentCell.getCurrentEntities().size();
                    cellBuilder.append(getCellType(currentCell));
                    cellBuilder.append(delimiter);
                    cellBuilder.append(entities);
                    cellBuilder.append(delimiter);
                    cellBuilder.append(getItemType(currentCell));
                    writer.write(cellBuilder.toString());
                    writer.newLine();
                    if (entities > 0) {
                        for (Entity entity : currentCell.getCurrentEntities()) {
                            StringBuilder entityBuilder = new StringBuilder();
                            entityBuilder.append(getEntityType(entity));
                            entityBuilder.append(delimiter);
                            entityBuilder.append(getEntityDirectionType(entity));
                            writer.write(entityBuilder.toString());
                            writer.newLine();
                        }
                    }
                }
            }
        } catch (IOException ex) {
            LOGGER.log(WARNING, "There was a problem saving this level to a file", ex);
        }
    }

    private static String getEntityType(Entity entity) {
        String entityAcronym = "";

        if (entity instanceof User) {
            entityAcronym = "U";
        } else if (entity instanceof SmartTargetingEnemy) {
            entityAcronym = "STE";
        } else if (entity instanceof DumbTargetingEnemy) {
            entityAcronym = "DTE";
        } else if (entity instanceof StraightLineEnemy) {
            entityAcronym = "SLE";
        } else if (entity instanceof WallFollowingEnemy) {
            entityAcronym = "WFE";
        }

        return entityAcronym;
    }

    private static String getEntityDirectionType(Entity entity) {
        String directionType = "";

        switch (entity.getDirection()) {
            case UP:
                directionType = "U";
                break;
            case DOWN:
                directionType = "D";
                break;
            case LEFT:
                directionType = "L";
                break;
            case RIGHT:
                directionType = "R";
                break;
            case IDLE:
                directionType = "I";
                break;
            default:
                break;
        }

        return directionType;
    }

    /**
     * Used to check if there's an item on a current cell. If there is, convert
     * the item to it's acronym.
     * @param currentCell The cell that needs checking
     * @return A string representing the acronym for the cell's item
     */
    private static String getItemType(Cell currentCell) {
        if (currentCell instanceof Ground) {
            if (((Ground) currentCell).hasItem()) {
                Collectable item = ((Ground) currentCell).getItem();

                if (item instanceof ElementItem) {
                    if (((ElementItem) item) == FIRE_BOOTS) {
                        return "FBI";
                    } else if (((ElementItem) item) == ICE_SKATES) {
                        return "ISI";
                    }

                    return "FI";
                }

                Key key = (Key) item;
                String itemAcroynm = "";

                switch (key) {
                    case RED_KEY:
                        itemAcroynm = "RKI";
                        break;
                    case BLUE_KEY:
                        itemAcroynm = "BKI";
                        break;
                    case YELLOW_KEY:
                        itemAcroynm = "YKI";
                        break;
                    case GREEN_KEY:
                        itemAcroynm = "GKI";
                        break;
                    case TOKEN_KEY:
                        itemAcroynm = "TKI";
                        break;
                    default:
                        break;
                }

                return itemAcroynm;
            }

            return "X";
        }

        return "X";
    }

    private static String getCellType(Cell currentCell) {
        String cellAcronym = "";

        if (currentCell instanceof ColourDoor) {
            switch (((ColourDoor) currentCell).getColour()) {
                case RED:
                    cellAcronym = "RD";
                    break;
                case BLUE:
                    cellAcronym = "BD";
                    break;
                case YELLOW:
                    cellAcronym = "YD";
                    break;
                case GREEN:
                    cellAcronym = "GD";
                    break;
                default:
                    break;
            }
        } else if (currentCell instanceof TokenDoor) {
            cellAcronym = "TD" + ((TokenDoor) currentCell).getTokens();
        } else if (currentCell instanceof Wall) {
            cellAcronym = "WC";
        } else if (currentCell instanceof Element) {
            switch (((Element) currentCell).getElementType()) {
                case FIRE:
                    cellAcronym = "FC";
                    break;
                case WATER:
                    cellAcronym = "WTC";
                    break;
                case ICE:
                    cellAcronym = "IC";
                    break;
                default:
                    break;
            }
        } else if (currentCell instanceof Goal) {
            cellAcronym = "GOC";
        } else if (currentCell instanceof Teleporter) {
            cellAcronym = "TC";
        } else if (currentCell instanceof Ground) {
            cellAcronym = "GC";
        }

        return cellAcronym;
    }


}
