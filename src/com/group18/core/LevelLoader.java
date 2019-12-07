package com.group18.core;

import com.group18.controller.GameController;
import com.group18.exception.InvalidMoveException;
import com.group18.model.Colour;
import com.group18.model.Direction;
import com.group18.model.ElementType;
import com.group18.model.Level;
import com.group18.model.cell.*;
import com.group18.model.entity.*;
import com.group18.model.item.ElementItem;
import com.group18.model.item.Key;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;

import static com.group18.model.Direction.*;
import static java.util.logging.Level.WARNING;

/**
 * This class allows the loading of both default level files and level files
 * that have been saved in progress.
 *
 * @author danielturato
 */
public class LevelLoader {

    /**
     * The directory which will hold all the default level files
     */
    public static final String DEFAULT_LEVEL_DIRECTORY = "./src/resources/levels/Level";

    /**
     * The directory which will hold all the saved level files
     */
    public static final String SAVED_LEVEL_DIRECTORY = "./src/resources/saved-levels/";

    /**
     * The logger which will allows us to output errors in a nice format
     */
    private static final Logger LOGGER = Logger.getLogger("LevelLoader");

    /**
     * True if loading from a saved file
     */
    private static boolean loadSave = false;

    /**
     * Load a saved level file for a specified level
     * @param level The saved level number
     * @param user The user who wants the saved the level
     * @return A level object containing the saved level
     */
    public static Level loadSavedLevel(int level, User user) {
        String fileName = SAVED_LEVEL_DIRECTORY + user.getUsername() + "-level-save" + level + ".txt";
        loadSave = true;
        return load(level, user, fileName);
    }

    /**
     * Load a default level file for a specified level
     * @param level The level to be loaded
     * @param user The user that will be associated with the level
     * @return The level object
     */
    public static Level loadLevel(int level, User user) {
        String fileName = DEFAULT_LEVEL_DIRECTORY + level + ".txt";
        return load(level, user, fileName);
    }

    private static Level load(int level, User user, String fileName) {
        Cell[][] cells = null;

        try {
            Scanner inputStream = new Scanner(new File(fileName));
            int lineCounter = 0;
            int boardHeight = 0;
            int boardWidth = 0;

            Cell previousCell = null;
            int entities = 0;
            while (inputStream.hasNextLine()) {
                Scanner line = new Scanner(inputStream.nextLine());
                line.useDelimiter(",");

                if (lineCounter == 0) {
                    cells = new Cell[line.nextInt()][line.nextInt()];
                } else if (loadSave && lineCounter == 1) {
                    GameController.setTotalSavedTime(line.nextLong());
                    loadSave = false;
                } else if (entities > 0) {
                    placeEntity(line, previousCell, user);
                    entities--;
                } else {
                    Point point = new Point(boardWidth, boardHeight);
                    entities = fillCell(line, point, cells, user);
                    previousCell = cells[(int) point.getY()][(int) point.getX()];

                    if (boardWidth+1 < cells[0].length) {
                        boardWidth++;
                    } else if (boardWidth+1 >= cells[0].length) {
                        if (boardHeight+1 < cells.length) {
                            boardWidth = 0;
                            boardHeight++;
                        }
                    }
                }

                lineCounter++;
            }


            setTeleporterPartners(cells);
            Level levelObj = new Level(cells, level);
            setLevelFor(cells, levelObj);

            return levelObj;

        } catch (FileNotFoundException ex) {
            LOGGER.log(WARNING, String.format("Level file %s does not exist", level), ex);
        }

        return null;
    }

    /**
     * Set the level for each cell
     * @param cells The cells in the level
     * @param levelObj The level object
     */
    private static void setLevelFor(Cell[][] cells, Level levelObj) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j].setLevel(levelObj);
            }
        }
    }

    /**
     * There will only ever be one teleporter per level. So after all the cells are set
     * we iterate over the cells, and partner up the teleporters
     * @param cells
     */
    private static void setTeleporterPartners(Cell[][] cells) {
        Teleporter teleporter = null;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j] instanceof Teleporter) {
                    if (teleporter == null) {
                        teleporter = (Teleporter) cells[i][j];
                    } else {
                        Teleporter newPartner = (Teleporter) cells[i][j];
                        newPartner.setPartner(teleporter);
                        teleporter.setPartner(newPartner);
                    }
                }
            }
        }
    }

    /**
     * Fill in a cell at a specific point in the level
     * @param line The current line in the level input file aka the current cell
     * @param point The point in the 2D level grid we are currently at
     * @param cells The 2D array of cells for the level
     * @param user The user associated with this level
     * @return The number of entities on this cell
     */
    private static int fillCell(Scanner line, Point point, Cell[][] cells, User user) {
        Cell cell = createNewCell(line, point);
        cell.setCoordinates(point);

        int entities = Integer.parseInt(line.next());
        String potentialItem = line.next();
        if (!potentialItem.equals(("X"))) {
            setItem(cell, potentialItem);
        }

        cells[(int) point.getY()][(int) point.getX()] = cell;

        return entities;
    }

    /**
     * Set an item for a specified cell
     * @param cell The cell in which an item will be placed upon
     * @param potentialItem The potential item name to be placed on the cell
     */
    private static void setItem(Cell cell, String potentialItem) {
        ItemAcronym itemAcronym = ItemAcronym.valueOf(potentialItem);
        // If we can place an item, the cell must be a ground cell
        Ground ground = (Ground) cell;

        switch (itemAcronym) {
            case FBI:
                ground.setItem(ElementItem.FIRE_BOOTS);
                break;
            case FI:
                ground.setItem(ElementItem.FLIPPERS);
                break;
            case ISI:
                ground.setItem(ElementItem.ICE_SKATES);
                System.out.println("Done");
                break;
            case TKI:
                ground.setItem(Key.TOKEN_KEY);
                break;
            case GKI:
                ground.setItem(Key.GREEN_KEY);
                break;
            case RKI:
                ground.setItem(Key.RED_KEY);
                break;
            case BKI:
                ground.setItem(Key.BLUE_KEY);
                break;
            case YKI:
                ground.setItem(Key.YELLOW_KEY);
                break;
            default:
                break;
        }
    }

    /**
     * Retrieve a Direction value based on the value given in a level input file
     * @param potentialDirection The potential direction name
     * @return The direction value
     */
    private static Direction retrieveDirection(String potentialDirection) {
        Direction direction = null;

        switch (potentialDirection) {
            case "L":
                direction = LEFT;
                break;
            case "R":
                direction = RIGHT;
                break;
            case "U":
                direction = UP;
                break;
            case "D":
                direction = DOWN;
                break;
            case "I":
                direction = IDLE;
                break;
            default:
                break;
        }

        return direction;
    }

    /**
     * Place an entity on to a specified cell
     * @param line The level input line associated with the cell
     * @param cell The cell that the entity will be placed on
     * @param user The user that potentially can be placed on the cell
     */
    private static void placeEntity(Scanner line, Cell cell, User user) {
        String entityType = line.next();
        Direction direction = retrieveDirection(line.next());
        if (!entityType.equals("X")) {
            EntityAcronym entityAcronym = EntityAcronym.valueOf(entityType);
            try {
                switch (entityAcronym) {
                    case U:
                        cell.placePlayer(user);
                        user.setCurrentCell(cell);
                        user.setDirection(direction);
                        break;
                    case SLE:
                        StraightLineEnemy sle = new StraightLineEnemy(null);
                        cell.placeEnemy(sle);
                        sle.setCurrentCell(cell);
                        sle.setDirection(direction);
                        sle.setOrientation(direction);
                        break;
                    case STE:
                        SmartTargetingEnemy ste = new SmartTargetingEnemy();
                        cell.placeEnemy(ste);
                        ste.setCurrentCell(cell);
                        ste.setDirection(direction);
                        break;
                    case WFE:
                        WallFollowingEnemy wfe = new WallFollowingEnemy();
                        cell.placeEnemy(wfe);
                        wfe.setCurrentCell(cell);
                        wfe.setDirection(direction);
                        break;
                    case DTE:
                        DumbTargetingEnemy dte = new DumbTargetingEnemy();
                        cell.placeEnemy(dte);
                        dte.setCurrentCell(cell);
                        dte.setDirection(direction);
                        break;
                    default:
                        break;
                }
            } catch (InvalidMoveException ex) {
                LOGGER.log(WARNING, "This level file involves an entity placement that's invalid", ex);
            }
        }
    }

    /**
     * Creates a new cell
     * @param line The level input line associated with the cell
     * @param point The point in which this cell is associated with
     * @return The new generated cell
     */
    private static Cell createNewCell(Scanner line, Point point) {
        String potentialCell = line.next();
        CellAcronym cellType;
        if (potentialCell.length() > 2) {
            if (Character.isDigit(potentialCell.charAt(2))) {
                cellType = CellAcronym.TD;
            } else  {
                cellType = CellAcronym.valueOf(potentialCell);
            }
        } else {
            cellType = CellAcronym.valueOf(potentialCell);
        }

        Cell cell = null;

        switch (cellType) {
            case WC:
                cell = new Wall(point);
                break;
            case GC:
                cell = new Ground(point);
                break;
            case FC:
                cell = new Element(ElementType.FIRE, point);
                break;
            case WTC:
                cell = new Element(ElementType.WATER, point);
                break;
            case IC:
                cell = new Element(ElementType.ICE, point);
                break;
            case TC:
                cell = new Teleporter(null, point);
                break;
            case GOC:
                cell = new Goal(point);
                break;
            case GD:
                cell = new ColourDoor(Colour.GREEN, point);
                break;
            case RD:
                cell = new ColourDoor(Colour.RED, point);
                break;
            case BD:
                cell = new ColourDoor(Colour.BLUE, point);
                break;
            case YD:
                cell = new ColourDoor(Colour.YELLOW, point);
                break;
            case TD:
                int tokens = Integer.parseInt(String.valueOf(potentialCell.charAt(2)));
                cell = new TokenDoor(tokens, point);
                break;
            default:
                break;
        }

        return cell;
    }

    /**
     * An enum representing all the cell acronyms in the level file
     */
    private enum CellAcronym {
        WC,
        GC,
        FC,
        WTC,
        IC,
        TC,
        GOC,
        TD,
        GD,
        RD,
        BD,
        YD;
    }

    /**
     * An enum representing all the entity acronyms in the level file
     */
    private enum EntityAcronym {
        U,
        SLE,
        STE,
        WFE,
        DTE;
    }

    /**
     * An item representing all the item acronyms in the level file
     */
    private enum ItemAcronym {
        FBI,
        FI,
        ISI,
        TKI,
        GKI,
        RKI,
        BKI,
        YKI;
    }

}
