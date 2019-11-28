package com.group18.viewmodel;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A base class for all view models
 */
public abstract class ViewModel {

    /**
     * The cell width of all cells
     */
    private static final int CELL_WIDTH = 64;

    /**
     * The image associated with this View Model
     */
    private Image image;

    /**
     * The ImageView associated with this View Model
     */
    private ImageView imageView;

    /**
     * Creates a new view model
     * @param image The image associated with this view model
     */
    ViewModel(Image image) {
        this.image = image;
    }

    /**
     * Get the image associated with this View Model
     * @return The image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Set the image associated with this View Model
     * @param image The new image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Get the Image View associated with this View Model
     * @return The image view
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Set the Image View for this model
     * @param height The height of the image view
     * @param width The width of the image view
     */
    public void setImageView(int height, int width) {
        this.imageView = new ImageView(this.image);
        this.imageView.setFitHeight(CELL_WIDTH);
        this.imageView.setFitWidth(CELL_WIDTH);
        this.imageView.setX(width * CELL_WIDTH);
        this.imageView.setY(height * CELL_WIDTH);
    }
}
