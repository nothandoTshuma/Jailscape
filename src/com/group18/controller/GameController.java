package com.group18.controller;


import com.group18.core.LevelLoader;
import com.group18.core.LevelSaver;
import com.group18.core.ResourceRepository;
import com.group18.core.UserRepository;
import com.group18.exception.InvalidLevelException;
import com.group18.exception.InvalidMoveException;
import com.group18.model.Direction;
import com.group18.model.Level;
import com.group18.model.State;
import com.group18.model.cell.*;
import com.group18.model.entity.*;
import com.group18.model.item.Collectable;
import com.group18.model.item.ElementItem;
import com.group18.model.item.Key;
import com.group18.viewmodel.EnemyViewModel;
import com.group18.viewmodel.ItemViewModel;
import com.group18.viewmodel.UserViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

/**
 * The controller for JailScape which controls the state & action
 * for each possible playable level.
 *
 * @author frasergrandfield ethanpugh danielturato
 */
public class GameController extends BaseController {

    /**
     * The width of each cell in each level. Also used to determine
     * how far each entity can move in speed.
     */
    private static final int CELL_WIDTH = 64 ;

    /**
     * A logger which allows specific output to the console
     */
    private static final Logger LOGGER = Logger.getLogger("GameController");

    /**
     * The level object associated with this level
     */
    private static Level level;

    /**
     * The user view model associated with this level
     */
    private static UserViewModel userViewModel;

    /**
     * The board pane for this controller
     */
    private static Pane boardPane;

    /**
     * The primary stage for this controller
     */
    private static Stage primaryStage;

    /**
     * The initial epoch start time for this level
     */
    private static Instant startTime;

    /**
     * The total elapsed pause time for this level
     */
    private static Long totalPausedTime = 0L;

    /**
     * The total saved time used if this level comes from a save
     */
    private static Long totalSavedTime = 0L;

    /**
     * The current level number for this controller
     */
    private static int currentLevel;

    /**
     * The group which holds all images of all cells
     */
    private static Group cellImages;

    /**
     * The background music player for the game
     */
    private static MediaPlayer backgroundMusicPlayer;

    /**
     * Holds if the current animation of a user has been completed
     */
    private boolean animationCompleted = true;

    /**
     * The enemy view models associated with this level
     */
    private List<EnemyViewModel> enemyViewModels = new ArrayList<>();

    /**
     * The item view models associated with this level
     */
    private List<ItemViewModel> itemViewModels = new ArrayList<>();

    /**
     * The current width of this level
     */
    private int levelWidth;

    /**
     * The current height of this level
     */
    private int levelHeight;

    /**
     * Used to determine if the user pressed a button
     */
    private boolean pressed = false;

    /**
     * Creates a new Game Controller, starting the Game for the user at the
     * specified stage
     * @param stage The primary stage for the game to be played on
     */
    public GameController(Stage stage) {
        primaryStage = stage;
        init();
    }

    public GameController() {

    }

    /**
     * The init method, that will initialise the whole game in which the user
     * will be able to comfortably be able to play
     */
    private void init() {
        createBoard();

        Scene scene = new Scene(new BorderPane(boardPane),500,500);
        restrictView(scene);

        scene.setOnKeyPressed(e -> processKey(e.getCode()));
        scene.setOnKeyReleased(e -> pressed = false);

        primaryStage.setScene(scene);
        primaryStage.show();
        startTime = Instant.now();
        playSound("BackgroundMusic");
    }


    /**
     * Restrict the view, so the user can only view a specific section of the game
     * @param scene The scene that needs restricting
     */
    private void restrictView(Scene scene) {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(scene.widthProperty());
        clip.heightProperty().bind(scene.heightProperty());

        ImageView userImageView = userViewModel.getImageView();
        clip.xProperty().bind(Bindings.createDoubleBinding(() ->
                        clampRange(userImageView.getX() - scene.getWidth() / 2,
                                0, boardPane.getWidth() - scene.getWidth()),
                                    userImageView.xProperty(), scene.widthProperty()));

        clip.yProperty().bind(Bindings.createDoubleBinding(() ->
                        clampRange(
                                userImageView.getY() - scene.getHeight() / 2,
                                0, boardPane.getHeight() - scene.getHeight()),
                                userImageView.yProperty(), scene.heightProperty()));

        boardPane.setClip(clip);
        boardPane.translateXProperty().bind(clip.xProperty().multiply(-1));
        boardPane.translateYProperty().bind(clip.yProperty().multiply(-1));
    }

    /**
     * Create and fill the whole board for this specific level
     */
    private void createBoard() {
        Pane pane = drawCells(level);
        boardPane = pane;
        drawAssets(pane, level);
    }

    /**
     * Load a level from a specific level file
     */
    public static void loadBaseLevel(int levelNum) {
        currentLevel = levelNum;
        level = LevelLoader.loadLevel(levelNum, userViewModel.getUser());
    }

    public static void loadSavedLevel(int levelNum) {
        currentLevel = levelNum;
        level = LevelLoader.loadSavedLevel(levelNum, userViewModel.getUser());
    }

    /**
     * Draw each entity onto the level
     * @param pane The pane the entities will be drawn on
     * @param level The level object that these entities are on
     */
    private void drawAssets(Pane pane, Level level) {
        Cell[][] cells = level.getBoard();

        Group sprites = new Group();
        for (int i = 0; i < levelHeight; i++) {
            for (int j = 0; j < levelWidth; j++) {
                Cell cell = cells[j][i];
                if (cell.getCurrentEntities().size() > 0) {
                    Entity entity = cell.getCurrentEntities().get(0);
                    if (entity instanceof User) {
                        //TODO:drt - Don't create new user view model after implementing user loading
                        userViewModel = new UserViewModel((User) entity);
                        userViewModel.setImageView(j, i);
                        sprites.getChildren().add(userViewModel.getImageView());
                    } else {
                        EnemyViewModel enemyViewModel = createEnemyViewModel((Enemy) entity, j, i);
                        enemyViewModels.add(enemyViewModel);
                        sprites.getChildren().add(enemyViewModel.getImageView());
                    }
                }

                if (cell instanceof Ground) {
                    Ground ground = (Ground) cell;
                    if (ground.hasItem()) {
                        ItemViewModel itemViewModel = createItemViewModel(ground.getItem(), j, i);
                        itemViewModels.add(itemViewModel);
                        sprites.getChildren().addAll(itemViewModel.getImageView());
                    }
                }
            }
        }

        pane.getChildren().add(sprites);
    }

    /**
     * Creates a new Item View Model based on an item on a cell
     * @param item The item
     * @param y The item's Y position
     * @param x The item's X position
     * @return The ItemViewModel
     */
    private ItemViewModel createItemViewModel(Collectable item, int y, int x) {
        ItemViewModel itemViewModel = injectItemImages(item);
        itemViewModel.setImageView(y, x);
        return itemViewModel;
    }

    /**
     * Inject the required images for each item
     * @param item The item
     * @return The view model of the item
     */
    private ItemViewModel injectItemImages(Collectable item) {
        ItemViewModel itemViewModel = null;

        if (item instanceof ElementItem) {
            switch (((ElementItem) item)) {
                case FIRE_BOOTS:
                    itemViewModel =
                            new ItemViewModel(new Image(ResourceRepository.getResource("FireBoots")), item);
                    break;
                case FLIPPERS:
                    itemViewModel =
                            new ItemViewModel(new Image(ResourceRepository.getResource("Flippers")), item);
                    break;
                default:
                    break;
            }
        } else {
            switch (((Key) item)) {
                case RED_KEY:
                    itemViewModel =
                            new ItemViewModel(new Image(ResourceRepository.getResource("Key-Red")), item);
                    break;
                case BLUE_KEY:
                    itemViewModel =
                            new ItemViewModel(new Image(ResourceRepository.getResource("Key-Blue")), item);
                    break;
                case GREEN_KEY:
                    itemViewModel =
                            new ItemViewModel(new Image(ResourceRepository.getResource("Key-Green")), item);
                    break;
                case YELLOW_KEY:
                    itemViewModel =
                            new ItemViewModel(new Image(ResourceRepository.getResource("Key-Yellow")), item);
                    break;
                case TOKEN_KEY:
                    itemViewModel =
                            new ItemViewModel(new Image(ResourceRepository.getResource("Token")), item);
                    break;
                default:
                    break;
            }
        }
        return itemViewModel;
    }

    /**
     * Creates a new EnemyViewModel based on an Enemy object
     * @param enemy The enemy object
     * @param y It's Y position
     * @param x It's X position
     * @return The EnemyViewModel
     */
    private EnemyViewModel createEnemyViewModel(Enemy enemy, int y, int x) {
        EnemyViewModel enemyViewModel = injectEnemyImages(enemy);
        enemyViewModel.setImageView(y, x);

        return enemyViewModel;
    }

    /**
     * Draw each cell into a new pane
     * @param level The level object associated with this level
     * @return A pane containing all the cell images associated with this level
     */
    private Pane drawCells(Level level) {
        Pane board = new Pane();
        Cell[][] cells = level.getBoard();
        int boardWidth = level.getBoardWidth();
        int boardHeight = level.getBoardHeight();
        setBoardArea(boardWidth, boardHeight);
        System.out.println(levelHeight);
        System.out.println(levelWidth);

        Group cellGroups = new Group();
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                Cell cell = cells[i][j];
                ImageView imageView = createSpriteImageView(i, j, cell);
                cellGroups.getChildren().add(imageView);
            }
        }

        cellImages = cellGroups;
        board.getChildren().add(cellGroups);
        setBoardLimits(board);

        return board;
    }

    /**
     * Set's the board's min & max size limits
     * @param board The board pane associated with this level
     */
    private void setBoardLimits(Pane board) {
        board.setMinSize(levelWidth * 64, levelHeight * 64);
        board.setPrefSize(levelWidth * 64, levelHeight * 64);
        board.setMaxSize(levelWidth * 64, levelHeight * 64);
    }

    /**
     * Create a sprite image view, constraining it's width,height and
     * setting it's relative position
     * @param i The X position
     * @param j The Y position
     * @param cell The cell this imageView will be associated with
     * @return An imageview of the cell
     */
    private ImageView createSpriteImageView(int i, int j, Cell cell) {
        Image spriteImage = getSpriteImage(cell);

        ImageView imageView = new ImageView(spriteImage);
        imageView.setFitHeight(64);
        imageView.setFitWidth(64);
        imageView.setX(j * 64);
        imageView.setY(i * 64);
        return imageView;
    }

    /**
     * Get the image resource of a cell, depending on it's type
     * @param cell The cell we want the image off
     * @return The image of the cell
     */
    private Image getSpriteImage(Cell cell) {
        Image spriteImage = null;
        if (cell instanceof ColourDoor) {
            switch (((ColourDoor) cell).getColour()) {
                case RED:
                    spriteImage = new Image(ResourceRepository.getResource("Red-Door"));
                    break;
                case BLUE:
                    spriteImage = new Image(ResourceRepository.getResource("Blue-Door"));
                    break;
                case YELLOW:
                    spriteImage = new Image(ResourceRepository.getResource("Yellow-Door"));
                    break;
                case GREEN:
                    spriteImage = new Image(ResourceRepository.getResource("Green-Door"));
                    break;
                default:
                    break;
            }
        } else if (cell instanceof TokenDoor) {
            spriteImage = new Image(ResourceRepository.getResource("Token-Door"));
        } else if (cell instanceof Wall) {
            spriteImage = new Image(ResourceRepository.getResource("Wall"));
        } else if (cell instanceof Element) {
            switch (((Element) cell).getElementType()) {
                case FIRE:
                    spriteImage = new Image(ResourceRepository.getResource("Element-Fire"));
                    break;
                case WATER:
                    spriteImage = new Image(ResourceRepository.getResource("Element-Water"));
                    break;
                default:
                    break;
            }
        } else if (cell instanceof Goal) {
            spriteImage = new Image(ResourceRepository.getResource("Goal"));
        } else if (cell instanceof Teleporter) {
            spriteImage = new Image(ResourceRepository.getResource("Teleporter"));
        } else if (cell instanceof Ground) {
            spriteImage = new Image(ResourceRepository.getResource("Ground"));
        }

        return spriteImage;
    }

    /**
     * Move the player in the game
     * @param deltaX The user's X translation value
     * @param deltaY The user's Y translation value
     */
    private void movePlayer(int deltaX, int deltaY) {
        ImageView userImageView = userViewModel.getImageView();

        pressed = true;
        double x = (clampRange(userImageView.getX() + deltaX,
                        0, boardPane.getWidth() - userImageView.getFitWidth()));

        double y =
                (clampRange(userImageView.getY() + deltaY,
                        0, boardPane.getHeight() - userImageView.getFitHeight()));


        if (!(userViewModel.getUser().getCurrentCell() instanceof Teleporter)) {
            animateUser(userImageView, x, y);
        } else {
            double newX = userViewModel.getUser().getCurrentCell().getPosition().getX() * 64;
            double newY = userViewModel.getUser().getCurrentCell().getPosition().getY() * 64;
            animateUser(userImageView, newX, newY);
        }

        if (userViewModel.getUser().getCurrentCell() instanceof Goal) {
            triggerAlert("Congratulations! You have completed Level" + currentLevel, State.LEVEL_WON);
        }

        checkForItemPickups(x, y);

    }

    /**
     * Check if the user has just picked up an item
     * @param x The user's new X position
     * @param y The user's new Y position
     */
    private void checkForItemPickups(double x, double y) {
        for (ItemViewModel itemViewModel : itemViewModels) {
            double iX = itemViewModel.getImageView().getX();
            double iY = itemViewModel.getImageView().getY();

            if (iX == x && iY == y) {
                itemViewModel.getImageView().setVisible(false);
            }
        }
    }

    /**
     * Animate a user by moving them from cell to cell in a smooth transition. Also
     * animating them walking when moving.
     * @param userImageView The user's image view
     * @param x Their new X position
     * @param y Their new Y position
     */
    private void animateUser(ImageView userImageView, double x, double y) {
        Timeline timeline = new Timeline();
        KeyValue keyValueX = new KeyValue(userImageView.xProperty(), x);
        KeyValue keyValueY = new KeyValue(userImageView.yProperty(), y);

        KeyFrame walking = new KeyFrame(Duration.millis(100), e -> {
           userImageView.setImage(new Image(ResourceRepository.getResource("User-Walk")));
        });

        KeyFrame movement = new KeyFrame(Duration.millis(500), keyValueX, keyValueY);

        timeline.setOnFinished(e -> {
            userImageView.setImage(new Image(ResourceRepository.getResource("User-Idle")));
            animationCompleted = true;
        });

        timeline.getKeyFrames().addAll(movement, walking);

        timeline.play();
    }

    /**
     * Move an enemy by translating their current position
     * @param deltaX The X translation value
     * @param deltaY The Y translation value
     * @param enemy The enemy image view that will be changing
     */
    private void moveEnemy(int deltaX, int deltaY, EnemyViewModel enemy) {
        ImageView enemyImageView = enemy.getImageView();
        double x =
                (clampRange(enemyImageView.getX() + deltaX,
                        0, boardPane.getWidth() - enemyImageView.getFitWidth()));

        double y =
                (clampRange(enemyImageView.getY() + deltaY,
                        0, boardPane.getHeight() - enemyImageView.getFitHeight()));

        animateEnemy(enemyImageView, x, y);

        if (enemy.getEnemy().getCurrentCell().hasPlayerAndEnemy()) {
            triggerAlert("Unlucky! You have been killed by an enemy.", State.LEVEL_LOST);
        }
    }

    /**
     * Animates an enemies movement, so in which they move smoothly from cell to cell
     * @param enemyImageView The enemy image view, that needs animated
     * @param x The enemies new X position
     * @param y The enemies new Y position
     */
    private void animateEnemy(ImageView enemyImageView, double x, double y) {
        Timeline timeline = new Timeline();
        KeyValue keyValueX = new KeyValue(enemyImageView.xProperty(), x);
        KeyValue keyValueY = new KeyValue(enemyImageView.yProperty(), y);

        KeyFrame movement = new KeyFrame(Duration.millis(500), keyValueX, keyValueY);
        timeline.getKeyFrames().add(movement);

        timeline.play();
    }

    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
    }

    /**
     * Used to execute the movement phase of each enemy involved with
     * this level. Each enemy calculates the direction in which they want to
     * move in.
     */
    private void moveEnemies() {
        User user = userViewModel.getUser();
        for (EnemyViewModel evm : this.enemyViewModels) {
            Enemy enemy = evm.getEnemy();
            Direction nextDirection = enemy.getNextDirection(user, level);
            level.moveEnemy(enemy, nextDirection);

            switch (nextDirection) {
                case LEFT:
                    moveEnemy(-CELL_WIDTH, 0, evm);
                    break;
                case RIGHT:
                    moveEnemy(CELL_WIDTH, 0,evm);
                    break;
                case UP:
                    moveEnemy(0, -CELL_WIDTH, evm);
                    break;
                case DOWN:
                    moveEnemy(0, CELL_WIDTH, evm);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Used to process a key pressed, and execute specific code depending on what key
     * was pressed
     * @param code The keyboard KeyCode that was pressed
     */
    private void processKey(KeyCode code) {
        User user = userViewModel.getUser();
        try {
            if (!pressed && animationCompleted) {
                switch (code) {
                    case ESCAPE:
                        pauseGame();
                        break;
                    case LEFT:
                        level.movePlayer(user, Direction.LEFT);
                        movePlayer(-CELL_WIDTH, 0);
                        animationCompleted = false;
                        moveEnemies();
                        break;
                    case RIGHT:
                        level.movePlayer(user, Direction.RIGHT);
                        movePlayer(CELL_WIDTH, 0);
                        animationCompleted = false;
                        moveEnemies();
                        break;
                    case UP:
                        level.movePlayer(user, Direction.UP);
                        movePlayer(0, -CELL_WIDTH);
                        animationCompleted = false;
                        moveEnemies();
                        break;
                    case DOWN:
                        level.movePlayer(user, Direction.DOWN);
                        movePlayer(0, CELL_WIDTH);
                        animationCompleted = false;
                        moveEnemies();
                        break;
                    default:
                        break;
                }
            }
        } catch (InvalidMoveException ex) {
            LOGGER.log(WARNING, "The user has attempted an invalid move!", ex);
        }

    }

    /**
     * Called every time the user wishes to pause the game
     */
    private void pauseGame() {
        Instant currentPauseTime = Instant.now();
        boardPane.setEffect(new GaussianBlur());

        VBox pauseMenu = createPauseMenu();
        Button resume = new Button("Resume");
        Button saveAndQuit = new Button("Save and Quit");
        pauseMenu.getChildren().addAll(resume, saveAndQuit);
        pauseMenu.setAlignment(Pos.CENTER);

        Stage popupStage = new Stage(StageStyle.TRANSPARENT);
        popupStage.initOwner(primaryStage);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(pauseMenu, Color.TRANSPARENT));

        resume.setOnAction(e -> {
            boardPane.setEffect(null);
            updateTotalPauseTime(currentPauseTime);
            popupStage.hide();
        });

        saveAndQuit.setOnAction(e -> {
            popupStage.hide();
            LevelSaver.saveLevel(currentLevel, level, userViewModel.getUser(), calculateCurrentSavedTime());
            loadMainMenu(userViewModel.getUser());
        });

        popupStage.show();
    }

    /**
     * Calculate the current elapsed time used for saves
     * @return The calculated elapsed time up till now
     */
    private Long calculateCurrentSavedTime() {
        Instant elapsed = Instant.now();
        Long newFinishTime =
                java.time.Duration.between(startTime, elapsed).toMillis() - totalPausedTime + totalSavedTime;
        totalPausedTime = 0L;
        totalSavedTime = 0L;

        return newFinishTime;
    }

    /**
     * Replace a cell image at a specific position with a ground image
     * @param point The position of the image
     */
    public static void replaceCell(Point point) {
        ImageView imageView = new ImageView(new Image(ResourceRepository.getResource("Ground")));
        imageView.setY(point.getY() * 64);
        imageView.setX(point.getX() * 64);

        int idx = (int) ((int)(level.getBoardHeight() * point.getY()) + point.getX());
        cellImages.getChildren().set(idx, imageView);
    }

    /**
     * Creates the style's a features for the pause menu
     * @return The Vbox holding the pause menu
     */
    private VBox createPauseMenu() {
        VBox pauseMenu = new VBox(10);
        Label header = new Label("Game Paused");
        pauseMenu.getChildren().add(header);

        pauseMenu.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        pauseMenu.setAlignment(Pos.CENTER);
        pauseMenu.setPadding(new Insets(40));

        return pauseMenu;
    }

    /**
     * Updates the accumulated pause time
     * @param pauseStart The time start of the current pause
     */
    private void updateTotalPauseTime(Instant pauseStart) {
        totalPausedTime += java.time.Duration.between(pauseStart, Instant.now()).toMillis();
    }

    /**
     * Inject the required images into an Enemy View Model
     * @param enemy The enemy object
     * @return The enemies view model
     */
    private EnemyViewModel injectEnemyImages(Enemy enemy) {
        EnemyViewModel enemyViewModel = null;

        if (enemy instanceof SmartTargetingEnemy) {
            enemyViewModel = new EnemyViewModel(
                    enemy, new Image(ResourceRepository.getResource("SmartTargetingEnemy")));
        } else if (enemy instanceof DumbTargetingEnemy) {
            enemyViewModel = new EnemyViewModel(
                    enemy, new Image(ResourceRepository.getResource("DumbTargetingEnemy")));
        } else if (enemy instanceof StraightLineEnemy) {
            enemyViewModel = new EnemyViewModel(
                    enemy, new Image(ResourceRepository.getResource("StraightLineEnemy")));
        } else if (enemy instanceof WallFollowingEnemy) {
            enemyViewModel = new EnemyViewModel(
                    enemy, new Image(ResourceRepository.getResource("WallFollowingEnemy")));
        }

        return enemyViewModel;
    }


    /**
     * Trigger a message alert with also a change in state
     * @param message the message to be shown to the user
     * @param state The state that is being changed
     */
    public void triggerAlert(String message, State state) {
        backgroundMusicPlayer.stop();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        User user = userViewModel.getUser();
        if (state == State.LEVEL_LOST) {
            playSound("PlayerDeath");
            user.resetInventory(level.getCurrentLevel());
            alert.setHeaderText("LEVEL LOST");
        } else if (state == State.LEVEL_WON) {
            alert.setHeaderText("LEVEL WON");
            playSound("LevelWin");
            addNewFinishTime();
            user.setCurrentCell(null);
            if (user.getHighestLevel() == currentLevel) {
                if (currentLevel < 5) {
                    user.incrementLevel();
                }
            }
            user.resetInventory(currentLevel);
            UserRepository.save(user);
        }
        alert.setContentText(message);
        alert.showAndWait();
        loadMainMenu(user);
    }


    /**
     * Used to add a new level finish time to the user's score list.
     */
    private static void addNewFinishTime() {
        User user = userViewModel.getUser();
        Instant elapsed = Instant.now();
        Long newFinishTime =
                java.time.Duration.between(startTime, elapsed).toMillis() - totalPausedTime + totalSavedTime;
        totalPausedTime = 0L;
        totalSavedTime = 0L;
        try {
            user.addQuickestTime(newFinishTime, currentLevel);
        } catch (InvalidLevelException ex) {
            LOGGER.log(WARNING, "The user has completed a level that they should not have completed", ex);
        }
    }

    /**
     * Set the user for this game
     * @param user The user
     */
    public static void setUser(User user) {
        userViewModel = new UserViewModel(user);
    }

    /**
     * Set the width & height of this level
     * @param x The width of this level
     * @param y The height of this level
     */
    private void setBoardArea(int x, int y) {
        this.levelWidth = x;
        this.levelHeight = y;
    }


    public static void setBackgroundMusicPlayer(MediaPlayer backgroundMusicPlayer) {
        GameController.backgroundMusicPlayer = backgroundMusicPlayer;
    }

    public static void setTotalSavedTime(Long totalSavedTime) {
        GameController.totalSavedTime = totalSavedTime;
    }
}

