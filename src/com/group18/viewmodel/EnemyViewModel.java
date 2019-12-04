package com.group18.viewmodel;

import com.group18.model.entity.Enemy;
import javafx.scene.image.Image;

/**
 * A view model linking front-end assets to a specific enemy object
 *
 * @author danielturato
 */
public class EnemyViewModel extends ViewModel {

    /**
     * The enemy associated with this View Model
     */
    private Enemy enemy;

    /**
     * Creates a new Enemy View Model
     * @param enemy The enemy associated with this View Model
     * @param image The image associated with this View Model
     */
    public EnemyViewModel(Enemy enemy, Image image) {
        super(image);
        this.enemy = enemy;
    }

    /**
     * Get the enemy associated with this Enemy
     * @return The enemy
     */
    public Enemy getEnemy() {
        return enemy;
    }

    /**
     * Set the enemy for this View Model
     * @param enemy The new enemy
     */
    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }
}
