package com.group18.controller;


import com.group18.core.CreateLevelLayout;
import com.group18.core.FileReader;
import com.group18.core.LevelLoader;
import com.group18.exception.InvalidMoveException;
import com.group18.model.Direction;
import com.group18.model.Level;
import com.group18.model.State;
import com.group18.model.cell.Cell;
import com.group18.model.entity.Entity;
import com.group18.model.entity.User;
import com.group18.service.MessageOfTheDayService;
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

public class GameController extends Application {

    private static State currentState;
    private Level level;
    private User user;


    private static int playerx;
    private static int playery;
    private ImageView player;
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
        levelHeight = x;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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

        clip.xProperty().bind(Bindings.createDoubleBinding(
                () -> clampRange(player.getX() - scene.getWidth() / 2, 0, pane.getWidth() - scene.getWidth()),
                player.xProperty(), scene.widthProperty()));
        clip.yProperty().bind(Bindings.createDoubleBinding(
                () -> clampRange(player.getY() - scene.getHeight() / 2, 0, pane.getHeight() - scene.getHeight()),
                player.yProperty(), scene.heightProperty()));

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
                    ImageView spriteImage = new ImageView(entity.getSpriteImage());
                    spriteImage.setFitHeight(64);
                    spriteImage.setFitWidth(64);
                    spriteImage.setX(j * 64);
                    spriteImage.setY(i * 64);
                    sprites.getChildren().add(spriteImage);
                    if (entity instanceof User) {
                        this.user = (User) entity;
                        player = spriteImage;
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
            pressed = true;
            player.setX(clampRange(player.getX() + deltaX, 0, pane.getWidth() - player.getFitWidth()));
            player.setY(clampRange(player.getY() + deltaY, 0, pane.getHeight() - player.getFitHeight()));
        }
    }

    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
    }

    private void processKey(KeyCode code) {
        switch (code) {
            case LEFT:
                try {
                    this.level.movePlayer(this.user, Direction.LEFT);
                    movePlayer(-speed, 0);
                } catch (InvalidMoveException ex) {
                    //TODO - handle they cant move on cell
                    System.out.println("this is a wall");
                }
                break ;
            case RIGHT:
                try {
                    this.level.movePlayer(this.user, Direction.RIGHT);
                    movePlayer(speed, 0);
                } catch (InvalidMoveException ex) {
                    //TODO - handle they cant move on cell
                    System.out.println("this is a wall");
                }
                break ;
            case UP:
                try {
                    this.level.movePlayer(this.user, Direction.UP);
                    movePlayer(0, -speed);
                } catch (InvalidMoveException ex) {
                    //TODO - handle they cant move on cell
                    System.out.println("this is a wall");
                }
                break ;
            case DOWN:
                try {
                    this.level.movePlayer(this.user, Direction.DOWN);
                    movePlayer(0, speed);
                } catch (InvalidMoveException ex) {
                    //TODO - handle they cant move on cell
                    System.out.println("this is a wall");
                }
                break ;
            default:
                break ;
        }
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
    }



    public static void main(String[] args) {
        launch(args);
    }
}

