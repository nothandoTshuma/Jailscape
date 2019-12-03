package com.group18.controller;


import com.group18.core.LevelLoader;
import com.group18.core.ResourceRepository;
import com.group18.exception.InvalidLevelException;
import com.group18.exception.InvalidMoveException;
import com.group18.model.Direction;
import com.group18.model.Level;
import com.group18.model.State;
import com.group18.model.cell.Cell;
import com.group18.model.entity.*;
import com.group18.viewmodel.EnemyViewModel;
import com.group18.viewmodel.UserViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

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
public class GameController extends Application {

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
     * Holds the current state of the level
     */
    private State currentState;

    /**
     * The level object associated with this level
     */
    private Level level;

    /**
     * The user view model associated with this level
     */
    private UserViewModel userViewModel;

    /**
     * The enemy view models associated with this level
     */
    private List<EnemyViewModel> enemyViewModels = new ArrayList<>();

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
     * The board pane for this controller
     */
    private Pane boardPane;

    /**
     * The primary stage for this controller
     */
    private Stage primaryStage;

    /**
     * The initial epoch start time for this level
     */
    private Instant startTime;

    /**
     * The total elapsed pause time for this level
     */
    private Long totalPausedTime = 0L;

    /**
     * The current level number for this controller
     */
    private int currentLevel;

    /**
     * The alert label for this Controller
     */
    private Label alertLabel;

    /**
     * The alert service for this controller
     */
    private Service alertService = new AlertService();

    /**
     * An alert service which will wait 3 seconds.
     * Used to display alerts to the user for a short amount of time.
     */
    class AlertService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(3000);
                    return null;
                }
            };
        }
    }

    /**
     * The start method, that will initialise the whole game in which the user
     * will be able to comfortably be able to play
     * @param primaryStage The primary stage of this Game
     * @throws Exception This method can throw many possible exceptions
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO:drt - This needs to be somewhere else.
        ResourceRepository.createResourceMap();
        loadLevel();
        createBoard();
        createAlertLabel();

        Scene scene = new Scene(new BorderPane(this.boardPane),500,500);
        restrictView(scene);

        this.currentState = State.IN_PROGRESS;
        scene.setOnKeyPressed(e -> processKey(e.getCode()));
        scene.setOnKeyReleased(e -> pressed = false);
        this.primaryStage = primaryStage;

        primaryStage.setScene(scene);
        primaryStage.show();
        startTime = Instant.now();
    }

    /**
     * Creates an alert label, that allows the game to alert the user
     * in changes they need knowledge of
     */
    private void createAlertLabel() {
        Label alert = new Label();
        alert.setStyle("-fx-background-color: white");
        alert.setVisible(false);
        this.alertLabel = alert;

        alertService.setOnSucceeded(e -> {
            alert.setVisible(false);
            alertService.reset();
        });

        this.boardPane.getChildren().addAll(alert);
    }

    /**
     * Restrict the view, so the user can only view a specific section of the game
     * @param scene The scene that needs restricting
     */
    private void restrictView(Scene scene) {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(scene.widthProperty());
        clip.heightProperty().bind(scene.heightProperty());

        ImageView userImageView = this.userViewModel.getImageView();
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
        this.boardPane = pane;
        drawEntities(pane, level);
    }

    /**
     * Load a level from a specific level file
     */
    private void loadLevel() {
        //TODO:drt - Load user!
        User user = new User("Daniel");
        //TODO:drt - Set level!
        currentLevel = 1;
        Level level = LevelLoader.loadLevel(1, user);
        this.level = level;
    }

    /**
     * Draw each entity onto the level
     * @param pane The pane the entities will be drawn on
     * @param level The level object that these entities are on
     */
    private void drawEntities(Pane pane, Level level) {
        Cell[][] cells = level.getBoard();

        Group sprites = new Group();
        for (int i = 0; i < levelHeight; i++) {
            for (int j = 0; j < levelWidth; j++) {
                Cell cell = cells[j][i];
                if (cell.getCurrentEntities().size() > 0) {
                    Entity entity = cell.getCurrentEntities().get(0);
                    if (entity instanceof User) {
                        //TODO:drt - Don't create new user view model after implementing user loading
                        this.userViewModel = new UserViewModel((User) entity);
                        this.userViewModel.setImageView(j, i);
                        sprites.getChildren().add(userViewModel.getImageView());
                    } else {
                        EnemyViewModel enemyViewModel = createEnemyViewModel((Enemy) entity, j, i);
                        enemyViewModels.add(enemyViewModel);
                        sprites.getChildren().add(enemyViewModel.getImageView());
                    }
                }
            }
        }

        pane.getChildren().add(sprites);
    }

    /**
     * Creates a new EnemyViewModel based on an Enemy object
     * @param enemy The enemy object
     * @param y It's Y position
     * @param x It's X position
     * @return The EnemyViewModel
     */
    private EnemyViewModel createEnemyViewModel(Enemy enemy, int y, int x) {
        EnemyViewModel enemyViewModel = injectImages(enemy);
        enemyViewModel.setImageView(y, x);

        return enemyViewModel;
    }

    /**
     * Draw each cell into a new pane
     * @param level The level object associated with this level
     * @return A pane containing all the cell images associated with this level
     */
    public Pane drawCells(Level level) {
        Pane board = new Pane();
        Cell[][] cells = level.getBoard();
        int boardWidth = level.getBoardWidth();
        int boardHeight = level.getBoardHeight();
        setBoardArea(boardWidth, boardHeight);

        Group cellGroups = new Group();
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                Cell cell = cells[i][j];
                ImageView imageView = createSpriteImageView(i, j, cell);
                cellGroups.getChildren().add(imageView);
            }
        }

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
        ImageView imageView = new ImageView(cell.getSpriteImage());
        imageView.setFitHeight(64);
        imageView.setFitWidth(64);
        imageView.setX(j * 64);
        imageView.setY(i * 64);
        return imageView;
    }

    /**
     * Move the player in the game
     * @param deltaX The user's X translation value
     * @param deltaY The user's Y translation value
     */
    private void movePlayer(int deltaX, int deltaY) {
        if (!pressed) {
            ImageView userImageView = this.userViewModel.getImageView();

            pressed = true;
            double x = (clampRange(userImageView.getX() + deltaX,
                            0, boardPane.getWidth() - userImageView.getFitWidth()));

            double y =
                    (clampRange(userImageView.getY() + deltaY,
                            0, boardPane.getHeight() - userImageView.getFitHeight()));


            animateUser(userImageView, x, y);

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
        });

        timeline.getKeyFrames().addAll(movement, walking);

        timeline.play();
    }

    /**
     * Move an enemy by translating their current position
     * @param deltaX The X translation value
     * @param deltaY The Y translation value
     * @param enemyImageView The enemy image view that will be changing
     */
    private void moveEnemy(int deltaX, int deltaY, ImageView enemyImageView) {
        double x =
                (clampRange(enemyImageView.getX() + deltaX,
                        0, boardPane.getWidth() - enemyImageView.getFitWidth()));

        double y =
                (clampRange(enemyImageView.getY() + deltaY,
                        0, boardPane.getHeight() - enemyImageView.getFitHeight()));

        animateEnemy(enemyImageView, x, y);
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
        User user = this.userViewModel.getUser();
        for (EnemyViewModel evm : this.enemyViewModels) {
            Enemy enemy = evm.getEnemy();
            Direction nextDirection = enemy.getNextDirection(user, level);
            this.level.moveEnemy(enemy, nextDirection);

            switch (nextDirection) {
                case LEFT:
                    moveEnemy(-CELL_WIDTH, 0, evm.getImageView());
                    break;
                case RIGHT:
                    moveEnemy(CELL_WIDTH, 0, evm.getImageView());
                    break;
                case UP:
                    moveEnemy(0, -CELL_WIDTH, evm.getImageView());
                    break;
                case DOWN:
                    moveEnemy(0, CELL_WIDTH, evm.getImageView());
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
        User user = this.userViewModel.getUser();
        alertLabel.setVisible(false);
        try {
            switch (code) {
                case ESCAPE:
                    pauseGame();
                    break;
                case LEFT:
                    this.level.movePlayer(user, Direction.LEFT);
                    movePlayer(-CELL_WIDTH, 0);
                    moveEnemies();
                    break ;
                case RIGHT:
                    this.level.movePlayer(user, Direction.RIGHT);
                    movePlayer(CELL_WIDTH, 0);
                    moveEnemies();
                    break ;
                case UP:
                    this.level.movePlayer(user, Direction.UP);
                    movePlayer(0, -CELL_WIDTH);
                    moveEnemies();
                    break ;
                case DOWN:
                    this.level.movePlayer(user, Direction.DOWN);
                    movePlayer(0, CELL_WIDTH);
                    moveEnemies();
                    break ;
                default:
                    break ;
            }
        } catch (InvalidMoveException ex) {
            LOGGER.log(WARNING, "The user has attempted an invalid move!", ex);
            alertLabel.setText("You cannot move on to that cell!");
            alertLabel.setVisible(true);
        }

    }

    /**
     * Called every time the user wishes to pause the game
     */
    private void pauseGame() {
        Instant currentPauseTime = Instant.now();
        this.boardPane.setEffect(new GaussianBlur());

        VBox pauseMenu = createPauseMenu();
        Button resume = new Button("Resume");
        Button saveAndQuit = new Button("Save and Quit");
        pauseMenu.getChildren().addAll(resume, saveAndQuit);

        Stage popupStage = new Stage(StageStyle.TRANSPARENT);
        popupStage.initOwner(primaryStage);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(pauseMenu, Color.TRANSPARENT));

        resume.setOnAction(event -> {
            this.boardPane.setEffect(null);
            updateTotalPauseTime(currentPauseTime);
            popupStage.hide();
        });

        this.currentState = State.PAUSED;
        popupStage.show();
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
    private EnemyViewModel injectImages(Enemy enemy) {
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
     * Trigger a message alert for the user to see in the game
     * @param message The message to be shown
     */
    public static void triggerAlert(String message) {
        //TODO:drt - The game needs to be in a border boardPane with Hidden alert boardPane at the top
    }

    /**
     * Trigger a message alert with also a change in state
     * @param message the message to be shown to the user
     * @param state The state that is being changed
     */
    public void triggerAlert(String message, State state) {
        //TODO:drt - Game lost or game won so we show actual alert and the game ends.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.showAndWait();
        addNewFinishTime();
        Platform.exit();
    }

    /**
     * Used to add a new level finish time to the user's score list.
     */
    private void addNewFinishTime() {
        User user = userViewModel.getUser();
        Instant elapsed = Instant.now();
        Long newFinishTime =
                java.time.Duration.between(startTime, elapsed).toMillis() - totalPausedTime;

        try {
            user.addQuickestTime(newFinishTime, currentLevel);
        } catch (InvalidLevelException ex) {
            LOGGER.log(WARNING, "The user has completed a level that they shouldn't of completed", ex);
        }
    }

    /**
     * Set the width & height of this level
     * @param x The width of this level
     * @param y The height of this level
     */
    public void setBoardArea(int x, int y) {
        this.levelWidth = x;
        this.levelHeight = y;
    }

    //TODO:drt - Delete this method once fully tested
    public static void main(String[] args) {
        launch(args);
    }
}

