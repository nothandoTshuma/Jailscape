package com.group18.controller;


import com.group18.core.LevelLoader;
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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class GameController extends Application {

    private static final int CELL_WIDTH = 64 ; // pixels / second

    private State currentState;

    private Level level;

    private UserViewModel userViewModel;

    private List<EnemyViewModel> enemyViewModels = new ArrayList<>();

    private int levelWidth;

    private int levelHeight;

    private boolean pressed = false;

    private Pane boardPane;


    @Override
    public void start(Stage primaryStage) throws Exception {
        loadLevel();
        createBoard();

        Scene scene = new Scene(new BorderPane(this.boardPane),500,500);
        restrictView(scene);

        scene.setOnKeyPressed(e -> processKey(e.getCode()));
        scene.setOnKeyReleased(e -> pressed = false);

        primaryStage.setScene(scene);
        primaryStage.show();


    }

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

    private void createBoard() {
        Pane pane = drawCells(level);
        this.boardPane = pane;
        drawEntities(pane, level);
    }

    private void loadLevel() {
        //TODO:drt - Load user!
        User user = new User("Daniel");
        Level level = LevelLoader.loadLevel(1, user);
        this.level = level;
    }

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

    private EnemyViewModel createEnemyViewModel(Enemy enemy, int y, int x) {
        EnemyViewModel enemyViewModel = injectImages(enemy);
        enemyViewModel.setImageView(y, x);

        return enemyViewModel;
    }

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

    private void setBoardLimits(Pane board) {
        board.setMinSize(levelWidth * 64, levelHeight * 64);
        board.setPrefSize(levelWidth * 64, levelHeight * 64);
        board.setMaxSize(levelWidth * 64, levelHeight * 64);
    }

    private ImageView createSpriteImageView(int i, int j, Cell cell) {
        ImageView imageView = new ImageView(cell.getSpriteImage());
        imageView.setFitHeight(64);
        imageView.setFitWidth(64);
        imageView.setX(j * 64);
        imageView.setY(i * 64);
        return imageView;
    }

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

    private void animateUser(ImageView userImageView, double x, double y) {
        Timeline timeline = new Timeline();
        KeyValue keyValueX = new KeyValue(userImageView.xProperty(), x);
        KeyValue keyValueY = new KeyValue(userImageView.yProperty(), y);

        KeyFrame walking = new KeyFrame(Duration.millis(100), e -> {
           userImageView.setImage(new Image("resources/assets/Player/Walk/PlayerWalk.gif"));
        });

        KeyFrame movement = new KeyFrame(Duration.millis(500), keyValueX, keyValueY);

        timeline.setOnFinished(e -> {
            userImageView.setImage(new Image("resources/assets/Player/Idle/PlayerIdle.gif"));
        });

        timeline.getKeyFrames().addAll(movement, walking);

        timeline.play();
    }

    private void moveEnemy(int deltaX, int deltaY, ImageView enemyImageView) {
        double x =
                (clampRange(enemyImageView.getX() + deltaX,
                        0, boardPane.getWidth() - enemyImageView.getFitWidth()));

        double y =
                (clampRange(enemyImageView.getY() + deltaY,
                        0, boardPane.getHeight() - enemyImageView.getFitHeight()));

        animateEnemy(enemyImageView, x, y);
    }

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

    private void processKey(KeyCode code) {
        User user = this.userViewModel.getUser();
        switch (code) {
            case LEFT:
                try {
                    this.level.movePlayer(user, Direction.LEFT);
                    movePlayer(-CELL_WIDTH, 0);
                } catch (InvalidMoveException ex) {
                    //TODO - handle they cant move on cell
                    System.out.println("this is a wall");
                }
                break ;
            case RIGHT:
                try {
                    this.level.movePlayer(user, Direction.RIGHT);
                    movePlayer(CELL_WIDTH, 0);
                } catch (InvalidMoveException ex) {
                    //TODO - handle they cant move on cell
                    System.out.println("this is a wall");
                }
                break ;
            case UP:
                try {
                    this.level.movePlayer(user, Direction.UP);
                    movePlayer(0, -CELL_WIDTH);
                } catch (InvalidMoveException ex) {
                    //TODO - handle they cant move on cell
                    System.out.println("this is a wall");
                }
                break ;
            case DOWN:
                try {
                    this.level.movePlayer(user, Direction.DOWN);
                    movePlayer(0, CELL_WIDTH);
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

    private EnemyViewModel injectImages(Enemy enemy) {
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
        //TODO:drt - The game needs to be in a border boardPane with Hidden alert boardPane at the top
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
        alert.showAndWait();
        Platform.exit();
    }

    public void setBoardArea(int x, int y) {
        this.levelWidth = x;
        this.levelHeight = y;
    }


    public static void main(String[] args) {
        launch(args);
    }
}

