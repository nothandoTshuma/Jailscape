package com.group18.controller;


import com.group18.core.CreateLevelLayout;
import com.group18.core.FileReader;
import com.group18.core.LevelLoader;
import com.group18.exception.InvalidMoveException;
import com.group18.model.Direction;
import com.group18.model.Level;
import com.group18.model.State;
import com.group18.model.cell.Cell;
import com.group18.model.entity.*;
import com.group18.service.MessageOfTheDayService;
import com.group18.viewmodel.EnemyViewModel;
import com.group18.viewmodel.UserViewModel;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameController extends Application {

    private State currentState;

    private Level level;

    private UserViewModel userViewModel;

    private List<EnemyViewModel> enemyViewModels = new ArrayList<>();

    private static int playerx;
    private static int playery;
    private static int levelWidth;
    private static int levelHeight;

    private final int speed = 64 ; // pixels / second
    private boolean up ;
    private boolean down ;
    private boolean left ;
    private boolean right ;
    private boolean pressed = false;
    private Pane pane;

    public static void setPlayerPos(int x, int y) {
        playerx = x;
        playery = y;
    }

    public static void setLevelSize(int x, int y) {
        levelWidth = x;
        levelHeight = y;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO:drt - Dynamically load user from selected options.
        User user = new User("Daniel");
        Level level = LevelLoader.loadLevel(1, user);
        this.level = level;

        Pane pane = drawCells(level);
        this.pane = pane;
        drawEntities(pane, level);

        Scene scene = new Scene(new BorderPane(pane),500,500);
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(scene.widthProperty());
        clip.heightProperty().bind(scene.heightProperty());

        ImageView userImageView = this.userViewModel.getImageView();
        clip.xProperty().bind(Bindings.createDoubleBinding(
                () -> clampRange(userImageView.getX() - scene.getWidth() / 2, 0, pane.getWidth() - scene.getWidth()),
                userImageView.xProperty(), scene.widthProperty()));
        clip.yProperty().bind(Bindings.createDoubleBinding(
                () -> clampRange(userImageView.getY() - scene.getHeight() / 2, 0, pane.getHeight() - scene.getHeight()),
                userImageView.yProperty(), scene.heightProperty()));

        pane.setClip(clip);
        pane.translateXProperty().bind(clip.xProperty().multiply(-1));
        pane.translateYProperty().bind(clip.yProperty().multiply(-1));

        scene.setOnKeyPressed(e -> processKey(e.getCode()));
        scene.setOnKeyReleased(e -> pressed = false);

        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private void drawEntities(Pane pane, Level level) {
        Cell[][] cells = level.getBoard();

        Group sprites = new Group();
        for (int i = 0; i < levelHeight; i++) {
            for (int j = 0; j < levelWidth; j++) {
                Cell cell = cells[j][i];
                if (cell.getCurrentEntities().size() > 0) {
                    Entity entity = cell.getCurrentEntities().get(0);
                    //TODO:drt - View model for enemies
                    if (entity instanceof User) {
                        //TODO:drt - Don't create new user view model after implementing user loading
                        this.userViewModel = new UserViewModel((User) entity);
                        this.userViewModel.setImageView(j, i);
                        sprites.getChildren().add(userViewModel.getImageView());
                    } else {
                        EnemyViewModel enemyViewModel = createEnemyViewModel((Enemy) entity);
                        enemyViewModel.setImageView(j, i);
                        enemyViewModels.add(enemyViewModel);
                        sprites.getChildren().add(enemyViewModel.getImageView());
                    }
                }
            }
        }

        pane.getChildren().add(sprites);
    }

    public Pane drawCells(Level level) {
        Pane board = new Pane();
        Cell[][] cells = level.getBoard();
        int boardWidth = level.getBoardWidth();
        int boardHeight = level.getBoardHeight();
        setLevelSize(boardWidth, boardHeight);

        Group cellGroups = new Group();
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                Cell cell = cells[i][j];
                if (cell.hasPlayer()) {
                    setPlayerPos(j, i);
                }
                ImageView imageView = new ImageView(cell.getSpriteImage());
                imageView.setFitHeight(64);
                imageView.setFitWidth(64);
                imageView.setX(j * 64);
                imageView.setY(i * 64);
                cellGroups.getChildren().add(imageView);
            }
        }

        board.getChildren().add(cellGroups);

        board.setMinSize(levelWidth * 64, levelHeight * 64);
        board.setPrefSize(levelWidth * 64, levelHeight * 64);
        board.setMaxSize(levelWidth * 64, levelHeight * 64);

        return board;
    }

    private void movePlayer(int deltaX, int deltaY) {
        if (!pressed) {
            ImageView userImageView = this.userViewModel.getImageView();
            pressed = true;
            userImageView.setX
                    (clampRange(userImageView.getX() + deltaX,
                            0, pane.getWidth() - userImageView.getFitWidth()));

            userImageView.setY
                    (clampRange(userImageView.getY() + deltaY,
                            0, pane.getHeight() - userImageView.getFitHeight()));
        }
    }

    private void moveEnemy(int deltaX, int deltaY, ImageView enemyImageView) {
        enemyImageView.setX
                (clampRange(enemyImageView.getX() + deltaX,
                        0, pane.getWidth() - enemyImageView.getFitWidth()));

        enemyImageView.setY
                (clampRange(enemyImageView.getY() + deltaY,
                        0, pane.getHeight() - enemyImageView.getFitHeight()));
    }

    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
    }

    private void moveEnemies() {
        User user = this.userViewModel.getUser();
        for (EnemyViewModel evm : this.enemyViewModels) {
            Enemy enemy = evm.getEnemy();
            Direction nextDirection = enemy.getNextDirection(user, level);
            this.level.moveEnemy(enemy, nextDirection);

            switch (nextDirection) {
                case LEFT:
                    moveEnemy(-speed, 0, evm.getImageView());
                    break;
                case RIGHT:
                    moveEnemy(speed, 0, evm.getImageView());
                    break;
                case UP:
                    moveEnemy(0, -speed, evm.getImageView());
                    break;
                case DOWN:
                    moveEnemy(0, speed, evm.getImageView());
                    break;
                default:
                    break;
            }
        }
    }

    private void processKey(KeyCode code) {
        User user = this.userViewModel.getUser();
        switch (code) {
            case LEFT:
                try {
                    this.level.movePlayer(user, Direction.LEFT);
                    movePlayer(-speed, 0);
                } catch (InvalidMoveException ex) {
                    //TODO - handle they cant move on cell
                    System.out.println("this is a wall");
                }
                break ;
            case RIGHT:
                try {
                    this.level.movePlayer(user, Direction.RIGHT);
                    movePlayer(speed, 0);
                } catch (InvalidMoveException ex) {
                    //TODO - handle they cant move on cell
                    System.out.println("this is a wall");
                }
                break ;
            case UP:
                try {
                    this.level.movePlayer(user, Direction.UP);
                    movePlayer(0, -speed);
                } catch (InvalidMoveException ex) {
                    //TODO - handle they cant move on cell
                    System.out.println("this is a wall");
                }
                break ;
            case DOWN:
                try {
                    this.level.movePlayer(user, Direction.DOWN);
                    movePlayer(0, speed);
                } catch (InvalidMoveException ex) {
                    //TODO - handle they cant move on cell
                    System.out.println("this is a wall");
                }
                break ;
            default:
                break ;
        }

        moveEnemies();

    }

    private EnemyViewModel createEnemyViewModel(Enemy enemy) {
        EnemyViewModel enemyViewModel = null;

        if (enemy instanceof SmartTargetingEnemy) {
            enemyViewModel = new EnemyViewModel(
                    enemy, new Image("resources/assets/enemy/SmartEnemyIdle.png"));
        } else if (enemy instanceof DumbTargetingEnemy) {
            enemyViewModel = new EnemyViewModel(
                    enemy, new Image("resources/assets/enemy/DumbEnemyIdle.png"));
        } else if (enemy instanceof StraightLineEnemy) {
            enemyViewModel = new EnemyViewModel(
                    enemy, new Image("resources/assets/enemy/StraightEnemyIdle.png"));
        } else if (enemy instanceof WallFollowingEnemy) {
            enemyViewModel = new EnemyViewModel(
                    enemy, new Image("resources/assets/enemy/WallEnemyIdle.png"));
        }

        return enemyViewModel;
    }

    /**
     * Trigger a message alert for the user to see in the game
     * @param message The message to be shown
     */
    public static void triggerAlert(String message) {
        //TODO:drt - The game needs to be in a border pane with Hidden alert pane at the top
    }

    /**
     * Trigger a message alert with also a change in state
     * @param message the message to be shown to the user
     * @param state The state that is being changed
     */
    public static void triggerAlert(String message, State state) {
        //TODO:drt - Game lost or game won so we show actual alert and the game ends.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}

